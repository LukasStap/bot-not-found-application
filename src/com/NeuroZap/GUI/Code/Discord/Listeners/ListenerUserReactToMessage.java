package com.NeuroZap.GUI.Code.Discord.Listeners;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;

import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageAttachment;
import de.btobastian.javacord.entities.message.embed.Embed;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.message.emoji.Emoji;
import de.btobastian.javacord.events.message.reaction.ReactionAddEvent;
import de.btobastian.javacord.events.message.reaction.ReactionRemoveEvent;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;

public class ListenerUserReactToMessage implements ReactionAddListener, ReactionRemoveListener {

	private static List<String> reactableChannels = new ArrayList<String>();
	private static TextChannel starChannel = null;
	private final int emojiCount = 10;
	
	public static void registerChannels()
	{
		if (starChannel == null && MainDiscordBot.api().getTextChannelById("532707795854819343").isPresent())
		{
			starChannel = MainDiscordBot.api().getTextChannelById("532707795854819343").get();
			
			reactableChannels.add("541030030709489664"); // general
			reactableChannels.add("564903746883026981"); // memes-n-shitposts 
			reactableChannels.add("564904257661435926"); // this-spam-chat
			reactableChannels.add("510286046173593601"); // people-pets-postitivity
			reactableChannels.add("524010104819941416"); // food
			reactableChannels.add("510626903766335530"); // bot-cmds
			reactableChannels.add("551823840674709504"); // patron-txt
			reactableChannels.add("503537856162037760"); // mute-chat
			reactableChannels.add("550436470649782285"); // uni-art-comissions
			reactableChannels.add("551342065519296532"); // nova-art-comissions
			reactableChannels.add("503319954876137474"); // lyrics
			reactableChannels.add("547331903020335105"); // production
			reactableChannels.add("409407549381804047"); // art
			reactableChannels.add("408603886346305557"); // fan-art
			reactableChannels.add("532972660565475328"); // merch-you-like
			reactableChannels.add("528926740089667594"); // merch-posts
			reactableChannels.add("504278428246081548"); // promo
			reactableChannels.add("528914200030871552"); // questions
			reactableChannels.add("522777618429509642"); // complaints
			reactableChannels.add("537013344234897428"); // suggestions-vote
			//reactableChannels.add("520246221646856212"); // bot test
		}
	}

	@Override
	public void onReactionAdd(ReactionAddEvent ev) 
	{
		try
		{
			Message msg = MainDiscordBot.api().getMessageById(ev.getMessageId(), ev.getChannel()).get();
			if (reactableChannels.contains(ev.getChannel().getIdAsString()) && ev.getEmoji().isUnicodeEmoji() && ev.getEmoji().getMentionTag().toCharArray()[0] == 11088 && 
					msg.getReactionByEmoji(ev.getEmoji()).get().getCount() >= emojiCount && msg.getAttachments().isEmpty())
			{
				
				try 
				{
					for (Message m : starChannel.getHistory(200).get().getMessages())
					{
						String desc = null;
						
						try
						{
							desc = m.getEmbeds().get(0).getFooter().get().getText().get();
							if (desc.equals(ev.getMessageId() + ""))
							{
								EmbedBuilder eb = getFormattedDescription(msg, ev.getEmoji(), m.getEmbeds().get(0), m);
								if (eb != null)
									m.edit(eb);
								return;
							}
						}
						catch (Exception ex)
						{
							continue;
						}

					}
				} catch (Exception ex)
				{
					
				}
				EmbedBuilder eb = getFormattedDescription(msg, ev.getEmoji(), null, null);
				if (eb != null)
					starChannel.sendMessage(eb);
				try
				{
					if (starChannel.getHistory(201).get().getMessages().size() >= 201)
					{
						starChannel.getHistory(201).get().getMessages().get(200).delete();
					}
				} catch (Exception e) {

				}
			}
		}
		catch (Exception ex)
		{
			MainDiscordBot.getNeuro().sendMessage(ex.getMessage());
			return;
		}
	}
	
	@Override
	public void onReactionRemove(ReactionRemoveEvent ev) 
	{
		Message msg = null;
		try {
			msg = MainDiscordBot.api().getMessageById(ev.getMessageId(), ev.getChannel()).get();
		} catch (InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}
		if (reactableChannels.contains(ev.getChannel().getIdAsString()) && ev.getEmoji().isUnicodeEmoji() && ev.getEmoji().getMentionTag().toCharArray()[0] == 11088  && msg.getAttachments().isEmpty())
		{
			try {
				for (Message m : starChannel.getHistory(200).get().getMessages())
				{
					String desc = null;
					
					try
					{
						desc = m.getEmbeds().get(0).getFooter().get().getText().get();
						if (desc.equals(ev.getMessageId() + ""))
						{
							EmbedBuilder eb = getFormattedDescription(msg, ev.getEmoji(), m.getEmbeds().get(0), m);
							if (eb != null)
								m.edit(eb);
							return;
						}
					}
					catch (Exception ex)
					{
						continue;
					}
				}
			} catch (Exception e) {
				MainDiscordBot.getNeuro().sendMessage(e.getMessage());
			}
		}
	}
	
	private EmbedBuilder getFormattedDescription(Message m, Emoji starEmoji, Embed original, Message originalM)
	{
		try
		{
			EmbedBuilder eb = new EmbedBuilder();
			eb.setFooter(m.getId() + "");
			
			if (original == null)
				eb.setTimestamp();
			else
				eb.setTimestamp(original.getTimestamp().get());

			String desc = "Channel: (#" + m.getChannel().toString().replace("ServerTextChannel (id: " + m.getChannel().getId() + ", name: ", "") + '\n';
			try
			{
				desc += "Stars: **" + m.getReactionByEmoji(starEmoji).get().getCount() + "** :star:" + '\n' + '\n';
			} catch (Exception ex)
			{
				originalM.delete();
				return null;
			}
			
			desc += "Quote: \"**" + m.getContent().replace("*", "") + "**\"";
			
			eb.setDescription(desc);
			eb.setAuthor(m.getAuthor().getDiscriminatedName(), "", m.getAuthor().getAvatar().getUrl().toString());
			eb.setColor(Color.RED);
			
			for (MessageAttachment ma : originalM.getAttachments())
			{
				if (ma.isImage())
				{
					// yeet
				}
			}
			return eb;
		} catch (Exception ex)
		{
			MainDiscordBot.getNeuro().sendMessage(ex.getMessage());
		}
		return null;
	}

}

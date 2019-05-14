package com.NeuroZap.GUI.Code.Applying;

import java.util.concurrent.ExecutionException;

import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;

import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.events.message.MessageCreateEvent;
import de.btobastian.javacord.listeners.message.MessageCreateListener;

public class ListenerKeepAppsSticky implements MessageCreateListener {
	
	@Override
	public void onMessageCreate(MessageCreateEvent ev) 
	{
		if (ev.getMessage().getAuthor().asUser().get() != MainDiscordBot.api().getYourself() && 
				ev.getChannel() == MainDiscordBot.getHQAppManagementChannel() && ReviewMechanics.currentReviewMessage_1 != null)
		{
			try {
				EmbedBuilder eb1 = ReviewMechanics.currentlyReviewing.getFormattedApplications(ReviewMechanics.revealed).get(0);
				EmbedBuilder eb2 = ReviewMechanics.currentlyReviewing.getFormattedApplications(ReviewMechanics.revealed).get(1);
				ReviewMechanics.currentReviewMessage_2.get().delete();
				ReviewMechanics.currentReviewMessage_1.get().delete();
				ReviewMechanics.currentReviewMessage_1 = ev.getChannel().sendMessage(eb1);
				ReviewMechanics.currentReviewMessage_2 = ev.getChannel().sendMessage(eb2);
			} catch (InterruptedException | ExecutionException e) {
				MainDiscordBot.getHQAppManagementChannel().sendMessage("Could not repost the sticky message: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

}

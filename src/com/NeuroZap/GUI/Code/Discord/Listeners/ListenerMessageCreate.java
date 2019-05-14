package com.NeuroZap.GUI.Code.Discord.Listeners;

import com.NeuroZap.GUI.Code.Applying.Applicant;
import com.NeuroZap.GUI.Code.Applying.Applicants;

import de.btobastian.javacord.entities.channels.PrivateChannel;
import de.btobastian.javacord.events.message.MessageCreateEvent;
import de.btobastian.javacord.listeners.message.MessageCreateListener;

public class ListenerMessageCreate implements MessageCreateListener {

	public static int gained = 0;
	
	@Override
	public void onMessageCreate(MessageCreateEvent ev) 
	{
		if (ev.getChannel() instanceof PrivateChannel && !ev.getMessage().getContent().startsWith("!!"))
		{
			Applicant appl = Applicants.getApplicant(ev.getMessage().getAuthor().asUser().get());
			if (appl != null)
				appl.onPrivateMessage(ev.getMessage());
		}
		/*if (ev.getChannel() != MainDiscordBot.getSongRequestsChannel() || ev.getMessage().getUserAuthor().get() != MainDiscordBot.api().getYourself())
			return;
		
		String msg = ev.getMessage().getContent();
		
		if (msg.equalsIgnoreCase("SR ENABLED"))
		{
			CommandRapSongRequests.songReqEnabled = true;
			
			if (MainTwitchBot.initialized())
				MainTwitchBot.bot().sendMessage("Enabled !!sr", MainTwitchBot.bot().channel);
			
			ev.getMessage().delete();
		}
		else if (msg.equalsIgnoreCase("SR DISABLED"))
		{
			CommandRapSongRequests.songReqEnabled = false;
			
			if (MainTwitchBot.initialized())
				MainTwitchBot.bot().sendMessage("Disabled !!sr", MainTwitchBot.bot().channel);
			
			ev.getMessage().delete();
		}
		else if (msg.startsWith("Remove: "))
		{
			String name = msg.replace("Remove: ", "");
			User rem = null;
			
			for (User u : SongRequests.requests.keySet())
			{
				if (u.toString().equalsIgnoreCase(name))
				{
					rem = u;
					break;
				}
			}
			
			SongRequests.requests.remove(rem);
			ev.getMessage().delete();
			
			try {
				
				List<Message> history = new ArrayList<Message>(MainDiscordBot.getSongRequestsChannel().getHistory(1000).get().getMessages());
				
				for (Message m : history)
				{
					if (m.getContent().startsWith("By " + name))
					{
						m.delete();
						break;
					}
				}
				
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		else if (msg.startsWith("FOLLOWERS: "))
		{
			MainTwitchBot.bot().sendMessage("@the404studios We've reached " + msg.replace("FOLLOWERS: ", "") + " followers!", MainTwitchBot.bot().channel);
			ev.getMessage().delete();
		}
		else if (msg.startsWith("GAINED: "))
		{
			gained = Integer.parseInt(msg.replace("GAINED: ", ""));
			ev.getMessage().delete();
		}
		else if (msg.equalsIgnoreCase("POGGERS"))
		{
			Random rand = new Random();
			boolean a = rand.nextBoolean();
			boolean b = rand.nextBoolean();
			String mesg = "";
			
			if (a)
				mesg = "POGGERS ";
			else
				mesg = "PogChamp ";
			
			if (b)
				mesg += "in the chat!";
			else
				mesg += "woo!";
			
			MainTwitchBot.bot().sendMessage(mesg, MainTwitchBot.bot().channel);
			ev.getMessage().delete();
		}
		else if (msg.equalsIgnoreCase("FLAMES"))
		{
			Random rand = new Random();
			int ch = rand.nextInt(4) + 2;
			boolean a = rand.nextBoolean();

			String mesg = "";
			
			if (a)
				mesg = "CurseLit ";
			else
				mesg = "TwitchLit ";
			
			for (int i = 0; i <= ch; i++)
			{
				boolean b = rand.nextBoolean();
				
				if (b)
					mesg += "CurseLit ";
				else
					mesg += "TwitchLit ";
			}
			
			if (rand.nextBoolean())
				mesg += "PogChamp";
			
			MainTwitchBot.bot().sendMessage(mesg, MainTwitchBot.bot().channel);
			ev.getMessage().delete();
		}
		else if (msg.equals("FETCH INFO"))
		{
			//if (MainTwitchBot.bot().channel.isLive())
				MainDiscordBot.getSongRequestsChannel().sendMessage("INFO: " + 
					MainTwitchBot.bot().channel.getViewers().size());

			ev.getMessage().delete();
		}
		else if (msg.startsWith("POINTSCOST:"))
		{
			int cost = Integer.parseInt(msg.split(" ")[1]);
			
			CommandRapSongRequests.pointsCost = cost;
			
			ev.getMessage().delete();
		}
		else if (msg.startsWith("ADDPOINTS:"))
		{
			String username = msg.split(" ")[1];
			int points = Integer.parseInt(msg.split(" ")[2]);
			
			MainTwitchBot.bot().sendMessage("@" + username + " thanks for donating! You gained " + points + " points.", MainTwitchBot.bot().channel);
			
			ev.getMessage().delete();
		}*/
	}

}

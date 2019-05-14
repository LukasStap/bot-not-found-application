package com.NeuroZap.GUI.Code.Discord.Listeners;

import de.btobastian.javacord.events.message.MessageDeleteEvent;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;

public class ListenerMessageDelete implements MessageDeleteListener {

	@Override
	public void onMessageDelete(MessageDeleteEvent ev) 
	{
		/*if (ev.getChannel() != MainDiscordBot.getSongRequestsChannel())
			return;
		
		String msg = ev.getMessage().get().getContent();

		String[] split = msg.split("[:] ");
		if (split[0].startsWith("By ") && split.length == 2)
		{
			User toRemove = null;

			for (User u : SongRequests.requests.keySet())
			{
				if (SongRequests.requests.get(u).equals(split[1]))
				{
					toRemove = u;
					break;
				}
			}
			
			if (toRemove != null)
			{
				SongRequests.requests.remove(toRemove);
				if (MainTwitchBot.initialized())
					MainTwitchBot.bot().sendMessage("Removed " + toRemove.toString() + "'s request.", MainTwitchBot.bot().channel);
			}
		}*/
	}

}

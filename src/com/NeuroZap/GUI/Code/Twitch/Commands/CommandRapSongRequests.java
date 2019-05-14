package com.NeuroZap.GUI.Code.Twitch.Commands;

import com.NeuroZap.GUI.GUIMain;
import com.NeuroZap.GUI.Code.Twitch.MainTwitchBot;
import com.NeuroZap.GUI.Code.Twitch.SongRequests;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;

public class CommandRapSongRequests extends TBotCommand {
	
	public static boolean songReqEnabled = false;

	public static int pointsCost = 20;

	@Override
	public String name() {
		return "sr";
	}

	@Override
	public String helpText() {
		return "**!!sr <URL>** - Lets people request songs for 404 when he enables song requests." + '\n' +
				"**!!sr list** - Lists the currently requested songs.";
	}

	@Override
	public boolean isCommand() {
		return true;
	}
	
	@Override
	public boolean modCommand() {
		return false;
	}
	
	@Override
	public int requiredValidationLevel() {
		return 0;
	}

	@Override
	public void execute(User user, Channel channel, String message) {
		if (message.startsWith("!sr"))
		{
			if (!songReqEnabled)
			{
				MainTwitchBot.bot().sendMessage("Song requests are disabled for now.", channel);
				return;
			}
			
			String[] split = message.split(" ");
			if (split.length == 1)
			{
				MainTwitchBot.bot().sendMessage("!!sr <URL> - request 404 to rap for an instrumental song you like/listen to it OR !!sr list", channel);
				if (pointsCost != -1)
					MainTwitchBot.bot().sendMessage("Costs " + pointsCost + " points to request a song.", channel);
			}
			else if (split.length == 2)
			{
				if (split[1].equalsIgnoreCase("list"))
				{
					if (SongRequests.requests.isEmpty())
					{
						MainTwitchBot.bot().sendMessage("Currently there are no song requests.", channel);
						return;
					}
					
					String msg = "";
					
					int i = 1;
					for (User u : SongRequests.requests.keySet())
					{
						msg += "#" + i + " by " + u.toString() + ": " + SongRequests.requests.get(u) + " ";
						i++;
					}
					MainTwitchBot.bot().sendMessage(msg, channel);
				} else {
					if (pointsCost != -1)
					{
						int p = GUIMain.points().getPoints(user);
						
						if (pointsCost > p)
						{
							MainTwitchBot.bot().sendMessage("You don't have enough points to request a song! (" + p + " out of " + pointsCost + ")", channel);
							return;
						}
						
						GUIMain.points().addPoints(user, -pointsCost);
					}
					SongRequests.addSongRequest(user, channel, split[1]);
				}
			}
		}
	}

}

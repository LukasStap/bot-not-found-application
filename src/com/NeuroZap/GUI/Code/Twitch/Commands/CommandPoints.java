package com.NeuroZap.GUI.Code.Twitch.Commands;

import com.NeuroZap.GUI.GUIMain;
import com.NeuroZap.GUI.Code.Twitch.MainTwitchBot;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;

public class CommandPoints extends TBotCommand {

	@Override
	public String name() {
		return "points";
	}

	@Override
	public String helpText() {
		// TODO Auto-generated method stub
		return "**!!points** - Shows the amount of points you have." + '\n' + 
				"Points can be used for various things, like song requesting. Leaderboards soon to come!";
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
		
		if (message.startsWith("!points"))
		{
			if (message.length() == 2 && (message.split(" ")[1].equalsIgnoreCase("help") || message.split(" ")[1].equalsIgnoreCase("?")))
			{
				MainTwitchBot.bot().sendMessage("You automatically get points by watching 404's stream! You'll be able to use them in various commands, like !!sr and !!gamble.", channel);
				return;
			}
			int points = GUIMain.points().getPoints(user);
			
			MainTwitchBot.bot().sendMessage("@" + user.toString() + " your points: [" + points + "]", channel);
		}
	}

}
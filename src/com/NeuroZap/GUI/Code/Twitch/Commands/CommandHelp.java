package com.NeuroZap.GUI.Code.Twitch.Commands;

import com.NeuroZap.GUI.Code.Twitch.MainTwitchBot;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;

public class CommandHelp extends TBotCommand {

	@Override
	public String name() {
		return "help";
	}

	@Override
	public String helpText() {
		// TODO Auto-generated method stub
		return "**!!help** - Show the commands you can do in twitch.";
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
		
		if (message.startsWith("!help"))
		{
			//if (user.isMod(channel))
			//	MainTwitchBot.bot().sendMessage("!!sr !!stop - more soon to come", channel);
			//else
				MainTwitchBot.bot().sendMessage("!!sr !!followers !!joke !!points !!gamble - more soon to come", channel);
		}
	}

}

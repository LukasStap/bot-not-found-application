package com.NeuroZap.GUI.Code.Twitch.Commands;

import com.NeuroZap.GUI.Code.Discord.Listeners.ListenerMessageCreate;
import com.NeuroZap.GUI.Code.Twitch.MainTwitchBot;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;

public class CommandFollowersGained extends TBotCommand {

	@Override
	public String name() {
		return "followers";
	}

	@Override
	public String helpText() {
		// TODO Auto-generated method stub
		return "**!!followers** - Shows followers gained during the stream.";
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
		
		if (message.startsWith("!followers"))
		{
			if (ListenerMessageCreate.gained <= 0)
				MainTwitchBot.bot().sendMessage("Followers gained during the stream: 0", MainTwitchBot.bot().channel);
			else
				MainTwitchBot.bot().sendMessage("Followers gained during the stream: " + ListenerMessageCreate.gained, MainTwitchBot.bot().channel);
		}
	}

}

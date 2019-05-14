package com.NeuroZap.GUI.Code.Twitch.Commands;

import com.NeuroZap.GUI.Code.Twitch.MainTwitchBot;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;

public abstract class TBotCommand implements TIBotCommand {

	public void onCommand(User user, Channel channel, String message)
	{
		if (channel != MainTwitchBot.bot().channel) 
			return;

		execute(user, channel, message);
	}

}

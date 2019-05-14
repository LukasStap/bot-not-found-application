package com.NeuroZap.GUI.Code.Twitch.Commands;

import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;

public interface TIBotCommand {
	
	public String name();
	
	public String helpText();
	
	public boolean isCommand();
	
	public boolean modCommand();
	
	/**
	 * 0 - anyone
	 * 1 - mods or higher
	 * 2 - admins or higher
	 * 3 - owner only
	 * @return
	 */
	public int requiredValidationLevel();
	
	public void execute(User user, Channel channel, String message);

}

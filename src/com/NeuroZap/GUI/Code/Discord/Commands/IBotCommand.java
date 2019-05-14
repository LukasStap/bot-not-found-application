package com.NeuroZap.GUI.Code.Discord.Commands;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.message.Message;

public interface IBotCommand {
	
	public String name();
	
	public String helpText();
	
	/**
	 * 0 - anyone
	 * 1 - mods or higher
	 * 2 - admins or higher
	 * 3 - owner only
	 * @return
	 */
	public int requiredValidationLevel();

	public void execute(DiscordApi api, Message msg);

}

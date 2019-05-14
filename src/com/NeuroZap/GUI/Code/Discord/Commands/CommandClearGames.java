package com.NeuroZap.GUI.Code.Discord.Commands;

import java.util.List;

import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;

public class CommandClearGames extends BotCommand {

	@Override
	public String name() {
		return "cleargames";
	}

	@Override
	public String helpText() {
		return "**!!cleargames** - Clears your game roles.";
	}

	@Override
	public int requiredValidationLevel()
	{
		return 0;
	}
	
	@Override
	public void execute(DiscordApi api, Message msg) 
	{
		User u = msg.getAuthor().asUser().get();
		
		if (u == api.getYourself())
			return;

		List<Role> roles = u.getRoles(MainDiscordBot.get404Discord());
		roles.remove(MainDiscordBot.getCSGORole());
		roles.remove(MainDiscordBot.getGmodRole());
		roles.remove(MainDiscordBot.getVRChatRole());
		roles.remove(MainDiscordBot.getRustRole());
		MainDiscordBot.get404Discord().updateRoles(u, roles);

		msg.getChannel().sendMessage("Your game roles have been cleared.");
	}

}

package com.NeuroZap.GUI.Code.Discord.Commands;

import com.NeuroZap.GUI.GUIMain;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.message.Message;

public class CommandPoints extends BotCommand {

	@Override
	public String name() {
		return "points";
	}

	@Override
	public String helpText() {
		return "**!!points <Twitch name>** - Displays the amount of points someone has.";
	}

	@Override
	public int requiredValidationLevel()
	{
		return 0;
	}
	
	@Override
	public void execute(DiscordApi api, Message msg) 
	{
		String[] data = msg.getContent().split(" ");
		
		if (data.length != 2)
		{
			msg.getChannel().sendMessage("**!!points <Twitch name>** - Displays the amount of points someone has.");
			return;
		}
		
		String name = data[1];
		
		if (!GUIMain.points().userExistsInDatabase(name))
		{
			msg.getChannel().sendMessage("User not found!");
			return;
		}
		
		int points = GUIMain.points().getPoints(name);
		
		msg.getChannel().sendMessage(name + "'s points: **" + points + "**");
	}

}

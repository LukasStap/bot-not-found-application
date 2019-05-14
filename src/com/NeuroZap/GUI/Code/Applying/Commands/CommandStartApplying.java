package com.NeuroZap.GUI.Code.Applying.Commands;

import java.util.ArrayList;
import java.util.List;

import com.NeuroZap.GUI.Code.Applying.ApplyMechanics;
import com.NeuroZap.GUI.Code.Discord.Commands.BotCommand;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.PrivateChannel;
import de.btobastian.javacord.entities.message.Message;

public class CommandStartApplying extends BotCommand {

	private List<String> confirm = new ArrayList<String>();
	
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "apply";
	}

	@Override
	public String helpText() {
		// TODO Auto-generated method stub
		return "**!!apply** - Starts the application process. (You can only private message me this command)";
	}

	@Override
	public int requiredValidationLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void execute(DiscordApi api, Message msg) {
		if (!(msg.getChannel() instanceof PrivateChannel))
		{
			msg.getChannel().sendMessage("This command can only be executed in my private messages.");
			return;
		}
		
		String[] args = msg.getContent().split(" ");

		if (args.length == 2 && args[1].equalsIgnoreCase("discord"))
		{
			if (!confirm.contains(msg.getAuthor().getIdAsString()))
			{
				if (!ApplyMechanics.canApply(msg.getAuthor().asUser().get()))
					return;
				
				confirm.add(msg.getAuthor().getIdAsString());
				msg.getChannel().sendMessage("Hello there! Would you like to apply to be a staff member in The 404 Community Discord server? "
						+ "If so, please type the command again as confirmation, and we will start the application process.");
				
				return;
			} else {
				confirm.remove(msg.getAuthor().getIdAsString());
				ApplyMechanics.startApplying(msg.getAuthor().asUser().get());
			}
		} else
		{
			msg.getChannel().sendMessage("You can only use !!apply discord for now.");
			return;
		}
		

	}

}

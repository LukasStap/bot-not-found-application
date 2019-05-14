package com.NeuroZap.GUI.Code.Discord.Commands;

import com.NeuroZap.GUI.Code.Twitch.Commands.TBCommands;
import com.NeuroZap.GUI.Code.Twitch.Commands.TBotCommand;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class CommandHelp extends BotCommand {

	@Override
	public String name() {
		return "help";
	}

	@Override
	public String helpText() {
		return "**!!help** - Displays command usage";
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

		int lvl = -1;
		
		try
		{
			lvl = BotCommand.getValidationLevel(u);
		}
		catch (Exception ex)
		{
			lvl = 0;
		}
		
		String mesg = "----------------------------" + '\n';

		mesg += "**DISCORD commands:**" + '\n';
		
		boolean f = false;
		
		for (BotCommand bc : BCommands.getCmds())
		{
			if (bc.helpText() != null && lvl >= bc.requiredValidationLevel())
			{
				mesg += bc.helpText() + '\n';
				f = true;
			}
		}
		
		if (!f)
			mesg += "-" + '\n';
		
		mesg += '\n' + " " + '\n';
		
		mesg += "**TWITCH commands:**" + '\n';
		
		f = false;
		
		for (TBotCommand tbc : TBCommands.getCmds())
		{
			if (tbc.helpText() != null && lvl >= tbc.requiredValidationLevel())
			{
				mesg += tbc.helpText() + '\n';
				f = true;
			}
		}
		
		if (!f)
			mesg += "-" + '\n';

		msg.getChannel().sendMessage(mesg);

		//msg.getChannel().sendMessage("Sent help commands. Slide into DMs.");
	}

}

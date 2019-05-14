
package com.NeuroZap.GUI.Code.Discord.Commands;

import java.util.ArrayList;
import java.util.List;

import com.NeuroZap.GUI.Code.Applying.Commands.CommandGeneralInfo;
import com.NeuroZap.GUI.Code.Applying.Commands.CommandStartApplying;


public class BCommands {
	
	private static List<BotCommand> cmds = new ArrayList<BotCommand>();
	
	public static void register()
	{
		cmds.clear();
		
		cmds.add(new CommandHelp());
		cmds.add(new CommandClearGames());
		cmds.add(new CommandStartApplying());
		cmds.add(new CommandGeneralInfo());
		//cmds.add(new CommandPruneCertified());
	}
	
	public static List<BotCommand> getCmds()
	{
		return new ArrayList<BotCommand>(cmds);
	}
	
}

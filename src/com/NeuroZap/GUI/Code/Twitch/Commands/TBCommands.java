
package com.NeuroZap.GUI.Code.Twitch.Commands;

import java.util.ArrayList;
import java.util.List;


public class TBCommands {
	
	private static List<TBotCommand> cmds = new ArrayList<TBotCommand>();
	
	public static void register()
	{
		cmds.clear();
		
		cmds.add(new CommandRandomResponses());
		// cmds.add(new CommandStopBot());
		// cmds.add(new CommandCallWifu());
		//cmds.add(new CommandRapSongRequests());
		cmds.add(new CommandHelp());
		//cmds.add(new CommandFollowersGained());
		cmds.add(new CommandJoke());
		//cmds.add(new CommandPoints());
		//cmds.add(new CommandGamble());
	}
	
	public static List<TBotCommand> getCmds()
	{
		return new ArrayList<TBotCommand>(cmds);
	}
	
}

package com.NeuroZap.GUI.Code.Discord;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.NeuroZap.GUI.GUIMain;
import com.NeuroZap.GUI.Code.Applying.ApplyMechanics;
import com.NeuroZap.GUI.Code.Applying.ListenerKeepAppsSticky;
import com.NeuroZap.GUI.Code.Discord.Commands.BCommands;
import com.NeuroZap.GUI.Code.Discord.Commands.BotCommand;
import com.NeuroZap.GUI.Code.Discord.Listeners.ListenerMessageCreate;
import com.NeuroZap.GUI.Code.Discord.Listeners.ListenerMessageDelete;
import com.NeuroZap.GUI.Code.Discord.Listeners.ListenerUserJoinDiscord;
import com.NeuroZap.GUI.Code.Discord.Listeners.ListenerUserLeft;
import com.NeuroZap.GUI.Code.Discord.Listeners.ListenerUserReactToMessage;
import com.NeuroZap.GUI.Code.Twitch.Commands.TBCommands;

import de.btobastian.javacord.AccountType;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.DiscordApiBuilder;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.permissions.Role;

public class MainDiscordBot {
	
	private static DiscordApi api;
	
	public static void initializeDiscordBot()
	{
		
		CompletableFuture<DiscordApi> fut = (new DiscordApiBuilder()).setAccountType(AccountType.BOT).setToken("NDU1MDc1MTAxNTI5MDc5ODMw.Df2tRg.Mgri1jIFT6lYm_qGQ1SlTWyds_g").login();
		try {
			api = fut.get();
		} catch (InterruptedException | ExecutionException e) {
			GUIMain.inst().getMainLabel().setEnabled(true);
			GUIMain.inst().getMainLabel().setText("Discord bot failed to load up! Could be internet.");
			e.printStackTrace();
		}
	
		if (api != null) {
			BCommands.register();
			TBCommands.register();

			for (BotCommand cmd : BCommands.getCmds()) {
				api.addMessageCreateListener(cmd);
			}
			
			api.addMessageDeleteListener(new ListenerMessageDelete());
			api.addMessageCreateListener(new ListenerMessageCreate());
			api.addServerMemberJoinListener(new ListenerUserJoinDiscord());
			api.addServerMemberBanListener(new ListenerUserLeft());
			api.addServerMemberLeaveListener(new ListenerUserLeft());
			api.addMessageCreateListener(new ListenerKeepAppsSticky());
			//api.addReactionAddListener(new ListenerUserReactToMessage());
			//api.addReactionRemoveListener(new ListenerUserReactToMessage());
		
			ListenerUserReactToMessage.registerChannels();
			checkUpRoles();
			InfoDisplay.updateInfo();
			ApplyMechanics.cleanupApps();

			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
			scheduler.scheduleAtFixedRate(new Runnable() {
				public void run() { 
					Random rand = new Random();
					
					int ch = rand.nextInt(11) + 1;
					
					if (ch == 1)
						api.updateActivity("$UICIDEBOY$");
					if (ch == 2)
						api.updateActivity("the ping nyxx game");
					if (ch == 3)
						api.updateActivity("learning to rap");
					if (ch == 4)
						api.updateActivity("snorting");
					if (ch == 5)
						api.updateActivity("human");
					if (ch == 6)
						api.updateActivity("2l of cocaine");
					if (ch == 7)
						api.updateActivity("with yo lady");
					if (ch == 8)
						api.updateActivity("family friendly");
					if (ch == 9)
						api.updateActivity("gatling raps");
					if (ch == 10)
						api.updateActivity("you");
					if (ch == 11)
						api.updateActivity("yeetus");
				}}, 0, 10, TimeUnit.MINUTES);
		}
	}
	
	public static void checkUpRoles()
	{
		for (User u : get404Discord().getMembers())
		{
			if (u.getMutualServers().contains(get404Discord()))
			{
				List<Role> roles = u.getRoles(get404Discord());
				boolean changed = false;
				if (!roles.contains(getCertified()) && !u.getIdAsString().equals("272391399679328256") && !u.getIdAsString().equals("206505852260057098"))
				{
					roles.add(getCertified());
					changed = true;
				}
				
				if (u.getActivity().isPresent())
				{
					String name = u.getActivity().get().getName();
					if (name.equalsIgnoreCase("Rust"))
					{
						changed = true;
						roles.add(getRustRole());
						roles.add(getGameSeparatorRole());
					}
					else if (name.equalsIgnoreCase("VRChat"))
					{
						changed = true;
						roles.add(getVRChatRole());
						roles.add(getGameSeparatorRole());
					}
					else if (name.equalsIgnoreCase("Counter-Strike: Global Offensive"))
					{
						changed = true;
						roles.add(getCSGORole());
						roles.add(getGameSeparatorRole());
					}
					else if (name.equalsIgnoreCase("Garry's Mod"))
					{
						changed = true;
						roles.add(getGmodRole());
						roles.add(getGameSeparatorRole());
					}
				}
				
				if (changed)
					get404Discord().updateRoles(u, roles);
			}
		}
	}
	
	public static TextChannel getTestChannel()
	{
		if (GUIMain.testMode)
			return api.getTextChannelById("520246221646856212").get();
		else
			return null;
	}
	
	public static TextChannel getBotCommandsChannel()
	{
		return api.getTextChannelById("510626903766335530").get();
	}

	public static TextChannel getNewsChannel()
	{
		return api.getTextChannelById("503209366376153113").get();
	}
	
	public static TextChannel getInfoChannel()
	{
		if (!GUIMain.testMode)
			return api.getTextChannelById("380420066946056193").get();
		else
			return getTestChannel();
	}
	
	public static User get404()
	{
		return api.getUserById("228565374285774848").get();
	}

	public static User getNeuro()
	{
		return api.getUserById("223072513293418496").get();
	}
	
	public static Server get404Discord()
	{
		return api.getServerById("293877891022979072").get();
	}
	
	// --------------------------------
	
	public static Server getHQDiscord()
	{
		return api.getServerById("546715235042590730").get();
	}
	
	public static TextChannel getHQAppLogChannel()
	{
		return api.getTextChannelById("574898177614938122").get();
	}
	
	public static TextChannel getHQAppManagementChannel()
	{
		return api.getTextChannelById("575300456063172608").get();
	}
	
	// --------------------------------
	
	public static Role getCertified()
	{
		return api.getRoleById("314977056129679361").get();
	}
	
	public static Role get404Gang()
	{
		return api.getRoleById("521877249264451591").get();
	}
	
	public static Role get404Army()
	{
		return api.getRoleById("506114595925262337").get();
	}
	
	public static Role getOwnerRole()
	{
		return api.getRoleById("309152254844207104").get();
	}
	
	public static Role getAdminRole()
	{
		return api.getRoleById("535927244720308234").get();
	}
	
	public static Role getModRole()
	{
		return api.getRoleById("503227649833959454").get();
	}
	
	public static Role getJuniorModRole()
	{
		return api.getRoleById("518701214704336899").get();
	}
	
	public static Role getSeniorModRole()
	{
		return api.getRoleById("535720264160378890").get();
	}
	
	public static Role getSuperModRole()
	{
		return api.getRoleById("535720355432628224").get();
	}
	
	public static Role getDevRole()
	{
		return api.getRoleById("535717949466476546").get();
	}
	
	public static DiscordApi api()
	{
		return api;
	}
	
	// -------------------------------- GAMES ROLES
	
	public static Role getCSGORole()
	{
		return api.getRoleById("522943438417821716").get();
	}
	
	public static Role getVRChatRole()
	{
		return api.getRoleById("522942874053246977").get();
	}
	
	public static Role getGmodRole()
	{
		return api.getRoleById("522943298885910543").get();
	}
	
	public static Role getRustRole()
	{
		return api.getRoleById("522943141096062997").get();
	}
	
	public static Role getGameSeparatorRole()
	{
		return api.getRoleById("538832515688038412").get();
	}
	


}

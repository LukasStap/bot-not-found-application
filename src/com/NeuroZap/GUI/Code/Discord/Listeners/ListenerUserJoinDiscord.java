package com.NeuroZap.GUI.Code.Discord.Listeners;

import java.util.ArrayList;
import java.util.List;

import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;

import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.events.server.member.ServerMemberJoinEvent;
import de.btobastian.javacord.listeners.server.member.ServerMemberJoinListener;

public class ListenerUserJoinDiscord implements ServerMemberJoinListener {

	@Override
	public void onServerMemberJoin(ServerMemberJoinEvent ev) 
	{
		if (ev.getServer() == MainDiscordBot.get404Discord())
		{
			List<Role> roles = new ArrayList<Role>();

			roles.add(MainDiscordBot.getCertified());

			MainDiscordBot.get404Discord().updateRoles(ev.getUser(), roles);
		}
	}

}

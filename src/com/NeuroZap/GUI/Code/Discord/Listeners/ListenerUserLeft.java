package com.NeuroZap.GUI.Code.Discord.Listeners;

import com.NeuroZap.GUI.Code.Applying.Applicants;
import com.NeuroZap.GUI.Code.Applying.ApplyMechanics;
import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.events.server.member.ServerMemberBanEvent;
import de.btobastian.javacord.events.server.member.ServerMemberLeaveEvent;
import de.btobastian.javacord.listeners.server.member.ServerMemberBanListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberLeaveListener;

public class ListenerUserLeft implements ServerMemberBanListener, ServerMemberLeaveListener {

	@Override
	public void onServerMemberBan(ServerMemberBanEvent ev) 
	{
		User u = ev.getUser();
		if (Applicants.isApplying(u) && ev.getServer() == MainDiscordBot.get404Discord())
		{
			Applicants.removeApplicant(Applicants.getApplicant(u));
			ApplyMechanics.logAction("(*" + u.getIdAsString() + "*) could not finish their application because they were banned.");
		}
	}

	@Override
	public void onServerMemberLeave(ServerMemberLeaveEvent ev) 
	{
		User u = ev.getUser();
		if (Applicants.isApplying(u) && ev.getServer() == MainDiscordBot.get404Discord())
		{
			Applicants.removeApplicant(Applicants.getApplicant(u));
			ApplyMechanics.logAction("(*" + u.getIdAsString() + "*) could not finish their application because they left the discord server.");
		}
	}

}

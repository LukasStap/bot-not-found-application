package com.NeuroZap.GUI.Code.Applying.Commands;

import java.awt.Color;
import java.util.List;

import com.NeuroZap.GUI.Code.Applying.Applicants;
import com.NeuroZap.GUI.Code.Configs.ApplicationData;
import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

public class CommandList {
	
	public static void listApps(Message msg, int page, boolean acceptedApps)
	{
		String desc = "";
		List<String> apps = null;
		
		if (acceptedApps)
			apps = Applicants.getAcceptedIds();
		else
			apps = Applicants.getOnHoldIds();
		
		int maxPage = apps.size() / 10 + 1;
		
		if (apps.size() == 0)
		{
			page = 0;
			maxPage = 0;
			desc = "*No applications found.*";
		}
		else
		{
			
			if (page > maxPage)
			{
				msg.getChannel().sendMessage("Invalid page! Maximum page can only be **" + maxPage + "**.");
				return;
			}
			
			for (int i = 10 * (page - 1); i < 10 * page; i++)
			{
				if (apps.size() == i)
					break;
				
				String id = apps.get(i);
				desc += "**" + MainDiscordBot.api().getUserById(id).get().getDiscriminatedName() + "** " + 
						"(*" + id + "*) | " + ApplicationData.getAppConfig(apps.get(i)).getString("date") + '\n';
				
				desc += "Reviewed by: ";
				List<String> reviewees = ApplicationData.getAppConfig(apps.get(i)).getStringList("reviewed");
				
				if (reviewees.isEmpty())
					desc += "-";
				
				for (String rev : reviewees)
				{
					desc += MainDiscordBot.api().getUserById(rev).get().getDiscriminatedName() + " ";
				}
				desc += '\n';
				
				desc += "Accepted by: ";
				List<String> accepted = ApplicationData.getAppConfig(apps.get(i)).getStringList("accepted");
				
				if (accepted.isEmpty())
					desc += "-";
				
				for (String rev : accepted)
				{
					desc += MainDiscordBot.api().getUserById(rev).get().getDiscriminatedName() + " ";
				}
				desc += '\n';
				
				desc += "Denied by: ";
				List<String> denied = ApplicationData.getAppConfig(apps.get(i)).getStringList("denied");
				
				if (denied.isEmpty())
					desc += "-";
				
				for (String rev : denied)
				{
					desc += MainDiscordBot.api().getUserById(rev).get().getDiscriminatedName() + " ";
				}
				desc += '\n';
			}
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		if (acceptedApps)
			eb.setTitle("**Accepted Applications** (" + page + "/" + maxPage + ")");
		else
			eb.setTitle("**On-Hold Applications** (" + page + "/" + maxPage + ")");
		eb.setColor(Color.CYAN);
		eb.setDescription(desc);
		eb.setFooter("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		msg.getChannel().sendMessage(eb);
	}

}

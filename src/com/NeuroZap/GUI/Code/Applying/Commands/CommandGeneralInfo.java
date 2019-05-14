package com.NeuroZap.GUI.Code.Applying.Commands;

import java.awt.Color;
import java.util.List;

import com.NeuroZap.GUI.Code.Applying.Applicants;
import com.NeuroZap.GUI.Code.Applying.ReviewMechanics;
import com.NeuroZap.GUI.Code.Configs.ApplicationData;
import com.NeuroZap.GUI.Code.Configs.MainData;
import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;
import com.NeuroZap.GUI.Code.Discord.Commands.BotCommand;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

public class CommandGeneralInfo extends BotCommand {

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "apps";
	}

	@Override
	public String helpText() {
		// TODO Auto-generated method stub
		return "**!!apps** - An admin command, used to manage the submitted staff applications.";
	}

	@Override
	public int requiredValidationLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void execute(DiscordApi api, Message msg) {
		if (!msg.getChannel().equals(MainDiscordBot.getHQAppManagementChannel()))
		{
			msg.getChannel().sendMessage("This command can only be executed in the HQ application management channel.");
			return;
		}
		
		String[] args = msg.getContent().split(" ");
		if (args.length >= 2 && args[1].equalsIgnoreCase("review"))
		{
			ReviewMechanics.onReviewCommand(msg.getAuthor().asUser().get(), msg.getChannel(), args);
			return;
		}
		if (args.length == 1)
		{
			String desc = "----" + '\n';
			
			List<String> apps = Applicants.getIdsWhoApplied();
			
			String date = "-";
			if (!apps.isEmpty())
				date = ApplicationData.getAppConfig(apps.get(apps.size() - 1))
				.getString("date");
			
			desc += "Total application count: **" + apps.size() + "**" + '\n';
			desc += "Last app submitted: " + date + '\n';
			desc += '\n';
			
			int acc = MainData.get().getStringList("applications.accepted").size();
			int rej = MainData.get().getStringList("applications.denied").size();
			int onHold = MainData.get().getStringList("applications.on-hold").size();
			desc += "Total on-hold applications: **" + onHold + "**" + '\n';
			desc += "Total accepted applications: **" + acc + "**" + '\n';
			desc += "Total rejected applications: **" + rej + "**" + '\n';
			desc += "Total reviewed applications: **" + (acc + rej + onHold) + "**" + '\n';
			
			desc += "----";
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("**Application Info**");
			eb.setColor(Color.CYAN);
			eb.setDescription(desc);
			eb.setFooter("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			msg.getChannel().sendMessage(eb);
			return;
		}
		else if (args.length == 2)
		{
			if (args[1].equalsIgnoreCase("help"))
			{
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle("**Application Help**");
				eb.setColor(Color.CYAN);
				
				String desc = "**!!apps** - Shows you general information about the applications." + '\n' + '\n';
				desc += "**!!apps list [optional page]** - Lists IDs of all applications which are on-hold." + '\n' + '\n';
				desc += "**!!apps listaccepted [optional page]** - Lists IDs of all applications which are accepted." + '\n' + '\n';
				desc += "**!!apps delete <ID>** - Removes an application from the database." + '\n' + '\n';
				desc += "**!!apps review** - Accesses the application review mode. Write this command to see more info." + '\n' + '\n';
				desc += '\n';
				eb.setDescription(desc);
				eb.setFooter("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
				msg.getChannel().sendMessage(eb);
				return;
			}
			else if (args[1].equalsIgnoreCase("list"))
			{
				CommandList.listApps(msg, 1, false);
			}
			else if (args[1].equalsIgnoreCase("listaccepted"))
			{
				CommandList.listApps(msg, 1, true);
			}
		}
		else if (args.length == 3)
		{
			if (args[1].equalsIgnoreCase("list"))
			{
				int page = -1;
				
				try
				{
					page = Integer.parseInt(args[2]);
				} catch (Exception ex)
				{
					msg.getChannel().sendMessage("Incorrect command. Please use **!!apps list <page number>**");
					return;
				}
				
				CommandList.listApps(msg, page, false);
			}
			else if (args[1].equalsIgnoreCase("listaccepted"))
			{
				int page = -1;
				
				try
				{
					page = Integer.parseInt(args[2]);
				} catch (Exception ex)
				{
					msg.getChannel().sendMessage("Incorrect command. Please use **!!apps listaccepted <page number>**");
					return;
				}
				
				CommandList.listApps(msg, page, true);
			} 
			else if (args[1].equalsIgnoreCase("delete"))
			{
				if (args.length != 3)
				{
					msg.getChannel().sendMessage("**!!apps delete <ID>** - Removes an application from the database.");
					return;
				}
				
				String id = args[2];
				
				List<String> appl = Applicants.getIdsWhoApplied();
				List<String> onHold = Applicants.getOnHoldIds();
				List<String> acc = Applicants.getAcceptedIds();
				List<String> denied = Applicants.getDeniedIds();
				
				if (!appl.contains(id) && !onHold.contains(id) &&
						!acc.contains(id) && !denied.contains(id))
				{
					msg.getChannel().sendMessage("Application with that ID doesn't exist.");
					return;
				}
				
				appl.remove(id);
				onHold.remove(id);
				acc.remove(id);
				denied.remove(id);
				
				MainData.get().set("applications.applied", appl);
				MainData.get().set("applications.on-hold", onHold);
				MainData.get().set("applications.accepted", acc);
				MainData.get().set("applications.denied", denied);
				MainData.save();
				
				msg.getChannel().sendMessage("Removed from data lists..");

				if (ApplicationData.deleteAppConfig(id))
				{
					msg.getChannel().sendMessage("Application has been fully deleted.");
				} else {
					msg.getChannel().sendMessage("Could not delete the main application file. Might not've existed in the first place?");
				}
			}
		}
	}
	
}

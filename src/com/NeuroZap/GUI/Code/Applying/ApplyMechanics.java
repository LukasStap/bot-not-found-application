package com.NeuroZap.GUI.Code.Applying;

import java.awt.Color;
import java.util.concurrent.ExecutionException;

import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

public class ApplyMechanics {

	public static boolean canApply(User u)
	{
		if (Applicants.isApplying(u))
		{
			Applicant appl = Applicants.getApplicant(u);
			
			if (!Applicants.confirmStopApplying.contains(appl))
			{
				u.sendMessage("Would you like to stop the application process? Type the command again to confirm and cancel the process.");
				Applicants.confirmStopApplying.add(appl);
			} else {
				u.sendMessage("Stopping the process..");
				Applicants.confirmStopApplying.remove(appl);
				appl.stopApplying();
			}
			return false;
		}
		else if (Applicants.hasApplied(u))
		{
			
			
			u.sendMessage("You have already applied and Your application has been submitted.");
			return false;
		}
		return true;
	}
	
	public static Applicant startApplying(User u)
	{
		
		
		u.sendMessage("We will now start the staff application process. "
				+ "Before anything, a note: if something goes wrong during this application process, "
				+ "for example if I stop responding, please message " + MainDiscordBot.getNeuro().getMentionTag() + " "
						+ "or " + MainDiscordBot.api().getUserById("272391399679328256").get().getMentionTag() + ". "
						+ "As such, I recommend saving all your answers somewhere, in case an error occurs and You would need to repeat this process." 
						+ '\n' + '\n'
						+ "Without further adue, let's begin the application process. Please answer every question with a single message. "
						+ "If you mess up, you can cancel this process by using the command **!!apply discord** and restarting.");
		
		Applicant appl = new Applicant(u);
		appl.setQuestion("If you understand and wish to proceed, please message me any message that isn't a command.");
		u.sendMessage("If you understand and wish to proceed, please message me any message that isn't a command (the message can't start with a !!)");
		logAction("(*" + u.getIdAsString() + "*) has started the application process.");
		return appl;
	}
	
	public static void logAction(String s)
	{
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.DARK_GRAY);
		eb.setDescription("" + s + "");
		eb.setFooter("=-=-=-=-  -=-=-=-=");
		MainDiscordBot.getHQAppLogChannel().sendMessage(eb);
	}
	
	public static void logAction(String s, String subMessage)
	{
		logAction(s);
		MainDiscordBot.getHQAppLogChannel().sendMessage(subMessage);
	}
	
	public static void cleanupApps()
	{
		int i = 0;
		try {
			for (Message m : MainDiscordBot.getHQAppManagementChannel().getHistory(300).get().getMessages())
			{
				String footer = null;

				if (!m.getEmbeds().isEmpty())
				{
					footer = m.getEmbeds().get(0).getFooter().get().getText().get();
					if (footer.startsWith("ID: "))
					{
						m.delete();
						i++;
					}
				}

			}
		} catch (InterruptedException | ExecutionException e) {
			MainDiscordBot.getHQAppLogChannel().sendMessage("Tried to delete old apps in the app management channel, but encountered an error: " + e.getMessage());
			e.printStackTrace();
		}
		if (i != 0)
		{
			logAction("Cleaned up one or more old apps from the app management channel.");
		}
	}

}

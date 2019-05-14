package com.NeuroZap.GUI.Code.Applying;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.simpleyaml.configuration.file.FileConfiguration;

import com.NeuroZap.GUI.Code.Configs.ApplicationData;
import com.NeuroZap.GUI.Code.Configs.MainData;
import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class Applicant {
	
	private User u;
	private String askedQuestion = null;
	private int stage = 0;
	private int maxChar = 30;
	
	private List<String> answers = new ArrayList<String>();
	
	public Applicant(User u)
	{
		this.u = u;
		Applicants.addApplicant(this);
	}
	
	public String getId()
	{
		return u.getIdAsString();
	}
	
	public User getUser()
	{
		return u;
	}
	
	public void setQuestion(String s)
	{
		askedQuestion = s;
	}
	
	public void onPrivateMessage(Message m)
	{
		if (askedQuestion != null)
		{
			if (m.getContent().contains("|"))
			{
				m.getAuthor().asUser().get().sendMessage("Please don't use the symbol | in Your answer. We know, really specific, but that would break some stuff.");
				return;
			}
			String answer = m.getContent();
			if (answer.length() > maxChar)
			{
				m.getAuthor().asUser().get().sendMessage("This answer is too big. Please shorten it to up to " + maxChar + " characters.");
				return;
			}
			if (stage == 2)
			{
				int age = -1;
				try
				{
					age = Integer.parseInt(answer);
				}
				catch (Exception ex)
				{
					m.getAuthor().asUser().get().sendMessage("Please answer that question with a valid numerical answer.");
					return;
				}
				
				if (age <= 0 || age >= 110)
				{
					m.getAuthor().asUser().get().sendMessage("Please answer that question with a valid numerical answer.");
					return;
				}
				
				if (age > 0 && age <= 12)
				{
					ApplyMechanics.logAction("*(" + u.getIdAsString() + ")* has defined their age less or equal to 12 in their application. (**" + age + "**)", 
							MainDiscordBot.api().getUserById("272391399679328256").get().getMentionTag() + " ^");
				}
			}
			if (stage == 15)
			{
				if (answer.equalsIgnoreCase("no"))
				{
					stage = 18;
				}
				else if (!answer.equalsIgnoreCase("yes"))
				{
					m.getAuthor().asUser().get().sendMessage("Please answer that question with either 'Yes' or 'No'.");
					return;
				}
			}
			
			answers.add(askedQuestion + "|" + answer);
			stage++;
			askQuestion();
		}
	}
	
	private void askQuestion()
	{
		switch (stage)
		{
			case 1:
				setQuestion("Name");
				maxChar = 30;
				u.sendMessage("What is Your name? (Up to " + maxChar + " characters)");
				break;
			case 2:
				setQuestion("Age");
				maxChar = 3;
				u.sendMessage("How old are You? Please write a number. (Up to " + maxChar + " characters)");
				break;
			case 3:
				setQuestion("Country and timezone");
				maxChar = 60;
				u.sendMessage("What country and time zone do You live in? (Up to " + maxChar + " characters)");
				break;
			case 4:
				setQuestion("Currently active in Discord?");
				maxChar = 20;
				u.sendMessage("Are You currently an active user in our Discord? (Up to " + maxChar + " characters)");
				break;
			case 5:
				setQuestion("When did you join our discord?");
				maxChar = 30;
				u.sendMessage("When did you join our discord? (Up to " + maxChar + " characters)");
				break;
			case 6:
				setQuestion("How often on Discord per day?");
				maxChar = 50;
				u.sendMessage("How often are You on Discord per day? (Up to " + maxChar + " characters)");
				break;
			case 7:
				setQuestion("What position are You applying for?");
				maxChar = 15;
				u.sendMessage("What position are You applying for? Please choose either Jr. Mod, Mod or Sr. Mod (Up to " + maxChar + " characters)");
				break;
			case 8:
				setQuestion("Which qualities do You possess that would be helpful to the team and community?");
				maxChar = 150;
				u.sendMessage("Which qualities (in Your opinion) do You possess that would be helpful to the moderator team, and otherwise the community? (Up to " + maxChar + " characters)");
				break;
			case 9:
				setQuestion("How important do You think rules are in a community like ours' and why?");
				maxChar = 90;
				u.sendMessage("How important do You think rules are in a community like ours' and why? (Up to " + maxChar + " characters)");
				break;
			case 10:
				setQuestion("Do You have experience working in team environments?");
				maxChar = 100;
				u.sendMessage("Do You have experience working in team environments? If so, please explain. (Up to " + maxChar + " characters)");
				break;
			case 11:
				setQuestion("How would You handle conflict in a team environment?");
				maxChar = 100;
				u.sendMessage("How would You handle conflict in a team environment? (Up to " + maxChar + " characters)");
				break;
			case 12:
				setQuestion("How would You deal with a fellow moderator, or someone else, that You feel is being stubborn or rude?");
				maxChar = 100;
				u.sendMessage("How would You deal with a fellow moderator, or someone else You are arguing with, that You feel is being stubborn or rude? (Up to " + maxChar + " characters)");
				break;
			case 13:
				setQuestion("Why do You want to be a Discord Moderator with us?");
				maxChar = 100;
				u.sendMessage("Why do You want to be a Discord Moderator with us? (Up to " + maxChar + " characters)");
				break;
			case 14:
				setQuestion("What's the meaning behind Your username?");
				maxChar = 100;
				u.sendMessage("What's the meaning behind Your username? (Up to " + maxChar + " characters)");
				break;
			case 15:
				setQuestion("Have You ever been a staff member in a Discord Server before?");
				maxChar = 3;
				u.sendMessage("Have You ever been a staff member in a Discord Server before? Please answer with either 'yes' or 'no'.");
				break;
			case 16:
				setQuestion("In what server(-s) have You been staff before?");
				maxChar = 100;
				u.sendMessage("In what server(-s) have You been staff before? (Up to " + maxChar + " characters)");
				break;
			case 17:
				setQuestion("What tasks and duties were You in charge of in Your previous staff experience?");
				maxChar = 100;
				u.sendMessage("What tasks and duties were You in charge of in Your previous staff experience? (Up to " + maxChar + " characters)");
				break;
			case 18:
				setQuestion("What is Your proudest moment of being a moderator?");
				maxChar = 100;
				u.sendMessage("What is Your proudest moment of being a moderator? (Up to " + maxChar + " characters)");
				break;
			default:
				finishApplying();
				break;
		}
	}
	
	public void finishApplying()
	{
		u.sendMessage("You have finished! Submitting Your application..");
		
		FileConfiguration config = ApplicationData.getAppConfig(u);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		
		Calendar cal = Calendar.getInstance();
		
		try {
			sdf.setTimeZone(cal.getTimeZone());
			String s = sdf.parse(sdf.format(cal.getTime())).toString();
			config.set("date", s);
		} catch (ParseException e) {
			e.printStackTrace();
			MainDiscordBot.getHQAppLogChannel().sendMessage("Exception when trying to write the time in: " + e.getLocalizedMessage());
		}
		
		config.set("answers", answers);
		ApplicationData.saveAppConfig(u, config);
		
		List<String> applied = MainData.get().getStringList("applications.applied");
		applied.add(u.getIdAsString());
		MainData.get().set("applications.applied", applied);
		
		Applicants.removeApplicant(this);
		u.sendMessage("Submitted. It will now be reviewed by the staff members whenever they have the time for it. Please be patient and don't ask for "
				+ "them to review it. We will get back to You if Your application has been accepted or denied. Have a good day! <3");
		
		ApplyMechanics.logAction("*(" + u.getIdAsString() + ")* has finished the application process. App submitted.");
		MainData.get().set("applications.total-submitted", MainData.get().getInt("applications.total-submitted") + 1);
		MainData.save();
	}

	public void stopApplying()
	{
		u.sendMessage("You have stopped the application process. You can restart whenever You would like to with the !!apply command.");
		ApplyMechanics.logAction("*(" + u.getIdAsString() + ")* has cancelled the application process.");
		Applicants.removeApplicant(this);
	}
	
}

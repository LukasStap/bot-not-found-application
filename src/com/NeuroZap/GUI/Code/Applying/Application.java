package com.NeuroZap.GUI.Code.Applying;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.simpleyaml.configuration.file.FileConfiguration;

import com.NeuroZap.GUI.Code.Configs.ApplicationData;
import com.NeuroZap.GUI.Code.Configs.MainData;
import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

/**
 * This class represents applications.
 */
public class Application {
	
	private User u;

	private String dateSubmitted;
	private FileConfiguration conf;
	
	private Map<String, String> answers;
	
	private boolean locked = false;
	
	public Application(User u)
	{
		new Application(u.getIdAsString());
	}

	public Application(String id)
	{
		this.u = MainDiscordBot.api().getUserById(id).get();
		conf = ApplicationData.getAppConfig(id);
		dateSubmitted = conf.getString("date");
		answers = new LinkedHashMap<String, String>();

		String firstQ = "";
		String firstA = "";
		for (String s : conf.getStringList("answers"))
		{
			String[] qa = s.split("[|]");
			if (firstQ == "")
			{
				firstQ = qa[0];
				firstA = qa[1];
				continue;
			}
			answers.put(qa[0], qa[1]);
			if (qa[0].equalsIgnoreCase("age"))
			{
				answers.put(firstQ, firstA);
			}
		}
	}
	

	
	public String getId()
	{
		return u.getIdAsString();
	}
	
	public User getUser()
	{
		return u;
	}
	
	public void lockApplication()
	{
		locked = true;
	}
	
	public boolean locked()
	{
		return locked;
	}
	
	public List<EmbedBuilder> getFormattedApplications(boolean reveal)
	{
		List<EmbedBuilder> builders = new ArrayList<EmbedBuilder>();
		EmbedBuilder eb1 = new EmbedBuilder();
		EmbedBuilder eb2 = new EmbedBuilder();
		
		String dName = "[*Hidden*]";
		if (reveal)
			dName = "**" + u.getDiscriminatedName() + "**";
		
		String desc1 = " App submitted: " + dateSubmitted + '\n' + '\n';
		String desc2 = "";
		
		desc1 += " Discord name: " + dName + '\n' + '\n';
		int i = 1;
		
		for (String q : answers.keySet())
		{
			String answer = answers.get(q);
			if (q.equalsIgnoreCase("name") && !reveal)
			{
				answer = "[*Hidden*]";
			}
			if (i < 13)
				desc1 += "#" + i++ + " " + q + ": " + answer + '\n' + '\n';
			else
				desc2 += "#" + i++ + " " + q + ": " + answer + '\n' + '\n';
		}
		Map<String, String> comments = getComments();
		
		if (!getWhoReviewed().isEmpty())
		{
			desc2 += "**Application reviewed by:**" + '\n';
			for (String id : getWhoReviewed())
			{
				desc2 += MainDiscordBot.api().getUserById(id).get().getDiscriminatedName() + '\n';
			}
			desc2 += '\n';
		}
		if (!getWhoAccepted().isEmpty())
		{
			desc2 += "**Application accepted by:**" + '\n';
			for (String id : getWhoAccepted())
			{
				desc2 += MainDiscordBot.api().getUserById(id).get().getDiscriminatedName() + '\n';
			}
			desc2 += '\n';
		}
		if (!getWhoDenied().isEmpty())
		{
			desc2 += "**Application denied by:**" + '\n';
			for (String id : getWhoDenied())
			{
				desc2 += MainDiscordBot.api().getUserById(id).get().getDiscriminatedName() + '\n';
			}
			desc2 += '\n';
		}
		
		if (!comments.isEmpty())
		{
			desc2 += "**Comments:**" + '\n';
			for (String commentee: comments.keySet())
			{
				desc2 += commentee + ": " + comments.get(commentee) + '\n' + '\n';
			}
		}
		
		eb1.setTitle("**Application Review (1/2)**");
		eb1.setColor(Color.CYAN);
		eb1.setDescription(desc1);
		
		if (!reveal)
			eb1.setFooter("ID: [*Hidden*]");
		else
			eb1.setFooter("ID: " + u.getIdAsString());
		
		eb2.setTitle("**Application Review (2/2)**");
		eb2.setColor(Color.CYAN);
		eb2.setDescription(desc2);
		
		if (!reveal)
			eb2.setFooter("ID: [*Hidden*]");
		else
			eb2.setFooter("ID: " + u.getIdAsString());
		
		builders.add(eb1);
		builders.add(eb2);
		
		return builders;
	}
	
	public Map<String, String> getComments()
	{
		Map<String, String> comments = new LinkedHashMap<String, String>();
		List<String> fileComments = conf.getStringList("comments");
		
		for (String c : fileComments)
		{
			String[] data = c.split("[|]");

			comments.put(data[0], data[1]);
		}
		
		return comments;
		
	}
	
	public void addComment(User u, String comment)
	{
		List<String> comments = conf.getStringList("comments");
		String name = u.getDiscriminatedName();
		
		if (name.contains("|"))
			name = name.replace("|", "");
		
		comments.add("(" + (comments.size() + 1) + ") **" + name + "**|" + comment);
		conf.set("comments", comments);
		
		ApplicationData.saveAppConfig(this.u, conf);
	}
	
	public List<String> getWhoAccepted()
	{
		return conf.getStringList("accepted");
	}
	
	public List<String> getWhoDenied()
	{
		return conf.getStringList("denied");
	}
	
	public List<String> getWhoReviewed()
	{
		return conf.getStringList("reviewed");
	}
	
	public void addAccepted(User u)
	{
		List<String> accepted = conf.getStringList("accepted");
		List<String> denied = conf.getStringList("denied");
		if (!accepted.contains(u.getIdAsString()))
			accepted.add(u.getIdAsString());
		denied.remove(u.getIdAsString());
		conf.set("accepted", accepted);
		conf.set("denied", denied);
		ApplicationData.saveAppConfig(this.u, conf);
	}
	
	public void addDenied(User u)
	{
		List<String> accepted = conf.getStringList("accepted");
		List<String> denied = conf.getStringList("denied");
		accepted.remove(u.getIdAsString());
		if (!denied.contains(u.getIdAsString()))
			denied.add(u.getIdAsString());
		conf.set("accepted", accepted);
		conf.set("denied", denied);
		ApplicationData.saveAppConfig(this.u, conf);
	}
	
	public void addReviewed(User u)
	{
		List<String> reviewed = conf.getStringList("reviewed");
		if (!reviewed.contains(u.getIdAsString()))
			reviewed.add(u.getIdAsString());
		conf.set("reviewed", reviewed);
		ApplicationData.saveAppConfig(this.u, conf);
	}

	public void accept()
	{
		List<String> apps = Applicants.getIdsWhoApplied();
		List<String> onHold = Applicants.getOnHoldIds();
		List<String> accepted = Applicants.getAcceptedIds();
		
		apps.remove(u.getIdAsString());
		onHold.remove(u.getIdAsString());
		
		if (!accepted.contains(u.getIdAsString()))
			accepted.add(u.getIdAsString());
		
		MainData.get().set("applications.applied", apps);
		MainData.get().set("applications.on-hold", onHold);
		MainData.get().set("applications.accepted", accepted);
		MainData.save();
	}
	
	public void deny()
	{
		List<String> apps = Applicants.getIdsWhoApplied();
		List<String> onHold = Applicants.getOnHoldIds();
		List<String> denied = Applicants.getDeniedIds();
		
		apps.remove(u.getIdAsString());
		onHold.remove(u.getIdAsString());
		
		if (!denied.contains(u.getIdAsString()))
			denied.add(u.getIdAsString());
		MainData.get().set("applications.applied", apps);
		MainData.get().set("applications.on-hold", onHold);
		MainData.get().set("applications.denied", denied);
		MainData.save();
	}
	
	public void onHold()
	{
		List<String> apps = Applicants.getIdsWhoApplied();
		List<String> onHold = Applicants.getOnHoldIds();
		apps.remove(u.getIdAsString());
		if (!onHold.contains(u.getIdAsString()))
			onHold.add(u.getIdAsString());
		MainData.get().set("applications.applied", apps);
		MainData.get().set("applications.on-hold", onHold);
		MainData.save();
	}

}

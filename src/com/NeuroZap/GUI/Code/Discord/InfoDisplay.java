package com.NeuroZap.GUI.Code.Discord;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.NeuroZap.GUI.GUIMain;
import com.NeuroZap.GUI.Code.Configs.MainData;
import com.NeuroZap.GUI.Code.Twitch.MainTwitchBot;
import com.NeuroZap.GUI.Code.YouTube.MainYouTubeBot;
import com.cavariux.twitchirc.Chat.Channel;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

public class InfoDisplay {

	private static ScheduledExecutorService scheduler = null;
	private static com.google.api.services.youtube.model.Channel fourChannel = null;
	private static Map<String, Integer> subs = new HashMap<String, Integer>();
	private static Map<String, Integer> follows = new HashMap<String, Integer>();
	private static Map<String, Integer> size = new HashMap<String, Integer>();
	private static Map<String, Long> views = new HashMap<String, Long>();
	private static int lastCheckedHour = -1;
	private static int lastCheckedMinute = -1;
	private static int startedHourSubs = 0;
	private static int startedHourDiscordSize = 0;
	private static int startedHourTwitchFollowers = 0;
	private static long startedHourViews = 0;
	
	public static void updateInfo()
	{
		try
		{
			scheduler = Executors.newScheduledThreadPool(1);
			scheduler.schedule(new Runnable() {
				public void run() {
					Message m = fetchMessage();
					if (m == null)
					{
						m = createMessage();
						
						if (m == null)
							return;
					}
					
					String desc = "---------------" + '\n';
					desc += "- - - -> **Staff Team** <- - - -" + '\n' + '\n';
					desc = applyStaff(desc);
					desc += "---------------" + '\n';
	
		            try {
						YouTube.Channels.List channels = MainYouTubeBot.getYouTubeService().channels().list("statistics");
						channels.setId("UCrNTseMRDdl089VTnEpQnrg");
			            ChannelListResponse response = channels.execute();
			            fourChannel = response.getItems().get(0);
					} catch (IOException e) {
						e.printStackTrace();
					}
	
		            Channel ch = MainTwitchBot.bot().channel;
		            
			        int subCount = fourChannel.getStatistics().getSubscriberCount().intValue();
			        long viewCount = fourChannel.getStatistics().getViewCount().longValue();
			        int followCount = ch.getFollowersNum();
			        int sizeCount = MainDiscordBot.get404Discord().getMemberCount();
					checkUpNewSubs(subCount, viewCount, sizeCount, followCount);
					checkUpSubs(subCount);
					
					int newSubs = 0;
					long newViews = 0;
					int newFollows = 0;
					int newSize = 0;
					for (int bi : subs.values())
					{
						newSubs += bi;
					}
					for (int bi : follows.values())
					{
						newFollows += bi;
					}
					for (int bi : size.values())
					{
						newSize += bi;
					}
					for (long bi : views.values())
					{
						newViews += bi;
					}
					
					desc += "**https://youtube.com/channel/UCrNTseMRDdl089VTnEpQnrg**" + '\n';
		            desc += "YouTube subscribers: **" + subCount + "** (**" + newSubs + "** in the last " + (subs.size() - 1) + " hour(-s))" + '\n';
		            desc += "YouTube total views: " + viewCount + " (" + newViews + " in the last " + (views.size() - 1) + " hour(-s))" + '\n';
		            desc += "YouTube total videos: " + fourChannel.getStatistics().getVideoCount() + '\n' + '\n';

					desc += "**http://twitch.tv/the404studios**" + '\n';
					desc += "Twitch followers: " + followCount + " (" + newFollows + " in the last " + (views.size() - 1) + " hour(-s))" + '\n';
					if (!ch.isLive())
					{
						desc += "Live on Twitch: **No**" + '\n' + '\n';
					}
					else {
						desc += "Live on Twitch: Yes (**" + ch.getTitle() + "**) playing (*" + ch.getGame() + "*)" + '\n';
						desc += "Current viewcount: " + ch.getViewers().size() + '\n';
						desc += "Total viewcount: " + ch.getTotalViews() + '\n' + '\n';
					}
					
					desc += "Discord size: **" + sizeCount + "**" + " (" + newSize + " in the last " + (views.size() - 1) + " hour(-s))" + '\n' + '\n';
					
					EmbedBuilder eb = new EmbedBuilder();
					eb.setTitle("**404 Community Information**          ");
					eb.setColor(Color.BLUE);
					eb.setDescription(desc);
					eb.setFooter("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
					eb.setThumbnail(MainDiscordBot.get404().getAvatar().getUrl().toString());
					m.edit("", eb);
					MainDiscordBot.checkUpRoles();
					checkMilestones();
					MainTwitchBot.initializeTwitchBot();
					updateInfo();
			}}, 60, TimeUnit.SECONDS);
		} catch (Exception ex)
		{
			MainDiscordBot.getNeuro().sendMessage("Exception in info update timer: " + ex.getLocalizedMessage());
			ex.printStackTrace();
			//updateInfo();
		}
	}
	
	private static void checkMilestones()
	{
		
	}
	
	private static void checkUpNewSubs(int sub, long view, int discSize, int twitchFollows)
	{
		if (lastCheckedHour == -1)
		{
			lastCheckedHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			lastCheckedMinute = Calendar.getInstance().get(Calendar.MINUTE);
			startedHourSubs = sub;
			startedHourViews = view;
			startedHourDiscordSize = discSize;
			startedHourTwitchFollowers = twitchFollows;
			subs.put(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "", 0);
			views.put(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "", (long)0);
		}
		else
		{
			if (lastCheckedHour != Calendar.getInstance().get(Calendar.HOUR_OF_DAY) && lastCheckedMinute >= Calendar.getInstance().get(Calendar.MINUTE))
			{
				lastCheckedHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				startedHourSubs = sub;
				startedHourViews = view;
				startedHourDiscordSize = discSize;
				startedHourTwitchFollowers = twitchFollows;
			}

			subs.put(lastCheckedHour + "", sub - startedHourSubs);
			views.put(lastCheckedHour + "", view - startedHourViews);
			size.put(lastCheckedHour + "", discSize - startedHourDiscordSize);
			follows.put(lastCheckedHour + "", twitchFollows - startedHourTwitchFollowers);
		}
	}
	
	private static String applyStaff(String desc)
	{
		List<User> listed = new ArrayList<User>();
		
		boolean found = false;
		boolean extraSpace = false;
		for (User u : MainDiscordBot.api().getUsers())
		{
			if (!listed.contains(u) && u.getRoles(MainDiscordBot.get404Discord()).contains(MainDiscordBot.getOwnerRole()))
			{
				found = true;
				String name = u.getMentionTag();
				if (u == MainDiscordBot.get404Discord().getOwner())
					name += " :crown:";
				if (u.getNickname(MainDiscordBot.get404Discord()).isPresent())
					desc += "**" + name + "** (*" + u.getName() + "*) - **Owner**" + '\n';
				else
					desc += "**" + name + "** - **Owner**" + '\n';
				listed.add(u);
			}
		}
		
		if (!found)
		{
			desc += "*No owners*" + '\n';
			extraSpace = true;
		}
		else
			desc += "" + '\n';
		
		found = false;
		for (User u : MainDiscordBot.api().getUsers())
		{
			if (!listed.contains(u) && u.getRoles(MainDiscordBot.get404Discord()).contains(MainDiscordBot.getAdminRole()))
			{
				if (extraSpace)
				{
					desc += "" + '\n';
					extraSpace = false;
				}
				found = true;
				String name = u.getMentionTag();
				if (u == MainDiscordBot.get404Discord().getOwner())
					name += " :crown:";
				if (u.getNickname(MainDiscordBot.get404Discord()).isPresent())
					desc += "**" + name + "** (*" + u.getName() + "*) - **Super Admin**" + '\n';
				else
					desc += "**" + name + "** - **Super Admin**" + '\n';
				listed.add(u);
			}
		}
		
		if (!found)
		{
			desc += "*No super administrators*" + '\n';
			extraSpace = true;
		}
		else
			desc += "" + '\n';
		
		found = false;
		for (User u : MainDiscordBot.api().getUsers())
		{
			if (!listed.contains(u) && u.getRoles(MainDiscordBot.get404Discord()).contains(MainDiscordBot.getDevRole()))
			{
				if (extraSpace)
				{
					desc += "" + '\n';
					extraSpace = false;
				}
				found = true;
				String name = u.getMentionTag();
				if (u == MainDiscordBot.get404Discord().getOwner())
					name += " :crown:";
				if (u.getNickname(MainDiscordBot.get404Discord()).isPresent())
					desc += "**" + name + "** (*" + u.getName() + "*) - Administrator" + '\n';
				else
					desc += "**" + name + "** - Administrator" + '\n';
				listed.add(u);
			}
		}
		
		if (!found)
		{
			desc += "*No administrators*" + '\n';
			extraSpace = true;
		}
		else
			desc += "" + '\n';
		
		found = false;
		for (User u : MainDiscordBot.api().getUsers())
		{
			if (!listed.contains(u) && u.getRoles(MainDiscordBot.get404Discord()).contains(MainDiscordBot.getSuperModRole()))
			{
				if (extraSpace)
				{
					desc += "" + '\n';
					extraSpace = false;
				}
				found = true;
				String name = u.getMentionTag();
				if (u == MainDiscordBot.get404Discord().getOwner())
					name += " :crown:";
				if (u.getNickname(MainDiscordBot.get404Discord()).isPresent())
					desc += "**" + name + "** (*" + u.getName() + "*) - Super Moderator" + '\n';
				else
					desc += "**" + name + "** - Super Moderator" + '\n';
				listed.add(u);
			}
		}
		
		if (!found)
		{
			desc += "*No super moderators*" + '\n';
			extraSpace = true;
		}
		else
			desc += "" + '\n';
		
		found = false;
		for (User u : MainDiscordBot.api().getUsers())
		{
			if (!listed.contains(u) && u.getRoles(MainDiscordBot.get404Discord()).contains(MainDiscordBot.getSeniorModRole()))
			{
				if (extraSpace)
				{
					desc += "" + '\n';
					extraSpace = false;
				}
				found = true;
				String name = u.getMentionTag();
				if (u == MainDiscordBot.get404Discord().getOwner())
					name += " :crown:";
				if (u.getNickname(MainDiscordBot.get404Discord()).isPresent())
					desc += "**" + name + "** (*" + u.getName() + "*) - Senior Moderator" + '\n';
				else
					desc += "**" + name + "** - Senior Moderator" + '\n';
				listed.add(u);
			}
		}
		
		if (!found)
		{
			desc += "*No senior moderators*" + '\n';
			extraSpace = true;
		}
		else
			desc += "" + '\n';
		
		desc += " -> **You should contact the people listed below if any issues arise!** <-";
		extraSpace = true;
		found = false;
		for (User u : MainDiscordBot.api().getUsers())
		{
			if (!listed.contains(u) && u.getRoles(MainDiscordBot.get404Discord()).contains(MainDiscordBot.getModRole()))
			{
				if (extraSpace)
				{
					desc += "" + '\n';
					extraSpace = false;
				}
				found = true;
				String name = u.getMentionTag();
				if (u == MainDiscordBot.get404Discord().getOwner())
					name += " :crown:";
				if (u.getNickname(MainDiscordBot.get404Discord()).isPresent())
					desc += "**" + name + "** (*" + u.getName() + "*) - Moderator" + '\n';
				else
					desc += "**" + name + "** - Moderator" + '\n';
				listed.add(u);
			}
		}
		
		if (!found)
		{
			desc += "*No moderators*" + '\n';
			extraSpace = true;
		}
		else
			desc += "" + '\n';
		
		int jMods = 0;
		for (User u : MainDiscordBot.api().getUsers())
		{
			if (!listed.contains(u) && u.getRoles(MainDiscordBot.get404Discord()).contains(MainDiscordBot.getJuniorModRole()))
			{
				if (extraSpace)
				{
					desc += "" + '\n';
					extraSpace = false;
				}
				listed.add(u);
				jMods++;
			}
		}
		desc += "**[" + jMods + "]** Junior Moderators" + '\n';

		return desc;
	}

	private static void checkUpSubs(int subs)
	{
		int lastGoal = MainData.get().getInt("last-sub-thousands");
		int current = (int)(subs / 10000);

		if (lastGoal == 0)
		{
			MainData.get().set("last-sub-thousands", current);
			MainData.save();
			lastGoal = current;
		}

		if (current > lastGoal && GUIMain.testMode == false)
		{
			char[] chars = ((current * 10000) + "").toCharArray();
			int size = ((current * 10000) + "").length();
			String sSubs = "";
			
			for (char c : chars)
			{
				size--;
				sSubs += c;
				
				if (size != 0 && size % 3 == 0)
					sSubs += ",";
			}
			
			MainDiscordBot.getNewsChannel().sendMessage("@everyone We've reached " + sSubs + " subscribers on YouTube!!!");
			MainData.get().set("last-sub-thousands", current);
			MainData.save();
		}
	}
	
	private static Message fetchMessage()
	{
		Message m = null;
		File f = new File("404bot-data.txt");

		if (!f.exists())
			try {
				f.createNewFile();
			} catch (IOException ex) {
				MainDiscordBot.getNeuro().sendMessage(ex.getMessage());
				ex.printStackTrace();
			}
		
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedReader reader = Files.newBufferedReader(f.toPath(), charset)) {
		    String line = null;
		    String id = "";
		    while ((line = reader.readLine()) != null) {
		        if (line.startsWith("msg-id: "))
		        {
		        	id = line.replace("msg-id: ", "");
		        	m = MainDiscordBot.getInfoChannel().getMessageById(id).get();
		        }
		    }
		} catch (IOException | InterruptedException | ExecutionException ex) {
			return null;
		}
		
		return m;
	}
	
	private static Message createMessage()
	{
		Message m = null;
		String id = null;
		try {
			m = MainDiscordBot.getInfoChannel().sendMessage("<placeholder>").get();
			id = m.getIdAsString();
		} catch (Exception e) {
			MainDiscordBot.getNeuro().sendMessage(e.getMessage());
			e.printStackTrace();
		}
		
		if (m == null || id == null)
		{
			MainDiscordBot.getNeuro().sendMessage("Couldn't send a message to the #info channel!");
			return null;
		}
		
		File f = new File("404bot-data.txt");
		
		if (f.exists())
			f.delete();

		try {
			f.createNewFile();
		} catch (IOException ex) {
			MainDiscordBot.getNeuro().sendMessage(ex.getMessage());
			ex.printStackTrace();
		}
		
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedWriter writer = Files.newBufferedWriter(f.toPath(), charset)) {
		    writer.write("msg-id: " + id + '\n');
		} catch (IOException ex) {
			MainDiscordBot.getNeuro().sendMessage(ex.getMessage());
			ex.printStackTrace();
		}
		
		return m;
	}

}

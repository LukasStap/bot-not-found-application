package com.NeuroZap.GUI.Code.PointsSystem;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.NeuroZap.GUI.GUIMain;
import com.NeuroZap.GUI.Code.Twitch.MainTwitchBot;
import com.cavariux.twitchirc.Chat.User;

public class PointsSystem {
	
	public static void init()
	{
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		
		scheduler.scheduleAtFixedRate(new Runnable() {
		public void run() { 
			
			boolean contains404 = false;

			for (User u : MainTwitchBot.bot().channel.getViewers())
			{
				if (u.toString().equalsIgnoreCase("the404studios"))
					contains404 = true;
			}
			
			if (contains404)
			{
				Random rand = new Random();
				for (User u : MainTwitchBot.bot().channel.getViewers())
				{
					String name = u.toString();
					if (name.equalsIgnoreCase("streamelements") || name.equalsIgnoreCase("stay_hydrated_bot") || name.equalsIgnoreCase("the404bot") ||
							name.equalsIgnoreCase("the404studios"))
						continue;
					//if (u.isSubscribed(MainTwitchBot.bot().channel, ""))
					//	GUIMain.points().addPoints(u, rand.nextInt(4) + 2);
					//else
						GUIMain.points().addPoints(u, rand.nextInt(3) + 1);
				}
			}
			
		}}, 2, 2, TimeUnit.MINUTES);
	}

}

package com.NeuroZap.GUI.Code.Twitch;

import java.util.HashMap;
import java.util.Map;

import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;

public class SongRequests {
	
	public static Map<User, String> requests = new HashMap<User, String>();
	private static int max = 10;
	
	private static boolean validateUrl(String url)
	{
		if (url == null) {
	        return false;
	    }
		
	    return url.matches("^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+");
	}
	
	public static void addSongRequest(User u, Channel ch, String url)
	{
		if (requests.containsKey(u))
		{
			MainTwitchBot.bot().sendMessage("@" + u.toString() + " you are already queued up.", ch);
			return;
		}
		if (requests.size() >= max)
		{
			MainTwitchBot.bot().sendMessage("@" + u.toString() + " cannot add more requests, maximum reached (" + max + ")", ch);
			return;
		}
		if (!validateUrl(url))
		{
			MainTwitchBot.bot().sendMessage("@" + u.toString() + " that link isn't a valid youtube URL.", ch);
			return;
		}
		for (String vUrl : requests.values())
		{
			if (vUrl.equalsIgnoreCase(url))
			{
				MainTwitchBot.bot().sendMessage("@" + u.toString() + " seems like that URL has already been added.", ch);
				return;
			}
		}
		requests.put(u, url);
		MainTwitchBot.bot().sendMessage("@" + u.toString() + " queued up!", ch);
		// MainDiscordBot.getSongRequestsChannel().sendMessage("By " + u.toString() + ": " + url);
	}
}

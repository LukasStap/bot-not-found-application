package com.NeuroZap.GUI.Code.Twitch;

import java.awt.EventQueue;

import com.NeuroZap.GUI.Code.Twitch.Commands.TBCommands;
import com.NeuroZap.GUI.Code.Twitch.Commands.TBotCommand;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;
import com.cavariux.twitchirc.Core.TwitchBot;

public class MainTwitchBot extends TwitchBot {
	
	private static MainTwitchBot bot;

	public Channel channel;
	
	public static boolean testMode = false;
	
	//private List<String> randMessages = new ArrayList<String>();
	
	public MainTwitchBot()
	{
		if (bot != null)
		{
			bot.stop();
		}
		bot = this;
		
		bot.setUsername("the404bot");
		bot.setOauth_Key("oauth:u2oe1bylo91al1cqe3uqz9amx13ggp");
		bot.setClientID("pifs1meh7kri8yga3uldlhu3ziok0h");
		bot.connect();

		if (testMode)
			channel = bot.joinChannel("#neurozap");
		else
			channel = bot.joinChannel("#the404studios");

		reinitMessages();

		EventQueue.invokeLater(new Runnable() { public void run() {
			bot.start();
		}});
	}

	private void reinitMessages()
	{
		/*if (randMessages.isEmpty())
		{
			ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
			scheduler.scheduleAtFixedRate(new Runnable() {
				public void run() {
					if (!channel.isLive())
						return;
					
					if (randMessages.isEmpty())
						reinitMessages();
					
					Random rand = new Random();
					
					String msg = randMessages.get(rand.nextInt(randMessages.size()));
					
					bot.sendMessage(msg, channel);
					randMessages.remove(msg);
				}}, 1, 5, TimeUnit.MINUTES);
			
			randMessages.clear();
			randMessages.add("Check out our discord! http://discord.gg/Q7KBA96");
			randMessages.add("The sponsor of this stream - !1337");
			randMessages.add("404 streams on youtube too. Check it out! https://www.youtube.com/channel/UCrNTseMRDdl089VTnEpQnrg");
			randMessages.add("We have some random commands, check out !!help");
			randMessages.add("Subbing & donating supports 404 a ton! Massive thanks to anybody who donated or are planning on doing it! <3 BlessRNG");
			randMessages.add("404 has a SoundCloud! https://soundcloud.com/mason-roberts-23");
		}*/
	}
	
	public static void initializeTwitchBot()
	{
		bot = new MainTwitchBot();
	}
	
	public static boolean initialized()
	{
		return bot != null;
	}
	
	public static MainTwitchBot bot()
	{
		return bot;
	}

	@Override
	public void onMessage(User user, Channel channel, String message)
	{
		for (TBotCommand cmd : TBCommands.getCmds())
		{
			if (cmd.isCommand())
				continue;
			
			cmd.onCommand(user, channel, message);
		}
	}
	
	@Override
	public void onCommand(User user, Channel channel, String command)
	{
		for (TBotCommand cmd : TBCommands.getCmds())
		{
			if (!cmd.isCommand())
				continue;
			
			cmd.onCommand(user, channel, command);
		}
	}
}

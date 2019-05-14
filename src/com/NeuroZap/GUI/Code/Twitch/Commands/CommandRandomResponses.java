package com.NeuroZap.GUI.Code.Twitch.Commands;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.NeuroZap.GUI.Code.Twitch.MainTwitchBot;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;

public class CommandRandomResponses extends TBotCommand {

	private boolean cooldown = false;
	
	@Override
	public String name() {
		return null;
	}

	@Override
	public String helpText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCommand() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean modCommand() {
		return false;
	}
	
	@Override
	public int requiredValidationLevel() {
		return 0;
	}

	@Override
	public void execute(User user, Channel channel, String message) {
		if (cooldown || user.toString().equalsIgnoreCase("the404bot"))
			return;
		
		if (message.equalsIgnoreCase("binkers") || message.equalsIgnoreCase("binkie") || 
				message.toLowerCase().startsWith("binkers") || message.toLowerCase().startsWith("binkie") ||
				message.toLowerCase().contains("binkers") || message.toLowerCase().contains("binkie"))
		{
			Random rand = new Random();
			int ch = rand.nextInt(3) + 1;
			
			String msg = "Binkers ";
			if (rand.nextBoolean())
				msg = "Wifu ";

			if (ch == 1)
				msg += "<3";
			if (ch == 2)
				msg += "PogChamp";
			if (ch == 3)
				msg += "Kreygasm";
			
			MainTwitchBot.bot().sendMessage(msg, channel);
		}
		
		else if (message.toLowerCase().startsWith("yuh"))
			MainTwitchBot.bot().sendMessage("Yuhh", channel);
		
		else if (!message.equalsIgnoreCase("ayo") && message.toLowerCase().startsWith("ay"))
			MainTwitchBot.bot().sendMessage("Ayye", channel);
		
		else if (message.equalsIgnoreCase("oof"))
			MainTwitchBot.bot().sendMessage("oof.", channel);

		else if (message.equalsIgnoreCase("succ"))
			MainTwitchBot.bot().sendMessage("the SUCC BlessRNG", channel);
		
		else if (message.equalsIgnoreCase("swiggity swooty"))
			MainTwitchBot.bot().sendMessage("commin for da booty!", channel);
		
		else if (message.equalsIgnoreCase("hold f to pay respects") || message.equalsIgnoreCase("f to pay respects") || message.equalsIgnoreCase("f"))
			MainTwitchBot.bot().sendMessage("F", channel);
		
		else if (message.equalsIgnoreCase("daddy"))
			MainTwitchBot.bot().sendMessage("daddy 404 Kreygasm <3", channel);
		
		else if (message.equalsIgnoreCase("Kreygasm"))
			MainTwitchBot.bot().sendMessage("Kreygasm", channel);
		
		else if (message.equalsIgnoreCase("lit") || message.equalsIgnoreCase("pogchamp") || 
				message.toLowerCase().contains("CurseLit") || message.toLowerCase().contains("TwitchLit") || message.equalsIgnoreCase("poggers") || message.toLowerCase().contains("the404404fire"))
			MainTwitchBot.bot().sendMessage("CurseLit TwitchLit CurseLit TwitchLit", channel);
		
		else if (message.contains("1337"))
			MainTwitchBot.bot().sendMessage("24k !1337 WOOOOOO SPONSOR SwiftRage", channel);
		
		else if (message.equalsIgnoreCase("i wanna die") || message.equalsIgnoreCase("i want to die"))
			MainTwitchBot.bot().sendMessage("same", channel);
		
		cooldown = true;
		
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.schedule(new Runnable() {
			public void run() { 
				cooldown = false;
			}}, 15, TimeUnit.SECONDS);
	}

}

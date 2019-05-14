package com.NeuroZap.GUI.Code.Twitch.Commands;

import java.util.Random;

import com.NeuroZap.GUI.GUIMain;
import com.NeuroZap.GUI.Code.Twitch.MainTwitchBot;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;

public class CommandGamble extends TBotCommand {

	@Override
	public String name() {
		return "gamble";
	}

	@Override
	public String helpText() {
		// TODO Auto-generated method stub
		return "**!!gamble** - Gamble to get extra points.";
	}

	@Override
	public boolean isCommand() {
		return true;
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
		
		if (message.startsWith("!gamble"))
		{
			try
			{
				int points = Integer.parseInt(message.split(" ")[1]);
				int playerPoints = GUIMain.points().getPoints(user);
				
				if (playerPoints < points)
				{
					MainTwitchBot.bot().sendMessage("Not enough points! (" + points + " > " + playerPoints + ")", channel);
					return;
				}
				
				Random rand = new Random();
				
				if (rand.nextInt(100) < 66)
				{
					MainTwitchBot.bot().sendMessage("Gamble lost!", channel);
					if (rand.nextBoolean())
					{
						int regain = rand.nextInt(points);
						MainTwitchBot.bot().sendMessage("You did regain " + regain + " points.", channel);
						GUIMain.points().addPoints(user, -(points - regain));
					}
					return;
				}
				
				int gain = rand.nextInt((int)(points / 2)) + (int)(points / 2) + 1;
				MainTwitchBot.bot().sendMessage("You won " + gain + " points!", channel);
				GUIMain.points().addPoints(user, gain);
			}
			catch (Exception ex)
			{
				MainTwitchBot.bot().sendMessage("Correct command - !!gamble <amount>", channel);
			}
		}
	}

}

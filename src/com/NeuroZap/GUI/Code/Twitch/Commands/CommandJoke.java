package com.NeuroZap.GUI.Code.Twitch.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.NeuroZap.GUI.Code.Twitch.MainTwitchBot;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;

public class CommandJoke extends TBotCommand {

	private List<String> jokes = new ArrayList<String>();
	private boolean cooldown = false;
	
	@Override
	public String name() {
		return "joke";
	}

	@Override
	public String helpText() {
		// TODO Auto-generated method stub
		return "**!!joke** - Tells a random joke.";
	}

	@Override
	public boolean isCommand() {
		return true;
	}
	
	@Override
	public boolean modCommand() {
		return true;
	}
	
	@Override
	public int requiredValidationLevel() {
		return 3;
	}

	@Override
	public void execute(User user, Channel channel, String message) {
		
		if (message.startsWith("!joke") && !cooldown)
		{
			if (jokes.size() == 0)
				init();
			
			Random rand = new Random();
			
			if (jokes.size() != 0)
				MainTwitchBot.bot().sendMessage(jokes.get(rand.nextInt(jokes.size())), MainTwitchBot.bot().channel);
			
			cooldown = true;
			
			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
			scheduler.schedule(new Runnable() {
				public void run() { 
					cooldown = false;
				}}, 30, TimeUnit.SECONDS);
		}
	}
	
	private void init()
	{
		jokes.add("@%p LUL");
		jokes.add("\"I can't rap\" - The404Studios");
		jokes.add("What is the difference between a snowman and a snowwoman? Snowballs.");
		jokes.add("How did the tree go on the internet? It logged in.");
		jokes.add("A boy asks Dad \"Dad, why is my sister called Paris?\" Dad replies \"Because she was conceived in Paris.\"" + 
				"The boy says, \"Ahh, thanks Dad.\" \"You're welcome, Backseat.\"");
		jokes.add("What did snoop dogg say when lil wayne tried to buy a 2pac of m&ms for 50 cents? that is ludacris");
		jokes.add("Why does snoop dogg carry an umbrella? fo' drizzle.");
		jokes.add("Horse walks into the bar. The bartender asks 'why the long face?'");
		jokes.add("I used to like my neighbors, until they put a password on their Wi-Fi.");
		jokes.add("What did the fish say when he swam into a wall? -- Damn");
		jokes.add("Why did the duck go to rehab? Because he was a quack addict");
		jokes.add("I say no to alcohol, it just doesn't listen.");
	}

}

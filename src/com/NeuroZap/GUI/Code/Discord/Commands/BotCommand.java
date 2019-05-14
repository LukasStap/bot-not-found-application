package com.NeuroZap.GUI.Code.Discord.Commands;

import java.util.List;

import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.events.message.MessageCreateEvent;
import de.btobastian.javacord.listeners.message.MessageCreateListener;

public abstract class BotCommand implements IBotCommand, MessageCreateListener {
	
	@Override
	public void onMessageCreate(MessageCreateEvent ev)
	{
		Message message = ev.getMessage();
		DiscordApi api = ev.getApi();
		if (message.getAuthor().asUser().get().equals(api.getYourself()) || (!message.getPrivateChannel().isPresent() 
				/*&& !message.getChannel().equals(MainDiscordBot.getTestChannel())*/ 
				&& !message.getChannel().equals(MainDiscordBot.getBotCommandsChannel())
				&& !message.getChannel().equals(MainDiscordBot.getHQAppManagementChannel()))) return;

		String msg = message.getContent();
		if (!msg.startsWith("!!" + name())) return;
		
		User sender = ev.getMessage().getAuthor().asUser().get();
		
		//Role mod = MainDiscordBot.api().getRoleById("300761787635335169").get();
		//Role admin = MainDiscordBot.api().getRoleById("294187804903342083").get();
		//Role owner = MainDiscordBot.api().getRoleById("309152254844207104").get();

		//if (requiredValidationLevel() > 0)
		//{
			//List<Role> roles = sender.getRoles(MainDiscordBot.get404Discord());
			
			//if (roles.contains(owner))
			//{
				execute(api, message);
			//}
		//}
		//else
			//execute(api, message);
	}	
	
	public static int getValidationLevel(User u)
	{
		Role mod = MainDiscordBot.api().getRoleById("503227649833959454").get();
		Role admin = MainDiscordBot.api().getRoleById("294187804903342083").get();
		Role owner = MainDiscordBot.api().getRoleById("309152254844207104").get();
		
		List<Role> roles = u.getRoles(MainDiscordBot.get404Discord());
		
		if (u == MainDiscordBot.getNeuro() || u == MainDiscordBot.get404() || roles.contains(owner))
			return 3;
		
		if (roles.contains(admin))
			return 2;
		
		if (roles.contains(mod))
			return 1;
		
		return 0;
	}

}

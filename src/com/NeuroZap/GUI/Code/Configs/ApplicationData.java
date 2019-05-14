package com.NeuroZap.GUI.Code.Configs;

import java.io.File;
import java.io.IOException;

import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;

import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;

import de.btobastian.javacord.entities.User;

public class ApplicationData {

	public static FileConfiguration getAppConfig(User u) {
		FileConfiguration config = null;
		File file = new File("Applications/", u.getIdAsString() + ".yml");
	    config = YamlConfiguration.loadConfiguration(file);

	    return config;
	}
	
	public static FileConfiguration getAppConfig(String id) {
		FileConfiguration config = null;
		File file = new File("Applications/", id + ".yml");
	    config = YamlConfiguration.loadConfiguration(file);

	    return config;
	}
	
	public static void saveAppConfig(User u, FileConfiguration config) {
		File file = new File("Applications/", u.getIdAsString() + ".yml");
	    try {
	        config.save(file);
	    } catch (IOException ex) {
	        MainDiscordBot.getHQAppLogChannel().sendMessage("IOException whilst saving app file: " + ex.getMessage());
	    }
	}
	
	public static void saveAppConfig(String id, FileConfiguration config) {
		File file = new File("Applications/", id + ".yml");
	    try {
	        config.save(file);
	    } catch (IOException ex) {
	        MainDiscordBot.getHQAppLogChannel().sendMessage("IOException whilst saving app file: " + ex.getMessage());
	    }
	}
	
	public static boolean deleteAppConfig(User u) {
		File file = new File("Applications/", u.getIdAsString() + ".yml");
		
		return file.delete();
	}
	
	public static boolean deleteAppConfig(String id) {
		File file = new File("Applications/", id + ".yml");
		
		return file.delete();
	}
	
}

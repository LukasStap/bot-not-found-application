package com.NeuroZap.GUI.Code.Configs;

import java.io.File;
import java.io.IOException;

import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;

public class MainData {

	private static FileConfiguration customConfig = null;
	private static File customConfigFile = null;


	public static void reload() {

	    if (customConfigFile == null) {
	    	customConfigFile = new File("404config.yml");
	    	System.out.print(customConfigFile.getAbsolutePath());
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	}
	

	public static FileConfiguration get() {
	    if (customConfig == null) {
	        reload();
	    }
	    return customConfig;
	}

	public static void save() {

	    if (customConfig == null || customConfigFile == null) {
	    	return;
	    }
	    try {
	        get().save(customConfigFile);
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}
	
}

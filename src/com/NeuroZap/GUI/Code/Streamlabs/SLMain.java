package com.NeuroZap.GUI.Code.Streamlabs;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.streamlabs4j.api.StreamlabsApi;
import com.github.twitch4j.streamlabs4j.api.StreamlabsApiBuilder;

public class SLMain extends EventManager {
	
	private static StreamlabsApi slapi = null;

	public static void initialize()
	{
		StreamlabsApiBuilder slb = StreamlabsApiBuilder.builder();
		
		slapi = slb
		.withClientId("")
		.withClientSecret("")
		.withEventManager(new SLMain())
		.build();
	}
	
	public static StreamlabsApi api()
	{
		return slapi;
	}

}

package com.NeuroZap.GUI;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;
import com.NeuroZap.GUI.Code.PointsSystem.Processing;
import com.NeuroZap.GUI.Code.Twitch.MainTwitchBot;
import com.NeuroZap.GUI.Code.YouTube.MainYouTubeBot;

public class GUIMain {

	private String version = "1.1";
	
	private static GUIMain inst;
	
	private JFrame frame;
	private JLabel mainLabel;
	
	private static Processing pd;
	
	public static boolean testMode = false;
	private static boolean outToFile = true;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (outToFile)
					{
						PrintStream fileOut = new PrintStream("./out.txt");
						PrintStream fileErr = new PrintStream("./err.txt");
						
						System.setOut(fileOut);
						System.setErr(fileErr);
					}
					
					GUIMain window1 = new GUIMain();
					if (testMode)
						window1.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUIMain() {
		inst = this;
		try {
			MainYouTubeBot.authorize();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainTwitchBot.initializeTwitchBot();
		MainDiscordBot.initializeDiscordBot();
		
		//pd = new Processing();
		//PointsSystem.init();
		
		if (testMode)
			initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 208, 160);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("404 Discord <-> Twitch Bots v" + version);

		mainLabel = new JLabel("Bot started");
		mainLabel.setBounds(57, 46, 72, 16);
		frame.getContentPane().add(mainLabel);
	}
	
	public JLabel getMainLabel()
	{
		return mainLabel;
	}

	public static GUIMain inst()
	{
		return inst;
	}
	
	public static Processing points()
	{
		return pd;
	}
}

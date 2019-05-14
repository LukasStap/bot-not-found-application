package com.NeuroZap.GUI.Code.PointsSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.cavariux.twitchirc.Chat.User;

public class Processing {
	
	private class Data {
		
		private String username;
		private String password;

		private String database = "FouroFour_Points";
		
		public boolean enabled = false;

		private String url = "jdbc:mysql://localhost:3306/";

		public Data(String username, String password) {
			this.username = username;
			this.password = password;
		}
		
		public void firstConnect() {
			try {
				Connection con = DriverManager.getConnection(url + "?", username, password);

				if (con == null)
					return;

				ResultSet resultSet = con.getMetaData().getCatalogs();

				// iterate each catalog in the ResultSet
				while (resultSet.next()) {
					// Get the database name, which is at position 1
					String databaseName = resultSet.getString(1);
					if (databaseName.equalsIgnoreCase(database))
						return;
				}

				resultSet.close();

				Statement stat = con.createStatement();

				try {
					stat.executeUpdate("CREATE DATABASE " + database);
					createTables();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public Connection connect() {
			try {
				Connection c = DriverManager.getConnection(url + database, username, password);
				enabled = true;
				return c;
			} catch (SQLException e) {
				e.printStackTrace();
				enabled = false;
				return null;
			}
		}
		
		public void refreshConn()
		{
			Connection conn = getConnection();
			
			if (conn == null)
				return;
		
			try {
				Statement stat = conn.createStatement();
				stat.executeQuery("SELECT COUNT(ID) FROM user;");
				data.resetCount();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		private void createTables() {
			Connection conn = connect();

			if (conn == null)
				return;

			try {
				Statement stat = conn.createStatement();

				String defInt = "INTEGER DEFAULT 0,";
				// String defBool = "BOOLEAN DEFAULT FALSE,";

				stat.executeUpdate("CREATE TABLE user" + "(ID VARCHAR(255) not NULL," + "Points " + defInt
						+ "PRIMARY KEY (ID))");
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public void personInsert(String id) {
			Connection conn = permConn;

			if (conn == null)
				return;

			try {
				Statement stat = conn.createStatement();
				ResultSet set = stat.executeQuery("SELECT COUNT(ID) FROM user WHERE ID = '" + id + "';");
				resetCount();
				if (set.next()) {
					if (set.getInt("COUNT(ID)") != 0)
						return;
					stat.executeUpdate("INSERT INTO user (ID) VALUES('" + id + "');");
				} else {
					stat.executeUpdate("INSERT INTO user (ID) VALUES('" + id + "');");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		
		public void setPoints(String id, int points)
		{
			Connection conn = permConn;

			if (conn == null)
				return;

			try {
				Statement stat = conn.createStatement();
				stat.executeUpdate("UPDATE user SET Points = " + points + " WHERE ID = '" + id + "';");
				resetCount();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public int getPoints(String id)
		{
			Connection conn = permConn;

			if (conn == null)
				return 0;

			try {
				Statement stat = conn.createStatement();
				ResultSet points = stat.executeQuery("SELECT Points FROM user WHERE ID = '" + id + "';");
				resetCount();
				if (points.next()) {
					return points.getInt("Points");
				} else {
					personInsert(id);
					return 0;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return 0;
		}
		
		public boolean personExists(String id)
		{
			Connection conn = permConn;

			if (conn == null)
				return false;

			try {
				Statement stat = conn.createStatement();
				ResultSet points = stat.executeQuery("SELECT Points FROM user WHERE ID = '" + id + "';");
				resetCount();
				return points.next();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
		}

		private void resetCount()
		{
			setTimer(60);
		}

	}
	
	// -----------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static Map<User, Integer> points = new HashMap<User, Integer>();
	public static Map<String, Integer> sPoints = new HashMap<String, Integer>();
	
	private Data data;
	private Connection permConn;

	private static int timer = 60;
	
	public Processing()
	{
		String username = "404bot";
		String password = "AppleSauce79";
		
		//String username = "root";
		//String password = "advanced";
		
		data = new Data(username,password);
		
		data.firstConnect();
		
		permConn = data.connect();

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() { 
                timer--;
                if(timer==0)
                	data.refreshConn();
			}}, 1, 1, TimeUnit.SECONDS);

		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() { 
			saveProgress();
			}}, 1, 1, TimeUnit.MINUTES);
	}
	
	public static void setTimer(int time) {
		timer = time;
	}

	public void addPoints(User u, int toAdd)
	{
		if (!points.containsKey(u))
		{
			addUser(u);
		}
			
		points.put(u, points.get(u) + toAdd);
	}

	public Connection getConnection()
	{
		return permConn;
	}
	
	public int getPoints(User u)
	{
		try
		{
			return points.get(u);
		}
		catch (Exception ex)
		{
			try
			{
				addUser(u);
				return points.get(u);
			}
			catch (Exception ex1)
			{
				return 0;
			}
		}
	}
	
	public int getPoints(String user)
	{
		if (sPoints.containsKey(user))
			return sPoints.get(user);
		
		return data.getPoints(user);
	}

	public void addUser(User u)
	{
		points.put(u, data.getPoints(u.toString()));
		sPoints.put(u.toString(), data.getPoints(u.toString()));
	}

	public void flushUserProgress(User u)
	{
		if (!points.containsKey(u))
			return;
		
		int toSet = points.get(u);

		data.setPoints(u.toString(), toSet);
		
		points.remove(u);
		sPoints.remove(u.toString());
	}
	
	public boolean userExistsInDatabase(String id)
	{
		return data.personExists(id);
	}
	
	public void saveProgress()
	{
		for (User u : points.keySet())
		{
			int progress = points.get(u);

			data.setPoints(u.toString(), progress);

		}
		points.clear();
		sPoints.clear();
	}

	public boolean isEnabled()
	{
		return data.enabled;
	}
}

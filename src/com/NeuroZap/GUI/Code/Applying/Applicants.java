package com.NeuroZap.GUI.Code.Applying;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.NeuroZap.GUI.Code.Configs.ApplicationData;
import com.NeuroZap.GUI.Code.Configs.MainData;

import de.btobastian.javacord.entities.User;

/** 
 * This class consists of static methods that relate to applicants themselves.
 */
public class Applicants {
	
	private static List<Applicant> applicants = new ArrayList<Applicant>();
	public static List<Applicant> confirmStopApplying = new ArrayList<Applicant>();
	
	public static boolean isApplying(User u)
	{
		for (Applicant appl : applicants)
		{
			if (appl.getId().equalsIgnoreCase(u.getIdAsString()))
				return true;
		}
		
		return false;
	}
	
	public static boolean hasApplied(User u)
	{
		return hasApplied(u.getIdAsString());
	}
	
	public static boolean hasApplied(String id)
	{
		if (getIdsWhoApplied().contains(id) || getOnHoldIds().contains(id) ||
				getAcceptedIds().contains(id) || getDeniedIds().contains(id))
		{
			String[] dateData = ApplicationData.getAppConfig(id).getString("date").split(" "); // Sun May 12 14:55:03 CEST 2019
			String month = dateData[1],
					day = dateData[2],
					year = dateData[5];
			
			return !enoughTimePassed(Integer.parseInt(year), month, Integer.parseInt(day));
		}
		return false;
	}
	
	private static boolean enoughTimePassed(int year, String month, int day)
	{
		Calendar cal = Calendar.getInstance();
		
		int nMonth = monthToInt(month) + 2;
		
		int currentYear = cal.get(Calendar.YEAR);
		int nCurrentMonth = Calendar.getInstance().get(Calendar.MONTH);
		int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

			sdf.setTimeZone(cal.getTimeZone());
			
			String s = sdf.parse(sdf.format(cal.getTime())).toString();
			nCurrentMonth = monthToInt(s.split("")[1]);
		} catch (ParseException e) {
			e.printStackTrace();
			return true;
		}
		
		if (nMonth > 12)
		{
			year++;
			nMonth -= 12;
		}
		
		return (year < currentYear || 
			(year == currentYear && nMonth < nCurrentMonth) || 
			(year == currentYear && nMonth == nCurrentMonth && day < currentDay));
	}
	
	private static int monthToInt(String month)
	{
		switch (month.toLowerCase())
		{
			case "jan":
				return 1;
			case "feb":
				return 2;
			case "mar":
				return 3;
			case "apr":
				return 4;
			case "may":
				return 5;
			case "jun":
				return 6;
			case "jul":
				return 7;
			case "aug":
				return 8;
			case "sep":
				return 9;
			case "oct":
				return 10;
			case "nov":
				return 11;
			case "dec":
				return 12;
			default:
				return -1;
		}
	}
	
	public static Applicant getApplicant(User u)
	{
		for (Applicant appl : applicants)
		{
			if (appl.getId().equalsIgnoreCase(u.getIdAsString()))
				return appl;
		}
		
		return null;
	}
	
	public static void addApplicant(Applicant appl)
	{
		applicants.add(appl);
	}
	
	public static void removeApplicant(Applicant appl)
	{
		applicants.remove(appl);
		confirmStopApplying.remove(appl);
	}
	
	public static List<String> getIdsWhoApplied()
	{
		return MainData.get().getStringList("applications.applied");
	}
	
	public static List<String> getOnHoldIds()
	{
		return MainData.get().getStringList("applications.on-hold");
	}
	
	public static List<String> getAcceptedIds()
	{
		return MainData.get().getStringList("applications.accepted");
	}
	
	public static List<String> getDeniedIds()
	{
		return MainData.get().getStringList("applications.denied");
	}
	
}

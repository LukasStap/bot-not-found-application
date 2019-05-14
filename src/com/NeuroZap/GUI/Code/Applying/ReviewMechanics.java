package com.NeuroZap.GUI.Code.Applying;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.NeuroZap.GUI.Code.Configs.MainData;
import com.NeuroZap.GUI.Code.Discord.MainDiscordBot;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

public class ReviewMechanics {
	
	private static final int acceptsNeeded = 2;
	private static final int deniesNeeded = 2;
	
	public static Application currentlyReviewing = null;
	
	public static boolean revealed = false;
	
	public static CompletableFuture<Message> currentReviewMessage_1 = null;
	public static CompletableFuture<Message> currentReviewMessage_2 = null;
	
	private static List<String> confirms = new ArrayList<String>();
	
	public static void onReviewCommand(User u, TextChannel tc, String[] args)
	{
		if (args.length == 2)
		{
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("**Application Review**");
			eb.setColor(Color.CYAN);
			
			String desc = "This is the application review mode. Here are the commands that you should note:" + '\n' + '\n';
			desc += "**!!apps review start** - Picks the oldest submitted application and starts the review process." + '\n' + '\n';
			desc += "**!!apps review start <ID>** - Shows you the application of the specific ID you used. **Please be advised that this "
					+ "command only works for applications who have been put on-hold, accepted or denied.**" + '\n' + '\n';
			desc += "**!!apps review reveal** - Reveals the username and ID of the application that is currently being reviewed. "
					+ "**Use this only after evaluating the app.**" + '\n' + '\n';
			desc += "**!!apps review comment <comment message>** - Leaves a comment on the application You are reviewing for others to check, if You are going to leave it on-hold." + '\n' + '\n';
			desc += "**!!apps review cancel** - Cancels the reviewing process. Use this if you cannot review the application in time/have other issues. "
					+ "**If you finished reviewing the app but cannot make a decision yet, look below for a 'stop' command instead to put it on-hold.**" + '\n' + '\n';
			desc += "**!!apps review accept [optional comment]** - Casts your vote to accept this application." + '\n' + '\n';
			desc += "**!!apps review deny [optional comment]** - Casts your vote to deny this application." + '\n' + '\n';
			desc += "**!!apps review confirm [optional comment]** - **You only need to use this command if you are reviewing an application with someone else, because !!apps review stop does "
					+ "this automatically.** Confirms and lists you as a user who has reviewed the application." + '\n' + '\n';
			desc += "**!!apps review stop [optional comment]** - Stops reviewing the application. After You use this command, the application will be considered 'on-hold' (reviewed, but without a vote) before a decision "
					+ "has been made to either accept or deny it." + '\n' + '\n';
			desc += "*For the four commands above this message, you can optionally leave a comment after deciding to accept/deny, or stop reviewing the app.*" + '\n' + '\n';
			desc += "Current amount of votes needed to accept an application: **" + acceptsNeeded + "**" + '\n';
			desc += "Current amount of votes needed to deny an application: **" + deniesNeeded + "**" + '\n';
			eb.setDescription(desc);
			eb.setFooter("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			tc.sendMessage(eb);
			return;
		}
		else if (args.length >= 3)
		{
			if (args[2].equalsIgnoreCase("start"))
				onStartCommand(tc, args);
			
			else if (args[2].equalsIgnoreCase("reveal"))
				onRevealUsername(tc);
			
			else if (args[2].equalsIgnoreCase("comment"))
				onComment(u, tc, args);
			
			else if (args[2].equalsIgnoreCase("cancel"))
				onCancel(tc);
			
			else if (args[2].equalsIgnoreCase("accept"))
				onAccept(u, tc, args);
			
			else if (args[2].equalsIgnoreCase("confirm"))
				onConfirm(u, tc, args);

			else if (args[2].equalsIgnoreCase("deny"))
				onDeny(u, tc, args);
			
			else if (args[2].equalsIgnoreCase("stop"))
				onStop(u, tc, args);
		}
	}
	
	public static Application getOldestApp()
	{
		List<String> applied = Applicants.getIdsWhoApplied();
		
		if (applied.isEmpty())
			return null;
		Application app = new Application(applied.get(0));
		
		return app;
	}
	
	private static void onStartCommand(TextChannel tc, String[] args)
	{
		if (currentlyReviewing != null)
		{
			tc.sendMessage("An application is already being reviewed. If this is an error, please contact Neuro.");
			return;
		}
		
		if (args.length == 4)
		{
			boolean found = false;
			
			
			if (MainData.get().getStringList("applications.accepted").contains(args[3]) || MainData.get().getStringList("applications.denied").contains(args[3]))
			{
				currentlyReviewing = new Application(args[3]);
				currentlyReviewing.lockApplication();
				found = true;
			}
			else if (MainData.get().getStringList("applications.on-hold").contains(args[3]))
			{
				currentlyReviewing = new Application(args[3]);
				found = true;
			}
			
			if (!found)
			{
				tc.sendMessage("This application either doesn't exist, or isn't in the accepted/rejected/on-hold category.");
				return;
			}
			currentReviewMessage_1 = tc.sendMessage(currentlyReviewing.getFormattedApplications(true).get(0));
			currentReviewMessage_2 = tc.sendMessage(currentlyReviewing.getFormattedApplications(true).get(1));
			
			revealed = true;
			return;
		}
		revealed = false;
		currentlyReviewing = getOldestApp();
		if (currentlyReviewing == null)
		{
			tc.sendMessage("No applications present to review.");
			return;
		}
		currentReviewMessage_1 = tc.sendMessage(currentlyReviewing.getFormattedApplications(false).get(0));
		currentReviewMessage_2 = tc.sendMessage(currentlyReviewing.getFormattedApplications(false).get(1));
		
	}
	
	private static void onRevealUsername(TextChannel tc)
	{
		if (currentlyReviewing == null)
		{
			tc.sendMessage("You can only use this command when You're reviewing an application.");
			return;
		}
		if (revealed == true)
		{
			tc.sendMessage("This application has already been revealed.");
			return;
		}
		try {
			Message m1 = currentReviewMessage_1.get();
			Message m2 = currentReviewMessage_2.get();
			m1.edit(currentlyReviewing.getFormattedApplications(true).get(0));
			m2.edit(currentlyReviewing.getFormattedApplications(true).get(1));
			revealed = true;
		} catch (InterruptedException | ExecutionException e) {
			tc.sendMessage("Something went wrong! Maybe the review doesn't exist anymore? If so, cancel the review process and try again. Exception message: "
					+ e.getLocalizedMessage());
			e.printStackTrace();
		}
		
	}
	
	private static void onComment(User u, TextChannel tc, String[] args)
	{
		if (currentlyReviewing == null)
		{
			tc.sendMessage("You can only use this command when You're reviewing an application.");
			return;
		}
		
		if (args.length == 3)
		{
			tc.sendMessage("**!!apps review comment <message>** - Leaves a comment on the application You are reviewing.");
			return;
		}
		
		String comment = "";
		
		for (int i = 3; i < args.length; i++)
		{
			comment += args[i] + " ";
		}
		
		if (comment.contains("|"))
		{
			tc.sendMessage("Sorry, but please don't use the '|' symbol in your comment and try again.");
			return;
		}
		
		currentlyReviewing.addComment(u, comment);
		tc.sendMessage("Added a comment to the application.");
		try {
			Message m1 = currentReviewMessage_1.get();
			Message m2 = currentReviewMessage_2.get();
			m1.edit(currentlyReviewing.getFormattedApplications(revealed).get(0));
			m2.edit(currentlyReviewing.getFormattedApplications(revealed).get(1));
		} catch (InterruptedException | ExecutionException e) {
			tc.sendMessage("Something went wrong when trying to update the application! Maybe the review doesn't exist anymore? If so, cancel the review process and try again. Exception message: "
					+ e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	private static boolean checkConfirm(TextChannel tc, User u)
	{
		if (!confirms.contains(u.getIdAsString()))
		{
			tc.sendMessage("Are You sure about your decision? Give the app one more look, and do the command again to accept/deny it.");
			confirms.add(u.getIdAsString());
			return false;
		}
		confirms.remove(u.getIdAsString());
		return true;
	}
	
	private static void onAccept(User u, TextChannel tc, String[] args)
	{
		if (currentlyReviewing == null)
		{
			tc.sendMessage("You can only use this command when You're reviewing an application.");
			return;
		}
		if (currentlyReviewing.locked())
		{
			tc.sendMessage("This application is locked, which means that it has already been accepted or denied. You cannot accept or deny it anymore. Use !!apps review cancel to stop reviewing it.");
			return;
		}
		
		if (!checkConfirm(tc, u))
			return;
		
		try {
			String comment = "";
			
			for (int i = 3; i < args.length; i++)
			{
				comment += args[i] + " ";
			}
			
			if (comment.contains("|"))
			{
				tc.sendMessage("Sorry, but please don't use the '|' symbol in your comment and try again.");
				return;
			}
			
			if (!comment.equalsIgnoreCase(""))
				currentlyReviewing.addComment(u, comment);
			
			Message m1 = currentReviewMessage_1.get();
			Message m2 = currentReviewMessage_2.get();
			List<String> accepted = currentlyReviewing.getWhoAccepted();
			if (accepted.contains(u.getIdAsString()))
			{
				tc.sendMessage("You have already accepted this application. Use !!apps review stop to stop reviewing the application.");
				return;
			}
			
			tc.sendMessage("You have accepted this application! More votes are needed to reach a full decision. If you are done with this specific application, please use !!apps review stop.");

			currentlyReviewing.addReviewed(u);
			currentlyReviewing.addAccepted(u);
			currentlyReviewing.onHold();
			m1.edit(currentlyReviewing.getFormattedApplications(revealed).get(0));
			m2.edit(currentlyReviewing.getFormattedApplications(revealed).get(1));
			
			if (accepted.size() == acceptsNeeded - 1)
			{
				tc.sendMessage("This application has got enough votes and has been fully accepted! Moving it to the accepted category..");
				currentlyReviewing.accept();
				m1.delete();
				m2.delete();
				currentReviewMessage_1 = null;
				currentReviewMessage_2 = null;
				currentlyReviewing = null;
				revealed = false;
			}
		} catch (InterruptedException | ExecutionException e) {
			tc.sendMessage("Something went wrong! Exception message: "
					+ e.getLocalizedMessage());
			e.printStackTrace();
		}
		
	}
	
	private static void onDeny(User u, TextChannel tc, String[] args)
	{
		if (currentlyReviewing == null)
		{
			tc.sendMessage("You can only use this command when You're reviewing an application.");
			return;
		}
		if (currentlyReviewing.locked())
		{
			tc.sendMessage("This application is locked, which means that it has already been accepted or denied. You cannot accept or deny it anymore. Use !!apps review cancel to stop reviewing it.");
			return;
		}
		
		if (!checkConfirm(tc, u))
			return;
		
		try {
			String comment = "";
			
			for (int i = 3; i < args.length; i++)
			{
				comment += args[i] + " ";
			}
			
			if (comment.contains("|"))
			{
				tc.sendMessage("Sorry, but please don't use the '|' symbol in your comment and try again.");
				return;
			}
			
			if (!comment.equalsIgnoreCase(""))
				currentlyReviewing.addComment(u, comment);
			
			Message m1 = currentReviewMessage_1.get();
			Message m2 = currentReviewMessage_2.get();
			List<String> denied = currentlyReviewing.getWhoDenied();
			if (denied.contains(u.getIdAsString()))
			{
				tc.sendMessage("You have already denied this application. Use !!apps review stop to stop reviewing the application.");
				return;
			}
			
			tc.sendMessage("You have denied this application! More votes are needed to reach a full decision. If you are done with this specific application, please use !!apps review stop.");
			currentlyReviewing.addReviewed(u);
			currentlyReviewing.addDenied(u);
			currentlyReviewing.onHold();

			if (denied.size() == deniesNeeded - 1)
			{
				tc.sendMessage("This application has been fully denied! Moving it to the denied category..");
				currentlyReviewing.deny();
				m1.delete();
				m2.delete();
				currentReviewMessage_1 = null;
				currentReviewMessage_2 = null;
				currentlyReviewing = null;
				revealed = false;
				
				try
				{
				MainDiscordBot.api().getUserById(currentlyReviewing.getId()).get().sendMessage("Hello! I regret to inform You that Your application has been denied. "
						+ "You will have the ability to reapply in **two months** from the **submittion from Your last application**. Please do not ask "
						+ "the staff members why Your application has been denied, because we had our unbiased preferences and reasons for doing so. "
						+ "Thank You for applying! Have a good day.");
				} catch (Exception ex)
				{}
				return;
			}

			m1.edit(currentlyReviewing.getFormattedApplications(revealed).get(0));
			m2.edit(currentlyReviewing.getFormattedApplications(revealed).get(1));
			
		} catch (InterruptedException | ExecutionException e) {
			tc.sendMessage("Something went wrong! Exception message: "
					+ e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	private static void onConfirm(User u, TextChannel tc, String[] args)
	{
		if (currentlyReviewing == null)
		{
			tc.sendMessage("You can only use this command when You're reviewing an application.");
			return;
		}
		if (currentlyReviewing.locked())
		{
			tc.sendMessage("This application is locked, which means that it has already been accepted or denied. You cannot accept or deny it anymore. Use !!apps review cancel to stop reviewing it.");
			return;
		}

		String comment = "";
		
		for (int i = 3; i < args.length; i++)
		{
			comment += args[i] + " ";
		}
		
		if (comment.contains("|"))
		{
			tc.sendMessage("Sorry, but please don't use the '|' symbol in your comment and try again.");
			return;
		}
		
		if (!comment.equalsIgnoreCase(""))
			currentlyReviewing.addComment(u, comment);

		tc.sendMessage("You have confirmed that you have reviewed this application.");

		currentlyReviewing.addReviewed(u);
		currentlyReviewing.onHold();

		try {
			currentReviewMessage_1.get().edit(currentlyReviewing.getFormattedApplications(revealed).get(0));
			currentReviewMessage_2.get().edit(currentlyReviewing.getFormattedApplications(revealed).get(1));
		} catch (InterruptedException | ExecutionException e) {
			tc.sendMessage("Something went wrong! Exception message: "
					+ e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	private static void onStop(User u, TextChannel tc, String[] args)
	{
		if (currentlyReviewing == null)
		{
			tc.sendMessage("You can only use this command when You're reviewing an application.");
			return;
		}
		if (currentlyReviewing.locked())
		{
			tc.sendMessage("This application is locked, which means that it has already been accepted or denied. You cannot accept or deny it anymore. Use !!apps review cancel to stop reviewing it.");
			return;
		}
		
		try {
			String comment = "";
			
			for (int i = 3; i < args.length; i++)
			{
				comment += args[i] + " ";
			}
			
			if (comment.contains("|"))
			{
				tc.sendMessage("Sorry, but please don't use the '|' symbol in your comment and try again.");
				return;
			}
			
			if (!comment.equalsIgnoreCase(""))
				currentlyReviewing.addComment(u, comment);
			
			Message m1 = currentReviewMessage_1.get();
			Message m2 = currentReviewMessage_2.get();
			
			tc.sendMessage("You have put this application off on-hold.");

			currentlyReviewing.addReviewed(u);
			currentlyReviewing.onHold();
			
			m1.delete();
			m2.delete();
			
			currentReviewMessage_1 = null;
			currentReviewMessage_2 = null;
			
			currentlyReviewing = null;
			revealed = false;
			
		} catch (InterruptedException | ExecutionException e) {
			tc.sendMessage("Something went wrong! Exception message: "
					+ e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	private static void onCancel(TextChannel tc)
	{
		if (currentlyReviewing == null)
		{
			tc.sendMessage("You can only use this command when You're reviewing an application.");
			return;
		}

		try {
			tc.sendMessage("You have cancelled the reviewal of this application.");
			currentReviewMessage_1.get().delete();
			currentReviewMessage_2.get().delete();
			currentReviewMessage_1 = null;
			currentReviewMessage_2 = null;
			currentlyReviewing = null;
			revealed = false;
		} catch (InterruptedException | ExecutionException e) {
			tc.sendMessage("Something went wrong! Exception message: "
					+ e.getLocalizedMessage());
			e.printStackTrace();
		}

	}

}

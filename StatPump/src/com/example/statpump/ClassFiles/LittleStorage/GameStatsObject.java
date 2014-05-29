package com.example.statpump.ClassFiles.LittleStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.statpump.statpump.R;
import com.example.statpump.ClassFiles.APIInteraction;
import com.example.statpump.ClassFiles.APIObject;
import com.example.statpump.ClassFiles.LittleStorage.PlayerInfoObject.ParsePlayerInfo;
/**
 * Handles the game stats
 * @author Jeff
 *
 */
public class GameStatsObject 
{
	public String matchHome;
	public String matchDate;
	public String winner;
	public String score;
	public String venueName;
	public int attendance = 0;
	
	public String teamAName;
	public String teamBName;
	public String teamAStats;
	public String teamBStats;
	public String teamALineup;
	public String teamBLineup;
	public Map<String, String> teamAIndivStats = new HashMap<String, String>();
	public Map<String, String> teamBIndivStats = new HashMap<String, String>();
	public boolean isPlayed = true;
	public boolean isHiddenA = false;
	public boolean isHiddenB = false;

	
	public int matchId;
	public APIObject apiObj;
	public PlayerSearchObject psObj;
	public Context cont;
	public String matchURL;
	public int teamAID;
	public int teamBID;
		
	/**
	 * Sets the basic, given data
	 * @param date
	 * @param home
	 * @param id
	 * @param ao
	 * @param po
	 */
	public GameStatsObject(String date, String home, int id, APIObject ao, PlayerSearchObject po, Context c)
	{
		matchDate = date;
		matchHome = home;
		matchId = id;
		apiObj = ao;
		psObj = po;
		cont = c;
		matchURL = ao.formGetMatchInfoDoneUrl(matchId);
		this.spawnAsync();
	}
	
	/**
	 * Spawns the xml parsing threads
	 */
	public void spawnAsync()
	{
		ParseGameStats task = this.new ParseGameStats(this);
		task.execute(this);
	}
	
	/**
	 * Handles the xml parsing 
	 * @param doc
	 * @param g
	 * @return
	 */
	public GameStatsObject parseXML(Document doc, GameStatsObject g)
	{
		Elements links = doc.select("match");
		for(Element elem : links)
		{
			if(elem.attr("status") != null && elem.attr("status").equals("Played"))
			{
				g.isPlayed = true;
			}
			else
			{
				g.isPlayed = false;
			}
			g.teamAName = elem.attr("team_a_name");
			g.teamAID = (g.apiObj.teamIDMap.get(teamAName));
			g.teamBName = elem.attr("team_b_name");
			g.teamBID = (g.apiObj.teamIDMap.get(teamBName));
			if(g.isPlayed)
			{
				if(elem.attr("winner").equals("team_A"))
				{
					g.winner = g.teamAName;
					g.score = elem.attr("fs_a") + " - " + elem.attr("fs_b");
				}
				else
				{
					g.winner = g.teamBName;
					g.score = elem.attr("fs_b") + " - " + elem.attr("fs_a");
				}
				System.out.println(g.winner + ", " + g.score);
			}
		}
		links = doc.select("venue");
		for(Element elem : links)
		{
			g.venueName = elem.attr("name");
		}
		
		if(g.isPlayed)
		{
			links = doc.select("attendance");
			for(Element elem : links)
			{
				try{
				g.attendance = Integer.parseInt(elem.attr("value"));
				} catch(NumberFormatException e)
				{
					g.attendance = -1;
				}
			}
			System.out.println(g.attendance);
		}
		Elements lineup = doc.select("lineups event");
		StringBuilder lineupA = new StringBuilder(1000);
		StringBuilder lineupB = new StringBuilder(1000);
		lineupA.append(g.teamAName + " Appearing Players:\n");
		lineupB.append(g.teamBName + " Appearing Players:\n");
		for(Element player : lineup)
		{
			if(player.attr("team_id").equals(String.valueOf(g.teamAID)))
			{
				if(player.attr("person").length() > 0)
				{
					lineupA.append(player.attr("person"));
					if(player.attr("shirtnumber").length() > 0)
					{
						lineupA.append(" #" + player.attr("shirtnumber"));
					}
					lineupA.append("\n");
				}
			}
			if(player.attr("team_id").equals(String.valueOf(g.teamBID)))
			{
				if(player.attr("person").length() > 0)
				{
					lineupB.append(player.attr("person"));
					if(player.attr("shirtnumber").length() > 0)
					{
						lineupB.append(" #" + player.attr("shirtnumber"));
					}
					lineupB.append("\n");
				}
			}
		}
		lineup = doc.select("lineups_bench event");
		for(Element player : lineup)
		{
			if(player.attr("team_id").equals(String.valueOf(g.teamAID)))
			{
				if(player.attr("person").length() > 0)
				{
					lineupA.append(player.attr("person"));
					if(player.attr("shirtnumber").length() > 0)
					{
						lineupA.append(" #" + player.attr("shirtnumber"));
					}
					lineupA.append("\n");
				}
			}
			if(player.attr("team_id").equals(String.valueOf(g.teamBID)))
			{
				if(player.attr("person").length() > 0)
				{
					lineupB.append(player.attr("person"));
					if(player.attr("shirtnumber").length() > 0)
					{
						lineupB.append(" #" + player.attr("shirtnumber"));
					}
					lineupB.append("\n");
				}
			}
		}
		if(lineupA.toString().equals(g.teamAName + " Appearing Players:\n")) 
		{ 
			lineupA.append("No lineup listed");
		}
		if(lineupB.toString().equals(g.teamBName + " Appearing Players:\n"))
		{
			lineupB.append("No lineup listed");
		}
		g.teamALineup = lineupA.toString();
		g.teamBLineup = lineupB.toString();
		System.out.println(g.teamALineup);
		System.out.println(g.teamBLineup);
		if(g.apiObj.sportURL.contains("baseball"))
		{
			System.out.println("Calling baseball");
			g.baseballIndiv(g, doc);
		}
		else if(g.apiObj.sportURL.contains("americanfootball"))
		{
			System.out.println("Calling football");
			g.footballIndiv(g, doc);
		}
		else if(g.apiObj.sportURL.contains("basketball"))
		{
			System.out.println("Calling basketball");
			g.basketballIndiv(g, doc);
		}
		else if(g.apiObj.sportURL.contains("soccer"))
		{
			System.out.println("Calling soccer");
			g.soccerIndiv(g, doc);
		}
		else if(g.apiObj.sportURL.contains("hockey"))
		{
			System.out.println("Calling hockey");
			g.hockeyIndiv(g, doc);
		}
		return g;
	}
	
	/**
	 * Handles the hockey individual parsing
	 * @param g
	 * @param doc
	 */
	private void hockeyIndiv(GameStatsObject g, Document doc)
	{
		StringBuilder teamAStats = new StringBuilder(100);
		StringBuilder teamBStats = new StringBuilder(100);
		teamAStats.append(g.teamAName + " Team Stats:\n");
		teamBStats.append(g.teamBName + " Team Stats:\n");
		Elements teamElems = doc.select("team");
		for(Element iter : teamElems)
		{
			Element teamElem = iter.child(0);
			if(iter.attr("team_id").equals(String.valueOf(g.teamAID)))
			{
				if(teamElem.attr("team_BKS").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_BKS") + " blocked shots\n");
				}
				if(teamElem.attr("team_FOwins").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_FOwins") + " face-off wins\n");
				}
				if(teamElem.attr("team_GVA").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_GVA") + " giveaways\n");
				}
				if(teamElem.attr("team_Hits").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_Hits") + " hits\n");
				}
				if(teamElem.attr("team_PIM").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_PIM") + " penalties in minutes\n");
				}
				if(teamElem.attr("team_PP_attempts").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_PP_attempts") + " power play attempts\n");
				}
				if(teamElem.attr("team_PP_goals").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_PP_goals") + " power play goals\n");
				}
				if(teamElem.attr("team_SOG_p1").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_SOG_p1") + " shots on goal in period 1\n");
				}
				if(teamElem.attr("team_SOG_p2").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_SOG_p2") + " shots on goal in period 2\n");
				}
				if(teamElem.attr("team_SOG_p3").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_SOG_p3") + " shots on goal in period 3\n");
				}
				if(teamElem.attr("team_SOG_tot").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_SOG_tot") + " total shots on goal\n");
				}
				if(teamElem.attr("team_TKA").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_TKA") + " takeaways\n");
				}
			}
			if(iter.attr("team_id").equals(String.valueOf(g.teamBID)))
			{
				if(teamElem.attr("team_BKS").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_BKS") + " blocked shots\n");
				}
				if(teamElem.attr("team_FOwins").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_FOwins") + " face-off wins\n");
				}
				if(teamElem.attr("team_GVA").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_GVA") + " giveaways\n");
				}
				if(teamElem.attr("team_Hits").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_Hits") + " hits\n");
				}
				if(teamElem.attr("team_PIM").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_PIM") + " penalties in minutes\n");
				}
				if(teamElem.attr("team_PP_attempts").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_PP_attempts") + " power play attempts\n");
				}
				if(teamElem.attr("team_PP_goals").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_PP_goals") + " power play goals\n");
				}
				if(teamElem.attr("team_SOG_p1").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_SOG_p1") + " shots on goal in period 1\n");
				}
				if(teamElem.attr("team_SOG_p2").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_SOG_p2") + " shots on goal in period 2\n");
				}
				if(teamElem.attr("team_SOG_p3").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_SOG_p3") + " shots on goal in period 3\n");
				}
				if(teamElem.attr("team_SOG_tot").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_SOG_tot") + " total shots on goal\n");
				}
				if(teamElem.attr("team_TKA").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_TKA") + " takeaways\n");
				}
			}
		}
		if(teamAStats.toString().equals(g.teamAName + " Team Stats:\n"))
		{
			teamAStats.append("No team stats available\n");
		}
		if(teamBStats.toString().equals(g.teamBName + " Team Stats:\n"))
		{
			teamBStats.append("No team stats available\n");
		}
		g.teamAStats = teamAStats.toString();
		g.teamBStats = teamBStats.toString();
		g.teamAStats = g.teamAStats.replaceAll("\\.00", "");
		g.teamBStats = g.teamBStats.replaceAll("\\.00", "");
		System.out.println(g.teamAStats);
		System.out.println(g.teamBStats);
		Elements stats = doc.select("matchstatistics statistics");
		for(Element stat : stats)
		{
			String name = stat.parent().attr("name");
			StringBuilder statsStr = new StringBuilder(1000);
			statsStr.append(name + " Statistics:\n");
			//Goalie
			if(stat.hasAttr("gk_goals_against"))
			{
				if(stat.attr("gk_goals_against").length() > 0)
				{
					statsStr.append(stat.attr("gk_goals_against") + " goals allowed on " + stat.attr("gk_shots_against") + " shots, with " + 
							stat.attr("gk_saves") + " saves\n");
				}
				if(stat.attr("gk_pp_saves").length() > 0)
				{
					statsStr.append(stat.attr("gk_pp_saves") + " PP saves on " + stat.attr("gk_pp_shots_against") + " PP shots\n");
				}
				if(stat.attr("gk_sh_saves").length() > 0)
				{
					statsStr.append(stat.attr("gk_sh_saves") + " SH saves on " + stat.attr("gk_sh_shots_against") + " SH shots\n");
				}
				if(stat.attr("gk_assists").length() > 0 && !stat.attr("gk_assists").equals("0"))
				{
					statsStr.append(stat.attr("gk_assists") + " assists\n");
				}
				if(stat.attr("gk_goals").length() > 0 && !stat.attr("gk_goals").equals("0"))
				{
					statsStr.append(stat.attr("gk_goals ") + " goals\n");
				}
				if(stat.attr("gk_points").length() > 0 && !stat.attr("gk_points").equals("0"))
				{
					statsStr.append(stat.attr("gk_points") + " points\n");
				}
				if(stat.attr("gk_minutes").length() > 0)
				{
					statsStr.append(stat.attr("gk_minutes") + ":" + stat.attr("gk_seconds") + " played\n");
				}
			}
			else //Non-goalie
			{
				if(stat.attr("goals").length() > 0)
				{
					statsStr.append(stat.attr("goals") + " goals scored\n"+ stat.attr("shots_on_goal") + " shots on goal\n" + 
					stat.attr("assists") + " assists\n" + stat.attr("faceoffs_won") + " faceoffs won\n" + stat.attr("plus_minus") + " plus/minus\n" + 
					stat.attr("penalties") + " penalties\n" + stat.attr("points") + " points\n");
				}
				if(stat.attr("sh_minutes").length() > 0)
				{
					statsStr.append(stat.attr("sh_minutes") + ":" + stat.attr("sh_seconds") + " time on ice shorthanded\n");
				}
				if(stat.attr("ev_minutes").length() > 0)
				{
					statsStr.append(stat.attr("ev_minutes") + ":" + stat.attr("ev_seconds") + " time on ice with even strength\n");
				}
				if(stat.attr("pp_minutes").length() > 0 && !stat.attr("pp_minutes").equals("0"))
				{
					statsStr.append(stat.attr("pp_minutes") + " minutes on ice while in a power play\n");
				}
				if(stat.attr("minutes").length() > 0)
				{
					statsStr.append(stat.attr("minutes") + ":" + stat.attr("seconds") + " played\n");
				}
			}
			if(stat.parent().attr("team_id").equals(g.teamAID))
			{
				g.teamAIndivStats.put(name, statsStr.toString());
			}
			if(stat.parent().attr("team_id").equals(g.teamBID))
			{
				g.teamBIndivStats.put(name, statsStr.toString());
			}
		}
		if(g.teamAIndivStats.size() == 0)
		{
			g.teamAIndivStats.put("No stats available", "No stats available for this game. Either it hasn't been played yet, is currently in progress, or the stats aren't available. If it's not the latter, stats will be available soon after the game.");
		}
		if(g.teamBIndivStats.size() == 0)
		{
			g.teamBIndivStats.put("No stats available", "No stats available for this game. Either it hasn't been played yet, is currently in progress, or the stats aren't available. If it's not the latter, stats will be available soon after the game.");
		}
	}
	
	/**
	 * Handles the soccer individual parsing
	 * @param g
	 * @param doc
	 */
	private void soccerIndiv(GameStatsObject g, Document doc) {
		StringBuilder teamAStats = new StringBuilder(100);
		StringBuilder teamBStats = new StringBuilder(100);
		teamAStats.append(g.teamAName + " Team Stats:\n");
		teamBStats.append(g.teamBName + " Team Stats:\n");
		Elements teamElems = doc.select("bookings");
		for(Element iter : teamElems)
		{
			for(Element child : iter.children())
			{
				if(child.attr("team_id").equals(String.valueOf(g.teamAID)))
				{
					teamAStats.append(child.attr("name") + " given to " + child.attr("person") + "\n");
				}
				if(child.attr("team_id").equals(String.valueOf(g.teamBID)))
				{
					teamBStats.append(child.attr("name") + " given to " + child.attr("person") + "\n");
				}
			}
		}
		if(teamAStats.toString().equals(g.teamAName + " Team Stats:\n"))
		{
			teamAStats.append("No team stats available\n");
		}
		if(teamBStats.toString().equals(g.teamBName + " Team Stats:\n"))
		{
			teamBStats.append("No team stats available\n");
		}
		g.teamAStats = teamAStats.toString();
		g.teamBStats = teamBStats.toString();
		g.teamAStats = g.teamAStats.replaceAll("\\.00", "");
		g.teamBStats = g.teamBStats.replaceAll("\\.00", "");
		System.out.println(g.teamAStats);
		System.out.println(g.teamBStats);
		Elements goals = doc.select("goals goal");
		for(Element goal : goals)
		{
			for(Element event : goal.children())
			{
				if(event.attr("team_id").equals(String.valueOf(g.teamAID)))
				{
					if(g.teamAIndivStats.containsKey(event.attr("person")))
					{
						String set = g.teamAIndivStats.get(event.attr("person")) + "\n";
						set += event.attr("name");
						if(event.attr("minute").length() > 1)
						{
							set += " in minute " + event.attr("minute");
						}
						g.teamAIndivStats.put(event.attr("person"), set);
					}
					else
					{
						String set = event.attr("person") + " Game Statistics:" + "\n";
						set += event.attr("name");
						if(event.attr("minute").length() > 1)
						{
							set += " in minute " + event.attr("minute");
						}
						g.teamAIndivStats.put(event.attr("person"), set);
					}
				}
				if(event.attr("team_id").equals(String.valueOf(g.teamBID)))
				{
					if(g.teamBIndivStats.containsKey(event.attr("person")))
					{
						String set = g.teamBIndivStats.get(event.attr("person")) + "\n";
						set += event.attr("name");
						if(event.attr("minute").length() > 1)
						{
							set += " in minute " + event.attr("minute");
						}
						g.teamBIndivStats.put(event.attr("person"), set);
					}
					else
					{
						String set = event.attr("person") + " Game Statistics:" + "\n";
						set += event.attr("name");
						if(event.attr("minute").length() > 1)
						{
							set += " in minute " + event.attr("minute");
						}
						g.teamBIndivStats.put(event.attr("person"), set);
					}
				}
			}
		}
		if(g.teamAIndivStats.size() == 0)
		{
			g.teamAIndivStats.put("No stats available", "No stats available for this game. Either it hasn't been played yet, or the stats aren't available");
		}
		if(g.teamBIndivStats.size() == 0)
		{
			g.teamBIndivStats.put("No stats available", "No stats available for this game. Either it hasn't been played yet, or the stats aren't available");
		}
	}

	/**
	 * Handles the basketball individual parsing
	 * @param g
	 * @param doc
	 */
	private void basketballIndiv(GameStatsObject g, Document doc) {
		StringBuilder teamAStats = new StringBuilder(100);
		StringBuilder teamBStats = new StringBuilder(100);
		teamAStats.append(g.teamAName + " Team Stats:\n");
		teamBStats.append(g.teamBName + " Team Stats:\n");
		Elements teamElems = doc.select("team");
		for(Element iter : teamElems)
		{
			Element teamElem = iter.child(0);
			if(iter.attr("team_id").equals(String.valueOf(g.teamAID)))
			{
				if(teamElem.attr("team_fastbk_pts").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_fastbk_pts") + " fastbreak points\n");
				}
				if(teamElem.attr("team_ptsinpt").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_ptsinpt") + " points in paint\n");
				}
				if(teamElem.attr("team_second_chncpts").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_second_chncpts") + " second chance points\n");
				}
			}
			if(iter.attr("team_id").equals(String.valueOf(g.teamBID)))
			{
				if(teamElem.attr("team_fastbk_pts").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_fastbk_pts") + " fastbreak points\n");
				}
				if(teamElem.attr("team_ptsinpt").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_ptsinpt") + " points in paint\n");
				}
				if(teamElem.attr("team_second_chncpts").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_second_chncpts") + " second chance points\n");
				}
			}
		}
		if(teamAStats.toString().equals(g.teamAName + " Team Stats:\n"))
		{
			teamAStats.append("No team stats available\n");
		}
		if(teamBStats.toString().equals(g.teamBName + " Team Stats:\n"))
		{
			teamBStats.append("No team stats available\n");
		}
		g.teamAStats = teamAStats.toString();
		g.teamBStats = teamBStats.toString();
		g.teamAStats = g.teamAStats.replaceAll("\\.00", "");
		g.teamBStats = g.teamBStats.replaceAll("\\.00", "");
		System.out.println(g.teamAStats);
		System.out.println(g.teamBStats);
		Elements players = doc.select("matchstatistics statistics");
		for(Element player : players)
		{
			String name = player.parent().attr("name");
			StringBuilder stats = new StringBuilder(1000);
			stats.append(name + " Statistics:\n");
			if(player.attr("seconds").length() > 0 && player.attr("minutes").length() > 0 && !(player.attr("seconds").equals("0") && player.attr("minutes").equals("0")))
			{
				stats.append(player.attr("minutes") + ":" + player.attr("seconds") + " time played\n");
			}
			if(player.attr("three_points_tries").length() > 0 && !(player.attr("three_points_tries").equals("0")))
			{
				stats.append(player.attr("three_points_in") + "/" + player.attr("three_points_tries") + " from 3 point range\n");
			}
			if(player.attr("two_points_tries").length() > 0 && !(player.attr("two_points_tries").equals("0")))
			{
				stats.append(player.attr("two_points_in") + "/" + player.attr("two_points_tries") + " from 2 point shots\n");
			}
			if(!player.attr("free_throws_tries").equals("0"))
			{
				stats.append(player.attr("free_throws_in") + "/" + player.attr("free_throws_tries") + " on free throw attempts\n");
			}
			if(!player.attr("field_goals_attempts").equals("0"))
			{
				stats.append(player.attr("field_goals_made") + "/" + player.attr("field_goals_attempts") + " on field goals total\n");
			}
			stats.append(player.attr("turnover") + " turnovers, " + player.attr("assists") + " assists\n" + player.attr("blocks") + " blocks");
			if(!player.attr("steals").equals("0"))
			{
				stats.append(", " + player.attr("steals") + " steals");
			}
			stats.append("\n");
			if(!player.attr("total_rebounds").equals("0"))
			{
				stats.append(player.attr("total_rebounds") + " rebounds, " + player.attr("defense_rebounds") + " defensive, " + player.attr("offense_rebounds") + " offensive\n");
			}
			if(!player.attr("plus_minus").equals("0"))
			{
				stats.append(player.attr("plus_minus") + " plus/minus\n");
			}
			if(!player.attr("personal_fouls").equals("0"))
			{
				stats.append(player.attr("personal_fouls") + " fouls\n");
			}
			if(player.parent().attr("team_id").equals(String.valueOf(g.teamAID)))
			{
				g.teamAIndivStats.put(name, stats.toString());
			}
			if(player.parent().attr("team_id").equals(String.valueOf(g.teamBID)))
			{
				g.teamBIndivStats.put(name, stats.toString());
			}
		}
		if(g.teamAIndivStats.size() == 0)
		{
			g.teamAIndivStats.put("No stats available", "No stats available for this game. Either it hasn't been played yet, or the stats aren't available");
		}
		if(g.teamBIndivStats.size() == 0)
		{
			g.teamBIndivStats.put("No stats available", "No stats available for this game. Either it hasn't been played yet, or the stats aren't available");
		}
	}

	/**
	 * Handles the football individual parsing
	 * @param g
	 * @param doc
	 */
	private void footballIndiv(GameStatsObject g, Document doc) {
		StringBuilder teamAStats = new StringBuilder(100);
		StringBuilder teamBStats = new StringBuilder(100);
		teamAStats.append(g.teamAName + " Team Stats:\n");
		teamBStats.append(g.teamBName + " Team Stats:\n");
		Elements teamElems = doc.select("team");
		for(Element iter : teamElems)
		{
			Element teamElem = iter.child(0);
			if(iter.attr("team_id").equals(String.valueOf(g.teamAID)))
			{
				if(teamElem.attr("gtge_att").length() > 0)
				{
					teamAStats.append(teamElem.attr("gtge_made") + "/" + teamElem.attr("gtge_att") + " in goal to go situations\n");
				}
				if(teamElem.attr("redzone_att").length() > 0)
				{
					teamAStats.append(teamElem.attr("redzone_made") + "/" + teamElem.attr("redzone_att") + " in the red zone\n");
				}
				if(teamElem.attr("team_3dwn_conv_a").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_3dwn_conv_c") + "/" + teamElem.attr("team_3dwn_conv_a") + " on third down\n");
				}
				if(teamElem.attr("team_4dwn_conv_a").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_4dwn_conv_c")+ "/"+ teamElem.attr("team_4dwn_conv_a") + " on fourth down\n");
				}
				if(teamElem.attr("team_oyds").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_oplays") + " offensive plays for " + teamElem.attr("team_oyds") + " yards\n");
				}
				if(teamElem.attr("team_pass_yds").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_pass_comp") + "/" + teamElem.attr("team_pass_att") + " on passing plays for " + teamElem.attr("team_pass_yds") + " yards\n");
				}
				if(teamElem.attr("team_rush_yds").length() > 0)
				{
					teamAStats.append(teamElem.attr("team_rush_att") + " rushes for " + teamElem.attr("team_rush_yds") + " yards\n");
				}
				if(teamElem.attr("time_ofposs").length() > 0)
				{
					teamAStats.append("Time of possession: " + teamElem.attr("time_ofposs").replace(".", ":") + "\n");
				}
			}
			if(iter.attr("team_id").equals(String.valueOf(g.teamBID)))
			{
				if(teamElem.attr("gtge_att").length() > 0)
				{
					teamBStats.append(teamElem.attr("gtge_made") + "/" + teamElem.attr("gtge_att") + " in goal to go situations\n");
				}
				if(teamElem.attr("redzone_att").length() > 0)
				{
					teamBStats.append(teamElem.attr("redzone_made") + "/" + teamElem.attr("redzone_att") + " in the red zone\n");
				}
				if(teamElem.attr("team_3dwn_conv_a").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_3dwn_conv_c") + "/" + teamElem.attr("team_3dwn_conv_a") + " on third down\n");
				}
				if(teamElem.attr("team_4dwn_conv_a").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_4dwn_conv_c")+ "/"+ teamElem.attr("team_4dwn_conv_a") + " on fourth down\n");
				}
				if(teamElem.attr("team_oyds").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_oplays") + " offensive plays for " + teamElem.attr("team_oyds") + " yards\n");
				}
				if(teamElem.attr("team_pass_yds").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_pass_comp") + "/" + teamElem.attr("team_pass_att") + " on passing plays for " + teamElem.attr("team_pass_yds") + " yards\n");
				}
				if(teamElem.attr("team_rush_yds").length() > 0)
				{
					teamBStats.append(teamElem.attr("team_rush_att") + " rushes for " + teamElem.attr("team_rush_yds") + " yards\n");
				}
				if(teamElem.attr("time_ofposs").length() > 0)
				{
					teamBStats.append("Time of possession: " + teamElem.attr("time_ofposs").replace(".", ":") + "\n");
				}
			}
		}
		if(teamAStats.toString().equals(g.teamAName + " Team Stats:\n"))
		{
			teamAStats.append("No team stats available\n");
		}
		if(teamBStats.toString().equals(g.teamBName + " Team Stats:\n"))
		{
			teamBStats.append("No team stats available\n");
		}
		g.teamAStats = teamAStats.toString();
		g.teamBStats = teamBStats.toString();
		g.teamAStats = g.teamAStats.replaceAll("\\.00", "");
		g.teamBStats = g.teamBStats.replaceAll("\\.00", "");
		System.out.println(g.teamAStats);
		System.out.println(g.teamBStats);
		Elements statsList = doc.select("matchstatistics statistics");
		for(Element stats : statsList)
		{
			String name = stats.parent().attr("name");
			StringBuilder str = new StringBuilder(1000);
			str.append(name + " Statistics:\n");
			//Running data
			if(stats.hasAttr("rush_att"))
			{
				str.append(stats.attr("rush_att") + " carries for " + stats.attr("rush_yds") + " yards and " + stats.attr("rush_td") + " TDs\n");
			}
			//Receiving data
			if(stats.hasAttr("rec"))
			{
				str.append(stats.attr("rec") + " catches for " + stats.attr("rec_yds") + " yards and " + stats.attr("rec_td") + " TDs\n");
			}
			//Passing data
			if(stats.hasAttr("att_pass"))
			{
				str.append(stats.attr("c_pass") + "/" + stats.attr("att_pass") + " for " + stats.attr("pass_yds") + " yards and " + stats.attr("pass_td") + " TDs\n");
				str.append(stats.attr("pass_int") + " interception(s), " + stats.attr("fum") + " fumble(s)\n");
			}
			//Punting data
			if(stats.hasAttr("punt_tot"))
			{
				str.append(stats.attr("punt_tot") + " punts for an average of " + stats.attr("punt_avg") + " yards, with a long of " + stats.attr("punt_lg") + " yards\n");
			}
			//Kicking data
			if(stats.hasAttr("kick_xp"))
			{
				str.append(stats.attr("kick_fgt") + "/" + stats.attr("kick_fg") + " on field goals\n"
						+ stats.attr("kick_xp") + "/" + stats.attr("kick_xpt") + " on extra points\n");
			}
			//Defensive data
			if(stats.hasAttr("sacks"))
			{
				str.append(stats.attr("tot") + " tackle(s)\n");
				if(!stats.attr("sacks").equals("0"))
				{
					str.append(stats.attr("sacks") + " sack(s)\n");
				}
				if(!stats.attr("def_td").equals("0"))
				{
					str.append(stats.attr("def_td") + " touchdown(s)");
				}
				if(!stats.attr("int").equals("0"))
				{
					str.append(stats.attr("int") + " interception(s)\n");
				}
				if(!stats.attr("def_ff").equals("0"))
				{
					str.append(stats.attr("def_ff") + " forced fumble(s)\n");
				}
			}
			if(str.toString().equals(name + " Statistics:\n"))
			{
				str.append(name + " made an appearance, but made no statistical impact\n");
			}
			if(stats.parent().attr("team_id").equals(String.valueOf(g.teamAID)))
			{
				g.teamAIndivStats.put(name,  str.toString());
			}
			if(stats.parent().attr("team_id").equals(String.valueOf(g.teamBID)))
			{
				g.teamBIndivStats.put(name,  str.toString());
			}
		}
		if(g.teamAIndivStats.size() == 0)
		{
			g.teamAIndivStats.put("No stats available", "No stats available for this game. Either it hasn't been played yet, or the stats aren't available");
		}
		if(g.teamBIndivStats.size() == 0)
		{
			g.teamBIndivStats.put("No stats available", "No stats available for this game. Either it hasn't been played yet, or the stats aren't available");
		}
	}

	/**
	 * Handles the baseball individual parsing
	 * @param g
	 * @param doc
	 */
	private void baseballIndiv(GameStatsObject g, Document doc) {
		StringBuilder teamAStats = new StringBuilder(100);
		StringBuilder teamBStats = new StringBuilder(100);
		teamAStats.append(g.teamAName + " Team Stats:\n");
		teamBStats.append(g.teamBName + " Team Stats:\n");
		Elements teamElems = doc.select("team");
		for(Element iter : teamElems)
		{
			for(Element teamElem : iter.children())
			{
				if(iter.attr("team_id").equals(String.valueOf(g.teamAID)))
				{
					if(teamElem.attr("bat_RISP_att").length() > 0)
					{
						teamAStats.append(teamElem.attr("bat_RISP_att") + " at-bats with RISP\n");
					}
					if(teamElem.attr("bat_RISP_comp").length() > 0)
					{
						teamAStats.append(teamElem.attr("bat_RISP_comp") + " hits with RISP\n");
					}
					if(teamElem.attr("bat_team_LOB").length() > 0)
					{
						teamAStats.append(teamElem.attr("bat_team_LOB") + " batters left on base\n");
					}
					if(teamElem.attr("errors").length() > 0)
					{
						teamAStats.append(teamElem.attr("errors") + " team errors\n");
					}
					if(teamElem.attr("hits").length() > 0)
					{
						teamAStats.append(teamElem.attr("hits") + " team hits\n");
					}
				}
				if(iter.attr("team_id").equals(String.valueOf(g.teamBID)))
				{
					if(teamElem.attr("bat_RISP_att").length() > 0)
					{
						teamBStats.append(teamElem.attr("bat_RISP_att") + " at-bats with RISP\n");
					}
					if(teamElem.attr("bat_RISP_comp").length() > 0)
					{
						teamBStats.append(teamElem.attr("bat_RISP_comp") + " hits with RISP\n");
					}
					if(teamElem.attr("bat_team_LOB").length() > 0)
					{
						teamBStats.append(teamElem.attr("bat_team_LOB") + " batters left on base\n");
					}
					if(teamElem.attr("errors").length() > 0)
					{
						teamBStats.append(teamElem.attr("errors") + " team errors\n");
					}
					if(teamElem.attr("hits").length() > 0)
					{
						teamBStats.append(teamElem.attr("hits") + " team hits\n");
					}
				}
			}
		}
		if(teamAStats.toString().equals(g.teamAName + " Team Stats:\n"))
		{
			teamAStats.append("No team stats available\n");
		}
		if(teamBStats.toString().equals(g.teamBName + " Team Stats:\n"))
		{
			teamBStats.append("No team stats available\n");
		}
		g.teamAStats = teamAStats.toString();
		g.teamBStats = teamBStats.toString();
		g.teamAStats = g.teamAStats.replaceAll("\\.00", "");
		g.teamBStats = g.teamBStats.replaceAll("\\.00", "");
		System.out.println(g.teamAStats);
		System.out.println(g.teamBStats);
		Elements statsList = doc.select("matchstatistics statistics");
		for(Element stats : statsList)
		{
			String name = stats.parent().attr("name");
			StringBuilder str = new StringBuilder(1000);
			str.append(name + " Statistics:\n");
			if(stats.hasAttr("tot_pitch") || stats.hasAttr("pitch_totpitch"))
			{
				if(stats.hasAttr("er"))
				{
					str.append("ERA: " + stats.attr("era") + "\n");
				}
				if(stats.hasAttr("bb_pit"))
				{
					str.append(stats.attr("bb_pit") + " walked batters, " + 
							stats.attr("so_pit") + " Ks\n");
				}
				if(stats.hasAttr("er"))
				{
					str.append(stats.attr("er") + " earned runs on " + stats.attr("h_pit") 
							+ " hits");
					if(!stats.attr("hr").equals("0"))
					{
						str.append(", " + stats.attr("hr") + " of which were HR(s)");
					}
					str.append("\n");
				}
				if(stats.hasAttr("pitch_totpitch"))
				{
					str.append(stats.attr("pitch_totpitch") + " pitches (" + stats.attr("pitch_totstrikes") + " of which were strikes) thrown to a total of " + 
							stats.attr("pitch_batfaced") + " batters over " + stats.attr("ip") + " innings\n");
				}
				if(!stats.attr("pitch_loss").equals("0"))
				{
					str.append("Got the loss\n");
				}
				else if(!stats.attr("pitch_save").equals("0"))
				{
					str.append("Got the save\n");
				}
				else if(!stats.attr("pitch_won").equals("0"))
				{
					str.append("Got the win\n");
				}
			}
			if(stats.attr("ab").length() > 0 && stats.hasAttr("avg"))
			{
				if(!stats.attr("ab").equals("0"))
				{
					str.append(stats.attr("h")+ "/" + stats.attr("ab") + " on the day\n");
				} 
				if(stats.hasAttr("doubles"))
				{
					str.append(stats.attr("doubles") + " doubles, ");
					str.append(stats.attr("triples") + " triples, ");
					str.append(stats.attr("hrs") + " home run(s)\n");
				}
				if(stats.hasAttr("r") && stats.hasAttr("rbi"))
				{
					str.append(stats.attr("r") + " runs, " + stats.attr("rbi") + " RBIs\n");
				}
				if(stats.hasAttr("so") && stats.hasAttr("bb"))
				{
					str.append(stats.attr("bb") + " walks, " + stats.attr("so") + " Ks\n");
				}
				if(stats.hasAttr("avg"))
				{
					str.append("Hitting " + stats.attr("avg"));
					if(!stats.attr("obp").equals("0") && !stats.attr("slg").equals("0"))
					{
						str.append(", OBP: " + stats.attr("obp") + ", Slugging: " + stats.attr("slg"));
					}
					str.append("\n");
				} 
				if(stats.hasAttr("assists") && !stats.attr("assists").equals("0"))
				{
					str.append(stats.attr("assists") + " assist(s)\n");
				}
			}
			if(stats.parent().attr("team_id").equals(String.valueOf(g.teamAID)))
			{
				g.teamAIndivStats.put(name, str.toString());
			}
			if(stats.parent().attr("team_id").equals(String.valueOf(g.teamBID)))
			{
				g.teamBIndivStats.put(name,  str.toString());
			}
		}
		if(g.teamAIndivStats.size() == 0)
		{
			g.teamAIndivStats.put("No stats available", "No stats available for this game. Either it hasn't been played yet, or the stats aren't available");
		}
		if(g.teamBIndivStats.size() == 0)
		{
			g.teamBIndivStats.put("No stats available", "No stats available for this game. Either it hasn't been played yet, or the stats aren't available");
		}
	}

	/**
	 * Handles the parsing of the xml for game stats asynchronously
	 * @author Jeff
	 *
	 */
	public class ParseGameStats extends AsyncTask<Object, Void, GameStatsObject> 
	{
			APIObject obj;
			Context a;
			ProgressDialog pda;
			PlayerSearchObject po;
			boolean setContent;
			GameStatsObject gsObj;
		    public ParseGameStats(GameStatsObject g) 
		    {
		    	gsObj = g;
		    	obj = gsObj.apiObj;
		    	a = gsObj.cont;
		    	po = gsObj.psObj;
		        pda = new ProgressDialog(cont);
		        pda.setCancelable(false);
		        pda.setMessage("Please wait, fetching the information...");
		        pda.show();
		    }


			@Override
			protected void onPreExecute(){ 
			   super.onPreExecute();  
			}
	
			@Override
			protected void onPostExecute(GameStatsObject result){
			   super.onPostExecute(result);
			   pda.dismiss();
			   setContentView(gsObj);
			}    
			 
		    @Override
		    protected GameStatsObject doInBackground(Object... data) 
		    {
		    	GameStatsObject gsObj = (GameStatsObject)data[0];
		    	try {
					Document doc = APIInteraction.getXML(gsObj.matchURL, gsObj.apiObj);
					gsObj.parseXML(doc, gsObj);
					return gsObj;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return null;
		    }
	  }
	
	/**
	 * Sets all the stats of the view
	 * @param gsObj
	 */
	public void setContentView(final GameStatsObject gsObj)
	{ 
		LinearLayout layout = (LinearLayout)((Activity) cont).findViewById(R.id.statwell);
		layout.removeAllViews();
		View res = ((Activity) cont).getLayoutInflater().inflate(R.layout.sw_game_stats, layout, false);
		TextView header = (TextView)res.findViewById(R.id.game_stats_header);
		header.setText(gsObj.matchHome);
		TextView date = (TextView)res.findViewById(R.id.game_stats_date);
		date.setText(gsObj.matchDate);
		TextView venue = (TextView)res.findViewById(R.id.game_stats_venuename);
		venue.setText(gsObj.venueName);
		
		TextView teamA = (TextView)res.findViewById(R.id.game_stats_teamAName);
		TextView teamB = (TextView)res.findViewById(R.id.game_stats_teamBName);
		TextView outcome = (TextView)res.findViewById(R.id.game_stats_outcome);
		TextView attendance = (TextView)res.findViewById(R.id.game_stats_attendance);
		TextView teamAStats = (TextView)res.findViewById(R.id.game_stats_teamAStats);
		TextView teamBStats = (TextView)res.findViewById(R.id.game_stats_teamBStats);
		TextView teamALineup = (TextView)res.findViewById(R.id.game_stats_teamALineup);
		TextView teamBLineup = (TextView)res.findViewById(R.id.game_stats_teamBLineup);
		final ListView teamAIndiv = (ListView)res.findViewById(R.id.game_stats_teamAIndiv);
		final ListView teamBIndiv = (ListView)res.findViewById(R.id.game_stats_teamBIndiv);
		final Button showStatsA = (Button)res.findViewById(R.id.show_team_stats);
		final Button showIndivA = (Button)res.findViewById(R.id.show_indiv_stats);
		final Button showStatsB = (Button)res.findViewById(R.id.show_team_stats2);
		final Button showIndivB = (Button)res.findViewById(R.id.show_indiv_stats2);
		final RelativeLayout teamABase = (RelativeLayout)res.findViewById(R.id.team1_stats_base);
		final RelativeLayout teamBBase = (RelativeLayout)res.findViewById(R.id.team2_stats_base);
		final LinearLayout teamAButtons = (LinearLayout)res.findViewById(R.id.category_sub_base);
		final LinearLayout teamBButtons = (LinearLayout)res.findViewById(R.id.category_sub_base2);		
		if(gsObj.isPlayed)
		{ 
			final ArrayList<String> list = new ArrayList<String>();
		    for(String name : gsObj.teamAIndivStats.keySet())
		    {
		    	list.add(gsObj.teamAIndivStats.get(name));
		    }
		    final ArrayList<String> listB = new ArrayList<String>();
		    for(String name : gsObj.teamBIndivStats.keySet())
		    {
		    	listB.add(gsObj.teamBIndivStats.get(name));
		    }
		    Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		    Collections.sort(listB, String.CASE_INSENSITIVE_ORDER);
		    final ArrayAdapter<String> adapterA = new ArrayAdapter<String>(gsObj.cont,
		            android.R.layout.simple_list_item_1, list);
		    teamAIndiv.setAdapter(adapterA);
		    final ArrayAdapter<String> adapterB = new ArrayAdapter<String>(gsObj.cont,
		            android.R.layout.simple_list_item_1, listB);
		    teamBIndiv.setAdapter(adapterB);
		    teamAIndiv.setOnTouchListener(new ListView.OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	            	int action = event.getAction();
	                switch (action) {
	                case MotionEvent.ACTION_DOWN:
	                    // Disallow ScrollView to intercept touch events.
	                    v.getParent().requestDisallowInterceptTouchEvent(true);
	                    break;

	                case MotionEvent.ACTION_UP:
	                    // Allow ScrollView to intercept touch events.
	                    v.getParent().requestDisallowInterceptTouchEvent(false);
	                    break;
	                }

	                // Handle ListView touch events.
	                v.onTouchEvent(event);
	                return true;
	            }
	        });
		    teamBIndiv.setOnTouchListener(new ListView.OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	            	int action = event.getAction();
	                switch (action) {
	                case MotionEvent.ACTION_DOWN:
	                    // Disallow ScrollView to intercept touch events.
	                    v.getParent().requestDisallowInterceptTouchEvent(true);
	                    break;

	                case MotionEvent.ACTION_UP:
	                    // Allow ScrollView to intercept touch events.
	                    v.getParent().requestDisallowInterceptTouchEvent(false);
	                    break;
	                }

	                // Handle ListView touch events.
	                v.onTouchEvent(event);
	                return true;
	            }
	        });
			outcome.setText(gsObj.winner + " won " + gsObj.score);
			if(gsObj.attendance > 0)
			{
				attendance.setText("Attendance: " + gsObj.attendance);
			}
			else
			{
				attendance.setVisibility(View.GONE);
			}
			teamAIndiv.setVisibility(View.GONE);
			teamBIndiv.setVisibility(View.GONE);
			teamAStats.setText(gsObj.teamAStats);
			teamBStats.setText(gsObj.teamBStats);
			teamB.setText(gsObj.teamBName);
			teamA.setText(gsObj.teamAName);
			teamALineup.setText(gsObj.teamALineup);
			teamBLineup.setText(gsObj.teamBLineup);
			showStatsA.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					teamAIndiv.setVisibility(View.GONE);
					teamABase.setVisibility(View.VISIBLE);
				}
			});
			showIndivA.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					teamAIndiv.setVisibility(View.VISIBLE);
					teamABase.setVisibility(View.GONE);
				}
			});
			showStatsB.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					teamBIndiv.setVisibility(View.GONE);
					teamBBase.setVisibility(View.VISIBLE);
				}
			});
			showIndivB.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					teamBIndiv.setVisibility(View.VISIBLE);
					teamBBase.setVisibility(View.GONE);
				}
			});
		}
		else
		{
			outcome.setText("No stats available for this game. Either it hasn't been played yet, is currently in progress, or the stats aren't available. If it's not the latter, stats will be available soon after the game.");
			attendance.setText("For live scores go to \"Team Lookup/Team Information\"");
			attendance.setTextSize(16);
			attendance.setTextColor(Color.parseColor("#FF3333"));
			teamAStats.setVisibility(View.GONE);
			teamBStats.setVisibility(View.GONE);
			teamA.setVisibility(View.GONE);
			teamB.setVisibility(View.GONE);
			teamALineup.setVisibility(View.GONE);
			teamBLineup.setVisibility(View.GONE);
			teamAIndiv.setVisibility(View.GONE);
			teamBIndiv.setVisibility(View.GONE);
			teamAButtons.setVisibility(View.GONE);
			teamBButtons.setVisibility(View.GONE);
		} 
		layout.addView(res);
	}
}

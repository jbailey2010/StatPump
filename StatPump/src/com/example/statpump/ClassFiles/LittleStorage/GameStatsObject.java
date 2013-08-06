package com.example.statpump.ClassFiles.LittleStorage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

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
	public String matchDate;
	public String matchHome;
	public int matchId;
	public APIObject apiObj;
	public PlayerSearchObject psObj;
	public Context cont;
	public String matchURL;
	public boolean isPlayed = true;
	public int attendance = 0;
	public String venueName;
	public String teamAName;
	public String teamBName;
	public int teamAID;
	public int teamBID;
	public String teamAStats;
	public String teamBStats;
	//NOT SET BELOW
	public String teamALineup;
	public String teamBLineup;
	public Map<String, String> teamAIndivStats = new HashMap<String, String>();
	public Map<String, String> teamBIndivStats = new HashMap<String, String>();
	//NOT SET ABOVE
	public String winner;
	public String score;
	
	
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
			System.out.println(g.isPlayed);
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
				g.attendance = Integer.parseInt(elem.attr("value"));
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
			   //Call some fn here...
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
}

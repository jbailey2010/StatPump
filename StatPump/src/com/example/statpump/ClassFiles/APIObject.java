package com.example.statpump.ClassFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.statpump.statpump.R;
import com.example.statpump.InterfaceAugmentation.StatWellUsage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TwoLineListItem;
/**
 * Nothing fancy, stores all of the api interaction data
 * @author Jeff
 *
 */
public class APIObject 
{
	//Interface objects to make population cleaner
	public Spinner sportSpinner;
	public Spinner team1Spinner;
	public Spinner team2Spinner;
	public Context context;
	//Maps sport to sport id
	public Map<String, Integer> sportIDMap = new HashMap<String, Integer>();
	public Map<String, Integer> teamIDMap = new HashMap<String, Integer>();
	public List<String> opponents = new ArrayList<String>();
	public List<String> teamSet1 = new ArrayList<String>();
	public String sport;
	public String sportURL;
	public int sportID;
	//Team string and id
	public String team1;
	public int team1ID;
	public String team2;
	public int team2ID;
	public String matchDate;
	public String matchHome;
	public int matchID;
	//Venue data
	public int venueID;
	//Year ID for later queries
	public int yearID;
	public int roundID;
	public String yearStart;
	public String yearEnd;
	//Other data
	public String favoriteTeam;
	public String statwellSetting;
	//The constant part of the url for the query
	public String urlBase = "http://api.globalsportsmedia.com/";
	public String urlValidate = "&authkey=865c0c0b4ab0e063e5caa3387c1a8741&username=statp";
	//Sets up the stat objects
	public Map<String, String> fixes = new HashMap<String, String>();
	public List<String> ignore = new ArrayList<String>();
	/** 
	 * Maps the sports to ids...etc. Basic set up stuff.
	 */
	public APIObject(Context cont)
	{
		context = cont;
		/**
		 * The hashmap maps to the id of the sport (coverage, click league), to 
		 * be used to find the id of the year
		 */
		sportIDMap.put("American Football - NFL", 1);
		sportIDMap.put("American Football - NCAA Division 1", 5);
		sportIDMap.put("Baseball - MLB", 1);
		sportIDMap.put("Basketball - NBA", 13);
		//sportIDMap.put("Basketball - NCAA Division 1", 43);
		sportIDMap.put("Hockey - NHL", 383);
		sportIDMap.put("Soccer - Premier League", 8);
		//sportIDMap.put("Soccer - UEFA Champions League", 10);
		//sportIDMap.put("Soccer - Primera Division", 7);
		//sportIDMap.put("Soccer - MLS", 33);
		
		//Baseball stuff
		fixes.put("baseball/batting_at_bats", "At Bats");
		fixes.put("baseball/batting_runs", "Runs");
		fixes.put("baseball/batting_hits", "Hits");
		fixes.put("baseball/batting_runs_batted_in", "RBIs");
		fixes.put("baseball/batting_runs_battled_in", "RBIs");
		fixes.put("baseball/batting_left_on_base", "Batters Stranded");
		fixes.put("baseball/pitching_wins", "Wins");
		fixes.put("baseball/pitching_losses", "Losses");
		fixes.put("baseball/pitching_innings_pitcher", "IP");
		fixes.put("baseball/pitching_hits_allowed", "Hits Allowed");
		fixes.put("baseball/pitching_base_on_balls", "Walks");
		fixes.put("baseball/pitching_strikeouts", "Strikeouts");
		fixes.put("baseball/pitching_home_runs_allowed", "HR Allowed");
		fixes.put("baseball/pitching_saves", "Saves");
		fixes.put("baseball/pitching_earned_runs", "Earned Runs");
		ignore.add("baseball/pitching_appearances");
		fixes.put("baseball/batting_average", "Batting Avg");
		fixes.put("baseball/pitching_era", "ERA");
		ignore.add("baseball/batting_games");
		fixes.put("baseball/Doubles", "Double");
		fixes.put("baseball/Home Runs", "Home Run");
		
		//Football
		fixes.put("american_football/offense_passing_yards", "Passing Yards");
		fixes.put("american_football/defense_tackles", "Tackles");
		fixes.put("american_football/rushing_yards", "Rushing Yards");
		fixes.put("american_football/receiving_yards", "Receiving Yards");
		fixes.put("american_football/defense_interceptions", "Interceptions");
		fixes.put("american_football/fumbles", "Fumbles");
		fixes.put("american_football/punting_yards", "Punting Yards");
		fixes.put("american_football/return_yards", "Return Yards");
		fixes.put("american_football/offense_passing_touchdowns", "Passing Touchdowns");
		fixes.put("american_football/rushing_touchdowns", "Rushing Touchdowns");
		fixes.put("american_football/receiving_touchdowns", "Receiving Touchdowns");
		fixes.put("american_football/defense_sacks", "Sacks");
		fixes.put("american_football/defense_forced_fumbles", "Forced Fumbles");
		fixes.put("american_football/punting_total_punts", "Total Punts");
		fixes.put("american_football/kicking_points", "Total Points From Kicking");
		fixes.put("american_football/punting_longest_punt", "Longest Punt");
		fixes.put("american_football/receiving_longest_reception", "Longest Catch");
		fixes.put("american_football/kicking_longest_kick", "Longest Kick");
		fixes.put("american_football/rushing_longest_run", "Longest Run");
		fixes.put("americanfootball/offense_passing_yards", "Passing Yards");
		fixes.put("americanfootball/defense_tackles", "Tackles");
		fixes.put("americanfootball/rushing_yards", "Rushing Yards");
		fixes.put("americanfootball/receiving_yards", "Receiving Yards");
		fixes.put("americanfootball/defense_interceptions", "Interceptions");
		fixes.put("americanfootball/fumbles", "Fumbles");
		fixes.put("americanfootball/punting_yards", "Punting Yards");
		fixes.put("americanfootball/return_yards", "Return Yards");
		fixes.put("americanfootball/offense_passing_touchdowns", "Passing TDs");
		fixes.put("americanfootball/rushing_touchdowns", "Rushing TDs");
		fixes.put("americanfootball/receiving_touchdowns", "Receiving TDs");
		fixes.put("americanfootball/defense_sacks", "Sacks");
		fixes.put("americanfootball/defense_forced_fumbles", "Forced Fumbles");
		fixes.put("americanfootball/punting_total_punts", "Total Punts");
		fixes.put("americanfootball/kicking_points", "Total Points From Kicking");
		fixes.put("americanfootball/punting_longest_punt", "Longest Punt");
		fixes.put("americanfootball/receiving_longest_reception", "Longest Catch");
		fixes.put("americanfootball/kicking_longest_kick", "Longest Kick");
		fixes.put("americanfootball/rushing_longest_run", "Longest Run");
		
		//Basketball
		fixes.put("basketball/minutes", "Minutes");
		fixes.put("basketball/points", "Points Per Game");
		fixes.put("basketball/assists", "Assists Per Game");
		fixes.put("basketball/rebounds", "Rebounds Per Game");
		fixes.put("basketball/turnovers", "Turnovers Per Game");
		fixes.put("basketball/steals", "Steals Per Game");
		fixes.put("basketball/blocks", "Blocks Per Game");
		fixes.put("basketball/fouls", "Fouls Per Game");
		fixes.put("basketball/blocks_received", "Blocks Received Per Game");
		fixes.put("basketball/defense_rebounds", "Defensive Rebounds Per Game");
		fixes.put("basketball/offense_rebounds", "Offensive Rebounds Per Game");
		fixes.put("basketball/field_goals_shooting", "Field Goal Shooting Percentage");
		fixes.put("basketball/two_point_shooting", "Two Point Shooting Percentage");
		fixes.put("basketball/three_point_shooting", "Three Point Shooting Percentage");
		fixes.put("basketball/free_throw_shooting", "Free Throw Shooting Percentage");
		
		//Soccer
		fixes.put("soccer/yellow_cards", "Yellow Cards");
		fixes.put("soccer/red_cards", "Red Cards");
		fixes.put("soccer/goals", "Goals");
		fixes.put("soccer/assists", "Assists");
		
		//Hockey
		fixes.put("hockey/assists", "Assists");
		fixes.put("hockey/points", "Points");
		fixes.put("hockey/goals", "Goals");
		fixes.put("hockey/plus_minus", "Plus/Minus");
		fixes.put("hockey/penalties", "Penalties");
		fixes.put("hockey/faceoff_percentage", "Faceoff Percentage");
		fixes.put("hockey/goalkeeper_saves_percentage", "Saves Percentage");
		fixes.put("hockey/power_play_goals", "Power Play Goals");
		fixes.put("hockey/short_handed_goals", "Short Handed Goals");
		fixes.put("hockey/time_on_ice", "Time On Ice");
		fixes.put("hockey/shutouts", "Shutouts");
		fixes.put("hockey/goalkeeper_appearances", "Goalkeeper Appearances");
		fixes.put("hockey/goalkeeper_wins", "Goalkeeper Wins");
		fixes.put("hockey/goalkeeper_losses", "Goalkeeper Losses");
		fixes.put("hockey/shots_on_goal_percentage", "Percentage Of Shots On Goal");
		fixes.put("hockey/time_on_ice_game", "Time On Ice Per Game");
	}
	
	/**
	 * Clears all stored data
	 */
	public void clearObject()
	{
		this.teamIDMap.clear();
		this.sport = null;
		this.sportURL = null;
		this.sportID = 0;
		this.team1 = null;
		this.team1ID=0;
		this.team2 = null;
		this.team2ID = 0;
		this.yearID=0;
		this.teamSet1 = null;
		this.opponents = null;
		this.yearID = 0;
		this.yearStart = "";
		this.yearEnd = "";
		this.favoriteTeam = "";
		this.statwellSetting = "";
		this.matchDate = "";
		this.matchHome="";
		this.matchID = 0;
	}
	
	/**
	 * Sets the interface objects
	 */
	public void setUpObject(Spinner sport, Spinner team1, Spinner team2)
	{
		this.sportSpinner = sport;
		this.team1Spinner = team1;
		this.team2Spinner = team2;
	}
	
	/**
	 * Sets the sport selection, getting the relevant data
	 * @param selection
	 */
	public void sportSelected(String selection, Activity cont)
	{
		String sport = selection.split(" \\(")[0];
		String sportURL = sport.split(" - ")[0].toLowerCase().replaceAll(" ", "");
		this.sport = sport;
		this.sportURL = sportURL;
		this.sportID = this.sportIDMap.get(this.sport);
		System.out.println("New sport is "+ this.sport + ", " + this.sportURL + ", " + this.sportID);
		APIInteraction.getSeasonId(this);
		this.teamIDMap.clear();
		APIInteraction.getTeams(this, cont);
	}
	
	/**
	 * Forms the url for getting a valid season
	 */
	public String formGetSeasonUrl()
	{
		return this.sportURL + "/get_seasons?id=" + this.sportID + "&type=competition";
	}
	
	/**
	 * Forms the get teams URL
	 * @return
	 */
	public String formGetTeamUrl()
	{
		return this.sportURL + "/get_tables?id=" + this.yearID + "&type=season&tabletype=total";
	}
	
	/**
	 * forms the get match URL
	 * @return
	 */
	public String formGetMatchUrl()
	{
		return this.sportURL + "/get_matches?id=" + this.team1ID + "&type=team&start_date=" + this.yearStart + "&end_date=" + this.yearEnd;
	}
	
	/**
	 * Forms the get match URL (specific match)
	 * @return
	 */
	public String formGetSpecMatchUrl()
	{
		if(!this.sportURL.contains("soccer"))
		{
			return this.sportURL + "/get_matches?id=" + this.matchID + "&type=match&detailed=yes&statistics=yes";
		}
		return this.sportURL + "/get_matches?id=" + this.matchID + "&type=match&detailed=yes";
	}
	
	/**
	 * Forms the get venue url
	 * @return
	 */
	public String formGetVenueUrl()
	{
		int idStr = -1;
		for(String name : this.teamIDMap.keySet())
		{
			String team = this.matchHome.split(" at ")[1];
			if(name.equals(team))
			{
				idStr = this.teamIDMap.get(name);
			}
		}
		return this.sportURL + "/get_venues?id=" + idStr + "&type=team&detailed=yes";
	}
	
	/**
	 * Forms the get team info url
	 */
	public String formGetTeamInfoUrl(int teamID)
	{
		return this.sportURL + "/get_teams?id=" + teamID + "&type=team&detailed=yes";
	}
	
	/**
	 * Forms the get match info url (for statistics)
	 * @param matchID
	 * @return
	 */
	public String formGetMatchInfoDoneUrl(int matchID)
	{
		return this.sportURL + "/get_matches?id=" + matchID + "&type=match&detailed=yes&statistics=yes";
	}
	
	/**
	 * Forms the get referees query
	 * @param matchID
	 * @return
	 */
	public String formGetRefsUrl(int matchID)
	{
		return this.sportURL + "/get_referees?type=match&id=" + matchID;
	}
	
	/**
	 *Forms the get squads url given a team id
	 */
	public String formGetSquadUrl(int team1id2)
	{
		return this.sportURL + "/get_Squads?id=" + team1id2 + "&type=team&detailed=yes&statistics=yes&active=yes";
	}
	
	/**
	 * Forms the get team stats url
	 * @param teamID
	 * @return
	 */
	public String formGetTeamStatsUrl(int teamID)
	{
		return this.sportURL + "/get_player_statistics?id=" + this.roundID + "&type=round&team_id=" + teamID;
	}
	
	/**
	 * Sets the year ID after the asynctask ends
	 */
	public void setSeasonId(String id)
	{
		System.out.println("Setting id as " + id);
		if(id == null)
		{
			return;
		}
		this.yearID = Integer.parseInt(id);
	}

	/**
	 * Sets the team id map
	 */
	public void setTeamSet(APIObject result) {
		this.teamIDMap = result.teamIDMap;
		List<String>teams = new ArrayList<String>();
		teams.add("Select a Team");
		for(String team : this.teamSet1)
		{
			teams.add(team);
		}
		//Setting the adapter
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this.context, 
				android.R.layout.simple_spinner_dropdown_item, teams);
		this.team1Spinner.setAdapter(spinnerArrayAdapter);
	}
	
	/**
	 * Sets the opponents to the second spinner
	 * @param result
	 */
	public void setOpponents(APIObject result)
	{
		this.opponents = result.opponents;
		List<String>teams = new ArrayList<String>();
		teams.add("Select a Team");
		for(String team : this.opponents)
		{
			if(this.teamIDMap.containsKey(team))
			{
				teams.add(team);
			}
		}
		//Setting the adapter
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this.context, 
				android.R.layout.simple_spinner_dropdown_item, teams);
		this.team2Spinner.setAdapter(spinnerArrayAdapter);
	} 
	
	/**
	 * Calls the function that will spawn the asynctask
	 */
	public void getOpponentsInit(String team1, Activity act)
	{
		this.team1 = team1;
		this.team1ID = this.teamIDMap.get(this.team1);
		APIInteraction.getOpponents(this, act);
	}

	/**
	 * Works the matchups, if there's 1 no dialog, if multiple the user picks
	 * @param result
	 * @param act
	 * @param o
	 */
	public void handleMatchups(final List<String> result, final Activity act, final APIObject o) 
	{
		
		if(result.size() == 1)
		{
			String total = result.get(0);
			String[] set = total.split("%%%");
    		this.matchDate = set[0];
    		this.matchID = Integer.parseInt(set[1]);
    		this.matchHome = set[2];
		}
		else
		{
			final Dialog dialog = new Dialog(act, R.style.RoundCornersFull);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.match_date_selection);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		    lp.copyFrom(dialog.getWindow().getAttributes());
		    lp.width = WindowManager.LayoutParams.FILL_PARENT;
		    dialog.getWindow().setAttributes(lp);
		    dialog.setCancelable(false);
			dialog.show();
			final Spinner dates = (Spinner)dialog.findViewById(R.id.date_spinner);
			List<Map<String, String>> data = new ArrayList<Map<String, String>>();
			for (String date : result) {
			    Map<String, String> datum = new HashMap<String, String>(2);
			    String[] set = date.split("%%%");
			    datum.put("title", set[2]);
			    datum.put("date", set[0]);
			    data.add(datum);
			}
			SimpleAdapter adapter = new SimpleAdapter(act, data,
			                                          android.R.layout.simple_list_item_2,
			                                          new String[] {"title", "date"},
			                                          new int[] {android.R.id.text1,
			                                                     android.R.id.text2});
			dates.setAdapter(adapter);
			Button submit = (Button)dialog.findViewById(R.id.match_date_selection_submit);
			submit.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					HashMap<String, String> data = (HashMap<String, String>) dates.getSelectedItem();
					o.matchHome = data.get("title");
					o.matchDate = data.get("date");
					for(String results : result)
					{ 
						if(results.contains(o.matchHome) && results.contains(o.matchDate))
						{
							String[] set = results.split("%%%");
							o.matchID = Integer.parseInt(set[1]);
							break;
						}
					}
					dialog.dismiss();
				}
			});
		}
	}
}

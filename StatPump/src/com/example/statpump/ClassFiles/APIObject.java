package com.example.statpump.ClassFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
	//Year ID for later queries
	public int yearID;
	public String yearStart;
	public String yearEnd;
	//Other data
	public String favoriteTeam;
	//The constant part of the url for the query
	public String urlBase = "http://api.globalsportsmedia.com/";
	public String urlValidate = "&authkey=865c0c0b4ab0e063e5caa3387c1a8741&username=statp";
	
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
		sportIDMap.put("Basketball - NCAA Division 1", 43);
		sportIDMap.put("Hockey - NHL", 383);
		sportIDMap.put("Soccer - Premier League", 8);
		sportIDMap.put("Soccer - UEFA Champions League", 10);
		sportIDMap.put("Soccer - Primera Division", 7);
		sportIDMap.put("Soccer - MLS", 33);
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
	 * Sets the year ID after the asynctask ends
	 */
	public void setSeasonId(String id)
	{
		System.out.println("Setting id as " + id);
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
			teams.add(team);
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
}

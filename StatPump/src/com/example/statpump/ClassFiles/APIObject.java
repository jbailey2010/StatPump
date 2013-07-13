package com.example.statpump.ClassFiles;

import java.util.HashMap;
import java.util.Map;
/**
 * Nothing fancy, stores all of the api interaction data
 * @author Jeff
 *
 */
public class APIObject 
{
	//Maps sport to sport id
	public Map<String, Integer> sportIDMap = new HashMap<String, Integer>();
	public String sport;
	public int sportID;
	//Team string and id
	public String team1;
	public int team1ID;
	public String team2;
	public int team2ID;
	//Year ID for later queries
	public int yearID;
	//The constant part of the url for the query
	public String urlBase = "http://api.globalsportsmedia.com/";
	public String urlValidate = "&authkey=865c0c0b4ab0e063e5caa3387c1a8741&username=statp";
	
	/**
	 * Maps the sports to ids...etc. Basic set up stuff.
	 */
	public APIObject()
	{
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
	 * Sets the sport selection, getting the relevant data
	 * @param selection
	 */
	public void sportSelected(String selection)
	{
		String sport = selection.split(" \\(")[0];
		this.sport = sport;
		this.sportID = this.sportIDMap.get(this.sport);
	}
}

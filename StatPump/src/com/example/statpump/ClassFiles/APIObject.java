package com.example.statpump.ClassFiles;

import java.util.HashMap;
import java.util.Map;

public class APIObject 
{
	public Map<String, Integer> sportIDMap = new HashMap<String, Integer>();
	public String sport;
	public String team1;
	public String team2;
	public String urlBase = "http://api.globalsportsmedia.com/";
	public String urlValidate = "&authkey=865c0c0b4ab0e063e5caa3387c1a8741&username=statp";
	
	public APIObject()
	{
		/**
		 * Populate the hashmap
		 */
	}
}

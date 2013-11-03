package com.example.statpump.ClassFiles;

import java.io.IOException;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.XPatherException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


public class APIInteraction 
{
	/**
	 * Gets the document given the url (middle part)
	 * @param url
	 * @param queryObj
	 * @return
	 * @throws IOException
	 */
	public static Document getXML(String url, APIObject queryObj) throws IOException
	{
		System.out.println(queryObj.urlBase + url + queryObj.urlValidate);
		Document doc = Jsoup.connect(queryObj.urlBase + url + queryObj.urlValidate).timeout(0).get();
		return Jsoup.parse(doc.toString(), "", Parser.xmlParser());
	}
	
	/**
	 * Gets a given attribute from xml
	 * @param doc
	 * @param params
	 * @param attr
	 * @return
	 */
	public static String parseXML(Document doc, String params, String attr)
	{
		StringBuilder result = new StringBuilder(5000);
        Elements links = doc.select(params);
        for (Element element : links) 
        {
        	result.append(element.attr(attr)+"\n");
        }
        return result.toString();
	}
	
	/**
	 * Gets all of the matchups of a team
	 */
	public static List<String> parseXMLSetOpponents(Document doc, String params, APIObject obj)
	{ 
        Elements links = doc.select(params);
        List<String> result = new ArrayList<String>();
        for (Element element : links) 
        {
        	if((element.attr("team_a_name").equals(obj.team1) && element.attr("team_b_name").equals(obj.team2))
        			|| (element.attr("team_a_name").equals(obj.team2) && element.attr("team_b_name").equals(obj.team1)))
        	{
        		String date = element.attr("date_utc");
        		String id = element.attr("match_id");
        		String home = obj.team1 + " at " + obj.team2;
        		if(element.attr("team_a_name").equals(obj.team1))
        		{
        			home = obj.team2 + " at " + obj.team1;
        		}
        		result.add(date + "%%%" + id + "%%%" + home);
        	}
        }
        return result;
	}
	
	/**
	 * Returns the opponents of a team
	 */
	public static List<String> parseXMLOpp(Document doc, String params, String name)
	{
        Elements links = doc.select(params);
        List<String> teams = new ArrayList<String>();
        for (Element element : links) 
        { 
        	if(!teams.contains(element.attr("team_a_name")) && element.attr("team_b_name").equals(name))
        	{
        		teams.add(element.attr("team_a_name"));
      		}
        	else if(element.attr("team_a_name").equals(name) && !teams.contains(element.attr("team_b_name")))
        	{
        		teams.add(element.attr("team_b_name"));
        	}
        	
        }
        return teams;
	}

	/**
	 * Calls the season id asynctask
	 * @param obj
	 */
	public static void getSeasonId(APIObject obj)
	{
		APIInteraction holder = new APIInteraction();
		ParseSeasonID task = holder.new ParseSeasonID(obj);
		task.execute();
	}

	 /**
	  * Does the season id parsing
	  * @author Jeff
	  *
	  */
	public class ParseSeasonID extends AsyncTask<Object, Void, String> 
	{
			APIObject obj;
			String sec = null;
			String secStart = null;
			String secEnd = null;
		    public ParseSeasonID(APIObject object) 
		    {
		        obj = object;
		    }
		    
			@Override
			protected void onPreExecute(){ 
			   super.onPreExecute();  
			}
	
			@Override
			protected void onPostExecute(String result){
			   obj.setSeasonId(result, sec, secStart, secEnd);
			   super.onPostExecute(result);
			}
			
		    @Override
		    protected String doInBackground(Object... data) 
		    {
		    	try {
					Document doc = getXML(obj.formGetSeasonUrl(), obj);
					String dataSet = parseXML(doc, "season", "season_id");
					String start = parseXML(doc, "season", "start_date");
					String end = parseXML(doc, "season", "end_date");
					obj.yearStart = start.split("\n")[start.split("\n").length-1];
					obj.yearEnd = end.split("\n")[end.split("\n").length-1];
					String result = dataSet.split("\n")[dataSet.split("\n").length-1];
					if(dataSet.split("\n").length >= 2)
					{
						sec = dataSet.split("\n")[dataSet.split("\n").length - 2];
						secStart = start.split("\n")[start.split("\n").length-2];
						secEnd = end.split("\n")[end.split("\n").length-2];
					}
					obj.yearID = Integer.parseInt(result);

					return result;
				} catch (IOException e) {
					e.printStackTrace();
				} 
				return null; 
		    }
	  }
	
	/**
	 * Calls the get teams asynctask
	 * @param obj
	 * @param act
	 */
	public static void getTeams(APIObject obj, Activity act)
	{
		APIInteraction holder = new APIInteraction();
		ParseTeamID task = holder.new ParseTeamID(obj, act);
		task.execute(obj);
	}
	
	/**
	 * Populates the team/id map
	 * @author Jeff
	 *
	 */
	public class ParseTeamID extends AsyncTask<Object, Void, APIObject> 
	{
			APIObject obj;
			Activity a;
			ProgressDialog pda;
		    public ParseTeamID(APIObject object, Activity act) 
		    {
		        obj = object;
		        a = act;
		        pda = new ProgressDialog(act);
		        pda.setCancelable(false);
		        pda.setMessage("Please wait, fetching the teams...");
		        pda.show();
		    }
		    
			@Override
			protected void onPreExecute(){ 
			   super.onPreExecute();  
			}
	
			@Override
			protected void onPostExecute(APIObject result){
			   super.onPostExecute(result);
			   pda.dismiss();
			   obj.setTeamSet(result);
			}
			 
		    @Override
		    protected APIObject doInBackground(Object... data) 
		    {
		    	APIObject obj = (APIObject)data[0];
		    	try {
		    		long time = System.nanoTime();
					Document doc = getXML(obj.formGetTeamUrl(), obj);
					String dataSet = parseXML(doc, "ranking", "team_id");
					String[] idSet = dataSet.split("\n");
					System.out.println("ID set size "  + idSet.length);
					String secSet = parseXML(doc, "ranking", "club_name");
					String[] teamSet = secSet.split("\n");
					System.out.println("Team set size " + teamSet.length);
					if(teamSet.length <= 2)
					{
						doc = getXML(obj.formGetLastTeamUrl(), obj);
						dataSet = parseXML(doc, "ranking", "team_id");
						idSet = dataSet.split("\n");
						secSet = parseXML(doc, "ranking", "club_name");
						teamSet = secSet.split("\n");
					}
					obj.roundID = Integer.valueOf(parseXML(doc, "round", "round_id").split("\n")[0]);
					List<String> teams = Arrays.asList(teamSet);
					Map<String, Integer> inter = new HashMap<String, Integer>();
					for(int i = 0; i < idSet.length; i++)
					{
						inter.put(teamSet[i], Integer.parseInt(idSet[i]));
					}
					Collections.sort(teams, String.CASE_INSENSITIVE_ORDER);
					obj.teamSet1 = teams;
					for(int i = 0; i < idSet.length; i++)
					{
						obj.teamIDMap.put(teams.get(i), inter.get(teams.get(i)));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return obj;
		    }
	  }
	
	/**
	 * Spawns the get opponent asynctask
	 * @param obj
	 * @param act
	 */
	public static void getOpponents(APIObject obj, Activity act)
	{
		APIInteraction holder = new APIInteraction();
		ParseOpponentID task = holder.new ParseOpponentID(obj, act);
		task.execute(obj);
	}
	
	/**
	 * Gets the opponents
	 * @author Jeff
	 *
	 */
	public class ParseOpponentID extends AsyncTask<Object, Void, APIObject> 
	{
			APIObject obj;
			Activity a;
			ProgressDialog pda;
		    public ParseOpponentID(APIObject object, Activity act) 
		    {
		        obj = object;
		        a = act;
		        pda = new ProgressDialog(act);
		        pda.setCancelable(false);
		        pda.setMessage("Please wait, fetching the opponents...");
		        pda.show();
		    }
		    
			@Override
			protected void onPreExecute(){ 
			   super.onPreExecute();  
			}
	
			@Override
			protected void onPostExecute(APIObject result){
			   super.onPostExecute(result);
			   pda.dismiss();
			   obj.setOpponents(result);
			}
			 
		    @Override
		    protected APIObject doInBackground(Object... data) 
		    {
		    	APIObject obj = (APIObject)data[0];
		    	try {
					Document doc = getXML(obj.formGetMatchUrl(), obj);
					List<String> dataSet = parseXMLOpp(doc, "match", obj.team1);
					if(dataSet.size() <= 2)
					{
						doc = getXML(obj.formGetMatchLastUrl(), obj);
						dataSet = parseXMLOpp(doc, "match", obj.team1);
					}
					System.out.println("Opponent set size " + dataSet.size());
					Collections.sort(dataSet, String.CASE_INSENSITIVE_ORDER);
					obj.opponents = dataSet;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return obj;
		    }
	  }
	
	/**
	 * Spawns the asynctask that gets the dates
	 * @param obj
	 * @param act
	 */
	public static void getOpponentDates(APIObject obj, Activity act)
	{
		APIInteraction holder = new APIInteraction();
		ParseOpponentDates task = holder.new ParseOpponentDates(obj, act);
		task.execute(obj);
	}
	
	/**
	 * Parses for dates of matchups
	 * @author Jeff
	 *
	 */
	public class ParseOpponentDates extends AsyncTask<Object, Void, List<String>> 
	{
			APIObject obj;
			Activity a;
			ProgressDialog pda;
		    public ParseOpponentDates(APIObject object, Activity act) 
		    {
		        obj = object;
		        a = act;
		        pda = new ProgressDialog(act);
		        pda.setCancelable(false);
		        pda.setMessage("Please wait, fetching the matches...");
		        pda.show();
		    }
		    
			@Override
			protected void onPreExecute(){ 
			   super.onPreExecute();  
			}
	
			@Override
			protected void onPostExecute(List<String> result){
			   super.onPostExecute(result);
			   pda.dismiss();
			   obj.handleMatchups(result, a, obj);
			   //obj.setOpponents(result);
			}
			 
		    @Override
		    protected List<String> doInBackground(Object... data) 
		    {
		    	APIObject obj = (APIObject)data[0];
		    	try {
					Document doc = getXML(obj.formGetMatchUrl(), obj);
					List<String> dataSet = parseXMLSetOpponents(doc, "match", obj);
					if(dataSet.size() == 0)
					{
						doc = getXML(obj.formGetMatchLastUrl(), obj);
						dataSet = parseXMLSetOpponents(doc, "match", obj);
					}
					System.out.println("Matchup size: " + dataSet.size());
					return dataSet;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return null;
		    }
	  }
}

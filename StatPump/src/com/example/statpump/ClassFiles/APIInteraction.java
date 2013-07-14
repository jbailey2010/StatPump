package com.example.statpump.ClassFiles;

import java.io.IOException;


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
	 * Parses the xml if it matches a condition
	 */
	public static String parseXML2Conditions(Document doc, String params, String attr, String condAttr, String cond, 
			String secAttr, String secCond)
	{
		StringBuilder result = new StringBuilder(5000);
		Elements links = doc.select(params);
        for (Element element : links) 
        {
        	if(element.attr(condAttr).equals(cond) && !element.attr(secAttr).equals(secCond))
        	{
        		result.append(element.attr(attr)+"\n");
        	}
        }
		return result.toString();
	}

	/**
	 * Calls the season id asynctask
	 * @param obj
	 */
	public static void getSeasonId(APIObject obj)
	{
		APIInteraction holder = new APIInteraction();
		ParseSeasonID task = holder.new ParseSeasonID(obj);
		System.out.println("Executing season task");
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
			   obj.setSeasonId(result);
			   super.onPostExecute(result);
			}
			
		    @Override
		    protected String doInBackground(Object... data) 
		    {
		    	try {
					Document doc = getXML(obj.formGetSeasonUrl(), obj);
					String dataSet = parseXML(doc, "season", "season_id");
					String result = dataSet.split("\n")[dataSet.split("\n").length-1];
					obj.yearID = Integer.parseInt(result);
					return result;
				} catch (IOException e) {
					System.out.println("BUG");
					e.printStackTrace();
				} 
				return null; 
		    }
	  }
	
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
					Document doc = getXML(obj.formGetTeamUrl(), obj);
					String dataSet = parseXML(doc, "team", "team_id");
					String secSet = parseXML(doc, "team", "official_name");
					String[] idSet = dataSet.split("\n");
					System.out.println("ID set size "  + idSet.length);
					String[] teamSet = secSet.split("\n");
					System.out.println("Team set size " + teamSet.length);
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
}

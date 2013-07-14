package com.example.statpump.ClassFiles;

import java.io.IOException;


import java.util.HashMap;
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
			   super.onPostExecute(result);
			   obj.setSeasonId(result);
			}
			
		    @Override
		    protected String doInBackground(Object... data) 
		    {
		    	try {
					Document doc = getXML(obj.formGetSeasonUrl(), obj);
					String dataSet = parseXML(doc, "season", "season_id");
					return dataSet.split("\n")[dataSet.split("\n").length - 1];
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
					String[] teamSet = secSet.split("\n");
					for(int i = 0; i < idSet.length; i++)
					{
						obj.teamIDMap.put(teamSet[i], Integer.parseInt(idSet[i]));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return obj;
		    }
	  }
}

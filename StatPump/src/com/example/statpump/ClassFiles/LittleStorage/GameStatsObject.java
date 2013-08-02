package com.example.statpump.ClassFiles.LittleStorage;

import java.io.IOException;

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
			System.out.println(g.apiObj.teamIDMap.get(teamAName));
			g.teamBName = elem.attr("team_b_name");
			System.out.println(g.apiObj.teamIDMap.get(teamBName));
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
		if(g.apiObj.sportURL.contains("baseball"))
		{
			g.baseballIndiv(g, doc);
		}
		else if(g.apiObj.sportURL.contains("american_football"))
		{
			g.footballIndiv(g, doc);
		}
		else if(g.apiObj.sportURL.contains("basketball"))
		{
			g.basketballIndiv(g, doc);
		}
		else if(g.apiObj.sportURL.contains("soccer"))
		{
			g.soccerIndiv(g, doc);
		}
		return g;
	}
	
	/**
	 * Handles the soccer individual parsing
	 * @param g
	 * @param doc
	 */
	private void soccerIndiv(GameStatsObject g, Document doc) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Handles the basketball individual parsing
	 * @param g
	 * @param doc
	 */
	private void basketballIndiv(GameStatsObject g, Document doc) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Handles the football individual parsing
	 * @param g
	 * @param doc
	 */
	private void footballIndiv(GameStatsObject g, Document doc) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Handles the baseball individual parsing
	 * @param g
	 * @param doc
	 */
	private void baseballIndiv(GameStatsObject g, Document doc) {
		// TODO Auto-generated method stub
		
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

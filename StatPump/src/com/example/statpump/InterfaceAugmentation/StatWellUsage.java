package com.example.statpump.InterfaceAugmentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.http.client.protocol.ClientContext;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.statpump.R;
import com.example.statpump.ClassFiles.APIObject;
import com.example.statpump.ClassFiles.LittleStorage.PlayerSearchObject;
import com.example.statpump.ClassFiles.LittleStorage.TeamInfoObject;
import com.example.statpump.ClassFiles.LittleStorage.VenueInfoObject;
/**
 * Handles the usage of the statwell
 * @author Jeff
 *
 */
public class StatWellUsage 
{
	public static BounceListView playerList;
	
	/**
	 * Distributes work based on statwell selection
	 * @param obj
	 * @param cont
	 * @param po 
	 */
	public static void statWellInit(APIObject obj, Context cont, PlayerSearchObject po)
	{
		/*
		 * Player Information
		 * Team Information
		 * Player Statistics
		 * ----------------
		 * Game Information
		 * Game Statistics
		 * Venue Information
		 */
		if(obj.statwellSetting.equals("Team Information"))
		{
			teamInfo(obj, cont);
		}
		if(obj.statwellSetting.equals("Venue Information"))
		{
			venueInfo(obj, cont);
		}
		if(obj.statwellSetting.equals("Player Statistics"))
		{
			playerStats(obj, cont, po);
		}
		if(obj.statwellSetting.equals("Player Information"))
		{
			playerInfo(obj, cont, po);
		}
	}
	
	public static void playerInfo(APIObject obj, Context cont, PlayerSearchObject po)
	{
		listViewPlayers(obj, cont, po);
		/*playerData.put(name + "//" + position + "//" + team1 + "//" + number, id);
		 * String sub = set[0];
			    if(!set[3].equals(null) && !set[3].equals(" "))
			    {
			    	sub += ", #" + set[3];
			    }
			    datum.put("title", sub);
			    datum.put("date", set[1] + " - " +  set[2]);
		 */
		//HANDLE ONCLICK STUFF HERE
	}
	
	public static void playerStats(APIObject obj, Context cont, PlayerSearchObject po)
	{
		listViewPlayers(obj, cont, po);
		/*playerData.put(name + "//" + position + "//" + team1 + "//" + number, id);
		 * String sub = set[0];
			    if(!set[3].equals(null) && !set[3].equals(" "))
			    {
			    	sub += ", #" + set[3];
			    }
			    datum.put("title", sub);
			    datum.put("date", set[1] + " - " +  set[2]);
		 */
		//HANDLE ONCLICK STUFF HERE
	}
	
	/**
	 * Sets the listview up with players, sorted by numbers OR alphabetically
	 * @param obj
	 * @param cont
	 * @param po
	 */
	public static void listViewPlayers(APIObject obj, Context cont, PlayerSearchObject po)
	{
		LinearLayout layout = (LinearLayout)((Activity) cont).findViewById(R.id.statwell);
		View res = ((Activity) cont).getLayoutInflater().inflate(R.layout.player_list, layout, false);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		boolean isNumbers = true;
		int counter = 0;
		for(String key : po.players.keySet())
		{
			if(key.split("//")[3].equals(" "))
			{
				counter++;
			}
			if(counter > 3)
			{
				isNumbers = false;
				break;
			}
		}
		if(isNumbers)
		{
			PriorityQueue<String>inter = new PriorityQueue<String>(300, new Comparator<String>() 
					{
						@Override
						public int compare(String a, String b) 
						{
							int num1 = Integer.parseInt(a.split("//")[3]);
							int num2 = Integer.parseInt(b.split("//")[3]);
							if(!a.split("//")[3].equals(" ") && !b.split("//")[3].equals(" "))
							{
								if (num1 > num2)
							    {
							        return 1;
							    }
							    if (num1 < num2)
							    {
							    	return -1;
							    }
							}
						    return 0;
						}
					});
			for(String date: po.players.keySet())
			{
				inter.add(date);
			}
			while(!inter.isEmpty())
			{
				String date = inter.poll();
				Map<String, String> datum = new HashMap<String, String>(2);
			    String[] set = date.split("//");
			    String sub = set[0];
			    if(!set[3].equals(null) && !set[3].equals(" "))
			    {
			    	sub += ", #" + set[3];
			    }
			    datum.put("title", sub);
			    datum.put("date", set[1] + " - " +  set[2]);
			    data.add(datum);
			}
		}
		else
		{
			Set<String> keys = po.players.keySet();
			List<String> players = new ArrayList<String>();
			for(String key : keys)
			{
				players.add(key);
			}
			Collections.sort(players, String.CASE_INSENSITIVE_ORDER);
			for(String date : players)
			{
				Map<String, String> datum = new HashMap<String, String>(2);
			    String[] set = date.split("//");
			    String sub = set[0];
			    if(!set[3].equals(null) && !set[3].equals(" "))
			    {
			    	sub += ", #" + set[3];
			    }
			    datum.put("title", sub);
			    datum.put("date", set[1] + " - " +  set[2]);
			    data.add(datum);
			}
		}
		SimpleAdapter adapter = new SimpleAdapter(cont, data,
		                                          android.R.layout.simple_list_item_2,
		                                          new String[] {"title", "date"},
		                                          new int[] {android.R.id.text1,
		                                                     android.R.id.text2});
		BounceListView listview = (BounceListView)res.findViewById(R.id.player_list);
		listview.setAdapter(adapter);
		playerList = listview;
		listview.setOverscrollHeader(cont.getResources().getDrawable(R.drawable.overscroll_blue));
		listview.setOverscrollFooter(cont.getResources().getDrawable(R.drawable.overscroll_green));
		layout.addView(res);
	}
	
	/**
	 * Gets the venue information data set up
	 * @param obj
	 * @param cont
	 */
	public static void venueInfo(APIObject obj, Context cont)
	{
		VenueInfoObject vio = new VenueInfoObject(obj, cont);
	}
	
	/**
	 * Populates the statwell with a team information
	 * @param obj
	 * @param cont
	 */
	public static void teamInfo(APIObject obj, Context cont)
	{
		TeamInfoObject tio = new TeamInfoObject(obj, cont);
	}
}

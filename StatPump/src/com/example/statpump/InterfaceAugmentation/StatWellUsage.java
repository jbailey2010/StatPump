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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TwoLineListItem;

import com.example.statpump.R;
import com.example.statpump.ClassFiles.APIObject;
import com.example.statpump.ClassFiles.LittleStorage.PlayerInfoObject;
import com.example.statpump.ClassFiles.LittleStorage.PlayerSearchObject;
import com.example.statpump.ClassFiles.LittleStorage.PlayerStatsObject;
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
	static LinearLayout sw;
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
	
	/**
	 * Gets the player info onclick
	 * @param obj
	 * @param cont
	 * @param po
	 */
	public static void playerInfo(final APIObject obj, final Context cont, final PlayerSearchObject po)
	{
		View res = listViewPlayers(obj, cont, po);
		final PlayerInfoObject pio = new PlayerInfoObject();
		pio.number = "Number not listed";
		BounceListView lv = (BounceListView)res.findViewById(R.id.player_list);
		lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				sw.removeAllViews();
				String tweet = ((TwoLineListItem)arg1).getText1().getText().toString();
				pio.name = tweet.split(", ")[0];
				if(tweet.split(", ").length > 1)
				{
					pio.number = tweet.split(", ")[1].replace("#", "");
				}
				String teamPos = ((TwoLineListItem)arg1).getText2().getText().toString();
				pio.pos = teamPos.split(" - ")[0];
				pio.team = teamPos.split(" - ")[1];
				pio.playerID = po.players.get(pio.name + "//" + pio.pos + "//" + pio.team + "//" + pio.number);
				pio.spawnMoreInfo(obj, cont, po, true);
			}
		});
	}
	
	/**
	 * Gets the player stats onclick
	 * @param obj
	 * @param cont
	 * @param po
	 */
	public static void playerStats(final APIObject obj, final Context cont, final PlayerSearchObject po)
	{
		final PlayerStatsObject o = new PlayerStatsObject();
		View res = listViewPlayers(obj, cont, po);
		BounceListView lv = (BounceListView)res.findViewById(R.id.player_list);
		lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				sw.removeAllViews();
				String tweet = ((TwoLineListItem)arg1).getText1().getText().toString();
				String name = tweet.split(", ")[0];
				String number = "Number not listed";
				if(tweet.split(", ").length > 1)
				{
					number = tweet.split(", ")[1].replace("#", "");
				}
				String teamPos = ((TwoLineListItem)arg1).getText2().getText().toString();
				String pos = teamPos.split(" - ")[0];
				String team = teamPos.split(" - ")[1];
				int playerID = po.players.get(name + "//" + pos + "//" + team + "//" + number);
				o.spawnAsync(playerID, cont, obj, po, true, name, team, pos, number);
			}
		});
	}
	
	/**
	 * Sets the listview up with players, sorted by numbers OR alphabetically
	 * @param obj
	 * @param cont
	 * @param po
	 */
	public static View listViewPlayers(APIObject obj, Context cont, PlayerSearchObject po)
	{
		sw = (LinearLayout)((Activity) cont).findViewById(R.id.statwell);
		View res = ((Activity) cont).getLayoutInflater().inflate(R.layout.player_list, sw, false);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		boolean isNumbers = true;
		int counter = 0;
		for(String key : po.players.keySet())
		{
			if(key.contains("Number not listed"))
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

							if(!a.split("//")[3].equals(" ") && !a.split("//")[3].equals("Number not listed") && 
									!b.split("//")[3].equals("Number not listed"))
							{
								int num1 = Integer.parseInt(a.split("//")[3]);
								int num2 = Integer.parseInt(b.split("//")[3]);
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
			    if(!set[3].equals(null) && !set[3].equals("Number not listed"))
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
		sw.addView(res);
		return res;
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

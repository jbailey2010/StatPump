package com.example.statpump.InterfaceAugmentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.statpump.statpump.R;
import com.example.statpump.ClassFiles.APIObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
/**
 * Handles some user input in the interface
 * @author Jeff
 *
 */
public class ManageSportSelection 
{
	static Context context;
	static Spinner sportSpinner;
	static ImageView sportLogo;
	static String selectedSport;
	static String selectedSportSpec;
	public static Integer[] iconSet = {R.drawable.ssdd, R.drawable.nfldd, R.drawable.ncaadd,
			R.drawable.mlbdd, R.drawable.nbadd, R.drawable.nhldd, 
			R.drawable.soccerdd};

	
	public static String getSportSelection(int index)
	{
		System.out.println(index);
		if(index == 1)
		{
			return "American Football - NFL (United States)";
		}
		if(index == 2)
		{
			return "American Football - NCAA Division 1 (United States)";
		}
		if(index == 3)
		{
			return "Baseball - MLB (United States)";
		}
		if(index == 4)
		{
			return "Basketball - NBA (United States)";
		}
		if(index == 5)
		{
			return "Hockey - NHL (United States)";
		}
		if(index == 6)
		{
			return "Soccer - Premier League (England)";
		}
		return "Something else...:(";
	}
	
	/**
	 * Populates the sport spinner
	 * @param spinner
	 * @param cont
	 */
	public static void populateSpinner(Spinner spinner, Context cont)
	{
		sportSpinner = spinner;
		context = cont;
		List<Map<String, Integer>> data = new ArrayList<Map<String, Integer>>();
		for(Integer icon : iconSet)
		{
			Map<String, Integer> datum = new HashMap<String, Integer>();
			datum.put("icon", icon);
			data.add(datum);
		}
		SimpleAdapter adapter = new SimpleAdapter(cont, data,
                R.layout.image_dropdown,
                new String[] {"icon"},
                new int[] {R.id.sport_icon});
		sportSpinner.setAdapter(adapter);
		/*
		List<String> sports = new ArrayList<String>();
		sports.add("Select a Sport");
		sports.add("American Football - NFL (United States)");
		sports.add("American Football - NCAA Division 1 (United States)");
		sports.add("Baseball - MLB (United States)");
		//sports.add("Baseball - World Baseball Classic (World)");
		sports.add("Basketball - NBA (United States)");
		//sports.add("Basketball - NCAA Division 1 (United States)");
		//sports.add("Basketball - Olympics (World)");
		//sports.add("Cricket - India Premier League (India)");
		//sports.add("Golf - PGA Tour (World)");
		//sports.add("Golf - LPGA Tour (World)");
		sports.add("Hockey - NHL (United States)");
		//sports.add("Hockey - Olympics (World)");
		//sports.add("Motorsports - FIA F1 World Championships (World)");
		//sports.add("Motorsports - NASCAR Sprint Cup Series (United States)");
		//sports.add("Rugby - World Cup (World)");
		sports.add("Soccer - Premier League (England)");
		//sports.add("Soccer - UEFA Champions League (England)");
		//sports.add("Soccer - Primera Division (Spain)");
		//sports.add("Soccer - MLS (United States)"); 
		//sports.add("Soccer - World Cup (World)");
		//sports.add("Tennis - ATP World Tour (World)");
		//sports.add("Tennis - WTA Tour (World)");
		//sports.add("Volleyball - World Championship (Europe)");
		//sports.add("Volleyball - Olympics (World)");
		 
		//Setting the adapter
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, 
				android.R.layout.simple_spinner_dropdown_item, sports);
		sportSpinner.setAdapter(spinnerArrayAdapter);*/
	}

	/**
	 * Sets the image given setting of spinner
	 */
	public static void setSportImage(String sportStr, Context cont,
			ImageView image, APIObject obj) 
	{ 
		if(sportStr.contains("Baseball"))
		{
			System.out.println("Setting to image"); 
			image.setImageResource(R.drawable.mlb);
			image.setScaleType(ScaleType.FIT_XY);
		}
		else if(sportStr.contains("Basketball"))
		{
			image.setImageResource(R.drawable.nba);
			image.setScaleType(ScaleType.FIT_XY);
		}
		else if(sportStr.contains("Football") && sportStr.contains("NCAA"))
		{
			image.setImageResource(R.drawable.cfb);
			image.setScaleType(ScaleType.FIT_XY);
		}
		else if(sportStr.contains("Football"))
		{ 
			image.setImageResource(R.drawable.nfl);
			image.setScaleType(ScaleType.FIT_XY);
		}
		else if(sportStr.contains("Hockey"))
		{
			image.setImageResource(R.drawable.nhl);
			image.setScaleType(ScaleType.FIT_XY);
		}
		else if(sportStr.contains("Soccer"))
		{
			image.setImageResource(R.drawable.futbol);
			image.setScaleType(ScaleType.FIT_XY);
		}
		obj.sportSelected(sportStr, (Activity) cont);
	}

}

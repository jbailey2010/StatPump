package com.example.statpump.InterfaceAugmentation;

import java.util.ArrayList;
import java.util.List;

import com.example.statpump.R;
import com.example.statpump.ClassFiles.APIObject;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
	 
	/**
	 * Populates the sport spinner
	 * @param spinner
	 * @param cont
	 */
	public static void populateSpinner(Spinner spinner, Context cont)
	{
		sportSpinner = spinner;
		context = cont;
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
		sportSpinner.setAdapter(spinnerArrayAdapter);
	}

	/**
	 * Sets the image given setting of spinner
	 */
	public static void setSportImage(String sportStr, Context cont,
			ImageView image, APIObject obj) 
	{
		if(sportStr.contains("Australian Football"))
		{
			image.setImageResource(R.drawable.ausfootball);
		}
		else if(sportStr.contains("Baseball"))
		{
			image.setImageResource(R.drawable.baseball);
		}
		else if(sportStr.contains("Basketball"))
		{
			image.setImageResource(R.drawable.basketball);
		}
		else if(sportStr.contains("Cricket"))
		{
			image.setImageResource(R.drawable.cricket);
		}
		else if(sportStr.contains("Football"))
		{ 
			image.setImageResource(R.drawable.football);
		}
		else if(sportStr.contains("Golf"))
		{
			image.setImageResource(R.drawable.golf);
		}
		else if(sportStr.contains("Handball"))
		{
			image.setImageResource(R.drawable.handball);
		}
		else if(sportStr.contains("Hockey"))
		{
			image.setImageResource(R.drawable.hockey);
		}
		else if(sportStr.contains("Motorsports"))
		{
			image.setImageResource(R.drawable.car);
		}
		else if(sportStr.contains("Rugby"))
		{
			image.setImageResource(R.drawable.rugby);
		}
		else if(sportStr.contains("Soccer"))
		{
			image.setImageResource(R.drawable.soccer);
		}
		else if(sportStr.contains("Tennis"))
		{
			image.setImageResource(R.drawable.tennis);
		}
		else if(sportStr.contains("Volleyball"))
		{
			image.setImageResource(R.drawable.volleyball);
		}
		obj.sportSelected(sportStr, (Activity) cont);
	}

}

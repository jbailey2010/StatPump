package com.example.statpump.InterfaceAugmentation;

import java.util.ArrayList;
import java.util.List;

import com.example.statpump.R;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
/**
 * Handles some user input in the interface
 * @author Jeff
 *
 */
public class ManageInput 
{
	static Context context;
	static Spinner sportSpinner;
	static Spinner sportSpecSpinner;
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
		sports.add("Soccer");
		sports.add("American Football");
		sports.add("Australian Football");
		sports.add("Baseball");
		sports.add("Basketball");
		sports.add("Cricket");
		sports.add("Golf");
		sports.add("Handball");
		sports.add("Hockey");
		sports.add("Motorsports");
		sports.add("Rugby");
		sports.add("Tennis");
		sports.add("Volleyball");
		
		//Setting the adapter
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, 
				android.R.layout.simple_spinner_dropdown_item, sports);
		sportSpinner.setAdapter(spinnerArrayAdapter);
	}
	
	/**
	 * Populates the second spinner with the subset of data
	 * (specific versions of the sports)
	 */
	public static void populateSpecSpinner(String selection, Spinner spinner, Context cont, ImageView image)
	{
		sportSpecSpinner = spinner;
		List<String> sports = new ArrayList<String>();
		if(selection.equals("Soccer"))
		{
			image.setImageResource(R.drawable.soccer);
		}
		else if(selection.equals("American Football"))
		{
			image.setImageResource(R.drawable.football);
		}
		else if(selection.equals("Australian Football"))
		{
			image.setImageResource(R.drawable.ausfootball);
		}
		else if(selection.equals("Baseball"))
		{
			image.setImageResource(R.drawable.baseball);
		}
		else if(selection.equals("Basketball"))
		{
			image.setImageResource(R.drawable.basketball);
		}
		else if(selection.equals("Cricket"))
		{
			image.setImageResource(R.drawable.cricket);
		}
		else if(selection.equals("Golf"))
		{
			image.setImageResource(R.drawable.golf);
		}
		else if(selection.equals("Handball"))
		{
			image.setImageResource(R.drawable.handball);
		}
		else if(selection.equals("Hockey"))
		{
			image.setImageResource(R.drawable.hockey);
		}
		else if(selection.equals("Motorsports"))
		{
			image.setImageResource(R.drawable.car);
		}
		else if(selection.equals("Rugby"))
		{
			image.setImageResource(R.drawable.rugby);
		}
		else if(selection.equals("Tennis"))
		{
			image.setImageResource(R.drawable.tennis);
		}
		else if(selection.equals("Volleyball"))
		{
			image.setImageResource(R.drawable.volleyball);
		}
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, 
				android.R.layout.simple_spinner_dropdown_item, sports);
		sportSpecSpinner.setAdapter(spinnerArrayAdapter);
	}
}

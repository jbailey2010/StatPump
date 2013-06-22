package com.example.statpump.InterfaceAugmentation;

import java.util.ArrayList;
import java.util.List;

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
	public static void populateSpecSpinner(String selection, Spinner spinner, Context cont)
	{
		sportSpecSpinner = spinner;
		List<String> sports = new ArrayList<String>();
		if(selection.equals("Soccer"))
		{
			
		}
		else if(selection.equals("American Football"))
		{
			
		}
		else if(selection.equals("Australian Football"))
		{
			
		}
		else if(selection.equals("Baseball"))
		{
			
		}
		else if(selection.equals("Basketball"))
		{
			
		}
		else if(selection.equals("Cricket"))
		{
			
		}
		else if(selection.equals("Golf"))
		{
			
		}
		else if(selection.equals("Handball"))
		{
			
		}
		else if(selection.equals("Hockey"))
		{
			
		}
		else if(selection.equals("Motorsports"))
		{
			
		}
		else if(selection.equals("Rugby"))
		{
			
		}
		else if(selection.equals("Tennis"))
		{
			
		}
		else if(selection.equals("Volleyball"))
		{
			
		}
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, 
				android.R.layout.simple_spinner_dropdown_item, sports);
		sportSpecSpinner.setAdapter(spinnerArrayAdapter);
	}
}

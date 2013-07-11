package com.example.statpump.Pages;

import java.util.ArrayList;
import java.util.List;

import com.example.statpump.R;
import com.example.statpump.R.layout;
import com.example.statpump.R.menu;
import com.example.statpump.ClassFiles.FacebookWork;
import com.example.statpump.ClassFiles.HandleInput;
import com.example.statpump.ClassFiles.TwitterWork;
import com.example.statpump.InterfaceAugmentation.ManageSportSelection;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Handles the user lookup for team specific stuff
 * @author Jeff
 *
 */
public class HomeTeam extends Activity {
	private static final String FacebookWork = null;
	final Context cont = this;
	public List<String> sportList = new ArrayList<String>();
	public List<String> teamList = new ArrayList<String>();
	public Menu menuObj = null;
	Spinner sport;
	Spinner team1;
	String sportStr;
	String team1Str;
	Button submit;
	Button clear;
	TextView headerText;
	ImageView sportImg;
	public boolean isFirst;
	
	/**
	 * Sets up the layout, initial loading...etc.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_team);
		initialSetUp();
		if(menuObj != null)
		{
			checkInternet();
			isFirst = false;
		}
	} 
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuObj = menu;
		getMenuInflater().inflate(R.menu.home_team, menu);
		checkInternet();
		return true;
	}
	
	/** 
	 * Runs the on selection part of the menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{  
		switch (item.getItemId()) 
		{
			case R.id.search_player: 
				return true;
			case R.id.twitter:
				TwitterWork.twitterInitial(cont);
		    	return true; 
			case R.id.facebook:  
				com.example.statpump.ClassFiles.FacebookWork.facebookInit(cont);
				return true;
			case R.id.switch_game:
				Intent intent = new Intent(cont, Home.class);
		        cont.startActivity(intent);	
				return true;
			case R.id.help:
				HandleInput.helpPopUp(cont);
				return true;
			case R.id.set_statwell_team:
				setStatWellDialog();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Don't allow user to go back to server connecting
	 */
	@Override
	public void onBackPressed() {
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    if(menuObj != null)
	    {
	    	checkInternet();
	    }
	    
	}
	
	public void checkInternet()
	{
		sport = (Spinner)findViewById(R.id.team_sport_spinner);
		team1 = (Spinner)findViewById(R.id.team_name_spinner);
		submit = (Button)findViewById(R.id.team_submit);
		clear = (Button)findViewById(R.id.team_clear);
		headerText = (TextView)findViewById(R.id.team_title);
		sportImg = (ImageView)findViewById(R.id.team_sport_image);
		if(!HandleInput.confirmInternet(cont))
		{
			sport.setClickable(false); 
			team1.setClickable(false);
			submit.setClickable(false);
			clear.setClickable(false);
			headerText.setText("No Internet Connection Available");
			for(int i = 0; i < menuObj.size(); i++)
			{
				menuObj.getItem(i).setEnabled(false);
			}
		}
		else
		{
			sport.setClickable(true);
			team1.setClickable(true);
			submit.setClickable(true);
			clear.setClickable(true);
			for(int i = 0; i < menuObj.size(); i++)
			{
				menuObj.getItem(i).setEnabled(true);
				if(team1.isShown())
				{
					headerText.setText("Hit Submit When You're Ready");
					if((team1.getSelectedView()).equals("Select a Team"))
					{
						headerText.setText("Select The Team Below");
					}
				}
				else if(!(sport.getSelectedItem()).equals("Select a Sport"))
				{
					headerText.setText("Select the Team Below");
				}
				else
				{
					headerText.setText("Select a Sport Below");
				}
			}
		}
	}
	
	/**
	 * Set global fields
	 */
	public void initialSetUp(){
		sport = (Spinner)findViewById(R.id.team_sport_spinner);
		team1 = (Spinner)findViewById(R.id.team_name_spinner);
		submit = (Button)findViewById(R.id.team_submit);
		clear = (Button)findViewById(R.id.team_clear);
		headerText = (TextView)findViewById(R.id.team_title);
		sportImg = (ImageView)findViewById(R.id.team_sport_image);
		setUpInterface();
		ManageSportSelection.populateSpinner(sport, cont);
	}
	
	/**
	 * Sets up the spinners and the relavent listeners such that
	 * when a relevant item is picked, it unhides stuff
	 */
	public void setUpInterface()
	{
		sport = (Spinner)findViewById(R.id.team_sport_spinner);
		team1 = (Spinner)findViewById(R.id.team_name_spinner);
		submit = (Button)findViewById(R.id.team_submit);
		clear = (Button)findViewById(R.id.team_clear);
		headerText = (TextView)findViewById(R.id.team_title);
		sportImg = (ImageView)findViewById(R.id.team_sport_image);
		team1.setVisibility(View.INVISIBLE);
		submit.setVisibility(View.INVISIBLE);
		clear.setVisibility(View.INVISIBLE);
		sport.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(!((TextView)arg1).getText().toString().equals("Select a Sport") && sport.getAdapter() != null && sport.getAdapter().getCount() > 0)
				{
					sportImg.setVisibility(View.VISIBLE);
					sportStr = ((TextView)arg1).getText().toString();	
					headerText.setText("Select the Team Below");
					clear.setVisibility(View.VISIBLE);
					team1.setVisibility(View.VISIBLE);
					ManageSportSelection.setSportImage(sportStr, cont, sportImg);
				}
				else
				{
					sportImg.setVisibility(View.INVISIBLE);
					team1.setVisibility(View.INVISIBLE);
					submit.setVisibility(View.INVISIBLE);
					clear.setVisibility(View.INVISIBLE);
					headerText.setText("Select a Sport Below");
				} 
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		team1.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(!((TextView)arg1).getText().toString().equals("Select a Team"))
				{
					headerText.setText("Hit Submit When You're Ready");
					submit.setVisibility(View.VISIBLE);
					team1Str = ((TextView)arg1).getText().toString();
				}
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		clear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				sport.setSelection(0);
				team1.setVisibility(View.INVISIBLE);
				headerText.setText("Select a Sport Below");
				sportImg.setVisibility(View.INVISIBLE);
			}
		});
		/*
		 * TO BE DONE:
		 * Clear button augmented once there's functionality
		 * Function that populates dropdowns
		 * Submit button
		 */
	}
	
	/**
	 * Sets the statwell dialog
	 */
	public void setStatWellDialog()
	{
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.statwell_team);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
		dialog.show();
		
		Button close = (Button)dialog.findViewById(R.id.statwell_team_cancel);
		close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

}

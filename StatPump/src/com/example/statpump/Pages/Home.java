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
import android.app.ActionBar; 
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
/**
 * Handles user lookup for game related stuff
 * @author Jeff
 *
 */
public class Home extends Activity {
	final Context cont = this;
	public List<String> sportList = new ArrayList<String>();
	public List<String> teamList = new ArrayList<String>();
	public Menu menuObj = null;
	Spinner sport;
	Spinner team1;
	Spinner team2;
	String sportStr;
	String team1Str;
	String team2Str;
	Button submit;
	Button clear;
	TextView headerText; 
	ImageView sportImg;
	public boolean isFirst = true;
	
	/**
	 * Sets up initial loading/views for the activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
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
		getMenuInflater().inflate(R.menu.home, menu);
		menuObj = menu;
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
				FacebookWork.facebookInit(cont);
				return true;
			case R.id.switch_team:
				Intent intent = new Intent(cont, HomeTeam.class);
		        cont.startActivity(intent);	
				return true;
			case R.id.help:
				HandleInput.helpPopUp(cont);
				return true;
			case R.id.set_statwell_game:
				setStatWellDialog();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Block back button pressing so the user can't go back to server connecting
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
		sport = (Spinner)findViewById(R.id.game_sport_spinner);
		team1 = (Spinner)findViewById(R.id.game_team1_name_spinner);
		team2 = (Spinner)findViewById(R.id.game_team2_name_spinner);
		submit = (Button)findViewById(R.id.game_submit);
		clear = (Button)findViewById(R.id.game_clear);
		headerText = (TextView)findViewById(R.id.game_title);
		sportImg = (ImageView)findViewById(R.id.game_sport_image);
		if(!HandleInput.confirmInternet(cont))
		{
			headerText.setText("No Internet Connection Available");
			for(int i = 0; i < menuObj.size(); i++)
			{
				menuObj.getItem(i).setEnabled(false);
			}
			sport.setClickable(false);
			team1.setClickable(false);
			team2.setClickable(false);
			submit.setClickable(false);
			clear.setClickable(false);
		}
		else
		{
			sport.setClickable(true);
			team1.setClickable(true);
			team2.setClickable(true);
			submit.setClickable(true);
			clear.setClickable(true);
			for(int i = 0; i < menuObj.size(); i++)
			{
				menuObj.getItem(i).setEnabled(true);
			}
			if(team1.isShown())
			{
				headerText.setText("Select the First Team Below");
				if(!((TextView)team1.getSelectedView()).getText().toString().equals("Select a Team"))
				{
					headerText.setText("Select the Second Team Below");
				}
			}
			if(team2.isShown())
			{
				headerText.setText("Select the Second Team Below");
				if(!((TextView)team2.getSelectedView()).getText().toString().equals("Select a Team"))
				{
					headerText.setText("Hit Submit When You're Ready");
				}
			}
			else if(!team1.isShown() && !team2.isShown())
			{
				headerText.setText("Select a Sport Below");
			}
		}
	}
	
	public void initialSetUp()
	{
		sport = (Spinner)findViewById(R.id.game_sport_spinner);
		team1 = (Spinner)findViewById(R.id.game_team1_name_spinner);
		team2 = (Spinner)findViewById(R.id.game_team2_name_spinner);
		submit = (Button)findViewById(R.id.game_submit);
		clear = (Button)findViewById(R.id.game_clear);
		sportImg = (ImageView)findViewById(R.id.game_sport_image);
		headerText = (TextView)findViewById(R.id.game_title);
		setUpInterface();
		ManageSportSelection.populateSpinner(sport, cont);
	}
	 
	/**
	 * Sets up the spinners and the relavent listeners such that
	 * when a relevant item is picked, it unhides stuff
	 */
	public void setUpInterface()
	{
		sport = (Spinner)findViewById(R.id.game_sport_spinner);
		team1 = (Spinner)findViewById(R.id.game_team1_name_spinner);
		team2 = (Spinner)findViewById(R.id.game_team2_name_spinner);
		submit = (Button)findViewById(R.id.game_submit);
		clear = (Button)findViewById(R.id.game_clear);
		sportImg = (ImageView)findViewById(R.id.game_sport_image);
		headerText = (TextView)findViewById(R.id.game_title);
		team1.setVisibility(View.INVISIBLE);
		team2.setVisibility(View.INVISIBLE);
		submit.setVisibility(View.INVISIBLE); 
		clear.setVisibility(View.INVISIBLE);
		sport.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(!((TextView)arg1).getText().toString().equals("Select a Sport")  && sport.getAdapter() != null && sport.getAdapter().getCount() > 0)
				{
					sportImg.setVisibility(View.VISIBLE);
					sportStr = ((TextView)arg1).getText().toString();	
					clear.setVisibility(View.VISIBLE);
					team1.setVisibility(View.VISIBLE);
					headerText.setText("Select the First Team Below");
					ManageSportSelection.setSportImage(sportStr, cont, sportImg);
				}
				else
				{
					sportImg.setVisibility(View.INVISIBLE);
					team1.setVisibility(View.INVISIBLE);
					team2.setVisibility(View.INVISIBLE);
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
					team2.setVisibility(View.VISIBLE);
					team1Str = ((TextView)arg1).getText().toString();
					if(((TextView)team2.getSelectedView()).getText().toString().equals("Select a Team"))
					{
						headerText.setText("Select the Second Team Below");
					}
					else
					{
						headerText.setText("Click Submit");
						submit.setVisibility(View.VISIBLE);
					}
				}
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		team2.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(!((TextView)arg1).getText().toString().equals("Select a Team"))
				{
					submit.setVisibility(View.VISIBLE);
					team2Str = ((TextView)arg1).getText().toString();
					if(((TextView)team1.getSelectedView()).getText().toString().equals("Select a Team"))
					{
						headerText.setText("Select the Second Team Below");
					}
					else
					{
						headerText.setText("Hit Submit When You're Ready");
						submit.setVisibility(View.VISIBLE);
					}
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
				team2.setVisibility(View.INVISIBLE);
				headerText.setText("Select a Sport Below");
				sportImg.setVisibility(View.INVISIBLE);
			}
		});
		/*
		 * TO BE DONE:
		 * Clear button augmented with functionality
		 * Function that populates dropdowns
		 * Submit button
		 * Keep updating header
		 */
	}
	
	/**
	 * Makes the dialog for statwell show up
	 */
	public void setStatWellDialog()
	{
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.statwell_game);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
		dialog.show();
		
		Button close = (Button)dialog.findViewById(R.id.statwell_game_cancel);
		close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
}

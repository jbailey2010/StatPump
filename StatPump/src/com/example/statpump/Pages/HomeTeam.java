package com.example.statpump.Pages;

import java.util.ArrayList;
import java.util.List;

import com.example.statpump.R;
import com.example.statpump.R.layout;
import com.example.statpump.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Handles the user lookup for team specific stuff
 * @author Jeff
 *
 */
public class HomeTeam extends Activity {
	final Context cont = this;
	public List<String> sportList = new ArrayList<String>();
	public List<String> teamList = new ArrayList<String>();
	Spinner sport;
	Spinner team1;
	String sportStr;
	String team1Str;
	Button submit;
	Button clear;
	
	/**
	 * Sets up the layout, initial loading...etc.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_team);
		setUpInterface();
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_team, menu);
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
		    	return true;
			case R.id.facebook:
				return true;
			case R.id.switch_game:
				Intent intent = new Intent(cont, Home.class);
		        cont.startActivity(intent);	
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
		team1.setVisibility(View.INVISIBLE);
		submit.setVisibility(View.INVISIBLE);
		clear.setVisibility(View.INVISIBLE);
		sport.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(!((TextView)arg1).getText().toString().equals("Select a Sport"))
				{
					clear.setVisibility(View.VISIBLE);
					team1.setVisibility(View.VISIBLE);
					sportStr = ((TextView)arg1).getText().toString();	
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
					submit.setVisibility(View.VISIBLE);
					team1Str = ((TextView)arg1).getText().toString();
				}
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		/*
		 * TO BE DONE:
		 * Clear button
		 * Function that populates dropdowns
		 * Submit button
		 */
	}

}

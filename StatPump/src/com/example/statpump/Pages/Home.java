package com.example.statpump.Pages;

import java.util.ArrayList;
import java.util.List;

import com.example.statpump.R;
import com.example.statpump.R.layout;
import com.example.statpump.R.menu;
import com.example.statpump.ClassFiles.TwitterWork;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
	Spinner sport;
	Spinner team1;
	Spinner team2;
	String sportStr;
	String team1Str;
	String team2Str;
	Button submit;
	Button clear;
	TextView headerText;
	
	/**
	 * Sets up initial loading/views for the activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setUpInterface();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
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
				return true;
			case R.id.switch_team:
				Intent intent = new Intent(cont, HomeTeam.class);
		        cont.startActivity(intent);	
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
		headerText = (TextView)findViewById(R.id.game_title);
		team1.setVisibility(View.INVISIBLE);
		team2.setVisibility(View.INVISIBLE);
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
					headerText.setText("Select the First Team Below");
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
						headerText.setText("Click Submit");
						submit.setVisibility(View.VISIBLE);
					}
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
		 * Keep updating header
		 */
	}
}

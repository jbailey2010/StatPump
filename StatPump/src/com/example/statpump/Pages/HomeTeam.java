package com.example.statpump.Pages;

import com.example.statpump.R;
import com.example.statpump.R.layout;
import com.example.statpump.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class HomeTeam extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_team);
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

}

package com.example.statpump.Pages;

import java.util.ArrayList;
import java.util.List;

import com.example.statpump.R;
import com.example.statpump.R.layout;
import com.example.statpump.R.menu;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * Handles user lookup for game related stuff
 * @author Jeff
 *
 */
public class Home extends Activity {
	final Context cont = this;
	
	/**
	 * Sets up initial loading/views for the activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
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

}

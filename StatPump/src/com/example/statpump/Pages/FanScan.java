package com.example.statpump.Pages;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.example.statpump.ClassFiles.HandleInput;
import com.example.statpump.ClassFiles.HandleStats;
import com.example.statpump.ClassFiles.TwitterWork;
import com.statpump.statpump.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;

public class FanScan extends Activity {
	public Context cont; 
	SideNavigationView sideNavigationView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cont = this;
		setContentView(R.layout.activity_fan_scan);
		ISideNavigationCallback sideNavigationCallback = new ISideNavigationCallback() {
		    @Override
		    public void onSideNavigationItemClick(int itemId) {
		    	switch (itemId) {
	            case R.id.go_home:
	            	Intent intent = new Intent(cont, Loading.class);
	    	        cont.startActivity(intent);	
	                break;
	            case R.id.switch_lookup:
	            	Intent intent2 = new Intent(cont, Home.class);
	    	        cont.startActivity(intent2);	
	                break;
	            case R.id.to_team_lookup:
	            	Intent tl = new Intent(cont, HomeTeam.class);
	            	cont.startActivity(tl);
	            	break;
	            case R.id.ticket_popup:
	            	HandleInput.ticketPopup(cont);
	            	break;
	            case R.id.help:
	            	HandleInput.helpPopUp(cont);
	                break;
	            case R.id.stats:
	            	HandleStats.handleStatsInit(cont);
	            	break;
	            default:
	                return;
		    	}
		    }
		};
		sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
	    sideNavigationView.setMenuItems(R.menu.side_navigation_menu_game);
	    sideNavigationView.setMenuClickCallback(sideNavigationCallback);
	   // sideNavigationView.setMode(/*SideNavigationView.Mode*/);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
 
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fan_scan, menu);
		return true;
	}
 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) 
		{
			case android.R.id.home:
		        sideNavigationView.toggleMenu();
		        return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


}

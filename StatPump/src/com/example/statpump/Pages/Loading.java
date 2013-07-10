package com.example.statpump.Pages;


import java.io.IOException;

import com.example.statpump.R;
import com.example.statpump.R.layout;
import com.example.statpump.R.menu;
import com.example.statpump.ClassFiles.APIInteraction;
import com.example.statpump.ClassFiles.HandleInput;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * Establishes the connection to the server
 * @author Jeff
 *
 */
public class Loading extends Activity {

	final Context cont = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); 
        try { 
        	System.out.println("Calling");
			APIInteraction.getXML("baseball/get_teams?id=79&type=season&detailed=yes");;
		} catch (IOException e) { 
			System.out.println("Failed");
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}*/
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
		TextView prompt = (TextView)findViewById(R.id.loading_prompt);
		ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar1);
		//REMOVE THIS WHEN THE SERVER IS SET UP
		if(HandleInput.confirmInternet(cont))
		{
			dummyFn();
			prompt.setText("Please wait, attempting to contact the server...");
			prompt.setTextSize(14);
			pb.setVisibility(View.VISIBLE);
		}
		else 
		{
			prompt.setText("No internet connection available.");
			prompt.setTextSize(25);
			pb.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loading, menu);
		return true;
	}
	 
	/**
	 * Change this to something more relavent later (dummy call to api to test?)
	 */
	public void dummyFn()
	{
		ImageView logo = (ImageView)findViewById(R.id.logo_loading);
		logo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
		        HandleInput.chooseTeamOrGame(cont);
			}
			
		});
	}

}

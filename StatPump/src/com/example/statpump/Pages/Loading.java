package com.example.statpump.Pages;


import com.example.statpump.R;
import com.example.statpump.R.layout;
import com.example.statpump.R.menu;
import com.example.statpump.ClassFiles.HandleInput;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
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
		//REMOVE THIS WHEN THE SERVER IS SET UP
		dummyFn();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loading, menu);
		return true;
	}
	 
	/**
	 * REMOVE THIS LATER
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

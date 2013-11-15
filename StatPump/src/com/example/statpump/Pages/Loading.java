package com.example.statpump.Pages;


import java.io.IOException;


import org.jsoup.nodes.Document;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.statpump.statpump.R;
import com.statpump.statpump.R.layout;
import com.statpump.statpump.R.menu;
import com.example.statpump.ClassFiles.APIInteraction;
import com.example.statpump.ClassFiles.APIObject;
import com.example.statpump.ClassFiles.HandleInput;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Establishes the connection to the server
 * @author Jeff
 *
 */
public class Loading extends Activity {
	boolean isValid = false;
	final Context cont = this;
	TextView prompt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
		prompt = (TextView)findViewById(R.id.loading_prompt);
		ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar1);
		if(HandleInput.confirmInternet(cont))
		{
			if(prompt != null)
			{
				prompt.setText("Please wait, attempting to contact the server...");
				prompt.setTextSize(15);
			}
			pb.setVisibility(View.VISIBLE);
			dummyFn();

		}
		else 
		{
			prompt.setText("No internet connection available.");
			prompt.setTextSize(25);
			pb.setVisibility(View.INVISIBLE);
			isValid = false;
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
		APIObject dummy = new APIObject(cont);
		Loading obj = new Loading();
		ImageView image = (ImageView)findViewById(R.id.logo_loading);
		ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar1);
		DummyQuery task = obj.new DummyQuery(prompt, image, cont, pb);
		task.execute(dummy, cont);
	} 
	
	/**
	 * After the async task, decides if the query was successful
	 * @param p 
	 * @param flag
	 */
	public void decideNetwork(boolean result, TextView text, ImageView logo, final Context cont, ProgressBar p)
	{
		if(result)
		{
			p.setVisibility(View.GONE);
			text.setVisibility(View.GONE);
			HandleInput.chooseTeamOrGame(cont);			
		}
		else
		{
			text.setVisibility(View.VISIBLE);
			text.setText("Unable to connect to the server. If you're certain you have an internet connection (REQUIRED), please reset the app and try again.");
			text.setTextSize(25);
			isValid = false;
		}
	}
	
	/**
	 * Does the test query to see if it's valid
	 * @author Jeff
	 *
	 */
	public class DummyQuery extends AsyncTask<Object, Void, Boolean> 
	{
	    TextView pr;
	    ImageView img;
	    ProgressBar p;
	    Context con;
	    public DummyQuery(TextView view, ImageView image, Context cont, ProgressBar pb)
	    {
	    	pr = view;
	    	img = image;
	    	con = cont;
	    	p = pb;
	    }
		@Override
		protected void onPreExecute(){ 
		   super.onPreExecute();
 
		}

		@Override
		protected void onPostExecute(Boolean result){
		   super.onPostExecute(result);
		   decideNetwork(result, pr, img, con, p);
		}
		
	    @Override
	    protected Boolean doInBackground(Object... data) 
	    {
	    	boolean flag = false;
	    	APIObject dummy = (APIObject)data[0];
	    	final Context cont = (Context)data[1];
	    	try {
				Document doc = APIInteraction.getXML("baseball/get_seasons?&authorized=yes&active=yes", dummy);
				flag = true;
			} catch (IOException e) {
				flag = false;
			}

			return flag;
	    }
	}

}

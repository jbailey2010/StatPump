package com.example.statpump.ClassFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import com.example.statpump.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Does the twitter-related work
 */
public class TwitterWork 
{
	String validURL = "";
	public static void twitterInitial(Context cont)
	{
		TwitterWork obj = new TwitterWork();
	    TwitterConnection task = obj.new TwitterConnection((Activity)cont);
	    task.execute(cont);
	}
	
	public class TwitterConnection extends AsyncTask<Object, Void, Void> 
	{
		ProgressDialog pdia;
		Activity act;
	    public TwitterConnection(Activity activity) 
	    {
	        pdia = new ProgressDialog(activity);
	        pdia.setCancelable(false);
	        act = activity;
	    }
	    
		@Override
		protected void onPreExecute(){ 
		   super.onPreExecute();
		        pdia.setMessage("Please wait, fetching the URL...");
		        pdia.show();    
		}

		@Override
		protected void onPostExecute(Void result){
		   super.onPostExecute(result);
		   pdia.dismiss();
		}
		
	    @Override
	    protected Void doInBackground(Object... data) 
	    {
	    	Twitter twitter = TwitterFactory.getSingleton();
	        twitter.setOAuthConsumer("De64oQ246ojYaGQfVb1rw",
	        		"xVpbhUMjPceJDD6pTU2qpjX4qvbBFi1eBW7vr3pg3YI");
	        try {
				RequestToken requestToken = twitter.getOAuthRequestToken();
		        AccessToken accessToken = null;
		        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		        validURL = requestToken.getAuthorizationURL();
		        System.out.println(validURL);
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return null;
	    }
	  }
}

package com.example.statpump.ClassFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import com.example.statpump.R;
import com.example.statpump.FileIO.ReadFromFile;
import com.example.statpump.FileIO.WriteToFile;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Does the twitter-related work
 */
public class TwitterWork 
{
	String validURL = "";
	int pin = -1;
	Twitter twitter;
	static Twitter userTwitter;
	RequestToken requestToken;
	AccessToken accessToken = null;
	static AccessToken userToken = null;
	static ListView searchOutput;
	Dialog searchOutputDialog;
	/**
	 * Calls the validation URL asynctask
	 * @param cont
	 */
	public static void twitterInitial(Context cont)
	{
		long check = ReadFromFile.readUseID(cont);
		//Not yet set
		if(check == -1)
		{
			TwitterWork obj = new TwitterWork();
		    TwitterConnection task = obj.new TwitterConnection((Activity)cont);
		    task.execute(cont);
		}
		else //it IS set, so call a function to 'log in' the user'
		{
			logInUser(cont);
		}
	}
	
	/**
	 * Logs in the user and makes a pop up asking them what they'd like to do
	 */
	public static void logInUser(final Context cont)
	{
		if(userToken == null)
		{
			String token = ReadFromFile.readToken(cont);
			String tokenSecret = ReadFromFile.readTokenSecret(cont);
			userToken = new AccessToken(token, tokenSecret);
			userTwitter = TwitterFactory.getSingleton();
			userTwitter.setOAuthConsumer("De64oQ246ojYaGQfVb1rw",
	        		"xVpbhUMjPceJDD6pTU2qpjX4qvbBFi1eBW7vr3pg3YI");
			userTwitter.setOAuthAccessToken(userToken);
		}
		twitterChoose(cont);
	}
	
	/**
	 * Sets up the user choosing of what they want to do
	 * @param cont
	 */
	public static void twitterChoose(final Context cont)
	{
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.twitter_choose);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();		
	    Button cancel = (Button)dialog.findViewById(R.id.twitter_choose_cancel);
	    Button search = (Button)dialog.findViewById(R.id.twitterChooseSearch);
	    Button tweet = (Button)dialog.findViewById(R.id.twitterChooseTweet);
	    cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				return;
			}
	    });
	    search.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				handleSearch(cont);
			}
	    });
	    tweet.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				handleTweet(cont, 0);
			}
	    });
	}
	
	/**
	 * Gets the user input for searches of tweets
	 * @param cont
	 */
	public static void handleSearch(final Context cont)
	{
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.twitter_search_input);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();
	    Button back = (Button)dialog.findViewById(R.id.tweet_search_back);
	    Button cancel = (Button)dialog.findViewById(R.id.tweet_search_cancel);
	    Button submit = (Button)dialog.findViewById(R.id.tweet_search_submit);
	    back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				twitterChoose(cont);
			}
	    });
	    cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				return;
			}
	    });
	    submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				EditText inputField = (EditText)dialog.findViewById(R.id.search_input);
				String input = inputField.getText().toString();
				if(input.length() == 0)
				{
					Toast.makeText(cont, "Please enter at least one term", Toast.LENGTH_SHORT).show();
				}
				else
				{
					dialog.dismiss();
					showResults(cont, input.replace(",", " "));
				}
			}
	    });
	}
	
	/**
	 * Sets up the output on the resutls, front end wise
	 */
	public static void showResults(final Context cont, String query)
	{
		final Dialog searchOutputDialog = new Dialog(cont);
		searchOutputDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		searchOutputDialog.setContentView(R.layout.twitter_search_output);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(searchOutputDialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    searchOutputDialog.getWindow().setAttributes(lp);
	    searchOutputDialog.show();
	    Button back = (Button)searchOutputDialog.findViewById(R.id.search_back);
	    Button close = (Button)searchOutputDialog.findViewById(R.id.search_close);
	    back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				searchOutputDialog.dismiss();
				handleSearch(cont);
			}
	    });
	    close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				searchOutputDialog.dismiss();
			}
	    });
	    searchOutput = (ListView)searchOutputDialog.findViewById(R.id.tweets_results);
	    TwitterWork obj = new TwitterWork();
	    TwitterSearchResults task = obj.new TwitterSearchResults((Activity)cont);
	    task.execute(cont, query, userTwitter, searchOutputDialog);
	}
	
	/**
	 * Queries the twitter api to get the resultant tweets
	 */
	public class TwitterSearchResults extends AsyncTask<Object, Void, QueryResult> 
	{
		ProgressDialog pdia;
		Activity act;
	    public TwitterSearchResults(Activity activity) 
	    {
	        pdia = new ProgressDialog(activity);
	        pdia.setCancelable(false);
	        act = activity;
	    }
	    
		@Override
		protected void onPreExecute(){ 
		   super.onPreExecute();
		        pdia.setMessage("Please wait, fetching the results...");
		        pdia.show();    
		}

		@Override
		protected void onPostExecute(QueryResult result){
		   super.onPostExecute(result);
		   pdia.dismiss();
		   outputResults(result, act);
		}
		
	    @Override
	    protected QueryResult doInBackground(Object... data) 
	    {
	    	Context cont = (Context)data[0];
	    	String query = (String)data[1];
	    	Twitter userTwitter = (Twitter)data[2];
	    	Query queryObj = new Query(query);
	    	queryObj.setCount(30);
	        QueryResult result;
			try {
				result = userTwitter.search(queryObj);
		        return result;
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	    }
	}
	
	/**
	 * Sets the tweet output listview
	 * @param queryResults
	 * @param cont
	 */
	public static void outputResults(QueryResult queryResults, Context cont)
	{
		//searchOutputDialog, searchOutput
	    List<String> results = new ArrayList<String>(10000);
	    List<Status> statuses = queryResults.getTweets();
	    for(Status status: statuses)
	    {
	    	results.add(status.getUser().getName() + " (" + status.getCreatedAt() + "):\n\n" + status.getText() + "\n");
	    }
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(cont,
	            android.R.layout.simple_list_item_1, results);
	    searchOutput.setAdapter(adapter);
	}
	
	/**
	 * Handles user tweeting and the interface
	 */
	public static void handleTweet(final Context cont, int flag)
	{
		if(flag == -1)
		{
			Toast.makeText(cont, "Please Enter a Status", Toast.LENGTH_SHORT).show();
		}
		else if(flag == 1)
		{
			Toast.makeText(cont, "Updated Status!", Toast.LENGTH_SHORT).show();
		}
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.twitter_tweet);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();	
	    Button back = (Button)dialog.findViewById(R.id.tweet_back);
	    Button cancel = (Button)dialog.findViewById(R.id.tweet_cancel);
	    Button submit = (Button)dialog.findViewById(R.id.tweet_submit);
	    final EditText inputBox = (EditText)dialog.findViewById(R.id.tweet_input);
	    back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				twitterChoose(cont);
			}
	    });
	    cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				return;
			}
	    });
	    submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				TwitterWork obj = new TwitterWork();
			    TwitterTweet task = obj.new TwitterTweet((Activity)cont);
			    task.execute(cont, inputBox, userTwitter);
			}
	    });
	}
	/**
	 * Gets the validation URL from twitter
	 * @author Jeff
	 *
	 */
	public class TwitterTweet extends AsyncTask<Object, Void, Integer> 
	{
		ProgressDialog pdia;
		Activity act;
	    public TwitterTweet(Activity activity) 
	    {
	        pdia = new ProgressDialog(activity);
	        pdia.setCancelable(false);
	        act = activity;
	    }
	    
		@Override
		protected void onPreExecute(){ 
		   super.onPreExecute();
		        pdia.setMessage("Please wait, posting the tweet...");
		        pdia.show();    
		}

		@Override
		protected void onPostExecute(Integer result){
		   super.onPostExecute(result);
		   pdia.dismiss();
		   handleTweet((Context)act, result);
		}
		
	    @Override
	    protected Integer doInBackground(Object... data) 
	    {
	    	Context cont = (Context)data[0];
	    	EditText inputBox = (EditText)data[1];
	    	Twitter userTwitter = (Twitter)data[2];
			String input = inputBox.getText().toString();
			if(input.length() > 0)
			{
				try {
					userTwitter.updateStatus(input);
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 1;
			}
			else
			{
				return -1;
			}
	    }
	}
	
	/**
	 * Gets the validation URL from twitter
	 * @author Jeff
	 *
	 */
	public class TwitterConnection extends AsyncTask<Object, Void, Twitter> 
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
		protected void onPostExecute(Twitter result){
		   super.onPostExecute(result);
		   pdia.dismiss();
		   handleURL(act, result);
		}
		
	    @Override
	    protected Twitter doInBackground(Object... data) 
	    {
	    	final Context cont = (Context)data[0];
	    	Twitter twitter = TwitterFactory.getSingleton();
	        twitter.setOAuthConsumer("De64oQ246ojYaGQfVb1rw",
	        		"xVpbhUMjPceJDD6pTU2qpjX4qvbBFi1eBW7vr3pg3YI");
	        try {
				requestToken = twitter.getOAuthRequestToken();
		        accessToken = null;
		        validURL = requestToken.getAuthorizationURL();
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return twitter;
	    }
	}
	
	/**
	 * Creates a dialog to get the user to validate it, then enter the pin
	 * @param cont
	 */
	public void handleURL(final Activity cont, Twitter twit)
	{
		twitter = twit;
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.twitter_login);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();
	    Button validate = (Button)dialog.findViewById(R.id.twitter_confirm_go);
	    validate.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(validURL));
				cont.startActivity(i);
				dialog.dismiss();
				handlePin(cont, twitter);
			}
	    });
	    Button cancel = (Button)dialog.findViewById(R.id.twitter_confirm_cancel);
	    cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				return;
			}
	    });
	}
	
	/**
	 * Gets and fetches the valid pin
	 * @param cont
	 */
	public void handlePin(final Activity cont, final Twitter twitter)
	{
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.twitter_validate_pin);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();	
	    final EditText input = (EditText)dialog.findViewById(R.id.twitter_pin_field);
	    Button submit = (Button)dialog.findViewById(R.id.twitter_pin_go);
	    submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String text = input.getText().toString();
				if(text.length() < 5)
				{ 
					Toast.makeText(cont, "Please Enter a Valid PIN", Toast.LENGTH_SHORT).show();
				}
				else
				{
					try{
						pin = Integer.parseInt(text);
						dialog.dismiss();
						finalizeValidation(cont, twitter);
					}
					catch (NumberFormatException e){
						Toast.makeText(cont, "Please Enter a PIN of Only Numbers", Toast.LENGTH_SHORT).show();
					}
				}
			}
	    });
	    Button cancel = (Button)dialog.findViewById(R.id.twitter_pin_cancel);
	    cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
	    });
	}
	
	/**
	 * Calls the authentication asynctask
	 * @param cont
	 */
	public void finalizeValidation(Context cont, Twitter twitter)
	{
		TwitterWork obj = new TwitterWork();
	    TwitterValidate task = obj.new TwitterValidate((Activity)cont);
	    task.execute(cont, twitter, requestToken, Integer.toString(pin));
	} 
	
	/**
	 * Gets the validation URL from twitter
	 * @author Jeff
	 *
	 */
	public class TwitterValidate extends AsyncTask<Object, Void, AccessToken> 
	{
		ProgressDialog pdia;
		Activity act;
	    public TwitterValidate(Activity activity) 
	    {
	        pdia = new ProgressDialog(activity);
	        pdia.setCancelable(false);
	        act = activity;
	    }
	    
		@Override
		protected void onPreExecute(){ 
		   super.onPreExecute();
		        pdia.setMessage("Please wait, validating your account...");
		        pdia.show();    
		}

		@Override
		protected void onPostExecute(AccessToken result){
		   super.onPostExecute(result);
		   pdia.dismiss();
		   handleAccessToken(act, result);
		}
		
	    @Override
	    protected AccessToken doInBackground(Object... data) 
	    {
	    	Context cont = (Context)data[0];
	    	Twitter twit = (Twitter)data[1];
	    	RequestToken rt = (RequestToken)data[2];
	    	String pinStr = (String)data[3];
	    	AccessToken accessToken;
			try {
				accessToken = twit.getOAuthAccessToken(rt, pinStr);
				WriteToFile.storeID(twit.verifyCredentials().getId(), cont);
			} catch (TwitterException e) {
		        if(401 == e.getStatusCode()){
			          System.out.println("Unable to get the access token.");
			          return null;
			    }else{
			          Toast.makeText(cont, "Error validating token", Toast.LENGTH_SHORT).show();
			          return null;
			    }
			}
			twit.setOAuthAccessToken(accessToken);
			return accessToken;
	    }
	}
	
	/**
	 * Saves the rest of it to file
	 * @param cont
	 * @param accessToken
	 */
	public void handleAccessToken(Activity cont, AccessToken accessToken)
	{ 
		if(accessToken == null)
		{
			return;
		}
		WriteToFile.storeToken(accessToken, cont);
		Toast.makeText(cont, "Successfully set up your account!", Toast.LENGTH_SHORT).show();
	}
}

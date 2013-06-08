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
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Does the twitter-related work
 */
public class TwitterWork 
{
	String validURL = "";
	int pin = -1;
	/**
	 * Calls the validation URL asynctask
	 * @param cont
	 */
	public static void twitterInitial(Context cont)
	{
		TwitterWork obj = new TwitterWork();
	    TwitterConnection task = obj.new TwitterConnection((Activity)cont);
	    task.execute(cont);
	}
	
	/**
	 * Gets the validation URL from twitter
	 * @author Jeff
	 *
	 */
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
		   handleURL(act);
		}
		
	    @Override
	    protected Void doInBackground(Object... data) 
	    {
	    	final Context cont = (Context)data[0];
	    	Twitter twitter = TwitterFactory.getSingleton();
	        twitter.setOAuthConsumer("De64oQ246ojYaGQfVb1rw",
	        		"xVpbhUMjPceJDD6pTU2qpjX4qvbBFi1eBW7vr3pg3YI");
	        try {
				RequestToken requestToken = twitter.getOAuthRequestToken();
		        AccessToken accessToken = null;
		        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		        validURL = requestToken.getAuthorizationURL();
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return null;
	    }
	}
	
	/**
	 * Creates a dialog to get the user to validate it, then enter the pin
	 * @param cont
	 */
	public void handleURL(final Activity cont)
	{
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
				handlePin(cont);
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
	public void handlePin(final Activity cont)
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
						finalizeValidation(cont);
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
	 * 
	 * @param cont
	 */
	public void finalizeValidation(Context cont)
	{
		
	}
}

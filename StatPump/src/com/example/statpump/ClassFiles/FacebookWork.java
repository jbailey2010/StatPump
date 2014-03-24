package com.example.statpump.ClassFiles;

import java.net.MalformedURLException;
import java.net.URL;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.statpump.statpump.R;
import com.example.statpump.FileIO.ReadFromFile;
import com.example.statpump.FileIO.WriteToFile;
import com.example.statpump.InterfaceAugmentation.SwipeDismissListViewTouchListener;



import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Permission;
import facebook4j.Post;
import facebook4j.PostUpdate;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;
import android.widget.AdapterView.OnItemClickListener;
/**
 * A library of all the facebook connectivity code
 * @author Jeff
 *
 */  
public class FacebookWork 
{
	public static Facebook facebook;
	public static Context cont;
	
	/**
	 * Helps with connectivity
	 * @author Jeff
	 *
	 */
	 private enum RequestCode {
	        OAuth(1),
	        Detail(2),
	        ;
	        
	        private int code;
	        private RequestCode(int code) {
	            this.code = code;
	        }
	        public static RequestCode getInstance(int code) {
	            for (RequestCode e : RequestCode.values()) {
	                if (e.code == code) {
	                    return e;
	                }
	            }
	            return null;
	        }
	        
	    }
	 
	 /**
	  * Determines if the user needs to connect or not
	  * @param context
	  */
	public static void facebookInit(Context context)
	{
		cont = context;
		String key = ReadFromFile.ReadFBToken(cont);
		if(!key.equals("Not set"))
		{
			keyWork(cont, key);
		}
		else
		{
			startFacebook(cont);
		}
	}
	
	/**
	 * A quick function to handle if there's no available internet in attempting to authenticate
	 */
	public static void failed()
	{
		Toast.makeText(cont, "No available internet connection", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * See if the key is usable yet
	 */
	public static void keyWork(Context context, String key)
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthAppId("134586500079124")
          .setOAuthAppSecret("41c5573b70df438816a7c81d49fa3e75")
          .setOAuthPermissions("publish_stream,read_stream,publish_actions");
        FacebookFactory ff = new FacebookFactory(cb.build());
        facebook = ff.getInstance();
		AccessToken access = new AccessToken(key);
        facebook.setOAuthAccessToken(access);
        if(access.getExpires() != null && access.getExpires() <= 10.0)
        {
        	startFacebook(context);
        }
        else
        {
        	startInterface(facebook.getOAuthAccessToken(), facebook);
        }
	}
	
	/**
	 * Starts the connectivity authorization
	 * @param context
	 */
	public static void startFacebook(Context context)
	{
		Intent intent = new Intent(cont, OAuthActivity.class);
        ((Activity)cont).startActivityForResult(intent, RequestCode.OAuth.code);
	}
	
	/**
	 * Sets up the interface to see what the user wants to do
	 * @param access
	 */
	public static void startInterface(final AccessToken access, Facebook fb)
	{
		facebook = fb;
		WriteToFile.storeFacebookToken(access.getToken(), cont);
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.facebook_choose);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();
	    Button close = (Button)dialog.findViewById(R.id.facebook_choose_cancel);
	    close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
	    });
	    Button status = (Button)dialog.findViewById(R.id.facebook_choose_status);
	    status.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				postStatus(access);
				dialog.dismiss();
			}
	    });
	    Button search = (Button)dialog.findViewById(R.id.facebook_choose_search);
	    search.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				searchPosts(access);
				dialog.dismiss();
			}
	    });
	}
	
	/**
	 * Handles the posting of the statuses
	 * @param access
	 */
	public static void postStatus(final  AccessToken access)
	{
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.facebook_status);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();
		final EditText status = (EditText)dialog.findViewById(R.id.facebook_status_field);
	    Button cancel = (Button)dialog.findViewById(R.id.facebook_status_close);
	    cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
	    });
	    Button back = (Button)dialog.findViewById(R.id.facebook_status_back);
	    back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				startInterface(access, facebook);
			}
	    });
	    Button submit = (Button)dialog.findViewById(R.id.facebook_status_submit);
	    submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String post = status.getText().toString();
				if(post.length() > 1)
				{
					FacebookWork obj=  new FacebookWork();
					FacebookPostStatus task = obj.new FacebookPostStatus((Activity)cont);
					task.execute(cont, post, facebook);
					Toast.makeText(cont, "Updated status to " + post, Toast.LENGTH_SHORT).show();
					dialog.dismiss();
					startInterface(access, facebook);
				}
			}
	    });
	}
	
	/**
	 * The asynctask that posts the statuses themselves
	 * @author Jeff
	 *
	 */
	public class FacebookPostStatus extends AsyncTask<Object, Void, Integer> 
	{
		ProgressDialog pdia;
		Activity act;
	    public FacebookPostStatus(Activity activity) 
	    {
	        pdia = new ProgressDialog(activity);
	        pdia.setCancelable(false);
	        act = activity;
	    }
	    
		@Override
		protected void onPreExecute(){ 
		   super.onPreExecute();
		        pdia.setMessage("Please wait, posting the status...");
		        pdia.show();    
		}

		@Override
		protected void onPostExecute(Integer result){
		   super.onPostExecute(result);
		   pdia.dismiss();
		}
		
	    @Override
	    protected Integer doInBackground(Object... data) 
	    {
	    	Context cont = (Context)data[0];
	    	String query = (String)data[1];
	    	Facebook facebook = (Facebook)data[2];
	    	int result = 0;
	    	try {
				facebook.postStatusMessage(query);
			} catch (FacebookException e) {
				if(e.isCausedByNetworkIssue())
				{
					Toast.makeText(act, "No available internet connection", Toast.LENGTH_SHORT).show();
					return null;
				}
				// TODO Auto-generated catch block
				else
				{
					e.printStackTrace();
				}
			}
	    	return result;
	    }
	}
	
	/**
	 * Handles the front end of the searching
	 * @param access
	 */
	public static void searchPosts(final AccessToken access)
	{
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.facebook_search);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();
		final EditText status = (EditText)dialog.findViewById(R.id.search_input);
	    Button cancel = (Button)dialog.findViewById(R.id.facebook_search_close);
	    cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
	    });
	    Button back = (Button)dialog.findViewById(R.id.facebook_search_back);
	    back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				startInterface(access, facebook);
			}
	    });
	    Button submit = (Button)dialog.findViewById(R.id.facebook_search_submit);
	    submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String post = status.getText().toString();
				if(post.length() > 1)
				{
					dialog.dismiss();
					FacebookWork obj=  new FacebookWork();
					FacebookSearchStatus task = obj.new FacebookSearchStatus((Activity)cont, access);
					task.execute(cont, post, facebook);
				}
			}
	    });
	}
	
	/**
	 * Asynctask that handles the searching
	 * @author Jeff
	 *
	 */
	public class FacebookSearchStatus extends AsyncTask<Object, Void, ResponseList<Post>> 
	{
		ProgressDialog pdia;
		Activity act;
		AccessToken access;
	    public FacebookSearchStatus(Activity activity, AccessToken ac) 
	    {
	        pdia = new ProgressDialog(activity);
	        pdia.setCancelable(false);
	        act = activity;
	        access = ac;
	    }
	    
		@Override
		protected void onPreExecute(){ 
		   super.onPreExecute();
		        pdia.setMessage("Please wait, searching the posts...");
		        pdia.show();    
		}

		@Override
		protected void onPostExecute(ResponseList<Post> result){
		   super.onPostExecute(result);
		   pdia.dismiss();
		   if(result != null)
		   {
			   showResults(result, access);
		   }
		}
		
	    @Override
	    protected ResponseList<Post> doInBackground(Object... data) 
	    {
	    	Context cont = (Context)data[0];
	    	String query = (String)data[1];
	    	Facebook facebook = (Facebook)data[2];
	    	try {
				ResponseList<Post> responses = facebook.searchPosts(query);
				return responses;
			} catch (FacebookException e) {
				if(e.isCausedByNetworkIssue())
				{
					Toast.makeText(act, "No available internet connection", Toast.LENGTH_SHORT).show();
					return null;
				}
				// TODO Auto-generated catch block
				else
				{
					e.printStackTrace();
				}
			}
	    	return null;
	    }
	}
	
	/**
	 * Handles the front end of the output
	 * @param responses
	 * @param access
	 */
	public static void showResults(ResponseList<Post> responses, final AccessToken access)
	{
		final Dialog dialog = new Dialog(cont);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.twitter_search_output);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();
	    Button back = (Button)dialog.findViewById(R.id.search_back);
	    back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				searchPosts(access);
			}
	    });
	    Button close = (Button)dialog.findViewById(R.id.search_close);
	    close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
	    });
	    final ListView searchOutput= (ListView)dialog.findViewById(R.id.tweets_results);
	    TextView resultHeader = (TextView)dialog.findViewById(R.id.results_header);
	    resultHeader.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				searchOutput.smoothScrollToPosition(0);
			}
	    });
	    List<String> results = new ArrayList<String>(10000);
	    
	    Button help = (Button)dialog.findViewById(R.id.search_help);
	    help.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				facebookSearchHelp();
			}
	    });
	    final List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	    for(Post post : responses)
	    {
	    	Map<String, String> datum = new HashMap<String, String>(2);
	    	datum.put("header", post.getMessage());
	    	datum.put("footer", "\n" + post.getFrom().getName() + "\n" + post.getCreatedTime() + "");
	    	data.add(datum);
	    }
	    final SimpleAdapter adapter = new SimpleAdapter(cont, data, 
	    		R.layout.web_listview_item, 
	    		new String[] {"header", "footer"}, 
	    		new int[] {R.id.text1, 
	    			R.id.text2});
	    searchOutput.setAdapter(adapter);
	    searchOutput.setOverscrollHeader(cont.getResources().getDrawable(R.drawable.overscroll_blue));
	    searchOutput.setOverscrollFooter(cont.getResources().getDrawable(R.drawable.overscroll_green));
	    if(android.os.Build.VERSION.SDK_INT > 11)
	    {
		    SwipeDismissListViewTouchListener touchListener =
	                new SwipeDismissListViewTouchListener(
	                        searchOutput,
	                        new SwipeDismissListViewTouchListener.OnDismissCallback() {
	                            @Override
	                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
	                                for (int position : reverseSortedPositions) {
	                                    data.remove(position);
	                                }
	                                adapter.notifyDataSetChanged();
	                                Toast.makeText(cont, "Hiding this post temporarily", Toast.LENGTH_SHORT).show();
	                            }
	                        });
	        searchOutput.setOnTouchListener(touchListener);
	        searchOutput.setOnScrollListener(touchListener.makeScrollListener());
	    }
	}
	
	
	/**
	 * Handles the help pop up 
	 */
	public static void facebookSearchHelp()
	{
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.facebooklist_help);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();
	    Button close = (Button)dialog.findViewById(R.id.facebook_help_close);
	    close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				return;
			}
	    });
	}
}

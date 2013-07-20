package com.example.statpump.Pages;

import java.util.ArrayList;
import java.util.List;










import com.example.statpump.R;
import com.example.statpump.R.layout;
import com.example.statpump.R.menu;
import com.example.statpump.ClassFiles.APIInteraction;
import com.example.statpump.ClassFiles.APIObject;
import com.example.statpump.ClassFiles.FacebookWork;
import com.example.statpump.ClassFiles.HandleInput;
import com.example.statpump.ClassFiles.TwitterWork;
import com.example.statpump.InterfaceAugmentation.ManageSportSelection;





import com.example.statpump.InterfaceAugmentation.StatWellUsage;

import android.os.Bundle;
import android.app.ActionBar; 
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu; 
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Handles user lookup for game related stuff
 * @author Jeff
 *
 */
public class Home extends Activity {
	final Context cont = this;
	public List<String> sportList = new ArrayList<String>();
	public List<String> teamList = new ArrayList<String>();
	public Menu menuObj = null;
	Spinner sport;
	Spinner team1;
	Spinner team2;
	String sportStr;
	String team1Str;
	String team2Str;
	Button submit;
	Button clear;
	TextView headerText; 
	ImageView sportImg;
	LinearLayout sw;
	public boolean isSubmit = false;
	APIObject obj = new APIObject(this);
	
	/**
	 * Sets up initial loading/views for the activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initialSetUp();		
		if(menuObj != null)
		{
			checkInternet();
		}
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		menuObj = menu;
		checkInternet();
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
				if(isSubmit)
				{
					//Call function
				}
				else
				{
					Toast.makeText(cont, "Please fill out all of the information below", Toast.LENGTH_SHORT).show();
				}
				return true;
			case R.id.twitter:
				TwitterWork.twitterInitial(cont);
		    	return true;
			case R.id.facebook: 
				FacebookWork.facebookInit(cont);
				return true;
			case R.id.switch_team:
				Intent intent = new Intent(cont, HomeTeam.class);
		        cont.startActivity(intent);	
				return true;
			case R.id.help:
				HandleInput.helpPopUp(cont);
				return true;
			case R.id.set_statwell_game:
				if(isSubmit)
				{
					setStatWellDialog(false);
				}
				else
				{
					Toast.makeText(cont, "You must hit submit before setting the content", Toast.LENGTH_SHORT).show();
				}
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
	
	@Override
	protected void onResume() {
	    super.onResume();
	    if(menuObj != null)
		{
		    checkInternet();
		}
	}
	
	/**
	 * Sees if there's internet. Adjusts interface as such
	 */
	public void checkInternet()
	{
		sport = (Spinner)findViewById(R.id.game_sport_spinner);
		team1 = (Spinner)findViewById(R.id.game_team1_name_spinner);
		team2 = (Spinner)findViewById(R.id.game_team2_name_spinner);
		obj.setUpObject(sport, team1, team2);
		submit = (Button)findViewById(R.id.game_submit);
		clear = (Button)findViewById(R.id.game_clear);
		headerText = (TextView)findViewById(R.id.game_title);
		sportImg = (ImageView)findViewById(R.id.game_sport_image);
		if(!HandleInput.confirmInternet(cont))
		{
			headerText.setText("No Internet Connection Available");
			for(int i = 0; i < menuObj.size(); i++)
			{
				menuObj.getItem(i).setEnabled(false);
			}
			sport.setClickable(false);
			team1.setClickable(false);
			team2.setClickable(false);
			submit.setClickable(false);
			clear.setClickable(false);
		}
		else
		{
			sport.setClickable(true);
			team1.setClickable(true);
			team2.setClickable(true);
			submit.setClickable(true);
			clear.setClickable(true);
			for(int i = 0; i < menuObj.size(); i++)
			{
				menuObj.getItem(i).setEnabled(true);
			}
			headerText.setText("Select a Sport Below");
			sport.setSelection(0);
		}
	}
	
	/**
	 * Sets the variables and populates the first spinner
	 */
	public void initialSetUp()
	{ 
		sport = (Spinner)findViewById(R.id.game_sport_spinner);
		team1 = (Spinner)findViewById(R.id.game_team1_name_spinner);
		team2 = (Spinner)findViewById(R.id.game_team2_name_spinner);
		submit = (Button)findViewById(R.id.game_submit);
		clear = (Button)findViewById(R.id.game_clear);
		sportImg = (ImageView)findViewById(R.id.game_sport_image);
		headerText = (TextView)findViewById(R.id.game_title);
		sw = (LinearLayout)findViewById(R.id.statwell);
		setUpInterface();
		ManageSportSelection.populateSpinner(sport, cont);
	}
	 
	/**
	 * Sets up the spinners and the relavent listeners such that
	 * when a relevant item is picked, it unhides stuff
	 */
	public void setUpInterface()
	{
		sport = (Spinner)findViewById(R.id.game_sport_spinner);
		team1 = (Spinner)findViewById(R.id.game_team1_name_spinner);
		team2 = (Spinner)findViewById(R.id.game_team2_name_spinner);
		submit = (Button)findViewById(R.id.game_submit);
		clear = (Button)findViewById(R.id.game_clear);
		sportImg = (ImageView)findViewById(R.id.game_sport_image);
		headerText = (TextView)findViewById(R.id.game_title);
		team1.setVisibility(View.INVISIBLE);
		team2.setVisibility(View.INVISIBLE);
		submit.setVisibility(View.INVISIBLE); 
		clear.setVisibility(View.INVISIBLE);
		sport.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				isSubmit = false;
				sw.removeAllViews();
				if(arg1 != null && ((TextView)arg1).getText() != null && 
						!((TextView)arg1).getText().toString().equals("Select a Sport")  && sport.getAdapter() != null
						&& sport.getAdapter().getCount() > 0)
				{
					sportImg.setVisibility(View.VISIBLE);
					sportStr = ((TextView)arg1).getText().toString();	
					clear.setVisibility(View.VISIBLE);
					team1.setVisibility(View.VISIBLE);
					team1.setSelection(0);
					team2.setVisibility(View.INVISIBLE);
					submit.setVisibility(View.INVISIBLE);
					headerText.setText("Select the First Team Below");
					ManageSportSelection.setSportImage(sportStr, cont, sportImg, obj);
				}
				else
				{
					sportImg.setVisibility(View.INVISIBLE);
					team1.setVisibility(View.INVISIBLE);
					team2.setVisibility(View.INVISIBLE);
					submit.setVisibility(View.INVISIBLE); 
					clear.setVisibility(View.INVISIBLE);
					headerText.setText("Select a Sport Below");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		team1.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				sw.removeAllViews();
				isSubmit = false;
				if(arg1 != null && !((TextView)arg1).getText().toString().equals("Select a Team"))
				{
					team2.setVisibility(View.VISIBLE);
					team1Str = ((TextView)arg1).getText().toString();
					headerText.setText("Select the Second Team Below");
					team2.setSelection(0);
					obj.getOpponentsInit(team1Str, (Activity)cont);
				}
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		team2.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				isSubmit = false;
				sw.removeAllViews();
				if(!((TextView)arg1).getText().toString().equals("Select a Team"))
				{
					submit.setVisibility(View.VISIBLE);
					team2Str = ((TextView)arg1).getText().toString();
					obj.team2 = team2Str;
					obj.team2ID = obj.teamIDMap.get(obj.team2);
					if(((TextView)team1.getSelectedView()).getText().toString().equals("Select a Team"))
					{
						headerText.setText("Select the Second Team Below");
					}
					else
					{
						headerText.setText("Hit Submit When You're Ready");
						submit.setVisibility(View.VISIBLE);
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				sw.removeAllViews();
				isSubmit = true;
				obj.team2 = (((TextView)team2.getSelectedView()).getText().toString());
				obj.team2ID = obj.teamIDMap.get(obj.team2);
				headerText.setText(obj.team1 + " vs. " + obj.team2);
				APIInteraction.getOpponentDates(obj, (Activity) cont);
				HandleInput.checkFavorite(obj, cont);
				setStatWellDialog(true);
				
			}
		});
		clear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				sw.removeAllViews();
				obj.clearObject();
				sport.setSelection(0);
				team1.setVisibility(View.INVISIBLE);
				team2.setVisibility(View.INVISIBLE);
				headerText.setText("Select a Sport Below");
				sportImg.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	/**
	 * Makes the dialog for statwell show up
	 */
	public void setStatWellDialog(boolean cancel)
	{
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.statwell_game);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
		dialog.show();
		final RadioButton gameInfo = (RadioButton)dialog.findViewById(R.id.statwell_game_info);//.statwell_player_info);
		final RadioButton gameStats = (RadioButton)dialog.findViewById(R.id.statwell_game_stats);//.statwell_team_info);
		final RadioButton venueInfo = (RadioButton)dialog.findViewById(R.id.statwell_venue_info);//.statwell_team_stats);
		Button close = (Button)dialog.findViewById(R.id.statwell_game_cancel);
		close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		if(cancel)
		{
			dialog.setCancelable(false);
			close.setVisibility(View.GONE);
		}
		
		Button submit = (Button)dialog.findViewById(R.id.statwell_game_submit);
		submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//MORE HERE LATER
				if(gameInfo.isChecked())
				{
					obj.statwellSetting = "Game Information";
				}
				else if(gameStats.isChecked())
				{
					obj.statwellSetting = "Game Statistics";
				}
				else if(venueInfo.isChecked())
				{
					obj.statwellSetting = "Venue Information";
				}
				sw.removeAllViews();
				StatWellUsage.statWellInit(obj, cont);
				dialog.dismiss();
			}
		});
	}
}

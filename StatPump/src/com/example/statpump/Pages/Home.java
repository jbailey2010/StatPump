package com.example.statpump.Pages;

import java.util.ArrayList;


import java.util.List;

import com.statpump.statpump.R;
import com.statpump.statpump.R.layout;
import com.statpump.statpump.R.menu;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.example.statpump.ClassFiles.APIInteraction;
import com.example.statpump.ClassFiles.APIObject;
import com.example.statpump.ClassFiles.HandleInput;
import com.example.statpump.ClassFiles.HandleStats;
import com.example.statpump.ClassFiles.TwitterWork;
import com.example.statpump.ClassFiles.LittleStorage.PlayerSearchObject;
import com.example.statpump.InterfaceAugmentation.ManageSportSelection;





import com.example.statpump.InterfaceAugmentation.MyActionBarListener;
import com.example.statpump.InterfaceAugmentation.NDSpinner;
import com.example.statpump.InterfaceAugmentation.StatWellUsage;
import com.socialize.ActionBarUtils;
import com.socialize.Socialize;
import com.socialize.entity.Entity;
import com.socialize.ui.actionbar.ActionBarOptions;
import com.socialize.ui.actionbar.ActionBarView;

import android.os.Bundle;
import android.app.ActionBar; 
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Menu; 
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Handles user lookup for game related stuff
 * @author Jeff
 *
 */
public class Home extends Activity {
	public boolean isMax = false;
	final Context cont = this;
	static Context c;
	public List<String> sportList = new ArrayList<String>();
	public List<String> teamList = new ArrayList<String>();
	public Menu menuObj = null;
	NDSpinner sport;
	NDSpinner team1;
	NDSpinner team2;
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
	PlayerSearchObject po;
	ActionBarView view;
	Button search;
	Button vInfo;
	Button gInfo;
	Button gStats;
	SideNavigationView sideNavigationView;
	MyActionBarListener listener;
	
	/**
	 * Sets up initial loading/views for the activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		c = cont;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Socialize.onCreate(this, savedInstanceState);
		// Your entity key. May be passed as a Bundle parameter to your activity
		String entityKey = "http://www.statpump.com/homegamelookup";
		
		// Create an entity object including a name
		// The Entity object is Serializable, so you could also store the whole object in the Intent
		Entity entity = Entity.newInstance(entityKey, "Game Lookup Home");
		listener = new MyActionBarListener();
		// Wrap your existing view with the action bar.
		// your_layout refers to the resource ID of your current layout.
		//// Create an options instance to disable comments
		ActionBarOptions options = new ActionBarOptions();

		// Hide sharing
		options.setHideShare(true);
		options.setFillColor(Color.parseColor("#272727"));
		options.setBackgroundColor(Color.parseColor("#191919"));
		options.setAccentColor(Color.parseColor("#0000ff"));
		
		View actionBarWrapped = ActionBarUtils.showActionBar(this, R.layout.activity_home, entity, options);
		// Now set the view for your activity to be the wrapped view.
		setContentView(actionBarWrapped);		
		
		initialSetUp();		
		ISideNavigationCallback sideNavigationCallback = new ISideNavigationCallback() {
		    @Override
		    public void onSideNavigationItemClick(int itemId) {
		    	switch (itemId) {
	            case R.id.go_home:
	            	Intent intent = new Intent(cont, Loading.class);
	    	        cont.startActivity(intent);	
	                break;
	            case R.id.switch_lookup:
	            	Intent intent2 = new Intent(cont, HomeTeam.class);
	    	        cont.startActivity(intent2);	
	                break;
	            case R.id.fanscan:
	            	Intent fsIntent = new Intent(cont, FanScan.class);
	            	cont.startActivity(fsIntent);
	            	break;
	            case R.id.ticket_popup:
	            	HandleInput.ticketPopup(cont);
	            	break;
	            case R.id.help:
	            	HandleInput.helpPopUp(cont);
	                break;
	            case R.id.max_min_sw:
	            	if(isSubmit)
	            	{
	            		maxMinSW(cont);
	            	}
	            	else
	            	{
	            		Toast.makeText(cont, "Please fill out the fields below first", Toast.LENGTH_SHORT).show();
	            	}
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
		if(menuObj != null)
		{
			checkInternet();
		}
		search = (Button)findViewById(R.id.sw_game_search);
		vInfo = (Button)findViewById(R.id.sw_game_venueinfo);
		gInfo = (Button)findViewById(R.id.sw_game_gameinfo);
		gStats = (Button)findViewById(R.id.sw_game_gamestats);
		search.setVisibility(View.GONE);
		vInfo.setVisibility(View.GONE);
		gInfo.setVisibility(View.GONE);
		gStats.setVisibility(View.GONE);
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
			case R.id.twitter:
				TwitterWork.twitterInitial(cont);
		    	return true;
			case android.R.id.home:
		        sideNavigationView.toggleMenu();
		        return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	
	@Override
	protected void onResume() {
	    super.onResume();
	    Socialize.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		// Call Socialize in onPause
		Socialize.onPause(this);
	}

	@Override
	protected void onDestroy() {
		// Call Socialize in onDestroy before the activity is destroyed
		Socialize.onDestroy(this);
		
		super.onDestroy();
	}
	
	public void maxMinSW(Context cont) 
	{
		LinearLayout sw_base = (LinearLayout)findViewById(R.id.statwell_base);
		if(!isMax)
		{
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
			        ViewGroup.LayoutParams.MATCH_PARENT);
			p.topMargin = 0;
			sw_base.setLayoutParams(p);
			isMax = true;
		}
		else
		{
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
			        ViewGroup.LayoutParams.MATCH_PARENT);
			int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 205, getResources().getDisplayMetrics());
			p.topMargin = height;
			sw_base.setLayoutParams(p);
			isMax = false;
			
		}
	}
	
	/**
	 * Sees if there's internet. Adjusts interface as such
	 */
	public void checkInternet()
	{
		sport = (NDSpinner)findViewById(R.id.game_sport_spinner);
		team1 = (NDSpinner)findViewById(R.id.game_team1_name_spinner);
		team2 = (NDSpinner)findViewById(R.id.game_team2_name_spinner);
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
		sport = (NDSpinner)findViewById(R.id.game_sport_spinner);
		team1 = (NDSpinner)findViewById(R.id.game_team1_name_spinner);
		team2 = (NDSpinner)findViewById(R.id.game_team2_name_spinner);
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
		sport = (NDSpinner)findViewById(R.id.game_sport_spinner);
		team1 = (NDSpinner)findViewById(R.id.game_team1_name_spinner);
		team2 = (NDSpinner)findViewById(R.id.game_team2_name_spinner);
		submit = (Button)findViewById(R.id.game_submit);
		clear = (Button)findViewById(R.id.game_clear);
		sportImg = (ImageView)findViewById(R.id.game_sport_image);
		headerText = (TextView)findViewById(R.id.game_title);
		final RelativeLayout headerBase = (RelativeLayout)findViewById(R.id.header_base);
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
				if(arg1 != null && arg2 > 0
						&& sport.getAdapter().getCount() > 0)
				{
					sportImg.setVisibility(View.VISIBLE);
					//sportStr = ManageSportSelection.getSportSelection(arg2);
					sportStr = ((TextView)arg1).getText().toString();	
					clear.setVisibility(View.VISIBLE);
					team1.setVisibility(View.VISIBLE);
					team1.setSelection(0);
					team2.setVisibility(View.INVISIBLE);
					submit.setVisibility(View.INVISIBLE);
					headerText.setText("Select the First Team Below");
					headerText.setTextColor(Color.parseColor("#ffffff"));
					headerBase.setBackgroundResource(R.drawable.header_background);
					ManageSportSelection.setSportImage(sportStr, cont, sportImg, obj);
				}
				else
				{
					sportImg.setVisibility(View.INVISIBLE);
					team1.setVisibility(View.INVISIBLE);
					team2.setVisibility(View.INVISIBLE);
					submit.setVisibility(View.INVISIBLE); 
					clear.setVisibility(View.INVISIBLE);
					headerBase.setBackground(null);
					headerText.setTextColor(Color.parseColor("#000000"));
					headerText.setText("Select a Sport Below");
				}
				search.setTextColor(Color.parseColor("#ffffff"));
				vInfo.setTextColor(Color.parseColor("#ffffff"));
				gInfo.setTextColor(Color.parseColor("#ffffff"));
				gStats.setTextColor(Color.parseColor("#ffffff"));
				search.setVisibility(View.GONE);
				vInfo.setVisibility(View.GONE);
				gInfo.setVisibility(View.GONE);
				gStats.setVisibility(View.GONE);
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
				search.setTextColor(Color.parseColor("#ffffff"));
				vInfo.setTextColor(Color.parseColor("#ffffff"));
				gInfo.setTextColor(Color.parseColor("#ffffff"));
				gStats.setTextColor(Color.parseColor("#ffffff"));
				search.setVisibility(View.GONE);
				vInfo.setVisibility(View.GONE);
				gInfo.setVisibility(View.GONE);
				gStats.setVisibility(View.GONE);
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
				search.setTextColor(Color.parseColor("#ffffff"));
				vInfo.setTextColor(Color.parseColor("#ffffff"));
				gInfo.setTextColor(Color.parseColor("#ffffff"));
				gStats.setTextColor(Color.parseColor("#ffffff"));
				search.setVisibility(View.GONE);
				vInfo.setVisibility(View.GONE);
				gInfo.setVisibility(View.GONE);
				gStats.setVisibility(View.GONE);
				
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
				APIInteraction.getOpponentDates(obj, (Activity) cont);
				HandleInput.checkFavorite(obj, cont);
				setStatWellDialog(true);
				po = new PlayerSearchObject(cont, obj);
				search.setBackgroundResource(R.drawable.not_selected_tab);
				vInfo.setBackgroundResource(R.drawable.not_selected_tab);
				gInfo.setBackgroundResource(R.drawable.not_selected_tab);
				gStats.setBackgroundResource(R.drawable.not_selected_tab);
				search.setVisibility(View.VISIBLE);
				vInfo.setVisibility(View.VISIBLE);
				gInfo.setVisibility(View.VISIBLE);
				gStats.setVisibility(View.VISIBLE);
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
				headerText.setTextColor(Color.parseColor("#000000"));
				headerText.setBackground(null);
				headerText.setText("Select a Sport Below");
				sportImg.setVisibility(View.INVISIBLE);
				search.setBackgroundResource(R.drawable.not_selected_tab);
				vInfo.setBackgroundResource(R.drawable.not_selected_tab);
				gInfo.setBackgroundResource(R.drawable.not_selected_tab);
				gStats.setBackgroundResource(R.drawable.not_selected_tab);
				search.setTextColor(Color.parseColor("#ffffff"));
				vInfo.setTextColor(Color.parseColor("#ffffff"));
				gInfo.setTextColor(Color.parseColor("#ffffff"));
				gStats.setTextColor(Color.parseColor("#ffffff"));
				search.setVisibility(View.GONE);
				vInfo.setVisibility(View.GONE);
				gInfo.setVisibility(View.GONE);
				gStats.setVisibility(View.GONE);
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
				if(obj.matchID > 0 && po.players.size() > 0)
				{
					System.out.println(obj.matchHome + " - " + obj.matchDate + ", " + obj.matchID);
					if(gameInfo.isChecked())
					{
						obj.statwellSetting = "Game Information";
						gInfo.setBackgroundResource(R.drawable.selected_tab);
						gInfo.setTextColor(Color.parseColor("#000000"));
					}
					else if(gameStats.isChecked())
					{
						obj.statwellSetting = "Game Statistics";
						gStats.setBackgroundResource(R.drawable.selected_tab);
						gStats.setTextColor(Color.parseColor("#000000"));
					}
					else if(venueInfo.isChecked())
					{
						obj.statwellSetting = "Venue Information";
						vInfo.setBackgroundResource(R.drawable.selected_tab);
						vInfo.setTextColor(Color.parseColor("#000000"));
					}
					sw.removeAllViews();
					StatWellUsage.statWellInit(obj, cont, po);
					headerText.setText(obj.matchHome + " - " + obj.matchDate);	
					
					String entityKey = "http://www.statpump.com/" + sportStr + "/" + team1Str + "_vs_" + team2Str + "on" + obj.matchDate;
					Entity entity = Entity.newInstance(entityKey, obj.matchDate + ": " + obj.matchHome);
					view = listener.getActionBarView();
					
					if (view != null) {
						view.setEntity(entity);
						view.refresh();
					}	
					dialog.dismiss();
				}
				else
				{
					Toast.makeText(cont, "Please wait a moment...", Toast.LENGTH_SHORT).show();
				}
			}
		});
		search.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				sw.removeAllViews();
				po.searchInit(obj, cont, po, sw);
				search.setBackgroundResource(R.drawable.not_selected_tab);
				vInfo.setBackgroundResource(R.drawable.not_selected_tab);
				gInfo.setBackgroundResource(R.drawable.not_selected_tab);
				gStats.setBackgroundResource(R.drawable.not_selected_tab);
				search.setTextColor(Color.parseColor("#ffffff"));
				vInfo.setTextColor(Color.parseColor("#ffffff"));
				gInfo.setTextColor(Color.parseColor("#ffffff"));
				gStats.setTextColor(Color.parseColor("#ffffff"));
				search.setTextColor(Color.parseColor("#000000"));
				search.setBackgroundResource(R.drawable.selected_tab);
			}
		});
		vInfo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				sw.removeAllViews();
				obj.statwellSetting = "Venue Information";
				StatWellUsage.statWellInit(obj,  cont, po);
				search.setBackgroundResource(R.drawable.not_selected_tab);
				vInfo.setBackgroundResource(R.drawable.not_selected_tab);
				gInfo.setBackgroundResource(R.drawable.not_selected_tab);
				gStats.setBackgroundResource(R.drawable.not_selected_tab);
				search.setTextColor(Color.parseColor("#ffffff"));
				vInfo.setTextColor(Color.parseColor("#ffffff"));
				gInfo.setTextColor(Color.parseColor("#ffffff"));
				gStats.setTextColor(Color.parseColor("#ffffff"));
				vInfo.setTextColor(Color.parseColor("#000000"));
				vInfo.setBackgroundResource(R.drawable.selected_tab);
			}
		});
		gInfo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				sw.removeAllViews();
				obj.statwellSetting = "Game Information";
				StatWellUsage.statWellInit(obj,  cont, po);
				search.setBackgroundResource(R.drawable.not_selected_tab);
				vInfo.setBackgroundResource(R.drawable.not_selected_tab);
				gInfo.setBackgroundResource(R.drawable.not_selected_tab);
				gStats.setBackgroundResource(R.drawable.not_selected_tab);
				search.setTextColor(Color.parseColor("#ffffff"));
				vInfo.setTextColor(Color.parseColor("#ffffff"));
				gInfo.setTextColor(Color.parseColor("#ffffff"));
				gStats.setTextColor(Color.parseColor("#ffffff"));
				gInfo.setTextColor(Color.parseColor("#000000"));
				gInfo.setBackgroundResource(R.drawable.selected_tab);
			}
		});
		gStats.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!obj.sport.contains("Soccer"))
				{
					sw.removeAllViews();
					obj.statwellSetting = "Game Statistics";
					StatWellUsage.statWellInit(obj, cont, po);
					search.setBackgroundResource(R.drawable.not_selected_tab);
					vInfo.setBackgroundResource(R.drawable.not_selected_tab);
					gInfo.setBackgroundResource(R.drawable.not_selected_tab);
					gStats.setBackgroundResource(R.drawable.not_selected_tab);
					search.setTextColor(Color.parseColor("#ffffff"));
					vInfo.setTextColor(Color.parseColor("#ffffff"));
					gInfo.setTextColor(Color.parseColor("#ffffff"));
					gStats.setTextColor(Color.parseColor("#ffffff"));
					gStats.setTextColor(Color.parseColor("#000000"));
					gStats.setBackgroundResource(R.drawable.selected_tab);
				}
				else
				{
					Toast.makeText(cont, "Game Stats not currently available for Soccer", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}

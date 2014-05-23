package com.example.statpump.Pages;

import java.util.ArrayList;


import java.util.List;

import com.statpump.statpump.R;
import com.statpump.statpump.R.layout;
import com.statpump.statpump.R.menu;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

/**
 * Handles the user lookup for team specific stuff
 * @author Jeff
 *
 */
public class HomeTeam extends Activity {
	public boolean isMax = false;
	final Context cont = this;
	static Context c;
	public List<String> sportList = new ArrayList<String>();
	public List<String> teamList = new ArrayList<String>();
	public Menu menuObj = null;
	Spinner sport;
	NDSpinner team1;
	String sportStr;
	String team1Str;
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
	Button pInfo;
	Button tInfo;
	Button pStats;
	SideNavigationView sideNavigationView;
	MyActionBarListener listener;
	/**
	 * Sets up the layout, initial loading...etc.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		c = cont;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_team);
		Socialize.onCreate(this, savedInstanceState);
		// Your entity key. May be passed as a Bundle parameter to your activity
		String entityKey = "http://www.statpump.com/hometeamlookup";
		
		// Create an entity object including a name
		// The Entity object is Serializable, so you could also store the whole object in the Intent
		Entity entity = Entity.newInstance(entityKey, "Team Lookup Home");
		listener = new MyActionBarListener();
		// Wrap your existing view with the action bar.
		// your_layout refers to the resource ID of your current layout.
		// Create an options instance to disable comments
			ActionBarOptions options = new ActionBarOptions();
 
			// Hide sharing
			options.setHideShare(true);
			options.setFillColor(Color.parseColor("#272727"));
			options.setBackgroundColor(Color.parseColor("#191919"));
			options.setAccentColor(Color.parseColor("#0000ff"));
			
			View actionBarWrapped = ActionBarUtils.showActionBar(this, R.layout.activity_home_team, entity, options, listener);
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
	            	Intent intent2 = new Intent(cont, Home.class);
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
	    sideNavigationView.setMenuItems(R.menu.side_navigation_menu_team);
	    sideNavigationView.setMenuClickCallback(sideNavigationCallback);
	   // sideNavigationView.setMode(/*SideNavigationView.Mode*/);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
		if(menuObj != null)
		{
			checkInternet();
		}
		search = (Button)findViewById(R.id.sw_team_search);
		pInfo = (Button)findViewById(R.id.sw_team_playerinfo);
		tInfo = (Button)findViewById(R.id.sw_team_teaminfo);
		pStats = (Button)findViewById(R.id.sw_team_playerstats);
		search.setVisibility(View.GONE);
		pInfo.setVisibility(View.GONE);
		tInfo.setVisibility(View.GONE);
		pStats.setVisibility(View.GONE);
	} 
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuObj = menu;
		getMenuInflater().inflate(R.menu.home_team, menu);
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
	 * Sees if there's internet, adjusts if there is
	 */
	public void checkInternet()
	{
		sport = (Spinner)findViewById(R.id.team_sport_spinner);
		team1 = (NDSpinner)findViewById(R.id.team_name_spinner);
		submit = (Button)findViewById(R.id.team_submit);
		clear = (Button)findViewById(R.id.team_clear);
		headerText = (TextView)findViewById(R.id.team_title);
		sportImg = (ImageView)findViewById(R.id.team_sport_image);
		if(!HandleInput.confirmInternet(cont))
		{
			sport.setClickable(false); 
			team1.setClickable(false);
			submit.setClickable(false);
			clear.setClickable(false);
			headerText.setText("No Internet Connection Available");
			for(int i = 0; i < menuObj.size(); i++)
			{
				menuObj.getItem(i).setEnabled(false);
			}
		}
		else
		{
			sport.setClickable(true);
			team1.setClickable(true);
			submit.setClickable(true);
			clear.setClickable(true);
			headerText.setText("Select a Sport Below");
			sport.setSelection(0);
			for(int i = 0; i < menuObj.size(); i++)
			{
				menuObj.getItem(i).setEnabled(true);
			}
		}
	}
	
	/**
	 * Set global fields
	 */
	public void initialSetUp(){
		sport = (Spinner)findViewById(R.id.team_sport_spinner);
		team1 = (NDSpinner)findViewById(R.id.team_name_spinner);
		submit = (Button)findViewById(R.id.team_submit);
		clear = (Button)findViewById(R.id.team_clear);
		headerText = (TextView)findViewById(R.id.team_title);
		sportImg = (ImageView)findViewById(R.id.team_sport_image);
		setUpInterface();
		ManageSportSelection.populateSpinner(sport, cont);
	}
	
	/**
	 * Sets up the spinners and the relavent listeners such that
	 * when a relevant item is picked, it unhides stuff
	 */
	public void setUpInterface()
	{
		sport = (Spinner)findViewById(R.id.team_sport_spinner);
		sw = (LinearLayout)findViewById(R.id.statwell);
		team1 = (NDSpinner)findViewById(R.id.team_name_spinner);
		obj.setUpObject(sport, team1, null);
		submit = (Button)findViewById(R.id.team_submit);
		clear = (Button)findViewById(R.id.team_clear);
		headerText = (TextView)findViewById(R.id.team_title);
		final RelativeLayout headerBase = (RelativeLayout)findViewById(R.id.header_base);
		sportImg = (ImageView)findViewById(R.id.team_sport_image);
		team1.setVisibility(View.INVISIBLE);
		submit.setVisibility(View.INVISIBLE);
		clear.setVisibility(View.INVISIBLE);
		sport.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				isSubmit = false;
				sw.removeAllViews();
				if(arg1 != null && arg2 > 0 && 
						sport.getAdapter() != null && sport.getAdapter().getCount() > 0)
				{
					sportImg.setVisibility(View.VISIBLE);
					//sportStr = ManageSportSelection.getSportSelection(arg2);
					sportStr = ((TextView)arg1).getText().toString();	
					headerText.setText("Select the Team Below");
					headerText.setTextColor(Color.parseColor("#ffffff"));
					headerBase.setBackgroundResource(R.drawable.header_background);
					clear.setVisibility(View.VISIBLE);
					team1.setVisibility(View.VISIBLE);
					team1.setSelection(0);
					submit.setVisibility(View.INVISIBLE);
					ManageSportSelection.setSportImage(sportStr, cont, sportImg, obj);
				}
				else
				{
					sportImg.setVisibility(View.INVISIBLE);
					team1.setVisibility(View.INVISIBLE);
					submit.setVisibility(View.INVISIBLE);
					clear.setVisibility(View.INVISIBLE);
					headerText.setText("Select a Sport Below");
					headerText.setTextColor(Color.parseColor("#000000"));
					headerBase.setBackground(null);
				} 
				search.setTextColor(Color.parseColor("#ffffff"));
				pInfo.setTextColor(Color.parseColor("#ffffff"));
				tInfo.setTextColor(Color.parseColor("#ffffff"));
				pStats.setTextColor(Color.parseColor("#ffffff"));
				search.setVisibility(View.GONE);
				pInfo.setVisibility(View.GONE);
				tInfo.setVisibility(View.GONE);
				pStats.setVisibility(View.GONE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		team1.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				isSubmit = false;
				sw.removeAllViews();
				if(!((TextView)arg1).getText().toString().equals("Select a Team"))
				{
					headerText.setText("Hit Submit When You're Ready");
					submit.setVisibility(View.VISIBLE);
					team1Str = ((TextView)arg1).getText().toString();
					obj.team1 = team1Str;
					obj.team1ID = obj.teamIDMap.get(obj.team1);
					search.setTextColor(Color.parseColor("#ffffff"));
					pInfo.setTextColor(Color.parseColor("#ffffff"));
					tInfo.setTextColor(Color.parseColor("#ffffff"));
					pStats.setTextColor(Color.parseColor("#ffffff"));
					search.setVisibility(View.GONE);
					pInfo.setVisibility(View.GONE);
					tInfo.setVisibility(View.GONE);
					pStats.setVisibility(View.GONE);
				}
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				isSubmit = true;
				headerText.setText(obj.team1);
				HandleInput.checkFavorite(obj, cont);
				setStatWellDialog(true);
				sw.removeAllViews();
				po = new PlayerSearchObject(cont, obj);
				search.setBackgroundResource(R.drawable.not_selected_tab);
				pInfo.setBackgroundResource(R.drawable.not_selected_tab);
				tInfo.setBackgroundResource(R.drawable.not_selected_tab);
				pStats.setBackgroundResource(R.drawable.not_selected_tab);
				search.setTextColor(Color.parseColor("#ffffff"));
				pInfo.setTextColor(Color.parseColor("#ffffff"));
				tInfo.setTextColor(Color.parseColor("#ffffff"));
				pStats.setTextColor(Color.parseColor("#ffffff"));
				search.setVisibility(View.VISIBLE);
				pInfo.setVisibility(View.VISIBLE);
				tInfo.setVisibility(View.VISIBLE);
				pStats.setVisibility(View.VISIBLE);
			}
		});
		clear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				sw.removeAllViews();
				obj.clearObject();
				sport.setSelection(0);
				headerText.setTextColor(Color.parseColor("#000000"));
				headerText.setBackground(null);
				team1.setVisibility(View.INVISIBLE);
				headerText.setText("Select a Sport Below");
				sportImg.setVisibility(View.INVISIBLE);
				search.setBackgroundResource(R.drawable.not_selected_tab);
				pInfo.setBackgroundResource(R.drawable.not_selected_tab);
				tInfo.setBackgroundResource(R.drawable.not_selected_tab);
				pStats.setBackgroundResource(R.drawable.not_selected_tab);
				search.setTextColor(Color.parseColor("#ffffff"));
				pInfo.setTextColor(Color.parseColor("#ffffff"));
				tInfo.setTextColor(Color.parseColor("#ffffff"));
				pStats.setTextColor(Color.parseColor("#ffffff"));
				search.setVisibility(View.GONE);
				pInfo.setVisibility(View.GONE);
				tInfo.setVisibility(View.GONE);
				pStats.setVisibility(View.GONE);
			}
		});
	}
	
	/**
	 * Sets the statwell dialog
	 */
	public void setStatWellDialog(boolean cancel)
	{
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.statwell_team);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
		dialog.show();
		final RadioButton playerInfo = (RadioButton)dialog.findViewById(R.id.statwell_player_info);
		final RadioButton teamInfo = (RadioButton)dialog.findViewById(R.id.statwell_team_info);
		final RadioButton teamStats = (RadioButton)dialog.findViewById(R.id.statwell_team_stats);
		Button close = (Button)dialog.findViewById(R.id.statwell_team_cancel);
		close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		if(cancel)
		{
			close.setVisibility(View.GONE);
			dialog.setCancelable(false);
		}
		String entityKey = "http://www.statpump.com/" + sportStr + "/" + team1Str;
		Entity entity = Entity.newInstance(entityKey, team1Str);
		view = listener.getActionBarView();

		if (view != null) {
			view.setEntity(entity);
			view.refresh();
		}	
		Button submit = (Button)dialog.findViewById(R.id.statwell_team_submit);
		submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(po.players.size() > 0)
				{
					if(playerInfo.isChecked())
					{
						obj.statwellSetting = "Player Information";
						pInfo.setBackgroundResource(R.drawable.selected_tab);
						pInfo.setTextColor(Color.parseColor("#000000"));
					}
					else if(teamInfo.isChecked())
					{
						obj.statwellSetting = "Team Information";
						tInfo.setBackgroundResource(R.drawable.selected_tab);
						tInfo.setTextColor(Color.parseColor("#000000"));
					}
					else if(teamStats.isChecked())
					{
						obj.statwellSetting = "Player Statistics";
						pStats.setBackgroundResource(R.drawable.selected_tab);
						pStats.setTextColor(Color.parseColor("#000000"));
					}
					sw.removeAllViews();
					StatWellUsage.statWellInit(obj, cont, po);
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
				pInfo.setBackgroundResource(R.drawable.not_selected_tab);
				tInfo.setBackgroundResource(R.drawable.not_selected_tab);
				pStats.setBackgroundResource(R.drawable.not_selected_tab);
				search.setTextColor(Color.parseColor("#ffffff"));
				pInfo.setTextColor(Color.parseColor("#ffffff"));
				tInfo.setTextColor(Color.parseColor("#ffffff"));
				pStats.setTextColor(Color.parseColor("#ffffff"));
				search.setTextColor(Color.parseColor("#000000"));
				search.setBackgroundResource(R.drawable.selected_tab);
			}
		});
		pInfo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				sw.removeAllViews();
				obj.statwellSetting = "Player Information";
				StatWellUsage.statWellInit(obj,  cont, po);
				search.setBackgroundResource(R.drawable.not_selected_tab);
				pInfo.setBackgroundResource(R.drawable.not_selected_tab);
				tInfo.setBackgroundResource(R.drawable.not_selected_tab);
				pStats.setBackgroundResource(R.drawable.not_selected_tab);
				search.setTextColor(Color.parseColor("#ffffff"));
				pInfo.setTextColor(Color.parseColor("#ffffff"));
				tInfo.setTextColor(Color.parseColor("#ffffff"));
				pStats.setTextColor(Color.parseColor("#ffffff"));
				pInfo.setTextColor(Color.parseColor("#000000"));
				pInfo.setBackgroundResource(R.drawable.selected_tab);
			}
		});
		tInfo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				sw.removeAllViews();
				obj.statwellSetting = "Team Information";
				StatWellUsage.statWellInit(obj,  cont, po);
				search.setBackgroundResource(R.drawable.not_selected_tab);
				pInfo.setBackgroundResource(R.drawable.not_selected_tab);
				tInfo.setBackgroundResource(R.drawable.not_selected_tab);
				pStats.setBackgroundResource(R.drawable.not_selected_tab);
				search.setTextColor(Color.parseColor("#ffffff"));
				pInfo.setTextColor(Color.parseColor("#ffffff"));
				tInfo.setTextColor(Color.parseColor("#ffffff"));
				pStats.setTextColor(Color.parseColor("#ffffff"));
				tInfo.setTextColor(Color.parseColor("#000000"));
				tInfo.setBackgroundResource(R.drawable.selected_tab);
			}
		});
		pStats.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				sw.removeAllViews();
				obj.statwellSetting = "Player Statistics";
				StatWellUsage.statWellInit(obj, cont, po);
				search.setBackgroundResource(R.drawable.not_selected_tab);
				pInfo.setBackgroundResource(R.drawable.not_selected_tab);
				tInfo.setBackgroundResource(R.drawable.not_selected_tab);
				pStats.setBackgroundResource(R.drawable.not_selected_tab);
				search.setTextColor(Color.parseColor("#ffffff"));
				pInfo.setTextColor(Color.parseColor("#ffffff"));
				tInfo.setTextColor(Color.parseColor("#ffffff"));
				pStats.setTextColor(Color.parseColor("#ffffff"));
				pStats.setTextColor(Color.parseColor("#000000"));
				pStats.setBackgroundResource(R.drawable.selected_tab);
			}
		});
	}

}

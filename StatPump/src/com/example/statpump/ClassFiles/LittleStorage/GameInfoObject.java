package com.example.statpump.ClassFiles.LittleStorage;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.statpump.statpump.R;
import com.example.statpump.ClassFiles.APIObject;

public class GameInfoObject 
{
	static VenueInfoObject vio;
	static TeamInfoObject tio1;
	static TeamInfoObject tio2;
	
	/**
	 * Sets the venue info object data
	 * @param obj
	 * @param cont
	 * @param po
	 */ 
	public void createVenueInfo(APIObject obj, Context cont, PlayerSearchObject po)
	{
		vio = new VenueInfoObject(obj, cont, false);
	}
	
	/**
	 * Sets the first team's information
	 * @param result
	 * @param obj
	 * @param a
	 */
	public static void getTeamInfo1(VenueInfoObject result, APIObject obj, Context a)
	{
		vio = result;
		tio1 = new TeamInfoObject(obj, a, obj.team1, 2);
	}
 
	/**
	 * Sets the second team's information 
	 * @param result
	 * @param obj
	 * @param a
	 */
	public static void getTeamInfo2(TeamInfoObject result, APIObject obj, Context a) {
		tio1 = result;
		tio2 = new TeamInfoObject(obj, a, obj.team2, 3);
	}

	/**
	 * Gets the final teams information and sets the display
	 * @param result
	 * @param obj
	 * @param a
	 */
	public static void setDisplay(TeamInfoObject result, APIObject obj, Context cont) {
		tio2 = result;
		LinearLayout layout = (LinearLayout)((Activity) cont).findViewById(R.id.statwell);
		layout.removeAllViews();
		View res = ((Activity) cont).getLayoutInflater().inflate(R.layout.sw_game_info, layout, false);
		TextView header = (TextView)res.findViewById(R.id.game_info_header);
		header.setText(obj.matchHome);
		TextView date = (TextView)res.findViewById(R.id.game_info_date);
		date.setText(obj.matchDate);
		TextView outcome = (TextView)res.findViewById(R.id.game_info_outcome);
		if(result.isPlayed)
		{
			outcome.setText(result.winner + ", " + result.score);
		}
		else
		{
			outcome.setVisibility(View.GONE);
		}
		TextView refs = (TextView)res.findViewById(R.id.game_info_refs);
		if(tio2.referees != null && tio2.referees.length() > 2)
		{
			refs.setText("Referees: " + tio2.referees);
		}
		else
		{
			refs.setVisibility(View.GONE);
		}
		TextView team1 = (TextView)res.findViewById(R.id.game_info_team1);
		TextView team2 = (TextView)res.findViewById(R.id.game_info_team2);
		TextView team1Rec = (TextView)res.findViewById(R.id.game_info_team1record);
		TextView team2Rec = (TextView)res.findViewById(R.id.game_info_team2record);
		TextView team1Place = (TextView)res.findViewById(R.id.game_info_team1divrank);
		TextView team2Place = (TextView)res.findViewById(R.id.game_info_team2divrank);
		if(tio1.officialName == null || tio1.officialName.length() < 2)
		{
			team1.setText(tio1.clubName);
		}
		else
		{
			team1.setText(tio1.officialName);
		}
		if(tio2.officialName == null || tio2.officialName.length() < 2)
		{
			team2.setText(tio2.clubName);
		}
		else
		{
			team2.setText(tio2.officialName);
		}
		if(tio1.record != null && tio1.record.length() > 2)
		{
			team1Rec.setText(tio1.record);
		}
		else
		{
			team1Rec.setVisibility(View.GONE);
		}
		if(tio2.record != null && tio2.record.length() > 2)
		{
			team2Rec.setText(tio2.record);
		}
		else
		{
			team2Rec.setVisibility(View.GONE);
		}
		
		if(tio1.place != null && tio1.record.length() > 2)
		{
			if(tio1.place.equals("N/A"))
			{
				team1Place.setText(tio1.place);
			}
			else if(tio1.group != null && tio1.group.length() > 2)
			{
				team1Place.setText("Ranked " + tio1.place + " in " + tio1.group);
			}
			else
			{
				team1Place.setText("Ranked " + tio1.place);
			}
		}
		else
		{
			team1Place.setVisibility(View.GONE);
		}
		if(tio2.place != null && tio2.record.length() > 2)
		{
			if(tio2.place.equals("N/A"))
			{
				team2Place.setText(tio2.place);
			}
			else if(tio2.group != null && tio2.group.length() > 2)
			{
				team2Place.setText("Ranked " + tio2.place + " in " + tio2.group);
			}
			else
			{
				team2Place.setText("Ranked " + tio2.place);
			}
		}
		else
		{
			team2Place.setVisibility(View.GONE);
		}
		
		TextView loc = (TextView)res.findViewById(R.id.game_info_info_header);
		loc.setText(vio.name);
		TextView founded = (TextView)res.findViewById(R.id.game_info_info_founded);
		founded.setText("Opened " + vio.founded);
		TextView homeTeam = (TextView)res.findViewById(R.id.game_info_info_hometeam);
		homeTeam.setText("Home of " + vio.homeTeam);
		TextView capacity = (TextView)res.findViewById(R.id.game_info_info_capacity);
		capacity.setText("Holds " + vio.capacity + " people");
		TextView type = (TextView)res.findViewById(R.id.game_info_fieldtype);
		type.setText("Field type is " + vio.fieldType);
		TextView addr = (TextView)res.findViewById(R.id.game_info_address);
		addr.setText(vio.address);
		layout.addView(res);
		
	}
}

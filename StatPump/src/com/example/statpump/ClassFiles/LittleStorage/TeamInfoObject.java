package com.example.statpump.ClassFiles.LittleStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.statpump.statpump.R;
import com.example.statpump.ClassFiles.APIInteraction;
import com.example.statpump.ClassFiles.APIObject;
import com.example.statpump.ClassFiles.HandleInput;
import com.example.statpump.ClassFiles.APIInteraction.ParseTeamID;
import com.example.statpump.InterfaceAugmentation.OutBounceListView;

/**
 * Gets team info from the API
 * @author Jeff
 *
 */
public class TeamInfoObject 
{
	public String clubName;
	public String officialName;
	public String city;
	public String country;
	public String address;
	public String zip;
	public String phone;
	public String fax;
	public String url;
	public String email;
	public String founded;
	public String record;
	public String place;
	public String group;
	public String winner;
	public String score;
	public boolean isPlayed;
	public String referees;
	public APIObject obj;
	public List<String> schedule = new ArrayList<String>();
	public TeamInfoObject(APIObject o, Context cont, String team1, int flag)
	{
		spawnAsync(o, cont, team1, flag);
	}
	
	/**
	 * Spawns the async task to get team info
	 * @param ao
	 * @param cont
	 * @param team1 
	 */
	public void spawnAsync(APIObject ao, Context cont, String team1, int flag)
	{
		obj = ao;
		ParseTeamInfo task = this.new ParseTeamInfo(obj, cont, this, flag);
		task.execute(obj, this, team1);
	}
	
	/**
	 * Fills the content of team info
	 */
	public void teamInfoFill(TeamInfoObject result, final Activity cont) 
	{
		LinearLayout layout = (LinearLayout)cont.findViewById(R.id.statwell);
		View res = ((Activity) cont).getLayoutInflater().inflate(R.layout.sw_team_info, layout, false);
		TextView header = (TextView)res.findViewById(R.id.sw_team_info_header);
		if(this.officialName == null || this.officialName.length() < 2)
		{
			header.setText(this.clubName);
		}
		else
		{
			header.setText(this.officialName);
		}
		TextView founded = (TextView)res.findViewById(R.id.sw_team_info_founded);
		if(this.founded == null || this.founded.length() < 2)
		{
			founded.setVisibility(View.GONE);
		}
		else
		{
			founded.setText("Founded: " + this.founded);
		}
		final TextView email = (TextView)res.findViewById(R.id.sw_team_info_email);
		if(this.email == null || this.email.length() < 2)
		{
			email.setVisibility(View.GONE);
		}
		else
		{
			email.setText(this.email);
		}
		email.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	int action = event.getAction();
                switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        }); 
		email.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
				            "mailto",email.getText().toString(), null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT");
				cont.startActivity(Intent.createChooser(emailIntent, "Send email"));
				return true;
			}
		});
		final TextView url = (TextView)res.findViewById(R.id.sw_team_info_url);
		if(this.url == null || this.url.length() < 2)
		{
			url.setVisibility(View.GONE);
		}
		else
		{
			url.setText(this.url);
		}
		url.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	int action = event.getAction();
                switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
		url.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url.getText().toString()));
				cont.startActivity(i);
				return true;
			}
		});
		TextView fax = (TextView)res.findViewById(R.id.sw_team_info_fax);
		if(this.fax == null || this.fax.length() < 2)
		{
			fax.setVisibility(View.GONE);
		}
		else
		{
			fax.setText("Fax: " + this.fax);
		}
		TextView phone = (TextView)res.findViewById(R.id.sw_team_info_phone);
		if(this.phone == null || this.phone.length() < 2)
		{
			phone.setVisibility(View.GONE);
		}
		else
		{
			phone.setText("Phone: " + this.phone);
		}
		TextView address = (TextView)res.findViewById(R.id.sw_team_info_address);
		TextView address2 = (TextView) res.findViewById(R.id.sw_team_info_address2);
		TextView address3 = (TextView) res.findViewById(R.id.sw_team_info_address3);
		if(this.address != null && this.address.length() > 2)
		{
			address.setText(this.address);
		}
		else
		{
			address.setVisibility(View.GONE);
		}
		if(this.city != null && this.city.length() > 2)
		{
			if(this.zip == null || this.zip.length() < 2)
			{
				address2.setText(this.city);
			}
			else
			{
				address2.setText(this.city + ", " + this.zip);
			}
		}
		else
		{
			address2.setVisibility(View.GONE);
		}
		if(this.country != null && this.country.length() > 2)
		{
			address3.setText(this.country);
		}
		else
		{
			address3.setVisibility(View.GONE);
		}
		TextView record = (TextView)res.findViewById(R.id.sw_team_info_record);
		if(this.record != null && this.record.length() > 2)
		{
			record.setText(this.record);
		}
		else
		{
			record.setVisibility(View.GONE);
		}
		TextView place = (TextView)res.findViewById(R.id.sw_team_info_place);
		if(this.place != null && this.record.length() > 2)
		{
			if(place.equals("N/A"))
			{
				place.setText(this.place);
			}
			else if(this.group != null && this.group.length() > 2)
			{
				place.setText("Ranked " + this.place + " in " + this.group);
			}
			else
			{
				place.setText("Ranked " + this.place);
			}
		}
		else
		{
			place.setVisibility(View.GONE);
		}
		OutBounceListView schedule = (OutBounceListView)res.findViewById(R.id.game_info_schedule);
		if(this.schedule.size() > 1)
		{
			final List<Map<String, String>> data = new ArrayList<Map<String, String>>();
			for(String match : this.schedule)
			{
		    	Map<String, String> datum = new HashMap<String, String>(2);
				String[] matchData = match.split("////");
				datum.put("main", matchData[0] + " (" + matchData[1] + ")");
				if(matchData.length == 3)
				{
					datum.put("sub", matchData[2]);
				}
				else
				{
					datum.put("sub", "");
				}
				data.add(datum);
			}
		    final SimpleAdapter adapter = new SimpleAdapter(cont, data, 
		    		R.layout.bold_header_elem, 
		    		new String[] {"main", "sub"}, 
		    		new int[] {R.id.text1, 
		    			R.id.text2});
		    schedule.setAdapter(adapter);
			schedule.setOnTouchListener(new ListView.OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	            	int action = event.getAction();
	                switch (action) {
	                case MotionEvent.ACTION_DOWN:
	                    // Disallow ScrollView to intercept touch events.
	                    v.getParent().requestDisallowInterceptTouchEvent(true);
	                    break;

	                case MotionEvent.ACTION_UP:
	                    // Allow ScrollView to intercept touch events.
	                    v.getParent().requestDisallowInterceptTouchEvent(false);
	                    break;
	                }

	                // Handle ListView touch events.
	                v.onTouchEvent(event);
	                return true;
	            }
	        });
		}
		else
		{
			schedule.setVisibility(View.GONE);
		}
		layout.addView(res);
	}

	/**
	 * Populates the new object
	 * @param doc
	 * @param obj
	 * @return
	 */
	public static TeamInfoObject parseXML(Document doc, TeamInfoObject obj)
	{
        Elements links = doc.select("team");
        for (Element element : links) 
        {
            obj.clubName = element.attr("club_name");
            obj.officialName = element.attr("official_name");
            obj.city = element.attr("city");
            obj.country = element.attr("country");
            obj.address = element.attr("address");
            obj.zip = element.attr("address_zip");
            obj.phone = element.attr("tel");
            obj.fax = element.attr("fax");
            obj.url = element.attr("url");
            obj.email = element.attr("email");
            obj.founded = element.attr("founded");
        }
        return obj;
	}
	
	/**
	 * Parses the schedule information
	 */
	public static TeamInfoObject parseSchedule(Document doc, TeamInfoObject obj, String team1)
	{
		Elements links = doc.select("match");
		for(Element element : links)
		{
			StringBuilder game = new StringBuilder(100);
			game.append(element.attr("team_B_name") + " at " + element.attr("team_A_name"));
			game.append("////" + element.attr("date_utc"));
			if(HandleInput.isInteger(element.attr("fs_A")))
			{
				game.append("////");
				if(element.attr("team_A_name").equals(team1))
				{
					game.append(element.attr("fs_A") + " - " + element.attr("fs_B"));
				}
				else
				{
					game.append(element.attr("fs_B") + " - " + element.attr("fs_A"));
				}
			}
			obj.schedule.add(game.toString());
		}
		return obj;
	}
	
	/**
	 * Gets the record information
	 */
	public static TeamInfoObject parseXMLRecord(Document doc, TeamInfoObject obj, APIObject ao, String team)
	{
        Elements links = doc.select("ranking");
        if(links.size() <= 2)
        {
        	try {
				doc = APIInteraction.getXML(ao.formGetLastTeamUrl(), ao);
				links = doc.select("ranking");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        for (Element element : links) 
        {
        	if(element.attr("club_name").equals(team))
        	{
        		obj.record = element.attr("matches_won") + " - " + element.attr("matches_lost") + " - " + element.attr("matches_draw");
        		if(obj.record == null || obj.record.equals("---") || obj.record.equals(" - - "))
        		{
        			obj.record = "0-0-0";
        		}
        		obj.place = element.attr("rank");
        		if(obj.place == null || obj.equals(""))
        		{
        			obj.place = "N/A";
        		}
        		obj.group = element.parent().parent().attr("title");
        		if(obj.group == null || obj.group.equals(""))
        		{
        			obj.group = element.parent().parent().attr("name");
        		}
        		break;
        	}
        }
        return obj;
	}
	
	/**
	 * Gets the team info
	 * @author Jeff
	 *
	 */
	public class ParseTeamInfo extends AsyncTask<Object, Void, TeamInfoObject> 
	{
			APIObject obj;
			Context a;
			ProgressDialog pda;
			TeamInfoObject o;
			int indiv;
		    public ParseTeamInfo(APIObject object, Context cont, TeamInfoObject tio, int flag) 
		    {
		    	indiv = flag;
		        obj = object;
		        a = cont;
		        o = tio;
		        pda = new ProgressDialog(cont);
		        pda.setCancelable(false);
		        pda.setMessage("Please wait, fetching the information...");
		        pda.show();
		    }
		    
			@Override
			protected void onPreExecute(){ 
			   super.onPreExecute();  
			}
	
			@Override
			protected void onPostExecute(TeamInfoObject result){
			   super.onPostExecute(result);
			   pda.dismiss();
			   if(indiv == 1)
			   {
				   o.teamInfoFill(result, (Activity) a);
			   }
			   else if(indiv == 2)
			   {
				   GameInfoObject.getTeamInfo2(result, obj, a);
			   }
			   else
			   {
				   GameInfoObject.setDisplay(result, obj, a);
			   }
			}
			 
		    @Override
		    protected TeamInfoObject doInBackground(Object... data) 
		    {
		    	APIObject obj = (APIObject)data[0];
		    	TeamInfoObject tio = (TeamInfoObject)data[1];
		    	String team1 = (String)data[2];
		    	try {
					Document doc = APIInteraction.getXML(obj.formGetTeamInfoUrl(obj.teamIDMap.get(team1)), obj);
					o = parseXML(doc, tio);
					Document doc2 = APIInteraction.getXML(obj.formGetTeamUrl(), obj);
					o = parseXMLRecord(doc2, tio, obj, team1);
					if(indiv == 1)
					{
						Document doc3 = APIInteraction.getXML(obj.formGetMatchUrl(), obj);
						o = parseSchedule(doc3, tio, team1);
					}
					if(indiv == 3)
					{
						Document docRefs = APIInteraction.getXML(obj.formGetRefsUrl(obj.matchID), obj);
						Elements elem = docRefs.select("person");
						StringBuilder refs = new StringBuilder(10000);
						for(Element iter : elem)
						{
							refs.append(iter.attr("name") + ", ");
						}
						if(refs.toString().length() > 2)
						{
							o.referees = refs.toString().substring(0, refs.toString().length()-2);
						}
						Document docWinner;
						try{
							docWinner = APIInteraction.getXML(obj.formGetMatchInfoDoneUrl(obj.matchID), obj);
						} catch(HttpStatusException e)
						{
							docWinner = APIInteraction.getXML(obj.formGetMatchInfoDoneUrlSoccer(obj.matchID), obj);
						}
						Elements links = docWinner.select("match");
						for(Element iter : links)
						{
							if(iter.attr("status").equals("Played"))
							{
								o.isPlayed = true;
								if(iter.attr("winner").equals("team_A"))
								{
									o.winner = "Winner: " + iter.attr("team_a_name");
									o.score = iter.attr("fs_A") + " - " + iter.attr("fs_B");
								}
								else
								{
									o.winner = "Winner: " + iter.attr("team_b_name");
									o.score = iter.attr("fs_B") + " - " + iter.attr("fs_A");
								}
							}
						}
					}
					return o;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return null;
		    }
	  }
}

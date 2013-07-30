package com.example.statpump.ClassFiles.LittleStorage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.statpump.R;
import com.example.statpump.ClassFiles.APIInteraction;
import com.example.statpump.ClassFiles.APIObject;
import com.example.statpump.ClassFiles.APIInteraction.ParseTeamID;

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
	public APIObject obj;
	
	public TeamInfoObject(APIObject o, Context cont)
	{
		spawnAsync(o, cont);
	}
	
	/**
	 * Spawns the async task to get team info
	 * @param ao
	 * @param cont
	 */
	public void spawnAsync(APIObject ao, Context cont)
	{
		obj = ao;
		ParseTeamInfo task = this.new ParseTeamInfo(obj, cont, this);
		task.execute(obj, this);
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
		String addr = "";
		if(this.address != null && this.address.length() > 2)
		{
			addr = this.address + "\n";
		}
		if(this.city != null && this.city.length() > 2)
		{
			addr += this.city;
		}
		if(this.zip == null || this.zip.length() < 2)
		{
			addr += "\n";
		}
		else
		{
			addr += ", " + this.zip + "\n";
		}
		if(this.country != null && this.country.length() > 2)
		{
			addr += this.country;
		}
		address.setText(addr);
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
	 * Gets the record information
	 */
	public static TeamInfoObject parseXMLRecord(Document doc, TeamInfoObject obj, APIObject ao)
	{
        Elements links = doc.select("ranking");
        for (Element element : links) 
        {
        	if(element.attr("club_name").equals(ao.team1))
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
		    public ParseTeamInfo(APIObject object, Context cont, TeamInfoObject tio) 
		    {
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
			   o.teamInfoFill(result, (Activity) a);
			}
			 
		    @Override
		    protected TeamInfoObject doInBackground(Object... data) 
		    {
		    	APIObject obj = (APIObject)data[0];
		    	TeamInfoObject tio = (TeamInfoObject)data[1];
		    	try {
					Document doc = APIInteraction.getXML(obj.formGetTeamInfoUrl(), obj);
					o = parseXML(doc, tio);
					Document doc2 = APIInteraction.getXML(obj.formGetTeamUrl(), obj);
					o = parseXMLRecord(doc2, tio, obj);
					return o;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return null;
		    }
	  }
}

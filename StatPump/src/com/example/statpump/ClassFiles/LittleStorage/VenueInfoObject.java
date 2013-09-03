package com.example.statpump.ClassFiles.LittleStorage;

import java.io.IOException;
import java.text.DecimalFormat;

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

/**
 * Gets the information for the venue and sets it
 * @author Jeff
 *
 */
public class VenueInfoObject 
{
	public String name;
	public String founded;
	public String homeTeam;
	public String capacity;
	public String address;
	public String fieldType;
	public String architects;
	public String url;
	public String coordinates;
	public String prevNames;
	/**
	 * Calls the function that spawns the asynctask
	 * @param obj
	 * @param cont
	 */
	public VenueInfoObject(APIObject obj, Context cont, boolean indivFlag) 
	{
		spawnVenueAsync(obj, cont, indivFlag);
	}

	/**
	 * Spawns the asynctask that gets the venue info
	 * @param obj
	 * @param cont
	 * @param indivFlag 
	 */
	public void spawnVenueAsync(APIObject obj, Context cont, boolean indivFlag) 
	{
		ParseVenueInfo task = this.new ParseVenueInfo(obj, cont, this, indivFlag);
		task.execute(obj, this);
	}
	
	/**
	 * Gets the venue information from the document
	 * @param obj
	 * @param doc
	 * @return
	 */
	public VenueInfoObject parseXML(VenueInfoObject obj, Document doc)
	{
		Elements links = doc.select("team");
		for(Element elem : links)
		{
			System.out.println(elem.html());
			obj.homeTeam = elem.attr("club_name");
			Element p = elem.parent();
			obj.prevNames = p.attr("previous_names");
			obj.name = p.attr("name");
			obj.founded = p.attr("opened");
			obj.capacity = p.attr("capacity");
			obj.address = p.attr("address") + "\n" + p.attr("city") + "\n" + p.attr("area_name");
			obj.url = p.attr("url");
			obj.architects = p.attr("architect");
			obj.fieldType = p.attr("surface");
			String latStr = p.attr("maps_geocode_latitude");
			String longStr = p.attr("maps_geocode_longitude");
			if(!latStr.equals("") && !longStr.equals(""))
			{
				DecimalFormat df = new DecimalFormat("#.####");
				obj.coordinates = "Latitude: " + df.format(Double.parseDouble(latStr)) + ", Longitude: " + df.format(Double.parseDouble(longStr));
			}
			break;
		}
		return obj;
	}
	
	/**
	 * Gets the venue information fetching going
	 * @author Jeff
	 *
	 */
	public class ParseVenueInfo extends AsyncTask<Object, Void, VenueInfoObject> 
	{
			APIObject obj;
			Context a;
			ProgressDialog pda;
			VenueInfoObject o;
			boolean flag;
		    public ParseVenueInfo(APIObject object, Context cont, VenueInfoObject venueInfoObject, boolean indivFlag) 
		    {
		    	flag = indivFlag;
		        obj = object;
		        a = cont;
		        o = venueInfoObject;
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
			protected void onPostExecute(VenueInfoObject result){
			   super.onPostExecute(result);
			   pda.dismiss();
			   if(flag)
			   {
				   setVenueInfo(result, obj, (Activity) a);
			   }
			   else
			   {
				   GameInfoObject.getTeamInfo1(result, obj, a);
			   }
			}
			 
		    @Override
		    protected VenueInfoObject doInBackground(Object... data) 
		    {
		    	APIObject obj = (APIObject)data[0];
		    	VenueInfoObject tio = (VenueInfoObject)data[1];
		    	try {
					Document venueDoc = APIInteraction.getXML(obj.formGetVenueUrl(), obj);
					tio = parseXML(tio, venueDoc);
					return tio;
				}	catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
		    }
	  }

	public void setVenueInfo(VenueInfoObject obj, APIObject ao, final Activity cont)
	{
		LinearLayout layout = (LinearLayout)cont.findViewById(R.id.statwell);
		View res = ((Activity) cont).getLayoutInflater().inflate(R.layout.sw_venue_info, layout, false);
		TextView header = (TextView)res.findViewById(R.id.sw_venue_info_header);
		header.setText(obj.name);
		TextView prevNames = (TextView)res.findViewById(R.id.sw_venue_previousnames);
		if(obj.prevNames != null && obj.prevNames.length() > 3)
		{
			prevNames.setText("Previous Names: " + obj.prevNames);
		}
		else
		{
			prevNames.setVisibility(View.GONE);
		}
		TextView founded = (TextView)res.findViewById(R.id.sw_venue_info_founded);
		if(obj.founded != null && obj.founded.length() > 3)
		{
			founded.setText("Opened: " + obj.founded);
		}
		else
		{
			founded.setVisibility(View.GONE);
		}
		TextView homeTeam = (TextView)res.findViewById(R.id.sw_venue_info_hometeam);
		if(obj.homeTeam != null && obj.homeTeam.length() > 3)
		{
			homeTeam.setText("Home of " + obj.homeTeam);
		}
		else
		{
			homeTeam.setVisibility(View.GONE);
		}
		TextView cap = (TextView)res.findViewById(R.id.sw_venue_info_capacity);
		if(obj.capacity != null && obj.capacity.length() > 2)
		{
			cap.setText("Holds " + obj.capacity + " people");
		}
		else
		{
			cap.setVisibility(View.GONE);
		}
		TextView fieldType = (TextView)res.findViewById(R.id.sw_venue_fieldtype);
		if(obj.fieldType != null && obj.fieldType.length() > 2)
		{
			fieldType.setText("Field Type is " + obj.fieldType);
		}
		else
		{
			fieldType.setVisibility(View.GONE);
		}
		final TextView url = (TextView)res.findViewById(R.id.sw_venue_url);
		if(obj.url != null && obj.url.length() > 2)
		{
			url.setText(obj.url);
		}
		else
		{
			url.setVisibility(View.GONE);
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
		TextView address = (TextView)res.findViewById(R.id.sw_venue_info_address);
		if(obj.address != null && obj.address.length() > 3)
		{
			address.setText(obj.address);
		}
		else
		{
			address.setVisibility(View.GONE);
		}
		TextView architects = (TextView)res.findViewById(R.id.sw_venue_architects);
		if(obj.architects != null && obj.architects.length() > 3)
		{
			architects.setText("Designed by " + obj.architects);
		}
		else
		{
			architects.setVisibility(View.GONE);
		}
		TextView coords = (TextView)res.findViewById(R.id.sw_venue_coordinates);
		if(obj.coordinates != null && obj.coordinates.length() > 3)
		{
			coords.setText(obj.coordinates);
		}
		else
		{
			coords.setVisibility(View.GONE);
		}
		layout.addView(res);
	}
}


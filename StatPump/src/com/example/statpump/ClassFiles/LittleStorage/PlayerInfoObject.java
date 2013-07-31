package com.example.statpump.ClassFiles.LittleStorage;

import java.io.IOException;
import java.text.DecimalFormat;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.statpump.R;
import com.example.statpump.ClassFiles.APIInteraction;
import com.example.statpump.ClassFiles.APIObject;
import com.example.statpump.ClassFiles.LittleStorage.PlayerSearchObject.ParseSquads;
import com.example.statpump.InterfaceAugmentation.StatWellUsage;

public class PlayerInfoObject 
{
	public String name;
	public String team;
	public String pos;
	public String number;
	public int playerID;
	public String hometown;
	public String gender;
	public String height;
	public String weight;
	public APIObject ao;
	public PlayerSearchObject pso;
	
	/**
	 * Constructor
	 */
	public PlayerInfoObject()
	{
		
	}

	/**
	 * Spawns the asynctask to get more info
	 * @param obj
	 * @param cont
	 * @param po
	 */
	public void spawnMoreInfo(APIObject obj, Context cont, PlayerSearchObject po, boolean flag) {
		ao = obj;
		pso = po;
		ParsePlayerInfo task = this.new ParsePlayerInfo(obj, cont, this, flag);
		task.execute(obj, this);
	}
	
	/**
	 * Gets the more info object by parsing the given doc
	 * @param obj
	 * @param doc
	 * @return
	 */
	public PlayerInfoObject parseXML(APIObject obj, Document doc)
	{
		Elements links = doc.select("person");
		for(Element element : links)
		{
			int iterID = Integer.parseInt(element.attr("person_id"));
			if(iterID == this.playerID)
			{
				this.gender = element.attr("gender");
				this.hometown = "Hometown:\n" + element.attr("place_of_birth") + "\n" + element.attr("country_of_birth");
				double heightCM = Double.parseDouble(element.attr("height"));
				this.height = heightCM + " centimeters\n";
				int heightIN = (int) (heightCM * 0.393);
				int feet = (int) (heightIN/12.0);
				int inLeft = heightIN - (feet * 12);
				this.height += feet + " feet " + inLeft + " inches";
				int weight = Integer.parseInt(element.attr("weight"));
				this.weight = weight + " kilograms\n";
				DecimalFormat df = new DecimalFormat("#.##");
				double pounds = weight * 2.2;
				this.weight += df.format(pounds) + " pounds";
				break;
			}
		}
		return this;
	}
	
	/**
	 * Handles the more info xml 
	 * @author Jeff
	 *
	 */
	public class ParsePlayerInfo extends AsyncTask<Object, Void, PlayerInfoObject> 
	{
			APIObject obj;
			Context a;
			ProgressDialog pda;
			PlayerInfoObject o;
			boolean setContent;
		    public ParsePlayerInfo(APIObject object, Context cont, PlayerInfoObject tio, boolean flag) 
		    {
		    	setContent = flag;
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
			protected void onPostExecute(PlayerInfoObject result){
			   super.onPostExecute(result);
			   pda.dismiss();
			   if(setContent)
			   {
				   o.teamInfoFill(result, (Activity) a);
			   }
			   else
			   {
				   //CALL FUNCTION TO SPAWN PLAYER STATS HERE!
			   }
			}
			 
		    @Override
		    protected PlayerInfoObject doInBackground(Object... data) 
		    {
		    	APIObject obj = (APIObject)data[0];
		    	PlayerInfoObject tio = (PlayerInfoObject)data[1];
		    	try {
					Document doc = APIInteraction.getXML(obj.formGetSquadUrl(obj.teamIDMap.get(obj.team1)), obj);
					o=o.parseXML(obj, doc);
					return o;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return null;
		    }
	  }

	/**
	 * Fills out the info stuff
	 * @param result
	 * @param a
	 */
	public void teamInfoFill(PlayerInfoObject result, final Activity a) {
		final LinearLayout layout = (LinearLayout)a.findViewById(R.id.statwell);
		View res = ((Activity) a).getLayoutInflater().inflate(R.layout.sw_player_info, layout, false);
		TextView nameView = (TextView)res.findViewById(R.id.sw_playerinfo_name);
		nameView.setText(result.name);
		TextView number = (TextView)res.findViewById(R.id.sw_playerinfo_number);
		if(!result.number.contains("not listed"))
		{
			number.setText("#" + result.number);
		}
		else
		{
			number.setText(result.number);
		}
		TextView team = (TextView)res.findViewById(R.id.sw_playerinfo_team);
		team.setText(result.team);
		TextView pos = (TextView)res.findViewById(R.id.sw_playerinfo_pos);
		pos.setText(result.pos);
		TextView height = (TextView)res.findViewById(R.id.sw_playerinfo_height);
		height.setText(result.height);
		TextView weight = (TextView)res.findViewById(R.id.sw_playerinfo_weight);
		weight.setText(result.weight);
		TextView gender = (TextView)res.findViewById(R.id.sw_playerinfo_gender);
		gender.setText(result.gender);
		TextView homeTown = (TextView)res.findViewById(R.id.sw_playerinfo_hometown);
		homeTown.setText(result.hometown);
		layout.addView(res);
		ImageView back = (ImageView)res.findViewById(R.id.sw_back_arrow);
		back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layout.removeAllViews();
				StatWellUsage.playerInfo(ao, a, pso);
			}
		});
	}
}

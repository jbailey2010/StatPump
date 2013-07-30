package com.example.statpump.ClassFiles.LittleStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.statpump.R;
import com.example.statpump.ClassFiles.APIInteraction;
import com.example.statpump.ClassFiles.APIObject;
import com.example.statpump.ClassFiles.HandleInput;
import com.example.statpump.ClassFiles.APIInteraction.ParseOpponentDates;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TwoLineListItem;
/**
 * Gets the squads and handles the search
 * @author Jeff
 *
 */
public class PlayerSearchObject 
{
	public Map<String, Integer> players = new HashMap<String, Integer>();
	public int playerID;
	public String playerTeam;
	public String playerName;
	public int playerNumber;
	public String playerPos;
	
	/**
	 * Spawns the asynctask that gets the squads
	 * @param cont
	 * @param obj
	 */
	public PlayerSearchObject(Context cont, APIObject obj)
	{
		ParseSquads task = this.new ParseSquads(obj, cont, this);
		task.execute(obj, this);
	}
	
	/**
	 * Gets all the player data
	 * @param team1 
	 */
	public Map<String, Integer> parseXMLSquads(Document doc, Map<String, Integer> playerData, String team1)
	{
        Elements links = doc.select("statistics");
        for (Element element : links) 
        { 
        	String number = "Number not listed";
        	if(element.hasAttr("shirtno") && HandleInput.isInteger(element.attr("shirtno")))
        	{
        		number = element.attr("shirtno");
        	}
        	if(element.hasAttr("shirtnumber") && HandleInput.isInteger(element.attr("shirtnumber")))
        	{
        		number = element.attr("shirtnumber");
        	}
        	Element parent = element.parent();
        	int id = Integer.parseInt(parent.attr("person_id"));
        	String position = parent.attr("position");
        	if(position == null || position.length() < 3)
        	{
        		position = "Position not listed";
        	}
        	String name = "";
        	if(parent.hasAttr("firstname"))
        	{
        		name = parent.attr("firstname") + " " + parent.attr("lastname");
        	}
        	if(parent.hasAttr("first_name"))
        	{
        		name = parent.attr("first_name") + " " + parent.attr("last_name");        				
        	}
        	playerData.put(name + "//" + position + "//" + team1 + "//" + number, id);
        }
		if(playerData.size() == 0)
		{
			System.out.println("Size was zero.");
			Elements linksSet = doc.select("person");
			for(Element element : linksSet)
			{
				String number = "Number not listed";
				int id = Integer.parseInt(element.attr("person_id"));
	        	String position = element.attr("position");
	        	String name = "";
	        	if(element.hasAttr("firstname"))
	        	{
	        		name = element.attr("firstname") + " " + element.attr("lastname");
	        	}
	        	if(element.hasAttr("first_name"))
	        	{
	        		name = element.attr("first_name") + " " + element.attr("last_name");
	        				
	        	}
	        	playerData.put(name + "//" + position + "//" + team1 + "//" + number, id);
			}
		}
        return playerData;
	}
	
	/**
	 * Sets the squads once they're fetched
	 * @param squads
	 */
	public void setSquads(Map<String, Integer> squads)
	{
		this.players = null;
		this.players = squads;
	}
	
	/**
	 * Gets the squads if need be
	 * @author Jeff
	 *
	 */
	public class ParseSquads extends AsyncTask<Object, Void, Map<String, Integer>> 
	{
			APIObject obj;
			Context a;
			PlayerSearchObject po;
		    public ParseSquads(APIObject object, Context cont, PlayerSearchObject pso) 
		    {
		        obj = object;
		        a = cont;
		        po = pso;
		    }
		    
			@Override
			protected void onPreExecute(){ 
			   super.onPreExecute();  
			}
	
			@Override
			protected void onPostExecute(Map<String, Integer> result){
			   super.onPostExecute(result);
			   po.setSquads(result);
			}
			 
		    @Override
		    protected Map<String, Integer> doInBackground(Object... data) 
		    {
		    	APIObject obj = (APIObject)data[0];
		    	PlayerSearchObject pso = (PlayerSearchObject)data[1];
		    	try {
		    		Map<String, Integer> playerData = new HashMap<String, Integer>();
		    		Document doc = APIInteraction.getXML(obj.formGetSquadUrl(obj.team1ID), obj);
		    		playerData = parseXMLSquads(doc, playerData, obj.team1);
		    		System.out.println(playerData.size() + " players");
		    		if(obj.team2 != null && obj.team2.length() > 2)
		    		{
		    			System.out.println("Calling for 2 teams");
		    			doc = APIInteraction.getXML(obj.formGetSquadUrl(obj.team2ID), obj);
		    			playerData = parseXMLSquads(doc, playerData, obj.team2);
		    			System.out.println(playerData.size() + " players");
		    		}
		    		return playerData;
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return null;
		    }
	  }

	public void searchInit(APIObject obj, Context cont, final PlayerSearchObject o, final LinearLayout sw) 
	{
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.player_search);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
		dialog.show();
		Button cancel = (Button)dialog.findViewById(R.id.player_search_cancel);
		cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		final AutoCompleteTextView input = (AutoCompleteTextView)dialog.findViewById(R.id.player_search_input);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		
		for (String date : this.players.keySet()) {
			//playerData.put(name + "//" + position + "//" + number, id)
		    Map<String, String> datum = new HashMap<String, String>(2);
		    String[] set = date.split("//");
		    String sub = set[0];
		    if(!set[3].equals(null) && !set[3].equals("Number not listed"))
		    {
		    	sub += ", #" + set[3];
		    }
		    datum.put("title", sub);
		    datum.put("date", set[1] + " - " +  set[2]);
		    data.add(datum);
		}
		SimpleAdapter adapter = new SimpleAdapter(cont, data,
		                                          android.R.layout.simple_list_item_2,
		                                          new String[] {"title", "date"},
		                                          new int[] {android.R.id.text1,
		                                                     android.R.id.text2});
		input.setAdapter(adapter);
		input.setThreshold(1);
		input.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				input.setText("");
				String nameNum = ((TwoLineListItem)arg1).getText1().getText().toString();
				String[]nameNumSet = nameNum.split(", ");
				String key = nameNumSet[0] + "//";
				String positionTeam = ((TwoLineListItem)arg1).getText2().getText().toString();
				String[]ptSet = positionTeam.split(" - ");
				key += ptSet[0] + "//" + ptSet[1] + "//" + nameNumSet[1];
				o.playerID = o.players.get(key);
				o.playerName = nameNumSet[0];
				o.playerNumber = Integer.parseInt(nameNumSet[1]);
				o.playerTeam = ptSet[1];
				o.playerPos = ptSet[0];
				sw.removeAllViews();
				//CALL POPULATE STATWELL HERE WITH THE DATA
				dialog.dismiss();
			}
		});
	}
}

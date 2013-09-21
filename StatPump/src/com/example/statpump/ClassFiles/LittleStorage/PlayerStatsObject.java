package com.example.statpump.ClassFiles.LittleStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.statpump.statpump.R;
import com.example.statpump.ClassFiles.APIInteraction;
import com.example.statpump.ClassFiles.APIObject;
import com.example.statpump.ClassFiles.LittleStorage.PlayerInfoObject.ParsePlayerInfo;
import com.example.statpump.InterfaceAugmentation.StatWellUsage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * Handles parsing of the player's stats
 * @author Jeff
 *
 */
public class PlayerStatsObject 
{
	public APIObject ao;
	public PlayerSearchObject p;
	public List<String> stats = new ArrayList<String>();
	/**
	 * Spawns the parsing async task
	 */
	public void spawnAsync(int playerID, Context cont, APIObject obj, PlayerSearchObject po, boolean flag, String name, String team, String pos, String num)
	{
		ao = obj;
		p = po;
		ParsePlayerStats task = this.new ParsePlayerStats(obj, cont, this, flag, name, team, pos, num, po);
		task.execute(obj, this, playerID);
	} 
	
	public List<String> parseXML(Document doc, int playerID)
	{
		List<String> stats = new ArrayList<String>();
		Elements links = doc.select("person");
		for(Element elem : links)
		{
			if(Integer.parseInt(elem.attr("person_id")) == playerID)
			{
				String parent = elem.parent().tagName();
				if(!ao.ignore.contains(ao.sportURL + "/" + parent))
				{
					String val = elem.attr("value");
					if(val.contains(".00"))
					{
						val = val.replace(".00", "");
					}
					if(ao.fixes.containsKey(ao.sportURL + "/" + parent))
					{
						stats.add(val + " " + ao.fixes.get(ao.sportURL + "/" + parent));
					}
					else
					{
						System.out.println(ao.sportURL);
						System.out.println("DID NOT FIND " + parent);
					}
				}
			}
		}
		if(stats.size() == 0)
		{
			stats.add("No stats available for this player. Either this season is upcoming or they have no stats listed");
		}
		return stats;
	}
	
	public static String capitalize(String str, char[] delimiters) {
        int delimLen = (delimiters == null ? -1 : delimiters.length);
        if (str == null || str.length() == 0 || delimLen == 0) {
            return str;
        }
        int strLen = str.length();
        StringBuffer buffer = new StringBuffer(strLen);
        boolean capitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);

            if (isDelimiter(ch, delimiters)) {
                buffer.append(ch);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer.append(Character.toTitleCase(ch));
                capitalizeNext = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }
	
	/**
     * Is the character a delimiter.
     *
     * @param ch  the character to check
     * @param delimiters  the delimiters
     * @return true if it is a delimiter
     */
    private static boolean isDelimiter(char ch, char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        }
        for (int i = 0, isize = delimiters.length; i < isize; i++) {
            if (ch == delimiters[i]) {
                return true;
            }
        }
        return false;
    }
	
	public class ParsePlayerStats extends AsyncTask<Object, Void, PlayerStatsObject> 
	{
			APIObject obj;
			Context a;
			ProgressDialog pda;
			PlayerStatsObject o;
			boolean setContent;
			String n;
			String t;
			String p;
			String number;
			PlayerSearchObject psObj;
		    public ParsePlayerStats(APIObject object, Context cont, PlayerStatsObject tio, boolean flag, String name, String team, String pos, String num, 
		    		PlayerSearchObject pso) 
		    {
		        obj = object;
		        a = cont;
		        o = tio;
		        n = name;
		        t = team;
		        p = pos;
		        number = num;
		        setContent = flag;
		        pda = new ProgressDialog(cont);
		        pda.setCancelable(false);
		        pda.setMessage("Please wait, fetching the information...");
		        pda.show();
		        psObj = pso;
		    }
		    
			@Override
			protected void onPreExecute(){ 
			   super.onPreExecute();  
			}
	
			@Override
			protected void onPostExecute(PlayerStatsObject result){
			   super.onPostExecute(result);
			   pda.dismiss();
			   if(setContent)
			   {
				   o.teamInfoFill(result, (Activity) a, n, t, p, number);
			   }
			   else
			   {
				   psObj.finishSearch(a, result, obj);
			   }
			}
			 
		    @Override
		    protected PlayerStatsObject doInBackground(Object... data) 
		    {
		    	APIObject obj = (APIObject)data[0];
		    	PlayerStatsObject tio = (PlayerStatsObject)data[1];
		    	Integer playerID = (Integer)data[2];
		    	try {
					Document doc = APIInteraction.getXML(obj.formGetTeamStatsUrl(obj.teamIDMap.get(obj.team1)), obj);
					List<String> stats = o.parseXML(doc, playerID);
					o.stats = stats;
					return o;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return null;
		    }
	  }

	public void teamInfoFill(PlayerStatsObject result, final Activity a, String n,
			String t, String p2, String number) {
		final LinearLayout layout = (LinearLayout)a.findViewById(R.id.statwell);
		View res = ((Activity) a).getLayoutInflater().inflate(R.layout.sw_player_stats, layout, false);
		TextView name = (TextView)res.findViewById(R.id.sw_playerstats_name);
		name.setText(n);
		TextView position = (TextView)res.findViewById(R.id.sw_playerstats_pos);
		position.setText(p2);
		TextView team = (TextView)res.findViewById(R.id.sw_playerstats_team);
		team.setText(t);
		String num = "";
		if(number.contains("not"))
		{
			num = number;
		}
		else
		{
			num = "#" + number;
		}
		TextView numberView = (TextView)res.findViewById(R.id.sw_playerstats_number);
		numberView.setText(num);
		TextView stats = (TextView)res.findViewById(R.id.sw_playerstats_statslist);
		StringBuilder statsList = new StringBuilder(1000);
		for(String stat : this.stats)
		{
			String[] statSet = stat.split(" ");
			StringBuilder statBuilder = new StringBuilder(100);
			for(int i = 1; i < statSet.length; i++)
			{
				statBuilder.append(statSet[i] + " ");
			}
			String statStr = statBuilder.toString();
			statStr = statStr.substring(0, statStr.length() - 1);
			statStr = statStr + ": ";
			statStr = statStr + statSet[0];
			statsList.append(statStr + "\n");
		}
		stats.setText(statsList.toString());
		ImageView back = (ImageView)res.findViewById(R.id.sw_back_arrow);
		back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layout.removeAllViews();
				StatWellUsage.playerStats(ao, a, p);
			}
		});
		layout.addView(res);
	}
}

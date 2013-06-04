package com.example.statpump.ClassFiles;

import com.example.statpump.R;
import com.example.statpump.Pages.Home;
import com.example.statpump.Pages.HomeTeam;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

/**
 * A library of functions to handle user input
 * @author Jeff
 *
 */ 
public class HandleInput 
{
	/**
	 * Pops up the dialog to allow the user to decide if they want a lookup relative to a game
	 * or a team
	 * @param cont
	 */
	public static void chooseTeamOrGame(final Context cont)
	{
		final Dialog dialog = new Dialog(cont);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.search_decider);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
		dialog.show();
		Button game = (Button)dialog.findViewById(R.id.game_search);
		Button team = (Button)dialog.findViewById(R.id.team_search);
		game.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(cont, Home.class);
		        cont.startActivity(intent);					
			}
		});
		team.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(cont, HomeTeam.class);
		        cont.startActivity(intent);					
			}
		});
	}
}

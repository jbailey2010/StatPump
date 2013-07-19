package com.example.statpump.InterfaceAugmentation;

import org.apache.http.client.protocol.ClientContext;

import android.content.Context;

import com.example.statpump.ClassFiles.APIObject;
import com.example.statpump.ClassFiles.LittleStorage.TeamInfoObject;
/**
 * Handles the usage of the statwell
 * @author Jeff
 *
 */
public class StatWellUsage 
{
	/**
	 * Distributes work based on statwell selection
	 * @param obj
	 * @param cont
	 */
	public static void statWellInit(APIObject obj, Context cont)
	{
		/*
		 * Player Information
		 * Team Information
		 * Team Statistics
		 * ----------------
		 * Game Information
		 * Game Statistics
		 * Venue Information
		 */
		if(obj.statwellSetting.equals("Team Information"))
		{
			teamInfo(obj, cont);
		}
	}
	
	public static void teamInfo(APIObject obj, Context cont)
	{
		TeamInfoObject tio = new TeamInfoObject(obj, cont);
	}
}

package com.example.statpump.FileIO;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * A library to handle the various readings from file
 * @author Jeff
 *
 */
public class ReadFromFile 
{
	/**
	 * Reads the use id from file
	 */
	public static long readUseID(Context cont)
	{
		SharedPreferences prefs = cont.getSharedPreferences("StatPump", 0); 
		return prefs.getLong("Use ID", -1);
	}
	
	/**
	 * Reads the token from file
	 */
	public static String readToken(Context cont)
	{
		SharedPreferences prefs = cont.getSharedPreferences("StatPump", 0); 
		return prefs.getString("Token", "Not set");
	}
	
	/**
	 * Reads the token secret from file
	 */
	public static String readTokenSecret(Context cont)
	{
		SharedPreferences prefs = cont.getSharedPreferences("StatPump", 0); 
		return prefs.getString("Token Secret", "Not set");
	}
}

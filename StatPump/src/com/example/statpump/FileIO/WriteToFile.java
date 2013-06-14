package com.example.statpump.FileIO;

import java.util.List;

import twitter4j.auth.AccessToken;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * A library of functions that write various things to file
 * @author Jeff
 */
public class WriteToFile 
{
	/**
	 * Writes the use ID to file
	 */
	public static void storeID(long l, Context cont)
	{
		SharedPreferences.Editor editor = cont.getSharedPreferences("StatPump", 0).edit();
		editor.putLong("Use ID", l);
		editor.commit();
	}
	
	/**
	 * Writes the token data to file to be read later
	 */
	public static void storeToken(AccessToken token, Context cont)
	{
		SharedPreferences.Editor editor = cont.getSharedPreferences("StatPump", 0).edit();
		editor.putString("Token", token.getToken());
		editor.putString("Token Secret", token.getTokenSecret());
		editor.commit();
	}
	
	/**
	 * Writes the facebook token data to file to be read later
	 */
	public static void storeFacebookToken(String token, Context cont)
	{
		SharedPreferences.Editor editor = cont.getSharedPreferences("StatPump", 0).edit();
		editor.putString("Facebook Token", token);
		editor.commit();
	}
}

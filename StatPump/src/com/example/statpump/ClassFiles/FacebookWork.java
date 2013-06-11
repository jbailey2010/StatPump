package com.example.statpump.ClassFiles;

import java.net.URL;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FacebookWork 
{
	public static Facebook facebook;
	
	public static void setUpFacebook()
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthAppId("PUT APP ID HERE")
		  .setOAuthAppSecret("PUT APP SECRET HERE")
		  .setOAuthPermissions("PUT PERMISSION CSV HERE");
		FacebookFactory ff = new FacebookFactory(cb.build());
		facebook = ff.getInstance();
	}
}

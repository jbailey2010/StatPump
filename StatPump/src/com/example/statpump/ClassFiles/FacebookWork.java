package com.example.statpump.ClassFiles;

import java.net.MalformedURLException;
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
	public static Context cont;
	
	 private enum RequestCode {
	        OAuth(1),
	        Detail(2),
	        ;
	        
	        private int code;
	        private RequestCode(int code) {
	            this.code = code;
	        }
	        public static RequestCode getInstance(int code) {
	            for (RequestCode e : RequestCode.values()) {
	                if (e.code == code) {
	                    return e;
	                }
	            }
	            return null;
	        }
	        
	    }
	 
	public static void startFacebook(Context context)
	{
		System.out.println("In start");
		cont = context;
		Intent intent = new Intent(cont, OAuthActivity.class);
        ((Activity)cont).startActivityForResult(intent, RequestCode.OAuth.code);
	}
}

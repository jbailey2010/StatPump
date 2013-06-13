/*
 * Copyright 2012 Ryuji Yamashita
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.statpump.ClassFiles;

import java.net.URL;
import java.util.concurrent.CountDownLatch;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;

/**
 * @author Ryuji Yamashita - roundrop at gmail.com
 */
@TargetApi(3)
public class OAuthTask extends AsyncTask<Object, Void, OAuthWebView> {

    private OAuthWebView mOAuthWebView;
    private URL mCallbackURL;
    private String mCode;
    private CountDownLatch mLatch = new CountDownLatch(1);

    @Override
    protected OAuthWebView doInBackground(Object... params) {
        mOAuthWebView = (OAuthWebView) params[0];
        mCallbackURL = (URL) params[1];

        mOAuthWebView.setWebViewClient(new InternalWebViewClient());
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthAppId("134586500079124")
          .setOAuthAppSecret("41c5573b70df438816a7c81d49fa3e75")
          .setOAuthPermissions("publish_stream");
        System.out.println("In asynctask connecting");
        FacebookFactory ff = new FacebookFactory(cb.build());
        Facebook facebook = ff.getInstance();
        mOAuthWebView.setFacebook(facebook);
        publishProgress();
        waitForAuthorization();
        if (mCode == null) {
            System.out.println("oauth code is null!!!!!!!!");
            return mOAuthWebView;
        }
        AccessToken accessToken = getAccessToken();
        if (accessToken == null) {
            System.out.println("Access Token is null!!!!!!!!");
            return mOAuthWebView;
        }
        try {
			mOAuthWebView.getFacebook().postStatusMessage("Test");
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return mOAuthWebView;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        Facebook facebook = mOAuthWebView.getFacebook();
        String url = facebook.getOAuthAuthorizationURL(mCallbackURL.toString());
        mOAuthWebView.loadUrl(url);
    }

 
    @Override
    protected void onPostExecute(OAuthWebView result) {
        mOAuthWebView.end();
        System.out.println("Executed: " + mOAuthWebView.getFacebook().getOAuthAccessToken().getToken());
        AccessToken access = mOAuthWebView.getFacebook().getOAuthAccessToken();
    }

    private void waitForAuthorization() {
        try {
            mLatch.await();
        } catch (InterruptedException e) {}
    }

    private AccessToken getAccessToken() {
        try {
            Facebook facebook = mOAuthWebView.getFacebook();
            return facebook.getOAuthAccessToken(mCode);
        } catch (FacebookException e) {
            e.printStackTrace();
            return null;
        }
    }



    private class InternalWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!url.startsWith(mCallbackURL.toString())) {
                return false;
            }
            Uri uri = Uri.parse(url);
            mCode = uri.getQueryParameter("code");
            mLatch.countDown();
            return true;
        }
        
    }

}

package com.example.statpump.ClassFiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import facebook4j.Facebook;

/**
 * @author Ryuji Yamashita - roundrop at gmail.com
 */
public class OAuthActivity extends Activity implements OAuthWebView.Callback {
    
    public static final String DATA_KEY_FACEBOOK = "facebook";

    private OAuthWebView mOAuthWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	System.out.println("In oauthactivity");
        super.onCreate(savedInstanceState);
        mOAuthWebView = new OAuthWebView(this);
        setContentView(mOAuthWebView);
        mOAuthWebView.start(this);
    } 

    @Override
    public void onSuccess(Facebook facebook) {
        Intent data = new Intent();
        data.putExtra(DATA_KEY_FACEBOOK, facebook);
        setResult(RESULT_OK, data);
        finish();
    }

}

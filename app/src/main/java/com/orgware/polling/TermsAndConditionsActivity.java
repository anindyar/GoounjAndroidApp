package com.orgware.polling;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by nandagopal on 20/1/16.
 */
public class TermsAndConditionsActivity extends BaseActivity {

    Toolbar mToolbar;
    WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_webview_terms);
        mWebview = (WebView) findViewById(R.id.webview);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.home_bg));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Terms And Use");

        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setAppCacheEnabled(true);
        mWebview.getSettings().setBuiltInZoomControls(true);
        mWebview.getSettings().setGeolocationEnabled(true);
        mWebview.getSettings().setSupportMultipleWindows(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebview.loadUrl("http://goounj.com/?page_id=1193");


    }
}

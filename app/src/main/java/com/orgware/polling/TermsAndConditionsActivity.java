package com.orgware.polling;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by nandagopal on 20/1/16.
 */
public class TermsAndConditionsActivity extends BaseActivity {

    Toolbar mToolbar;
    WebView mWebview;
    ProgressDialog mPrgress;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_webview_terms);
        mPrgress = new ProgressDialog(this);
        mPrgress.setCancelable(false);
        mPrgress.setMessage("Loading...");
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

        mWebview.setWebViewClient(new WebViewClient()

        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // progressDialog.setMessage("Loading...");
                // progressDialog.setCancelable(false);
                // progressDialog.show();
                mPrgress.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // progressDialog.dismiss();
                mPrgress.dismiss();
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

        });

    }
}

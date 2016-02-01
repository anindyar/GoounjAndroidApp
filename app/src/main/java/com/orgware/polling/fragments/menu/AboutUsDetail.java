package com.orgware.polling.fragments.menu;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orgware.polling.MenuDetailActivity;
import com.orgware.polling.R;
import com.orgware.polling.fragments.BaseFragment;

/**
 * Created by nandagopal on 21/1/16.
 */
public class AboutUsDetail extends BaseFragment {

    Toolbar mToolbar;
    WebView mWebview;
    ProgressDialog mPrgress;

    @Override
    public void setTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_aboutus, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebview = (WebView) view.findViewById(R.id.webview);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MenuDetailActivity) act).setTitle("About Us");
//        mPrgress = new ProgressDialog(act);
//        mPrgress.setCancelable(false);
//        mPrgress.setMessage("Loading...");
//        mWebview.getSettings().setJavaScriptEnabled(true);
//        mWebview.getSettings().setAppCacheEnabled(true);
//        mWebview.getSettings().setBuiltInZoomControls(true);
//        mWebview.getSettings().setGeolocationEnabled(true);
//        mWebview.getSettings().setSupportMultipleWindows(true);
//        mWebview.getSettings().setUseWideViewPort(true);
//        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        mWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
//        mWebview.loadUrl("http://goounj.com");
//        loadWebView(mWebview);
    }

    private void loadWebView(WebView mWebview) {
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

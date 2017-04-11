package com.lions.torque.caring.servicecar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lions.torque.caring.R;

public class Webview_Activity extends AppCompatActivity {


    WebView webView;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_);
        webView = (WebView)findViewById(R.id.webview);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

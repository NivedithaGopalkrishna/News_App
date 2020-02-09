package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewActivity extends AppCompatActivity {
private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webview =  (WebView)findViewById(R.id.webview);
        Intent newsurl = getIntent();
        String url = (String)newsurl.getExtras().getSerializable("selectednews");
        Log.d("url in webactivity",url.toString());
        //setTitle(source.name);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(url);
    }

    public void onBackPressed()
    {
        if(webview.canGoBack()){
            webview.goBack();
        }else {
            super.onBackPressed();
        }
    }
}

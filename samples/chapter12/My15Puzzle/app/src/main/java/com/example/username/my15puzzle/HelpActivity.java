package com.example.username.my15puzzle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl("file:///android_asset/help/index.html");

// 下記はインターネットにアクセスする場合。※忘れずにパーミッション"android.permission.INTERNET"をつけてから実行すること
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("http://www.google.com");

    }
}

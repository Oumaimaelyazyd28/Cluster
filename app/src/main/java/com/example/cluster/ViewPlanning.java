package com.example.cluster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLEncoder;

public class ViewPlanning extends AppCompatActivity {

    WebView planView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_planning);

        planView = (WebView) findViewById(R.id.viewPlan);
        planView.getSettings().setJavaScriptEnabled(true);
        String name = getIntent().getStringExtra("name");
        String url = getIntent().getStringExtra("url");

        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle(name);
        pd.setMessage("Opening...!");

        planView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }
        });

        String url1 = "";
        try {
            url = URLEncoder.encode(url,"UTF-8");
        } catch (Exception ex){

        }

        planView.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);
    }

}
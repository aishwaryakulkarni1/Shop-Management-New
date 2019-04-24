package com.inevitablesol.www.shopmanagement.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.inevitablesol.www.shopmanagement.R;

public class Delete_Document extends AppCompatActivity {

    private String link;
    private WebView webView;
    private static final String TAG = "Delete_Document";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete__document);
        webView=(WebView)findViewById(R.id.webImage);
        webView.getSettings().setJavaScriptEnabled(true);

        Intent intent=getIntent();
        if(intent.hasExtra("id"))
        {
            link=intent.getStringExtra("link");

            String url = "http://docs.google.com/gview?embedded=true&url=" + link;
            Log.i(TAG, "Opening PDF: " + url);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
          //  webView.loadUrl(link);
        }
    }
}

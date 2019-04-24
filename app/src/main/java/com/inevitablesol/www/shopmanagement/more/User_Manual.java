package com.inevitablesol.www.shopmanagement.more;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.inevitablesol.www.shopmanagement.R;

public class User_Manual extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__manual);
//        WebView myWebView = (WebView) findViewById(R.id.webview);
//        myWebView.loadUrl("https://drive.google.com/file/d/1IcXD_Rp6LPBlyT-VUjEz0MC-ZoBlEV9P/view");
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1IcXD_Rp6LPBlyT-VUjEz0MC-ZoBlEV9P/view")));
    }
}

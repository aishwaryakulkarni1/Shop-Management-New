package com.inevitablesol.www.shopmanagement.more;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.inevitablesol.www.shopmanagement.R;

public class User_Manual_Q_A extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_q_a);
//        WebView myWebView = (WebView) findViewById(R.id.webview);
//        myWebView.loadUrl("http://www.mechatrontechgear.com/faq.html");

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mechatrontechgear.com/faq.html")));
    }
}

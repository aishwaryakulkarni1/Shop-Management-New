package com.inevitablesol.www.shopmanagement.more;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.webkit.WebView;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

public class More_AboutUS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more__about_us);

        WebView aboutText=(WebView)findViewById(R.id.about_text);
          aboutText.setBackgroundColor(Color.BLACK);

       // Html.fromHtml("<h1>Your App Name, Version </h1>" + getString(R.string.about_text));
        //aboutText.setText(Html.fromHtml("<h1>Your App Name, Version </h1>" + getString(R.string.about_text)));
       // aboutText.setText(fromHtml(getString(R.string.about_text)))
        // ;
        String data =getString(R.string.about_text);
        aboutText.getSettings().setJavaScriptEnabled(true);
        aboutText.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
    }


    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}

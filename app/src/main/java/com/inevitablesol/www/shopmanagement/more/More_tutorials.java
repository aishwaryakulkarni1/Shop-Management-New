package com.inevitablesol.www.shopmanagement.more;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.inevitablesol.www.shopmanagement.R;

public class More_tutorials extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_tutorials);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCW71mA5Q4Of3beNS2A6daAA/videos")));
    }
}

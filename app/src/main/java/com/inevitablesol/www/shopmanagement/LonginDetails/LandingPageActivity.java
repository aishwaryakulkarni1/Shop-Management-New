package com.inevitablesol.www.shopmanagement.LonginDetails;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.inevitablesol.www.shopmanagement.R;

public class LandingPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                startActivity(new Intent(LandingPageActivity.this,SigninActivity.class));

            }
        }, 3000);

        getSupportActionBar().hide();


    }
}

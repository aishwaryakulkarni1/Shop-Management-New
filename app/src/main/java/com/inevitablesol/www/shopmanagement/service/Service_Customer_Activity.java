package com.inevitablesol.www.shopmanagement.service;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.R;

public class Service_Customer_Activity extends AppCompatActivity
{
    private static final String TAG = "Service_Customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_customer_landing);
        Log.d(TAG, "onCreate: ");
        ((ImageView)findViewById(R.id.service_customer_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {
                Log.d(TAG, "onClick: ");
                 startActivity(new Intent(Service_Customer_Activity.this,Service_Customer_View.class));
            }
        });

        ((ImageView)findViewById(R.id.service_customer_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: ");
                startActivity(new Intent(Service_Customer_Activity.this,Service_Customer_Add.class));
            }
        });

        ((ImageView)findViewById(R.id.service_customer_edit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: ");
                startActivity(new Intent(Service_Customer_Activity.this,Service_Customer_Edit.class));
            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG,"ConfigurationChanged");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    
}

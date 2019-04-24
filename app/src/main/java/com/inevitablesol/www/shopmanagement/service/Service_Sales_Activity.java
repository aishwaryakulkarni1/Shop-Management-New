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

public class Service_Sales_Activity extends AppCompatActivity {

    private static final String TAG = "Service_Sales_Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.service_total_sales);

        ((ImageView)findViewById(R.id.img_total_salse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Service_Sales_Activity.this,Service_Sales_Total_Sale.class));
            }
        });
        ((ImageView)findViewById(R.id.img_sale_DateTime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(Service_Sales_Activity.this,Service_Sales_DateTimeSale.class));
            }
        });

        ((ImageView)findViewById(R.id.img_paymentMode)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(Service_Sales_Activity.this,Service_Sales_PaymentMode.class));
            }
        });

        ((ImageView)findViewById(R.id.img_service_sale_customer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(Service_Sales_Activity.this,Service_Sales_Customer.class));
            }
        });

        Log.d(TAG, "onCreate: ");
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

package com.inevitablesol.www.shopmanagement.service;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.R;

public class Services_Activity extends BaseActivity  implements View.OnClickListener

{

   private ImageView add_services, billing_service,customer_services;
    private static final String TAG = "Services_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_services__add_service);
        getLayoutInflater().inflate(R.layout.activity_services__add_service, frameLayout);

        mDrawerList.setItemChecked(position, true);
        toolbar.setTitle(listArray[position]);
         add_services=(ImageView)findViewById(R.id.service_add);
           billing_service=(ImageView)findViewById(R.id.service_billing);
           customer_services=(ImageView)findViewById(R.id.service_customer);
           customer_services.setOnClickListener(this);
          add_services.setOnClickListener(this);
        //   view_services.setOnClickListener(this);
            billing_service.setOnClickListener(this);
        ((ImageView)findViewById(R.id.service_sale)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(Services_Activity.this,Service_Sales_Activity.class));
            }
        });
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onClick(View v)
    {
        int id=v.getId();
        switch (id)
        {
            case R.id.service_add:
                startActivity(new Intent(Services_Activity.this,Service_add_service_landing.class));
               // startActivity(new Intent(Services_Activity.this,Service_AddServices.class));
                Toast.makeText(getApplicationContext(),"Add Service",Toast.LENGTH_LONG).show();
                break;
            case  R.id.service_billing:
                startActivity(new Intent(Services_Activity.this,Service_Billing.class));
                Toast.makeText(getApplicationContext(),"Add Service",Toast.LENGTH_LONG).show();
                break;
            case  R.id.service_customer:
                startActivity(new Intent(Services_Activity.this,Service_Customer_Activity.class));
               // Toast.makeText(getApplicationContext(),"Add Service",Toast.LENGTH_LONG).show();
                break;

        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
    }
}

package com.inevitablesol.www.shopmanagement.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.R;

public class SettingsActivity extends BaseActivity
{
   private LinearLayout  linearLayout_notification;
    private  LinearLayout shop_LinearLayout,li_shop_setting,li_generalSetting,li_billing,li_import,li_export;
    private View linearPaymentAccepted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
        getLayoutInflater().inflate(R.layout.activity_settings, frameLayout);

        shop_LinearLayout=(LinearLayout)findViewById(R.id.ll_shopUpdate);
        li_shop_setting=(LinearLayout)findViewById(R.id.ll_shopSetting);
        li_generalSetting=(LinearLayout)findViewById(R.id.ll_generalSetting);
        li_billing=(LinearLayout)findViewById(R.id.ll_billing);
        li_import=(LinearLayout)findViewById(R.id.ll_export);
        linearLayout_notification=(LinearLayout)findViewById(R.id.ll_shopnotification);
        li_export=(LinearLayout)findViewById(R.id.ll_export);

        li_import=(LinearLayout)findViewById(R.id.setting_import);

        li_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               startActivity(new Intent(SettingsActivity.this,Import_Activity.class));
            }
        });

        li_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SettingsActivity.this,Import_Activity.class));
            }
        });

        linearLayout_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SettingsActivity.this,Billing_notifications.class));
            }
        });

        li_billing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(SettingsActivity.this,Billing_Settings.class));

            }
        });
        shop_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                Intent intent=new Intent(SettingsActivity.this,UpdatedShop.class);
                startActivity(intent);


            }
        });

        li_shop_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent=new Intent(SettingsActivity.this,ShopSetting.class);
                startActivity(intent);
            }
        });



        li_generalSetting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent=new Intent(SettingsActivity.this,GeneralSetting.class);
                startActivity(intent);

            }
        });


        mDrawerList.setItemChecked(position, true);
        toolbar.setTitle(listArray[position]);
    }
}

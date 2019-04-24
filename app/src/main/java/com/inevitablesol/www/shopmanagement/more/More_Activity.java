package com.inevitablesol.www.shopmanagement.more;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.R;

public class More_Activity extends BaseActivity
{
    private LinearLayout layout_aboutUs;
    private  LinearLayout layout_contactUS;
    private  LinearLayout layout_notification;
    private  LinearLayout layout_usermanual;
    private  LinearLayout layout_Q_$_A;
    private  LinearLayout linearLayout_tutorails;
    private  LinearLayout linearLayout_feedBack;
    private  LinearLayout linearLayout_policy;
    private  LinearLayout linearLayout_servcies;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_more);
        getLayoutInflater().inflate(R.layout.activity_more,frameLayout);

        mDrawerList.setItemChecked(position, true);
        toolbar.setTitle(listArray[position]);
        layout_notification=(LinearLayout)findViewById(R.id.ll_notification);
        layout_aboutUs=(LinearLayout)findViewById(R.id.ll_aboutUs);
        layout_contactUS=(LinearLayout)findViewById(R.id.ll_contactUs);
        layout_usermanual=(LinearLayout)findViewById(R.id.ll_user_manual);
        layout_Q_$_A=(LinearLayout)findViewById(R.id.ll_q_a);
        linearLayout_feedBack=(LinearLayout)findViewById(R.id.ll_feedback);
        linearLayout_servcies=(LinearLayout)findViewById(R.id.ll_services);


        linearLayout_servcies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(More_Activity.this,M_Hourz_Services.class));
            }
        });

        linearLayout_policy=(LinearLayout)findViewById(R.id.ll_policy);
        linearLayout_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(More_Activity.this,More_Policy.class));

            }
        });

        linearLayout_feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(More_Activity.this,More_feedBack.class));

            }
        });

         linearLayout_tutorails=(LinearLayout)findViewById(R.id.ll_tutorails);
         linearLayout_tutorails.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 startActivity(new Intent(More_Activity.this,More_tutorials.class));
             }
         });
        layout_Q_$_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(More_Activity.this,User_Manual_Q_A.class));

            }
        });
        layout_usermanual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(More_Activity.this,User_Manual.class));
            }
        });
        layout_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(More_Activity.this,More_Notifications.class));
            }
        });

        layout_contactUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(More_Activity.this,More_ContactUS.class));

            }
        });

        layout_aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(More_Activity.this,More_AboutUS.class));

            }
        });
    }
}

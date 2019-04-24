package com.inevitablesol.www.shopmanagement.more;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.inevitablesol.www.shopmanagement.R;

public class More_Services extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more__services);
        LinearLayout termsandCondition=(LinearLayout)findViewById(R.id.ly_policy_inforForm);
        termsandCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/forms/q3Gy4X2ea4m6PACC2")));

            }
        });

        LinearLayout  reocord=(LinearLayout)findViewById(R.id.ly_policy_recordform);
        reocord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/forms/pv81PlCxce0Q70uw2")));

            }
        });
        LinearLayout  linearLayout=(LinearLayout)findViewById(R.id.mhorzcampaign);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSfimpRAkuoIb6E3cQ2nTrkpqKPkBYAT_9jrOR_CKms1T57qgw/viewform?usp=sf_link")));

            }
        });
    }
}

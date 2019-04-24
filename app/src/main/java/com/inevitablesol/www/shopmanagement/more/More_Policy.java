package com.inevitablesol.www.shopmanagement.more;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.inevitablesol.www.shopmanagement.R;

public class More_Policy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more__policy);
       LinearLayout termsandCondition=(LinearLayout)findViewById(R.id.ly_policy_terms);
        termsandCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mechatrontechgear.com/terms.html")));

            }
        });

        LinearLayout  ly_policy=(LinearLayout)findViewById(R.id.ly_policy);
        ly_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mechatrontechgear.com/privacy.html")));

            }
        });

        LinearLayout ly_policy_refund=(LinearLayout)findViewById(R.id.ly_policy_refund);
        ly_policy_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mechatrontechgear.com/refunds.html")));
            }
        });

        LinearLayout  linearLayoutTerms=(LinearLayout)findViewById(R.id.ly_policy_termsUse);
        linearLayoutTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mechatrontechgear.com/termsofuse.html")));

            }
        });


        LinearLayout    servviceAgreement =(LinearLayout)findViewById(R.id.ly_policy_ServiceAgreement);
        servviceAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mechatrontechgear.com/serviceagreement.html")));

            }
        });

        LinearLayout    ly_policy_Email =(LinearLayout)findViewById(R.id.ly_policy_Email);
        ly_policy_Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mechatrontechgear.com/emailabuse.html")));

            }
        });


        LinearLayout    ly_policy_Disclaimer =(LinearLayout)findViewById(R.id.ly_policy_Disclaimer);
        ly_policy_Disclaimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mechatrontechgear.com/disclaimer.html")));

            }
        });






    }
}

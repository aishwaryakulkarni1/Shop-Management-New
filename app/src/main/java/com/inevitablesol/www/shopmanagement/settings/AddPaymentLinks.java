package com.inevitablesol.www.shopmanagement.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.inevitablesol.www.shopmanagement.R;

public class AddPaymentLinks extends AppCompatActivity implements View.OnClickListener
{
    private ImageView add_paytmLink,add_Bhim_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_links);
        add_paytmLink=(ImageView)findViewById(R.id.billing_addPaytmScreen);
        add_Bhim_link=(ImageView)findViewById(R.id.billing_addBhimUi);
        add_Bhim_link.setOnClickListener(this);
        add_paytmLink.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.billing_addPaytmScreen:
                startActivity(new Intent(AddPaymentLinks.this,AddPaytmScreen.class));
                break;
            case R.id.billing_addBhimUi:
                startActivity(new Intent(AddPaymentLinks.this,AddBhim_Screen.class));
                break;
        }

    }

}

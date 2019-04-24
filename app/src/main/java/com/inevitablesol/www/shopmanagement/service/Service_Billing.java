package com.inevitablesol.www.shopmanagement.service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.inevitablesol.www.shopmanagement.R;

public class Service_Billing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_billing_customer);

        ((AppCompatButton)findViewById(R.id.proceedtobilling)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Service_Billing.this,Service_Billing_MakePayment.class));
            }
        });

    }
}

package com.inevitablesol.www.shopmanagement.service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.inevitablesol.www.shopmanagement.R;

public class Service_add_service_landing extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_landing);

        ImageView  addService=(ImageView)findViewById(R.id.ser_add_service);
        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Service_add_service_landing.this,Service_AddServices.class));
            }
        });

        ImageView  viewServcie=(ImageView)findViewById(R.id.ser_view_service);
           viewServcie.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v)
               {

                   startActivity(new Intent(Service_add_service_landing.this,Service_ViewServices.class));
               }
           });

        ImageView  editServcie=(ImageView)findViewById(R.id.ser_edit_service);
        editServcie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(Service_add_service_landing.this,Service_EditService.class));
            }
        });

    }
}

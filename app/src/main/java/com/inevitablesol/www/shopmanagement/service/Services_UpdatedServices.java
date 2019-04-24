package com.inevitablesol.www.shopmanagement.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Services_UpdatedServices extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_name,txt_id,txt_amount,txt_due,txt_duration;
    private  String  service_id;
    private AppCompatButton updateService;
    private String UPDATED_SERVIES="http://35.161.99.113:9000/api/service_mgmt/edit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services__updated_services);
     //   txt_id = (TextView) findViewById(R.id.txt_ServiceID);
        txt_amount = (TextView) findViewById(R.id.input_service_amount);
        txt_name = (TextView) findViewById(R.id.input_service_serviceName);
        txt_duration = (TextView) findViewById(R.id.input_service_duration);
        txt_due = (TextView) findViewById(R.id.input_service_Due);
        updateService=(AppCompatButton)findViewById(R.id.update_service);
        updateService.setOnClickListener(this);


    }

    @Override
    public void onClick(View v)
    {
        int id=v.getId();
        switch (id)
        {
            case R.id.update_service :
                break;
        }

    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}



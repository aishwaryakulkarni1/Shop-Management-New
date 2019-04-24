package com.inevitablesol.www.shopmanagement.service;

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

public class Services_SelectedService extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_name,txt_id,txt_amount,txt_due,txt_duration;
    private AppCompatButton bt_remove;
    private String DELETE_SERVICES=" http://35.161.99.113:9000/api/service_mgmt/delete";
    private  String service_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services__selected_service);
        txt_id = (TextView) findViewById(R.id.txt_ServiceID);
        txt_amount = (TextView) findViewById(R.id.txt_ServiceAmount);
        txt_name = (TextView) findViewById(R.id.txt_ServiceName);
        txt_duration = (TextView) findViewById(R.id.txt_ServiceDuration);
        txt_due = (TextView) findViewById(R.id.txt_view_Due);
        bt_remove=(AppCompatButton)findViewById(R.id.bt_service_removeService);
        bt_remove.setOnClickListener(this);
        Intent intent=getIntent();
        if(intent.hasExtra("name"))
        {
            String name=intent.getStringExtra("name");
             service_id=intent.getStringExtra("service_id");
            String amount =intent.getStringExtra("amount");
            String due=intent.getStringExtra("due");
             String duration=intent.getStringExtra("duration");
                   txt_id.setText(service_id);
                   txt_amount.setText(amount);
                   txt_name.setText(name);
                    txt_duration.setText(duration);
                    txt_due.setText(due);
        }else
        {

        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.bt_service_removeService:
                removeCustomer();
                break;
        }


    }

    private void removeCustomer()
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Updating...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_SERVICES, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();
                try
                {
                    loading.dismiss();
                    JSONObject jsonObject=new JSONObject(resp);
                    String message=jsonObject.getString("message");
                    if(message.equalsIgnoreCase("Delete data succesfully."))

                    {
                        loading.dismiss();
                        Toast.makeText(Services_SelectedService.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }else
                    {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }



                }catch (JSONException e)
                {

                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if(error instanceof NoConnectionError)
                    Toast.makeText(Services_SelectedService.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("service_id",service_id);
                Log.d("services_id",service_id);
                return   params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

          }
    }


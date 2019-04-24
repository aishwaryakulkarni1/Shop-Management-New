package com.inevitablesol.www.shopmanagement.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.inevitablesol.www.shopmanagement.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Service_EditService extends AppCompatActivity implements View.OnClickListener {
    private EditText et_serachCustomer;
    private String GET_SERVICEDETAILS="http://35.161.99.113:9000/api/service_mgmt/service_search";

    private RecyclerView recyclerView;
    private Services_ServiceAdapter services_serviceAdapter;

    private ArrayList<Services> services;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__edit_service);
    }
    @Override
    public void onClick(View v)
    {


    }


}

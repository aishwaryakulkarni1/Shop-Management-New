package com.inevitablesol.www.shopmanagement.service;

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

public class Service_ViewServices extends AppCompatActivity implements View.OnClickListener {

    private EditText et_serachCustomer;
    private String GET_SERVICEDETAILS="http://35.161.99.113:9000/api/service_mgmt/service_search";

    private RecyclerView recyclerView;
    private Services_ServiceAdapter services_serviceAdapter;

    private ArrayList<Services> services;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_product_dailog);
//        et_serachCustomer=(EditText)findViewById(R.id.input_serviceSearch);
//        et_serachCustomer.setOnClickListener(this);
//        recyclerView = (RecyclerView)findViewById(R.id.stock_recyclerView);
//        et_serachCustomer.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s)
//            {
//                String serviceName=s.toString().trim();
//                if(!serviceName.isEmpty() && serviceName.length()>0)
//                {
//                    getServices(serviceName);
//                }else
//                {
//
//                }
//
//
//            }
//        });

//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
//        {
//
//            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
//
//                @Override public boolean onSingleTapUp(MotionEvent e)
//                {
//                    return true;
//                }
//
//            });
//
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
//            {
//                View child = rv.findChildViewUnder(e.getX(), e.getY());
//                if(child != null && gestureDetector.onTouchEvent(e))
//                {
//                    int position = rv.getChildAdapterPosition(child);
//                    Services Info= services.get(position);
//                    Intent intent=new Intent(Service_ViewServices.this,Services_SelectedService.class);
//                    intent.putExtra("name",Info.getService_name());
//                    intent.putExtra("service_id",Info.getService_id());
//                    intent.putExtra("amount",Info.getService_amount());
//                    intent.putExtra("due",Info.getService_due());
//                    intent.putExtra("duration",Info.getService_duration());
//                    startActivity(intent);
//
//
//
//
//                }
//                return  false;
//
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e)
//            {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });


    }

//    private void getServices(final String serviceName)
//    {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SERVICEDETAILS, new Response.Listener<String>()
//        {
//            @Override
//            public void onResponse(String response)
//            {
//                String resp = response.toString().trim();
//
//                Log.d("res",resp);
//                JSONObject jsonObject= null;
//                try
//                {
//                    jsonObject = new JSONObject(resp);
//                    String message =      jsonObject.getString("message");
//
//
//                    if(message.equalsIgnoreCase("Data available"))
//                    {
//
//                        Services_view_parser services_view_parser=new Services_view_parser(resp);
//                                               services_view_parser.serviceDetails();
//                             services=services_view_parser.getService();
//
//                        recyclerView.setHasFixedSize(true);
//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                        recyclerView.setLayoutManager(layoutManager);
//                        services_serviceAdapter=new Services_ServiceAdapter(services,Service_ViewServices.this);
//                        recyclerView.setAdapter(services_serviceAdapter);
//                    }
//                    else
//                    {
//                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
//                        if(services!=null)
//                        {
//                            services.clear();
//                            services_serviceAdapter.notifyDataSetChanged();
//                            recyclerView.setHasFixedSize(true);
//                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                            recyclerView.setLayoutManager(layoutManager);
//                            services_serviceAdapter=new Services_ServiceAdapter(services,Service_ViewServices.this);
//                            recyclerView.setAdapter(services_serviceAdapter);
//                        }
//                    }
//
//                } catch (JSONException e)
//                {
//                    Log.d("Exception",""+e);
//                    e.printStackTrace();
//                }
//
//
//
//
//
//
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError
//            {
//                Map<String,String> params = new HashMap<>();
//                params.put("name",serviceName);
//
//                return   params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }
//
    @Override
    public void onClick(View v)
    {


    }
}

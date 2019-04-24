package com.inevitablesol.www.shopmanagement.analysis;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.analysis.date.DatePickFragments;
import com.inevitablesol.www.shopmanagement.constatnt.WEBAPI;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RepetativeCustomer extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener, View.OnClickListener ,WEBAPI{

    private static final String TAG = "HighestSellingDay";
    private ProgressDialog loading;
    private GlobalPool globalPool;
    private String startDate;
    private String id;
    private RecyclerView recyclerView;

    private ArrayList<Top_seven_Class> top_seven_classes=new ArrayList<Top_seven_Class>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repetative_customer);
        TextView textView=(TextView)findViewById(R.id.id_sale_fromDate) ;
        globalPool=(GlobalPool)this.getApplicationContext();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        Intent intent=getIntent();

        if(intent.hasExtra("startDate"))
        {
            startDate=intent.getStringExtra("startDate");
            id=intent.getStringExtra("id");
            textView.setText(startDate);

            Log.d(TAG, "onCreate: State :"+startDate);
            Log.d(TAG, "onCreate: Id"+id);
            getTotalSale_Status(startDate,id);
        }

    }

     @Override
     public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
     {
         Log.d(TAG, "onDateSet:"+year+"month"+month+"dayOfMonth");

     }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
//            case R.id.im_fromdatePicker:
//                DatePickFragments.getInstance().show(getSupportFragmentManager(),"ff");
//                Log.d(TAG, "onClick: Ref of singleTon"+ DatePickFragments.getInstance());
//                break;
//            case R.id.im_toDatePicker:
//                DatePickFragments.getInstance().show(getSupportFragmentManager(),"ff");
//                Log.d(TAG, "onClick: Ref of singleTon"+ DatePickFragments.getInstance());
//                break;
            default:


        }

    }



    private void  getTotalSale_Status(final String startDate, final String id)
    {
        Log.d(TAG, "getTotalSale_Status:");
        loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        final StringRequest req = new StringRequest(Request.Method.POST,WEBAPI.HighestRepeatativeCustomer,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        loading.dismiss();
                        Log.d(TAG, response.toString());

                        try
                        {
                            JSONObject jsonObject=new JSONObject(response);
                            String message= jsonObject.getString("message");
                            if(message.equalsIgnoreCase("true"))
                            {
                                JSONArray jsonArray=jsonObject.getJSONArray("records");
                                int len=jsonArray.length();
                                for (int i=0;i<len;i++)
                                {
                                    Top_seven_Class top_seven =new Top_seven_Class();

                                    if(top_seven instanceof Top_seven_Class)
                                    {
                                        top_seven.setNum(jsonArray.getJSONObject(i).getString("num"));
                                        top_seven.setCreated_Date(jsonArray.getJSONObject(i).getString("cust_name"));
                                        top_seven.setInvoice_count(jsonArray.getJSONObject(i).getString("RepeatCustomerCount"));
                                        top_seven.setTotal(jsonArray.getJSONObject(i).getString("RepeatCustomerCount"));
                                        top_seven_classes.add(top_seven);

                                    }

                                }

                                Log.d(TAG, "onResponse: TopClass"+top_seven_classes.toString());
                                Top_class_Adapter top_class_adapter=new Top_class_Adapter(top_seven_classes,RepetativeCustomer.this);
                                recyclerView.setHasFixedSize(true);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(top_class_adapter);


                                Log.d(TAG, "onResponse: "+response);
                            }else
                            {
                                Log.d(TAG, "onResponse: Else"+response);
                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", globalPool.getDbname());
                params.put("startDate",startDate);
                params.put("id",id);
                Log.d(TAG, "getParams:"+params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);


    }
}

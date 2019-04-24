package com.inevitablesol.www.shopmanagement.analysis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.inevitablesol.www.shopmanagement.constatnt.WEBAPI;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellingProduct extends AppCompatActivity
{
    private static final String TAG = "HighestSellingDay";
    private ProgressDialog loading;
    private GlobalPool globalPool;
    private String startDate;
    private String id;
    private RecyclerView recyclerView;

    private ArrayList<Top_seven_Class> top_seven_classes=new ArrayList<Top_seven_Class>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_product);
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


    private void  getTotalSale_Status(final String startDate, final String id)
    {
        Log.d(TAG, "getTotalSale_Status:");
        loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        final StringRequest req = new StringRequest(Request.Method.POST, WEBAPI.HighestSale,
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
                                        top_seven.setCreated_Date(jsonArray.getJSONObject(i).getString("created_date"));
                                        top_seven.setInvoice_count(jsonArray.getJSONObject(i).getString("invoicecount"));
                                        top_seven.setTotal(jsonArray.getJSONObject(i).getString("total"));
                                        top_seven_classes.add(top_seven);

                                    }

                                }

                                Log.d(TAG, "onResponse: TopClass"+top_seven_classes.toString());
                                Top_class_Adapter top_class_adapter=new Top_class_Adapter(top_seven_classes,SellingProduct.this);
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

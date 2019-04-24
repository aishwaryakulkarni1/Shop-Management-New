package com.inevitablesol.www.shopmanagement.product_info;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.JsonObject;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Single_product_details extends AppCompatActivity
{
    private TextView tx_itemName,txt_company,txt_specification,txt_owner,txt_hscn;
    private static final String TAG = "Single_product_details";
    private GlobalPool globalPool;
    private String item_Id;
    private  TextView category;
    private  TextView txt_itemBarcode;
    private  TextView txt_mUnit;
    private  TextView txt_unit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_details);
        tx_itemName=(TextView)findViewById(R.id.itemView_itemname);
        txt_company=(TextView)findViewById(R.id.itemView_Company);
        txt_specification=(TextView)findViewById(R.id.itemView_spec);
        txt_hscn=(TextView)findViewById(R.id.itemView_hsn);
        category=(TextView)findViewById(R.id.itemView_pname);
        txt_owner=(TextView)findViewById(R.id.itemView_author);


        txt_mUnit=(TextView)findViewById(R.id.txt_munit);
        txt_unit=(TextView)findViewById(R.id.txt_unit);
        txt_itemBarcode=(TextView)findViewById(R.id.txt_itembarcode);
        Intent intent=getIntent();
        globalPool= (GlobalPool) this.getApplicationContext();

        if(intent.hasExtra("name"))
        {
            tx_itemName.setText(intent.getStringExtra("name"));
            txt_owner.setText(intent.getStringExtra("owner"));
            txt_specification.setText(intent.getStringExtra("specification"));
            txt_hscn.setText(intent.getStringExtra("hsn"));
            item_Id=intent.getStringExtra("item_id");

            txt_company.setText(intent.getStringExtra("company"));
            txt_mUnit.setText(intent.getStringExtra("munit"));

            txt_unit.setText(intent.getStringExtra("barcode"));
            txt_itemBarcode.setText(intent.getStringExtra("company"));

        }
        getItemDetails();
    }


    private void getItemDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETITEMDETAILSBYITEMID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {

                    try {
                        Log.d(TAG, "onResponse: Item Details "+response);
                        JSONObject jsonObject=new JSONObject(response);
                                   JSONArray jsonArray= jsonObject.getJSONArray("records");
                                                      JSONObject jsonObject1=   jsonArray.getJSONObject(0);
                                                      category.setText(jsonObject1.getString("product_type"));
                                                      txt_mUnit.setText(jsonObject1.getString("measurement_unit"));
                                                       txt_itemBarcode.setText(jsonObject1.getString("item_barcode"));


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    Log.d("Exception", "" + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(Single_product_details.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", globalPool.getDbname());
                params.put("item_id",item_Id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}

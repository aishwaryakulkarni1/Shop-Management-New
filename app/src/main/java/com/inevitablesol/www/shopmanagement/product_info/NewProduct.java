package com.inevitablesol.www.shopmanagement.product_info;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewProduct extends AppCompatActivity implements View.OnClickListener
{
    private AppCompatButton addProduct;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String dbname;
    private EditText mpoduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        mpoduct=(EditText)findViewById(R.id.new_product);
        addProduct=(AppCompatButton)findViewById(R.id.bt_addnewProductDetails);
        addProduct.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.bt_addnewProductDetails:
                final String product = mpoduct.getText().toString().trim();
                if(product!=null && product.length()>1)
                {
                    addData(product);
                }else {
                    Toast.makeText(getApplicationContext(),"Please add product",Toast.LENGTH_LONG).show();
                }

                break;
        }

    }

    private void addData(final String product)
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.ADDNEW_PRODUCTTYPE, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try {
                    Log.d("response",response);
                    JSONObject msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Add new product succesfully"))
                    {
                        Toast.makeText(NewProduct.this, "Product Add Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.putExtra("MESSAGE","added");
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                    }

                }catch (JSONException e)
                {

                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(NewProduct.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                Log.d("product",product);
                params.put("dbname", dbname);
                params.put("product_type", product);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

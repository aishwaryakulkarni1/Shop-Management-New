package com.inevitablesol.www.shopmanagement.MenuItemModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddNewProduct extends AppCompatActivity implements View.OnClickListener {
    private EditText name, company, owner, specification;
    private AppCompatButton bt_addnewProductType;
    private String p_name, p_company, p_owner, p_specification, created_by;
    private String AddNEWPRODUCT = "http://35.161.99.113:9000/webapi/product/create_menu_item";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        name = (EditText) findViewById(R.id.new_productItem);
        bt_addnewProductType = (AppCompatButton) findViewById(R.id.bt_addnewProductType);
        bt_addnewProductType.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_addnewProductType:
//                 Intent returnIntent = new Intent();
//                 setResult(Activity.RESULT_OK,returnIntent);
//                 finish();
                final String product = name.getText().toString().trim();
                if (product != null && product.length() > 1) {
                    addData(product);
                } else {
                    Toast.makeText(getApplicationContext(), "Please add product", Toast.LENGTH_LONG).show();
                }

                break;
        }

    }

    private void addData(final String product) {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.ADDNEW_PRODUCTTYPE, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try {
                    Log.d("response", response);
                    JSONObject msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Add new product succesfully"))
                    {
//                        Intent intent=new Intent();
//                        intent.putExtra("MESSAGE","added");
//                        setResult(Activity.RESULT_OK,intent);
//                        finish();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }

                } catch (JSONException e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(AddNewProduct.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                Log.d("product", product);

                params.put("dbname", dbname);
                params.put("product_type", product);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}

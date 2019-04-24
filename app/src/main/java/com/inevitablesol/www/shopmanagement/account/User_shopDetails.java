package com.inevitablesol.www.shopmanagement.account;

import android.app.ProgressDialog;
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
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class User_shopDetails extends AppCompatActivity
{
    private static final String TAG = "User_shopDetails";
    private GlobalPool globalPool;

    TextView  txt_shopname,txt_shop_type,txt_adddress,txt_cin ,txt_shop_pan,txt_mobile,txt_email,txt_shop_act;
    TextView txt_gst,txt_shop_description;

    TextView txt_shop_owner,txt_shop_owner_number,txt_shop_onwer_email;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_shop_details);
        globalPool = (GlobalPool) this.getApplication();
        txt_shopname=(TextView)findViewById(R.id.shop_name);
        txt_shop_type=(TextView)findViewById(R.id.shop_type);
        txt_gst=(TextView)findViewById(R.id.shop_gst);
        txt_adddress=(TextView)findViewById(R.id.shop_address);
        txt_shop_pan=(TextView)findViewById(R.id.shop_pan_number);
        txt_shop_act=(TextView)findViewById(R.id.shop_act_name);
        txt_cin=(TextView)findViewById(R.id.shop_act_cin);
        txt_mobile=(TextView)findViewById(R.id.shop_mobile);
        txt_email=(TextView)findViewById(R.id.shop_email);
        txt_shop_description=(TextView)findViewById(R.id.txt_shop_description);
        txt_shop_owner=(TextView)findViewById(R.id.shop_owner);
        txt_shop_owner_number=(TextView)findViewById(R.id.shop_owner_number);
        txt_shop_onwer_email=(TextView)findViewById(R.id.show_owner_email);
        getShopDetails();
    }






    private void getShopDetails()
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_SHOPDETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.d(TAG, "onResponse()" + response + "]");
                    String resp = response.toString().trim();


                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                    else
                        {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("records");

                        txt_shopname.setText(jsonObject1.getString("s_name"));
                        txt_adddress.setText(jsonObject1.getString("address"));
//                        input_district.setText(jsonObject1.getString("district"));
//                        input_state.setText(jsonObject1.getString("state"));
//
                        txt_shop_pan.setText(jsonObject1.getString("pincode"));
                        txt_mobile.setText(jsonObject1.getString("mobile_no"));
                        txt_email.setText(jsonObject1.getString("email_id"));
                        txt_gst.setText(jsonObject1.getString("gstin_no"));
                        txt_shop_type.setText(jsonObject1.getString("shop_type"));
//
//
//                        input_pincode.setText(jsonObject1.getString("pincode"));
//                        input_mobile_no.setText(jsonObject1.getString("mobile_no"));
//                        input_email.setText(jsonObject1.getString("email_id"));
//                        input_pass.setText(jsonObject1.getString("password"));
//                        input_shoptype.setText(jsonObject1.getString("shop_type"));
//
//
//                        input_service_type.setText(jsonObject1.getString("type_of_service_provided"));
                           txt_shop_owner.setText(jsonObject1.getString("shop_owner"));
                        txt_shop_owner_number.setText(jsonObject1.getString("shop_owner_mobile_no"));
                        txt_shop_onwer_email.setText(jsonObject1.getString("shop_owner_email_id"));
                        txt_shop_act.setText(jsonObject1.getString("shop_act_no"));
                        txt_shop_description.setText(jsonObject1.getString("shop_details"));


                    }
                    loading.dismiss();


                } catch (Exception e) {
                    Log.d("Exception", "" + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", globalPool.getDbname());
                params.put("s_id", globalPool.getShopId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}




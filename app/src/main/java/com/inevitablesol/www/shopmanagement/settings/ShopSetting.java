package com.inevitablesol.www.shopmanagement.settings;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;




public class ShopSetting extends AppCompatActivity {

    private static final String TAG = "ShopSetting";
    private GlobalPool globalPool;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_setting);
        globalPool= (GlobalPool) this.getApplicationContext();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getShopDetails();
    }





    private void getShopDetails()
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_SHOPDETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {

                    Log.d(TAG, "onResponse()" + response + "]");
                    String resp = response.toString().trim();
                    Log.d("UserDetails", resp);

                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else
                    {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("records");

                        loading.dismiss();

                    }



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

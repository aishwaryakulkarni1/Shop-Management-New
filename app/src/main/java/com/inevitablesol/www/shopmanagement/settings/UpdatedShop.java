package com.inevitablesol.www.shopmanagement.settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.inevitablesol.www.shopmanagement.LonginDetails.SigninActivity;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.Validation.Validation;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class UpdatedShop extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText input_name, input_mobile_no, input_email, input_pass, input_address, input_district,
            input_state, input_pincode, input_shoptype, input_service_type, input_shop_owner, input_owner_mobile,
            input_owner_email, input_shop_act, input_shop_details;

    TextView link_login;

    private static final String KEY_SNAME = "s_name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DISTRICT = "district";
    private static final String KEY_STATE = "state";
    private static final String KEY_PINCODE = "pincode";
    private static final String KEY_MOBILE_NO = "mobile_no";
    private static final String KEY_EMAILiD = "email_id";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_SHOP_TYPE = "shop_type";
    private static final String KEY_SERVICE_TYPE = "type_of_service_provided";
    private static final String KEY_SHOP_OWNER = "shop_owner";
    private static final String KEY_OWNER_MOBILE = "shop_owner_mobile_no";
    private static final String KEY_OWNER_EMAIL = "shop_owner_email_id";
    private static final String KEY_SHOP_ACT = "shop_act_no";
    private static final String KEY_SHOP_DETAILS = "shop_details";

    AppCompatButton bt_signup;
    private String REGISTER_SHOP = "";

    private static final String TAG = "UpdatedShop";
    private Context context = UpdatedShop.this;

    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private String shop_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updated_shop);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        link_login = (TextView) findViewById(R.id.link_login);
        //link_login.setOnClickListener();
        input_name = (TextInputEditText) findViewById(R.id.input_name);
        input_mobile_no = (TextInputEditText) findViewById(R.id.input_mobile_no);
        input_email = (TextInputEditText) findViewById(R.id.input_email);
        input_pass = (TextInputEditText) findViewById(R.id.input_pass);
        input_address = (TextInputEditText) findViewById(R.id.input_address);
        input_district = (TextInputEditText) findViewById(R.id.input_district);
        input_state = (TextInputEditText) findViewById(R.id.input_state);
        input_pincode = (TextInputEditText) findViewById(R.id.input_pincode);
        input_shoptype = (TextInputEditText) findViewById(R.id.input_shoptype);
        input_service_type = (TextInputEditText) findViewById(R.id.input_service_type);
        input_shop_owner = (TextInputEditText) findViewById(R.id.input_shop_owner);
        input_owner_mobile = (TextInputEditText) findViewById(R.id.input_owner_mobile);
        input_owner_email = (TextInputEditText) findViewById(R.id.input_owner_email);
        input_shop_act = (TextInputEditText) findViewById(R.id.input_shop_act);
        input_shop_details = (TextInputEditText) findViewById(R.id.input_shop_details);

        bt_signup = (AppCompatButton) findViewById(R.id.bt_update);
        bt_signup.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        shop_id = sharedpreferences.getString("s_id", "");
        if (!shop_id.isEmpty()) {
            getShopDetails();
        } else {
            finish();
            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_LONG).show();
        }

    }


    private boolean checkValidation()
    {
        boolean ret = true;

        if (!Validation.hasText(input_name)) ret = false;
        if (!Validation.hasText(input_address)) ret = false;
        if (!Validation.hasText(input_district)) ret = false;
        if (!Validation.hasText(input_state)) ret = false;
        if (!Validation.hasText(input_pincode)) ret = false;
        if (!Validation.isPhoneNumber(input_mobile_no, true)) ret = false;
        if (!Validation.isEmailValid(input_email.getText().toString().trim())) ret = false;
        if (!Validation.hasText(input_pass)) ret = false;
        if (!Validation.hasText(input_shoptype)) ret = false;
        if (!Validation.hasText(input_service_type)) ret = false;
        if (!Validation.hasText(input_shop_owner)) ret = false;
        if (!Validation.isPhoneNumber(input_owner_mobile, true)) ret = false;
        if (!Validation.isEmailValid(input_owner_email.getText().toString().trim())) ret = false;
        if (!Validation.hasText(input_shop_act)) ret = false;
        if (!Validation.hasText(input_shop_details)) ret = false;

        return ret;
    }

    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.bt_update:
                if (checkValidation())
                {
                    update_shop();
                } else {
                    Toast.makeText(this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.link_login:
                Intent intent = new Intent(this, SigninActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    public void update_shop()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.UPDATE_SHOP_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                String resp = response.toString().trim();

                Log.d("Add Response", resp);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("Update shop Details Succesfully."))
                    {

                        Toast.makeText(getApplicationContext(), "Shop Updated  successfully.", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                } catch (JSONException e)
                {
                    loading.dismiss();
                    e.printStackTrace();
                }
//                if (resp.equalsIgnoreCase("Add new shop succesfully."))
//                {
//                    Toast.makeText(ShopRegistrationActivity.this, "Shop registered successfully.", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(ShopRegistrationActivity.this, "Unable to register shop.", Toast.LENGTH_SHORT).show();
//                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplicationContext(), "Please connect to the internet before continuing.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                String shopName = input_name.getText().toString();
                String address = input_address.getText().toString();
                String district = input_district.getText().toString();
                String state = input_state.getText().toString();
                String pincode = input_pincode.getText().toString();
                String mobil_no = input_mobile_no.getText().toString();
                String emailId = input_email.getText().toString();
                String password = input_pass.getText().toString();
                String shop_type = input_shoptype.getText().toString();
                String service_type = input_service_type.getText().toString();
                String shop_owner = input_shop_owner.getText().toString();
                String owner_mobile = input_owner_mobile.getText().toString();
                String owner_email = input_owner_email.getText().toString();
                String shop_act = input_shop_act.getText().toString();
                String shop_details = input_shop_details.getText().toString();
                Map<String, String> params = new LinkedHashMap<>();
                params.put(KEY_SNAME, shopName);
                params.put(KEY_ADDRESS, address);
                params.put(KEY_DISTRICT, district);
                params.put(KEY_STATE, state);
                params.put(KEY_PINCODE, pincode);
                params.put(KEY_MOBILE_NO, mobil_no);
                params.put(KEY_EMAILiD, emailId);
                params.put(KEY_PASSWORD, password);
                params.put(KEY_SHOP_TYPE, shop_type);
                params.put(KEY_SERVICE_TYPE, service_type);
                params.put(KEY_SHOP_OWNER, shop_owner);
                params.put(KEY_OWNER_MOBILE, owner_mobile);
                params.put(KEY_OWNER_EMAIL, owner_email);
                params.put(KEY_SHOP_ACT, shop_act);
                params.put(KEY_SHOP_DETAILS, shop_details);
                params.put("s_id",shop_id);

                Log.d("SParams", params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
                    Log.d("UserDetails", resp);

                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else
                        {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("records");

                        input_name.setText(jsonObject1.getString("s_name"));
                        input_address.setText(jsonObject1.getString("address"));
                        input_district.setText(jsonObject1.getString("district"));
                        input_state.setText(jsonObject1.getString("state"));

                        input_pincode.setText(jsonObject1.getString("pincode"));
                        input_mobile_no.setText(jsonObject1.getString("mobile_no"));
                        input_email.setText(jsonObject1.getString("email_id"));
                        input_pass.setText(jsonObject1.getString("password"));
                        input_shoptype.setText(jsonObject1.getString("shop_type"));


                        input_pincode.setText(jsonObject1.getString("pincode"));
                        input_mobile_no.setText(jsonObject1.getString("mobile_no"));
                        input_email.setText(jsonObject1.getString("email_id"));
                        input_pass.setText(jsonObject1.getString("password"));
                        input_shoptype.setText(jsonObject1.getString("shop_type"));


                        input_service_type.setText(jsonObject1.getString("type_of_service_provided"));
                        input_shop_owner.setText(jsonObject1.getString("shop_owner"));
                        input_owner_mobile.setText(jsonObject1.getString("shop_owner_mobile_no"));
                        input_owner_email.setText(jsonObject1.getString("shop_owner_email_id"));
                        input_shop_act.setText(jsonObject1.getString("shop_act_no"));
                        input_shop_details.setText(jsonObject1.getString("shop_details"));
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
                params.put("dbname", dbname);
                params.put("s_id", shop_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}

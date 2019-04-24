package com.inevitablesol.www.shopmanagement.RegistrationModule;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.inevitablesol.www.shopmanagement.more.More_ContactUS;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopRegistrationActivity extends AppCompatActivity
{

    private static final String REGISTER_SHOP = "http://35.161.99.113:9000/webapi/admin/database";
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
    private static final String TAG = "ShopRegistrationActivit";

    TextInputEditText input_name, input_mobile_no, input_email, input_pass, input_address, input_district,
            input_state, input_pincode, input_shoptype, input_service_type, input_shop_owner, input_owner_mobile,
            input_owner_email, input_shop_act, input_shop_details,input_gst;

    TextView link_login;

    AppCompatButton bt_signup;
    private String custMobile;
//    private Spinner category_spinner,sub_category;
    private int MY_PERMISSIONS_REQUEST_CALL_PHONE=101;
    Pattern pattern=Pattern.compile("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_registration);
        getSupportActionBar().hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        link_login = (TextView) findViewById(R.id.link_login);
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
        input_gst=(TextInputEditText)findViewById(R.id.input_shop_gstin);



        bt_signup = (AppCompatButton) findViewById(R.id.bt_signup);
        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (checkValidation()  && isValidGst())
                {
                    register_shop();
                    bt_signup.setEnabled(false);
                    //finish();
                }else if(!isValidGst())
                {
                    Toast.makeText(ShopRegistrationActivity.this, "Please Add Valid GST", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                Intent intent = new Intent(ShopRegistrationActivity.this, SigninActivity.class);
                startActivity(intent);


            }
        });

        TextView textView=(TextView)findViewById(R.id.phoneNumber);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callToCustomer();
            }
        });


//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.category, android.R.layout.simple_spinner_item);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        category_spinner.setAdapter(adapter);
//
//
//         category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//             @Override
//             public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//             {
//                 String item =parent.getItemAtPosition(position).toString();
//                 Log.d(TAG, "onItemSelected:"+item);
//                 if(item.equalsIgnoreCase("Account Type"))
//                 {
//                     Log.d(TAG, "onItemSelected:"+item);
//                     ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
//                             R.array.account_type, android.R.layout.simple_spinner_item);
//
//                     adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                     sub_category.setAdapter(adapter1);
//                 }else
//                 {
//                     Log.d(TAG, "onItemSelected:Else"+item);
//                     ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),R.array.customer, android.R.layout.simple_spinner_item);
//                     adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                     sub_category.setAdapter(adapter1);
//                 }
//
//             }
//
//             @Override
//             public void onNothingSelected(AdapterView<?> parent) {
//
//             }
//         });
    }

    private boolean isValidGst()
    {

        Matcher matcher = pattern.matcher(input_gst.getText().toString().trim());
        if(matcher.matches()) {
            return true;
        }
        else
            {
            return false;
        }
    }

    private void callToCustomer()
    {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else
        {
            callPhone();
        }
    }



    private void callPhone()
    {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + "7861998866"));


        if (ActivityCompat.checkSelfPermission(ShopRegistrationActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        startActivity(callIntent);
    }

    public void register_shop()
    {
        Log.d(TAG, "register_shop: ");
        custMobile=input_mobile_no.getText().toString();
        final String gst=input_gst.getText().toString();
//        String category=category_spinner.getSelectedItem().toString();
//        String subcategory=sub_category.getSelectedItem().toString();

       // final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_SHOP, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
             //   loading.dismiss();
                String resp = response.toString().trim();
                Log.d("Signup response", resp);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");

                    if (message.equalsIgnoreCase("Shop Added succesfully"))
                    {

                        _checkDiialog();
                        sendMessage();

//
                    }
                    else 
                    {

                        Toast.makeText(ShopRegistrationActivity.this, "Error in shop addition", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
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
               /// loading.dismiss();

                if (error instanceof NoConnectionError) {
                    Toast.makeText(ShopRegistrationActivity.this, "Please connect to the internet before continuing.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {

                Map<String, String> params = new LinkedHashMap<>();
                params.put(KEY_SNAME, input_name.getText().toString());
                params.put(KEY_ADDRESS,  input_address.getText().toString());
                params.put(KEY_DISTRICT,  input_district.getText().toString());
                params.put(KEY_STATE, input_state.getText().toString());
                params.put(KEY_PINCODE, input_pincode.getText().toString());
                params.put(KEY_MOBILE_NO, input_mobile_no.getText().toString());
                params.put(KEY_EMAILiD, input_email.getText().toString());
                params.put(KEY_PASSWORD,input_pass.getText().toString());
                params.put(KEY_SHOP_TYPE,  input_shoptype.getText().toString());
                params.put(KEY_SERVICE_TYPE,input_service_type.getText().toString());
                params.put(KEY_SHOP_OWNER, input_shop_owner.getText().toString());
                params.put(KEY_OWNER_MOBILE, input_owner_mobile.getText().toString());
                params.put(KEY_OWNER_EMAIL, input_owner_email.getText().toString());
                params.put(KEY_SHOP_ACT, input_shop_act.getText().toString());
                params.put(KEY_SHOP_DETAILS, input_shop_details.getText().toString());
                params.put("gstin_no",gst);
                Log.d("SParams _new ", params.toString());

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void _checkDiialog()
    {
        final Dialog dialog =new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sign_up_dialog);
        final AppCompatButton compatButton=(AppCompatButton)dialog.findViewById(R.id.bt_signupSuccessfully);
        final LinearLayout linearLayout=(LinearLayout)dialog.findViewById(R.id.ly_calltoCustomer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callPhone();
            }
        });
         compatButton.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View v)
             {
                  if(dialog!=null)
                  {
                      dialog.dismiss();
                  }
                     Toast.makeText(ShopRegistrationActivity.this, "Welcome to Mhourz, Let's get down to business.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ShopRegistrationActivity.this,SigninActivity.class);
                        startActivity(intent);
                        finish();

             }
         });
        dialog.show();
    }


    public void sendMessage()
    {
        String message1 = new String();

        String message = "Dear Customer, We have received your M Hourz account details, our team will approve your request in some time and will get back to you soon !\n" +
                " Thanks & Regards\nTeam M Hourz";
        String mobile = custMobile;
        try
        {
            message1 = URLEncoder.encode(message, "utf-8");
        } catch (UnsupportedEncodingException e) {

        }

        String sender = "SHMGMT";
        String route = "4";
        String country = "91";
        String json = "json";
        String uri = Uri.parse("http://api.msg91.com/api/sendhttp.php?")
                .buildUpon()
                .appendQueryParameter("authkey", "133779ATT6JFXy0k5850e783")
                .appendQueryParameter("mobiles", mobile)
                .appendQueryParameter("message", message1)
                .appendQueryParameter("sender", sender)
                .appendQueryParameter("route", route)
                .appendQueryParameter("country", country)
                .appendQueryParameter("response", json)
                .build().toString();


        final Context context = getApplicationContext();
        StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null) {
                                JSONObject j = new JSONObject(response);
                                String type1 = j.getString("type");
                                if (type1.contains("success"))
                                {
                                    sendMessageToOwner();
                                    Toast.makeText(getApplication(), " message sent successfully", Toast.LENGTH_LONG).show();
                                } else
                                {
                                    Toast.makeText(getApplication(), "Message couldn't reach you, try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void sendMessageToOwner()
    {
        String message1 = new String();

        String message = "Dear Customer, We have received new Account Registration Request, Please Check admin Panel and Aprove Id after Verifying Data  \n" +
                "User Name :"+custMobile + " Thanks & Regards\nTeam M Hourz";
        String mobile = "7020979072";
        try
        {
            message1 = URLEncoder.encode(message, "utf-8");
        } catch (UnsupportedEncodingException e) {

        }

        String sender = "SHMGMT";
        String route = "4";
        String country = "91";
        String json = "json";
        String uri = Uri.parse("http://api.msg91.com/api/sendhttp.php?")
                .buildUpon()
                .appendQueryParameter("authkey", "133779ATT6JFXy0k5850e783")
                .appendQueryParameter("mobiles", mobile)
                .appendQueryParameter("message", message1)
                .appendQueryParameter("sender", sender)
                .appendQueryParameter("route", route)
                .appendQueryParameter("country", country)
                .appendQueryParameter("response", json)
                .build().toString();


        final Context context = getApplicationContext();
        StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null) {
                                JSONObject j = new JSONObject(response);
                                String type1 = j.getString("type");
                                if (type1.contains("success"))
                                {
                                    sendMessageToOwner();
                                    Toast.makeText(getApplication(), " message sent successfully", Toast.LENGTH_LONG).show();
                                } else
                                {
                                    Toast.makeText(getApplication(), "Message couldn't reach you, try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }


    private boolean checkValidation() {
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
//        if (!Validation.hasText(input_shop_act)) ret = false;
//        if (!Validation.hasText(input_shop_details)) ret = false;


        return ret;
    }

}

package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.Activity;
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
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Billing_BankDetails99 extends AppCompatActivity
{

    private SharedPreferences sharedpreferences;
    private  final String MyPREFERENCES = "MyPrefs";
    private String dbname;

    private TextView txt_mobile,txt_mobile_vpa;
    private AppCompatButton  bt_next;
    private  String data;
    private static final String TAG = "Billing_BankDetails99";
    private SqlDataBase sqlDataBase;

    private TextInputEditText et_transactionId;


    private  String taxableValue,total_gst,shipping_charges,totalAmount,amount_paid,balanceDue,ModeofPayment,other_charges;
    private Context context=Billing_BankDetails99.this;
    private String custMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_99_detail_status);
        txt_mobile=(TextView)findViewById(R.id.mobile_number);
        txt_mobile_vpa=(TextView)findViewById(R.id.mobile_VPA);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));
        et_transactionId=(TextInputEditText)findViewById(R.id.et_transactionId_bankDetails);
        bt_next=(AppCompatButton)findViewById(R.id.bank_detailsPaymentDone99);

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               String id=et_transactionId.getText().toString().trim();
              //  sendMessage();
                if (id.length()>0)
                {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result","payment done");
                    returnIntent.putExtra("id",id);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
                else
                {
                    Toast.makeText(context, "please enter Id", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Intent intent=getIntent();
        if(intent.hasExtra("data"))
        {

            taxableValue=intent.getStringExtra("taxableValue");
            total_gst=  intent.getStringExtra("totalgst");
            custMobile=intent.getStringExtra("custMobile");
            other_charges = intent.getStringExtra("otherCharges");
            shipping_charges= intent.getStringExtra("shippingCharges");
            amount_paid= intent.getStringExtra("amountpaid");
            balanceDue= intent.getStringExtra("balanceDue");
            ModeofPayment= intent.getStringExtra("totalAmount");
            totalAmount= intent.getStringExtra("totalAmount");
            ModeofPayment= intent.getStringExtra("modeofPayment");
            data=intent.getStringExtra("data");
            dbname=intent.getStringExtra("dbname");


            Log.d(TAG, "onCreate() called with: savedInstanceState = [" + data + "]");
        }

        sqlDataBase=new SqlDataBase(context);

        getBankDetail();
    }


    private void getBankDetail()
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETPAYTMDEATLS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {


                String resp = response.toString().trim();
                loading.dismiss();
                Log.d("Bank Deatls",resp);
                JSONObject jsonObject= null;
                try
                {
                    JSONObject json = new JSONObject(resp);
                    JSONArray jsonArray=json.getJSONArray("records");
                    jsonObject=jsonArray.getJSONObject(0);
                    String bankname =jsonObject.getString("mobile_no");
                    String  detais=jsonObject.getString("details");
                    txt_mobile.setText(bankname);
                    txt_mobile_vpa.setText(detais);





                } catch (JSONException e)
                {
                    Log.d("Exception",""+e);
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();

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
                params.put("dbname", dbname);
                params.put("type","99");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }



//    public void sendMessage()
//    {
//        String message1 = new String();
//
//        String message = "Dear Custome \n Thanks for Your Business \n Your Total Amount :"+totalAmount +" \n Invoice No :"+invId +"\n  please Visit us again ! \n shop name :";
//        String mobile = custMobile;
//        try
//        {
//            message1 = URLEncoder.encode(message, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//
//        }
//
//        String sender = "SHMGMT";
//        String route = "4";
//        String country = "91";
//        String json = "json";
//        String uri = Uri.parse("http://api.msg91.com/api/sendhttp.php?")
//                .buildUpon()
//                .appendQueryParameter("authkey", "133779ATT6JFXy0k5850e783")
//                .appendQueryParameter("mobiles", mobile)
//                .appendQueryParameter("message", message1)
//                .appendQueryParameter("sender", sender)
//                .appendQueryParameter("route", route)
//                .appendQueryParameter("country", country)
//                .appendQueryParameter("response", json)
//                .build().toString();
//
//
//        final Context context = getApplicationContext();
//        StringRequest stringRequest = new StringRequest(uri,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            if (response != null) {
//                                JSONObject j = new JSONObject(response);
//                                String type1 = j.getString("type");
//                                if (type1.contains("success"))
//                                {
//                                    Toast.makeText(getApplication(), " message sent successfully", Toast.LENGTH_LONG).show();
//                                } else
//                                {
//                                    Toast.makeText(getApplication(), "Message couldn't reach you, try again", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        } catch (JSONException e) {
//
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(stringRequest);
//    }








}

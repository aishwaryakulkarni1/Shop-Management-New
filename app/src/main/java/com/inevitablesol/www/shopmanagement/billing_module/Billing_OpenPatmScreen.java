package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.inevitablesol.www.shopmanagement.settings.Billing_Settings;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Billing_OpenPatmScreen extends AppCompatActivity implements View.OnClickListener
{


    private SqlDataBase sqlDataBase;
    private static final String TAG = "Billing_OpenPatmScreen";
    private AppCompatButton paytmData,bt_paytmFail;
    private SharedPreferences sharedpreferences;
    private  final String MyPREFERENCES = "MyPrefs";
    private String dbname;

    private TextView txt_mobile,txt_mobile_vpa;
    private final static int PAYTMCODE = 110;
    private  ImageView imageView;

    private GlobalPool globalPool;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_paytm_details);
        sqlDataBase=new SqlDataBase(this);
        Log.d(TAG, "onCreate Paytm : ");
        
        paytmData=(AppCompatButton)findViewById(R.id.paytmSuccess);
         txt_mobile=(TextView)findViewById(R.id.mobile_number);
         txt_mobile_vpa=(TextView)findViewById(R.id.mobile_VPA);


        paytmData.setOnClickListener(this);
        bt_paytmFail=(AppCompatButton)findViewById(R.id.paytmFailure);
        bt_paytmFail.setOnClickListener(this);
        globalPool = (GlobalPool) this.getApplication();
       // Bitmap bitmap=StringToBitMap(image);
         imageView = (ImageView) findViewById(R.id.view_paytmScreen);
       // imageView.setImageBitmap(bitmap);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));


    }


    public Bitmap StringToBitMap(String encodedString)
    {
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e)
        {
            e.getMessage();
            return null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bank_details, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.new_bankdetails:
                Intent intent = new Intent(Billing_OpenPatmScreen.this, Billing_Settings.class);
                startActivityForResult(intent, PAYTMCODE);
                Toast.makeText(getApplicationContext(), "Please Add PayTm Details", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.home:
                Toast.makeText(this, "Please complete Process", Toast.LENGTH_SHORT).show();
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    protected void onResume() 
    {
        Log.d(TAG, "onResume: ");
        super.onResume();
        String image=sqlDataBase.getPaytmAppScreen();
        Bitmap bitmap=StringToBitMap(image);
        imageView.setImageBitmap(bitmap);
        getBankDetail();
        getUserInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYTMCODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK)
            {
                Log.d(TAG, "onActivityResult: ");

            }
        }
    }



    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.paytmSuccess:

//                Intent intent = new Intent(Billing_OpenPatmScreen.this, Paytm_payment_Status.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","payment done");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                break;
            case R.id.paytmFailure:
                Intent returnIntent1 = new Intent();
                returnIntent1.putExtra("result","payment fail");
                setResult(Activity.RESULT_OK,returnIntent1);
                finish();
                break;

        }

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
                    Toast.makeText(getApplicationContext(), "Please Add PayTm Details", Toast.LENGTH_SHORT).show();
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);

                params.put("type","paytm");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getUserInfo()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.SHOP_DOCUMENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                Log.d(TAG, "onResponse: Images " + response);
                JSONObject msg = null;
                try
                {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=         jsonObject.getJSONArray("documentList");
                    JSONObject jsonObject1=       jsonArray.getJSONObject(0);
                    Log.d(TAG, "onResponse: Link"+jsonObject1.getString("ImageLink"));
                    Picasso.with(Billing_OpenPatmScreen.this).load(jsonObject1.getString("ImageLink")).fit().centerInside().into(imageView);



                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(Billing_OpenPatmScreen.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", globalPool.getDbname());
                params.put("dbname", globalPool.getDbname());
                params.put("shopId",globalPool.getShopId());
                params.put("type","Paytm");


                Log.d(TAG, "getParams:BankDetails" + params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

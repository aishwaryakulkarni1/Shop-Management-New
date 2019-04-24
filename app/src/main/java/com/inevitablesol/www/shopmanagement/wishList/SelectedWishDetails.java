package com.inevitablesol.www.shopmanagement.wishList;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectedWishDetails extends AppCompatActivity implements View.OnClickListener {

    private TextView et_custName,et_mobileNumber,et_emailId,et_description;
    private  TextView txt_stillDate,txt_remainder,et_qty,txt_status;
    private RecyclerView recyclerView;

    private  AppCompatButton bt_save;
    private String dbname;
    private String id;
    private JSONArray itemData;
    private WishListAdapterListView selectedItemAdapter;
    private ArrayList<WishListItems_pojo> iLists;

    private ImageView bt_sms,bt_mail,bt_call;
    private AppCompatButton bt_done,bt_cancel;

    private String mobile,email;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private String Status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_list_details);
        et_custName = (TextView) findViewById(R.id.wish_view_name);
        et_mobileNumber = (TextView) findViewById(R.id.wish_view_number);
        et_emailId = (TextView) findViewById(R.id.wish_view_email);
        txt_stillDate = (TextView) findViewById(R.id.wish_view_tilldate);
        txt_remainder = (TextView) findViewById(R.id.wish_view_remainder);
         bt_done=(AppCompatButton)findViewById(R.id.wish_done);
        txt_status=(TextView)findViewById(R.id.wish_changedStatus);
          bt_done.setOnClickListener(this);
          bt_cancel=(AppCompatButton)findViewById(R.id.wish_cancel);
          bt_cancel.setOnClickListener(this);
         bt_sms= (ImageView) findViewById(R.id.wish_sms);
          bt_sms.setOnClickListener(this);
        bt_mail= (ImageView) findViewById(R.id.wish_mail);
        bt_mail.setOnClickListener(this);
        bt_call= (ImageView) findViewById(R.id.wish_call);
        bt_call.setOnClickListener(this);

        et_qty = (TextView) findViewById(R.id.wish_view_qty);

        recyclerView = (RecyclerView) findViewById(R.id.wish_view_Recycler);

//        bt_save = (AppCompatButton) findViewById(R.id.add_to_wishList);
//        bt_save.setOnClickListener(this);
        et_description = (TextView) findViewById(R.id.wish_view_description);


        Intent intent=getIntent();
        if(intent.hasExtra("id"))
        {
           id=intent.getStringExtra("id");
            et_custName.setText(intent.getStringExtra("name"));
            mobile=intent.getStringExtra("mobile");
            et_mobileNumber.setText(mobile);
            email=intent.getStringExtra("email");
            et_emailId.setText(email);
            et_description.setText(intent.getStringExtra("description"));
           txt_stillDate.setText(intent.getStringExtra("tillDate"));
           txt_remainder.setText(intent.getStringExtra("reminder"));
            Status=intent.getStringExtra("status");
            Log.d(TAG, "onCreate:Status"+Status);
            txt_status.setText(Status);
            et_qty.setText(intent.getStringExtra("totalQty"));
            dbname=intent.getStringExtra("dbname");
            getItemDetails(id,dbname);

            if(Status.equalsIgnoreCase("Completed"))
            {
                bt_done.setEnabled(false);
            }

            if(Status.equalsIgnoreCase("Canceled"))
            {
                bt_done.setEnabled(false);
            }

        }
    }

    @Override
    public void onClick(View v)
    {

         switch (v.getId())
         {
             case R.id.wish_done:
                 String status="Completed";
                 if(Status.equalsIgnoreCase(status))
                 {
                     Toast.makeText(this, "Already Completed", Toast.LENGTH_SHORT).show();
                 }else
                 {

                     statusChanged(status);
                 }

                 break;
             case R.id.wish_cancel:
                 String statusC="Canceled";
                 if(Status.equalsIgnoreCase(statusC))
                 {
                     Log.d(TAG, "onClick() called with: v = [" + Status + "]");
                     Toast.makeText(this, "Already Canceled", Toast.LENGTH_SHORT).show();

                 }else
                 {
                     Log.d(TAG, "onClick() called with: v = [" + Status + "]");
                     statusChanged(statusC);
                 }
                 break;
             case R.id.wish_call:
                 callToCustomer();
                 break;
             case R.id.wish_mail:
                 sendMail();
                 break;
             case R.id.wish_sms:
                 sendSms();
                 break;
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
        callIntent.setData(Uri.parse("tel:" + mobile));


        if (ActivityCompat.checkSelfPermission(SelectedWishDetails.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        startActivity(callIntent);
    }



    private void sendSms()
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mobile));
        intent.putExtra("sms_body", "hi");
        startActivity(intent);
    }

    private void sendMail()
    {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, " ");
        i.putExtra(Intent.EXTRA_TEXT   , "  ");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(SelectedWishDetails.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void statusChanged(final String status)
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_Wish_STATUSDONE, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try
                {
                    Log.d("response",response);

                    try {
                        Log.d("response",response);
                        JSONObject msg = new JSONObject(response);
                        String message = msg.getString("message");
                        if(message.equalsIgnoreCase("Wish Updated"))
                        {
                            Intent intent=new Intent(SelectedWishDetails.this,View_WishList.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                    }catch (JSONException e)
                    {

                    }

                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(SelectedWishDetails.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("wishid",id);
                params.put("status",status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getItemDetails(final String expId, final String dbname)
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_Wish_BY_P_ID, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try
                {
                    Log.d("response",response);
                    JSONObject jsonObject=new JSONObject(response);
                    itemData=jsonObject.getJSONArray("records");
                    Log.d(TAG, "onResponse: Item Data"+itemData);
                    displayData(itemData);



                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(SelectedWishDetails.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("dbname", dbname);
                params.put("wishid",expId);
                Log.d("wishListId", params.toString());
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private static final String TAG = "SelectedWishDetails";

    private void displayData(JSONArray itemData)
    {

        Log.d(TAG, "displayItem:"+itemData);
        iLists=new ArrayList<>();
        for(int i=0;i<itemData.length();i++)
        {
            try
            {
                WishListItems_pojo itemList=new WishListItems_pojo();
                JSONObject jsonObject =itemData.getJSONObject(i);


                itemList.setName(jsonObject.getString("name"));
                itemList.setQty(jsonObject.getString("qty"));
                itemList.setCompany(jsonObject.getString("company"));

                iLists.add(itemList);


            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }

        selectedItemAdapter  = new WishListAdapterListView(iLists, SelectedWishDetails.this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(selectedItemAdapter);
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}

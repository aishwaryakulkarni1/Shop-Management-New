package com.inevitablesol.www.shopmanagement.vendor_module;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.DateFormat.ParseDate;
import com.inevitablesol.www.shopmanagement.R;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.Validation.DatePicker_Class;
import com.inevitablesol.www.shopmanagement.wishList.DatePick;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.validation.ValidatorHandler;

public class SelectedVendorDetails extends AppCompatActivity implements View.OnClickListener

{
    private static final String DELETE_VENDOR = "http://35.161.99.113:9000/webapi/vendor/delete";

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private TextView txt_address,txt_name,txt_email,txt_mobile;
    private  TextView txt_id,txt_gst_status,txt_gst_number,txt_created_by,txt_created_Date,txt_contactPerson,txt_shopname;
    private  int id;
    AppCompatButton remove_vendor;
    private String dbname;
    private SharedPreferences sharedpreferences;
    private ImageView img_call,img_sms,img_mail;
    private  String mobile,email;
    private static final String TAG = "SelectedVendorDetails";
    private String StateCode;
    private String state;

     private TextView  text_placeOfSupply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_vendor_details);
         txt_name=(TextView)findViewById(R.id.txt_view_vendorName);
        txt_shopname=(TextView)findViewById(R.id.txt_view_shopName);
          txt_address=(TextView)findViewById(R.id.txt_view_v_address);
          txt_email=(TextView)findViewById(R.id.txt_view_v_email);
          txt_mobile=(TextView)findViewById(R.id.txt_view_v_contact);
          txt_id=(TextView)findViewById(R.id.txt_view_v_id);
          txt_created_by=(TextView)findViewById(R.id.txt_view_v_CreatedBy);
          txt_created_Date=(TextView)findViewById(R.id.txt_view_v_CreatedDate);
          txt_gst_number=(TextView)findViewById(R.id.txt_view_v_GSTNO);
            txt_gst_status=(TextView)findViewById(R.id.txt_view_v_GstStatus);
           txt_contactPerson=(TextView)findViewById(R.id.txt_view_v_contactPerson);
           remove_vendor=(AppCompatButton)findViewById(R.id.remove_vendor);
         text_placeOfSupply=(TextView)findViewById(R.id.txt_view_placeofsuppier);

         img_call=(ImageView)findViewById(R.id.v_call);
          img_mail=(ImageView)findViewById(R.id.v_mail);
          img_sms=(ImageView)findViewById(R.id.v_sms);


           img_mail.setOnClickListener(this);
           img_call.setOnClickListener(this);
             img_sms.setOnClickListener(this);
         remove_vendor.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                 showWarningMessage();

             }
         });
        Intent intent=getIntent();
        if(intent.hasExtra("v_name"))
        {
           String shopname=intent.getStringExtra("shop_name");
            String name=intent.getStringExtra("v_name");
             mobile=intent.getStringExtra("v_mobile");
            String address=intent.getStringExtra("v_address");
             email=intent.getStringExtra("v_email");
            String gst_status=intent.getStringExtra("gststatus");
             String gst_In=intent.getStringExtra("gst_number");
             String created_by=intent.getStringExtra("created_by");
              String created_date=intent.getStringExtra("created_date");
              String  contact_person =intent.getStringExtra("contact_person");
                      StateCode=intent.getStringExtra("stateCode");
                         dbname=intent.getStringExtra("dbname");
                         state=intent.getStringExtra("state");
                id=intent.getIntExtra("v_id",0);
            txt_name.setText(name);
            txt_address.setText(address);
             txt_email.setText(email);
            txt_mobile.setText(mobile);
             txt_id.setText(String.valueOf(id));
              txt_created_Date.setText(ParseDate.geDate(created_date));
             txt_created_by.setText(created_by);
             txt_gst_status.setText(gst_status);
             txt_gst_number.setText(gst_In);
            txt_contactPerson.setText(contact_person);
            txt_shopname.setText(shopname);
            text_placeOfSupply.setText(state);

            Log.d(TAG, "onCreate:StateCode"+StateCode);
            Log.d(TAG, "onCreate: GST Status"+gst_status);



        }else
        {

        }
    }

    private void showWarningMessage()
    {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_dialog);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Do you want to Remove Vendor?");

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                deleteVendor();
                dialog.dismiss();
//                Intent intent = new Intent(SelectedVendorDetails.this, SigninActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                dialog.dismiss();


            }
        });

        Button cancelDialog = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        dialog.show();


    }


    private void deleteVendor()
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Updating...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_VENDOR, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();
                try
                {
                    loading.dismiss();
                    JSONObject jsonObject=new JSONObject(resp);
                    String message=jsonObject.getString("message");
                    if(message.equalsIgnoreCase("Delete data succesfully."))

                    {
                        loading.dismiss();
                        Intent intent=new Intent();
                        intent.putExtra("DELETED",message);
                        setResult(Activity.RESULT_OK,intent);
                        finish();
//


                    }else
                    {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }



                }catch (JSONException e)
                {

                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if(error instanceof NoConnectionError)
                    Toast.makeText(SelectedVendorDetails.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("vendor_id", String.valueOf(id));


                return   params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case  R.id.v_call:
                 callToCustomer();
                 break;
            case  R.id.v_mail:
                     sendMail();
                 break;
            case R.id.v_sms:
                sendSms();
                break;

           }
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
            Toast.makeText(SelectedVendorDetails.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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


            if (ActivityCompat.checkSelfPermission(SelectedVendorDetails.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            startActivity(callIntent);
        }
    }




package com.inevitablesol.www.shopmanagement.customer_module;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.inevitablesol.www.shopmanagement.vendor_module.SelectedVendorDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SelectedCustomerDetails extends AppCompatActivity implements View.OnClickListener
{

    private static final String DELETE_CUSTOMER = "http://35.161.99.113:9000/api/customer/delete";
    private TextView txt_address,txt_name,txt_email,txt_mobile;
    private  TextView txt_home_Deliver;
    private AppCompatButton bt_Delete_Button;
    private  String id;
    private String dbname;

    private  String mobile,email;

    private ImageView img_call,img_sms,img_mail;
    private  TextView txt_placeofSupply,txt_gst;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_customer_details);
        txt_name=(TextView)findViewById(R.id.txt_view_CustonerName);
        txt_address=(TextView)findViewById(R.id.txt_view_c_address);
        txt_email=(TextView)findViewById(R.id.txt_view_c_email);
        txt_mobile=(TextView)findViewById(R.id.txt_view_c_contact);
         txt_home_Deliver=(TextView)findViewById(R.id.txt_view_home_delivery);
         bt_Delete_Button=(AppCompatButton)findViewById(R.id.bt_removeCustomer);
         bt_Delete_Button.setOnClickListener(this);
         txt_placeofSupply=(TextView)findViewById(R.id.txt_view_placeofsuppier);
         txt_gst=(TextView)findViewById(R.id.txt_view_gst) ;


        img_call=(ImageView)findViewById(R.id.v_call);
        img_mail=(ImageView)findViewById(R.id.v_mail);
        img_sms=(ImageView)findViewById(R.id.v_sms);


        img_mail.setOnClickListener(this);
        img_call.setOnClickListener(this);
        img_sms.setOnClickListener(this);


        Intent intent=getIntent();


        if(intent.hasExtra("c_name"))
        {

            String name=intent.getStringExtra("c_name");
             mobile=intent.getStringExtra("c_mobile");
            String address=intent.getStringExtra("c_address");
            email=intent.getStringExtra("c_email");
             dbname=intent.getStringExtra("dbname");
            id=intent.getStringExtra("c_id");
            String status=intent.getStringExtra("delevery_status");
              txt_gst.setText(intent.getStringExtra("gst"));
              txt_placeofSupply.setText(intent.getStringExtra("supplier"));
            txt_name.setText(name);
            txt_address.setText(address);
            txt_email.setText(email);
            txt_mobile.setText(mobile);
            txt_home_Deliver.setText(status);
        }else
        {

        }
    }

    @Override
    public void onClick(View v)
    {
        int id=v.getId();
        switch (id)
        {

            case R.id.bt_removeCustomer:
                      ShowDialog();
                   break;
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

    private void ShowDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_dialog);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Do you want to Remove Customer?");

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                deleteCustomer();
                dialog.dismiss();
            }
        });

        Button cancelDialog = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    private void deleteCustomer()
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Updating...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.DELETE_CUSTOMER, new Response.Listener<String>()
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
                         Intent intent=new Intent();
                        intent.putExtra("DELETED",message);
                        setResult(Activity.RESULT_OK,intent);
                        finish();
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
                    Toast.makeText(SelectedCustomerDetails.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("customer_id",id);

                return   params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
            Toast.makeText(SelectedCustomerDetails.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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


        if (ActivityCompat.checkSelfPermission(SelectedCustomerDetails.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        startActivity(callIntent);
    }
}

package com.inevitablesol.www.shopmanagement.account;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.vendor_module.SelectedVendorDetails;
import com.payu.custombrowser.CustomBrowserMain;
import com.squareup.picasso.Picasso;

public class View_User_Deatil extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "View_User_Deatil";
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE =101 ;
    private TextInputEditText user_name,user_email,user_dateofbirth,user_mobile,user_empcode,user_createdOn,user_createdby;
    private TextInputEditText user_passWord;
    private TextInputEditText user_role;
    private  Alluser alluser;
    private ImageView img_call,img_sms,img_mail;
    private GlobalPool globalPool;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__user__deatil);
         user_name=(TextInputEditText)findViewById(R.id.u_name);
         globalPool=(GlobalPool)this.getApplicationContext();
         user_role=(TextInputEditText)findViewById(R.id.et_userType);
         imageView=(ImageView)findViewById(R.id.user_pic);
         user_email=(TextInputEditText)findViewById(R.id.u_email);
         user_empcode=(TextInputEditText)findViewById(R.id.u_code);
         user_mobile=(TextInputEditText)findViewById(R.id.u_mobile);
         user_createdOn=(TextInputEditText)findViewById(R.id.u_createdOn);
         user_createdby=(TextInputEditText)findViewById(R.id.u_creatdby);
         img_call=(ImageView)findViewById(R.id.u_call);
         img_call.setOnClickListener(this);
        img_sms=(ImageView)findViewById(R.id.u_sms);
        img_sms.setOnClickListener(this);
        img_mail=(ImageView)findViewById(R.id.u_mail);
        img_mail.setOnClickListener(this);



        Intent intent=getIntent();
                  Bundle bundle=      intent.getExtras();
               alluser= (Alluser) bundle.getSerializable("info");
             Log.d(TAG, "onCreate: "+alluser.toString());

             user_mobile.setText(alluser.getUNumber());
             user_name.setText(alluser.getUName());
             user_empcode.setText(alluser.getEcode());
             user_email.setText(alluser.getUEmail());
             user_role.setText(alluser.getURole());
             user_createdOn.setText(alluser.getCreatedOn());
             user_createdby.setText(globalPool.getUsername());
             if(!alluser.getLink().isEmpty())
             {
                 try {
                     Picasso.with(this).load(alluser.getLink()).fit().centerInside().into(imageView);
                 } catch (Exception e)
                 {
                     e.printStackTrace();
                 }

             }else {
                 imageView.setImageBitmap(null);
             }
    }



    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case  R.id.u_call:
                callToCustomer();
                break;
            case  R.id.u_mail:
                sendMail();
                break;
            case R.id.u_sms:
                sendSms();
                break;

        }
    }

    private void sendSms()
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + alluser.getUNumber()));
        intent.putExtra("sms_body", "hi");
        startActivity(intent);
    }

    private void sendMail()
    {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{alluser.getUEmail()});
        i.putExtra(Intent.EXTRA_SUBJECT, " ");
        i.putExtra(Intent.EXTRA_TEXT   , "  ");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(View_User_Deatil.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
        callIntent.setData(Uri.parse("tel:" + alluser.getUNumber()));


        if (ActivityCompat.checkSelfPermission(View_User_Deatil.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        startActivity(callIntent);
    }
}

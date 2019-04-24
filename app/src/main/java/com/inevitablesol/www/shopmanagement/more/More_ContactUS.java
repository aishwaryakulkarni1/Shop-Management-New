package com.inevitablesol.www.shopmanagement.more;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.vendor_module.SelectedVendorDetails;

public class More_ContactUS extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more__contact_us);
        TextView textView=(TextView)findViewById(R.id.phoneNumber);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callToCustomer();
            }
        });
        final TextView saletxt=(TextView)findViewById(R.id.saleteam);
        saletxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sendMail(saletxt.getText().toString().trim());
            }
        });

        final TextView info_txt=(TextView)findViewById(R.id.infoteam);
        info_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail(info_txt.getText().toString().trim());
            }
        });

        final TextView team_txt=(TextView)findViewById(R.id.team);
        team_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail(team_txt.getText().toString().trim());
            }
        });
        final TextView support_txt=(TextView)findViewById(R.id.supportteam);
        support_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail(support_txt.getText().toString().trim());
            }
        });
    }

    private void sendMail(String email)
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
            Toast.makeText(More_ContactUS.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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


        if (ActivityCompat.checkSelfPermission(More_ContactUS.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        startActivity(callIntent);
    }
}

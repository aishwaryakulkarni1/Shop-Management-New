package com.inevitablesol.www.shopmanagement.vendor_module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.inevitablesol.www.shopmanagement.R;

public class VendorActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_addVendor;
    ImageView viewVendor,editVedor;
    private static final String TAG = "VendorActivity";
     private Context context=VendorActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_home);
       // getLayoutInflater().inflate(R.layout.vendor_home);
         img_addVendor=(ImageView)findViewById(R.id.addVendor);
         img_addVendor.setOnClickListener(this);
        viewVendor=(ImageView)findViewById(R.id.viewVendor);
          viewVendor.setOnClickListener(this);
          editVedor=(ImageView)findViewById(R.id.editVendor);
           editVedor.setOnClickListener(this);


    }

    @Override
    public void onClick(View v)
    {
        int id=v.getId();
        switch (id)
        {
            case R.id.addVendor:
                //Toast.makeText(context,"add Vendor",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(VendorActivity.this,AddVendor.class);
                startActivity(intent);
                 break;

            case R.id.viewVendor:
               // Toast.makeText(context,"View Vendor",Toast.LENGTH_LONG).show();
                Intent intent1=new Intent(VendorActivity.this,View_Vendor.class);
                startActivity(intent1);
                break;

            case R.id.editVendor:
              //  Toast.makeText(context,"Edit Vendor",Toast.LENGTH_LONG).show();
               startActivity(new Intent(VendorActivity.this,EditVendorActivity.class));
                break;

        }

    }
}

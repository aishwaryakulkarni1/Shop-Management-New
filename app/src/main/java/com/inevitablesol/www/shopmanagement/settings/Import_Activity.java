package com.inevitablesol.www.shopmanagement.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.inevitablesol.www.shopmanagement.R;

public class Import_Activity extends AppCompatActivity
{

    private LinearLayout li_VendorList,li_customerDeatils,li_itemList,li_menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_);
        li_VendorList=(LinearLayout)findViewById(R.id.ll_imp_vendorList);
        li_customerDeatils=(LinearLayout)findViewById(R.id.li_custImport);
        li_itemList=(LinearLayout)findViewById(R.id.li_itemList) ;

        li_menuItem=(LinearLayout)findViewById(R.id.li_menuItem) ;
        li_VendorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Import_Activity.this,Import_VendorList.class));
            }
        });

        li_customerDeatils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Import_Activity.this,Import_Customer.class));
            }
        });

        li_itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Import_Activity.this,Import_Item.class));
            }
        });

        li_menuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Import_Activity.this,Import_MenuItem.class));
            }
        });

    }
}

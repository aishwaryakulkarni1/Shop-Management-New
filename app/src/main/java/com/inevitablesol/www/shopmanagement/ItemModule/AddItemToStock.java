package com.inevitablesol.www.shopmanagement.ItemModule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

public class AddItemToStock extends AppCompatActivity
{
    TextView txt_View_itemName,txt_qty,txt_created_by,txt_modified_by,txt_product_name,txt_mrp;
    TextView txt_price,txt_specification;
    private static final String TAG = "AddItemToStock";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item_from_stock);
        txt_View_itemName=(TextView)findViewById(R.id.txt_view_ItemName);
        txt_product_name=(TextView)findViewById(R.id.txt_view_ProductType);
        txt_qty=(TextView)findViewById(R.id.txt_view_item_Quantity);

        txt_price=(TextView)findViewById(R.id.txt_view_itemPrice);
        txt_specification=(TextView)findViewById(R.id.txt_view_itemSpecification);

        Intent intent=getIntent();
        if(intent.hasExtra("item_name"))
        {

                  String name=  intent.getStringExtra("item_name");
                  String product_type=intent.getStringExtra("product_type");
                  String qty=intent.getStringExtra("qty");
                   String original_price=intent.getStringExtra("original_price");
                   String specification=intent.getStringExtra("specification");
                   String mrp=intent.getStringExtra("mrp");
                   String created_by=intent.getStringExtra("hsn_ssc_code");
                   String modified_by=intent.getStringExtra("modified_by");
                      txt_View_itemName.setText(name);
                      txt_product_name.setText(product_type);
                      txt_qty.setText(qty);
                     txt_price.setText(mrp);
                     txt_modified_by.setText(modified_by);
                     txt_created_by.setText(created_by);
                     txt_specification.setText(specification);
        }else
        {

        }




    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume:");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed:");
    }
}

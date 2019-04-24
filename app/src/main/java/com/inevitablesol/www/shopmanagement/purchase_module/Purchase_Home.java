package com.inevitablesol.www.shopmanagement.purchase_module;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.product_info.ProductActivity;
import com.inevitablesol.www.shopmanagement.vendor_module.VendorActivity;

public class Purchase_Home extends BaseActivity implements View.OnClickListener
{

    private ImageView addItem, addnewProduct, viewItem, viewpurchaseStock,vendorActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_purchase__home, frameLayout);
        mDrawerList.setItemChecked(position, true);
        addItem = (ImageView) findViewById(R.id.addPurchaseItem);
        viewItem = (ImageView) findViewById(R.id.viewPurchaseItem);
        viewpurchaseStock = (ImageView) findViewById(R.id.purchaseStock);
        addnewProduct=(ImageView)findViewById(R.id.add_newProduct);
        vendorActivity=(ImageView)findViewById(R.id.vendorActivity);

        addItem.setOnClickListener(this);
        viewItem.setOnClickListener(this);
        viewItem.setOnClickListener(this);
        viewpurchaseStock.setOnClickListener(this);
        addnewProduct.setOnClickListener(this);
        mDrawerList.setItemChecked(position, true);
        vendorActivity.setOnClickListener(this);
        String logCheck =listArray[position];
        if (logCheck.equalsIgnoreCase("Logout"))
        {
//            setTitle(listArray[0]);
            toolbar.setTitle(listArray[0]);
        }else
        {

//            setTitle(listArray[position]);
            toolbar.setTitle(listArray[position]);
        }


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.addPurchaseItem:
                startActivity(new Intent(Purchase_Home.this, AddPurchaseItem.class));
                break;
            case R.id.viewPurchaseItem:
                startActivity(new Intent(Purchase_Home.this, PurchaseView.class));
                break;

            case R.id.purchaseStock:
                startActivity(new Intent(Purchase_Home.this, Purchase_Stock_Storage.class));
                break;
            case R.id.add_newProduct:
                 startActivity(new Intent(Purchase_Home.this, ProductActivity.class));
                break;
            case R.id.vendorActivity:
                startActivity(new Intent(Purchase_Home.this, VendorActivity.class));

                break;


        }

    }
}

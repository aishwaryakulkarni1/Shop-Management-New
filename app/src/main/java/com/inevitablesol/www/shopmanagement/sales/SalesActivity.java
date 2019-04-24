package com.inevitablesol.www.shopmanagement.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.R;


import java.util.ArrayList;

public class SalesActivity extends BaseActivity implements View.OnClickListener {
    private static final String GET_SALSE ="http://35.161.99.113:9000/api/sale/TotalSales" ;
    private ImageView img_Total_sale;

    private  ImageView img_customerDetail;
    private  ImageView product_type,modeOfpayment;
    private  ImageView img_sale_DateTime,img_saleUser;

    private   ArrayList<SaleInfo> saleInfos;
    private RecyclerView recyclerView;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getLayoutInflater().inflate(R.layout.activity_sales, frameLayout);

            mDrawerList.setItemChecked(position, true);
            toolbar.setTitle(listArray[position]);
          img_Total_sale=(ImageView)findViewById(R.id.img_total_salse);
          img_customerDetail=(ImageView)findViewById(R.id.img_customerDetail);
          img_customerDetail.setOnClickListener(this);
         img_saleUser=(ImageView)findViewById(R.id.img_saleUser);
          img_saleUser.setOnClickListener(this);
         recyclerView = (RecyclerView)findViewById(R.id.stock_recyclerView);

         modeOfpayment=(ImageView)findViewById(R.id.img_modeOfpayment);
         modeOfpayment.setOnClickListener(this);
         img_Total_sale.setOnClickListener(this);
        img_sale_DateTime=(ImageView)findViewById(R.id.img_sale_DateTime);
         img_sale_DateTime.setOnClickListener(this);
          product_type=(ImageView)findViewById(R.id.product_type);
           product_type.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id=v.getId();
        switch (id)
        {
              case R.id.img_total_salse:
                  Intent intent=new Intent(SalesActivity.this,Sales_TotalSales.class);
                  startActivity(intent);
                break;
            case R.id.img_customerDetail:
                Intent intent2=new Intent(SalesActivity.this,Sales_CustomerActivity.class);
                 startActivity(intent2);
                break;
            case R.id.img_modeOfpayment:
                Intent intent1=new Intent(SalesActivity.this,Sales_ModeOfPayment.class);
                startActivity(intent1);
                 break;
            case R.id.product_type:
                Intent intent3=new Intent(SalesActivity.this,Sales_ProductType.class);
                startActivity(intent3);
                break;

            case R.id.img_sale_DateTime:
                Intent intent5=new Intent(SalesActivity.this,Sales_DateTime.class);
                startActivity(intent5);
                break;
            case R.id.img_saleUser:
                 startActivity(new Intent(SalesActivity.this,Sale_Users.class));
                break;




        }

    }




}

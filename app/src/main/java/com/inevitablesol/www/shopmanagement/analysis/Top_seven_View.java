package com.inevitablesol.www.shopmanagement.analysis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;


public class Top_seven_View extends AppCompatActivity
{
    private static final String TAG="Top_seven_View";

    private LinearLayout txt_highestSaletoCust,lv_repetativeDay,linear_ProductSale;
    private LinearLayout txt_topSellingDay;
    private LinearLayout txt_purchaseDay,txt_highestCustomerDay;

    private TextView txt_toDate,txt_fromdate;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_analysis);
//        txt_toDate=(TextView)findViewById(R.id.txt_toDate);
        txt_fromdate=(TextView)findViewById(R.id.txt_fromdate);


        txt_topSellingDay=(LinearLayout)findViewById(R.id.txt_hight_sellingDay);
        txt_purchaseDay=(LinearLayout)findViewById(R.id.textView_purchaseDay);
        txt_highestCustomerDay=(LinearLayout)findViewById(R.id.txt_highestCustomerDay) ;
        txt_highestSaletoCust=(LinearLayout)findViewById(R.id.txt_saleToCustomer);

        lv_repetativeDay=(LinearLayout)findViewById(R.id.repetative_customer);

        linear_ProductSale=(LinearLayout)findViewById(R.id.linear_productSale);

        final Intent intent=getIntent();
        if(intent.hasExtra("from_date"))
        {
          String fromDate=   intent.getStringExtra("from_date");
            id=  intent.getStringExtra("id");
             txt_fromdate.setText(fromDate);
            Log.d(TAG, "onCreate: From Date"+fromDate);
            Log.d(TAG, "onCreate: Id"+id);
//            txt_toDate.setText(toDate);

            //getTotalSale_Status();
        }


        linear_ProductSale.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(Top_seven_View.this, SellingProduct.class));

            }
        });

        lv_repetativeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent1=new Intent(Top_seven_View.this,RepetativeCustomer.class);
                intent1.putExtra("startDate",txt_fromdate.getText().toString().trim());
                intent1.putExtra("id",id);
                startActivity(intent1);
            }
        });

        txt_highestSaletoCust.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Top_seven_View.this,HighestSaleToCustomer.class);
                intent.putExtra("startDate",txt_fromdate.getText().toString().trim());
                intent.putExtra("id",id);
                startActivity(intent);

            }
        });

        txt_highestCustomerDay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Top_seven_View.this,HighestCustomerDay.class);
                intent.putExtra("startDate",txt_fromdate.getText().toString().trim());
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        txt_topSellingDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent1=new Intent(Top_seven_View.this,HighestSellingDay.class);
                intent1.putExtra("startDate",txt_fromdate.getText().toString().trim());
                intent1.putExtra("id",id);
                startActivity(intent1);
            }
        });

        txt_purchaseDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent1=new Intent(Top_seven_View.this,HighestPurchaseDay.class);
                intent1.putExtra("startDate",txt_fromdate.getText().toString().trim());
                intent1.putExtra("id",id);
                startActivity(intent1);
            }
        });



    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
    }
}

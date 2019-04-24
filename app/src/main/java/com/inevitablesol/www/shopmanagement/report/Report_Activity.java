package com.inevitablesol.www.shopmanagement.report;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.R;

public class Report_Activity extends BaseActivity implements View.OnClickListener {


private ImageView daysbook,sales,profit_loss,discount,purchase,expense,tax,taxRate,gst;
    private static final String TAG = "Report_Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_report,frameLayout);

        daysbook=(ImageView)findViewById(R.id.r_dayBook);
        daysbook.setOnClickListener(this);
        discount=(ImageView)findViewById(R.id.r_discount);
        discount.setOnClickListener(this);
        sales= (ImageView)findViewById(R.id.r_sale);
        sales.setOnClickListener(this);


        purchase= (ImageView) findViewById(R.id.r_purchase);
        purchase.setOnClickListener(this);
        profit_loss=(ImageView)findViewById(R.id.r_profit_loss);
        profit_loss.setOnClickListener(this);
        expense=(ImageView)findViewById(R.id.r_expense);
        expense.setOnClickListener(this);
        tax=(ImageView)findViewById(R.id.r_tax);
        tax.setOnClickListener(this);
        taxRate=(ImageView)findViewById(R.id.r_rate);
        taxRate.setOnClickListener(this);
        gst= (ImageView) findViewById(R.id.r_gst);
        gst.setOnClickListener(this);
        mDrawerList.setItemChecked(position, true);
        toolbar.setTitle(listArray[position]);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed:Report Activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

            case R.id.r_dayBook:
                   startActivity(new Intent(this,DayBook_Activity.class));
                break;
            case R.id.r_profit_loss:
                startActivity(new Intent(this,Profit_and_loss.class));
                break;
            case R.id.r_discount:
                startActivity(new Intent(this,Report_Disount.class));
                break;
            case R.id.r_expense:
                startActivity(new Intent(this,Report_expensive.class));
                break;
            case  R.id.r_purchase:
                startActivity(new Intent(this,Report_Purchase.class));
                break;

            case  R.id.r_rate:
                startActivity(new Intent(this,Report_TaxRate.class));
                break;
            case R.id.r_tax:
                startActivity(new Intent(this,Report_Tax.class));
                break;
            case  R.id.r_sale:
                startActivity(new Intent(this,Report_Sale.class));
                break;
            case R.id.r_gst:
                startActivity(new Intent(this,Report_GST_Activity.class));
                break;




            default:
                Toast.makeText(this, "Wrong Choice", Toast.LENGTH_SHORT).show();

        }

    }
}

package com.inevitablesol.www.shopmanagement.billing_module;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.Quotation.Quotation_custInfo;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

public class BillingDetails extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout lt_makeBill, lt_makeQouatation, lt_makeinstant,li_invocieHsitory,ll_quatation_history;
    SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs";
    private SqlDataBase sqlDataBase;



    private String dbname;
    private Context context=BillingDetails.this;

    private static final String MySETTINGS = "MySetting";
    private SharedPreferences sharedpreferences2;

    private static final String TAG = "BillingDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_details);
        lt_makeBill = (LinearLayout) findViewById(R.id.make_bill);
        lt_makeBill.setOnClickListener(this);
        lt_makeQouatation = (LinearLayout) findViewById(R.id.make_a_qouatation);
        lt_makeinstant = (LinearLayout) findViewById(R.id.instant_bill);
        lt_makeinstant.setOnClickListener(this);
        lt_makeQouatation.setOnClickListener(this);
        li_invocieHsitory=(LinearLayout)findViewById(R.id.invoice_history);
        li_invocieHsitory.setOnClickListener(this);
        li_invocieHsitory=(LinearLayout)findViewById(R.id.Quotation_history);
        li_invocieHsitory.setOnClickListener(this);
        ll_quatation_history=(LinearLayout)findViewById(R.id.Quotation_history) ;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sqlDataBase = new SqlDataBase(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));

        sharedpreferences2 = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);


        try
        {

            String quotation =(sharedpreferences2.getString("quotation", null));
            Log.d(TAG, "onCreate:quotation Status"+quotation);
            if(quotation !=null)
            {
                if(quotation.equalsIgnoreCase("Yes"))
                {
                    lt_makeQouatation.setVisibility(View.VISIBLE);
                    ll_quatation_history.setVisibility(View.VISIBLE);


                }else
                {
                    lt_makeQouatation.setVisibility(View.GONE);
                    ll_quatation_history.setVisibility(View.GONE);


                }

            }

        }catch (NullPointerException e)
        {

        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.make_bill:
                startActivity(new Intent(BillingDetails.this, MakeBill_selectCustomer.class));
                break;
            case R.id.make_a_qouatation:
                startActivity(new Intent(context,Quotation_custInfo.class));
                break;
            case R.id.instant_bill:
                startActivity(new Intent(BillingDetails.this, Make_InstantBill.class));
                break;
            case R.id.invoice_history:
                startActivity(new Intent(this,Invoice_History.class));
                break;
            case R.id.Quotation_history:
                startActivity(new Intent(this,Quotation_History.class));
                break;
            default:
                Toast.makeText(context, "Invalid", Toast.LENGTH_SHORT).show();
                break;

        }

    }

}



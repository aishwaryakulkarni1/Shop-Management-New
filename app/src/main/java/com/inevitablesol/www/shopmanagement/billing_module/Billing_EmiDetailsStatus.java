package com.inevitablesol.www.shopmanagement.billing_module;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;

public class Billing_EmiDetailsStatus extends AppCompatActivity implements View.OnClickListener {

    private  String taxableValue,total_gst,shipping_charges,totalAmount,amount_paid,balanceDue,ModeofPayment,other_charges,transactionId;
    private String comName,rateofInterest,boundTime,policyNumber,pancard;
    private static final String TAG = "Billing_EmiDetailsStatu";

    private AppCompatButton bt_next;
    private TextInputEditText et_transactionId;

  private TextView txt_comName,txt_policynumber,txt_pancard,txt_rateOfinterest,txt_timeofBound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing__emi_details_status);
        txt_comName=(TextView)findViewById(R.id.company_name);
        txt_pancard=(TextView)findViewById(R.id.pancard);
        txt_policynumber=(TextView)findViewById(R.id.policy_number);
        txt_rateOfinterest=(TextView)findViewById(R.id.rateofIntrrest);
        txt_timeofBound=(TextView)findViewById(R.id.timeofBound);
        bt_next=(AppCompatButton)findViewById(R.id.emi_detailsPaymentDone);
        bt_next.setOnClickListener(this);
        et_transactionId=(TextInputEditText)findViewById(R.id.emi_invId);



        Intent intent=getIntent();
        if(intent.hasExtra("taxableValue"))
        {
          comName=  intent.getStringExtra("comName");
          rateofInterest=  intent.getStringExtra("rateof_interest");
           policyNumber= intent.getStringExtra("policyNumber");
           pancard= intent.getStringExtra("pancard");
           boundTime= intent.getStringExtra("boundTime");

            taxableValue=intent.getStringExtra("taxableValue");
            total_gst=  intent.getStringExtra("totalgst");
            other_charges = intent.getStringExtra("otherCharges");
            shipping_charges= intent.getStringExtra("shippingCharges");
            amount_paid= intent.getStringExtra("amountpaid");
            balanceDue= intent.getStringExtra("balanceDue");
            ModeofPayment= intent.getStringExtra("totalAmount");
            totalAmount= intent.getStringExtra("totalAmount");
            ModeofPayment= intent.getStringExtra("modeofPayment");
            transactionId=intent.getStringExtra("transactionId");
            txt_timeofBound.setText(boundTime);
            txt_rateOfinterest.setText(rateofInterest);
            txt_policynumber.setText(policyNumber);
            txt_pancard.setText(pancard);
            txt_comName.setText(comName);
            et_transactionId.setText(transactionId);


        }





    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.emi_detailsPaymentDone:
                Intent intent = new Intent(Billing_EmiDetailsStatus.this, Billing_OnSuccessCash_PaymentMode.class);
                intent.putExtra("taxableValue",taxableValue);
                intent.putExtra("totalgst",total_gst);
                intent.putExtra("otherCharges",other_charges);
                intent.putExtra("shippingCharges",shipping_charges);
                intent.putExtra("amountpaid",amount_paid);
                intent.putExtra("balanceDue",balanceDue);
                intent.putExtra("totalAmount",totalAmount);
                intent.putExtra("modeofPayment",ModeofPayment);
                intent.putExtra("transactionId",transactionId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Toast.makeText(this, "Please Complete Process", Toast.LENGTH_SHORT).show();

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        //   super.onBackPressed();
        Toast.makeText(this, "Don't Go Back", Toast.LENGTH_SHORT).show();
    }
}

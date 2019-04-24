package com.inevitablesol.www.shopmanagement.billing_module;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.account.Edit_User;

public class Paytm_payment_Status extends AppCompatActivity
{
    EditText et_transactionId;
    AppCompatButton bt_done;
    private static final String TAG = "Paytm_payment_Status";
    private  String taxableValue,total_gst,shipping_charges,totalAmount,amount_paid,balanceDue,ModeofPayment,other_charges;
    private String Tid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_paytm__status);
        et_transactionId=(EditText)findViewById(R.id.et_transactionId);
        bt_done=(AppCompatButton)findViewById(R.id.payment_done);

        Intent intent=getIntent();
        if(intent.hasExtra("taxableValue"))
        {

            taxableValue=intent.getStringExtra("taxableValue");
            total_gst=  intent.getStringExtra("totalgst");
            Tid=  intent.getStringExtra("tranId");
            et_transactionId.setText(Tid);

            Log.d(TAG, "onCreate: ID"+Tid);

           other_charges = intent.getStringExtra("otherCharges");
            shipping_charges= intent.getStringExtra("shippingCharges");
            amount_paid= intent.getStringExtra("amountpaid");
            balanceDue= intent.getStringExtra("balanceDue");
            ModeofPayment= intent.getStringExtra("totalAmount");
           totalAmount= intent.getStringExtra("totalAmount");
            ModeofPayment= intent.getStringExtra("modeofPayment");
        }

        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String transactionId=et_transactionId.getText().toString().trim();

                Intent intent = new Intent(Paytm_payment_Status.this, Billing_OnSuccessCash_PaymentMode.class);
                intent.putExtra("taxableValue",taxableValue);
                intent.putExtra("totalgst",total_gst);
                intent.putExtra("otherCharges",other_charges);
                intent.putExtra("shippingCharges",shipping_charges);
                intent.putExtra("amountpaid",amount_paid);
                intent.putExtra("balanceDue",balanceDue);
                intent.putExtra("totalAmount",totalAmount);
                intent.putExtra("modeofPayment",ModeofPayment);
                intent.putExtra("transactionId",et_transactionId.getText().toString().trim());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();



                Log.d(TAG, "onClick: ");
            }
        });
        et_transactionId.setText(Tid);

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

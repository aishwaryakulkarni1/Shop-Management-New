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

public class Billing_ChequeDetailsStatus extends AppCompatActivity implements View.OnClickListener {

    private    TextView txt_accountHolder,txt_date, txt_paymentType,txt_accountNumber,txt_bankName;
    private TextInputEditText et_transactionId;
    private AppCompatButton bt_next;

    private static final String TAG = "Billing_ChequeDetailsSt";

    private  String taxableValue,total_gst,shipping_charges,totalAmount,amount_paid,balanceDue,ModeofPayment,other_charges,transactionId;
    private String bankName,chequeNumber,status;
    private String accountHolderName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing__cheque_details_status);
        txt_accountHolder=(TextView)findViewById(R.id.account_holderName);
        txt_accountNumber=(TextView)findViewById(R.id.cheque_number);
        txt_date=(TextView)findViewById(R.id.cheque_date);
        txt_paymentType=(TextView)findViewById(R.id.type_of_payment);
        txt_bankName=(TextView)findViewById(R.id.bank_name);
        et_transactionId=(TextInputEditText)findViewById(R.id.et_transactionId_bankDetails);
        bt_next=(AppCompatButton)findViewById(R.id.cheque_detailsPaymentDone);
        bt_next.setOnClickListener(this);

        Intent intent=getIntent();
        if(intent.hasExtra("taxableValue"))
        {

            bankName=  intent.getStringExtra("bankName");
            accountHolderName= intent.getStringExtra("bankHolderName");

            chequeNumber= intent.getStringExtra("chequeNumber");
            status=intent.getStringExtra("typeOfPayment");

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
            et_transactionId.setText(transactionId);
            txt_date.setText(intent.getStringExtra("date"));
            txt_accountNumber.setText(chequeNumber);
          //  txt_date.setText(status);
            txt_accountHolder.setText(accountHolderName);
            txt_paymentType.setText(status);
            txt_bankName.setText(bankName);

        }

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.cheque_detailsPaymentDone:
                transactionId=et_transactionId.getText().toString().trim();
                Intent intent = new Intent(Billing_ChequeDetailsStatus.this, Billing_OnSuccessCash_PaymentMode.class);
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

//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//        Log.d(TAG, "onBackPressed: ");
//        finish();
//        Intent intent = new Intent(Billing_ChequeDetailsStatus.this, BillingHistory.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case android.R.id.home:
//                finish();
//                Intent intent = new Intent(Billing_ChequeDetailsStatus.this, BillingHistory.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                return true;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }


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

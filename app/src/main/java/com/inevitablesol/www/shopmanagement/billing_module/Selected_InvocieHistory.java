package com.inevitablesol.www.shopmanagement.billing_module;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;

public class Selected_InvocieHistory extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Selected_InvocieHistory";
    private Context context=Selected_InvocieHistory.this;
    private String invocieId,cName,c_id,amount,status,payment_id,desrciption,modeOfpayment,dateTime,dbname;

    private TextView tx_name,tx_email,txt_mobile,txt_tranId,tx_balanceDue,tx_amount,tx_invocie_id,tx_paymentId,tx_status,tx_description,tx_modeOfPayment,tx_dateTime;

    private AppCompatButton  addPayemnt;

    private TextInputEditText et_paid_amount;

    private SharedPreferences sharedpreferences;
    private  final String MyPREFERENCES = "MyPrefs";
    private String transactionId;
    private  String balanceDue,c_mail,c_mobile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected__invocie_history);
        tx_amount=(TextView)findViewById(R.id.txt_H_amount);
        tx_dateTime=(TextView)findViewById(R.id.txt_H_Datetime);
        tx_description=(TextView)findViewById(R.id.txt_H_Description);
        tx_status=(TextView)findViewById(R.id.txt_H_status);
        tx_name=(TextView)findViewById(R.id.txt_H_Name);
        tx_paymentId=(TextView)findViewById(R.id.txt_H_PaymentId);
        tx_invocie_id=(TextView)findViewById(R.id.txt_H_InvocieId);
        et_paid_amount=(TextInputEditText)findViewById(R.id.et_H_PaidAmount);
         tx_balanceDue=(TextView) findViewById(R.id.txt_H_balnceDue);
        tx_email=(TextView)findViewById(R.id.txt_H_Email);
        txt_mobile=(TextView)findViewById(R.id.txt_H_Mobile);
        tx_modeOfPayment=(TextView)findViewById(R.id.txt_H_mode);
        addPayemnt=(AppCompatButton)findViewById(R.id.add_remainingAmount);
        addPayemnt.setOnClickListener(this);


        Intent intent=getIntent();
        if(intent.hasExtra("in_id"))
        {
          invocieId=   intent.getStringExtra("in_id");
            tx_invocie_id.setText(invocieId);
            cName  =intent.getStringExtra("c_name");
           tx_name.setText(cName);
           c_id= intent.getStringExtra("c_id");
            amount= intent.getStringExtra("amount");
          tx_amount.setText(amount );
            payment_id= intent.getStringExtra("payment_id");
          tx_paymentId.setText(payment_id);
            status= intent.getStringExtra("status");
          tx_status.setText(status);
           c_mail= intent.getStringExtra("c_email");
           c_mobile =intent.getStringExtra("mobile");
          balanceDue=  intent.getStringExtra("balanceDue");
              tx_balanceDue.setText(balanceDue);
              tx_email.setText(c_mail);
              txt_mobile.setText(c_mobile);
            tx_description.setText(desrciption= intent.getStringExtra("description"));
         tx_modeOfPayment.setText(modeOfpayment= intent.getStringExtra("modeOfPayment"));
           tx_dateTime.setText(dateTime= intent.getStringExtra("dateTime"));
           dbname= intent.getStringExtra("dbname");

        }else
        {
            Toast.makeText(this, "No Date Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.add_remainingAmount:

                 String paidAmount=et_paid_amount.getText().toString().trim();
                  if(paidAmount.length()>0 &&!paidAmount.isEmpty())
            {
                Intent intent=new Intent(context,SelectModeOFPaymnet.class);

                intent.putExtra("in_id",invocieId);
                intent.putExtra("c_name",cName);
                intent.putExtra("email",c_mail);
                intent.putExtra("mobile",c_mobile);
                intent.putExtra("c_id",c_id);
                intent.putExtra("amount",amount);
                intent.putExtra("payment_id",payment_id);
                intent.putExtra("status",status);
                intent.putExtra("description",desrciption);
                intent.putExtra("modeOfPayment",modeOfpayment);
                intent.putExtra("dateTime",dateTime);
                intent.putExtra("paidAmount",paidAmount);
                intent.putExtra("balanceDue",balanceDue);
                intent.putExtra("dbname",dbname);
                startActivity(intent);


            }else
                  {
                      Toast.makeText(context, "Select Amount", Toast.LENGTH_SHORT).show();
                  }
                break;
            default:
                Toast.makeText(context, "Wrong Chooice", Toast.LENGTH_SHORT).show();
                break;
        }

    }








}

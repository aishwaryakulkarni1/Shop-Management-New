package com.inevitablesol.www.shopmanagement.billing_module;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;

public class Selected_Quotation_History extends AppCompatActivity {

    private String QId;
    private  String amount;

    private TextView tx_Qid,tx_Amount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected__quotation__history);
        tx_Amount=(TextView)findViewById(R.id.txt_Q_amount);
        tx_Qid=(TextView)findViewById(R.id.txt_Q_Id);
        Intent intent=getIntent();
        if(intent.hasExtra("Q_id"))
        {
           QId=   intent.getStringExtra("in_id");
            tx_Qid.setText(QId);
          //  tx_invocie_id.setText(invocieId);
//            cName  =intent.getStringExtra("c_name");
//            tx_name.setText(cName);
//            c_id= intent.getStringExtra("c_id");
            amount= intent.getStringExtra("Amount");
            tx_Amount.setText(amount);
//            tx_amount.setText(amount );
//            payment_id= intent.getStringExtra("payment_id");
//            tx_paymentId.setText(payment_id);
//            status= intent.getStringExtra("status");
//            tx_status.setText(status);
//            tx_description.setText(desrciption= intent.getStringExtra("description"));
//            tx_modeOfPayment.setText(modeOfpayment= intent.getStringExtra("modeOfPayment"));
//            tx_dateTime.setText(dateTime= intent.getStringExtra("dateTime"));
//            dbname= intent.getStringExtra("dbname");

        }else
        {
            Toast.makeText(this, "No Date Available", Toast.LENGTH_SHORT).show();
        }
    }
}

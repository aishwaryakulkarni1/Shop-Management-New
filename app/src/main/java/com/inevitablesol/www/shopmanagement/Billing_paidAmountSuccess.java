package com.inevitablesol.www.shopmanagement;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.billing_module.BillingHistory;
import com.inevitablesol.www.shopmanagement.billing_module.Billing_OnSuccessCash_PaymentMode;
import com.inevitablesol.www.shopmanagement.billing_module.Invoice_History;
import com.inevitablesol.www.shopmanagement.billing_module.PrinterPojoClass;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

public class Billing_paidAmountSuccess extends AppCompatActivity {


    private  TextView txt_taxableValue,txt_totalgst,txt_otherCharges,txt_totalAmount
            ,txt_amountPaid,txt_balanceDue,txt_AmountinRupees,txt_paymentMode,txt_shippingCharge,txt_transactionId;
    private AppCompatButton  bt_home;
    private String modeOfpayment;
    private String transactionId;

    private PrinterPojoClass printerPojoClass;
    private GlobalPool globalPool;
    private SqlDataBase sqlDataBase;
    private Context context=Billing_paidAmountSuccess.this;
    private static final String TAG = "Billing_paidAmountSucce";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_paid_amount_success);

        txt_amountPaid=(TextView)findViewById(R.id.status_balance_paid);
        txt_balanceDue=(TextView)findViewById(R.id.status_balanceDue);

        txt_totalAmount=(TextView)findViewById(R.id.status_totalAmount);
        txt_AmountinRupees=(TextView)findViewById(R.id.s_totalAmount);
        bt_home=(AppCompatButton)findViewById(R.id.jumptoBillingHistory);
        txt_paymentMode=(TextView)findViewById(R.id.status_paymentMode);
        txt_transactionId=(TextView)findViewById(R.id.transactionId);
        sqlDataBase=new SqlDataBase(context);
         getSupportActionBar().hide();
        globalPool=(GlobalPool)this.getApplicationContext();

        Intent intent=getIntent();
        if(intent.hasExtra("amountpaid"))
        {

            transactionId=intent.getStringExtra("transactionId");
            txt_amountPaid.setText( intent.getStringExtra("amountpaid"));
           // txt_balanceDue.setText( intent.getStringExtra("balanceDue"));
            txt_totalAmount.setText( intent.getStringExtra("totalAmount"));
            txt_AmountinRupees.setText( intent.getStringExtra("amountpaid"));
            modeOfpayment=intent.getStringExtra("modeofPayment");
            printerPojoClass= (PrinterPojoClass) intent.getSerializableExtra("printclass");
            txt_paymentMode.setText(modeOfpayment);

              double  balanceDue=Double.parseDouble(intent.getStringExtra("balanceDue"))-Double.parseDouble(intent.getStringExtra("amountpaid"));
              double   balance_due=Math.round(balanceDue * 100.0) / 100.0;
            txt_balanceDue.setText(String.valueOf(balance_due));


            try
            {

                txt_transactionId.setText(transactionId);
                //txt_transactionId.setText("transactionId");

            }catch (Exception e)
            {

            }

        }

        bt_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sqlDataBase.deleteDataBase_ItemTable();
                Intent intent = new Intent(Billing_paidAmountSuccess.this, Invoice_History.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        Toast.makeText(getApplicationContext(), "Please go to home page", Toast.LENGTH_SHORT).show();
    }
}

package com.inevitablesol.www.shopmanagement.billing_module;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.printerClasses.PrintBill;
import com.inevitablesol.www.shopmanagement.settings.Billing_Settings;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

public class Billing_OnSuccessCash_PaymentMode extends AppCompatActivity
{

    private  TextView txt_taxableValue,txt_totalgst,txt_otherCharges,txt_totalAmount,txt_amountPaid,txt_balanceDue,txt_AmountinRupees,txt_paymentMode,txt_shippingCharge,txt_transactionId;
    private AppCompatButton  bt_home;
    private String modeOfpayment;
    private static final String TAG = "Billing_OnSuccessCash_P";
    private String transactionId;

    private   PrinterPojoClass printerPojoClass;
    private GlobalPool globalPool;
    private SqlDataBase sqlDataBase;
    private Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing__on_success_mode);
        txt_taxableValue=(TextView)findViewById(R.id.status_taxableValue);
        txt_amountPaid=(TextView)findViewById(R.id.status_balance_paid);
        txt_balanceDue=(TextView)findViewById(R.id.status_balanceDue);
        txt_otherCharges=(TextView)findViewById(R.id.status_otherchrages);
        txt_shippingCharge=(TextView)findViewById(R.id.status_shippingCharge);
        txt_totalgst=(TextView)findViewById(R.id.status_total_gst);
        txt_totalAmount=(TextView)findViewById(R.id.status_totalAmount);
        txt_AmountinRupees=(TextView)findViewById(R.id.s_totalAmount);
        bt_home=(AppCompatButton)findViewById(R.id.jumptoBillingHistory);
        txt_paymentMode=(TextView)findViewById(R.id.status_paymentMode);
        txt_transactionId=(TextView)findViewById(R.id.transactionId);
        sqlDataBase=new SqlDataBase(context);

        globalPool=(GlobalPool)this.getApplicationContext();
        bt_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sqlDataBase.deleteDataBase_ItemTable();
                           Intent intent = new Intent(Billing_OnSuccessCash_PaymentMode.this, BillingHistory.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

            }
        });
        Intent intent=getIntent();
        if(intent.hasExtra("taxableValue"))
        {
            txt_taxableValue.setText(intent.getStringExtra("taxableValue"));
            txt_totalgst.setText(intent.getStringExtra("totalgst"));
            transactionId=intent.getStringExtra("transactionId");

            txt_otherCharges.setText(intent.getStringExtra("otherCharges"));
            txt_shippingCharge.setText(intent.getStringExtra("shippingCharges"));
           txt_amountPaid.setText( intent.getStringExtra("amountpaid"));
           txt_balanceDue.setText( intent.getStringExtra("balanceDue"));
           txt_totalAmount.setText( intent.getStringExtra("totalAmount"));
            txt_AmountinRupees.setText( intent.getStringExtra("amountpaid"));

              modeOfpayment=intent.getStringExtra("modeofPayment");

               printerPojoClass= (PrinterPojoClass) intent.getSerializableExtra("printclass");

//            Log.d(TAG, "onCreate: Serialazable "+printerPojoClass.getCustName());

             txt_paymentMode.setText(modeOfpayment);
            try
            {
                txt_transactionId.setText(transactionId);
                //txt_transactionId.setText("transactionId");

            }catch (Exception e)
            {

            }

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Log.d(TAG, "onBackPressed: ");
//        finish();
//        Intent intent = new Intent(Billing_OnSuccessCash_PaymentMode.this, BillingHistory.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case android.R.id.home:
//                finish();
////                Intent intent = new Intent(Billing_OnSuccessCash_PaymentMode.this, BillingHistory.class);
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
////                startActivity(intent);
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
            case R.id.print_invoice:
                Intent intent = new Intent(this, PrintBill.class);
                intent.putExtra("name",printerPojoClass.getCustName() );
                intent.putExtra("email", printerPojoClass.getEmail());
                intent.putExtra("phone", printerPojoClass.getPhone());
                intent.putExtra("address", printerPojoClass.getAddress());
                intent.putExtra("custid", printerPojoClass.getAddress());
                intent.putExtra("h_status", printerPojoClass.getH_status());
                intent.putExtra("gst", printerPojoClass.getGst());
                intent.putExtra("supplier", printerPojoClass.getSupplier());
                intent.putExtra("taxableValue", printerPojoClass.getTaxableValue());
                intent.putExtra("cgst", printerPojoClass.getCgst());
                intent.putExtra("sgst", printerPojoClass.getSgst());
                intent.putExtra("igst", printerPojoClass.getIgst());
                intent.putExtra("shipping_charge", printerPojoClass.getShipping_charge());
                intent.putExtra("other_charge", printerPojoClass.getOtherCharge());
                intent.putExtra("amountpaid", printerPojoClass.getAmountPaid());
                intent.putExtra("balanceDue", printerPojoClass.getBalanceDue());
                intent.putExtra("totalAmount", printerPojoClass.getTotalAmount());
                intent.putExtra("totalGst", printerPojoClass.getTotalGst());
                intent.putExtra("shopName",globalPool.getShopName());
                intent.putExtra("shopAddress",globalPool.getShop_address());
                intent.putExtra("code","pincode");
                intent.putExtra("invId",transactionId);
                intent.putExtra("state",globalPool.getShopNumber());

                //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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


    @Override
   public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_printclas, menu);
        return true;
    }

}

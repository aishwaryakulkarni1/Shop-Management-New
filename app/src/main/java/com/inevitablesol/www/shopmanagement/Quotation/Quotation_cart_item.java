package com.inevitablesol.www.shopmanagement.Quotation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.billing_module.BillingCartItems;
import com.inevitablesol.www.shopmanagement.billing_module.CalculateBillItemAdapter;
import com.inevitablesol.www.shopmanagement.measurements.Measurment_Unit;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.sql_lite.ItemDetalisClass;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

import java.util.ArrayList;

public class Quotation_cart_item extends AppCompatActivity implements View.OnClickListener {

    private SqlDataBase sqlDataBase;
    private RecyclerView recyclerView;
    private ArrayList<ItemDetalisClass> itemDetalisClasses;

    private AppCompatButton make_payment;

    private String custName;
    private String custMobile;
    private String custmail;
    private String cust_Id;
    private String address;
    private String deliver_status;
    private String gst_in;
    private String supplier;
    private Context context=Quotation_cart_item.this;
    private static final String TAG = "Quotation_cart_item";
    private GlobalPool globalPool;
    CalculateBillItemAdapter billingStockAdater;
    private SwitchCompat switchCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_cart_item);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_itemSelected);
        TextView txt_custName = (TextView) findViewById(R.id.bill_custName);
        TextView txt_custNumber = (TextView) findViewById(R.id.bill_custNumebr);
        TextView txt_custEmail = (TextView) findViewById(R.id.bill_custEmail);
        make_payment = (AppCompatButton) findViewById(R.id.billing_nextToCalculate);
        make_payment.setOnClickListener(this);
        globalPool = (GlobalPool) this.getApplication();

        Intent intent = getIntent();
        sqlDataBase = new SqlDataBase(this);
        if (intent.hasExtra("name"))
        {
            try
            {

                custName = intent.getStringExtra("name");
                custMobile = intent.getStringExtra("mobile");
                custmail = intent.getStringExtra("email");
                cust_Id = intent.getStringExtra("custid");
                address = intent.getStringExtra("address");
                gst_in = intent.getStringExtra("gst");
                supplier = intent.getStringExtra("supplier");
                deliver_status = intent.getStringExtra("h_status");
                txt_custEmail.setText(custmail.trim());
                txt_custName.setText(custName.trim());
                txt_custNumber.setText(custMobile.trim());


            } catch (NullPointerException e)
            {

            }

        }

        itemDetalisClasses = sqlDataBase.getSelectedItemDetails();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
       billingStockAdater = new CalculateBillItemAdapter(getApplicationContext(), itemDetalisClasses, Quotation_cart_item.this);
        recyclerView.setAdapter(billingStockAdater);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {

            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent e)
                {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
            {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e))
                {
                    int position = rv.getChildAdapterPosition(child);
                    final ItemDetalisClass   billData= itemDetalisClasses.get(position);
                    Log.d(TAG, "onInterceptTouchEvent: billing "+billData.toString());
                    //billData.setTotal_taxableValue("0.0");

                    // billingStockAdater.notifyDataSetChanged();
                    showDialog_discount(billData);








                }
                return  false;

            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e)
            {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


    }


    private void showDialog_discount(final ItemDetalisClass billData)
    {
        Log.d(TAG, "showDialog_discount: Object "+billData.toString());
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.add_instant_dis);
        dialog.setCancelable(false);


        TextView  tx_itemName=(TextView)dialog.findViewById(R.id.dis_itemName);
        tx_itemName.setText(billData.getItem_name());

        TextView tx_discount=(TextView)dialog.findViewById(R.id.dis_inRepees);
        TextView  tx_discountedPrice=(TextView)dialog.findViewById(R.id.discoutedPrice);
        tx_discount.setText(billData.getDiscount());
        tx_discountedPrice.setText(String.valueOf(Double.parseDouble(billData.getUnit_price())-Double.parseDouble(billData.getDiscount())));

        TextView tx_changedUnit=(TextView)dialog.findViewById(R.id.changedUnit);
        tx_changedUnit.setText(billData.getChnagedUnit());
        TextView txt_measurmentUnit=(TextView)dialog.findViewById(R.id.txt_measurmentUnit);
        txt_measurmentUnit.setText(billData.getUnit());

       // tx_discountedPrice.setText(billData.getTotal_taxableValue());

        //  ( (TextView)dialog.findViewById(R.id.dis_inRepees)).setText(billData.getDiscount());
        //( (TextView)dialog.findViewById(R.id.dis_discountedPrice)).setText(billData.get);

//        TextView tx_qty=(TextView)dialog.findViewById(R.id.dis_qty);
//        tx_qty.setText(billData.getSelectd_qty());

        TextView  tx_unitPrice=(TextView)dialog.findViewById(R.id.unitPrice);
        tx_unitPrice.setText(billData.getUnit_price());
        final TextView tx_totalPrice=(TextView)dialog.findViewById(R.id.total_amount);




        TextView txt_discountedUnitPrice=(TextView)dialog.findViewById(R.id.dis_unitPricenew);
        txt_discountedUnitPrice.setText(billData.getUnit_price());
        final TextInputEditText et_instantDiscount=(TextInputEditText)dialog.findViewById(R.id.instant_discount);
        final TextView  et_discountedPrice=(TextView)dialog.findViewById(R.id.discounted_price);
        TextView  et_textQty=(TextView)dialog.findViewById(R.id.et_totalQty);

        et_textQty.setText(billData.getSelectd_qty());

        AppCompatButton add_Discount=(AppCompatButton)dialog.findViewById(R.id.add_discount);

        add_Discount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    String taxable_value = tx_totalPrice.getText().toString().trim();
                    if(globalPool.getGst_status())
                    {

                        String qty = billData.getSelectd_qty();
                        String gst = billData.getGst_per();
                        Log.d(TAG, "onClick: GST And Qty" + gst + "Qty :" + qty);
                        double taxvalue = Double.parseDouble(taxable_value);
                        double taxab_value = Math.round(taxvalue * 100.0) / 100.00f;

                        double gst_per = Double.parseDouble(gst) / 100.00f;
                        double cal_gst = taxab_value * gst_per;
                        String total_gst = String.valueOf(Math.round(cal_gst * 100.0) / 100.00f);
                        double totalAmount = cal_gst + taxab_value;
                        Log.d(TAG, "addItem:ItemId" + billData.getItem_id());
                        Log.d(TAG, "addItem:Qty" + qty);
                        Log.d(TAG, "addItem:taxableValue" + taxable_value);
                        Log.d(TAG, "addItem:get_per" + gst_per);
                        Log.d(TAG, "addItem:cal_gst" + total_gst);
                        //  Log.d(TAG, "addItem:TotalAmount"+totalAmont);

                        Log.d(TAG, "onClick:  total price" + totalAmount);
                        if (!taxable_value.isEmpty())
                        {

                            billData.setTotal_taxableValue(String.valueOf(taxab_value));

                            sqlDataBase.updatedDiscontedPrice(String.valueOf(taxab_value), billData.getItem_id(), String.valueOf(totalAmount), String.valueOf(cal_gst));
                            billingStockAdater.notifyDataSetChanged();
                            dialog.dismiss();
                        } else
                        {
                            Toast.makeText(Quotation_cart_item.this, "Please add Discount", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Log.d(TAG, "onClick: TaxableValue"+taxable_value);
                        double totalAmount = 0.0+ Double.parseDouble(taxable_value);
                        sqlDataBase.updatedDiscontedPrice(String.valueOf(taxable_value), billData.getItem_id(), String.valueOf(totalAmount), String.valueOf(0.0));
                        billingStockAdater.notifyDataSetChanged();
                        dialog.dismiss();


                    }
                } catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }catch (Exception e)
                {
                    Toast.makeText(Quotation_cart_item.this, "Please refresh Page", Toast.LENGTH_SHORT).show();
                }

            }
        });
        AppCompatButton  cancel=(AppCompatButton)dialog.findViewById(R.id.dis_cancel);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        et_instantDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                String joistring=billData.getUnit().concat(billData.getChnagedUnit());
                try {
                    joistring=billData.getUnit().concat(billData.getChnagedUnit());
                    Log.d(TAG, "afterTextChanged: JoinString"+joistring);

                    double discount=Double.parseDouble(et_instantDiscount.getText().toString());
                    double discounted_p=Double.parseDouble(billData.getUnit_price())-discount;
                    et_discountedPrice.setText(String.valueOf(discounted_p));
                    ;


                    // double taxvalue=Double.parseDouble(billData.getSelectd_qty())*Double.parseDouble(String.valueOf(discounted_p));
                    //  double taxab_value=Math.round(taxvalue*100.0)/100.00f;
                    tx_totalPrice.setText(String.valueOf(String.valueOf(Measurment_Unit.getCalulatedAmount(joistring,discounted_p,Integer.parseInt(billData.getSelectd_qty())))));

                } catch (NumberFormatException e1)
                {
                    double discounted_price=Double.parseDouble(billData.getUnit_price())-0;
                    et_discountedPrice.setText(String.valueOf(discounted_price));

                    // double taxvalue=Double.parseDouble(billData.getSelectd_qty())*Double.parseDouble(String.valueOf(discounted_price));
                    // double taxab_value=Math.round(taxvalue*100.0)/100.00f;
                    tx_totalPrice.setText(String.valueOf(String.valueOf(Measurment_Unit.getCalulatedAmount(joistring,discounted_price,Integer.parseInt(billData.getSelectd_qty())))));
                    e1.printStackTrace();
                }


            }
        });


        dialog.show();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.billing_nextToCalculate:
                Intent intent = new Intent(context, Quatation_taxes.class);
                intent.putExtra("name", custName);
                intent.putExtra("email", custmail);
                intent.putExtra("phone", custMobile);
                intent.putExtra("address", address);
                intent.putExtra("custid", cust_Id);
                intent.putExtra("h_status", deliver_status);
                intent.putExtra("gst", gst_in);
                intent.putExtra("supplier", supplier);
                startActivity(intent);


                break;
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();

                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.printerClasses.PrintBill;
import com.inevitablesol.www.shopmanagement.purchase_module.PurchaseBillingDetails;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.inevitablesol.www.shopmanagement.purchase_module.PurchaseBillingDetails.MyPREFERENCES;

public class CalculateBillingCart extends AppCompatActivity implements View.OnClickListener {
    private TextView txt_taxableValue,txt_sgst,txt_igst,txt_cgst;
    private LinearLayout ly_cgst,ly_sgst,ly_igst;
    private static final String TAG = "CalculateBillingCart";
    private Context context=CalculateBillingCart.this;
    private SqlDataBase sqlDataBase;
    private TextView txt_gst;
    private double gstValue;
    private double s_gst_Value;
    private double c_gst_Value;
    private RadioGroup radioGroup;
    private double taxableValue;
    private TextInputEditText et_shippingCharge, et_otherCharge;
    private TextView txt_totalAmount;
    private CheckBox rb_statusBill;
    private TextInputEditText et_amountpaid;
    private  TextView et_balanceDue;
    private AppCompatButton choosePaymentMode;
    private SharedPreferences sharedpreferences;

    private  String custName,custMobile,custmail,cust_Id,supplyier,address,gst_in,deliver_status;
    private String supplier;
    private double taxableValue1;
    private double gstValue1;
    private double s_gst_Value1;
    private double c_gst_Value1;
    private  AppCompatButton print;

    private String dbname;
    private String shopName;
    private String shopAdddress;
    private String shopstate;
    private String pincode;

    private  LinearLayout linear_shippingCharges,linear_otherCharges,linear_gst;
    private static final String MySETTINGS = "MySetting";
    private SharedPreferences sharedpreferences2;
    private GlobalPool globalPool ;

    private LinearLayout addReminder;
    private ImageView img_remainder;
    private TextView txt_remainder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_billing_cart);
        txt_taxableValue=(TextView)findViewById(R.id.txt_taxableValue_bill);
        globalPool = (GlobalPool) this.getApplication();
        txt_sgst=(TextView)findViewById(R.id.s_gst);
        txt_igst=(TextView)findViewById(R.id.txt_igst);
        txt_cgst=(TextView)findViewById(R.id.txt_cgst);
        ly_cgst=(LinearLayout)findViewById(R.id.ly_cgst);
        ly_igst=(LinearLayout)findViewById(R.id.lt_igst);
        ly_sgst=(LinearLayout)findViewById(R.id.ly_sgst);
        txt_gst=(TextView)findViewById(R.id.txt_view_gst);
        et_shippingCharge=(TextInputEditText)findViewById(R.id.et_shipping_charge);
        txt_totalAmount=(TextView)findViewById(R.id.bill_totalAmount);
        et_otherCharge=(TextInputEditText)findViewById(R.id.et_otherCharge);
        et_amountpaid= (TextInputEditText) findViewById(R.id.et_amountPaid);
        et_balanceDue=(TextView)findViewById(R.id.et_balanceDue);
        //print=(AppCompatButton)findViewById(R.id.print_bill);
       // print.setOnClickListener(this);
        linear_shippingCharges=(LinearLayout)findViewById(R.id.linear_shippingStatus);
        linear_otherCharges=(LinearLayout)findViewById(R.id.linear_otherStatus);
        linear_gst=(LinearLayout)findViewById(R.id.linear_gst);
        choosePaymentMode=(AppCompatButton)findViewById(R.id.purchase_choosePayment);
        choosePaymentMode.setOnClickListener(this);
        ly_sgst.setOnClickListener(this);
        ly_igst.setOnClickListener(this);
        ly_cgst.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences2 = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));

        addReminder = (LinearLayout) findViewById(R.id.linear_addReminder);
        img_remainder = (ImageView) findViewById(R.id.wish_img_remainder);
        txt_remainder = (TextView) findViewById(R.id.wish_setRemainder);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        try
        {

            if(globalPool!=null)
            {

                if (globalPool.getGst_status())
                {
                    linear_gst.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onCreate:IF Status");
                } else
                {
                    linear_gst.setVisibility(View.GONE);
                    Log.d(TAG, "onCreate: gst Status Else");
                }
            }else
            {
                Toast.makeText(context, "please Refresh Activity", Toast.LENGTH_SHORT).show();
            }

        }catch (NullPointerException e)
        {
            Log.d(TAG, "onCreate:error"+e);
        }
        if(linear_gst.getVisibility()==View.VISIBLE)
        {
            sqlDataBase=new SqlDataBase(context);
            taxableValue=sqlDataBase.getTaxableValue();
            gstValue=sqlDataBase.getTotalGST();
        }else
        {
            sqlDataBase=new SqlDataBase(context);
            taxableValue=sqlDataBase.getTaxableValue();
            gstValue=0.0;
        }

          taxableValue1=Math.round(taxableValue * 100.0) / 100.0;
          gstValue1=Math.round(gstValue * 100.0) / 100.0;
        Log.d(TAG, "onCreate:TaxableValue"+taxableValue1);
        Log.d(TAG, "onCreate:TotalGSt"+gstValue1);


         if(taxableValue1>0)
              txt_taxableValue.setText(String.valueOf(taxableValue1));

        txt_gst.setText(String.valueOf(gstValue1));
        rb_statusBill=(CheckBox) findViewById(R.id.rb_bill_statusFull);

        ly_igst.setVisibility(View.GONE);
        s_gst_Value=gstValue1/2;
        s_gst_Value1=Math.round(s_gst_Value * 100.0) / 100.0;
        c_gst_Value=gstValue1/2;
        c_gst_Value1=Math.round(c_gst_Value * 100.0) / 100.0;
        txt_cgst.setText(String.valueOf(c_gst_Value1));
        txt_sgst.setText(String.valueOf(s_gst_Value1));
        txt_igst.setText("0.0");
        double totalAmount=taxableValue1+gstValue1;
        double totalAmnt=Math.round(totalAmount * 100.0) / 100.0;
        txt_totalAmount.setText(String.valueOf(totalAmnt));

        radioGroup=(RadioGroup)findViewById(R.id.igst_status) ;
            Intent intent=getIntent();

        if (intent.hasExtra("name"))
        {
            try
            {

                custName = intent.getStringExtra("name");
                custMobile = intent.getStringExtra("phone");
                custmail = intent.getStringExtra("email");
                cust_Id=intent.getStringExtra("custid");
                address=intent.getStringExtra("address");
                gst_in=intent.getStringExtra("gst");
                supplier=intent.getStringExtra("supplier");
                deliver_status=intent.getStringExtra("h_status");



            } catch (NullPointerException e)
            {

            }

        }




        try {

            String shippingCharges=(sharedpreferences2.getString("shipping_status", null));
            Log.d(TAG, "onCreate:Shipping"+shippingCharges);
            if(shippingCharges!=null) {
                if (shippingCharges.equalsIgnoreCase("Yes"))
                {

                  linear_shippingCharges.setVisibility(View.VISIBLE);

                } else {
                    linear_shippingCharges.setVisibility(View.GONE);

                }
            }

        }catch (NullPointerException e)
        {
            Log.d(TAG, "onCreate:error"+e);
        }






        try {

            String otherCharges=(sharedpreferences2.getString("othercharges_status", null));
            Log.d(TAG, "onCreate:otherCharges"+otherCharges);
            if(otherCharges!=null) {
                if (otherCharges.equalsIgnoreCase("Yes"))
                {

                    linear_otherCharges.setVisibility(View.VISIBLE);

                } else {
                    linear_otherCharges.setVisibility(View.GONE);

                }
            }

        }catch (NullPointerException e)
        {
            Log.d(TAG, "onCreate:error"+e);
        }





        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Log.d(TAG, "onCheckedChanged: "+checkedId);
                switch (checkedId)
                {
                    case R.id.igst_statusyes:
                        calculateGst();
                        break;
                    case R.id.igst_statusno:
                        removeigst();
                        break;
                }


            }
        });


        et_shippingCharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                try
                {
                    if(s.toString().trim().length()>0)
                    {
                        String shipping = et_shippingCharge.getText().toString().trim();
                        double shipping_value = Double.parseDouble(shipping);
                        if (shipping_value >= 0)
                        {
                            double totalAmount = shipping_value + taxableValue1 + gstValue1;

                                         double tAmount= Math.round(totalAmount * 100.0) / 100.0;
                            txt_totalAmount.setText(String.valueOf(tAmount));
                        } else
                        {
                            Toast.makeText(getApplication(), "Please enter Valid Value", Toast.LENGTH_LONG).show();
                        }
                    }else
                    {
                        double totalAmount = 0.0 + taxableValue1 + gstValue1;
                        double tAmount= Math.round(totalAmount * 100.0) / 100.0;
                        txt_totalAmount.setText(String.valueOf(tAmount));

                    }
                }catch (NullPointerException e)
                {
                    Log.d(TAG, "afterTextChanged:"+e);
                    Toast.makeText(getApplication(),"Please enter Valid Value",Toast.LENGTH_LONG).show();
                }catch (NumberFormatException e)
                {
                    Log.d(TAG, "afterTextChanged:"+e);
                }catch (Exception e)
                {
                    Log.d(TAG, "afterTextChanged:"+e);
                }



            }
        });


        et_otherCharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

                try {
                     if(s.toString().length()>0)
                     {
                         String shipping = et_shippingCharge.getText().toString().trim();
                         double shipping_value = Double.parseDouble(shipping);
                         double otherCharge = Double.parseDouble(s.toString());

                         if (shipping_value >= 0)
                         {
                             double totalAmount = shipping_value + taxableValue1 + (gstValue1) + otherCharge;
                             double tAmount= Math.round(totalAmount * 100.0) / 100.0;
                             txt_totalAmount.setText(String.valueOf(tAmount));
                         } else
                         {

                             Toast.makeText(getApplication(), "Please enter Valid Value", Toast.LENGTH_LONG).show();
                         }
                     }else
                     {

                         String shipping = et_shippingCharge.getText().toString().trim();
                         double shipping_value = Double.parseDouble(shipping);
                         double totalAmount = shipping_value + taxableValue1 + (gstValue1) + 0.0;
                         double tAmount= Math.round(totalAmount * 100.0) / 100.0;
                         txt_totalAmount.setText(String.valueOf(tAmount));
                     }

                } catch (NumberFormatException e)
                {
                    Log.d(TAG, "afterTextChanged:"+e);
                    Toast.makeText(context, "Please enter Valid Value", Toast.LENGTH_SHORT).show();
                }catch (NullPointerException e)
                {
                    Log.d(TAG, "afterTextChanged:"+e);
                }catch (Exception e)
                {
                    Log.d(TAG, "afterTextChanged:"+e);
                }
            }
        });

        rb_statusBill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {

                displayBillAmount();


            }


        });


        et_amountpaid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                try
                {
                    String t_amount;
                    double paidAmopint=0.0;
                    double dueBalance=0.0;
                    if(s.toString().length()>0)
                    {
                        t_amount = txt_totalAmount.getText().toString().trim();
                        paidAmopint = Double.parseDouble(s.toString());
                        if (paidAmopint<=Double.parseDouble(t_amount))
                        {
                            dueBalance = Double.parseDouble(t_amount) - paidAmopint;
                            et_balanceDue.setText(String.valueOf(String.format("%.2f",dueBalance)));
                            //REMINDER VISIBILITY
                            if (dueBalance > 0.0 && paidAmopint>=0.0 && paidAmopint<Double.parseDouble(t_amount))
                            {
                                addReminder.setVisibility(View.VISIBLE);

                                img_remainder.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changeWishDateReminder(txt_remainder);
                                    }
                                });
                            } else {
                                addReminder.setVisibility(View.INVISIBLE);
                            }
                        }else
                        {
                            Toast.makeText(context, "Invalid Amount", Toast.LENGTH_SHORT).show();
                        }

                    }else
                    {
                        t_amount = txt_totalAmount.getText().toString().trim();
                        dueBalance = Double.parseDouble(t_amount) - 0.0;
                        et_balanceDue.setText(String.valueOf(String.format("%.2f",dueBalance)));
                        //REMINDER VISIBILITY
                        if (dueBalance > 0.0 && paidAmopint>=0.0 && paidAmopint<Double.parseDouble(t_amount))
                        {
                            addReminder.setVisibility(View.VISIBLE);

                            img_remainder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    changeWishDateReminder(txt_remainder);
                                }
                            });
                        } else {
                            addReminder.setVisibility(View.INVISIBLE);
                        }
                    }

                } catch (Exception e)
                {
                    Log.d(TAG, "afterTextChanged:"+e);
                }

            }
        });

    }

    private void changeWishDateReminder(final TextView txt){

        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateTimeString = date1.format(myCalendar.getTime());
                txt.setText(currentDateTimeString);

            }
        };

        new DatePickerDialog(CalculateBillingCart.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void displayBillAmount()
    {
        if(rb_statusBill.isChecked())
        {
            String  amount=txt_totalAmount.getText().toString().trim();
            et_amountpaid.setText(amount);
            et_amountpaid.setEnabled(false);

        }else
        {
            et_amountpaid.setEnabled(true);

        }
    }


    private void removeigst()
    {
        ly_cgst.setVisibility(View.VISIBLE);
        ly_sgst.setVisibility(View.VISIBLE);
        ly_igst.setVisibility(View.GONE);
        double s_gst_Value=gstValue1/2;
        double  c_gst_Value=gstValue1/2;
        txt_cgst.setText(String.valueOf(c_gst_Value));
        txt_sgst.setText(String.valueOf(s_gst_Value));
        txt_igst.setText("0.0");

    }

    private void calculateGst()
    {
        ly_igst.setVisibility(View.VISIBLE);
        ly_cgst.setVisibility(View.GONE);
        ly_sgst.setVisibility(View.GONE);
        txt_cgst.setText("0.0");
        txt_sgst.setText("0.0");
        txt_igst.setText(String.valueOf(gstValue1));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume:");
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAppShopDetails();
        Log.d(TAG, "onStart:");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy:");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause:");
    }

    @Override
    public void onClick(View v)
    {

        String   taxablevalue = null;
        String   cgst = "0.0";
        String  sgst  = "0.0";
        String  igst  = "0.0";

        String shipping_charge = "0.0";
        String other_charge  = "0.0";
        String amountpaid  = null;
        String amountDue  = null;
        String totalAmount  = null;
        String totalGst  = "0.0";
        switch (v.getId())
        {
            case R.id.purchase_choosePayment:

                try
                {
                    shipping_charge   = et_shippingCharge.getText().toString().trim();
                    other_charge    =   et_otherCharge.getText().toString().trim();

                }catch (Exception e)
                {

                }
                if(linear_gst.getVisibility()==View.VISIBLE)
                {
                    cgst              =txt_cgst.getText().toString().trim();
                    sgst              =txt_sgst.getText().toString().trim();
                    igst               =txt_igst.getText().toString().trim();
                    totalGst            =txt_gst.getText().toString().trim();

                }else
                {

                }

                      taxablevalue      =txt_taxableValue.getText().toString().trim();
                      amountpaid      =et_amountpaid.getText().toString().trim();
                      amountDue       =et_balanceDue.getText().toString().trim();
                      totalAmount     =txt_totalAmount.getText().toString().trim();

                   if(shipping_charge.isEmpty() || shipping_charge.length() == 0)
                   {
                       shipping_charge="0.00";
                   }
                if(other_charge.isEmpty() || shipping_charge.length()== 0)
                {
                    other_charge="0.00";
                }

                  if(amountpaid.isEmpty() && amountpaid.length()==0)
                  {
                      et_amountpaid.setError("*");
                  }else
                  {
                      Log.d(TAG, "onClick:");
                    //  Intent intent = new Intent(this, Billing_ChoosePaymentMode.class);
                      Intent intent = new Intent(CalculateBillingCart.this, Billing_ProceedToPayment.class);
                      intent.putExtra("name", custName);
                      intent.putExtra("email", custmail);
                      intent.putExtra("phone", custMobile);
                      intent.putExtra("address", address);
                      intent.putExtra("custid", cust_Id);
                      intent.putExtra("h_status", deliver_status);
                      intent.putExtra("gst", gst_in);
                      intent.putExtra("supplier", supplier);
                      intent.putExtra("taxableValue", taxablevalue);
                      intent.putExtra("cgst", cgst);
                      intent.putExtra("sgst", sgst);
                      intent.putExtra("igst", igst);
                      intent.putExtra("shipping_charge", shipping_charge);
                      intent.putExtra("other_charge", other_charge);
                      intent.putExtra("amountpaid", amountpaid);
                      intent.putExtra("balanceDue", amountDue);
                      intent.putExtra("totalAmount", totalAmount);
                      intent.putExtra("totalGst", totalGst);
                    //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                      startActivity(intent);
                  }
                break;
//            case R.id.print_bill:
//                taxablevalue      =txt_taxableValue.getText().toString().trim();
//                cgst              =txt_cgst.getText().toString().trim();
//                sgst              =txt_sgst.getText().toString().trim();
//                igst              =txt_igst.getText().toString().trim();
//                shipping_charge   = et_shippingCharge.getText().toString().trim();
//                other_charge    =et_otherCharge.getText().toString().trim();
//                amountpaid      =et_amountpaid.getText().toString().trim();
//                amountDue       =et_balanceDue.getText().toString().trim();
//                totalAmount=       txt_totalAmount.getText().toString().trim();
//                totalGst            =txt_gst.getText().toString().trim();
//
//                if(amountpaid.isEmpty() && amountpaid.length()==0)
//                {
//                    et_amountpaid.setError("*");
//                }else
//                {
//                    Intent intent = new Intent(this, PrintBill.class);
//                    intent.putExtra("name", custName);
//                    intent.putExtra("email", custmail);
//                    intent.putExtra("phone", custMobile);
//                    intent.putExtra("address", address);
//                    intent.putExtra("custid", cust_Id);
//                    intent.putExtra("h_status", deliver_status);
//                    intent.putExtra("gst", gst_in);
//                    intent.putExtra("supplier", supplier);
//                    intent.putExtra("taxableValue", taxablevalue);
//                    intent.putExtra("cgst", cgst);
//                    intent.putExtra("sgst", sgst);
//                    intent.putExtra("igst", igst);
//                    intent.putExtra("shipping_charge", shipping_charge);
//                    intent.putExtra("other_charge", other_charge);
//                    intent.putExtra("amountpaid", amountpaid);
//                    intent.putExtra("balanceDue", amountDue);
//                    intent.putExtra("totalAmount", totalAmount);
//                    intent.putExtra("totalGst", totalGst);
//                    intent.putExtra("shopName",globalPool.getShopName());
//                    intent.putExtra("shopAddress",globalPool.getShop_address());
//                    intent.putExtra("code",pincode);
//                    intent.putExtra("state",globalPool.getShopNumber());
//
//                  //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//                break;

        }




    }



    private void getAppShopDetails()
    {
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, WebApi.GETSHOPDETAILS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                try
                {
                    Log.d(TAG, "onResponse:ShopDetails" + response);
                    String resp =       response.toString().trim();
                    JSONObject obj =   new JSONObject(resp).getJSONObject("records");
                          shopName=obj.getString("s_name");
                           shopAdddress=obj.getString("address");
                            shopstate=obj.getString("state");
                            pincode=obj.getString("pincode");





                } catch (Exception e)
                {
                    Log.d("Exception", "" + e);
                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(CalculateBillingCart.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                Log.d(TAG, "getParams:"+params.toString());

                return params;
            }
        });

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

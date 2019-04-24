package com.inevitablesol.www.shopmanagement.purchase_module;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.inevitablesol.www.shopmanagement.wishList.Add_WishList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PurchaseBillingDetails extends AppCompatActivity implements View.OnClickListener {

    private String vendor_id;
    private String gstNumber;
    private String invoiceNumber;
    private String invoiceDate;
    private String itemArray;
    private TextView txt_totalTax,txt_gst;

    private EditText et_shippingCharge;
    private EditText et_otherCharge;
    private TextView txt_totalAmount;
    private EditText et_amountpaid;
    private TextView et_balanceDue;
    private AppCompatButton savetoDb;

    RadioGroup radioGroup;
    private RadioButton igstStatus_yes;
    private RadioButton igstStatus_No;
    //private
    RadioGroup  reverse_Charges;
    private  RadioButton reverse_charge_yes;
    private  RadioButton reverse_charges_no;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String userId;
    private String dbname;
    private static final String TAG = "PurchaseBillingDetails";
    private String taxableValue;
    private String gstValue;
     private  TextView txt_sgst;
     private  TextView txt_cgst;
      private  TextView txt_igst;
     private LinearLayout ly_igst,ly_sgst,ly_cgst,ly_totalGst;
    private double s_gst_Value=0.0;
    private double c_gst_Value=0.0;
    private CheckBox rb_statusBill;
    private SqlDataBase sqlDataBase;
    private  String stateCode;


    private  JSONArray imageArray=new JSONArray();
    private  JSONArray imageArray2=new JSONArray();
    private  JSONObject jsonObject=new JSONObject();
    private double taxableValue1;
    private double gstValue1;
    private double s_gst_Value1;
    private double c_gst_Value1;


    private static final String MySETTINGS = "MySetting";
    private SharedPreferences sharedpreferences2;
    private String paytm_reminder_days;
    private boolean duePaymentSms;

    private String balanceDue;
    private Context context=PurchaseBillingDetails.this;
    private String message1;
    private String smsDue;
    private String userMobile;
    private LinearLayout addReminder;
    private TextView txt_remainder;
    private ImageView img_remainder;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_details);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent intent=getIntent();
            if(intent.hasExtra("v_id"))
            {
                 vendor_id      =intent.getStringExtra("v_id");
                  gstNumber     =intent.getStringExtra("gst_number");
                 invoiceNumber  =intent.getStringExtra("invoiceNumber");
                  invoiceDate   =intent.getStringExtra("invoiceDate");
                 itemArray      =intent.getStringExtra("itemArray");
                 taxableValue   =intent.getStringExtra("taxableValue");
                 gstValue       =intent.getStringExtra("gstValue");
                 stateCode      =intent.getStringExtra("stateCode");
                   Log.d(TAG, "itemArray: "+itemArray);
            }
             txt_totalTax=(TextView)findViewById(R.id.txt_view_taxableValue);
             txt_gst=(TextView)findViewById(R.id.txt_view_gst);

              txt_totalAmount=(TextView)findViewById(R.id.et_totalamount);
               et_amountpaid=(EditText)findViewById(R.id.et_amountPaid);
            et_shippingCharge=(EditText)findViewById(R.id.et_shipping_charge);
            et_balanceDue=(TextView)findViewById(R.id.et_balanceDue);
            et_otherCharge=(EditText)findViewById(R.id.et_otherCharge);
           radioGroup=(RadioGroup)findViewById(R.id.igst_status) ;
            ly_totalGst=(LinearLayout)findViewById(R.id.ly_totalgst);
            ly_totalGst.setOnClickListener(this);
           radioGroup.setOnClickListener(this);
             reverse_Charges=(RadioGroup)findViewById(R.id.reverse_charges);
              reverse_charge_yes=(RadioButton)findViewById(R.id.reverse_Charges_yes);
               reverse_charges_no=(RadioButton)findViewById(R.id.reverse_charges_no);

           igstStatus_yes=(RadioButton)findViewById(R.id.igst_statusyes);
           igstStatus_No=(RadioButton)findViewById(R.id.igst_statusno);
           igstStatus_No.setOnClickListener(this);
           igstStatus_yes.setOnClickListener(this);
//           rd_status_bill_amount=(RadioGroup)findViewById(R.id.rg_status_bill);
           rb_statusBill=(CheckBox) findViewById(R.id.rb_bill_statusFull);
         txt_sgst=(TextView)findViewById(R.id.s_gst);
          txt_igst=(TextView)findViewById(R.id.txt_igst);
          txt_cgst=(TextView)findViewById(R.id.txt_cgst);
        ly_cgst=(LinearLayout)findViewById(R.id.ly_cgst);
        ly_igst=(LinearLayout)findViewById(R.id.lt_igst);
        ly_sgst=(LinearLayout)findViewById(R.id.ly_sgst);
        ly_sgst.setOnClickListener(this);
        ly_igst.setOnClickListener(this);
         ly_cgst.setOnClickListener(this);

        addReminder = (LinearLayout) findViewById(R.id.linear_addReminder);
        txt_remainder = (TextView) findViewById(R.id.wish_setRemainder);
        img_remainder = (ImageView) findViewById(R.id.wish_img_remainder);

        savetoDb=(AppCompatButton)findViewById(R.id.purchase_saveTodb);
         savetoDb.setOnClickListener(this);
        taxableValue1=Math.round(Double.parseDouble(taxableValue) * 100.0) / 100.0;
        gstValue1=Math.round(Double.parseDouble(gstValue) * 100.0) / 100.0;
        BigDecimal taxValue=new BigDecimal(taxableValue1);

         txt_totalTax.setText(String.valueOf(taxValue));
          txt_gst.setText(String.valueOf(gstValue1));


               ly_igst.setVisibility(View.GONE);
               s_gst_Value=gstValue1/2;
               c_gst_Value=gstValue1/2;

        s_gst_Value1=Math.round(s_gst_Value * 100.0) / 100.0;
        c_gst_Value=gstValue1/2;
        c_gst_Value1=Math.round(c_gst_Value * 100.0) / 100.0;
        txt_cgst.setText(String.valueOf(c_gst_Value1));
        txt_sgst.setText(String.valueOf(s_gst_Value1));
        txt_igst.setText("0.0");
        double totalAmount=taxableValue1+gstValue1;
        double totalAmnt=Math.round(totalAmount * 100.0) / 100.0;
        BigDecimal totalAmount_b=new BigDecimal(totalAmnt);

        Log.d(TAG, "onCreate: ToatalAmt"+totalAmnt);

        txt_totalAmount.setText(String.valueOf(Math.round(totalAmount_b.doubleValue() * 100.0) / 100.0));

        sharedpreferences2 = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);



            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
           rb_statusBill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
           {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
               {

                       displayBillAmount();


               }
           });
              sqlDataBase=new SqlDataBase(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        userId= sharedpreferences.getString("userId","");
        userMobile=sharedpreferences.getString("usermobile","");

          et_shippingCharge.addTextChangedListener(new TextWatcher()
          {
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
                      String shipping=et_shippingCharge.getText().toString().trim();
                      double shipping_value=Double.parseDouble(shipping);
                      if(shipping_value>=0)
                      {
                          double totalAmount=shipping_value+Double.parseDouble(taxableValue)+Double.parseDouble(gstValue);
                          txt_totalAmount.setText(String.valueOf(totalAmount));
                      }else
                      {
                          Toast.makeText(getApplication(),"Please enter Valid Value",Toast.LENGTH_LONG).show();
                      }
                  }catch (Exception e)
                  {
                      Toast.makeText(getApplication(),"Please enter Valid Value",Toast.LENGTH_LONG).show();
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
              public void afterTextChanged(Editable s) {

                  try
                  {
                      String shipping = et_shippingCharge.getText().toString().trim();
                      double shipping_value = Double.parseDouble(shipping);
                      double otherCharge = Double.parseDouble(s.toString());
                      if (shipping_value >= 0) {
                          double totalAmount = shipping_value + Double.parseDouble(taxableValue) + Double.parseDouble(gstValue) + otherCharge;
                          txt_totalAmount.setText(String.valueOf(totalAmount));
                      } else
                      {
                          Toast.makeText(getApplication(), "Please enter Valid Value", Toast.LENGTH_LONG).show();
                      }

                  } catch (Exception e)
                  {

                  }
              }
          });

                  et_amountpaid.addTextChangedListener(new TextWatcher()
                  {
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

        ly_totalGst.setVisibility(View.VISIBLE);
        getInvoivceImage();


        try
        {

            paytm_reminder_days =(sharedpreferences2.getString("paymentReminder_Days", null));
            String paytm_reminder =(sharedpreferences2.getString("paymentReminder", null));
             smsDue =(sharedpreferences2.getString("purchase_sms", null));

            Log.d(TAG, "onCreate:Paytm reminder "+paytm_reminder);
            Log.d(TAG, "onCreate:Paytm reminder Days"+paytm_reminder_days);
            Log.d(TAG, "onCreate:purchase_sms Due"+smsDue);

            if(paytm_reminder!=null)
            {

                    if(smsDue.equalsIgnoreCase("yes"))
                    {
                        duePaymentSms=true;
                    }else
                    {
                        duePaymentSms=false;
                    }



            }

        }catch (NullPointerException e)
        {

        }




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

        new DatePickerDialog(PurchaseBillingDetails.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void getInvoivceImage()
    {
       int i=0;
        try {
        ArrayList<String> imagelist =sqlDataBase.getInvocieImage(invoiceNumber);
        Log.d(TAG, "saveToDataBase:Image"+imagelist);
            Log.d(TAG, "saveToDataBase:ImageSize"+imagelist.size());

        for (String image:imagelist)

        {
            Log.d(TAG, "getInvoivceImage() called"+image);
            jsonObject =new JSONObject();

                jsonObject.put("image",image);
                  imageArray.put(i,jsonObject);

                   i++;
        }

            for(int j=0;j<imageArray.length();j++)
            {
                JSONObject jsonObject=imageArray.getJSONObject(j);
                String img =jsonObject.toString();
                String abc=img.replaceAll("\\\\", "");
                imageArray2.put(i,abc);
                Log.d(TAG, "getInv:"+abc);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e)
        {

        }catch (Exception e)
        {
            Log.d(TAG, "getInvoivceImage:"+e);
        }

        Log.d(TAG, "getInvoivceImage:"+imageArray2);

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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.purchase_saveTodb:
                try
                {
                    saveToDataBase();
                    break;
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
        }

    }

    private void saveToDataBase() throws Exception
    {
       String sgst,cgst;
        int r_id=reverse_Charges.getCheckedRadioButtonId();
        RadioButton radioButton=   (RadioButton)findViewById(r_id);
        String  reverse_Charges=radioButton.getText().toString().trim();


         String   total_taxablueValue=txt_totalTax.getText().toString().trim();
         String   total_gst=txt_gst.getText().toString().trim();
         String   shippingCharge=et_shippingCharge.getText().toString().trim();
         String   otherCharge=et_otherCharge.getText().toString().trim();
        balanceDue=et_balanceDue.getText().toString().trim();
         String   total_Amount=txt_totalAmount.getText().toString().trim();
         String   amount_Paid=et_amountpaid.getText().toString().trim();


                       sgst=txt_sgst.getText().toString().trim();
                       cgst=txt_cgst.getText().toString().trim();
                   String   igst=txt_igst.getText().toString().trim();
                     if(shippingCharge.isEmpty() || shippingCharge.length() ==0)
                     {
                         shippingCharge="0.0";
                     }
        if(otherCharge.isEmpty() || otherCharge.length() ==0)
        {
            otherCharge="0.0";
        }

          if(amount_Paid.length()>0 &&!amount_Paid.isEmpty() && balanceDue.length()>0 && !balanceDue.isEmpty()) {

              JSONArray jsonArray = new JSONArray(itemArray);

              JSONObject jsonObject = new JSONObject();

              jsonObject.put("vendor_id", vendor_id);
              jsonObject.put("invoice_no", invoiceNumber);
              jsonObject.put("invoice_date", invoiceDate);
              jsonObject.put("gst_total", gstValue);
              jsonObject.put("place_of_supply", stateCode);
              jsonObject.put("reverse_changes", reverse_Charges);
              jsonObject.put("purchase_bill_photo", "11");
              jsonObject.put("shipping_charges", shippingCharge);
              jsonObject.put("other_charges", otherCharge);
              jsonObject.put("total_amount", total_Amount);
              jsonObject.put("amount_paid", amount_Paid);
              jsonObject.put("balance_due", balanceDue);
              jsonObject.put("itemArray", jsonArray);
              jsonObject.put("subtotal", taxableValue);
              jsonObject.put("imageArray", imageArray);
              jsonObject.put("cgst", cgst);
              jsonObject.put("sgst", sgst);
              jsonObject.put("igst", igst);
              jsonObject.put("created_by", userId);

              Log.d(TAG, "getParams: " + jsonObject);

              String item = jsonObject.toString();
              save(item.replaceAll("\\\\", ""));
          }else
          {
              Toast.makeText(this, "Plase Add Amount", Toast.LENGTH_SHORT).show();
          }




    }

    private void save(final String items)
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Updating...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.ADD_PURCHASE, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();
                Log.d(" Added Successfully",resp);
                try
                {
                    loading.dismiss();

                    JSONObject msg = new JSONObject(response);
                    String message = msg.getString("message");

                    if(message.equalsIgnoreCase("Add data succesfully"))
                    {
                         sqlDataBase.deleteInvoiceTable();

                        Log.d(TAG, "onResponse: due Payment SMS"+duePaymentSms);

                        if(duePaymentSms)
                        {
                            if(Double.parseDouble(balanceDue)>0)
                            {


                                    sendDue_payment_Sms();

                            }
                        }
                        Toast.makeText(getApplicationContext()," purchase  Added successfully",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(PurchaseBillingDetails.this,AddPurchaseItem.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);

                    }else
                    {
                        Toast.makeText(getApplicationContext(),"Can not Add Details",Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e)
                {

                }





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {



                Map<String,String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("data",items);
                Log.d(TAG, "getParamsWithImage: "+items);

                return   params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                Log.d(TAG, "onOptionsItemSelected:");
                return true;
        }
        return false;
    }



    private void sendDue_payment_Sms()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, Integer.parseInt(paytm_reminder_days));
        Date result = cal.getTime();

        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(result);
        Log.d(TAG, "onClick:Result"+currentDateTimeString);


        String message = " Dear User  \n This is just Friendly Reminder \n  for Due Payment  of  Purchase !\n -" +
                " of Rs<" +  balanceDue+ ">  \n-" +
                "Thanks You \n M-HOURZ";

        String mobile_no =userMobile;//tv_custNumber.getText().toString().trim();
        final String schtime = currentDateTimeString +" "+"11:11";
        Log.d(TAG, "sendDue_payment_Sms: SchTime"+schtime);
        final String uri = Uri.parse("\n" +
                "http://bhashsms.com/api/schedulemsg.php?")
                .buildUpon()
                .appendQueryParameter("user", "TEAM_MHOURZ")
                .appendQueryParameter("pass", "MECHATRON")
                .appendQueryParameter("text", message)
                .appendQueryParameter("sender", "MHOURZ")
                .appendQueryParameter("phone", mobile_no)
                .appendQueryParameter("priority", "ndnd")
                .appendQueryParameter("stype", "normal")
                .appendQueryParameter("time", schtime)
                .build().toString();

        Log.d(TAG, "sendDue_payment_Sms: URI"+uri);

        StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null)
                            {

                                   if (response.matches(".*\\d+.*"))
                                {
                                    Log.d(TAG, "onResponse: URl"+uri);
                                    Log.d(TAG, "onResponse:Wish List"+uri);
                                    Log.d(TAG, "onResponse:shecdule"+schtime);
                                    Toast.makeText(getApplication(), " SCHEDULE MESSAGE SEND SUCCESSFULLY", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplication(), "  ", Toast.LENGTH_LONG).show();

                                }
                            }
                        } catch (Exception e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}

package com.inevitablesol.www.shopmanagement.Quotation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.billing_module.BillingItems;
import com.inevitablesol.www.shopmanagement.billing_module.Quotation_History;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.pddstudio.urlshortener.URLShortener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Quatation_taxes extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_taxableValue,txt_sgst,txt_igst,txt_cgst;
    private LinearLayout ly_cgst,ly_sgst,ly_igst;
    private Context context=Quatation_taxes.this;
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

    private  String custName,custMobile,custmail,cust_Id,address,gst_in,deliver_status;
    private String supplier;

    private static final String TAG = "Quatation_taxes";

    private ArrayList<BillingItems> billingItems;
    private JSONArray selected_items = new JSONArray();

    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private double taxableValue1;
    private double gstValue1;
    private double s_gst_Value1;
    private double c_gst_Value1;
    private  String totalAmount;


    private  LinearLayout linear_gst,linearLayout_shipping_charges,linear_otherCharges;

    private static final String MySETTINGS = "MySetting";
    private SharedPreferences sharedpreferences2;
    private GlobalPool globalPool;
    private String invId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quatation_taxes);
        txt_taxableValue=(TextView)findViewById(R.id.txt_taxableValue_bill);
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
        globalPool = (GlobalPool) this.getApplication();

        linearLayout_shipping_charges=(LinearLayout)findViewById(R.id.linear_shippingStatus);
        linear_otherCharges=(LinearLayout)findViewById(R.id.linear_otherStatus);

        linear_gst=(LinearLayout)findViewById(R.id.linear_gst);
        linearLayout_shipping_charges=(LinearLayout)findViewById(R.id.linear_shippingStatus);
        linear_otherCharges=(LinearLayout)findViewById(R.id.linear_otherStatus);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        choosePaymentMode=(AppCompatButton)findViewById(R.id.purchase_choosePayment);
        choosePaymentMode.setOnClickListener(this);
        ly_sgst.setOnClickListener(this);
        ly_igst.setOnClickListener(this);
        ly_cgst.setOnClickListener(this);
        sqlDataBase=new SqlDataBase(context);
        taxableValue=sqlDataBase.getTaxableValue();
        gstValue=sqlDataBase.getTotalGST();





        billingItems= sqlDataBase.getBillingItems();




        radioGroup=(RadioGroup)findViewById(R.id.igst_status) ;
        Intent intent=getIntent();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences2 = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));

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

        try
        {

            String gststatus=(sharedpreferences2.getString("gst", null));
            Log.d(TAG, "onCreate:gst"+gststatus);
            if(gststatus!=null) {
                if (globalPool.getGst_status())
                {

                    linear_gst.setVisibility(View.VISIBLE);

                } else
                {
                    linear_gst.setVisibility(View.GONE);

                }
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
        linearLayout_shipping_charges=(LinearLayout)findViewById(R.id.linear_shippingStatus);
        linear_otherCharges=(LinearLayout)findViewById(R.id.linear_otherStatus);

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
        if(taxableValue1>0)
            txt_taxableValue.setText(String.valueOf(taxableValue1));

        txt_gst.setText(String.valueOf(gstValue1));
        Log.d(TAG, "onCreate:TaxableValue"+taxableValue1);
        Log.d(TAG, "onCreate:TotalGSt"+gstValue1);



        try {

            String shippingCharges=(sharedpreferences2.getString("shipping_status", null));
            Log.d(TAG, "onCreate:Shipping"+shippingCharges);
            if(shippingCharges!=null) {
                if (shippingCharges.equalsIgnoreCase("Yes"))
                {

                    linearLayout_shipping_charges.setVisibility(View.VISIBLE);

                } else {
                    linearLayout_shipping_charges.setVisibility(View.GONE);

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
                        if (shipping_value >= 0) {
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
            public void afterTextChanged(Editable s) {

                try {
                    if(s.toString().length()>0)
                    {
                        String shipping = et_shippingCharge.getText().toString().trim();
                        double shipping_value = Double.parseDouble(shipping);
                        double otherCharge = Double.parseDouble(s.toString());
                        if (shipping_value >= 0) {
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


        makeArray();


    }



    private void removeigst()
    {
        ly_cgst.setVisibility(View.VISIBLE);
        ly_sgst.setVisibility(View.VISIBLE);
        ly_igst.setVisibility(View.GONE);
        double s_gst_Value=gstValue/2;
        double  c_gst_Value=gstValue/2;
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

        switch (v.getId())
        {
            case R.id.purchase_choosePayment:
                String   taxablevalue      =txt_taxableValue.getText().toString().trim();
                String  cgst              =txt_cgst.getText().toString().trim();
                String   sgst              =txt_sgst.getText().toString().trim();
                String   igst               =txt_igst.getText().toString().trim();
                String   shipping_charge   = et_shippingCharge.getText().toString().trim();
                String     other_charge    =et_otherCharge.getText().toString().trim();
                 totalAmount=       txt_totalAmount.getText().toString().trim();
                String totalGst            =txt_gst.getText().toString().trim();

                getBillingData(taxablevalue,cgst,igst,sgst,other_charge,totalAmount,totalAmount,shipping_charge,totalGst);
                break;
            default:
                break;

        }


    }


    private void makeArray()
    {

        int i = 0;
        if(billingItems.size()>0)
        {
            for (BillingItems itemDetalisClass : billingItems)
            {
                JSONObject billingDataArray = new JSONObject();
                Log.d("ItemId", itemDetalisClass.getItem_id());
                Log.d("itemName", itemDetalisClass.getItem_name());
                Log.d("stockQty", itemDetalisClass.getStock_qty());
                try
                {
                    billingDataArray.put("item_id", itemDetalisClass.getItem_id());
                    billingDataArray.put("item_name", itemDetalisClass.getItem_name());
                    billingDataArray.put("gst", itemDetalisClass.getItem_gst());
                    billingDataArray.put("qty", itemDetalisClass.getSelected_qty());
                    billingDataArray.put("hsn_code",itemDetalisClass.getHsnCode());
                    billingDataArray.put("total_price", itemDetalisClass.getTotal_Calculated_price());
                    billingDataArray.put("total_gst", itemDetalisClass.getTotal_cal_gst());

                    selected_items.put(i, billingDataArray);
                    i++;
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }else
        {
            Toast.makeText(context, "Item Not Selected", Toast.LENGTH_SHORT).show();

        }


        Log.d(TAG, "makeArray:"+selected_items);

    }




    private JSONObject billingData = new JSONObject();


    private void getBillingData(String taxablevalue, String cgst, String igst, String sgst, String other_charge, String totalAmount, String amount, String shipping_charge, String totalGst)
    {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String u_id = sharedpreferences.getString("userId", "");


        try {
            billingData.put("customer_id", cust_Id);
            billingData.put("cust_number",custMobile);
            billingData.put("cust_name",custName);
            billingData.put("cust_email_id",custmail);
            billingData.put("address",address);
            billingData.put("home_delivery", deliver_status);
            billingData.put("created_by", u_id);
            billingData.put("place_of_supply",supplier);
            billingData.put("taxable_value", taxablevalue);
            billingData.put("total_gst", totalGst);
            billingData.put("igst", igst);
            billingData.put("cgst", cgst);
            billingData.put("shop_name",globalPool.getShopName());
            billingData.put("shop_number",globalPool.getUsermobile());
            billingData.put("gstin",globalPool.getGstNumnebr());
            billingData.put("shop_address",globalPool.getShop_address());
            billingData.put("shop_eid",globalPool.getShop_email());
            billingData.put("sgst",sgst);
            billingData.put("shipping_charges", shipping_charge);
            billingData.put("other_charges", other_charge);
            billingData.put("total", totalAmount);
            billingData.put("itemArray", selected_items);


            Log.d("Object", "" + billingData);
            Log.d("array", "" + selected_items);

            final ProgressDialog loading = ProgressDialog.show(this, "Saving ....", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.CREATEQUOTATION, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    String resp = response.toString().trim();
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        Log.d("RESP", resp);
                        String msg = jsonObject.getString("message");
                        invId=jsonObject.getString("data");
                        Log.d("MSG", msg);
                        if (msg.equalsIgnoreCase("Add data succesfully"))
                        {
                            loading.dismiss();
                            sqlDataBase.deleteItemTable();
                            sendMessage();
                            Intent intent = new Intent(Quatation_taxes.this, Quotation_History.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else
                        {
                            loading.dismiss();
                            Toast.makeText(getApplicationContext(), "Cant Add Data", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    Log.d("Add Succussuflly", resp);




                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    if (error instanceof NoConnectionError)
                    {
                        loading.dismiss();
                        Toast.makeText(Quatation_taxes.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("data", String.valueOf(billingData));
                    params.put("dbname",dbname);
                    params.put("shop_name",globalPool.getShopName());
                    params.put("shop_number",globalPool.getUsermobile());
                    params.put("gstin",globalPool.getGstNumnebr());
                    params.put("shop_address",globalPool.getShop_address());
                    params.put("shop_eid",globalPool.getShop_email());
                    Log.d(TAG, "getParams:"+params.toString());

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }



    public void sendMessage()
    {

        String longUrl= "http://shopmanagment.surge.sh/Shopnotify/?dbname="+globalPool.getDbname()+"&invoice_id="+invId;
        URLShortener.shortUrl(longUrl, new URLShortener.LoadingCallback() {
            @Override
            public void startedLoading() {

            }

            @Override
            public void finishedLoading(@Nullable String shortUrl)
            {
                shortUrl=shortUrl;
                //make sure the string is not null
                Log.d(TAG, "finishedLoading: Link"+shortUrl);
                String message = "Dear Customer, \n" +
                        " Thanks for Your Business \n" +
                        " of Total Amount : Rs"+ totalAmount + " \n  Invoice No : "+invId +"  Invoice Link :"+shortUrl+
                        " \n  Please Visit us again ! \n "+globalPool.getShopName() ;
                String mobile = custMobile;

                String uri = Uri.parse("\n" +
                        "http://bhashsms.com/api/sendmsg.php?")
                        .buildUpon()
                        .appendQueryParameter("user", "TEAM_MHOURZ")
                        .appendQueryParameter("pass", "MECHATRON")
                        .appendQueryParameter("text", message)
                        .appendQueryParameter("sender", "MHOURZ")
                        .appendQueryParameter("phone", mobile)
                        .appendQueryParameter("priority", "ndnd")
                        .appendQueryParameter("stype", "normal")
                        .build().toString();

                Log.d(TAG, "TestMEssage: Uri"+uri);
                final Context context = getApplicationContext();
                StringRequest stringRequest = new StringRequest(uri,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if (response != null)
                                    {
                                        Log.d(TAG, "onResponse: Sms new "+response);

                                        if (response.contains("S."))
                                        {
                                            Toast.makeText(getApplication(), "message sent successfully", Toast.LENGTH_LONG).show();
                                        } else
                                        {
                                            Toast.makeText(getApplication(), "Message couldn't reach you, try again", Toast.LENGTH_LONG).show();
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
        });
//        String message1 = new String();
//
//        String message = "Dear " +custName +"\n" +
//                "Please find Quotation summary "+ "\n"+
//                 " Invoice Link : http://shopmanagment.surge.sh/Shopnotify/?dbname="+globalPool.getDbname()+"&invoice_id="+invId+
//                " Thanks for Your Business \n" +
//                " Your Total Amount :"+ totalAmount + " \n  We hope to see You Soon  \n " +globalPool.getShopName();
//        String mobile = custMobile;
//
//        String uri = Uri.parse("\n" +
//                "http://bhashsms.com/api/sendmsg.php?")
//                .buildUpon()
//                .appendQueryParameter("user", "TEAM_MHOURZ")
//                .appendQueryParameter("pass", "MECHATRON")
//                .appendQueryParameter("text", message)
//                .appendQueryParameter("sender", "MHOURZ")
//                .appendQueryParameter("phone", mobile)
//                .appendQueryParameter("priority", "ndnd")
//                .appendQueryParameter("stype", "normal")
//                .build().toString();
//
//        Log.d(TAG, "TestMEssage: Uri"+uri);
//
//
//        final Context context = getApplicationContext();
//        StringRequest stringRequest = new StringRequest(uri,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            if (response != null) {
//                                JSONObject j = new JSONObject(response);
//                                String type1 = j.getString("type");
//                                if (type1.contains("success"))
//                                {
//                                    Toast.makeText(getApplication(), " message sent successfully", Toast.LENGTH_LONG).show();
//                                } else
//                                {
//                                    Toast.makeText(getApplication(), "Message couldn't reach you, try again", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        } catch (JSONException e) {
//
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(stringRequest);
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

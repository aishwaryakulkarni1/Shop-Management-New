package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.inevitablesol.www.shopmanagement.Billing_paidAmountSuccess;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.pddstudio.urlshortener.URLShortener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SelectModeOFPaymnet extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {


    private static final String TAG = "SelectModeOFPaymnet";
    private static final int CHEQUEDETAILS = 111;
    private Context context = com.inevitablesol.www.shopmanagement.billing_module.SelectModeOFPaymnet.this;
    private ArrayList<BillingItems> billingItems;
    private JSONArray selected_items = new JSONArray();
    private SqlDataBase sqlDataBase;
    private RadioGroup radioGroup;
    private ImageView proceedTopayment;
    private String ModeofPayment;
    private String custMobile;
    private String custemail;
    private String custaddOne;
    private String custId;
    private String h_status;
    private String gstin;
    private String supplier;

    private SharedPreferences sharedpreferences;
    private final String MyPREFERENCES = "MyPrefs";

    private String MOFPayment_shortCut;

    private JSONObject billingData = new JSONObject();

    //
    private String invoiceId, cName, c_id, amount, status, payment_id, desrciption, modeOfpayment, dateTime;
    private String custName;
    private String dbname;
    private int PAYTM = 1;
    private int BHIM = 2;

    private TextView txt_cname;
    private String transactionId = "transactionId";

    private String balanceDue, paidAmount;
    private String modePayment;
    private String Charges;


    private TextView txt_customerAmount;



    private LinearLayout linearLayout_cashPayment;
    private LinearLayout linearLayout_RazorPay, linearLayout_paytm, linearLayout_bhimApp;

    private LinearLayout linearLayout_EMI, linnear_sendLink, linear_bankDetails, linear_cheque;
    LinearLayout linearLayout_payUbiz;
    private GlobalPool globalPool;
    private String totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_billing_new_page);
        txt_cname = (TextView) findViewById(R.id.txt_BcustName);
        txt_customerAmount = (TextView) findViewById(R.id.txt_custTotal);
        sqlDataBase = new SqlDataBase(context);
        globalPool = (GlobalPool) this.getApplication();

        linearLayout_cashPayment = (LinearLayout) findViewById(R.id.ly_cashPayment);
        linearLayout_cashPayment.setOnClickListener(this);
        linearLayout_bhimApp = (LinearLayout) findViewById(R.id.ly_bhim);
        linearLayout_bhimApp.setOnClickListener(this);

        linearLayout_paytm = (LinearLayout) findViewById(R.id.ly_paytm);
        linearLayout_paytm.setOnClickListener(this);
        linearLayout_RazorPay = (LinearLayout) findViewById(R.id.ly_razorpay);
        linearLayout_RazorPay.setOnClickListener(this);

        linearLayout_EMI = (LinearLayout) findViewById(R.id.ly_emi);
        linearLayout_EMI.setOnClickListener(this);

        linear_bankDetails = (LinearLayout) findViewById(R.id.ly_bank_details);
        linear_bankDetails.setOnClickListener(this);
        linnear_sendLink = (LinearLayout) findViewById(R.id.ly_sendLink);
        linnear_sendLink.setOnClickListener(this);

        linear_cheque = (LinearLayout) findViewById(R.id.ly_cheque);
        linear_cheque.setOnClickListener(this);

        linearLayout_payUbiz = (LinearLayout) findViewById(R.id.ly_payBiz);
        linearLayout_payUbiz.setOnClickListener(this);
        Intent intent = getIntent();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));

        if (intent.hasExtra("in_id"))
        {
            invoiceId = intent.getStringExtra("in_id");
            cName = intent.getStringExtra("c_name");
            txt_cname.setText(cName);
            custemail = intent.getStringExtra("email");
            custMobile = intent.getStringExtra("mobile");
            Log.d(TAG, "onCreate: cName" + cName + "email" + custemail + "Mobile" + custMobile);

            c_id = intent.getStringExtra("c_id");
            amount = intent.getStringExtra("amount");
            payment_id = intent.getStringExtra("payment_id");
            status = intent.getStringExtra("status");

            desrciption = intent.getStringExtra("description");
            modePayment = intent.getStringExtra("modeOfPayment");
            Log.d(TAG, "onCreate: Mode of Payment" + modePayment);
            dateTime = intent.getStringExtra("dateTime");
            balanceDue = intent.getStringExtra("balanceDue");
            paidAmount = intent.getStringExtra("paidAmount");
            dbname = intent.getStringExtra("dbname");
            totalAmount=intent.getStringExtra("totalAmount");
            txt_customerAmount.setText(paidAmount);



        } else
            {

            Toast.makeText(this, "No Date Available", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onRestart()
    {
        super.onRestart();
        Log.d(TAG, "onRestart:");


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
    }

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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart:");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_cashPayment:
                linearLayout_cashPayment.setClickable(false);
                MOFPayment_shortCut = "Cash";
                ModeofPayment = "Cash payment";
                Charges = "0";
                updateInvoiceCOD();
                break;
            case R.id.ly_razorpay:
                ModeofPayment = "Razorpay";
                linearLayout_RazorPay.setClickable(false);
                MOFPayment_shortCut = "Razorpay";
                Charges = "1";
                startPayment();
                break;
            case R.id.ly_paytm:
                linearLayout_paytm.setClickable(false);
                ModeofPayment = "Paytm Wallet ";
                MOFPayment_shortCut = "Paytm";
                startActivityForResult((new Intent(SelectModeOFPaymnet.this, Billing_OpenPatmScreen.class)), PAYTM);
                break;
            case R.id.ly_bhim:
                MOFPayment_shortCut = "BHIM";
                ModeofPayment = "BHIM UPI App";
                linearLayout_bhimApp.setClickable(false);
                startActivityForResult((new Intent(context, GetBhimDetails_AddRemaiming.class)), BHIM);
                break;
            case R.id.ly_emi:
                MOFPayment_shortCut = "EMI";
                ModeofPayment = "EMI";
                linearLayout_EMI.setClickable(false);
                getEmiDetails();
                break;
            case R.id.ly_sendLink:
                MOFPayment_shortCut = "PFA";
                ModeofPayment = "Send Payment Gatway Link";
                linnear_sendLink.setClickable(false);
                Charges = "2";
                sendPaymentLinkToCustomer();
                break;
            case R.id.ly_bank_details:
                ModeofPayment = "To Bank Account";
                linear_bankDetails.setClickable(false);
                MOFPayment_shortCut = "Bank";
                getBankDetails();
                break;
            case R.id.ly_cheque:
                MOFPayment_shortCut = "Cheque";
                ModeofPayment = "Cheque Payment";
                linear_cheque.setClickable(false);
                getChequeDetails();
                break;
            case R.id.ly_payBiz:
                MOFPayment_shortCut = "PayUbiz";
                ModeofPayment = "Debit Card Payment (Powered by PayUbiz)";
                linearLayout_payUbiz.setClickable(false);
                Charges = "5";
                break;
            default:
                Toast.makeText(context, "Wrong Choice", Toast.LENGTH_SHORT).show();

        }
    }

    private void getChequeDetails()
    {

        Intent intent=new Intent(context,DuePayment_ChequeDeatils.class);
        intent.putExtra("custMobile",custMobile);
        intent.putExtra("email",custemail);
        intent.putExtra("amountpaid",paidAmount);
        intent.putExtra("balanceDue",balanceDue);
        intent.putExtra("totalAmount",totalAmount);
        intent.putExtra("modeofPayment",ModeofPayment);
        intent.putExtra("transactionId",transactionId);
        intent.putExtra("dbname",dbname);
        intent.putExtra("invoice_id", invoiceId);
        intent.putExtra("paymentId",payment_id);
        intent.putExtra("mode", ModeofPayment);
        intent.putExtra("shortPmode", MOFPayment_shortCut);
        intent.putExtra("charges", Charges);
        startActivity(intent);


    }

    private void getBankDetails()
    {
        Intent intent=new Intent(context,DuePayment_BankDeatils.class);
        intent.putExtra("custMobile",custMobile);
        intent.putExtra("email",custemail);
        intent.putExtra("amountpaid",paidAmount);
        intent.putExtra("balanceDue",balanceDue);
        intent.putExtra("totalAmount",totalAmount);
        intent.putExtra("modeofPayment",ModeofPayment);
        intent.putExtra("transactionId",transactionId);
        intent.putExtra("dbname",dbname);
        intent.putExtra("invoice_id", invoiceId);
        intent.putExtra("paymentId",payment_id);
        intent.putExtra("mode", ModeofPayment);
        intent.putExtra("shortPmode", MOFPayment_shortCut);
        intent.putExtra("charges", Charges);
        startActivity(intent);
    }

    private void getEmiDetails()
    {
        Intent intent=new Intent(context,Due_paymnetEmi.class);
        intent.putExtra("custMobile",custMobile);
        intent.putExtra("email",custemail);
        intent.putExtra("amountpaid",paidAmount);
        intent.putExtra("balanceDue",balanceDue);
        intent.putExtra("totalAmount",totalAmount);
        intent.putExtra("modeofPayment",ModeofPayment);
        intent.putExtra("transactionId",transactionId);
        intent.putExtra("dbname",dbname);
        intent.putExtra("paymentId",payment_id);
        intent.putExtra("invoice_id", invoiceId);
        intent.putExtra("mode", ModeofPayment);
        intent.putExtra("shortPmode", MOFPayment_shortCut);
        intent.putExtra("charges", Charges);
        startActivity(intent);
    }


    private void getBankDetails_Bhim()
    {
        Intent intent=new Intent(context,DuePayment_BhimDetails.class);
        intent.putExtra("custMobile",custMobile);
        intent.putExtra("email",custemail);
        intent.putExtra("amountpaid",paidAmount);
        intent.putExtra("balanceDue",balanceDue);
        intent.putExtra("totalAmount",totalAmount);
        intent.putExtra("modeofPayment",ModeofPayment);
        intent.putExtra("transactionId",transactionId);
        intent.putExtra("dbname",dbname);
        intent.putExtra("invoice_id", invoiceId);
        intent.putExtra("paymentId",payment_id);
        intent.putExtra("mode", ModeofPayment);
        intent.putExtra("shortPmode", MOFPayment_shortCut);
        intent.putExtra("charges", Charges);
        startActivity(intent);
    }

    private void getBankDetails_paytm()
    {
        Intent intent=new Intent(context,DuePayment_Paytm.class);
        intent.putExtra("custMobile",custMobile);
        intent.putExtra("email",custemail);
        intent.putExtra("amountpaid",paidAmount);
        intent.putExtra("balanceDue",balanceDue);
        intent.putExtra("totalAmount",amount);
        intent.putExtra("modeofPayment",ModeofPayment);
        intent.putExtra("transactionId",transactionId);
        intent.putExtra("dbname",dbname);
        intent.putExtra("invoice_id", invoiceId);
        intent.putExtra("paymentId",payment_id);
        intent.putExtra("mode", ModeofPayment);
        intent.putExtra("shortPmode", MOFPayment_shortCut);
        intent.putExtra("charges", Charges);
        startActivity(intent);
    }


    private void updateInvoiceData()
    {
        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyy-MM-dd HH:mm");
        final String formatedInvocie="inv_"+invoiceId+ dateFormat.format(dt);

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(SelectModeOFPaymnet.this).add(new StringRequest(Request.Method.POST, WebApi.INVIOCEAMOUNTPAIDHISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    Log.d(TAG, "onResponse:" + msg);
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    } else
                        {
                        try
                        {
                            sendMessage(invoiceId);
                            loading.dismiss();
                            Intent intent = new Intent(SelectModeOFPaymnet.this, Billing_paidAmountSuccess.class);
                            intent.putExtra("amountpaid",paidAmount);
                            intent.putExtra("balanceDue",balanceDue);
                            intent.putExtra("totalAmount",totalAmount);
                            intent.putExtra("amount",amount);
                            intent.putExtra("modeofPayment",ModeofPayment);
                            intent.putExtra("transactionId",formatedInvocie);

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } catch (NullPointerException e) {

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(SelectModeOFPaymnet.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("invoice_id", invoiceId);
                params.put("mode", ModeofPayment);
                params.put("amount_paid", paidAmount);
                params.put("transno", formatedInvocie);
                params.put("shortPmode", MOFPayment_shortCut);
                params.put("charges", Charges);
                Log.d("getParams:InvocieByID", params.toString());
                return params;
            }


        });
    }


    private void updateInvoiceCOD()
    {
        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyy-MM-dd HH:mm");
        final String formatedInvocie="inv_"+invoiceId+ dateFormat.format(dt);

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(SelectModeOFPaymnet.this).add(new StringRequest(Request.Method.POST, WebApi.INVIOCEAMOUNTPAIDHISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    Log.d(TAG, "onResponse:" + msg);
                    if (message.equalsIgnoreCase("Data not available")) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    } else
                        {
                        try
                        {

                            sendMessage(invoiceId);
                            loading.dismiss();
                            Intent intent = new Intent(SelectModeOFPaymnet.this, Billing_paidAmountSuccess.class);
                            intent.putExtra("amountpaid",paidAmount);
                            intent.putExtra("balanceDue",balanceDue);
                            intent.putExtra("totalAmount",totalAmount);
                            intent.putExtra("amount",amount);
                            intent.putExtra("modeofPayment",ModeofPayment);
                            intent.putExtra("transactionId",formatedInvocie);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Log.d(TAG, "onResponse: Intent"+intent.toString());
                            startActivity(intent);
                            finish();


                        } catch (NullPointerException e) {

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(SelectModeOFPaymnet.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("invoice_id", invoiceId);
                params.put("mode", ModeofPayment);
                params.put("shortPmode", MOFPayment_shortCut);
                params.put("amount_paid", paidAmount);
                params.put("transno", formatedInvocie);
                Log.d("getParams:InvocieByID", params.toString());
                return params;
            }


        });
    }


    public void sendMessage(final String invId)
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
                            public void onResponse(String response)
                            {
                                try
                                {
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
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);

            }
        });

    }


    public void startPayment() {

        String paise = convertRupeeToPaisa(Double.parseDouble(paidAmount));
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        try {
            JSONObject options = new JSONObject();
            options.put("name", "MechaTron TechGear PVT LTD");
            options.put("description", "Bill Payment");
            //You can omit the image option to fetch the image from dashboard
//            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", paise);

            JSONObject preFill = new JSONObject();
            preFill.put("email", custemail);
            preFill.put("contact", custMobile);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {

        try
        {
            this.transactionId = razorpayPaymentID;
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            getBillingDataForRayZorPAy();
        } catch (Exception e)
        {
            Log.e("Razorpay", "Exception in onPaymentSuccess", e);
        }

    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onPaymentError: Code" + code);
            Log.d(TAG, "onPaymentError:Response" + response);
        } catch (Exception e) {
            Log.e("Razorpay", "Exception in onPaymentError", e);
        }

    }


    private void getBillingDataForRayZorPAy() {

        Log.d(TAG, "getBillingDataForRayZorPAy: " + transactionId);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String u_id = sharedpreferences.getString("userId", "");


        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(SelectModeOFPaymnet.this).add(new StringRequest(Request.Method.POST, WebApi.INVIOCEAMOUNTPAIDHISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    Log.d(TAG, "onResponse:" + msg);
                    if (message.equalsIgnoreCase("Data not available")) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    } else {
                        try
                        {
                            sendMessage(invoiceId);
                            loading.dismiss();
                            Intent intent = new Intent(SelectModeOFPaymnet.this, Billing_paidAmountSuccess.class);
                            intent.putExtra("amountpaid",paidAmount);
                            intent.putExtra("balanceDue",balanceDue);
                            intent.putExtra("totalAmount",totalAmount);
                            intent.putExtra("amount",amount);
                            intent.putExtra("modeofPayment",ModeofPayment);
                            intent.putExtra("transactionId",transactionId);



                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();


                        } catch (NullPointerException e) {

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(SelectModeOFPaymnet.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("invoice_id", invoiceId);
                params.put("mode", ModeofPayment);
                params.put("amount_paid", paidAmount);
                params.put("transno", transactionId);
                params.put("shortPmode", MOFPayment_shortCut);
                params.put(" Charges", Charges);
                Log.d("getParams:InvocieByID", params.toString());
                return params;
            }


        });





    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data)
        {

            if(requestCode == PAYTM && resultCode == Activity.RESULT_OK)
            {
                try
                {
                    String status=data.getStringExtra("result");
                    Log.d(TAG, "onActivityResult:"+status);
                    if(status.equalsIgnoreCase("payment done"))
                    {
                        getBankDetails_Paytm();

                        //updateInvoiceData();
                       // getBillingDataForPaytmStatus();
                        Log.d(TAG, "onActivityPATM");

                    }else if(status.equalsIgnoreCase("payment fail"))
                    {
                        String p_status= "payment fail from Paytm";
                        //updateInvoiceData();
                        Toast.makeText(getApplicationContext(),"payment Fail",Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e)
                {
                    Log.d(TAG, "onActivityResult:"+e);
                }




            }else if(requestCode == BHIM  && resultCode == Activity.RESULT_OK)
            {

                try
                {
                    String status=data.getStringExtra("result");
                    Log.d(TAG, "onActivityResult:Bhim Status"+status);

                    if(status.equalsIgnoreCase("payment done"))
                    {

                        getBankDetails_Bhim();
                       // updateInvoiceData();
                       // getBillingDataForPaytmStatus();
                        Toast.makeText(getApplicationContext()," Payment done",Toast.LENGTH_LONG).show();

                    }else if(status.equalsIgnoreCase("payment fail"))
                    {
                        String p_status= "payment fail from Bhim App";
                       // getBillingDataForPaytmStatusfail(p_status);
                        Toast.makeText(getApplicationContext()," Payment Fail",Toast.LENGTH_LONG).show();

                    }

                    //  getBillingDataForPaytmStatus();


                }catch (Exception e)
                {

                }






            }



        }


    private void getBankDetails_Paytm()
    {

        Intent intent=new Intent(context,DuePayment_Paytm.class);
        intent.putExtra("custMobile",custMobile);
        intent.putExtra("email",custemail);
        intent.putExtra("amountpaid",paidAmount);
        intent.putExtra("balanceDue",balanceDue);
        intent.putExtra("totalAmount",totalAmount);
        intent.putExtra("modeofPayment",ModeofPayment);
        intent.putExtra("transactionId",transactionId);
        intent.putExtra("dbname",dbname);
        intent.putExtra("invoice_id", invoiceId);
        intent.putExtra("paymentId",payment_id);
        intent.putExtra("mode", ModeofPayment);
        intent.putExtra("shortPmode", MOFPayment_shortCut);
        intent.putExtra("charges", Charges);
        startActivity(intent);

    }


    private void sendPaymentLinkToCustomer()
        {

            String  amount=convertRupeeToPaisa(Double.parseDouble(paidAmount));
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("email", custemail);
                jsonObject.put("contact", custMobile);
                final JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("customer", jsonObject);
                jsonObject1.put("type", "link");
                jsonObject1.put("view_less", 1);
                jsonObject1.put("amount", Double.parseDouble(amount));
                jsonObject1.put("currency", "INR");
                jsonObject1.put("description", "Payment link for this purpose - xyz.");
                Log.d("SMSLInk", String.valueOf(jsonObject1));

                try
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.SENDMAILAND_SMS, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String resp = response.toString().trim();
                            try
                            {
                                Log.d("smsLinkResponse",resp);
                                JSONObject jsonObject = new JSONObject(resp);
                                String message=     jsonObject.getString("message");
                                if(message.equalsIgnoreCase("SMS Sent succesfully"))
                                {
                                   updateInvoiceData();
                                }else
                                {
                                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof NoConnectionError)
                            {
                                Toast.makeText(SelectModeOFPaymnet.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError
                        {
                            Map<String, String> params = new HashMap<>();
                            params.put("link", String.valueOf(jsonObject1));


                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(stringRequest);

                } catch (Exception e) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



        public String convertRupeeToPaisa(Double rupees)
        {

            double paise;
            String result = new String();

            paise = rupees * 100;

            long ps = (long) paise;
            result = String.valueOf(ps);

            return result;
        }



    }



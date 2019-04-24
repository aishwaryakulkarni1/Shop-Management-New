package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.payuui.Activity.PayUBaseActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Billing_ChoosePaymentMode extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {
    private static final String TAG = "Billing_ChoosePaymentMo";
    private static final int CHEQUEDETAILS = 111;
    private Context context=Billing_ChoosePaymentMode.this;
      private ArrayList<BillingItems> billingItems;
    private JSONArray selected_items = new JSONArray();
    private SqlDataBase sqlDataBase;
    private RadioGroup radioGroup;
    private AppCompatButton proceedTopayment;
    private String ModeofPayment;
    private  String MOFPayment_shortCut;
    private String custMobile;
    private String custemail;
    private String custaddOne;
    private String custId;
    private String h_status;
    private String gstin;
    private String supplier;

   private  SharedPreferences sharedpreferences;
    private  final String MyPREFERENCES = "MyPrefs";

    private JSONObject billingData = new JSONObject();

    //
    private String taxableValue,total_gst,igst,sgst,cgst,other_charges,shipping_charges,amount_paid,balanceDue,totalAmount;
    private String custName;
    private String dbname;
    private int PAYTM=1;
    private  int BHIM=2;

    private TextView txt_cname;
    private  String transactionId=null;


    private static final String MySETTINGS = "MySetting";
    private SharedPreferences sharedpreferences2;

    // Radio Button

    private  RadioButton radioButton_paytm;

    private  RadioButton radioButton_bhim;
    private  RadioButton radioButton_bankDeatials;

    private  RadioButton radioButton_using99;

    private  RadioButton radioButton_check;
    private  RadioButton radioButton_emi;
    private int BANK99=3;

    private  static  boolean duePaymentSms;
    private String message1;
    private String paytm_reminder_days;
    private String invId;


    // pay U classes

    private String merchantKey, userCredentials;

    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;

    // This sets the configuration
    private PayuConfig payuConfig;

    private Spinner environmentSpinner;

    // Used when generating hash from SDK
    private PayUChecksum checksum;

  //  private static final String TAG = "MainActivity";

    public static final String masterClean = "http://146.148.40.15/masterclean/mobilePhp/";
    public static final String HASHGENERATOR = masterClean+"hashGenerator.php";
    public static final String SUCCESS = "http://146.148.40.15/masterclean/mobilePhp/success.php";


    //textView For total
    private  TextView  txt_customerAmount;

    final GlobalPool globalPool = (GlobalPool) this.getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing__choose_payment_mode);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sqlDataBase=new SqlDataBase(context);
        radioGroup = (RadioGroup) findViewById(R.id.rd_getDetails);

        txt_cname=(TextView)findViewById(R.id.txt_BcustName);
        radioButton_paytm=(RadioButton)findViewById(R.id.radiobutton_paytm);
        radioButton_bhim=(RadioButton)findViewById(R.id.radiobutton_bhim);
        radioButton_bankDeatials=(RadioButton)findViewById(R.id.radio_usingBankDeatils);
        radioButton_using99=(RadioButton)findViewById(R.id.radio_usingBank99);
        proceedTopayment = (AppCompatButton) findViewById(R.id.proceedtopayment);
        radioButton_check=(RadioButton)findViewById(R.id.radio_check);
         radioButton_emi=(RadioButton)findViewById(R.id.radio_emi);
        txt_customerAmount=(TextView)findViewById(R.id.txt_custTotal);
        proceedTopayment.setOnClickListener(this);
        Intent intent=getIntent();
        if(intent.hasExtra("name"))
        {

            custName=intent.getStringExtra("name");
            custMobile=intent.getStringExtra("phone");
            custemail=intent.getStringExtra("email");
            custaddOne=intent.getStringExtra("address");
            custId=intent.getStringExtra("custid");
            h_status=intent.getStringExtra("h_status");
            gstin=intent.getStringExtra("gst");
            supplier=intent.getStringExtra("supplier");
            taxableValue=   intent.getStringExtra("taxableValue");
            cgst= intent.getStringExtra("cgst");
            sgst=  intent.getStringExtra("sgst");
            shipping_charges=intent.getStringExtra("shipping_charge");
            igst=  intent.getStringExtra("igst");;
            other_charges=  intent.getStringExtra("other_charge");
            amount_paid=  intent.getStringExtra("amountpaid");
            balanceDue=  intent.getStringExtra("balanceDue");
            totalAmount=  intent.getStringExtra("totalAmount");
            total_gst=intent.getStringExtra("totalGst");
            txt_cname.setText(custName);
            txt_customerAmount.setText(totalAmount);
        }
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
           dbname = (sharedpreferences.getString("dbname", null));

              billingItems= sqlDataBase.getBillingItems();

                      if(globalPool.getGst_status())
                      {
                          Log.d(TAG, "onCreate: IF Status");
                      }else
                      {
                          Log.d(TAG, "onCreate: gst Status Else");
                      }


                       makeArray();


           // pay U Payment Getway


        Payu.setInstance(this);
        proceedTopayment.setEnabled(true);

        sharedpreferences2 = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
            try
            {

                String paytm =(sharedpreferences2.getString("paytm_status", null));

                Log.d(TAG, "onCreate:Paytm Status"+paytm);
                if(paytm !=null)
                {
                    if(paytm.equalsIgnoreCase("Yes"))
                    {
                       radioButton_paytm.setVisibility(View.VISIBLE);

                    }else
                    {
                        radioButton_paytm.setVisibility(View.GONE);

                    }

                }

            }catch (NullPointerException e)
            {

            }
           //sendEmail();

        try
        {

             paytm_reminder_days =(sharedpreferences2.getString("wishList_paymentReminder_Days", null));
            String paytm_reminder =(sharedpreferences2.getString("wishList_paymentReminder", null));
            String smsDue =(sharedpreferences2.getString("makeBill_sms", null));

            Log.d(TAG, "onCreate:Paytm reminder "+paytm_reminder);
            Log.d(TAG, "onCreate:Paytm reminder Days"+paytm_reminder_days);
            Log.d(TAG, "onCreate:make bill sms Due"+smsDue);

             if(paytm_reminder!=null)
             {
                  if(paytm_reminder.equalsIgnoreCase("yes"))
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



        try
        {

            String using99 =(sharedpreferences2.getString("bank_99", null));
            Log.d(TAG, "onCreate:99 Status"+using99);
            if(using99 !=null)
            {
                if(using99.equalsIgnoreCase("Yes"))
                {
                    radioButton_using99.setVisibility(View.VISIBLE);

                }else
                {
                    radioButton_using99.setVisibility(View.GONE);

                }

            }

        }catch (NullPointerException e)
        {

        }


        try
        {

            String  bhim  =(sharedpreferences2.getString("bhim_status", null));
            Log.d(TAG, "onCreate:Bhim Status"+bhim);
            if(bhim !=null)
            {
                if(bhim.equalsIgnoreCase("Yes"))
                {
                    radioButton_bhim.setVisibility(View.VISIBLE);

                }else
                {
                    radioButton_bhim.setVisibility(View.GONE);

                }

            }

        }catch (NullPointerException e)
        {

        }


        try
        {

            String  bankDeatils  =(sharedpreferences2.getString("bank_details", null));
            Log.d(TAG, "onCreate:Bank Status"+bankDeatils);
            if(bankDeatils !=null)
            {
                if(bankDeatils.equalsIgnoreCase("Yes"))
                {
                    radioButton_bankDeatials.setVisibility(View.VISIBLE);

                }else
                {
                    radioButton_bankDeatials.setVisibility(View.GONE);

                }

            }

        }catch (NullPointerException e)
        {

        }



        try
        {

            String  bank_check  =(sharedpreferences2.getString("bank_check", null));
            Log.d(TAG, "onCreate:Bank Status"+bank_check);
            if(bank_check !=null)
            {
                if(bank_check.equalsIgnoreCase("Yes"))
                {
                    radioButton_check.setVisibility(View.VISIBLE);

                }else
                {
                    radioButton_check.setVisibility(View.GONE);

                }

            }

        }catch (NullPointerException e)
        {

        }


        try
        {

            String  bankemi  =(sharedpreferences2.getString("bank_emi", null));
            Log.d(TAG, "onCreate:Bank emi Status"+bankemi);
            if(bankemi !=null)
            {
                if(bankemi.equalsIgnoreCase("Yes"))
                {
                    radioButton_emi.setVisibility(View.VISIBLE);

                }else
                {
                   radioButton_emi.setVisibility(View.GONE);

                }

            }

        }catch (NullPointerException e)
        {

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
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart:");
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

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.proceedtopayment:
                saveData();
                break;
        }

    }

    private void saveData()
    {
        try {

             proceedTopayment.setEnabled(false);
            Log.d(TAG, "saveData: Button Status"+proceedTopayment.isEnabled());
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            if (radioButton != null)
            {
                ModeofPayment = radioButton.getText().toString().trim();

                Log.d(TAG, "saveData:" + ModeofPayment);

                 if(ModeofPayment.equalsIgnoreCase("Debit Card payment (PayU)"))
                 {
                     MOFPayment_shortCut="PayUbiz";
                     navigateToBaseActivity();

                 }

                else if (ModeofPayment.equalsIgnoreCase("Paytm"))
                {
                    MOFPayment_shortCut="Paytm";

                    startActivityForResult((new Intent(Billing_ChoosePaymentMode.this, Billing_OpenPatmScreen.class)), PAYTM);
//                    startActivity(new Intent(Billing_ChoosePaymentMode.this, Billing_OpenPatmScreen.class));
                } else if(ModeofPayment.equalsIgnoreCase("Cash payment") )
                {
                    MOFPayment_shortCut="Cash";
                    getBillingDataForStatusCashPayment();
                }else if( ModeofPayment.equalsIgnoreCase("Debit Card payment"))
                {
                    MOFPayment_shortCut="DC";

                    startPayment();
                   // getBillingDataForStatus();

                }else if( ModeofPayment.equalsIgnoreCase("Net Banking"))
                {
                    MOFPayment_shortCut="NB";
                    startPayment();
                    // getBillingDataForStatus();

                }
                else if( ModeofPayment.equalsIgnoreCase("Credit  Card payment"))
                {
                    MOFPayment_shortCut="CC";
                    startPayment();
                }
                else if( ModeofPayment.equalsIgnoreCase("E Wallet"))
                {
                    MOFPayment_shortCut="E Wallet";
                    startPayment();

                }else if(ModeofPayment.equalsIgnoreCase("Bhim App"))
                {
                    MOFPayment_shortCut="BHIM";

                    startActivityForResult((new Intent(context,GetBhimDetails.class)),BHIM);

                }
                else if (ModeofPayment.equalsIgnoreCase("By Cheque"))
                {
                    MOFPayment_shortCut="Cheque";
                    _getChequeDetails();

                } else if (ModeofPayment.equalsIgnoreCase("EMI"))
                {
                    MOFPayment_shortCut="EMI";
                    _getInvIdForEmi();

                }
                else if (ModeofPayment.equalsIgnoreCase("Using Banking details"))
                {
                    MOFPayment_shortCut="Bank";

                    _getBankDetails();

                }
                else if(ModeofPayment.equalsIgnoreCase("Send payment gateway link"))
                {

                    MOFPayment_shortCut="PFA";

                    sendPaymentLinkToCustomer();
                }
                else if(ModeofPayment.equalsIgnoreCase("Using *99#"))
                {
                    MOFPayment_shortCut="*99#";
                    Intent intent=new Intent(context,Billing_BankDetails99.class);
                    intent.putExtra("taxableValue",taxableValue);
                     intent.putExtra("custMobile",custMobile);
                    intent.putExtra("totalgst",total_gst);
                    intent.putExtra("otherCharges",other_charges);
                    intent.putExtra("shippingCharges",shipping_charges);
                    intent.putExtra("amountpaid",amount_paid);
                    intent.putExtra("balanceDue",balanceDue);
                    intent.putExtra("totalAmount",totalAmount);
                    intent.putExtra("modeofPayment",ModeofPayment);
                    intent.putExtra("transactionId",transactionId);
                    intent.putExtra("data", String.valueOf(billingData));
                    intent.putExtra("dbname",dbname);
                    startActivityForResult(intent, BANK99);
                }
               else if (  ModeofPayment.equalsIgnoreCase("Freecharge") || ModeofPayment.equalsIgnoreCase("Mobikwik") || ModeofPayment.equalsIgnoreCase("UPI apps"))
                {
                    MOFPayment_shortCut=ModeofPayment;

                    startPayment();
                }else
            {
                Toast.makeText(context, "Other", Toast.LENGTH_SHORT).show();
            }
            } else
            {
                Toast.makeText(context, "Select At least One payment Type", Toast.LENGTH_SHORT).show();
            }
        }catch (NullPointerException e)
        {
            Toast.makeText(context, "Please Refresh Screen", Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {
            Toast.makeText(context, "Please Refresh Screen", Toast.LENGTH_SHORT).show();
        }


    }




    public void navigateToBaseActivity()
    {


        // merchantKey="";
        merchantKey = "F7qyGi";//((EditText) findViewById(R.id.editTextMerchantKey)).getText().toString();
        String amount = totalAmount;//((EditText) findViewById(R.id.editTextAmount)).getText().toString();
        String email = custemail;//((EditText) findViewById(R.id.editTextEmail)).getText().toString();

//        String value = String.valueOf(PayuConstants.STAGING_ENV);//environmentSpinner.getSelectedItem().toString();
       int environment;
//        String TEST_ENVIRONMENT = getResources().getString(R.string.test);
//        if (value.equals(TEST_ENVIRONMENT))
//            environment = PayuConstants.STAGING_ENV;
//        else
            environment = PayuConstants.PRODUCTION_ENV;

        userCredentials = merchantKey + ":" + email;
        Log.d(TAG, "navigateToBaseActivity: Emai"+userCredentials);


        //TODO Below are mandatory params for hash genetation
        mPaymentParams = new PaymentParams();
        /**
         * For Test Environment, merchantKey = please contact mobile.integration@payu.in with your app name and registered email id

         */
        mPaymentParams.setKey(merchantKey);
        mPaymentParams.setAmount(amount);
        mPaymentParams.setProductInfo("M-HOURZ");
        mPaymentParams.setFirstName(custName);
        mPaymentParams.setEmail(custemail);
        mPaymentParams.setPhone(custMobile);


        /*
        * Transaction Id should be kept unique for each transaction.
        * */
        mPaymentParams.setTxnId("" + System.currentTimeMillis());

        /**
         * Surl --> Success url is where the transaction response is posted by PayU on successful transaction
         * Furl --> Failre url is where the transaction response is posted by PayU on failed transaction
         */
        // mPaymentParams.setSurl(masterClean+"success.php");
        mPaymentParams.setSurl(SUCCESS);
        mPaymentParams.setFurl("https://payu.herokuapp.com/failure");
        mPaymentParams.setNotifyURL(mPaymentParams.getSurl());  //for lazy pay

        /*
         * udf1 to udf5 are options params where you can pass additional information related to transaction.
         * If you don't want to use it, then send them as empty string like, udf1=""
         * */
        mPaymentParams.setUdf1("udf1");
        mPaymentParams.setUdf2("udf2");
        mPaymentParams.setUdf3("udf3");
        mPaymentParams.setUdf4("udf4");
        mPaymentParams.setUdf5("udf5");

        /**
         * These are used for store card feature. If you are not using it then user_credentials = "default"
         * user_credentials takes of the form like user_credentials = "merchant_key : user_id"
         * here merchant_key = your merchant key,
         * user_id = unique id related to user like, email, phone number, etc.
         * */
        mPaymentParams.setUserCredentials(userCredentials);

        //TODO Pass this param only if using offer key
        //mPaymentParams.setOfferKey("cardnumber@8370");

        //TODO Sets the payment environment in PayuConfig object
        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);
        //   payuConfig.setEnvironment(PayuConstants.MOBILE_STAGING_ENV);
        //TODO It is recommended to generate hash from server only. Keep your key and salt in server side hash generation code.
        generateHashFromServer(mPaymentParams);

        /**
         * Below approach for generating hash is not recommended. However, this approach can be used to test in PRODUCTION_ENV
         * if your server side hash generation code is not completely setup. While going live this approach for hash generation
         * should not be used.
         * */
        //   String salt = "";
        //  generateHashFromSDK(mPaymentParams, salt);

    }


    public void generateHashFromServer(PaymentParams mPaymentParams) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayuConstants.KEY, mPaymentParams.getKey()));
        postParamsBuffer.append(concatParams(PayuConstants.AMOUNT, mPaymentParams.getAmount()));
        postParamsBuffer.append(concatParams(PayuConstants.TXNID, mPaymentParams.getTxnId()));
        postParamsBuffer.append(concatParams(PayuConstants.EMAIL, null == mPaymentParams.getEmail() ? "" : mPaymentParams.getEmail()));
        postParamsBuffer.append(concatParams(PayuConstants.PRODUCT_INFO, mPaymentParams.getProductInfo()));
        postParamsBuffer.append(concatParams(PayuConstants.FIRST_NAME, null == mPaymentParams.getFirstName() ? "" : mPaymentParams.getFirstName()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF1, mPaymentParams.getUdf1() == null ? "" : mPaymentParams.getUdf1()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF2, mPaymentParams.getUdf2() == null ? "" : mPaymentParams.getUdf2()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF3, mPaymentParams.getUdf3() == null ? "" : mPaymentParams.getUdf3()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF4, mPaymentParams.getUdf4() == null ? "" : mPaymentParams.getUdf4()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF5, mPaymentParams.getUdf5() == null ? "" : mPaymentParams.getUdf5()));
        postParamsBuffer.append(concatParams(PayuConstants.USER_CREDENTIALS, mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials()));

        // for offer_key
        if (null != mPaymentParams.getOfferKey())
            postParamsBuffer.append(concatParams(PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey()));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }


    private class GetHashesFromServerTask extends AsyncTask<String, String, PayuHashes> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Billing_ChoosePaymentMode.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected PayuHashes doInBackground(String... postParams)
        {
            Log.d(TAG, "doInBackground:"+postParams);
            PayuHashes payuHashes = new PayuHashes();
            try {

                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL(HASHGENERATOR);

                // get the payuConfig first
                String postParam = postParams[0];
                Log.d(TAG, "doInBackground: Key Parameter "+postParam);

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        //TODO Below three hashes are mandatory for payment flow and needs to be generated at merchant server
                        /**
                         * Payment hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_hash -
                         *
                         * sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||SALT)
                         *
                         */
                        case "payment_hash":
                            payuHashes.setPaymentHash(response.getString(key));
                            break;
                        /**
                         * vas_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating vas_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be "default"
                         *
                         */
                        case "vas_for_mobile_sdk_hash":
                            payuHashes.setVasForMobileSdkHash(response.getString(key));
                            break;
                        /**
                         * payment_related_details_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_related_details_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "payment_related_details_for_mobile_sdk_hash":
                            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(response.getString(key));
                            break;

                        //TODO Below hashes only needs to be generated if you are using Store card feature
                        /**
                         * delete_user_card_hash is used while deleting a stored card.
                         * Below is formula for generating delete_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "delete_user_card_hash":
                            payuHashes.setDeleteCardHash(response.getString(key));
                            break;
                        /**
                         * get_user_cards_hash is used while fetching all the cards corresponding to a user.
                         * Below is formula for generating get_user_cards_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "get_user_cards_hash":
                            payuHashes.setStoredCardsHash(response.getString(key));
                            break;
                        /**
                         * edit_user_card_hash is used while editing details of existing stored card.
                         * Below is formula for generating edit_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "edit_user_card_hash":
                            payuHashes.setEditCardHash(response.getString(key));
                            break;
                        /**
                         * save_user_card_hash is used while saving card to the vault
                         * Below is formula for generating save_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "save_user_card_hash":
                            payuHashes.setSaveCardHash(response.getString(key));
                            break;

                        //TODO This hash needs to be generated if you are using any offer key
                        /**
                         * check_offer_status_hash is used while using check_offer_status api
                         * Below is formula for generating check_offer_status_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be Offer Key.
                         *
                         */
                        case "check_offer_status_hash":
                            payuHashes.setCheckOfferStatusHash(response.getString(key));
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return payuHashes;
        }

        @Override
        protected void onPostExecute(PayuHashes payuHashes) {
            super.onPostExecute(payuHashes);

            progressDialog.dismiss();
            launchSdkUI(payuHashes);
        }
    }

    public void launchSdkUI(PayuHashes payuHashes) {

        Intent intent = new Intent(this, PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);

        //Lets fetch all the one click card tokens first
        startActivityForResult(intent,PayuConstants.PAYU_REQUEST_CODE);

    }




    private void Get_99Detials()
    {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String u_id = sharedpreferences.getString("userId", "");


        try
        {
            billingData.put("customer_id", custId);
            billingData.put("cust_number",custMobile);
            billingData.put("cust_name",custName);
            billingData.put("cust_email_id",custemail);
            billingData.put("address",custaddOne);
            billingData.put("home_delivery", h_status);
            billingData.put("created_by", u_id);
            billingData.put("place_of_supply",supplier);
            billingData.put("taxable_value", taxableValue);
            billingData.put("total_gst", total_gst);
            billingData.put("igst", igst);
            billingData.put("cgst", cgst);
            billingData.put("sgst",sgst);
            billingData.put("shipping_charges", shipping_charges);
            billingData.put("other_charges", other_charges);
            billingData.put("total", totalAmount);
            billingData.put("balance_due", balanceDue);
            billingData.put("amount_paid", amount_paid);
            billingData.put("mode_of_payment", ModeofPayment);
            billingData.put("itemArray", selected_items);
            billingData.put("description","Not Upddated");
            billingData.put("amount",totalAmount);
            billingData.put("transno","not applicable");



            Log.d("Object", "" + billingData);
            Log.d("array", "" + selected_items);



            Intent intent=new Intent(context,Billing_BankDetails99.class);
            intent.putExtra("taxableValue",taxableValue);
            intent.putExtra("totalgst",total_gst);
            intent.putExtra("otherCharges",other_charges);
            intent.putExtra("shippingCharges",shipping_charges);
            intent.putExtra("amountpaid",amount_paid);
            intent.putExtra("balanceDue",balanceDue);
            intent.putExtra("totalAmount",totalAmount);
            intent.putExtra("modeofPayment",ModeofPayment);
            intent.putExtra("transactionId",transactionId);
            intent.putExtra("data", String.valueOf(billingData));
            intent.putExtra("dbname",dbname);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();





        } catch (JSONException e)
        {
            e.printStackTrace();
        }


    }

    private void _getBankDetails()
    {


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String u_id = sharedpreferences.getString("userId", "");
        try
        {
            billingData.put("customer_id", custId);
            billingData.put("cust_number",custMobile);
            billingData.put("cust_name",custName);
            billingData.put("cust_email_id",custemail);
            billingData.put("address",custaddOne);
            billingData.put("home_delivery", h_status);
            billingData.put("created_by", u_id);
            billingData.put("place_of_supply",supplier);
            billingData.put("taxable_value", taxableValue);
            billingData.put("total_gst", total_gst);
            billingData.put("igst", igst);
            billingData.put("cgst", cgst);
            billingData.put("sgst",sgst);
            billingData.put("shipping_charges", shipping_charges);
            billingData.put("other_charges", other_charges);
            billingData.put("total", totalAmount);
            billingData.put("balance_due", balanceDue);
            billingData.put("amount_paid", amount_paid);
            billingData.put("mode_of_payment", ModeofPayment);
            billingData.put("shortPmode",MOFPayment_shortCut);
            billingData.put("itemArray", selected_items);
            billingData.put("description","Not Upddated");
            billingData.put("amount",totalAmount);
            billingData.put("transno","not applicable");



            Log.d("Object", "" + billingData);
            Log.d("array", "" + selected_items);


            Intent intent=new Intent(context,Billing_BankDetails.class);
                                      intent.putExtra("taxableValue",taxableValue);
                                           intent.putExtra("custMobile",custMobile);
                                           intent.putExtra("totalgst",total_gst);
                                           intent.putExtra("otherCharges",other_charges);
                                           intent.putExtra("shippingCharges",shipping_charges);
                                           intent.putExtra("amountpaid",amount_paid);
                                           intent.putExtra("balanceDue",balanceDue);
                                           intent.putExtra("totalAmount",totalAmount);
                                           intent.putExtra("email",custemail);
                                           intent.putExtra("modeofPayment",ModeofPayment);
                                           intent.putExtra("transactionId",transactionId);
                                           intent.putExtra("data", String.valueOf(billingData));
                                           intent.putExtra("dbname",dbname);
                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(intent);
                               finish();





        } catch (JSONException e)
        {
            e.printStackTrace();
        }





    }

    private void _getChequeDetails()
    {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String u_id = sharedpreferences.getString("userId", "");


        try
        {
            billingData.put("customer_id", custId);
            billingData.put("cust_number",custMobile);
            billingData.put("cust_name",custName);
            billingData.put("cust_email_id",custemail);
            billingData.put("address",custaddOne);
            billingData.put("home_delivery", h_status);
            billingData.put("created_by", u_id);
            billingData.put("place_of_supply",supplier);
            billingData.put("taxable_value", taxableValue);
            billingData.put("total_gst", total_gst);
            billingData.put("igst", igst);
            billingData.put("cgst", cgst);
            billingData.put("sgst",sgst);
            billingData.put("shipping_charges", shipping_charges);
            billingData.put("other_charges", other_charges);
            billingData.put("total", totalAmount);
            billingData.put("balance_due", balanceDue);
            billingData.put("amount_paid", amount_paid);
            billingData.put("mode_of_payment", ModeofPayment);
            billingData.put("shortPmode",MOFPayment_shortCut);
            billingData.put("itemArray", selected_items);
            billingData.put("description","Not Upddated");
            billingData.put("amount",totalAmount);
            billingData.put("transno","not applicable");



            Log.d("Object", "" + billingData);
            Log.d("array", "" + selected_items);


            Intent intent=new Intent(context,Billing_ChequeDetails.class);

            intent.putExtra("taxableValue",taxableValue);
            intent.putExtra("custMobile",custMobile);
            intent.putExtra("totalgst",total_gst);
            intent.putExtra("otherCharges",other_charges);
            intent.putExtra("shippingCharges",shipping_charges);
            intent.putExtra("amountpaid",amount_paid);
            intent.putExtra("balanceDue",balanceDue);
            intent.putExtra("totalAmount",totalAmount);
            intent.putExtra("modeofPayment",ModeofPayment);
            intent.putExtra("transactionId",transactionId);
            intent.putExtra("data", String.valueOf(billingData));
            intent.putExtra("dbname",dbname);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();


        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void _getInvIdForEmi()
    {



        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String u_id = sharedpreferences.getString("userId", "");


        try
        {
            billingData.put("customer_id", custId);
            billingData.put("cust_number",custMobile);
            billingData.put("cust_name",custName);
            billingData.put("cust_email_id",custemail);
            billingData.put("address",custaddOne);
            billingData.put("home_delivery", h_status);
            billingData.put("created_by", u_id);
            billingData.put("place_of_supply",supplier);
            billingData.put("taxable_value", taxableValue);
            billingData.put("total_gst", total_gst);
            billingData.put("igst", igst);
            billingData.put("cgst", cgst);
            billingData.put("sgst",sgst);
            billingData.put("shipping_charges", shipping_charges);
            billingData.put("other_charges", other_charges);
            billingData.put("total", totalAmount);
            billingData.put("balance_due", balanceDue);
            billingData.put("amount_paid", amount_paid);
            billingData.put("mode_of_payment", ModeofPayment);
            billingData.put("shortPmode",MOFPayment_shortCut);
            billingData.put("itemArray", selected_items);
            billingData.put("description","Not Upddated");
            billingData.put("amount",totalAmount);
            billingData.put("transno","not applicable");



            Log.d("Object", "" + billingData);
            Log.d("array", "" + selected_items);


            Intent intent=new Intent(context,Billing_EmiDetails.class);
            intent.putExtra("taxableValue",taxableValue);
            intent.putExtra("custMobile",custMobile);
            intent.putExtra("totalgst",total_gst);
            intent.putExtra("otherCharges",other_charges);
            intent.putExtra("shippingCharges",shipping_charges);
            intent.putExtra("amountpaid",amount_paid);
            intent.putExtra("balanceDue",balanceDue);
            intent.putExtra("totalAmount",totalAmount);
            intent.putExtra("modeofPayment",ModeofPayment);
            intent.putExtra("transactionId",transactionId);
            intent.putExtra("data", String.valueOf(billingData));
           // intent.putExtra("transactionId",invId);
            intent.putExtra("dbname",dbname);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();




        } catch (JSONException e)
        {
            e.printStackTrace();
        }



    }

    private void getBillingData()
    {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String u_id = sharedpreferences.getString("userId", "");


        try {
            billingData.put("customer_id", custId);
            billingData.put("cust_number",custMobile);
            billingData.put("cust_name",custName);
            billingData.put("cust_email_id",custemail);
            billingData.put("address",custaddOne);
            billingData.put("home_delivery", h_status);
            billingData.put("created_by", u_id);
            billingData.put("place_of_supply",supplier);
            billingData.put("taxable_value", taxableValue);
            billingData.put("total_gst", total_gst);
            billingData.put("igst", igst);
            billingData.put("cgst", cgst);
            billingData.put("sgst",sgst);
            billingData.put("shipping_charges", shipping_charges);
            billingData.put("other_charges", other_charges);
            billingData.put("total", totalAmount);
            billingData.put("balance_due", balanceDue);
            billingData.put("amount_paid", amount_paid);
            billingData.put("mode_of_payment", ModeofPayment);
            billingData.put("shortPmode",MOFPayment_shortCut);
            billingData.put("itemArray", selected_items);
            billingData.put("description","Not Upddated");
            billingData.put("amount",totalAmount);
            billingData.put("transno","not applicable");




            Log.d("Object", "" + billingData);
            Log.d("array", "" + selected_items);

           // final ProgressDialog loading = ProgressDialog.show(this, "Saving ....", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.CREATEBILL, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    String resp = response.toString().trim();
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        Log.d("RESP", resp);
                        String msg = jsonObject.getString("message");
                        Log.d("MSG", msg);
                        invId=jsonObject.getString("data");
                        if (msg.equalsIgnoreCase("Add data succesfully"))
                        {
                          //  loading.dismiss();
                            sqlDataBase.deleteDataBase_ItemTable();
                            if(duePaymentSms)
                            {
                                if(Double.parseDouble(amount_paid)>0)
                                {
                                    sendDue_payment_Sms();
                                }
                            }
                             sendMessage(invId);

                            Intent intent = new Intent(Billing_ChoosePaymentMode.this, Billing_OnSuccessCash_PaymentMode.class);
                             intent.putExtra("taxableValue",taxableValue);
                             intent.putExtra("totalgst",total_gst);
                             intent.putExtra("otherCharges",other_charges);
                             intent.putExtra("shippingCharges",shipping_charges);
                             intent.putExtra("amountpaid",amount_paid);
                             intent.putExtra("balanceDue",balanceDue);
                             intent.putExtra("totalAmount",totalAmount);
                             intent.putExtra("transactionId","transactionId");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();


//                            Intent intent = new Intent(Billing_ChoosePaymentMode.this, BillingHistory.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
                           // finish();
                        } else
                        {
                            //loading.dismiss();
                            Toast.makeText(getApplicationContext(), "Cant Add Data", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    Log.d("Add Succussuflly", resp);




                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    if (error instanceof NoConnectionError)
                    {
                        //loading.dismiss();
                        Toast.makeText(Billing_ChoosePaymentMode.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("data", String.valueOf(billingData));
                    params.put("dbname",dbname);
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




    private void getBillingDataForStatus()
    {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String u_id = sharedpreferences.getString("userId", "");


        try {
            billingData.put("customer_id", custId);
            billingData.put("cust_number",custMobile);
            billingData.put("cust_name",custName);
            billingData.put("cust_email_id",custemail);
            billingData.put("address",custaddOne);
            billingData.put("home_delivery", h_status);
            billingData.put("created_by", u_id);
            billingData.put("place_of_supply",supplier);
            billingData.put("taxable_value", taxableValue);
            billingData.put("total_gst", total_gst);
            billingData.put("igst", igst);
            billingData.put("cgst", cgst);
            billingData.put("sgst",sgst);
            billingData.put("shipping_charges", shipping_charges);
            billingData.put("other_charges", other_charges);
            billingData.put("total", totalAmount);
            billingData.put("balance_due", balanceDue);
            billingData.put("amount_paid", amount_paid);
            billingData.put("mode_of_payment", ModeofPayment);
            billingData.put("shortPmode",MOFPayment_shortCut);
            billingData.put("itemArray", selected_items);
            billingData.put("description","Not Upddated");
            billingData.put("amount",totalAmount);
            billingData.put("transno",transactionId);



            Log.d("Object", "" + billingData);
            Log.d("array", "" + selected_items);

            // final ProgressDialog loading = ProgressDialog.show(this, "Saving ....", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.CREATEBILL, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    String resp = response.toString().trim();
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        Log.d("RESP", resp);
                        String msg = jsonObject.getString("message");
                        Log.d("MSG", msg);
                        invId=jsonObject.getString("data");
                        if (msg.equalsIgnoreCase("Add data succesfully"))
                        {
                            //  loading.dismiss();
                            sqlDataBase.deleteDataBase_ItemTable();
                            if(duePaymentSms)
                            {
                                if(Double.parseDouble(amount_paid)>0)
                                {
                                    sendDue_payment_Sms();
                                }
                            }
                            sendMessage(invId);

                            Intent intent = new Intent(Billing_ChoosePaymentMode.this, Billing_OnSuccessCash_PaymentMode.class);
                            intent.putExtra("taxableValue",taxableValue);
                            intent.putExtra("totalgst",total_gst);
                            intent.putExtra("otherCharges",other_charges);
                            intent.putExtra("shippingCharges",shipping_charges);
                            intent.putExtra("amountpaid",amount_paid);
                            intent.putExtra("balanceDue",balanceDue);
                            intent.putExtra("totalAmount",totalAmount);
                            intent.putExtra("modeofPayment",ModeofPayment);
                            intent.putExtra("transactionId",invId);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();


//                           Intent intent = new Intent(Billing_ChoosePaymentMode.this, BillingHistory.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
                        } else
                        {
                            //loading.dismiss();
                            Toast.makeText(getApplicationContext(), "Cant Add Data", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    Log.d("Add Succussuflly", resp);




                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    if (error instanceof NoConnectionError)
                    {
                        //loading.dismiss();
                        Toast.makeText(Billing_ChoosePaymentMode.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("data", String.valueOf(billingData));
                    params.put("dbname",dbname);
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



    private void getBillingDataForStatusCashPayment()
    {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String u_id = sharedpreferences.getString("userId", "");


        try {
            billingData.put("customer_id", custId);
            billingData.put("cust_number",custMobile);
            billingData.put("cust_name",custName);
            billingData.put("cust_email_id",custemail);
            billingData.put("address",custaddOne);
            billingData.put("home_delivery", h_status);
            billingData.put("created_by", u_id);
            billingData.put("place_of_supply",supplier);
            billingData.put("taxable_value", taxableValue);
            billingData.put("total_gst", total_gst);
            billingData.put("igst", igst);
            billingData.put("cgst", cgst);
            billingData.put("sgst",sgst);
            billingData.put("shipping_charges", shipping_charges);
            billingData.put("other_charges", other_charges);
            billingData.put("total", totalAmount);
            billingData.put("balance_due", balanceDue);
            billingData.put("amount_paid", amount_paid);
            billingData.put("mode_of_payment", ModeofPayment);
            billingData.put("shortPmode",MOFPayment_shortCut);
            billingData.put("itemArray", selected_items);
            billingData.put("description","Not Upddated");
            billingData.put("amount",totalAmount);
            billingData.put("transno","cash Payment");



            Log.d("Object", "" + billingData);
            Log.d("array", "" + selected_items);

            // final ProgressDialog loading = ProgressDialog.show(this, "Saving ....", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.CREATEBILL, new Response.Listener<String>()
            {
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
                            //  loading.dismiss();
                            sqlDataBase.deleteDataBase_ItemTable();

                            if(duePaymentSms)
                            {
                                if(Double.parseDouble(amount_paid)>0)
                                {
                                    sendDue_payment_Sms();
                                }
                            }
                            sendMessage(invId);

                            Intent intent = new Intent(Billing_ChoosePaymentMode.this, Billing_OnSuccessCash_PaymentMode.class);
                            intent.putExtra("taxableValue",taxableValue);
                            intent.putExtra("totalgst",total_gst);
                            intent.putExtra("otherCharges",other_charges);
                            intent.putExtra("shippingCharges",shipping_charges);
                            intent.putExtra("amountpaid",amount_paid);
                            intent.putExtra("balanceDue",balanceDue);
                            intent.putExtra("totalAmount",totalAmount);
                            intent.putExtra("modeofPayment",ModeofPayment);
                            intent.putExtra("transactionId",invId);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();


//                            Intent intent = new Intent(Billing_ChoosePaymentMode.this, BillingHistory.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
                        } else
                        {
                            //loading.dismiss();
                            Toast.makeText(getApplicationContext(), "Cant Add Data", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    Log.d("Add Succussuflly", resp);




                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    if (error instanceof NoConnectionError)
                    {
                        //loading.dismiss();
                        Toast.makeText(Billing_ChoosePaymentMode.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("data", String.valueOf(billingData));
                    params.put("dbname",dbname);
                    Log.d(TAG, "getParams:"+params.toString());

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }



    }

    private void sendDue_payment_Sms()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, Integer.parseInt(paytm_reminder_days));
        Date result = cal.getTime();

        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(result);
        Log.d(TAG, "onClick: Result"+currentDateTimeString);


        String message = "Reminder: Your  Payment Due !\n -" +
                    " of Rs<" +  balanceDue+ "> ON :-<" + currentDateTimeString + "> \n-" +
                    "Thanks You";
            try {
                message1 = URLEncoder.encode(message, "utf-8");
            } catch (UnsupportedEncodingException e) {

            }
            // schtime=2016-04-09 15:40:14
            String mobile_no =custMobile;//tv_custNumber.getText().toString().trim();
            String sender = "MHOURZ";
            String route = "4";
            String country = "91";
            String json = "json";
            String schtime = currentDateTimeString +" "+"15:45:20";
            final String uri = Uri.parse("http://api.msg91.com/api/sendhttp.php?")
                    .buildUpon()
                    .appendQueryParameter("authkey", "105038AUHubywkcg56c18998")
                    .appendQueryParameter("mobiles", mobile_no)
                    .appendQueryParameter("message", message1)
                    .appendQueryParameter("sender", sender)
                    .appendQueryParameter("route", route)
                    .appendQueryParameter("country", country)
                    .appendQueryParameter("response", json)
                    .appendQueryParameter("schtime", schtime)
                    .build().toString();


            StringRequest stringRequest = new StringRequest(uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (response != null) {
                                    JSONObject j = new JSONObject(response);
                                    String type1 = j.getString("type");
                                    if (type1.contains("success")
                                            )
                                    {
                                        Log.d(TAG, "onResponse: URl"+uri);
                                        Toast.makeText(getApplication(), " SCHEDULE MESSAGE SEND SUCCESSFULLY", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplication(), "  ", Toast.LENGTH_LONG).show();

                                    }
                                }
                            } catch (JSONException e) {

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






    private void getBillingDataForPaytmStatus()
    {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String u_id = sharedpreferences.getString("userId", "");


        try
        {
            billingData.put("customer_id", custId);
            billingData.put("cust_number",custMobile);
            billingData.put("cust_name",custName);
            billingData.put("cust_email_id",custemail);
            billingData.put("address",custaddOne);
            billingData.put("home_delivery", h_status);
            billingData.put("created_by", u_id);
            billingData.put("place_of_supply",supplier);
            billingData.put("taxable_value", taxableValue);
            billingData.put("total_gst", total_gst);
            billingData.put("igst", igst);
            billingData.put("cgst", cgst);
            billingData.put("sgst",sgst);
            billingData.put("shipping_charges", shipping_charges);
            billingData.put("other_charges", other_charges);
            billingData.put("total", totalAmount);
            billingData.put("balance_due", balanceDue);
            billingData.put("amount_paid", amount_paid);
            billingData.put("mode_of_payment", ModeofPayment);
            billingData.put("shortPmode",MOFPayment_shortCut);
            billingData.put("itemArray", selected_items);
            billingData.put("description","Not Upddated");
            billingData.put("amount",totalAmount);
            billingData.put("transno","not applicable");


            Log.d("Object", "" + billingData);
            Log.d("array", "" + selected_items);

            // final ProgressDialog loading = ProgressDialog.show(this, "Saving ....", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.CREATEBILL, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    String resp = response.toString().trim();
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        Log.d("RESP", resp);
                        String msg = jsonObject.getString("message");
                        Log.d("MSG", msg);
                        invId=jsonObject.getString("data");
                        if (msg.equalsIgnoreCase("Add data succesfully"))
                        {
                            //  loading.dismiss();
                            sqlDataBase.deleteDataBase_ItemTable();
                            if(duePaymentSms)
                            {
                                if(Double.parseDouble(amount_paid)>0)
                                {
                                    sendDue_payment_Sms();
                                }
                            }
                            sendMessage(invId);

                            Intent intent = new Intent(Billing_ChoosePaymentMode.this, Paytm_payment_Status.class);
                            intent.putExtra("taxableValue",taxableValue);
                            intent.putExtra("totalgst",total_gst);
                            intent.putExtra("otherCharges",other_charges);
                            intent.putExtra("shippingCharges",shipping_charges);
                            intent.putExtra("amountpaid",amount_paid);
                            intent.putExtra("balanceDue",balanceDue);
                            intent.putExtra("totalAmount",totalAmount);
                            intent.putExtra("modeofPayment",ModeofPayment);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        } else
                        {
                            //loading.dismiss();
                            Toast.makeText(getApplicationContext(), "Cant Add Data", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    Log.d("Add Succussuflly", resp);




                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    if (error instanceof NoConnectionError)
                    {
                        //loading.dismiss();
                        Toast.makeText(Billing_ChoosePaymentMode.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("data", String.valueOf(billingData));
                    params.put("dbname",dbname);
                    Log.d(TAG, "getParams:"+params.toString());

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }



    }


    public void sendMessage(String invId)
    {
        String message1 = new String();

        String message = "Dear Customer, \n" +
                " Thanks for Your Business \n" +
                " of Total Amount : RS"+ totalAmount + " \n  Invoice No : "+invId +" \n  Please Visit us again ! \n shop name :";
        String mobile = custMobile;
        try
        {
            message1 = URLEncoder.encode(message, "utf-8");
        } catch (UnsupportedEncodingException e) {

        }

        String sender = "MHOURZ";
        String route = "4";
        String country = "91";
        String json = "json";
        String uri = Uri.parse("http://api.msg91.com/api/sendhttp.php?")
                .buildUpon()
                .appendQueryParameter("authkey", "133779ATT6JFXy0k5850e783")
                .appendQueryParameter("mobiles", mobile)
                .appendQueryParameter("message", message1)
                .appendQueryParameter("sender", sender)
                .appendQueryParameter("route", route)
                .appendQueryParameter("country", country)
                .appendQueryParameter("response", json)
                .build().toString();


        final Context context = getApplicationContext();
        StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null)
                            {
                                Log.d(TAG, "onResponse: Sms"+response);
                                JSONObject j = new JSONObject(response);
                                String type1 = j.getString("type");
                                if (type1.equalsIgnoreCase("success"))
                                {
                                    Toast.makeText(getApplication(), "message sent successfully", Toast.LENGTH_LONG).show();
                                } else
                                {
                                    Toast.makeText(getApplication(), "Message couldn't reach you, try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {

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




    private void getBillingDataForPaytmStatus99(final String transactionid)
    {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String u_id = sharedpreferences.getString("userId", "");


        try
        {
            billingData.put("customer_id", custId);
            billingData.put("cust_number",custMobile);
            billingData.put("cust_name",custName);
            billingData.put("cust_email_id",custemail);
            billingData.put("address",custaddOne);
            billingData.put("home_delivery", h_status);
            billingData.put("created_by", u_id);
            billingData.put("place_of_supply",supplier);
            billingData.put("taxable_value", taxableValue);
            billingData.put("total_gst", total_gst);
            billingData.put("igst", igst);
            billingData.put("cgst", cgst);
            billingData.put("sgst",sgst);
            billingData.put("shipping_charges", shipping_charges);
            billingData.put("other_charges", other_charges);
            billingData.put("total", totalAmount);
            billingData.put("balance_due", balanceDue);
            billingData.put("amount_paid", amount_paid);
            billingData.put("shortPmode",MOFPayment_shortCut);
            billingData.put("mode_of_payment", ModeofPayment);
            billingData.put("itemArray", selected_items);
            billingData.put("description","Not Upddated");
            billingData.put("amount",totalAmount);
            billingData.put("transno",transactionid);


            Log.d("Object", "" + billingData);
            Log.d("array", "" + selected_items);

            // final ProgressDialog loading = ProgressDialog.show(this, "Saving ....", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.CREATEBILL, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    String resp = response.toString().trim();
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        Log.d("RESP", resp);
                        String msg = jsonObject.getString("message");
                        Log.d("MSG", msg);
                        invId=jsonObject.getString("data");
                        if (msg.equalsIgnoreCase("Add data succesfully"))
                        {
                            sendMessage(invId);
                            if(duePaymentSms)
                            {
                                if(Double.parseDouble(amount_paid)>0)
                                {
                                    sendDue_payment_Sms();
                                }
                            }
                            //  loading.dismiss();
                            sqlDataBase.deleteDataBase_ItemTable();

                            Intent intent = new Intent(Billing_ChoosePaymentMode.this, Paytm_payment_Status.class);
                            intent.putExtra("taxableValue",taxableValue);
                            intent.putExtra("totalgst",total_gst);
                            intent.putExtra("otherCharges",other_charges);
                            intent.putExtra("shippingCharges",shipping_charges);
                            intent.putExtra("amountpaid",amount_paid);
                            intent.putExtra("balanceDue",balanceDue);
                            intent.putExtra("totalAmount",totalAmount);
                            intent.putExtra("id",transactionid);
                            intent.putExtra("modeofPayment",ModeofPayment);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        } else
                        {
                            //loading.dismiss();
                            Toast.makeText(getApplicationContext(), "Cant Add Data", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    Log.d("Add Succussuflly", resp);




                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    if (error instanceof NoConnectionError)
                    {
                        //loading.dismiss();
                        Toast.makeText(Billing_ChoosePaymentMode.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("data", String.valueOf(billingData));
                    params.put("dbname",dbname);
                    Log.d(TAG, "getParams:"+params.toString());

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }



    }


    private void getBillingDataForPaytmStatusfail(String p_status)
    {
        amount_paid="0.0";
        balanceDue=totalAmount;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String u_id = sharedpreferences.getString("userId", "");


        try {
            billingData.put("customer_id", custId);
            billingData.put("cust_number",custMobile);
            billingData.put("cust_name",custName);
            billingData.put("cust_email_id",custemail);
            billingData.put("address",custaddOne);
            billingData.put("home_delivery", h_status);
            billingData.put("created_by", u_id);
            billingData.put("place_of_supply",supplier);
            billingData.put("taxable_value", taxableValue);
            billingData.put("total_gst", total_gst);
            billingData.put("igst", igst);
            billingData.put("cgst", cgst);
            billingData.put("sgst",sgst);
            billingData.put("shipping_charges", shipping_charges);
            billingData.put("other_charges", other_charges);
            billingData.put("total", totalAmount);
            billingData.put("balance_due", balanceDue);
            billingData.put("amount_paid", amount_paid);
            billingData.put("mode_of_payment", ModeofPayment);
            billingData.put("itemArray", selected_items);
            billingData.put("description",p_status);
            billingData.put("amount",totalAmount);
            billingData.put("transno",transactionId);


            Log.d("Object", "" + billingData);
            Log.d("array", "" + selected_items);

            // final ProgressDialog loading = ProgressDialog.show(this, "Saving ....", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.CREATEBILL, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    String resp = response.toString().trim();
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        Log.d("RESP", resp);
                        String msg = jsonObject.getString("message");
                        Log.d("MSG", msg);
                        if (msg.equalsIgnoreCase("Add data succesfully"))
                        {
                            //  loading.dismiss();
                            sqlDataBase.deleteDataBase_ItemTable();

                            Intent intent = new Intent(Billing_ChoosePaymentMode.this, Billing_OnSuccessCash_PaymentMode.class);
                            intent.putExtra("taxableValue",taxableValue);
                            intent.putExtra("totalgst",total_gst);
                            intent.putExtra("otherCharges",other_charges);
                            intent.putExtra("shippingCharges",shipping_charges);
                            intent.putExtra("amountpaid",amount_paid);
                            intent.putExtra("balanceDue",balanceDue);
                            intent.putExtra("totalAmount",totalAmount);
                            intent.putExtra("modeofPayment",ModeofPayment);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();


//                             Intent intent = new Intent(Billing_ChoosePaymentMode.this, BillingHistory.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();

                        } else
                        {
                            //loading.dismiss();
                            Toast.makeText(getApplicationContext(), "Cant Add Data", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    Log.d("Add Succussuflly", resp);




                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    if (error instanceof NoConnectionError)
                    {
                        //loading.dismiss();
                        Toast.makeText(Billing_ChoosePaymentMode.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("data", String.valueOf(billingData));
                    params.put("dbname",dbname);
                    Log.d(TAG, "getParams:"+params.toString());

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }



    }


    public void startPayment()
    {

        String paise=convertRupeeToPaisa(Double.parseDouble(amount_paid));
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String usermobile = (sharedpreferences.getString("usermobile", null));
        String useremail = (sharedpreferences.getString("useremail", null));

        try
        {
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
        } catch (Exception e)
        {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

   @Override
    public void onPaymentSuccess(String razorpayPaymentID)
    {

        try
        {
            this.transactionId=razorpayPaymentID;
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            getBillingDataForStatus();
        } catch (Exception e)
        {
            Log.e("Razorpay", "Exception in onPaymentSuccess", e);
        }

    }

    @Override
    public void onPaymentError(int code, String response)
    {
        try
        {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onPaymentError: Code"+code);
            Log.d(TAG, "onPaymentError:Response"+response);

//            Intent intent=new  Intent(context,Billing_OnfailureActivity.class);
//            startActivity(intent);

        } catch (Exception e)
        {
            Log.e("Razorpay", "Exception in onPaymentError", e);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {


        if (requestCode == PayuConstants.PAYU_REQUEST_CODE)
        {
            if (data != null)
            {

                /**
                 * Here, data.getStringExtra("payu_response") ---> Implicit response sent by PayU
                 * data.getStringExtra("result") ---> Response received from merchant's Surl/Furl
                 *
                 * PayU sends the same response to merchant server and in app. In response check the value of key "status"
                 * for identifying status of transaction. There are two possible status like, success or failure
                 * */

            String successData=data.getStringExtra("result");
                Log.d(TAG, "onActivityResult:Data"+successData);

                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Payu's Data : " + data.getStringExtra("payu_response") + "\n\n\n Merchant's Data: " + data.getStringExtra("result"))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                dialog.dismiss();
                            }
                        }).show();
                                 getBillingDataForStatus();

            } else
            {
                Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
            }
        }



        if(requestCode == PAYTM && resultCode == Activity.RESULT_OK)
    {
        try
        {
            String status=data.getStringExtra("result");
            Log.d(TAG, "onActivityResult:"+status);
            if(status.equalsIgnoreCase("payment done"))
            {
                getBillingDataForPaytmStatus();
                Log.d(TAG, "onActivityPATM");

            }else if(status.equalsIgnoreCase("payment fail"))
            {
                String p_status= "payment fail from Paytm";
                getBillingDataForPaytmStatusfail(p_status);
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
                    getBillingDataForPaytmStatus();
                    Toast.makeText(getApplicationContext()," Payment done",Toast.LENGTH_LONG).show();

                }else if(status.equalsIgnoreCase("payment fail"))
                {
                    String p_status= "payment fail from Bhim App";
                    getBillingDataForPaytmStatusfail(p_status);
                    Toast.makeText(getApplicationContext()," Payment Fail",Toast.LENGTH_LONG).show();

                }

              //  getBillingDataForPaytmStatus();


            }catch (Exception e)
            {

            }






        }else if(requestCode == BANK99 && resultCode==Activity.RESULT_OK)
        {
            String status=data.getStringExtra("result");
            String id=data.getStringExtra("id");
            Log.d(TAG, "onActivityResult:Bhim Status"+status);
            Log.d(TAG, "onActivityResult:ID"+id);
            getBillingDataForPaytmStatus99(id);

        }



    }




    private void sendPaymentLinkToCustomer()
    {

        String  amount=convertRupeeToPaisa(Double.parseDouble(amount_paid));
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
                                getBillingData();
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
                            Toast.makeText(Billing_ChoosePaymentMode.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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

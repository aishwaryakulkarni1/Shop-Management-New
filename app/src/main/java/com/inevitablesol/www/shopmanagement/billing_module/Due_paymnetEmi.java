package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Due_paymnetEmi extends AppCompatActivity implements View.OnClickListener
{

    private static final String TAG = "Due_paymnetEmi";
    private Context context=Due_paymnetEmi.this;
    private TextView tx_date;
    private ImageView date_picker;
    private TextInputEditText et_TimeofBound,et_rateofInterst,et_policy,et_et_pancard,et_companyName;
    private AppCompatButton bt_proceedToPayment;
    private String data;
    private String dbname;
    private SqlDataBase sqlDataBase;
    private  String taxableValue,total_gst,shipping_charges,totalAmount,amount_paid,balanceDue,ModeofPayment,other_charges;
    private String custMobile;
    private String invId;





    private TextInputEditText tran_numbner;
    private String trans_num;
    private String invoiceId;
    private String charges;
    private String MOFPayment_shortCut;


    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    private String email;
    private GlobalPool globalPool;
    private String payment_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_paymnet_emi);

        tx_date= (TextView) findViewById(R.id.txt_emiDate);
        et_et_pancard=(TextInputEditText)findViewById(R.id.emi_pancard);
        et_rateofInterst=(TextInputEditText)findViewById(R.id.emi_rateofInterest);
        et_policy=(TextInputEditText)findViewById(R.id.emi_policyNumber);
        et_TimeofBound=(TextInputEditText)findViewById(R.id.emi_timeofBound);
        et_companyName=(TextInputEditText)findViewById(R.id.emi_companyName);
        bt_proceedToPayment=(AppCompatButton) findViewById(R.id.bt_EmiDetail_proceed);
        bt_proceedToPayment.setOnClickListener(this);
        date_picker=(ImageView)findViewById(R.id.date_EmiDate);
        date_picker.setOnClickListener(this);
        globalPool = (GlobalPool) this.getApplication();
        tran_numbner=(TextInputEditText)findViewById(R.id.transaction_number);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent intent = getIntent();
        if (intent.hasExtra("balanceDue"))
        {
            amount_paid = intent.getStringExtra("amountpaid");
            balanceDue = intent.getStringExtra("balanceDue");
            ModeofPayment = intent.getStringExtra("totalAmount");
            totalAmount = intent.getStringExtra("totalAmount");
            ModeofPayment = intent.getStringExtra("modeofPayment");
            custMobile = intent.getStringExtra("custMobile");
            data = intent.getStringExtra("data");
            dbname = intent.getStringExtra("dbname");
            email=intent.getStringExtra("email");
            invoiceId=intent.getStringExtra("invoice_id");
            charges=intent.getStringExtra("charges");
            MOFPayment_shortCut=intent.getStringExtra("shortPmode");

            Log.d(TAG, "onCreate() called with: savedInstanceState = [" + data + "]");
        }
        et_et_pancard.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        sqlDataBase = new SqlDataBase(context);
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


    private void showDate()
    {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date =
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        Date date1 = myCalendar.getTime();

                        updateDate(myCalendar);
                    }

                };

        new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));


    }

    private void updateDate(Calendar myCalendar)
    {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(myCalendar.getTime());
        tx_date.setText(currentDateTimeString);
    }


    private void updateDate()
    {
        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat =new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(dt);
        tx_date.setText(currentDateTimeString);

    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDate();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    public String toString()
    {
        Log.d(TAG, "toString: "+super.toString());
        return super.toString();

    }


    @Override
    public void onClick(View v)
    {
        try

        {
            switch (v.getId())
            {
                case R.id.date_EmiDate:
                    showDate();
                    break;
                case R.id.bt_EmiDetail_proceed:
                    bt_proceedToPayment.setClickable(false);
                    final String   panCardDetails=et_et_pancard.getText().toString().trim();
                    Matcher matcher = pattern.matcher(panCardDetails);
                    trans_num =tran_numbner.getText().toString().trim();
// Check if pattern matches
                    if (matcher.matches())
                    {
                        updateInvoiceData();
                        Log.d("Matching","Yes");
                    }else
                    {
                        Toast.makeText(getApplicationContext()," Please Add  Valid Pan Card",Toast.LENGTH_LONG).show();
                    }

                    break;
                default:
                    Toast.makeText(context, "Wroung Choice", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {

            e.printStackTrace();
        } finally {
        }


    }

    private void getEmiDeatails(final String invID)
    {

        final String   panCardDetails=et_et_pancard.getText().toString().trim();
        final String   policyDetail=et_policy.getText().toString().trim();
        final String   boundType=et_TimeofBound.getText().toString().trim();
        String   emi_date =tx_date.getText().toString().trim();
        final String rateOfInterest=et_rateofInterst.getText().toString().trim();
        final String   companyName=et_companyName.getText().toString().trim();




        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.AddEMIDETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Add data succesfully"))
                    {
                        loading.dismiss();
                        sqlDataBase.deleteItemTable();
                        sendMessage(invoiceId);

                        loading.dismiss();
                        Intent intent = new Intent(Due_paymnetEmi.this, Billing_paidAmountSuccess.class);
                        intent.putExtra("amountpaid",amount_paid);
                        intent.putExtra("balanceDue",balanceDue);
                        intent.putExtra("totalAmount",totalAmount);
                        intent.putExtra("modeofPayment",ModeofPayment);
                        intent.putExtra("transactionId",trans_num);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } else
                    {
                        try
                        {
                            loading.dismiss();

                            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                            loading.dismiss();


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
                    Toast.makeText(Due_paymnetEmi.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("invoice_id", invoiceId);
                params.put("company_name",companyName);
                params.put("rate_of_interest",rateOfInterest);
                params.put("time_of_bound",boundType);
                params.put("policy_no",policyDetail);
                params.put("pan_card_no",panCardDetails);
                params.put("paymentId",payment_id);
                Log.i(TAG, "getParams:EMI"+params.toString());
                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private void updateInvoiceData() {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(Due_paymnetEmi.this).add(new StringRequest(Request.Method.POST, WebApi.INVIOCEAMOUNTPAIDHISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    Log.d(TAG, "onResponse: EMI" + msg);
                    payment_id=msg.getString("paymentId");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    } else
                    {
                        try
                        {

                           sendMessage(invoiceId);
                            getEmiDeatails(invId);
                            loading.dismiss();
                            Intent intent = new Intent(Due_paymnetEmi.this, Billing_paidAmountSuccess.class);
                            intent.putExtra("amountpaid",amount_paid);
                            intent.putExtra("balanceDue",balanceDue);
                            intent.putExtra("totalAmount",totalAmount);
                            intent.putExtra("modeofPayment",ModeofPayment);
                            intent.putExtra("transactionId",trans_num);

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }catch (NullPointerException e) {

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
                    Toast.makeText(Due_paymnetEmi.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

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
                params.put("amount_paid", amount_paid);
                params.put("transno", trans_num);
                params.put("shortPmode", MOFPayment_shortCut);
                params.put("charges", "0");
                Log.d("getParams:InvocieByID", params.toString());
                return params;
            }


        });
    }


    private void _getInvIdForEmi()
    {
        try
        {

            JSONObject jsonObject=null;

            Log.d(TAG, "storeBillingData: Data"+data);

            jsonObject=new JSONObject(data);

            jsonObject.put("transactionId",trans_num);
            Log.d(TAG, "storeBillingData: New object"+data);
            final JSONObject finalJsonObject = jsonObject;


            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.CREATEBILL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    String resp = response.toString().trim();
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        Log.d("RESP", resp);
                        String msg = jsonObject.getString("message");
                        invId= jsonObject.getString("data");
                        Log.d(TAG, "onResponse:"+data);

                        Log.d("MSG", msg);
                        if (msg.equalsIgnoreCase("Add data succesfully"))
                        {
                            getEmiDeatails(invId);

                        } else
                        {

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

                        Toast.makeText(Due_paymnetEmi.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    Map<String, String> params = new HashMap<>();
                    params.put("data", String.valueOf(finalJsonObject));
                    params.put("dbname",dbname);
                    params.put("transactionId",trans_num);
                    Log.d(TAG, "getParams:"+params.toString());

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } catch (Exception e)
        {
            e.printStackTrace();
        }



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



//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case android.R.id.home:
//                Toast.makeText(this, "Please Complete Process", Toast.LENGTH_SHORT).show();
//
//                return true;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        //Toast.makeText(this, "Don't Go Back", Toast.LENGTH_SHORT).show();
    }

}

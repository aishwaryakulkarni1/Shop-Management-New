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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import com.inevitablesol.www.shopmanagement.Billing_paidAmountSuccess;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.settings.Billing_Settings;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.pddstudio.urlshortener.URLShortener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DuePayment_ChequeDeatils extends AppCompatActivity implements View.OnClickListener
{

    private Spinner typeofPayment;
    private TextView txt_date;
    private Context context=DuePayment_ChequeDeatils.this;
    private ImageView datePicker;

    // editText
    private TextInputEditText et_bankName;
    private TextInputEditText et_payeeName;
    private  TextInputEditText et_chequeNumber;

    private AppCompatButton bt_proceedToPay;

    private String dbname;
    private SqlDataBase sqlDataBase;
    private  String data;
    private  String  chequeNumber;
    private  Spinner spinner;
    private  String custMobile;

    private  String taxableValue,total_gst,shipping_charges,totalAmount,amount_paid,balanceDue,ModeofPayment,other_charges;
    private String invId;
    private final int BANKREQUESTCODE=150;
    private GlobalPool globalPool;
    private TextInputEditText tran_numbner;
    private String trans_num;

    private static final String TAG = "DuePayment_ChequeDeatil";
    private String email;
    private String invoiceId;
    private String charges;
    private String MOFPayment_shortCut;
    private String payment_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_payment__cheque_deatils);

        typeofPayment=(Spinner)findViewById(R.id.typeofpayment);
        bt_proceedToPay= (AppCompatButton) findViewById(R.id.bt_chequeDetail_proceed);
        bt_proceedToPay.setOnClickListener(this);
        globalPool=(GlobalPool)this.getApplicationContext();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeofPayment.setAdapter(adapter);
        //TextView
        txt_date=(TextView)findViewById(R.id.txt_chequeDate);
        //ImageView
        datePicker= (ImageView) findViewById(R.id.date_chequeDate);
        datePicker.setOnClickListener(this);
        //EditText

        et_bankName= (TextInputEditText) findViewById(R.id.cheque_bankName);
        et_chequeNumber= (TextInputEditText) findViewById(R.id.cheque_number);
        et_payeeName= (TextInputEditText) findViewById(R.id.cheque_payeeName);
        tran_numbner=(TextInputEditText)findViewById(R.id.transaction_number);

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
            case R.id.new_bankdetails:
                Intent intent = new Intent(DuePayment_ChequeDeatils.this, Billing_Settings.class);
                startActivityForResult(intent, BANKREQUESTCODE);
                Toast.makeText(context, "Please Add Bank Details", Toast.LENGTH_SHORT).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        try {
            switch (v.getId())
            {
                case R.id.date_chequeDate:
                    showDate();
                    break;
                case R.id.bt_chequeDetail_proceed:
                    trans_num =tran_numbner.getText().toString().trim();
                    if(trans_num.length()>0)
                    {
                        updateInvoiceData();
                    }else {
                        Toast.makeText(context, "Please Add Transaction number", Toast.LENGTH_SHORT).show();
                    }

                    //Toast.makeText(context, "Under Working", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    Toast.makeText(context, "Wroung Choice", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e)
        {
        }
    }

    private void showDate()
    {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date =
                new DatePickerDialog.OnDateSetListener()
                {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth)
                    {
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
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MMM/yy");
        String currentDateTimeString = dateFormat.format(myCalendar.getTime());
        txt_date.setText(currentDateTimeString);
    }


    private void updateDate()
    {
        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        // String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        String currentDateTimeString = dateFormat.format(dt);
        txt_date.setText(currentDateTimeString);

    }


    private void getChequeDetails(final String invID)
    {
        try
        {


            final String bankName=et_bankName.getText().toString().trim();
            final String  personName=et_payeeName.getText().toString().trim();
            chequeNumber =et_chequeNumber.getText().toString().trim();
            final String  typeOfPayment=typeofPayment.getSelectedItem().toString().trim();
            final  String date=txt_date.getText().toString().trim();
            final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.AddCHEQUE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    Log.d(TAG, "onResponse: " + response);
                    JSONObject msg = null;
                    try {
                        msg = new JSONObject(response);
                        String message = msg.getString("message");
                        if (message.equalsIgnoreCase("Add data succesfully"))
                        {
                            loading.dismiss();
                            sqlDataBase.deleteItemTable();
                            sendMessage();
                            loading.dismiss();
                            Intent intent = new Intent(DuePayment_ChequeDeatils.this, Billing_paidAmountSuccess.class);
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
                        Toast.makeText(DuePayment_ChequeDeatils.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                    }

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    Log.d("dbname", dbname);
                    params.put("dbname", dbname);
                    params.put("book_name",bankName);
                    params.put("name",personName);
                    params.put("cheque_no",chequeNumber);
                    params.put("type_of_payment",typeOfPayment);
                    params.put("invoice_id",invID);
                    params.put("paymentId",payment_id);
                    Log.d(TAG, "getParams:ChequeParam"+params.toString());

                    return params;
                }


            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);






        }catch (Exception e)
        {
            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
        }



    }


    public void sendMessage()
    {



        String message = "Dear Customer, \n" +
                " Thanks for Your Business \n" +
                " of Total Amount : Rs"+ totalAmount + " \n  Invoice No : "+invId +"  Invoice Link : http://shopmanagment.surge.sh/Shopnotify/?dbname="+globalPool.getDbname()+"&invoice_id="+invId+
                " \n  Please Visit us again ! \n "+globalPool.getShopName();
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


    private void updateInvoiceData() {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(DuePayment_ChequeDeatils.this).add(new StringRequest(Request.Method.POST, WebApi.INVIOCEAMOUNTPAIDHISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("RESP", response);
                    String  msg = jsonObject.getString("message");
                   // invId= jsonObject.getString("paymentId");
                    payment_id=jsonObject.getString("paymentId");
                    Log.d(TAG, "onResponse:"+data);
                    if (msg.equalsIgnoreCase("Data not available")) {
                        Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    } else
                    {
                        try
                        {
                            sendMessage(invoiceId);
                            getChequeDetails(invoiceId);
                            loading.dismiss();
                            Intent intent = new Intent(DuePayment_ChequeDeatils.this, Billing_paidAmountSuccess.class);

                            intent.putExtra("amountpaid",amount_paid);
                            intent.putExtra("balanceDue",balanceDue);
                            intent.putExtra("totalAmount",totalAmount);
                            intent.putExtra("modeofPayment",ModeofPayment);
                            intent.putExtra("transactionId",trans_num);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();


//


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
                    Toast.makeText(DuePayment_ChequeDeatils.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
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

}

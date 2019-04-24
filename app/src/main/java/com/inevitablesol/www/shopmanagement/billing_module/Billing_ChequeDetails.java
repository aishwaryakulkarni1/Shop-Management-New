package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.settings.Billing_Settings;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Billing_ChequeDetails extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Billing_ChequeDetails";
    private Spinner typeofPayment;
    private TextView txt_date;
    private Context context=Billing_ChequeDetails.this;
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
    private String payment_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing__cheque_details);
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

        Intent intent=getIntent();
        if(intent.hasExtra("data"))
        {

            taxableValue=intent.getStringExtra("taxableValue");
            total_gst=  intent.getStringExtra("totalgst");
            other_charges = intent.getStringExtra("otherCharges");
            shipping_charges= intent.getStringExtra("shippingCharges");
            amount_paid= intent.getStringExtra("amountpaid");
            balanceDue= intent.getStringExtra("balanceDue");
            ModeofPayment= intent.getStringExtra("totalAmount");
            totalAmount= intent.getStringExtra("totalAmount");
            ModeofPayment= intent.getStringExtra("modeofPayment");
            custMobile=intent.getStringExtra("custMobile");
            data=intent.getStringExtra("data");
            dbname=intent.getStringExtra("dbname");
            Log.d(TAG, "onCreate() called with: savedInstanceState = [" + data + "]");

        }

        sqlDataBase=new SqlDataBase(context);


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
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_bank_details, menu);
////        return true;
////    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.new_bankdetails:
                Intent intent = new Intent(Billing_ChequeDetails.this, Billing_Settings.class);
                startActivityForResult(intent, BANKREQUESTCODE);
                Toast.makeText(context, "Please Add Bank Details", Toast.LENGTH_SHORT).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
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
                        storeBillingData();
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

                            Intent intent=new Intent(context,Billing_ChequeDetailsStatus.class);
                            intent.putExtra("bankName",bankName);
                            intent.putExtra("bankHolderName",personName);
                            intent.putExtra("chequeNumber",chequeNumber);
                            intent.putExtra("typeOfPayment",typeOfPayment);
                            intent.putExtra("taxableValue",taxableValue);
                            intent.putExtra("totalgst",total_gst);
                            intent.putExtra("date",date);
                            intent.putExtra("otherCharges",other_charges);
                            intent.putExtra("shippingCharges",shipping_charges);
                            intent.putExtra("amountpaid",amount_paid);
                            intent.putExtra("balanceDue",balanceDue);
                            intent.putExtra("totalAmount",totalAmount);
                            intent.putExtra("modeofPayment",ModeofPayment);
                            intent.putExtra("transactionId",trans_num);
                            intent.putExtra("dbname",dbname);
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
                        Toast.makeText(Billing_ChequeDetails.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

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

    private boolean storeBillingData()
    {
        try
        {
          JSONObject jsonObject=null;

            Log.d(TAG, "storeBillingData: Data"+data);

                 jsonObject=new JSONObject(data);

                jsonObject.put("transactionId",trans_num);
            Log.d(TAG, "storeBillingData: New object"+data);

       ;
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
                    payment_id=jsonObject.getString("paymentId");
                    Log.d(TAG, "onResponse:"+data);

                    Log.d("MSG", msg);
                    if (msg.equalsIgnoreCase("Add data succesfully"))
                    {

                       getChequeDetails(trans_num);

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
                    Toast.makeText(Billing_ChequeDetails.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
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

        return false;
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
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            default:
//
//                return super.onOptionsItemSelected(item);
//        }
//
//
//    }







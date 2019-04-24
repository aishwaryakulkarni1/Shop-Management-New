package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.billing_module.Adapter.BHArrayAdapter;
import com.inevitablesol.www.shopmanagement.billing_module.Parser.BillingHistoryParser;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BillingHistory extends BaseActivity {
    TextView currentDate,tv_total_sales;
    ImageView datePicker;
    private static final String TAG = "BillingHistory";
    private String currentDateTimeString;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private RecyclerView bh_recyclerview;
    ArrayList<InvoiceHistoryInfo> invoiceHistoryInfos;
    BHArrayAdapter dataAdapter;
    private SqlDataBase sqlDataBase;

    private ImageView transactionHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_billing_history, frameLayout);

        bh_recyclerview = (RecyclerView)findViewById(R.id.bh_recyclerview);

        transactionHistory=(ImageView)findViewById(R.id.transaction_history);

        transactionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BillingHistory.this,Transaction_History.class));
            }
        });

        datePicker = (ImageView) findViewById(R.id.date_billingDate);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDate();
            }
        });
        currentDate = (TextView) findViewById(R.id.bill_curruntDate);

        sqlDataBase=new SqlDataBase(this);

        ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(BillingHistory.this, BillingDetails.class));

            }
        });



        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));


        mDrawerList.setItemChecked(position, true);
        String logCheck = listArray[position];
        if (logCheck.equalsIgnoreCase("Logout"))
        {
//            setTitle(listArray[0]);
            toolbar.setTitle(listArray[0]);
        } else {

//            setTitle(listArray[position]);
            toolbar.setTitle("Welcome to Market Hourz");
            Typeface fromAsset  =   Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Light.ttf");
            ((TextView)toolbar.getChildAt(1)).setTypeface(fromAsset);


        }
        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyy-MM-dd");
        currentDateTimeString = dateFormat.format(dt);
        currentDate.setText(currentDateTimeString);

        get_billing_history();

//        if( sqlDataBase.check_items())
//        {
//            Log.d(TAG, "onResponse:DB Available");
//
//        }else
//        {
//            getAllItemDetails();
//
//        }





            


    }

    private void showDate() {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

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

        new DatePickerDialog(BillingHistory.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));


    }

    private void updateDate(Calendar myCalendar)
    {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(myCalendar.getTime());
        currentDate.setText(currentDateTimeString);

        get_billing_history();
    }


    private void get_billing_history()

    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);


        Volley.newRequestQueue(BillingHistory.this).add(new StringRequest(Request.Method.POST, WebApi.BILLING_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.toString().trim();
                Log.d("response", response);
                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        loading.dismiss();

                        bh_recyclerview.setAdapter(null);

                    } else
                    {


                        loading.dismiss();

                        BillingHistoryParser parser = new BillingHistoryParser(resp);
                        parser.billingHistoryParser();
                        String[] invoice_id = BillingHistoryParser.invoice_id;
                        String[] amount_paid = BillingHistoryParser.amount_paid;
                        String[] balance_due = BillingHistoryParser.balance_due;
                        String[] mode_of_payment = BillingHistoryParser.mode_of_payment;
                        try
                        {

                            final double[] total_amt = new double[amount_paid.length];
                            for (int i=0; i < amount_paid.length; i++)
                            {
                                total_amt[i] = Double.parseDouble(amount_paid[i]);
                            }
                            double sum =0.00;
                            for( double i : total_amt)
                            {
                                Log.d(TAG, "onResponse:toatalAmount"+i);
                                sum += i;
                            }
                            double collectedAmount = Math.round(sum * 100.0) / 100.0;
                            Log.d(TAG, "onResponse:CollectedAmount"+collectedAmount);
                            Log.d(TAG, "onResponse:CollectedSUM"+sum);
                            tv_total_sales = (TextView) findViewById(R.id.tv_total_sales1);
                            String strAmount=String.valueOf(String.format("%.2f",collectedAmount));
                            tv_total_sales.setText(strAmount);


                        }catch (NumberFormatException e)
                        {
                            Log.e(TAG, "onResponse:Billing"+e );

                        }catch (NullPointerException e)
                        {
                            Log.e(TAG, "onResponse:Billing"+e );

                        }catch (Exception e)
                        {
                            Log.e(TAG, "onResponse:Billing"+e );
                        }



                        invoiceHistoryInfos = parser.makeArray();
                        bh_recyclerview.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        bh_recyclerview.setLayoutManager(layoutManager);
                        dataAdapter=new BHArrayAdapter(invoiceHistoryInfos);
                        bh_recyclerview.setAdapter(dataAdapter);


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
                    Toast.makeText(BillingHistory.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("created_date", currentDate.getText().toString());
                Log.d(TAG, "getParams:" + params.toString());

                return params;

            }
        });

    }

    private ArrayList prepareBH(String[] invoice_id, String[] amount_paid, String[] balance_due, String[] mode_of_payment) {
        ArrayList<InvoiceHistoryInfo> address_ver = new ArrayList<InvoiceHistoryInfo>();
        if (invoice_id.length == 0)
        {
            Log.d("NO Invoices found", "NO INVOICES FOUND");
        } else
        {
            for (int i = 0; i < invoice_id.length; i++) {
                InvoiceHistoryInfo invoiceHistoryInfo = new InvoiceHistoryInfo();
                invoiceHistoryInfo.setInvoice_id(invoice_id[i]);
                invoiceHistoryInfo.setAmount_paid(amount_paid[i]);
                invoiceHistoryInfo.setBalance_due(balance_due[i]);
                invoiceHistoryInfo.setMode_of_payment(mode_of_payment[i]);

                address_ver.add(invoiceHistoryInfo);
            }
        }


        return address_ver;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.d(TAG, "onCreateOptionsMenu: ");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }



}







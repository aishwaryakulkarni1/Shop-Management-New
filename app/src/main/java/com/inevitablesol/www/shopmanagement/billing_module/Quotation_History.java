package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
import com.inevitablesol.www.shopmanagement.Quotation.Quotation_Details;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.billing_module.Adapter.QuotationHistory_Adapter;
import com.inevitablesol.www.shopmanagement.billing_module.Parser.Quotation_history_parser;
import com.inevitablesol.www.shopmanagement.billing_module.Parser.Quotation_pojo;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Quotation_History extends AppCompatActivity {

    TextView currentDate,tv_total_sales;
    ImageView datePicker;
    private static final String TAG = "BillingHistory";
    private String currentDateTimeString;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private RecyclerView bh_recyclerview;
    ArrayList<Quotation_pojo> HistoryInfos;
    QuotationHistory_Adapter dataAdapter;
    private SqlDataBase sqlDataBase;
    private  Context context = Quotation_History.this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation__history);
       // getLayoutInflater().inflate(R.layout.activity_billing_history, frameLayout);

        bh_recyclerview = (RecyclerView)findViewById(R.id.bh_recyclerview);

        datePicker = (ImageView) findViewById(R.id.date_billingDate);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDate();
            }
        });
        currentDate = (TextView) findViewById(R.id.bill_curruntDate);

        sqlDataBase=new SqlDataBase(this);



        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname =(sharedpreferences.getString("dbname", null));


        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        currentDateTimeString = dateFormat.format(dt);
        currentDate.setText(currentDateTimeString);

        get_billing_history();

        bh_recyclerview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
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
                    Quotation_pojo invocieInfo= HistoryInfos.get(position);
                    Log.d(TAG, "onInterceptTouchEvent:"+invocieInfo);
                    Intent intent=new Intent(Quotation_History.this,Quotation_Details.class);
                    intent.putExtra("Q_id",String.valueOf(invocieInfo.getQ_id()));
                    intent.putExtra("Amount",String.valueOf(invocieInfo.getAmount()));
                    intent.putExtra("name",invocieInfo.getCustName());
                    intent.putExtra("mobile",invocieInfo.getCustMobile());
                    intent.putExtra("gst",invocieInfo.getTotalGst());
                    intent.putExtra("date",invocieInfo.getCreatedDate());
                    intent.putExtra("email",invocieInfo.getEmail());
                    intent.putExtra("taxableValue",invocieInfo.getTaxableValue());
//                    intent.putExtra("c_name",invocieInfo.getCustomerName());
//                    intent.putExtra("c_id",String.valueOf(invocieInfo.getCustomerId()));
//                    intent.putExtra("amount",invocieInfo.getAmount());
//                    intent.putExtra("payment_id",String.valueOf(invocieInfo.getPaymentId()));
//                    intent.putExtra("status",invocieInfo.getStatus());
//                    intent.putExtra("description",invocieInfo.getDescription());
//                    intent.putExtra("modeOfPayment",invocieInfo.getModeOfPayment());
//                    intent.putExtra("dateTime",invocieInfo.getDateTime());
                    intent.putExtra("dbname",dbname);
                    startActivity(intent);
                }
                return  false;

            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e)
            {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
            {

            }
        });


    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void showDate()
    {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

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

    private void updateDate(Calendar myCalendar) {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(myCalendar.getTime());
        currentDate.setText(currentDateTimeString);

        get_billing_history();
    }


    private void get_billing_history()

    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);


        Volley.newRequestQueue(context).add(new StringRequest(Request.Method.POST, WebApi.QUOTATION_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
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

                    } else
                    {


                        loading.dismiss();
                        Quotation_history_parser parser = new Quotation_history_parser(resp);
                        parser.billingHistoryParser();

                        String[] amount     = Quotation_history_parser.amount;

                        try
                        {

                            final double[] total_amt = new double[amount.length];
                            for (int i=0; i < amount.length; i++)
                            {
                                total_amt[i] = Double.parseDouble(amount[i]);
                            }
                            double sum =0.0;
                            for( double i : total_amt) {
                                sum += i;
                            }
                            tv_total_sales = (TextView) findViewById(R.id.tv_total_sales1);
                            tv_total_sales.setText(String.valueOf(Math.round(sum * 100.0) / 100.0));


                        }catch (NumberFormatException e)
                        {

                        }catch (NullPointerException e)
                        {

                        }catch (Exception e)
                        {

                        }



                        HistoryInfos = parser.makeArray();
                        bh_recyclerview.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        bh_recyclerview.setLayoutManager(layoutManager);
                        dataAdapter=new QuotationHistory_Adapter(HistoryInfos);
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
                    Toast.makeText(context, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

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

    private ArrayList<InvoiceHistoryInfo> prepareBH(String[] invoice_id,String[] amount_paid, String[] balance_due, String[] mode_of_payment) {
        ArrayList address_ver = new ArrayList<>();
        if (invoice_id.length == 0)
        {
            Log.d("NO Invoices found", "NO INVOICES FOUND");
        } else {
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


}

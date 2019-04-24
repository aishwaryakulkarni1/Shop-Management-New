package com.inevitablesol.www.shopmanagement.report;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.constatnt.WEBAPI;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import org.apache.poi.ss.formula.functions.T;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Report_expensive extends AppCompatActivity implements WEBAPI
{

    private static final String TAG = "Report_expensive";

    private TextView currentDate;
    private GlobalPool globalPool;
    private ProgressDialog loading;
    private String currentDateTimeString;
    private  ExpReports expReports;
    private ArrayList<Exprecord> exprecords;
    private RecyclerView recyclerView;
    private ExpAdapter expAdapter;
    private ImageView datePicker;

    private  TextView txt_totalBalnce;
    private  TextView txt_totalAmount;
    private ImageView imgExpenseDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_total_expense);

        datePicker = (ImageView) findViewById(R.id.date_billingDate);


        recyclerView=(RecyclerView)findViewById(R.id.stock_recyclerView);
        currentDate = (TextView) findViewById(R.id.bill_curruntDate);
        globalPool= (GlobalPool) this.getApplicationContext();
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDate();
            }
        });
        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyy-MM-dd");
        currentDateTimeString = dateFormat.format(dt);
        currentDate.setText(currentDateTimeString);

        imgExpenseDownload = (ImageView) findViewById(R.id.sale_download_product);
        imgExpenseDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Report_expensive.this,ReportActivity_Dailog.class));
            }
        });
        getExpReport();

    }

    private void showDate()
    {

        new YearMonthPickerDialog(this, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month)
            {
                Log.d(TAG, "onYearMonthSet: Year Month"+year+" "+month);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, 1);


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                currentDateTimeString= dateFormat.format(calendar.getTime());

                currentDate.setText(dateFormat.format(calendar.getTime()));
                // calendar.set(calendar.MONTH,month+1);
                Log.d(TAG, "onYearMonthSet: " + dateFormat.format(calendar.getTime()));
                calendar.add(Calendar.MONTH,1);
                calendar.add(Calendar.DATE,-1);

                Log.d(TAG, "onYearMonthSet: "+dateFormat.format(calendar.getTime()));

                //calendar.add(Calendar.DATE,-1);

                // nextMonth=dateFormat.format(calendar.getTime());


                Log.d(TAG, "onYearMonthSet:" + dateFormat.format(calendar.getTime()));

                getExpReport();

            }
        }).show();
    }

    private void getExpReport()
    {
        Log.d(TAG, "getExpReport:");
        loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        final StringRequest req = new StringRequest(Request.Method.POST, EXPENSEREPORT,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d(TAG, response.toString());
                        loading.dismiss();

                        try
                        {
                            JSONObject jsonObject=new JSONObject(response);
                            String message=jsonObject.getString("message");
                            if(message.equalsIgnoreCase("false"))
                            {
                                expAdapter.clearView();
                                expAdapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "Data Not Available", Toast.LENGTH_SHORT).show();

                            }else
                            {
                                Gson gson=new Gson();
                                ExpReports expReports=new ExpReports();
                                expReports=gson.fromJson(response,expReports.getClass());
                                exprecords= (ArrayList<Exprecord>) expReports.getExprecords();

                                expAdapter =new ExpAdapter(exprecords);
                                recyclerView.setHasFixedSize(true);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(expAdapter);

                                txt_totalAmount.setText(String.valueOf(expAdapter.getTotalAmount()));
                                txt_totalBalnce.setText(String.valueOf(expAdapter.totalBalance));



                            }





                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", globalPool.getDbname());
                params.put("startDate",currentDateTimeString);
                params.put("id","3");
                Log.d(TAG, "getParams:"+params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
        // Adding request to request queue
        // AppController.getInstance(getApplication()).addToRequestQueue(req);

    }
}

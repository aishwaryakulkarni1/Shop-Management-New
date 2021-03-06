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
import com.inevitablesol.www.shopmanagement.account.DocumentTypeAdapter;
import com.inevitablesol.www.shopmanagement.constatnt.WEBAPI;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Report_Purchase extends AppCompatActivity implements WEBAPI {

    private ImageView datePicker;
    private static final String TAG = "Report_Purchase";
    private TextView currentDate;
    private GlobalPool globalPool;
    private ProgressDialog loading;
    private String currentDateTimeString;
    private  PurchaseReports purchaseReports;
    private ArrayList<Purchaserecord> purchaserecords;
    private RecyclerView recyclerView;
    private Report_PurchaseAdapter purchaseReportAdapter;
    private ImageView imgPurchaseDownload;
    private  TextView txt_totalBalnce;
    private  TextView txt_totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_total_purchase);
        txt_totalAmount=(TextView)findViewById(R.id.txt_totalAmount);
        txt_totalBalnce=(TextView)findViewById(R.id.txt_balance);
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

        imgPurchaseDownload = (ImageView) findViewById(R.id.sale_download_product);
        imgPurchaseDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Report_Purchase.this,ReportActivity_Dailog.class));
            }
        });
        getReportByPurchase();
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

                getReportByPurchase();




            }
        }).show();
    }



    private void getReportByPurchase()
    {
        Log.d(TAG, "getReportByPurchase:");
        loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        final StringRequest req = new StringRequest(Request.Method.POST, PURCHASEREPORT,
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
                                               purchaseReportAdapter.clearView();
                                               purchaseReportAdapter.notifyDataSetChanged();
                                               Toast.makeText(getApplicationContext(), "Data Not Available", Toast.LENGTH_SHORT).show();

                                           }else
                                           {
                                               Gson gson=new Gson();
                                               PurchaseReports purchaseReport=new PurchaseReports();
                                               purchaseReports=gson.fromJson(response,purchaseReport.getClass());
                                               purchaserecords= (ArrayList<Purchaserecord>) purchaseReports.getPurchaserecords();

                                               purchaseReportAdapter=new Report_PurchaseAdapter(purchaserecords);
                                               recyclerView.setHasFixedSize(true);

                                               RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                               recyclerView.setLayoutManager(layoutManager);
                                               recyclerView.setAdapter(purchaseReportAdapter);

                                               txt_totalAmount.setText(String.valueOf(purchaseReportAdapter.getTotalAmount()));
                                               txt_totalBalnce.setText(String.valueOf(purchaseReportAdapter.totalBalance));



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

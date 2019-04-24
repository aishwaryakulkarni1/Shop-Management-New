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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Report_Tax extends AppCompatActivity
{
    private ImageView datePicker;
    private static final String TAG = "Report_Tax";
    private TextView currentDate;
    private GlobalPool globalPool;
    private ProgressDialog loading;
    private String currentDateTimeString;
    private RecyclerView recyclerView;
    private ImageView imgTaxDownload;

    private TextView sale_tax,sale_gst,sale_amnt,pur_tax,pur_gst,pur_amnt;
    private TextView  sale_cgst,sale_igst,sale_sgst,pur_cgst,pur_igst,pur_sgst;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tax_report);
        sale_tax=(TextView)findViewById(R.id.sale_tax);
        sale_amnt=(TextView)findViewById(R.id.sale_amount);
        sale_gst=(TextView)findViewById(R.id.sale_gst);

        sale_cgst=(TextView)findViewById(R.id.sale_cgst);
        sale_igst=(TextView)findViewById(R.id.sale_igst);
        sale_sgst=(TextView)findViewById(R.id.sale_sgst);

        pur_amnt=(TextView)findViewById(R.id.pur_totalAmount);
        pur_tax=(TextView)findViewById(R.id.pur_tax);
        pur_gst=(TextView)findViewById(R.id.pur_totalgst);

        pur_cgst=(TextView)findViewById(R.id.pur_cgst);
        pur_igst=(TextView)findViewById(R.id.pur_igst);
        pur_sgst=(TextView)findViewById(R.id.pur_sgst);

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

        imgTaxDownload = (ImageView) findViewById(R.id.sale_download_product);
        imgTaxDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Report_Tax.this,ReportActivity_Dailog.class));
            }
        });

        getReport();
    }

    private void showDate() {
        new YearMonthPickerDialog(this, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                Log.d(TAG, "onYearMonthSet: Year Month" + year + " " + month);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, 1);


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                currentDateTimeString = dateFormat.format(calendar.getTime());

                currentDate.setText(dateFormat.format(calendar.getTime()));
                // calendar.set(calendar.MONTH,month+1);
                Log.d(TAG, "onYearMonthSet: " + dateFormat.format(calendar.getTime()));
                calendar.add(Calendar.MONTH, 1);
                calendar.add(Calendar.DATE, -1);

                Log.d(TAG, "onYearMonthSet: " + dateFormat.format(calendar.getTime()));

                //calendar.add(Calendar.DATE,-1);

                // nextMonth=dateFormat.format(calendar.getTime());


                Log.d(TAG, "onYearMonthSet:" + dateFormat.format(calendar.getTime()));

                getReport();


            }
        }).show();
    }

    private void getReport()
    {
        Log.d(TAG, "getReportByPurchase:");
        loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        final StringRequest req = new StringRequest(Request.Method.POST, WEBAPI.TAXREPORT,
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
                            String message= jsonObject.getString("message");
                            Log.d(TAG, "onResponse: Measseag"+message);

                            if(message.equalsIgnoreCase("true"))
                            {

                                JSONArray jsonArray=jsonObject.getJSONArray("taxrecords");
                                            JSONObject jsonObject1= jsonArray.getJSONObject(0);
                                             pur_tax.setText(jsonObject1.getString("taxable"));
                                             pur_gst.setText(jsonObject1.getString("gst_total"));
                                             pur_amnt.setText(jsonObject1.getString("totalAmount"));

                                             pur_sgst.setText(jsonObject1.getString("sgst"));
                                           pur_igst.setText(jsonObject1.getString("igst"));
                                           pur_cgst.setText(jsonObject1.getString("cgst"));
                                JSONObject jsonObject2= jsonArray.getJSONObject(1);
                                sale_tax.setText(jsonObject2.getString("taxable"));
                                sale_gst.setText(jsonObject2.getString("gst_total"));
                                sale_amnt.setText(jsonObject2.getString("totalAmount"));

                                sale_sgst.setText(jsonObject2.getString("sgst"));
                                sale_igst.setText(jsonObject2.getString("igst"));
                                sale_cgst.setText(jsonObject2.getString("cgst"));


                            }





//



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
    }
}

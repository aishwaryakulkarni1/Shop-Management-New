package com.inevitablesol.www.shopmanagement.report;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Report_TaxRate extends AppCompatActivity {

    private ImageView imgTaxRateDownload;
    private String currentDateTimeString;
    private ImageView datePicker;
    private TextView currentDate;
    private static final String TAG = "Report_TaxRate";
    private ProgressDialog loading;
    private String url = "http://35.161.99.113:9000/webapi/report/taxRateReport";

    private String urlJsonArray1 = "http://35.161.99.113:9000/webapi/report/taxRategView0";
    private String urlJsonArray2 = "http://35.161.99.113:9000/webapi/report/taxRategView25";
    private String urlJsonArray3 = "http://35.161.99.113:9000/webapi/report/taxRategView3";
    private String urlJsonArray4= "http://35.161.99.113:9000/webapi/report/taxRategView5";
    private String urlJsonArray5 = "http://35.161.99.113:9000/webapi/report/taxRategView12";
    private String urlJsonArray6 = "http://35.161.99.113:9000/webapi/report/taxRategView18";
    private String urlJsonArray7 = "http://35.161.99.113:9000/webapi/report/taxRategView28";
    private TextView txt_taxvalue1, txt_cgst1, txt_sgst1, txt_igst1;
    private TextView txt_taxvalue2, txt_cgst2, txt_sgst2, txt_igst2;
    private TextView txt_taxvalue3, txt_cgst3, txt_sgst3, txt_igst3;
    private TextView txt_taxvalue4, txt_cgst4, txt_sgst4, txt_igst4;
    private TextView txt_taxvalue5, txt_cgst5, txt_sgst5, txt_igst5;
    private TextView txt_taxvalue6, txt_cgst6, txt_sgst6, txt_igst6;
    private TextView txt_taxvalue7, txt_cgst7, txt_sgst7, txt_igst7;

    private GlobalPool globalPool;
    private String TotalTaxableValue28, TotalCGst28, TotalSgst28, TotalIGST28;
    private String TotalTaxableValue18,TotalCGst18,TotalSgst18,TotalIGST18;
    private String TotalTaxableValue12,TotalCGst12,TotalSgst12,TotalIGST12;
    private String TotalTaxableValue5,TotalCGst5,TotalSgst5,TotalIGST5;
    private String TotalTaxableValue3,TotalCGst3,TotalSgst3,TotalIGST3;
    private String TotalTaxableValue025,TotalCGst025,TotalSgst025,TotalIGST025;
    private String TotalTaxableValue0,TotalCGst0,TotalSgst0,TotalIGST0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tax_rate_report);

        globalPool = (GlobalPool) this.getApplicationContext();
        datePicker = (ImageView) findViewById(R.id.total_imgfromDate);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        currentDate = (TextView) findViewById(R.id.bill_curruntDate);
        currentDateTimeString = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
        currentDate.setText(currentDateTimeString);

        imgTaxRateDownload = (ImageView) findViewById(R.id.sale_download_product);
        imgTaxRateDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Report_TaxRate.this, ReportActivity_Dailog.class));
            }
        });

        txt_taxvalue7 = (TextView) findViewById(R.id.tax_value_28);
        txt_cgst7 = (TextView) findViewById(R.id.cgst_28);
        txt_sgst7 = (TextView) findViewById(R.id.sgst_28);
        txt_igst7 = (TextView) findViewById(R.id.igst_28);

        txt_taxvalue6 = (TextView) findViewById(R.id.tax_value_18);
        txt_cgst6 = (TextView) findViewById(R.id.cgst_18);
        txt_sgst6 = (TextView) findViewById(R.id.sgst_18);
        txt_igst6 = (TextView) findViewById(R.id.igst_18);

        txt_taxvalue5 = (TextView) findViewById(R.id.tax_value_12);
        txt_cgst5 = (TextView) findViewById(R.id.cgst_12);
        txt_sgst5 = (TextView) findViewById(R.id.sgst_12);
        txt_igst5 = (TextView) findViewById(R.id.igst_12);

        txt_taxvalue4 = (TextView) findViewById(R.id.tax_value_5);
        txt_cgst4 = (TextView) findViewById(R.id.cgst_5);
        txt_sgst4 = (TextView) findViewById(R.id.sgst_5);
        txt_igst4 = (TextView) findViewById(R.id.igst_5);

        txt_taxvalue3 = (TextView) findViewById(R.id.tax_value_3);
        txt_cgst3 = (TextView) findViewById(R.id.cgst_3);
        txt_sgst3 = (TextView) findViewById(R.id.sgst_3);
        txt_igst3 = (TextView) findViewById(R.id.igst_3);

        txt_taxvalue2 = (TextView) findViewById(R.id.tax_value_025);
        txt_cgst2 = (TextView) findViewById(R.id.cgst_025);
        txt_sgst2 = (TextView) findViewById(R.id.sgst_025);
        txt_igst2 = (TextView) findViewById(R.id.igst_025);

        txt_taxvalue1 = (TextView) findViewById(R.id.tax_value_0);
        txt_cgst1 = (TextView) findViewById(R.id.cgst_0);
        txt_sgst1 = (TextView) findViewById(R.id.sgst_0);
        txt_igst1 = (TextView) findViewById(R.id.igst_0);
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

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
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

                getTaxRate();
            }
        }).show();
    }

    private void getTaxRate() {
        Log.d(TAG, "getTaxRate");
        loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

//TAX RATE 28
        final StringRequest req1 = new StringRequest(Request.Method.POST, urlJsonArray7,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        loading.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            /*String message = jsonObject.getString("Data Available");
                            if(message.equalsIgnoreCase("Data Available"))
                            {*/
                            JSONArray jsonArray = jsonObject.getJSONArray("taxRate28");

                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                            TotalTaxableValue28 = jsonObject1.getString("TotalTaxableValue");
                            TotalCGst28 = jsonObject1.getString("TotalCGst");
                            TotalSgst28 = jsonObject1.getString("TotalSgst");
                            TotalIGST28 = jsonObject1.getString("TotalIGST");

                            txt_taxvalue7.setText(TotalTaxableValue28);
                            txt_cgst7.setText(TotalCGst28);
                            txt_sgst7.setText(TotalSgst28);
                            txt_igst7.setText(TotalIGST28);
                           /* }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Data Unavailable",Toast.LENGTH_LONG).show();
                            }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NumberFormatException e) {
                            loading.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("dbname", globalPool.getDbname());
                param.put("startDate", currentDateTimeString);
                Log.d(TAG, "getParams:" + param.toString());

                return param;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(req1);

//TAX RATE 18
        final StringRequest req2 = new StringRequest(Request.Method.POST, urlJsonArray6,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        loading.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            /*String message = jsonObject.getString("Data Available");
                            if(message.equalsIgnoreCase("Data Available"))
                            {*/
                            JSONArray jsonArray = jsonObject.getJSONArray("taxRate18");

                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            TotalTaxableValue18 = jsonObject1.getString("TotalTaxableValue");
                            TotalCGst18 = jsonObject1.getString("TotalCGst");
                            TotalSgst18 = jsonObject1.getString("TotalSgst");
                            TotalIGST18 = jsonObject1.getString("TotalIGST");

                            txt_taxvalue6.setText(TotalTaxableValue18);
                            txt_cgst6.setText(TotalCGst18);
                            txt_sgst6.setText(TotalSgst18);
                            txt_igst6.setText(TotalIGST18);
                           /* }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Data Unavailable",Toast.LENGTH_LONG).show();
                            }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NumberFormatException e) {
                            loading.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("dbname", globalPool.getDbname());
                param.put("startDate", currentDateTimeString);
                Log.d(TAG, "getParams:" + param.toString());

                return param;
            }
        };

        RequestQueue rq1 = Volley.newRequestQueue(getApplicationContext());
        rq1.add(req2);

//TAX RATE 12
        final StringRequest req3 = new StringRequest(Request.Method.POST, urlJsonArray5,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        loading.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            /*String message = jsonObject.getString("Data Available");
                            if(message.equalsIgnoreCase("Data Available"))
                            {*/
                            JSONArray jsonArray = jsonObject.getJSONArray("taxRate12");

                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            TotalTaxableValue12 = jsonObject1.getString("TotalTaxableValue");
                            TotalCGst12 = jsonObject1.getString("TotalCGst");
                            TotalSgst12 = jsonObject1.getString("TotalSgst");
                            TotalIGST12 = jsonObject1.getString("TotalIGST");

                            txt_taxvalue5.setText(TotalTaxableValue12);
                            txt_cgst5.setText(TotalCGst12);
                            txt_sgst5.setText(TotalSgst12);
                            txt_igst5.setText(TotalIGST12);
                           /* }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Data Unavailable",Toast.LENGTH_LONG).show();
                            }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NumberFormatException e) {
                            loading.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("dbname", globalPool.getDbname());
                param.put("startDate", currentDateTimeString);
                Log.d(TAG, "getParams:" + param.toString());

                return param;
            }
        };

        RequestQueue rq2 = Volley.newRequestQueue(getApplicationContext());
        rq2.add(req3);

//TAX RATE 5
        final StringRequest req4 = new StringRequest(Request.Method.POST, urlJsonArray4,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        loading.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            /*String message = jsonObject.getString("Data Available");
                            if(message.equalsIgnoreCase("Data Available"))
                            {*/
                            JSONArray jsonArray = jsonObject.getJSONArray("taxRate5");

                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            TotalTaxableValue5 = jsonObject1.getString("TotalTaxableValue");
                            TotalCGst5 = jsonObject1.getString("TotalCGst");
                            TotalSgst5 = jsonObject1.getString("TotalSgst");
                            TotalIGST5 = jsonObject1.getString("TotalIGST");

                            txt_taxvalue4.setText(TotalTaxableValue5);
                            txt_cgst4.setText(TotalCGst5);
                            txt_sgst4.setText(TotalSgst5);
                            txt_igst4.setText(TotalIGST5);
                           /* }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Data Unavailable",Toast.LENGTH_LONG).show();
                            }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NumberFormatException e) {
                            loading.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("dbname", globalPool.getDbname());
                param.put("startDate", currentDateTimeString);
                Log.d(TAG, "getParams:" + param.toString());

                return param;
            }
        };

        RequestQueue rq3 = Volley.newRequestQueue(getApplicationContext());
        rq3.add(req4);

 //TAX RATE 3
        final StringRequest req5 = new StringRequest(Request.Method.POST, urlJsonArray3,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        loading.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            /*String message = jsonObject.getString("Data Available");
                            if(message.equalsIgnoreCase("Data Available"))
                            {*/
                            JSONArray jsonArray = jsonObject.getJSONArray("taxRate3");

                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            TotalTaxableValue3 = jsonObject1.getString("TotalTaxableValue");
                            TotalCGst3 = jsonObject1.getString("TotalCGst");
                            TotalSgst3 = jsonObject1.getString("TotalSgst");
                            TotalIGST3 = jsonObject1.getString("TotalIGST");

                            txt_taxvalue3.setText(TotalTaxableValue3);
                            txt_cgst3.setText(TotalCGst3);
                            txt_sgst3.setText(TotalSgst3);
                            txt_igst3.setText(TotalIGST3);
                           /* }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Data Unavailable",Toast.LENGTH_LONG).show();
                            }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NumberFormatException e) {
                            loading.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("dbname", globalPool.getDbname());
                param.put("startDate", currentDateTimeString);
                Log.d(TAG, "getParams:" + param.toString());

                return param;
            }
        };

        RequestQueue rq4 = Volley.newRequestQueue(getApplicationContext());
        rq4.add(req5);

//TAXRATE 0.25
        final StringRequest req6 = new StringRequest(Request.Method.POST, urlJsonArray2,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        loading.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            /*String message = jsonObject.getString("Data Available");
                            if(message.equalsIgnoreCase("Data Available"))
                            {*/
                            JSONArray jsonArray = jsonObject.getJSONArray("taxRate25");

                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            TotalTaxableValue025 = jsonObject1.getString("TotalTaxableValue");
                            TotalCGst025 = jsonObject1.getString("TotalCGst");
                            TotalSgst025= jsonObject1.getString("TotalSgst");
                            TotalIGST025 = jsonObject1.getString("TotalIGST");

                            txt_taxvalue2.setText(TotalTaxableValue025);
                            txt_cgst2.setText(TotalCGst025);
                            txt_sgst2.setText(TotalSgst025);
                            txt_igst2.setText(TotalIGST025);
                           /* }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Data Unavailable",Toast.LENGTH_LONG).show();
                            }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NumberFormatException e) {
                            loading.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("dbname", globalPool.getDbname());
                param.put("startDate", currentDateTimeString);
                Log.d(TAG, "getParams:" + param.toString());

                return param;
            }
        };

        RequestQueue rq5 = Volley.newRequestQueue(getApplicationContext());
        rq5.add(req6);

//TAX RATE 0
        final StringRequest req7 = new StringRequest(Request.Method.POST, urlJsonArray1,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        loading.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            /*String message = jsonObject.getString("Data Available");
                            if(message.equalsIgnoreCase("Data Available"))
                            {*/
                            JSONArray jsonArray = jsonObject.getJSONArray("taxRate0");

                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            TotalTaxableValue0 = jsonObject1.getString("TotalTaxableValue");
                            TotalCGst0 = jsonObject1.getString("TotalCGst");
                            TotalSgst0= jsonObject1.getString("TotalSgst");
                            TotalIGST0 = jsonObject1.getString("TotalIGST");

                            txt_taxvalue1.setText(TotalTaxableValue0);
                            txt_cgst1.setText(TotalCGst0);
                            txt_sgst1.setText(TotalSgst0);
                            txt_igst1.setText(TotalIGST0);
                           /* }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Data Unavailable",Toast.LENGTH_LONG).show();
                            }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NumberFormatException e) {
                            loading.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("dbname", globalPool.getDbname());
                param.put("startDate", currentDateTimeString);
                Log.d(TAG, "getParams:" + param.toString());

                return param;
            }
        };

        RequestQueue rq6 = Volley.newRequestQueue(getApplicationContext());
        rq6.add(req7);
    }
}

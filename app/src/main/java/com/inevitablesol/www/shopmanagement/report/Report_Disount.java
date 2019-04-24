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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Report_Disount extends AppCompatActivity {

    private ImageView imgDiscDownload;
    private String currentDateTimeString;
    private ImageView datePicker;
    private TextView currentDate;
    private TextView txt_s_totinvoice,txt_s_totsp,txt_s_totdiscount,txt_taxable,txt_totalsale;
    private TextView txt_p_totinvoice,txt_p_totsp,txt_p_totdiscount;

    private static final String TAG = "Report_Disount";
    private ProgressDialog loading;
    private String urlJsonArray1= "http://35.161.99.113:9000/webapi/report/discountSaleReport";
    private String urlJsonArray2= "http://35.161.99.113:9000/webapi/report/discountPurchaseReport";
    private GlobalPool globalPool;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_discount);

        datePicker= (ImageView) findViewById(R.id.date_billingDate);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        currentDate = (TextView) findViewById(R.id.bill_curruntDate);
        currentDateTimeString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        currentDate.setText(currentDateTimeString);

        imgDiscDownload = (ImageView) findViewById(R.id.img_discount);
        imgDiscDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Report_Disount.this,ReportActivity_Dailog.class));
            }
        });

        globalPool= (GlobalPool) this.getApplicationContext();

        txt_s_totinvoice = (TextView) findViewById(R.id.s_total_invoice);
        txt_s_totsp = (TextView)findViewById(R.id.s_total_sp);
        txt_s_totdiscount = (TextView)findViewById(R.id.s_total_discount);
        txt_taxable = (TextView)findViewById(R.id.taxable_value);
        txt_totalsale = (TextView)findViewById(R.id.total_sale);

        txt_p_totinvoice = (TextView) findViewById(R.id.p_total_invoice);
        txt_p_totsp = (TextView) findViewById(R.id.p_total_sp);
        txt_p_totdiscount = (TextView) findViewById(R.id.p_total_discount);
    }

    private void showDate(){
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
        new DatePickerDialog(Report_Disount.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));
    }

    private void updateDate(Calendar myCalendar){
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        currentDateTimeString= dateFormat.format(myCalendar.getTime());
        currentDate.setText(currentDateTimeString);

        getTotalSale_Status();

    }

    private void getTotalSale_Status() {

        Log.d(TAG,"getTotalSale_Status:");
        loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        final StringRequest req = new StringRequest(Request.Method.POST,urlJsonArray1,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d(TAG, response.toString());
                        loading.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            /*String message = jsonObject.getString("Data Available");
                           if(message.equalsIgnoreCase("Data Available"))
                            {*/
                                JSONArray jsonArray = jsonObject.getJSONArray("salerecords");
                                JSONObject jsonObject1=jsonArray.getJSONObject(0);
                                String totalInvoice = jsonObject1.getString("totalInvoice");
                                String totalTaxablevalue = jsonObject1.getString("totalTaxablevalue");
                                String  totalselling = jsonObject1.getString("totalselling");
                                String totalDiscount =  jsonObject1.getString("totalDiscount");
                                String TotalSale = jsonObject1.getString("TotalSale");

                                txt_s_totinvoice.setText(totalInvoice);
                                txt_taxable.setText(totalTaxablevalue);
                                txt_s_totsp.setText(totalselling);
                                txt_s_totdiscount.setText(totalDiscount);
                                txt_totalsale.setText(TotalSale);
                           /* }

                            else
                            {
                                Toast.makeText(getApplicationContext(),"Data Unavailable",Toast.LENGTH_LONG).show();
                            }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (NumberFormatException e)
                        {
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

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> param = new HashMap<>();
                param.put("dbname",globalPool.getDbname());
                param.put("date", currentDateTimeString);
                Log.d(TAG,"getParams:"+param.toString());

                return param;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        requestQueue1.add(req);

        final StringRequest req1 = new StringRequest(Request.Method.POST,urlJsonArray2,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d(TAG, response.toString());
                        loading.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                         // String message = jsonObject.getString("Data Available");
                          //  if(message.equalsIgnoreCase("Data Available"))
                          //  {
                                JSONArray jsonArray = jsonObject.getJSONArray("purchaserecords");

                                JSONObject jsonObject2=jsonArray.getJSONObject(0);
                                String no_of_purchase = jsonObject2.getString("no_of_purchase");
                                String Total_Selling = jsonObject2.getString("Total_Selling");
                                String total_discount =  jsonObject2.getString("total_discount");

                                txt_p_totinvoice.setText(no_of_purchase);
                                txt_p_totsp.setText(Total_Selling);
                                txt_p_totdiscount.setText(total_discount);
                            //}
                           /* else
                            {
                                Toast.makeText(getApplicationContext(),"Data Unavailable",Toast.LENGTH_LONG).show();
                            }
*/                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (NumberFormatException e)
                        {
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

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> param = new HashMap<>();
                param.put("dbname",globalPool.getDbname());
                param.put("date", currentDateTimeString);
                Log.d(TAG,"getParams:"+param.toString());

                return param;
            }
        };
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(req1);


    }
}

package com.inevitablesol.www.shopmanagement.report;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.gson.JsonObject;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.billing_module.BillingHistory;
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

public class DayBook_Activity extends AppCompatActivity implements ReportFragment.OnFragmentInteractionListener
{

    private ImageView imageView;
    private TextView txt_totalSale,txt_totalInvoice,txt_amntReceived,txt_balanceDue;

    private TextView txt_p_totalSale,txt_p_totalInvoice,txt_p_amntReceived,txt_p_balanceDue;

    private TextView txt_e_totalSale,txt_e_totalInvoice,txt_e_amntReceived,txt_e_balanceDue;


    private static final String TAG = "DayBook_Activity";
    private  ProgressDialog loading;
    private String urlJsonArry= "http://35.161.99.113:9000/webapi/report/dayReport";
    private GlobalPool globalPool;
    private String currentDateTimeString;
    private ImageView datePicker;
    private TextView currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // getLayoutInflater().inflate(R.layout.activity_day_book,frameLayout);

        setContentView(R.layout.activity_day_book);

        datePicker = (ImageView) findViewById(R.id.date_billingDate);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDate();
            }
        });
        currentDate = (TextView) findViewById(R.id.bill_curruntDate);
        txt_totalSale=(TextView)findViewById(R.id.total_sale);
        txt_amntReceived=(TextView)findViewById(R.id.total_received);
        txt_balanceDue=(TextView)findViewById(R.id.total_balanceDue);
        txt_totalInvoice=(TextView)findViewById(R.id.total_invoice);

        txt_p_totalSale=(TextView)findViewById(R.id.p_total_sale);
        txt_p_amntReceived=(TextView)findViewById(R.id.p_total_received);
        txt_p_balanceDue=(TextView)findViewById(R.id.p_total_balanceDue);
        txt_p_totalInvoice=(TextView)findViewById(R.id.p_total_invoice);

        txt_e_totalSale=(TextView)findViewById(R.id.e_total_sale);
        txt_e_amntReceived=(TextView)findViewById(R.id.e_total_received);
        txt_e_balanceDue=(TextView)findViewById(R.id.e_total_balanceDue);
        txt_e_totalInvoice=(TextView)findViewById(R.id.e_total_invoice);

        //Current Date Display
        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyy-MM-dd");
        currentDateTimeString = dateFormat.format(dt);
        currentDate.setText(currentDateTimeString);
        getTotalSale_Status();

        globalPool= (GlobalPool) this.getApplicationContext();

        imageView=(ImageView)findViewById(R.id.img_dayBook);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(DayBook_Activity.this,ReportActivity_Dailog.class);
                intent.putExtra("DayBook","DayBook");
                startActivity(intent);
            }
        });


    }


    @Override
    public void onFragmentInteraction(Uri uri)
    {

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


                updateDate(myCalendar);
            }

        };

        new DatePickerDialog(DayBook_Activity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));


    }

    private void updateDate(Calendar myCalendar)
    {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        currentDateTimeString= dateFormat.format(myCalendar.getTime());
        currentDate.setText(currentDateTimeString);

        getTotalSale_Status();
    }


    private void  getTotalSale_Status()
    {
        Log.d(TAG, "getTotalSale_Status:");
        loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        final StringRequest req = new StringRequest(Request.Method.POST,urlJsonArry,
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
                            if(message.equalsIgnoreCase("true"))
                            {
                                try
                                {
                                    JSONArray jsonArray=jsonObject.getJSONArray("records");



                                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                    if (jsonObject1.length()!=0) {
                                        txt_totalInvoice.setText(jsonObject1.getString("total_invoice"));
                                        txt_totalSale.setText(jsonObject1.getString("total_sale"));
                                        txt_balanceDue.setText(jsonObject1.getString("balance_due"));
                                        txt_amntReceived.setText(jsonObject1.getString("paid"));
                                    }


                                    JSONObject jsonObject3 = jsonArray.getJSONObject(1);
                                    if(jsonObject3.length()!=0) {
                                        txt_e_totalInvoice.setText(jsonObject3.getString("total_invoice"));
                                        txt_e_totalSale.setText(jsonObject3.getString("total_sale"));
                                        txt_e_balanceDue.setText(jsonObject3.getString("balance_due"));
                                        txt_e_amntReceived.setText(jsonObject3.getString("paid"));
                                    }

                                    JSONObject jsonObject2 = jsonArray.getJSONObject(2);
                                    if(jsonObject2.length()!=0) {
                                        txt_p_totalInvoice.setText(jsonObject2.getString("total_invoice"));
                                        txt_p_totalSale.setText(jsonObject2.getString("total_sale"));
                                        txt_p_balanceDue.setText(jsonObject2.getString("balance_due"));
                                        txt_p_amntReceived.setText(jsonObject2.getString("paid"));
                                    }
                                } catch (NumberFormatException e)
                                {
                                    loading.dismiss();
                                    e.printStackTrace();
                                } catch (JSONException e)
                                {
                                    loading.dismiss();
                                    e.printStackTrace();
                                }
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
                params.put("id","2");
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

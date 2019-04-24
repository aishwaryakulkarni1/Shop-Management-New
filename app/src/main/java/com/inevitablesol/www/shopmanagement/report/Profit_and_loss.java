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
import com.payu.payuui.Widget.MonthYearPickerDialog;
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

public class Profit_and_loss extends AppCompatActivity
{
    public String total_taxable,TotalGST, Total_Amount ;
    public String TotalSelling,TotalSgst,totalsale;
    public String total_Exp_taxable,Exp_TotalGST,Total_Exp_Amount;

    public float p_taxable,s_taxable,e_taxable,totalTax=0;
    public float p_gst,s_gst, e_gst,totalGst = 0;
    public float p_amt,s_amt,e_amt,totalAmt =0;


    private ImageView imgPLDownload;
    private TextView currentDate;
    private ImageView datePicker;
    private String currentDateTime;

    private TextView p_tax_value,p_tot_gst,p_tot_amt;
    private TextView s_tax_value,s_tot_gst,s_tot_amt;
    private TextView e_tax_value,e_tot_gst,e_tot_amt;
    private TextView total_tax_value,tot_gst,tot_amt,profit,loss;

    private static final String TAG = "Profit_and_loss";
    private ProgressDialog loading;
    private String urlJsonArray1= "http://35.161.99.113:9000/webapi/report/profitPurchase";
    private String urlJsonArray2= "http://35.161.99.113:9000/webapi/report/profitSale";
    private String urlJsonArray3= "http://35.161.99.113:9000/webapi/report/exp_profit";
    private GlobalPool globalPool;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_business_status);

        globalPool= (GlobalPool) this.getApplicationContext();
        p_tax_value = (TextView) findViewById(R.id.p_tax_value);
        p_tot_gst = (TextView) findViewById(R.id.p_tot_gst);
        p_tot_amt = (TextView) findViewById(R.id.p_tot_amt);

        s_tax_value = (TextView) findViewById(R.id.s_tax_value);
        s_tot_gst = (TextView) findViewById(R.id.s_tot_gst);
        s_tot_amt = (TextView) findViewById(R.id.s_tot_amt);

        e_tax_value=(TextView) findViewById(R.id.e_tax_value);
        e_tot_gst = (TextView) findViewById(R.id.e_tot_gst);
        e_tot_amt = (TextView) findViewById(R.id.e_tot_amt);

        total_tax_value = (TextView) findViewById(R.id.total_tax_value);
        tot_gst = (TextView) findViewById(R.id.tot_gst);
        tot_amt = (TextView) findViewById(R.id.tot_amt);
        profit = (TextView) findViewById(R.id.profit);
        loss = (TextView) findViewById(R.id.loss);

        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyy-MM");
        currentDateTime = dateFormat.format(dt);
        currentDate.setText(currentDateTime);
        getProfitLoss_Status();
        getCalculations();

        datePicker = (ImageView) findViewById(R.id.date_billingDate);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });




        imgPLDownload = (ImageView) findViewById(R.id.sale_download_product);
        imgPLDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profit_and_loss.this,ReportActivity_Dailog.class));
            }
        });



    }

    private void showDate(){
        new YearMonthPickerDialog(this, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month)
            {
                Log.d(TAG, "onYearMonthSet: Year Month"+year+" "+month);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH,1);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
                currentDateTime= dateFormat.format(calendar.getTime());

                currentDate.setText(dateFormat.format(currentDateTime));
                // calendar.set(calendar.MONTH,month+1);
                Log.d(TAG, "onYearMonthSet: " + dateFormat.format(calendar.getTime()));
                calendar.add(Calendar.MONTH,1);
                calendar.add(Calendar.DATE,-1);

                getProfitLoss_Status();
                Log.d(TAG, "onYearMonthSet: "+dateFormat.format(calendar.getTime()));

                //calendar.add(Calendar.DATE,-1);

                // nextMonth=dateFormat.format(calendar.getTime());

                Log.d(TAG, "onYearMonthSet:" + dateFormat.format(calendar.getTime()));

                getCalculations();


            }
        }).show();
    }

    public void getProfitLoss_Status(){


        Log.d(TAG,"getProfitLossStatus");
        loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        //Purchase
        final StringRequest req1 = new StringRequest(Request.Method.POST,urlJsonArray1,
                new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response)
                    {
                        Log.d(TAG, response.toString());
                        loading.dismiss();

                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            String msg = jsonObject1.getString("message");
                            if(msg.equalsIgnoreCase("Data Available"))
                            {
                                JSONArray jsonArray = jsonObject1.getJSONArray("PurchaseProfit");

                                JSONObject jsonObject2 = jsonArray.getJSONObject(0);

                                total_taxable = jsonObject2.getString("total_taxable");
                                TotalGST = jsonObject2.getString("TotalGST");
                                Total_Amount = jsonObject2.getString("Total_Amount");

                                p_tax_value.setText(total_taxable);
                                p_tot_gst.setText(TotalGST);
                                p_tot_amt.setText(Total_Amount);

                                p_taxable = Float.parseFloat(total_taxable);
                                p_gst = Float.parseFloat(TotalGST);
                                p_amt = Float.parseFloat(Total_Amount);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Data Unavailable",Toast.LENGTH_LONG).show();
                            }



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

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("dbname",globalPool.getDbname());
                param.put("date", currentDateTime);
                Log.d(TAG,"getParams:"+param.toString());

                return param;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        requestQueue1.add(req1);

        Log.d(total_taxable,"2total_taxable");

        //Sale
        final StringRequest req2 = new StringRequest(Request.Method.POST,urlJsonArray2,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d(TAG, response.toString());
                        loading.dismiss();

                        try {
                            JSONObject jsonObject3 = new JSONObject(response);
                            String msg = jsonObject3.getString("message");
                            Log.d("My Message", msg.toString());
                            if(msg.equalsIgnoreCase("Data Available"))
                            {
                                JSONArray jsonArray = jsonObject3.getJSONArray("salereprofit");

                                JSONObject jsonObject4 = jsonArray.getJSONObject(0);
                                TotalSelling = jsonObject4.getString("TotalSelling");
                                TotalSgst = jsonObject4.getString("TotalGst");
                                totalsale=jsonObject4.getString("totalsale");

                                s_tax_value.setText(TotalSelling);
                                s_tot_gst.setText(TotalSgst);
                                s_tot_amt.setText(totalsale);

                                s_taxable = Float.parseFloat(TotalSelling);
                                s_gst = Float.parseFloat(TotalSgst);
                                s_amt = Float.parseFloat(totalsale);

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Data Unavailable",Toast.LENGTH_LONG).show();
                            }

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("dbname",globalPool.getDbname());
                param.put("date", currentDateTime);
                Log.d(TAG,"getParams:"+param.toString());

                return param;
            }
        };
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(req2);

//Expense
        final StringRequest req3 = new StringRequest(Request.Method.POST,urlJsonArray3,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d(TAG, response.toString());
                        loading.dismiss();

                        try {
                            JSONObject jsonObject5 = new JSONObject(response);
                            String message = jsonObject5.getString("message");
                            if(message.equalsIgnoreCase("Data Available"))
                            {
                                JSONArray jsonArray = jsonObject5.getJSONArray("ExpenseProfit");

                                JSONObject jsonObject6 = jsonArray.getJSONObject(0);
                                total_Exp_taxable =jsonObject6.getString("total_Exp_taxable");
                                Exp_TotalGST = jsonObject6.getString("Exp_TotalGST");
                                Total_Exp_Amount = jsonObject6.getString("Total_Exp_Amount");

                                e_tax_value.setText(total_Exp_taxable);
                                e_tot_gst.setText(Exp_TotalGST);
                                e_tot_amt.setText(Total_Exp_Amount);

                                e_taxable = Float.parseFloat(total_Exp_taxable);
                                e_gst = Float.parseFloat(Exp_TotalGST);
                                e_amt = Float.parseFloat(Total_Exp_Amount);

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Data Unavailable", Toast.LENGTH_LONG).show();
                            }

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("dbname",globalPool.getDbname());
                param.put("date", currentDateTime);
                Log.d(TAG,"getParams:"+param.toString());

                return param;
            }

        };
        RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
        requestQueue3.add(req3);


    }

    public void getCalculations(){

        Log.d(TAG,"getCalculations");

        totalTax = p_taxable+s_taxable+e_taxable;
        String sumTax = Float.toString(totalTax);

        totalGst = p_gst + s_gst + e_gst;
        String sumGst = Float.toString(totalGst);

        totalAmt = p_amt + s_amt +e_amt;
        String  sumAmt = Float.toString(totalAmt);

        total_tax_value.setText(sumTax);
        tot_gst.setText(sumGst);
        tot_amt.setText(sumAmt);
    }
}

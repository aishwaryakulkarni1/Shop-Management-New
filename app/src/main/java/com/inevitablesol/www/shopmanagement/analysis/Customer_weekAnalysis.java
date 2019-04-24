package com.inevitablesol.www.shopmanagement.analysis;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer_weekAnalysis extends AppCompatActivity
{

    private ImageView imageView;
    private GraphView graphView;
  //  private TextView txtfromDate,txtToDate;
    private static final String TAG = "Customer_weekAnalysis";

    private String urlJsonArry="http://35.161.99.113:9000/webapi/custana/week";
    private  ProgressDialog loading;
    private GlobalPool globalPool;
    private TextView fromDate;
    private TextView txtTodate;
    private Integer totalCustomer=0;
    private TextView txt_totalCustomer;
    private  TextView txt_txt_highestDay,txt_lowestCust;

    private TextView txt_highestday,txt_lowestDay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_week_analysis);
        imageView=(ImageView)findViewById(R.id.img_onlyYear);
        txt_totalCustomer=(TextView)findViewById(R.id.total_customer);
         txt_txt_highestDay=(TextView)findViewById(R.id.txt_higestDay);
        txt_lowestCust=(TextView)findViewById(R.id.txt_lowestDay) ;
                 // txtfromDate = (TextView) findViewById(R.id.fromdate);
        globalPool= (GlobalPool) this.getApplicationContext();
       // txtToDate=(TextView)findViewById(R.id.toDate);
        graphView = (GraphView) findViewById(R.id.graph);
        fromDate=(TextView)findViewById(R.id.txt_fromdate);
        txtTodate=(TextView)findViewById(R.id.txt_todate);

        txt_highestday=(TextView)findViewById(R.id.highestDay);
        txt_lowestDay=(TextView)findViewById(R.id.lowestDay);







        graphView=(GraphView)findViewById(R.id.graph);

        Intent intent=getIntent();
      if(intent.hasExtra("to_date"))
    {
        fromDate.setText(intent.getStringExtra("from_date"));
        txtTodate.setText(intent.getStringExtra("to_date"));
        getTotalSale_Status(intent.getStringExtra("from_date"),intent.getStringExtra("to_date"));
    }
    }


//




    @Override
    protected void onResume()
    {
        super.onResume();
       // showGraph(saleCount);
    }

    private void showGraph(ArrayList<Integer> saleCount)
    {
        graphView.clearFocus();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]
                {
                        new DataPoint(0, 0),
                new DataPoint(1, saleCount.get(0)),
                new DataPoint(2, saleCount.get(1)),
                new DataPoint(3, saleCount.get(2)),
                new DataPoint(4, saleCount.get(3)),
                new DataPoint(5, saleCount.get(4)),
                new DataPoint(6, saleCount.get(5)),
                new DataPoint(7, saleCount.get(6)),


        });
//        graphView.getGridLabelRenderer().setVerticalAxisTitle("No of Customer");
//        graphView.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.color_grey));
//       // series.setColor(getResources().getColor(R.color.green));
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);
         //series.setSpacing(20);
        graphView.getViewport().setMaxY(Collections.max(saleCount)+1);
        graphView.getViewport().setMaxX(7);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[] { " ","D1", "D2", "D3","D4" ,"D5","D6","D7" });
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graphView.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.colorPrimary));
        graphView.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.colorPrimary));
        graphView.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.color_grey));
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScrollableY(true);
        graphView.addSeries(series);
        // series.setDrawBackground(true);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(15);
        series.setAnimated(true);
//        series.setDrawBackground(true);
//        series.setDrawDataPoints(true);
//        series.setDataPointsRadius(15);
        series.setAnimated(true);


    }



    private void  getTotalSale_Status(final String from_date, final String to_date)
    {
        Log.d(TAG, "getTotalSale_Status:");
        loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        final StringRequest req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        loading.dismiss();
                        Log.d(TAG, response.toString());

                        try
                        {
                            JSONObject jsonObject=new JSONObject(response);
                            String message= jsonObject.getString("message");
                            if(message.equalsIgnoreCase("Data available"))
                            {
                                Gson converter = new Gson();
                                Type type = new TypeToken<List<Integer>>(){}.getType();
                                ArrayList<Integer> saleCount = converter.fromJson(jsonObject.getString("week1"), type );

                                Log.d(TAG, "onResponse: CUST INFO"+saleCount);
                                  showGraph(saleCount);
                                calculateDays(saleCount);
                                //convert List to Array in Java String [] strArray = list.toArray(new String[0]);
//                                ArrayList<Double> purchaseCount = converter.fromJson(jsonObject.getString("purchaseCount"), type );
//                                ArrayList<Double> expCount = converter.fromJson(jsonObject.getString("expCount"), type );
//                                mutliChart_Graph(saleCount,purchaseCount,expCount);
//                                calculateValue(saleCount,purchaseCount,expCount);
//                                //String[] s =new String[]{jsonObject.getString("saleCount")};
//                                //  Log.d(TAG, "onResponse: String"+s[0]);
//                                //   int[] s1= ;
//                                //List<String> strings= new ArrayList<String>(Integer.parseInt(jsonObject.getString("saleCount")));
//                                Log.d(TAG, "onResponse: "+saleCount.toString());
//
//                                Log.d(TAG, "onResponse: Purchase"+purchaseCount.toString());
//                                Log.d(TAG, "onResponse: Exp"+expCount.toString());

                                // JSONArray jsonArray=new JSONArray();
                                //  String  [] string =new ArrayList<String>s;
                                //  ArrayList<String> strings=new ArrayList<String>(s);

                                // double[] strings=new double[]{Double.parseDouble(jsonObject.getString("saleCount"))};
                                //Log.d(TAG, "onResponse: "+string[1]);
//                                double sale= Double.parseDouble(jsonObject.getString("saleCount"));
//                                double purchase= Double.parseDouble(jsonObject.getString("purchaseCount"));
//                                double expcount= Double.parseDouble(jsonObject.getString("expCount"));
                                // barGraph(sale,purchase,expcount);
                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
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
                params.put("from_date",from_date);
                params.put("to_date",to_date);
                params.put("type","week");
                Log.d(TAG, "getParams:"+params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
        // Adding request to request queue
        // AppController.getInstance(getApplication()).addToRequestQueue(req);

    }

    private void calculateDays(ArrayList<Integer> saleCount)
    {
        int i=0;
             if(saleCount.size()>0)
             {
                 for (Integer a:saleCount)
                 {
                     totalCustomer +=a;

                 }
             }
        txt_totalCustomer.setText(String.valueOf(totalCustomer));

        Log.d(TAG, "calculateCustomer "+totalCustomer);

          int max = Collections.max(saleCount);
          int min =  Collections.min(saleCount);
                  int day=     saleCount.indexOf(max);
                 int minday=     saleCount.indexOf(min);
        Log.d(TAG, "calculateDays: max"+max);
        Log.d(TAG, "calculateDays: min"+min);

        String Day=findDay(day);
        String minDay=findDay(minday);
        txt_lowestDay.setText(minDay);
        txt_highestday.setText(Day);

        Log.d(TAG, "calculateDays: Min "+minDay);
        txt_txt_highestDay.setText(String.valueOf(max));
        txt_lowestCust.setText(String.valueOf(min));
        Log.d(TAG, "calculateDays: "+Day);
    }


    private String findDay(int salindex)
    {
        switch (salindex)
        {
            case 0:
                return  "DAY 1";
            case 1:
                return  "DAY 2";

            case 2:
                return  "DAY 3";

            case 3:
                return  "DAY 4";

            case 4:
                return  "DAY 5";

            case 5:
                return  "DAY 6";

            case 6:
                return  "DAY 7";


            default:
                Toast.makeText(getApplicationContext(),"Wrong Day",Toast.LENGTH_LONG).show();
        }
        return "Day";

    }
}

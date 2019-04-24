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
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import org.bouncycastle.jce.provider.JCEBlockCipher;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer_monthAnalysis extends AppCompatActivity
{
    private  String nextMonth;
    private ImageView imageView;
    private  GraphView graphView;
    private TextView txtmonth;
    private TextView txtYear;
    private  TextView txt_weekMaxCustomer;
    private  TextView txt_maxweek,txt_loweek;


    private static final String TAG = "Customer_monthAnalysis";
    private  ProgressDialog loading;

    private String urlJsonArry="http://35.161.99.113:9000/webapi/custana/week";
    private GlobalPool globalPool;
    private ArrayList<Integer> week1=new ArrayList<>();
    private ArrayList<Integer> week2=new ArrayList<>();
    private ArrayList<Integer> week3=new ArrayList<>();
    private ArrayList<Integer> week4=new ArrayList<>();
    private ArrayList<Integer> week5=new ArrayList<>();
    private  TextView txt_highestCust,txt_lowestCustWeek;
    private  TextView txt_month;

    private  ArrayList<Integer> weekArray=new ArrayList<>();//c_img_onlymonth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_month_analysis);
      //  imageView = (ImageView) findViewById(R.id.c_img_onlymonth);

        txtmonth = (TextView) findViewById(R.id.a_c_month);
       // txtYear = (TextView) findViewById(R.id.a_c_year);
//        graphView = (GraphView) findViewById(R.id.graph);
        txt_weekMaxCustomer=(TextView)findViewById(R.id.highestCustomer);
        txt_lowestCustWeek=(TextView)findViewById(R.id.lowest_Customer_week);
        txt_highestCust=(TextView)findViewById(R.id.highestCustomer_week);

        txt_maxweek=(TextView)findViewById(R.id.highestweek);
        txt_loweek=(TextView)findViewById(R.id.lowestweek);

        globalPool= (GlobalPool) this.getApplicationContext();


//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//
//               // chageMonth();
//            }
//        });
        graphView = (GraphView) findViewById(R.id.graph);
        Intent intent=getIntent();
        if(intent.hasExtra("to_date"))
        {
            txtmonth.setText(intent.getStringExtra("from_date"));
            getTotalSale_Status(intent.getStringExtra("from_date"),intent.getStringExtra("to_date"));
        }

    }


    private void chageMonth()
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
                txt_month.setText(dateFormat.format(calendar.getTime()));
                // calendar.set(calendar.MONTH,month+1);
                Log.d(TAG, "onYearMonthSet: " + dateFormat.format(calendar.getTime()));
                calendar.add(Calendar.MONTH,1);
                calendar.add(Calendar.DATE,-1);

                nextMonth=dateFormat.format(calendar.getTime());

                //calendar.add(Calendar.DATE,-1);

                // nextMonth=dateFormat.format(calendar.getTime());

                getTotalSale_Status(txt_month.getText().toString().trim(),nextMonth);

                Log.d(TAG, "onYearMonthSet:" + dateFormat.format(calendar.getTime()));




            }
        }).show();
   }

//    private void chageDate()
//    {
//        final Calendar myCalendar = Calendar.getInstance();
//
//        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
//        {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                Date date1 = myCalendar.getTime();
//
//                //updateDate(myCalendar);
//                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM");
//                String currentDateTimeString = dateFormat.format(myCalendar.getTime());
//                txtmonth.setText(currentDateTimeString);
//
//            }
//
//        };
//
//        new DatePickerDialog(this, date, myCalendar
//                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//
//        Log.d("date", String.valueOf(myCalendar.getTime()));
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
       // showGraph();
    }

//    private void showGraph()
//    {
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
//                new DataPoint(0, 3),
//                new DataPoint(1, 6),
//                new DataPoint(2, 78),
//                new DataPoint(3, 45),
//                new DataPoint(4, 80),
//                new DataPoint(5, 25)
//        });
////        graphView.getGridLabelRenderer().setVerticalAxisTitle("No of Customer");
////        graphView.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.color_grey));
////       // series.setColor(getResources().getColor(R.color.green));
//        graphView.getViewport().setXAxisBoundsManual(true);
//        graphView.getViewport().setYAxisBoundsManual(true);
//
//        graphView.getViewport().setMaxY(100);
//        graphView.getViewport().setMaxX(5);
//
//        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
//        staticLabelsFormatter.setHorizontalLabels(new String[]{ "", "W1", "W2", "W3", "W4", "W5"});
//        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
//
//        graphView.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.colorPrimary));
//        graphView.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.colorPrimary));
//        graphView.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.color_grey));
//        graphView.getViewport().setScrollable(true);
//        graphView.getViewport().setScrollableY(true);
//        graphView.addSeries(series);
//        series.setDrawBackground(true);
//        series.setDrawDataPoints(true);
//        series.setDataPointsRadius(15);
//        series.setAnimated(true);
//
//
//    }

    private void showGraph()
    {
         graphView.clearFocus();

        weekArray.add((int) calculateSum(week1));
        weekArray.add((int) calculateSum(week2));
        weekArray.add((int) calculateSum(week3));
        weekArray.add((int) calculateSum(week4));
        weekArray.add((int) calculateSum(week5));

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0),
                new DataPoint(1, calculateSum(week1)),
                new DataPoint(2, calculateSum(week2)),
                new DataPoint(3, calculateSum(week3)),
                new DataPoint(4, calculateSum(week4)),
                new DataPoint(5, calculateSum(week5))
        });
//        graphView.getGridLabelRenderer().setVerticalAxisTitle("No of Customer");
//        graphView.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.color_grey));
//       // series.setColor(getResources().getColor(R.color.green));
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMaxY(Collections.max(weekArray));
        graphView.getViewport().setMaxX(5);

//        graphView.getViewport().setMaxY(100);
//        graphView.getViewport().setMaxX(5);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[]{ "", "W1", "W2", "W3", "W4", "W5"});
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


//        series.setDrawBackground(true);
//        series.setDrawDataPoints(true);
//        series.setDataPointsRadius(15);
        loading.dismiss();
//        if(weekArray.size()>0)
//        {
//            weekArray.clear();
//        }

        Log.d(TAG, "showGraph: WeekArray"+weekArray);
        calculateWeek();


    }

    private void calculateWeek()
    {
        Object maxCust = Collections.max(weekArray);
        Object minCust = Collections.min(weekArray);

        Log.d(TAG, "calculateValue: max Cust" + maxCust);
        Log.d(TAG, "calculateValue: min Cust" + minCust);


        txt_weekMaxCustomer.setText(String.valueOf((int) calculateSum(weekArray)));



        Log.d(TAG, "calculateValue: sale index" + weekArray.indexOf(maxCust));

        int maxindex = weekArray.indexOf(maxCust);
        int mindex = weekArray.indexOf(minCust);

        txt_loweek.setText("W"+findWeek(mindex));
        txt_maxweek.setText("W"+findWeek(maxindex));



//        String Day=findDay(salindex);
     //   txt_highestCust.setText(String.valueOf(findWeek(maxindex)));
       // txt_lowestCustWeek.setText(String.valueOf(findWeek(mindex)));

        txt_highestCust.setText(String.valueOf(maxCust));
        txt_lowestCustWeek.setText(String.valueOf(minCust));

       // calculateLowestValue(weekArray );




        Log.d(TAG, "calculateValue: sale week"+findWeek(maxindex));


    }

//    private void calculateLowestValue(ArrayList<Integer> weekArray)
//    {
//        int min = weekArray.get(0);
//        int max = weekArray.get(0);
//
//        for(Integer i: weekArray)
//        {
//            if(i < min) min = i;
//            if(i > max) max = i;
//        }
//
//        System.out.println("min = " + min);
//        System.out.println("max = " + max);
//
//    }

    private int findWeek(int maxindex)
    {
        switch ( maxindex)
        {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            default:
                Toast.makeText(getApplicationContext(), "Wrong Week", Toast.LENGTH_SHORT).show();


        }
        return -1;
    }



    private void  getTotalSale_Status(final String from_date, final String to_date)
    {
        Log.d(TAG, "getTotalSale_Status:");
        loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        final StringRequest req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        try
                        {
                            JSONObject jsonObject=new JSONObject(response);
                            String message= jsonObject.getString("message");
                            if(message.equalsIgnoreCase("Data available"))
                            {
                                Gson converter = new Gson();
                                Type type = new TypeToken<List<Integer>>() {}.getType();
//                                ArrayList<Double> saleCount = converter.fromJson(jsonObject.getString("saleCount"), type); //convert List to Array in Java String [] strArray = list.toArray(new String[0]);
//                                ArrayList<Double> purchaseCount = converter.fromJson(jsonObject.getString("purchaseCount"), type);
//                                ArrayList<Double> expCount = converter.fromJson(jsonObject.getString("expCount"), type);//monthSale1
//                                calculateValue(saleCount, purchaseCount, expCount);
//
                                if(week1.size()>0)
                                {
                                    week1.clear();
                                    week2.clear();
                                    week3.clear();
                                    week4.clear();
                                    week5.clear();
                                }
                                week1 = converter.fromJson(jsonObject.getString("week1"), type);
                                week2 = converter.fromJson(jsonObject.getString("week2"), type);
                                week3 = converter.fromJson(jsonObject.getString("week3"), type);
                                week4 = converter.fromJson(jsonObject.getString("week4"), type);
                                week5 = converter.fromJson(jsonObject.getString("week5"), type);

                                  showGraph();
//
//                                expCount1 = converter.fromJson(jsonObject.getString("weekExp1"), type);
//                                expCount2 = converter.fromJson(jsonObject.getString("weekExp2"), type);
//                                expCount3 = converter.fromJson(jsonObject.getString("weekExp3"), type);
//                                expCount4 = converter.fromJson(jsonObject.getString("weekExp4"), type);
//                                expCount5 = converter.fromJson(jsonObject.getString("weekExp5"), type);
//
//
//                                purchaseCount1 = converter.fromJson(jsonObject.getString("weekPurchase1"), type);
//                                purchaseCount2 = converter.fromJson(jsonObject.getString("weekPurchase2"), type);
//                                purchaseCount3 = converter.fromJson(jsonObject.getString("weekPurchase3"), type);
//                                purchaseCount4 = converter.fromJson(jsonObject.getString("weekPurchase4"), type);
//                                purchaseCount5 = converter.fromJson(jsonObject.getString("weekPurchase5"), type);
//
//                                mutliChart_Graph(saleCount, purchaseCount, expCount);


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
                params.put("type","month");
                Log.d(TAG, "getParams:"+params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
        // Adding request to request queue
        // AppController.getInstance(getApplication()).addToRequestQueue(req);

    }



    private  double calculateSum(ArrayList<Integer> doubles)
    {
        int sum=0;
        int i = 0;
        try {

            for (Integer sale : doubles)
            {
                if(sale!=null)
                sum += sale;

                i++;


            }
        } catch (NumberFormatException e)
        {
            e.printStackTrace();
            return sum;

        } finally
        {
            Log.d(TAG, "calculateSum:  i" + i);

        }
        return sum;

    }
}

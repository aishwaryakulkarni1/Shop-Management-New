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
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

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

public class Customer_YearAnalysis extends AppCompatActivity
{

   private ImageView imageView;
    private GraphView graphView;
    private TextView  txtyear;

    private ProgressDialog loading;

    private TextView txt_highest_month,txt_lowestMonth;

    private String urlJsonArry="http://35.161.99.113:9000/webapi/custana/week";
    private GlobalPool globalPool;
    private static final String TAG = "Customer_YearAnalysis";
    private ArrayList<Integer> yearCount1=new ArrayList<>();
    private ArrayList<Integer> monthList1=new ArrayList<>();
    private ArrayList<Integer> monthList2=new ArrayList<>();
    private ArrayList<Integer> monthList3=new ArrayList<>();
    private ArrayList<Integer> monthList5=new ArrayList<>();
    private ArrayList<Integer> monthList6=new ArrayList<>();
    private ArrayList<Integer> monthList7=new ArrayList<>();
    private ArrayList<Integer> monthList8=new ArrayList<>();
    private ArrayList<Integer> monthList9=new ArrayList<>();
    private ArrayList<Integer> monthList10=new ArrayList<>();
    private ArrayList<Integer> monthList11=new ArrayList<>();
    private ArrayList<Integer> monthList12=new ArrayList<>();
    private ArrayList<Integer> monthList4=new ArrayList<>();
    private ArrayList<Integer> monthArray=new ArrayList<>();

    private TextView txt_maxCust,txt_highestCust,txt_minCust;
    private TextView txt_year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_year_analysis);
        //imageView=(ImageView)findViewById(R.id.img_onlyYear);
        txtyear= (TextView) findViewById(R.id.yearOnly);
        graphView = (GraphView) findViewById(R.id.graph);
        txt_highest_month=(TextView)findViewById(R.id.highestmonth);
        txt_lowestMonth=(TextView)findViewById(R.id.lowestmonth);

        txt_maxCust=(TextView)findViewById(R.id.txt_totalCustomer);
        txt_highestCust=(TextView)findViewById(R.id.highestCust);
        txt_minCust=(TextView)findViewById(R.id.txt_lowestCust);
        txt_year=(TextView)findViewById(R.id.yearOnly);

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//
//                chageYear();
//            }
//        });
        graphView=(GraphView)findViewById(R.id.graph);
        globalPool= (GlobalPool) this.getApplicationContext();

        Intent intent=getIntent();
        if(intent.hasExtra("to_date"))
        {
            txt_year.setText(intent.getStringExtra("from_date"));
            getTotalSale_Status(intent.getStringExtra("from_date"),intent.getStringExtra("to_date"));
        }
    }
//    private void chageDate()
//    {
//        final Calendar myCalendar = Calendar.getInstance();
//
//        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth)
//            {
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                Date date1 = myCalendar.getTime();
//
//                //updateDate(myCalendar);
//                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yy");
//                String currentDateTimeString = dateFormat.format(myCalendar.getTime());
//                txtyear.setText(currentDateTimeString);
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
//
//        Intent intent=getIntent();
//        if(intent.hasExtra("to_date"))
//        {
//            getTotalSale_Status(intent.getStringExtra("from_date"),intent.getStringExtra("to_date"));
//        }
//
//    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }

    private void showGraph()
    {
        monthArray.add((int) calculateSum(monthList1));
        monthArray.add((int) calculateSum(monthList2));
        monthArray.add((int) calculateSum(monthList3));
        monthArray.add((int) calculateSum(monthList4));
        monthArray.add((int) calculateSum(monthList5));

        monthArray.add((int) calculateSum(monthList6));
        monthArray.add((int) calculateSum(monthList7));
        monthArray.add((int) calculateSum(monthList8));
        monthArray.add((int) calculateSum(monthList9));
        monthArray.add((int) calculateSum(monthList10));

        monthArray.add((int) calculateSum(monthList11));
        monthArray.add((int) calculateSum(monthList12));
        graphView.clearFocus();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(1, calculateSum(monthList1)),
                new DataPoint(2, calculateSum(monthList2)),
                new DataPoint(3, calculateSum(monthList3)),

                new DataPoint(4, calculateSum(monthList4)),
                new DataPoint(5, calculateSum(monthList5)),
                new DataPoint(6, calculateSum(monthList6)),
                new DataPoint(7, calculateSum(monthList7)),
                new DataPoint(8,calculateSum(monthList8)),
                new DataPoint(9,calculateSum(monthList9)),
                new DataPoint(10,calculateSum(monthList10)),
                new DataPoint(11,calculateSum(monthList11)),
                new DataPoint(12,calculateSum(monthList12))

        });
//        graphView.getGridLabelRenderer().setVerticalAxisTitle("No of Customer");
//        graphView.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.color_grey));
//       // series.setColor(getResources().getColor(R.color.green));
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);

        graphView.getViewport().setMaxY(Collections.max(monthArray));
        graphView.getViewport().setMaxX(12);


        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"JAN", "FEB", "MAR","APR" ,"May","JUN","JUL","AUG","SEP","OCT","NOV","DEC"});
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
//        if(monthArray.size()>0)
//        {
//            monthArray.clear();
//        }



         calculateWeek();

        Log.d(TAG, "showGraph: MonthArray"+monthArray);


    }


    private void calculateWeek()
    {
        Object maxCust = Collections.max(monthArray);
        Object minCust = Collections.min(monthArray);

        Log.d(TAG, "calculateValue: max Cust" + maxCust);
        Log.d(TAG, "calculateValue: min Cust" + minCust);


        txt_maxCust.setText(String.valueOf((int) calculateSum(monthArray)));



        Log.d(TAG, "calculateValue: month index" + monthArray.indexOf(maxCust));

        int maxindex = monthArray.indexOf(maxCust);
        int mindex = monthArray.indexOf(minCust);

        Log.d(TAG, "calculateWeek: "+maxindex);
        Log.d(TAG, "calculateWeek: "+mindex);



//        String Day=findDay(salindex);
        //   txt_highestCust.setText(String.valueOf(findWeek(maxindex)));
        // txt_lowestCustWeek.setText(String.valueOf(findWeek(mindex)));

        txt_highestCust.setText(String.valueOf(maxCust));
        txt_minCust.setText(String.valueOf(minCust));
        txt_highest_month.setText(findWeek(maxindex));
        txt_lowestMonth.setText(findWeek(mindex));

        // calculateLowestValue(weekArray );




      //  Log.d(TAG, "calculateValue: sale week"+findWeek(maxindex));


    }

    private String  findWeek(int maxindex)
    {
        switch ( maxindex)
        {
            case 0:
                return "JAN";
            case 1:
                return "FEB";
            case 2:
                return "MAR";
            case 3:
                return "APR";
            case 4:
                return "May";
            case 5:
                return "JUN";
            case 6:
                return "JUL";
            case 7:
                return "AUG";
            case 8:
                return "SEP";
            case 9:
                return "OCT";
            case 10:
                return "NOV";
            case 11:
                return "DEC";
            default:
                Toast.makeText(getApplicationContext(), "Wrong Week", Toast.LENGTH_SHORT).show();


        }
        return "";
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
                        Log.d(TAG, response.toString());

                        try
                        {
                            JSONObject jsonObject=new JSONObject(response);
                            String message= jsonObject.getString("message");
                            if(message.equalsIgnoreCase("Data available"))
                            {
                                loading.dismiss();
                                Gson converter = new Gson();
                                Type type = new TypeToken<List<Integer>>() {}.getType();
                                if(monthList1.size()>0)
                                {
                                    monthList1.clear();
                                    monthList2.clear();
                                    monthList3.clear();
                                    monthList4.clear();
                                    monthList5.clear();

                                    monthList6.clear();
                                    monthList7.clear();
                                    monthList8.clear();
                                    monthList9.clear();
                                    monthList10.clear();

                                    monthList11.clear();
                                    monthList12.clear();
                                }

                                monthList1 = converter.fromJson(jsonObject.getString("month1"), type);
                                monthList2 = converter.fromJson(jsonObject.getString("month2"), type);
                                monthList3 = converter.fromJson(jsonObject.getString("month3"), type);
                                monthList4 = converter.fromJson(jsonObject.getString("month4"), type);
                                monthList5 = converter.fromJson(jsonObject.getString("month5"), type);


                                monthList6 = converter.fromJson(jsonObject.getString("month6"), type);
                                monthList7 = converter.fromJson(jsonObject.getString("month7"), type);
                                monthList8 = converter.fromJson(jsonObject.getString("month8"), type);
                                monthList9 = converter.fromJson(jsonObject.getString("month9"), type);
                                monthList10 = converter.fromJson(jsonObject.getString("month10"), type);
                                monthList11 = converter.fromJson(jsonObject.getString("month11"), type);
                                monthList12 = converter.fromJson(jsonObject.getString("month12"), type);

                                   showGraph();



                                    Log.d(TAG, "onResponse: "+monthList1);



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
            public void onErrorResponse(VolleyError error)
            {
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
                params.put("type","year");
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


    private void chageYear()
    {

        new YearMonthPickerDialog(this, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month)
            {
                Log.d(TAG, "onYearMonthSet: Year Month"+year+" "+month);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                txtyear.setText(dateFormat.format(calendar.getTime()));
                Log.d(TAG, "onYearSet: " + dateFormat.format(calendar.getTime()));
                calendar.add(Calendar.YEAR,1);
                calendar.add(Calendar.DATE,-1);

                String nextYear = dateFormat.format(calendar.getTime());
                getTotalSale_Status(txtyear.getText().toString().trim(),nextYear);
                Log.d(TAG, "onYearSet: " + nextYear);

            }
        }).show();
    }

}

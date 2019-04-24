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
import com.inevitablesol.www.shopmanagement.datePicker.DatePick;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Total_WeekAnalysis extends AppCompatActivity
{
    private GraphView graph;
    private static final String TAG = "Total_WeekAnalysis";
    private String urlJsonArry="http://35.161.99.113:9000/webapi/report/doubleDate";
    private GlobalPool globalPool;
    private TextView txt_sale,txt_purchase,txt_exp;

    private  TextView txt_saleDay,txt_expDay,txt_purchaseDay;

    private  TextView txt_purchaseAmount,txt_saleamount,txt_expAmount;
    double mtotal_sale=0.0;
    double mtotal_expn=0.0;
    double mtotal_purchase=0.0;
    TextView fromDate,txtTodate,txt_month;
    private  ProgressDialog loading;
    private ArrayList<Double> totalArray=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total__week_analysis);
        graph=(GraphView)findViewById(R.id.graph);
        fromDate=(TextView)findViewById(R.id.txt_fromdate);
        txtTodate=(TextView)findViewById(R.id.txt_todate);


        globalPool= (GlobalPool) this.getApplicationContext();
        txt_exp=(TextView)findViewById(R.id.txt_expense);
        txt_sale=(TextView)findViewById(R.id.txt_sale);
        txt_purchase=(TextView)findViewById(R.id.txt_purchase);
         txt_expDay=(TextView)findViewById(R.id.expday);
        txt_saleDay=(TextView)findViewById(R.id.saleDay);
         txt_purchaseDay=(TextView)findViewById(R.id.purchaseDay);

         txt_purchaseAmount=(TextView)findViewById(R.id.purchaseAmount);
          txt_saleamount=(TextView)findViewById(R.id.saleAmount);
          txt_expAmount=(TextView)findViewById(R.id.expamount);

        Intent intent=getIntent();
        if(intent.hasExtra("to_date"))
        {
              fromDate.setText(intent.getStringExtra("from_date"));
              txtTodate.setText(intent.getStringExtra("to_date"));

            getTotalSale_Status(intent.getStringExtra("from_date"),intent.getStringExtra("to_date"));
        }


    }



    @Override
    protected void onResume() {
        super.onResume();
       // mutliChart_Graph();
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
                                Type type = new TypeToken<List<Double>>(){}.getType();
                                ArrayList<Double> saleCount = converter.fromJson(jsonObject.getString("saleCount"), type ); //convert List to Array in Java String [] strArray = list.toArray(new String[0]);
                                ArrayList<Double> purchaseCount = converter.fromJson(jsonObject.getString("purchaseCount"), type );
                                ArrayList<Double> expCount = converter.fromJson(jsonObject.getString("expCount"), type );
                                calculateValue(saleCount,purchaseCount,expCount);
                                mutliChart_Graph(saleCount,purchaseCount,expCount);

                                //String[] s =new String[]{jsonObject.getString("saleCount")};
                              //  Log.d(TAG, "onResponse: String"+s[0]);
                             //   int[] s1= ;
                                //List<String> strings= new ArrayList<String>(Integer.parseInt(jsonObject.getString("saleCount")));
                                Log.d(TAG, "onResponse: "+saleCount.toString());

                                Log.d(TAG, "onResponse: Purchase"+purchaseCount.toString());
                                Log.d(TAG, "onResponse: Exp"+expCount.toString());

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
                Log.d(TAG, "getParams:"+params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
        // Adding request to request queue
        // AppController.getInstance(getApplication()).addToRequestQueue(req);

    }

    private void calculateValue(ArrayList<Double> saleCount, ArrayList<Double> purchaseCount, ArrayList<Double> expCount)
    {
        int i=0;
        try
        {

            for (double sale: saleCount)
            {
                mtotal_sale+=sale;
                mtotal_expn+=Double.parseDouble(String.valueOf(expCount.get(i)));
                mtotal_purchase+=Double.parseDouble(String.valueOf(purchaseCount.get(i)));
                i++;


            }
        } catch (NumberFormatException e)
        {
            e.printStackTrace();
        }finally
        {
            Log.d(TAG, "calculateValue:  i"+i);
            txt_purchase.setText(String.valueOf(Math.round(mtotal_purchase*100)/100.00));
            txt_sale.setText(String.valueOf(Math.round(mtotal_sale*100)/100.00));
            txt_exp.setText(String.valueOf(Math.round(mtotal_expn*100)/100.00));
        }

                  Double maxSale= Collections.max(saleCount);
                  Double   maxPurchase=  Collections.max(purchaseCount);
                   Double  maxexpCount=   Collections.max(expCount);
                 Log.d(TAG, "calculateValue: max Sale"+maxSale);

                  Log.d(TAG, "calculateValue: sale index"+saleCount.indexOf(maxSale));
                 Log.d(TAG, "calculateValue: sale index"+purchaseCount.indexOf(maxPurchase));
                     Log.d(TAG, "calculateValue: sale index"+expCount.indexOf(maxexpCount));
                       int salindex=saleCount.indexOf(maxSale);
                       int expIndex=expCount.indexOf(maxexpCount);
                         int maxindex = purchaseCount.indexOf(maxPurchase);
                             String Day=findDay(salindex);
                        txt_purchaseDay.setText(findDay(maxindex));
                         txt_expDay.setText(findDay(expIndex));
                          txt_saleDay.setText(findDay(salindex));

                          txt_purchaseAmount.setText(String.valueOf(Math.round(maxPurchase*100)/100.00));
                          txt_saleamount.setText(String.valueOf(Math.round(maxSale*100)/100.00));
                           txt_expAmount.setText(String.valueOf(Math.round(maxexpCount*100)/100.00));

        Log.d(TAG, "calculateValue: Day"+Day);

          totalArray.add(mtotal_sale);
          totalArray.add(mtotal_expn);
          totalArray.add(mtotal_purchase);


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


    private void mutliChart_Graph( ArrayList<Double>  saleCount, ArrayList<Double>  purchaseCount,  ArrayList<Double>  expCount)
    {
        Log.d(TAG, "mutliChart_Graph: Purchase"+purchaseCount.toString());
        Log.d(TAG, "mutliChart_Graph: Purchase"+String.valueOf(purchaseCount.get(3)));
        graph.clearFocus();



        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]
                {
                new DataPoint(0, 0),
                new DataPoint(1,Double.parseDouble(String.valueOf(saleCount.get(0)))),
                new DataPoint(2, Double.parseDouble(String.valueOf(saleCount.get(1)))),
                new DataPoint(3, Double.parseDouble(String.valueOf(saleCount.get(2)))),
                new DataPoint(4, Double.parseDouble(String.valueOf(saleCount.get(3)))),
                        new DataPoint(5, Double.parseDouble(String.valueOf(saleCount.get(4)))),
                        new DataPoint(6, Double.parseDouble(String.valueOf(saleCount.get(5)))),
                        new DataPoint(7,Double.parseDouble(String.valueOf(saleCount.get(6))))
        });
        series.setSpacing(30);
        series.setColor(getResources().getColor(R.color.green));
        series.setAnimated(true);
        graph.addSeries(series);

        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, Double.parseDouble(String.valueOf(purchaseCount.get(0)))),
                new DataPoint(2, Double.parseDouble(String.valueOf(purchaseCount.get(1)))),
                new DataPoint(3, Double.parseDouble(String.valueOf(purchaseCount.get(2)))),
                new DataPoint(4, Double.parseDouble(String.valueOf(purchaseCount.get(3)))),
                new DataPoint(5, Double.parseDouble(String.valueOf(purchaseCount.get(4)))),
                new DataPoint(6, Double.parseDouble(String.valueOf(purchaseCount.get(5)))),
                new DataPoint(7, Double.parseDouble(String.valueOf(purchaseCount.get(6))))
        });
        series2.setColor(getResources().getColor(R.color.yellow));
        series2.setSpacing(30);
        series2.setAnimated(true);
        graph.addSeries(series2);

        BarGraphSeries<DataPoint> series3 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, Double.parseDouble(String.valueOf(expCount.get(0)))),
                new DataPoint(2, Double.parseDouble(String.valueOf(expCount.get(1)))),
                new DataPoint(3, Double.parseDouble(String.valueOf(expCount.get(2)))),
                new DataPoint(4, Double.parseDouble(String.valueOf(expCount.get(3)))),
                new DataPoint(5, Double.parseDouble(String.valueOf(expCount.get(4)))),
                new DataPoint(6, Double.parseDouble(String.valueOf(expCount.get(5)))),
                new DataPoint(7, Double.parseDouble(String.valueOf(expCount.get(6))))
        });
        series3.setColor(getResources().getColor(R.color.deepOrange));
        series3.setSpacing(30);
        series3.setAnimated(true);
        graph.addSeries(series3);



        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(Collections.max(totalArray));
        graph.getViewport().setMaxX(7);
        // graph.getViewport().setMinX(-2);
//        graph.getViewport().setMaxX(6);
//        graph.getViewport().setMaxY(100);
//        graph.getViewport().setMaxX(7);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] { " ","D1", "D2", "D3","D4" ,"D5","D6","D7" });
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.colorPrimary));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.colorPrimary));
        graph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.color_grey));
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);

        // legend
        series.setTitle("SALE");
        series3.setTitle("EXP");
        series2.setTitle("PUR");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        loading.dismiss();
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

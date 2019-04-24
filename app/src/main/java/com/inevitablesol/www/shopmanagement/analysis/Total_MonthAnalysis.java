package com.inevitablesol.www.shopmanagement.analysis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Total_MonthAnalysis extends AppCompatActivity
{
 private GraphView graphView;
    private static final String TAG = "Total_MonthAnalysis";
    private GlobalPool globalPool;
    private String urlJsonArry="http://35.161.99.113:9000/webapi/report/doubleDate";


    private TextView txt_purchaseAmount, txt_saleamount, txt_expAmount;
    double mtotal_sale = 0.0;
    double mtotal_expn = 0.0;
    double mtotal_purchase = 0.0;
    TextView fromDate, txtTodate;
    private TextView txt_saleDay, txt_expDay, txt_purchaseDay;

    private TextView txt_sale,txt_purchase,txt_exp;

    ArrayList<Double> saleCount1; //;converter.fromJson(jsonObject.getString("monthSale1"), type);
    ArrayList<Double> saleCount2;// = converter.fromJson(jsonObject.getString("monthSale2"), type);
    ArrayList<Double> saleCount3 ;// = converter.fromJson(jsonObject.getString("monthSale3"), type);
    ArrayList<Double> saleCount4 ;//= converter.fromJson(jsonObject.getString("monthSale4"), type);
    ArrayList<Double> saleCount5 ;// = converter.fromJson(jsonObject.getString("monthSale5"), type);
    ArrayList<Double> saleCount6  ;// = converter.fromJson(jsonObject.getString("monthSale6"), type);
    ArrayList<Double> saleCount7  ;// = converter.fromJson(jsonObject.getString("monthSale7"), type);


    ArrayList<Double> purchaseCount1  ;//= converter.fromJson(jsonObject.getString("monthPur1"), type);
    ArrayList<Double> purchaseCount2  ;// = converter.fromJson(jsonObject.getString("monthPur2"), type);
    ArrayList<Double> purchaseCount3  ;//= converter.fromJson(jsonObject.getString("monthPur3"), type);
    ArrayList<Double> purchaseCount4  ;//= converter.fromJson(jsonObject.getString("monthPur4"), type);
    ArrayList<Double> purchaseCount5  ;//= converter.fromJson(jsonObject.getString("monthPur5"), type);
    ArrayList<Double> purchaseCount6  ;//= converter.fromJson(jsonObject.getString("monthPur6"), type);
    ArrayList<Double> purchaseCount7  ;//= converter.fromJson(jsonObject.getString("monthPur7"), type);



    ArrayList<Double> expCount1  ;//= converter.fromJson(jsonObject.getString("monthExp1"), type);
    ArrayList<Double> expCount2 ;//= converter.fromJson(jsonObject.getString("monthExp2"), type);
    ArrayList<Double> expCount3 ;// = converter.fromJson(jsonObject.getString("monthExp3"), type);
    ArrayList<Double> expCount4  ;//= converter.fromJson(jsonObject.getString("monthExp4"), type);
    ArrayList<Double> expCount5  ;//= converter.fromJson(jsonObject.getString("monthExp5"), type);
    ArrayList<Double> expCount6  ;// = converter.fromJson(jsonObject.getString("monthExp6"), type);
    ArrayList<Double> expCount7  ;// = converter.fromJson(jsonObject.getString("monthExp7"), type);

    ArrayList<Double> weekexp=new ArrayList<>();
    ArrayList<Double> weekpurchase=new ArrayList<>();
    ArrayList<Double> weeksale=new ArrayList<>();

    private ProgressDialog loading;
    private  TextView txt_month;

    private ArrayList<Double> totalValue=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total__month_analysis);
        graphView=(GraphView)findViewById(R.id.graph);
        globalPool= (GlobalPool) this.getApplicationContext();
        txt_exp=(TextView)findViewById(R.id.txt_expense);
        txt_sale=(TextView)findViewById(R.id.txt_sale);
        txt_purchase=(TextView)findViewById(R.id.txt_purchase);

        txt_purchaseAmount=(TextView)findViewById(R.id.purchaseAmount);
        txt_saleamount=(TextView)findViewById(R.id.saleAmount);
        txt_expAmount=(TextView)findViewById(R.id.expamount);
        txt_month=(TextView)findViewById(R.id.txt_month);


        txt_expDay=(TextView)findViewById(R.id.month_expday);
        txt_saleDay=(TextView)findViewById(R.id.month_saleDay);
        txt_purchaseDay=(TextView)findViewById(R.id.month_purchaseDay);




        Intent intent=getIntent();
        if(intent.hasExtra("to_date"))
        {
            txt_month.setText(intent.getStringExtra("from_date"));
            getTotalSale_Status(intent.getStringExtra("from_date"),intent.getStringExtra("to_date"));
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        //multiChart_graph();
    }

    private void mutliChart_Graph(ArrayList<Double> saleCount, ArrayList<Double> purchaseCount, ArrayList<Double> expCount)
    {

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, calculateSum(saleCount1)),
                new DataPoint(2, calculateSum(saleCount2)),
                new DataPoint(3, calculateSum(saleCount3)),
                new DataPoint(4, calculateSum(saleCount4)),
                new DataPoint(5, calculateSum(saleCount5))

        });
        series.setSpacing(30);
        series.setColor(getResources().getColor(R.color.green));
        series.setAnimated(true);
        graphView.addSeries(series);

        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, calculateSum(purchaseCount1)),
                new DataPoint(2, calculateSum(purchaseCount2)),
                new DataPoint(3, calculateSum(purchaseCount3)),
                new DataPoint(4, calculateSum(purchaseCount4)),
                new DataPoint(5, calculateSum(purchaseCount5))
        });
        series2.setColor(getResources().getColor(R.color.yellow));
        series2.setSpacing(30);
        series2.setAnimated(true);
        graphView.addSeries(series2);

        BarGraphSeries<DataPoint> series3 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, calculateSum(expCount1)),
                new DataPoint(2, calculateSum(expCount2)),
                new DataPoint(3, calculateSum(expCount3)),
                new DataPoint(4, calculateSum(expCount4)),
                new DataPoint(5, calculateSum(expCount5))
        });
        series3.setColor(getResources().getColor(R.color.deepOrange));
        series3.setSpacing(30);
        series3.setAnimated(true);
        graphView.addSeries(series3);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMaxY(Collections.max(totalValue));
        graphView.getViewport().setMaxX(5);
       // graphView.getGridLabelRenderer().setVerticalAxisTitle("Total in Rs");


        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[] { " ","W1", "w2", "w3","w4" ,"w5" });
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graphView.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.colorPrimary));
        graphView.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.colorPrimary));
        graphView.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.color_grey));
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScrollableY(true);

         weekexp.add(calculateSum(expCount1));
        weekexp.add(calculateSum(expCount2));
        weekexp.add(calculateSum(expCount3));
        weekexp.add(calculateSum(expCount4));
        weekexp.add(calculateSum(expCount5));

         weekpurchase.add(calculateSum(purchaseCount1));
        weekpurchase.add(calculateSum(purchaseCount2));
        weekpurchase.add(calculateSum(purchaseCount3));
        weekpurchase.add(calculateSum(purchaseCount4));
        weekpurchase.add(calculateSum(purchaseCount5));

        weeksale.add(calculateSum(saleCount1));
        weeksale.add(calculateSum(saleCount2));
        weeksale.add(calculateSum(saleCount3));
        weeksale.add(calculateSum(saleCount4));
        weeksale.add(calculateSum(saleCount5));

          loading.dismiss();

        Log.d(TAG, "mutliChart_Graph: Week Exp"+weekexp.toString());
        Log.d(TAG, "mutliChart_Graph: Week pyrchase"+weekpurchase.toString());
        Log.d(TAG, "mutliChart_Graph: Week sale"+weeksale.toString());
        //Log.d(TAG, "mutliChart_Graph: Week sale"+weeksale.toString());
        calculateWeek();

    }

    private void calculateWeek()
    {
        Object maxSale = Collections.max(weeksale);
        Object maxPurchase = Collections.max(weekpurchase);
        Object maxexpCount = Collections.max(weekexp);
        Log.d(TAG, "calculateValue: max Sale" + maxSale);
        Log.d(TAG, "calculateValue: max exp" + maxexpCount);
        Log.d(TAG, "calculateValue: max purchase" + maxPurchase);

        txt_purchaseAmount.setText(String.valueOf(maxPurchase));
        txt_saleamount.setText(String.valueOf(Math.round((Double) maxSale * 100.0) / 100.0));
        txt_expAmount.setText(String.valueOf(maxexpCount));


        Log.d(TAG, "calculateValue: sale index" + weeksale.indexOf(maxSale));
        Log.d(TAG, "calculateValue: purchase index" + weekpurchase.indexOf(maxPurchase));
        Log.d(TAG, "calculateValue: exp index" + weekexp.indexOf(maxexpCount));
        int saleindex = weeksale.indexOf(maxSale);
        int purchasepIndex = weekpurchase.indexOf(maxPurchase);
        int expindex = weekexp.indexOf(maxexpCount);


//        String Day=findDay(salindex);
        txt_purchaseDay.setText(String.valueOf(findWeek(purchasepIndex)));
        txt_expDay.setText(String.valueOf(findWeek(expindex)));
        txt_saleDay.setText(String.valueOf(findWeek(saleindex)));



         Log.d(TAG, "calculateValue: sale week"+findWeek(saleindex));
        Log.d(TAG, "calculateValue: purchase week"+findWeek(purchasepIndex));

        Log.d(TAG, "calculateValue: exp week"+findWeek(expindex));

    }

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

    @Override
    protected void onDestroy()
    {

        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        graphView.clearFocus();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
                                Type type = new TypeToken<List<Double>>() {}.getType();
                                ArrayList<Double> saleCount = converter.fromJson(jsonObject.getString("saleCount"), type); //convert List to Array in Java String [] strArray = list.toArray(new String[0]);
                                ArrayList<Double> purchaseCount = converter.fromJson(jsonObject.getString("purchaseCount"), type);
                                ArrayList<Double> expCount = converter.fromJson(jsonObject.getString("expCount"), type);//monthSale1

                                calculateValue(saleCount, purchaseCount, expCount);

                                saleCount1 = converter.fromJson(jsonObject.getString("weekSale1"), type);
                                saleCount2 = converter.fromJson(jsonObject.getString("weekSale2"), type);
                                saleCount3 = converter.fromJson(jsonObject.getString("weekSale3"), type);
                                saleCount4 = converter.fromJson(jsonObject.getString("weekSale4"), type);
                                saleCount5 = converter.fromJson(jsonObject.getString("weekSale5"), type);

                                expCount1 = converter.fromJson(jsonObject.getString("weekExp1"), type);
                                expCount2 = converter.fromJson(jsonObject.getString("weekExp2"), type);
                                expCount3 = converter.fromJson(jsonObject.getString("weekExp3"), type);
                                expCount4 = converter.fromJson(jsonObject.getString("weekExp4"), type);
                                expCount5 = converter.fromJson(jsonObject.getString("weekExp5"), type);
                                purchaseCount1 = converter.fromJson(jsonObject.getString("weekPurchase1"), type);
                                purchaseCount2 = converter.fromJson(jsonObject.getString("weekPurchase2"), type);
                                purchaseCount3 = converter.fromJson(jsonObject.getString("weekPurchase3"), type);
                                purchaseCount4 = converter.fromJson(jsonObject.getString("weekPurchase4"), type);
                                purchaseCount5 = converter.fromJson(jsonObject.getString("weekPurchase5"), type);

                                mutliChart_Graph(saleCount, purchaseCount, expCount);


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



    private void calculateValue(ArrayList<Double> saleCount, ArrayList<Double> purchaseCount, ArrayList<Double> expCount) {
        int i = 0;
        try {

            for (double sale : saleCount)
            {
                mtotal_sale += sale;
                mtotal_expn += Double.parseDouble(String.valueOf(expCount.get(i)));
                mtotal_purchase += Double.parseDouble(String.valueOf(purchaseCount.get(i)));
                i++;


            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally
        {
            Log.d(TAG, "calculateValue:  i" + i);
            txt_purchase.setText(String.valueOf(Math.round(mtotal_purchase*100)/100.00));
            txt_sale.setText(String.valueOf(Math.round(mtotal_sale*100)/100.00));
            txt_exp.setText(String.valueOf(Math.round(mtotal_expn*100)/100.00));

            totalValue.add(mtotal_expn);
            totalValue.add(mtotal_purchase);
            totalValue.add(mtotal_sale);
        }

       /* Object maxSale = Collections.max(saleCount);
        Object maxPurchase = Collections.max(purchaseCount);
        Object maxexpCount = Collections.max(expCount);
        Log.d(TAG, "calculateValue: max Sale" + maxSale);
        Log.d(TAG, "calculateValue: sale index" + saleCount.indexOf(maxSale));
        Log.d(TAG, "calculateValue: purchase index" + purchaseCount.indexOf(maxPurchase));
        Log.d(TAG, "calculateValue: exp index" + expCount.indexOf(maxexpCount));
//        int salindex = saleCount.indexOf(maxSale);
//        int expIndex = expCount.indexOf(maxSale);
//        int maxindex = purchaseCount.indexOf(maxSale);
////        String Day=findDay(salindex);
//        txt_purchaseDay.setText(findDay(maxindex));
//        txt_expDay.setText(findDay(expIndex));
//        txt_saleDay.setText(findDay(salindex));
//
//        txt_purchaseAmount.setText(String.valueOf(maxPurchase));
//        txt_saleamount.setText(String.valueOf(maxSale));
//        txt_expAmount.setText(String.valueOf(maxexpCount));

        //  Log.d(TAG, "calculateValue: Day"+Day);
        */


    }



    private  double calculateSum(ArrayList<Double> doubles)
    {
        double sum=0.0;
        int i = 0;
        try {

            for (double sale : doubles) {
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

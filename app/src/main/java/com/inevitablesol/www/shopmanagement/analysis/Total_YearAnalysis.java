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
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Total_YearAnalysis extends AppCompatActivity {

    private GraphView graph;
    private GlobalPool globalPool;
    private static final String TAG = "Total_YearAnalysis";
    private String urlJsonArry = "http://35.161.99.113:9000/webapi/report/year";

    private TextView txt_purchaseAmount, txt_saleamount, txt_expAmount;
    double mtotal_sale = 0.0;
    double mtotal_expn = 0.0;
    double mtotal_purchase = 0.0;
    TextView fromDate, txtTodate, txt_month;
    private TextView txt_saleDay, txt_expDay, txt_purchaseDay;

    private TextView txt_sale,txt_purchase,txt_exp;
    private  ProgressDialog loading;


    ArrayList<Double> saleCount1 =new ArrayList<>(); //;converter.fromJson(jsonObject.getString("monthSale1"), type);
//    ArrayList<Double> saleCount2;// = converter.fromJson(jsonObject.getString("monthSale2"), type);
//    ArrayList<Double> saleCount3 ;// = converter.fromJson(jsonObject.getString("monthSale3"), type);
//    ArrayList<Double> saleCount4 ;//= converter.fromJson(jsonObject.getString("monthSale4"), type);
//    ArrayList<Double> saleCount5 ;// = converter.fromJson(jsonObject.getString("monthSale5"), type);
//    ArrayList<Double> saleCount6  ;// = converter.fromJson(jsonObject.getString("monthSale6"), type);
//    ArrayList<Double> saleCount7  ;// = converter.fromJson(jsonObject.getString("monthSale7"), type);
//    ArrayList<Double> saleCount8   ;//= converter.fromJson(jsonObject.getString("monthSale8"), type);
//    ArrayList<Double> saleCount9   ;//= converter.fromJson(jsonObject.getString("monthSale9"), type);
//    ArrayList<Double> saleCount10  ;//= converter.fromJson(jsonObject.getString("monthSale10"), type);
//    ArrayList<Double> saleCount11  ;//= converter.fromJson(jsonObject.getString("monthSale11"), type);
//    ArrayList<Double> saleCount12  ;//= converter.fromJson(jsonObject.getString("monthSale12"), type);

    ArrayList<Double> purchaseCount1 = new ArrayList<>();//= converter.fromJson(jsonObject.getString("monthPur1"), type);
//


    ArrayList<Double> expCount1  =new ArrayList<>();//= converter.fromJson(jsonObject.getString("monthExp1"), type);
//    ArrayList<Double> expCount2 ;//= converter.fromJson(jsonObject.getString("monthExp2"), type);
//    ArrayList<Double> expCount3 ;// = converter.fromJson(jsonObject.getString("monthExp3"), type);
//    ArrayList<Double> expCount4  ;//= converter.fromJson(jsonObject.getString("monthExp4"), type);
//    ArrayList<Double> expCount5  ;//= converter.fromJson(jsonObject.getString("monthExp5"), type);
//    ArrayList<Double> expCount6  ;// = converter.fromJson(jsonObject.getString("monthExp6"), type);
//    ArrayList<Double> expCount7  ;// = converter.fromJson(jsonObject.getString("monthExp7"), type);
//    ArrayList<Double> expCount8  ;//= converter.fromJson(jsonObject.getString("monthExp8"), type);
//    ArrayList<Double> expCount9  ;//= converter.fromJson(jsonObject.getString("monthExp9"), type);
//    ArrayList<Double> expCount10  ;//= converter.fromJson(jsonObject.getString("monthExp10"), type);
//    ArrayList<Double> expCount11  ;//= converter.fromJson(jsonObject.getString("monthExp11"), type);
//    ArrayList<Double> expCount12  ;//= converter.fromJson(jsonObject.getString("monthExp12"), type);



    ArrayList<Double> monthexp=new ArrayList<>();
    ArrayList<Double> monthpurchase=new ArrayList<>();
    ArrayList<Double> monthsale=new ArrayList<>();
    private TextView year;
    private ArrayList<Double> totalValue=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total__year_analysis);
        graph = (GraphView) findViewById(R.id.graph);
        globalPool = (GlobalPool) this.getApplicationContext();

        txt_exp=(TextView)findViewById(R.id.txt_expense);
        txt_sale=(TextView)findViewById(R.id.txt_sale);
        txt_purchase=(TextView)findViewById(R.id.txt_purchase);

        txt_expDay=(TextView)findViewById(R.id.expday);
        txt_saleDay=(TextView)findViewById(R.id.saleDay);
        txt_purchaseDay=(TextView)findViewById(R.id.purchaseDay);
        year=(TextView)findViewById(R.id.year);

        txt_purchaseAmount=(TextView)findViewById(R.id.purchaseAmount);
        txt_saleamount=(TextView)findViewById(R.id.saleAmount);
        txt_expAmount=(TextView)findViewById(R.id.expamount);

        Intent intent = getIntent();
        if (intent.hasExtra("to_date"))
        {
            year.setText(intent.getStringExtra("from_date"));
            getTotalSale_Status(intent.getStringExtra("from_date"), intent.getStringExtra("to_date"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mutliChart_Graph(saleCount, purchaseCount, expCount);
    }

    private void mutliChart_Graph(ArrayList<Double> saleCount, ArrayList<Double> purchaseCount, ArrayList<Double> expCount)
    {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0),
                new DataPoint(1, saleCount1.get(0)),
                new DataPoint(2,saleCount1.get(1)),
                new DataPoint(3, saleCount1.get(2)),
                new DataPoint(4, saleCount1.get(3)),
                new DataPoint(5, saleCount1.get(4)),
                new DataPoint(6, saleCount1.get(5)),
                new DataPoint(7, saleCount1.get(6)),
                new DataPoint(8, saleCount1.get(7)),
                new DataPoint(9, saleCount1.get(8)),
                new DataPoint(10, saleCount1.get(9)),
                new DataPoint(11,saleCount1.get(10)),
                new DataPoint(12, saleCount1.get(11))

        });
        series.setSpacing(30);
        series.setColor(getResources().getColor(R.color.green));
        series.setAnimated(true);
        graph.addSeries(series);

        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0),
                new DataPoint(1, purchaseCount1.get(0)),
                new DataPoint(2,purchaseCount1.get(1)),
                new DataPoint(3, purchaseCount1.get(2)),
                new DataPoint(4, purchaseCount1.get(3)),
                new DataPoint(5, purchaseCount1.get(4)),
                new DataPoint(6, purchaseCount1.get(5)),
                new DataPoint(7, purchaseCount1.get(6)),
                new DataPoint(8, purchaseCount1.get(7)),
                new DataPoint(9, purchaseCount1.get(8)),
                new DataPoint(10, purchaseCount1.get(9)),
                new DataPoint(11,purchaseCount1.get(10)),
                new DataPoint(12, purchaseCount1.get(11))

        });
        series2.setColor(getResources().getColor(R.color.yellow));
        series2.setSpacing(30);
        series2.setAnimated(true);
        graph.addSeries(series2);

        BarGraphSeries<DataPoint> series3 = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0),
                new DataPoint(1, expCount1.get(0)),
                new DataPoint(2,expCount1.get(1)),
                new DataPoint(3, expCount1.get(2)),
                new DataPoint(4, expCount1.get(3)),
                new DataPoint(5, expCount1.get(4)),
                new DataPoint(6, expCount1.get(5)),
                new DataPoint(7, expCount1.get(6)),
                new DataPoint(8, expCount1.get(7)),
                new DataPoint(9, expCount1.get(8)),
                new DataPoint(10, expCount1.get(9)),
                new DataPoint(11,expCount1.get(10)),
                new DataPoint(12, expCount1.get(11))

        });
        series3.setColor(getResources().getColor(R.color.deepOrange));
        series3.setSpacing(30);
        series3.setAnimated(true);
        graph.addSeries(series3);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(Collections.max(totalValue));
        graph.getViewport().setMaxX(12);
        // graph.getViewport().setMinX(-2);
//        graph.getViewport().setMaxX(6);
//        graph.getViewport().setMaxY(100);
//        graph.getViewport().setMaxX(7);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"JAN", "FEB", "MAR", "APR", "May", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"});
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
        // series.setDrawValuesOnTop(true);
        //  series.setValuesOnTopColor(Color.RED);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

//        monthsale.add(calculateSum(saleCount1));
//

        Log.d(TAG, "mutliChart_Graph: month Exp"+expCount1.toString());
        Log.d(TAG, "mutliChart_Graph: month purchase"+purchaseCount1.toString());
        Log.d(TAG, "mutliChart_Graph: month sale"+saleCount1.toString());
        calculateMonth();



    }

    private void calculateMonth()
    {

        loading.dismiss();

        double maxSale = Collections.max(saleCount1);
        double maxPurchase =Collections.max(purchaseCount1);
        double maxexpCount =Collections.max(expCount1);

        Log.d(TAG, "calculateValue: max Sale" + maxSale);
        Log.d(TAG, "calculateValue: max exp" + maxexpCount);
        Log.d(TAG, "calculateValue: max purchase" + maxPurchase);

        int expIndex = expCount1.indexOf(maxexpCount);
        int maxindex = purchaseCount1.indexOf(maxPurchase);
        int saleindex= saleCount1.indexOf(maxSale);
       String Day=findmonth(saleindex);

        Log.d(TAG, "calculateValue: index Sale" + saleindex);
        Log.d(TAG, "calculateValue: index exp" + expIndex);
        Log.d(TAG, "calculateValue: index  purchase" + maxindex);

        txt_purchaseDay.setText(String.valueOf(findmonth(maxindex)));
        txt_expDay.setText(String.valueOf(findmonth(expIndex)));
        txt_saleDay.setText(String.valueOf(findmonth(saleindex)));

        txt_purchaseAmount.setText(String.valueOf(Math.round(maxPurchase*100)/100.00));;
        txt_saleamount.setText(String.valueOf(Math.round(maxSale*100)/100.00));;
        txt_expAmount.setText(String.valueOf(Math.round(maxexpCount*100)/100.00));;

          Log.d(TAG, "calculateValue: Day"+Day);
  }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void getTotalSale_Status(final String from_date, final String to_date)
    {
        Log.d(TAG, "getTotalSale_Status:");
        loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        final StringRequest req = new StringRequest(Request.Method.POST, urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equalsIgnoreCase("Data available")) {
                                Gson converter = new Gson();
                                Type type = new TypeToken<List<Double>>() {}.getType();
                                ArrayList<Double> saleCount = converter.fromJson(jsonObject.getString("saleCount"), type); //convert List to Array in Java String [] strArray = list.toArray(new String[0]);
                                ArrayList<Double> purchaseCount = converter.fromJson(jsonObject.getString("purchaseCount"), type);
                                ArrayList<Double> expCount = converter.fromJson(jsonObject.getString("expCount"), type);//monthSale1

                                  saleCount1 .add(Double.parseDouble(jsonObject.getString("monthSale1")));
                                  saleCount1 .add(Double.parseDouble(jsonObject.getString("monthSale2")));
                                 saleCount1.add(Double.parseDouble(jsonObject.getString("monthSale3")));
                                 saleCount1 .add(Double.parseDouble(jsonObject.getString("monthSale4")));
                                saleCount1.add(Double.parseDouble(jsonObject.getString("monthSale5")));
                                saleCount1.add(Double.parseDouble(jsonObject.getString("monthSale6")));
                                saleCount1.add(Double.parseDouble(jsonObject.getString("monthSale7")));
                                saleCount1.add(Double.parseDouble(jsonObject.getString("monthSale8")));
                                saleCount1.add(Double.parseDouble(jsonObject.getString("monthSale9")));
                                saleCount1.add(Double.parseDouble(jsonObject.getString("monthSale10")));
                                saleCount1.add(Double.parseDouble(jsonObject.getString("monthSale11")));
                                 saleCount1.add(Double.parseDouble(jsonObject.getString("monthSale12")));

                                try
                                {
                                    purchaseCount1.add(Double.parseDouble(jsonObject.getString("monthPur1")));
                                } catch (NumberFormatException e)
                                {
                                    purchaseCount1.add(00.0);
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                purchaseCount1.add(Double.parseDouble(jsonObject.getString("monthPur2")));
                                purchaseCount1.add(Double.parseDouble(jsonObject.getString("monthPur3")));
                                purchaseCount1.add(Double.parseDouble(jsonObject.getString("monthPur4")));
                                purchaseCount1.add(Double.parseDouble(jsonObject.getString("monthPur5")));
                                purchaseCount1.add(Double.parseDouble(jsonObject.getString("monthPur6")));
                                purchaseCount1.add(Double.parseDouble(jsonObject.getString("monthPur7")));
                                purchaseCount1.add(Double.parseDouble(jsonObject.getString("monthPur8")));
                                purchaseCount1.add(Double.parseDouble(jsonObject.getString("monthPur9")));
                                purchaseCount1.add(Double.parseDouble(jsonObject.getString("monthPur10")));
                                purchaseCount1.add(Double.parseDouble(jsonObject.getString("monthPur11")));
                                purchaseCount1.add(Double.parseDouble(jsonObject.getString("monthPur12")));


                                 expCount1.add(Double.parseDouble(jsonObject.getString("monthExp1")));
                                expCount1.add(Double.parseDouble(jsonObject.getString("monthExp2")));
                                expCount1.add(Double.parseDouble(jsonObject.getString("monthExp3")));
                                expCount1.add(Double.parseDouble(jsonObject.getString("monthExp4")));
                                expCount1.add(Double.parseDouble(jsonObject.getString("monthExp5")));
                                expCount1.add(Double.parseDouble(jsonObject.getString("monthExp6")));
                                expCount1.add(Double.parseDouble(jsonObject.getString("monthExp7")));
                                expCount1.add(Double.parseDouble(jsonObject.getString("monthExp8")));
                                expCount1.add(Double.parseDouble(jsonObject.getString("monthExp9")));
                                expCount1.add(Double.parseDouble(jsonObject.getString("monthExp10")));
                                expCount1.add(Double.parseDouble(jsonObject.getString("monthExp11")));
                                expCount1.add(Double.parseDouble(jsonObject.getString("monthExp12")));
                                calculateValue(saleCount, purchaseCount, expCount);
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

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", globalPool.getDbname());
                params.put("from_date", from_date);
                params.put("to_date", to_date);
                Log.d(TAG, "getParams:" + params.toString());
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

            for (double sale : saleCount) {
                mtotal_sale += sale;
                mtotal_expn += Double.parseDouble(String.valueOf(expCount.get(i)));
                mtotal_purchase += Double.parseDouble(String.valueOf(purchaseCount.get(i)));
                i++;


            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {

            totalValue.add(mtotal_expn);
            totalValue.add(mtotal_sale);
            totalValue.add(mtotal_purchase);
            Log.d(TAG, "calculateValue:  i" + i);
            txt_purchase.setText(String.valueOf( Math.round(mtotal_purchase * 100.0) / 100.0));
            txt_sale.setText(String.valueOf(Math.round(mtotal_sale * 100.0) / 100.0));
            txt_exp.setText(String.valueOf(Math.round(mtotal_expn * 100.0) / 100.0));
        }

//        Object maxSale = Collections.max(saleCount);
//        Object maxPurchase = Collections.max(purchaseCount);
//        Object maxexpCount = Collections.max(expCount);
//        Log.d(TAG, "calculateValue: max Sale" + maxSale);
//        Log.d(TAG, "calculateValue: sale index" + saleCount.indexOf(maxSale));
//        Log.d(TAG, "calculateValue: purchase index" + purchaseCount.indexOf(maxPurchase));
//        Log.d(TAG, "calculateValue: exp index" + expCount.indexOf(maxexpCount));
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
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally
        {
            Log.d(TAG, "calculateSum:  i" + i);

        }
        return sum;

    }


//    public static void main(String args[]){
//
//        int[] array = {10, 20, 30, 40, 50, 10};
//    } int sum = 0; //Advanced for loop for( int num : array) { sum = sum+num; } System.out.println("Sum of array elements is:"+sum); }


    private String findmonth(int salindex) {
        switch (salindex) {
            case 0:
                return " 1";
            case 1:
                return "2";

            case 2:
                return " 3";

            case 3:
                return "4";

            case 4:
                return " 5";

            case 5:
                return "6";

            case 6:
                return "7";

            case 7:
                return " 7";
            case 8:
                return " 8";

            case 9:
                return " 9";

            case 10:
                return " 10";

            case 11:
                return " 11";

            case 12:
                return "12";
            default:
                Toast.makeText(getApplicationContext(), "Wrong Day", Toast.LENGTH_LONG).show();
        }
        return "Day";

    }




}

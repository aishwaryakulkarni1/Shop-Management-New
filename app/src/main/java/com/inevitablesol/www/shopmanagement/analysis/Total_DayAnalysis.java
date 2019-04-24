package com.inevitablesol.www.shopmanagement.analysis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Total_DayAnalysis extends AppCompatActivity {

    private GraphView graphView;

    private static final String TAG = "Total_DayAnalysis";
    private String urlJsonArry="http://35.161.99.113:9000/webapi/report/singleDate";
     GlobalPool globalPool;
    private String fromDate;

    private TextView txt_date;

    private TextView txt_saleAmount,txt_purchaseAmount,txt_expAmount;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total__day_analysis);
        graphView = (GraphView) findViewById(R.id.graph);
        globalPool= (GlobalPool) this.getApplicationContext();
        txt_date=(TextView)findViewById(R.id.txt_date);
        txt_saleAmount=(TextView)findViewById(R.id.saleAmount);
        txt_expAmount=(TextView)findViewById(R.id.expamount);
        txt_purchaseAmount=(TextView)findViewById(R.id.purchaseAmount);

        Intent intent=getIntent();
         if(intent.hasExtra("Date"))
         {
               fromDate=intent.getStringExtra("Date");
             txt_date.setText(fromDate);
         }



        getTotalSale_Status();
    }



    private void barGraph(double sale, double purchase, double expcount)
    {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, sale),
                new DataPoint(2, purchase),
                new DataPoint(3, expcount)

        });
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"","SALE","PURCH","EXPN"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        series.setColor(getResources().getColor(R.color.green));
        graph.addSeries(series);

        ArrayList<Double> maxValue=new ArrayList<>();
        maxValue.add(sale);
        maxValue.add(purchase);
        maxValue.add(expcount);

  // styling
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(Collections.max(maxValue));
        graph.getViewport().setMaxX(5);

        series.setSpacing(30);

        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
      ///  graphView.getGridLabelRenderer().setVerticalAxisTitle("No of Customer");
        graphView.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.color_grey));
        graphView.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.colorPrimary));
        graphView.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.colorPrimary));
        graphView.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.color_grey));

// draw values on top

        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);

        loading.dismiss();


    }


     private void  getTotalSale_Status()
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
                                                  try {
                                                      double sale= Double.parseDouble(jsonObject.getString("saleCount"));
                                                      double purchase= Double.parseDouble(jsonObject.getString("purchaseCount"));
                                                      double expcount= Double.parseDouble(jsonObject.getString("expCount"));
                                                      txt_purchaseAmount.setText(String.valueOf(purchase));
                                                      txt_expAmount.setText(String.valueOf(expcount));
                                                      txt_saleAmount.setText(String.valueOf(sale));
                                                      barGraph(sale,purchase,expcount);
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
                       params.put("date",fromDate);
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

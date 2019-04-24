package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.Transaction_Adapter;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Transaction_History extends AppCompatActivity {
    private GlobalPool globalPool;
    private static final String TAG = "Transaction_History";
    private ProgressDialog progressDialog;

    private  ArrayList<Transrecord> List=new ArrayList<>();
    private RecyclerView recyclerView;
    ImageView datePicker;
    private TextView currentDate;
    private  TextView currentBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_transaction);
        globalPool = (GlobalPool) this.getApplicationContext();
        progressDialog=new ProgressDialog(this);
        Log.d(TAG, "onCreate: ");
        currentBalance=(TextView)findViewById(R.id.currentBalance);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        datePicker = (ImageView) findViewById(R.id.date_billingDate);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDate();
            }
        });
        currentDate = (TextView) findViewById(R.id.bill_curruntDate);


        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyy-MM-dd");
        currentDate.setText(dateFormat.format(dt));
        getData();
          getTotalAmount();
    }

    private void getTotalAmount()
    {

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dbname", globalPool.getDbname());


        Ion.with(this)
                .load("POST", "http://35.161.99.113:9000/webapi/invoice/transactionBalance")
                .progressHandler(new ProgressCallback()
                {

                    @Override
                    public void onProgress(long downloaded, long total)
                    {
                        //progressDialog.setMessage("Downloading "+downloaded+"/"+total);

                    }
                })
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        Log.d(TAG, "onCompleted: Amount "+result);
                                 JsonElement jsonElement= result.get("balance");
                        Log.d(TAG, "onCompleted: element"+jsonElement);

                    }
                });

    }

    private void showDate() {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth)
            {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date date1 = myCalendar.getTime();

                updateDate(myCalendar);
            }

        };

        new DatePickerDialog(Transaction_History.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));


    }

    private void updateDate(Calendar myCalendar)
    {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(myCalendar.getTime());
        currentDate.setText(currentDateTimeString);

        getData();
    }

    private void getData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dbname", globalPool.getDbname());
        jsonObject.addProperty("date", currentDate.getText().toString().trim());


        Ion.with(this)
                .load("http://35.161.99.113:9000/webapi/invoice/transactionList")
                .progressHandler(new ProgressCallback() {

                    @Override
                    public void onProgress(long downloaded, long total)
                    {
                        //progressDialog.setMessage("Downloading "+downloaded+"/"+total);

                    }
                })
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        Log.d(TAG, "onCompleted: "+result);
                        TranHistory tranHistory = new TranHistory();
                        Gson gson = new Gson();

                        tranHistory = gson.fromJson(result, tranHistory.getClass());
                        Log.d(TAG, "onCompleted: "+tranHistory.getMessage());
                        Transaction_Adapter transaction_adapter=null;

                        if(!tranHistory.getMessage().equalsIgnoreCase("Data not available"))
                           {

                               List = (ArrayList<Transrecord>) tranHistory.getTransrecords();

                               transaction_adapter = new Transaction_Adapter(List, Transaction_History.this);
                               //progressDialog.dismiss();
                               Log.d(TAG, "onCompleted: " + List.toString());
                               recyclerView.setHasFixedSize(true);

                               RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                               recyclerView.setLayoutManager(layoutManager);
                               recyclerView.setAdapter(transaction_adapter);
                             //  currentBalance.setText(String.valueOf(transaction_adapter.getCurrentBalance(List)));


                           }else
                           {
                               if(transaction_adapter!=null)
                               {
                                  // transaction_adapter = new Transaction_Adapter(List, Transaction_History.this);
                                   transaction_adapter.clearView();
                                   transaction_adapter.notifyDataSetChanged();
                               }

                               Toast.makeText(globalPool, "Data not Available", Toast.LENGTH_SHORT).show();
                           }

                        // do stuff with the result or error
                    }
                });


    }

}

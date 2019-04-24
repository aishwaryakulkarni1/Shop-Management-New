package com.inevitablesol.www.shopmanagement.expenses;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.product_info.adapter.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class View_expense extends AppCompatActivity implements View.OnClickListener {
   private TextView txt_to_date;
    private Spinner exp_type;
    private ImageView img_todate,img_fromdate;
    private SharedPreferences sharedpreferences;
    public  final String MyPREFERENCES = "MyPrefs" ;
    private String dbname;
    private String userId;
   private  TextView txt_FromDate;
    private static final String TAG = "View_expense";
    private ArrayList<ExpList> ex_list;
    private RecyclerView recyclerView;

    private    Gson_Exp_Adapter Adapter;

    private TextView txt_totalExp;
    private  TextView txt_banance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_expansive);
        exp_type=(Spinner)findViewById(R.id.sp_exp_type);
        txt_to_date=(TextView)findViewById(R.id.exp_toDate);
        txt_FromDate=(TextView)findViewById(R.id.exp_fromdate);
        recyclerView=(RecyclerView)findViewById(R.id.exl_list);
        img_todate=(ImageView)findViewById(R.id.exp_todate_image);
        img_fromdate=(ImageView)findViewById(R.id.exp_img_fromDate);
        img_fromdate.setOnClickListener(this);
        img_todate.setOnClickListener(this);
        txt_banance=(TextView)findViewById(R.id.total_balalnce);
        txt_totalExp=(TextView)findViewById(R.id.exp_view_totalExp);
         ImageView imageView= (ImageView) findViewById(R.id.exp_download);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));
        userId= sharedpreferences.getString("userId","");
        getAllExpensesType();
        currentDate();
        prevDate();

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {

            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent e)
                {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
            {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e))
                {
                    int position = rv.getChildAdapterPosition(child);
                    ExpList  vInfo= ex_list.get(position);
                    Log.d(TAG, "onInterceptTouchEvent: "+vInfo.toString());
                    Intent intent=new Intent(View_expense.this,SelectedExpensiveDetails.class);
                    intent.putExtra("catId",String.valueOf(vInfo.getCatId()));
                    intent.putExtra("expid",String.valueOf(vInfo.getId()));
                    intent.putExtra("edate",vInfo.getEdate());
                    intent.putExtra("expName",vInfo.getExpName());
                    intent.putExtra("subTotal",vInfo.getSubTotal());
                    intent.putExtra("totalGst",vInfo.getTotalGst());
                    intent.putExtra("totalQty",vInfo.getTotalQty());
                    intent.putExtra("otherCharges",vInfo.getOtherCharges());
                    intent.putExtra("totalAmt",vInfo.getTotalAmt());
                    intent.putExtra("amountPaid",vInfo.getAmountPaid());
                    intent.putExtra("balance",vInfo.getBalance());
                    intent.putExtra("paymentMode",vInfo.getPaymentMode());
                    intent.putExtra("refNo",vInfo.getRefNo());
                    intent.putExtra("description",vInfo.getDescription());
                    intent.putExtra("createdDate",vInfo.getCreatedDate());
                    intent.putExtra("createdBy",vInfo.getCreatedBy());
                    intent.putExtra("imageCode",vInfo.getImageCode());
                    intent.putExtra("dbname",dbname);

                    startActivity(intent);



                }
                return  false;

            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e)
            {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        exp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item was selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id)

            {

                String p_id = Exp_Adapter.productId[pos];

                 String toDate=txt_to_date.getText().toString().trim();
                 String  fromDate=txt_FromDate.getText().toString().trim();
                 getAllExpensesType(fromDate,toDate,p_id);
                  getTotal(fromDate,toDate,p_id);

            }

            public void onNothingSelected(AdapterView parent) {
                // Do nothing.
            }
        });

    }

    private void prevDate()
    {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.YEAR, -1); // to get previous year add -1
        Date nextYear = cal.getTime();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String prevDateTimeString = dateFormat.format(cal.getTime());
        txt_FromDate.setText(prevDateTimeString);
        String todate= txt_to_date.getText().toString().trim();
        Log.d(TAG, "prevDate: ");


    }

    private void currentDate()
    {
        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        // String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        String currentDateTimeString = dateFormat.format(dt);
        txt_to_date.setText(currentDateTimeString);
        txt_FromDate.setText(currentDateTimeString);
    }



    private void changeExpensesFromDate()
    {

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

                //updateDate(myCalendar);
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                txt_FromDate.setText(currentDateTimeString);

                String p_id = exp_type.getSelectedItem().toString();

                String toDate=  txt_to_date.getText().toString().trim();
                String  fromDate= txt_FromDate.getText().toString().trim();
                Log.d(TAG, "onDateSet: FromDate"+fromDate);
                Log.d(TAG, "onDateSet: Todate"+toDate);
                getAllExpensesType(fromDate,toDate,p_id);
                getTotal(fromDate,toDate,p_id);
            }

        };

        new DatePickerDialog(View_expense.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));

    }

    private void changeExpensesDate(final TextView txt_to_date)
    {

        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date date1 = myCalendar.getTime();

                //updateDate(myCalendar);
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                txt_to_date.setText(currentDateTimeString);

                String p_id = exp_type.getSelectedItem().toString();

                String toDate=  txt_to_date.getText().toString().trim();
                String  fromDate= txt_FromDate.getText().toString().trim();
                getAllExpensesType(fromDate,toDate,p_id);
                getTotal(fromDate,toDate,p_id);
            }

        };

        new DatePickerDialog(View_expense.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.exp_todate_image:
                changeExpensesDate(txt_to_date);
                break;
            case R.id.exp_img_fromDate:
                changeExpensesFromDate();
        }

    }

    private void getAllExpensesType()
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_NEW_PRODUCT_EXP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try
                {
                    Log.d("response",response);


                    Exp_Adapter exp_adapter=new Exp_Adapter(response);
                    exp_adapter.productParser();

                    ProductAdapter productAdapter = new ProductAdapter(View_expense.this, R.layout.product_list, Exp_Adapter.productName, Exp_Adapter.productId);
                    exp_type.setAdapter(productAdapter);


                } catch (Exception e)
                {
                    Log.d(TAG,"Error"+e);
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(View_expense.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                Log.d(TAG, "getParams:"+params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




    private void getAllExpensesType(final String prevDateTimeString, final String todate,final String catId)
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_EXP_BY_DATE, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                JSONObject jsonObject=null;
                JSONObject msg;

                loading.dismiss();
                try
                {
                    Log.d("response",response);
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if(message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplicationContext(),"Data not available",Toast.LENGTH_LONG).show();
                        if (Adapter != null)
                        {
                            Adapter.clearView();
                            Adapter.notifyDataSetChanged();
                        }

                    }else
                    {
                        TotalExpList totalExpList = new TotalExpList();
                        Gson gson = new Gson();
                        totalExpList = gson.fromJson(response, totalExpList.getClass());
                        ex_list = (ArrayList<ExpList>) totalExpList.getExpList();
                        Adapter = new Gson_Exp_Adapter(ex_list, View_expense.this);
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(Adapter);


                    }



                } catch (Exception e)
                {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(View_expense.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("from_date",prevDateTimeString);
                params.put("to_date",todate);
                params.put("expcat",catId);
                Log.d(TAG, "getParams: "+params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTotal(final String prevDateTimeString, final String todate, final String p_id)
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_TOTAL_AMOUNT, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try
                {
                    Log.d("response Total ",response);
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray  jsonArray=jsonObject.getJSONArray("records");
                    Log.d(TAG, "onResponse: record"+jsonArray);
                    txt_totalExp.setText(jsonArray.getJSONObject(0).getString("totalamt"));
                    txt_banance.setText(jsonArray.getJSONObject(0).getString("balance"));

                } catch (Exception e)
                {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(View_expense.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("from_date",prevDateTimeString);
                params.put("to_date",todate);
                params.put("expcat",p_id);
                Log.d(TAG, "getParams Total: "+params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}

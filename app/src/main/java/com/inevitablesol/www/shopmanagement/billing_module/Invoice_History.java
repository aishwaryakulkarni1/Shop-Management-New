package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Invoice_History extends AppCompatActivity
{
    private static final String TAG = "Invoice_History";
    private RecyclerView recyclerView;

    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    private SearchView searchView;
    private String dbname;
    private ArrayList<InvoiceRecord> invoiceRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_invoice__history);
         searchView=(SearchView)findViewById(R.id.SearchView_inv_cust);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
         recyclerView=(RecyclerView)findViewById(R.id.invoice_ViewRecycler);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));
        if(searchView!=null)
        {
            searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Log.d(TAG, "onQueryTextSubmit: "+query);
                if(!query.isEmpty() && query.length()>0)
                {
                    getInvoiceData(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if(!newText.isEmpty() && newText.length()>0)
                {
                    getInvoiceData(newText);
                }
                return true;
            }


        });

        searchView.setQueryHint("Search Invocie By Id");
      //  searchView.setIconified(true);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                searchView.setIconified(false);

            }
        });
        searchView.setIconifiedByDefault(false);
       // searchViewCustomer.setQueryHint("Search Customer Name");
        // searchViewCustomer.setIconified(true);

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
                    InvoiceRecord invocieInfo= invoiceRecords.get(position);
                    Log.d(TAG, "onInterceptTouchEvent:"+invocieInfo);
                    Intent intent=new Intent(Invoice_History.this, Invoice_Details.class);

                    //Intent intent=new Intent(Invoice_History.this,Selected_InvocieHistory.class);
                    intent.putExtra("in_id",String.valueOf(invocieInfo.getInvoiceId()));
                    intent.putExtra("c_name",invocieInfo.getCustomerName());
                    intent.putExtra("c_email",invocieInfo.getEmail_id());
                    intent.putExtra("mobile",invocieInfo.getMobile_number());
                    intent.putExtra("balanceDue",invocieInfo.getBalance_due());
                    intent.putExtra("invObject",invocieInfo);
                    intent.putExtra("c_id",String.valueOf(invocieInfo.getCustomerId()));
                     intent.putExtra("amount",invocieInfo.getAmount());
                     intent.putExtra("payment_id",String.valueOf(invocieInfo.getPaymentId()));
                     intent.putExtra("status",invocieInfo.getStatus());
                     intent.putExtra("description",invocieInfo.getDescription());
                     intent.putExtra("modeOfPayment",invocieInfo.getModeOfPayment());
                     intent.putExtra("dateTime",invocieInfo.getDateTime());
                     intent.putExtra("total_gst",invocieInfo.getTotal_gst());
                     intent.putExtra("taxableValue",invocieInfo.getTaxable_value());
                     intent.putExtra("total",invocieInfo.getTotal());
                     intent.putExtra("amountpaid",invocieInfo.getAmount_paid());
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
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
            {

            }
        });





    }


    private void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
        Log.d(TAG, "showInputMethod: ");
    }

    private void getInvoiceData(final String newText)
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(Invoice_History.this).add(new StringRequest(Request.Method.POST, WebApi.GETINVOICEBYNUMBER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject msg = null;
                try
                {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    } else
                    {
                        try
                        {
                            Log.d(TAG, "onInvocie:"+response);
                            Gson gson = new Gson();
                            InvocieHistoryList invocieHistoryList=new InvocieHistoryList();
                             invocieHistoryList=gson.fromJson(response,invocieHistoryList.getClass());
                             invoiceRecords=(ArrayList<InvoiceRecord>)invocieHistoryList.getInvoiceRecords();

                             InvoiceAdapter invoiceAdapter=new InvoiceAdapter(invoiceRecords,Invoice_History.this);
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(invoiceAdapter);

                            loading.dismiss();
                        } catch (NullPointerException e)
                        {

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(Invoice_History.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("invoice_id",newText);
                Log.d("getParams:InvocieByID",params.toString());
                return params;
            }


        });
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }




}

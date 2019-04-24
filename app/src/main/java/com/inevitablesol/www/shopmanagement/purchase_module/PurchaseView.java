package com.inevitablesol.www.shopmanagement.purchase_module;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.inevitablesol.www.shopmanagement.vendor_module.Record;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class PurchaseView extends AppCompatActivity 
{


    private static final String TAG = "PurchaseView";
    private Context context=PurchaseView.this;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String dbname;
    private ArrayList<PurchaseViewRecord> purchaseViewRecords;

        private RecyclerView recyclerView;
        private SearchView searchViewCustomer;
    private ArrayList<Record> vendorLists;
    private ArrayList<PurchaseViewRecord> purchaseViewArray;
    private  PurchaseViewAdapter  vendorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_view);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        recyclerView=(RecyclerView)findViewById(R.id.purchase_ViewRecycle);
        searchViewCustomer=(SearchView)findViewById(R.id.vendorSearchView);
       // searchViewCustomer.setIconifiedByDefault(false);
       // searchViewCustomer.setQueryHint("Search Vendor");
        searchViewCustomer.setQueryHint("Search  Name /Invoice No");
      
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));
        Log.d(TAG, "onCreate:");

        searchViewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                searchViewCustomer.setIconified(false);

            }
        });


        searchViewCustomer.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Log.d(TAG, "onQueryTextSubmit: "+query);
                if(!query.isEmpty() && query.length()>0)
                {
                    getData(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if(!newText.isEmpty() && newText.length()>0)
                {
                    getData(newText);
                }
                else
                {
                    getVendorRecords();
                }
                return true;
            }

        });
        View closeButton =  searchViewCustomer.findViewById(android.support.v7.appcompat.R.id.search_close_btn);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                EditText et = (EditText) findViewById(R.id.search_src_text);

                //Clear the text from EditText view
                et.setText("");
                //Clear query
                searchViewCustomer.setQuery("", false);
                getVendorRecords();
                hideKeyboard();

            }
        });

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
                    PurchaseViewRecord vInfo= purchaseViewArray.get(position);
                    Intent intent=new Intent(PurchaseView.this,PurchaseView_listItem.class);
                    Log.d(TAG, "onInterceptTouchEvent:"+vInfo.toString());
                    intent.putExtra("v_id",String.valueOf(vInfo.getVendorId()));
                    intent.putExtra("invoice_num",vInfo.getInvoiceNo());
                    intent.putExtra("owner",vInfo.getCompany());;
                    intent.putExtra("purchase_id",String.valueOf(vInfo.getPurchaseId()));
                    intent.putExtra("vendor_id",String.valueOf(vInfo.getVendorId()));
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

        getVendorRecords();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void getVendorRecords()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(PurchaseView.this).add(new StringRequest(Request.Method.POST, WebApi.GETALLVENDOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject msg = null;
                try
                {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Data not available")) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    } else
                    {
                        try
                        {

                            Gson gson = new Gson();

                            PurchaseRecords vendorInvocieList=new PurchaseRecords();
                                        vendorInvocieList=gson.fromJson(response,vendorInvocieList.getClass());
                            purchaseViewArray=(ArrayList<PurchaseViewRecord>)vendorInvocieList.getPurchaseViewRecords();

                            PurchaseViewAdapter  vendorAdapter = new PurchaseViewAdapter(purchaseViewArray, PurchaseView.this);
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(vendorAdapter);
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
                    Toast.makeText(PurchaseView.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);

                return params;
            }


        });
    }

    private void getData(final String query)
    {
        Log.d(TAG, "getData:"+query);

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);


        Volley.newRequestQueue(PurchaseView.this).add(new StringRequest(Request.Method.POST, WebApi.GETVENDORBYOWNER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    Log.d(TAG, "onResponse:"+message);

                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        loading.dismiss();
                        if(vendorAdapter!=null)
                        {
                            vendorAdapter.clearView();
                            vendorAdapter.notifyDataSetChanged();
                        }
                    } else
                    {
                        try
                        {

                            Gson gson = new Gson();

                            PurchaseRecords vendorInvocieList=new PurchaseRecords();
                            vendorInvocieList=gson.fromJson(response,vendorInvocieList.getClass());
                            purchaseViewArray=(ArrayList<PurchaseViewRecord>)vendorInvocieList.getPurchaseViewRecords();
                                      if(vendorAdapter !=null)
                                      {
                                          vendorAdapter.clearView();
                                          vendorAdapter.notifyDataSetChanged();
                                      }
                           vendorAdapter  = new PurchaseViewAdapter(purchaseViewArray, PurchaseView.this);
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(vendorAdapter);
                            loading.dismiss();


                        } catch (NullPointerException e)
                        {

                        }

                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(PurchaseView.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                if (query.matches("[A-Za-z]+"))
                {
                    params.put("company", query);
                }
                else
                {
                    params.put("invoice_no", query);
                }

                Log.d(TAG, "getParams:"+params.toString());

                return params;

            }


        });


    }


    @Override
    protected void onRestart()
    {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
                
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }



    private void hideKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

 
}

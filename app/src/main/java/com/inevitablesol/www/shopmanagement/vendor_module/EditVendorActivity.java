package com.inevitablesol.www.shopmanagement.vendor_module;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.Toast;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditVendorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static final String TAG = "View_Vendor";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String dbname;
    private ArrayList<Record> vendorLists=null;
    private SearchView searchCustomer;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__vendor);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));
        searchCustomer=(SearchView) findViewById(R.id.input_searchVendor);
        //searchCustomer=(SearchView) findViewById(R.id.input_searchVendor);
       // searchCustomer.setIconifiedByDefault(false);
     //   searchCustomer.setQueryHint("Search Vendor Name");

        searchCustomer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        getVendorDetails();

        View closeButton = searchCustomer.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
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
                    Record  vInfo= vendorLists.get(position);
                    Intent intent=new Intent(EditVendorActivity.this,UpdateVendorDetail.class);
                    intent.putExtra("v_id",vInfo.getVendorId());
                    intent.putExtra("shop_name",vInfo.getCompany());
                    intent.putExtra("v_name",vInfo.getOwnerName());
                    intent.putExtra("v_mobile",vInfo.getMobileNo());
                    intent.putExtra("v_address",vInfo.getAddress());
                    intent.putExtra("v_email",vInfo.getEmailId());
                    intent.putExtra("gststatus",vInfo.getGstDetails());
                    intent.putExtra("gst_number",vInfo.getGstinNo());
                    intent.putExtra("hsn_ssc_code",vInfo.getCreatedBy());
                    intent.putExtra("created_date",vInfo.getCreatedDate());
                    intent.putExtra("contact_person",vInfo.getContactPerson());
                    intent.putExtra("stateCode",vInfo.getStateCode());
                    intent.putExtra("dbname",dbname);
                    Log.d(TAG, "onInterceptTouchEvent:Update"+vInfo.toString());

                    startActivityForResult(intent,1);



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


        searchCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                searchCustomer.setIconified(false);

            }
        });
        searchCustomer.setQueryHint("Search Vendor Name ");
//      View closeButton = searchCustomer.findViewById(android.support.v7.appcompat.R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                EditText et = (EditText) findViewById(R.id.search_src_text);

                //Clear the text from EditText view
                et.setText("");
                //Clear query
                searchCustomer.setQuery("", false);
                //Collapse the action view
//                searchViewCustomer.onActionViewCollapsed();
                getVendorDetails();
                hideKeyboard();

            }
        });

        searchCustomer.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                    if (newText.matches(".*\\d+.*"))
                    {
                        getData(newText);
                        //params.put("mobile_no", s);
                    }
                    else
                    {
                        getData_UsingCompany(newText);
                        //params.put("company", s);
                    }

                }else
                {
                    Log.d(TAG, "onQueryTextChange: ");
                    getVendorDetails();

                }

                return true;
            }

        });




    }
    private void getData_UsingCompany(final String company)
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebApi.GET_VENDORBYCOMPANY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.d("response",response);
                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Search not found"))
                    {
                        Toast.makeText(getApplication(),message,Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    }else
                    {
                        try
                        {
                            VendorList vendorList=new VendorList();
                            Gson gson=new Gson();
                            vendorList= gson.fromJson(response,vendorList.getClass());
                            vendorLists=(ArrayList<Record>)vendorList.getRecords();
                            VendorAdapter vendorAdapter=new VendorAdapter(vendorLists,EditVendorActivity.this);
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(vendorAdapter);

                            Log.d(TAG, "onResponse: "+response);
                            loading.dismiss();
                        }catch (NullPointerException e)
                        {

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(EditVendorActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);


                params.put("company", company);

                Log.d(TAG, "getParams:View Vendor BY COMPANY"+params.toString());
                return  params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditVendorActivity.this);
        requestQueue.add(stringRequest);

    }



    private void getVendorDetails()
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebApi.GETVENDDOR_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.d("response",response);
                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Search not found"))
                    {
                        Toast.makeText(getApplication(),message,Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    }else
                    {
                        try
                        {

                            VendorList vendorList=new VendorList();
                            Gson gson=new Gson();
                            vendorList= gson.fromJson(response,vendorList.getClass());
                            vendorLists=(ArrayList<Record>)vendorList.getRecords();
                            VendorAdapter vendorAdapter=new VendorAdapter(vendorLists,EditVendorActivity.this);
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(vendorAdapter);

                            Log.d(TAG, "onResponse: "+response);
                            loading.dismiss();
                        }catch (NullPointerException e)
                        {

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(EditVendorActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                return  params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditVendorActivity.this);
        requestQueue.add(stringRequest);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2

        if (resultCode == Activity.RESULT_OK)
        {
            String result = data.getStringExtra("Updated");
            Log.d(TAG, "onActivityResult: "+result);
            if (result.equalsIgnoreCase("succesfully updated"))
            {
                getVendorDetails();
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        }

    }


    private void getData(final String s)
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebApi.GETCOMPANYBYID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.d("response",response);
                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Search not found"))
                    {
                        Toast.makeText(getApplication(),message,Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    }else
                    {
                        try
                        {
                            VendorList vendorList=new VendorList();
                            Gson gson=new Gson();
                            vendorList= gson.fromJson(response,vendorList.getClass());
                            vendorLists=(ArrayList<Record>)vendorList.getRecords();
                            VendorAdapter vendorAdapter=new VendorAdapter(vendorLists,EditVendorActivity.this);
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(vendorAdapter);

                            Log.d(TAG, "onResponse: "+response);
                            loading.dismiss();
                        }catch (NullPointerException e)
                        {

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(EditVendorActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("mobile_no",s);
                return  params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditVendorActivity.this);
        requestQueue.add(stringRequest);

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

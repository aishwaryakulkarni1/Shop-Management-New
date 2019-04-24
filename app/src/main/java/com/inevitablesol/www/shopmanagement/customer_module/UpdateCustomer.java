package com.inevitablesol.www.shopmanagement.customer_module;

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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateCustomer extends AppCompatActivity {

    private EditText searchCust;
    private RecyclerView recyclerView;
    ArrayList<CustomerInfo> customerInfos;

    private CustomerAdapter customerAdapter;
    private String dbname;


    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private static final String TAG = "ViewCustomer";
    private SearchView searchViewCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_view_customerfor_update);
        recyclerView = (RecyclerView)findViewById(R.id.cust_recyclerView);

        searchViewCustomer=(SearchView)findViewById(R.id.simpleSearchView);
       // searchViewCustomer.setIconifiedByDefault(true);
        searchViewCustomer.setQueryHint("Search Customer Name");
     //  searchViewCustomer.setIconified(true);
        searchViewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                searchViewCustomer.setIconified(false);

            }
        });
        View closeButton = searchViewCustomer.findViewById(android.support.v7.appcompat.R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                EditText et = (EditText) findViewById(R.id.search_src_text);

                //Clear the text from EditText view
               // et.setText("");


                //Clear query
                searchViewCustomer.setQuery("", false);
                getAllCustomerDetails();
              //  hideKeyboard();

            }
        });
        searchViewCustomer.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Log.d(TAG, "onQueryTextSubmit: "+query);
                if(!query.isEmpty() && query.length()>0)
                {
                    getCustDetail(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if(!newText.isEmpty() && newText.length()>0)
                {
                    getCustDetail(newText);
                }
                else
                {
                    getAllCustomerDetails();
                }
                return true;
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
                    CustomerInfo customerInfo= customerInfos.get(position);
                    Log.d(TAG, "onInterceptTouchEvent: Customer"+customerInfo.toString());
                    Intent intent=new Intent(UpdateCustomer.this,UpdateCustomerDetail.class);
                    intent.putExtra("c_id",customerInfo.getCustomer_id());
                    intent.putExtra("c_name",customerInfo.getCustomer_name());
                    intent.putExtra("c_mobile",customerInfo.getMobile_numbe());
                    intent.putExtra("c_address",customerInfo.getAddress());
                    intent.putExtra("c_email",customerInfo.getEmail_id());
                    intent.putExtra("gstin",customerInfo.getGstin());
                    intent.putExtra("placeofsupply",customerInfo.getPlaceofsupply());
                    intent.putExtra("state",customerInfo.getState());
                    intent.putExtra("dbname",dbname);
                    intent.putExtra("delivery_status",customerInfo.getHome_delivery());
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


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    private void getCustDetail(final String custName)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETCUSTBYNAME, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();

                Log.d("res",resp);
                JSONObject jsonObject= null;
                try
                {
                    jsonObject = new JSONObject(resp);
                    String message =      jsonObject.getString("message");


                    if(message.equalsIgnoreCase("Search found"))
                    {
                        try
                        {

                            CustomerParser customerParser=new CustomerParser(resp);
                            customerParser.custDetails();
                            customerInfos= customerParser.makeArray();
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            customerAdapter=new CustomerAdapter(customerInfos,UpdateCustomer.this);
                            recyclerView.setAdapter(customerAdapter);

                        }catch (Exception e)
                        {
                            Log.d(TAG, "onResponse: "+e);
                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        if(customerInfos!=null)
                        {
                            customerInfos.clear();
                            customerAdapter.notifyDataSetChanged();
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            customerAdapter = new CustomerAdapter(customerInfos, UpdateCustomer.this);
                            recyclerView.setAdapter(customerAdapter);
                        }
                    }

                } catch (JSONException e)
                {
                    Log.d("Exception",""+e);
                    e.printStackTrace();
                }









            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("customer_name",custName);

                return   params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2

        if (resultCode == Activity.RESULT_OK)
        {
            String result = data.getStringExtra("DELETED");
            Log.d(TAG, "onActivityResult: "+result);
            if (result.equalsIgnoreCase("Delete data succesfully."))
            {

                startActivity(getIntent());

            }
        }
        if (resultCode == Activity.RESULT_CANCELED)
        {
            //Write your code if there's no result
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllCustomerDetails();

    }


    private void getAllCustomerDetails()
    {



        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETCUSTINFO, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();
                loading.dismiss();
                Log.d("res",resp);
                JSONObject jsonObject= null;
                try
                {
                    jsonObject = new JSONObject(resp);
                    String message =      jsonObject.getString("message");


                    if(message.equalsIgnoreCase("Data available"))
                    {
                        try
                        {

                            CustomerParser customerParser=new CustomerParser(resp);
                            customerParser.custDetails();
                            customerInfos= customerParser.makeArray();
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            customerAdapter=new CustomerAdapter(customerInfos,UpdateCustomer.this);
                            recyclerView.setAdapter(customerAdapter);
                        }catch (Exception e)
                        {
                            Log.d(TAG, "onResponse: "+e);
                        }

                    }
                    else
                    {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        if(customerInfos!=null)
                        {
                            customerInfos.clear();
                            customerAdapter.notifyDataSetChanged();
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            customerAdapter = new CustomerAdapter(customerInfos, UpdateCustomer.this);
                            recyclerView.setAdapter(customerAdapter);
                        }
                    }

                } catch (JSONException e)
                {
                    Log.d("Exception",""+e);
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}


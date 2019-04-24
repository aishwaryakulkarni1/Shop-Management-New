package com.inevitablesol.www.shopmanagement.ItemModule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.inevitablesol.www.shopmanagement.ItemModule.Adapter.StockAdapter;
import com.inevitablesol.www.shopmanagement.ItemModule.Parser.ParseStock;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.billing_module.BillingProductAdapter;
import com.inevitablesol.www.shopmanagement.billing_module.BillingProductParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Add_Item extends AppCompatActivity {


    RecyclerView recyclerView;

    Spinner mProductSpinner;

    ActionBar actionBar;
    TextView tv_noData;
    SharedPreferences sharedpreferences;
    public final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private EditText mName;
    private static final String GET_PRODUCT = " http://35.161.99.113:9000/webapi/product/list";
    private static final String GET_All = "http://35.161.99.113:9000/webapi/product/all";
    private static final String TAG = "Add_Item";
    private String mProductName;
    private String mProductType;
    private ArrayList<StockInfo> stockInfoArrayList =new ArrayList<StockInfo>();
    private StockAdapter stockAdapter;
    private static final String GET_STOCK = "http://35.161.99.113:9000/webapi/product/p_info";

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__item);
        recyclerView = (RecyclerView) findViewById(R.id.stock_recyclerView);
        mProductSpinner = (Spinner) findViewById(R.id.spnn_product);
        mName = (EditText) findViewById(R.id.input_productname);
        tv_noData = (TextView) findViewById(R.id.tv_noData);

        actionBar = getSupportActionBar();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        stockAdapter=new StockAdapter(stockInfoArrayList,this);

        getAppProductDetails();

        mName.addTextChangedListener(new TextWatcher()
        {

            public void afterTextChanged(Editable s)
            {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, final int start, int before, int count) {
                try
                {
                    query = mName.getText().toString().toLowerCase();
                    Log.d(TAG, "onTextChanged:" + query);
                    Log.d(TAG, "onTextChanged:Length" + query.length());
                    if (query.toString().isEmpty() || query.toString().equalsIgnoreCase(""))
                    {
                        String mProductName = mProductSpinner.getSelectedItem().toString();
                        Log.d(TAG, "onTextChanged:Product" + mProductName);
                        if (mProductName.equalsIgnoreCase("1"))
                        {
                            get_all(mProductName);
                        } else {
                            get_allstock(mProductName);
                        }


                    }else {
                        Log.d(TAG, "onTextChanged:" + query);
                        stockAdapter.getFilter().filter(query);


                    }

                } catch (NumberFormatException e)
                {

                } catch (ArrayIndexOutOfBoundsException e)
                {

                }
            }


        });


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {

                    Log.d(TAG, "onInterceptTouchEvent:Length" + stockInfoArrayList.size());
                    int position = rv.getChildAdapterPosition(child);
                    Log.d(TAG, "onInterceptTouchEvent:" + position);

                    StockInfo stockInfo = stockInfoArrayList.get(position);
                    Log.d(TAG, "onInterceptTouchEvent:AddIItem" + stockInfo.toString());


                    Intent intent = new Intent(Add_Item.this, SelectedItemFromStock.class);
                    //  Toast.makeText(Add_Item.this, "Product ID: " + stockInfo.getItem_id(), Toast.LENGTH_SHORT).show();
                    intent.putExtra("itemId", String.valueOf(stockInfo.getItem_id()));
                    intent.putExtra("item_name", stockInfo.getItem_name());
                    intent.putExtra("company", stockInfo.getCompany());
                    intent.putExtra("owner", stockInfo.getOwner());
                    intent.putExtra("product_type", stockInfo.getProduct_type());
                    intent.putExtra("product_id", stockInfo.getProduct_id());
                    intent.putExtra("storageqty", stockInfo.getStorage_qty());
                    intent.putExtra("stockQty", stockInfo.getStock_qty());
                    intent.putExtra("original_price", stockInfo.getOriginal_price());
                    intent.putExtra("specification", stockInfo.getSpecification());
                    intent.putExtra("mrp", stockInfo.getMrp());
                    intent.putExtra("totalPrice", stockInfo.getTotalPrice());
                    intent.putExtra("unitPrice", stockInfo.getUnitPrice());
                    intent.putExtra("discount", stockInfo.getDiscount());
                    intent.putExtra("gst", stockInfo.getGst());
                    intent.putExtra("hsn_ssc_code", stockInfo.getHsn_ssc_code());
                    intent.putExtra("p_price",stockInfo.getO_price());
                    intent.putExtra("p_gst",stockInfo.getO_gst());
                    intent.putExtra("p_mrp",stockInfo.getO_mrp());
                    intent.putExtra("stockinfo",stockInfo);
                    startActivity(intent);


                }
                return false;

            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


    }


    private void getAppProductDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.d(TAG, "onResponse() called with: response = [" + response + "]");


                    String resp = response.toString().trim();
                    JSONObject jsonObject=new JSONObject(resp);
                    JSONArray jsonArray=jsonObject.getJSONArray("records");
                    int len=jsonArray.length();
                    Log.d(TAG, "onResponse: Length of Array"+len);

                    BillingProductParser product = new BillingProductParser(resp);
                    product.billingproductParser();
                    BillingProductAdapter productAdapter = new BillingProductAdapter(Add_Item.this, R.layout.product_list, BillingProductParser.productName, BillingProductParser.productId);
                    mProductSpinner.setAdapter(productAdapter);
                    mProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                            mProductName = BillingProductParser.productId[position];
                            mProductType = BillingProductParser.productName[position];
                            String itemName = mName.getText().toString().trim();
                            Log.d("name", mProductName);

                            if (mProductType.equalsIgnoreCase("All"))
                            {
                                get_all(mProductName);
                            } else {
                                get_allstock(mProductName);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (Exception e) {
                    Log.d("Exception", "" + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(Add_Item.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void get_all(final String mProductName)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_All, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.toString().trim();
                Log.d("Stock Resp", resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    JSONArray jsonArray = jsonObject.getJSONArray("records");
                    Log.d(TAG, "onResponse:Size" + jsonArray.length());
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        stockAdapter.clearView();
                        stockAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.GONE);
                        tv_noData.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        return;
                    } else
                    {
                        ParseStock parseStock = new ParseStock(resp);
                        parseStock.parseJSON();

                        recyclerView.setVisibility(View.VISIBLE);
                        tv_noData.setVisibility(View.GONE);
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);

                        stockInfoArrayList = parseStock.prepareStock();

                        if (stockAdapter == null)
                        {
                            stockAdapter = new StockAdapter(stockInfoArrayList, Add_Item.this);
                            recyclerView.setAdapter(stockAdapter);


                        } else
                        {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            stockAdapter = new StockAdapter(stockInfoArrayList, Add_Item.this);
                            recyclerView.setAdapter(stockAdapter);


                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(Add_Item.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("dbname", dbname);
                params.put("product_id", mProductName);
                params.put("status", "active");
                Log.d(TAG, "getParams:" + params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    public void get_allstock(final String mProductName)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_STOCK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.toString().trim();
                Log.d("Stock Resp", resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        try
                        {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.GONE);
                            tv_noData.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            return;
                        }catch (NullPointerException e)
                        {

                        }
                    } else
                    {
                        ParseStock parseStock = new ParseStock(resp);
                        parseStock.parseJSON();
                        recyclerView.setVisibility(View.VISIBLE);
                        tv_noData.setVisibility(View.GONE);
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        stockInfoArrayList = parseStock.prepareStock();

                        if (stockAdapter == null)
                        {
                            stockAdapter = new StockAdapter(stockInfoArrayList, Add_Item.this);
                            recyclerView.setAdapter(stockAdapter);
                        } else
                        {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            stockAdapter = new StockAdapter(stockInfoArrayList, Add_Item.this);
                            recyclerView.setAdapter(stockAdapter);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(Add_Item.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                params.put("product_id", mProductName);
                params.put("status", "active");
                Log.d(TAG, "getParams:Param" + params.toString());


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume:", "ViewItems");
        //getAppProductDetails();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
    }
}

package com.inevitablesol.www.shopmanagement.MenuItemModule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.inevitablesol.www.shopmanagement.ItemModule.Parser.ParseStock;
import com.inevitablesol.www.shopmanagement.ItemModule.StockInfo;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.billing_module.BillingProductParser;
import com.inevitablesol.www.shopmanagement.product_info.adapter.ProductAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuHistoryItem extends AppCompatActivity
{

    private static final String GET_PRODUCT = " http://35.161.99.113:9000/webapi/product/list";
    private static final String GET_All = "http://35.161.99.113:9000/webapi/product/all";
    private static final String GET_STOCK = "http://35.161.99.113:9000/webapi/product/p_info";
    private static final String REMOVE_ITEM = "http://35.161.99.113:9000/webapi/product/remove";

    Spinner mProductSpinner;
    private String mProductName, mProductType;
    private EditText mName;
    private  Menu_StockAdapter stockAdapter;
    ArrayList<StockInfo> stockInfoArrayList;
    RecyclerView recyclerView;
    String viewType;
    TextView tv_noData;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private static final String TAG = "ViewMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_history_item);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mName = (EditText) findViewById(R.id.input_productname);
        mProductSpinner = (Spinner) findViewById(R.id.spnn_product);
        tv_noData = (TextView) findViewById(R.id.tv_noData);
        recyclerView = (RecyclerView) findViewById(R.id.stock_recyclerView);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        Intent intent = getIntent();
        Log.d(TAG, "onCreate: ");

        getAppProductDetails();


        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(mProductSpinner);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width=dm.widthPixels;
            int height=dm.heightPixels;
            double wi=(double)width/(double)dm.xdpi;
            double hi=(double)height/(double)dm.ydpi;
            // Set popupWindow height to 500px
            popupWindow.setHeight( (height/2));
        }

        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e)
        {

        }


        mName.addTextChangedListener(new TextWatcher()
        {

            public void afterTextChanged(Editable s) {
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


                    }

                    final ArrayList<StockInfo> filteredList = new ArrayList<>();
                    if(stockInfoArrayList!=null)
                    {

                        for (int i = 0; i < stockInfoArrayList.size(); i++)
                        {

//                   final String text = stockInfoArrayList.get(i).toString().toLowerCase();
                            StockInfo stockInfo = stockInfoArrayList.get(i);
                            String text = stockInfo.getItem_name().toString().toLowerCase();
                            Log.d(TAG, "onTextChanged:  Stock Info" + stockInfo);
                            Log.d(TAG, "onTextChanged:  Query" + query);
                            if (text.startsWith(String.valueOf(query)))
                            {
                                filteredList.add(stockInfoArrayList.get(i));
                            }
                        }
                    }


                    recyclerView.setLayoutManager(new LinearLayoutManager(MenuHistoryItem.this));
                    stockAdapter = new Menu_StockAdapter(filteredList, MenuHistoryItem.this);
                    recyclerView.setAdapter(stockAdapter);
                    stockAdapter.notifyDataSetChanged();

                } catch (NumberFormatException e)
                {

                } catch (ArrayIndexOutOfBoundsException e) {

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


                    int position = rv.getChildAdapterPosition(child);
                    StockInfo stockInfo = stockInfoArrayList.get(position);
                    Log.d(TAG, "onInterceptTouchEvent:Menu"+stockInfo.toString());


                    Intent editIntent = new Intent(MenuHistoryItem.this, Menu_SelectedItem.class);
                    editIntent.putExtra("hsn",stockInfo.getHsn_ssc_code());
                    editIntent.putExtra("company",stockInfo.getCompany());
                    editIntent.putExtra("storage_qty",stockInfo.getStorage_qty());
                    editIntent.putExtra("item_price",stockInfo.getOriginal_price());
                    editIntent.putExtra("name", stockInfo.getItem_name());
                    editIntent.putExtra("product_id", String.valueOf(stockInfo.getProduct_id()));
                    editIntent.putExtra("item_id", String.valueOf(stockInfo.getItem_id()));
                    editIntent.putExtra("product_type", stockInfo.getProduct_type());
                    editIntent.putExtra("mrp", stockInfo.getMrp());
                    editIntent.putExtra("specification", stockInfo.getSpecification());
                    editIntent.putExtra("total_price",stockInfo.getTotalPrice());
                    editIntent.putExtra("discount",stockInfo.getDiscount());
                    editIntent.putExtra("unit_price",stockInfo.getUnitPrice());
                    editIntent.putExtra("gst",stockInfo.getGst());
                    editIntent.putExtra("disPer",stockInfo.getDisCountPer());
                    editIntent.putExtra("disRs",stockInfo.getDisPrice());

                    startActivityForResult(editIntent, 1);
//


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

    private void getAppProductDetails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    String resp = response.toString().trim();
                    BillingProductParser productParser = new BillingProductParser(resp);
                    productParser.billingproductParser();
                    ProductAdapter productAdapter = new ProductAdapter(MenuHistoryItem.this, R.layout.product_list, BillingProductParser.productName, BillingProductParser.productId);
                    mProductSpinner.setAdapter(productAdapter);
                    mProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                            mProductName =  BillingProductParser.productId[position];
                            mProductType =  BillingProductParser.productName[position];
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
                    Toast.makeText(MenuHistoryItem.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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




    public void get_all(final String mProductName) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_All, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.toString().trim();
                Log.d("Stock Resp", resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("Data not available")) {


                        try {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.GONE);
                            tv_noData.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            return;
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    } else
                    {
                        recyclerView.setVisibility(View.VISIBLE);
                        tv_noData.setVisibility(View.GONE);
                        ParseStock parseStock = new ParseStock(resp);
                        parseStock.parseJSON();


                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);

                        stockInfoArrayList = parseStock.prepareStock();

                        if (stockAdapter == null) {
                            stockAdapter = new Menu_StockAdapter(stockInfoArrayList, MenuHistoryItem.this);
                            recyclerView.setAdapter(stockAdapter);



                        } else {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            stockAdapter = new Menu_StockAdapter(stockInfoArrayList, MenuHistoryItem.this);
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
                    Toast.makeText(MenuHistoryItem.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("dbname", dbname);
                params.put("product_id", mProductName);
                params.put("status", "infinite");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void get_allstock(final String mProductName) {
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
                        try {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.GONE);
                            tv_noData.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            return;
                        } catch (NullPointerException e)
                        {
                            e.printStackTrace();
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
                            stockAdapter = new Menu_StockAdapter(stockInfoArrayList, MenuHistoryItem.this);
                            recyclerView.setAdapter(stockAdapter);

                        } else
                        {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            stockAdapter = new Menu_StockAdapter(stockInfoArrayList, MenuHistoryItem.this);
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
                    Toast.makeText(MenuHistoryItem.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("dbname", dbname);
                params.put("product_id", mProductName);
                params.put("status", "infinite");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}

package com.inevitablesol.www.shopmanagement.ItemModule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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
import com.inevitablesol.www.shopmanagement.ItemModule.Adapter.StockAdapter;
import com.inevitablesol.www.shopmanagement.ItemModule.Parser.ParseStock;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.product_info.adapter.ProductAdapter;
import com.inevitablesol.www.shopmanagement.product_info.adapter.ProductParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import com.inevitablesol.www.shopmanagement.Adapter.ProductAdapter;
//import com.inevitablesol.www.shopmanagement.Parser.ParseStock;
//import com.inevitablesol.www.shopmanagement.Parser.ProductParser;

public class AddToItems extends AppCompatActivity
{

   // private static final String GET_STOCK = "http://35.161.99.113:9000/api/item/list";
   private static final String GET_STOCK = "http://35.161.99.113:9000/webapi/product/p_info";

    private static final String ADD_STOCK = "http://35.161.99.113:9000/api/item/create_new";
    private  static  final String GET_PRODUCT=" http://35.161.99.113:9000/webapi/product/list";

    ArrayList<StockInfo> stockInfoArrayList;
    StockAdapter stockAdapter;
    RecyclerView recyclerView;
    AppCompatButton bt_addstock;
    private String mProductName;
    Spinner mProductSpinner;
    private  EditText mName;

    private static final String TAG = "AddToItems";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        recyclerView = (RecyclerView)findViewById(R.id.stock_recyclerView);
         mProductSpinner=(Spinner)findViewById(R.id.spnn_product);
        mName=(EditText)findViewById(R.id.input_productname);

//        get_allstock();

        getAppProductDetails();

        mName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                 String itemName=s.toString().trim();
//                getAppProduct(itemName);
                try{

                    stockAdapter.filter(itemName);
                }catch (NullPointerException e)
                {
                    Toast.makeText(AddToItems.this, "No items to search", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void getAppProduct( final String itemName)
    {
        mProductName= (String) mProductSpinner.getSelectedItem();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_STOCK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();
                Log.d("Resp",resp);

                try {
                    JSONObject jsonObject=new JSONObject(resp);
                    String message=jsonObject.getString("message");
                    if(message.equalsIgnoreCase("Search not found"))
                    {
                        stockAdapter.clearView();
                        stockAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        return;
                    }else
                    {
                        ParseStock parseStock = new ParseStock(resp);
                        parseStock.parseJSON();



                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);

                        stockInfoArrayList = parseStock.prepareStock();

                        if(stockAdapter==null) {
                            stockAdapter = new StockAdapter(stockInfoArrayList, AddToItems.this);
                            recyclerView.setAdapter(stockAdapter);


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
                                        Intent intent = new Intent(AddToItems.this, SelectedItemFromStock.class);

                                        intent.putExtra("item_name", stockInfo.getItem_name());
                                        intent.putExtra("product_type", stockInfo.getProduct_type());
                                        intent.putExtra("qty", stockInfo.getStock_qty());
                                        intent.putExtra("original_price", stockInfo.getOriginal_price());
                                        intent.putExtra("specification", stockInfo.getSpecification());
                                        intent.putExtra("mrp", stockInfo.getMrp());
                                        intent.putExtra("hsn_ssc_code", stockInfo.getHsn_ssc_code());
                                        intent.putExtra("modified_by", stockInfo.getProduct_id());

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
                        }else
                        {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            stockAdapter = new StockAdapter(stockInfoArrayList, AddToItems.this);
                            recyclerView.setAdapter(stockAdapter);


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

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();

                params.put("product_id",mProductName);
                params.put("item_name",itemName);

                Log.d("product_id",mProductName);
                Log.d("Item_name",itemName);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getAppProductDetails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    Log.d(TAG, "onResponse:Product"+response);

                    String resp = response.toString().trim();
                    ProductParser productParser=new ProductParser(resp);
                    productParser.productParser();
                    ProductAdapter productAdapter=new ProductAdapter(AddToItems.this,R.layout.product_list,ProductParser.productName,ProductParser.productId);
                    mProductSpinner.setAdapter(productAdapter);
                    mProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                            mProductName=   ProductParser.productId[position];
                            String itemName=mName.getText().toString().trim();
                            Log.d("name",mProductName);
                            get_allstock(mProductName);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }catch (Exception e)
                {
                    Log.d("Exception",""+e);
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(AddToItems.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("dbname","shop_mgmt");
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
            public void onResponse(String response)
            {
                String resp = response.toString().trim();
                Log.d("Stock Resp",resp);
                try {
                    JSONObject jsonObject=new JSONObject(resp);
                    String message=jsonObject.getString("message");
                    if(message.equalsIgnoreCase("Data not available"))
                    {
                        stockAdapter.clearView();
                        stockAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        return;
                    }else
                    {
                        ParseStock parseStock = new ParseStock(resp);
                        parseStock.parseJSON();



                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);

                        stockInfoArrayList = parseStock.prepareStock();

                        if(stockAdapter==null) {
                            stockAdapter = new StockAdapter(stockInfoArrayList, AddToItems.this);
                            recyclerView.setAdapter(stockAdapter);


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
                                        Intent intent = new Intent(AddToItems.this, AddItemToStock.class);

                                        intent.putExtra("item_name", stockInfo.getItem_name());
                                        intent.putExtra("product_type", stockInfo.getProduct_type());
                                        intent.putExtra("qty", stockInfo.getStock_qty());
                                        intent.putExtra("original_price", stockInfo.getOriginal_price());
                                        intent.putExtra("specification", stockInfo.getSpecification());
                                        intent.putExtra("mrp", stockInfo.getMrp());
                                        intent.putExtra("hsn_ssc_code", stockInfo.getHsn_ssc_code());
                                        intent.putExtra("modified_by", stockInfo.getProduct_id());

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
                        }else
                        {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            stockAdapter = new StockAdapter(stockInfoArrayList, AddToItems.this);
                            recyclerView.setAdapter(stockAdapter);


                        }

                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(AddToItems.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();

                params.put("dbname","shop_mgmt");
                params.put("product_id",mProductName);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume:");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
}

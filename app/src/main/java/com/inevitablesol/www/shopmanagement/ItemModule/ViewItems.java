package com.inevitablesol.www.shopmanagement.ItemModule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.inevitablesol.www.shopmanagement.R;
//import com.inevitablesol.www.shopmanagement.Adapter.ProductAdapter;
import com.inevitablesol.www.shopmanagement.ItemModule.Adapter.StockAdapter;
import com.inevitablesol.www.shopmanagement.billing_module.BillingProductAdapter;
import com.inevitablesol.www.shopmanagement.billing_module.BillingProductParser;
//import com.inevitablesol.www.shopmanagement.Parser.ParseStock;
//import com.inevitablesol.www.shopmanagement.Parser.ProductParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewItems extends AppCompatActivity {

    // private static final String GET_STOCK = "http://35.161.99.113:9000/api/item/list";
    private static final String GET_STOCK = "http://35.161.99.113:9000/webapi/product/p_info";
    private static final String GET_All = "http://35.161.99.113:9000/webapi/product/all";
    private static final String ADD_STOCK = "http://35.161.99.113:9000/api/item/create_new";
    private static final String GET_PRODUCT = " http://35.161.99.113:9000/webapi/product/list";
    private static final String REMOVE_ITEM = "http://35.161.99.113:9000/webapi/product/remove";
    private static final String TAG = "ViewItems";

    ArrayList<StockInfo> stockInfoArrayList;
    StockAdapter stockAdapter;
    RecyclerView recyclerView;
    AppCompatButton bt_addstock;
    private String mProductName, mProductType;
    Spinner mProductSpinner;
    private EditText mName;
    String viewType;
    ActionBar actionBar;
    TextView tv_noData;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        recyclerView = (RecyclerView) findViewById(R.id.stock_recyclerView);
        mProductSpinner = (Spinner) findViewById(R.id.spnn_product);
        mName = (EditText) findViewById(R.id.input_productname);
        tv_noData = (TextView) findViewById(R.id.tv_noData);

        actionBar = getSupportActionBar();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");


        Intent intent = getIntent();
        if (intent.hasExtra("viewtype")) {
            viewType = intent.getStringExtra("viewtype");
            if (viewType.equalsIgnoreCase("addItem")) {
                setTitle("Add Item");
            }
            if (viewType.equalsIgnoreCase("viewItem")) {
                setTitle("View Items");
            }
            if (viewType.equalsIgnoreCase("removeItem")) {
                setTitle("Remove Item");
            }
            if (viewType.equalsIgnoreCase("editItem")) {
                setTitle("Edit Item");
            }
        }



//        get_allstock();

        getAppProductDetails();

        mName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, final int start, int before, int count) {
                try
                {
                    query = mName.getText().toString().toLowerCase();
                    Log.d(TAG, "onTextChanged:"+query);
                    Log.d(TAG, "onTextChanged:Length"+query.length());
                    if (query.toString().isEmpty() || query.toString() == "")
                    {
                        String mProductName = mProductSpinner.getSelectedItem().toString();
                        Log.d(TAG, "onTextChanged:Product" + mProductName);
                        if (mProductName.equalsIgnoreCase("1")) {
                            get_all(mProductName);
                        } else {
                            get_allstock(mProductName);
                        }

                    }


                    final ArrayList<StockInfo> filteredList = new ArrayList<>();

                    for (int i = 0; i < stockInfoArrayList.size(); i++) {

//                   final String text = stockInfoArrayList.get(i).toString().toLowerCase();
                        StockInfo stockInfo = stockInfoArrayList.get(i);
                        String text = stockInfo.getItem_name().toString().toLowerCase();
                        Log.d(TAG, "onTextChanged:Stock Info"+stockInfo);
                        Log.d(TAG, "onTextChanged:Query"+query);
                        if (text.startsWith(String.valueOf(query))) {

                            filteredList.add(stockInfoArrayList.get(i));
                        }
                    }

//                    stockInfoArrayList.clear();
//
//                    for (int i = 0; i < filteredList.size(); i++) {
//                        Log.d(TAG, "onTextChanged:I" + i);
//                        Log.d(TAG, "onTextChanged:SizeOF" + filteredList.size());
//                        stockInfoArrayList.add(filteredList.get(i));
//
//                    }
//
//                    Log.d(TAG, "onTextChanged:Size" + stockInfoArrayList.size());
//                    recyclerView.setLayoutManager(new LinearLayoutManager(Add_Item.this));
//                    stockAdapter = new StockAdapter(stockInfoArrayList, Add_Item.this);
//                    recyclerView.setAdapter(stockAdapter);
//                    stockAdapter.notifyDataSetChanged();
                    stockInfoArrayList.clear();
                    stockInfoArrayList.addAll(filteredList);
                    Log.d(TAG, "onTextChanged: Stock"+stockInfoArrayList.toString());
                    Log.d(TAG, "onTextChanged: filter"+filteredList.toString());

                    recyclerView.setLayoutManager(new LinearLayoutManager(ViewItems.this));
                    stockAdapter = new StockAdapter(filteredList, ViewItems.this);
                    recyclerView.setAdapter(stockAdapter);
                    stockAdapter.notifyDataSetChanged();

                } catch (NumberFormatException e)
                {

                } catch (ArrayIndexOutOfBoundsException e) {

                }
            }


        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK)
            {
                getAppProductDetails();
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                //Write your code if there's no result
            }if(requestCode ==2)
            {
                getAppProductDetails();
            }
        }
    }

    public void addTextListener()
    {

        mName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final ArrayList<StockInfo> filteredList = new ArrayList<>();

                for (int i = 0; i < stockInfoArrayList.size(); i++) {

//                    final String text = stockInfoArrayList.get(i).toString().toLowerCase();
                    StockInfo stockInfo = stockInfoArrayList.get(i);
                    String text = stockInfo.getItem_name().toString().toLowerCase();
                    if (text.startsWith(String.valueOf(query))) {

                        filteredList.add(stockInfoArrayList.get(i));
                    }
                }
                if(stockAdapter !=null)
                {
                    stockAdapter.clearView();
                    stockAdapter.notifyDataSetChanged();
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(ViewItems.this));
                stockAdapter = new StockAdapter(filteredList, ViewItems.this);
                recyclerView.setAdapter(stockAdapter);
                stockAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }


    public void confirmDialog(final String itemId)
    {

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_rm_item_dialog);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Do you want to remove item from stock?");

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonYes);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                remove_item(itemId);

                dialog.dismiss();


            }
        });

        Button cancelDialog = (Button) dialog.findViewById(R.id.dialogButtonNo);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        dialog.show();

    }


    public void remove_item(final String itemId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REMOVE_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.toString().trim();
                Log.d("Remove Item", resp);
                try {
                    JSONObject msg = new JSONObject(resp);
                    String message = msg.getString("message");

                    if (message.equalsIgnoreCase("succesfully remove")) {
                        finish();
                        startActivity(getIntent());
                        Toast.makeText(ViewItems.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ViewItems.this, "Unable to remove item.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(ViewItems.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                dbname=shop_mgmt
//                item_id=4
                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                params.put("item_id", itemId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private void getAppProductDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {

                    Log.d(TAG, "onResponse() called with: response = [" + response + "]");


                    String resp = response.toString().trim();
                    BillingProductParser product = new BillingProductParser(resp);
                    product.billingproductParser();
                    BillingProductAdapter productAdapter = new BillingProductAdapter(ViewItems.this, R.layout.product_list, BillingProductParser.productName, BillingProductParser.productId);
                    mProductSpinner.setAdapter(productAdapter);
                    mProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                            mProductName = BillingProductParser.productId[position];
                            mProductType = BillingProductParser.productName[position];
                            String itemName = mName.getText().toString().trim();
                            Log.d("name", mProductName);

                            if (mProductType.equalsIgnoreCase("All"))
                            {
                                get_all(mProductName);
                            } else
                            {
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
                    Toast.makeText(ViewItems.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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


    public void get_allstock(final String mProductName)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_STOCK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.toString().trim();
                Log.d("Stock Resp", resp);
                try
                {
                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        stockAdapter.clearView();
                        stockAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.GONE);
                        tv_noData.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        ParseStock parseStock = new ParseStock(resp);
                        parseStock.parseJSON();

                        recyclerView.setVisibility(View.VISIBLE);
                        tv_noData.setVisibility(View.GONE);
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);

                        stockInfoArrayList = parseStock.prepareStock();

                        if (stockAdapter == null) {
                            stockAdapter = new StockAdapter(stockInfoArrayList, ViewItems.this);
                            recyclerView.setAdapter(stockAdapter);

                            try {
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
                                            if (viewType.equalsIgnoreCase("viewItem")) {
                                                Toast.makeText(ViewItems.this, "Only allowed to view", Toast.LENGTH_SHORT).show();
                                            }
                                            if (viewType.equalsIgnoreCase("addItem")) {


                                                if (stockInfo.getStorage_qty().equalsIgnoreCase("0")) {
                                                    Toast.makeText(ViewItems.this, "Storage is empty", Toast.LENGTH_SHORT).show();
                                                } else {

                                                    Intent intent = new Intent(ViewItems.this, SelectedItemFromStock.class);
                                                    Toast.makeText(ViewItems.this, "Product ID: " + stockInfo.getItem_id(), Toast.LENGTH_SHORT).show();
                                                    intent.putExtra("itemId", stockInfo.getItem_id());
                                                    intent.putExtra("item_name", stockInfo.getItem_name());
                                                    intent.putExtra("product_type", stockInfo.getProduct_type());
                                                    intent.putExtra("qty", stockInfo.getStorage_qty());
                                                    intent.putExtra("original_price", stockInfo.getOriginal_price());
                                                    intent.putExtra("specification", stockInfo.getSpecification());
                                                    intent.putExtra("mrp", stockInfo.getMrp());
                                                    intent.putExtra("hsn_ssc_code", stockInfo.getHsn_ssc_code());
                                                    intent.putExtra("modified_by", stockInfo.getProduct_id());
                                                    Log.d(TAG, "onInterceptTouchEvent:StockInfo_for_ItemAdd"+stockInfo.toString());
                                                    startActivityForResult(intent, 2);
                                                }

                                            }
                                            if (viewType.equalsIgnoreCase("removeItem")) {
                                                if (stockInfo.getStorage_qty().equalsIgnoreCase("0")) {
                                                    confirmDialog(String.valueOf(stockInfo.getItem_id()));
                                                } else {
                                                    Toast.makeText(ViewItems.this, "Item cannot be deleted. Storage quantity is greater than 0", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            if (viewType.equalsIgnoreCase("editItem")) {
                                                Intent editIntent = new Intent(ViewItems.this, EditItemActivity.class);
                                                editIntent.putExtra("hsn", stockInfo.getHsn_ssc_code());
                                                editIntent.putExtra("company", stockInfo.getCompany());
                                                editIntent.putExtra("storage_qty", stockInfo.getStorage_qty());
                                                editIntent.putExtra("item_price", stockInfo.getOriginal_price());
                                                editIntent.putExtra("name", stockInfo.getItem_name());
                                                editIntent.putExtra("product_id", stockInfo.getProduct_id());
                                                editIntent.putExtra("item_id", String.valueOf(stockInfo.getItem_id()));
                                                editIntent.putExtra("product_type", stockInfo.getProduct_type());
                                                editIntent.putExtra("mrp", stockInfo.getMrp());
                                                editIntent.putExtra("specification", stockInfo.getSpecification());
                                                editIntent.putExtra("unitPrice", stockInfo.getUnitPrice());
                                                editIntent.putExtra("discount", stockInfo.getDiscount());
                                                editIntent.putExtra("totalPrice", stockInfo.getTotalPrice());
                                                editIntent.putExtra("gst", stockInfo.getGst());
                                                startActivityForResult(editIntent, 1);
                                            }


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
                            }catch (Exception e)
                            {

                            }
                            }else{
                                stockAdapter.clearView();
                                stockAdapter.notifyDataSetChanged();
                                stockAdapter = new StockAdapter(stockInfoArrayList, ViewItems.this);
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
                    Toast.makeText(ViewItems.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("dbname", dbname);
                params.put("product_id", mProductName);
                params.put("status", "active");

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
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        stockAdapter.clearView();
                        stockAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.GONE);
                        tv_noData.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        return;
                    } else {
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
                            stockAdapter = new StockAdapter(stockInfoArrayList, ViewItems.this);
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
                                        if (viewType.equalsIgnoreCase("viewItem")) {
                                            Toast.makeText(ViewItems.this, "Only allowed to view", Toast.LENGTH_SHORT).show();
                                        } else
                                        {

                                            Log.d(TAG, "onInterceptTouchEvent:Length"+stockInfoArrayList.size());
                                            int position = rv.getChildAdapterPosition(child);
                                            Log.d(TAG, "onInterceptTouchEvent:"+position);

                                            StockInfo stockInfo = stockInfoArrayList.get(position);
                                            if (stockInfo.getStorage_qty().equalsIgnoreCase("0")) {
                                                Toast.makeText(ViewItems.this, "Storage is empty", Toast.LENGTH_SHORT).show();
                                            }
                                            if (viewType.equalsIgnoreCase("addItem")) {

                                                Intent intent = new Intent(ViewItems.this, SelectedItemFromStock.class);
                                                Toast.makeText(ViewItems.this, "Product ID: " + stockInfo.getItem_id(), Toast.LENGTH_SHORT).show();
                                                intent.putExtra("itemId", stockInfo.getItem_id());
                                                intent.putExtra("item_name", stockInfo.getItem_name());
                                                intent.putExtra("product_type", stockInfo.getProduct_type());
                                                intent.putExtra("qty", stockInfo.getStorage_qty());
                                                intent.putExtra("original_price", stockInfo.getOriginal_price());
                                                intent.putExtra("specification", stockInfo.getSpecification());
                                                intent.putExtra("mrp", stockInfo.getMrp());
                                                intent.putExtra("hsn_ssc_code", stockInfo.getHsn_ssc_code());
                                                intent.putExtra("modified_by", stockInfo.getProduct_id());
                                                startActivity(intent);
                                            }

                                            if (viewType.equalsIgnoreCase("removeItem")) {
                                                if (stockInfo.getStorage_qty().equalsIgnoreCase("0")) {
                                                    confirmDialog(String.valueOf(stockInfo.getItem_id()));
                                                } else {
                                                    Toast.makeText(ViewItems.this, "Item cannot be deleted. Storage quantity is greater than 0", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            if (viewType.equalsIgnoreCase("editItem"))
                                            {

                                                Intent editIntent = new Intent(ViewItems.this, EditItemActivity.class);
                                                editIntent.putExtra("hsn",stockInfo.getHsn_ssc_code());
                                                editIntent.putExtra("company",stockInfo.getCompany());
                                                editIntent.putExtra("storage_qty",stockInfo.getStorage_qty());
                                                editIntent.putExtra("item_price",stockInfo.getOriginal_price());
                                                editIntent.putExtra("name", stockInfo.getItem_name());
                                                editIntent.putExtra("product_id", stockInfo.getProduct_id());
                                                editIntent.putExtra("item_id", String.valueOf(stockInfo.getItem_id()));
                                                editIntent.putExtra("product_type", stockInfo.getProduct_type());
                                                editIntent.putExtra("mrp", stockInfo.getMrp());
                                                editIntent.putExtra("specification", stockInfo.getSpecification());
                                                editIntent.putExtra("unitPrice",stockInfo.getUnitPrice());
                                                editIntent.putExtra("discount",stockInfo.getDiscount());
                                                editIntent.putExtra("totalPrice",stockInfo.getTotalPrice());
                                                editIntent.putExtra("gst",stockInfo.getGst());
                                                startActivityForResult(editIntent,1);
                                            }

                                        }
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
                        } else
                        {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            stockAdapter = new StockAdapter(stockInfoArrayList, ViewItems.this);
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
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(ViewItems.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("dbname", dbname);
                params.put("product_id", mProductName);
                params.put("status", "active");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d( "onResume:","ViewItems");
        getAppProductDetails();
    }

}

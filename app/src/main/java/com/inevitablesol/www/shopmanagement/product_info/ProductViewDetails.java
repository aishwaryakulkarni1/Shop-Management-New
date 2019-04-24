package com.inevitablesol.www.shopmanagement.product_info;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.inevitablesol.www.shopmanagement.ItemModule.Add_Item;
import com.inevitablesol.www.shopmanagement.ItemModule.HistoryActivity;
import com.inevitablesol.www.shopmanagement.ItemModule.Parser.ParseStock;
import com.inevitablesol.www.shopmanagement.ItemModule.StockInfo;
import com.inevitablesol.www.shopmanagement.ItemModule.View_Items;
import com.inevitablesol.www.shopmanagement.MenuItemModule.EditMenu_new;
import com.inevitablesol.www.shopmanagement.MenuItemModule.ViewMenuActivity;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.billing_module.BillingProductAdapter;
import com.inevitablesol.www.shopmanagement.billing_module.BillingProductParser;
import com.inevitablesol.www.shopmanagement.purchase_module.ItemParser;
import com.inevitablesol.www.shopmanagement.purchase_module.PurchaseViewAdapter;
import com.inevitablesol.www.shopmanagement.purchase_module.PurchaseViewRecord;
import com.inevitablesol.www.shopmanagement.vendor_module.Record;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pritam on 01-12-2017.
 */

public class ProductViewDetails extends AppCompatActivity
{

    private static final String TAG = "ProductViewDeatils";
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String dbname;
    private ArrayList<PurchaseViewRecord> purchaseViewRecords;

    private RecyclerView recyclerView;
    private SearchView searchViewCustomer;
    private ArrayList<Record> vendorLists;
    private ArrayList<PurchaseViewRecord> purchaseViewArray;
    private PurchaseViewAdapter vendorAdapter;
    private Spinner mProductSpinner;
    private String mProductName;
    private String mProductType;
    private EditText mName;
    private ProductViewItem_Parser stockAdapter;
    private ArrayList<StockInfo> stockInfoArrayList;
    private StockAdapter stockAdapter_one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view_deatils);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        recyclerView=(RecyclerView)findViewById(R.id.product_ViewRecycle);
        mName = (EditText) findViewById(R.id.input_productname);
        mProductSpinner = (Spinner) findViewById(R.id.spnn_product);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));
        Log.d(TAG, "onCreate:");

        try
        {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(mProductSpinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight((int)height/2);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e)
        {
            // silently fail...
        }



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
                if (child != null && gestureDetector.onTouchEvent(e))
                {
                     int   position = rv.getChildAdapterPosition(child);
                    StockInfo stockInfo = stockInfoArrayList.get(position);
                             String name= mProductSpinner.getSelectedItem().toString().trim();
                             Log.d(TAG, "onInterceptTouchEvent: Name"+name);
                            Intent editIntent = new Intent(ProductViewDetails.this, Single_product_details.class);
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
                            editIntent.putExtra("total_price",stockInfo.getTotalPrice());
                            editIntent.putExtra("discount",stockInfo.getDiscount());
                            editIntent.putExtra("unit_price",stockInfo.getUnitPrice());
                            editIntent.putExtra("gst",stockInfo.getGst());
                            editIntent.putExtra("owner",stockInfo.getOwner());
                            editIntent.putExtra("discPrice",stockInfo.getDisPrice());
                            editIntent.putExtra("disPer",stockInfo.getDisCountPer());
                            editIntent.putExtra("munit",stockInfo.getMunit());
                            editIntent.putExtra("unit",stockInfo.getUnit());
                            editIntent.putExtra("barcode",stockInfo.getItembarcode());
                            Log.d(TAG, "onInterceptTouchEvent:  Item Deatils "+stockInfo.toString());
                            startActivity(editIntent);




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
        mName.addTextChangedListener(new TextWatcher()
        {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, final int start, int before, int count) {

                try {
                    query = mName.getText().toString().toLowerCase();
                    Log.d(TAG, "onTextChanged:"+query);
                    Log.d(TAG, "onTextChanged:Length"+query.length());
                    if (query.toString().isEmpty() || query.toString() == "")
                    {
                        String mProductName = mProductSpinner.getSelectedItem().toString();
                        Log.d(TAG, "onTextChanged:Product" + mProductName);
                        if (mProductName.equalsIgnoreCase("1"))
                        {

                                getItemDetailsByID(mProductName);
                                //get_all(mProductName);
                            } else
                            {
                                getItemDetailsByID(mProductName);
                            }

                    }else
                    {
                        Log.d(TAG, "onTextChanged:" + query);
                        stockAdapter.getFilter().filter(query);
                    }


//                    final ArrayList<StockInfo> filteredList = new ArrayList<>();
//
//                    if(stockInfoArrayList!=null)
//                    {
//
//                        for (int i = 0; i < stockInfoArrayList.size(); i++) {
//
////                   final String text = stockInfoArrayList.get(i).toString().toLowerCase();
//                            StockInfo stockInfo = stockInfoArrayList.get(i);
//                            String text = stockInfo.getItem_name().toString().toLowerCase();
//                            Log.d(TAG, "onTextChanged:Stock Info" + stockInfo);
//                            Log.d(TAG, "onTextChanged:Query" + query);
//                            if (text.startsWith(String.valueOf(query))) {
//
//                                filteredList.add(stockInfoArrayList.get(i));
//                            }
//                        }
//                    }

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

//                    recyclerView.setLayoutManager(new LinearLayoutManager(ProductViewDetails.this));
//                    stockAdapter_one = new StockAdapter(filteredList, ProductViewDetails.this);
//                    recyclerView.setAdapter(stockAdapter_one);
//                    stockAdapter_one.notifyDataSetChanged();

                } catch (NumberFormatException e)
                {

                } catch (ArrayIndexOutOfBoundsException e)
                {

                }

            }


        });

        getAppProductDetails();

    }







    @Override//
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


    private void getAppProductDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {

                    Log.d(TAG, "onResponse: Product Deatils"+response);

                    String resp = response.toString().trim();
                    BillingProductParser product = new BillingProductParser(resp);
                    product.billingproductParser();
                    BillingProductAdapter productAdapter = new BillingProductAdapter(ProductViewDetails.this, R.layout.product_list, BillingProductParser.productName, BillingProductParser.productId);
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
                                getItemDetailsByID(mProductName);
                                //get_all(mProductName);
                            } else
                            {
                                getItemDetailsByID(mProductName);
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
                    Toast.makeText(ProductViewDetails.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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




    private void getItemDetailsByID(final String p_id)
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETITEMDETAISBYID, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    Log.d(TAG, "onResponse: Item Details "+response);
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        try
                        {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.GONE);
                            //  tv_noData.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            return;
                        } catch (NullPointerException  e)
                        {
                            e.printStackTrace();
                        }
                    } else
                    {
                        try
                        {
                            ItemParser_for_View productParser = new ItemParser_for_View(response);
                                         productParser.parseJSON();

                            recyclerView.setVisibility(View.VISIBLE);
                            //    tv_noData.setVisibility(View.GONE);
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);

                            stockInfoArrayList = productParser.prepareStock();
                            stockAdapter = new ProductViewItem_Parser(stockInfoArrayList, ProductViewDetails.this);
                            recyclerView.setAdapter(stockAdapter);

                        } catch (NullPointerException  e)
                        {
                            e.printStackTrace();
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(ProductViewDetails.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("product_id", p_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }




}

package com.inevitablesol.www.shopmanagement.purchase_module;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.inevitablesol.www.shopmanagement.BuildConfig;
import com.inevitablesol.www.shopmanagement.ItemModule.Adapter.StockAdapter;
import com.inevitablesol.www.shopmanagement.ItemModule.Parser.ParseStock;
import com.inevitablesol.www.shopmanagement.ItemModule.StockInfo;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.RegistrationModule.ReadWriteExcelFile;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.billing_module.BillingProductAdapter;
import com.inevitablesol.www.shopmanagement.billing_module.BillingProductParser;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.sql_lite.ItemDetalisClass;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Purchase_Stock_Storage extends AppCompatActivity
{

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
    private SwitchCompat switchCompat;

    SqlDataBase sqlDataBase;

    private GlobalPool globalPool;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase__stock__storage);
        recyclerView = (RecyclerView) findViewById(R.id.stock_recyclerView);
        mProductSpinner = (Spinner) findViewById(R.id.spnn_product);
        mName = (EditText) findViewById(R.id.input_productname);
        tv_noData = (TextView) findViewById(R.id.tv_noData);
        switchCompat=(SwitchCompat)findViewById(R.id.barcodeSearch);
        globalPool=(GlobalPool)this.getApplicationContext();

        actionBar = getSupportActionBar();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        getAppProductDetails();

        sqlDataBase=new SqlDataBase(this);


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
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d(TAG, "onCheckedChanged: "+isChecked);
                if(isChecked)
                {
                    mName.setHint("Item Barcode");

                }
                else
                    mName.setHint("Product Name");
            }
        });



        mName.addTextChangedListener(new TextWatcher()
        {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count)
            {
                try {
                    if (!switchCompat.isChecked())
                    {

                        query = mName.getText().toString().toLowerCase();
                        Log.d(TAG, "onTextChanged:" + query);
                        Log.d(TAG, "onTextChanged:Length" + query.length());
                        if (query.length() > 0) {
                            final ArrayList<StockInfo> filteredList = new ArrayList<>();

                            for (int i = 0; i < stockInfoArrayList.size(); i++) {

//                    final String text = stockInfoArrayList.get(i).toString().toLowerCase();
                                StockInfo stockInfo = stockInfoArrayList.get(i);
                                Log.d(TAG, "onTextChanged:Stock Info" + stockInfo);
                                Log.d(TAG, "onTextChanged:Query" + query);
                                String text = stockInfo.getItem_name().toString().toLowerCase();
                                if (text.startsWith(String.valueOf(query)))

                                {

                                    filteredList.add(stockInfoArrayList.get(i));
                                }
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(Purchase_Stock_Storage.this));
                            stockAdapter = new StockAdapter(filteredList, Purchase_Stock_Storage.this);
                            recyclerView.setAdapter(stockAdapter);
                            stockAdapter.notifyDataSetChanged();
                        } else {
                            mProductName = mProductSpinner.getSelectedItem().toString();
                            Log.d(TAG, "onTextChanged:" + mProductName);

                            if (mProductType.equalsIgnoreCase("All")) {
                                get_all(mProductName);
                            } else {
                                get_allstock(mProductName);
                            }


                        }
                        // data set changed
                    }else
                    {
                        getItemDetails(mName.getText().toString());
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Log.i(TAG, "onTextChanged:"+e);
                }
            }
        });


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
                    BillingProductAdapter productAdapter = new BillingProductAdapter(Purchase_Stock_Storage.this, R.layout.product_list, BillingProductParser.productName, BillingProductParser.productId);
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
                    Toast.makeText(Purchase_Stock_Storage.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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
                            stockAdapter = new StockAdapter(stockInfoArrayList, Purchase_Stock_Storage.this);
                            recyclerView.setAdapter(stockAdapter);
                        } else
                        {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            stockAdapter = new StockAdapter(stockInfoArrayList, Purchase_Stock_Storage.this);
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
                    Toast.makeText(Purchase_Stock_Storage.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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
                        try {
                            stockAdapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.GONE);
                            tv_noData.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                            stockAdapter = new StockAdapter(stockInfoArrayList, Purchase_Stock_Storage.this);
                            recyclerView.setAdapter(stockAdapter);

                        }else
                            {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            stockAdapter = new StockAdapter(stockInfoArrayList, Purchase_Stock_Storage.this);
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
                    Toast.makeText(Purchase_Stock_Storage.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stock_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.imp_vendor:
                Log.d(TAG, "onOptionsItemSelected: Import");

                return true;
            case R.id.download_stock:
                createExls();
                Log.d(TAG, "onOptionsItemSelected:Export ");
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void createExls()
    {
        ReadWriteExcelFile readWriteExcelFile = new ReadWriteExcelFile();
        try {
            if(isStoragePermissionGranted())
            {
                readWriteExcelFile.getStockDeatils(getApplicationContext(),"Stock",stockInfoArrayList);

                File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File directory = new File (sdCard.getAbsolutePath() + "/Stock/"+"StockDeatils");
                File file = new File(directory, "stock.xls");

                try
                {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);




                    Uri path = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".provider",file);

                    intent.setDataAndType(path,"application/vnd.ms-excel");

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
                    //    this. startActivity(intent);
                    //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    final Notification noti = new NotificationCompat.Builder(this)
                            .setContentTitle("Download completed")
                            .setContentText("stock")
                            .setSmallIcon(R.drawable.app_icon)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setContentIntent(pIntent).build();


                    noti.flags |= Notification.FLAG_AUTO_CANCEL;

                    NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(0, noti);






                } catch (ActivityNotFoundException e)
                {
                    Toast.makeText(getApplicationContext(), "No Application available to view XLS", Toast.LENGTH_SHORT).show();
                }


                return;

            }else
            {
                Log.d(TAG, "onResponse:False");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public  boolean isStoragePermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
            {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    private void getItemDetails(String s)
    {
        try {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("barcode", s);
            jsonObject.addProperty("dbname", globalPool.getDbname());
            Log.d(TAG, "getItemDetails: " + s.trim());
            Ion.with(this)
                    .load("POST", WebApi.GETITEMDETAILSBYBARCODE)
                    .progressHandler(new ProgressCallback() {

                        @Override
                        public void onProgress(long downloaded, long total) {

                        }
                    })
                    .setJsonObjectBody(jsonObject)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result)
                        {
                            Log.d(TAG, "onCompleted:" + result);


                            try {
                                JSONObject jsonObject1=new JSONObject(result.toString());

                                JSONArray jsonArray =jsonObject1.getJSONArray("records");
                                if(stockInfoArrayList.size()>0)
                                {
                                    stockInfoArrayList.clear();
                                }
                                for (int i=0;i>jsonArray.length();i++)
                                {

                                       JSONObject  jsonObject2=jsonArray.getJSONObject(i);
                                       StockInfo stockInfo=new StockInfo();
                                       stockInfo.setStock_qty(jsonObject2.getString("stock_qty"));
                                       stockInfo.setStorage_qty(jsonObject2.getString("storage_qty"));
                                       stockInfoArrayList.add(stockInfo);


                                }




                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                            //  showData(result);

                            // do stuff with the result or error
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

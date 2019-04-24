package com.inevitablesol.www.shopmanagement.MenuItemModule;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.inevitablesol.www.shopmanagement.BuildConfig;
import com.inevitablesol.www.shopmanagement.ItemModule.Parser.ParseStock;
import com.inevitablesol.www.shopmanagement.ItemModule.StockInfo;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.RegistrationModule.ReadWriteExcelFile;
import com.inevitablesol.www.shopmanagement.billing_module.BillingProductParser;
import com.inevitablesol.www.shopmanagement.product_info.adapter.ProductAdapter;
import com.inevitablesol.www.shopmanagement.settings.Import_MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewMenuActivity extends AppCompatActivity {

    private static final String GET_PRODUCT = " http://35.161.99.113:9000/webapi/product/list";
    private static final String GET_All = "http://35.161.99.113:9000/webapi/product/all";
    private static final String GET_STOCK = "http://35.161.99.113:9000/webapi/product/p_info";
    private static final String REMOVE_ITEM = "http://35.161.99.113:9000/webapi/product/remove";

    Spinner mProductSpinner;
    private String mProductName, mProductType;
    private EditText mName;
    Menu_StockAdapter stockAdapter;
    ArrayList<StockInfo> stockInfoArrayList;
    RecyclerView recyclerView;
    String viewType;
    TextView tv_noData;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private static final String TAG = "ViewMenuActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mName = (EditText) findViewById(R.id.input_productname);
        mProductSpinner = (Spinner) findViewById(R.id.spnn_product);
        tv_noData = (TextView) findViewById(R.id.tv_noData);
        recyclerView = (RecyclerView) findViewById(R.id.stock_recyclerView);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        Intent intent = getIntent();
        Log.d(TAG, "onCreate: ");


                  if (intent.hasExtra("viewtype"))
                  {
                      viewType = intent.getStringExtra("viewtype");
                  }
                  if (viewType.equalsIgnoreCase("view")) {
                      setTitle("View Item");
                  }
                  if (viewType.equalsIgnoreCase("remove")) {
                      setTitle("Remove Item");
                  }
                  if (viewType.equalsIgnoreCase("edit")) {
                      setTitle("Edit Item");
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
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    if (viewType.equalsIgnoreCase("view"))
                    {
                        Toast.makeText(ViewMenuActivity.this, "Only allowed to view", Toast.LENGTH_SHORT).show();
                    } else {


                        int position = rv.getChildAdapterPosition(child);
                        StockInfo stockInfo = stockInfoArrayList.get(position);
                        if (stockInfo.getStorage_qty().equalsIgnoreCase("0"))
                        {
                           // Toast.makeText(ViewMenuActivity.this, "Storage is empty", Toast.LENGTH_SHORT).show();
                        }


                        if (viewType.equalsIgnoreCase("remove"))
                        {
                            if (stockInfo.getStorage_qty().equalsIgnoreCase("0")) {
                                confirmDialog(String.valueOf(stockInfo.getItem_id()));
                            } else {
                                Toast.makeText(ViewMenuActivity.this, "Item cannot be deleted. Storage quantity is greater than 0", Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (viewType.equalsIgnoreCase("edit"))
                        {

                            Intent editIntent = new Intent(ViewMenuActivity.this, EditMenu_new.class);
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
                            editIntent.putExtra("discPrice",stockInfo.getDisPrice());
                            editIntent.putExtra("disPer",stockInfo.getDisCountPer());
                            editIntent.putExtra("stockinfo",stockInfo);
                            Log.d(TAG, "onInterceptTouchEvent: Edit Menu "+stockInfo.toString());
                            startActivityForResult(editIntent, 1);

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



        getAppProductDetails();


        mName.addTextChangedListener(new TextWatcher()
        {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, final int start, int before, int count)
            {

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


                    }else
                    {
//

                        // stockAdapter.filter(query.toString());
                        // stockAdapter.clearView();
                        Log.d(TAG, "onTextChanged:" + query);
                        stockAdapter.getFilter().filter(query);
                        //stockAdapter.notifyDataSetChanged();

                    }

//

                } catch (NumberFormatException e)
                {

                } catch (ArrayIndexOutOfBoundsException e) {

                }
//                try {
//                    query = mName.getText().toString().toLowerCase();
//                    Log.d(TAG, "onTextChanged:"+query);
//                    Log.d(TAG, "onTextChanged:Length"+query.length());
//                    if (query.toString().isEmpty() || query.toString() == "")
//                    {
//                        String mProductName = mProductSpinner.getSelectedItem().toString();
//                        Log.d(TAG, "onTextChanged:Product" + mProductName);
//                        get_all(mProductName);
//
//                    }
//
//
//                    final ArrayList<StockInfo> filteredList = new ArrayList<>();
//
//                    if (stockInfoArrayList!=null)
//                    {
//                        for (int i = 0; i < stockInfoArrayList.size(); i++) {
//
//    //                   final String text = stockInfoArrayList.get(i).toString().toLowerCase();
//                            StockInfo stockInfo = stockInfoArrayList.get(i);
//                            String text = stockInfo.getItem_name().toString().toLowerCase();
//                            Log.d(TAG, "onTextChanged:Stock Info"+stockInfo);
//                            Log.d(TAG, "onTextChanged:Query"+query);
//                            if (text.startsWith(String.valueOf(query))) {
//
//                                filteredList.add(stockInfoArrayList.get(i));
//                            }
//                        }
//                    }
//
//
//
//                    recyclerView.setLayoutManager(new LinearLayoutManager(ViewMenuActivity.this));
//                    stockAdapter = new Menu_StockAdapter(filteredList, ViewMenuActivity.this);
//                    recyclerView.setAdapter(stockAdapter);
//                    stockAdapter.notifyDataSetChanged();
//
//                } catch (NumberFormatException e)
//                {
//
//                } catch (ArrayIndexOutOfBoundsException e) {
//
//                }
            }


        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                getAppProductDetails();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
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


    public void remove_item(final String itemId)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REMOVE_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.toString().trim();
                Log.d("Remove Item", resp);
                try {
                    JSONObject msg = new JSONObject(resp);
                    String message = msg.getString("message");
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(ViewMenuActivity.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
//                    if (message.equalsIgnoreCase("succesfully remove")) {
//                        finish();
//                        startActivity(getIntent());
//                        Toast.makeText(ViewMenuActivity.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(ViewMenuActivity.this, "Unable to remove item.", Toast.LENGTH_SHORT).show();
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(ViewMenuActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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
                try {

                    String resp = response.toString().trim();
                    BillingProductParser  productParser = new BillingProductParser(resp);
                    productParser.billingproductParser();
                    ProductAdapter productAdapter = new ProductAdapter(ViewMenuActivity.this, R.layout.product_list, BillingProductParser.productName, BillingProductParser.productId);
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
                    Toast.makeText(ViewMenuActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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

                        if (stockAdapter == null)
                        {
                            stockAdapter = new Menu_StockAdapter(stockInfoArrayList, ViewMenuActivity.this);
                            recyclerView.setAdapter(stockAdapter);


                        } else
                        {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            stockAdapter = new Menu_StockAdapter(stockInfoArrayList, ViewMenuActivity.this);
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
                    Toast.makeText(ViewMenuActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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

    public void get_allstock(final String mProductName)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_STOCK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
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
                        } catch (Exception e)
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
                            stockAdapter = new Menu_StockAdapter(stockInfoArrayList, ViewMenuActivity.this);
                            recyclerView.setAdapter(stockAdapter);


//                            recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//
//                                GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
//
//                                    @Override
//                                    public boolean onSingleTapUp(MotionEvent e) {
//                                        return true;
//                                    }
//
//                                });
//
//                                @Override
//                                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                                    View child = rv.findChildViewUnder(e.getX(), e.getY());
//                                    if (child != null && gestureDetector.onTouchEvent(e)) {
//
//                                        int position = rv.getChildAdapterPosition(child);
//                                        StockInfo stockInfo = stockInfoArrayList.get(position);
//                                        if (viewType.equalsIgnoreCase("view")) {
//                                            Toast.makeText(ViewMenuActivity.this, "Only allowed to view", Toast.LENGTH_SHORT).show();
//                                        }
//
//                                        if (viewType.equalsIgnoreCase("remove")) {
//                                            if (stockInfo.getStorage_qty().equalsIgnoreCase("0")) {
//                                                confirmDialog(String.valueOf(stockInfo.getItem_id()));
//                                            } else {
//                                                Toast.makeText(ViewMenuActivity.this, "Item cannot be deleted. Storage quantity is greater than 0", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//
//                                        if (viewType.equalsIgnoreCase("edit")) {
//                                            Intent editIntent = new Intent(ViewMenuActivity.this, EditMenuItemActivity.class);
//                                            editIntent.putExtra("hsn",stockInfo.getHsn_ssc_code());
//                                            editIntent.putExtra("company",stockInfo.getCompany());
//                                            editIntent.putExtra("storage_qty",stockInfo.getStorage_qty());
//                                            editIntent.putExtra("item_price",stockInfo.getOriginal_price());
//                                            editIntent.putExtra("name", stockInfo.getItem_name());
//                                            editIntent.putExtra("product_id", stockInfo.getProduct_id());
//                                            editIntent.putExtra("item_id", String.valueOf(stockInfo.getItem_id()));
//                                            editIntent.putExtra("product_type", stockInfo.getProduct_type());
//                                            editIntent.putExtra("mrp", stockInfo.getMrp());
//                                            editIntent.putExtra("specification", stockInfo.getSpecification());
//                                            editIntent.putExtra("total_price",stockInfo.getTotalPrice());
//                                            editIntent.putExtra("discount",stockInfo.getDiscount());
//                                            editIntent.putExtra("unit_price",stockInfo.getUnitPrice());
//                                            startActivityForResult(editIntent, 1);
//                                        }
//
//
//                                    }
//                                    return false;
//
//                                }
//
//                                @Override
//                                public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//                                }
//
//                                @Override
//                                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//                                }
//                            });
                        } else
                        {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            stockAdapter = new Menu_StockAdapter(stockInfoArrayList, ViewMenuActivity.this);
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
                    Toast.makeText(ViewMenuActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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




    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause:");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_menu_item, menu);
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
            case R.id.imp_menu:
                startActivity(new Intent(ViewMenuActivity.this, Import_MenuItem.class));
                Log.d(TAG, "onOptionsItemSelected: Import");

                return true;
            case R.id.exp_menu:
                createExle();
                Log.d(TAG, "onOptionsItemSelected:Export ");
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void createExle()
    {
        ReadWriteExcelFile readWriteExcelFile = new ReadWriteExcelFile();
        try {
            if(isStoragePermissionGranted())
            {
                readWriteExcelFile.getMenuItemDeatils(getApplicationContext(),"Items",stockInfoArrayList);
                try
                {
                    File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File directory = new File (sdCard.getAbsolutePath() + "/Menu/"+"Export");
                    File file = new File(directory, "menu.xls");
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    //intent.setAction(android.content.Intent.ACTION_VIEW);



                    Uri path = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".provider",file);

                    intent.setDataAndType(path,"application/vnd.ms-excel");

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
                    //    this. startActivity(intent);
                    //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    final Notification noti = new NotificationCompat.Builder(this)
                            .setContentTitle("Download completed")
                            .setContentText("Menu Item")
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



}

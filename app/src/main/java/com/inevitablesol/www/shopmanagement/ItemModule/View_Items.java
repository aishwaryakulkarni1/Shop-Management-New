package com.inevitablesol.www.shopmanagement.ItemModule;

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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.inevitablesol.www.shopmanagement.BuildConfig;
import com.inevitablesol.www.shopmanagement.ItemModule.Adapter.StockAdapter;
import com.inevitablesol.www.shopmanagement.ItemModule.Parser.ParseStock;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.RegistrationModule.ReadWriteExcelFile;
import com.inevitablesol.www.shopmanagement.billing_module.BillingProductAdapter;
import com.inevitablesol.www.shopmanagement.billing_module.BillingProductParser;
import com.inevitablesol.www.shopmanagement.settings.Import_Item;
import com.inevitablesol.www.shopmanagement.settings.Import_VendorList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class View_Items extends AppCompatActivity
{

    RecyclerView recyclerView;

    Spinner mProductSpinner;

    ActionBar actionBar;
    TextView tv_noData;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private EditText mName;
    private static final String GET_PRODUCT = " http://35.161.99.113:9000/webapi/product/list";
    private static final String GET_All = "http://35.161.99.113:9000/webapi/product/all";
    private static final String TAG = "Add_Item";
    private String mProductName;
    private String mProductType;
    private ArrayList<StockInfo> stockInfoArrayList;
    private StockAdapter stockAdapter;
    private static final String GET_STOCK = "http://35.161.99.113:9000/webapi/product/p_info";



    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__items);

        recyclerView = (RecyclerView) findViewById(R.id.stock_recyclerView);
        mProductSpinner = (Spinner) findViewById(R.id.spnn_product);
        mName = (EditText) findViewById(R.id.input_productname);
        tv_noData = (TextView)findViewById(R.id.tv_noData);

        actionBar = getSupportActionBar();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        getAppProductDetails();






        mName.addTextChangedListener(new TextWatcher()
        {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, final int start, int before, int count) {

                try {
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
                        stockAdapter.getFilter().filter(query);


                    }
//

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
                if (child != null && gestureDetector.onTouchEvent(e))
                {

                    int position = rv.getChildAdapterPosition(child);
                    StockInfo stockInfo = stockInfoArrayList.get(position);
                    Intent editIntent = new Intent(View_Items.this, History_itemDetails.class);
                    editIntent.putExtra("stockinfo",stockInfo);
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
                    editIntent.putExtra("o_mrp",stockInfo.getO_mrp());
                    editIntent.putExtra("discountPer",stockInfo.getDisCountPer());
                    Log.d(TAG, "onInterceptTouchEvent:Stock Info"+stockInfo.toString());
                    startActivity(editIntent);
                }
                return false;

            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
            {

            }
        });

    }


    public void addTextListener()
    {

        mName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, final int start, int before, int count)
            {

                query = query.toString().toLowerCase();
                if(query.toString().isEmpty() ||query.toString()=="")
                {
                    String mProductName=mProductSpinner.getSelectedItem().toString();
                    Log.d(TAG, "onTextChanged:Product"+mProductName);
                   // get_allstock(mProductName);
                    get_all(mProductName);

                }


                final ArrayList<StockInfo> filteredList = new ArrayList<>();

                for (int i = 0; i < stockInfoArrayList.size(); i++)
                {
                    StockInfo stockInfo = stockInfoArrayList.get(i);
                    String text = stockInfo.getItem_name().toString().toLowerCase();
                    if (text.startsWith(String.valueOf(query))) {

                        filteredList.add(stockInfoArrayList.get(i));
                    }
                }

                stockInfoArrayList.clear();

                for (int i = 0; i < filteredList.size(); i++) {
                    Log.d(TAG, "onTextChanged:I" + i);
                    Log.d(TAG, "onTextChanged:SizeOF" + filteredList.size());
                    stockInfoArrayList.add(filteredList.get(i));

                }

                Log.d(TAG, "onTextChanged:Size"+stockInfoArrayList.size());
                recyclerView.setLayoutManager(new LinearLayoutManager(View_Items.this));
                stockAdapter = new StockAdapter(stockInfoArrayList, View_Items.this);
                recyclerView.setAdapter(stockAdapter);
                stockAdapter.notifyDataSetChanged();
            }


        });





        try
        {
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

    }




    private void getAppProductDetails()
    {

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
                    BillingProductAdapter productAdapter = new BillingProductAdapter(View_Items.this, R.layout.product_list, BillingProductParser.productName, BillingProductParser.productId);
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
                    Toast.makeText(View_Items.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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
                            stockAdapter = new StockAdapter(stockInfoArrayList, View_Items.this);
                            recyclerView.setAdapter(stockAdapter);


                        } else
                        {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            stockAdapter = new StockAdapter(stockInfoArrayList, View_Items.this);
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
                    Toast.makeText(View_Items.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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
                    if (message.equalsIgnoreCase("Data not available")) {
                        try {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.GONE);
                            tv_noData.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            return;
                        }catch (NullPointerException e)
                        {

                        }
                    }else

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
                            stockAdapter = new StockAdapter(stockInfoArrayList, View_Items.this);
                            recyclerView.setAdapter(stockAdapter);



                        } else
                        {
                            stockAdapter.clearView();
                            stockAdapter.notifyDataSetChanged();
                            stockAdapter = new StockAdapter(stockInfoArrayList, View_Items.this);
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
                    Toast.makeText(View_Items.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
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
            case R.id.imp_item:

                 startActivity(new Intent(View_Items.this, Import_Item.class));
                Log.d(TAG, "onOptionsItemSelected: Import");

                return true;
            case R.id.exp_item:
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
                readWriteExcelFile.getItemDeatils(getApplicationContext(),"Items",stockInfoArrayList);

                try
                {
                    File sdCard =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File directory = new File (sdCard.getAbsolutePath() + "/Items/"+"Export");
                    File file = new File(directory, "Items.xls");
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
                            .setContentText("Items")
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

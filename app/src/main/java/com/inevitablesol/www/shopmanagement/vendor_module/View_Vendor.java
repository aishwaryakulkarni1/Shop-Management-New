package com.inevitablesol.www.shopmanagement.vendor_module;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.BuildConfig;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.RegistrationModule.ReadWriteExcelFile;
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
import com.inevitablesol.www.shopmanagement.settings.Import_VendorList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class View_Vendor extends AppCompatActivity
{

    private RecyclerView recyclerView;
    private static final String TAG = "View_Vendor";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String dbname;
    private  ArrayList<Record> vendorLists=null;
    private SearchView searchCustomer;

    private   VendorAdapter vendorAdapter;
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

        searchCustomer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
      //

        searchCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                searchCustomer.setIconified(false);

            }
        });
       searchCustomer.setQueryHint("Search Vendor Name");
      //  searchCustomer.setIconified(true);
        View closeButton = searchCustomer.findViewById(android.support.v7.appcompat.R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                EditText et = (EditText) findViewById(R.id.search_src_text);

                et.setText("");
                searchCustomer.setQuery("", false);
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


        getVendorDetails();

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
                    Log.d(TAG, "onInterceptTouchEvent:Info"+vInfo.toString());
                    Intent intent=new Intent(View_Vendor.this,SelectedVendorDetails.class);
                    intent.putExtra("v_id",vInfo.getVendorId());
                    intent.putExtra("shop_name",vInfo.getCompany());
                    intent.putExtra("v_name",vInfo.getOwnerName());
                    intent.putExtra("v_mobile",vInfo.getMobileNo());
                    intent.putExtra("v_address",vInfo.getAddress());
                    intent.putExtra("v_email",vInfo.getEmailId());
                    intent.putExtra("gststatus",vInfo.getGstDetails());
                    intent.putExtra("gst_number",vInfo.getGstinNo());
                    intent.putExtra("created_by",vInfo.getUname());
                    intent.putExtra("created_date",vInfo.getCreatedDate());
                    intent.putExtra("stateCode",vInfo.getStateCode());
                    intent.putExtra("state",vInfo.getState());
                    intent.putExtra("contact_person",vInfo.getContactPerson());
                    intent.putExtra("dbname",dbname);
                    Log.d(TAG, "onInterceptTouchEvent:"+vInfo.toString());

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
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_vendor, menu);
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

                startActivity(new Intent(View_Vendor.this, Import_VendorList.class));
                Log.d(TAG, "onOptionsItemSelected: Import");

                return true;
            case R.id.exp_vendor:
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
                readWriteExcelFile.getVendorDeatils(getApplicationContext(),"Vendor",vendorLists);

                File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
              ;
                File directory = new File (sdCard.getAbsolutePath() + "/Vendors/"+"Export");
                File file = new File(directory, "vendors.xls");
//
//

                try
                {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);

                    Uri path = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".provider",file);

                    intent.setDataAndType(path,"application/vnd.ms-excel");

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
                    final Notification noti = new NotificationCompat.Builder(this)
                            .setContentTitle("Download completed")
                            .setContentText("Vendors")
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
        if (Build.VERSION.SDK_INT >= 23)
        {
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
        else
            { //permission is automatically granted on sdk<23 upon installation

            Log.v(TAG,"Permission is granted");
            return true;
        }
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
                        if(vendorAdapter!=null)
                        {
                            vendorAdapter.clearView();
                            vendorAdapter.notifyDataSetChanged();
                        }




                    }else
                    {
                        try
                        {
                            VendorList vendorList=new VendorList();
                            Gson gson=new Gson();
                            vendorList= gson.fromJson(response,vendorList.getClass());
                            vendorLists=(ArrayList<Record>)vendorList.getRecords();
                            if(vendorAdapter ==null)
                            {

                                vendorAdapter=new VendorAdapter(vendorLists,View_Vendor.this);
                                recyclerView.setHasFixedSize(true);

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(vendorAdapter);

                                Log.d(TAG, "onResponse: "+response);
                            }else

                                {
                                    vendorAdapter.clearView();
                                    vendorAdapter.notifyDataSetChanged();
                                    vendorAdapter=new VendorAdapter(vendorLists,View_Vendor.this);
                                    recyclerView.setHasFixedSize(true);

                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setAdapter(vendorAdapter);


                            }

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
                    Toast.makeText(View_Vendor.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(View_Vendor.this);
        requestQueue.add(stringRequest);

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
                            VendorAdapter vendorAdapter=new VendorAdapter(vendorLists,View_Vendor.this);
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
                    Toast.makeText(View_Vendor.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("mobile_no", s);


                Log.d(TAG, "getParams:View Vendor By name"+params.toString());
                return  params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(View_Vendor.this);
        requestQueue.add(stringRequest);

    }

    private void getVendorDetails()
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebApi.GETVENDDOR_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.d("responseLsit",response);
                JSONObject msg = null;
                try
                {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
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
                            VendorAdapter vendorAdapter=new VendorAdapter(vendorLists,View_Vendor.this);
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
                    Toast.makeText(View_Vendor.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(View_Vendor.this);
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
                getVendorDetails();
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        }

    }


    private void hideKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onBackPressed() 
    {
        super.onBackPressed();

        Log.d(TAG, "onBackPressed:");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }




}

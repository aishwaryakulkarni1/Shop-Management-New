package com.inevitablesol.www.shopmanagement.customer_module;

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
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.SearchView;
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

import com.android.volley.NoConnectionError;
import com.inevitablesol.www.shopmanagement.BuildConfig;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.RegistrationModule.ReadWriteExcelFile;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.settings.Import_Customer;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewCustomer extends AppCompatActivity
{

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_view_customer);
        //searchCust=(EditText)findViewById(R.id.input_searchCusttomer);
        recyclerView = (RecyclerView)findViewById(R.id.cust_recyclerView);



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
                    Log.d(TAG, "onInterceptTouchEvent: "+customerInfo.toString());
                    Intent intent=new Intent(ViewCustomer.this,SelectedCustomerDetails.class);
                    intent.putExtra("c_id",customerInfo.getCustomer_id());
                    intent.putExtra("c_name",customerInfo.getCustomer_name());
                    intent.putExtra("c_mobile",customerInfo.getMobile_numbe());
                    intent.putExtra("c_address",customerInfo.getAddress());
                    intent.putExtra("c_email",customerInfo.getEmail_id());
                    intent.putExtra("gst",customerInfo.getGstin());
                    intent.putExtra("supplier",customerInfo.getState());
                    intent.putExtra("dbname",dbname);
                    intent.putExtra("delevery_status",customerInfo.getHome_delivery());
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
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");

        getAllCustomerDetails();

        searchViewCustomer=(SearchView)findViewById(R.id.simpleSearchView);
        searchViewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                searchViewCustomer.setIconified(false);

            }
        });
        //searchViewCustomer.setIconifiedByDefault(false);
        searchViewCustomer.setQueryHint("Search Customer Name");
       // searchViewCustomer.setIconified(true);
        View closeButton = searchViewCustomer.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
       
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                EditText et = (EditText) findViewById(R.id.search_src_text);

                //Clear the text from EditText view
                et.setText("");
                //Clear query
                searchViewCustomer.setQuery("", false);
                //Collapse the action view
//                searchViewCustomer.onActionViewCollapsed();
                getAllCustomerDetails();
                hideKeyboard();

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
                }else
                {
                    getAllCustomerDetails();
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



    }



    private void hideKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
                            hideKeyboard();
                            CustomerParser customerParser=new CustomerParser(resp);
                            customerParser.custDetails();
                            customerInfos= customerParser.makeArray();
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            customerAdapter=new CustomerAdapter(customerInfos,ViewCustomer.this);
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
                            customerAdapter = new CustomerAdapter(customerInfos, ViewCustomer.this);
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
                            ViewCustomer.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                            CustomerParser customerParser=new CustomerParser(resp);
                            customerParser.custDetails();
                            customerInfos= customerParser.makeArray();
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            customerAdapter=new CustomerAdapter(customerInfos,ViewCustomer.this);
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
                            customerAdapter = new CustomerAdapter(customerInfos, ViewCustomer.this);
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
    protected void onStart()
    {

        super.onStart();
        Log.d(TAG, "onStart: ");


    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Log.d(TAG, "onRestart: ");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cust, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.imp_customer:
                startActivity(new Intent(ViewCustomer.this, Import_Customer.class));
                Log.d(TAG, "onOptionsItemSelected: Import");
                return true;
            case R.id.exp_customer:
                Log.d(TAG, "onOptionsItemSelected:Export ");
                createExle();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void createExle()
    {
        ReadWriteExcelFile readWriteExcelFile = new ReadWriteExcelFile();
        try
        {
            if(isStoragePermissionGranted())
            {
                readWriteExcelFile.getCustomerDeatils(getApplicationContext(),"cutomer",customerInfos);


                    File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File directory = new File (sdCard.getAbsolutePath() + "/Cutomers/"+"Export");
                    File file = new File(directory, "cutomer.xls");
//
//

                    try
                    {
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
                                .setContentText("Customers")
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

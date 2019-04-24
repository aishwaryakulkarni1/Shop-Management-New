package com.inevitablesol.www.shopmanagement.wishList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.gson.Gson;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class View_WishList extends AppCompatActivity
{
    private Spinner spinner;
    private static final String TAG = "View_WishList";



    private String userId;
    private ArrayList<WishGsonList> ex_list;
    private RecyclerView recyclerView;
    private WishAdapter wishAdapter;

    private SharedPreferences sharedpreferences;
    public  final String MyPREFERENCES = "MyPrefs" ;
    private String dbname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_whishlist);
        spinner=(Spinner)findViewById(R.id.spnn_wish);
        recyclerView=(RecyclerView)findViewById(R.id.wish_recyclerViewGson);


        String[] paymentMode = {" Select Status","Completed","Canceled","Pending"};
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,R.layout.simple_spinner,R.id.sp_item, paymentMode);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                          String name= parent.getSelectedItem().toString().trim();
                               if(name.equalsIgnoreCase("Select Status"))
                               {
                                   getWishListByStatusEmpty("null");
                               }else
                               {
                                   getWishListByStatus(name);
                               }

                Log.d(TAG, "onItemSelected:"+name);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));
        userId= sharedpreferences.getString("userId","");

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
                     WishGsonList vInfo= ex_list.get(position);
                    Log.d(TAG, "onInterceptTouchEvent: "+vInfo.toString());
                    Intent intent=new Intent(View_WishList.this,SelectedWishDetails.class);
                    intent.putExtra("id",String.valueOf(vInfo.getId()));
                    intent.putExtra("name",vInfo.getCustName());
                    intent.putExtra("mobile",vInfo.getMobNo());
                    intent.putExtra("email",vInfo.getEmail());
                    intent.putExtra("description",vInfo.getDescription());
                    intent.putExtra("tillDate",vInfo.getTillDate());
                    intent.putExtra("reminder",vInfo.getReminderDate());
                    intent.putExtra("status",vInfo.getStatus());
                    intent.putExtra("totalQty",vInfo.getTotalqty());
                    intent.putExtra("dbname",dbname);
                    startActivity(intent);



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

    private void getWishListByStatusEmpty(String aNull)
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_WISHLISTBYNAME, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try
                {
                    Log.d("response",response);
                    JSONObject msg = new JSONObject(response);
                    String message = msg.getString("message");

                    if(message.equalsIgnoreCase("Data Available"))
                    {
                        WishData wishData = new WishData();
                        Gson gson = new Gson();
                        wishData = gson.fromJson(response, wishData.getClass());
                        ex_list = (ArrayList<WishGsonList>) wishData.getWishGsonList();

                        if (wishAdapter != null) {
                            wishAdapter.clearView();
                            wishAdapter.notifyDataSetChanged();
                        }
                        wishAdapter = new WishAdapter(ex_list, View_WishList.this);
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(wishAdapter);


                    }else
                    {
                        if (wishAdapter != null) {
                            wishAdapter.clearView();
                            wishAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(getApplicationContext(),"Data Not Available",Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e)
                {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(View_WishList.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);

                Log.d(TAG, "getParams: "+params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private void getWishListByStatus(final String name)
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_WISHLISTBYNAME, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try
                {
                    Log.d("response",response);
                    JSONObject msg = new JSONObject(response);
                    String message = msg.getString("message");

                    if(message.equalsIgnoreCase("Data Available"))
                    {
                        WishData wishData = new WishData();
                        Gson gson = new Gson();
                        wishData = gson.fromJson(response, wishData.getClass());
                        ex_list = (ArrayList<WishGsonList>) wishData.getWishGsonList();

                        if (wishAdapter != null) {
                            wishAdapter.clearView();
                            wishAdapter.notifyDataSetChanged();
                        }
                        wishAdapter = new WishAdapter(ex_list, View_WishList.this);
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(wishAdapter);


                    }else
                    {
                        if (wishAdapter != null) {
                            wishAdapter.clearView();
                            wishAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(getApplicationContext(),"Data Not Available",Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e)
                {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(View_WishList.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("status",name);
                Log.d(TAG, "getParams: "+params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

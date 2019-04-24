package com.inevitablesol.www.shopmanagement.account;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.gson.Gson;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.vendor_module.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewSubUser extends AppCompatActivity
{
    private static final String TAG = "ViewSubUser";
    private GlobalPool globalPool;
  private ArrayList<Alluser> allusers;
    private RecyclerView recyclerView;
    private   UserTypeAdapter userTypeAdapter=null;
    private TextView tx_userCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sub_user);
        Spinner userInfo=(Spinner)findViewById(R.id.no_of_user);
        recyclerView=(RecyclerView)findViewById(R.id.stock_recyclerView);
        globalPool = (GlobalPool) this.getApplication();
        tx_userCount=(TextView)findViewById(R.id.txt_totalUser);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.users, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userInfo.setAdapter(adapter);

         userInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
             {
                                String role= (String) adapter.getItem(position);
                                        getUserInfo(role);

             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });


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
                    Alluser vInfo= allusers.get(position);
                    Log.d(TAG, "onInterceptTouchEvent:Info"+vInfo.toString());
                    Intent intent=new Intent(ViewSubUser.this,View_User_Deatil.class);
                    intent.putExtra("info",vInfo);

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

    private void getUserInfo(final  String role)
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.USER_GETSUBUSEER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                Log.d(TAG, "onResponse sss: " + response);
                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if(message.equalsIgnoreCase("Data available"))
                    {

                        Gson  gson=new Gson();
                        AllUserByType allUserByType=new AllUserByType();
                        allUserByType=gson.fromJson(response,allUserByType.getClass());
                           allusers= (ArrayList<Alluser>) allUserByType.getAlluser();

                        userTypeAdapter=new UserTypeAdapter(allusers);
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(userTypeAdapter);
                        Log.d(TAG, "onResponse: Count"+userTypeAdapter.getItemCount());
                        tx_userCount.setText(String.valueOf(userTypeAdapter.getItemCount()));


                    }else

                    {
                        Log.d(TAG, "onResponse: Else");
                        try {
                            userTypeAdapter.clearView();
                            userTypeAdapter.notifyDataSetChanged();
                        } catch (Exception e)
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
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(ViewSubUser.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", globalPool.getDbname());
                params.put("dbname", globalPool.getDbname());
                params.put("user_type",role);

                Log.d(TAG, "getParams:BankDetails" + params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

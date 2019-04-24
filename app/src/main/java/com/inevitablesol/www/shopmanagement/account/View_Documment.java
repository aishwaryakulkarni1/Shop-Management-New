package com.inevitablesol.www.shopmanagement.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class View_Documment extends AppCompatActivity {

    private Spinner mspinner;;
    ArrayAdapter<CharSequence> adapter;
    private static final String TAG = "View_Documment";
    private GlobalPool globalPool;
    private RecyclerView recyclerView;
    private ShopDocuments listDocument;
    private ArrayList<DocumentList> docList;
    private DocumentTypeAdapter documentTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__documment);
        recyclerView=(RecyclerView)findViewById(R.id.stock_recyclerView);
        mspinner=(Spinner)findViewById(R.id.no_of_user);
        globalPool = (GlobalPool) this.getApplication();
         adapter = ArrayAdapter.createFromResource(this,
                R.array.shop_document, android.R.layout.simple_spinner_item);
        mspinner.setAdapter(adapter);

        mspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    DocumentList vInfo= docList.get(position);
//                    Bundle bundle=new Bundle();
//                    bundle.putSerializable("record",  vInfo);
                    Log.d(TAG, "onInterceptTouchEvent:Info"+vInfo.toString());


                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(vInfo.getImageLink()));
                    startActivity(i);





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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.SHOP_DOCUMENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                Log.d(TAG, "onResponse: Images " + response);
                JSONObject msg = null;
                try
                {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if(message.equalsIgnoreCase("Image Retrived"))
                    {

                        Gson  gson=new Gson();
                        ShopDocuments shopDocuments=new ShopDocuments();
                        listDocument=gson.fromJson(response,shopDocuments.getClass());
                        docList= (ArrayList<DocumentList>) listDocument.getDocumentList();

                        documentTypeAdapter=new DocumentTypeAdapter(docList);
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(documentTypeAdapter);


                    }else
                    {
                        Log.d(TAG, "onResponse: Else");

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
                    Toast.makeText(View_Documment.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", globalPool.getDbname());
                params.put("dbname", globalPool.getDbname());
                params.put("shopId",globalPool.getShopId());
                params.put("type",role);


                Log.d(TAG, "getParams:BankDetails" + params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

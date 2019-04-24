package com.inevitablesol.www.shopmanagement.purchase_module;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.inevitablesol.www.shopmanagement.account.DocumentList;
import com.inevitablesol.www.shopmanagement.account.DocumentTypeAdapter;
import com.inevitablesol.www.shopmanagement.account.ShopDocuments;
import com.inevitablesol.www.shopmanagement.account.View_Documment;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class PurchaseImageList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static final String TAG = "PurchaseImageList";
    private GlobalPool globalPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_image_list);
        recyclerView=(RecyclerView)findViewById(R.id.stock_recyclerView);
        globalPool = (GlobalPool) this.getApplication();

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
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if(message.equalsIgnoreCase("Image Retrived"))
                    {

                        Gson gson=new Gson();
//                        ShopDocuments shopDocuments=new ShopDocuments();
//                        listDocument=gson.fromJson(response,shopDocuments.getClass());
//                        docList= (ArrayList<DocumentList>) listDocument.getDocumentList();
//
//                        documentTypeAdapter=new DocumentTypeAdapter(docList);
//                        recyclerView.setHasFixedSize(true);
//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                        recyclerView.setLayoutManager(layoutManager);
//                        recyclerView.setAdapter(documentTypeAdapter);


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
                    Toast.makeText(PurchaseImageList.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

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

package com.inevitablesol.www.shopmanagement.purchase_module;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.account.DocumentList;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetPurchaseImages extends AppCompatActivity
{

      ImageView imageView;
    private static final String TAG = "GetPurchaseImages";
    private GlobalPool globalPool;
    private ArrayList<PurchaseImagePoJo> imageArray=new ArrayList<PurchaseImagePoJo>();
    private RecyclerView recyclerView;
    private  boolean isDocUploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_purchase_images);
        globalPool= (GlobalPool) this.getApplicationContext();
        recyclerView=(RecyclerView)findViewById(R.id.purchase_ImageId);

        Intent intent=getIntent();
        if(intent.hasExtra("invNumber"))
        {
            getAllImages(intent.getStringExtra("invNumber"));
        }

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
                    PurchaseImagePoJo vInfo= imageArray.get(position);
//                    Bundle bundle=new Bundle();
//                    bundle.putSerializable("record",  vInfo);
                    Log.d(TAG, "onInterceptTouchEvent:Info"+vInfo.toString());


                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(vInfo.getLink()));
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

    private void getAllImages(final String purchaseId)
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GetPurchaseImage, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                Log.d(TAG, "onResponse: "+response);
                try
                {


                    JSONObject jsonObject=new JSONObject(response);
                               JSONArray jsonArray= jsonObject.getJSONArray("records");
                               for (int i=0;i<jsonArray.length();i++)
                               {
                                   JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                   PurchaseImagePoJo purchaseImagePoJo=new PurchaseImagePoJo();
                                   purchaseImagePoJo.setId(jsonObject1.getString("id"));
                                   purchaseImagePoJo.setImageName(jsonObject1.getString("imageName"));
                                   purchaseImagePoJo.setLink(jsonObject1.getString("link"));
                                   purchaseImagePoJo.setPurchaseId(jsonObject1.getString("p_id"));


                                                   imageArray.add(purchaseImagePoJo);

                               }

                    Log.d(TAG, "onResponse: List"+imageArray);

                    PurchaseImageAdapter imageAdpater=new PurchaseImageAdapter( getApplicationContext(),GetPurchaseImages.this,imageArray);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(imageAdpater);
                    loading.dismiss();

                  //  Log.d(TAG, "onResponse: Countr"+imageAdpater.getItemCount());





                } catch (Exception e)
                {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(GetPurchaseImages.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("dbname", globalPool.getDbname());
                params.put("p_id",purchaseId);
                Log.d("parameter", params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }
}

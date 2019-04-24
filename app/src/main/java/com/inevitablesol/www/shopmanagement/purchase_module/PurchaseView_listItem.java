package com.inevitablesol.www.shopmanagement.purchase_module;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PurchaseView_listItem extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private TextView txt_shopName,txt_c_personName,txt_email,txt_address,txt_gstCode,txt_placeOfSupplier,txt_gstStatus,txt_mobile,txt_invocieNumber,txt_invocieDate;
    private AppCompatButton bt_next;
    private static final String TAG = "PurchaseView_listItem";
    private Context context=PurchaseView_listItem.this;
    private String dbname;
    private JSONArray jsonArray;
    private  JSONArray jsonArray2;
    private JSONArray itemData;
    String companyName,contactPerson,mobileNumber,emailId,address,gstin,gstStatus;
    private  ArrayList<ItemList> iLists=new ArrayList<>();
    private String purchase_id;

   private SharedPreferences sharedpreferences;
    private  final String MyPREFERENCES = "MyPrefs";
    private TextView txt_state;

    AppCompatButton ImagesView;
    private String invoiceNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_with_image);
        txt_shopName = (TextView) findViewById(R.id.p_view_shopName);
        txt_c_personName = (TextView) findViewById(R.id.p_view_contact_person);
        txt_email = (TextView) findViewById(R.id.p_v_email_id);
        txt_address = (TextView) findViewById(R.id.p_v_address);
        txt_gstCode = (TextView) findViewById(R.id.p_v_gstIn);
        txt_gstStatus = (TextView) findViewById(R.id.p_v_gstStatus);
        txt_mobile = (TextView) findViewById(R.id.p_v_mobileNumber);
        txt_placeOfSupplier=(TextView)findViewById(R.id.p_v_statusCode);
        txt_invocieDate=(TextView)findViewById(R.id.p_view_invoiceDate);
        txt_invocieNumber=(TextView)findViewById(R.id.p_view_invoiceNumber);
        recyclerView = (RecyclerView) findViewById(R.id.purchase_vendor_item);
        txt_state=(TextView)findViewById(R.id.p_v_state);
        ImagesView=(AppCompatButton) findViewById(R.id.p_v_Image);
        ImagesView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                    Intent intent=new Intent(PurchaseView_listItem.this,GetPurchaseImages.class);
                    intent.putExtra("invNumber",invoiceNumber);

                startActivity(intent);
            }
        });



        bt_next = (AppCompatButton) findViewById(R.id.p_v_next);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        bt_next.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.hasExtra("v_id"))
        {
            String invoiceNumber = intent.getStringExtra("invoice_num");
            String v_id = intent.getStringExtra("v_id");
             dbname = intent.getStringExtra("dbname");
            String owner = intent.getStringExtra("owner");
            purchase_id=intent.getStringExtra("purchase_id");
            String vendor_id=intent.getStringExtra("vendor_id");


            getdetailByPurchaseId(purchase_id ,vendor_id);

        } else

            {

            Toast.makeText(context, "Plase Refresh Screen", Toast.LENGTH_SHORT).show();
            finish();
        }
        Log.d(TAG, "onCreate:");

    }



    private void getdetailByPurchaseId(final String purchase_id , final String vendor_id)
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(PurchaseView_listItem.this).add(new StringRequest(Request.Method.POST, WebApi.GETPURCHASEDETAILSBYID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    } else {
                        try
                        {
                            loading.dismiss();
                            JSONObject jsonObject=new JSONObject(response);
                                           jsonArray= jsonObject.getJSONArray("rows");
                                            jsonArray2=jsonObject.getJSONArray("purchase_view_records");

                                           itemData=jsonObject.getJSONArray("itemdata");
                                        displayData(itemData);

                            //loop through each object
                            for (int i=0; i<jsonArray.length(); i++)
                            {

                                try {
                                    JSONObject jsonProductObject = jsonArray.getJSONObject(i);
                                    Log.d(TAG, "onResponse: Json"+jsonProductObject.toString());
                                    companyName = jsonProductObject.getString("company");
                                    contactPerson = jsonProductObject.getString("contact_person");
                                    mobileNumber =jsonProductObject.getString("mobile_no");
                                    emailId=jsonProductObject.getString("email_id");
                                    address=jsonProductObject.getString("address");
                                    gstin=jsonProductObject.getString("gstin_no");
                                    gstStatus=jsonProductObject.getString("gst_details");
                                    txt_state.setText(jsonProductObject.getString("state"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                            loading.dismiss();
                            Toast.makeText(context, companyName, Toast.LENGTH_SHORT).show();
                            txt_shopName.setText(companyName);
                            txt_c_personName.setText(contactPerson);
                            txt_mobile.setText(mobileNumber);
                            txt_email.setText(emailId);
                            txt_address.setText(address);
                            txt_gstCode.setText(gstin);
                            txt_gstStatus.setText(gstStatus);

                             if(jsonArray2!=null)
                            {
                                JSONObject placeOfSupply=jsonArray2.getJSONObject(0);
                                String pSupplier=placeOfSupply.getString("place_of_supply");
                                  invoiceNumber=placeOfSupply.getString("invoice_no");
                                String invoiceDate=placeOfSupply.getString("invoice_date");
                                txt_invocieNumber.setText(invoiceNumber);
                                txt_invocieDate.setText(invoiceDate);
                                txt_placeOfSupplier.setText(pSupplier);

                            }
                            Log.d(TAG, "onResponse:item"+itemData);
                            Log.d(TAG, "onResponse:json"+jsonArray);


                        } catch (NullPointerException e)
                        {

                        }catch (JSONException e)
                        {

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(PurchaseView_listItem.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("vendor_id",vendor_id);
                params.put("purchase_id",purchase_id);
                Log.d(TAG, "getParams:GETINVOICE"+params.toString());
                return params;
            }


        });
    }

    private void displayData(JSONArray itemData)
    {
        Log.d(TAG, "displayItem:"+itemData);

        for(int i=0;i<itemData.length();i++)
        {
            try
            {
                            ItemList itemList=new ItemList();
                               JSONObject jsonObject =itemData.getJSONObject(i);
                               itemList.setItemName(jsonObject.getString("item_name"));
                               itemList.setUnitPrice(jsonObject.getString("unit_price"));
                               itemList.setDiscount(jsonObject.getString("discount"));
                                itemList.setQty(jsonObject.getString("qty"));
                               iLists.add(itemList);


            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }

        View_SelectedList view_selectedList=new View_SelectedList(iLists,PurchaseView_listItem.this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(view_selectedList);






    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
           case  R.id.p_v_next:
               Intent intent=new Intent(context,PurchaseView_listImage.class);
                intent.putExtra("json", String.valueOf(jsonArray2));
                intent.putExtra("purchase_id",purchase_id);
            startActivity(intent);
            break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                finish();
                Log.d(TAG, "onOptionsItemSelected:");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

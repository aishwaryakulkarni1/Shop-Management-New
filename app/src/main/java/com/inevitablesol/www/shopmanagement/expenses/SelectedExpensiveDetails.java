package com.inevitablesol.www.shopmanagement.expenses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectedExpensiveDetails extends AppCompatActivity implements View.OnClickListener {
    private TextView txt_exp_type,txt_exp_name,txt_exp_date;
    private  TextView txt_exp_qty,txt_totalGst,txt_amountPaid,txt_totalAmount,txt_otherCharges,txt_balance,txt_refNumber,txt_subtotal,txt_modeofPayment;
    private  TextView txt_exp_description;
    private String dbname;

    private AppCompatButton bt_add_remaining;

    private String AmountPaid,Balance,catId;
    private String expId;
    private JSONArray itemData;
    private ArrayList<ExpensesListItems> iLists=new ArrayList<>();
    private static final String TAG = "SelectedExpensiveDetail";
    private RecyclerView recyclerView;
    private ItemAdapterListView selectedItemAdapter;

    private AppCompatButton viewImages;
    private String imageCode;
    private String TotalAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expensive_detials);
        txt_exp_type=(TextView)findViewById(R.id.exp_Type);
        txt_exp_date=(TextView)findViewById(R.id.exp_date);
        txt_exp_name=(TextView)findViewById(R.id.exp_name);

        txt_exp_qty=(TextView)findViewById(R.id.exp_qty);
        txt_totalGst=(TextView)findViewById(R.id.exp_gst);
        txt_amountPaid=(TextView)findViewById(R.id.exp_paid);
        txt_balance=(TextView)findViewById(R.id.exp_balance);
        txt_totalAmount=(TextView)findViewById(R.id.exp_totalAmount);
        txt_otherCharges=(TextView)findViewById(R.id.exp_otherCharges);
        txt_refNumber=(TextView)findViewById(R.id._exp_refNumber);
        txt_exp_description=(TextView)findViewById(R.id.et_exp_description);
        txt_subtotal=(TextView)findViewById(R.id.exp_sub_total);
        txt_modeofPayment=(TextView)findViewById(R.id.exp_modeofpayment);
        bt_add_remaining=(AppCompatButton)findViewById(R.id.exp_addRemaining);
        recyclerView=(RecyclerView)findViewById(R.id.item_recyclerView);
        bt_add_remaining.setOnClickListener(this);
        viewImages=(AppCompatButton) findViewById(R.id.image_view);
        viewImages.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent=new Intent(SelectedExpensiveDetails.this,GetExpenseImages.class);
                intent.putExtra("imageCode",imageCode);

               startActivity(intent);

            }
        });

        Intent intent=getIntent();
        if(intent.hasExtra("catId"))
        {
            catId=intent.getStringExtra("catId");
            txt_exp_type.setText(catId);
           txt_exp_date.setText(intent.getStringExtra("edate"));
            txt_exp_name.setText(intent.getStringExtra("expName"));
            txt_subtotal.setText(intent.getStringExtra("subTotal"));
            txt_totalGst.setText(intent.getStringExtra("totalGst"));
            txt_exp_qty.setText(intent.getStringExtra("totalQty"));
            txt_otherCharges.setText(intent.getStringExtra("otherCharges"));
           txt_totalAmount.setText(intent.getStringExtra("totalAmt"));
           TotalAmount= intent.getStringExtra("totalAmt");

           AmountPaid= intent.getStringExtra("amountPaid");
            Balance=intent.getStringExtra("balance");
            imageCode=intent.getStringExtra("imageCode");
            txt_amountPaid.setText(AmountPaid);
           txt_balance.setText(Balance);
           txt_modeofPayment.setText(intent.getStringExtra("paymentMode"));
           txt_refNumber.setText(intent.getStringExtra("refNo"));
           txt_exp_description.setText(intent.getStringExtra("description"));
            intent.getStringExtra("createdDate");
            intent.getStringExtra("createdBy");
           dbname= intent.getStringExtra("dbname");
            expId=intent.getStringExtra("expid");
            getItemDetails(expId,dbname);



        }

    }

    private void getItemDetails(final String expId, final String dbname)
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_EXP_BY_P_ID, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try {
                    Log.d("response", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    if (message.equalsIgnoreCase("Data available"))
                    {
                        itemData = jsonObject.getJSONArray("records");
                        displayData(itemData);

                    }

                }catch (Exception e) {
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
                    Toast.makeText(SelectedExpensiveDetails.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("expid",expId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void displayData(JSONArray itemData)
    {
        Log.d(TAG, "displayItem:"+itemData);
        iLists=new ArrayList<>();
        for(int i=0;i<itemData.length();i++)
        {
            try
            {
                ExpensesListItems itemList=new ExpensesListItems();
                JSONObject jsonObject =itemData.getJSONObject(i);


                itemList.setItemName(jsonObject.getString("name"));
                itemList.setUnitPrice(jsonObject.getString("unit_price"));
                itemList.setTotalPrice(jsonObject.getString("total"));
                itemList.setQty(jsonObject.getString("qty"));
                iLists.add(itemList);


            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        selectedItemAdapter  = new ItemAdapterListView(iLists, SelectedExpensiveDetails.this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(selectedItemAdapter);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.exp_addRemaining:
                if(Double.parseDouble(Balance)>0) {
                    Intent intent = new Intent(SelectedExpensiveDetails.this, Exp_Add_RemainingAmount.class);
                    intent.putExtra("Amount", AmountPaid);
                    intent.putExtra("Balance", Balance);
                    intent.putExtra("catId", catId);
                    intent.putExtra("dbname", dbname);
                    intent.putExtra("exp_id", expId);
                    intent.putExtra("totalAmount",TotalAmount);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(getApplicationContext(),"Amount Already Paid",Toast.LENGTH_LONG).show();
                }
                break;
        }

    }
}

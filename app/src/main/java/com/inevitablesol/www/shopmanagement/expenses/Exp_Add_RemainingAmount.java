package com.inevitablesol.www.shopmanagement.expenses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Exp_Add_RemainingAmount extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_amountPaid,txt_balance;
    private TextInputEditText et_paid;
    private AppCompatButton bt_save;
    private String dbname,exp_id,amount,balance;
    private static final String TAG = "Exp_Add_RemainingAmount";
    private String totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_remainning);
        txt_amountPaid=(TextView)findViewById(R.id.exp_remaining_paid);
        txt_balance=(TextView)findViewById(R.id.exp_remaining_totalBalance);
        et_paid=(TextInputEditText)findViewById(R.id.newPaidAmount);
        bt_save=(AppCompatButton)findViewById(R.id.exp_changes);
        bt_save.setOnClickListener(this);
        Intent intent=getIntent();
        if(intent.hasExtra("exp_id"))
        {
            amount=intent.getStringExtra("Amount");
            balance=intent.getStringExtra("Balance");
            dbname=intent.getStringExtra("dbname");
            totalAmount=intent.getStringExtra("totalAmount");
            txt_amountPaid.setText(amount);
            txt_balance.setText(balance);
            exp_id=intent.getStringExtra("exp_id");
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.exp_changes:
                String paidAmount=et_paid.getText().toString().trim();

                if(paidAmount.length()>0 && Double.parseDouble(paidAmount)<=Double.parseDouble(balance))
                {
                      double totalpaid=Double.parseDouble(amount)+Double.parseDouble(paidAmount);
                       double balance=Double.parseDouble(totalAmount)-totalpaid;
                    getDetails(String.valueOf(totalpaid),balance);
                }
                else
                {
                    Toast.makeText(this, "Please Add  valid Amount", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    private void getDetails(final String paidAmount, final double balance)
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.ADD_REMAININGAMOUNT, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try {
                    Log.d("response",response);
                    JSONObject msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if(message.equalsIgnoreCase("Amount Updated"))
                    {
                        Intent intent=new Intent(Exp_Add_RemainingAmount.this,View_expense.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                }catch (JSONException e)
                {

                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(Exp_Add_RemainingAmount.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("expid", exp_id);
                params.put("paid_amt",paidAmount);
                params.put("total_bal", String.valueOf(balance));
                Log.d(TAG, "getParams: "+params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}

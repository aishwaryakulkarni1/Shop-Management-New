package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.Billing_paidAmountSuccess;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.pddstudio.urlshortener.URLShortener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class DuePayment_BhimDetails extends AppCompatActivity
{
    private EditText tran_numbner;
    private String trans_num;
    private String invoiceId;
    private String charges;
    private String MOFPayment_shortCut;

    private  String taxableValue,total_gst,shipping_charges,totalAmount,amount_paid,balanceDue,ModeofPayment,other_charges;
    private String custMobile;
    private String invId;

    private String data;
    private String dbname;
    private SqlDataBase sqlDataBase;

    private Context context=DuePayment_BhimDetails.this;

    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    private String email;
    private static final String TAG = "DuePayment_BhimDetails";
    private AppCompatButton bt_done;
    private GlobalPool globalPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_payment__bhim_details);
        tran_numbner=(EditText)findViewById(R.id.transaction_number);
        bt_done=(AppCompatButton)findViewById(R.id.payment_done);
        Intent intent = getIntent();
        if (intent.hasExtra("balanceDue"))
        {
            amount_paid = intent.getStringExtra("amountpaid");
            balanceDue = intent.getStringExtra("balanceDue");
            ModeofPayment = intent.getStringExtra("totalAmount");
            totalAmount = intent.getStringExtra("totalAmount");
            ModeofPayment = intent.getStringExtra("modeofPayment");
            custMobile = intent.getStringExtra("custMobile");
            data = intent.getStringExtra("data");
            dbname = intent.getStringExtra("dbname");
            email=intent.getStringExtra("email");
            invoiceId=intent.getStringExtra("invoice_id");
            charges=intent.getStringExtra("charges");
            MOFPayment_shortCut=intent.getStringExtra("shortPmode");


        }

        sqlDataBase = new SqlDataBase(context);

        bt_done.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                updateInvoiceData();

                Log.d(TAG, "onClick: ");
            }
        });

        globalPool = (GlobalPool) this.getApplication();
    }

    private void updateInvoiceData()
    {
        trans_num =tran_numbner.getText().toString().trim();

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(DuePayment_BhimDetails.this).add(new StringRequest(Request.Method.POST, WebApi.INVIOCEAMOUNTPAIDHISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    Log.d(TAG, "onResponse: EMI" + msg);
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    } else
                    {
                        try
                        {
                             sendMessage(invId);

                            invId = msg.getString("paymentId");

                            Log.d("MSG", message);
                            if (message.equalsIgnoreCase("Updated succesfully"))
                            {


                                Intent intent = new Intent(DuePayment_BhimDetails.this, Billing_paidAmountSuccess.class);

                                intent.putExtra("amountpaid",amount_paid);
                                intent.putExtra("balanceDue",balanceDue);
                                intent.putExtra("totalAmount",totalAmount);
                                intent.putExtra("modeofPayment",ModeofPayment);
                                intent.putExtra("transactionId",trans_num);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            } else {

                                Toast.makeText(getApplicationContext(), "Cant Add Data", Toast.LENGTH_LONG).show();
                            }
                        }catch (NullPointerException e) {

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(DuePayment_BhimDetails.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("invoice_id", invoiceId);
                params.put("mode", ModeofPayment);
                params.put("amount_paid", amount_paid);
                params.put("transno", trans_num);
                params.put("shortPmode", MOFPayment_shortCut);
                params.put("charges", "0");
                Log.d("getParams:InvocieByID", params.toString());
                return params;
            }


        });
    }


    public void sendMessage(final String invId)
    {

        String longUrl= "http://shopmanagment.surge.sh/Shopnotify/?dbname="+globalPool.getDbname()+"&invoice_id="+invId;
        URLShortener.shortUrl(longUrl, new URLShortener.LoadingCallback() {
            @Override
            public void startedLoading() {

            }

            @Override
            public void finishedLoading(@Nullable String shortUrl)
            {
                shortUrl=shortUrl;
                //make sure the string is not null
                Log.d(TAG, "finishedLoading: Link"+shortUrl);
                String message = "Dear Customer, \n" +
                        " Thanks for Your Business \n" +
                        " of Total Amount : Rs"+ totalAmount + " \n  Invoice No : "+invId +"  Invoice Link :"+shortUrl+
                        " \n  Please Visit us again ! \n "+globalPool.getShopName() ;
                String mobile = custMobile;

                String uri = Uri.parse("\n" +
                        "http://bhashsms.com/api/sendmsg.php?")
                        .buildUpon()
                        .appendQueryParameter("user", "TEAM_MHOURZ")
                        .appendQueryParameter("pass", "MECHATRON")
                        .appendQueryParameter("text", message)
                        .appendQueryParameter("sender", "MHOURZ")
                        .appendQueryParameter("phone", mobile)
                        .appendQueryParameter("priority", "ndnd")
                        .appendQueryParameter("stype", "normal")
                        .build().toString();

                Log.d(TAG, "TestMEssage: Uri"+uri);
                final Context context = getApplicationContext();
                StringRequest stringRequest = new StringRequest(uri,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                try
                                {
                                    if (response != null)
                                    {
                                        Log.d(TAG, "onResponse: Sms new "+response);

                                        if (response.contains("S."))
                                        {
                                            Toast.makeText(getApplication(), "message sent successfully", Toast.LENGTH_LONG).show();
                                        } else
                                        {
                                            Toast.makeText(getApplication(), "Message couldn't reach you, try again", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                } catch (Exception e) {

                                }


                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);

            }
        });

    }
}

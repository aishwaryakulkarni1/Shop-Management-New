package com.inevitablesol.www.shopmanagement.Quotation;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.DateFormat.ParseDate;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.billing_module.Invoice_Details;
import com.inevitablesol.www.shopmanagement.billing_module.Invoice_item_list;
import com.inevitablesol.www.shopmanagement.billing_module.Invoice_list_recycler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Quotation_Details extends AppCompatActivity implements View.OnClickListener
{

  private   TextView tx_name,txt_email,tx_mobile;
   private TextView tx_totalQty;
    TextView  tx_invNumber,tx_invDate;
    private  TextView tx_taxableValue,tx_totalAmount;
    private  TextView tx_gst,tx_amountPaid,tx_balanceDue;

    private RecyclerView recyclerView;
    AppCompatButton bt_addRemaining;
    private static final String TAG = "Invoice_Details";
    private String dbname;
    private String GET_ITEMS=" http://35.161.99.113:9000/webapi/quotation/itemQt";
    private JSONArray jsonArray;
    private int totalQty;
    private ImageView img_call,img_mail,img_sms;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private  String mobile,email;
    private String q_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation__details);
        tx_name=(TextView)findViewById(R.id.q_h_name);
        txt_email=(TextView)findViewById(R.id.q_h_email);
        tx_mobile=(TextView)findViewById(R.id.q_h_mobile);
        tx_invNumber=(TextView)findViewById(R.id.q_h_numer);
        tx_totalQty=(TextView)findViewById(R.id.q_totalQty);
        tx_invDate=(TextView)findViewById(R.id.q_h_datetime);
        tx_taxableValue=(TextView)findViewById(R.id.q_h_taxValue);
//        tx_amountPaid=(TextView)findViewById(R.id.q_amountpaid);
//        tx_balanceDue=(TextView)findViewById(R.id.q_h_balancedue);
        tx_gst=(TextView)findViewById(R.id.q_h_gst);
        tx_totalAmount=(TextView)findViewById(R.id.q_h_totalAmount);
        recyclerView=(RecyclerView)findViewById(R.id.q_h_recycle);
        img_call=(ImageView)findViewById(R.id.q_call);
        img_mail=(ImageView)findViewById(R.id.q_mail);
        img_sms=(ImageView)findViewById(R.id.q_sms);
        img_call.setOnClickListener(this);
        img_mail.setOnClickListener(this);
        img_sms.setOnClickListener(this);

//        bt_addRemaining=(AppCompatButton)findViewById(R.id.q_h_addremaining);
//        bt_addRemaining.setOnClickListener(this);

        Intent intent=getIntent();
        if(intent.hasExtra("Q_id"))
        {
             q_id=intent.getStringExtra("Q_id");
            //tx_totalQty.setText("10");
            // dbname=intent.getStringExtra("dbname");
            mobile=intent.getStringExtra("mobile");
            email=intent.getStringExtra("email");
//            String inv_id=intent.getStringExtra("in_id");
           tx_name.setText(intent.getStringExtra("name"));
                 tx_mobile.setText(intent.getStringExtra("mobile"));
               tx_invDate.setText(ParseDate.geDate(intent.getStringExtra("date")));
            txt_email.setText(intent.getStringExtra("email"));
           tx_taxableValue.setText(intent.getStringExtra("taxableValue"));
           // tx_amountPaid.setText(intent.getStringExtra("amountpaid"));
           tx_gst.setText(intent.getStringExtra("gst"));
         // tx_balanceDue.setText(intent.getStringExtra("balanceDue"));
           tx_invNumber.setText(q_id);
          tx_totalAmount.setText(intent.getStringExtra("Amount"));
            dbname=intent.getStringExtra("dbname");
            Log.d(TAG, "onCreate: Invoice Id"+intent.getStringExtra("in_id"));

            _getItemDetails(q_id,dbname);
        }

    }

    private void _getItemDetails(final String inv_id, final String dbname)
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(Quotation_Details.this).add(new StringRequest(Request.Method.POST, GET_ITEMS, new Response.Listener<String>() {
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
                            //   Log.d(TAG, "onResponse:");
                            JSONObject jsonObject=new JSONObject(response);
                            jsonArray= jsonObject.getJSONArray("records");
//
//                            itemData=jsonObject.getJSONArray("itemdata");
                            displayData(jsonArray);

                            //loop through each object

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
                    Toast.makeText(Quotation_Details.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("qt_id",inv_id);

                Log.d(TAG, "getParams:GETINVOICE item Array"+params.toString());
                return params;
            }


        });
    }

    private void displayData(JSONArray jsonArray)
    {
        int i=0;
        totalQty=0;
        Log.d(TAG, "displayData: "+jsonArray);
        ArrayList<Invoice_item_list> list=new ArrayList<Invoice_item_list>();

        while (i<jsonArray.length())
        {
            try {

                Invoice_item_list  invoice_item_list=new Invoice_item_list();
                invoice_item_list.setItemName(jsonArray.getJSONObject(i).getString("item_name"));
                invoice_item_list.setItemQty(jsonArray.getJSONObject(i).getString("qty"));
                invoice_item_list.setItemPrice(jsonArray.getJSONObject(i).getString("total_price"));
                list.add(invoice_item_list);
                totalQty +=Integer.parseInt(jsonArray.getJSONObject(i).getString("qty"));
                i++;
                Log.d(TAG, "displayData: I"+i);
                Log.d(TAG, "displayData: List"+list.toString());
            } catch (JSONException e)
            {
                e.printStackTrace();
            }


        }


        Invoice_list_recycler view_selectedList=new Invoice_list_recycler(list,Quotation_Details.this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(view_selectedList);
        Log.d(TAG, "displayData: Qty"+totalQty);
        this. tx_totalQty.setText(String.valueOf(totalQty));

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case  R.id.inv_h_addremaining:
                add_Payment();
                break;
            case  R.id.q_call:
                callToCustomer();
                break;
            case  R.id.q_mail:
                sendMail();
                break;
            case R.id.q_sms:
                sendSms();
                break;
            default:
                Toast.makeText(this, "Wrong Chooice", Toast.LENGTH_SHORT).show();


        }



    }

    private void sendSms()
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mobile));
        intent.putExtra("sms_body", "hi");
        startActivity(intent);
    }

    private void sendMail()
    {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, " ");
        i.putExtra(Intent.EXTRA_TEXT   , "  ");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(Quotation_Details.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callToCustomer()
    {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else
        {
            callPhone();
        }



    }


    private void callPhone()
    {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mobile));


        if (ActivityCompat.checkSelfPermission(Quotation_Details.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        startActivity(callIntent);
    }


    private void add_Payment()
    {
        Log.d(TAG, "add_Payment: ");


//        String paidAmount=.getText().toString().trim();
//        if(paidAmount.length()>0 &&!paidAmount.isEmpty())
//        {
//            Intent intent=new Intent(context,SelectModeOFPaymnet.class);
//
//            intent.putExtra("in_id",);
//            intent.putExtra("c_name",cName);
//            intent.putExtra("email",c_mail);
//            intent.putExtra("mobile",c_mobile);
//            intent.putExtra("c_id",c_id);
//            intent.putExtra("amount",amount);
//            intent.putExtra("payment_id",payment_id);
//            intent.putExtra("status",status);
//            intent.putExtra("description",desrciption);
//            intent.putExtra("modeOfPayment",modeOfpayment);
//            intent.putExtra("dateTime",dateTime);
//            intent.putExtra("paidAmount",paidAmount);
//            intent.putExtra("balanceDue",balanceDue);
//            intent.putExtra("dbname",dbname);
//            startActivity(intent);
//
//
//        }else
//        {
//            Toast.makeText(context, "Select Amount", Toast.LENGTH_SHORT).show();
//        }
//        break;
//        default:
//        Toast.makeText(context, "Wrong Chooice", Toast.LENGTH_SHORT).show();
//        break;
    }
}

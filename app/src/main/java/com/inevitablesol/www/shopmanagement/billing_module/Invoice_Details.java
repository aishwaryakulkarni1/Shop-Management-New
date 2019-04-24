package com.inevitablesol.www.shopmanagement.billing_module;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
import com.inevitablesol.www.shopmanagement.DateFormat.ParseDate;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.purchase_module.PurchaseView_listItem;
import com.inevitablesol.www.shopmanagement.purchase_module.View_SelectedList;
import com.inevitablesol.www.shopmanagement.vendor_module.SelectedVendorDetails;
import com.inevitablesol.www.shopmanagement.wishList.DatePick;
import com.pddstudio.urlshortener.URLShortener;

import org.apache.poi.ss.formula.functions.T;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

import static com.inevitablesol.www.shopmanagement.customer_module.CustomerParser.cust_Name;
import static com.inevitablesol.www.shopmanagement.customer_module.CustomerParser.mobile;

public class Invoice_Details extends AppCompatActivity implements View.OnClickListener {
    TextView tx_name,txt_email,tx_mobile;
    TextView tx_totalQty;
    TextView  tx_invNumber,tx_invDate;
    private  TextView tx_taxableValue,tx_totalAmount;
    private  TextView tx_gst,tx_amountPaid,tx_balanceDue;

    private RecyclerView recyclerView;
    AppCompatButton bt_addRemaining;
    private static final String TAG = "Invoice_Details";
    private String dbname;
    private String GET_ITEMS="http://35.161.99.113:9000/webapi/invoice/itemInvoice";
    private JSONArray jsonArray;
    private int totalQty;
    private ImageView img_call,img_mail,img_sms;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private  String mobile,email;
    private String inv_id;
    private String cname;

    private  TextView txBalnce,txDate,txpaid,txtotal;
    private AppCompatButton paymentMode;
 private   InvoiceRecord invoiceRecord;
 private Context context=Invoice_Details.this;

    private TextView et_ifc, et_accountNumber, et_address;
    private TextView et_bankName;
    private TextView et_holderName;
    private String paymentID;
    private TextView txtcompanyName;
    private TextView txtRateof_interate;
    private TextView txtpancard;
    private TextView txttimebond;
    private TextView txtPolicyYear;
    private TextView txtdate_emi;
    private TextView txbankName;
    private TextView txname;
    private TextView typeofPayment;
    private TextView txtchechqueNumber;
    private TextView txtchequeDate;
    private GlobalPool globalPool;
    private TextView txnamecheque;
    private TextView txbankNameCheque;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice__details);
        tx_name=(TextView)findViewById(R.id.inv_h_name);
        txt_email=(TextView)findViewById(R.id.inv_h_email);
        tx_mobile=(TextView)findViewById(R.id.inv_h_mobile);
        tx_invNumber=(TextView)findViewById(R.id.inv_h_numer);
        tx_totalQty=(TextView)findViewById(R.id.inv_totalQty);
        tx_invDate=(TextView)findViewById(R.id.inv_h_datetime);
         tx_taxableValue=(TextView)findViewById(R.id.inv_h_taxValue);
         tx_amountPaid=(TextView)findViewById(R.id.inv_amountpaid);
         tx_balanceDue=(TextView)findViewById(R.id.inv_h_balancedue);
          tx_gst=(TextView)findViewById(R.id.inv_h_gst);
        tx_totalAmount=(TextView)findViewById(R.id.inv_h_totalAmount);
          recyclerView=(RecyclerView)findViewById(R.id.inv_h_recycle);
        img_call=(ImageView)findViewById(R.id.inv_call);
        img_mail=(ImageView)findViewById(R.id.inv_mail);
        img_sms=(ImageView)findViewById(R.id.inv_sms);
        txDate=(TextView)findViewById(R.id.txdate);
        txBalnce=(TextView)findViewById(R.id.txbalance);
        txpaid=(TextView)findViewById(R.id.txpaid);
        txtotal=(TextView)findViewById(R.id.txtotal);
        ButterKnife.bind(this);
        img_call.setOnClickListener(this);
        img_mail.setOnClickListener(this);
        img_sms.setOnClickListener(this);
        paymentMode=(AppCompatButton)findViewById(R.id.payment_mode);

        paymentMode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String mode=invoiceRecord.getModeOfPayment();
                Log.d(TAG, "onClick: mode"+mode);
                switch (mode)
                {
                    case "Cash payment":
                        showCash();
                        break;
                    case "Send Payment Gatway Link":
                        showCash();
                        break;
                    case "BHIM UPI App":
                        showCash_bhim();
                        break;
                    case "Paytm Wallet ":
                        showPaytm();
                        break;
                    case "To Bank Account":
                        toBankAccount();
                        break;
                    case "EMI":
                        toemi_details();
                        break;
                    case "Cheque Payment":
                        tochequeDeatils();
                        break;
                    case "Razorpay":
                        RazorpayDeatils();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"Please refresh page",Toast.LENGTH_LONG).show();
                }
            }
        });
        globalPool = (GlobalPool) this.getApplication();

        bt_addRemaining=(AppCompatButton)findViewById(R.id.inv_h_addremaining);
         bt_addRemaining.setOnClickListener(this);

        Intent intent=getIntent();
        if(intent.hasExtra("in_id"))
        {
                   mobile=intent.getStringExtra("mobile");
                     email=intent.getStringExtra("c_email");
                      cname= intent.getStringExtra("c_name");
            inv_id=intent.getStringExtra("in_id");
            tx_name.setText(cname);
            tx_mobile.setText(intent.getStringExtra("mobile"));
            tx_invDate.setText(ParseDate.geDate(intent.getStringExtra("dateTime")));
            txt_email.setText(intent.getStringExtra("c_email"));
            tx_taxableValue.setText(intent.getStringExtra("taxableValue"));
            tx_amountPaid.setText(intent.getStringExtra("amountpaid"));
            tx_gst.setText(intent.getStringExtra("total_gst"));
            tx_balanceDue.setText(intent.getStringExtra("balanceDue"));
            tx_invNumber.setText(intent.getStringExtra("in_id"));
            tx_totalAmount.setText(intent.getStringExtra("total"));
            dbname=intent.getStringExtra("dbname");
             invoiceRecord= (InvoiceRecord) intent.getSerializableExtra("invObject");
            txtotal.setText(invoiceRecord.getTotal());
            txpaid.setText(invoiceRecord.getAmount());
            txBalnce.setText(invoiceRecord.getBalance());
            txDate.setText( ParseDate.geDate(invoiceRecord.getDateTime()));
            Log.d(TAG, "onCreate: Inv Object"+invoiceRecord);

            Log.d(TAG, "onCreate: Invoice Id"+intent.getStringExtra("in_id"));

            _getItemDetails(inv_id,dbname);

        }



    }

    private void RazorpayDeatils()
    {
        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.show_payment_details);

        TextView    txDate=(TextView)dialog.findViewById(R.id.txdate);
        TextView txBalnce=(TextView)dialog.findViewById(R.id.txbalance);
        TextView txpaid=(TextView)dialog.findViewById(R.id.txpaid);
        TextView  txtotal=(TextView)dialog.findViewById(R.id.txtotal);
        TextView transactionNumber=(TextView)dialog.findViewById(R.id.transaction_ref);
        TextView  paymode=(TextView)dialog.findViewById(R.id.paymentMode);
        txtotal.setText(invoiceRecord.getTotal());
        txpaid.setText(invoiceRecord.getAmount());
        txBalnce.setText(invoiceRecord.getBalance());
        txDate.setText( ParseDate.geDate(invoiceRecord.getDateTime()));

        transactionNumber.setText(String.valueOf(invoiceRecord.getTransno()));
        paymode.setText(invoiceRecord.getModeOfPayment());
        Log.d(TAG, "onCreate: Inv Object"+invoiceRecord);
        dialog.show();

    }

    private void tochequeDeatils()
    {
        getCheckDetail();
        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.show_payment_chequedetails);

        TextView    txDate=(TextView)dialog.findViewById(R.id.txdate);
        TextView txBalnce=(TextView)dialog.findViewById(R.id.txbalance);
        TextView txpaid=(TextView)dialog.findViewById(R.id.txpaid);
        TextView  txtotal=(TextView)dialog.findViewById(R.id.txtotal);
        TextView transactionNumber=(TextView)dialog.findViewById(R.id.transaction_number);
        TextView  paymode=(TextView)dialog.findViewById(R.id.paymentMode);

          txbankNameCheque=(TextView)dialog.findViewById(R.id.bankName);
          txnamecheque =(TextView)dialog.findViewById(R.id.name);
          typeofPayment=(TextView)dialog.findViewById(R.id.typeofpayment);
          txtchechqueNumber=(TextView)dialog.findViewById(R.id.cheque_number);
           txtchequeDate=(TextView)dialog.findViewById(R.id.date);
        txtotal.setText(invoiceRecord.getTotal());
        txpaid.setText(invoiceRecord.getAmount());
        txBalnce.setText(invoiceRecord.getBalance_due());
        txDate.setText( ParseDate.geDate(invoiceRecord.getDateTime()));

       transactionNumber.setText(invoiceRecord.getTransno());
        paymode.setText(invoiceRecord.getModeOfPayment());
        Log.d(TAG, "onCreate: Inv Object"+invoiceRecord);
        dialog.show();

    }

    private void toemi_details()
    {
        getEMIDetails();
        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.show_payment_emidetails);

        TextView    txDate=(TextView)dialog.findViewById(R.id.txdate);
        TextView txBalnce=(TextView)dialog.findViewById(R.id.txbalance);
        TextView txpaid=(TextView)dialog.findViewById(R.id.txpaid);
        TextView  txtotal=(TextView)dialog.findViewById(R.id.txtotal);
       TextView transactionNumber=(TextView)dialog.findViewById(R.id.transaction_number);
        TextView  paymode=(TextView)dialog.findViewById(R.id.paymentMode);
        txtotal.setText(invoiceRecord.getTotal());
        txpaid.setText(invoiceRecord.getAmount());
        txBalnce.setText(invoiceRecord.getBalance_due());
        txDate.setText( ParseDate.geDate(invoiceRecord.getDateTime()));
         txtcompanyName=(TextView)dialog.findViewById(R.id.txt_view_company);
          txtRateof_interate=(TextView)dialog.findViewById(R.id.txt_view_rateofinterest);
           txtpancard=(TextView)dialog.findViewById(R.id.txt_pancard);
          txttimebond=(TextView)dialog.findViewById(R.id.txt_timebond);
        txtPolicyYear=(TextView)dialog.findViewById(R.id.txt_policyyear);
         txtdate_emi=(TextView)dialog.findViewById(R.id.txtdate);


        transactionNumber.setText(String.valueOf(invoiceRecord.getTransno()));
        paymode.setText(invoiceRecord.getModeOfPayment());
        Log.d(TAG, "onCreate: Inv Object"+invoiceRecord);
        dialog.show();

    }

    private void toBankAccount()
    {
        getBankDetail();
        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.show_payment_bankdetails);

        TextView    txDate=(TextView)dialog.findViewById(R.id.txdate);
        TextView txBalnce=(TextView)dialog.findViewById(R.id.txbalance);
        TextView txpaid=(TextView)dialog.findViewById(R.id.txpaid);
        TextView  txtotal=(TextView)dialog.findViewById(R.id.txtotal);
        TextView transactionNumber=(TextView)dialog.findViewById(R.id.transaction_number);
        TextView  paymode=(TextView)dialog.findViewById(R.id.paymentMode);
        txtotal.setText(invoiceRecord.getTotal());
        txpaid.setText(invoiceRecord.getAmount());
        et_accountNumber=(TextView)dialog.findViewById(R.id.accountNumber);
        et_address=(TextView)dialog.findViewById(R.id.address);
        et_bankName=(TextView)dialog.findViewById(R.id.bankName);
        et_ifc=(TextView)dialog.findViewById(R.id.ifscNumber);
        et_holderName=(TextView)dialog.findViewById(R.id.name);
        txBalnce.setText(invoiceRecord.getBalance());
        txDate.setText(ParseDate.geDate(invoiceRecord.getDateTime()));

        transactionNumber.setText(invoiceRecord.getTransno());
        paymode.setText(invoiceRecord.getModeOfPayment());
        Log.d(TAG, "onCreate: Inv Object"+invoiceRecord);

        dialog.show();

    }

    private void getBankDetail()
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.VIEWACCOUNT_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {


                String resp = response.toString().trim();

                Log.d("Bank Deatls", resp);
                JSONObject jsonObject = null;
                try {
                    loading.dismiss();
                    JSONObject json = new JSONObject(resp);
                    JSONArray jsonArray = json.getJSONArray("records");
                    jsonObject = jsonArray.getJSONObject(0);
                    String bankname = jsonObject.getString("bankname");
                    String accountnumber = jsonObject.getString("accno");
                    String accountname = jsonObject.getString("accountname");
                    String address = jsonObject.getString("address");
                    String ifsc = jsonObject.getString("ifsc");


                    et_accountNumber.setText(accountnumber);
                    et_ifc.setText(ifsc);
                    et_holderName.setText(accountname);
                    et_bankName.setText(bankname);
                    et_address.setText(address);


                } catch (JSONException e)
                {
                    Log.d("Exception", "" + e);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getCheckDetail()
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETCHECK_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {


                String resp = response.toString().trim();

                Log.d("Cheque Deatls", resp);
                JSONObject jsonObject = null;
                try {
                    loading.dismiss();
                 JSONObject json = new JSONObject(resp);
                    JSONArray jsonArray = json.getJSONArray("transactionRecord");
                    jsonObject = jsonArray.getJSONObject(0);
                    Log.d(TAG, "onResponse: "+jsonObject);
//                     txbankNameCheque.setText(jsonObject.getString("book_name"));
                      txnamecheque.setText(jsonObject.getString("name"));
                      typeofPayment.setText(jsonObject.getString("type_of_payment"));
                        txtchequeDate.setText(ParseDate.geDate(jsonObject.getString("created_date")));

                    txtchechqueNumber.setText(jsonObject.getString("cheque_no"));
                  //  String ifsc = jsonObject.getString("ifsc");
//
//
//                    et_accountNumber.setText(accountnumber);
//                    et_ifc.setText(ifsc);
//                    et_holderName.setText(accountname);
//                    et_bankName.setText(bankname);
//                    et_address.setText(address);


                } catch (JSONException e)
                {
                    Log.d("Exception", "" + e);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                params.put("payment_id", String.valueOf(invoiceRecord.getPaymentId()));

                Log.d(TAG, "getParams: Cheque"+params.toString());
                Log.d("payment_id", String.valueOf(invoiceRecord.getPaymentId()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void getEMIDetails() {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETEMIDETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {


                String resp = response.toString().trim();

                Log.d("Bank Deatls", resp);
                JSONObject jsonObject = null;
                try {
                    loading.dismiss();
                    JSONObject json = new JSONObject(resp);
                    JSONArray jsonArray = json.getJSONArray("transactionRecord");
                    jsonObject = jsonArray.getJSONObject(0);
                    txtcompanyName.setText(jsonObject.getString("company_name"));
                    txtRateof_interate.setText(jsonObject.getString("rate_of_interest"));
                    txttimebond.setText(jsonObject.getString("time_of_bound"));
                   txtpancard.setText( jsonObject.getString("pan_card_no"));
                    txtdate_emi.setText(ParseDate.geDate(jsonObject.getString("created_date")));
                    txtPolicyYear.setText(jsonObject.getString("policy_no"));
//
//

                } catch (JSONException e)
                {
                    Log.d("Exception", "" + e);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                params.put("payment_id", String.valueOf(invoiceRecord.getPaymentId()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showPaytm()
    {

        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.show_payment_details);
        TextView    txDate=(TextView)dialog.findViewById(R.id.txdate);
        TextView txBalnce=(TextView)dialog.findViewById(R.id.txbalance);
        TextView txpaid=(TextView)dialog.findViewById(R.id.txpaid);
        TextView  txtotal=(TextView)dialog.findViewById(R.id.txtotal);
        TextView transactionNumber=(TextView)dialog.findViewById(R.id.transaction_ref);
        TextView  paymode=(TextView)dialog.findViewById(R.id.paymentMode);
        txtotal.setText(invoiceRecord.getTotal());
        txpaid.setText(invoiceRecord.getAmount());
        txBalnce.setText(invoiceRecord.getBalance());
        txDate.setText( ParseDate.geDate(invoiceRecord.getDateTime()));
        transactionNumber.setText(invoiceRecord.getTransno());
        paymode.setText(invoiceRecord.getModeOfPayment());
        Log.d(TAG, "onCreate: Inv Object"+invoiceRecord);

        dialog.show();
    }

    private void showCash()
    {

        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.show_payment_details);

        TextView    txDate=(TextView)dialog.findViewById(R.id.txdate);
        TextView txBalnce=(TextView)dialog.findViewById(R.id.txbalance);
        TextView txpaid=(TextView)dialog.findViewById(R.id.txpaid);
        TextView  txtotal=(TextView)dialog.findViewById(R.id.txtotal);
        TextView transactionNumber=(TextView)dialog.findViewById(R.id.transaction_ref);
        TextView  paymode=(TextView)dialog.findViewById(R.id.paymentMode);
        txtotal.setText(invoiceRecord.getTotal());
        txpaid.setText(invoiceRecord.getAmount());
        txBalnce.setText(invoiceRecord.getBalance());
        txDate.setText( ParseDate.geDate(invoiceRecord.getDateTime()));

        transactionNumber.setText(String.valueOf(invoiceRecord.getTransno()));
        paymode.setText(invoiceRecord.getModeOfPayment());
        Log.d(TAG, "onCreate: Inv Object"+invoiceRecord);
        dialog.show();

    }

    private void showCash_bhim()
    {

        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.show_payment_details);

        TextView    txDate=(TextView)dialog.findViewById(R.id.txdate);
        TextView txBalnce=(TextView)dialog.findViewById(R.id.txbalance);
        TextView txpaid=(TextView)dialog.findViewById(R.id.txpaid);
        TextView  txtotal=(TextView)dialog.findViewById(R.id.txtotal);
        TextView transactionNumber=(TextView)dialog.findViewById(R.id.transaction_ref);
        TextView  paymode=(TextView)dialog.findViewById(R.id.paymentMode);
        txtotal.setText(invoiceRecord.getTotal());
        txpaid.setText(invoiceRecord.getAmount());
        txBalnce.setText(invoiceRecord.getBalance());
        txDate.setText( ParseDate.geDate(invoiceRecord.getDateTime()));

        transactionNumber.setText(String.valueOf(invoiceRecord.getTransno()));
        paymode.setText(invoiceRecord.getModeOfPayment());
        Log.d(TAG, "onCreate: Inv Object"+invoiceRecord);
        dialog.show();

    }

    private void _getItemDetails(final String inv_id, final String dbname)
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(Invoice_Details.this).add(new StringRequest(Request.Method.POST, GET_ITEMS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                JSONObject msg = null;
                try
                {

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
                    Toast.makeText(Invoice_Details.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("invoice_id",inv_id);
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


        Invoice_list_recycler  view_selectedList=new Invoice_list_recycler(list,Invoice_Details.this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(view_selectedList);
        Log.d(TAG, "displayData: Qty"+totalQty);
        this.tx_totalQty.setText(String.valueOf(totalQty));

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case  R.id.inv_h_addremaining:
                 add_Payment();
                break;
            case  R.id.inv_call:
                callToCustomer();
                break;
            case  R.id.inv_mail:
                sendMail();
                break;
            case R.id.inv_sms:
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
            Toast.makeText(Invoice_Details.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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


        if (ActivityCompat.checkSelfPermission(Invoice_Details.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        startActivity(callIntent);
    }


    private void add_Payment()
    {
        Log.d(TAG, "add_Payment: ");

        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.invoice_instatnt_payment);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 9);
         TextView tx_tAmount   =(TextView)dialog.findViewById(R.id.int_totalAmount);
        TextView tx_aPaid      =(TextView)dialog.findViewById(R.id.int_totalPaid);
        TextView tx_bDue      =(TextView)dialog.findViewById(R.id.int_balanceDue);
        AppCompatButton cancel=(AppCompatButton)dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        final TextInputEditText et_amount =(TextInputEditText)dialog.findViewById(R.id.int_amount);
        AppCompatButton bt_amount   =(AppCompatButton)dialog. findViewById(R.id.int_save);
        tx_tAmount.setText(tx_totalAmount.getText().toString().trim());
         tx_bDue.setText(tx_balanceDue.getText().toString().trim());
          tx_aPaid.setText(tx_amountPaid.getText().toString().trim());
              bt_amount.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v)
                  {
                       String amount=et_amount.getText().toString().trim();
                        double balanceDue=Double.parseDouble(tx_balanceDue.getText().toString().trim());
                           if(!amount.isEmpty() &&Double.parseDouble(amount)<=balanceDue)
                           {
                                 if(balanceDue== 0.0)
                                 {
                                     Toast.makeText(Invoice_Details.this, " Amount already Paid", Toast.LENGTH_SHORT).show();
                                 }else

                                 {
                                     _proceedToPayment(amount);
                                 }

                           }else
                           {
                               Toast.makeText(Invoice_Details.this, "Please enter valid  Amount", Toast.LENGTH_SHORT).show();
                           }

                  }
              });
        dialog.show();



    }

    private void _proceedToPayment(String amount)
    {
        Toast.makeText(this, "Amount: "+amount, Toast.LENGTH_SHORT).show();


        if(amount.length()>0 &&!amount.isEmpty())
        {

              if(cname.equalsIgnoreCase("instant"))
              {
                  updateInvoiceCOD(amount);
              }else
              {
                  Log.d(TAG, "_proceedToPayment: Cname"+cname);
                  Intent intent=new Intent(Invoice_Details.this,SelectModeOFPaymnet.class);
                  intent.putExtra("in_id",inv_id);
                  intent.putExtra("c_name",cname);
                  intent.putExtra("email",email);
                  intent.putExtra("mobile",mobile);
                  intent.putExtra("totalAmount",tx_totalAmount.getText().toString());
                  intent.putExtra("amount",tx_amountPaid.getText().toString());
                  intent.putExtra("paidAmount",String.valueOf(Math.round(Double.parseDouble(amount)*100.0)/100));
                  intent.putExtra("paymentID",invoiceRecord.getPaymentId());
                  intent.putExtra("balanceDue",tx_balanceDue.getText().toString().trim());
                  intent.putExtra("dbname",dbname);
                  startActivity(intent);
              }



        }else
        {
            Toast.makeText(getApplicationContext(), "Select Amount", Toast.LENGTH_SHORT).show();
        }



    }


    private void updateInvoiceCOD(final String amount)
    {


        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(Invoice_Details.this).add(new StringRequest(Request.Method.POST, WebApi.INVIOCEAMOUNTPAIDHISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject msg = null;
                try
                {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    Log.d(TAG, "onResponse:"+msg);
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    } else
                    {
                        try
                        {
                          //  sendMessage(inv_id);
                            loading.dismiss();
                            Intent intent=new Intent(Invoice_Details.this,Invoice_History.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();


                        } catch (NullPointerException e)
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
                    Toast.makeText(Invoice_Details.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("invoice_id",inv_id);
                params.put("mode","COD");
                params.put("shortPmode","COD");
                params.put("amount_paid",amount);
                params.put("transno","COD");
                Log.d("getParams:InvocieByID",params.toString());
                return params;
            }


        });
    }

//    public void sendMessage(final String invId)
//    {
//
//        String longUrl= "http://shopmanagment.surge.sh/Shopnotify/?dbname="+globalPool.getDbname()+"&invoice_id="+invId;
//        URLShortener.shortUrl(longUrl, new URLShortener.LoadingCallback() {
//            @Override
//            public void startedLoading() {
//
//            }
//
//            @Override
//            public void finishedLoading(@Nullable String shortUrl)
//            {
//                shortUrl=shortUrl;
//                //make sure the string is not null
//                Log.d(TAG, "finishedLoading: Link"+shortUrl);
//                String message = "Dear Customer, \n" +
//                        " Thanks for Your Business \n" +
//                        " of Total Amount : Rs"+  + " \n  Invoice No : "+invId +"  Invoice Link :"+shortUrl+
//                        " \n  Please Visit us again ! \n "+globalPool.getShopName() ;
//
//
//                String uri = Uri.parse("\n" +
//                        "http://bhashsms.com/api/sendmsg.php?")
//                        .buildUpon()
//                        .appendQueryParameter("user", "TEAM_MHOURZ")
//                        .appendQueryParameter("pass", "MECHATRON")
//                        .appendQueryParameter("text", message)
//                        .appendQueryParameter("sender", "MHOURZ")
//                        .appendQueryParameter("phone", mobile)
//                        .appendQueryParameter("priority", "ndnd")
//                        .appendQueryParameter("stype", "normal")
//                        .build().toString();
//
//                Log.d(TAG, "TestMEssage: Uri"+uri);
//                final Context context = getApplicationContext();
//                StringRequest stringRequest = new StringRequest(uri,
//                        new Response.Listener<String>()
//                        {
//                            @Override
//                            public void onResponse(String response)
//                            {
//                                try
//                                {
//                                    if (response != null)
//                                    {
//                                        Log.d(TAG, "onResponse: Sms new "+response);
//
//                                        if (response.contains("S."))
//                                        {
//                                            Toast.makeText(getApplication(), "message sent successfully", Toast.LENGTH_LONG).show();
//                                        } else
//                                        {
//                                            Toast.makeText(getApplication(), "Message couldn't reach you, try again", Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//
//                                } catch (Exception e) {
//
//                                }
//
//
//                            }
//                        },
//                        new Response.ErrorListener()
//                        {
//                            @Override
//                            public void onErrorResponse(VolleyError error)
//                            {
//                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        });
//
//                RequestQueue requestQueue = Volley.newRequestQueue(context);
//                requestQueue.add(stringRequest);
//
//            }
//        });
//
//    }


}

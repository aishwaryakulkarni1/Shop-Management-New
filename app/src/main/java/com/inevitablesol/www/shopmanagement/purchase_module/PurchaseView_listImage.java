package com.inevitablesol.www.shopmanagement.purchase_module;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PurchaseView_listImage extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PurchaseView_listImage";


    RadioGroup radioGroup;
    private TextView tx_taxable, tx_gstTotal, tx_shipping, tx_charges, tx_total, tx_amount, tx_balance, tx_reverseCharges, tx_paid;

    private AppCompatButton bt_payRemaning;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private String purchase_id;

    Integer integer;

    private static String UPDATE_PURCHASE="http://35.161.99.113:9000/webapi/purchase/updatepayment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_with_image_download);

        tx_taxable = (TextView) findViewById(R.id.p_subtotal);
        tx_gstTotal = (TextView) findViewById(R.id.p_gst_total);
        tx_shipping = (TextView) findViewById(R.id.p_shipping_charge);
        tx_amount = (TextView) findViewById(R.id.p_total);
        tx_balance = (TextView) findViewById(R.id.p_balance);
        tx_reverseCharges = (TextView) findViewById(R.id.p_reversecharge);
        tx_charges = (TextView) findViewById(R.id.p_other_charges);
        tx_paid = (TextView) findViewById(R.id.p_amountpaid);
       // radioGroup = (RadioGroup) findViewById(R.id.report_button);
        bt_payRemaning = (AppCompatButton) findViewById(R.id.pay_remaining);
        bt_payRemaning.setOnClickListener(this);
//        radioGroup.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));


        Intent intent = getIntent();
        if (intent.hasExtra("json")) {
            String jsonData = intent.getStringExtra("json");
            Log.d(TAG, "" + jsonData);
            try {
                JSONArray jsonObject = new JSONArray(jsonData);
                JSONObject jsonObject1 = jsonObject.getJSONObject(0);
                //  String purchase_id = jsonObject1.getString("purchase_id");
                String subtotal = jsonObject1.getString("subtotal");
                String gstToal = jsonObject1.getString("gst_total");
                String cgst = jsonObject1.getString("cgst");
                String sgst = jsonObject1.getString("sgst");
                String igst = jsonObject1.getString("igst");
                String shipping_charges = jsonObject1.getString("shipping_charges");
                String other_charge = jsonObject1.getString("other_charges");
                String totalAmount = jsonObject1.getString("total_amount");
                String amount_paid = jsonObject1.getString("amount_paid");
                String balance_due = jsonObject1.getString("balance_due");
                String rev_charges = jsonObject1.getString("reverse_changes");
                 purchase_id = jsonObject1.getString("purchase_id");

                tx_taxable.setText(subtotal);
                tx_gstTotal.setText(gstToal);
                tx_amount.setText(totalAmount);
                tx_paid.setText(amount_paid);
                tx_balance.setText(balance_due);
                tx_shipping.setText(shipping_charges);
                tx_charges.setText(other_charge);
                tx_reverseCharges.setText(rev_charges);

                //  Log.d(TAG, "onCreate:purchaseId"+purchase_id);

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
            }
        }

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                switch (checkedId) {
//                    case R.id.xls:
//                        //  makeReport();
//                        break;
//                }
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_remaining:
                addRemaining();
                break;
            default:
                Toast.makeText(this, "Invalid ", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void addRemaining() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pay_remaing_purchase);
        dialog.setTitle("Add item Details");
        AppCompatButton pay_remaining = (AppCompatButton) dialog.findViewById(R.id.pay_remaining);
        AppCompatButton cancel = (AppCompatButton) dialog.findViewById(R.id.pay_cancel);
        final TextView tx_total = (TextView) dialog.findViewById(R.id.pay_total);
        final TextView tbalance = (TextView) dialog.findViewById(R.id.pay_balance);
        final  TextView txt_apaid=(TextView)dialog.findViewById(R.id.pay_amountpaid);
        final TextInputEditText new_paidAmount = (TextInputEditText) dialog.findViewById(R.id.pay_paid_amount);
        final String totalAmount = tx_amount.getText().toString();
        final String paidAmount = tx_paid.getText().toString();
        final String balaceDue = tx_balance.getText().toString();
        tx_total.setText(totalAmount);
        txt_apaid.setText(paidAmount);

        tbalance.setText(balaceDue);

              new_paidAmount.addTextChangedListener(new TextWatcher() {
                  @Override
                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                  }

                  @Override
                  public void onTextChanged(CharSequence s, int start, int before, int count) {

                  }

                  @Override
                  public void afterTextChanged(Editable s)
                  {
                      try {
                          if(Double.parseDouble(s.toString())<=Double.parseDouble(balaceDue))
                          {

                          }else
                          {
                              new_paidAmount.setError("invalid Amount");
                          }
                      } catch (NumberFormatException e)
                      {
                          e.printStackTrace();
                      }


                  }
              });




            pay_remaining.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    try {
                        double newPaidAmount = Double.parseDouble(new_paidAmount.getText().toString().trim());
                        double  old_paidAmount=Double.parseDouble(paidAmount);
                        double  old_balanceDue=Double.parseDouble(balaceDue);
                        double new_totalAmountPaid=old_paidAmount+newPaidAmount;

                        double new_balanceDue=Double.parseDouble(totalAmount)-new_totalAmountPaid;
                        Log.d(TAG, "onClick: balance Due"+new_balanceDue);
                        Log.d(TAG, "onClick: totalAmount"+new_totalAmountPaid);

                        if(newPaidAmount>0 && old_balanceDue> 0.0)
                        {
                                   if(new_balanceDue<=old_balanceDue)
                                   {
                                       updateDueBalance(new_balanceDue,new_totalAmountPaid);
                                       dialog.dismiss();
                                   }else
                                   {
                                       Toast.makeText(PurchaseView_listImage.this, " Due Amount not be exceed", Toast.LENGTH_SHORT).show();
                                   }

                        }else if(old_balanceDue==0)
                        {

                            Toast.makeText(PurchaseView_listImage.this, "Amount Already Paid", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(PurchaseView_listImage.this, "Invalid Amount", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e)
                    {
                        e.printStackTrace();
                    }


                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

//             DisplayMetrics metrics = getResources().getDisplayMetrics();
//             int width = metrics.widthPixels;
//             int height = metrics.heightPixels;
//             dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 6);
            dialog.show();
            Log.d(TAG, "addRemaining: ");

//    private void makeReport()
//    {
//        Log.d(TAG, "makeReport:");
//
//        ReadWriteExcelFile readWriteExcelFile=new ReadWriteExcelFile();
//        try {
//            readWriteExcelFile.testFile(this,"Test", saleInfos);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
    }

    private void updateDueBalance(final double new_balanceDue, final double new_totalAmountPaid)
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_PURCHASE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.d("response",response);
                    JSONObject jsonObject = new JSONObject(response);
                    String message = null;
                    loading.dismiss();
                    message = jsonObject.getString("message");
                    Log.d(TAG, "onResponse:"+message);
                    Toast.makeText(PurchaseView_listImage.this, message, Toast.LENGTH_SHORT).show();
                     finish();
                    Intent intent=new Intent(PurchaseView_listImage.this,PurchaseView.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Intent intent = getIntent();

                // String stock_qty = input_addtostock.getText().toString();
                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                params.put("balance_due", String.valueOf(new_balanceDue));
                params.put("amount_paid", String.valueOf(new_totalAmountPaid));
                params.put("purchase_id",purchase_id);

                Log.d("Update Purchase Amount", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}

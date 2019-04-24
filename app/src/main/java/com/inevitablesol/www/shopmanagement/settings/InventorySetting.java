package com.inevitablesol.www.shopmanagement.settings;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.inevitablesol.www.shopmanagement.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InventorySetting extends AppCompatActivity
{

    private RadioButton radioButton_unitpriceYes,getRadioButton_unitpriceNo;
    private RadioGroup radioGroup_unitPrice;




    private RadioButton radioButton_gstYes,getRadioButton_gstNo;
    private RadioGroup radioGroup_gst;

    // Discount

    private RadioButton radioButton_discountYes,getRadioButton_discountNo;
    private RadioGroup radioGroup_discount;

    // Quotation


    private RadioButton radioButton_quotationYes,radioButton_quotationNo;
    private RadioGroup radioGroup_quotation;


    private static final String MySETTINGS = "MySetting";
    private SharedPreferences sharedpreferences;

    private static final String TAG = "InventorySetting";
    private  final String MyPREFERENCES = "MyPrefs";


    private  RadioGroup radioGroup_passCode;
    private  RadioButton radioButton_passCodeYes,getRadioButton_passCodeNo;
    private String dbname;
    private TextView et_passcode;
    private  boolean GST_STATUS;



    GlobalPool globalPool;


    RadioGroup  radioButton_menu;

    RadioButton radio_menuItem_yes,radio_menuItem_no;
    private SqlDataBase sqlDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_setting);
        radioButton_unitpriceYes=(RadioButton)findViewById(R.id.rb_unit_price_yes);
        getRadioButton_unitpriceNo=(RadioButton)findViewById(R.id.rb_unit_price_no);
        radioGroup_unitPrice=(RadioGroup)findViewById(R.id.rg_unit_price);
        radioButton_gstYes=(RadioButton)findViewById(R.id.rb_gst_yes);
        getRadioButton_gstNo=(RadioButton)findViewById(R.id.rb_gst_no);
        radioGroup_gst=(RadioGroup)findViewById(R.id.rg_gst);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        radioButton_discountYes=(RadioButton)findViewById(R.id.rb_discount_yes);
        getRadioButton_discountNo=(RadioButton)findViewById(R.id.rb_discount_no);
        radioGroup_discount=(RadioGroup)findViewById(R.id.rg_discount);

        radioGroup_quotation=(RadioGroup)findViewById(R.id.rg_quotation);
        radioButton_quotationYes=(RadioButton)findViewById(R.id.rb_quotation_yes);
        radioButton_quotationNo =(RadioButton)findViewById(R.id.rb_quotation_no);
         globalPool = (GlobalPool) getApplicationContext();
        sqlDataBase=new SqlDataBase(this);



             getPassCodeDeatails();


        radioGroup_passCode=(RadioGroup)findViewById(R.id.rg_passCode);
        radioButton_passCodeYes=(RadioButton)findViewById(R.id.rb_passCodeYes);
        getRadioButton_passCodeNo=(RadioButton)findViewById(R.id.rb_passCodeNo);
        et_passcode=(TextView) findViewById(R.id.passcode_show);

        radioButton_menu=(RadioGroup)findViewById(R.id.rb_menuItem);
        radio_menuItem_yes =(RadioButton)findViewById(R.id.rb_menuItem_yes);
        radio_menuItem_no=(RadioButton)findViewById(R.id.rb_menuItem_no);



        SharedPreferences sharedpreferences2 = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         dbname = (sharedpreferences2.getString("dbname", null));

              GST_STATUS   = globalPool.getGst_status();



        radioButton_menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.rb_menuItem_yes:
                        globalPool.setMenuItemStatus(true);
                        SaveMenuItemStatus("enable");
                        sqlDataBase.deleteDataBase_ItemTable();
                        break;
                    case R.id.rb_menuItem_no:
                        globalPool.setMenuItemStatus(false);
                        SaveMenuItemStatus("disable");
                        sqlDataBase.deleteDataBase_ItemTable();
                        break;
                        default:
                            Toast.makeText(globalPool, "Wrong Choice", Toast.LENGTH_SHORT).show();
                }

            }
        });



        radioGroup_quotation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Log.d(TAG, "onCheckedChanged:"+checkedId);

                switch (checkedId)
                {

                    case R.id.rb_quotation_yes:
                        saveInventoryStatus_quotation("yes");
                        break;
                    case R.id.rb_quotation_no:
                        saveInventoryStatus_quotation("No");
                        break;
                }


            }
        });

        radioGroup_discount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Log.d(TAG, "onCheckedChanged:"+checkedId);

                switch (checkedId)
                {

                    case R.id.rb_discount_yes:
                        saveInventoryStatus_discount("yes");
                        break;
                    case R.id.rb_discount_no:
                        saveInventoryStatus_discount("No");
                        break;
                }


            }
        });



        radioGroup_gst.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Log.d(TAG, "onCheckedChanged:"+checkedId);

                switch (checkedId)
                {
                    case R.id.rb_gst_yes:
                        saveInventoryStatus_gst("yes");
                        globalPool.setGst_status(true);
                        break;
                    case R.id.rb_gst_no:
                        saveInventoryStatus_gst("No");
                        globalPool.setGst_status(false);
                        break;
                }


            }
        });

        radioGroup_unitPrice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Log.d(TAG, "onCheckedChanged:"+checkedId);

                 switch (checkedId)
                 {

                     case R.id.rb_unit_price_yes:
                          saveInventoryStatus_unit("yes");
                         break;
                     case R.id.rb_unit_price_no:
                         saveInventoryStatus_unit("No");
                         break;
                 }


            }
        });
        sharedpreferences = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);

        Log.d(TAG, "onCreate: Menu item Status"+globalPool.isMenuItemStatus());


        try
        {
            String Status = (sharedpreferences.getString("unit_price", null));
          //  String gst = (sharedpreferences.getString("gst", null));
            String discount = (sharedpreferences.getString("discount", null));
            String quotation = (sharedpreferences.getString("quotation", null));
            String passcode = (sharedpreferences.getString("passcode", null));
            String menu_item = (sharedpreferences.getString("menu_item", null));


            Log.i(TAG, "onCreate:Unit Price"+Status);
            Log.i(TAG, "onCreate:gst"+GST_STATUS);
            Log.i(TAG, "onCreate:discount"+discount);
            Log.i(TAG, "onCreate:quotation"+quotation);
            Log.i(TAG, "onCreate:passCode"+passcode);
            Log.i(TAG, "onCreate:menu"+menu_item);

             if(Status!=null)
             {
                 if (Status.equalsIgnoreCase("Yes"))
                 {
                     radioButton_unitpriceYes.setChecked(true);

                 } else {
                     getRadioButton_unitpriceNo.setChecked(true);

                 }
             }

            if(quotation!=null)
            {
                if (quotation.equalsIgnoreCase("Yes"))
                {
                    radioButton_quotationYes.setChecked(true);

                } else {
                    radioButton_quotationNo.setChecked(true);

                }
            }

            if(GST_STATUS)
            {

                    radioButton_gstYes.setChecked(true);
            }else
            {

                getRadioButton_gstNo.setChecked(true);
            }

            if(discount!=null)
            {
                if (discount.equalsIgnoreCase("Yes"))
                {
                    radioButton_discountYes.setChecked(true);

                } else
                {
                    getRadioButton_discountNo.setChecked(true);

                }
            }

            if(passcode!=null)
            {

                if (passcode.equalsIgnoreCase("Yes"))
                {
                    radioButton_passCodeYes.setChecked(true);

                } else
                {
                    getRadioButton_passCodeNo.setChecked(true);

                }

            }
            if(menu_item.equalsIgnoreCase("enable"))
            {
                radio_menuItem_yes.setChecked(true);
               // radio_menuItem_yes.setChecked(false);

            }else
            {
                radio_menuItem_no.setChecked(true);
            }


        }catch (Exception e)
        {
            Log.d(TAG, "onCreate() returned: " +e);
        }
        radioGroup_passCode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                switch (checkedId)
                {
                    case R.id.rb_passCodeYes:
                        saveInventoryStatus_passCode("yes");
                        showPassCode();
                        break;
                    case R.id.rb_passCodeNo:
                        saveInventoryStatus_passCode("No");
                        savePassCodeDeatils("");
                        break;
                }

            }
        });




}

    private void saveInventoryStatus_passCode(String passCode)
    {

        Log.d(TAG, "saveStatus:PassCode"+passCode);
        SharedPreferences.Editor editor = sharedpreferences.edit();
                       editor.putString("passcode",passCode);
                       editor.commit();



    }

    private void saveInventoryStatus_quotation(String status)
    {
        Log.d(TAG, "saveStatus:Quotation"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("quotation",status);
        editor.commit();

    }

    private void saveInventoryStatus_discount(String status)
    {
        Log.d(TAG, "saveStatus:Discount"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("discount",status);
        editor.commit();

    }

    private void saveInventoryStatus_gst(String status)
    {
        Log.d(TAG, "saveStatus:gst"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("gst",status);
        editor.commit();

    }

    private void saveInventoryStatus_unit(String status)
    {
        Log.d(TAG, "saveStatus:unit"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("unit_price",status);
        editor.commit();
    }

    private void SaveMenuItemStatus(String status)
    {
        Log.d(TAG, "saveStatus: unit"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("menu_item",status);
        editor.commit();
    }



    private void showPassCode()
    {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.password_dialog);
        dialog.setTitle("Add Pass Code");
        final EditText et_passCode=(TextInputEditText)dialog.findViewById(R.id.et_passcode);
        AppCompatButton bt_save=(AppCompatButton)dialog.findViewById(R.id.save_passCode);
        AppCompatButton cancel=(AppCompatButton)dialog.findViewById(R.id.passcode_cancel);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passCode_et=et_passCode.getText().toString().trim();
                if(passCode_et.length()>0)
                {

                    savePassCodeDeatils(passCode_et);
                    dialog.dismiss();


                }

            }
        });
        dialog.setCancelable(false);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void  savePassCodeDeatils(final String passCode)
            {
                final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.SAVEPASSCODE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        JSONObject msg = null;
                        try {
                            msg = new JSONObject(response);
                            String message = msg.getString("message");
                            if (message.equalsIgnoreCase("Passcode added"))
                            {
                                loading.dismiss();
                                Toast.makeText(getApplication(), "PassCode Status Changed", Toast.LENGTH_LONG).show();


//                        Intent intent = new Intent(Billing_EmiDetails.this, BillingHistory.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();

                            } else
                            {
                                try
                                {
                                    loading.dismiss();
                                    Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                                    loading.dismiss();


                                } catch (NullPointerException e) {

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
                            Toast.makeText(InventorySetting.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                        }

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        Log.d("dbname", dbname);
                        params.put("dbname", dbname);
                        params.put("passcode", passCode);



                        Log.i(TAG, "getParams:Bank" + params.toString());
                        return params;
                    }


                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);


            }

    private void getPassCodeDeatails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.PASSCODE_DEATILS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();
                Log.d("Pass Code Deatils", resp);

                try
                {
                    JSONObject obj = new JSONObject(resp);
                    String  jsonObject1=obj.getString("message");

                        JSONArray jsonArray=obj.getJSONArray("records");
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String passcode=jsonObject.getString("passcode");

                            et_passcode.setText(passcode);

                        Log.d(TAG, "onResponse:passCode"+passcode);







                }catch (Exception e)
                {
                    Log.d(TAG, "onResponse:E"+e);

                }
//




            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(InventorySetting.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        } )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("dbname", dbname);

                return params;
            }
        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);

        AppController.getInstance(this.getApplicationContext()).addToRequestQueue(stringRequest);
    }


}

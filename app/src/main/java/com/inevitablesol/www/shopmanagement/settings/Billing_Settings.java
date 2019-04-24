package com.inevitablesol.www.shopmanagement.settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Billing_Settings extends AppCompatActivity
{

    private RadioGroup radioGroup_homeDelivery;
    private static final String TAG = "Billing_Settings";

    private static final String MySETTINGS = "MySetting";
   private SharedPreferences sharedpreferences;

    private  final String MyPREFERENCES = "MyPrefs";

    // home Delivery Status

       private RadioButton rb_homeDeliveryStatusYes;
       private  RadioButton rb_homeDeliveryStatusNo;

    // shipping Charges

     private  RadioGroup rg_shippingCharges;
     private  RadioButton rb_shippingYes,rb_shippingNo;

    //  other Charges

      private  RadioButton  other_radioButton_yes,other_radioButton_No;
     private  RadioGroup   radioGruop_otherCharges;

    // Paytm Options

     private  RadioButton radioButton_paytmYes,radioButton_paytmNo;
     private  RadioGroup  radioGroup_paytm;
     private   LinearLayout linearLayout_paytm;
     private  AppCompatButton  save_paytmDeatials;
     private  TextInputEditText et_mobile_paytm,et_address_paytm;


    // Bhim options


    private  RadioButton radioButton_bhimYes,radioButton_bhimNo;
    private  RadioGroup  radioGroup_bhim;

    private   LinearLayout linearLayout_bhim;
    private  AppCompatButton  save_bhimDeatils;
    private  TextInputEditText et_mobile_bhim,et_address_bhim;



    // Using Bank Details

    private  RadioGroup  rg_usingBankDetails;
    private  RadioButton rb_usingBankDeatilsYes,rb_usingBankDeatilsNo;


    private LinearLayout linear_bankDeatils;


    // Using #99
    private  RadioGroup radioGroup_99;
    private  RadioButton radioButton_99_yes,radioButton_99_no;
    private  LinearLayout linearLayout_99;


    // Using Emi

    private  RadioButton radioButton_emi_yes,radioButton_emi_no;
    private LinearLayout linearLayout_emi;
    private RadioGroup  radioGroup_emi;
    private  RadioGroup radioGroup_check;
    private  RadioButton radioButton_check_yes,radioButton_check_no;
    
    
    // Using  bankDeatils
    
    private AppCompatButton  save_bankDeatls;

    private TextInputEditText et_bankName,et_holder,et_ifsc,et_address,_et_confirmAccount,et_setting_accountNumber;
    private String dbname;

    //
    private  AppCompatButton button_upi99;
    private TextInputEditText et_mobile,et_adddress_99;

    GlobalPool globalVariable;
  //
    private  AppCompatButton add_paytmScreen,add_bhimScreen;


    // Gst




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing__settings);
        radioGroup_homeDelivery=(RadioGroup)findViewById(R.id.rg_homeDeliveryStatus);
        rb_homeDeliveryStatusYes=(RadioButton)findViewById(R.id.rg_homeDeliveryStatusYES);
        rb_homeDeliveryStatusNo=(RadioButton)findViewById(R.id.rg_homeDeliveryStatusNo);
        linearLayout_paytm=(LinearLayout)findViewById(R.id.linear_paytm);
        linearLayout_bhim=(LinearLayout)findViewById(R.id.linear_bhim);
        linear_bankDeatils=(LinearLayout)findViewById(R.id.linear_usingBankDetails);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
         save_bankDeatls=(AppCompatButton)findViewById(R.id.setting_bankDetails);
         save_bankDeatls.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 updateBankDeatls();
             }


         });

        // inititalize bank deatils View
         et_bankName=(TextInputEditText)findViewById(R.id.setting_bankName);
         et_address=(TextInputEditText)findViewById(R.id.setting_address);
         et_holder=(TextInputEditText)findViewById(R.id.setting_accountholder);
          et_ifsc=(TextInputEditText)findViewById(R.id.setting_ifsc);
        _et_confirmAccount=(TextInputEditText)findViewById(R.id.setting_confirmAccount);
          et_setting_accountNumber=(TextInputEditText)findViewById(R.id.setting_accountnumber);

         globalVariable = (GlobalPool)this.getApplication();
               getBankDetailForPaytm();
                getBankDetailForBhim();


        rg_shippingCharges=(RadioGroup)findViewById(R.id.bill_shippingCharges);
         rb_shippingYes=(RadioButton)findViewById(R.id.rb_shippingChargeYes);
          rb_shippingNo=(RadioButton)findViewById(R.id.rb_shippingChargeNo);

           radioGruop_otherCharges=(RadioGroup)findViewById(R.id.other_chrages);

         other_radioButton_No=(RadioButton)findViewById(R.id.rb_otherChargesNo);
         other_radioButton_yes=(RadioButton)findViewById(R.id.rb_otherChargesYes);

          radioGroup_paytm=(RadioGroup)findViewById(R.id.paytm_options);
          radioButton_paytmYes=(RadioButton)findViewById(R.id.rb_paytm_yes);
          radioButton_paytmNo=(RadioButton)findViewById(R.id.rb_paytm_no);

              et_mobile_paytm=(TextInputEditText)findViewById(R.id.mobile_paytm);
               et_address_paytm=(TextInputEditText)findViewById(R.id.address_paytm);
        save_paytmDeatials=(AppCompatButton)findViewById(R.id.setting_paytm);
        add_bhimScreen=(AppCompatButton)findViewById(R.id.add_bhimScreen);
        add_paytmScreen=(AppCompatButton)findViewById(R.id.add_paytmScreen);

        add_paytmScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(Billing_Settings.this,AddPaytmScreen.class));
            }
        });

         add_bhimScreen.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                 startActivity(new Intent(Billing_Settings.this,AddBhim_Screen.class));
             }
         });
         save_paytmDeatials.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                 final String mobile= et_mobile_paytm.getText().toString().trim();

                 final String address=et_address_paytm.getText().toString().trim();
                 if(mobile.length()>0 && address.length()>0)
                 {
                     save_bankDeatlsBPU99("paytm",mobile,address);
                 }else
                 {
                     Toast.makeText(Billing_Settings.this, "Please add Deatils", Toast.LENGTH_SHORT).show();
                 }

             }
         });


        radioGroup_bhim=(RadioGroup)findViewById(R.id.Bhim_options);
        radioButton_bhimYes=(RadioButton)findViewById(R.id.rb_bim_yes);
        radioButton_bhimNo=(RadioButton)findViewById(R.id.rb_bim_no);

        et_mobile_bhim=(TextInputEditText)findViewById(R.id.mobile_bhim);
        et_address_bhim=(TextInputEditText)findViewById(R.id.address_bhim);
        save_bhimDeatils=(AppCompatButton)findViewById(R.id.setting_bhim);
        save_bhimDeatils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String mobile=  et_mobile_bhim.getText().toString().trim();
                final String address= et_address_bhim.getText().toString().trim();
                if(mobile.length()>0 && address.length()>0)
                {
                    save_bankDeatlsBPU99("bhim",mobile,address);
                }else
                {
                    Toast.makeText(Billing_Settings.this, "Please add Deatils", Toast.LENGTH_SHORT).show();
                }

            }
        });


        rg_usingBankDetails=(RadioGroup)findViewById(R.id.rg_usingBankDetails);
        rb_usingBankDeatilsYes=(RadioButton)findViewById(R.id.rb_usingbankDetailsYes);
        rb_usingBankDeatilsNo=(RadioButton)findViewById(R.id.rb_usingbankDetailsNo);

        radioGroup_emi=(RadioGroup)findViewById(R.id.emi_options);
           radioButton_emi_yes=(RadioButton)findViewById(R.id.rb_emi_yes);
           radioButton_emi_no=(RadioButton)findViewById(R.id.rb_emi_no);
           linearLayout_emi=(LinearLayout)findViewById(R.id.linear_emiSetting);





              radioGroup_check=(RadioGroup)findViewById(R.id.check_options);
               radioButton_check_yes=(RadioButton)findViewById(R.id.rb_check_yes);
                radioButton_check_no=(RadioButton)findViewById(R.id.rb_check_no);


               radioGroup_check.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(RadioGroup group, int checkedId)
                   {


                       Log.d(TAG, "onCheckedChanged: "+checkedId);
                       switch (checkedId)
                       {
                           case R.id.rb_check_yes:
                               Using_Check_Details("yes");

                               break;
                           case R.id.rb_check_no:
                               Using_Check_Details("No");
                             //  linearLayout_99.setVisibility(View.GONE);
                               break;
                       }



                   }
               });


          radioGroup_emi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(RadioGroup group, int checkedId)
              {
                  Log.d(TAG, "onCheckedChanged: "+checkedId);
                  switch (checkedId)
                  {
                      case R.id.rb_emi_yes:
                          UsingEmiDetails("yes");

                          break;
                      case R.id.rb_emi_no:
                          UsingEmiDetails("No");
                        //linearLayout_99.setVisibility(View.GONE);
                          break;
                  }

              }
          });







        rg_usingBankDetails.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {


                Log.d(TAG, "onCheckedChanged: "+checkedId);
                switch (checkedId)
                {
                    case R.id.rb_usingbankDetailsYes:
                        UsingBankDetailsChanges("yes");
                        linear_bankDeatils.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_usingbankDetailsNo:
                        UsingBankDetailsChanges("No");
                        linear_bankDeatils.setVisibility(View.GONE);
                        break;
                }


            }
        });

        radioGroup_bhim.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {


                Log.d(TAG, "onCheckedChanged: "+checkedId);
                switch (checkedId)
                {
                    case R.id.rb_bim_yes:
                        BhimChanges("yes");
                        linearLayout_bhim.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_bim_no:
                        BhimChanges("No");
                        linearLayout_bhim.setVisibility(View.GONE);
                        break;
                }

            }
        });





        radioGroup_paytm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                Log.d(TAG, "onCheckedChanged: "+checkedId);
                switch (checkedId)
                {
                    case R.id.rb_paytm_yes:
                        paytmChanges("yes");
                        linearLayout_paytm.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_paytm_no:
                        paytmChanges("No");
                        linearLayout_paytm.setVisibility(View.GONE);
                        break;
                }


            }
        });


        radioGruop_otherCharges.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                Log.d(TAG, "onCheckedChanged: "+checkedId);
                switch (checkedId)
                {
                    case R.id.rb_otherChargesYes:
                        saveOtherCharges("yes");
                        break;
                    case R.id.rb_otherChargesNo:
                        saveOtherCharges("No");
                        break;
                }


            }
        });



        rg_shippingCharges.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                Log.d(TAG, "onCheckedChanged: "+checkedId);

                switch (checkedId)
                {
                    case R.id.rb_shippingChargeYes:
                        saveShippingStatus("yes");
                        break;
                    case R.id.rb_shippingChargeNo:
                        saveShippingStatus("No");
                        break;
                }


            }
        });


        radioGroup_homeDelivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Log.d(TAG, "onCheckedChanged: "+checkedId);
                switch (checkedId)
                {
                    case R.id.rg_homeDeliveryStatusYES:
                        saveStatus("yes");
                        break;
                    case R.id.rg_homeDeliveryStatusNo:
                        saveStatus("No");
                        break;
                }
            }
        });

         try
         {
             sharedpreferences = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);

             try
             {
                 String Status = (sharedpreferences.getString("home_DeliveryStatus", null));
                 if(Status.equalsIgnoreCase("Yes"))
                 {
                     rb_homeDeliveryStatusYes.setChecked(true);

                 }else
                 {
                     rb_homeDeliveryStatusNo.setChecked(true);

                 }
                 Log.i(TAG, "onCreate:Home Status"+Status);
             }catch (Exception e)
             {
                 Log.d(TAG, "onCreate() returned: " +e);
             }


             String otherCharges= null;
             try
             {
                 otherCharges = (sharedpreferences.getString("othercharges_status", null));
                 if(otherCharges.equalsIgnoreCase("Yes"))
                 {
                     other_radioButton_yes.setChecked(true);
                     other_radioButton_No.setChecked(false);

                 }else
                 {
                     other_radioButton_No.setChecked(true);

                 }

             } catch (Exception e)
             {
                 e.printStackTrace();
             }
             String shippingCharges= null;
             try
             {
                 shippingCharges = (sharedpreferences.getString("shipping_status", null));
                 if(shippingCharges.equalsIgnoreCase("Yes"))
                 {
                     rb_shippingYes.setChecked(true);
                     rb_shippingNo.setChecked(false);

                 }else
                 {
                     rb_shippingNo.setChecked(true);

                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }




             String emi= null;
             try {
                 emi = (sharedpreferences.getString("bank_emi", null));
                 if(emi.equalsIgnoreCase("Yes"))
                 {
                     radioButton_emi_yes.setChecked(true);
                     radioButton_emi_no.setChecked(false);

                 }else
                 {
                     radioButton_emi_no.setChecked(true);

                 }
             } catch (Exception e)
             {
                 e.printStackTrace();
             }



             String bannk_check= null;
             try {
                 bannk_check = (sharedpreferences.getString("bank_check", null));
                 if(bannk_check.equalsIgnoreCase("Yes"))
                 {
                     radioButton_check_yes.setChecked(true);
                     radioButton_check_no.setChecked(false);

                 }else
                 {
                     radioButton_check_no.setChecked(true);

                 }
             } catch (Exception e)
             {
                 e.printStackTrace();
             }




             String paytm = null;
             try {
                 paytm = (sharedpreferences.getString("paytm_status", null));
                 if(paytm.equalsIgnoreCase("Yes"))
                 {
                     radioButton_paytmYes.setChecked(true);
                     radioButton_paytmNo.setChecked(false);

                 }else
                 {
                     radioButton_paytmNo.setChecked(true);
                     linearLayout_paytm.setVisibility(View.GONE);
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
             String bhimStatus = null;
             try {
                 bhimStatus = (sharedpreferences.getString("bhim_status", null));

                 if(bhimStatus.equalsIgnoreCase("Yes"))
                 {
                     radioButton_bhimYes.setChecked(true);


                 }else
                 {
                     radioButton_bhimNo.setChecked(true);
                     linearLayout_bhim.setVisibility(View.GONE);

                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }

             String bankDeatils= null;
             try {
                 bankDeatils = (sharedpreferences.getString("bank_details", null));

                 if(bankDeatils.equalsIgnoreCase("Yes"))
                 {
                      rb_usingBankDeatilsYes.setChecked(true);
                     linear_bankDeatils.setVisibility(View.VISIBLE);


                 }else
                 {
                     rb_usingBankDeatilsNo.setChecked(true);
                     linear_bankDeatils.setVisibility(View.GONE);

                 }
                 Log.d(TAG, "onCreate:Bank Deatils"+bankDeatils);
             } catch (Exception e) {
                 e.printStackTrace();
             }




             String bank99Status= null;
             try {
                 bank99Status = (sharedpreferences.getString("bank_99", null));
                 Log.d(TAG, "onCreate:Bank 99 Deatils"+ bank99Status);

                 if(bank99Status.equalsIgnoreCase("Yes"))
                 {
                     radioButton_99_yes.setChecked(true);
                     linearLayout_99.setVisibility(View.VISIBLE);


                 }else
                 {
                     radioButton_99_no.setChecked(true);
                     linearLayout_99.setVisibility(View.GONE);

                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }

         }catch (NullPointerException e)
         {
             Log.i(TAG, "onCreate:error"+e);
         }catch (Exception e)
         {
             Log.i(TAG, "onCreate:error"+e);
         }

    }

    private void save_bankDeatlsBPU99(final String type, final String mobile, final String address)
    {


        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.SAVEBP99, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, "onResponse: " + response);
                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Details Added"))
                    {
                        loading.dismiss();
                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();


//                        Intent intent = new Intent(Billing_EmiDetails.this, BillingHistory.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();

                    } else
                    {
                        try {
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
                    Toast.makeText(Billing_Settings.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("mobile_no", mobile);
                params.put("details", address);
                params.put("type", type);


                Log.i(TAG, "getParams:Bank" + params.toString());
                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);




    }

    private void updateBankDeatls()
    {

         final String bankName=et_bankName.getText().toString().trim();
         final String  bankHolder=et_holder.getText().toString().trim();
         final String ifc=et_ifsc.getText().toString().trim();
         final String  accountNumber=et_setting_accountNumber.getText().toString().trim();
        final String   confirmPassword=_et_confirmAccount.getText().toString().trim();
         final String address= et_address.getText().toString().trim();

         if(accountNumber.length()>0 && bankHolder.length()>0 && bankName.length()>0)
         {

             final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
             StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.SAVEBANKDETIALS, new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {
                     Log.d(TAG, "onResponse: " + response);
                     JSONObject msg = null;
                     try {
                         msg = new JSONObject(response);
                         String message = msg.getString("message");
                         if (message.equalsIgnoreCase("Account Added"))
                         {
                             loading.dismiss();
                             Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();

                                  finish();
//                        Intent intent = new Intent(Billing_EmiDetails.this, BillingHistory.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();

                         } else {
                             try {
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
                         Toast.makeText(Billing_Settings.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                     }

                 }
             }) {
                 @Override
                 protected Map<String, String> getParams() throws AuthFailureError {
                     Map<String, String> params = new HashMap<>();
                     Log.d("dbname", dbname);
                     params.put("dbname", dbname);
                     params.put("bankname", bankName);
                     params.put("accountname", bankHolder);
                     params.put("accno", accountNumber);
                     params.put("ifsc", ifc);
                     params.put("address", address);

                     Log.i(TAG, "getParams:Bank" + params.toString());
                     return params;
                 }


             };
             RequestQueue requestQueue = Volley.newRequestQueue(this);
             requestQueue.add(stringRequest);

         }else
         {
             Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
         }







    }


    private void Using_Check_Details(String status)
    {
        Log.d(TAG, "check_saveStatus:"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("bank_check",status);
        editor.commit();

    }

    private void UsingEmiDetails(String status)
    {
        Log.d(TAG, "emi_saveStatus:"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("bank_emi",status);
        editor.commit();

    }

    private void UsingBankDetails99(String status)
    {
        Log.d(TAG, "99_saveStatus:"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("bank_99",status);
        editor.commit();
    }

    private void UsingBankDetailsChanges(String status)
    {

        Log.d(TAG, "bank_saveStatus:"+status);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("bank_details",status);
        editor.commit();

    }

    private void BhimChanges(String status)
    {
        Log.d(TAG, "bhim_saveStatus:"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("bhim_status",status);
        editor.commit();
    }

    private void paytmChanges(String status)
    {

        Log.d(TAG, "paytm_saveStatus:"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("paytm_status",status);
        editor.commit();

    }

    private void saveOtherCharges(String status)
    {

        Log.d(TAG, "other_saveStatus:"+status);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("othercharges_status",status);
        editor.commit();
    }

    private void saveShippingStatus(String status)
    {


        Log.d(TAG, "shipping_saveStatus:"+status);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("shipping_status",status);
        editor.commit();

    }

    private void saveStatus(String status)
    {
        Log.d(TAG, "h_saveStatus:"+status);
        GlobalPool globalPool =new GlobalPool();
        globalPool.setH_status(status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("home_DeliveryStatus",status);
        editor.commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        SharedPreferences sharedpreferences2 = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences2.getString("dbname", null));
        updateBankDeatls();
        getBankDetail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }



    private void getBankDetail()
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.VIEWACCOUNT_DETAILS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {


                String resp = response.toString().trim();
                loading.dismiss();
                Log.d("Bank Details",resp);
                JSONObject jsonObject= null;
                try
                {
                    JSONObject json = new JSONObject(resp);
                    JSONArray jsonArray=json.getJSONArray("records");
                    jsonObject=jsonArray.getJSONObject(0);
                    et_bankName.setText(jsonObject.getString("bankname"));
                    et_setting_accountNumber.setText(jsonObject.getString("accno"));
                    _et_confirmAccount.setText(jsonObject.getString("accno"));
                    et_holder.setText(jsonObject.getString("accountname"));
                     et_address.setText(jsonObject.getString("address"));
                    et_ifsc.setText(jsonObject.getString("ifsc"));





                } catch (JSONException e)
                {
                    Log.d("Exception",""+e);
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();

                params.put("dbname", globalVariable.getDbname());
                Log.d(TAG, "getParams:"+params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getBankDetailForPaytm()
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETPAYTMDEATLS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {


                String resp = response.toString().trim();
                loading.dismiss();
                Log.d("Bank Details Paytm",resp);
                JSONObject jsonObject= null;
                try
                {
                    JSONObject json = new JSONObject(resp);
                    JSONArray jsonArray=json.getJSONArray("records");
                    jsonObject=jsonArray.getJSONObject(0);
                    et_mobile_paytm.setText(jsonObject.getString("mobile_no"));
                      et_address_paytm.setText(jsonObject.getString("details"));
//                    txt_mobile.setText(bankname);
//                    txt_mobile_vpa.setText(detais);




                } catch (JSONException e)
                {
                    Log.d("Exception",""+e);
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", globalVariable.getDbname());
                params.put("type","paytm");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void getBankDetailForBhim()
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETPAYTMDEATLS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {


                String resp = response.toString().trim();
                loading.dismiss();
                Log.d("Bank Deatls",resp);
                JSONObject jsonObject= null;
                try
                {
                    JSONObject json = new JSONObject(resp);
                    JSONArray jsonArray=json.getJSONArray("records");
                    jsonObject=jsonArray.getJSONObject(0);
                    String bankname =jsonObject.getString("mobile_no");
                    String  detais=jsonObject.getString("details");
                    et_mobile_bhim.setText(bankname);
                    et_address_bhim.setText(detais);




                } catch (JSONException e)
                {
                    Log.d("Exception",""+e);
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", globalVariable.getDbname());
                params.put("type","bhim");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}

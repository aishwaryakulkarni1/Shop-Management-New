package com.inevitablesol.www.shopmanagement.customer_module;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.Validation.Validation;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.billing_module.Parser.S_parser;
import com.inevitablesol.www.shopmanagement.billing_module.SuppierAdapter;
import com.inevitablesol.www.shopmanagement.vendor_module.GST_validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AddCustomer extends AppCompatActivity implements View.OnClickListener
{

    private TextInputEditText txt_cname,txt_caddress,txt_cemail,txt_cphone;
    AppCompatButton addCust;
    RadioButton homedilivery_yes;
    RadioButton homeDelivery_no;
    RadioGroup homedilivery;
    private String homedelivery_status;

    private ImageView viewDetails;
    private String dbname;


    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private static final String TAG = "AddCustomer";
    private String customer;
    private Spinner mSupplier;
    private EditText txtEdit_gst;
    private RadioGroup radioGroup;
    private LinearLayout linearLayout,linearLayout_address;
    private String placeofSupply;
    private boolean mphone_flag;
    private boolean memail_flag;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        txt_cname=(TextInputEditText)findViewById(R.id.input_custName);
        txt_cemail=(TextInputEditText)findViewById(R.id.input_custEmail);
        txt_cphone=(TextInputEditText)findViewById(R.id.input_CustMobile);
        txt_caddress=(TextInputEditText)findViewById(R.id.input_custAddress);
        mSupplier=(Spinner)findViewById(R.id.billing_supplier);
        addCust=(AppCompatButton)findViewById(R.id.add_CustDetails);
        homedilivery=(RadioGroup)findViewById(R.id.cust_delivery_button);
        homeDelivery_no=(RadioButton)findViewById(R.id.cust_radioButton1);
        homedilivery_yes =(RadioButton)findViewById(R.id.cust_radioButton2);
        txtEdit_gst=(EditText)findViewById(R.id.input_gst);
        addCust.setOnClickListener(this);
        linearLayout=(LinearLayout)findViewById(R.id.gst_linear);
        linearLayout_address=(LinearLayout)findViewById(R.id.cust_linear_address);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        radioGroup=(RadioGroup)findViewById(R.id.gst_details);

        homedilivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // checkedId is the RadioButton selected
                Log.d(TAG, "onCheckedChanged: "+checkedId);

                switch (checkedId)
                {
                    case R.id.bill_addressYes:
                         linearLayout_address.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bill_addressNo:
                        linearLayout_address.setVisibility(View.GONE);
                        break;
                }
            }
        });




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.gst_yes_vendor:
                        linearLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.gst_no_vendor:
                        linearLayout.setVisibility(View.GONE);

                        break;
                }

            }
        });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getAllSuplier();

        try {
            Intent intent=getIntent();
            customer=intent.getStringExtra("customer");
            Log.d(TAG, "onCreate:Customer"+customer);
        } catch (NullPointerException  e)
        {
            e.printStackTrace();
        }

//        txt_cphone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus)
//            {
//                Log.d(TAG, "onFocusChange:Phone "+hasFocus);
//                  if(!hasFocus)
//                  {
//                if(!Validation.isValidPhone(txt_cphone.getText().toString())) {
//                    txt_cphone.setError("Invalid mobile");
//                }
//                }
//
//
//
//            }
//        });
//        txt_cemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus)
//            {
//                Log.d(TAG, "onFocusChange: Email"+hasFocus);
//
//                if(!hasFocus)
//                {
//                    if (!Validation.isValidEmail(txt_cemail.getText().toString())) {
//                        txt_cemail.setError("Invalid Email");
//                    }
//                }
//
//            }
//        });

        txt_cphone.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(!Validation.isValidPhone(txt_cphone.getText().toString()))
                {
                    txt_cphone.setError("Invalid mobile");
                }else
                {

                    mphone_flag=true;
                }

            }
        });
        txt_cemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (!Validation.isValidEmail(txt_cemail.getText().toString()))
                {
                    txt_cemail.setError("Invalid Email");
                }else
                {
                    memail_flag=true;
                }

            }
        });

        try
        {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(mSupplier);

            // Set popupWindow height to 500px
            popupWindow.setHeight((int)height/2);

        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }



    @Override
    public void onClick(View v)
    {
        int id=v.getId();
        switch (id)
        {
            case R.id.add_CustDetails:
                boolean flag=checkValidation();
                Log.d(TAG, "onClick: "+flag);
                   if(flag)
                   {
                       getData();
                   }else if(!mphone_flag)
                   {
                       Toast.makeText(getApplicationContext(),"Please Enter Phone Valid Number ",Toast.LENGTH_LONG).show();
                   }
                  else  if(!memail_flag)
                   {
                       Toast.makeText(getApplicationContext(),"Please Enter  Valid Email ",Toast.LENGTH_LONG).show();
                   }
                break;
        }

    }

    private boolean checkValidation()
    {
        boolean val;

         val = Validation.hasText(txt_cname)? true :false;
        Log.d(TAG, "checkValidation: "+val);
         val = Validation.hasText(txt_cphone)? true :false;
        // val = Validation.hasText(txt_cemail)? true :false;
        Log.d(TAG, "checkValidation: "+val);
        return val;
    }

    private void getData()
    {
        String name=txt_cname.getText().toString().trim();
        String email=txt_cemail.getText().toString().trim();
        String c_phone=txt_cphone.getText().toString().trim();
        String gstin=txtEdit_gst.getText().toString().trim();
        int selectedId = homedilivery.getCheckedRadioButtonId();
         String address=txt_caddress.getText().toString().trim();
        RadioButton   radioButtonStatus = (RadioButton) findViewById(selectedId);
        homedelivery_status=radioButtonStatus.getText().toString().trim();
        String  placeofSupply=mSupplier.getSelectedItem().toString().trim();

        int radioGroupId=radioGroup.getCheckedRadioButtonId();
        RadioButton  RadioButton=(RadioButton)findViewById(radioGroupId);
        String gstStatus=RadioButton.getText().toString().trim();

        if(!name.isEmpty()  && c_phone.length()== 10 && ! homedelivery_status.isEmpty())
        {
            Log.d(TAG, "getData: IF");
              if(linearLayout.getVisibility()==View.VISIBLE)
              {
                  if(GST_validation.isValid_Gst(gstin))
                  {
                      addDetails(name,email,c_phone,homedelivery_status,address,gstin,placeofSupply,gstStatus);
                      return;
                  }else
                  {
                      txtEdit_gst.setError("Invalid GST");
                      Toast.makeText(getApplication(),"Please Add Valid Gst",Toast.LENGTH_LONG).show();
                  }


              }else
              {
                  Log.d(TAG, "getData: Else");
                    if(linearLayout_address.getVisibility()== View.VISIBLE)
                    {
                         if(!address.isEmpty())
                         {
                             addDetails(name,email,c_phone,homedelivery_status,address,gstin,placeofSupply,gstStatus);
                             return;

                         }else
                         {
                             Toast.makeText(getApplication(),"Please Add  Address",Toast.LENGTH_LONG).show();
                         }
                    }else
                    {
                        addDetails(name,email,c_phone,homedelivery_status,address,gstin,placeofSupply,gstStatus);
                        return;
                    }

              }

        }else if (!memail_flag)
        {
            Log.d(TAG, "getData: EMAIl");
            Toast.makeText(getApplicationContext(),"please Add Valid  EMAIL",Toast.LENGTH_LONG).show();
        }else  if(!mphone_flag)
        {
            Log.d(TAG, "getData: mPhone");
            Toast.makeText(getApplicationContext(),"please Add Valid  Phone",Toast.LENGTH_LONG).show();
        }else
        {

            Log.d(TAG, "getData: ");
            Toast.makeText(getApplicationContext(),"please Add Valid  Field",Toast.LENGTH_LONG).show();
        }


    }

    private void addDetails(final String name, final String email, final String c_phone, final String homedelivery_status, final String address, final String gst, final String placeofSupply, final String gstStatus)
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.ADD_CUSTOMER, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    String resp = response.toString().trim();
                    JSONObject msg = new JSONObject(response);
                    String message = msg.getString("message");
                    Log.d("Added Successfully",resp);
                    if(message.equalsIgnoreCase("Add new customer succesfully"))
                    {

                        try
                        {
                            if(customer!=null)
                            {
                                if(customer.equalsIgnoreCase("true"))
                                {
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                    Toast.makeText(AddCustomer.this, "customer Added succesfully", Toast.LENGTH_SHORT).show();

                                }

                            }else
                            {
                                Toast.makeText(AddCustomer.this, "customer Added succesfully", Toast.LENGTH_SHORT).show();
                                finish();
                                loading.dismiss();
                            }

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }



                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(AddCustomer.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }



            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("customer_name",name);
                params.put("mobile_number", c_phone);
                params.put("email_id",email);
                params.put("home_delivery",homedelivery_status);
                params.put("address",address);
                params.put("gstin",gst);
                params.put("gstStatus",gstStatus);
                params.put("place_of_supply",placeofSupply);
                Log.d(TAG, "getParams: "+params.toString());
                return   params;
            }
        };
         Volley.newRequestQueue(this).add(stringRequest);
        }


    private void getAllSuplier()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(AddCustomer.this).add(new StringRequest(Request.Method.GET, WebApi.GETSUPLER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject msg = null;
                        try
                        {
                            msg = new JSONObject(response);
                            String message = msg.getString("message");
                            if (message.equalsIgnoreCase("Data not available"))
                            {
                                Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                                loading.dismiss();

                            } else
                            {
                                try
                                {
                                    Log.d(TAG, "suplier:"+response);
                                    processData(response);
                                    loading.dismiss();
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
                            Toast.makeText(AddCustomer.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                        }

                    }
                })
        );


    }

    private void processData(String response)
    {
        S_parser S_parser =new S_parser(response);
        S_parser.productParser();
        SuppierAdapter productAdapter=new SuppierAdapter(AddCustomer.this,R.layout.supplier_list, S_parser.stateName, S_parser.stateCode);
        mSupplier.setAdapter(productAdapter);


    }






    }


package com.inevitablesol.www.shopmanagement.customer_module;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.inevitablesol.www.shopmanagement.Validation.Validation;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.billing_module.Parser.S_parser;
import com.inevitablesol.www.shopmanagement.billing_module.SuppierAdapter;
import com.inevitablesol.www.shopmanagement.vendor_module.GST_validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateCustomerDetail extends AppCompatActivity implements View.OnClickListener
{
    private static final String UPDATE_CUSTOMER ="http://35.161.99.113:9000/api/customer/edit" ;
    private TextView txt_cname,txt_caddress,txt_cemail,txt_cphone;
    AppCompatButton addCustomer;
    private String c_id;
    private String dbname;
    private RadioGroup homedilivery;
    private RadioButton homeDelivery_no;
    private RadioButton homedelivery_yes;
    private String homedelivery_status;


    private LinearLayout linearLayout,linearLayout_address;
    private String placeofSupply;
    private Spinner mSupplier;
    private EditText txtEdit_gst;

    private static final String TAG = "UpdateCustomerDetail";

    private  boolean  memail_flag=true;
    private  boolean  mphone_flag=true;
    private  String gstin;

    Pattern pattern=Pattern.compile("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}");


    private RadioGroup rd_gstDetails;
    private RadioButton gst_yes,gst_no;
    private RadioGroup radioGroup;
    private String state;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_customer_detail);

        txt_cname=(TextView)findViewById(R.id.input_customerName);
        txt_caddress=(TextView)findViewById(R.id.input_cutomerAddress);
        txt_cemail=(TextView)findViewById(R.id.input_customeremail);
        txt_cphone=(TextView)findViewById(R.id.input_customerMobile);
        homedilivery=(RadioGroup)findViewById(R.id.cust_delivery_button);
        homedelivery_yes=(RadioButton)findViewById(R.id.cust_radioButton1);
        homeDelivery_no =(RadioButton)findViewById(R.id.cust_radioButton2);
        mSupplier=(Spinner)findViewById(R.id.billing_supplier);
        txtEdit_gst=(EditText)findViewById(R.id.input_gst);
        rd_gstDetails=(RadioGroup)findViewById(R.id.gst_details);
        gst_yes=(RadioButton)findViewById(R.id.gst_yes);
        gst_no =(RadioButton)findViewById(R.id.gst_no);

        txtEdit_gst=(EditText)findViewById(R.id.input_gst);


        linearLayout=(LinearLayout)findViewById(R.id.gst_linear);
        linearLayout_address=(LinearLayout)findViewById(R.id.cust_linear_address);

        homedelivery_yes.setOnClickListener(this);
        homeDelivery_no.setOnClickListener(this);
        homedilivery.setOnClickListener(this);
        addCustomer=(AppCompatButton)findViewById(R.id.update_custDetails);
        addCustomer.setOnClickListener(this);
        radioGroup=(RadioGroup)findViewById(R.id.gst_details);

        Log.d(TAG, "onCreate: ");

        Intent intent=getIntent();
        if(intent.hasExtra("c_name"))
        {
            c_id=String.valueOf(intent.getStringExtra("c_id"));
            String name=intent.getStringExtra("c_name");
            String mobile=intent.getStringExtra("c_mobile");
            String address=intent.getStringExtra("c_address");
            String email=intent.getStringExtra("c_email");
              gstin=intent.getStringExtra("gstin");
            txtEdit_gst.setText(gstin.trim());
             placeofSupply=intent.getStringExtra("placeofsupply");
              state=intent.getStringExtra("state");
             dbname=intent.getStringExtra("dbname");
            homedelivery_status=intent.getStringExtra("delivery_status");
            Log.d(TAG, "onCreate: HD"+homedelivery_status);

            if(homedelivery_status.equalsIgnoreCase("Yes"))
            {
                homedelivery_yes.setChecked(true);
                homeDelivery_no.setChecked(false);
                linearLayout_address.setVisibility(View.VISIBLE);
            }else
            {
                homedelivery_yes.setChecked(false);
                homeDelivery_no.setChecked(true);
                linearLayout_address.setVisibility(View.GONE);
            }


            txt_cname.setText(name);
            txt_caddress.setText(address);
            txt_cemail.setText(email);
            txt_cphone.setText(mobile);

        }else
        {

        }

        Log.d(TAG, "onCreate:StateCode"+placeofSupply);
        Log.d(TAG, "onCreate: GST Status"+gstin);
        dbname=intent.getStringExtra("dbname");


        if(gstin.equalsIgnoreCase("Yes"))
        {
            gst_yes.setChecked(true);
            gst_no.setChecked(false);
            linearLayout.setVisibility(View.VISIBLE);
        }else
        {
            linearLayout.setVisibility(View.GONE);
            gst_yes.setChecked(false);
            gst_no.setChecked(true);
        }



        txt_cphone.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

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
                    mphone_flag=false;
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
                    memail_flag=false;
                }else
                {
                    memail_flag=true;
                }

            }
        });


        getAllSuplier();
        homedilivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // checkedId is the RadioButton selected
                Log.d(TAG, "onCheckedChanged: "+checkedId);

                switch (checkedId)
                {
                    case R.id.cust_radioButton1:
                        linearLayout_address.setVisibility(View.VISIBLE);
                        break;
                    case R.id.cust_radioButton2:
                        linearLayout_address.setVisibility(View.GONE);
                        break;
                }
            }
        });

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId)
//            {
//                switch (checkedId)
//                {
//                    case R.id.gst_yes_vendor:
//                        linearLayout.setVisibility(View.VISIBLE);
//                        break;
//                    case R.id.gst_no_vendor:
//                        linearLayout.setVisibility(View.GONE);
//
//                        break;
//                }
//
//            }
//        });
        rd_gstDetails.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                    Log.d(TAG, "onCheckedChanged: "+checkedId);

                    switch (checkedId)
                    {
                        case R.id.gst_yes:
                            linearLayout.setVisibility(View.VISIBLE);
                            break;
                        case R.id.gst_no:
                            linearLayout.setVisibility(View.GONE);
                            break;
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



    private void getAllSuplier()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(UpdateCustomerDetail.this).add(new StringRequest(Request.Method.GET, WebApi.GETSUPLER, new Response.Listener<String>() {
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
                            Toast.makeText(UpdateCustomerDetail.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                        }

                    }
                })
        );


    }
    private void processData(String response)
    {
        S_parser S_parser =new S_parser(response);
        S_parser.productParser();
        SuppierAdapter productAdapter=new SuppierAdapter(UpdateCustomerDetail.this,R.layout.supplier_list, S_parser.stateName, S_parser.stateCode);
        mSupplier.setAdapter(productAdapter);
        if(placeofSupply!=null)
        {
            int position=     productAdapter.getPosition(placeofSupply);
            mSupplier.setSelection(position);
        }


    }

    @Override
    public void onClick(View v)
    {
        int id=v.getId();
        switch (id)
        {
            case R.id.update_custDetails:
                boolean flag=checkValidation();
                Log.d(TAG, "onClick: "+flag);
                if(flag)
                {
                    UpdateRecord();
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
        Log.d(TAG, "checkValidation: "+val);
                Log.d(TAG, "checkValidation: "+val);

        return val;
    }

    private void UpdateRecord()
    {
        String name=txt_cname.getText().toString().trim();
        String address=txt_caddress.getText().toString().trim();
        String   mobile=txt_cphone.getText().toString().trim();
        String   email=txt_cemail.getText().toString().trim();

        String gstin=txtEdit_gst.getText().toString().trim();
        int selectedId = homedilivery.getCheckedRadioButtonId();
        RadioButton   radioButtonStatus = (RadioButton) findViewById(selectedId);
        homedelivery_status=radioButtonStatus.getText().toString().trim();


        String  placeofSupply=mSupplier.getSelectedItem().toString().trim();


        if(!name.isEmpty()  && mobile.length()==10 && !homedelivery_status.isEmpty())
        {
            if(linearLayout.getVisibility()==View.VISIBLE)
            {

                if(!gstin.isEmpty())
                {

                    Matcher matcher = pattern.matcher(gstin);


                    if (matcher.matches())
                    {
                        Log.d(TAG, "saveDetails:GST true");
                        Log.d(TAG, "UpdateRecord: Email"+memail_flag);
                        Log.d(TAG, "UpdateRecord: mphone"+mphone_flag);

                        if (memail_flag && mphone_flag)
                        {
                            Log.d(TAG, "saveDetails:GST true");
                            updateDetails(name, address, mobile, email, homedelivery_status, gstin, placeofSupply);
                        } else if (!mphone_flag)
                        {
                            Toast.makeText(getApplicationContext(), "Please Add Valid Phone", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Please Add Valid Email", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Please Add GST IN", Toast.LENGTH_SHORT).show();
                    }

                }
            }else
            {
                Log.d(TAG, "UpdateRecord: Email"+memail_flag);
                Log.d(TAG, "UpdateRecord: mphone"+mphone_flag);

                if(linearLayout_address.getVisibility()== View.VISIBLE)
                {
                    if (!address.isEmpty() && memail_flag)
                    {
                        updateDetails(name, address, mobile, email, homedelivery_status, gstin, placeofSupply);

                    } else if (address.isEmpty())
                    {

                        Toast.makeText(getApplication(), "Please Add  Address", Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(getApplication(), "Please Valid  Email", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    updateDetails(name,address,mobile,email,homedelivery_status,gstin,placeofSupply);
                }


            }

        }else
        {
            Toast.makeText(getApplicationContext(),"please Add All fields",Toast.LENGTH_LONG).show();
        }

//        if(!name.isEmpty()&& !address.isEmpty() && !mobile.isEmpty() && !email.isEmpty())
//        {
//           updateDetails(name,address,mobile,email,homedelivery_status, gstin, placeofSupply);
//
//        }
    }

    private void updateDetails(final String name, final String address, final String mobile, final String email, final String homedelivery_status, final String gstin, final String placeofSupply)
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Updating...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.UPDATE_CUSTOMER, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();
                try {
                    Log.d(TAG, "onResponse:"+resp);
                    loading.dismiss();
                    JSONObject jsonObject=new JSONObject(resp);
                    String message=jsonObject.getString("message");
                    if(message.equalsIgnoreCase("succesfully updated"))

                    {
                        loading.dismiss();
                        finish();


                    }else
                    {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }




                }catch (JSONException e)
                {

                }
                Log.d(" Added Successfully",resp);






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if(error instanceof NoConnectionError)
                    Toast.makeText(UpdateCustomerDetail.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("customer_id",c_id);
                params.put("customer_name", name);
                params.put("mobile_number",mobile);
                params.put("email_id",email);
                params.put("address",address);
                params.put("home_delivery",homedelivery_status);
                params.put("gstin",gstin);
                params.put("place_of_supply",placeofSupply);
                return   params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

}

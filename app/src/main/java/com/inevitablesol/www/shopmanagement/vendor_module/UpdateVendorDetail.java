package com.inevitablesol.www.shopmanagement.vendor_module;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.Validation.Validation;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.billing_module.Parser.S_parser;
import com.inevitablesol.www.shopmanagement.billing_module.SuppierAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateVendorDetail extends AppCompatActivity  implements View.OnClickListener{

    private static final String UPDATE_VENDOR ="http://35.161.99.113:9000/api/vendors/edit" ;
    private EditText txt_vname,txt_vaddress,txt_vemail,txt_vphone;
    private  TextView txt_shopName,txt_gst,txt_gst_number,et_c_person;
    AppCompatButton addvendor;
    private int id;
    private String dbname;
    private RadioGroup rd_gstDetails;
    private RadioButton gst_yes,gst_no;
    private RadioGroup radioGroup;

    private  EditText et_gst_number;
    private Spinner sp_Spinner;
    private static final String TAG = "UpdateVendorDetail";
   private  String stateCode;

    private LinearLayout linearLayout_gst;
    private boolean isvalidPhone;
    private boolean isvalidEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vendor_detail);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        txt_vname=(EditText) findViewById(R.id.update_v_OwnerName);
        txt_shopName=(EditText)findViewById(R.id.update_v_shopname);
        txt_vaddress=(EditText) findViewById(R.id.update_v_Address);
        txt_vemail=(EditText) findViewById(R.id.update_v_Email);
        txt_vphone=(EditText) findViewById(R.id.update_v_mobile);
         txt_gst_number=(EditText)findViewById(R.id.update_v_gstNumber);
         et_c_person=(EditText)findViewById(R.id.update_v_contactpersonName);
        rd_gstDetails=(RadioGroup)findViewById(R.id.gst_details);
        gst_yes=(RadioButton)findViewById(R.id.gst_yes);
        gst_no =(RadioButton)findViewById(R.id.gst_no);

        sp_Spinner=(Spinner)findViewById(R.id.spnn_placeofSuppiler);
        linearLayout_gst=(LinearLayout)findViewById(R.id.linear_update);


        txt_shopName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        et_c_person.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        txt_vname.setFilters(new InputFilter[]{new InputFilter.AllCaps()});





        txt_vphone.addTextChangedListener(new TextWatcher()
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
                if(!Validation.isValidPhone(txt_vphone.getText().toString()))
                {
                    txt_vphone.setError("Invalid mobile");
                    isvalidPhone=false;
                }else if(txt_vphone.getText().toString().trim().length()==10)
                {

                    isvalidPhone=true;
                }else
                {
                    isvalidPhone=false;

                }

            }
        });
        txt_vemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (!Validation.isValidEmail(txt_vemail.getText().toString()))
                {
                    txt_vemail.setError("Invalid Email");
                    isvalidEmail=false;
                }else
                {
                    isvalidEmail=true;
                }

            }
        });


//        txt_vphone.setOnFocusChangeListener(new View.OnFocusChangeListener()
//        {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus)
//            {
//                if(!hasFocus)
//                {
//                    if(!Validation.isValidPhone(txt_vphone.getText().toString()))
//                    {
//                        txt_vphone.setError("Invalid mobile");
//                    }
//                }
//
//                Log.d(TAG, "onFocusChange:Phone "+hasFocus);
//
//
//            }
//        });
//        txt_vemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus)
//            {
//                Log.d(TAG, "onFocusChange: Email"+hasFocus);
//
//                if(!hasFocus)
//                {
//                    if (!Validation.isValidEmail( txt_vemail.getText().toString())) {
//                        txt_vemail.setError("Invalid Email");
//                    }
//                }
//
//            }
//        });

        rd_gstDetails.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.gst_yes:
                        showDetails();
                        break;
                    case R.id.gst_no:
                        hideGstDetails();
                        break;
                }

            }
        });

        et_gst_number=(EditText)findViewById(R.id.update_v_gstNumber);


        addvendor=(AppCompatButton)findViewById(R.id.update_vendorDetails);
        addvendor.setOnClickListener(this);




        Intent intent=getIntent();
        if(intent.hasExtra("v_name"))
        {
            String shopname=intent.getStringExtra("shop_name");
            String name=intent.getStringExtra("v_name");
            String mobile=intent.getStringExtra("v_mobile");
            String address=intent.getStringExtra("v_address");
            String email=intent.getStringExtra("v_email");
            String gst_status=intent.getStringExtra("gststatus");
            String gst_In=intent.getStringExtra("gst_number");
            String created_by=intent.getStringExtra("created_by");
            String created_date=intent.getStringExtra("created_date");
            String  contact_person =intent.getStringExtra("contact_person");
             stateCode=intent.getStringExtra("stateCode");

            Log.d(TAG, "onCreate:StateCode"+stateCode);
            Log.d(TAG, "onCreate: GST Status"+gst_status);
            dbname=intent.getStringExtra("dbname");

            if(gst_status.equalsIgnoreCase("Yes"))
            {
                gst_yes.setChecked(true);
                gst_no.setChecked(false);
                linearLayout_gst.setVisibility(View.VISIBLE);
            }else
            {
                linearLayout_gst.setVisibility(View.GONE);
                gst_yes.setChecked(false);
                gst_no.setChecked(true);
            }
            id=intent.getIntExtra("v_id",0);
            txt_vname.setText(name);
            txt_vaddress.setText(address);
            txt_vemail.setText(email);
            txt_vphone.setText(mobile);
            txt_shopName.setText(shopname);
            txt_gst_number.setText(gst_In);
            et_c_person.setText(contact_person);

        }else
        {

        }
        getAllSuplier();

//        GlobalClass globalClass=new GlobalClass();
//        dbname   =globalClass.getDbName();
    }



    private void hideGstDetails()
    {
        linearLayout_gst.setVisibility(View.GONE);
    }

    private void getAllSuplier()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(UpdateVendorDetail.this).add(new StringRequest(Request.Method.GET, WebApi.GETSUPLER, new Response.Listener<String>() {
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
                        if (error instanceof NoConnectionError)
                        {
                            Toast.makeText(UpdateVendorDetail.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                        }

                    }
                })
        );


    }

    private void processData(String response)
    {
        S_parser S_parser =new S_parser(response);
        S_parser.productParser();
        SuppierAdapter productAdapter=new SuppierAdapter(UpdateVendorDetail.this,R.layout.supplier_list, S_parser.stateName, S_parser.stateCode);
        sp_Spinner.setAdapter(productAdapter);
        sp_Spinner.setSelection(productAdapter.getPosition(stateCode));


    }

    @Override
    public void onClick(View v)
    {
        int id=v.getId();
        switch (id)
        {
            case R.id.update_vendorDetails:
                UpdateRecord();
                break;
        }


    }
    Pattern pattern=Pattern.compile("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}");

    private void UpdateRecord()
    {
        String name=txt_vname.getText().toString().trim();
        String address=txt_vaddress.getText().toString().trim();
        String   mobile=txt_vphone.getText().toString().trim();
        String   email=txt_vemail.getText().toString().trim();
        int radioGroupId=rd_gstDetails.getCheckedRadioButtonId();
        RadioButton  RadioButton=(RadioButton)findViewById(radioGroupId);
        String gstStatus=RadioButton.getText().toString().trim();
        String gst_In=txt_gst_number.getText().toString().trim();
        String shopName=txt_shopName.getText().toString().trim();
         String c_name=et_c_person.getText().toString().trim();
        String stateCode=sp_Spinner.getSelectedItem().toString().trim();


        if(!name.isEmpty()&& !address.isEmpty() && !mobile.isEmpty()  && !c_name.isEmpty() && shopName.length()>0 )
        {
            if(linearLayout_gst.getVisibility()==View.VISIBLE)
            {
                if(!gst_In.isEmpty()) {
                    Matcher matcher = pattern.matcher(gst_In);


                    if (matcher.matches()) {
                        Log.d(TAG, "saveDetails:GST true");
                        if ( isvalidPhone)
                        {
                            Log.d(TAG, "saveDetails:GST true");
                            updateDetails(name, address, mobile, email, gstStatus, gst_In, shopName, c_name, stateCode);
                        } else if (!isvalidPhone) {
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

                if( isvalidPhone)
                {
                    updateDetails(name,address,mobile,email,gstStatus,gst_In,shopName,c_name,stateCode);
                }else if(!isvalidPhone)
                {
                    Toast.makeText(getApplication(),"Invalid Phone",Toast.LENGTH_LONG).show();

                }else {

                    Toast.makeText(getApplication(),"Invalid Email",Toast.LENGTH_LONG).show();
                }


            }


        }else if(email.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Add email", Toast.LENGTH_SHORT).show();

        }else if(shopName.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Add shop", Toast.LENGTH_SHORT).show();
        }else  if(mobile.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Add Mobile Details", Toast.LENGTH_SHORT).show();

        }
    }

    private void updateDetails(final String name, final String address, final String mobile, final String email, final String gstStatus, final String gst_In, final String shopName, final String c_name, final String stateCode)
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Updating...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.UPDATE_VENDOR, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();
                try {
                    loading.dismiss();
                    JSONObject jsonObject=new JSONObject(resp);
                    String message=jsonObject.getString("message");
                    if(message.equalsIgnoreCase("succesfully updated"))
                    {
                        Toast.makeText(UpdateVendorDetail.this, "Vendor Details Updated Succesfully", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                        Intent intent=new Intent();
                        intent.putExtra("Updated",message);
                        setResult(Activity.RESULT_OK,intent);
                        finish();

                    }else
                    {
                        Toast.makeText(UpdateVendorDetail.this, message, Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e)
                {
                    Log.d(" Added Successfully",resp);
                }







            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if(error instanceof NoConnectionError)

                    Toast.makeText(UpdateVendorDetail.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("vendor_id",String.valueOf(id));
                params.put("owner_name", name);
                params.put("address",address);
                params.put("email_id",email);
                params.put("mobile_no",mobile);
                params.put("company",shopName);
                params.put("contact_person",c_name);
                params.put("gst_details",gstStatus);
                params.put("gstin_no",gst_In);
                params.put("stateCode",stateCode);
                Log.d(TAG, "getParams:Update"+params.toString());
                return   params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }


    private void showDetails()
    {
        linearLayout_gst.setVisibility(View.VISIBLE);


    }


}

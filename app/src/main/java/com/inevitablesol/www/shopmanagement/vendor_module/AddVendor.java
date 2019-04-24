package com.inevitablesol.www.shopmanagement.vendor_module;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.Validation.Validation;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.analysis.Total_Analyasis;
import com.inevitablesol.www.shopmanagement.billing_module.Billing_BankDetails;
import com.inevitablesol.www.shopmanagement.billing_module.Parser.S_parser;
import com.inevitablesol.www.shopmanagement.billing_module.SuppierAdapter;
import com.inevitablesol.www.shopmanagement.settings.Billing_Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddVendor extends AppCompatActivity implements View.OnClickListener
{
    private static final String ADD_VENDOR = "http://35.161.99.113:9000/api/vendors/create_new";
    private  EditText  et_vname,et_vaddress,et_vemail,et_vphone,et_shop,et_contact_personname;
    AppCompatButton addvendor;
    private String dbname;
    private RadioGroup rd_gstDetails;
    private  EditText et_gst_number;
    private  RadioGroup radioGroup;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String userId;
    private Spinner sp_supplier;
    private static final String TAG = "AddVendor";

    private LinearLayout linearLayout;

    private  boolean  isvalidEmail,isvalidPhone;

     Pattern pattern=Pattern.compile("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}");


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendord);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        et_vname=(EditText)findViewById(R.id.input_vendorName);
         et_vaddress=(EditText)findViewById(R.id.input_vendorAddress);
         et_vemail=(EditText)findViewById(R.id.input_vendordemail);
         et_vphone=(EditText)findViewById(R.id.input_vendorMobile);
         addvendor=(AppCompatButton)findViewById(R.id.add_vendorDetails);
         et_shop=(EditText)findViewById(R.id.input_vendorShopName) ;
         rd_gstDetails=(RadioGroup)findViewById(R.id.gst_details);
         et_gst_number=(EditText)findViewById(R.id.input_GSTIN);
         et_contact_personname=(EditText)findViewById(R.id.input_vendorContactPersonName);
        addvendor.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        sp_supplier=(Spinner)findViewById(R.id.spnn_placeofSuppiler);
        userId= sharedpreferences.getString("userId","");

          linearLayout=(LinearLayout)findViewById(R.id.gst_linear);
         et_shop.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
         et_vname.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
         et_contact_personname.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        radioGroup=(RadioGroup)findViewById(R.id.gst_details);




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.gst_yes_vendor:
                          showDetails();
                         break;
                    case R.id.gst_no_vendor:
                         hideGstDetails();
                         break;
                }


            }
        });
        getAllSuplier();


        et_vphone.addTextChangedListener(new TextWatcher()
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
                if(!Validation.isValidPhone(et_vphone.getText().toString()))
                {
                    et_vphone.setError("Invalid mobile");
                    isvalidPhone=false;
                }else if(et_vphone.getText().toString().trim().length()==10)
                {

                    isvalidPhone=true;
                }else
                {
                    isvalidPhone=false;

                }

            }
        });
        et_vemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (!Validation.isValidEmail(et_vemail.getText().toString()))
                {
                    et_vemail.setError("Invalid Email");
                    isvalidEmail=false;
                }else
                {
                    isvalidEmail=true;
                }

            }
        });

//        GlobalClass globalClass=new GlobalClass();
//        dbname   =globalClass.getDbName();

    }


    private void getAllSuplier()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(AddVendor.this).add(new StringRequest(Request.Method.GET, WebApi.GETSUPLER, new Response.Listener<String>() {
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
                            Toast.makeText(AddVendor.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                        }

                    }
                })
        );


    }

    private void processData(String response)
    {
        S_parser S_parser =new S_parser(response);
        S_parser.productParser();
        SuppierAdapter productAdapter=new SuppierAdapter(AddVendor.this,R.layout.supplier_list, S_parser.stateName, S_parser.stateCode);
        sp_supplier.setAdapter(productAdapter);
        sp_supplier.setPrompt("Select State");

    }


    private void showDetails()
    {
        linearLayout.setVisibility(View.VISIBLE);


    }

    private void hideGstDetails()
    {
        linearLayout.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v)
    {
        int id=v.getId();

        switch (id)
        {

            case R.id.add_vendorDetails:
                saveDetails();
                 break;
        }

    }

    private void saveDetails()
    {
        //GST_validation gst_validation=new GST_validation();
        String name=et_vname.getText().toString().trim();
         String email=et_vemail.getText().toString().trim();
         String v_phone=et_vphone.getText().toString().trim();
         String v_address=et_vaddress.getText().toString().trim();
        String  v_company=et_shop.getText().toString().trim();
        String  v_contactPersonName=et_contact_personname.getText().toString().trim();
        int radioGroupId=rd_gstDetails.getCheckedRadioButtonId();
        RadioButton  RadioButton=(RadioButton)findViewById(radioGroupId);
        String gstStatus=RadioButton.getText().toString().trim();
        String gst_In=et_gst_number.getText().toString().trim();
        String  stateCode=sp_supplier.getSelectedItem().toString().trim();

        /*if(_isNotEmty())
        {
            if(linearLayout.getVisibility()==View.GONE)
            {
                  if( isvalidPhone)
                  {
                      addDetails(name,email,v_phone,v_address,v_company,v_contactPersonName,gstStatus,gst_In,stateCode);
                  }else if(!isvalidPhone)
                  {
                      Toast.makeText(getApplication(),"Invalid Phone",Toast.LENGTH_LONG).show();

                  }else
                      {

                      Toast.makeText(getApplication(),"Invalid Email",Toast.LENGTH_LONG).show();
                  }

            }else
            {
                if(!gst_In.isEmpty())
                {
                    Matcher matcher = pattern.matcher(gst_In);



                    if(matcher.matches())
                    {
                        Log.d(TAG, "saveDetails:GST true");
                        if(isvalidEmail || isvalidPhone)
                        {
                            addDetails(name,email,v_phone,v_address,v_company,v_contactPersonName,gstStatus,gst_In,stateCode);

                        }else if(!isvalidPhone)
                        {
                            Toast.makeText(getApplicationContext(),"Please Add Valid Phone",Toast.LENGTH_LONG).show();

                        }else
                        {
                            Toast.makeText(getApplicationContext(),"Please Add Valid Email",Toast.LENGTH_LONG).show();
                        }

                    }else
                    {
                        Log.d(TAG, "saveDetails:GST else");
                        Toast.makeText(getApplicationContext(),"Please add  Valid GSTIN ",Toast.LENGTH_LONG).show();
                        et_gst_number.setError("INVALID");
                    }

                }else
                {
                    Toast.makeText(getApplicationContext(),"Please add Valid  GSTIN ",Toast.LENGTH_LONG).show();
                }
            }
        }else
        {
            Toast.makeText(getApplicationContext()," Please Check  Required Field",Toast.LENGTH_LONG).show();
        }*/

//gst validation from add customer
        if(!name.isEmpty()  && v_phone.length()== 10)
        {
            Log.d(TAG, "getData: IF");
            if(linearLayout.getVisibility()==View.VISIBLE)
            {
                if(GST_validation.isValid_Gst(gst_In))
                {
                    addDetails(name,email,v_phone,v_address,v_company,v_contactPersonName,gst_In,gstStatus,stateCode);
                    return;
                }else
                {
                    et_gst_number.setError("Invalid GST");
                    Toast.makeText(getApplication(),"Please Add Valid Gst",Toast.LENGTH_LONG).show();
                }


            }else
            {
                if(isvalidEmail || isvalidPhone)
                {
                    addDetails(name,email,v_phone,v_address,v_company,v_contactPersonName,gstStatus,gst_In,stateCode);

                }else if(!isvalidPhone)
                {
                    Toast.makeText(getApplicationContext(),"Please Add Valid Phone",Toast.LENGTH_LONG).show();

                }else
                {
                    Toast.makeText(getApplicationContext(),"Please Add Valid Email",Toast.LENGTH_LONG).show();
                }
            }

        }else
        {
            Toast.makeText(getApplicationContext()," Please Check  Required Field",Toast.LENGTH_LONG).show();
        }

    }





    private boolean _isNotEmty()
    {
        boolean flag=false;
        if(Validation.hasText(et_vname))
            flag=true;
        if(Validation.hasText(et_vphone))
            flag=true;
        if(Validation.hasText(et_contact_personname))
            flag=true;
        if(Validation.hasText(et_vaddress))
            flag=true;

        return flag;

    }

    private void addDetails(final String name, final String email, final String v_phone,
                            final String v_address, final String v_company, final String v_contactPersonName,
                            final String gstStatus, final String gst_In, final String stateCode)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.ADD_VENDOR, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();
                Log.d(" Added Successfully",resp);
               try
               {

                   JSONObject msg = new JSONObject(response);
                   String message = msg.getString("message");

                    if(message.equalsIgnoreCase("Add new vendor succesfully"))
                    {
                        Toast.makeText(getApplicationContext(),"New Vendor Added Successfully",Toast.LENGTH_LONG).show();
                        finish();
                    }else
                    {
                        Toast.makeText(getApplicationContext(),"Can not Add Details",Toast.LENGTH_LONG).show();
                    }

               }catch (JSONException e)
               {

               }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("owner_name",name);
                params.put("address", v_address);
                params.put("mobile_no",v_phone);
                params.put("email_no",email);
                params.put("company",v_company);
                params.put("contact_person",v_contactPersonName);
                params.put("gst_details",gstStatus);
                params.put("gstin_no",gst_In);
                params.put("created_by",userId);
                params.put("stateCode",stateCode);
                Log.d(TAG, "getParams:"+params.toString());
                return   params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_export_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_bankdetails:

                return true;
            default:

                return super.onOptionsItemSelected(item);
        }



    }

}

package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.billing_module.Parser.S_parser;
import com.inevitablesol.www.shopmanagement.customer_module.AddCustomer;
import com.inevitablesol.www.shopmanagement.customer_module.CustomerParser;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.sql_lite.ItemDetailsParser;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeBill_selectCustomer extends AppCompatActivity implements View.OnClickListener {

    private RadioButton homedilivery_yes;
    private RadioButton homeDelivery_no;
   private  RadioGroup homedilivery;

    private  TextView  et_custName;
    private TextView et_custMobile;
    private TextView et_emailId;
    private TextView et_addressOne;
    private TextView et_address_two;
    private EditText et_addthree;
    private static final String TAG = "MakeBill_selectCustomer";

    private static final String MyPREFERENCES = "MyPrefs";
    private ListView listView;
    private EditText searchCustomer;
    //private SqlDataBase sqlDataBase;
    private SharedPreferences sharedpreferences;
    private String dbname;
    private SimpleAdapter newAdapter;
    private String custSelected;
    private String custId;
    private AppCompatButton nexttomakebiil;
    private RadioButton radioButtonStatus;
    private Spinner spinner;
    private TextView et_gstIn;

    private   ArrayList sp_list=new ArrayList();

    private SqlDataBase sqlDataBase;
    private static final String MySETTINGS = "MySetting";


    private LinearLayout linear_home,linear_Address;
    
    private  ImageView add_newCustomer;
    private int CUSTOMER=101;
    private Context context=MakeBill_selectCustomer.this;
    private SuppierAdapter productAdapter;
    private GlobalPool globalPool;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_billing_makebill);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        homedilivery=(RadioGroup)findViewById(R.id.home_delivery_button);
        homeDelivery_no=(RadioButton)findViewById(R.id.bill_addressNo);
        homedilivery_yes =(RadioButton)findViewById(R.id.bill_addressYes);
        globalPool= (GlobalPool)this.getApplication();
        et_emailId=(TextView)findViewById(R.id.input_billMailId);
        et_addressOne=(TextView)findViewById(R.id.input_billAddress_one);
        et_address_two=(TextView)findViewById(R.id.input_billAddtwo);
        et_addthree=(AppCompatEditText)findViewById(R.id.input_billaddThree);
        spinner=(Spinner)findViewById(R.id.billing_supplier);
        et_gstIn=(TextView)findViewById(R.id.input_bill_gst);
        nexttomakebiil=(AppCompatButton)findViewById(R.id.proceedtobilling);

        nexttomakebiil.setOnClickListener(this);
        et_custName=(TextView)findViewById(R.id.input_billcustName);
        et_custMobile=(TextView)findViewById(R.id.input_bill_MobileNumber);
        et_emailId=(TextView)findViewById(R.id.input_billMailId);

        linear_home=(LinearLayout)findViewById(R.id.linear_hommeStatus);
        linear_Address=(LinearLayout)findViewById(R.id.linear_address);
        add_newCustomer=(ImageView)findViewById(R.id.add_newcustomer);
        add_newCustomer.setOnClickListener(this);

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
                        showAddress();
                        break;
                    case R.id.bill_addressNo:
                        hideAddress();
                        break;
                }
            }
        });

        listView=(ListView)findViewById(R.id.list_customerInfo);
        searchCustomer=(EditText)findViewById(R.id.input_searchCustomer);

        //custName=(Spinner)findViewById(R.id.spnn_custmomerName);
       // sqlDataBase=new SqlDataBase(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));


        try
        {
            sharedpreferences = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
            String Status = (sharedpreferences.getString("home_DeliveryStatus", null));
            if(Status!=null)
            {
                if (Status.equalsIgnoreCase("Yes"))
                {
                    linear_Address.setVisibility(View.VISIBLE);
                    linear_home.setVisibility(View.VISIBLE);

                } else {

                    linear_Address.setVisibility(View.GONE);
                    linear_home.setVisibility(View.GONE);
                }
            }
            Log.i(TAG, "onCreate:Status"+Status);

        }catch (NullPointerException e)
        {
            Log.i(TAG, "onCreate:error"+e);
        }catch (Exception e)
        {
            Log.i(TAG, "onCreate:error"+e);
        }
        getAllCustomerDetails();

        getAllSuplier();
        sqlDataBase=new SqlDataBase(this);


        if( sqlDataBase.check_items())
        {
            Log.d(TAG, "onResponse:DB Available");
            sqlDataBase.updateQty();

        }else
        {
            getAllItemDetails();

        }


        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight((int)height/2);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

      et_gstIn.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {

          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {

          }

          @Override
          public void afterTextChanged(Editable s)
          {
              if(s.toString().length()<15)
              {
                  et_gstIn.setError("Invalid");
              }

          }
      });


    }

    private void getAllSuplier()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(MakeBill_selectCustomer.this).add(new StringRequest(Request.Method.GET, WebApi.GETSUPLER, new Response.Listener<String>() {
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
                    Toast.makeText(MakeBill_selectCustomer.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        })
        );


    }

    private void processData(String response)
    {
        S_parser S_parser =new S_parser(response);
        S_parser.productParser();
          productAdapter=new SuppierAdapter(MakeBill_selectCustomer.this,R.layout.supplier_list, S_parser.stateName, S_parser.stateCode);
        spinner.setAdapter(productAdapter);


    }


    private void getAllCustomerDetails()
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETCUSTINFO, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {

                    loading.dismiss();
                    String resp = response.toString().trim();
                    Log.d(TAG, "onResponse:CustDetails "+resp);
                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else
                    {
                        CustomerParser customerParser=new CustomerParser(resp);
                        customerParser.custDetails();
                        //=customerParser.makeArray();

                        final List<Map<String, String>> cust = new ArrayList<Map<String, String>>();
                        Map<String, String> map;
                        int counter = CustomerParser.cust_Name.length;
                        for (int i = 0; i < counter; i++)
                        {
                            map = new HashMap<>();
                            map.put("name", CustomerParser.cust_Name[i]);
                            map.put("mobile", CustomerParser.mobile[i]);
                            cust.add(map);
                            Log.d(TAG, "onResponse: "+cust);
                        }

                        newAdapter = new SimpleAdapter(MakeBill_selectCustomer.this, cust, R.layout.list_customerinfo, new String[]{"name", "mobile"}, new int[]{R.id.customer_name, R.id.customer_mobile});
                        listView.setAdapter(newAdapter);
                        listView.setVisibility(View.INVISIBLE);

                        searchCustomer.addTextChangedListener(new TextWatcher()
                        {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                listView.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                                listView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void afterTextChanged(Editable s)
                            {
                                MakeBill_selectCustomer.this.newAdapter.getFilter().filter(s);
                                listView.setVisibility(View.VISIBLE);
                            }
                        });


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Map custSel = (Map) newAdapter.getItem(position);
                                custSelected = (String) custSel.get("name") + " " + custSel.get("mobile");
                                searchCustomer.setText(custSelected);

                                listView.setVisibility(View.GONE);

                                getCustomerInfo((String)custSel.get("name"));

                            }
                        });


//


                    }
                }catch(Exception e)
                {
                    Log.d("Exception", "" + e);
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
            params.put("dbname", dbname);
            return params;
        }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showAddress()
    {
        linear_Address.setVisibility(View.VISIBLE);
        //linear_home.setVisibility(View.VISIBLE);
//        et_addressOne.setEnabled(true);
//        et_address_two.setEnabled(true);
//        et_addthree.setEnabled(true);

    }

    private void hideAddress()
    {
        linear_Address.setVisibility(View.GONE);
        //linear_home.setVisibility(View.GONE);
       // et_addressOne.setText("");
//        et_addressOne.setEnabled(false);
//        et_address_two.setEnabled(false);
//        et_addthree.setEnabled(false);

    }


    public void onRadioButtonClicked(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.bill_addressYes:
                if (checked)
                    break;
            case R.id.bill_addressNo:
                if (checked)
                    Toast.makeText(getApplicationContext(),"hide Address",Toast.LENGTH_LONG).show();
                    // Ninjas rule
                    break;
        }
    }



    private void getCustomerInfo(final String name)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETCUSTBYNAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();
                Log.d("cust Info",resp);
                try
                {

                    JSONObject jsonObject=new JSONObject(resp);
                    if(jsonObject.getString("status_code").equalsIgnoreCase("0"))
                    {
                        try
                        {
                            Log.d("success", "0");
                            JSONArray jsonArray = jsonObject.getJSONArray("cust_records");
                            JSONObject             jsonObject1 = jsonArray.getJSONObject(0);
                            String name =          jsonObject1.getString("customer_name");
                            String mobile =        jsonObject1.getString("mobile_number");
                            String email =         jsonObject1.getString("email_id");
                            String home_delivery = jsonObject1.getString("home_delivery");
                            String address =    jsonObject1.getString("address");

                            String gstin= jsonObject1.getString("gstin");
                            String placeofSupply=jsonObject1.getString("place_of_supply");
                               et_gstIn.setText(gstin);
                                if(placeofSupply!=null)
                                {
                                           int position=     productAdapter.getPosition(placeofSupply);
                                            spinner.setSelection(position);
                                }

                                  custId=      jsonObject1.getString("customer_id");
                            if (home_delivery.equalsIgnoreCase("yes"))
                            {
                                homedilivery_yes.setChecked(true);
                                homeDelivery_no.setChecked(false);
                            } else
                            {
                                homeDelivery_no.setChecked(true);
                                homedilivery_yes.setChecked(false);
                            }
                            et_custName.setText(name);
                            et_custMobile.setText(mobile);
                            et_emailId.setText(email);
                            et_addressOne.setText(address);


                            hidedata();
                            always_Hide();



                        }catch (Exception e)
                        {
                            Log.d("Exception",""+e);
                        }



                    }else
                    {
                        et_addressOne.setText("");
                        et_custMobile.setText("");
                        et_emailId.setText("");
                        et_addressOne.setText("");
                        Toast.makeText(getApplication(),"record not found",Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e)
                {


                }




            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(MakeBill_selectCustomer.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("customer_name",name);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void hidedata()
    {
        Log.d(TAG, "hidedata() called");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
    }
    private  void always_Hide()
    {
        Log.i(TAG, "always_Hide:");
        InputMethodManager inputManager =
                (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View v)
    {
        try
        {
            int id=v.getId();

            switch (id)
            {
                case R.id.proceedtobilling:
                    saveBillingDetails();

                    break;
                
                case R.id.add_newcustomer:
                      saveCustomer();
                    break;
                default:
                    Toast.makeText(getApplication(),"no View Found",Toast.LENGTH_LONG).show();
            }


        }catch (NullPointerException e)
        {
            Toast.makeText(getApplication()," View not Found",Toast.LENGTH_LONG).show();
        }
    }

    private void saveCustomer() 
    {
        Log.d(TAG, "saveCustomer:");

        Intent intent=new  Intent(context,AddCustomer.class);
        intent.putExtra("customer","true");
        startActivityForResult((intent),CUSTOMER);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CUSTOMER && resultCode == Activity.RESULT_OK)
        {
            try
            {
                getAllCustomerDetails();


            } catch (Exception e)
            {
                Log.d(TAG, "onActivityResult:" + e);
            }
        }
    }


        private void saveBillingDetails()
    {
        try
        {
            String homedelivery_status=null;
             String addTwo="null";
             String  addThree="null";

            String name=et_custName.getText().toString().trim();
            String mobile=et_custMobile.getText().toString().trim();
            String email=et_emailId.getText().toString().trim();
            String state_id=spinner.getSelectedItem().toString();

            String  gstin=et_gstIn.getText().toString().trim();
            String addOne = null;
            try
            {
                 addOne=et_addressOne.getText().toString().trim();
                  addTwo =et_address_two.getText().toString().trim();
                  addThree=et_addthree.getText().toString().trim();
                int  selectedId = homedilivery.getCheckedRadioButtonId();


                // find the radiobutton by returned id
                radioButtonStatus = (RadioButton) findViewById(selectedId);
                homedelivery_status=radioButtonStatus.getText().toString().trim();
            }catch (Exception e)
            {
                homedelivery_status="No";

            }


            if (homedelivery_status.equalsIgnoreCase("Yes"))
            {

                if(name.length()>=1 && mobile.length()>=10  && addOne.length()>=1)
                {
                    Intent intent = new Intent(MakeBill_selectCustomer.this, MakeBillingCart.class);
                    intent.putExtra("name",name);
                    intent.putExtra("custid",custId);
                    intent.putExtra("mobile",mobile);
                    intent.putExtra("email",email);
                    intent.putExtra("addOne",addOne);
                    intent.putExtra("addTwo",addTwo);
                    intent.putExtra("addThree",addThree);
                    intent.putExtra("gst",gstin);
                    intent.putExtra("supplier",state_id);
                    intent.putExtra("h_status",homedelivery_status);
                    intent.putExtra("dbname",dbname);

                    startActivity(intent);
                    // addBillingDetails(name,mobile,email,addOne,addTwo,addThree,homedelivery_status);

                }else if(TextUtils.isEmpty(addOne))
                {
                    et_addressOne.setError("Invalid");
                  // Toast.makeText(getApplication(),"Plz Add All field",Toast.LENGTH_LONG).show();

                }
                else if (TextUtils.isEmpty(name))
                {
                    et_custName.setError("Invalid Name");
                    Toast.makeText(context, "Please Add Name", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(email))
                {
                    et_custMobile.setError("Invalid Mobile");
                    Toast.makeText(context, "Please Email", Toast.LENGTH_SHORT).show();
                }
            }else
            {

                if(name.length()>=1 && mobile.length()>=10 )
                {
                    Intent intent = new Intent(MakeBill_selectCustomer.this, MakeBillingCart.class);
                    intent.putExtra("name",name);
                    intent.putExtra("custid",custId);
                    intent.putExtra("mobile",mobile);
                    intent.putExtra("email",email);
                    intent.putExtra("addOne",addOne);
                    intent.putExtra("addTwo",addTwo);
                    intent.putExtra("addThree",addThree);
                    intent.putExtra("gst",gstin);
                    intent.putExtra("supplier",state_id);
                    intent.putExtra("h_status",homedelivery_status);
                    startActivity(intent);

                    // addBillingDetails(name,mobile,email,addOne,addTwo,addThree,homedelivery_status);
                }else
                {
                    Toast.makeText(getApplication(),"Plz Add All field",Toast.LENGTH_LONG).show();
                }

            }


        }catch (NullPointerException e)
        {

        }


    }



    private void getAllItemDetails()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_STOCKDETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();

                Log.d("Item Details  Billing",resp);
                try {
                    JSONObject jsonObject= new JSONObject(resp);
                                    String message= jsonObject.getString("message");
                       if(!message.equalsIgnoreCase("Data not available"))
                       {


                ItemDetailsParser itemDetailsParser=new ItemDetailsParser(resp);
                itemDetailsParser.parseItemList();
                String  selectedItemQty="0";
                try
                {

                    String []    p_id           =ItemDetailsParser.product_id;
                    String []   item_id        =ItemDetailsParser.item_id;
                    String []   item_name      =ItemDetailsParser.item_name;
                    String []   stock_qty      =ItemDetailsParser.stock_qt;
                    String[]   item_mrp        =ItemDetailsParser.item_mrp;
                    String[]  purchase_price   =ItemDetailsParser.purchase_price;
                    String[]  gst              =ItemDetailsParser.gst;
                    String []  totalPrice      =ItemDetailsParser.total_price;
                    String [] discount         =ItemDetailsParser.discount;
                    String[] unit_Price        =ItemDetailsParser.unit_price;
                    String [] hsnCode  =          ItemDetailsParser.hsn;
                    String[] status=            ItemDetailsParser.status;

                    String[] mUnit=ItemDetailsParser.mUnit;
                    String [] unit=ItemDetailsParser.unit;
                    String [] itembarcode=ItemDetailsParser.item_barcode;
                    String [] shortcut=ItemDetailsParser.shortcut;




                    for(int i=0;i<p_id.length;i++)
                    {
                        if(Integer.parseInt(stock_qty[i])> 0 || status[i].equalsIgnoreCase("infinite"))
                        {
                            sqlDataBase.addItemDetails(p_id[i],item_id[i],item_name[i],stock_qty[i],selectedItemQty,item_mrp[i],purchase_price[i],
                                    totalPrice[i],gst[i],discount[i],unit_Price[i],hsnCode[i],status[i], mUnit[i], unit[i], itembarcode[i], shortcut[i]);

                        }else
                        {
                            Log.d(TAG, "onResponse: QTY less then Zero "+Integer.parseInt(stock_qty[i]));
                            Log.d(TAG, "onResponse: Menu Item "+ status[i]);
                        }



                    }
                }catch (NumberFormatException e)
                {

                }catch (NullPointerException e)
                {

                }catch (Exception e)
                {

                }
                       }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                Log.d("new Db Installed","");










            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(MakeBill_selectCustomer.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                if(globalPool.isMenuItemStatus())
                {
                    params.put("menu", "enable");

                }else
                {
                    params.put("menu", "disable");

                }

                params.put("dbname", dbname);
                Log.d(TAG, "getParams: Menu Param"+params.toString());
                return params;

            }

        } ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}

package com.inevitablesol.www.shopmanagement.MenuItemModule;

import android.app.Activity;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.inevitablesol.www.shopmanagement.product_info.Add_New_product;
import com.inevitablesol.www.shopmanagement.product_info.Spinner_prodectParser;
import com.inevitablesol.www.shopmanagement.product_info.adapter.ProductAdapter;
import com.inevitablesol.www.shopmanagement.product_info.adapter.ProductParser;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class AddMenuItem extends AppCompatActivity implements View.OnClickListener {

    private static final String GET_PRODUCT = " http://35.161.99.113:9000/webapi/product/list";
    private static final String ADD_MENU_ITEM = "http://35.161.99.113:9000/webapi/product/create_menu_item";

    AppCompatButton bt_addstock;
    LinearLayout ll_addproductType;
    Spinner mProductSpinner;
    TextInputEditText input_productName,input_company,input_owner,input_hsn_san,input_specification,input_discountper,input_discountrs,input_unitPrice;
    private String mProductId;
   private SharedPreferences sharedpreferences;
    private  static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private static final String TAG = "AddMenuItem";
    private SqlDataBase sqlDataBase;
    private Spinner spinnerGst;
    private TextInputEditText input_discountedPrice,input_totalPrice;



    private  TextWatcher    discountedPer;
    private  TextWatcher    discounrtedRs;
    private double gstPercentage;

    private  Spinner sub_unit;
    private  Spinner measurment_unit;
    private ArrayAdapter<CharSequence> adapter;
    private ArrayAdapter<CharSequence> adapter2;

    private EditText  et_itemBarcode;
    private String shortCut;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_menu_item);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        bt_addstock = (AppCompatButton) findViewById(R.id.bt_addstock);
        bt_addstock.setOnClickListener(this);
        ll_addproductType = (LinearLayout) findViewById(R.id.ll_addproductType);
        ll_addproductType.setOnClickListener(this);
        mProductSpinner = (Spinner) findViewById(R.id.spnn_product);

        input_productName = (TextInputEditText) findViewById(R.id.input_productName);
        input_hsn_san = (TextInputEditText) findViewById(R.id.input_hsn_san_code);
       // input_company = (TextInputEditText) findViewById(R.id.input_company);
      //  input_owner = (TextInputEditText) findViewById(R.id.input_owner);
        input_unitPrice=(TextInputEditText)findViewById(R.id.input_unitPrice);
        input_discountedPrice=(TextInputEditText)findViewById(R.id.input_discountedPrice);
        spinnerGst=(Spinner)findViewById(R.id.et_gstpercentage);
        input_discountrs=(TextInputEditText)findViewById(R.id.input_dicountinRs);
        input_discountper=(TextInputEditText)findViewById(R.id.input_discountper);
        input_specification = (TextInputEditText) findViewById(R.id.input_specification);
        input_totalPrice=(TextInputEditText)findViewById(R.id.input_totalPrice);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        sqlDataBase=new SqlDataBase(this);

      //  et_itemBarcode=(EditText)findViewById(R.id.et_itemBarcode);

        sub_unit=(Spinner)findViewById(R.id.sub_measurementUnit);
        measurment_unit=(Spinner)findViewById(R.id.measurementUnit);

        adapter  = ArrayAdapter.createFromResource(this,
                R.array.measurement_unit, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measurment_unit.setAdapter(adapter);


        measurment_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item was selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id)
            {

                try {
                    Log.d(TAG, "onItemSelected: "+pos);
                    Log.d(TAG, "onItemSelected: "+measurment_unit.getSelectedItem().toString());
                    String unit=measurment_unit.getSelectedItem().toString();
                    if(unit.equalsIgnoreCase("Unit"))
                    {
                        adapter2  = ArrayAdapter.createFromResource(AddMenuItem.this,
                                R.array.unit, android.R.layout.simple_spinner_item);

                    }else if(unit.equalsIgnoreCase("Gram"))
                    {
                        adapter2  = ArrayAdapter.createFromResource(AddMenuItem.this,
                                R.array.gram, android.R.layout.simple_spinner_item);

                    }else if(unit.equalsIgnoreCase("liter"))
                    {
                        adapter2  = ArrayAdapter.createFromResource(AddMenuItem.this,
                                R.array.liter, android.R.layout.simple_spinner_item);

                    }
                    else if(unit.equalsIgnoreCase("meter"))
                    {
                        adapter2  = ArrayAdapter.createFromResource(AddMenuItem.this,
                                R.array.meter, android.R.layout.simple_spinner_item);

                    }
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sub_unit.setAdapter(adapter2);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            public void onNothingSelected(AdapterView parent)
            {
                // Do nothing.
            }
        });




        spinnerGst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Toast.makeText(SelectedItemFromStock.this, " WOW ", Toast.LENGTH_SHORT).show();
                try
                {
                    Double  disprice= Double.valueOf(input_discountedPrice.getText().toString().trim());
                    if(disprice!=null)
                    {
                        Log.d(TAG, "onItemSelected: Price" + disprice);
                        String gst = spinnerGst.getSelectedItem().toString().trim();
                        gstPercentage= Double.parseDouble(gst);
                        Log.d(TAG, "changeBasePrice:GST-"+gstPercentage);
                        double gstCal=(gstPercentage/100.0f);
                        Log.d(TAG, "GST: "+gstCal);
                        double totalgstPrice=disprice*gstCal;
                        Log.d(TAG, "onItemSelected: TotaslPrice"+totalgstPrice);
                        Double AmountWtiGst=disprice+totalgstPrice;
                        Double amount =Math.round(AmountWtiGst*100.0)/100.0;
                        input_totalPrice.setText(String.valueOf(amount));

                        Log.d(TAG, "onItemSelected:gstPer" + gst);
                    }

                }catch (Exception e)
                {
                    Log.d(TAG, "onItemSelected:Exception"+e);
                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });





        discounrtedRs=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

                try
                {

                    double unitPrice = Double.parseDouble(input_unitPrice.getText().toString());
                    double discount = Double.parseDouble(input_discountrs.getText().toString());
                    double totalAmount=unitPrice-discount;
                    Log.i(TAG, "afterTextChanged:TotalPrice"+totalAmount);
                    input_discountedPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));

                    // formula   discount in percentage
                    double dis_temp=  discount/unitPrice *100;

                    Log.d(TAG, "afterTextChanged:Discount in Percentage "+dis_temp);

                    //et_discountedPer.removeTextChangedListener((TextWatcher) et_discountedPer);
                    input_discountper.removeTextChangedListener(discountedPer);
                    input_discountper.setText(String.valueOf(Math.round(dis_temp*100.0)/100.0));
                    input_discountper.addTextChangedListener(discountedPer);

                    try
                    {
                        Double  disprice= Double.valueOf(input_discountedPrice.getText().toString().trim());
                        if(disprice!=null)
                        {
                            Log.d(TAG, "onItemSelected: Price" + disprice);
                            String gst = spinnerGst.getSelectedItem().toString().trim();
                            gstPercentage= Double.parseDouble(gst);
                            Log.d(TAG, "changeBasePrice:GST-"+gstPercentage);
                            double gstCal=(gstPercentage/100.0f);
                            Log.d(TAG, "GST: "+gstCal);
                            double totalgstPrice=disprice*gstCal;
                            Log.d(TAG, "onItemSelected: TotaslPrice"+totalgstPrice);
                            Double AmountWtiGst=disprice+totalgstPrice;
                            Double amount =Math.round(AmountWtiGst*100.0)/100.0;
                            input_totalPrice.setText(String.valueOf(amount));

                            Log.d(TAG, "onItemSelected:gstPer" + gst);
                        }

                    }catch (Exception e)
                    {
                        Log.d(TAG, "onItemSelected:Exception"+e);
                    }




                }catch (NumberFormatException e)
                {
                    double unitPrice =  Double.parseDouble(input_unitPrice.getText().toString());
                    double discount =0.0 ;
                    double totalAmount=unitPrice-discount;
                    input_discountper.setText("0.0");
                    input_discountedPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));
                    try
                    {
                        Double  disprice= Double.valueOf(input_discountedPrice.getText().toString().trim());
                        if(disprice!=null)
                        {
                            Log.d(TAG, "onItemSelected: Price" + disprice);
                            String gst = spinnerGst.getSelectedItem().toString().trim();
                            gstPercentage= Double.parseDouble(gst);
                            Log.d(TAG, "changeBasePrice:GST-"+gstPercentage);
                            double gstCal=(gstPercentage/100.0f);
                            Log.d(TAG, "GST: "+gstCal);
                            double totalgstPrice=disprice*gstCal;
                            Log.d(TAG, "onItemSelected: TotaslPrice"+totalgstPrice);
                            Double AmountWtiGst=disprice+totalgstPrice;
                            Double amount =Math.round(AmountWtiGst*100.0)/100.0;
                            input_totalPrice.setText(String.valueOf(amount));

                            Log.d(TAG, "onItemSelected:gstPer" + gst);
                        }

                    }catch (Exception Ee)
                    {
                        Log.d(TAG, "onItemSelected:Exception"+e);
                    }


                }





            }
        };

        discountedPer=new TextWatcher()
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

                try
                {
                    double unitPrice = Double.parseDouble(input_unitPrice.getText().toString());
                    double discountPer = Double.parseDouble(input_discountper.getText().toString());
                    double dis  = discountPer/100.0f;
                    double totalAmount=unitPrice-(unitPrice*dis);

                    Log.d(TAG, "afterTextChanged:Per"+dis);

                    Log.i(TAG, "afterTextChanged:TotalPrice"+totalAmount);
                    input_discountedPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));

                    //  add value in discount in Rs
                    double dis_tem=dis*unitPrice;
                    //  double discountinRss=dis_tem/100.0f;
                    Log.d(TAG, "afterTextChanged:DiscountinRs"+dis_tem);
                    input_discountrs.removeTextChangedListener(discounrtedRs);
                    input_discountrs.setText(String.valueOf(Math.round(dis_tem*100.0)/100.0));
                    input_discountrs.addTextChangedListener(discounrtedRs);
                    try
                    {
                        Double  disprice= Double.valueOf(input_discountedPrice.getText().toString().trim());
                        if(disprice!=null)
                        {
                            Log.d(TAG, "onItemSelected: Price" + disprice);
                            String gst = spinnerGst.getSelectedItem().toString().trim();
                            gstPercentage= Double.parseDouble(gst);
                            Log.d(TAG, "changeBasePrice:GST-"+gstPercentage);
                            double gstCal=(gstPercentage/100.0f);
                            Log.d(TAG, "GST: "+gstCal);
                            double totalgstPrice=disprice*gstCal;
                            Log.d(TAG, "onItemSelected: TotaslPrice"+totalgstPrice);
                            Double AmountWtiGst=disprice+totalgstPrice;
                            Double amount =Math.round(AmountWtiGst*100.0)/100.0;
                            input_totalPrice.setText(String.valueOf(amount));

                            Log.d(TAG, "onItemSelected:gstPer" + gst);
                        }

                    }catch (Exception e)
                    {
                        Log.d(TAG, "onItemSelected:Exception"+e);
                    }


                }catch (NumberFormatException e)
                {
                    double unitPrice =  Double.parseDouble(input_unitPrice.getText().toString());
                    double discount =0.0 ;
                    double totalAmount=unitPrice-discount;
                    input_discountedPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));
                    try
                    {
                        Double  disprice= Double.valueOf(input_discountedPrice.getText().toString().trim());
                        if(disprice!=null)
                        {
                            Log.d(TAG, "onItemSelected: Price" + disprice);
                            String gst = spinnerGst.getSelectedItem().toString().trim();
                            gstPercentage= Double.parseDouble(gst);
                            Log.d(TAG, "changeBasePrice:GST-"+gstPercentage);
                            double gstCal=(gstPercentage/100.0f);
                            Log.d(TAG, "GST: "+gstCal);
                            double totalgstPrice=disprice*gstCal;
                            Log.d(TAG, "onItemSelected: TotaslPrice"+totalgstPrice);
                            Double AmountWtiGst=disprice+totalgstPrice;
                            Double amount =Math.round(AmountWtiGst*100.0)/100.0;
                            input_totalPrice.setText(String.valueOf(amount));

                            Log.d(TAG, "onItemSelected:gstPer" + gst);
                        }

                    }catch (Exception Ee)
                    {
                        Log.d(TAG, "onItemSelected:Exception"+e);
                    }

                }

            }


        };

        input_discountrs.addTextChangedListener(discounrtedRs);
        input_discountper.addTextChangedListener(discountedPer);


            getAppProductDetails();

        try
        {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(mProductSpinner);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width=dm.widthPixels;
            int height=dm.heightPixels;
            double wi=(double)width/(double)dm.xdpi;
            double hi=(double)height/(double)dm.ydpi;
            // Set popupWindow height to 500px
            popupWindow.setHeight( (height/2));
        }
        catch(NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e)
        {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                getAppProductDetails();
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                //Write your code if there's no result
            }
        }
    }


    private void getAppProductDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {

                    Log.d(TAG, "onResponse() called with: response = [" + response + "]");

                    String resp = response.toString().trim();

//                    Spinner_prodectParser productParser=new Spinner_prodectParser(response);
//                    productParser.productParser();
//                    ProductParser productParser = new ProductParser(resp);
//                    productParser.productParser();
//                    ProductAdapter productAdapter = new ProductAdapter(AddMenuItem.this, R.layout.product_list, ProductParser.productName, ProductParser.productId);


                    Spinner_prodectParser productParser=new Spinner_prodectParser(response);
                    productParser.productParser();
                    ProductAdapter productAdapter= new ProductAdapter(AddMenuItem.this,R.layout.product_list,Spinner_prodectParser.productName,Spinner_prodectParser.productId);
                    mProductSpinner.setAdapter(productAdapter);
                    mProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                            mProductId = Spinner_prodectParser.productId[position];
//                            mProductType = ProductParser.productName[position];
//                            String itemName = mName.getText().toString().trim();



                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                } catch (Exception e)
                {
                    Log.d("Exception", "" + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(AddMenuItem.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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


    public void add_menuItem()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_MENU_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String resp = response.toString().trim();
                Log.d("Add Response",resp);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("Add new item succesfully"))
                    {
                        sqlDataBase.deleteItemTable();
                        Toast.makeText(AddMenuItem.this, "Item added successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(AddMenuItem.this, "Please connect to the internet before continuing.", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                String product_type = mProductSpinner.getSelectedItem().toString();
                String item_name = input_productName.getText().toString();
                String company = "not Defined";//input_company.getText().toString();
               // String owner = input_owner.getText().toString();
                String specification       = input_specification.getText().toString();
                String hsn_ssc_code        = input_hsn_san.getText().toString();
                String discount_percentage=input_discountper.getText().toString().trim();
                String discount_rs =input_discountrs.getText().toString().trim();
                String gst         =spinnerGst.getSelectedItem().toString().trim();
                String unitprice=input_unitPrice.getText().toString().trim();
                String  discountedPrice=input_discountedPrice.getText().toString().trim();
                String  totalPrice=input_totalPrice.getText().toString().trim();
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                 String itembarcode= "";//et_itemBarcode.getText().toString().trim();
                String u_id       =   sharedpreferences.getString("userId", "");
                String modifiedBy =    sharedpreferences.getString("userId", "");
                String  unit= sub_unit.getSelectedItem().toString().trim();
                try {
                    shortCut=unit.substring(unit.indexOf("(")+1,unit.indexOf(")"));
                    Log.d(TAG, "AddToCart: Short cut"+shortCut);
                } catch (Exception e)
                {

                    e.printStackTrace();
                }

                Map<String,String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("product_id",mProductId);
                params.put("item_name",item_name);
                params.put("company",company);
                params.put("owner","owner");
                params.put("specification",specification);
                params.put("hsn_ssc_code",hsn_ssc_code);
                params.put("storage_qty","0");
                params.put("stock_qty","0");
                params.put("purchase_price","0");
                params.put("gst",gst);
                params.put("mrp","0");
                params.put("created_by",u_id);
                params.put("unit_price",unitprice);
                params.put("total_price",totalPrice);
                params.put("discPrice",discountedPrice);
                params.put("discount",discount_rs);
                params.put("shortCut",shortCut);
                params.put("discPercent",discount_percentage);
                params.put("measurement_unit",measurment_unit.getSelectedItem().toString());
                params.put("unit",sub_unit.getSelectedItem().toString().trim());
                params.put("item_barcode",itembarcode);
                Log.d("MENU PARAMS",params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.bt_addstock:
                if (checkValidation())
                {
                    String product_type = mProductSpinner.getSelectedItem().toString();
                    Log.d(TAG, "onClick: Product"+product_type);
                         if(product_type.equalsIgnoreCase("0"))
                         {
                             Toast.makeText(this, "Please Select Product Type", Toast.LENGTH_SHORT).show();
                         }else
                         {
                             add_menuItem();
                         }

                }else{
                    Toast.makeText(this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.ll_addproductType:
                Intent addIntent = new Intent(this, AddNewProduct.class);
                startActivityForResult(addIntent, 1);
                break;
        }

    }

    private boolean checkValidation()
    {
        boolean ret=true;

        if (!Validation.hasText(input_productName)) ret = false;
      //  if (!Validation.hasText(input_company)) ret = false;
       // if (!Validation.hasText(input_owner)) ret = false;
        if (!Validation.hasText(input_specification)) ret = false;
        if (!Validation.hasText(input_hsn_san)) ret = false;


        return ret;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }
}

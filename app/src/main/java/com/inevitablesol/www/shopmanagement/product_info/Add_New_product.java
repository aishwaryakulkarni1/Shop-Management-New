package com.inevitablesol.www.shopmanagement.product_info;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.product_info.adapter.ProductAdapter;
import com.inevitablesol.www.shopmanagement.product_info.adapter.ProductParser;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Add_New_product extends AppCompatActivity implements View.OnClickListener {
    private ImageView addProduct;

    private static final String TAG = "Add_New_product";
    private static final String MyPREFERENCES = "MyPrefs" ;
    private String dbname;
    private EditText mpoduct;
    SharedPreferences sharedpreferences;
    private Spinner s_productType,p_type;
   private AppCompatButton saveDetails;
    private Context context=Add_New_product.this;
    private EditText productItem,et_companyName,et_company_owner,et_specification,et_hsn_code;

    private  Spinner m_unit;
    private EditText itembarcode;
 private GlobalPool globalPool;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new_product);
        globalPool=(GlobalPool)this.getApplicationContext();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        addProduct = (ImageView) findViewById(R.id.add_productItem);
        addProduct.setOnClickListener(this);
        saveDetails=(AppCompatButton)findViewById(R.id.bt_addnewProductDetails);
        saveDetails.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        itembarcode=(EditText)findViewById(R.id.item_bar_code) ;
        m_unit=(Spinner)findViewById(R.id.sp_measurementUnit);
        productItem=(EditText)findViewById(R.id.new_productItem);
        et_companyName=(EditText)findViewById(R.id.new_productCompany);
        et_company_owner=(EditText)findViewById(R.id.new_product_Owner);
        et_hsn_code=(EditText)findViewById(R.id.input_hsn_san);
        et_specification=(EditText)findViewById(R.id.new_productSpecification);
        p_type=(Spinner)findViewById(R.id.spnn_product_type);
        userId= sharedpreferences.getString("userId","");

        productItem.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.measurement_unit, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_unit.setAdapter(adapter);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));


        try
        {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(p_type);

            // Set popupWindow height to 500px
            popupWindow.setHeight((int)height/2);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        getProduct();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) 
        {
            case R.id.add_productItem:
                Intent intent = new Intent(Add_New_product.this, NewProduct.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.bt_addnewProductDetails:
                saveDetails();
                break;
                default:
                    Toast.makeText(context, "Wrong choice", Toast.LENGTH_SHORT).show();

        }

    }

    private void saveDetails()
    {

        String p_id = (String) p_type.getSelectedItem().toString().trim();
        String item_name = productItem.getText().toString().trim();
        String c_name = et_companyName.getText().toString().trim();
        String c_owner = et_company_owner.getText().toString().trim();
        String has_code = et_hsn_code.getText().toString().trim();
        String specification = et_specification.getText().toString().trim();
        String measeumentUnit=m_unit.getSelectedItem().toString().trim();
        String barcode=itembarcode.getText().toString().trim();
        if(item_name.length()>0 && !TextUtils.isEmpty(item_name) && !p_id.equals("0") && c_name.length()>0)
       {
           try
           {
               if(!(measeumentUnit.equalsIgnoreCase("Select Measurment unit")))
               {
                   final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);

                   JsonObject jsonObject = new JsonObject();
                   jsonObject.addProperty("p_id", "pritam");
                   jsonObject.addProperty("dbname", dbname);
                   jsonObject.addProperty("product_id", p_id);
                   jsonObject.addProperty("item_name", item_name);
                   jsonObject.addProperty("company", c_name);
                   jsonObject.addProperty("owner", c_owner);
                   jsonObject.addProperty("specification", specification);
                   jsonObject.addProperty("hsn_ssc_code", has_code);
                   jsonObject.addProperty("storage_qty", "0");
                   jsonObject.addProperty("stock_qty", "0");
                   jsonObject.addProperty("purchase_price", "0");
                   jsonObject.addProperty("gst", "0");
                   jsonObject.addProperty("mrp", "0");
                   jsonObject.addProperty("discount", "0");
                   jsonObject.addProperty("unit_price", "0");
                   jsonObject.addProperty("total_price", "0");
                   jsonObject.addProperty("discPrice", "0");
                   jsonObject.addProperty("discPercent", "0");
                   jsonObject.addProperty("measurement_unit", measeumentUnit);
                   jsonObject.addProperty("unit", " ");
                   jsonObject.addProperty("shortCode"," ");
                   jsonObject.addProperty("created_by",userId);
                   jsonObject.addProperty("item_barcode", barcode);
                   Log.d(TAG, "saveDetails: TEST");
                   Log.d(TAG, "saveDetails: Item" + jsonObject.toString());
                   Ion.with(context)
                           .load("http://35.161.99.113:9000/webapi/product/create_item")
                           .progressHandler(new ProgressCallback() 
                           {

                               @Override
                               public void onProgress(long downloaded, long total) {

                               }
                           })
                           .setJsonObjectBody(jsonObject)
                           .asJsonObject()
                           .setCallback(new FutureCallback<JsonObject>() 
                           {
                               @Override
                               public void onCompleted(Exception e, JsonObject result) {
                                   Log.d(TAG, "onCompleted: " + result);
                                   Toast.makeText(context, "Item added Succesfully", Toast.LENGTH_SHORT).show();
                                   Log.d("resonse", result.toString());
                                   loading.dismiss();
                                   finish();
                                   // do stuff with the result or error
                               }
                           });
               }else
                   
                   {

                   Toast.makeText(context, "PLease select Unit", Toast.LENGTH_SHORT).show();
                       return;
               }

           } catch (Exception e) {
               e.printStackTrace();
           }


        }else if(p_id.equals("0"))
        {
            Toast.makeText(context, "Please Select Product Type", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(context, "Please Add Item", Toast.LENGTH_SHORT).show();
        }

//    }


    }

//
//
//
//    private void storeDetails(final String p_id, final String item_name, final String c_name, final String c_owner, final String has_code, final String specification)
//    {
//        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
//        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebApi.ADD_NEW_ITEM, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response)
//            {
//
//                Toast.makeText(context, "Item added Succesfully", Toast.LENGTH_SHORT).show();
//                Log.d("resonse",response);
//                loading.dismiss();
//                finish();
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }){
//
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError
//                {
//                Map<String, String> params = new HashMap<>();
//                Log.d("dbname", dbname);
//                    params.put("dbname", dbname);
//                    params.put("product_id", p_id);
//                    params.put("item_name", item_name);
//                    params.put("company", c_name);
//                    params.put("owner", c_owner);
//                    params.put("specification", specification);
//                    params.put("hsn_ssc_code", has_code);
//                    params.put("storage_qty", "0");
//                    params.put("stock_qty", "0");
//                    params.put("purchase_price", "0");
//                    params.put("gst", "0");
//                    params.put("mrp", "0");
//                    params.put("discount", "0");
//                    params.put("unit_price", "0");
//                    params.put("total_price", "0");
//                    params.put("discPrice", "0");
//                    params.put("discPercent", "0");
//                    Log.d(TAG, "getParams:"+ params.toString());
//
//
//                return params;
//            }
//
//
//
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2

            if (resultCode == Activity.RESULT_OK)
            {
                String result = data.getStringExtra("MESSAGE");
                Log.d(TAG, "onActivityResult: "+result);
                if (result.equalsIgnoreCase("added"))
                {
                    getProduct();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }

    }

    private void getProduct()
    {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_NEW_PRODUCT, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try
                {
                    Log.d("response",response);

                    Spinner_prodectParser productParser=new Spinner_prodectParser(response);
                              productParser.productParser();
                    ProductAdapter productAdapter= new ProductAdapter(Add_New_product.this,R.layout.product_list,Spinner_prodectParser.productName,Spinner_prodectParser.productId);
                   // s_productType.setAdapter(productAdapter);
                    p_type.setAdapter(productAdapter);

                }catch (Exception e)
                {

                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(Add_New_product.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

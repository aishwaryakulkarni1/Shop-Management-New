package com.inevitablesol.www.shopmanagement.MenuItemModule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.inevitablesol.www.shopmanagement.ItemModule.Parser.CalculatePrice;

import com.inevitablesol.www.shopmanagement.Validation.Validation;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.inevitablesol.www.shopmanagement.R;

public class EditMenuItemActivity extends AppCompatActivity implements View.OnClickListener,CalculatePrice {

    private static final String UPDATE_STOCK = "http://35.161.99.113:9000/webapi/product/edit";
    private String name, product_type, mrp, specification, product_id, item_id, hsn, company, storage_qty, item_price;

   private TextInputEditText input_specification, input_mrp, input_basePrice, input_productType, input_productName, input_item_price, input_storage_qty, input_company, input_hsn;

    AppCompatButton update_itemDetails;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private EditText et_total_price, et_discount, et_input_unitPrice;
    private String unit_price, discount, total_price;
    private static final String TAG = "EditMenuItemActivity";
    private double gstPercentage = 0.0;

    private String gst;
    private TextWatcher textWatcherTP;
    private TextWatcher textWatcherDS;
    private EditText et_gstPercentage;
    private SqlDataBase sqlDataBase;
    private Spinner et_gst;

    private TextView txt_itemName,txt_productName,txt_company,txt_specification,txt_hsn,txt_productType;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        input_productName = (TextInputEditText) findViewById(R.id.input_productName);
        input_productType = (TextInputEditText) findViewById(R.id.input_productType);
        input_mrp = (TextInputEditText) findViewById(R.id.input_mrp);
        input_basePrice = (TextInputEditText)findViewById(R.id.input_unitPrice);

        input_specification = (TextInputEditText) findViewById(R.id.input_specification);
        input_item_price = (TextInputEditText) findViewById(R.id.input_item_price);
        input_storage_qty = (TextInputEditText) findViewById(R.id.input_storage_qty);
        input_company = (TextInputEditText) findViewById(R.id.input_company);
        input_hsn = (TextInputEditText) findViewById(R.id.input_hsn);
        update_itemDetails = (AppCompatButton) findViewById(R.id.update_itemDetails);
        update_itemDetails.setOnClickListener(this);
        et_input_unitPrice = (EditText) findViewById(R.id.input_unitPrice);
        et_discount = (EditText) findViewById(R.id.input_Discount);
        et_total_price = (EditText) findViewById(R.id.input_totalitemPrice);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        et_gst=(Spinner)findViewById(R.id.et_gstpercentage);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gst_tax_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
               et_gst.setAdapter(adapter);

        Intent intent = getIntent();
        sqlDataBase=new SqlDataBase(this);

        if (intent.hasExtra("name"))
        {
            name = intent.getStringExtra("name");
            product_id = intent.getStringExtra("product_id");
            product_type = intent.getStringExtra("product_type");
            mrp = intent.getStringExtra("mrp");
            specification = intent.getStringExtra("specification");
            item_id = intent.getStringExtra("item_id");

            hsn = intent.getStringExtra("hsn");
            company = intent.getStringExtra("company");
            storage_qty = intent.getStringExtra("storage_qty");
            item_price = intent.getStringExtra("item_price");
            unit_price = intent.getStringExtra("unit_price");
            total_price = intent.getStringExtra("total_price");
            discount = intent.getStringExtra("discount");
            gstPercentage = Double.parseDouble(intent.getStringExtra("gst"));

            input_productName.setText(name);
            input_productType.setText(product_type);
            input_mrp.setText(mrp);
            input_specification.setText(specification);

            input_hsn.setText(hsn);
            input_company.setText(company);
            input_storage_qty.setText(storage_qty);
            input_item_price.setText(item_price);
            et_discount.setText(discount);
            et_total_price.setText(total_price);
            et_input_unitPrice.setText(unit_price);

        }


        input_mrp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeBasePrice(s.toString());

            }
        });


        textWatcherTP = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_discount.removeTextChangedListener(textWatcherDS);
                calculateTotalDiscount();
                et_discount.addTextChangedListener(textWatcherDS);
            }
        };


        input_basePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: ");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged:");

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    double basep = Double.parseDouble(input_basePrice.getText().toString());
                    Log.d(TAG, "afterTextChanged: " + basep);
                    calCulateTotalPrice(basep);


                } catch (NumberFormatException e) {

                } catch (NullPointerException e) {

                }
            }
        });
        textWatcherDS = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Log.d(TAG, "Discount" + s);
                    if (s.toString().length() > 0)
                    {
                        et_total_price.removeTextChangedListener(textWatcherTP);
                        Double base_p = Double.parseDouble(input_basePrice.getText().toString().trim());
                        Double discount = Double.parseDouble(et_discount.getText().toString().trim());

                        if(discount<=base_p)
                        {
                            Log.d(TAG, "DiscountPrice:" + discount);
                            double totalDiscount = base_p - discount;
                            double totalPriceWithgst = totalDiscount + (totalDiscount * gstPercentage / 100.0f);

                            et_total_price.setText(String.valueOf(Math.round(totalPriceWithgst * 100.0) / 100.0));

                            et_total_price.addTextChangedListener(textWatcherTP);
                        }else
                        {

                            et_discount.setText(String.valueOf(0.0));


                            Toast.makeText(EditMenuItemActivity.this, "Discount Cant exceed  Base Price ", Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {

                }
            }
        };

        et_discount.addTextChangedListener(textWatcherDS);

    }


    private void calCulateTotalPrice(double base_p) {
        String discount = et_discount.getText().toString().trim();
        Log.d(TAG, "DiscountPrice:" + discount);
        double totalDiscount = base_p - Double.parseDouble(discount);
        double totalPriceWithgst = totalDiscount + (totalDiscount * gstPercentage / 100.0f);
        Log.d(TAG, "TotalPrice:" + totalPriceWithgst);
        ///et_totalPrice.removeTextChangedListener(textWatcherClass);
        et_total_price.setText(String.valueOf(Math.round(totalPriceWithgst * 100.0) / 100.0f));
    }


    private void changeBasePrice(String s) {
        try {
            Log.d(TAG, "changeBasePrice:GST-" + gstPercentage);
            double gstCal = 1 + (gstPercentage / 100.0f);
            Log.d(TAG, "GST: " + gstCal);
            double basePrice = Double.parseDouble(s) / gstCal;
            Log.d(TAG, "changeBasePrice: " + basePrice);
            double base_price = Math.round(basePrice * 100.0) / 100;
            Log.d(TAG, "changeBasePrice: " + base_price);
            input_basePrice.setText(String.valueOf(base_price));


        } catch (NumberFormatException e)
        {
//           Toast.makeText(context, "Invalid Mrp", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "changeBasePrice: " + e);

        } catch (NullPointerException e) {
            //Toast.makeText(context, "Invalid Mrp", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "changeBasePrice: " + e);

        } catch (Exception e) {
            // Toast.makeText(context, "Invalid Mrp", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "changeBasePrice: " + e);
        }


    }

    public void update_stock()
    {
        final String gstpercentage=et_gst.getSelectedItem().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_STOCK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.toString().trim();
                Log.d("Update Resp", resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String message = null;
                    message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("succesfully updated"))
                    {
                        sqlDataBase.deleteItemTable();
                        Toast.makeText(EditMenuItemActivity.this, "Item updated successfully. ", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(EditMenuItemActivity.this, "Please connect to the internet before continuing.", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                params.put("item_id", item_id);
                params.put("item_name", input_productName.getText().toString());
                params.put("specification", input_specification.getText().toString());
                params.put("mrp", input_mrp.getText().toString());
                params.put("product_id", product_id);
                params.put("total_price", et_total_price.getText().toString().trim());
                params.put("discount", et_discount.getText().toString().trim());
                params.put("unit_price", et_input_unitPrice.getText().toString().trim());
                params.put("gst",gstpercentage);
                Log.d("UpdateParameter", params.toString());
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
            case R.id.update_itemDetails:
                if (checkValidation()) {
                    update_stock();
                }

                break;
        }
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(input_productName)) ret = false;
        if (!Validation.hasText(input_productType)) ret = false;
        if (!Validation.hasText(input_mrp)) ret = false;
        if (!Validation.hasText(input_specification)) ret = false;


        return ret;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "On bp", Toast.LENGTH_SHORT).show();
                finish();
                Log.d("itemId", "onOptionsItemSelected:");
                return true;
        }

        //return true;
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause:");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }


    @Override
    public void calculateTotalPrice() {
        Log.d(TAG, "Discount:InterFace");
        String base_p = input_basePrice.getText().toString().trim();
        String discount = et_discount.getText().toString().trim();
        Log.d(TAG, "DiscountPrice:" + discount);
        double totalDiscount = Double.parseDouble(base_p) - Double.parseDouble(discount);
        double totalPriceWithgst = totalDiscount + (totalDiscount * gstPercentage / 100.0f);

        et_total_price.setText(String.valueOf(Math.round(totalPriceWithgst * 100.0) / 100.0));


    }

    @Override
    public void calculateTotalDiscount() {
        try {
            Log.d(TAG, "TotalPrice:Interface ");

            String BasePrice = input_basePrice.getText().toString().trim();
            Double TotalPrice = Double.parseDouble(et_total_price.getText().toString().trim());
            Double mrp = Double.parseDouble(input_mrp.getText().toString().trim());
            if (TotalPrice <= mrp) {
                double gstCal = 1 + (gstPercentage / 100.0f);
                double TotalDisCount = Double.parseDouble(BasePrice) - (TotalPrice) / gstCal;

                et_discount.setText(String.valueOf(Math.round(TotalDisCount * 100.0) / 100.0));


            } else {
                et_total_price.removeTextChangedListener(textWatcherTP);
                et_total_price.setText(String.valueOf(mrp));
                et_total_price.addTextChangedListener(textWatcherTP);

                Toast.makeText(EditMenuItemActivity.this, "Total Price  Cant exceed  Mrp", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {


        } catch (NullPointerException e) {

        } catch (Exception e) {

        }

    }
}

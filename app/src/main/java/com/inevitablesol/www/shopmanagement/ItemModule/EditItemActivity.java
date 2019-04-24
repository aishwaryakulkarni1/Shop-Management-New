package com.inevitablesol.www.shopmanagement.ItemModule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.inevitablesol.www.shopmanagement.ItemModule.Parser.CalculatePrice;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.Validation.Validation;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener,CalculatePrice {

    private static final String UPDATE_STOCK = "";
    private String name, product_type, mrp, specification, product_id, item_id,hsn,company,storage_qty,item_price;

    TextInputEditText input_specification, input_mrp, input_productType, input_productName,input_item_price,input_storage_qty,input_company,input_hsn;

    AppCompatButton update_itemDetails;

    private EditText et_BasePrice,et_totalPrice;
    private TextInputEditText et_discount;
    private String discount,totalPrice,unitPrice;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private static final String TAG = "EditItemActivity";
    private Context context=EditItemActivity.this;
    private double gstPercentage;
    private  Spinner et_gstPercentege;
    private TextWatcher textWatcherTP;
    private TextWatcher textWatcherDS;
    ArrayAdapter<CharSequence> adapter;
  

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        input_productName = (TextInputEditText) findViewById(R.id.input_productName);
        input_productType = (TextInputEditText) findViewById(R.id.input_productType);
        input_mrp = (TextInputEditText) findViewById(R.id.input_mrp);
        input_specification = (TextInputEditText) findViewById(R.id.input_specification);
        input_item_price = (TextInputEditText) findViewById(R.id.input_item_price);
        input_storage_qty = (TextInputEditText) findViewById(R.id.input_storage_qty);
        input_company =      (TextInputEditText) findViewById(R.id.input_company);
        input_hsn = (TextInputEditText) findViewById(R.id.input_hsn);
        et_totalPrice=(EditText)findViewById(R.id.input_totalitemPrice);
        et_gstPercentege=(Spinner)findViewById(R.id.et_gstpercentage);

        update_itemDetails = (AppCompatButton) findViewById(R.id.update_itemDetails);
        update_itemDetails.setOnClickListener(this);
        et_BasePrice=(EditText)findViewById(R.id.input_unitPrice);


        et_discount=(TextInputEditText)findViewById(R.id.input_Discount);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
         adapter = ArrayAdapter.createFromResource(this, R.array.gst_tax_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_gstPercentege.setAdapter(adapter);


        Intent intent = getIntent();
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
            totalPrice=intent.getStringExtra("totalPrice");
            unitPrice=intent.getStringExtra("unitPrice");
            discount=intent.getStringExtra("discount");
            String gst=intent.getStringExtra("gst");
            try
            {

                Log.d("Extras",hsn+" "+company+" "+storage_qty+" "+item_price+"gst"+gst);
                gstPercentage=Double.parseDouble(gst);

            }catch (Exception e)
            {
                Log.d(TAG, "onCreate:"+e);
            }



            input_productName.setText(name);
            input_productType.setText(product_type);
            input_mrp.setText(mrp);
            input_specification.setText(specification);
            input_hsn.setText(hsn);
            input_company.setText(company);
            input_storage_qty.setText(storage_qty);
            input_item_price.setText(item_price);
            et_discount.setText(discount);
            et_BasePrice.setText(unitPrice);
            et_totalPrice.setText(totalPrice);
            if (gst!=null)
            {

                int spinnerPosition = adapter.getPosition(gst.trim());
                et_gstPercentege.setSelection(spinnerPosition);
            }
        }

        input_mrp.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
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
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                et_discount.removeTextChangedListener(textWatcherDS);
                calculateTotalDiscount();
                et_discount.addTextChangedListener(textWatcherDS);
            }
        };

        et_totalPrice.addTextChangedListener(textWatcherTP);

        et_BasePrice.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                Log.d(TAG, "beforeTextChanged: ");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Log.d(TAG, "onTextChanged:");

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    double basep = Double.parseDouble(et_BasePrice.getText().toString());
                    Log.d(TAG, "afterTextChanged: " + basep);
                    calCulateTotalPrice(basep);


                } catch (NumberFormatException e)
                {

                } catch (NullPointerException e)
                {

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

                         et_totalPrice.removeTextChangedListener(textWatcherTP);
                         Double base_p = Double.parseDouble(et_BasePrice.getText().toString().trim());
                          Double discount = Double.parseDouble(et_discount.getText().toString().trim());
                        String gstPer= (String) et_gstPercentege.getSelectedItem();
                        gstPercentage= Double.parseDouble(gstPer);
                        if(discount<=base_p)
                        {
                            Log.d(TAG, "DiscountPrice:" + discount);
                            double totalDiscount = base_p - discount;
                            Log.d(TAG, "afterTextChanged:Gst"+gstPercentage);
                            double totalPriceWithgst = totalDiscount + (totalDiscount * gstPercentage / 100.0f);

                            et_totalPrice.setText(String.valueOf(Math.round(totalPriceWithgst * 100.0) / 100.0));

                            et_totalPrice.addTextChangedListener(textWatcherTP);
                        }else
                        {

                            et_discount.setText(String.valueOf(0.0));


                            Toast.makeText(EditItemActivity.this, "Discount Cant exceed  Base Price ", Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {

                }
            }
        };

        et_discount.addTextChangedListener(textWatcherDS);


    }

    // Calulating Base Price
    private void changeBasePrice(String s)
    {
       try
       {
           String gstPer= (String) et_gstPercentege.getSelectedItem();
           gstPercentage= Double.parseDouble(gstPer);
           Log.d(TAG, "changeBasePrice:GST-"+gstPercentage);
           double gstCal=1+(gstPercentage/100.0f);
           Log.d(TAG, "GST: "+gstCal);
           double basePrice=Double.parseDouble(s)/gstCal;
           Log.d(TAG, "changeBasePrice: "+basePrice);
                  double base_price= Math.round(basePrice*100.0)/100;
           Log.d(TAG, "changeBasePrice: "+base_price);
              et_BasePrice.setText(String.valueOf(base_price));


       }catch (NumberFormatException e)
       {
//           Toast.makeText(context, "Invalid Mrp", Toast.LENGTH_SHORT).show();
           Log.d(TAG, "changeBasePrice: "+e);

       }catch (NullPointerException e)
       {
           //Toast.makeText(context, "Invalid Mrp", Toast.LENGTH_SHORT).show();
           Log.d(TAG, "changeBasePrice: "+e);

       }catch (Exception e)
       {
          // Toast.makeText(context, "Invalid Mrp", Toast.LENGTH_SHORT).show();
           Log.d(TAG, "changeBasePrice: "+e);
       }


    }

    private void calCulateTotalPrice(double base_p)
    {
        String  discount=et_discount.getText().toString().trim();
        String gstPer= (String) et_gstPercentege.getSelectedItem();
        gstPercentage= Double.parseDouble(gstPer);
        Log.d(TAG, "DiscountPrice:"+discount);
         double totalDiscount=base_p-Double.parseDouble(discount);
         double totalPriceWithgst=totalDiscount+(totalDiscount*gstPercentage/100.0f);
        Log.d(TAG, "TotalPrice:"+totalPriceWithgst);
        ///et_totalPrice.removeTextChangedListener(textWatcherClass);
        et_totalPrice.setText(String.valueOf( Math.round(totalPriceWithgst*100.0)/100.0f));
    }

    public void update_stock()
    {
        final String gstpercentage = et_gstPercentege.getSelectedItem().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.UPDATE_STOCK, new Response.Listener<String>() {
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
                        Toast.makeText(EditItemActivity.this, "Item updated successfully. ", Toast.LENGTH_SHORT).show();
//                        Intent returnIntent = new Intent();
//                        setResult(Activity.RESULT_OK, returnIntent);

                        finish();
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(EditItemActivity.this, "Please connect to the internet before continuing.", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                params.put("item_id", item_id);
                params.put("item_name", input_productName.getText().toString());
                params.put("specification", input_specification.getText().toString());
                params.put("mrp", input_mrp.getText().toString());
                params.put("product_id", product_id);
                params.put("unit_price",et_BasePrice.getText().toString());
                params.put("discount",et_discount.getText().toString());
                params.put("total_price",et_totalPrice.getText().toString().trim());
                params.put("gst",gstpercentage);
                Log.d("Update Params",params.toString());
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

    private boolean checkValidation()
    {
        boolean ret = true;

        if (!Validation.hasText(input_productName)) ret = false;
        if (!Validation.hasText(input_productType)) ret = false;
        if (!Validation.hasText(input_mrp)) ret = false;
        if (!Validation.hasText(input_specification)) ret = false;


        return ret;
    }

    @Override
    public void calculateTotalPrice() 
    {
        String gstPer= (String) et_gstPercentege.getSelectedItem();
        gstPercentage= Double.parseDouble(gstPer);
        Log.d(TAG, "Discount:InterFace");
                        String base_p=et_BasePrice.getText().toString().trim();
                    String  discount=et_discount.getText().toString().trim();
                    Log.d(TAG, "DiscountPrice:"+discount);
                    double totalDiscount=Double.parseDouble(base_p)-Double.parseDouble(discount);
                    double totalPriceWithgst=totalDiscount+(totalDiscount*gstPercentage/100.0f);

                     et_totalPrice.setText(String.valueOf(Math.round(totalPriceWithgst*100.0)/100.0));


    }

    @Override
    public void calculateTotalDiscount()
    {
        try {
            Log.d(TAG, "TotalPrice:Interface ");

            String BasePrice = et_BasePrice.getText().toString().trim();
            Double TotalPrice = Double.parseDouble(et_totalPrice.getText().toString().trim());
            Double mrp= Double.parseDouble(input_mrp.getText().toString().trim());
            String gstPer= (String) et_gstPercentege.getSelectedItem();
            gstPercentage= Double.parseDouble(gstPer);
            if(TotalPrice<=mrp)
            {
                double gstCal = 1 + (gstPercentage / 100.0f);
                double TotalDisCount = Double.parseDouble(BasePrice) - (TotalPrice) / gstCal;

                et_discount.setText(String.valueOf(Math.round(TotalDisCount * 100.0) / 100.0));


            }else
            {
                et_totalPrice.removeTextChangedListener(textWatcherTP);
                et_totalPrice.setText(String.valueOf(mrp));
                et_totalPrice.addTextChangedListener(textWatcherTP);

                Toast.makeText(EditItemActivity.this, "Total Price  Cant exceed  Mrp", Toast.LENGTH_SHORT).show();
            }
        }catch (NumberFormatException e)
        {


        }catch (NullPointerException e)
        {

        }catch (Exception e)
        {

        }



    }


}
//
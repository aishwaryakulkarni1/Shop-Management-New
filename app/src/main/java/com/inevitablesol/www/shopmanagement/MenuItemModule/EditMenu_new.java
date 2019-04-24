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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.inevitablesol.www.shopmanagement.ItemModule.StockInfo;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditMenu_new extends AppCompatActivity implements View.OnClickListener {
    private TextView txt_itemName,txt_productName,txt_company,txt_specification,txt_hsn,txt_productType;
    AppCompatButton update_itemDetails;
    private static final String UPDATE_STOCK = "http://35.161.99.113:9000/webapi/product/edit";
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private EditText et_current_price, et_discount, et_input_unitPrice;
    private String unit_price, discount, total_price;
    private static final String TAG = "EditMenuItemActivity";
    private double gstPercentage = 0.0;

    private SqlDataBase sqlDataBase;
    private Spinner et_gst;
    private String name, product_type, mrp, specification, product_id, item_id, hsn, company, storage_qty, item_price;
    private  TextView txt_dPrice,txt_DRs,txt_DPER,txt_DUnit,txt_gst;
     TextInputEditText edit_discounted_price,et_totalPrice,et_discounted_ps;

    private AppCompatButton bt_editMenu;

    private  Spinner m_spinner;
    private String discountPer;
    private String discountPrice;


    private  TextWatcher    discountedPer;
    private  TextWatcher    discounrtedRs;

    private  TextView txt_itemBarcode;
    private  TextView txt_mUnit;
    private  TextView txt_unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_menu_item);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        txt_productName=(TextView)findViewById(R.id.edit_menu_productName);
        txt_productType=(TextView)findViewById(R.id.edit_menu_productType);
       // txt_company=(TextView)findViewById(R.id.edit_menu_productCompany);
        txt_specification=(TextView)findViewById(R.id.edit_menu_productSpecification);
        txt_hsn=(TextView)findViewById(R.id.edit_menu_productHSN);
        txt_dPrice=(TextView)findViewById(R.id.edit_menu_DicountedPr);
        txt_DRs=(TextView)findViewById(R.id.edit_menuDicountRs);
        txt_DUnit=(TextView)findViewById(R.id.edit_menuUnit);
        txt_DPER=(TextView)findViewById(R.id.edit_menuDiscout_per);
        et_input_unitPrice = (EditText) findViewById(R.id.edit_menu_unitPrice);
        et_discount = (EditText) findViewById(R.id.edit_menu_discount);
        et_current_price = (EditText) findViewById(R.id.edit_menu_price);
        txt_gst=(TextView)findViewById(R.id.txt_currentGstPercent);
        bt_editMenu=(AppCompatButton)findViewById(R.id.edit_menuItem);
        bt_editMenu.setOnClickListener(this);
        et_totalPrice=(TextInputEditText)findViewById(R.id.edit_menu_totalPrice);
        m_spinner=(Spinner)findViewById(R.id.et_gstpercentage_menu);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gst_tax_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_spinner.setAdapter(adapter);

        et_discounted_ps=(TextInputEditText)findViewById(R.id.edit_menu_dis_ps);

        edit_discounted_price = (TextInputEditText) findViewById(R.id.edit_menu_discountedPrice);

        sqlDataBase=new SqlDataBase(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        txt_mUnit=(TextView)findViewById(R.id.txt_munit);
        txt_unit=(TextView)findViewById(R.id.txt_unit);
        //txt_itemBarcode=(TextView)findViewById(R.id.txt_itembarcode);






        Intent intent=getIntent();


        if (intent.hasExtra("name"))
        {
            StockInfo stockInfo=(StockInfo)getIntent().getSerializableExtra("stockinfo");
            Log.d(TAG, "onCreate: "+stockInfo);
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
             discountPer=intent.getStringExtra("disPer");
              discountPrice=intent.getStringExtra("discPrice");

             txt_DUnit.setText(unit_price);
             txt_DRs.setText(discount);
             txt_dPrice.setText(discountPrice);



            gstPercentage = Double.parseDouble(intent.getStringExtra("gst"));
          //  Log.d(TAG, "onCreate: GSt"+gstPercentage);
             txt_gst .setText(String.valueOf(gstPercentage));
            txt_productName.setText(name);
            txt_productType.setText(product_type);
            txt_DPER.setText(discountPer);
            txt_specification.setText(specification);
            txt_hsn.setText(hsn);
            //txt_company.setText(company);
          //  txt_storageQty.setText(storage_qty);
           // txt_originalPrice.setText(item_price);
             et_discount.setText(discount);
             et_current_price.setText(total_price);
             et_input_unitPrice.setText(unit_price);
             edit_discounted_price.setText(total_price);

          //  txt_itemBarcode.setText(stockInfo.getItembarcode());
            txt_mUnit.setText(stockInfo.getMunit());
            txt_unit.setText(stockInfo.getUnit());


        }


        m_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Toast.makeText(SelectedItemFromStock.this, " WOW ", Toast.LENGTH_SHORT).show();
                try
                {
                    Double  disprice= Double.valueOf(edit_discounted_price.getText().toString().trim());
                    if(disprice!=null)
                    {
                        Log.d(TAG, "onItemSelected: Price" + disprice);
                        String gst = m_spinner.getSelectedItem().toString().trim();
                        gstPercentage= Double.parseDouble(gst);
                        Log.d(TAG, "changeBasePrice:GST-"+gstPercentage);
                        double gstCal=(gstPercentage/100.0f);
                        Log.d(TAG, "GST: "+gstCal);
                        double totalgstPrice=disprice*gstCal;
                        Log.d(TAG, "onItemSelected: TotaslPrice"+totalgstPrice);
                        Double AmountWtiGst=disprice+totalgstPrice;
                        Double amount =Math.round(AmountWtiGst*100.0)/100.0;
                        et_totalPrice.setText(String.valueOf(amount));
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

                    double unitPrice = Double.parseDouble(et_input_unitPrice.getText().toString());
                    double discount = Double.parseDouble(et_discount.getText().toString());
                    double totalAmount=unitPrice-discount;
                    Log.i(TAG, "afterTextChanged:TotalPrice"+totalAmount);
                    edit_discounted_price.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));

                    // formula   discount in percentage
                    double dis_temp=  discount/unitPrice *100;

                    Log.d(TAG, "afterTextChanged:Discount in Percentage "+dis_temp);

                    //et_discountedPer.removeTextChangedListener((TextWatcher) et_discountedPer);
                    et_discounted_ps.removeTextChangedListener(discountedPer);
                    et_discounted_ps.setText(String.valueOf(Math.round(dis_temp*100.0)/100.0));
                    et_discounted_ps.addTextChangedListener(discountedPer);
                    try
                    {
                        Double  disprice= Double.valueOf(edit_discounted_price.getText().toString().trim());
                        if(disprice!=null)
                        {
                            Log.d(TAG, "onItemSelected: Price" + disprice);
                            String gst = m_spinner.getSelectedItem().toString().trim();
                            gstPercentage= Double.parseDouble(gst);
                            Log.d(TAG, "changeBasePrice:GST-"+gstPercentage);
                            double gstCal=(gstPercentage/100.0f);
                            Log.d(TAG, "GST: "+gstCal);
                            double totalgstPrice=disprice*gstCal;
                            Log.d(TAG, "onItemSelected: TotaslPrice"+totalgstPrice);
                            Double AmountWtiGst=disprice+totalgstPrice;
                            Double amount =Math.round(AmountWtiGst*100.0)/100.0;
                            et_totalPrice.setText(String.valueOf(amount));

                            Log.d(TAG, "onItemSelected:gstPer" + gst);
                        }

                    }catch (Exception Ee)
                    {
                        Log.d(TAG, "onItemSelected:Exception"+Ee);
                    }






                }catch (NumberFormatException e)
                {
                    double unitPrice =  Double.parseDouble(et_input_unitPrice.getText().toString());
                    double discount =0.0 ;
                    double totalAmount=unitPrice-discount;
                    et_discounted_ps.setText("0.0");
                    edit_discounted_price.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));
                    try
                    {
                        Double  disprice= Double.valueOf(edit_discounted_price.getText().toString().trim());
                        if(disprice!=null)
                        {
                            Log.d(TAG, "onItemSelected: Price" + disprice);
                            String gst = m_spinner.getSelectedItem().toString().trim();
                            gstPercentage= Double.parseDouble(gst);
                            Log.d(TAG, "changeBasePrice:GST-"+gstPercentage);
                            double gstCal=(gstPercentage/100.0f);
                            Log.d(TAG, "GST: "+gstCal);
                            double totalgstPrice=disprice*gstCal;
                            Log.d(TAG, "onItemSelected: TotaslPrice"+totalgstPrice);
                            Double AmountWtiGst=disprice+totalgstPrice;
                            Double amount =Math.round(AmountWtiGst*100.0)/100.0;
                            et_totalPrice.setText(String.valueOf(amount));

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
                    double unitPrice = Double.parseDouble(et_input_unitPrice.getText().toString());
                    double discountPer = Double.parseDouble(et_discounted_ps.getText().toString());
                    double dis  = discountPer/100.0f;
                    double totalAmount=unitPrice-(unitPrice*dis);

                    Log.d(TAG, "afterTextChanged:UnitPrice"+unitPrice);
                    Log.d(TAG, "afterTextChanged:DiscountPer"+discountPer);
                    Log.d(TAG, "afterTextChanged:Dis"+dis);

                    Log.d(TAG, "afterTextChanged:Per"+dis);

                    Log.i(TAG, "afterTextChanged:TotalPrice"+totalAmount);
                    edit_discounted_price.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));

                    //  add value in discount in Rs
                    double dis_tem=dis*unitPrice;
                    //  double discountinRss=dis_tem/100.0f;
                    Log.d(TAG, "afterTextChanged:DiscountinRs"+dis_tem);
                    et_discount.removeTextChangedListener(discounrtedRs);
                    et_discount.setText(String.valueOf(Math.round(dis_tem*100.0)/100.0));
                    et_discount.addTextChangedListener(discounrtedRs);
                    try
                    {
                        Double  disprice= Double.valueOf(edit_discounted_price.getText().toString().trim());
                        if(disprice!=null)
                        {
                            Log.d(TAG, "onItemSelected: Price" + disprice);
                            String gst = m_spinner.getSelectedItem().toString().trim();
                            gstPercentage= Double.parseDouble(gst);
                            Log.d(TAG, "changeBasePrice:GST-"+gstPercentage);
                            double gstCal=(gstPercentage/100.0f);
                            Log.d(TAG, "GST: "+gstCal);
                            double totalgstPrice=disprice*gstCal;
                            Log.d(TAG, "onItemSelected: TotaslPrice"+totalgstPrice);
                            Double AmountWtiGst=disprice+totalgstPrice;
                            Double amount =Math.round(AmountWtiGst*100.0)/100.0;
                            et_totalPrice.setText(String.valueOf(amount));

                            Log.d(TAG, "onItemSelected:gstPer" + gst);
                        }

                    }catch (Exception Ee)
                    {
                        Log.d(TAG, "onItemSelected:Exception"+Ee);
                    }

                }catch (NumberFormatException e)
                {
                    Log.d(TAG, "afterTextChanged:Exception in Per"+e);
                    double unitPrice =  Double.parseDouble(et_input_unitPrice.getText().toString());
                    double discount =0.0 ;
                    double totalAmount=unitPrice-discount;
                    edit_discounted_price.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));
                    try
                    {
                        Double  disprice= Double.valueOf(edit_discounted_price.getText().toString().trim());
                        if(disprice!=null)
                        {
                            Log.d(TAG, "onItemSelected: Price" + disprice);
                            String gst = m_spinner.getSelectedItem().toString().trim();
                            gstPercentage= Double.parseDouble(gst);
                            Log.d(TAG, "changeBasePrice:GST-"+gstPercentage);
                            double gstCal=(gstPercentage/100.0f);
                            Log.d(TAG, "GST: "+gstCal);
                            double totalgstPrice=disprice*gstCal;
                            Log.d(TAG, "onItemSelected: TotaslPrice"+totalgstPrice);
                            Double AmountWtiGst=disprice+totalgstPrice;
                            Double amount =Math.round(AmountWtiGst*100.0)/100.0;
                            et_totalPrice.setText(String.valueOf(amount));

                            Log.d(TAG, "onItemSelected:gstPer" + gst);
                        }

                    }catch (Exception Ee)
                    {
                        Log.d(TAG, "onItemSelected:Exception"+e);
                    }
                }

//                try
//                {
//                    double unitPrice = Double.parseDouble(input_unitPrice.getText().toString());
//                    double discountPer = Double.parseDouble(input_discountper.getText().toString());
//                    double dis  = discountPer/100.0f;
//                    double totalAmount=unitPrice-(unitPrice*dis);
//
//                    Log.d(TAG, "afterTextChanged:Per"+dis);
//
//                    Log.i(TAG, "afterTextChanged:TotalPrice"+totalAmount);
//                    input_discountedPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));
//
//                    //  add value in discount in Rs
//                    double dis_tem=dis*unitPrice;
//                    //  double discountinRss=dis_tem/100.0f;
//                    Log.d(TAG, "afterTextChanged:DiscountinRs"+dis_tem);
//                    input_discountrs.removeTextChangedListener(discounrtedRs);
//                    input_discountrs.setText(String.valueOf(Math.round(dis_tem*100.0)/100.0));
//                    input_discountrs.addTextChangedListener(discounrtedRs);
//
//                }catch (NumberFormatException e)
//                {
//                    double unitPrice =  Double.parseDouble(input_unitPrice.getText().toString());
//                    double discount =0.0 ;
//                    double totalAmount=unitPrice-discount;
//                    input_discountedPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));
//                }

            }


        };

        et_discount.addTextChangedListener(discounrtedRs);
        et_discounted_ps.addTextChangedListener(discountedPer);


    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.edit_menuItem:
                getMenuItemData();
                break;
        }

    }

    private void getMenuItemData()
    {
        String gst = m_spinner.getSelectedItem().toString().trim();
        String totalPrice = et_totalPrice.getText().toString().trim();
        String discountedPrice = edit_discounted_price.getText().toString().trim();
         String  discount=et_discount.getText().toString().trim();
         String dicountps=et_discounted_ps.getText().toString().trim();
        String  unitPrice=et_input_unitPrice.getText().toString().trim();
        if(!totalPrice.isEmpty() && discountedPrice.length()>0)
        {
            _saveToDataBabe(gst, totalPrice, discountedPrice, discount, dicountps, unitPrice);
        }
        else
        {
            Toast.makeText(this, "Please Select Price", Toast.LENGTH_SHORT).show();
        }
    }

     private  void  _saveToDataBabe(final String gst, final String totalPrice, final String discountedPrice, final String discount, final String discountps, final String unitPrice)
     {

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
                         Toast.makeText(EditMenu_new.this, "Item updated successfully. ", Toast.LENGTH_SHORT).show();
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
             public void onErrorResponse(VolleyError error)
             {
                 if (error instanceof NoConnectionError)
                 {
                     Toast.makeText(EditMenu_new.this, "Please connect to the internet before continuing.", Toast.LENGTH_SHORT).show();
                 }

             }
         }) {
             @Override
             protected Map<String, String> getParams() throws AuthFailureError
             {
                 Map<String, String> params = new HashMap<>();
                 params.put("dbname", dbname);
                 params.put("item_id", item_id);
//                 params.put("item_name", input_productName.getText().toString());
//                 params.put("specification", input_specification.getText().toString());
//                 params.put("mrp", input_mrp.getText().toString());
                 params.put("product_id", product_id);
                 params.put("total_price", totalPrice);
                 params.put("discount", discount);
                 params.put("unit_price", unitPrice);
                 params.put("gst",gst);
                 params.put("discPrice",discountedPrice);
                 params.put("discPercent",discountps);


                 Log.d("Update Edit Menu", params.toString());
                 return params;
             }
         };
         RequestQueue requestQueue = Volley.newRequestQueue(this);
         requestQueue.add(stringRequest);
     }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}

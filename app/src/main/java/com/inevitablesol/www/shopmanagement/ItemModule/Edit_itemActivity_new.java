package com.inevitablesol.www.shopmanagement.ItemModule;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Edit_itemActivity_new extends AppCompatActivity  implements View.OnClickListener
{

    private static final String EDIT_STOCK = "http://35.161.99.113:9000/webapi/product/edit_stock";

    AppCompatButton btn_add_to_items;
    SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private static final String TAG = "Edit_itemActivity_new";


    private TextView txt_itemName,txt_productName,txt_company,txt_specification,txt_hsn,txt_productType;
    private  TextView txt_gst,txt_originalPrice,txt_mrp,txt_storageQty;
    private String stockQty;

    private String itemId;

    TextInputEditText et_addStock,et_discount,et_unitPrice,et_currentPrice;
    private TextInputEditText et_totalPrice;

    private static final String MySETTINGS = "MySetting";
    private SharedPreferences sharedpreferences2;


    private LinearLayout linearLayout_discount,linearLayout_discountPer;
    private Spinner et_gstPercentege;
    ArrayAdapter<CharSequence> adapter;
    TextInputEditText input_mrp;
    private TextInputEditText et_price;
    private  TextInputEditText et_disPer;

    private  TextWatcher tw_DiscountedPer;
    private  TextWatcher tw_discountedRs;
    private  TextView txt_discount;

    TextView  tx_p_storageQty,tx_p_originalPrice,tx_mrp,tx_gst;

        private SqlDataBase sqlDataBase;
    private double gstPercentage;


    private  Spinner sub_unit;
    ArrayAdapter<CharSequence> adapter2;


    private  TextView txt_itemBarcode;
    private  TextView txt_mUnit;
    private  TextView txt_unit;
    //private String shortCut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_new);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        txt_productName=(TextView)findViewById(R.id.add_new_productName);
        txt_productType=(TextView)findViewById(R.id.add_new_productType);
        txt_company=(TextView)findViewById(R.id.add_new_productCompany);
        txt_specification=(TextView)findViewById(R.id.add_new_productSpecification);
        txt_hsn=(TextView)findViewById(R.id.add_new_productHSN);
        txt_gst=(TextView)findViewById(R.id.add_new_gst);

        txt_mrp=(TextView)findViewById(R.id.add_newMrp);
        txt_storageQty=(TextView)findViewById(R.id.add_newStorageQty);
        txt_originalPrice=(TextView)findViewById(R.id.add_newOriginalPrice);
       // input_mrp = (TextInputEditText) findViewById(R.id.et_mrp);
        et_addStock=(TextInputEditText)findViewById(R.id.et_add_stock);
        et_discount=(TextInputEditText)findViewById(R.id.et_discount);
        et_unitPrice=(TextInputEditText)findViewById(R.id.et_s_unitprice);
        et_totalPrice=(TextInputEditText)findViewById(R.id.et_totalDiscountedPrice);
        et_currentPrice=(TextInputEditText)findViewById(R.id.et_currentPrice);
        linearLayout_discount=(LinearLayout)findViewById(R.id.linear_discount);
        linearLayout_discountPer=(LinearLayout)findViewById(R.id.linear_discountper);
          et_disPer=(TextInputEditText)findViewById(R.id.et_editItemDisPer);

        tx_p_originalPrice=(TextView)findViewById(R.id.p_originalprice);
        tx_p_storageQty=(TextView)findViewById(R.id.p_storageQty);
        tx_mrp=(TextView)findViewById(R.id.p_mrp);
        tx_gst=(TextView)findViewById(R.id.p_gst);


        txt_mUnit=(TextView)findViewById(R.id.txt_munit);
        txt_unit=(TextView)findViewById(R.id.txt_unit);
        txt_itemBarcode=(TextView)findViewById(R.id.txt_itembarcode);
        sub_unit=(Spinner)findViewById(R.id.sub_measurementUnit);



         //txt_discount=(TextView)findViewById(R.id//.add_dis);
        et_gstPercentege=(Spinner)findViewById(R.id.et_gstpercentage);
        et_price=(TextInputEditText)findViewById(R.id.et_Price);

        sqlDataBase=new SqlDataBase(this);


        et_gstPercentege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Toast.makeText(SelectedItemFromStock.this, " WOW ", Toast.LENGTH_SHORT).show();
                try
                {
                    Double  disprice= Double.valueOf(et_totalPrice.getText().toString().trim());
                    if(disprice!=null)
                    {
                        Log.d(TAG, "onItemSelected: Price" + disprice);
                        Double  gst = Double.valueOf(et_gstPercentege.getSelectedItem().toString().trim());

                        Log.d(TAG, "changeBasePrice:GST-"+gst);
                        double gstCal=(gst/100.0f);

                        Log.d(TAG, "GST: "+gstCal);
                        double totalgstPrice=disprice*gstCal;
                        Log.d(TAG, "onItemSelected:TotaslPrice"+totalgstPrice);
                        Double AmountWtiGst=disprice+totalgstPrice;
                        Double amount =Math.round(AmountWtiGst*100.0)/100.0;
                        et_price.setText(String.valueOf(amount));

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

        tw_DiscountedPer=new TextWatcher()
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
                    double unitPrice = Double.parseDouble(et_unitPrice.getText().toString());
                    double discountPer = Double.parseDouble(et_disPer.getText().toString());
                    double dis  = discountPer/100.0f;
                    double totalAmount=unitPrice-(unitPrice*dis);

                    Log.d(TAG, "afterTextChanged:Per"+dis);

                    Log.i(TAG, "afterTextChanged:TotalPrice"+totalAmount);
                    et_totalPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));

                    //  add value in discount in Rs
                    double dis_tem=dis*unitPrice;
                    //  double discountinRss=dis_tem/100.0f;
                    Log.d(TAG, "afterTextChanged:DiscountinRs"+dis_tem);
                    et_discount.removeTextChangedListener(tw_discountedRs);
                    et_discount.setText(String.valueOf(Math.round(dis_tem*100.0)/100.0));
                    et_discount.addTextChangedListener(tw_discountedRs);

                    try
                    {
                        Double  disprice= Double.valueOf(et_totalPrice.getText().toString().trim());
                        if(disprice!=null)
                        {
                            Log.d(TAG, "onItemSelected: Price" + disprice);
                            String gst = et_gstPercentege.getSelectedItem().toString().trim();
                            gstPercentage= Double.parseDouble(gst);
                            Log.d(TAG, "changeBasePrice:GST-"+gstPercentage);
                            double gstCal=(gstPercentage/100.0f);
                            Log.d(TAG, "GST: "+gstCal);
                            double totalgstPrice=disprice*gstCal;
                            Log.d(TAG, "onItemSelected: TotaslPrice"+totalgstPrice);
                            Double AmountWtiGst=disprice+totalgstPrice;
                            Double amount =Math.round(AmountWtiGst*100.0)/100.0;
                            et_price.setText(String.valueOf(amount));

                            Log.d(TAG, "onItemSelected:gstPer" + gst);
                        }

                    }catch (Exception e)
                    {
                        Log.d(TAG, "onItemSelected:Exception"+e);
                    }

                }catch (NumberFormatException e)
                {
                    double unitPrice =  Double.parseDouble(et_unitPrice.getText().toString());
                    double discount =0.0 ;
                    double totalAmount=unitPrice-discount;
                    et_totalPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));

                    try
                    {
                        Double  disprice= Double.valueOf(et_discount.getText().toString().trim());
                        if(disprice!=null)
                        {
                            Log.d(TAG, "onItemSelected: Price" + disprice);
                            String gst = et_gstPercentege.getSelectedItem().toString().trim();
                            gstPercentage= Double.parseDouble(gst);
                            Log.d(TAG, "changeBasePrice:GST-"+gstPercentage);
                            double gstCal=(gstPercentage/100.0f);
                            Log.d(TAG, "GST: "+gstCal);
                            double totalgstPrice=disprice*gstCal;
                            Log.d(TAG, "onItemSelected: TotaslPrice"+totalgstPrice);
                            Double AmountWtiGst=disprice+totalgstPrice;
                            Double amount =Math.round(AmountWtiGst*100.0)/100.0;
                            et_price.setText(String.valueOf(amount));

                            Log.d(TAG, "onItemSelected:gstPer" + gst);
                        }

                    }catch (Exception eeee)
                    {
                        Log.d(TAG, "onItemSelected:Exception"+e);
                    }





                }



            }
        };

        tw_discountedRs=new TextWatcher()
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

                    double unitPrice = Double.parseDouble(et_unitPrice.getText().toString());
                    double discount = Double.parseDouble(et_discount.getText().toString());
                    double totalAmount=unitPrice-discount;
                    Log.i(TAG, "afterTextChanged:TotalPrice"+totalAmount);
                    et_totalPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));

                    // formula   discount in percentage
                    double dis_temp=  discount/unitPrice *100;

                    Log.d(TAG, "afterTextChanged:Discount in Percentage "+dis_temp);

                    //et_discountedPer.removeTextChangedListener((TextWatcher) et_discountedPer);
                    et_disPer.removeTextChangedListener(tw_DiscountedPer);
                    et_disPer.setText(String.valueOf(Math.round(dis_temp*100.0)/100.0));
                    et_disPer.addTextChangedListener(tw_DiscountedPer);



                }catch (NumberFormatException e)
                {
                    double unitPrice =  Double.parseDouble(et_unitPrice.getText().toString());
                    double discount =0.0 ;
                    double totalAmount=unitPrice-discount;
                    et_disPer.setText("0.0");
                    et_totalPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));

                }


            }
        };

//
//

        adapter = ArrayAdapter.createFromResource(this, R.array.gst_tax_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_gstPercentege.setAdapter(adapter);


        et_unitPrice.addTextChangedListener(new TextWatcher() {
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
                       if(s.toString().length()>0)
                    {
                        if (linearLayout_discount.getVisibility() == View.VISIBLE)
                        {
                            double unitPrice = Double.parseDouble(et_unitPrice.getText().toString());
                            double discount = Double.parseDouble(et_discount.getText().toString());
                            double totalAmount = unitPrice - discount;
                            et_totalPrice.setText(String.valueOf(Math.round(totalAmount * 100.0) / 100.0));
                        } else
                        {

                            double unitPrice = Double.parseDouble(et_unitPrice.getText().toString());
                            double discount = 0.0;
                            double totalAmount = unitPrice - discount;
                            et_totalPrice.setText(String.valueOf(Math.round(totalAmount * 100.0) / 100.0));

                        }
                    }

                }catch (NumberFormatException e)
                {
                    double unitPrice = Double.parseDouble(et_unitPrice.getText().toString());
                    double discount = 0.0;
                    double totalAmount=unitPrice-discount;
                    et_totalPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));

                }





            }
        });
        et_discount.addTextChangedListener(tw_discountedRs);
        et_disPer.addTextChangedListener(tw_DiscountedPer);

//

        btn_add_to_items=(AppCompatButton)findViewById(R.id.btn_add_to_items);
        btn_add_to_items.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");

        sharedpreferences2 = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
        try
        {

            String discount =(sharedpreferences2.getString("discount", null));
            Log.d(TAG, "onCreate:Discount Status"+discount);
            if(discount !=null)
            {
                if(discount.equalsIgnoreCase("Yes"))
                {
                    linearLayout_discount.setVisibility(View.VISIBLE);
                    linearLayout_discountPer.setVisibility(View.VISIBLE);

                }else
                {
                    linearLayout_discount.setVisibility(View.GONE);
                    linearLayout_discountPer.setVisibility(View.GONE);

                }

            }

        }catch (NullPointerException e)
        {

        }
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            //android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(sub_unit);
            // Set popupWindow height to 500px
            //popupWindow.setHeight((int)height/2);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        if (intent.hasExtra("itemId"))
        {
            StockInfo stockInfo= (StockInfo) getIntent().getSerializableExtra("stockinfo");
            Log.d(TAG, "onCreate:Intent  "+stockInfo.toString());
            itemId = intent.getStringExtra("itemId");
            String name = intent.getStringExtra("item_name");
            String product_type = intent.getStringExtra("product_type");
            String company = intent.getStringExtra("company");
            String original_price = intent.getStringExtra("original_price");
            String specification = intent.getStringExtra("specification");
            String mrp = intent.getStringExtra("mrp");
            String hsn_san_code = intent.getStringExtra("hsn_ssc_code");
            String gst = intent.getStringExtra("gst");
            String storageQty=intent.getStringExtra("storageqty");
            String unitPrice_old=intent.getStringExtra("unitPrice");
            stockQty=intent.getStringExtra("stockQty");
             tx_p_originalPrice.setText(intent.getStringExtra("p_price"));
             tx_p_storageQty.setText(storageQty);
             tx_gst.setText(intent.getStringExtra("p_gst"));
             tx_mrp.setText(intent.getStringExtra("p_mrp"));
             txt_mrp.setText(intent.getStringExtra("discount"));

            txt_productType.setText(product_type);
            txt_company.setText(company);
            txt_productName.setText(name);
            txt_specification.setText(specification);
          //  txt_mrp.setText(mrp);

            txt_hsn.setText(hsn_san_code);
            txt_gst.setText(gst);
            txt_storageQty.setText(stockQty);
//            txt_productType.setText(product_type);
//            txt_company.setText(company);
//            txt_productName.setText(name);
//            txt_specification.setText(specification);
//            txt_hsn.setText(hsn_san_code);
            txt_originalPrice.setText(intent.getStringExtra("totalPrice"));
            txt_gst.setText(gst);
            et_currentPrice.setText(unitPrice_old);

            txt_itemBarcode.setText(stockInfo.getItembarcode());
            txt_mUnit.setText(stockInfo.getMunit());
            txt_unit.setText(stockInfo.getUnit());

//            try {
//                String unit=stockInfo.getMunit();
//
//                if(unit.equalsIgnoreCase("Unit"))
//                {
//                    adapter2  = ArrayAdapter.createFromResource(getApplicationContext(),
//                            R.array.unit, android.R.layout.simple_spinner_item);
//
//                }else if(unit.equalsIgnoreCase("Gram"))
//                {
//                    adapter2  = ArrayAdapter.createFromResource(getApplicationContext(),
//                            R.array.gram, android.R.layout.simple_spinner_item);
//
//                }else if(unit.equalsIgnoreCase("liter"))
//                {
//                    adapter2  = ArrayAdapter.createFromResource(getApplicationContext(),
//                            R.array.liter, android.R.layout.simple_spinner_item);
//
//                }
//                else if(unit.equalsIgnoreCase("meter"))
//                {
//                    adapter2  = ArrayAdapter.createFromResource(getApplicationContext(),
//                            R.array.meter, android.R.layout.simple_spinner_item);
//
//                }
//                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                sub_unit.setAdapter(adapter2);
//
//                sub_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        //sub_unit.getSelectedItem().toString();
//                        Object item = parent.getItemAtPosition(position);
//                        if (item != null) {
//
//                            Toast.makeText(Edit_itemActivity_new.this, item.toString(),
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        Toast.makeText(Edit_itemActivity_new.this, "Selected",
//                                Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
//
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
//
        } else
        {
            Toast.makeText(this, "Data Not Available", Toast.LENGTH_SHORT).show();

        }


    }

    public void addToStock(final String unitprice, final String discount, final String stock_qty, final String totalAmount, final String price)
    {

        //String  unit= sub_unit.getSelectedItem().toString().trim();
//        try {
//            shortCut=unit.substring(unit.indexOf("(")+1,unit.indexOf(")"));
//            Log.d(TAG, "AddToCart: Short cut"+shortCut);
//        } catch (Exception e)
//        {
//            shortCut=unit;
//            e.printStackTrace();
//        }
        final String gstpercentage = et_gstPercentege.getSelectedItem().toString().trim();
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EDIT_STOCK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                try
                {
                    Log.d("response",response);
                    JSONObject jsonObject = new JSONObject(response);
                    String message = null;
                    loading.dismiss();
                    message = jsonObject.getString("message");
                    Log.d(TAG, "onResponse:"+message);
                    if (message.equalsIgnoreCase("Stock Added"))
                    {
                          sqlDataBase.deleteDataBase_ItemTable();
                        Toast.makeText(Edit_itemActivity_new.this, "succesfully updated. ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Edit_itemActivity_new.this, Edit_Items.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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
                loading.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Intent intent = getIntent();

                // String stock_qty = input_addtostock.getText().toString();
                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                params.put("item_id", itemId);
                params.put("stock_qty", stock_qty);
                params.put("unit_price",unitprice);
                params.put("discount",discount);
                params.put("total_price",price);
              //  params.put("GstPrice",price);
               // params.put("mrp", input_mrp.getText().toString());
                params.put("gst",gstpercentage);
                params.put("discPrice",totalAmount);
                //params.put("shortCut",shortCut);
                params.put("discPercent",et_disPer.getText().toString());
                //params.put("unit",sub_unit.getSelectedItem().toString());
                Log.d("EDITSTOCK", params.toString());
                return params;


            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();
        switch (viewId)
        {
            case R.id.btn_add_to_items:
                //  String stock_qty = txt_.getText().toString();
                int storageQty=Integer.parseInt(txt_storageQty.getText().toString().trim());
                if (!(stockQty.equals("") || stockQty.equals(null)) && storageQty >0)
                {
                    getDeatils();

                } else if(storageQty<=0)
                {
                    Toast.makeText(this, "Can not  Add ", Toast.LENGTH_SHORT).show();

                }else
                {
                    Toast.makeText(this, "Please enter a quantity to continue.", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void getDeatils()
    {
        try
        {
            if(linearLayout_discount.getVisibility()== View.VISIBLE)
            {
                Log.d(TAG, "getDeatils:Visible");
                double unitPrice = Double.parseDouble(et_unitPrice.getText().toString());
                double discount = Double.parseDouble(et_discount.getText().toString());
                String  stock_qty= stockQty;//et_addStock.getText().toString().trim();
                String totalAmount=et_totalPrice.getText().toString().trim();
                String price=et_price.getText().toString().trim();
                addToStock(String.valueOf(unitPrice),String.valueOf(discount),stock_qty,totalAmount,price);
            }else
            {
                Log.d(TAG, "getDeatils:INVisible");
                double unitPrice = Double.parseDouble(et_unitPrice.getText().toString());
                double discount = 0.0;
                String  stock_qty= stockQty;// //et_addStock.getText().toString().trim();
                String totalAmount=et_totalPrice.getText().toString().trim();
                String price=et_price.getText().toString().trim();
                addToStock(String.valueOf(unitPrice),String.valueOf(discount),stock_qty,totalAmount,price);

            }

        }catch (Exception e)
        {
            Log.d(TAG, "getDeatils:EXCEPTION"+e);
        }


    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_SHORT).show();
        finish(); // call this to finish the

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

}

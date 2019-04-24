package com.inevitablesol.www.shopmanagement.ItemModule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SelectedItemFromStock extends AppCompatActivity implements View.OnClickListener
{
    private static final String ADD_TO_STOCK = "http://35.161.99.113:9000/webapi/product/add_stock";

       AppCompatButton  btn_add_to_items;
    SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private static final String TAG = "SelectedItemFromStock";



    private  TextView txt_itemName,txt_productName,txt_company,txt_specification,txt_hsn,txt_productType;
    private  TextView txt_gst,txt_originalPrice,txt_mrp,txt_storageQty;
    private String stockQty;

    private String itemId;

    TextInputEditText et_addStock,et_discount,et_unitPrice;
    Spinner et_gstpercentage;
    private TextInputEditText et_totalDiscountPrice;

    private  TextInputEditText et_TotalPrice;


    private LinearLayout linearLayout_discount,linearLayout_discountPer;

    private static final String MySETTINGS = "MySetting";
    private SharedPreferences sharedpreferences2;
    private SqlDataBase sqlDataBase;
    private double gstPercentage;

    private  TextInputEditText et_price;
    private TextInputEditText et_discountedPer;


    private  TextWatcher    discountedPer;
    private  TextWatcher    discounrtedRs;
    private String storageQty;
    private GlobalPool globalPool;
    private Context context;
    private String productName;

    private  Spinner sub_unit;
    ArrayAdapter<CharSequence> adapter2;


     private  TextView txt_itemBarcode;
     private  TextView txt_mUnit;
     private  TextView txt_unit;
    private String shortCut;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_new);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       txt_productName=(TextView)findViewById(R.id.add_new_productName);
        txt_productType=(TextView)findViewById(R.id.add_new_productType);
        txt_company=(TextView)findViewById(R.id.add_new_productCompany);
        txt_specification=(TextView)findViewById(R.id.add_new_productSpecification);
        txt_hsn=(TextView)findViewById(R.id.add_new_productHSN);
        txt_gst=(TextView)findViewById(R.id.add_new_gst);
        txt_mrp=(TextView)findViewById(R.id.add_newMrp);

        txt_mUnit=(TextView)findViewById(R.id.txt_munit);
        txt_unit=(TextView)findViewById(R.id.txt_unit);
        txt_itemBarcode=(TextView)findViewById(R.id.txt_itembarcode);
        context=this;
        sub_unit=(Spinner)findViewById(R.id.sub_measurementUnit);
        et_gstpercentage=(Spinner)findViewById(R.id.et_gstpercentage);
        et_discountedPer=(TextInputEditText)findViewById(R.id.et_discountPer);
        linearLayout_discount=(LinearLayout)findViewById(R.id.linear_discount);
        linearLayout_discountPer=(LinearLayout)findViewById(R.id.linear_discountper);

        globalPool=(GlobalPool)this.getApplicationContext();

        txt_storageQty=(TextView)findViewById(R.id.add_newStorageQty);
        txt_originalPrice=(TextView)findViewById(R.id.add_newOriginalPrice);

        et_addStock=(TextInputEditText)findViewById(R.id.et_add_stock);
        et_discount=(TextInputEditText)findViewById(R.id.et_discount);
        et_unitPrice=(TextInputEditText)findViewById(R.id.et_s_unitprice);
        et_totalDiscountPrice=(TextInputEditText)findViewById(R.id.et_totalDiscountedPrice);
        sqlDataBase=new SqlDataBase(this);

        et_price=(TextInputEditText)findViewById(R.id.et_totalPrice);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gst_tax_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_gstpercentage.setAdapter(adapter);




          et_gstpercentage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
              {
                  //Toast.makeText(SelectedItemFromStock.this, " WOW ", Toast.LENGTH_SHORT).show();
                  try
                  {
                      Double  disprice= Double.valueOf(et_totalDiscountPrice.getText().toString().trim());
                       if(disprice!=null)
                       {
                           Log.d(TAG, "onItemSelected: Price" + disprice);
                           String gst = et_gstpercentage.getSelectedItem().toString().trim();
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




              }

              @Override
              public void onNothingSelected(AdapterView<?> parent)
              {

              }
          });

        et_unitPrice.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
               try
               {
                   if(linearLayout_discount.getVisibility() == View.VISIBLE)
                   {
                       double unitPrice = Double.parseDouble(et_unitPrice.getText().toString());
                       double discount = Double.parseDouble(et_discount.getText().toString());
                       double totalAmount=unitPrice-discount;
                       Log.i(TAG, "afterTextChanged:TotalPrice"+totalAmount);
                       et_totalDiscountPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));

                   }else
                   {
                       double unitPrice = Double.parseDouble(et_unitPrice.getText().toString());
                       double discount = 0.0;
                       double totalAmount=unitPrice-discount;
                       Log.i(TAG, "afterTextChanged:TotalPrice"+totalAmount);
                       et_totalDiscountPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));

                   }


               }catch (NumberFormatException e)
               {
                   double unitPrice = 0.0;
                   String discounted=et_discount.getText().toString();
                   if(discounted.length()>0)
                   {
                       double discount = Double.parseDouble(discounted);
                       double totalAmount = unitPrice - discount;
                       et_totalDiscountPrice.setText(String.valueOf(Math.round(totalAmount * 100.0) / 100.0));
                   }

               }





            }
        });

        // discount
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

                     double unitPrice = Double.parseDouble(et_unitPrice.getText().toString());
                     double discount = Double.parseDouble(et_discount.getText().toString());
                     double totalAmount=unitPrice-discount;
                     Log.i(TAG, "afterTextChanged:TotalPrice"+totalAmount);
                     et_totalDiscountPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));

                     // formula   discount in percentage
                     double dis_temp=  discount/unitPrice *100;

                     Log.d(TAG, "afterTextChanged:Discount in Percentage "+dis_temp);

                     //et_discountedPer.removeTextChangedListener((TextWatcher) et_discountedPer);
                      et_discountedPer.removeTextChangedListener(discountedPer);
                     et_discountedPer.setText(String.valueOf(Math.round(dis_temp*100.0)/100.0));
                     et_discountedPer.addTextChangedListener(discountedPer);
                     try
                     {
                         Double  disprice= Double.valueOf(et_totalDiscountPrice.getText().toString().trim());
                         if(disprice!=null)
                         {
                             Log.d(TAG, "onItemSelected: Price" + disprice);
                             String gst = et_gstpercentage.getSelectedItem().toString().trim();
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
                     et_discountedPer.setText("0.0");
                     et_totalDiscountPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));
                     try
                     {
                         Double  disprice= Double.valueOf(et_totalDiscountPrice.getText().toString().trim());
                         if(disprice!=null)
                         {
                             Log.d(TAG, "onItemSelected: Price" + disprice);
                             String gst = et_gstpercentage.getSelectedItem().toString().trim();
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

                     }catch (Exception ee)
                     {
                         Log.d(TAG, "onItemSelected:Exception"+e);
                     }


                 }





             }
         };

        discountedPer=new TextWatcher() {
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
                    double discountPer = Double.parseDouble(et_discountedPer.getText().toString());
                    double dis  = discountPer/100.0f;
                    double totalAmount=unitPrice-(unitPrice*dis);

                    Log.d(TAG, "afterTextChanged:Per"+dis);

                    Log.i(TAG, "afterTextChanged:TotalPrice"+totalAmount);
                    et_totalDiscountPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));

                    //  add value in discount in Rs
                    double dis_tem=dis*unitPrice;
                  //  double discountinRss=dis_tem/100.0f;
                    Log.d(TAG, "afterTextChanged:DiscountinRs"+dis_tem);
                    et_discount.removeTextChangedListener(discounrtedRs);
                    et_discount.setText(String.valueOf(Math.round(dis_tem*100.0)/100.0));
                    et_discount.addTextChangedListener(discounrtedRs);
                    try
                    {
                        Double  disprice= Double.valueOf(et_totalDiscountPrice.getText().toString().trim());
                        if(disprice!=null)
                        {
                            Log.d(TAG, "onItemSelected: Price" + disprice);
                            String gst = et_gstpercentage.getSelectedItem().toString().trim();
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
                    et_totalDiscountPrice.setText(String.valueOf(Math.round(totalAmount*100.0)/100.0));
                    try
                    {
                        Double  disprice= Double.valueOf(et_totalDiscountPrice.getText().toString().trim());
                        if(disprice!=null)
                        {
                            Log.d(TAG, "onItemSelected: Price" + disprice);
                            String gst = et_gstpercentage.getSelectedItem().toString().trim();
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

                    }catch (Exception ee)
                    {
                        Log.d(TAG, "onItemSelected:Exception"+e);
                    }

                }

            }


        };
        et_discount.addTextChangedListener(discounrtedRs);
        et_discountedPer.addTextChangedListener(discountedPer);
//        et_discount.addTextChangedListener(new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s)
//            {
//
//
//        });




//        et_discountedPer.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after)
//            {
//
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


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


        btn_add_to_items=(AppCompatButton)findViewById(R.id.btn_add_to_items);
        btn_add_to_items.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");



        Intent intent = getIntent();
        if (intent.hasExtra("itemId"))
        {

            StockInfo stockInfo= (StockInfo) getIntent().getSerializableExtra("stockinfo");
            Log.d(TAG, "onCreate:Intent  "+stockInfo.toString());

             itemId = String.valueOf(intent.getStringExtra("itemId"));
             productName = intent.getStringExtra("item_name");
            String product_type = intent.getStringExtra("product_type");
            String company = intent.getStringExtra("company");
            String original_price = intent.getStringExtra("p_price");
            String specification = intent.getStringExtra("specification");
            String mrp = intent.getStringExtra("p_mrp");
            String hsn_san_code = intent.getStringExtra("hsn_ssc_code");
            String gst = intent.getStringExtra("gst");
             storageQty=intent.getStringExtra("storageqty");
               stockQty=intent.getStringExtra("stockQty");
               txt_itemBarcode.setText(stockInfo.getItembarcode());
               txt_mUnit.setText(stockInfo.getMunit());
               txt_unit.setText(stockInfo.getUnit());

              txt_productType.setText(product_type);
              txt_company.setText(company);
            txt_productName.setText(productName);
            txt_specification.setText(specification);
            txt_mrp.setText(mrp);
            txt_hsn.setText(hsn_san_code);
            txt_originalPrice.setText(original_price);
            txt_gst.setText(intent.getStringExtra("p_gst"));
            txt_storageQty.setText(storageQty);
            Log.d(TAG, "onResponse: Stock qty"+stockQty +"Storage Qty"+storageQty);

            try
            {
                String unit=stockInfo.getMunit();
                Log.d(TAG, "onCreate: uni"+unit);

                if(unit.equalsIgnoreCase("Unit"))
                {
                    adapter2  = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.unit, android.R.layout.simple_spinner_item);

                }else if(unit.equalsIgnoreCase("Gram"))
                {
                    adapter2  = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.gram, android.R.layout.simple_spinner_item);

                }else if(unit.equalsIgnoreCase("liter"))
                {
                    adapter2  = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.liter, android.R.layout.simple_spinner_item);

                }else if(unit.equalsIgnoreCase("meter"))
                {
                    adapter2  = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.meter, android.R.layout.simple_spinner_item);
                }

                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sub_unit.setAdapter(adapter2);
            } catch (Exception e)
            {
                e.printStackTrace();
            }





        } else
        {

        }
        Log.d(TAG, "onCreate: Gloobla Test"+globalPool.isTest());


    }

    public void addToStock(final String unitprice, final String discount, final String stock_qty, final String totalAmount, final String price, final String discount_percentage)
    {
        String  unit= sub_unit.getSelectedItem().toString().trim();
        try {
            shortCut=unit.substring(unit.indexOf("(")+1,unit.indexOf(")"));
            Log.d(TAG, "AddToCart: Short cut"+shortCut);
        } catch (Exception e)
        {
            shortCut=unit;

            e.printStackTrace();
        }
        final String gstpercentage = et_gstpercentage.getSelectedItem().toString().trim();
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_TO_STOCK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.d("response",response);
                    JSONObject jsonObject = new JSONObject(response);
                    String message = null;
                    loading.dismiss();
                    message = jsonObject.getString("message");
                    Log.d(TAG, "onResponse:"+message);
                    if (message.equalsIgnoreCase("Stock Added"))
                    {
                                    if(globalPool.isPruchasesms())
                                    {
                                        if (Double.parseDouble(storageQty) - Double.parseDouble(stock_qty) <= 10)
                                            sendReminderMessage();
                                    }

                        sqlDataBase.deleteItemTable();
                        Toast.makeText(SelectedItemFromStock.this, "Add successfully. ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SelectedItemFromStock.this, Add_Item.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

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
                params.put("total_price",price);
                params.put("discount",discount);
                params.put("discPrice",totalAmount);
                params.put("gst",gstpercentage);
                params.put("shortCut",shortCut);
                params.put("unit",sub_unit.getSelectedItem().toString());
                params.put("discPercent",discount_percentage);
                Log.d("ADDSTOCK", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendReminderMessage()
    {
        Log.d(TAG, "sendReminderMessage: Test ");

        String message = " Dear User , \n This is just Friendly Reminder \n  for low Storage Qty  \n " +
                " of Product  "+ productName+" \n " +
                "Thanks You \n M-HOURZ";

        String mobile_no =globalPool.getUser_noti_mobile();//tv_custNumber.getText().toString().trim();

        String uri = Uri.parse("\n" +
                "http://bhashsms.com/api/sendmsg.php?")
                .buildUpon()
                .appendQueryParameter("user", "TEAM_MHOURZ")
                .appendQueryParameter("pass", "MECHATRON")
                .appendQueryParameter("text", message)
                .appendQueryParameter("sender", "MHOURZ")
                .appendQueryParameter("phone", mobile_no)
                .appendQueryParameter("priority", "ndnd")
                .appendQueryParameter("stype", "normal")
                .build().toString();

        Log.d(TAG, "TestMEssage: Uri"+uri);
        final Context context = getApplicationContext();
        StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null)
                            {
                                Log.d(TAG, "onResponse: Sms new "+response);

                                if (response.contains("S."))
                                {
                                    Toast.makeText(getApplication(), "message sent successfully", Toast.LENGTH_LONG).show();
                                } else
                                {
                                    Toast.makeText(getApplication(), "Message couldn't reach you, try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();
        switch (viewId)
        {
            case R.id.btn_add_to_items:

                int storageQty=Integer.parseInt(txt_storageQty.getText().toString().trim());
                if (!(stockQty.equals("") || stockQty.equals(null)) && storageQty >0)
                {
                    getDeatils();

                } else if(storageQty<=0)
                {
                    Toast.makeText(getApplicationContext(), "Can not  Add ", Toast.LENGTH_SHORT).show();

                }else
                {
                    Toast.makeText(getApplicationContext(), "Please enter a quantity to continue.", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void getDeatils()
    {
        try
        {
            double unitPrice=0.0;
            double discount=0.0;
            double discount_percentage=0.0;
                if(linearLayout_discount.getVisibility()== View.VISIBLE)
                {
                    Log.d(TAG, "getDeatils: Visible");
                    try
                    {

                         unitPrice = Double.parseDouble(et_unitPrice.getText().toString());
                         discount = Double.parseDouble(et_discount.getText().toString());

                    }catch (NumberFormatException e)
                    {
                        String d_p=et_discountedPer.getText().toString().trim();
                        if(d_p.isEmpty())
                        {
                            discount_percentage=0.0;
                        }else
                        {
                            discount=0.0;

                        }
                    }


                    String  stock_qty=    et_addStock.getText().toString().trim();
                     String totalAmount=  et_totalDiscountPrice.getText().toString().trim();
                    String price=     et_price.getText().toString().trim();
                    discount_percentage= Double.parseDouble(et_discountedPer.getText().toString().trim());

                    addToStock(String.valueOf(unitPrice),String.valueOf(discount),stock_qty,totalAmount,price, String.valueOf(discount_percentage));

                }else
                {
                    Log.d(TAG, "getDeatils:INVisible");
                     unitPrice = Double.parseDouble(et_unitPrice.getText().toString());
                     discount = 0.0;
                    String  stock_qty= et_addStock.getText().toString().trim();
                    String totalAmount=et_totalDiscountPrice.getText().toString().trim();
                    String price=et_price.getText().toString().trim();
                     discount_percentage= Double.parseDouble(et_discountedPer.getText().toString().trim());
                    addToStock(String.valueOf(unitPrice),String.valueOf(discount),stock_qty,totalAmount,price, String.valueOf(discount_percentage));

                }

        }catch (Exception e)
        {
            Log.d(TAG, "getDeatils: EXCEPTION"+e);
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

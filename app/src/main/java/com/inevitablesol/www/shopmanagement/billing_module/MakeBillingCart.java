package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.expenses.ExpList;
import com.inevitablesol.www.shopmanagement.expenses.SelectedExpensiveDetails;
import com.inevitablesol.www.shopmanagement.expenses.View_expense;
import com.inevitablesol.www.shopmanagement.sql_lite.ItemDetalisClass;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.inevitablesol.www.shopmanagement.purchase_module.PurchaseBillingDetails.MyPREFERENCES;

public class MakeBillingCart extends AppCompatActivity implements View.OnClickListener ,DisplayQunatity {


    private static final int TYPE_CAROUSEL = 10;
    private Spinner   mproductType;


    private static final String GET_PRODUCT = " http://35.161.99.113:9000/webapi/product/list";
    private String mProductName;
    private RecyclerView recyclerView;

    AppCompatButton viewCart;
    TextView txt_custName;
     EditText  searchProduct;
    TextView total_bilingCart;
    private  String custName,custMobile,custemail,custaddOne;

    SqlDataBase sqlDataBase;
    private String custId;
    private String h_status;
    private String dbname;
    private static final String TAG = "MakeBillingCart";
    private SharedPreferences sharedpreferences;
    private String gstin;
    private String supplier;
    private TextView  txt_totalAmount;

    private ItemListForBilling billingStockAdater;
    private  ArrayList<ItemDetalisClass> itemDetalisClasses;
    private SwitchCompat switchCompat;
    private double totalTaxableValue=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_billing_cart);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mproductType=(Spinner)findViewById(R.id.spnn_product_bill);
        recyclerView = (RecyclerView)findViewById(R.id.bill_recyclerView);
         txt_custName=(TextView)findViewById(R.id.txt_billcustName);
         viewCart=(AppCompatButton)findViewById(R.id.bill_viewcart);
         searchProduct=(EditText)findViewById(R.id.bill_productname);
          searchProduct.setOnClickListener(this);
          switchCompat=(SwitchCompat)findViewById(R.id.barcodeswitch);
            txt_totalAmount=(TextView)findViewById(R.id.total_amount);
            total_bilingCart=(TextView)findViewById(R.id.total_billin_qty);
           viewCart.setOnClickListener(this);
        Intent intent=getIntent();

                  if(intent.hasExtra("name"))
                  {
                         custName=intent.getStringExtra("name");
                         custMobile=intent.getStringExtra("mobile");
                         custemail=intent.getStringExtra("email");
                         custaddOne=intent.getStringExtra("addOne");
                         custId=intent.getStringExtra("custid");
                         h_status=intent.getStringExtra("h_status");
                         gstin=intent.getStringExtra("gst");
                         supplier=intent.getStringExtra("supplier");
                         txt_custName.setText(custName);
                  }
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));



        getAppProductDetails();

        sqlDataBase=new SqlDataBase(this);

        searchProduct.addTextChangedListener(new TextWatcher()
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

                if(switchCompat.isChecked())
                {
                      if(!s.toString().isEmpty())
                    getDetailsForBarcode(s.toString());
                  //  Toast.makeText(MakeBillingCart.this, "under working", Toast.LENGTH_SHORT).show();

                }else

                    {

//                    String name=s.toString().trim();
//                    if(!name.isEmpty() && name.length()>0)
//                    {
                        getDetails(s.toString());
//                    }else
//                    {
//                        Toast.makeText(MakeBillingCart.this, "Plese enter item name", Toast.LENGTH_SHORT).show();
//                        //getDetails(name);
//                    }

                }


            }
        });
            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(isChecked)
                    {
                        searchProduct.setHint(R.string.itembarcode);
                    }else
                        {
                            searchProduct.setHint("Item Name");

                        Log.d(TAG, "onCheckedChanged: ");
                    }

                }
            });
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(mproductType);

            // Set popupWindow height to 500px
            popupWindow.setHeight((int)height/2);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {

            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent e)
                {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
            {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e))
                {
                    int position = rv.getChildAdapterPosition(child);
                    final ItemDetalisClass   billData= itemDetalisClasses.get(position);


                }
                return  false;

            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e)
            {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });




    }



    @Override
    public void onClick(View v)
    {
        int id=v.getId();
        switch (id)
        {
            case R.id.bill_viewcart:
                Intent intent=new Intent(MakeBillingCart.this,BillingCartItems.class);
                    intent.putExtra("name",custName);
                    intent.putExtra("mobile",custMobile);
                    intent.putExtra("email",custemail);
                    intent.putExtra("custid",custId);
                    intent.putExtra("h_status",h_status);
                    intent.putExtra("address",custaddOne);
                    intent.putExtra("gst",gstin);
                    intent.putExtra("supplier",supplier);
                    intent.putExtra("dbname",dbname);
                startActivity(intent);
                break;
        }

    }


    private void getAppProductDetails()
    {
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, GET_PRODUCT, new Response.Listener<String>()
       {
           @Override
           public void onResponse(String response) {
               try {
                   Log.d(TAG, "onResponse:ProductType" + response);
                   String resp = response.toString().trim();
                   BillingProductParser productParser = new BillingProductParser(resp);
                   productParser.billingproductParser();
                   BillingProductAdapter productAdapter = new BillingProductAdapter(MakeBillingCart.this, R.layout.product_list, BillingProductParser.productName, BillingProductParser.productId);

                   mproductType.setAdapter(productAdapter);
                   mproductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                       @Override
                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                       {
                           mProductName = BillingProductParser.productId[position];
                           Log.d("name", mProductName);
                           get_allstock(mProductName);
                       }

                       @Override
                       public void onNothingSelected(AdapterView<?> parent)
                       {

                       }
                   });


               } catch (Exception e) {
                   Log.d("Exception", "" + e);
               }


           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               if (error instanceof NoConnectionError) {
                   Toast.makeText(MakeBillingCart.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
               }
           }
       }) {

           @Override
           protected Map<String, String> getParams() throws AuthFailureError {

               Map<String, String> params = new HashMap<>();
               params.put("dbname", dbname);
               Log.d(TAG, "getParams() called"+params.toString());
               return params;

           }
       });

    }



    public void get_allstock(final String mProductID)
    {



        Log.d("id", mProductID);
       itemDetalisClasses = sqlDataBase.getItemDetails(Integer.parseInt(mProductID));
        for (ItemDetalisClass itemDetalisClass : itemDetalisClasses)
        {
            Log.d("itemid", itemDetalisClass.getItem_id());
            Log.d(TAG, "get_allstock: "+itemDetalisClasses);
            Log.d(TAG, "get_allstock:TotalPrice" + itemDetalisClass.getTotalPrice());
            Log.d(TAG, "get_allstock:Qty" + itemDetalisClass.getItem_qty());
            Log.d(TAG, "get_allstock:PurchasePrice" + itemDetalisClass.getItem_purchase());
            Log.d(TAG, "get_allstock:UnitPrice" + itemDetalisClass.getUnit_price());
            Log.d(TAG, "get_allstock:UnitPrice" + itemDetalisClass.getUnit_price());
            Log.d(TAG, "get_allstoc:Status" + itemDetalisClass.getStatus());
            if(itemDetalisClass.getStatus().equalsIgnoreCase("infinite"))
            {
                Log.d(TAG, "get_allstoc: menu Item Status" + itemDetalisClass.getStatus());
            }
        }
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

         billingStockAdater = new ItemListForBilling(this, itemDetalisClasses, MakeBillingCart.this);
         recyclerView.setAdapter(billingStockAdater);

    }


    @Override
    public void showQunatity(int qty)
    {
        Log.d("showQty", String.valueOf(qty));
            total_bilingCart.setText(String.valueOf(qty));

            double totalUnitPrice= sqlDataBase.getTotalAmount();
            Log.d(TAG, "onResume:Amount"+totalUnitPrice);
           txt_totalAmount.setText(String.valueOf(Math.round(totalUnitPrice *100.00)/100.00));




    }




    private void getDetails(String name)
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        String p_id=mproductType.getSelectedItem().toString().trim();
        ArrayList<ItemDetalisClass> itemDetalis=  sqlDataBase.searchItem(name,p_id);
        Log.d(TAG, "getDetails:BY_search"+itemDetalis);

         if(itemDetalis.size()>0)
         {
             recyclerView.setHasFixedSize(true);
             RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
             recyclerView.setLayoutManager(layoutManager);
             billingStockAdater=new ItemListForBilling(this,itemDetalis,MakeBillingCart.this);
             recyclerView.setAdapter(billingStockAdater);


         }else
         {
             Toast.makeText(getApplication(),"Item Not Found",Toast.LENGTH_LONG).show();


         }


    }


    private void getDetailsForBarcode(String name)
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        String p_id=mproductType.getSelectedItem().toString().trim();
        ArrayList<ItemDetalisClass> itemDetalis=  sqlDataBase.searchItemForBarcode(name,p_id);
        Log.d(TAG, "getDetails:BY_search"+itemDetalis);

           if(itemDetalis.size()>0)
           {
               for (ItemDetalisClass itemDetalisClass : itemDetalis)
               {
                   itemDetalisClass.setSelectd_qty("1");
                   addItem(itemDetalisClass.getItem_id(), "1", itemDetalisClass.getGst_per(),
                           itemDetalisClass.getTotal_taxableValue(), itemDetalisClass.getTotalPrice(), itemDetalisClass.getChnagedUnit(),itemDetalisClass.getUnit_price(),itemDetalisClass.getDiscount());
               }
               searchProduct.setText(null);
           }



        if(itemDetalis.size()>0)
        {
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            billingStockAdater=new ItemListForBilling(this,itemDetalis,MakeBillingCart.this);
            recyclerView.setAdapter(billingStockAdater);


        }else
        {
            Toast.makeText(getApplication(),"Item Not Found",Toast.LENGTH_LONG).show();


        }
        ;


    }

    private void addItem(String item_id, String qty, String gst, String taxable, String totalAmont, String changedUnit, String unit_price, String discount) throws NumberFormatException
    {
        //  double taxableValue=Double.parseDouble(qty)*((Double.parseDouble(unit_price)-Double.parseDouble(dicount)));
        totalTaxableValue=Integer.parseInt(qty)*((Double.parseDouble(unit_price)-Double.parseDouble(discount)));
        Log.d(TAG, "onClick: Taxable Value"+totalTaxableValue);

        double taxableValue=totalTaxableValue;

        String taxable_value=String.valueOf(Math.round(taxableValue*100.0)/100.0);

        double gst_per=  Double.parseDouble(gst)/100.0;
        double cal_gst=  taxableValue*gst_per;
        String total_gst=  String.valueOf(Math.round(cal_gst*100.0)/100.0);
        Log.d(TAG, "addItem:ItemId"+item_id);
        Log.d(TAG, "addItem:Qty"+qty);
        Log.d(TAG, "addItem:taxableValue"+taxable_value);
        Log.d(TAG, "addItem:get_per"+gst_per);
        Log.d(TAG, "addItem:cal_gst"+total_gst);
        Log.d(TAG, "addItem:TotalAmount"+totalAmont);
        Log.d(TAG, "addItem:changedUnit"+changedUnit);


        // sqlDataBase.addSelectedItemFromBilling(item_id,qty,totalAmont,taxable_value,total_gst);
        sqlDataBase.updateQty(item_id, qty, totalAmont,taxable_value,String.valueOf(cal_gst),changedUnit);
        int  totalQty=sqlDataBase.getTotalQunatitiy();
        showQunatity(totalQty);

    }




    private void openDialog(final ItemDetalisClass list)
    {
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.add_more_qty);
        final TextInputEditText texxtqty=(TextInputEditText)dialog.findViewById(R.id.more_qty);
        Log.d(TAG, "openDialog: Qty"+texxtqty);

        dialog.setCancelable(false);

        AppCompatButton add=(AppCompatButton)dialog.findViewById(R.id.add_qty);
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

               billingStockAdater.set(list);
            }
        });

        AppCompatButton cancel=(AppCompatButton)dialog.findViewById(R.id.dis_cancel);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();


    }
    @Override
    protected void onPostResume() 
    {

        super.onPostResume();
        Log.d(TAG, "onPostResume: ");

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume:");
        try {
            int qty=  sqlDataBase.getTotalQunatitiy();
            Log.d(TAG, "onResume:Qty");
            total_bilingCart.setText(String.valueOf(qty));

            double totalUnitPrice=sqlDataBase.getTotalAmount();
            Log.d(TAG, "onResume:Amount"+totalUnitPrice);
            if(totalUnitPrice>0)
            {
                txt_totalAmount.setText(String.valueOf(Math.round(totalUnitPrice *100.00)/100.00));
            }



        }catch (NullPointerException e)
        {

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed:");
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

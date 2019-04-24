package com.inevitablesol.www.shopmanagement.billing_module;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.InstantBill_selectedItem;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.sql_lite.ItemDetailsParser;
import com.inevitablesol.www.shopmanagement.sql_lite.ItemDetalisClass;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.inevitablesol.www.shopmanagement.purchase_module.PurchaseBillingDetails.MyPREFERENCES;

public class Make_InstantBill extends AppCompatActivity implements DisplayQunatity, View.OnClickListener {
    private Spinner   mproductType;



    private String mProductName;
    private RecyclerView recyclerView;

    AppCompatButton  viewCart;
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
    private Context context=Make_InstantBill.this;
    private GlobalPool globalPool;

    private static final String GET_PRODUCT = " http://35.161.99.113:9000/webapi/product/list";
    private SwitchCompat switchCompat;
    private double totalTaxableValue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make__instant_bill);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mproductType=(Spinner)findViewById(R.id.spnn_product_bill);
        recyclerView = (RecyclerView)findViewById(R.id.bill_recyclerView);
        txt_custName=(TextView)findViewById(R.id.txt_billcustName);
        viewCart=(AppCompatButton)findViewById(R.id.bill_viewcart);
        searchProduct=(EditText)findViewById(R.id.bill_productname);
        switchCompat=(SwitchCompat)findViewById(R.id.barcodeswitch);

        searchProduct.setOnClickListener(this);
        txt_totalAmount=(TextView)findViewById(R.id.total_amount);
        globalPool=(GlobalPool)this.getApplicationContext();
        total_bilingCart=(TextView)findViewById(R.id.total_billin_qty);
        viewCart.setOnClickListener(this);

     sqlDataBase=new SqlDataBase(context);

        if( sqlDataBase.check_items())
        {
            Log.d(TAG, "onResponse:DB Available");

        }else
        {
            getAllItemDetails();

        }




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
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(mproductType);

            // Set popupWindow height to 500px
            popupWindow.setHeight((int)height/2);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }




    }



    @Override
    public void onClick(View v)
    {
        int id=v.getId();
        switch (id)
        {
            case R.id.bill_viewcart:
                Intent intent=new Intent(Make_InstantBill.this,InstantBill_selectedItem.class);
                intent.putExtra("supplier","99");
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
                    BillingProductAdapter productAdapter = new BillingProductAdapter(Make_InstantBill.this, R.layout.product_list, BillingProductParser.productName, BillingProductParser.productId);

                    mproductType.setAdapter(productAdapter);
                    mproductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                            mProductName = BillingProductParser.productId[position];
                            Log.d("name", mProductName);
                            get_allstock(mProductName);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

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
                    Toast.makeText(Make_InstantBill.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);


                return params;
            }
        });

    }



    public void get_allstock(final String mProductID)
    {

        Log.d("id", mProductID);
        ArrayList<ItemDetalisClass> itemDetalisClasses = sqlDataBase.getItemDetails(Integer.parseInt(mProductID));
        for (ItemDetalisClass itemDetalisClass : itemDetalisClasses) {
            Log.d("itemid", itemDetalisClass.getItem_id());
            Log.d(TAG, "get_allstock:TotalPrice" + itemDetalisClass.getTotalPrice());
            Log.d(TAG, "get_allstock:Qty" + itemDetalisClass.getItem_qty());
            Log.d(TAG, "get_allstock:PurchasePrice" + itemDetalisClass.getItem_purchase());
            Log.d(TAG, "get_allstock:UnitPrice" + itemDetalisClass.getUnit_price());
        }
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        ItemListForBilling billingStockAdater = new ItemListForBilling(this, itemDetalisClasses, Make_InstantBill.this);
        recyclerView.setAdapter(billingStockAdater);

//

    }


    @Override
    public void showQunatity(int qty)
    {
        Log.d("showQty", String.valueOf(qty));
        total_bilingCart.setText(String.valueOf(qty));

        double totalUnitPrice=sqlDataBase.getTotalAmount();
        Log.d(TAG, "onResume:Amount"+totalUnitPrice);

        txt_totalAmount.setText(String.valueOf(Math.round(totalUnitPrice *100.00)/100.00));




    }




    private void getDetails(String name)
    {
        String p_id=mproductType.getSelectedItem().toString().trim();
        ArrayList<ItemDetalisClass> itemDetalis=  sqlDataBase.searchItem(name,p_id);
        Log.d(TAG, "getDetails:BY_search"+itemDetalis);

        if(itemDetalis.size()>0)
        {
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            ItemListForBilling billingStockAdater=new ItemListForBilling(this,itemDetalis,Make_InstantBill.this);
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
            ItemListForBilling  billingStockAdater=new ItemListForBilling(this,itemDetalis,Make_InstantBill.this);
            recyclerView.setAdapter(billingStockAdater);


        }else
        {
            Toast.makeText(getApplication(),"Item Not Found",Toast.LENGTH_LONG).show();


        }
        ;


    }

    @Override
    protected void onPostResume()
    {

        super.onPostResume();
        Log.d(TAG, "onPostResume: ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume:");
        try {
            int  qty= sqlDataBase.getTotalQunatitiy();
            total_bilingCart.setText(String.valueOf(qty));

            double totalUnitPrice=sqlDataBase.getTotalAmount();
            Log.d(TAG, "onResume:Amount"+totalUnitPrice);
            if(totalUnitPrice>0)
            {
                txt_totalAmount.setText(String.valueOf(totalUnitPrice));
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




    private void getAllItemDetails()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_STOCKDETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();

                Log.d("Item Details  Billing",resp);
                ItemDetailsParser itemDetailsParser=new ItemDetailsParser(resp);
                itemDetailsParser.parseItemList();
                String  selectedItemQty="0";



                try
                {

                    String []    p_id        =ItemDetailsParser.product_id;
                    String []   item_id      =ItemDetailsParser.item_id;
                    String []   item_name    =ItemDetailsParser.item_name;
                    String []   stock_qty     =ItemDetailsParser.stock_qt;

                    String[]   item_mrp        =ItemDetailsParser.item_mrp;
                    String[]  purchase_price   =ItemDetailsParser.purchase_price;
                    String[]  gst             =ItemDetailsParser.gst;
                    String []  totalPrice     =ItemDetailsParser.total_price;
                    String [] discount       =ItemDetailsParser.discount;
                    String[] unit_Price      =ItemDetailsParser.unit_price;
                    String [] hsnCode=         ItemDetailsParser.hsn;
                    String[] status=        ItemDetailsParser.status;
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

                Log.d("new Db Installed","");










            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(Make_InstantBill.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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
                return params;
            }

        } ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

}

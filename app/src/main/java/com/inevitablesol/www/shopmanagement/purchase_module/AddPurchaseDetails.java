package com.inevitablesol.www.shopmanagement.purchase_module;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.inevitablesol.www.shopmanagement.MainActivity;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.Validation.Validation;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class AddPurchaseDetails extends AppCompatActivity implements View.OnClickListener {

    private Spinner s_productType, s_itemTypem;

    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private static final String TAG = "AddPurchaseDetails";
    TextView txt_hsnCode, txt_Company;
    AppCompatButton bt_next, bt_addTolist;
    private EditText et_quantity, et_unitPrice;

    private JSONArray jsonArray = new JSONArray();
    private int i = 0;
    private String vendor_id, gst_number, invoiceDate, invoiceNumber;

    double taxableValue = 0.0;
    double totalGST = 0.0;
    private LinearLayout ly_mrp;

    private CheckBox mrp_checkBox;
    private EditText et_purchase_mrp;
    private double unit_Mrp = 0.0;
    private RecyclerView recyclerView;
    private Spinner et_gst;
    private String  stateCode;

    //

    private ArrayList<SelectedItemClass> selectedItemClasses = new ArrayList<SelectedItemClass>();


    private String ITEMNAME;

    private EditText et_discount;

    private  AppCompatButton bt_deletRow;
    private int totalQty=0;

    private TextView tx_qty;


    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;

    private final int PICK_IMAGE_REQUEST = 71;

    private  TextView  tx_itembarcode,tx_measurementUnit;
    private  Spinner sub_unit;
    ArrayAdapter<CharSequence> adapter2;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase_details);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        s_productType = (Spinner) findViewById(R.id.spnn_product_type);
        s_itemTypem = (Spinner) findViewById(R.id.spnn_item_type);
        txt_hsnCode = (TextView) findViewById(R.id.txt_view_hsn);
        txt_Company = (TextView) findViewById(R.id.txt_view_company);
        tx_qty=(TextView)findViewById(R.id.p_totalQty);
        bt_addTolist = (AppCompatButton) findViewById(R.id.purchase_addtoList);
        bt_next = (AppCompatButton) findViewById(R.id.purchase_next);
        et_quantity = (EditText) findViewById(R.id.et_qty);
        et_unitPrice = (EditText) findViewById(R.id.et_unitPrice);
        et_gst = (Spinner) findViewById(R.id.et_gstpercentage);
        bt_deletRow=(AppCompatButton)findViewById(R.id.purchase_deleteRow);
        bt_deletRow.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gst_tax_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_gst.setAdapter(adapter);
        bt_next.setOnClickListener(this);
        bt_addTolist.setOnClickListener(this);
        et_discount = (EditText) findViewById(R.id.purchase_discount);
        et_purchase_mrp = (EditText) findViewById(R.id.et_mrp);
        ly_mrp = (LinearLayout) findViewById(R.id.ly_mrp);
        ly_mrp.setOnClickListener(this);
        mrp_checkBox = (CheckBox) findViewById(R.id.check_mrp);
        mrp_checkBox.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.elist_selectedItem);

          tx_itembarcode=(TextView)findViewById(R.id.tx_item_bar_code);
          tx_measurementUnit=(TextView)findViewById(R.id.tx_measurmentUnit);
          //sub_unit=(Spinner)findViewById(R.id.sub_measurementUnit);
         // tx_measurementUnit.setText("gram");



//        sub_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            /**
//             * Called when a new item was selected (in the Spinner)
//             */
//            public void onItemSelected(AdapterView<?> parent,
//                                       View view, int pos, long id)
//            {
//
//                Log.d(TAG, "onItemSelected: "+pos);
//                Log.d(TAG, "onItemSelected: "+sub_unit.getSelectedItem().toString());
//            }
//
//            public void onNothingSelected(AdapterView parent)
//            {
//                // Do nothing.
//            }
//        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        mrp_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    ly_mrp.setVisibility(View.GONE);

                    Log.d(TAG, "onCheckedChangedTrue: ");
                } else
                    {
                    Log.d(TAG, "onCheckedChanged:False ");
                    calculate_Mrp();
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
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(s_productType);
            android.widget.ListPopupWindow popupWindow2 = (android.widget.ListPopupWindow) popup.get(s_itemTypem);
            //android.widget.ListPopupWindow popupWindow3 = (android.widget.ListPopupWindow) popup.get(sub_unit);

            // Set popupWindow height to 500px
            popupWindow.setHeight((int)height/2);
            popupWindow2.setHeight((int)height/2);
            //popupWindow3.setHeight((int)height/2);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));
        getProduct();

        Intent intent = getIntent();
        if (intent.hasExtra("v_id")) {
            vendor_id = intent.getStringExtra("v_id");
            gst_number = intent.getStringExtra("gst_nummber");
            invoiceDate = intent.getStringExtra("invoiceDate");
            invoiceNumber = intent.getStringExtra("invoiceNumber");
            stateCode=intent.getStringExtra("stateCode");
        }


    }

    private void calculate_Mrp()
    {
        ly_mrp.setVisibility(View.VISIBLE);


    }




    private void getProduct()
    {


        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_NEW_PRODUCT, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try {
                    Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                    try {

                        JSONObject msg = new JSONObject(response);
                        String message = msg.getString("message");


                        ProductParser productParser = new ProductParser(response);
                        productParser.productParser();
                        ProductAdapter productAdapter = new ProductAdapter(AddPurchaseDetails.this, R.layout.product_list, ProductParser.productName, ProductParser.productId);
                        s_productType.setAdapter(productAdapter);

                        s_productType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            /**
                             * Called when a new item was selected (in the Spinner)
                             */
                            public void onItemSelected(AdapterView<?> parent,
                                                       View view, int pos, long id)

                            {

                                String p_id = ProductParser.productId[pos];
                                getItemDetailsByID(p_id);

                            }

                            public void onNothingSelected(AdapterView parent) {
                                // Do nothing.
                            }
                        });


                    } catch (JSONException e) {

                    }


                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(AddPurchaseDetails.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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

    private void getItemDetailsByID(final String p_id)
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETITEMDETAISBYID, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try {
                    Log.d(TAG, "Items: " + response);
                    JSONObject msg = new JSONObject(response);
                    String message = msg.getString("message");

                    if (message.equalsIgnoreCase("Data available"))
                    {


                        ItemParser productParser = new ItemParser(response);
                        productParser.productParser();
                        ///  List<Map<String,String >> items=productParser.getItemArray();
                        ItemAdapter productAdapter = new ItemAdapter(AddPurchaseDetails.this, R.layout.product_list, ItemParser.itemName, ItemParser.itemId);
                        s_itemTypem.setAdapter(productAdapter);

                        s_itemTypem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            /**
                             * Called when a new item was selected (in the Spinner)
                             */
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
                            {

                                String i_id = ItemParser.itemId[pos];
                                ITEMNAME = ItemParser.itemName[pos];
                                Log.d(TAG, "onItemSelected: " + i_id);
                                Log.d(TAG, "onItemSelected: " + ITEMNAME);
                                getItemDetailsByItemID(i_id);

                            }

                            public void onNothingSelected(AdapterView parent)
                            {
                                // Do nothing.
                            }
                        });


                    } else
                    {
                        try {

                            txt_hsnCode.setText("");
                            txt_Company.setText("");
                            s_itemTypem.setAdapter(null);


                        } catch (Exception e)
                        {
                            Log.i(TAG, "onResponse:Exception"+e);
                        }

                    }


                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(AddPurchaseDetails.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("product_id", p_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void getItemDetailsByItemID(final String i_id)
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETITEMDETAILSBYITEMID, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try
                {


                    Log.d(TAG, "ItemsInfo: " + response);
                    JSONObject msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Data available")) {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("records");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String companyName = jsonObject1.getString("company");
                        String hsnCode = jsonObject1.getString("hsn_ssc_code");
                        txt_hsnCode.setText(hsnCode);
                        txt_Company.setText(companyName);
                        tx_measurementUnit.setText(jsonObject1.getString("measurement_unit"));
                        tx_itembarcode.setText(jsonObject1.getString("item_barcode"));

//                        try {
//                            String unit=jsonObject1.getString("measurement_unit");
//
//                            if(unit.equalsIgnoreCase("Unit"))
//                            {
//                                adapter2  = ArrayAdapter.createFromResource(getApplicationContext(),
//                                        R.array.unit, android.R.layout.simple_spinner_item);
//
//                            }else if(unit.equalsIgnoreCase("Gram"))
//                            {
//                                adapter2  = ArrayAdapter.createFromResource(getApplicationContext(),
//                                        R.array.gram, android.R.layout.simple_spinner_item);
//
//                            }else if(unit.equalsIgnoreCase("liter"))
//                            {
//                                adapter2  = ArrayAdapter.createFromResource(getApplicationContext(),
//                                        R.array.liter, android.R.layout.simple_spinner_item);
//
//                            }
//                            else if(unit.equalsIgnoreCase("meter"))
//                            {
//                                adapter2  = ArrayAdapter.createFromResource(getApplicationContext(),
//                                        R.array.meter, android.R.layout.simple_spinner_item);
//
//                            }
//                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            sub_unit.setAdapter(adapter2);
//                        } catch (JSONException e)
//                        {
//                            e.printStackTrace();
//                        }


                    } else
                    {
                        Log.d(TAG, "onResponse:Data Not Available");

                        txt_hsnCode.setText("");
                        txt_Company.setText("");

                        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(AddPurchaseDetails.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("item_id", i_id);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.purchase_deleteRow:

                 deleteRowFrom_list();
                break;
            case R.id.purchase_addtoList:
                String hsn_code = txt_hsnCode.getText().toString().trim();
                String company = txt_Company.getText().toString().trim();

                if (mrp_checkBox.isChecked())
                {
                    if (hsn_code != null && company.length() > 0)
                    {
                        AddToCart();
                        try
                        {
                            boolean b_value = addGST();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select product", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    try {

                        unit_Mrp = Double.parseDouble(et_purchase_mrp.getText().toString().trim());
                        if (hsn_code != null && company.length() > 0 && unit_Mrp > 0)
                        {
                            AddToCart();
                            try {
                                boolean b_value = addGST();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (unit_Mrp == 0) {
                                et_purchase_mrp.setError("Add Mrp");
                            }
                        }
                    } catch (NumberFormatException e) {
                        et_purchase_mrp.setError("Add Mrp");
                    } catch (Exception e) {

                    }
                }


                break;
            case R.id.purchase_next:
                try {

                      if(jsonArray.length()>0)
                      {
                          Intent intent = new Intent(AddPurchaseDetails.this, PurchaseBillingDetails.class);
                          intent.putExtra("v_id", vendor_id);
                          intent.putExtra("gst_number", gst_number);
                          intent.putExtra("invoiceDate", invoiceDate);
                          intent.putExtra("invoiceNumber", invoiceNumber);
                          intent.putExtra("itemArray", String.valueOf(jsonArray));
                          intent.putExtra("taxableValue", String.valueOf(taxableValue));
                          intent.putExtra("gstValue", String.valueOf(totalGST));
                          intent.putExtra("stateCode", stateCode);
                          startActivity(intent);
                      }else
                      {
                          Toast.makeText(this, "Please Add Item", Toast.LENGTH_SHORT).show();
                      }


                } catch (Exception e)
                {
                    e.printStackTrace();
                }


                Log.d(TAG, "onClick: " + jsonArray);
                break;
        }

    }

    private void deleteRowFrom_list()
    {
      i=0;
        totalQty=0;

        if(jsonArray.length()>0)
        {



            Iterator<SelectedItemClass> selectedItemClassIterator=selectedItemClasses.iterator();
            while (selectedItemClassIterator.hasNext())
            {
                 SelectedItemClass selectedItemClass=selectedItemClassIterator.next();
                  if(selectedItemClass.isChecked())
                  {
                      selectedItemClassIterator.remove();
                  }
            }

              DisplayList();


            Gson gson = new GsonBuilder().create();
            JsonArray myCustomArray = gson.toJsonTree(selectedItemClasses).getAsJsonArray();
            Log.d(TAG, "deleteRowFrom_list: Json Array"+myCustomArray);
            jsonArray=new JSONArray();

            for (SelectedItemClass selectedItemClass:selectedItemClasses)
            {
               // Log.d(TAG, "deleteRowFrom_list: Delete Row"+selectedItemClass.toString());

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("item_id", selectedItemClass.getItemId());
                    jsonObject.put("unit_price", selectedItemClass.getItemUnitPrice());
                    jsonObject.put("gst", selectedItemClass.getItemGst());
                    jsonObject.put("qty", selectedItemClass.getItemQty());
                    jsonObject.put("itemPrice", selectedItemClass.getItemPrice());
                    //org price =itemPrice +gstPrice-dicount;
                    jsonObject.put("orgPrice",selectedItemClass.getOrg_price());
                    jsonObject.put("GST_price", selectedItemClass.getGST_price());
                    jsonObject.put("mrp", selectedItemClass.getMrp());
                    jsonObject.put("discount", selectedItemClass.getDicount());
                    jsonObject.put("p_orgPrice",selectedItemClass.getP_orgPrice());
                    jsonObject.put("p_gst",selectedItemClass.getItemGst());
                    jsonObject.put("p_mrp",selectedItemClass.getP_mrp());

                    totalQty=Integer.parseInt(selectedItemClass.getItemQty());
                    tx_qty.setText(String.valueOf(totalQty));
                    jsonArray.put(i, jsonObject);

                    i++;
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

            Log.d(TAG, "deleteRowFrom_list:New Json"+jsonArray);


        }else
        {
            Toast.makeText(this, "Please select AtLeast", Toast.LENGTH_SHORT).show();

        }


    }

    private boolean addGST() throws Exception
    {
        taxableValue=0.0;
        totalGST=0.0;
        Log.d(TAG, "addGST: "+jsonArray);
        if (jsonArray.length() > 0)
        {
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String totalPrice = jsonObject.getString("itemPrice");
                String gst = jsonObject.getString("GST_price");
                taxableValue += Double.parseDouble(totalPrice);
                totalGST += Double.parseDouble(gst);


            }
            return true;
        } else {
            return false;
        }


    }

    private void AddToCart()
    {

        String shortCut = null;


        SelectedItemClass selectedItem = new SelectedItemClass();
        try {

            if (mrp_checkBox.isChecked())
            {
                unit_Mrp = 0.0;
            } else
            {
                unit_Mrp = Double.parseDouble(et_purchase_mrp.getText().toString().trim());
            }

            String productType = (String) s_productType.getSelectedItem().toString().trim();
            String item = (String) s_itemTypem.getSelectedItem().toString().trim();
            String hsn_code =     txt_hsnCode.getText().toString().trim();
            String company =     txt_Company.getText().toString().trim();
            String measurmentUnit=tx_measurementUnit.getText().toString().trim();
            String unitPrice =   et_unitPrice.getText().toString().trim();
            String qty =         et_quantity.getText().toString().trim();
//            String  unit= sub_unit.getSelectedItem().toString().trim();
//            try {
//                  shortCut=unit.substring(unit.indexOf("(")+1,unit.indexOf(")"));
//                Log.d(TAG, "AddToCart: Short cut"+shortCut);
//            } catch (Exception e)
//            {
//                shortCut=unit;
//
//                e.printStackTrace();
//            }
            final String gstpercentage = et_gst.getSelectedItem().toString().trim();

            //String  gstpercentage=et_gst.getText().toString().trim();
            String discount = et_discount.getText().toString().trim();
                   if(discount.isEmpty())
                   {
                       discount="0";
                   }


            if (gstpercentage.length() > 0 && qty.length() > 0 && unitPrice.length() > 0 )
            {
                totalQty+=Integer.parseInt(qty);
                tx_qty.setText(String.valueOf(totalQty));
                try
                {
                    double itemPrice = (Double.parseDouble(unitPrice) * Double.parseDouble(qty))-Double.parseDouble(discount);
                    BigDecimal decimalItemPrice=new BigDecimal(itemPrice);
                    Log.d(TAG, "AddToCart:BigValue"+decimalItemPrice);
                    double itemGst_Price = itemPrice * Double.parseDouble(gstpercentage) / 100;
                    BigDecimal decimalitemGst_Price=new BigDecimal(itemGst_Price);
                    double orgPrice=itemPrice+itemGst_Price;
                    BigDecimal decimalOrgPrice=new BigDecimal(orgPrice);
                     String  dorg=String.valueOf(decimalOrgPrice);
                     String   d_gst=String.valueOf(decimalitemGst_Price);
                     double o_price=Math.round(Double.parseDouble(dorg)* 100.0)/100.0;
                      double o_gst=Math.round(Double.parseDouble(d_gst)* 100.0)/100.0;


                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("item_id", item);
                    jsonObject.put("unit_price", unitPrice);
                    jsonObject.put("gst", gstpercentage);
                    jsonObject.put("qty", qty);
                    jsonObject.put("itemPrice", decimalItemPrice);
                    //org price =itemPrice +gstPrice-dicount;
                    jsonObject.put("orgPrice",o_price);
                    jsonObject.put("GST_price", o_gst);
                    jsonObject.put("mrp", unit_Mrp);
                    jsonObject.put("discount", discount);
                    jsonObject.put("p_orgPrice",unitPrice);
                    jsonObject.put("p_gst",gstpercentage);
                    jsonObject.put("p_mrp",unit_Mrp);
                    //jsonObject.put("shortCut",shortCut);
                    jsonObject.put("measurmentUnit",measurmentUnit);
                    //jsonObject.put("unit",unit);
                    jsonArray.put(i, jsonObject);

                    i++;
                    txt_Company.setText("");
                    txt_hsnCode.setText("");
                    tx_measurementUnit.setText("");
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gst_tax_array, android.R.layout.simple_spinner_item);
                    et_gst.setAdapter(adapter);
                    et_unitPrice.setText("");
                    et_quantity.setText("");
                    et_purchase_mrp.setText("");
                    et_discount.setText("");


                    selectedItem.setItemName(ITEMNAME);
                    selectedItem.setItemPrice(String.valueOf(decimalItemPrice));
                    selectedItem.setItemUnitPrice(String.valueOf(unitPrice));
                    selectedItem.setItemQty(qty);
                    selectedItem.setItemGst(gstpercentage);

                     selectedItem.setItemId(item);
                      selectedItem.setOrg_price(String.valueOf(o_price));
                      selectedItem.setDicount(discount);
                      selectedItem.setMrp(String.valueOf(unit_Mrp));
                      selectedItem.setGST_price(String.valueOf(o_gst));
                      selectedItem.setP_gst(String.valueOf(o_gst));
                      selectedItem.setP_orgPrice(String.valueOf(o_price));
                      selectedItem.setP_mrp(String.valueOf(unit_Mrp));
                      selectedItemClasses.add(selectedItem);

                    DisplayList();
                } catch (NumberFormatException e)
                {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
            {
                _validateData();


            }

            Log.d(TAG, "AddedToCart:" + jsonArray);

        } catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    private boolean _validateData()
    {
        boolean  val= true;
        if(Validation.hasText(et_quantity)) val=true;
        if(Validation.hasText(et_unitPrice)) val=true;
        if(Validation.hasText(et_discount))
        {
            et_discount.setText("0");
            val=true;
        }
        return val;

    }

    private void DisplayList()
    {
        SelectedItemAdapter selectedItemAdapter = new SelectedItemAdapter(selectedItemClasses, AddPurchaseDetails.this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(selectedItemAdapter);

    }


}
package com.inevitablesol.www.shopmanagement.purchase_module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.Validation.Validation;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BarcodeProduct extends AppCompatActivity {
    private static final String TAG = "BarcodeProduct";
    @BindView(R.id.edt_barcode)
    AppCompatEditText barcodeSearch;
    @BindView(R.id.close_button)
    ImageView iv_close;

    GlobalPool globalPool;
    @BindView(R.id.tx_item_bar_code)
    TextView txItemBarCode;
    @BindView(R.id.tx_measurmentUnit)
    TextView txMeasurmentUnit;
    @BindView(R.id.txt_view_hsn)
    TextView txtViewHsn;
    @BindView(R.id.txt_view_company)
    TextView txtViewCompany;
    @BindView(R.id.et_qty)
    EditText etQty;
    @BindView(R.id.et_unitPrice)
    EditText etUnitPrice;
    @BindView(R.id.sub_measurementUnit)
    Spinner subMeasurementUnit;
    @BindView(R.id.purchase_discount)
    EditText purchaseDiscount;
    @BindView(R.id.textView27)
    TextView textView27;
    @BindView(R.id.check_mrp)
    CheckBox checkMrp;
    @BindView(R.id.et_mrp)
    EditText etMrp;
    @BindView(R.id.ly_mrp)
    LinearLayout lyMrp;
    @BindView(R.id.et_gstpercentage)
    Spinner etGstpercentage;
    @BindView(R.id.purchase_addtoList)
    AppCompatButton purchaseAddtoList;
    @BindView(R.id.ll_userinfo)
    LinearLayout llUserinfo;
    @BindView(R.id.textView43)
    TextView textView43;
    @BindView(R.id.elist_selectedItem)
    RecyclerView recyclerView;
    @BindView(R.id.p_totalQty)
    TextView pTotalQty;
    @BindView(R.id.purchase_deleteRow)
    AppCompatButton purchaseDeleteRow;
    @BindView(R.id.purchase_next)
    AppCompatButton purchaseNext;
    @OnClick(R.id.purchase_next)
    public  void next()
    {
        try {

            if(jsonArray.length()>0)
            {
                Intent intent = new Intent(BarcodeProduct.this, PurchaseBillingDetails.class);
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

    }
    @BindView(R.id.activity_add_purchase_details)
    NestedScrollView activityAddPurchaseDetails;
    @BindView(R.id.txtItemid)
    TextView txtItemid;
    private Context context = this;

    private Spinner sub_unit;
    ArrayAdapter<CharSequence> adapter2;
    private double unit_Mrp;
    private int totalQty;
    private JSONArray jsonArray = new JSONArray();
    private int i = 0;

    private ArrayList<SelectedItemClass> selectedItemClasses = new ArrayList<SelectedItemClass>();


    private String ITEMNAME;
    private double taxableValue;
    private double totalGST;
    private TextView txitemId;

    private String vendor_id, gst_number, invoiceDate, invoiceNumber;
    private String  stateCode;
    private TextView txItename;
    private  TextView txProductName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_product);
        ButterKnife.bind(this);
        globalPool = (GlobalPool) this.getApplicationContext();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gst_tax_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etGstpercentage.setAdapter(adapter);

        barcodeSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s)
            {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        getItemDetails(s.toString());

                    }
                }, 1000);


            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeSearch.getText().clear();
            }
        });

        txItename=(TextView)findViewById(R.id.txItemname);
        txProductName =(TextView)findViewById(R.id.txProductName);

        Intent intent = getIntent();
        if (intent.hasExtra("v_id")) {
            vendor_id = intent.getStringExtra("v_id");
            gst_number = intent.getStringExtra("gst_nummber");
            invoiceDate = intent.getStringExtra("invoiceDate");
            invoiceNumber = intent.getStringExtra("invoiceNumber");
            stateCode=intent.getStringExtra("stateCode");
        }

        checkMrp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    lyMrp.setVisibility(View.GONE);

                    Log.d(TAG, "onCheckedChangedTrue: ");
                } else
                {
                    Log.d(TAG, "onCheckedChanged:False ");
                    calculate_Mrp();
                }


            }
        });

    }
    private void calculate_Mrp()
    {
        lyMrp.setVisibility(View.VISIBLE);


    }

    @OnClick(R.id.purchase_addtoList)
    public void add() {
        Log.d(TAG, "add: Test Add Item");


        String hsn_code = txtViewHsn.getText().toString().trim();
        String company = txtViewCompany.getText().toString().trim();

        if (checkMrp.isChecked()) {
            if (hsn_code != null && company.length() > 0) {
                AddToCart();
                try {
                    boolean b_value = addGST();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please Select product", Toast.LENGTH_SHORT).show();
            }

        } else {
            try {

                unit_Mrp = Double.parseDouble(etMrp.getText().toString().trim());
                if (hsn_code != null && company.length() > 0 && unit_Mrp > 0) {
                    AddToCart();
                    try {
                        boolean b_value = addGST();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (unit_Mrp == 0) {
                        etMrp.setError("Add Mrp");
                    }
                }
            } catch (NumberFormatException e) {
                etMrp.setError("Add Mrp");
            } catch (Exception e) {

            }
        }
    }

    private boolean addGST() throws Exception {
        taxableValue = 0.0;
        totalGST = 0.0;
        Log.d(TAG, "addGST: " + jsonArray);
        if (jsonArray.length() > 0) {
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

    @OnClick(R.id.purchase_deleteRow)
    public void deleteRow() {
        Log.d(TAG, "deleteRow: Delete Row");
    }

    {
        Log.d(TAG, "add: Test Add Item");
    }


    private void getItemDetails(String s)
    {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("barcode", s);
            jsonObject.addProperty("dbname", globalPool.getDbname());
            Log.d(TAG, "getItemDetails: " + s.trim());
            Ion.with(this)
                    .load("POST", WebApi.GETITEMDETAILSBYBARCODE)
                    .progressHandler(new ProgressCallback() {

                        @Override
                        public void onProgress(long downloaded, long total) {

                        }
                    })
                    .setJsonObjectBody(jsonObject)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result)
                        {
                            Log.d(TAG, "onCompleted:" + result);

                            showData(result);

                            // do stuff with the result or error
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showData(JsonObject response)
    {


        try {


            JSONObject msg = new JSONObject(response.toString());
            String message = msg.getString("message");
            if (message.equalsIgnoreCase("Data available")) {

                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("records");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String companyName = jsonObject1.getString("company");
                String hsnCode = jsonObject1.getString("hsn_ssc_code");
                txtViewHsn.setText(hsnCode);
                txtViewCompany.setText(companyName);
                ITEMNAME=jsonObject1.getString("item_name");
                txtItemid.setText(jsonObject1.getString("item_id"));
                txMeasurmentUnit.setText(jsonObject1.getString("measurement_unit"));
                txItemBarCode.setText(jsonObject1.getString("item_barcode"));

                txItename.setText(jsonObject1.getString("item_name"));

                txProductName.setText(jsonObject1.getString("product_type"));

                try {
                    String unit = jsonObject1.getString("measurement_unit");

                    if (unit.equalsIgnoreCase("Unit"))
                    {
                        adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
                                R.array.unit, android.R.layout.simple_spinner_item);

                    } else if (unit.equalsIgnoreCase("Gram")) {
                        adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
                                R.array.gram, android.R.layout.simple_spinner_item);

                    } else if (unit.equalsIgnoreCase("liter")) {
                        adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
                                R.array.liter, android.R.layout.simple_spinner_item);

                    } else if (unit.equalsIgnoreCase("meter")) {
                        adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
                                R.array.meter, android.R.layout.simple_spinner_item);

                    }
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subMeasurementUnit.setAdapter(adapter2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else
                {
                Log.d(TAG, "onResponse:Data Not Available");

                txtViewHsn.setText("");
                txtViewCompany.setText("");
                txProductName.setText("");
                 txItename.setText("");


                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e)
        {
            Log.d(TAG, "showData: "+e);

        }


    }


    private void AddToCart() {

        String shortCut = null;


        SelectedItemClass selectedItem = new SelectedItemClass();
        try {

            if (checkMrp.isChecked()) {
                unit_Mrp = 0.0;
            } else {
                unit_Mrp = Double.parseDouble(etMrp.getText().toString().trim());
            }

          //  String productType = (String).getSelectedItem().toString().trim();
            String item = txtItemid.getText().toString().trim();
            String hsn_code = txtViewHsn.getText().toString().trim();
            String company = txtViewCompany.getText().toString().trim();
            String measurmentUnit = subMeasurementUnit.getSelectedItem().toString().trim();
            String unitPrice = etUnitPrice.getText().toString().trim();
            String qty = etQty.getText().toString().trim();
            String unit = subMeasurementUnit.getSelectedItem().toString().trim();
            try {
                shortCut = unit.substring(unit.indexOf("(") + 1, unit.indexOf(")"));
                Log.d(TAG, "AddToCart: Short cut" + shortCut);
            } catch (Exception e) {

                e.printStackTrace();
            }
            final String gstpercentage = etGstpercentage.getSelectedItem().toString().trim();

            //String  gstpercentage=et_gst.getText().toString().trim();
            String discount = purchaseDiscount.getText().toString().trim();
            if (discount.isEmpty()) {
                discount = "0";
            }


            if (gstpercentage.length() > 0 && qty.length() > 0 && unitPrice.length() > 0) {
                totalQty += Integer.parseInt(qty);
                pTotalQty.setText(String.valueOf(totalQty));
                try {
                    double itemPrice = (Double.parseDouble(unitPrice) * Double.parseDouble(qty)) - Double.parseDouble(discount);
                    BigDecimal decimalItemPrice = new BigDecimal(itemPrice);
                    Log.d(TAG, "AddToCart:BigValue" + decimalItemPrice);
                    double itemGst_Price = itemPrice * Double.parseDouble(gstpercentage) / 100;
                    BigDecimal decimalitemGst_Price = new BigDecimal(itemGst_Price);
                    double orgPrice = itemPrice + itemGst_Price;
                    BigDecimal decimalOrgPrice = new BigDecimal(orgPrice);
                    String dorg = String.valueOf(decimalOrgPrice);
                    String d_gst = String.valueOf(decimalitemGst_Price);
                    double o_price = Math.round(Double.parseDouble(dorg) * 100.0) / 100.0;
                    double o_gst = Math.round(Double.parseDouble(d_gst) * 100.0) / 100.0;


                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("item_id", item);
                    jsonObject.put("unit_price", unitPrice);
                    jsonObject.put("gst", gstpercentage);
                    jsonObject.put("qty", qty);
                    jsonObject.put("itemPrice", decimalItemPrice);
                    //org price =itemPrice +gstPrice-dicount;
                    jsonObject.put("orgPrice", o_price);
                    jsonObject.put("GST_price", o_gst);
                    jsonObject.put("mrp", unit_Mrp);
                    jsonObject.put("discount", discount);
                    jsonObject.put("p_orgPrice", unitPrice);
                    jsonObject.put("p_gst", gstpercentage);
                    jsonObject.put("p_mrp", unit_Mrp);
                    jsonObject.put("shortCut", shortCut);
                    jsonObject.put("measurmentUnit", measurmentUnit);
                    jsonObject.put("unit", unit);
                    jsonArray.put(i, jsonObject);

                    i++;
                    txtViewCompany.setText("");
                    txtViewHsn.setText("");
                    txMeasurmentUnit.setText("");
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gst_tax_array, android.R.layout.simple_spinner_item);
                    etGstpercentage.setAdapter(adapter);
                    etUnitPrice.setText("");
                    etQty.setText("");
                    etMrp.setText("");
                    purchaseDiscount.setText("");
                    txtViewHsn.setText("");
                    txtViewCompany.setText("");
                    txProductName.setText("");
                    txItename.setText("");
                    txItemBarCode.setText("");
                    barcodeSearch.setText(null);


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
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                _validateData();


            }

            Log.d(TAG, "AddedToCart:" + jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private boolean _validateData() {
        boolean val = true;
        if (Validation.hasText(etQty)) val = true;
        if (Validation.hasText(etUnitPrice)) val = true;
        if (Validation.hasText(purchaseDiscount)) {
            purchaseDiscount.setText("0");
            val = true;
        }
        return val;

    }

    private void DisplayList() {
        SelectedItemAdapter selectedItemAdapter = new SelectedItemAdapter(selectedItemClasses, BarcodeProduct.this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(selectedItemAdapter);

    }

}

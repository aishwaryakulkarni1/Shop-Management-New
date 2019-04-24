package com.inevitablesol.www.shopmanagement.MenuItemModule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

public class Menu_SelectedItem extends AppCompatActivity implements View.OnClickListener {


    AppCompatButton btn_add_to_items;
    SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private String TAG;


    private  TextView txt_itemName,txt_productName,txt_company,txt_specification,txt_hsn,txt_productType;
    private  TextView txt_gst,txt_originalPrice,txt_mrp,txt_storageQty;

    private TextInputEditText et_unitPrice,et_disper,et_disRs,et_totalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_item);
        txt_productName=(TextView)findViewById(R.id.add_new_productName);
        txt_productType=(TextView)findViewById(R.id.add_new_productType);
        txt_company=(TextView)findViewById(R.id.add_new_productCompany);
        txt_specification=(TextView)findViewById(R.id.add_new_productSpecification);
        txt_hsn=(TextView)findViewById(R.id.add_new_productHSN);
        txt_gst=(TextView)findViewById(R.id.add_new_gst);
        txt_mrp=(TextView)findViewById(R.id.add_newMrp);
        txt_storageQty=(TextView)findViewById(R.id.add_newStorageQty);
        txt_originalPrice=(TextView)findViewById(R.id.add_newOriginalPrice);
        et_unitPrice=(TextInputEditText)findViewById(R.id.mh_unitPrice);
        et_disRs=(TextInputEditText)findViewById(R.id.mh_disRs);
        et_disper=(TextInputEditText)findViewById(R.id.mh_disPer);
        et_totalPrice=(TextInputEditText)findViewById(R.id.mh_totalPrice);

//        btn_add_to_items=(AppCompatButton)findViewById(R.id.btn_add_to_items);
//        btn_add_to_items.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");

        Intent intent=getIntent();

        if (intent.hasExtra("item_id"))
        {
            String name = intent.getStringExtra("name");
            String product_type = intent.getStringExtra("product_type");
            String company = intent.getStringExtra("company");
            String original_price = intent.getStringExtra("original_price");
            String specification = intent.getStringExtra("specification");
            String mrp = intent.getStringExtra("mrp");
            String hsn_san_code = intent.getStringExtra("hsn");
            String gst = intent.getStringExtra("gst");
            String storageQty= intent.getStringExtra("storage_qty");
            String unit_price=intent.getStringExtra("unit_price");
            et_unitPrice.setText(unit_price);
            et_disper.setText(intent.getStringExtra("disPer"));
            et_disRs.setText(intent.getStringExtra("discount"));

            txt_productType.setText(product_type);
            txt_company.setText(company);
            txt_productName.setText(name);
            txt_specification.setText(specification);
            txt_mrp.setText(mrp);
            txt_hsn.setText(hsn_san_code);
            txt_originalPrice.setText(intent.getStringExtra("total_price"));
            txt_gst.setText(gst);
            txt_storageQty.setText(storageQty);
            et_totalPrice.setText(intent.getStringExtra("total_price"));

//            intent.putExtra("itemId", stockInfo.getItem_id());
//            intent.putExtra("item_name", stockInfo.getItem_name());
//            intent.putExtra("company",stockInfo.getCompany());
//            intent.putExtra("owner",stockInfo.getOwner());
//            intent.putExtra("product_type", stockInfo.getProduct_type());
//            intent.putExtra("product_id",stockInfo.getProduct_id());
//            intent.putExtra("storageqty", stockInfo.getStorage_qty());
//            intent.putExtra("stockQty",stockInfo.getStock_qty());
//            intent.putExtra("original_price", stockInfo.getOriginal_price());
//            intent.putExtra("specification", stockInfo.getSpecification());
//            intent.putExtra("mrp", stockInfo.getMrp());
//            intent.putExtra("totalPrice",stockInfo.getTotalPrice());
//            intent.putExtra("unitPrice",stockInfo.getUnitPrice());
//            intent.putExtra("discount",stockInfo.getDiscount());
//            intent.putExtra("gst",stockInfo.getGst());
//            intent.putExtra("hsn_ssc_code", stockInfo.getHsn_ssc_code());
//            startActivity(intent);



            //Toast.makeText(this, "HSN : " + hsn_san_code, Toast.LENGTH_SHORT).show();



        } else {

        }





//                                        Intent editIntent = new Intent(Edit_Items.this, EditItemActivity.class);
//                                        editIntent.putExtra("hsn",stockInfo.getHsn_ssc_code());
//                                        editIntent.putExtra("company",stockInfo.getCompany());
//                                        editIntent.putExtra("storage_qty",stockInfo.getStorage_qty());
//                                        editIntent.putExtra("item_price",stockInfo.getOriginal_price());
//                                        editIntent.putExtra("name", stockInfo.getItem_name());
//                                        editIntent.putExtra("product_id", stockInfo.getProduct_id());
//                                        editIntent.putExtra("item_id", String.valueOf(stockInfo.getItem_id()));
//                                        editIntent.putExtra("product_type", stockInfo.getProduct_type());
//                                        editIntent.putExtra("mrp", stockInfo.getMrp());
//                                        editIntent.putExtra("specification", stockInfo.getSpecification());
//                                        editIntent.putExtra("unitPrice",stockInfo.getUnitPrice());
//                                        editIntent.putExtra("discount",stockInfo.getDiscount());
//                                        editIntent.putExtra("totalPrice",stockInfo.getTotalPrice());
//                                        editIntent.putExtra("gst",stockInfo.getGst());
//                                        startActivityForResult(editIntent,1);
        }


    @Override
    public void onClick(View v)
    {

    }

}

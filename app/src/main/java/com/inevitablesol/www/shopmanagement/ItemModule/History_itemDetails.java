package com.inevitablesol.www.shopmanagement.ItemModule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

public class History_itemDetails extends AppCompatActivity
{
    private TextView txt_h_item,txt_h_cname,txt_pname,txt_itemPrice,txt_discount,txt_specification,txt_unitPrice,txt_totalPrice,txt_gst;
    private TextView txt_stockStorage,txt_Type;
    private TextView txt_mrp,txt_hsn;

    private  TextView tx_discountPer;
    private static final String TAG = "History_itemDetails";


    private  TextView txnewunitPrice,txnewDiscount,txt_newGst,txt_newstockQty;
    private  TextView txt_itemBarcode;
    private  TextView txt_mUnit;
    private  TextView txt_unit;

     private  TextView txt_pid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_item_details);
        txt_discount=(TextView)findViewById(R.id.h_discount);
        txt_gst=(TextView)findViewById(R.id.h_gst);
        txt_h_cname=(TextView)findViewById(R.id.h_cName);
        txt_hsn=(TextView)findViewById(R.id.h_hsn);
        txt_specification=(TextView)findViewById(R.id.h_specification);
        txt_stockStorage=(TextView)findViewById(R.id.h_stockStorage);
        txt_unitPrice=(TextView)findViewById(R.id.h_unitPrice);
        txt_mrp=(TextView)findViewById(R.id.h_mrp);
        txt_totalPrice=(TextView)findViewById(R.id.h_totalPrice);
        txt_h_item=(TextView)findViewById(R.id.h_itemName);
        txt_pname=(TextView)findViewById(R.id.h_pame);
        txt_itemPrice=(TextView)findViewById(R.id.h_itemPrice);
         txt_Type=(TextView)findViewById(R.id.h_type);
         txt_pid=(TextView)findViewById(R.id.txt_p_id);
        txt_totalPrice=(TextView)findViewById(R.id.tv_discountedPrice);
        tx_discountPer=(TextView)findViewById(R.id.h_dicountPer) ;

        txt_newGst=(TextView)findViewById(R.id.add_new_gst);
        txnewDiscount=(TextView)findViewById(R.id.add_newMrp);
        txnewunitPrice=(TextView)findViewById(R.id.add_newunitPrice);
        txt_newstockQty=(TextView)findViewById(R.id.add_newStockQty);


        txt_mUnit=(TextView)findViewById(R.id.txt_munit);
        txt_unit=(TextView)findViewById(R.id.txt_unit);
        txt_itemBarcode=(TextView)findViewById(R.id.txt_itembarcode);




            Intent intent=getIntent();
        StockInfo stockInfo= (StockInfo) getIntent().getSerializableExtra("stockinfo");
        Log.d(TAG, "onCreate: "+stockInfo.toString());

        if(intent.hasExtra("item_id"))
        {
                    txt_discount.setText(intent.getStringExtra("discount"));
                    txt_h_item.setText(intent.getStringExtra("name"));
                    txt_hsn.setText(intent.getStringExtra("hsn"));
                    txt_itemPrice.setText(stockInfo.getO_price());
                     txt_mrp.setText(intent.getStringExtra("o_mrp"));
                     txt_h_cname.setText(intent.getStringExtra("company"));
                     txt_specification.setText(intent.getStringExtra("specification"));

                     txt_unitPrice.setText(intent.getStringExtra("unitPrice"));
                     txt_stockStorage.setText(intent.getStringExtra("storage_qty"));
                      txt_pid.setText(intent.getStringExtra("product_id"));
                       txt_Type.setText(intent.getStringExtra("product_type"));
                      txt_totalPrice.setText(intent.getStringExtra("totalPrice"));
                      tx_discountPer.setText(intent.getStringExtra("discountPer"));

                      txnewDiscount.setText(stockInfo.getDiscount());
                      txt_newGst.setText(stockInfo.getGst());
                     txt_gst.setText(stockInfo.getO_gst());
                      txnewunitPrice.setText(stockInfo.getUnitPrice());
                      txt_newstockQty.setText(stockInfo.getStock_qty());

                       txt_itemBarcode.setText(stockInfo.getItembarcode());
                          txt_mUnit.setText(stockInfo.getMunit());
                         txt_unit.setText(stockInfo.getUnit());






        }


    }
}

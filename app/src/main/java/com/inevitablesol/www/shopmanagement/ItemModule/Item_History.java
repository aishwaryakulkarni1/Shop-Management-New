package com.inevitablesol.www.shopmanagement.ItemModule;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

public class Item_History extends AppCompatActivity
{
    private TextView txt_productTye,txt_productName,txt_productHSN,txt_productSpecification,txt_productCompany;

    private TextView txt_p_stock,txt_p_orPrice,txt_mrp,txt_gst;

    private TextView txt_newUnitpirce,txt_stockPrice,txt_newgst,txt_TotalPrice,txt_discount;

    private TextInputEditText et_dis_price,et_gst,et_totalPrice;

    private static final String TAG = "Item_History";

    private  TextView txt_itemBarcode;
    private  TextView txt_mUnit;
    private  TextView txt_unit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_billing_item_history);
        txt_productTye=(TextView)findViewById(R.id.add_new_productType);
        txt_productCompany=(TextView)findViewById(R.id.add_new_productCompany);
        txt_productHSN=(TextView)findViewById(R.id.add_new_productHSN);
        txt_productName=(TextView)findViewById(R.id.add_new_productName);
        txt_productSpecification=(TextView)findViewById(R.id.add_new_productSpecification);
        txt_p_stock=(TextView)findViewById(R.id.p_storageQty);
        txt_p_orPrice=(TextView)findViewById(R.id.p_originalprice);
        txt_mrp=(TextView)findViewById(R.id.p_mrp);
        txt_gst=(TextView)findViewById(R.id.p_gst);
        et_dis_price=(TextInputEditText)findViewById(R.id.et_discountPrice);
        et_gst=(TextInputEditText)findViewById(R.id.et_gst);
        et_totalPrice=(TextInputEditText)findViewById(R.id.et_totalDiscountedPrice);

        txt_mUnit=(TextView)findViewById(R.id.txt_munit);
        txt_unit=(TextView)findViewById(R.id.txt_unit);
        txt_itemBarcode=(TextView)findViewById(R.id.txt_itembarcode);

         // Stock Info

          txt_newUnitpirce=(TextView)findViewById(R.id.add_newMrp);
         txt_newgst=(TextView)findViewById(R.id.add_new_gst);
         txt_stockPrice=(TextView)findViewById(R.id.add_newStockQty);
        txt_discount=(TextView)findViewById(R.id.add_newdiscount) ;
        always_Hide();


          Intent intent =getIntent();

          if(intent.hasExtra("stock_info"))
          {
              StockInfo stockInfo=(StockInfo)getIntent().getSerializableExtra("stock_info");
              Log.d(TAG, "onCreate: "+stockInfo);
                if(stockInfo!=null)
                {
                    txt_productName.setText(stockInfo.getItem_name());
                    txt_productTye.setText(stockInfo.getProduct_type());
                    txt_productHSN.setText(stockInfo.getHsn_ssc_code());
                    txt_productCompany.setText(stockInfo.getCompany());
                    txt_productSpecification.setText(stockInfo.getSpecification());
                    txt_gst.setText(stockInfo.getO_gst());
                    txt_p_orPrice.setText(stockInfo.getO_price());
                    txt_mrp.setText(stockInfo.getO_mrp());
                    txt_p_stock.setText(stockInfo.getStorage_qty());
                    txt_discount.setText(stockInfo.getDiscount());
                    txt_newgst.setText(stockInfo.getGst());
                    txt_newUnitpirce.setText(stockInfo.getUnitPrice());
                    txt_stockPrice.setText(stockInfo.getStock_qty());
                    et_gst.setText(stockInfo.getGst());
                    et_totalPrice.setText(stockInfo.getTotalPrice());
                    et_dis_price.setText(stockInfo.getDisPrice());

                    txt_itemBarcode.setText(stockInfo.getItembarcode());
                    txt_mUnit.setText(stockInfo.getMunit());
                    txt_unit.setText(stockInfo.getUnit());
                }

          }

    }


    private  void always_Hide()
    {
        Log.d(TAG, "hidedata() called");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}

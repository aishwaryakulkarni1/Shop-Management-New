package com.inevitablesol.www.shopmanagement.product_info;

import android.util.Log;

import com.inevitablesol.www.shopmanagement.ItemModule.StockInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pritam on 01-12-2017.
 */

public class ItemParser_for_View
{

    public static int [] item_id;
    public static String[] item_name;
    public static String[] company;
    public static String[] owner;
    public static String[] specification;
   // public static String[] product_type;
    public static String[] product_id;
    public static String[] hsn_ssc_code;
    public static String[] storage_qty;
    public static String[] stock_qty;
    public static String[] original_price;
    public static  String [] mrp;
    public  static String [] unit_Price;
    public  static  String[] total_price;
    public  static  String [] discount;
    public  static  String [] gst;
    public  static  String[] disPrice;
    public  static  String [] disPer;
    public  static  String[] o_price;
    public  static  String [] o_gst;
    public  static  String [] o_mrp;


    public static final String JSON_ARRAY ="records";
    public static final String KEY_ITEMID= "item_id";
    public static final String KEY_ITEMNAME="item_name";
    public static final String KEY_COMPANY="company";
    public static final String KEY_OWNER="owner";
    public static final String KEY_SPECIFICATION="specification";
    public static final String KEY_PRODUCTTYPE="product_type";
    public static final String KEY_PRODUCTID="product_id";
    public static final String KEY_HSN="hsn_ssc_code";
    public static final String KEY_STORAGE_QTY="storage_qty";
    public static final String KEY_STOCKQTY="stock_qty";
    public static final String KEY_ORIGINALPRICE ="purchase_price";
    public static final String KEY_MRP="mrp";
    private  static  final  String KEY_UNITPRICE="unit_price";
    private  static  final  String KEY_TOTALPRICE="total_price";
    private static  final  String KEY_DISCOUNT="discount";
    private  static  final  String KEY_GST="gst";
    private  static  final  String KEY_DISCOUNTEDPRICE="discPrice";
    private  static  final  String KEY_DPER="discPercent";

    private  static  final  String KEY_O_price="p_orgPrice";
    private  static  final  String kEY_O_GST="p_gst";
    private  static  final  String KEY_O_MRP="p_mrp";


    private JSONArray titles_list = null;

    private String json;

    public ItemParser_for_View(String json)
    {
        this.json = json;
    }

    public void parseJSON()
    {
        JSONObject jsonObject=null;
        try
        {
            jsonObject = new JSONObject(json);
            titles_list = jsonObject.getJSONArray(JSON_ARRAY);

            item_id = new int[titles_list.length()];
            item_name = new String[titles_list.length()];
            company = new String[titles_list.length()];
            owner = new String[titles_list.length()];
            specification= new String[titles_list.length()];
          //  product_type= new String[titles_list.length()];
            product_id= new String[titles_list.length()];
            hsn_ssc_code= new String[titles_list.length()];
            storage_qty=new String[titles_list.length()];
            stock_qty = new String[titles_list.length()];
            original_price= new String[titles_list.length()];
            mrp= new String[titles_list.length()];
            unit_Price=new String[titles_list.length()];
            total_price=new String[titles_list.length()];
            discount=new String[titles_list.length()];
            gst=new String[titles_list.length()];
            disPrice=new String[titles_list.length()];
            disPer=new String[titles_list.length()];
            o_gst=new String[titles_list.length()];
            o_mrp=new String[titles_list.length()];
            o_price=new String[titles_list.length()];




            for (int i = 0; i< titles_list.length(); i++)
            {
                JSONObject jo = titles_list.getJSONObject(i);
                item_id[i] = jo.getInt(KEY_ITEMID);
                item_name[i] = jo.getString(KEY_ITEMNAME);
                company[i]=jo.getString(KEY_COMPANY);
                owner[i]=jo.getString(KEY_OWNER);
                specification[i]=jo.getString(KEY_SPECIFICATION);
          //     product_type[i]=jo.getString(KEY_PRODUCTTYPE);
                product_id[i]=jo.getString(KEY_PRODUCTID);
                hsn_ssc_code[i]=jo.getString(KEY_HSN);
                stock_qty[i]=jo.getString(KEY_STOCKQTY);
                storage_qty[i]=jo.getString(KEY_STORAGE_QTY);
                original_price[i]=jo.getString(KEY_ORIGINALPRICE);
                mrp[i]=jo.getString(KEY_MRP);
                unit_Price[i]=jo.getString(KEY_UNITPRICE);
                total_price[i]=jo.getString(KEY_TOTALPRICE);
                discount[i]=jo.getString(KEY_DISCOUNT);
                gst[i]=jo.getString(KEY_GST);
                disPrice[i]=jo.getString(KEY_DISCOUNTEDPRICE);
                disPer[i]=jo.getString(KEY_DPER);
                o_price[i]=jo.getString(KEY_O_price);
                o_gst[i]=jo.getString(kEY_O_GST);
                o_mrp[i]=jo.getString(KEY_O_MRP);



            }



        }catch (JSONException e)
        {
            e.printStackTrace();
        }



    }

    public ArrayList<StockInfo> prepareStock() {
        ArrayList<StockInfo> stock_ver = new ArrayList<>();
        if (item_name.length == 0)
        {
            Log.d("NO STOCK INFO AVAILABLE", "NO STOCK INFO AVAILABLE");
        } else {
            for (int i = 0; i < item_name.length; i++) {
                StockInfo stockInfo = new StockInfo();
                stockInfo.setItem_id(item_id[i]);
                stockInfo.setItem_name(item_name[i]);
              //  stockInfo.setProduct_type(product_type[i]);
                stockInfo.setCompany(company[i]);
                stockInfo.setOwner(owner[i]);
                stockInfo.setStock_qty(stock_qty[i]);
                stockInfo.setStorage_qty(storage_qty[i]);
                stockInfo.setMrp(mrp[i]);
                stockInfo.setOriginal_price(original_price[i]);
                stockInfo.setSpecification(specification[i]);
                stockInfo.setHsn_ssc_code(hsn_ssc_code[i]);
                stockInfo.setProduct_id(product_id[i]);
                stockInfo.setDiscount(discount[i]);
                stockInfo.setTotalPrice(total_price[i]);
                stockInfo.setUnitPrice(unit_Price[i]);
                stockInfo.setGst(gst[i]);
                stockInfo.setDisPrice(disPrice[i]);
                stockInfo.setDisCountPer(disPer[i]);
                stockInfo.setO_price(o_price[i]);
                stockInfo.setO_mrp(o_mrp[i]);
                stockInfo.setO_gst(o_gst[i]);

//                stockInfo.setSelectedQuantity("0");

                stock_ver.add(stockInfo);
            }
        }


        return stock_ver;
    }
}

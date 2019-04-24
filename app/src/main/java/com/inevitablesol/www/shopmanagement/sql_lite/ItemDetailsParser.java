package com.inevitablesol.www.shopmanagement.sql_lite;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pritam on 17-05-2017.
 */
public class ItemDetailsParser
{
    public static String[] item_id;
    public static String[] item_name;
    public static  String [] product_id;
    public static String[] modified_date;
    public static String[] created_date;
    public static String[] item_mrp;
    public static String[]  stock_qt;
    public static String[]  purchase_price;
    public static String[]  specifications;
    public static String[]  created_by;
    public static String[]  modified_by;
    public  static String[] gst;
    public  static  String[] total_price;
    public  static  String [] discount;
    public  static  String [] unit_price;
    public  static  String [] hsn;
    public  static  String [] status;

    public  static  String [] mUnit;
    public  static  String [] unit;
    public  static  String [] item_barcode;

    public  static  String[] shortcut;

    private  static final String JSON_ARRAY ="records";
    private  static final String KEY_ITEMID= "item_id";
    private  static final String KEY_ITEMNAME="item_name";
    private  static final String KEY_PRODUCTID="product_id";
     private  static final String KEY_HSNCODE="hsn_ssc_code";
    private  static final String KEY_SPEC="specification";
     private  static final String KEY_STOCKQTY="stock_qty";
     private  static final String KEY_MRP="mrp";
     private  static  final String KEY_GST="gst";
    private    static final String KEY_PURCHASEPRICE="purchase_price";
    private  static  final  String KEY_UNIT_PRICE="unit_price";
    private  static  final String KEY_TOTAL_PRICE="total_price";
    private  static  final String KEY_DISCOUNT="discount";
    private  static final String KEY_CREATOR="created_by";
    private  static final String KEY_MODIFIER ="modified_by";
    private  static  final String KEY_CREATED_DATED="created_date";
    private  static  final  String KEY_Modi_DATE="modified_date";
    private  static  final String KEY_STATUS="status";

    private  static  final  String KEY_SHORTCUT="shortCode";

    private  static  final  String KEY_MEASURMENTUNIT="measurement_unit";
    private  static  final  String KEY_UNIT="unit";
    private  static  final  String ITEM_BARCODE="item_barcode";






    private JSONArray jsonArray = null;

   private   String json;
      public ItemDetailsParser(String json)
     {
         this.json=json;
     }

  public  void  parseItemList()
  {
      try
      {
          try {
              JSONObject jsonObject=new JSONObject(json);
              jsonArray=jsonObject.getJSONArray(JSON_ARRAY);
              item_id=new String[jsonArray.length()];
              item_name=new String[jsonArray.length()];
              product_id=new String[jsonArray.length()];
              modified_by=new String[jsonArray.length()];
              created_by=new String[jsonArray.length()];
              specifications=new String[jsonArray.length()];
              item_mrp=new  String[jsonArray.length()];
              stock_qt=new String[jsonArray.length()];
              modified_date=new String[jsonArray.length()];
              created_date=new  String[jsonArray.length()];
              purchase_price=new   String[jsonArray.length()];
              gst=new String[jsonArray.length()];
              total_price=new String[jsonArray.length()];
              discount=new String[jsonArray.length()];
              unit_price=new String[jsonArray.length()];
              hsn=new String[jsonArray.length()];
              status=new String[jsonArray.length()];

              item_barcode=new String[jsonArray.length()];
              mUnit=new String[jsonArray.length()];
              unit=new String[jsonArray.length()];
              shortcut=new String[jsonArray.length()];

          } catch (JSONException e)
          {
              e.printStackTrace();
          }catch (Exception e)
          {

          }

               if(jsonArray.length()>0)
               {


                   for (int i = 0; i < jsonArray.length(); i++) {
                       JSONObject jo = jsonArray.getJSONObject(i);
                       item_id[i] = jo.getString(KEY_ITEMID);
                       item_name[i] = jo.getString(KEY_ITEMNAME);
                       product_id[i] = jo.getString(KEY_PRODUCTID);
                       modified_by[i] = jo.getString(KEY_MODIFIER);
                       created_by[i] = jo.getString(KEY_CREATOR);
                       specifications[i] = jo.getString(KEY_SPEC);
                       item_mrp[i] = jo.getString(KEY_MRP);
                       stock_qt[i] = jo.getString(KEY_STOCKQTY);
                       purchase_price[i] = jo.getString(KEY_PURCHASEPRICE);
                       created_date[i] = jo.getString(KEY_CREATED_DATED);
                       modified_date[i] = jo.getString(KEY_Modi_DATE);
                       gst[i] = jo.getString(KEY_GST);
                       total_price[i] = jo.getString(KEY_TOTAL_PRICE);
                       discount[i] = jo.getString(KEY_DISCOUNT);
                       unit_price[i] = jo.getString(KEY_UNIT_PRICE);
                       hsn[i] = jo.getString(KEY_HSNCODE);
                       status[i] = jo.getString(KEY_STATUS);

                       mUnit[i]=jo.getString(KEY_MEASURMENTUNIT);
                       unit[i]=jo.getString(KEY_UNIT);
                       item_barcode[i]=jo.getString(ITEM_BARCODE);
                       shortcut[i]=jo.getString(KEY_SHORTCUT);
                   }
               }else

               {
                   //Toast.makeText(this, "Please Add Some Item", Toast.LENGTH_SHORT).show();
               }



      } catch (JSONException e)
      {
          e.printStackTrace();
      }


  }





}

package com.inevitablesol.www.shopmanagement.MenuItemModule.Parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pritam on 10-05-2017.
 */

public class ProductItemParser
{

    public  static  String[] itemId;
    public  static  String [] itemName;
    public static final String JSON_ARRAY ="records";
    public static final String KEY_ITEMID= "item_id";
    public static final String KEY_ITEMNAME="product_type";
    private   String jsonString;
    private JSONArray product=null;

    public ProductItemParser(String jsoString)
    {
        this.jsonString=jsoString;
    }

    public   void productItemParser()
    {
        JSONObject jsonObject;
        try {
            jsonObject=new JSONObject(jsonString);
            product=      jsonObject.getJSONArray(JSON_ARRAY);
            itemId=new String[product.length()];
            itemName=new String[product.length()];




            for (int i=0;i<product.length();i++)
            {
                JSONObject jsonObject1=      product.getJSONObject(i);
                itemId[i]=jsonObject1.getString("item_id");
                itemName[i]=jsonObject1.getString("item_name");


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

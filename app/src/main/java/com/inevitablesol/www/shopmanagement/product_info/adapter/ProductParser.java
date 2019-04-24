package com.inevitablesol.www.shopmanagement.product_info.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pritam on 05-05-2017.
 */

public class ProductParser
{
    public  static  String[] productId;
    public  static  String [] productName;
    public static final String JSON_ARRAY ="records";
    public static final String KEY_PRODUCTID= "product_id";
    public static final String KEY_ITEMNAME="product_type";
    private   String jsonString;
     private  JSONArray  product=null;

     public ProductParser(String jsoString)
    {
        this.jsonString=jsoString;
    }

    public   void productParser()
    {
        JSONObject jsonObject;
        try {

        jsonObject=new JSONObject(jsonString);
        product=      jsonObject.getJSONArray(JSON_ARRAY);
             productId=new String[product.length()];
              productName=new String[product.length()];




                      for (int i=0;i<product.length();i++)
             {
                 JSONObject jsonObject1=      product.getJSONObject(i);

                        productName[i] = jsonObject1.getString("product_type");
                        productId[i] = jsonObject1.getString("product_id");









            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

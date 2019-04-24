package com.inevitablesol.www.shopmanagement.billing_module;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pritam on 17-08-2017.
 */

public class BillingProductParser
{
    public  static  String[] productId;
    public  static  String [] productName;
    public static final String JSON_ARRAY ="records";
    public static final String KEY_PRODUCTID= "product_id";
    public static final String KEY_ITEMNAME="product_type";
    private   String jsonString;
    private JSONArray product=null;

    public BillingProductParser(String jsoString)
    {
        this.jsonString=jsoString;
    }

    public   void billingproductParser()
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

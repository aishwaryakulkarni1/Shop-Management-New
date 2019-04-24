package com.inevitablesol.www.shopmanagement.product_info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pritam on 08-01-2018.
 */

public class Spinner_prodectParser
{
    public  static  String[] productId;
    public  static  String [] productName;
    public static final String JSON_ARRAY ="records";
    public static final String KEY_PRODUCTID= "product_id";
    public static final String KEY_ITEMNAME="product_type";
    private   String jsonString;
    private JSONArray product=null;
    private  int j=0;

    public Spinner_prodectParser(String jsoString)
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

                if(i==0)
                {
                    productName[j] = "Select Prodcut Type";
                    productId[j] ="0";
                    j++;
                }else
                {
                    JSONObject jsonObject1=      product.getJSONObject(i);
                    productName[j] = jsonObject1.getString("product_type");
                    productId[j] = jsonObject1.getString("product_id");
                    j++;

                }










            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

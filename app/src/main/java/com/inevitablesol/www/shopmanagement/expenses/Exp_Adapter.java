package com.inevitablesol.www.shopmanagement.expenses;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pritam on 13-09-2017.
 */

public class Exp_Adapter
{
    public  static  String[] productId;
    public  static  String [] productName;
    public static final String JSON_ARRAY ="records";
    public static final String KEY_PRODUCTID= "id";
    public static final String KEY_ITEMNAME="type";
    private   String jsonString;
    private JSONArray product=null;

    private static final String TAG = "Exp_Adapter";

    public Exp_Adapter(String jsoString)
    {
        this.jsonString=jsoString;
    }

    public   void productParser()
    {
        JSONObject jsonObject;
        try {

            jsonObject=new JSONObject(jsonString);
            product=      jsonObject.getJSONArray(JSON_ARRAY);
            productId=new String[product.length()+1];
            productName=new String[product.length()+1];

             int len=product.length();
            Log.d(TAG, "productParser:"+len);
            productName[0] ="Select Expense Type";
            productId[0] = "00";

            for (int i=1;i<=len;i++)
            {
                int k=i-1;
                JSONObject jsonObject1=      product.getJSONObject(k);


                    productName[i] = jsonObject1.getString("type");
                    productId[i] = jsonObject1.getString("id");


            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
}

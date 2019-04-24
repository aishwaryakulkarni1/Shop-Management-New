package com.inevitablesol.www.shopmanagement.billing_module.Parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pritam on 14-08-2017.
 */

public class S_parser
{


    public  static  String[] Id;
    public  static  String [] stateName;
    public  static  String []  stateCode;
    public static final String JSON_ARRAY ="records";
    public static final String KEY_ID= "id";
    public static final String KEY_STATE="state";
    public  static final String KEY_CODE="state_code";
    private   String jsonString;
    private JSONArray product=null;

    public S_parser(String jsoString)
    {
        this.jsonString=jsoString;
    }

    public   void productParser()
    {
        JSONObject jsonObject;
        try {
            jsonObject=new JSONObject(jsonString);
            product=      jsonObject.getJSONArray(JSON_ARRAY);
            Id=new String[product.length()];
            stateName=new String[product.length()];
            stateCode=new String[product.length()];




            for (int i=0;i<product.length();i++)
            {
                JSONObject jsonObject1  =      product.getJSONObject(i);


                    Id[i]=jsonObject1.getString("id");
                    stateName[i]=jsonObject1.getString("state");
                    stateCode[i]=jsonObject1.getString("state_code");



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}

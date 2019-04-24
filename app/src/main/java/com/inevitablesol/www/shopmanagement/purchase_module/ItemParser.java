package com.inevitablesol.www.shopmanagement.purchase_module;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pritam on 07-07-2017.
 */

public class ItemParser
{
    public  static  String[] itemId;
    public  static  String [] itemName;
    public static final String JSON_ARRAY ="records";
    public static final String KEY_ITEMID= "item_id";
    public static final String KEY_ITEMNAME="item_name";
    private   String jsonString;
    private JSONArray product=null;

    List<Map<String, String>> items = new ArrayList<Map<String, String>>();
    private HashMap<String, String> mapData;

    public ItemParser(String jsoString)
    {
        this.jsonString=jsoString;
    }

    public   void productParser()
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
                itemName[i]=jsonObject1.getString(KEY_ITEMNAME);
                itemId[i]=jsonObject1.getString(KEY_ITEMID);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public List<Map<String, String>> getItemArray()
    {
        for (int i = 0; i < itemId.length; i++) {

            mapData = new HashMap<String, String>();
            mapData.put("itemname", itemName[i]);
            mapData.put("itemid", itemId[i]);
            items.add(mapData);
        }
        return items;

    }




}

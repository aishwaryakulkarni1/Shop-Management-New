package com.inevitablesol.www.shopmanagement.customer_module;



import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pritam on 25-05-2017.
 */

public class CustomerParser
{
    public static String[] cust_id;
    public static String[] cust_Name;
    public static String[] mobile;
    public static String[] email;
    public  static String[] home_delivery;
    public  static String [] address;
    public  static  String [] gstin;
    public  static   String[] placeofSupply;
    public  static  String[] state;
    public  static  String[] gstStatus;

    public static final String JSON_ARRAY = "cust_records";
    public static final String KEY_CUSTID = "customer_id";
    public static final String KEY_CUSTNAME = "customer_name";
    public static final String KEY_MOBILE = "mobile_number";
    public  static final String KEY_HOME_DELEIVERY="home_delivery";
    public static final String KEY_ADDRESS="address";

    public static final String KEY_EMAIL="email_id";
    public static  final  String KEY_GST="gstin";
    public  static  final  String KEY_place="place_of_supply";

    public  static  final  String KEY_SATE="state";
    public  static  final  String GSTSTATUS="gstStatus";
    private static final String TAG = "CustomerParser";


    private JSONArray cust_list = null;

    private String json;

    public CustomerParser(String json) {

        this.json = json;
    }

    public void custDetails()
    {
        try {
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(json);
            cust_list = jsonObject.getJSONArray(JSON_ARRAY);
            cust_id = new String[cust_list.length()];
            cust_Name = new String[cust_list.length()];
            mobile = new String[cust_list.length()];
            email=new String[cust_list.length()];
             home_delivery=new String[cust_list.length()];
             address=new String[cust_list.length()];
              gstin=new String[cust_list.length()];
            placeofSupply=new String[cust_list.length()];
            state=new String[cust_list.length()];
            gstStatus=new String[cust_list.length()];


            for (int i = 0; i < cust_list.length(); i++)
            {
                try {
                    JSONObject jsonObject1 = cust_list.getJSONObject(i);
                    cust_id[i] = jsonObject1.getString(KEY_CUSTID);
                    cust_Name[i] = jsonObject1.getString(KEY_CUSTNAME);
                    mobile[i] = jsonObject1.getString(KEY_MOBILE);
                    email[i]=jsonObject1.getString(KEY_EMAIL);
                    home_delivery[i]=jsonObject1.getString(KEY_HOME_DELEIVERY);
                    address[i]=jsonObject1.getString(KEY_ADDRESS);
                    gstin[i]=jsonObject1.getString(KEY_GST);
                    placeofSupply[i]=jsonObject1.getString(KEY_place);
                    state[i]=jsonObject1.getString(KEY_SATE);
                    gstStatus[i]=jsonObject1.getString(GSTSTATUS);
                } catch (JSONException e)
                {
                    Log.d(TAG, "custDetails: "+e);
                }

            }


        } catch (JSONException e) {

        }

    }
    public ArrayList<CustomerInfo> makeArray()
    {
        ArrayList arrayList=new ArrayList<>();

        if(cust_id.length>0)
        {

            for (int i=0;i<cust_id.length;i++)
            {
                CustomerInfo customerInfo=new CustomerInfo();
                 customerInfo.setCustomer_id(cust_id[i]);
                customerInfo.setCustomer_name(cust_Name[i]);
                 customerInfo.setAddress(address[i]);
                 customerInfo.setEmail_id(email[i]);
                  customerInfo.setHome_delivery(home_delivery[i]);
                 customerInfo.setMobile_numbe(mobile[i]);
                 customerInfo.setGstin(gstin[i]);
                customerInfo.setPlaceofsupply(placeofSupply[i]);
                customerInfo.setState(state[i]);
                customerInfo.setGstStatus(gstStatus[i]);
                 arrayList.add(customerInfo);


            }

            return  arrayList;
        }

        return  arrayList;

    }


}

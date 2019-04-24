package com.inevitablesol.www.shopmanagement.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pritam on 13-06-2017.
 */

public class Services_view_parser
{
    public  static String[] Service_id;
    public  static  String[] service_name;
    public  static  String [] duration;
    public  static  String [] amount;
    public  static  String [] due;
    public static final String JSON_ARRAY = "records";
    private static final String KEY_SERVICEID = "service_id";
    private  static  final String KEY_NAME="name";
    private  static  final String KEY_DURATION="duration";
    private  static  final String KEY_Amount="amount";
    private  static  final String KEY_DUE="due";


    private JSONArray cust_list = null;
    private String json;

    public Services_view_parser(String json)
    {

        this.json = json;
    }

     public  void serviceDetails()
     {
         try {
             JSONObject jsonObject = null;
             jsonObject = new JSONObject(json);
             cust_list = jsonObject.getJSONArray(JSON_ARRAY);
             Service_id=new String[cust_list.length()];
              service_name=new String[cust_list.length()];
              duration=new String[cust_list.length()];
             amount=new String[cust_list.length()];
             due=new String[cust_list.length()];

             for (int i = 0; i < cust_list.length(); i++)
             {
                 JSONObject jsonObject1 = cust_list.getJSONObject(i);

                 Service_id[i] = jsonObject1.getString(KEY_SERVICEID);
                 service_name[i]=jsonObject1.getString(KEY_NAME);
                 amount[i]=jsonObject1.getString(KEY_Amount);
                 due[i]=jsonObject1.getString(KEY_DUE);
                 duration[i]=jsonObject1.getString(KEY_DURATION);
             }

         }catch (JSONException e)
         {

         }
     }

    public ArrayList<Services> getService()
    {
        ArrayList services=new ArrayList<>();
        if(Service_id.length>0)
        {
            for (int i=0;i<Service_id.length;i++)
            {
                Services services1=new Services();
                      services1.setService_id(Service_id[i]);
                      services1.setService_amount(amount[i]);
                      services1.setService_due(due[i]);
                      services1.setService_duration(duration[i]);
                      services1.setService_name(service_name[i]);
                      services.add(services1);




            }
            return services;

        }
        return  services;
    }

}

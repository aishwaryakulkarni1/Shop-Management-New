package com.inevitablesol.www.shopmanagement.sales;

import android.util.Log;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pritam on 01-06-2017.
 */

public class UserParser
{
    public static String[] user_id;
    public static String[] user_name;
    public static String[] user_mobile;
 //   public static String[] user_email;
   //public static String[] user_role;



    public static final String JSON_ARRAY ="records";
    public static final String KEY_USERID= "u_id";
    public static final String KEY_USERNAME="u_name";
    public static final String KEY_USERMOBILE="u_number";

    public static final String KEY_USEREMAIL="u_email";

    public static final String USERROLE="u_role";


    private JSONArray  arrayList = null;

    private String json;

    public UserParser(String json)
    {
        this.json = json;
    }

    public void parseJSON()
    {
        JSONObject jsonObject=null;
        try
        {
            jsonObject = new JSONObject(json);
            arrayList = jsonObject.getJSONArray(JSON_ARRAY);

            user_id = new String[arrayList.length()];
            user_name = new String[arrayList.length()];
           // user_email = new String[arrayList.length()];
            user_mobile = new String[arrayList.length()];
           // user_role = new String[arrayList.length()];




            for (int i = 0; i< arrayList.length(); i++)
            {
                JSONObject jo = arrayList.getJSONObject(i);
                user_id[i] = jo.getString(KEY_USERID);
                user_name[i] = jo.getString(KEY_USERNAME);
                user_mobile[i]=jo.getString(KEY_USERMOBILE);
                //user_role[i]=jo.getString(USERROLE);
                // user_email[i]=jo.getString(KEY_USEREMAIL);
            }



        }catch (JSONException e)
        {
            e.printStackTrace();
        }



    }

    public ArrayList<UserRole> getUserInformations()
    {
        ArrayList usersDetails = new ArrayList<>();
        if (user_id.length == 0)
        {
            Log.d("NO STOCK INFO AVAILABLE", "NO STOCK INFO AVAILABLE");

        } else {
            for (int i = 0; i < user_id.length; i++) {
                UserRole userInfo = new UserRole();
                userInfo.setUser_id(user_id[i]);
                userInfo.setUser_name(user_name[i]);
                userInfo.setUser_mobile(user_mobile[i]);
                //userInfo.setUser_email(user_email[i]);
               // userInfo.setUser_role(user_role[i]);
                usersDetails.add(userInfo);
            }
        }


        return usersDetails;
    }
}

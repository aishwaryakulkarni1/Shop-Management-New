package com.inevitablesol.www.shopmanagement.billing_module.Parser;

import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pritam on 14-08-2017.
 */

public class Quotation_history_parser
{

    public static String[] quotation_id;
    public static String[] amount;
    private  static  String [] taxableValue;
    private  static String [] email;
    private  static  String [] custName;
    private  static  String [] custMobile;
    private  static  String [] totalGst;
    private  static  String [] createdDate;



    public static final String JSON_ARRAY = "records";
    public static final String KEY_INV_ID = "quotation_id";
    public static final String KEY_AMT_PAID = "total";
    private  static  final  String CUSTNAME="cust_name";
    private  static  final  String CUSTMOBILE="cust_number";
    private  static  final  String EMAIL="cust_email_id";
    private  static  final  String  TAXAVALUE="taxable_value";
    private  static  final  String  GST="total_gst";
    private  static  final  String Date="created_date";

    private String jsonString;
    private JSONArray invoice = null;

    public Quotation_history_parser(String jsoString) {
        this.jsonString = jsoString;
    }

    public void billingHistoryParser() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            invoice = jsonObject.getJSONArray(JSON_ARRAY);
            quotation_id = new String[invoice.length()];
            amount = new String[invoice.length()];
            taxableValue= new String[invoice.length()];
             totalGst= new String[invoice.length()];
             email= new String[invoice.length()];
              custMobile= new String[invoice.length()];
               custName= new String[invoice.length()];
            createdDate= new String[invoice.length()];



            for (int i = 0; i < invoice.length(); i++) {
                JSONObject jsonObject1 = invoice.getJSONObject(i);
                quotation_id[i] = jsonObject1.getString(KEY_INV_ID);
                amount[i] = jsonObject1.getString(KEY_AMT_PAID);
                 email[i]=jsonObject1.getString(EMAIL);
                 taxableValue[i]=jsonObject1.getString(TAXAVALUE);
                 totalGst[i]=jsonObject1.getString(GST);
                 custName[i]=jsonObject1.getString(CUSTNAME);
                 custMobile[i]=jsonObject1.getString(CUSTMOBILE);
                createdDate[i]=jsonObject1.getString(Date);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Quotation_pojo> makeArray()
    {
        ArrayList arrayList=new ArrayList<>();

        if(quotation_id.length>0)
        {

            for (int i=0;i<quotation_id.length;i++)
            {
                Quotation_pojo HistoryInfo=new Quotation_pojo();
                HistoryInfo.setQ_id(quotation_id[i]);
                HistoryInfo.setAmount(amount[i]);
                 HistoryInfo.setEmail(email[i]);
                 HistoryInfo.setTaxableValue(taxableValue[i]);
                 HistoryInfo .setCustMobile(custMobile[i]);
                 HistoryInfo.setCustName(custName[i]);
                 HistoryInfo.setTotalGst(totalGst[i]);
                 HistoryInfo.setCreatedDate(createdDate[i]);



                arrayList.add(HistoryInfo);

            }

            return  arrayList;
        }

        return  arrayList;

    }
}

package com.inevitablesol.www.shopmanagement.sales;

import android.util.Log;


import com.inevitablesol.www.shopmanagement.expenses.ExpenseAdapetrListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pritam on 30-05-2017.
 */

public class SaleParser
{
         public static String[] in_id;
        public static String[] amount_paid;
        public static String[] total_amount;
        public static String[] mode_of_payment;
        public static String[] created_date;
        public static String[] balanceDue;

       public  static  String []  taxable_value;
        public  static  String [] total_Gst;
        public  static  String [] status;



        public static final String JSON_ARRAY ="records";
        public static final String KEY_ITEMID= "invoice_id";
        public static final String KEY_MODEOFPAYMENT="mode_of_payment";

        public static final String KEY_AMOUNTPAID="amount_paid";

        public static final String KEY_CREATED_DATE="created_date";
        public static final String KEY_BALANCEDUE ="balance_due";
     public  static  final  String KEYTOTAL="total";


        private JSONArray titles_list = null;

        private String json;
    private double amount=0.0;
    private double balance;

    public SaleParser(String json)
        {
            this.json = json;
        }

        public void parseJSON()
        {
            JSONObject jsonObject=null;
            try
            {
                jsonObject = new JSONObject(json);
                titles_list = jsonObject.getJSONArray(JSON_ARRAY);

                in_id = new String[titles_list.length()];
                amount_paid = new String[titles_list.length()];
                total_amount = new String[titles_list.length()];
                 mode_of_payment = new String[titles_list.length()];
                balanceDue= new String[titles_list.length()];
                created_date=new String[titles_list.length()];
                 taxable_value=new String [titles_list.length()];
                 total_Gst=new String[titles_list.length()];
                 status =new String[titles_list.length()];



                for (int i = 0; i< titles_list.length(); i++)
                {
                    JSONObject jo = titles_list.getJSONObject(i);
                    in_id[i] = jo.getString(KEY_ITEMID);
                    amount_paid[i]=jo.getString(KEY_AMOUNTPAID);
                    mode_of_payment[i]=jo.getString(KEY_MODEOFPAYMENT);
                    balanceDue[i]=jo.getString(KEY_BALANCEDUE);
                    total_amount[i]=jo.getString(KEYTOTAL);
                    created_date[i]=jo.getString(KEY_CREATED_DATE);
                    taxable_value[i]=jo.getString("taxable_value");
                     total_Gst[i]=jo.getString("total_gst");
                     status[i]=jo.getString("status");
                }



            }catch (JSONException e)
            {
                e.printStackTrace();
            }



        }

        public ArrayList<SaleInfo> prepareSale()
        {
            ArrayList stock_ver = new ArrayList<>();
            if (in_id.length == 0)
            {
                Log.d("NO STOCK INFO AVAILABLE", "NO STOCK INFO AVAILABLE");
            } else {
                for (int i = 0; i < in_id.length; i++) {
                    SaleInfo stockInfo = new SaleInfo();
                         stockInfo.setCreated_Date(created_date[i]);
                         stockInfo.setInvoice_id(in_id[i]);
                         stockInfo.setModeOfPayment(mode_of_payment[i]);
                         stockInfo.setBalanceDue(balanceDue[i]);
                         stockInfo.setTotalAmount(total_amount[i]);
                         stockInfo.setAmountPaid(amount_paid[i]);

                           //
                             stockInfo.setStatus(status[i]);
                             stockInfo.setTaxable_value(taxable_value[i]);
                             stockInfo.setTotal_gst(total_Gst[i]);


                    stock_ver.add(stockInfo);
                }
            }


            return stock_ver;
        }


        public  double getAmount()
        {
            for (int i = 0; i < in_id.length; i++)
            {
                try {
                      amount +=Double.parseDouble(amount_paid[i]);

                }catch (Exception e)
                {

                }


            }
            return Math.round(amount * 100.0) / 100.0;
        }

    public  double getTotalBalance()
    {
        for (int i = 0; i < in_id.length; i++)
        {
            try {
                balance +=Double.parseDouble(balanceDue[i]);

            }catch (Exception e)
            {

            }


        }
        return Math.round(balance * 100.0) / 100.0;
    }



}



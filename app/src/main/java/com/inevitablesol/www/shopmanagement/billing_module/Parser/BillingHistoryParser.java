package com.inevitablesol.www.shopmanagement.billing_module.Parser;

import com.inevitablesol.www.shopmanagement.billing_module.InvoiceHistoryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Anup on 10-08-2017.
 */

public class BillingHistoryParser {

    public static String[] invoice_id;
    public static String[] amount_paid;
    public static String[] balance_due;
    public static String[] mode_of_payment;
    public static String[] shortcut;
    public  static  String[] total;

    public static final String JSON_ARRAY = "records";
    public static final String KEY_INV_ID = "invoice_id";
    public static final String KEY_AMT_PAID = "amount_paid";
    public static final String KEY_BALANCE_DUE = "balance_due";
    public static final String KEY_MODE = "mode_of_payment";
    public  static  final String KEY_SHORTCUT="shortPmode";
    public  static  final  String KEY_TOATAL="total";
    private String jsonString;
    private JSONArray invoice = null;

    public BillingHistoryParser(String jsoString) {
        this.jsonString = jsoString;
    }

    public void billingHistoryParser() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            invoice = jsonObject.getJSONArray(JSON_ARRAY);
            invoice_id = new String[invoice.length()];
            amount_paid = new String[invoice.length()];
            balance_due = new String[invoice.length()];
            mode_of_payment = new String[invoice.length()];
            shortcut=new String[invoice.length()];
            total=new String[invoice.length()];


            for (int i = 0; i < invoice.length(); i++)
            {
                JSONObject jsonObject1 = invoice.getJSONObject(i);
                invoice_id[i] = jsonObject1.getString(KEY_INV_ID);
                amount_paid[i] = jsonObject1.getString(KEY_AMT_PAID);
                balance_due[i] = jsonObject1.getString(KEY_BALANCE_DUE);
                mode_of_payment[i] = jsonObject1.getString(KEY_MODE);
                shortcut[i] = jsonObject1.getString(KEY_SHORTCUT);
                total[i]=jsonObject1.getString(KEY_TOATAL);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public ArrayList<InvoiceHistoryInfo> makeArray()
    {
        ArrayList<InvoiceHistoryInfo> arrayList = new ArrayList<InvoiceHistoryInfo>();
        try
        {


            if (invoice_id.length > 0) {

                for (int i = 0; i < invoice_id.length; i++)
                {
                    InvoiceHistoryInfo invoiceHistoryInfo = new InvoiceHistoryInfo();
                    invoiceHistoryInfo.setInvoice_id(invoice_id[i]);
                    invoiceHistoryInfo.setAmount_paid(String.format("%.2f", Double.parseDouble(amount_paid[i])));
                    invoiceHistoryInfo.setBalance_due(String.format("%.2f", Double.parseDouble(balance_due[i])));
                    invoiceHistoryInfo.setTotal(String.format("%.2f", Double.parseDouble(total[i])));
                    invoiceHistoryInfo.setMode_of_payment(mode_of_payment[i]);
                    invoiceHistoryInfo.setShortCut(shortcut[i]);

                    arrayList.add(invoiceHistoryInfo);

                }

                return arrayList;
            }

            return arrayList;

        } catch (Exception e)
        {

        }
        return arrayList;
    }

}

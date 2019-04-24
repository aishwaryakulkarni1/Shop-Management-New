package com.inevitablesol.www.shopmanagement.sql_lite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.inevitablesol.www.shopmanagement.billing_module.BillingItems;

import java.util.ArrayList;

/**
 * Created by Pritam on 17-05-2017.
 */

public class SqlDataBase extends SQLiteOpenHelper
{


    private static final String DATABASE_NAME ="shop_mgnt" ;
    private static final int DATABASE_VERSION =1;
    private static final String KEY_PRODUCT_ID ="p_id" ;
    private static final String KEY_PRODUCT_NAME = "p_name";
    private String ITEM_TABLE="selected_Item";

    private String KEY_ID="id";
    private String KEY_ITEM_ID="item_id";
    private String KEY_ITEM_NAME="item_name";
    private String KEY_STOCK_QTY="stock_qty";
    private String KEY_ITEM_MRP="item_mrp";
    private String KEY_PURCHASE_PRICE="purchase_price";

    private String KEY_SELECTED_QTY="selected_qty";

    private String  KET_TOTALPRICE="total_price";
    private  String KEY_GST="gst";
    private  String  KEY_DISCOUNT="discount";
    private  String KEY_HSN="hsn";
    private  String  KEY_CAL_GST="cal_gst";
    private String IMAGE_TABLE="image_table";
    private  String KEY_TOTAL_PRICE_CAL="total_price_cal";
    private  String KEY_TAXABLEVALUE="taxableValue";

    private String KEY_IMAGE1="image1";
    private String KEY_IMAGE2="image2";

    private String BHIM_APP="bhim_app";
    private String PAYTM_APP="paytm_app";
    private String KEY_UNIT_PRICE="unit_price";

    private String INVOICE_NUMBER="invoice_number";

    private  String IMAGE_NUMBER="img_number";

    private String KEY_IMAGE="image";
    private long noOfrow;
    private String KEY_STATUS="status";

    private  String KEY_MUNIT="mUnit";
    private  String KEY_UNIT="unit";
    private  String KEY_CHANGEDUNIT="changedUnit";
    private  String KEY_ITEMBARCODE="item_barcode";

    private  String KEY_SHORTCUT="shortcut";



    private String INVOICE_IMAGES="invoice_image";
    private static final String TAG = "SqlDataBase";


    String CREATE_TABLE = "CREATE TABLE " + ITEM_TABLE + "("
            + KEY_ID + " INTEGER  PRIMARY KEY  AUTOINCREMENT,"
            + KEY_PRODUCT_ID + " TEXT,"
            + KEY_ITEM_ID + " TEXT,"
            + KEY_ITEM_NAME + " TEXT,"
            + KEY_STOCK_QTY + " TEXT,"
            + KEY_SELECTED_QTY + " TEXT,"
            + KEY_ITEM_MRP + " TEXT,"
            + KEY_PURCHASE_PRICE + " TEXT,"
            + KEY_UNIT_PRICE+ " TEXT,"
            + KET_TOTALPRICE + " TEXT,"
            + KEY_GST + " TEXT,"
            + KEY_DISCOUNT + " TEXT,"
            + KEY_HSN + " TEXT,"
            + KEY_TOTAL_PRICE_CAL + " TEXT,"
            + KEY_TAXABLEVALUE + " TEXT,"
            + KEY_CAL_GST + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_MUNIT + " TEXT,"
            + KEY_UNIT + " TEXT,"
            + KEY_CHANGEDUNIT + " TEXT,"
            + KEY_SHORTCUT + " TEXT,"
            + KEY_ITEMBARCODE + " TEXT " +")";


    String CREATE_IMAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + PAYTM_APP + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_IMAGE + " TEXT" + ")";


    String CREATE_BHIM_APPSCREEN = "CREATE TABLE IF NOT EXISTS " + BHIM_APP + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_IMAGE + " TEXT" + ")";


    String CREATE_INVOICE_IMAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + INVOICE_IMAGES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + INVOICE_NUMBER + " TEXT,"
            + IMAGE_NUMBER + " TEXT,"
            + KEY_IMAGE1 + " TEXT" + ")";


    public SqlDataBase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_IMAGE_TABLE);
        db.execSQL(CREATE_BHIM_APPSCREEN);
        db.execSQL(CREATE_INVOICE_IMAGE_TABLE);
        Log.d(TAG, "onCreate:DB");




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PAYTM_APP);
        db.execSQL("DROP TABLE IF EXISTS " + IMAGE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BHIM_APP);
        db.execSQL("DROP TABLE IF EXISTS " + INVOICE_IMAGES);

        onCreate(db);
        Log.d(TAG, "onUpgrade:DB");

    }





      public  void  addItemDetails(String p_id, String item_id, String item_name, String item_qty,
                                   String selected_qty, String mrp, String purchase_price, String total_price, String gst, String discount, String unit_price, String hsn, String status, String munit, String unit, String barcode, String shorcut)
      {
          SQLiteDatabase db = this.getWritableDatabase();
          try {
              ContentValues values = new ContentValues();
              values.put(KEY_PRODUCT_ID, p_id);
              values.put(KEY_ITEM_ID, item_id);
              values.put(KEY_ITEM_NAME, item_name);
              values.put(KEY_STOCK_QTY, item_qty);
              values.put(KEY_SELECTED_QTY, selected_qty);
              values.put(KEY_ITEM_MRP, mrp);
              values.put(KEY_PURCHASE_PRICE, purchase_price);
              values.put(KEY_UNIT_PRICE, unit_price);
              values.put(KET_TOTALPRICE, total_price);
              values.put(KEY_GST, gst);
              values.put(KEY_DISCOUNT, discount);
              values.put(KEY_HSN, hsn);
              values.put(KEY_TOTAL_PRICE_CAL, "0");
              values.put(KEY_TAXABLEVALUE, "0");
              values.put(KEY_CAL_GST, "0");
              values.put(KEY_STATUS,status);
              values.put(KEY_MUNIT,munit);
              values.put(KEY_UNIT,unit);
              values.put(KEY_CHANGEDUNIT,"NOT");// need to change if api is ready
              values.put(KEY_SHORTCUT,shorcut);
              values.put(KEY_ITEMBARCODE,barcode);



              Log.d("item table", "p_id " + p_id + " " + ",i_id:" + item_id + ", item_name :" + item_name + ",item_qty " +
                      item_qty + ", selected " + selected_qty + ", item_price :" + mrp + ",t_price :" + total_price + "gst" + gst + "unit price :" +
                      unit_price +"hsn"+hsn+"status"+status+"mnuit"+munit+"unit"+unit+"barcode"+barcode);

            noOfrow=   db.insert(ITEM_TABLE, null, values);
              db.close();
          }catch (SQLException e)
          {
              Log.d(TAG, "addItemDetails:"+e);

          }catch (NullPointerException e)
          {
              Log.d(TAG, "addItemDetails:"+e);

          }catch (Exception e)
          {
              Log.d(TAG, "addItemDetails:"+e);
          }finally
          {
              Log.d(TAG, "addItemDetails:NumberOfRow:"+noOfrow);

          }

      }



      public ArrayList<ItemDetalisClass> getItemDetails(int p_id)
      {
          String sql;
          ArrayList arrayList=new ArrayList();
          SQLiteDatabase db = this.getReadableDatabase();



                if(p_id == 1)
                {
                    sql="SELECT *  FROM " + ITEM_TABLE +" ORDER BY " + KEY_ITEM_NAME + " ASC" ;
                }else
                {
                     sql="SELECT *  FROM " + ITEM_TABLE + " WHERE " + KEY_PRODUCT_ID + " = '" +p_id+"' " +"ORDER BY " + KEY_ITEM_NAME + " ASC";
                }

             Log.d("sqlQuery",sql);
            try {
                Cursor c = db.rawQuery(sql, null);
                        int i =c.getCount();
                Log.d(TAG, "getItemDetails:ByPID"+i);
                if (c.moveToFirst()) {
                    do {

                        ItemDetalisClass itemDetalisClass = new ItemDetalisClass();
                        itemDetalisClass.setProduct_id(c.getString(1));
                        itemDetalisClass.setItem_id(c.getString(2));
                        itemDetalisClass.setItem_name(c.getString(3));
                        itemDetalisClass.setItem_qty(c.getString(4));
                        itemDetalisClass.setSelectd_qty(c.getString(5));
                        itemDetalisClass.setMrp(c.getString(6));
                        itemDetalisClass.setItem_purchase(c.getString(7));
                         itemDetalisClass.setUnit_price(c.getString(8));
                        itemDetalisClass.setTotalPrice(c.getString(9));
                        itemDetalisClass.setGst_per(c.getString(10));
                        itemDetalisClass.setDiscount(c.getString(11));
                        itemDetalisClass.setTotal_calculared_price(c.getString(13));
                        itemDetalisClass.setTotal_taxableValue(c.getString(14));
                        itemDetalisClass.setCalculated_gst(c.getString(15));
                        itemDetalisClass.setStatus(c.getString(16));
                        itemDetalisClass.setmUnit(c.getString(17));
                        itemDetalisClass.setUnit(c.getString(18));
                        itemDetalisClass.setChnagedUnit(c.getString(19));

                        itemDetalisClass.setShortcut(c.getString(20));
                        itemDetalisClass.setItemBarcode(c.getString(21));
                        arrayList.add(itemDetalisClass);

                    } while (c.moveToNext());
                }

                c.close();
                db.close();
            }catch (Exception e)
            {
                Log.d("exception",""+e);
            }

          return arrayList;



      }






    public ArrayList<String> getInvocieImage(String in_number)
    {
        String image = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns={"image"};
        ArrayList arrayList=new ArrayList();


        try
        {

            String sql="SELECT  *  FROM " + INVOICE_IMAGES +";";
            Cursor c = db.rawQuery(sql, null);
                //Log.d("sqlQuery"c);
                 int id=c.getColumnCount();
                   image = c.getColumnName(0);

            if (c.moveToFirst())
            {
                do
                {

                    arrayList.add(c.getString(2));




                } while (c.moveToNext());
            }

            c.close();
            db.close();
        }catch (Exception e)
        {
            Log.d("Exception",""+e);
        }


        return arrayList;
    }



    public ArrayList<ItemDetalisClass> getSelectedItemDetails()
    {
        ArrayList arrayList=new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql="SELECT *  FROM " + ITEM_TABLE + " WHERE " + KEY_SELECTED_QTY + " > 0 ";
        Log.d("sqlQuery",sql);
        try
        {
            Cursor c = db.rawQuery(sql, null);

            if (c.moveToFirst())
            {
                do
                {

                    ItemDetalisClass itemDetalisClass = new ItemDetalisClass();
                    itemDetalisClass.setProduct_id(c.getString(1));
                    itemDetalisClass.setItem_id(c.getString(2));
                    itemDetalisClass.setItem_name(c.getString(3));
                    itemDetalisClass.setItem_qty(c.getString(4));
                    itemDetalisClass.setSelectd_qty(c.getString(5));
                    itemDetalisClass.setMrp(c.getString(6));
                    itemDetalisClass.setItem_purchase(c.getString(7));
                     itemDetalisClass.setUnit_price(c.getString(8));
                    itemDetalisClass.setTotalPrice(c.getString(9));
                    itemDetalisClass.setGst_per(c.getString(10));
                    itemDetalisClass.setDiscount(c.getString(11));
                    itemDetalisClass.setTotal_calculared_price(c.getString(13));
                    itemDetalisClass.setTotal_taxableValue(c.getString(14));
                    itemDetalisClass.setCalculated_gst(c.getString(15));
                    itemDetalisClass.setStatus(c.getString(16));
                    itemDetalisClass.setmUnit(c.getString(17));
                    itemDetalisClass.setUnit(c.getString(18));
                    itemDetalisClass.setChnagedUnit(c.getString(19));
                    itemDetalisClass.setShortcut(c.getString(20));
                    itemDetalisClass.setItemBarcode(c.getString(21));
                    arrayList.add(itemDetalisClass);

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        }catch (Exception e)
        {
            Log.d("exception",""+e);
        }finally {
            db.close();
        }

        return arrayList;



    }
    public  int addImage(String image)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_IMAGE, image);
            long id = db.insert(PAYTM_APP, null, values);//Insert query to store the record in the database
            db.close();
            Log.d(TAG, "addImage: " + id);
            return (int) id;
        }catch (Exception e)
        {
            Log.d("exception",""+e);

        }
        return -1;

    }


    public  int addBhimAppScreen(String image)
    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_IMAGE, image);
            long id = db.insert(BHIM_APP, null, values);//Insert query to store the record in the database
            db.close();
            Log.d(TAG, "addImageBhim: " + id);
            return (int) id;
        }catch (Exception e)
        {
            Log.d("exception",""+e);

        }
        return -1;

    }

    public  int  addInvoiceImage(String innumber, String image, int imagenumber)
    {
        Log.d(TAG, "addInvoiceNumber"+innumber);
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(INVOICE_NUMBER, innumber);
            values.put(IMAGE_NUMBER,imagenumber);
            values.put(KEY_IMAGE1, image);

            long id = db.insert(INVOICE_IMAGES, null, values);//Insert query to store the record in the database
            db.close();
            Log.d(TAG, "addImage: " + id);
            return (int) id;
        }catch (Exception e)
        {
            Log.d(TAG, "addInvoiceNumber"+innumber);
            Log.d("exception",""+e);
            return -1;
        }


    }




public  ArrayList<BillingItems>   getBillingItems()
{
    ArrayList arrayList=new ArrayList();
    SQLiteDatabase db = this.getReadableDatabase();
    String sql="SELECT *  FROM " + ITEM_TABLE + " WHERE " + KEY_SELECTED_QTY + " > 0 ";
    Log.d("sqlQuery",sql);
    try
    {
        Cursor c = db.rawQuery(sql, null);
        Log.d(TAG, "getBillingItems:"+c.getCount());

        if (c.moveToFirst())
        {
            do
            {

                BillingItems  billingItems = new BillingItems();
                billingItems.setId(c.getString(0));
                billingItems.setP_id(c.getString(1));
                billingItems .setItem_id(c.getString(2));
                billingItems .setItem_name(c.getString(3));
                billingItems .setStock_qty(c.getString(4));
                billingItems .setSelected_qty(c.getString(5));
                billingItems .setItem_mrp(c.getString(6));
                billingItems .setPurchase_price(c.getString(7));
                billingItems .setUnit_price(c.getString(8));
                billingItems .setTotal_price(c.getString(9));
                billingItems .setItem_gst(c.getString(10));
                billingItems .setDiscount(c.getString(11));
                billingItems.setHsnCode(c.getString(12));
                billingItems .setTotal_Calculated_price(c.getString(13));
                billingItems .setTaxablePrice(c.getString(14));
                billingItems .setTotal_cal_gst(c.getString(15));
                billingItems.setStatus(c.getString(16));
                billingItems.setmUnit(c.getString(17));
                billingItems.setUnit(c.getString(18));
                billingItems.setChangedUnit(c.getString(19));
                billingItems.setShortcut(c.getString(20));
                billingItems.setItembarcode(c.getString(21));


                arrayList.add(billingItems);

            } while (c.moveToNext());
        }

        c.close();
        db.close();
    }catch (Exception e)
    {
        Log.d("exception",""+e);
    }finally {
        db.close();
    }

    return arrayList;


}



    public ArrayList<ItemDetalisClass> searchItem(String name,String p_id)
    {
        ArrayList arrayList=new ArrayList();
        String sql;
        SQLiteDatabase db = this.getReadableDatabase();
         String itemName=name+"%";
        if(p_id.equalsIgnoreCase("1"))
        {
            sql="SELECT *  FROM " + ITEM_TABLE + " WHERE " + KEY_ITEM_NAME + " LIKE '"+itemName+"'";
        }else if(name.isEmpty() && name=="")
        {
            sql="SELECT *  FROM " + ITEM_TABLE + " WHERE " + KEY_PRODUCT_ID + " = '" +p_id +"';";
        }
        else
        {
            sql="SELECT *  FROM " + ITEM_TABLE + " WHERE " + KEY_ITEM_NAME + " LIKE '"+itemName+"'" +"AND " +KEY_PRODUCT_ID+" = '" +p_id +"';";
        }

        Log.d("sqlQuery",sql);
        try {
            Cursor c = db.rawQuery(sql, null);
            if (c.moveToFirst()) {
                do
                {

                    ItemDetalisClass itemDetalisClass = new ItemDetalisClass();
                    itemDetalisClass.setProduct_id(c.getString(1));
                    itemDetalisClass.setItem_id(c.getString(2));
                    itemDetalisClass.setItem_name(c.getString(3));
                    itemDetalisClass.setItem_qty(c.getString(4));
                    itemDetalisClass.setSelectd_qty(c.getString(5));
                    itemDetalisClass.setMrp(c.getString(6));
                    itemDetalisClass.setItem_purchase(c.getString(7));
                    itemDetalisClass.setUnit_price(c.getString(8));
                    itemDetalisClass.setTotalPrice(c.getString(9));
                    itemDetalisClass.setGst_per(c.getString(10));
                    itemDetalisClass.setDiscount(c.getString(11));
                    itemDetalisClass.setTotal_calculared_price(c.getString(13));
                    itemDetalisClass.setTotal_taxableValue(c.getString(14));
                    itemDetalisClass.setCalculated_gst(c.getString(15));
                    itemDetalisClass.setStatus(c.getString(16));
                    itemDetalisClass.setmUnit(c.getString(17));
                    itemDetalisClass.setUnit(c.getString(18));
                    itemDetalisClass.setChnagedUnit(c.getString(19));
                    itemDetalisClass.setShortcut(c.getString(20));
                    itemDetalisClass.setItemBarcode(c.getString(21));


                    arrayList.add(itemDetalisClass);

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        }catch (Exception e)
        {
            Log.d("exception",""+e);
        }

        return arrayList;



    }


    public ArrayList<ItemDetalisClass> searchItemForBarcode(String name,String p_id)
    {
        ArrayList arrayList=new ArrayList();
        String sql;
        SQLiteDatabase db = this.getReadableDatabase();
        String itemName=name;

            sql="SELECT *  FROM " + ITEM_TABLE + " WHERE " + KEY_ITEMBARCODE + "="+itemName+"";

        Log.d("sqlQuery",sql);
        try {
            Cursor c = db.rawQuery(sql, null);
            if (c.moveToFirst()) {
                do
                {

                    ItemDetalisClass itemDetalisClass = new ItemDetalisClass();
                    itemDetalisClass.setProduct_id(c.getString(1));
                    itemDetalisClass.setItem_id(c.getString(2));
                    itemDetalisClass.setItem_name(c.getString(3));
                    itemDetalisClass.setItem_qty(c.getString(4));
                    itemDetalisClass.setSelectd_qty(c.getString(5));
                    itemDetalisClass.setMrp(c.getString(6));
                    itemDetalisClass.setItem_purchase(c.getString(7));
                    itemDetalisClass.setUnit_price(c.getString(8));
                    itemDetalisClass.setTotalPrice(c.getString(9));
                    itemDetalisClass.setGst_per(c.getString(10));
                    itemDetalisClass.setDiscount(c.getString(11));
                    itemDetalisClass.setTotal_calculared_price(c.getString(13));
                    itemDetalisClass.setTotal_taxableValue(c.getString(14));
                    itemDetalisClass.setCalculated_gst(c.getString(15));
                    itemDetalisClass.setStatus(c.getString(16));
                    itemDetalisClass.setmUnit(c.getString(17));
                    itemDetalisClass.setUnit(c.getString(18));
                    itemDetalisClass.setChnagedUnit(c.getString(19));
                    itemDetalisClass.setShortcut(c.getString(20));
                    itemDetalisClass.setItemBarcode(c.getString(21));
                    arrayList.add(itemDetalisClass);

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        }catch (Exception e)
        {
            Log.d("exception",""+e);
        }

        return arrayList;



    }




    public void updateQty(String itemId, String qty, String totalAount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SELECTED_QTY, qty);
        values.put(KET_TOTALPRICE,totalAount);

        db.update(ITEM_TABLE, values, KEY_ITEM_ID + " = ?",
                new String[] { String.valueOf(itemId) });
    }




    public boolean check_items()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + ITEM_TABLE + ";";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        Log.d(TAG, "check_items:"+icount);
        if (icount > 0)
        {
            return true;
        }
//leave
        else {
            return false;
        }

         }




    public int getTotalQunatitiy()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT   SUM(" +KEY_SELECTED_QTY+ ") FROM "+ITEM_TABLE, null);
        if(cur.moveToFirst())
        {
            return  cur.getInt(0);
        }else{
            return 0;
        }

    }


    public double getTaxableValue()
    {

        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            String sql="SELECT   SUM(" +KEY_TAXABLEVALUE+ ") FROM "+ITEM_TABLE+"  WHERE  selected_qty > 0 " ;
            Cursor cur = db.rawQuery(sql, null);
            if(cur.moveToFirst())
            {
                return  cur.getDouble(0);
        }else
        {
            return 0;
        }
        }catch (Exception e)
        {
            Log.d(TAG, "getTaxableValue:"+e);
        }finally
        {
            db.close();
        }

          return 0;
    }
    public double getTotalGST()
    {

        SQLiteDatabase db = this.getReadableDatabase();
        try
        {

            Cursor cur = db.rawQuery("SELECT   SUM(" +KEY_CAL_GST+ ") FROM "+ITEM_TABLE, null);
            if(cur.moveToFirst())
            {
                return  cur.getDouble(0);
            }else
            {
                return 0;
            }
        }catch (Exception e)
        {
            Log.d(TAG, "getTaxableValue:"+e);
        }finally
        {
            db.close();
        }

        return 0;
    }

    public double getTotalAmount()
    {

        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            String sql="SELECT   SUM(" +KEY_TOTAL_PRICE_CAL+ ") FROM "+ITEM_TABLE+"  WHERE  selected_qty > 0 " ;

            Cursor cur = db.rawQuery(sql, null);
            if(cur.moveToFirst())
            {
                return  cur.getDouble(0);
            }else
            {
                return 0;
            }
        }catch (Exception e)
        {
            Log.d(TAG, "getTaxableValue:"+e);
        }finally
        {
            db.close();
        }

        return 0;
    }




    public void deleteItemTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
             db.execSQL("delete from " + ITEM_TABLE);
          //  db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + ITEM_TABLE + "'");
            db.execSQL(" VACUUM " + ITEM_TABLE);


        }catch (Exception e)
        {
            Log.d(TAG, "deleteItemTable:"+e);
        }
        finally {
            db.close();

        }


    }
    public void deleteInvoiceTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {

            db.execSQL("delete from " +INVOICE_IMAGES);
            db.execSQL(" VACUUM " + INVOICE_IMAGES);
        }catch (SQLException e)
        {
            Log.d(TAG, "deleteInvoiceTable:"+e);

        }catch (Exception e)
        {

        }finally
        {
            db.close();

        }


    }

    public  void deleteDataBase()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PAYTM_APP);
            db.execSQL("DROP TABLE IF EXISTS " + IMAGE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + BHIM_APP);
            db.execSQL("DROP TABLE IF EXISTS " + INVOICE_IMAGES);
            onCreate(db);
            Log.d(TAG, "DROP:DB");

            //   db.execSQL("delete from " +INVOICE_IMAGES);
            //db.execSQL(" VACUUM " + INVOICE_IMAGES);
        }catch (SQLException e)
        {
            Log.d(TAG, "deleteInvoiceTable:"+e);

        }catch (Exception e)
        {

        }finally
        {
            db.close();

        }

    }
    public  void deleteDataBase_ItemTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
//            db.execSQL("DROP TABLE IF EXISTS " + PAYTM_APP);
//            db.execSQL("DROP TABLE IF EXISTS " + IMAGE_TABLE);
//            db.execSQL("DROP TABLE IF EXISTS " + BHIM_APP);
//            db.execSQL("DROP TABLE IF EXISTS " + INVOICE_IMAGES);
            onCreate(db);
            Log.d(TAG, "DROP:DB");

            //   db.execSQL("delete from " +INVOICE_IMAGES);
            //db.execSQL(" VACUUM " + INVOICE_IMAGES);
        }catch (SQLException e)
        {
            Log.d(TAG, "deleteInvoiceTable:"+e);

        }catch (Exception e)
        {

        }finally
        {
            db.close();

        }

    }



    public void updateQty(String item_id, String qty, String total_price_cal, String taxable_value, String cal_gst, String changedUnit)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            ContentValues values = new ContentValues();
            values.put(KEY_SELECTED_QTY, qty);
            values.put(KEY_TOTAL_PRICE_CAL,total_price_cal);
            values.put(KEY_TAXABLEVALUE,taxable_value);
            values.put(KEY_CAL_GST,cal_gst);
            values.put(KEY_CHANGEDUNIT,changedUnit);

           int updatedid= db.update(ITEM_TABLE, values, KEY_ITEM_ID + " = ?",
                    new String[] { item_id });

            Log.d(TAG, "updateQty:UpdatedId"+updatedid);

        }catch (NullPointerException e)
        {
            Log.d(TAG, "updateQty:NullPointer");

        }catch (Exception e)
        {
            Log.d(TAG, "updateQty: Exception");

        }finally
        {
            db.close();
        }


    }


    public void updateQty()
    {

        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            ContentValues values = new ContentValues();
            values.put(KEY_SELECTED_QTY, "0");
            values.put(KEY_TOTAL_PRICE_CAL,"0");
            values.put(KEY_TAXABLEVALUE,"0");
            values.put(KEY_CAL_GST,"0");

            int updatedid= db.update(ITEM_TABLE, values, KEY_SELECTED_QTY + " > ?", new String[] { "0" });

            Log.d(TAG, "updateQty:UpdatedId"+updatedid);

        }catch (NullPointerException e)
        {
            Log.d(TAG, "updateQty:NullPointer");

        }catch (Exception e)
        {
            Log.d(TAG, "updateQty:Exception");

        }finally
        {
            db.close();
        }


    }




    public  String getPaytmAppScreen()
    {
        String image = null;
        SQLiteDatabase db = this.getReadableDatabase();
        //String sql="SELECT *  FROM " + PAYTM_APP + " WHERE " + KEY_ID+ " = 1";
        String[] yourColumn={KEY_ID,KEY_IMAGE};

     //   ursor c = scoreDb.

       // String sql= "SELECT *  FROM " + PAYTM_APP +" order by "+ KEY_ID+ " DESC" ;
        //Log.d("sqlQuery",sql);

        try {
            Cursor c =db.query(PAYTM_APP, yourColumn, null, null, null, null, KEY_ID+" DESC");// db.rawQuery(sql, null);
            if (c.moveToFirst()) {
                do {


                    image= c.getString(1);
                    break;
                   // return image;


                } while (c.moveToNext());
            }

            c.close();
            db.close();
        }catch (Exception e)
        {
            Log.d("exceptin",""+e);
        }


        return image;
    }


    public  String getBhimScreen()
    {
        String image = null;
        SQLiteDatabase db = this.getReadableDatabase();
      //  String sql=" SELECT *  FROM " + BHIM_APP + " WHERE " + KEY_ID+ " = 1";
       // Log.d("sqlQuery",sql);
        String[] yourColumn={KEY_ID,KEY_IMAGE};

        try {
            Cursor c =   c =db.query(BHIM_APP, yourColumn, null, null, null, null, KEY_ID+" DESC");
            if (c.moveToFirst())
            {
                do {


                    image= c.getString(1);
                    return image;

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        }catch (Exception e)
        {
            Log.d("exception",""+e);
        }


        return image;
    }

    public int deleteInvoiceImage(String invoiceNumber, int imageCount)
    {
        SQLiteDatabase db = this.getReadableDatabase();

       try
       {



         int i=  db.delete(INVOICE_IMAGES, IMAGE_NUMBER + "=?", new String[]{String.valueOf(imageCount)});
           return i;

       }catch (Exception e)
       {
           Log.d("exception",""+e);
       }finally {
           db.close();
       }



        return 0;
    }

    public void updatedDiscontedPrice(String taxableValue, String itemId, String totalAmount, String cal_gst)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            ContentValues values = new ContentValues();
           // values.put(KEY_SELECTED_QTY, qty);
           values.put(KEY_TOTAL_PRICE_CAL,totalAmount);
            values.put(KEY_TAXABLEVALUE,taxableValue);
            values.put(KEY_CAL_GST,cal_gst);

            int updatedid= db.update(ITEM_TABLE, values, KEY_ITEM_ID + " = ?",
                    new String[] { itemId });

            Log.d(TAG, "updateQty:UpdatedId"+updatedid);

        }catch (NullPointerException e)
        {
            Log.d(TAG, "updateQty:NullPointer");

        }catch (Exception e)
        {
            Log.d(TAG, "updateQty:Exception");

        }finally
        {
            db.close();
        }

    }
}

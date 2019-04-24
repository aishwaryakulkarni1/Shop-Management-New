package com.inevitablesol.www.shopmanagement.RegistrationModule;

/**
 * Created by Ujwal Tale on 15-09-2017.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.BuildConfig;
import com.inevitablesol.www.shopmanagement.ItemModule.StockInfo;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.customer_module.CustomerInfo;
import com.inevitablesol.www.shopmanagement.sales.SaleInfo;
import com.inevitablesol.www.shopmanagement.vendor_module.Record;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.DataOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import static android.content.Context.NOTIFICATION_SERVICE;


public class ReadWriteExcelFile {

//    public static void readXLSFile() throws IOException
//    {
//        InputStream ExcelFileToRead = new FileInputStream("C:/Test.xls");
//        HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);
//
//        HSSFSheet sheet=wb.getSheetAt(0);
//        HSSFRow row;
//        HSSFCell cell;
//
//        Iterator rows = sheet.rowIterator();
//
//        while (rows.hasNext())
//        {
//            row=(HSSFRow) rows.next();
//            Iterator cells = row.cellIterator();
//
//            while (cells.hasNext())
//            {
//                cell=(HSSFCell) cells.next();
//
//                if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
//                {
//                    System.out.print(cell.getStringCellValue()+" ");
//                }
//                else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
//                {
//                    System.out.print(cell.getNumericCellValue()+" ");
//                }
//                else
//                {
//                    //U Can Handel Boolean, Formula, Errors
//                }
//            }
//            System.out.println();
//        }
//
//    }
//


    private static final String TAG = "ReadWriteExcelFile";
        public static void writeXLSFile() throws IOException {

            String excelFileName = "/root/sdcard1/Test.xls";//name of excel file

            String sheetName = "Sheet1.xls";//name of sheet

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName) ;

            //iterating r number of rows
            for (int r=0;r < 5; r++ )
            {
                HSSFRow row = sheet.createRow(r);

                //iterating c number of columns
                for (int c=0;c < 5; c++ )
                {
                    HSSFCell cell = row.createCell(c);

                    cell.setCellValue("Cell "+r+" "+c);
                }
            }

            FileOutputStream fileOut = new FileOutputStream(excelFileName);

            //write this workbook to an Outputstream.
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        }

    public  static  void testFile(Context context, String fileName, ArrayList<SaleInfo> saleInfos) throws  IOException

    {
        int i=1;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet firstSheet = workbook.createSheet("Sheet1");
        //HSSFSheet secondSheet = workbook.createSheet("Sheet2");
        HSSFRow rowAA = firstSheet.createRow(0);
        HSSFCell cellAA = rowAA.createCell(0);
        HSSFCell cellAB = rowAA.createCell(1);
        HSSFCell cellAc = rowAA.createCell(2);
        HSSFCell cellAd = rowAA.createCell(3);
        HSSFCell cellAe = rowAA.createCell(4);
        cellAA.setCellValue("Created Date");
        cellAB.setCellValue("Invocie Date");
        cellAc.setCellValue("payment Mode");
        cellAd.setCellValue("Amount");
        cellAe.setCellValue("Balance");
        if(saleInfos.size()>0) {
            for (SaleInfo id : saleInfos)
            {

                HSSFRow rowA = firstSheet.createRow(i);
                HSSFCell cellA = rowA.createCell(0);
                HSSFCell cellB = rowA.createCell(1);
                HSSFCell cellc = rowA.createCell(2);
                HSSFCell celld = rowA.createCell(3);
                HSSFCell celle = rowA.createCell(4);
                cellB.setCellValue(id.getInvoice_id());
                cellA.setCellValue(id.getCreated_Date());
                cellc.setCellValue(id.getModeOfPayment());
                celld.setCellValue(id.getAmountPaid());
                celle.setCellValue(id.getBalanceDue());
                i++;
            }
        }else
        {

            Toast.makeText(context,"Data Not Available",Toast.LENGTH_LONG).show();
            return;
        }

//        HSSFRow rowB = secondSheet.createRow(0);
//        HSSFCell cellB = rowB.createCell(0);
//        cellB.setCellValue(new HSSFRichTextString("Sheet2"));
        FileOutputStream fos = null;
        try {

            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File (sdCard.getAbsolutePath() + "/SaleReports");
            directory.mkdirs();
            File file = new File(directory, fileName+".xls");
//            String str_path = Environment.getExternalStorageDirectory().toString();
//            File file ;
            //  file = new File(str_path, fileName + ".xls");
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(context, "Excel Sheet Generated", Toast.LENGTH_SHORT).show();
        }
    }




    public  static  void testFile(Context context, String fileName, ArrayList<SaleInfo> saleInfos,String Directory) throws  IOException

    {
        int i=1;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet firstSheet = workbook.createSheet("Sheet1");
        //HSSFSheet secondSheet = workbook.createSheet("Sheet2");
        HSSFRow rowAA = firstSheet.createRow(0);
        HSSFCell cellAA = rowAA.createCell(0);
        HSSFCell cellAB = rowAA.createCell(1);
        HSSFCell cellAc = rowAA.createCell(2);
        HSSFCell cellAd = rowAA.createCell(3);
        HSSFCell cellAe = rowAA.createCell(4);

        HSSFCell cellAf = rowAA.createCell(5);
        HSSFCell cellAg = rowAA.createCell(6);
        HSSFCell cellAh = rowAA.createCell(7);
        HSSFCell cellAi = rowAA.createCell(8);


        cellAA.setCellValue("Created Date");
        cellAB.setCellValue("Invocie Date");
        cellAc.setCellValue("Taxable Value");
        cellAd.setCellValue("Total GST");
        cellAe.setCellValue("Total Amount");

        cellAf.setCellValue("Amount Paid");
        cellAg.setCellValue("Balance");
        cellAh.setCellValue("Payment Mode");
        cellAi.setCellValue("Status");


        if(saleInfos.size()>0)
        {
            for (SaleInfo id : saleInfos)
            {

                HSSFRow rowA = firstSheet.createRow(i);
                HSSFCell cellA = rowA.createCell(0);
                HSSFCell cellB = rowA.createCell(1);
                HSSFCell cellc = rowA.createCell(2);
                HSSFCell celld = rowA.createCell(3);
                HSSFCell celle = rowA.createCell(4);
                HSSFCell cellf = rowA.createCell(5);
                HSSFCell cellg = rowA.createCell(6);
                HSSFCell cellh = rowA.createCell(7);
                HSSFCell celli = rowA.createCell(8);

                cellB.setCellValue(id.getInvoice_id());
                cellA.setCellValue(id.getCreated_Date());
                cellc.setCellValue(id.getTaxable_value());
                celld.setCellValue(id.getTotal_gst());
                celle.setCellValue(id.getTotalAmount());
                cellf.setCellValue(id.getAmountPaid());
                cellg.setCellValue(id.getBalanceDue());
                cellh.setCellValue(id.getModeOfPayment());
                celli.setCellValue(id.getStatus());

                i++;
            }
        }else
        {

            Toast.makeText(context,"Data Not Available",Toast.LENGTH_LONG).show();
            return;
        }

//        HSSFRow rowB = secondSheet.createRow(0);
//        HSSFCell cellB = rowB.createCell(0);
//        cellB.setCellValue(new HSSFRichTextString("Sheet2"));
        FileOutputStream fos = null;
        try {

            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File (sdCard.getAbsolutePath() + "/SaleReports/"+Directory);
            directory.mkdirs();
            File file = new File(directory, fileName+".xls");
//            String str_path = Environment.getExternalStorageDirectory().toString();
//            File file ;
            //  file = new File(str_path, fileName + ".xls");
            fos = new FileOutputStream(file);
            workbook.write(fos);
            workbook.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(context, "Excel Sheet Generated", Toast.LENGTH_SHORT).show();
        }
    }

      public   void  getCustomerDeatils(Context applicationContext, String cutomer, ArrayList<CustomerInfo> customerInfos)
      {
          int i=1;
          HSSFWorkbook workbook = new HSSFWorkbook();
          HSSFSheet firstSheet = workbook.createSheet("Sheet1");
          HSSFRow rowAA = firstSheet.createRow(0);

          HSSFCell cellAA = rowAA.createCell(0);
          HSSFCell cellAB = rowAA.createCell(1);
          HSSFCell cellAc = rowAA.createCell(2);
          HSSFCell cellAd = rowAA.createCell(3);
          HSSFCell cellAe = rowAA.createCell(4);

          HSSFCell cellAf = rowAA.createCell(5);
          HSSFCell cellAg = rowAA.createCell(6);
          HSSFCell cellAh = rowAA.createCell(7);
          HSSFCell cellAi = rowAA.createCell(8);

         // cellAA.setCellValue("customer_id");
          cellAB.setCellValue("customer name");
          cellAc.setCellValue("mobile number");
          cellAd.setCellValue("email id");
          cellAe.setCellValue("GST Register");
          cellAf.setCellValue("GSTIN");
          cellAg.setCellValue("place of supply");
          cellAh.setCellValue("Home Delivery");
          cellAi.setCellValue("Address");

          if(customerInfos.size()>0)
          {
              for (CustomerInfo customerInfos1:customerInfos)
              {

                  HSSFRow rowA = firstSheet.createRow(i);
                  HSSFCell cellA = rowA.createCell(0);
                  HSSFCell cellB = rowA.createCell(1);
                  HSSFCell cellc = rowA.createCell(2);
                  HSSFCell celld = rowA.createCell(3);
                  HSSFCell celle = rowA.createCell(4);
                  HSSFCell cellf = rowA.createCell(5);
                  HSSFCell cellg = rowA.createCell(6);
                  HSSFCell cellh = rowA.createCell(7);
                  HSSFCell celli = rowA.createCell(8);

                 // cellA.setCellValue(customerInfos1.getCustomer_id());
                  cellB.setCellValue(customerInfos1.getCustomer_name());
                  cellc.setCellValue(customerInfos1.getMobile_numbe());
                  celld.setCellValue(customerInfos1.getEmail_id());
                  celle.setCellValue(customerInfos1.getGstStatus());
                  cellf.setCellValue(customerInfos1.getGstin());
                  cellg.setCellValue(customerInfos1.getState());

                  cellh.setCellValue(customerInfos1.getHome_delivery());
                  celli.setCellValue(customerInfos1.getAddress());


                  i++;

              }


          }else
    {

        Toast.makeText(applicationContext,"Data Not Available",Toast.LENGTH_LONG).show();
        return;
    }
    FileOutputStream fos = null;
        try {

        File sdCard =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File directory = new File (sdCard.getAbsolutePath() + "/Cutomers/"+"Export");
        directory.mkdirs();
        File file = new File(directory, "cutomer.xls");
//            String str_path = Environment.getExternalStorageDirectory().toString();
//            File file ;
        //  file = new File(str_path, fileName + ".xls");
        fos = new FileOutputStream(file);
        workbook.write(fos);
        workbook.close();

//            Intent intent = new Intent();
//            intent.setAction(android.content.Intent.ACTION_VIEW);
//          //  File file = new File("YOUR_FILE_URI"); // set your filepath
//            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
//          //  intent.setDataAndType(path,"application/vnd.ms-excel");
//
//
//            try
//            {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                //intent.setAction(android.content.Intent.ACTION_VIEW);
//                PendingIntent pIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0);
//
//
//                    Uri path = FileProvider.getUriForFile(applicationContext, BuildConfig.APPLICATION_ID+".provider",file);
//
//                    intent.setDataAndType(path,"application/vnd.ms-excel");
//
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    applicationContext. startActivity(intent);
//                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                final Notification noti = new NotificationCompat.Builder(applicationContext)
//                        .setContentTitle("Download completed")
//                        .setContentText("Customers")
//                        .setSmallIcon(R.drawable.app_icon)
//                        .setContentIntent(pIntent).build();
//
//
//                noti.flags |= Notification.FLAG_AUTO_CANCEL;
//
//                NotificationManager notificationManager = (NotificationManager) applicationContext.getSystemService(NOTIFICATION_SERVICE);
//                notificationManager.notify(0, noti);
//
//
//
//
//
//            } catch (ActivityNotFoundException e)
//            {
//                Toast.makeText(applicationContext, "No Application available to view XLS", Toast.LENGTH_SHORT).show();
//            }
//




    } catch (IOException e)
    {
        e.printStackTrace();
    } finally {
        if (fos != null) {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(applicationContext, "Excel Sheet Generated", Toast.LENGTH_SHORT).show();
    }

      }





    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }




    public void getStockDeatils(Context applicationContext, String stock, ArrayList<StockInfo> stockInfoArrayList)
    {
        Log.d(TAG, "getStockDeatils: "+stockInfoArrayList);
        int i=0;

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet firstSheet = workbook.createSheet("Sheet1");
        //HSSFSheet secondSheet = workbook.createSheet("Sheet2");
        HSSFRow rowAA = firstSheet.createRow(0);
        HSSFCell cellAA = rowAA.createCell(0);
        HSSFCell cellAB = rowAA.createCell(1);
        HSSFCell cellAc = rowAA.createCell(2);
        HSSFCell cellAd = rowAA.createCell(3);


        cellAA.setCellValue("Product Type");
        cellAB.setCellValue("Product Name");
        cellAc.setCellValue("Storage Quantity");
        cellAd.setCellValue("Stock Quantity");

        if(stockInfoArrayList.size()>0)
        {

            for (StockInfo record:stockInfoArrayList)
            {
                HSSFRow rowA = firstSheet.createRow(i);
                HSSFCell cellA = rowA.createCell(0);
                HSSFCell cellB = rowA.createCell(1);
                HSSFCell cellc = rowA.createCell(2);
                HSSFCell celld = rowA.createCell(3);



                cellA.setCellValue(record.getProduct_type());
                cellB.setCellValue(record.getItem_name());
                cellc.setCellValue(record.getStock_qty());
                celld.setCellValue(record.getStorage_qty());






                i++;



            }
        }

        FileOutputStream fos = null;
        try {

            File sdCard =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File directory = new File (sdCard.getAbsolutePath() + "/Stock/"+"StockDeatils");
            directory.mkdirs();
            File file = new File(directory, "stock.xls");
//            String str_path = Environment.getExternalStorageDirectory().toString();
//            File file ;
            //  file = new File(str_path, fileName + ".xls");
            fos = new FileOutputStream(file);
            workbook.write(fos);
            workbook.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(applicationContext, "Excel Sheet Generated", Toast.LENGTH_SHORT).show();
        }

    }


    public void getVendorDeatils(Context applicationContext, String cutomer, ArrayList<Record> vendorLists)
    {

        Log.d(TAG, "getVendorDeatils: "+vendorLists.toString());
        Log.d(TAG, "getVendorDeatils: "+vendorLists.get(0).getGstinNo());
          int i=1;

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet firstSheet = workbook.createSheet("Sheet1");
        //HSSFSheet secondSheet = workbook.createSheet("Sheet2");
        HSSFRow rowAA = firstSheet.createRow(0);
        HSSFCell cellAA = rowAA.createCell(0);
        HSSFCell cellAB = rowAA.createCell(1);
        HSSFCell cellAc = rowAA.createCell(2);
        HSSFCell cellAd = rowAA.createCell(3);
        HSSFCell cellAe = rowAA.createCell(4);

        HSSFCell cellAf = rowAA.createCell(5);
        HSSFCell cellAg = rowAA.createCell(6);
        HSSFCell cellAh = rowAA.createCell(7);
        HSSFCell cellAi = rowAA.createCell(8);

        cellAA.setCellValue("Shop Name");
        cellAB.setCellValue("Owner name");
        cellAc.setCellValue(" Contact Person name");
        cellAd.setCellValue("Mobile Number");
        cellAe.setCellValue("Email id");
        cellAf.setCellValue("Address");
        cellAg.setCellValue("GST Registered(yes/no)");
        cellAh.setCellValue("GSTIN");
        cellAi.setCellValue(" Place of Supply");

        if(vendorLists.size()>0)
        {

            for (Record record:vendorLists)
            {
                HSSFRow rowA = firstSheet.createRow(i);
                HSSFCell cellA = rowA.createCell(0);
                HSSFCell cellB = rowA.createCell(1);
                HSSFCell cellc = rowA.createCell(2);
                HSSFCell celld = rowA.createCell(3);
                HSSFCell celle = rowA.createCell(4);
                HSSFCell cellf = rowA.createCell(5);
                HSSFCell cellg = rowA.createCell(6);
                HSSFCell cellh = rowA.createCell(7);
                HSSFCell celli = rowA.createCell(8);


                cellA.setCellValue(record.getCompany());
                cellB.setCellValue(record.getOwnerName());
                cellc.setCellValue(record.getContactPerson());
                celld.setCellValue(record.getMobileNo());
                celle.setCellValue(record.getEmailId());
                cellf.setCellValue(record.getAddress());
                cellg.setCellValue(record.getGstDetails());
                cellh.setCellValue(record.getGstinNo());

                celli.setCellValue(record.getState());





                i++;



            }
        }
        FileOutputStream fos = null;
        try {

            File sdCard =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File directory = new File (sdCard.getAbsolutePath() + "/Vendors/"+"Export");
            directory.mkdirs();
            File file = new File(directory, "vendors.xls");
//            String str_path = Environment.getExternalStorageDirectory().toString();
//            File file ;
            //  file = new File(str_path, fileName + ".xls");
            fos = new FileOutputStream(file);
            workbook.write(fos);
            workbook.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(applicationContext, "Excel Sheet Generated", Toast.LENGTH_SHORT).show();
        }




}

    public void getItemDeatils(Context applicationContext, String items, ArrayList<StockInfo> stockInfoArrayList)
    {
        Log.d(TAG, "getItemDeatils: "+stockInfoArrayList.toString());

        int i=1;

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet firstSheet = workbook.createSheet("Sheet1");
        //HSSFSheet secondSheet = workbook.createSheet("Sheet2");
        HSSFRow rowAA = firstSheet.createRow(0);
        HSSFCell cellAA = rowAA.createCell(0);
        HSSFCell cellAB = rowAA.createCell(1);
        HSSFCell cellAc = rowAA.createCell(2);
        HSSFCell cellAd = rowAA.createCell(3);
        HSSFCell cellAe = rowAA.createCell(4);

        HSSFCell cellAf = rowAA.createCell(5);
        HSSFCell cellAg = rowAA.createCell(6);
        HSSFCell cellAh = rowAA.createCell(7);


        HSSFCell cellAi = rowAA.createCell(8);
        HSSFCell cellAj = rowAA.createCell(9);
        HSSFCell cellAk = rowAA.createCell(10);


        HSSFCell cellAl = rowAA.createCell(11);
        HSSFCell cellAm = rowAA.createCell(12);
        HSSFCell cellAn = rowAA.createCell(13);


        HSSFCell cellAo = rowAA.createCell(14);
        HSSFCell cellAp = rowAA.createCell(15);
        HSSFCell cellAq = rowAA.createCell(16);

        HSSFCell cellAr = rowAA.createCell(17);
        HSSFCell cellAs = rowAA.createCell(18);

        cellAA.setCellValue("Product type");
        cellAB.setCellValue("Item Name");
        cellAc.setCellValue("Measurment Unit");
        cellAd.setCellValue("Item code/Barcode");
        cellAe.setCellValue("HSN ");
        cellAf.setCellValue("Product Company");
        cellAg.setCellValue("Product Owner/Author");
        cellAh.setCellValue("Specification");

      cellAi.setCellValue("Storage Qty");
        cellAj.setCellValue("Stock Qty");
        cellAk.setCellValue("Purchase Price");

        cellAl.setCellValue("MRP");
        cellAm.setCellValue("Unit Price");
        cellAn.setCellValue("Unit ");

        cellAo.setCellValue("Discount in Rs");
        cellAp.setCellValue("Discount in %");
        cellAq.setCellValue("Discounted Price");

        cellAr.setCellValue("GST %");
        cellAs.setCellValue("Price of Item");


        if(stockInfoArrayList.size()>0)
        {

            for (StockInfo record:stockInfoArrayList)
            {
                HSSFRow rowA = firstSheet.createRow(i);
                HSSFCell cellA = rowA.createCell(0);
                HSSFCell cellB = rowA.createCell(1);
                HSSFCell cellc = rowA.createCell(2);
                HSSFCell celld = rowA.createCell(3);
                HSSFCell celle = rowA.createCell(4);
                HSSFCell cellf = rowA.createCell(5);
                HSSFCell cellg = rowA.createCell(6);
                HSSFCell cellh = rowA.createCell(7);


                HSSFCell celli = rowA.createCell(8);
                HSSFCell cellj = rowA.createCell(9);
                HSSFCell cellk = rowA.createCell(10);
                HSSFCell celll = rowA.createCell(11);
                HSSFCell cellm = rowA.createCell(12);
                HSSFCell celln = rowA.createCell(13);
                HSSFCell cello = rowA.createCell(14);
                HSSFCell cellp = rowA.createCell(15);

                HSSFCell cellq = rowA.createCell(16);
                HSSFCell cellr = rowA.createCell(17);
                HSSFCell cells = rowA.createCell(18);


                cellA.setCellValue(record.getProduct_type());
                cellB.setCellValue(record.getItem_name());
                cellc.setCellValue(record.getMunit());
                celld.setCellValue(Double.parseDouble(record.getItembarcode()));
                celle.setCellValue(Double.parseDouble(record.getHsn_ssc_code()));
                cellf.setCellValue(record.getCompany());
                cellg.setCellValue(record.getOwner());
                cellh.setCellValue(record.getSpecification());
                celli.setCellValue(Double.parseDouble(record.getStorage_qty()));
                cellj.setCellValue(Double.parseDouble(record.getStock_qty()));
                cellk.setCellValue(Double.parseDouble(record.getO_price()));
                celll.setCellValue(Double.parseDouble(record.getO_mrp()));
                cellm.setCellValue(Double.parseDouble(record.getUnitPrice()));
                celln.setCellValue(record.getUnit());
                cello.setCellValue((Double.parseDouble(record.getDiscount())));
                cellp.setCellValue(Double.parseDouble(record.getDisCountPer()));
                cellq.setCellValue(Double.parseDouble(record.getDisPrice()));

                cellr.setCellValue(Double.parseDouble(record.getGst()));
                cells.setCellValue(Double.parseDouble(record.getTotalPrice()));





                i++;



            }
        }
        FileOutputStream fos = null;
        try {

            File sdCard =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File directory = new File (sdCard.getAbsolutePath() + "/Items/"+"Export");
            directory.mkdirs();
            File file = new File(directory, "Items.xls");
//            String str_path = Environment.getExternalStorageDirectory().toString();
//            File file ;
            //  file = new File(str_path, fileName + ".xls");
            fos = new FileOutputStream(file);
            workbook.write(fos);
            workbook.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(applicationContext, "Excel Sheet Generated", Toast.LENGTH_SHORT).show();
        }



    }

    public void getMenuItemDeatils(Context applicationContext, String items, ArrayList<StockInfo> stockInfoArrayList)
    {
        Log.d(TAG, "getMenuItemDeatils: "+stockInfoArrayList);


        int i=1;

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet firstSheet = workbook.createSheet("Sheet1");
        //HSSFSheet secondSheet = workbook.createSheet("Sheet2");
        HSSFRow rowAA = firstSheet.createRow(0);
        HSSFCell cellAA = rowAA.createCell(0);
        HSSFCell cellAB = rowAA.createCell(1);
        HSSFCell cellAc = rowAA.createCell(2);
        HSSFCell cellAd = rowAA.createCell(3);
        HSSFCell cellAe = rowAA.createCell(4);

        HSSFCell cellAf = rowAA.createCell(5);
        HSSFCell cellAg = rowAA.createCell(6);
        HSSFCell cellAh = rowAA.createCell(7);


        HSSFCell cellAi = rowAA.createCell(8);
        HSSFCell cellAj = rowAA.createCell(9);
        HSSFCell cellAk = rowAA.createCell(10);


        HSSFCell cellAl = rowAA.createCell(11);
        HSSFCell cellAm = rowAA.createCell(12);
        HSSFCell cellAn = rowAA.createCell(13);



        cellAA.setCellValue("Product type");
        cellAB.setCellValue("Item Name");
        cellAc.setCellValue("Measurment Unit");
        cellAd.setCellValue("Item code/Barcode");
        cellAe.setCellValue("HSN ");
        cellAf.setCellValue("Description");
        cellAg.setCellValue("Unit Price");
        cellAh.setCellValue("Unit");

        cellAi.setCellValue("Discount in Rs");
        cellAj.setCellValue("Discount in %");
        cellAk.setCellValue("Discounted Price");

        cellAl.setCellValue("GST %");
        cellAm.setCellValue("Price of Item");




        if(stockInfoArrayList.size()>0)
        {

            for (StockInfo record:stockInfoArrayList)
            {
                HSSFRow rowA = firstSheet.createRow(i);
                HSSFCell cellA = rowA.createCell(0);
                HSSFCell cellB = rowA.createCell(1);
                HSSFCell cellc = rowA.createCell(2);
                HSSFCell celld = rowA.createCell(3);
                HSSFCell celle = rowA.createCell(4);
                HSSFCell cellf = rowA.createCell(5);
                HSSFCell cellg = rowA.createCell(6);
                HSSFCell cellh = rowA.createCell(7);


                HSSFCell celli = rowA.createCell(8);
                HSSFCell cellj = rowA.createCell(9);
                HSSFCell cellk = rowA.createCell(10);
                HSSFCell celll = rowA.createCell(11);
                HSSFCell cellm = rowA.createCell(12);


                cellA.setCellValue(record.getProduct_type());
                cellB.setCellValue(record.getItem_name());
                cellc.setCellValue(record.getMunit());
                celld.setCellValue(Double.parseDouble(record.getItembarcode()));
                celle.setCellValue(Double.parseDouble(record.getHsn_ssc_code()));
                cellf.setCellValue(record.getSpecification());
                cellg.setCellValue(Double.parseDouble(record.getUnitPrice()));


                cellh.setCellValue(record.getUnit());

                celli.setCellValue(Double.parseDouble(record.getDiscount()));
                cellj.setCellValue(Double.parseDouble(record.getDisCountPer()));
                cellk.setCellValue(Double.parseDouble(record.getDisPrice()));

                celll.setCellValue(Double.parseDouble(record.getGst()));
                cellm.setCellValue(Double.parseDouble(record.getTotalPrice()));





                i++;



            }
        }
        FileOutputStream fos = null;
        try {

            File sdCard =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File directory = new File (sdCard.getAbsolutePath() + "/Menu/"+"Export");
            directory.mkdirs();
            File file = new File(directory, "menu.xls");
//            String str_path = Environment.getExternalStorageDirectory().toString();
//            File file ;
            //  file = new File(str_path, fileName + ".xls");
            fos = new FileOutputStream(file);
            workbook.write(fos);
            workbook.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(applicationContext, "Excel Sheet Generated", Toast.LENGTH_SHORT).show();
        }
    }
}
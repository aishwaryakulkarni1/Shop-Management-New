package com.inevitablesol.www.shopmanagement.analysis.date;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.Preference;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.BuildConfig;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.sales.SaleInfo;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.ItemArray;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.ItemArrayGst;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.Records;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.RecordsGst;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Pritam on 21-12-2017.
 */

public class PdfXml_ extends AppCompatActivity
{
    private static final String TAG = "PdfXml_";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
            Font.BOLD|Font.UNDERLINE);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 8,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 8,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
            Font.BOLD);
    private static Font small = new Font(Font.FontFamily.TIMES_ROMAN, 8,
            Font.NORMAL);

    Font font1 = new Font(Font.FontFamily.HELVETICA  , 25, Font.BOLD);
    Font font2 = new Font(Font.FontFamily.COURIER    , 18,
            Font.ITALIC | Font.UNDERLINE);
    Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 27);
    private boolean pdfReadWrite;
     private  Context context;
    private static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences sharedpreferences;

    private  int totalQty;


    public  PdfXml_(Context context ,SharedPreferences sharedpreferences)
    {
        this.context=context;
        this.sharedpreferences = sharedpreferences;
    }

    public PdfXml_()
    {

    }


    public  void openFile(String s, Context context, String total_sale)
    {
//        try
//        {
        File xls = new File(Environment.getExternalStorageDirectory()+ "/SaleReports/"+total_sale+"/" + s);
        Log.d(TAG, "openXLS: XLS"+xls);
        Log.d(TAG, "openXLS: File Name"+s);
//        Uri path = Uri.fromFile(xls);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(path,"application/vnd.ms-excel");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            this.startActivity(intent);
//        } catch (ActivityNotFoundException e)
//        {
//            Log.d(TAG, "openXLS:"+e);
//            Toast.makeText(context, "No Application available to view XLS", Toast.LENGTH_SHORT).show();
//        }

        //  File xls = new File(Environment.getExternalStorageDirectory()+ "/SaleReports/" + "TotalSale.xls");
        try
        {

         if(s.contains(".xls"))
         {
             Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",xls);
             Intent intent = new Intent(Intent.ACTION_VIEW);
             intent.setDataAndType(path,"application/vnd.ms-excel");

             intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
             context. startActivity(intent);
             //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

         }else
         {
             Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",xls);
             Intent intent = new Intent(Intent.ACTION_VIEW);
             intent.setDataAndType(path,"application/*");

             intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
             context. startActivity(intent);

         }




        } catch (ActivityNotFoundException e)
        {
            Toast.makeText(context, "No Application available to view XLS", Toast.LENGTH_SHORT).show();
        }

    }


    public void shareFile(String fileName, Context context, String total_sale)
    {

        Log.d(TAG, "onClick:Share");
        Intent intentFile = new Intent(Intent.ACTION_SEND);
        File file = new File(Environment.getExternalStorageDirectory()+ "/SaleReports/"+total_sale+"/" + fileName);

        if(file.exists())
        {
            intentFile.setType("application/xls");
            intentFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));
            intentFile.putExtra(Intent.EXTRA_SUBJECT, "Sharing File...");
            intentFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
            context.startActivity(Intent.createChooser(intentFile, "Share File"));
        }else
        {
            Toast.makeText(context, " File Not Available", Toast.LENGTH_SHORT).show();
        }

    }


    public   boolean _CreatePdf(String fname, Context fcontent, String Dir, ArrayList<SaleInfo> saleInfos)
    {

        FileOutputStream fos = null;
        try
        {
            ImageBuilders imgBulider=new ImageBuilders();
            File sdCard = Environment.getExternalStorageDirectory();

            File directory = new File(sdCard.getAbsolutePath() + "/SaleReports/" + Dir);
            directory.mkdirs();
            File file = new File(directory, fname + ".pdf");
            fos = new FileOutputStream(file);
            Document document = new Document();
            PdfWriter.getInstance(document, fos);
            document.open();

            // String uri = "@drawable/cashlessheading";  // where myresource (without the extension) is the file

       /*  int imageResource = getResources().getIdentifier(uri, null, getPackageName());
          Drawable res = getResources().getDrawable(imageResource);
            Log.d(TAG, "_CreatePdf:res"+res);
           String name= getResources().getResourceName(R.drawable.cashlessheading)+".png";

            Log.d(TAG, "_CreatePdf  : "+name);

           Log.d(TAG, "_CreatePdf: +"+Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/" + R.drawable.cashlessheading));
            Uri image1=Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/" + R.drawable.cashlessheading+".png");
            Log.d(TAG, "_CreatePdf: Uri"+image1.toString());*/

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pdf_header);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.pdf_header_second);
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
            byte[] bitmapdata2 = stream2.toByteArray();

            Image image=Image.getInstance(bitmapdata);
            Image image2=Image.getInstance(bitmapdata2);
            document.add(image);
            document.add(image2);
            addEmptyLine(new Paragraph(" "),1);
            // Image image2=Image.getInstance(imgBulider.getBitmapArray(R.drawable.second_page_pdg));
            // document.add(image2);
            addTitlePage(document,saleInfos);
            // close document
            document.newPage();
            createPageThree(document);
            document.close();
//            return true;
            pdfReadWrite=false;

            Toast.makeText(fcontent, "Pdf generated Successfully", Toast.LENGTH_SHORT).show();

        } catch (IOException e)
        {
            Toast.makeText(fcontent,""+ e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (DocumentException e)
        {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return true;
    }

    private void createPageThree(Document document)
            throws DocumentException
    {
        PdfPTable table = new PdfPTable(2);// 3 columns.
        table.setWidthPercentage(100);

        PdfPCell cell1 = new PdfPCell();
        PdfPCell cell2 = new PdfPCell(new Paragraph("Cell 2"));

        cell1.setBackgroundColor(BaseColor.GRAY);
        cell2.setBackgroundColor(BaseColor.GRAY);



        table.addCell(cell1);
        table.addCell(cell2);

        document.add(table);

    }


    private static void addTitlePage(Document document, ArrayList<SaleInfo> saleInfos)
            throws DocumentException
    {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        Chunk chunk=new Chunk("Shop/company Details",catFont);
        chunk.setUnderline(0.1f, -2f);
        document.add(chunk);
        preface.add(new Chunk("Name :",subFont));
        preface.add(new Chunk("test Shop ", small));
        addEmptyLine(preface, 1);
        preface.add(new Chunk("Address :", subFont));
        preface.add(new Chunk("Address :", small));
        addEmptyLine(preface, 1);
        preface.add(new Chunk("Contact No :", subFont));
        preface.add(new Chunk(" 8421477111:", small));
        addEmptyLine(preface, 1);
        preface.add(new Chunk("Email :", subFont));
        preface.add(new Chunk("Test_email@gmail.com",small));
        addEmptyLine(preface, 1);
        preface.add(new Chunk("GSTIN :", subFont));
        //preface=new Paragraph();
        preface.add(new Chunk("AAAAookmjSS",small));
        addEmptyLine(preface, 1);


        // Paragraph paragraph = new Paragraph("This is right aligned text");
        // preface.setAlignment(Element.ALIGN_RIGHT);
        //  paragraph.setIndentationLeft(50);
        //addEmptyLine(preface, 1);
        preface.add(new Paragraph("---------------------------------"));
        preface.add(new Paragraph("Sale Reports For [Payment Type -(Trans Percentage) ],for period [dd/mm/yy] to [dd/mm/yyy] :",smallBold));

        PdfPTable table = new PdfPTable(9);
        PdfPCell c1 = new PdfPCell(new Phrase("Trans"));
        //  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GREEN);
        //  c1.setColspan(2);
        table.addCell(c1);
        table.setWidthPercentage(100);

        c1 = new PdfPCell(new Phrase("Inv no"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GREEN);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Taxable Value"));
        c1.setBackgroundColor(BaseColor.GREEN);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Total GST"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GREEN);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Total Amount"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GREEN);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Amount  Paid "));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GREEN);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Balance "));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        c1.setBackgroundColor(BaseColor.GREEN);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Payment Mode"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GREEN);
        table.addCell(c1);
        c1 = new PdfPCell(new Phrase("Status"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GREEN);
        table.addCell(c1);



        //table.setHeaderRows(1);
        for (SaleInfo saleInfo:saleInfos)
        {
            table.addCell(saleInfo.getCreated_Date());
            table.addCell(saleInfo.getInvoice_id());
            table.addCell(saleInfo.getTaxable_value());
            table.addCell(saleInfo.getTotal_gst());
            table.addCell(saleInfo.getTotalAmount());
            table.addCell(saleInfo.getAmountPaid());
            table.addCell(saleInfo.getBalanceDue());
            table.addCell(saleInfo.getModeOfPayment());
            table.addCell(saleInfo.getStatus());

        }
        preface.add(table);
        document.add(preface);



        //document.add(paragraph);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private  void createTable(Document document, Records recordses, SaleInfo saleInfos)
            throws DocumentException
    {
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        table.setSpacingAfter(10f);
        float[] columnWidths = {1f, 2f, 1f,1f,1f,1f,1f};

       table.setWidths(columnWidths);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Sr.no"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Description of Goods/ Service"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("HSN/SAC"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("UOM"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Quantity"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Unit Price"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Amount"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);
        int i =1;
        for (ItemArray itemArray :recordses.getItemArray())
        {
            table.addCell(String.valueOf(i));
            table.addCell(itemArray.getItemName());
            table.addCell(itemArray.getHsn_code());
            table.addCell("");
            totalQty+=Integer.parseInt(itemArray.getQty());
            table.addCell(itemArray.getQty());
            table.addCell(String.valueOf(Double.parseDouble(itemArray.getTotalPrice())/Integer.parseInt(itemArray.getQty())));
            table.addCell(itemArray.getTotalPrice());
            i++;


        }

    /*    table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");
        table.addCell("2.3");
        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");
        table.addCell("2.3");
        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");
        table.addCell("2.3");*/



        try
        {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

    private static void createList(Section subCatPart)
    {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }

    public  boolean invoicePdf(String fname, Context fcontent, String Dir, SaleInfo saleInfos, Records recordses)
    {

        Log.d(TAG, "invoicePdf:");
        FileOutputStream fos = null;

        try
        {

            File sdCard = Environment.getExternalStorageDirectory();

            File directory = new File(sdCard.getAbsolutePath() + "/SaleReports/" + Dir);
            directory.mkdirs();
            File file = new File(directory, fname + ".pdf");
            fos = new FileOutputStream(file);
            Log.d(TAG, "invoicePdf: "+file);
            Document document = new Document();
            PdfWriter.getInstance(document, fos);
            document.open();

            try
            {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.invoice_header);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                Image image=Image.getInstance(bitmapdata);
                document.add(image);



            } catch (Exception e)
            {
                Log.d(TAG, "invoicePdf:Exception"+e);
                e.printStackTrace();
            }

           // addEmptyLine(new Paragraph(" "),1);
            AddPage(document,saleInfos,recordses);
//            addEmptyLine(new Paragraph(" "),2);
            createTable(document,recordses,saleInfos);
            addSecondBlock(document,recordses,saleInfos);


            document.close();
//            return true;
            pdfReadWrite=false;

            Toast.makeText(fcontent, "Pdf generated Successfully", Toast.LENGTH_SHORT).show();

        } catch (IOException e)
        {
            Log.d(TAG, "invoicePdf: Exception"+e);
            Toast.makeText(fcontent,""+ e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (DocumentException e)
        {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return true;


    }
    public  boolean AddPage(Document document, SaleInfo saleInfos, Records recordses) throws DocumentException {

        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        //chunk.setUnderline(1f, 20f);
        preface.add(new Paragraph("Seller Details",catFont));
         preface.add(new Chunk("Name : ",subFont));
        preface.add(new Chunk("      "));
        preface.add(new Chunk(sharedpreferences.getString("username",null), small));
        addEmptyLine(preface, 1);
        preface.add(new Chunk("Address : ", subFont));
        preface.add(new Chunk("      "));
        preface.add(new Chunk(sharedpreferences.getString("address",null), small));
        addEmptyLine(preface, 1);
        preface.add(new Chunk("Contact No :", subFont));
        preface.add(new Chunk("      "));
        preface.add(new Chunk(sharedpreferences.getString("usermobile",null), small));
        addEmptyLine(preface, 1);
        preface.add(new Chunk("Email :", subFont));
        preface.add(new Chunk("      "));
        preface.add(new Chunk(sharedpreferences.getString("useremail",null),small));
        addEmptyLine(preface, 1);
        preface.add(new Chunk("GSTIN :", subFont));
        preface.add(new Chunk("      "));
        //preface=new Paragraph();
        preface.add(new Chunk(sharedpreferences.getString("gstNo",null),small));
      //  addEmptyLine(preface, 1);
        Paragraph preface1 = new Paragraph();
      //  preface.setAlignment(Element.ALIGN_CENTER);
        preface1.add(new Chunk("State:", subFont));
        //preface=new Paragraph();
        preface1.add(new Chunk("      "));
        preface1.add(new Chunk(sharedpreferences.getString("state",null),small));
      //  preface1.add(new Chunk("State Code :", subFont));
        //preface=new Paragraph();
        //preface1.add(new Chunk("27",small));
        addEmptyLine(preface, 1);
        document.add(preface);
        document.add(preface1);

        Phrase phrase=new Phrase();
        phrase.add(new LineSeparator(2f,100f,BaseColor.ORANGE,1,10f));
        document.add(phrase);

        Paragraph pre = new Paragraph();
        document.add(new Paragraph("Bill To",catFont));
        pre.add(new Chunk("Name : ",subFont));
        pre.add(new Chunk(recordses.getCustomerName(), small));
        addEmptyLine(pre, 1);
        pre.add(new Chunk("Contact No :", subFont));
        pre.add(new Chunk(recordses.getMobileNumber(), small));
        addEmptyLine(pre, 1);
        pre.add(new Chunk("Email :", subFont));
        pre.add(new Chunk(recordses.getEmailId(),small));
        addEmptyLine(pre, 1);

        pre.add(new Chunk("GSTIN :", subFont));
        //preface=new Paragraph();
        pre.add(new Chunk("AAAAookmjSS",small));
        //  addEmptyLine(preface, 1);
        //  preface.setAlignment(Element.ALIGN_CENTER);
        pre.add(new Chunk("     "));
        pre.add(new Chunk("State:", subFont));
        //preface=new Paragraph();
        pre.add(new Chunk("Maharastra",small));
        pre.add(new Chunk("     "));
        pre.add(new Chunk("State Code :", subFont));
        //preface=new Paragraph();
        pre.add(new Chunk("27",small));

        document.add(pre);
        return false;
    }


    public  void  addSecondBlock(Document document, Records recordses, SaleInfo saleInfos) throws DocumentException
    {
        Paragraph preface = new Paragraph();
        preface.add(new Chunk("Total Qty :",catFont));
        preface.add(new Chunk(String.valueOf(totalQty)));
        addEmptyLine(preface,1);
        preface.add(new Chunk("Payment Type & Mode : ",subFont));
        preface.add(new Chunk(recordses.getModeOfPayment()));
        document.add(preface);
        document.add(new Paragraph("Terms and Conditions",catFont));
        document.add(new Paragraph("1.",catFont));
        document.add(new Paragraph("2.",catFont));
        document.add(new Paragraph("3.",catFont));


        createItemTable(document,recordses,saleInfos);

        document.add(new Paragraph("Shop Name : ",subFont));




        try
        {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.inv_bottom);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            Image image=Image.getInstance(bitmapdata);
            document.add(image);

//            Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.withoutgst_footer);
//            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
//            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
//            byte[] bitmapdata2 = stream2.toByteArray();
//            Image image2=Image.getInstance(bitmapdata2);
//            document.add(image2);
        } catch (Exception e)
        {
            Log.d(TAG, "invoicePdf:Exception"+e);
            e.printStackTrace();
        }



    }

    private void createItemTable(Document document, Records recordses, SaleInfo saleInfos) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(40);
       // table.setWidthPercentage(100);
       // table.setSpacingBefore(10f);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.setSpacingAfter(10f);
        //float[] columnWidths = {1f, 2f, 1f,1f,1f,1f,1f};

       // table.setWidths(columnWidths);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Sub Total"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        //c1.setNoWrap(false);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(saleInfos.getTaxable_value())));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);


         c1 = new PdfPCell(new Phrase("Discount "));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        //c1.setNoWrap(false);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(0.0)));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

         c1 = new PdfPCell(new Phrase("Shipping Charge"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
       // c1.setNoWrap(false);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(recordses.getShippingCharges())));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

         c1 = new PdfPCell(new Phrase("Other Charges"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
       // c1.setNoWrap(false);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(recordses.getOtherCharges())));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

         c1 = new PdfPCell(new Phrase(" Total"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.ORANGE);
      //  c1.setNoWrap(false);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(recordses.getTotal())));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBackgroundColor(BaseColor.ORANGE);

        table.addCell(c1);

         c1 = new PdfPCell(new Phrase("Amount Paid"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GREEN);

      //  c1.setNoWrap(false);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(recordses.getAmountPaid())));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBackgroundColor(BaseColor.GREEN);
        table.addCell(c1);


         c1 = new PdfPCell(new Phrase("Balance Due"));
        c1.setBackgroundColor(BaseColor.RED);
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
       // c1.setNoWrap(false);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(recordses.getBalanceDue())));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBackgroundColor(BaseColor.RED);
        table.addCell(c1);


        document.add(table);

    }


    public boolean invoicePdf_gst(String fname, Context fcontent, String Dir, ArrayList<SaleInfo> saleInfos, RecordsGst recordses)
    {
        Log.d(TAG, "invoicePdf:");

        FileOutputStream fos = null;

        try
        {

            File sdCard = Environment.getExternalStorageDirectory();

            File directory = new File(sdCard.getAbsolutePath() + "/SaleReports/" + Dir);
            directory.mkdirs();
            File file = new File(directory, fname + ".pdf");
            fos = new FileOutputStream(file);
            Log.d(TAG, "invoicePdf: "+file);
            Document document = new Document();
            PdfWriter.getInstance(document, fos);
            document.open();
            try
            {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.invoice_header);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                Image image=Image.getInstance(bitmapdata);
                document.add(image);

            } catch (Exception e)
            {
                Log.d(TAG, "invoicePdf : Exception"+e);
                e.printStackTrace();
            }
              addPage(document,saleInfos,recordses);
              createGstPdf_Table(document,recordses,saleInfos);
              gstLastPage(document,recordses,saleInfos);

            // addEmptyLine(new Paragraph(" "),1);
           //AddPage(document,saleInfos,recordses);
//            addEmptyLine(new Paragraph(" "),2);
         //  createTable(document,recordses);
         //   addSecondBlock(document,recordses);

            document.close();
//            return true;
            pdfReadWrite=false;

            Toast.makeText(fcontent, "Pdf generated Successfully", Toast.LENGTH_SHORT).show();

        } catch (IOException e)
        {
            Log.d(TAG, "invoicePdf: Exception"+e);
            Toast.makeText(fcontent,""+ e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (DocumentException e)
        {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return true;
    }






    private void gstLastPage(Document document, RecordsGst recordses, ArrayList<SaleInfo> saleInfos) throws DocumentException
    {

        Paragraph preface = new Paragraph();
     //   document.add(new Paragraph("Seller Details",catFont));
//        preface.setIndentationLeft(15);
//        preface.add(new Chunk("Invoice Value: ",subFont));
//        preface.add(new Chunk(new Chunk(sharedpreferences.getString("username",null), small)));
//        addEmptyLine(preface,1);
//        preface.add(new Chunk("Payment Mode : ", subFont));
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(45);
        // table.setWidthPercentage(100);
        // table.setSpacingBefore(10f);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);

        //table.setSpacingAfter(10f);
        //float[] columnWidths = {1f, 2f, 1f,1f,1f,1f,1f};

        // table.setWidths(columnWidths);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Grand Total(Inclusive Gst)"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        //c1.setNoWrap(false);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(recordses.getTotal())));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);


        c1 = new PdfPCell(new Phrase("Other Charges"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        // c1.setNoWrap(false);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(recordses.getOtherCharges())));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);
        c1 = new PdfPCell(new Phrase("Shipping Charge"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        // c1.setNoWrap(false);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(recordses.getSgst())));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(" Total"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.ORANGE);
        //  c1.setNoWrap(false);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(recordses.getTotal())));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBackgroundColor(BaseColor.ORANGE);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Amount Paid"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GREEN);

        //  c1.setNoWrap(false);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(recordses.getAmountPaid())));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBackgroundColor(BaseColor.GREEN);
        table.addCell(c1);


        c1 = new PdfPCell(new Phrase("Balance Due"));
        c1.setBackgroundColor(BaseColor.RED);
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        // c1.setNoWrap(false);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(recordses.getBalanceDue())));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBackgroundColor(BaseColor.RED);
        table.addCell(c1);
         preface.add(table);
      //  document.add(preface);
        preface.setIndentationLeft(15);
        preface.add(new Chunk("Invoice Value: ",subFont));
        preface.add(new Chunk(new Chunk(sharedpreferences.getString("username",null), small)));
        addEmptyLine(preface,1);
        preface.add(new Chunk("Payment Mode : ", subFont));
        preface.add(new Chunk(new Chunk(recordses.getModeOfPayment(), small)));
        document.add(preface);
        document.add(new Paragraph("Terms and Conditions",catFont));
        document.add(new Paragraph("1.",catFont));
        document.add(new Paragraph("2.",catFont));
        document.add(new Paragraph("3.",catFont));
        addEmptyLine(preface,2);

        try
        {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.inv_bottom);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            Image image=Image.getInstance(bitmapdata);

//            Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.payment_mode_footer);
//            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
//            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
//            byte[] bitmapdata1 = stream1.toByteArray();
//            Image image1=Image.getInstance(bitmapdata1);
            Paragraph preference=new Paragraph();
//            preference.add(new Chunk(image1,0,50));
//            preference.add(new Chunk(image,0,50));
            preference.add(image);
            document.add(preference);

        } catch (Exception e)
        {
            Log.d(TAG, "invoicePdf:Exception"+e);
            e.printStackTrace();
        }

    }

    private void createGstPdf_Table(Document document, RecordsGst recordsesClass, ArrayList<SaleInfo> saleInfos) throws DocumentException
    {
        Log.d(TAG, "createGstPdf_Table: ");
        PdfPTable table = new PdfPTable(11);
        table.setWidthPercentage(100);
        //table.setSpacingBefore(10f);
//
        table.setSpacingAfter(5f);
        float[] columnWidths = {1f, 2f, 1f,1f,1f,1f,1f,1f,1f,1f,1f};

        table.setWidths(columnWidths);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Sr.no"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Description of Goods/ Service"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("HSN\nSAC"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Qty\nUnit"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Rate"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Total Value"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        c1 = new PdfPCell(new Phrase("Dis"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);


        c1 = new PdfPCell(new Phrase("Total Value"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("CGST"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("SGST"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        c1 = new PdfPCell(new Phrase("IGST"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        int i =1;
        for (ItemArrayGst itemArray :recordsesClass.getItemArrayGst())
        {
            table.addCell(String.valueOf(i));
            table.addCell(itemArray.getItemName());
            table.addCell(itemArray.getHsnCode());
            table.addCell(itemArray.getQty());
            table.addCell(String.valueOf(Double.parseDouble(itemArray.getTotalPrice())/Integer.parseInt(itemArray.getQty())));
            table.addCell(recordsesClass.getTaxableValue());
            table.addCell(" ");
            table.addCell(itemArray.getTotalPrice());
            table.addCell(itemArray.getGst());
            table.addCell(String.valueOf(Double.parseDouble(itemArray.getGst())/2));
            table.addCell(String.valueOf(Double.parseDouble(itemArray.getGst())/2));

            i++;


        }




        try
        {
            document.add(table);


        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void addPage(Document document, ArrayList<SaleInfo> saleInfos, RecordsGst recordses) throws DocumentException
    {
        Paragraph paragraph2 =new Paragraph();
           paragraph2.setAlignment(Element.ALIGN_RIGHT);
           paragraph2.add(new Chunk("Date :",smallBold));
           paragraph2.add(new Chunk("  ",smallBold));
           paragraph2.add(new Chunk(recordses.getCreatedDate(),subFont));
         document.add(paragraph2);

        Paragraph preface = new Paragraph();
        document.add(new Paragraph("Seller Details",catFont));
        addEmptyLine(preface,1);
        preface.setIndentationLeft(15);
        preface.add(new Chunk("Name : ",subFont));
        preface.add(new Chunk(new Chunk(sharedpreferences.getString("username",null), small)));
        addEmptyLine(preface,1);
        preface.add(new Chunk("Address : ", subFont));
//        preface.add(new Chunk("      "));
        preface.add(new Chunk(sharedpreferences.getString("address",null), small));
        addEmptyLine(preface, 1);
        preface.add(new Chunk("Address : ", subFont));
        preface.add(new Chunk("      "));
        preface.add(new Chunk(sharedpreferences.getString("address",null), small));
        addEmptyLine(preface, 1);
        preface.add(new Chunk("Contact No :", subFont));
        preface.add(new Chunk("      "));
        preface.add(new Chunk(sharedpreferences.getString("usermobile",null), small));
        addEmptyLine(preface, 1);
        preface.add(new Chunk("Email :", subFont));
        preface.add(new Chunk("      "));
        preface.add(new Chunk(sharedpreferences.getString("useremail",null),small));
        addEmptyLine(preface, 1);
        preface.add(new Chunk("GSTIN :", subFont));
        preface.add(new Chunk("      "));
        //preface=new Paragraph();
        preface.add(new Chunk(sharedpreferences.getString("gstNo",null),small));
        //  addEmptyLine(preface, 1);
        //  preface.setAlignment(Element.ALIGN_CENTER);
        preface.add(new Chunk("State:", subFont));
        //preface=new Paragraph();
        preface.add(new Chunk("      "));
        preface.add(new Chunk(sharedpreferences.getString("state",null),small));
        //  preface1.add(new Chunk("State Code :", subFont));
        //preface=new Paragraph();
        //preface1.add(new Chunk("27",small));
        addEmptyLine(preface, 1);
        document.add(preface);

        Phrase phrase=new Phrase();
        phrase.add(new LineSeparator(2f,100f,BaseColor.ORANGE,1,10f));
        document.add(phrase);
        //addEmptyLine(phrase, 1);

        Paragraph pre = new Paragraph();
//        addEmptyLine(pre, 1);
        document.add(new Paragraph("Bill To",catFont));
        pre.setIndentationLeft(15);
        pre.add(new Chunk("Name : ",subFont));
        pre.add(new Chunk(recordses.getCustomerName(), small));
        addEmptyLine(pre, 1);
        pre.add(new Chunk("Contact No :", subFont));
        pre.add(new Chunk(recordses.getMobileNumber(), small));
        addEmptyLine(pre, 1);
        pre.add(new Chunk("Email :", subFont));
        pre.add(new Chunk(recordses.getEmailId(),small));
        addEmptyLine(pre, 1);

        pre.add(new Chunk("GSTIN :", subFont));
        //preface=new Paragraph();
        pre.add(new Chunk("AAAAookmjSS",small));
        //  addEmptyLine(preface, 1);
        //  preface.setAlignment(Element.ALIGN_CENTER);
        pre.add(new Chunk("     "));
        pre.add(new Chunk("State:", subFont));
        //preface=new Paragraph();
        pre.add(new Chunk("Maharastra",small));
        pre.add(new Chunk("     "));
        pre.add(new Chunk("State Code :", subFont));
        //preface=new Paragraph();
        pre.add(new Chunk("27",small));
        addEmptyLine(pre, 1);
        document.add(pre);
        Phrase ph=new Phrase();
        ph.add(new LineSeparator(2f,100f,BaseColor.ORANGE,1,10f));
        document.add(ph);
        Paragraph paragraph=new Paragraph();
        paragraph.setIndentationLeft(20);
        //paragraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(paragraph, 1);
        document.add(new Paragraph("Place Of Supply",catFont));

        paragraph.add(new Chunk("State : ",subFont));
        paragraph.add(new Chunk("Maharastra", small));
        paragraph.add(new Chunk("State Code : ",subFont));
        paragraph.add(new Chunk(recordses.getPlaceOfSupply(), small));


        // paragraph.add(new PdfPCell(new Phrase( new Chunk("test"))));
       // document.add(elements);


       // addEmptyLine(pre, 1);
        document.add(paragraph);
        Phrase phrase2=new Phrase();
        phrase2.add(new LineSeparator(2f,100f,BaseColor.ORANGE,1,10f));
        document.add(phrase2);


    }


}

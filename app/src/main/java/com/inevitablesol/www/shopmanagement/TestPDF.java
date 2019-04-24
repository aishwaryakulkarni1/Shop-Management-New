package com.inevitablesol.www.shopmanagement;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.sales.SaleInfo;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.ItemArray;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.Records;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.RecordsGst;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
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
 * Created by Pritam on 30-01-2018.
 */

public class TestPDF
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
    private Context context;
    private static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences sharedpreferences;


    public  TestPDF(Context context ,SharedPreferences sharedpreferences)
    {
        this.context=context;
        this.sharedpreferences = sharedpreferences;
    }


    public  void openFile(String s, Context context, String total_sale)
    {

        File xls = new File(Environment.getExternalStorageDirectory()+ "/SaleReports/"+total_sale+"/" + s);
        Log.d(TAG, "openXLS: XLS"+xls);
        Log.d(TAG, "openXLS: File Name"+s);
        try
        {
            if(s.contains(".xls"))
            {
                Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",xls);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path,"application/vnd.ms-excel");

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context. startActivity(intent);
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
            //createGstPdf_Table(document,recordses,saleInfos);
           // gstLastPage(document,recordses,saleInfos);



            document.close();
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


    private void addPage(Document document, ArrayList<SaleInfo> saleInfos, RecordsGst recordses) throws DocumentException
    {
        Paragraph paragraph2 =new Paragraph();
        paragraph2.setAlignment(Element.ALIGN_RIGHT);
        paragraph2.add(new Chunk("Date :",smallBold));
        paragraph2.add(new Chunk("  ",smallBold));
        paragraph2.add(new Chunk(recordses.getCreatedDate(),subFont));
        document.add(paragraph2);
         createTable(document);


//
//           Paragraph elements =new Paragraph();
//        elements.add(new Chunk("Seller Details",catFont));
//        elements.add(new Chunk("               "));
//        elements.add(new Chunk("Bill To",catFont));
//        document.add(elements);
//        elements.setAlignment(Element.ALIGN_CENTER);
//        document.add(elements);

        Paragraph preface = new Paragraph();
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

        preface.add(new Chunk("State:", subFont));
        preface.add(new Chunk("      "));
        preface.add(new Chunk(sharedpreferences.getString("state",null),small));
        addEmptyLine(preface, 1);
        document.add(preface);

        Phrase phrase=new Phrase();
        phrase.add(new LineSeparator(2f,100f,BaseColor.ORANGE,1,10f));
        document.add(phrase);
        Paragraph pre = new Paragraph();
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
        pre.add(new Chunk("AAAAookmjSS",small));
        pre.add(new Chunk("     "));
        pre.add(new Chunk("State:", subFont));
        pre.add(new Chunk("Maharastra",small));
        pre.add(new Chunk("     "));
        pre.add(new Chunk("State Code :", subFont));
        pre.add(new Chunk("27",small));
        addEmptyLine(pre, 1);
        document.add(pre);
        Phrase ph=new Phrase();
        ph.add(new LineSeparator(2f,100f,BaseColor.ORANGE,1,10f));
        document.add(ph);
        Paragraph paragraph=new Paragraph();
        paragraph.setIndentationLeft(20);
        addEmptyLine(paragraph, 1);
        document.add(new Paragraph("Place Of Supply",catFont));

        paragraph.add(new Chunk("State : ",subFont));
        paragraph.add(new Chunk("Maharastra", small));
        paragraph.add(new Chunk("State Code : ",subFont));
        paragraph.add(new Chunk(recordses.getPlaceOfSupply(), small));

        document.add(paragraph);
        Phrase phrase2=new Phrase();
        phrase2.add(new LineSeparator(2f,100f,BaseColor.ORANGE,1,10f));
        document.add(phrase2);


    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }



    private  void createTable(Document document)
            throws DocumentException
    {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        table.setSpacingAfter(10f);
        float[] columnWidths = {2f, 2f};

        table.setWidths(columnWidths);// 3 columns.

        PdfPCell cell1 = new PdfPCell(new Paragraph("Seller Details"));
        cell1.setBorder(PdfPCell.NO_BORDER);
       //
        Paragraph preParagraphp=new Paragraph();;
        preParagraphp.add(new Chunk("Name : ",subFont));

        preParagraphp.add(new Chunk(new Chunk(sharedpreferences.getString("username",null), small)));



        Paragraph preface = new Paragraph();
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

        preface.add(new Chunk("State:", subFont));
        preface.add(new Chunk("      "));
        preface.add(new Chunk(sharedpreferences.getString("state",null),small));



//        cell1.addElement(preParagraphp);
        PdfPCell cell3 = new PdfPCell(new Paragraph("Bill To"));
        cell3.setBorder(PdfPCell.NO_BORDER);

        table.addCell(cell1);
        table.setHeaderRows(1);

        table.addCell(cell3);
        table.addCell(preface);
        table.addCell(preface);




        try
        {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

}

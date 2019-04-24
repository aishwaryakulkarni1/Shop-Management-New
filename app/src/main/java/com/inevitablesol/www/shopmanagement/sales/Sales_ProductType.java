package com.inevitablesol.www.shopmanagement.sales;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.inevitablesol.www.shopmanagement.DateFormat.ParseDate;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.RegistrationModule.ReadWriteExcelFile;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.analysis.date.ImageBuilders;
import com.inevitablesol.www.shopmanagement.analysis.date.PdfXml_;
import com.inevitablesol.www.shopmanagement.product_info.adapter.ProductAdapter;
import com.inevitablesol.www.shopmanagement.product_info.adapter.ProductParser;
import com.inevitablesol.www.shopmanagement.purchase_module.ItemAdapter;
import com.inevitablesol.www.shopmanagement.purchase_module.ItemParser;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.InvocieGstRecords;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.InvocieWithGst;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sales_ProductType extends AppCompatActivity
{

    private Context context=Sales_ProductType.this;
    private static final String TAG = "Sales_ProductType";
    private  SharedPreferences sharedpreferences;
    public  final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private Spinner mProductSpinner;
    private EditText mName;
    private static final String GET_PRODUCT = " http://35.161.99.113:9000/webapi/product/list";


    private static final String GET_SALSE ="http://35.161.99.113:9000/webapi/sale/searchByProductType";

    private Spinner s_productType, s_itemTypem;
    private String ITEMNAME;

    private  SaleDetailAdapter saleDetailAdapter;

    private RecyclerView recyclerView;

    private ArrayList<SaleInfo> saleInfos;
    private ImageView imageView_downLoad;

    private  TextView txt_totalAmount;
    private  TextView txt_totalBalnce;


    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);
    private boolean pdfReadWrite;
    ReadWriteExcelFile readWriteExcelFile = new ReadWriteExcelFile();
    PdfXml_ pdfXml_;

    private static final String MySETTINGS = "MySetting";
    private SharedPreferences sharedpreferences2;
    private boolean Gst_Status;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales__product_type);
        s_productType = (Spinner) findViewById(R.id.spnn_product_type);
        s_itemTypem = (Spinner) findViewById(R.id.spnn_item_type);
        mProductSpinner = (Spinner) findViewById(R.id.spnn_product);
        recyclerView = (RecyclerView)findViewById(R.id.stock_recyclerView);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences2 = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");

        txt_totalAmount=(TextView)findViewById(R.id.exp_view_totalExp) ;
        txt_totalBalnce=(TextView)findViewById(R.id.txt_balance);

        imageView_downLoad=(ImageView)findViewById(R.id.sale_download_product);
        imageView_downLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                showReportOfProductType();

            }
        });


        try
        {

            String gstStatus=(sharedpreferences2.getString("gst", null));
            Log.d(TAG, "onCreate:gst"+gstStatus);
            if(gstStatus!=null)
            {
                if (gstStatus.equalsIgnoreCase("Yes"))
                {
                    Gst_Status=true;
                    Toast.makeText(context, "Gst On", Toast.LENGTH_SHORT).show();

                } else {
                    Gst_Status=false;
                    Toast.makeText(context, "Gst OFF", Toast.LENGTH_SHORT).show();
                    // linear_gst.setVisibility(View.GONE);

                }
            }

        }catch (NullPointerException e)
        {
            Gst_Status=false;
            Log.d(TAG, "onCreate:error"+e);
        }

        getProduct();

        pdfXml_=new PdfXml_(this,sharedpreferences);

        try
        {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(s_productType);

            // Set popupWindow height to 500px
            popupWindow.setHeight((int)height/2);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {

            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e))
                {

                    int position = rv.getChildAdapterPosition(child);
                    SaleInfo saleInfo = saleInfos.get(position);
                    Log.d(TAG, "onInterceptTouchEvent: Sale Info"+saleInfo);
                    showInvoice(saleInfo);

                }
                return false;

            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e)
            {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
            {

            }
        });

    }



    private void showInvoice(final SaleInfo saleInfo)
    {


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.sale_report_dialog);
        dialog.setTitle("Sale Report");

        final TextView txt_invNumber=(TextView)dialog.findViewById(R.id.inv_number);
        final  TextView  txt_inv_Date=(TextView)dialog.findViewById(R.id.inv_date);
        txt_invNumber.setText(saleInfo.getInvoice_id());
        txt_inv_Date.setText(ParseDate.geDate(saleInfo.getCreated_Date()));
        final AppCompatButton Download=(AppCompatButton)dialog.findViewById(R.id.sale_dateTimeDownload);
        final  AppCompatButton viewReport=(AppCompatButton)dialog.findViewById(R.id.sale_date_view);
        final  AppCompatButton share=(AppCompatButton)dialog.findViewById(R.id.share_total_saleDateTime);

        viewReport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // openXLS();

                pdfXml_.openFile("invoice.pdf",context,"SaleReports");


            }
        });
        final AppCompatButton  cancel=(AppCompatButton)dialog.findViewById(R.id.dialogButtonCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Download.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                try
                {
                    if(Gst_Status)
                    {
                        _getInvoice_details_forGst(saleInfo.getInvoice_id(),saleInfo);
                    }else
                    {
                        _getInvoice_details(saleInfo.getInvoice_id(),saleInfo);
                    }


                } catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });

        share.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try
                {

                   // share_Xls_SingleFile("Report_By_Id.xls");
                    pdfXml_.shareFile("invoice.pdf",context,"SaleReports");







                }catch (Exception e)
                {
                    Log.d(TAG, "onClick:Exception"+e);
                    Toast.makeText(context,"Please Select At Least One Option",Toast.LENGTH_LONG).show();
                }

            }
        });

        dialog.setCancelable(false);
        dialog.show();





    }


    private void _getInvoice_details_forGst(final String invoice_id, SaleInfo saleInfo)
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETGSTINVOCIE_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    loading.dismiss();

                    String resp = response.toString().trim();
                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");

                    if (message.equalsIgnoreCase("Data not available"))
                    {

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    } else
                    {
                        Log.d("Sale Report", resp);
                        InvocieWithGst invocieWithGst=new InvocieWithGst();
                        Gson gson=new Gson();
                        invocieWithGst=gson.fromJson(resp,invocieWithGst.getClass());
                        RecordsGst recordsGsts= invocieWithGst.getRecordsGst();
                        ArrayList<ItemArrayGst> itemArrayGsts= (ArrayList<ItemArrayGst>) recordsGsts.getItemArrayGst();
                        Log.d(TAG, "onResponse: "+itemArrayGsts.get(0).getItemName());

                        try {
                            if(isStoragePermissionGranted())
                            {

                                pdfXml_.invoicePdf_gst("invoice", context, "SaleReports", saleInfos,recordsGsts);
                            }else
                            {
                                Log.d(TAG, "onResponse:False");
                            }

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }


                } catch (Exception e)
                {
                    Log.d("Exception", "" + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(context, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("invoice_id", invoice_id);
                Log.i(TAG, "getParams:Single GST Invocie Deatils"+ params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void share_Xls_SingleFile(String fileName)
    {
        Log.d(TAG, "onClick:Share");
        Intent intentFile = new Intent(Intent.ACTION_SEND);
        File file = new File(Environment.getExternalStorageDirectory()+ "/SaleReports/Single_Sale_Report/" + fileName);

        if(file.exists())
        {
            intentFile.setType("application/xls");
            intentFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));

            intentFile.putExtra(Intent.EXTRA_SUBJECT, "Sharing File...");
            intentFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
            startActivity(Intent.createChooser(intentFile, "Share File"));
        }else
        {
            Toast.makeText(context, " File Not Available", Toast.LENGTH_SHORT).show();
        }


    }


    private void _getInvoice_details(final String invoice_id, final SaleInfo saleInfo)
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETSINGLEINVOICEREPORT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    loading.dismiss();

                    String resp = response.toString().trim();
                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");

                    if (message.equalsIgnoreCase("Data not available"))
                    {

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    } else
                    {
                        Log.d("Sale Report", resp);
                        InvocieGstRecords invocieGstRecords=new InvocieGstRecords();
                        Gson gson=new Gson();
                        invocieGstRecords=gson.fromJson(resp,invocieGstRecords.getClass());
                        Records recordses=invocieGstRecords.getRecords();
                        ArrayList<ItemArray> itemArrays= (ArrayList<ItemArray>) recordses.getItemArray();
                        Log.d(TAG, "onResponse: "+itemArrays.get(0).getItemName());
//                        SaleParser saleParser = new SaleParser(resp);
//                        saleParser.parseJSON();
//                        ArrayList<SaleInfo>  saleInfos = saleParser.prepareSale();
                       /* saleDetailAdapter = new SaleDetailAdapter(saleInfos, Sales_TotalSales.this);
                        recyclerView.setAdapter(saleDetailAdapter);
                        saleDetailAdapter.notifyDataSetChanged();*/

                        try {
                            if(isStoragePermissionGranted())
                            {

                                pdfXml_.invoicePdf("invoice", context, "SaleReports", saleInfo,recordses);
                                // pdfXml_.invoicePdf("invoice",context,"Invoice",saleInfos);
                                // readWriteExcelFile.testFile(context, "Invoice", saleInfos,"Total_Sale");
                                return;

                            }else
                            {
                                Log.d(TAG, "onResponse:False");
                            }

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }


                } catch (Exception e)
                {
                    Log.d("Exception", "" + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(context, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("invoice_id", invoice_id);
                Log.i(TAG, "getParams:Single Invocie Deatils"+ params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void showReportOfProductType()
    {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.total_sale_report_product_type);
         dialog.setCancelable(false);
        s_productType = (Spinner)dialog. findViewById(R.id.spnn_product_type);
        s_itemTypem = (Spinner) dialog.findViewById(R.id.spnn_item_type);
        final RadioGroup radioGroup1=(RadioGroup)dialog.findViewById(R.id.total_Sale);
        final AppCompatButton  cancel=(AppCompatButton)dialog.findViewById(R.id.dialogButtonCancel);
        final  AppCompatButton  share=(AppCompatButton)dialog.findViewById(R.id.sale_productshare);
         share.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View v)
             {

                 int selectedId1 = radioGroup1.getCheckedRadioButtonId();
                 RadioButton radioButton1 = (RadioButton)dialog.findViewById(selectedId1);
                 String  fileFormat1   =     radioButton1.getText().toString().trim();
                 Log.d(TAG, "onClick:Option"+fileFormat1);
                 if(fileFormat1.contains(".xls"))
                 {
                     try
                     {

                         Log.d(TAG, "onClick:Share");
                         Intent intentFile = new Intent(Intent.ACTION_SEND);
                         File file = new File(Environment.getExternalStorageDirectory()+ "/SaleReports/ProductType/" + "ProductSale.xls");

                         if(file.exists())
                         {
                             intentFile.setType("application/xls");
                             intentFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));
                             intentFile.putExtra(Intent.EXTRA_SUBJECT, "Sharing File...");
                             intentFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                             startActivity(Intent.createChooser(intentFile, "Share File"));
                         }else
                         {
                             Toast.makeText(context, " File Not Available", Toast.LENGTH_SHORT).show();
                         }

                     }catch (Exception e)
                     {

                     }
                 }else
                 {
                    pdfXml_.shareFile("ProductSale.pdf",context,"ProductType");
                 }


             }
         });
        final  AppCompatButton viewReport=(AppCompatButton)dialog.findViewById(R.id.total_saleView_product);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final  AppCompatButton download=(AppCompatButton)dialog.findViewById(R.id.total_saleDownload_product);

         download.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                 int selectedId1 = radioGroup1.getCheckedRadioButtonId();
                 RadioButton radioButton1 = (RadioButton)dialog.findViewById(selectedId1);
                 String  fileFormat1   =     radioButton1.getText().toString().trim();
                 Log.d(TAG, "onClick: Option"+fileFormat1);
                 if(fileFormat1.contains(".xls"))
                 {
                     generateReport();
                 }else
                 {
                     Log.d(TAG, "onClick: Else");
                     generateReport();
                     pdfReadWrite=true;
                 }


             }
         });
         getProductForDialogReport();

        viewReport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int selectedId1 = radioGroup1.getCheckedRadioButtonId();
                RadioButton radioButton1 = (RadioButton)dialog.findViewById(selectedId1);
                String  fileFormat1   =     radioButton1.getText().toString().trim();
                Log.d(TAG, "onClick:Option"+fileFormat1);
                if(fileFormat1.contains(".xls"))
                {
                    pdfXml_.openFile("ProductSale.xls",context,"ProductType");

                    //openXLS("ProductSale.xls");
                }else
                {
                    pdfXml_.openFile("ProductSale.pdf",context,"ProductType");
                }






            }
        });
        dialog.show();


    }

    private void openXLS(String filname)
    {


        try
        {
            File xls = new File(Environment.getExternalStorageDirectory()+ "/SaleReports/ProductType/" + filname);
            Uri path = Uri.fromFile(xls);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path,"application/vnd.ms-excel");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e)
        {
            Log.d(TAG, "openXLS:"+e);
            Toast.makeText(context, "No Application available to view XLS", Toast.LENGTH_SHORT).show();
        }

    }

    private void generateReport()
    {

        ReadWriteExcelFile readWriteExcelFile = new ReadWriteExcelFile();
        try {
            if(isStoragePermissionGranted())
            {
                if(pdfReadWrite)
                {
                    Log.d(TAG, "onClickpdf");
                    _CreatePdf("ProductSale",context,"ProductType",saleInfos);
                }
                else
                {
                    readWriteExcelFile.testFile(context, "ProductSale", saleInfos,"ProductType");
                    return;

                }



            }else
            {
                Log.d(TAG, "onResponse:False");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getProductForDialogReport()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_NEW_PRODUCT, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try {
                    Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                    try {

                        JSONObject msg = new JSONObject(response);
                        String message = msg.getString("message");


                        ProductParser productParser = new ProductParser(response);
                        productParser.productParser();
                        ProductAdapter productAdapter = new ProductAdapter(Sales_ProductType.this, R.layout.product_list, ProductParser.productName, ProductParser.productId);
                        s_productType.setAdapter(productAdapter);

                        s_productType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            /**
                             * Called when a new item was selected (in the Spinner)
                             */
                            public void onItemSelected(AdapterView<?> parent,
                                                       View view, int pos, long id)

                            {

                                String p_id = ProductParser.productId[pos];
                                getItemDetailsByID_Report(p_id);

                            }

                            public void onNothingSelected(AdapterView parent) {
                                // Do nothing.
                            }
                        });


                    } catch (JSONException e) {

                    }


                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(Sales_ProductType.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getItemDetailsByID_Report(final String p_id)
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETITEMDETAISBYID, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try {
                    Log.d(TAG, "Items: " + response);
                    JSONObject msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Data available"))
                    {


                        ItemParser productParser = new ItemParser(response);
                        productParser.productParser();
                        ///  List<Map<String,String >> items=productParser.getItemArray();
                        ItemAdapter productAdapter = new ItemAdapter(Sales_ProductType.this, R.layout.product_list, ItemParser.itemName, ItemParser.itemId);
                        s_itemTypem.setAdapter(productAdapter);

                        s_itemTypem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            /**
                             * Called when a new item was selected (in the Spinner)
                             */
                            public void onItemSelected(AdapterView<?> parent,
                                                       View view, int pos, long id) {

                                String i_id = ItemParser.itemId[pos];
                                ITEMNAME = ItemParser.itemName[pos];
                                Log.d(TAG, "onItemSelected: " + i_id);
                                Log.d(TAG, "onItemSelected: " + ITEMNAME);
                                getItemDetailsByItemID_Report(i_id);

                            }

                            public void onNothingSelected(AdapterView parent) {
                                // Do nothing.
                            }
                        });


                    } else {

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        s_itemTypem.setPrompt("Item Not Available");
                    }


                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(Sales_ProductType.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("product_id", p_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getItemDetailsByItemID_Report(final String i_id)
    {


        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SALSE, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    loading.dismiss();

                    String resp = response.toString().trim();
                    Log.d("Sale_userDetails",resp);
                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");

                    Log.d("Sale Report", resp);
                    SaleParser saleParser = new SaleParser(resp);
                    saleParser.parseJSON();
                    saleInfos = saleParser.prepareSale();



                }catch (Exception e)
                {
                    Log.d("Exception",""+e);
                }



            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(Sales_ProductType.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("item_id",i_id);
                params.put("dbname",dbname);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    public  boolean isStoragePermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
            {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }


    private void getProduct()
    {


        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_NEW_PRODUCT, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try {
                    Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                    try {

                        JSONObject msg = new JSONObject(response);
                        String message = msg.getString("message");


                        ProductParser productParser = new ProductParser(response);
                        productParser.productParser();
                        ProductAdapter productAdapter = new ProductAdapter(Sales_ProductType.this, R.layout.product_list, ProductParser.productName, ProductParser.productId);
                        s_productType.setAdapter(productAdapter);

                        s_productType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            /**
                             * Called when a new item was selected (in the Spinner)
                             */
                            public void onItemSelected(AdapterView<?> parent,
                                                       View view, int pos, long id)

                            {

                                String p_id = ProductParser.productId[pos];
                                getItemDetailsByID(p_id);

                            }

                            public void onNothingSelected(AdapterView parent) {
                                // Do nothing.
                            }
                        });


                    } catch (JSONException e) {

                    }


                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(Sales_ProductType.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private void getItemDetailsByID(final String p_id) {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETITEMDETAISBYID, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try {
                    Log.d(TAG, "Items: " + response);
                    JSONObject msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Data available"))
                    {


                        ItemParser productParser = new ItemParser(response);
                        productParser.productParser();
                        ///  List<Map<String,String >> items=productParser.getItemArray();
                        ItemAdapter productAdapter = new ItemAdapter(Sales_ProductType.this, R.layout.product_list, ItemParser.itemName, ItemParser.itemId);
                        s_itemTypem.setAdapter(productAdapter);

                        s_itemTypem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            /**
                             * Called when a new item was selected (in the Spinner)
                             */
                            public void onItemSelected(AdapterView<?> parent,
                                                       View view, int pos, long id) {

                                String i_id = ItemParser.itemId[pos];
                                ITEMNAME = ItemParser.itemName[pos];
                                Log.d(TAG, "onItemSelected: " + i_id);
                                Log.d(TAG, "onItemSelected: " + ITEMNAME);
                                getItemDetailsByItemID(i_id);
                                _getItemDeatils(i_id);

                            }

                            public void onNothingSelected(AdapterView parent) {
                                // Do nothing.
                            }
                        });


                    } else {

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        s_itemTypem.setPrompt("Item Not Available");
                    }


                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(Sales_ProductType.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("product_id", p_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }



    private void getItemDetailsByItemID(final String i_id) {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SALSE, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    loading.dismiss();

                    String resp = response.toString().trim();
                    Log.d("Sale_userDetails",resp);
                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");

                    if(message.equalsIgnoreCase("Data not available"))
                    {

                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();


                        if(saleDetailAdapter!=null)
                        {
                            saleDetailAdapter.clearView();
                            saleDetailAdapter.notifyDataSetChanged();
                            recyclerView.clearFocus();
                            saleInfos.clear();
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            saleDetailAdapter=new SaleDetailAdapter(saleInfos,Sales_ProductType.this);
                            recyclerView.setAdapter(saleDetailAdapter);
                            //totalSaleAmount=    saleParser.getAmount();

                        }

                    }else
                    {
                        Log.d("UserDetail",resp);
                        if(saleDetailAdapter!=null)
                        {
                            saleDetailAdapter.clearView();
                            saleDetailAdapter.notifyDataSetChanged();
                        }
                        SaleParser saleParser=new SaleParser(resp);
                        saleParser.parseJSON();
                        saleInfos = saleParser.prepareSale();
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        saleDetailAdapter=new SaleDetailAdapter(saleInfos,Sales_ProductType.this);
                        recyclerView.setAdapter(saleDetailAdapter);

//                        totalSaleAmount=    saleParser.getAmount();
//                        txt_totalAmount.setText(String.valueOf(totalSaleAmount));

                    }



                }catch (Exception e)
                {
                    Log.d("Exception",""+e);
                }



            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(Sales_ProductType.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("item_id",i_id);
                params.put("dbname",dbname);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void _getItemDeatils( final String i_id)
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETTOTALCOUNT_PRODUCTTYPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    loading.dismiss();

                    String resp = response.toString().trim();
                    Log.d(TAG, "onResponse: Total Amount"+resp);
                    JSONObject jsonObject = new JSONObject(resp);
                    JSONArray message = jsonObject.getJSONArray("records");
                    JSONObject jsonObject1=message.getJSONObject(0);
                    txt_totalAmount.setText(jsonObject1.getString("total_amount"));
                    txt_totalBalnce.setText(jsonObject1.getString("balance"));
                } catch (Exception e)
                {
                    Log.d("Exception", "" + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(context, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("item_id", i_id);
                Log.i(TAG, "getParams:"+params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }




    private  boolean _CreatePdf(String fname, Context fcontent, String Dir, ArrayList<SaleInfo> saleInfos)
    {

        FileOutputStream fos = null;
        try {
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

            Toast.makeText(Sales_ProductType.this, "Pdf generated Successfully", Toast.LENGTH_SHORT).show();

        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (DocumentException e) {
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
            }
        }
        return false;
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

    private static void createTable(Section subCatPart, Document document)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);
        try {
            document.add(subCatPart);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }


}

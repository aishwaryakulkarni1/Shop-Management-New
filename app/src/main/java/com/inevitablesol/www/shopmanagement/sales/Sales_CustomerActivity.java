package com.inevitablesol.www.shopmanagement.sales;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
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
import com.inevitablesol.www.shopmanagement.Validation.DatePicker_Class;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.analysis.date.ImageBuilders;
import com.inevitablesol.www.shopmanagement.analysis.date.PdfXml_;
import com.inevitablesol.www.shopmanagement.customer_module.CustomerAdapter;
import com.inevitablesol.www.shopmanagement.customer_module.CustomerInfo;
import com.inevitablesol.www.shopmanagement.customer_module.CustomerParser;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.InvocieGstRecords;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.InvocieWithGst;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.ItemArray;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.ItemArrayGst;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.Records;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.RecordsGst;
import com.inevitablesol.www.shopmanagement.wishList.DatePick;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Sales_CustomerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String GET_SALSE ="http://35.161.99.113:9000/webapi/sale/customer" ;
    private RecyclerView recyclerView;
    private EditText editText;
    private EditText editText_d;


    private ArrayList<CustomerInfo> custDetails;

    private CustomerAdapter customerAdapter;
    private ListView listView;
    private ListView listView_d;

    private SimpleAdapter newAdapter;
    private String custSelected;
    private ArrayList<SaleInfo> saleInfos;
    TextView txt_totalAmount;
    private double totalSaleAmount=0.0;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;



    private ImageView imageView_download;
    private TextView txt_fromDate;
    private TextView txt_toDate;
    private TextView txtname;
    private TextView txtmobile;
    private static final String TAG = "Sales_CustomerActivity";
    private TextView txttillDate;
    private String cid;
    private Context context=Sales_CustomerActivity.this;

    private  TextView totalExpense;
    private  TextView  totalBalance;

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
    private boolean Gst_Status;
    private static final String MySETTINGS = "MySetting";
    private SharedPreferences sharedpreferences2;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_cust_list);
        recyclerView = (RecyclerView)findViewById(R.id.stock_recyclerView);
        editText=(EditText)findViewById(R.id.input_searchCusttomer);
         listView=(ListView)findViewById(R.id.list_customerInfo);
        editText.setOnClickListener(this);


        txt_totalAmount=(TextView)findViewById(R.id.exp_view_totalExp);
        getCustomerDetails();
         totalExpense=(TextView)findViewById(R.id.exp_view_totalExp);
        totalBalance=(TextView)findViewById(R.id.totalBalance);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        sharedpreferences2 = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
        pdfXml_=new PdfXml_(this,sharedpreferences);

        imageView_download=(ImageView)findViewById(R.id.sale_download_customer);
        imageView_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSaleReportDialog();
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


       // _getItemDeatils();


    }

    private void _getItemDeatils(final String cid)
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETTOTALCOUNT, new Response.Listener<String>() {
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
                                        totalExpense.setText(jsonObject1.getString("total_amount"));
                                        totalBalance.setText(jsonObject1.getString("balance"));




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
                params.put("custid", cid);
                Log.i(TAG, "getParams:"+params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void showInvoice(final SaleInfo saleInfo)
    {


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.sale_report_dialog);
        dialog.setTitle("Sale Report");

        final  TextView txt_invNumber=(TextView)dialog.findViewById(R.id.inv_number);
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
                pdfXml_.openFile("invoice.pdf",context,"SaleReports");
               // openXLS();

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


    private void _getInvoice_details_forGst(final String invoice_id, final SaleInfo sale)
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

//                        InvocieGstRecords invocieGstRecords=new InvocieGstRecords();
//                        Gson gson=new Gson();
//                        invocieGstRecords=gson.fromJson(resp,invocieGstRecords.getClass());
//                        Records  recordses=invocieGstRecords.getRecords();
//                        ArrayList<ItemArray> itemArrays= (ArrayList<ItemArray>) recordses.getItemArray();
//                        Log.d(TAG, "onResponse: "+itemArrays.get(0).getItemName());
//                        SaleParser saleParser = new SaleParser(resp);
//                        saleParser.parseJSON();
//                        ArrayList<SaleInfo>  saleInfos = saleParser.prepareSale();
                       /* saleDetailAdapter = new SaleDetailAdapter(saleInfos, Sales_TotalSales.this);
                        recyclerView.setAdapter(saleDetailAdapter);
                        saleDetailAdapter.notifyDataSetChanged();*/

                        try {
                            if(isStoragePermissionGranted())
                            {

                               // pdfXml_.invoicePdf_gst("invoice", context, "SaleReports", sale,recordsGsts);
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

    private void showSaleReportDialog()
    {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.total_sale_report_customer);
        final  ImageView  fromdate     =(ImageView)dialog.findViewById(R.id.total_imgfromDate);

        final  ImageView  todate     =(ImageView)dialog.findViewById(R.id.total_todate_image);
        final AppCompatButton download=(AppCompatButton)dialog.findViewById(R.id.total_saleDownload);
        final RadioGroup radioGroup=(RadioGroup)dialog.findViewById(R.id.total_Sale);
        final ImageView imgMonth=(ImageView)dialog.findViewById(R.id.total_imgMonth);
        final  ImageView imgYear=(ImageView)dialog.findViewById(R.id.total_imgYear);
        final   TextView  txtmonth=(TextView)dialog.findViewById(R.id.t_month);
        final    TextView  txtYear=(TextView)dialog.findViewById(R.id.t_year);
        final   TextView  txtOnlyYear=(TextView)dialog.findViewById(R.id.txt_onlyYear);
        final    ImageView  imgOnlyYear=(ImageView)dialog.findViewById(R.id.img_onlyYear);
        final RadioGroup radioGroup1=(RadioGroup)dialog.findViewById(R.id.total_SaleRadio);
        final  AppCompatButton viewReport=(AppCompatButton)dialog.findViewById(R.id.total_saleView);
        final  AppCompatButton share=(AppCompatButton)dialog.findViewById(R.id.report_share);
        txtname=(TextView)dialog.findViewById(R.id.txt_name);
        txtmobile=(TextView)dialog.findViewById(R.id.txt_mobile);
        editText_d=(EditText)dialog.findViewById(R.id.input_searchCusttomer);
        listView_d=(ListView)dialog.findViewById(R.id.list_customerInfo);
        txt_fromDate =(TextView)dialog.findViewById(R.id.total_fromdate);
        txt_toDate  =(TextView)dialog.findViewById(R.id.total_toDate);
        txttillDate=(TextView)dialog.findViewById(R.id.txt_tillDate);

        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        // String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        String currentDateTimeString = dateFormat.format(dt);
        txt_fromDate.setText(currentDateTimeString);

         getCustomerDetailsDialog();


        txttillDate.setText(currentDateTimeString);



        imgMonth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
              DatePicker_Class datePicker_class= DatePicker_Class.getInstance();
                        if(datePicker_class!=null)
                        {
                              String  date   =   datePicker_class.getMonth(context,txtmonth);
                            Log.d(TAG, "onClick:"+date);
                        }
               // changeMonth(txtmonth);

            }
        });
//        imgYear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//
//                changeYear(txtYear);
//
//            }
//        });

        share.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try
                {
                    int selectedId1 = radioGroup1.getCheckedRadioButtonId();
                    RadioButton radioButton1 = (RadioButton)dialog.findViewById(selectedId1);
                    String  fileFormat1   =     radioButton1.getText().toString().trim();

                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton)dialog.findViewById(selectedId);
                    String  fileFormat   = radioButton.getText().toString().trim();


                    Log.d(TAG, "onClick:Option"+fileFormat1);
                    if(fileFormat1.equalsIgnoreCase("Month"))
                    {

                        if(fileFormat.equalsIgnoreCase(".xls"))
                        {
                            pdfXml_.shareFile("FromMonthYear.xls",context, "Customer");

                        }else
                        {
                            pdfXml_.shareFile("FromMonthYear.pdf",context, "Customer");

                        }

                     //  .. share_Xls_File("FromMonthYear.xls");

                    }else if(fileFormat1.equalsIgnoreCase("Till"))
                    {

                        if(fileFormat.equalsIgnoreCase(".xls"))
                        {
                            pdfXml_.shareFile("From_TillDate.xls",context, "Customer");

                        }else
                        {
                            pdfXml_.shareFile("From_TillDate.pdf",context, "Customer");

                        }

                        //share_Xls_File("From_TillDate.xls");
                    }
                    else
                    {
                        if(fileFormat.equalsIgnoreCase(".xls"))
                        {
                            pdfXml_.shareFile("FromDate.xls",context, "Customer");

                        }else
                        {
                            pdfXml_.shareFile("FromDate.pdf",context, "Customer");

                        }

                       // share_Xls_File("FromDate.xls");
                    }






                }catch (Exception e)
                {
                    Log.d(TAG, "onClick:Exception"+e);
                    Toast.makeText(getApplication(),"Please Select At Least One Option",Toast.LENGTH_LONG).show();
                }

            }
        });

        viewReport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int selectedId1 = radioGroup1.getCheckedRadioButtonId();
                RadioButton radioButton1 = (RadioButton)dialog.findViewById(selectedId1);
                String  fileFormat1   =     radioButton1.getText().toString().trim();

                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton)dialog.findViewById(selectedId);
                String  fileFormat   = radioButton.getText().toString().trim();

                Log.d(TAG, "onClick:Option"+fileFormat1);
                if(fileFormat1.equalsIgnoreCase("Month"))
                {
                    if(fileFormat.equalsIgnoreCase(".xls"))
                    {
                        pdfXml_.openFile("FromMonthYear.xls",context, "Customer");

                    }else
                    {
                        pdfXml_.openFile("FromMonthYear.pdf",context, "Customer");

                    }
                  //  openXLS("FromMonthYear.xls");

                }else if(fileFormat1.equalsIgnoreCase("Year"))
                {

                    if(fileFormat.equalsIgnoreCase(".xls"))
                    {
                        pdfXml_.openFile("YearSale.xls",context, "Customer");

                    }else
                    {
                        pdfXml_.openFile("YearSale.pdf",context, "Customer");

                    }

                   // openXLS("YearWiseSale.xls");

                }else if(fileFormat1.equalsIgnoreCase("Till"))
                {
                    if(fileFormat.equalsIgnoreCase(".xls"))
                    {
                        pdfXml_.openFile("From_TillDate.xls",context, "Customer");

                    }else
                    {
                        pdfXml_.openFile("From_TillDate.pdf",context, "Customer");

                    }


                }else
                {
                    if(fileFormat.equalsIgnoreCase(".xls"))
                    {
                        pdfXml_.openFile("FromDate.xls",context, "Customer");

                    }else
                    {
                        pdfXml_.openFile("FromDate.pdf",context, "Customer");

                    }
                    //openXLS("FromDate.xls");
                }


            }
        });

        download.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {

                    int selectedId1 = radioGroup1.getCheckedRadioButtonId();
                    RadioButton radioButton1 = (RadioButton)dialog.findViewById(selectedId1);
                    String  fileFormat1   =     radioButton1.getText().toString().trim();
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton)dialog.findViewById(selectedId);
                    String  fileFormat   = radioButton.getText().toString().trim();

                    Log.d(TAG, "onClick:Option" + fileFormat1);


                    if (fileFormat1.equalsIgnoreCase("Till"))
                    {


                        String Year = txttillDate.getText().toString().trim();

                        Log.d(TAG, "onClick:FileFormat" + fileFormat);

                        if (fileFormat.equalsIgnoreCase(".pdf"))
                        {
                            Log.d(TAG, "onClick:.pdf");


                            if (Year.length() > 0 && !Year.isEmpty())
                            {
                                pdfReadWrite=true;
                                getAllSale_Till(Year);
                            }

                            // _CreatePdf("SaleReport.pdf",context);
                        } else if (fileFormat.equalsIgnoreCase(".xls"))
                        {
                            Log.d(TAG, "onClick:Xls");
                            if (Year.length() > 0)
                            {
                                getAllSale_Till(Year);
                            } else {
                                Toast.makeText(getApplicationContext(), "Select Year", Toast.LENGTH_SHORT).show();
                            }


                        }


                    }  else if(fileFormat1.equalsIgnoreCase("Month"))
                    {


                        String fromDate = txtmonth.getText().toString().trim();
                        String toDate = txtYear.getText().toString().trim();

                        Log.d(TAG, "onClick:FileFormat"+fileFormat);

                        if(fileFormat.equalsIgnoreCase(".pdf"))
                        {
                            Log.d(TAG, "onClick:.pdf");


                            if (toDate.length() > 0 && !toDate.isEmpty())
                            {
                                getAllMoth_YearSale(fromDate, toDate);

                            }

                            // _CreatePdf("SaleReport.pdf",context);
                        }else  if(fileFormat.equalsIgnoreCase(".xls"))
                        {
                            Log.d(TAG, "onClick:Xls");
                            getAllMoth_YearSale(fromDate, toDate);

//

                        }




                    }else
                    {


                        String fromDate = txt_fromDate.getText().toString().trim();
                        String toDate = txt_toDate.getText().toString().trim();

                        Log.d(TAG, "onClick:FileFormat" + fileFormat);

                        if (fileFormat.equalsIgnoreCase(".pdf") && fileFormat1.equalsIgnoreCase("From")) {
                            Log.d(TAG, "onClick:.pdf");


                            if (toDate.length() > 0 && !toDate.isEmpty())
                            {
                                getAllSales(fromDate, toDate);

                            }

                            // _CreatePdf("SaleReport.pdf",context);
                        } else if (fileFormat.equalsIgnoreCase(".xls") && fileFormat1.equalsIgnoreCase("From"))
                        {

                            Log.d(TAG, "onClick:Xls");
                            getAllSales(fromDate, toDate);
//                    ReadWriteExcelFile readWriteExcelFile = new ReadWriteExcelFile();
//                    try {
//                        readWriteExcelFile.testFile(context, "TotalSale", saleInfos);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                        }
                    }

                    }catch (NullPointerException e)
                {
                    Toast.makeText(getApplicationContext(),"Please Select At Least One Option",Toast.LENGTH_LONG).show();
                }


            }
        });


        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Todate", Toast.LENGTH_SHORT).show();
                changeDate(txt_toDate);
            }
        });

        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate(txt_fromDate);
            }
        });
        final AppCompatButton  cancel=(AppCompatButton)dialog.findViewById(R.id.dialogButtonCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        //  dialog.setCancelable(false);
        dialog.show();

    }


    private void share_Xls_File(String fileName)
    {

        Log.d(TAG, "onClick:Share");
        Intent intentFile = new Intent(Intent.ACTION_SEND);
        File file = new File(Environment.getExternalStorageDirectory()+ "/SaleReports/Customer/" + fileName);

        if(file.exists())
        {
            intentFile.setType("application/xls");
            intentFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));

            intentFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File...");
            intentFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

            startActivity(Intent.createChooser(intentFile, "Share File"));
        }else
        {
            Toast.makeText(getApplicationContext(), " File Not Available", Toast.LENGTH_SHORT).show();
        }

    }


    private void getAllSales(final String fromDate, final String toDate)
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SALSEDATETIME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
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
                        SaleParser saleParser = new SaleParser(resp);
                        saleParser.parseJSON();
                        saleInfos = saleParser.prepareSale();
                        ReadWriteExcelFile readWriteExcelFile = new ReadWriteExcelFile();
                        try {
                            if(isStoragePermissionGranted())
                            {
                                if(pdfReadWrite)
                                {
                                    _CreatePdf("FromDate",context,"Customer",saleInfos);

                                }else
                                {
                                    readWriteExcelFile.testFile(getApplicationContext(), "FromDate", saleInfos,"Customer");
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


                } catch (Exception e) {
                    Log.d("Exception", "" + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplicationContext(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("from_date", fromDate);
                params.put("to_date", toDate);
                params.put("custid",cid);
                Log.i(TAG, "getParams:From Date"+params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private  static  final  String GET_TOTALSALE_MONTH="http://35.161.99.113:9000/webapi/sale/searchCustomerMonthYear";
    private void getAllMoth_YearSale(String fromDate, final String toDate)
    {


        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_TOTALSALE_MONTH, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                try {
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
                        SaleParser saleParser = new SaleParser(resp);
                        saleParser.parseJSON();
                        saleInfos = saleParser.prepareSale();
                        ReadWriteExcelFile readWriteExcelFile = new ReadWriteExcelFile();
                        try {
                            if(isStoragePermissionGranted())
                            {
                                if(pdfReadWrite)
                                {
                                    _CreatePdf("FromMonthYear",context,"Customer",saleInfos);
                                }
                                else
                                {
                                    readWriteExcelFile.testFile(getApplicationContext(), "FromMonthYear", saleInfos,"Customer");
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


                } catch (Exception e) {
                    Log.d("Exception", "" + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("monthyear", toDate);
                params.put("custid",cid);
                Log.i(TAG, "getParams:From Month Year"+params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }



    private void openXLS(String s)
    {
        try
        {
            File xls = new File(Environment.getExternalStorageDirectory()+ "/SaleReports/Customer/" + s);
            Uri path = Uri.fromFile(xls);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path,"application/vnd.ms-excel");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            this.startActivity(intent);
        } catch (ActivityNotFoundException e)
        {
            Log.d(TAG, "openXLS:"+e);
            Toast.makeText(this, "No Application available to view XLS", Toast.LENGTH_SHORT).show();
        }

    }



    private static final String GET_SALSEDATETIME ="http://35.161.99.113:9000/webapi/sale/searchCustomerDate";

    private void getAllSale_Till(final String todate)
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SALSEDATETIME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
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
                        SaleParser saleParser = new SaleParser(resp);
                        saleParser.parseJSON();
                        saleInfos = saleParser.prepareSale();
                        ReadWriteExcelFile readWriteExcelFile = new ReadWriteExcelFile();
                        try {
                            if(isStoragePermissionGranted())
                            {
                                  if(pdfReadWrite)
                                  {
                                      _CreatePdf("From_TillDate",context,"Customer",saleInfos);



                                  }else
                                  {
                                      readWriteExcelFile.testFile(getApplicationContext(), "From_TillDate", saleInfos,"Customer");
                                      return;

                                  }


                            }else
                            {
                                Log.d(TAG, "onResponse:False");
                            }

                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                    }


                } catch (Exception e) {
                    Log.d("Exception", "" + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplicationContext(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("from_date", "01-01-17");
                params.put("to_date", todate);
                params.put("custid",cid);
                Log.i(TAG, "getParams:Till Date"+params.toString());
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

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    private void getCustomerDetails()
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETCUSTINFO, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {

                     loading.dismiss();
                    String resp = response.toString().trim();
                    Log.d("cust Details resp", resp);

                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else
                    {
                        CustomerParser customerParser=new CustomerParser(resp);
                                               customerParser.custDetails();
                                               custDetails=customerParser.makeArray();

                        final List<Map<String, String>> cust = new ArrayList<Map<String, String>>();
                        Map<String, String> map = null;
                        int counter = CustomerParser.cust_Name.length;
                        for (int i = 0; i < counter; i++)
                        {
                            map = new HashMap<>();
                            map.put("name", CustomerParser.cust_Name[i]);
                            map.put("mobile", CustomerParser.mobile[i]);
                            map.put("custId",CustomerParser.cust_id[i]);
                            cust.add(map);
                        }
                        Log.d(TAG, "onResponse:Map"+map.toString());

                        newAdapter = new SimpleAdapter(Sales_CustomerActivity.this, cust, R.layout.list_customerinfo, new String[]{"name", "mobile"}, new int[]{R.id.customer_name, R.id.customer_mobile});
                        listView.setAdapter(newAdapter);

                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                listView.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                                listView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void afterTextChanged(Editable s)
                            {
                                Sales_CustomerActivity.this.newAdapter.getFilter().filter(s);
                                listView.setVisibility(View.VISIBLE);
                            }
                        });


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Map custSel = (Map) newAdapter.getItem(position);
                                cid=(String)custSel.get("custId");
                                Log.d(TAG, "onItemClick:CustiD"+cid);
                                custSelected = (String) custSel.get("name") + " " + custSel.get("mobile");
                                editText.setText(custSelected);
                                _getItemDeatils(cid);

                                listView.setVisibility(View.GONE);
                                 getDetails((String)custSel.get("name"));
                               // Toast.makeText(Sales_CustomerActivity.this, "Your selected customer is " + custSelected, Toast.LENGTH_LONG).show();
                            }
                        });


//

                    }
                }catch(Exception e)
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
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getDetails(final String  name)
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
                    Log.d("Sale_CustomerResponse",resp);
                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");

                    if(message.equalsIgnoreCase("Data not available"))
                    {

                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                    }else
                    {
                        Log.d("paymentDetail",resp);
                        SaleParser saleParser=new SaleParser(resp);
                        saleParser.parseJSON();
                        saleInfos = saleParser.prepareSale();
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);

                        SaleDetailAdapter saleDetailAdapter=new SaleDetailAdapter(saleInfos,Sales_CustomerActivity.this);
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
                    Toast.makeText(Sales_CustomerActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("dbname", dbname);
                params.put("customer_name",name);

                Log.d("name",name);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v)
    {
        int id=v.getId();



    }





    private void getCustomerDetailsDialog()
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETCUSTINFO, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {

                    loading.dismiss();
                    String resp = response.toString().trim();
                    Log.d("cust Details resp", resp);

                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else
                    {
                        CustomerParser customerParser=new CustomerParser(resp);
                        customerParser.custDetails();
                        custDetails=customerParser.makeArray();

                        final List<Map<String, String>> cust = new ArrayList<Map<String, String>>();
                        Map<String, String> map;
                        int counter = CustomerParser.cust_Name.length;
                        for (int i = 0; i < counter; i++) {
                            map = new HashMap<>();
                            map.put("name", CustomerParser.cust_Name[i]);
                            map.put("mobile", CustomerParser.mobile[i]);
                            map.put("custId",CustomerParser.cust_id[i]);
                            cust.add(map);
                        }

                        newAdapter = new SimpleAdapter(Sales_CustomerActivity.this, cust, R.layout.list_customerinfo, new String[]{"name", "mobile"}, new int[]{R.id.customer_name, R.id.customer_mobile});
                        listView_d.setAdapter(newAdapter);

                        editText_d.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                listView_d.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                                listView_d.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void afterTextChanged(Editable s)
                            {
                                Sales_CustomerActivity.this.newAdapter.getFilter().filter(s);
                                listView_d.setVisibility(View.VISIBLE);
                            }
                        });


                        listView_d.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Map custSel = (Map) newAdapter.getItem(position);
                                cid=(String)custSel.get("custId");
                                Log.d(TAG, "onItemClick:CustiD"+cid);
                                custSelected = (String) custSel.get("name") + " " + custSel.get("mobile");
                                editText_d.setText(custSelected);

                                 txtname.setText((CharSequence) custSel.get("name"));
                                txtmobile.setText((CharSequence) custSel.get("mobile"));
                                listView_d.setVisibility(View.GONE);
                               // getDetails((String)custSel.get("name"));
                                // Toast.makeText(Sales_CustomerActivity.this, "Your selected customer is " + custSelected, Toast.LENGTH_LONG).show();
                            }
                        });


//

                    }
                }catch(Exception e)
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
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void changeMonthOnly(final TextView txtdate)
    {


        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
             //   myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date date1 = myCalendar.getTime();

                //updateDate(myCalendar);
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM");
                String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                txtdate.setText(currentDateTimeString);

            }

        };

        new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));
    }

    private void changeMonth(final TextView txtdate)
    {


        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
               myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date date1 = myCalendar.getTime();

                //updateDate(myCalendar);
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM");
                String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                txtdate.setText(currentDateTimeString);

            }

        };

        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));
    }
    private void changeYear(final TextView txtdate)
    {


        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date date1 = myCalendar.getTime();

                //updateDate(myCalendar);
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM");
                String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                txtdate.setText(currentDateTimeString);

            }

        };

        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));
    }



    private void changeDate(final TextView txtdate)
    {


        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date date1 = myCalendar.getTime();

                //updateDate(myCalendar);
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                txtdate.setText(currentDateTimeString);

            }

        };

        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));
    }

    private void changeYearOnly(final TextView txtyear)
    {


        ; final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date date1 = myCalendar.getTime();

                //updateDate(myCalendar);
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy");
                String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                txtyear.setText(currentDateTimeString);

            }

        };

        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));

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

            Toast.makeText(Sales_CustomerActivity.this, "Pdf generated Successfully", Toast.LENGTH_SHORT).show();

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
        com.itextpdf.text.List list = new com.itextpdf.text.List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }


}

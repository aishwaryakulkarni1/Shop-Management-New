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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.RegistrationModule.ReadWriteExcelFile;
import com.inevitablesol.www.shopmanagement.TestPDF;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.analysis.date.PdfXml_;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.InvocieGstRecords;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.InvocieWithGst;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.ItemArray;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.ItemArrayGst;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.Records;
import com.inevitablesol.www.shopmanagement.sales.gson_schema.RecordsGst;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Sales_DateTime extends AppCompatActivity implements View.OnClickListener {
    private static final String GET_SALSE = "http://35.161.99.113:9000/webapi/sale/DateAndTime";
    TextView txt_toDate,txt_fromDate;
    ImageView  img_fromDate,img_toDate;
    private ArrayList<SaleInfo> saleInfos;

    private RecyclerView recyclerView;
    private  SharedPreferences sharedpreferences;
    private   final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private  ImageView download;
    private static final String TAG = "Sales_DateTime";
    private  Context context=Sales_DateTime.this;
    private TextView txt_totalAmount,txt_totalBalnce;
    private PdfXml_ pdfXml_;

    private static final String MySETTINGS = "MySetting";
    private SharedPreferences sharedpreferences2;
    private boolean Gst_Status;
    SaleInfo saleInfo;

    private TestPDF testPDF;

   // private  boolean   GST_STATUS;
    private   GlobalPool globalPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales__date_time);
           txt_toDate=(TextView)findViewById(R.id.id_sale_toDate);
           txt_fromDate=(TextView)findViewById(R.id.id_sale_fromDate);
           img_fromDate=(ImageView)findViewById(R.id.im_fromdatePicker);
           img_toDate=(ImageView)findViewById(R.id.im_toDatePicker);
           img_toDate.setOnClickListener(this);
           img_fromDate.setOnClickListener(this);
        download=(ImageView)findViewById(R.id.sale_download);
        download.setOnClickListener(this);
        globalPool = (GlobalPool) getApplicationContext();
        testPDF=new TestPDF(this,sharedpreferences);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences2 = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");
        pdfXml_ =new PdfXml_(this,sharedpreferences);

         txt_totalAmount=(TextView)findViewById(R.id.exp_view_totalExp) ;
         txt_totalBalnce=(TextView)findViewById(R.id.txt_balance);

        Gst_Status   = globalPool.getGst_status();
        Log.d(TAG, "onCreate: GST"+Gst_Status);

        recyclerView = (RecyclerView)findViewById(R.id.date_exl_list);

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
                    saleInfo = saleInfos.get(position);
                    Log.d(TAG, "onInterceptTouchEvent: Sale Info"+saleInfo);
                    showInvoice(saleInfo);

                }
                return false;

            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
            {

            }
        });


        updateDate();

    }

    @Override
    public void onClick(View v)
    {
        int id=v.getId();

         switch (id)
         {
             case R.id.im_fromdatePicker:
                 getSelectedFromDate();
                  break;
             case R.id.im_toDatePicker:
                    getSelectedToDate();
                 break;
                case R.id.sale_download:
                    // downLoadAllReports();
                   // DownloadFile();
                    break;
         }

    }




    private void showInvoice(final SaleInfo saleInfo)
    {


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.sale_report_dialog);
        dialog.setTitle("Sale Report");
        final  TextView txt_invNumber=(TextView)dialog.findViewById(R.id.inv_number);
        final  TextView  txt_inv_Date=(TextView)dialog.findViewById(R.id.inv_date);
        txt_invNumber.setText(saleInfo.getInvoice_id());
        txt_inv_Date.setText(saleInfo.getCreated_Date());
        final AppCompatButton Download=(AppCompatButton)dialog.findViewById(R.id.sale_dateTimeDownload);
        final  AppCompatButton viewReport=(AppCompatButton)dialog.findViewById(R.id.sale_date_view);
        final  AppCompatButton share=(AppCompatButton)dialog.findViewById(R.id.share_total_saleDateTime);
      //  final RadioGroup radioGroup1=(RadioGroup)dialog.findViewById(R.id.rg_saleTotalSale);

        viewReport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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
            public void onClick(View v)
            {
                try
                {
                    pdfXml_.shareFile("invoice.pdf",context,"SaleReports");

                    //share_Xls_File("Invoice.pdf");

                }catch (Exception e)
                {
                    Log.d(TAG, "onClick: Exception"+e);
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
                        Log.d(TAG, "onResponse:"+itemArrayGsts.get(0).getItemName());

                        try {
                            if(isStoragePermissionGranted())
                            {
                              //  testPDF.invoicePdf_gst("invoice", context, "SaleReports", saleInfos,recordsGsts);//
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
                        Log.d("Sale Report without GST", resp);
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



    private void share_Xls_File(String fileName)
    {

        Log.d(TAG, "onClick:Share");
        Intent intentFile = new Intent(Intent.ACTION_SEND);
        File file = new File(Environment.getExternalStorageDirectory()+ "/SaleReports/DateTime/" + fileName);

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
            Toast.makeText(context, " File Not Available", Toast.LENGTH_SHORT).show();
        }

    }

    private  boolean isStoragePermissionGranted()
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

    private void openXLS()
    {
        File sdCard = Environment.getExternalStorageDirectory();
        File xls = new File(sdCard.getAbsolutePath() + "/SaleReports/DateTime/" + "DateTime.xls");
        Uri path = Uri.fromFile(xls);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path,"application/vnd.ms-excel");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e)
        {
            Toast.makeText(context, "No Application available to view XLS", Toast.LENGTH_SHORT).show();
        }
    }

    private void getSelectedFromDate()
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
                updateFromDate(myCalendar);
            }

        };

        new DatePickerDialog(Sales_DateTime.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));

    }

    private void getSelectedToDate()
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
                 Date date1=myCalendar.getTime();

                updateToDate(myCalendar);
            }

        };

        new DatePickerDialog(Sales_DateTime.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));



    }

    private void updateDate()
    {
        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        // String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        String currentDateTimeString = dateFormat.format(dt);
        txt_fromDate.setText(currentDateTimeString);

    }

    private void updateFromDate(Calendar myCalendar)
    {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(myCalendar.getTime());
        txt_fromDate.setText(currentDateTimeString);
         Toast.makeText(getApplicationContext(),"select From Date",Toast.LENGTH_LONG).show();
    }


    private void updateToDate(Calendar myCalendar)
    {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(myCalendar.getTime());
        txt_toDate.setText(currentDateTimeString);
        String fromDate = txt_fromDate.getText().toString().trim();
        String toDate = txt_toDate.getText().toString().trim();

        if (toDate.length() > 0 && !toDate.isEmpty())
            getAllSales(fromDate, toDate);
    }

    private void getAllSales(final String fromDate, final String toDate)
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SALSE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loading.dismiss();

                    String resp = response.toString().trim();
                    Log.d("sales Report", resp);
                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");

                    if (message.equalsIgnoreCase("Data not available"))
                    {

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    } else
                    {
                        Log.d("paymentDetail", resp);
                        SaleParser saleParser = new SaleParser(resp);
                        saleParser.parseJSON();
                        saleInfos = saleParser.prepareSale();
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        SaleDetailAdapter saleDetailAdapter = new SaleDetailAdapter(saleInfos, Sales_DateTime.this);
                        recyclerView.setAdapter(saleDetailAdapter);
                        _getItemDeatils(fromDate,toDate);

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
                    Toast.makeText(Sales_DateTime.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("dbname",dbname);
                params.put("from_date", fromDate);
                params.put("to_date", toDate);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private void _getItemDeatils(final String fromDate, final String toDate)
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETTOTALCOUNT_FROMDATE, new Response.Listener<String>() {
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
                params.put("from_date", fromDate);
                params.put("to_date", toDate);
                Log.i(TAG, "getParams:"+params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }





}

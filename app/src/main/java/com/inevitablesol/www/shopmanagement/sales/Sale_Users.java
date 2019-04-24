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
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
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
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.RegistrationModule.ReadWriteExcelFile;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Sale_Users extends AppCompatActivity implements View.OnClickListener {

    private static final String GET_DETAILS ="http://35.161.99.113:9000/webapi/sale/allSaleUsers" ;
    Spinner spnn_saleUser;
    private ArrayList<SaleInfo> saleInfos;
    private static final String GET_SALSE ="http://35.161.99.113:9000/webapi/sale/user";
    private RecyclerView recyclerView;
    private ArrayList<UserRole> userInfo;

    TextView txt_totalAmount;
    private double totalSaleAmount=0.0;

    private static final String TAG = "Sale_Users";

    private  SaleDetailAdapter saleDetailAdapter;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;

    private ImageView downloadReport;
    private TextView txt_fromDate;
    private TextView txt_toDate;
    private String U_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale__users);
        spnn_saleUser=(Spinner)findViewById(R.id.spnn_saleUser);
        recyclerView = (RecyclerView)findViewById(R.id.stock_recyclerView);
       // txt_totalAmount=(TextView)findViewById(R.id.exp_view_totalExp);
        downloadReport=(ImageView)findViewById(R.id.sale_download_Users);
        downloadReport.setOnClickListener(this);

        getUserDetails();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = sharedpreferences.getString("dbname", "");

    }

    private void getUserDetails()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_DETAILS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {

                    String resp = response.toString().trim();
                    Log.d("UserDetails", resp);

                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else
                    {
                        UserParser userParser=new UserParser(resp);
                        userParser.parseJSON();
                                    userInfo=userParser.getUserInformations();
                            UserDetailAdapter userDetailAdapter=new UserDetailAdapter(Sale_Users.this, R.layout.product_list, UserParser.user_name,UserParser.user_id);
                        spnn_saleUser.setAdapter(userDetailAdapter);

                        spnn_saleUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                            {
                                String userId=spnn_saleUser.getSelectedItem().toString().trim();

                                getUserSsale(userId);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


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

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("dbname",dbname);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getUserSsale(final String userId)
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
                             saleDetailAdapter=new SaleDetailAdapter(saleInfos,Sale_Users.this);
                             recyclerView.setAdapter(saleDetailAdapter);
                             //totalSaleAmount=    saleParser.getAmount();
                             txt_totalAmount.setText(String.valueOf(0.0));
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
                         saleDetailAdapter=new SaleDetailAdapter(saleInfos,Sale_Users.this);
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
                    Toast.makeText(Sale_Users.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("u_id",userId);
                params.put("dbname",dbname);
                Log.d(TAG, "getParams: "+params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.sale_download_Users:
                showOption();
                break;
        }

    }

    private void showOption()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.total_sale_report_user);
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
         spnn_saleUser=(Spinner)dialog.findViewById(R.id.spnn_saleUser) ;
        final  TextView txtname=(TextView)dialog.findViewById(R.id.txt_name);
        final  TextView txtmobile=(TextView)dialog.findViewById(R.id.txt_mobile);
        txt_fromDate =(TextView)dialog.findViewById(R.id.total_fromdate);
        txt_toDate  =(TextView)dialog.findViewById(R.id.total_toDate);

        final  AppCompatButton share=(AppCompatButton)dialog.findViewById(R.id.Sale_User_Share);

        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MMM-yy");
        // String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        String currentDateTimeString = dateFormat.format(dt);
        txt_fromDate.setText(currentDateTimeString);

        UserDetailAdapter userDetailAdapter=new UserDetailAdapter(Sale_Users.this, R.layout.product_list, UserParser.user_name,UserParser.user_id);
        spnn_saleUser.setAdapter(userDetailAdapter);

        spnn_saleUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String userId=spnn_saleUser.getSelectedItem().toString().trim();
                                  String name= UserParser.user_name[position];
                                String mobile = UserParser.user_mobile[position];

                                   U_ID=UserParser.user_id[position];
                                   txtmobile.setText(mobile);
                                   txtname.setText(name);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imgOnlyYear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

              changeYearOnly(txtOnlyYear);

            }
        });

        imgMonth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeMonth(txtmonth);

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

                    Log.d(TAG, "onClick:Option"+fileFormat1);
                    if(fileFormat1.equalsIgnoreCase("Month"))
                    {

                        share_Xls_File("FromMonthYear.xls");

                    }else if(fileFormat1.equalsIgnoreCase("Till"))
                    {

                        share_Xls_File("From_TillDate.xls");
                    }else if(fileFormat1.equalsIgnoreCase("Last 6 Month"))
                    {

                        share_Xls_File("From_Last_six_Months.xls");
                    }
                    else
                    {

                        share_Xls_File("FromDate.xls");
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

                Log.d(TAG, "onClick:Option"+fileFormat1);
                if(fileFormat1.equalsIgnoreCase("Month"))
                {
                    openXLS("FromMonthYear_user.xls");

                }
                else if(fileFormat1.equalsIgnoreCase("Year"))
                {

                    openXLS("YearWiseSale_user.xls");

                }else if(fileFormat1.equalsIgnoreCase("Till"))
                {

                    openXLS("From_TillDate.xls");
                }else if(fileFormat1.equalsIgnoreCase("Last 6 Month"))
                {

                    openXLS("From_Last_six_Months.xls");//
                }else
                {
                    openXLS("FromDate_user.xls");
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
                    RadioButton radioButton1 = (RadioButton) dialog.findViewById(selectedId1);
                    String fileFormat1 = radioButton1.getText().toString().trim();

                    Log.d(TAG, "onClick:Option" + fileFormat1);

                    if(fileFormat1.equalsIgnoreCase("Last 6 Month"))
                    {
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.MONTH, -6);
                        Date result = cal.getTime();

                        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MMM-yy");
                        String currentDateTimeString = dateFormat.format(result);
                        Log.d(TAG, "onClick:Result"+currentDateTimeString);

                        Date dt = new Date();
                        java.text.SimpleDateFormat Format = new java.text.SimpleDateFormat("dd-MMM-yy");
                        // String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
                        String currentDate = dateFormat.format(dt);
                        Log.d(TAG, "onClick:Result"+currentDate);


                        getAllSale_LastSixMonths(currentDateTimeString,currentDate);

                    }

                    else if(fileFormat1.equalsIgnoreCase("Year"))
                    {

                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton)dialog.findViewById(selectedId);
                        String  fileFormat   = radioButton.getText().toString().trim();
                        String Year = txtOnlyYear.getText().toString().trim();

                        Log.d(TAG, "onClick:FileFormat"+fileFormat);

                        if(fileFormat.equalsIgnoreCase(".pdf"))
                        {
                            Log.d(TAG, "onClick:.pdf");


                            if (Year.length() > 0 && !Year.isEmpty())
                            {

                            }

                            // _CreatePdf("SaleReport.pdf",context);
                        }else  if(fileFormat.equalsIgnoreCase(".xls"))
                        {

                            Log.d(TAG, "onClick:Xls");
                            if (Year.length()>0)
                            {
                                getAllMoth_YearOnly(Year);
                            }else
                            {
                                Toast.makeText(getApplicationContext(), "Select Year", Toast.LENGTH_SHORT).show();
                            }


//                    ReadWriteExcelFile readWriteExcelFile = new ReadWriteExcelFile();
//                    try {
//                        readWriteExcelFile.testFile(context, "TotalSale", saleInfos);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                        }


                    }

                    else if(fileFormat1.equalsIgnoreCase("Month"))
                    {


                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton)dialog.findViewById(selectedId);
                        String  fileFormat   = radioButton.getText().toString().trim();
                        String fromDate = txtmonth.getText().toString().trim();
                        String toDate = txtYear.getText().toString().trim();

                        Log.d(TAG, "onClick:FileFormat"+fileFormat);

                        if(fileFormat.equalsIgnoreCase(".pdf"))
                        {
                            Log.d(TAG, "onClick:.pdf");


                            if (toDate.length() > 0 && !toDate.isEmpty())
                            {

                            }

                            // _CreatePdf("SaleReport.pdf",context);
                        }else  if(fileFormat.equalsIgnoreCase(".xls"))
                        {

                            Log.d(TAG, "onClick:Xls");
                            getAllMoth_YearSale(fromDate, toDate);

//                    ReadWriteExcelFile readWriteExcelFile = new ReadWriteExcelFile();
//                    try {
//                        readWriteExcelFile.testFile(context, "TotalSale", saleInfos);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                        }




                    }else {

                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton) dialog.findViewById(selectedId);
                        String fileFormat = radioButton.getText().toString().trim();
                        String fromDate = txt_fromDate.getText().toString().trim();
                        String toDate = txt_toDate.getText().toString().trim();

                        Log.d(TAG, "onClick:FileFormat" + fileFormat);

                        if (fileFormat.equalsIgnoreCase(".pdf") && fileFormat1.equalsIgnoreCase("From")) {
                            Log.d(TAG, "onClick:.pdf");


                            if (toDate.length() > 0 && !toDate.isEmpty()) {

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


    private void getAllSale_LastSixMonths(final String fromdate , final String todate)
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SALSEDATETIME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                        SaleParser saleParser = new SaleParser(resp);
                        saleParser.parseJSON();
                        saleInfos = saleParser.prepareSale();
                        ReadWriteExcelFile readWriteExcelFile = new ReadWriteExcelFile();
                        try {
                            if(isStoragePermissionGranted())
                            {
                                readWriteExcelFile.testFile(getApplicationContext(), "From_Last_six_Months", saleInfos,"User");
                                return;

                            }else
                            {
                                Log.d(TAG, "onResponse:False");
                            }

                        } catch (IOException e)
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
                params.put("from_date", fromdate);
                params.put("to_date", todate);
                params.put("u_id",U_ID);
                Log.i(TAG, "getParams:Till Date"+params.toString());
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
        File file = new File(Environment.getExternalStorageDirectory()+ "/SaleReports/User/" + fileName);

        if(file.exists()) {
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


    private void getAllMoth_YearOnly(final String year)
    {


        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_TOTALSALE_YEAR, new Response.Listener<String>()
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
                                readWriteExcelFile.testFile(getApplicationContext(), "YearWiseSale", saleInfos,"User");
                                return;

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
                params.put("year", year);
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
            File xls = new File(Environment.getExternalStorageDirectory()+ "/SaleReports/User/" + s);
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


    private static final String GET_SALSEDATETIME ="http://35.161.99.113:9000/webapi/sale/searchUserDate";

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
                                readWriteExcelFile.testFile(getApplicationContext(), "FromDate", saleInfos,"User");
                                return;

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
                params.put("u_id",U_ID);
                Log.i(TAG, "getParams:From Date"+params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private  static  final  String GET_TOTALSALE_MONTH="http://35.161.99.113:9000/webapi/sale/searchUserMonthYear";
    private void getAllMoth_YearSale(String fromDate, final String toDate)
    {


        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_TOTALSALE_MONTH, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
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
                        SaleParser saleParser = new SaleParser(resp);
                        saleParser.parseJSON();
                        saleInfos = saleParser.prepareSale();
                        ReadWriteExcelFile readWriteExcelFile = new ReadWriteExcelFile();
                        try {
                            if(isStoragePermissionGranted())
                            {
                                readWriteExcelFile.testFile(getApplicationContext(), "FromMonthYear", saleInfos,"User");
                                return;

                            }else
                            {
                                Log.d(TAG, "onResponse:False");
                            }

                        } catch (IOException e) {
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
                params.put("u_id",U_ID);
                params.put("monthyear", toDate);
                Log.i(TAG, "getParams:From Month Year"+params.toString());
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
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM");
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
                                  int dayOfMonth)
            {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date date1 = myCalendar.getTime();

                //updateDate(myCalendar);
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM-yy");
                String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                txtdate.setText(currentDateTimeString);

            }

        };

        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

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
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MMM-yy");
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
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yy");
                String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                txtyear.setText(currentDateTimeString);

            }

        };

        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));

    }

}

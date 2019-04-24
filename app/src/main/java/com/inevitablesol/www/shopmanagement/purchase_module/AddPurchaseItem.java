package com.inevitablesol.www.shopmanagement.purchase_module;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.billing_module.Parser.S_parser;
import com.inevitablesol.www.shopmanagement.billing_module.SuppierAdapter;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.product_info.adapter.ProductAdapter;
import com.inevitablesol.www.shopmanagement.product_info.adapter.ProductParser;
import com.inevitablesol.www.shopmanagement.settings.Utility;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.inevitablesol.www.shopmanagement.vendor_module.Record;
import com.inevitablesol.www.shopmanagement.vendor_module.VendorList;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddPurchaseItem extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddPurchaseItem";
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private Spinner s_product_type;

    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private String mProductID;
    private ArrayList<Record> vendorLists;

    private String gstDetails;
    private String gstNumber;
    private TextView txt_gst_status, txt_gst_number, invoiceDate;
    private ImageView datePicker;
    private AppCompatButton bt_next;
    private EditText et_innvoiceNumber;
    private ImageView inv_photo;
    private AppCompatButton appCompatButton;
    private String userChoosenTask;
    private int requestCode = 103;
    private int SELECT_FILE = 1;
    private ProgressDialog loading;
    private SqlDataBase sqlDataBase;
    private ImageView inv_photo2;
    private LinearLayout linearLayout;
    private int imageCount=0;

    public  ArrayList<Object> imageArrayList=new ArrayList<Object>();
    private File actualImage;
    private File compressedImage;

    private Bitmap imageCamera;
    private Spinner spinner;


    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;

    private final int PICK_IMAGE_REQUEST = 71;

    AppCompatButton uploadImage;
    private String mCurrentPhotoPath;
    private TextView txt_fileName;
    private String fileName;

    private  static final int PICK_PDF = 201;
    private  boolean isDocUploaded=true;
    private String imageFileId;
    private GlobalPool globalPool;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase_item);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        s_product_type = (Spinner) findViewById(R.id.spnn_product_type);
        globalPool = (GlobalPool) this.getApplication();

        txt_gst_number = (TextView) findViewById(R.id.txt_view_v_GSTNO);
        txt_gst_status = (TextView) findViewById(R.id.txt_view_v_GstStatus);
        invoiceDate = (TextView) findViewById(R.id.id_sale_Date);
        spinner=(Spinner)findViewById(R.id.purchase_supplier);
        txt_fileName=(TextView)findViewById(R.id.fileName);
        datePicker = (ImageView) findViewById(R.id.im_invDatePicker);
        inv_photo = (ImageView) findViewById(R.id.invoice_image);
        appCompatButton = (AppCompatButton) findViewById(R.id.take_inv_photo);
        appCompatButton.setOnClickListener(this);

        AppCompatButton  scaannBarcode=(AppCompatButton)findViewById(R.id.barcodeScan);
        scaannBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Record id=(Record)s_product_type.getSelectedItem();
                Log.d(TAG, "onClick:"+id.getVendorId());
                String gst_number = txt_gst_number.getText().toString().trim();
                String innvoiceDate = invoiceDate.getText().toString().trim();
                String invoiceNumber = et_innvoiceNumber.getText().toString().trim();
                String  stateCode=spinner.getSelectedItem().toString().trim();
                Log.d(TAG, "onClick:State CODE"+stateCode);

                if (invoiceNumber.length() > 0 && isDocUploaded)
                {
                    Intent intent = new Intent(AddPurchaseItem.this, BarcodeProduct.class);
                    intent.putExtra("v_id", String.valueOf(id.getVendorId()));
                    intent.putExtra("gst_nummber", gst_number);
                    intent.putExtra("invoiceDate", innvoiceDate);
                    intent.putExtra("invoiceNumber", invoiceNumber);
                    intent.putExtra("stateCode",stateCode);
                    Log.d(TAG, "onClick() called with: v = [" + intent.toString() + "]");
                    startActivity(intent);

                } else if(isDocUploaded)
                {
                    Toast.makeText(getApplicationContext(), " Add invoice Number", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), " Upload Docement", Toast.LENGTH_LONG).show();

                }


            }
        });
        uploadImage= (AppCompatButton) findViewById(R.id.uploadImage);
        uploadImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                uploadImage();
            }
        });

        bt_next = (AppCompatButton) findViewById(R.id.purchase_next);
        et_innvoiceNumber = (EditText) findViewById(R.id.input_innvoiceNumber);
        bt_next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                 Record id=(Record)s_product_type.getSelectedItem();
                 Log.d(TAG, "onClick:"+id.getVendorId());
                String gst_number = txt_gst_number.getText().toString().trim();
                String innvoiceDate = invoiceDate.getText().toString().trim();
                String invoiceNumber = et_innvoiceNumber.getText().toString().trim();
                String  stateCode=spinner.getSelectedItem().toString().trim();
                Log.d(TAG, "onClick:State CODE"+stateCode);

                if (invoiceNumber.length() > 0 && isDocUploaded)
                {
                    Intent intent = new Intent(AddPurchaseItem.this, AddPurchaseDetails.class);
                    intent.putExtra("v_id", String.valueOf(id.getVendorId()));
                    intent.putExtra("gst_nummber", gst_number);
                    intent.putExtra("invoiceDate", innvoiceDate);
                    intent.putExtra("invoiceNumber", invoiceNumber);
                    intent.putExtra("stateCode",stateCode);
                    Log.d(TAG, "onClick() called with: v = [" + intent.toString() + "]");
                    startActivity(intent);

                } else if(isDocUploaded)
                {
                    Toast.makeText(getApplicationContext(), " Add invoice Number", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), " Upload Docement", Toast.LENGTH_LONG).show();

                }

            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeInVoiceDate();

            }
        });

        sqlDataBase=new SqlDataBase(this);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));

        s_product_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item was selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Record g = (Record) parent.getItemAtPosition(pos);
                int v_id = g.getVendorId();
                getVendorDetailsByID(v_id);
            }

            public void onNothingSelected(AdapterView parent)
            {
                // Do nothing.
            }
        });

        inv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                deleteImage1();

            }
        });


        getVendorDetails();
        updateDate();
        getAllSuplier();

        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(s_product_type);

            // Set popupWindow height to 500px
            popupWindow.setHeight((int)height/2);

        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }


        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight((int)height/2);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }
    private void getAllSuplier()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);

        Volley.newRequestQueue(AddPurchaseItem.this).add(new StringRequest(Request.Method.GET, WebApi.GETSUPLER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject msg = null;
                        try
                        {
                            msg = new JSONObject(response);
                            String message = msg.getString("message");
                            if (message.equalsIgnoreCase("Data not available"))
                            {
                                Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                                loading.dismiss();

                            } else
                            {
                                try
                                {
                                    Log.d(TAG, "suplier:"+response);
                                    processData(response);
                                    loading.dismiss();
                                } catch (NullPointerException e)
                                {

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(AddPurchaseItem.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                        }

                    }
                })
        );


    }



    private void processData(String response)
    {
        S_parser S_parser =new S_parser(response);
        S_parser.productParser();
        SuppierAdapter productAdapter=new SuppierAdapter(AddPurchaseItem.this,R.layout.supplier_list, S_parser.stateName, S_parser.stateCode);
        spinner.setAdapter(productAdapter);


    }





    private void deleteImage1()
    {
        final CharSequence[] items = { "Delete Photo",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPurchaseItem.this);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Delete Photo"))
                {
                    try {
                        inv_photo.setImageDrawable(null);
                        BitmapDrawable bitmapDrawable2 = ((BitmapDrawable)inv_photo2.getDrawable());
                        if (inv_photo2.getDrawable() != null)
                        {
                            Bitmap bitmapImage2 = bitmapDrawable2.getBitmap();
                            inv_photo.setImageBitmap(bitmapImage2);
                            inv_photo2.setImageDrawable(null);
                        }

                    }catch (NullPointerException e)
                    {
                        Log.d("Exception",""+e);
                    }



                } else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void changeInVoiceDate() {

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

                updateDate(myCalendar);
            }

        };

        new DatePickerDialog(AddPurchaseItem.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));

    }

    private void getVendorDetailsByID(final int v_id) {


        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETVENDORBYID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    } else {
                        try {
                            loading.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("records");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                            gstNumber = jsonObject1.getString("gstin_no");
                            gstDetails = jsonObject1.getString("gst_details");
                            txt_gst_number.setText(gstNumber);
                            txt_gst_status.setText(gstDetails);


                        } catch (NullPointerException e) {

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(AddPurchaseItem.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("vendor_id", String.valueOf(v_id));
                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    private void getVendorDetails()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETVENDDOR_DETAILS, new Response.Listener<String>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Data not available")) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    } else {
                        try {

                            VendorList vendorList = new VendorList();
                            Gson gson = new Gson();
                            vendorList = gson.fromJson(response, vendorList.getClass());
                            vendorLists = (ArrayList<Record>) vendorList.getRecords();
                            VendorAdapterById vendorAdapterById = new VendorAdapterById(vendorLists, AddPurchaseItem.this);
                            s_product_type.setAdapter(vendorAdapterById);
                           // s_product_type.setPopupBackgroundResource(R.color.black);
                            Log.d(TAG, "onResponse: " + response);
                            loading.dismiss();
                        } catch (NullPointerException e) {

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(AddPurchaseItem.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

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


    private void updateDate() {
        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MMM-yy");
        // String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        String currentDateTimeString = dateFormat.format(dt);
        invoiceDate.setText(currentDateTimeString);

    }


    private void updateDate(Calendar myCalendar)
    {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MMM-yy");
        String currentDateTimeString = dateFormat.format(myCalendar.getTime());
        invoiceDate.setText(currentDateTimeString);
        Toast.makeText(getApplicationContext(), "select Date", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.take_inv_photo:
                String invoiceNumber = et_innvoiceNumber.getText().toString().trim();
                if(invoiceNumber.length()>0)
                {
                    selectImage();
                }else
                {
                    et_innvoiceNumber.setError("Invalid");
                }
                break;
        }

    }

    private void selectImage()
    {
        final CharSequence[] items = {"Choose from Library", "Take Photo","pdf",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPurchaseItem.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    boolean result = Utility.checkPermissionCamera(AddPurchaseItem.this);
                    userChoosenTask = "Take Photo";
                    if (requestPermissionForCamera())
                        cameraIntent();
                } else if (items[item].equals("Choose from Library"))
                {
                    boolean result = Utility.checkPermission(AddPurchaseItem.this);
                    userChoosenTask = "Choose from Library";
                    if (result)
                        if (checkPermissionForGallary())
                            galleryIntent();
                } else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }else
                    {

                    pdfFile();
                }
            }
        });
        builder.show();
    }

    private void pdfFile()
    {
        Intent cropIntent = new Intent();
        cropIntent.setType("application/pdf");
        cropIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(cropIntent, "Select File"), PICK_PDF);

    }

    public boolean checkPermissionForGallary()
    {
        int result = ContextCompat.checkSelfPermission(AddPurchaseItem.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public boolean requestPermissionForCamera()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddPurchaseItem.this, Manifest.permission.CAMERA))
        {
            Toast.makeText(AddPurchaseItem.this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            ActivityCompat.requestPermissions(AddPurchaseItem.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            return true;
        }
    }

    private void cameraIntent() {

       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, requestCode);
    }

    private void galleryIntent() {
        Intent cropIntent = new Intent();
        cropIntent.setType("image/jpg");
        cropIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(cropIntent, "Select File"), PICK_IMAGE_REQUEST);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            filePath = data.getData();

            Log.d(TAG, "onActivityResult: File Path"+filePath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                inv_photo.setImageBitmap(bitmap);
                Toast.makeText(this, "Please Upload Documnet ", Toast.LENGTH_SHORT).show();
                txt_fileName.setText(null);
                isDocUploaded=false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == PICK_PDF && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            filePath = data.getData();

            if (filePath.toString().startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getApplication().getContentResolver().query(filePath, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        txt_fileName.setText(fileName);
                        Toast.makeText(this, "Please Upload Documnet ", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onActivityResult: File Name"+fileName);
                        isDocUploaded=false;
                    }
                } finally {
                    cursor.close();
                }
            } else if (filePath.toString().startsWith("file://"))
            {
                File file=new File(filePath.toString());
                fileName = file.getName();
                txt_fileName.setText(fileName);
                Log.d(TAG, "onActivityResult: File"+fileName);
                isDocUploaded=false;
            }

            Log.d(TAG, "onActivityResult: File Path"+filePath);
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else
        {

            try
            {

             imageCamera = (Bitmap) data.getExtras().get("data");
            filePath =    getImageUri(getApplicationContext(),imageCamera);
                txt_fileName.setText(null);
                Log.d(TAG, "onActivityResult: File Path"+filePath);
                Toast.makeText(this, "Please Upload Documnet ", Toast.LENGTH_SHORT).show();
                inv_photo.setImageBitmap(imageCamera);
                isDocUploaded=false;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }



    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Purchase", null);
        return Uri.parse(path);
    }
    private void uploadImage()
    {

        if (filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child( globalPool.getDbname()+"/"+"purchase/" + UUID.randomUUID().toString());

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            Log.d(TAG, "onSuccess: "+taskSnapshot.getDownloadUrl());
                            progressDialog.dismiss();
                            uploloadToServer(et_innvoiceNumber.getText().toString().trim(),taskSnapshot.getDownloadUrl());
                            Toast.makeText(AddPurchaseItem.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddPurchaseItem.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }else
        {
            Toast.makeText(this, "File Path Not Uploaded", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploloadToServer(final String purchaseId, final Uri downloadUrl)
    {
        try {
            File file =createImageFile();
            Log.d(TAG, "uploloadToServer: "+file);
            Log.d(TAG, "uploloadToServer: Current Phot Path"+mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.UploadPurchaseImage, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try
                {
                    isDocUploaded=true;
                    inv_photo.setImageBitmap(null);
                    Log.d(TAG, "onResponse() called with: response = [" + response + "]");



                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(AddPurchaseItem.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
              //  params.put("p_id",createExpenseId());
                params.put("p_id", purchaseId);
                params.put("imageName",mCurrentPhotoPath);
                params.put("link",downloadUrl.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }
    private String createExpenseId() {
        String expName = et_innvoiceNumber.getText().toString().toLowerCase().trim();
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        imageFileId = expName + "/Purchase" + "JPEG_" + timeStamp + ".jpg";


        return imageFileId;


    }

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName =  "Purchase"+"JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    public String getReadableFileSize(long size)
    {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    public String getStringImage(Bitmap bmp) {
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        } catch (Exception e)
        {
            return null;
        }


    }


    public void showError(String errorMessage)
    {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }



    private void deleteImage(final ImageView imageView,final int imageCount)
    {
        final CharSequence[] items = { "Delete Photo",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPurchaseItem.this);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {

                if (items[item].equals("Delete Photo"))
                {
                    try
                    {
                        imageView.setImageDrawable(null);
                        String invoiceNumber = et_innvoiceNumber.getText().toString().trim();
                        int i = sqlDataBase.deleteInvoiceImage(invoiceNumber, imageCount);

                        if (i != -1)
                        {
                            Toast.makeText(getApplicationContext(), "Image Added Successfully", Toast.LENGTH_LONG).show();


                        } else
                        {

                            Toast.makeText(getApplicationContext(), "Can't Added", Toast.LENGTH_LONG).show();
                        }


                    }catch (NullPointerException e)
                    {
                        Log.d("Exception",""+e);
                    }



                } else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }




}

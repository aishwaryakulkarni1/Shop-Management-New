package com.inevitablesol.www.shopmanagement.expenses;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.product_info.adapter.ProductAdapter;
import com.inevitablesol.www.shopmanagement.settings.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class Add_expenses extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Add_expenses";
    private static final int EXP_TYPE = 0;
    private TextView txt_date;
    private ImageView img_date, add_expensesType;
    private AppCompatButton add_itemrow, bt_delete_rows;
    private Context context = Add_expenses.this;


    private SharedPreferences sharedpreferences;
    public final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private JSONArray jsonArray = new JSONArray();
    private JSONArray jsonArray2 = new JSONArray();
    private int i = 0;
    private int totalQty = 0;
    private double totalAmount = 0.0;
    private ArrayList<ExpensesListItems> expensesListItemses_new = new ArrayList<>();
    private ArrayList<ExpensesListItems> expensesListItemses = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText et_totalQty;
    private EditText et_subtotal;
    private EditText et_totalGst, et_otherCharges, et_totalAmount, et_balanceDue, et_amount_paid, et_exp_name;
    private EditText et_refnumber, et_description;
    private AppCompatButton save_expenses;
    private Spinner spinner, sp_type;
    private String userId;
    private ItemAdapter selectedItemAdapter;

    private Spinner spinner_supplier;

    // Exp Type

    private ArrayList<ExpensesType> arrayList = new ArrayList<>();
    private ArrayList<String> sp_data = new ArrayList<>();

    private CheckBox checkBox_full;
    private String typeofPayment;
    private AppCompatButton addImages, UploadImages;
    private ImageView exp_image;


    private int requestCode = 103;
    private int SELECT_FILE = 1;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private final int PICK_IMAGE_REQUEST = 71;

    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Bitmap exp_bitmap;
    private String mCurrentPhotoPath;
    private String imageFileId;
    private  static final int PICK_PDF = 201;
    private TextView txt_fileName;
    private String fileName;
    private  boolean isImageUploaded=true;
    private GlobalPool globalPool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        txt_date = (TextView) findViewById(R.id.txt_expDate);
        globalPool=(GlobalPool)this.getApplicationContext();
        img_date = (ImageView) findViewById(R.id.date_expDate);
        add_itemrow = (AppCompatButton) findViewById(R.id.add_expqty);
        add_itemrow.setOnClickListener(this);
        txt_fileName=(TextView)findViewById(R.id.fileName);
        add_expensesType = (ImageView) findViewById(R.id.add_exp);
        add_expensesType.setOnClickListener(this);
        sp_type = (Spinner) findViewById(R.id.spnn_exp_product_type);
        img_date.setOnClickListener(this);
        bt_delete_rows = (AppCompatButton) findViewById(R.id.exp_deleterow);
        bt_delete_rows.setOnClickListener(this);
        et_subtotal = (EditText) findViewById(R.id.exp_sub_total);
        et_totalGst = (EditText) findViewById(R.id.exp_totalGst);
        et_balanceDue = (EditText) findViewById(R.id.exp_balanceDue);
        et_totalAmount = (EditText) findViewById(R.id.exp_totalAmount);
        et_otherCharges = (EditText) findViewById(R.id.exp_otherCharges);
        et_amount_paid = (EditText) findViewById(R.id.exp_amount_paid);
        et_exp_name = (EditText) findViewById(R.id.et_exp_name);
        et_refnumber = (EditText) findViewById(R.id.et_exp_refNumber);
        et_description = (EditText) findViewById(R.id.et_exp_description);
        spinner = (Spinner) findViewById(R.id.spnn_type);
        checkBox_full = (CheckBox) findViewById(R.id.cb_fullexpenses);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        addImages = (AppCompatButton) findViewById(R.id.exp_addImages);
        UploadImages = (AppCompatButton) findViewById(R.id.exp_uploadImages);
        exp_image = (ImageView) findViewById(R.id.exp_image);
        addImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });
        UploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(et_exp_name.getText().toString().trim())) {
                    uploadImage();
                } else {
                    Toast.makeText(context, "Please Expense Name", Toast.LENGTH_SHORT).show();
                }


            }
        });


        Log.d(TAG, "onCreate: imageExpID" + imageFileId);


        checkBox_full.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    String amount = et_totalAmount.getText().toString().trim();
                    et_amount_paid.setText(amount);
                    et_amount_paid.setEnabled(false);

                }
                {
                    et_amount_paid.setEnabled(true);
                }

            }
        });

        save_expenses = (AppCompatButton) findViewById(R.id.save_Expenses);
        save_expenses.setOnClickListener(this);


        String[] paymentMode = {"Cash", "Debit Card payment", "Credit Card payment", "Net Banking", "E Wallet", "Paytm", "UPI apps", "Bhim App",
                "Using Banking details", "By Cheque", "EMI"};


        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.payment_mode,android.R.layout.simple_spinner_item);
        // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.payment_mode,android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_spinner, R.id.sp_item, paymentMode);
//
        //   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        et_amount_paid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    double paid = Double.parseDouble(et_amount_paid.getText().toString().trim());
                    double totalAmount = Double.parseDouble(et_totalAmount.getText().toString().trim());
                    double due = totalAmount - paid;
                    et_balanceDue.setText(String.valueOf(due));

                } catch (Exception e) {

                }


            }
        });


        et_otherCharges.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    double subtotal = Double.parseDouble(et_subtotal.getText().toString().trim());
                    double totalGst = Double.parseDouble(et_totalGst.getText().toString().trim());
                    double otherCharges = Double.parseDouble(et_otherCharges.getText().toString().trim());

                    double totalamnt = subtotal + totalGst + otherCharges;
                    et_totalAmount.setText(String.valueOf(totalamnt));


                } catch (NullPointerException e) {

                } catch (NumberFormatException e) {

                } catch (Exception e) {

                }

            }
        });

        et_totalGst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    double subtotal = Double.parseDouble(et_subtotal.getText().toString().trim());
                    double totalGst = Double.parseDouble(et_totalGst.getText().toString().trim());
                    double otherCharges = Double.parseDouble(et_otherCharges.getText().toString().trim());

                    double totalamnt = subtotal + totalGst + otherCharges;
                    et_totalAmount.setText(String.valueOf(totalamnt));


                } catch (NullPointerException e) {

                } catch (NumberFormatException e) {

                } catch (Exception e) {

                }

            }
        });


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));
        userId = sharedpreferences.getString("userId", "");
        getAllExpensesType();
        et_totalQty = (EditText) findViewById(R.id.ext_total_qty);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_exp);
        currentDate();


        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(sp_type);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            double wi = (double) width / (double) dm.xdpi;
            double hi = (double) height / (double) dm.ydpi;
            // Set popupWindow height to 500px
            popupWindow.setHeight((height / 2));
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {

        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Choose from Library", "Take Photo","Pdf",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Add_expenses.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    boolean result = Utility.checkPermissionCamera(Add_expenses.this);
                    // userChoosenTask = "Take Photo";
                    if (requestPermissionForCamera())
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    boolean result = Utility.checkPermission(Add_expenses.this);
//                    userChoosenTask = "Choose from Library";
                    if (result)
                        if (checkPermissionForGallary())
                            galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
                else {
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

    public boolean checkPermissionForGallary() {
        int result = ContextCompat.checkSelfPermission(Add_expenses.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public boolean requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Add_expenses.this, Manifest.permission.CAMERA)) {
            Toast.makeText(Add_expenses.this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            ActivityCompat.requestPermissions(Add_expenses.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();

            Log.d(TAG, "onActivityResult: File Path" + filePath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                exp_image.setImageBitmap(bitmap);
                isImageUploaded=false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(requestCode == PICK_PDF && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();

            if (filePath.toString().startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getApplication().getContentResolver().query(filePath, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        txt_fileName.setText(fileName);
                        isImageUploaded=false;
                        Log.d(TAG, "onActivityResult: File Name" + fileName);
                    }
                } finally {
                    cursor.close();
                }
            } else if (filePath.toString().startsWith("file://")) {
                File file = new File(filePath.toString());
                fileName = file.getName();
                txt_fileName.setText(fileName);
                Log.d(TAG, "onActivityResult: File" + fileName);
            }

            Log.d(TAG, "onActivityResult: File Path" + filePath);
            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imageView.setImageBitmap(bitmap);
                //uploadImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {


            try {
                exp_bitmap = (Bitmap) data.getExtras().get("data");
                filePath = getImageUri(getApplicationContext(), exp_bitmap);

                Log.d(TAG, "onActivityResult: File Path" + filePath);

            } catch (NullPointerException e) {
                Log.d("Null Pointer exception", e.getMessage());
            }

            try {
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                exp_image.setImageBitmap(exp_bitmap);
                isImageUploaded=false;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (requestCode == EXP_TYPE && resultCode == RESULT_OK) {
            String result = data.getStringExtra("MESSAGE");
            Log.d(TAG, "onActivityResult:" + result);
            getAllExpensesType();
            Log.d(TAG, "onActivityResult: ");
        }


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Purchase", null);
        return Uri.parse(path);
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child(globalPool.getDbname()+"/"+"expense/" + UUID.randomUUID().toString());

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, "onSuccess: " + taskSnapshot.getDownloadUrl());
                            progressDialog.dismiss();
                            try {
                                if (!TextUtils.isEmpty(et_exp_name.getText().toString().trim())) {
                                    uploloadToServer(taskSnapshot.getDownloadUrl());
                                } else
                                    {
                                    Toast.makeText(context, "Please Valid Expnese Name", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(Add_expenses.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Add_expenses.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        }
    }

    private void uploloadToServer(final Uri downloadUrl) {
        try {
            String file = createImageFile();
            Log.d(TAG, "uploloadToServer: " + file);
            Log.d(TAG, "uploloadToServer: Current Phot Path" + mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.UploadExpensesImage, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try {
                    exp_image.setImageBitmap(null);
                    isImageUploaded=true;
                    Log.d(TAG, "onResponse() called with: response = [" + response + "]");


                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(Add_expenses.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                params.put("dbname", dbname);
                params.put("imageCode", createExpenseId());
                params.put("imageName", mCurrentPhotoPath);
                params.put("imageLink", downloadUrl.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private String createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Expense" + "JPEG_" + timeStamp + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,
                /* prefix */ ".jpg"
                /* suffix */
                   /* directory */);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return imageFileName;
    }

    private String createExpenseId()
    {
        String expName = et_exp_name.getText().toString().toLowerCase().trim();
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        imageFileId = expName + "/Expense" + "JPEG_" + timeStamp + ".jpg";
        return imageFileId;
    }


    private void currentDate() {
        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(dt);
        txt_date.setText(currentDateTimeString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "onStop: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_expDate:
                //Toast.makeText(this, "Select Date Toast.LENGTH_SHORT",Toast.LENGTH_LONG).show();
                changeExpensesDate();
                break;

            case R.id.add_expqty:
                openDialogFor_item();
                break;

            case R.id.add_exp:
                startActivityForResult(new Intent(context, Add_expensesType.class), EXP_TYPE);
                break;

            case R.id.save_Expenses:
                String id = sp_type.getSelectedItem().toString().trim();
                Log.d(TAG, "onClick:Id" + id);
                if (id.equalsIgnoreCase("00")) {
                    Toast.makeText(context, "please Select Expense Type", Toast.LENGTH_LONG).show();
                } else {
                    save_Data();
                }

                break;
            case R.id.exp_deleterow:
                if (jsonArray.length() > 0) {
                    _delete_itemList();
                } else {
                    Toast.makeText(context, "Select Atleast One", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                Toast.makeText(context, "Wroung Choice", Toast.LENGTH_SHORT).show();
                break;


        }

    }


    private void _delete_itemList() {
        totalAmount = 0.0;
        totalQty = 0;
        Log.d(TAG, "_delete_itemList:before" + expensesListItemses);


        Iterator<ExpensesListItems> wishListItems_pojoIterator = expensesListItemses.iterator();
        Log.d(TAG, "_delete_itemList:Iterator" + wishListItems_pojoIterator);

        while (wishListItems_pojoIterator.hasNext()) {
            ExpensesListItems pj = wishListItems_pojoIterator.next();
            Log.d(TAG, "_deleteRow: pj" + pj + toString());
            if (pj.getIsChecked()) {
                i--;
                wishListItems_pojoIterator.remove();
            }

        }
        getDetails(expensesListItemses);
        DiplayListItem();
        if (expensesListItemses.isEmpty()) {
            selectedItemAdapter.clearView();
        }


    }

    private void getDetails(ArrayList<ExpensesListItems> expensesListItemses_new) {
        int j = 0;
        totalQty = 0;
        totalAmount = 0.0;

        try {
            jsonArray = new JSONArray();
            for (ExpensesListItems obj : expensesListItemses_new) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", obj.getItemName());
                jsonObject.put("qty", obj.getQty());
                jsonObject.put("unit_price", obj.getUnitPrice());
                jsonObject.put("total", obj.getTotalPrice());
                jsonArray.put(j, jsonObject);
                j++;
                totalQty += Integer.parseInt(obj.getQty());
                double total = Double.parseDouble(obj.getQty()) * Double.parseDouble(obj.getUnitPrice());
                // totalQty +=Integer.parseInt(obj.getQty());
                totalAmount += total;

                Log.d(TAG, "getDetails:" + jsonArray);
                Log.d(TAG, "getDetails:TotalQty" + totalQty);
                Log.d(TAG, "getDetails: TotalAmount" + totalAmount);

            }


        } catch (NullPointerException e) {

        } catch (JSONException e) {

        } catch (Exception e) {

        }


    }

    private void Diplay_new_List() {

        selectedItemAdapter.clearView();
        selectedItemAdapter.notifyDataSetChanged();
        et_totalQty.setText(String.valueOf(totalQty));
        et_subtotal.setText(String.valueOf(totalAmount));
        et_totalAmount.setText(String.valueOf(totalAmount));
        selectedItemAdapter = new ItemAdapter(expensesListItemses_new, Add_expenses.this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(selectedItemAdapter);

    }

    private void save_Data()
    {
        String date = txt_date.getText().toString().trim();

        Log.d(TAG, "save_Data:Date" + date);
        String id = sp_type.getSelectedItem().toString().trim();
        String expensive_name = et_exp_name.getText().toString().trim();
        String sub_total = et_subtotal.getText().toString().trim();
        String total_gst = et_totalGst.getText().toString().trim();
        String other_charges = et_otherCharges.getText().toString().trim();
        String totalAmount = et_totalAmount.getText().toString().trim();
        String amountPaid = et_amount_paid.getText().toString().trim();
        String balance = et_balanceDue.getText().toString().trim();
        typeofPayment = spinner.getSelectedItem().toString().trim();

        switch (typeofPayment) {

            case "Cash":
                typeofPayment = "Cash";
                break;
            case "Debit Card paymen":
                typeofPayment = "DC";
                break;
            case "Credit Card payment":
                typeofPayment = "CC";
                break;
            case "Net Banking":
                typeofPayment = "NB";
                break;
            case "E Wallet":
                typeofPayment = "EW";
                break;

            case "Paytm":
                typeofPayment = "Paytm";
                break;

            case "UPI apps":
                typeofPayment = "UPI";
                break;
            case "Bhim App":
                typeofPayment = "BHIM";
                break;

            case "Using Banking details":
                typeofPayment = "Bank";
                break;
            case "By Chequ":
                typeofPayment = "Cheque";
                break;
            case "EMI":
                typeofPayment = "EMI";
                break;
            default:


        }
        String ref_type = et_refnumber.getText().toString().trim();
        String description = et_description.getText().toString().trim();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("edate", date);
            jsonObject.put("exp_name", expensive_name);
            jsonObject.put("cat_id", id);
            jsonObject.put("sub_total", sub_total);
            jsonObject.put("total_gst", total_gst);
            jsonObject.put("total_qty", totalQty);
            jsonObject.put("other_charges", other_charges);
            jsonObject.put("total_amt", totalAmount);
            jsonObject.put("amount_paid", amountPaid);
            jsonObject.put("balance", balance);
            jsonObject.put("payment_mode", typeofPayment);
            jsonObject.put("ref_no", ref_type);
            jsonObject.put("description", description);
            jsonObject.put("created_by", userId);
            jsonObject.put("itemArray", jsonArray);

            Log.d(TAG, "save_Data:" + jsonObject);
            String jsonstr = jsonObject.toString();
            String formatedString = jsonstr.replaceAll("\\\\", "");
            Log.d(TAG, "save_Data:" + formatedString);

            if (sub_total.length() > 0 && totalQty > 0 && jsonArray.length() > 0 && expensive_name.length() > 0)
            {
                 if(isImageUploaded)
                 {
                     save_toDB(formatedString);
                 }else {
                     Toast.makeText(context, " Please Upload Image", Toast.LENGTH_SHORT).show();
                 }

            } else {
                Toast.makeText(context, "Please add All Values", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }

    }

    private void save_toDB(final String formatedString) {
        final ProgressDialog loading = ProgressDialog.show(this, "saving ...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.CREATE_EXPENSESE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.toString().trim();
                Log.d(" Added Successfully", resp);
                try {
                    loading.dismiss();

                    JSONObject msg = new JSONObject(response);
                    String message = msg.getString("message");

                    if (message.equalsIgnoreCase("Expense Added"))
                    {
                        Intent intent = new Intent(context, Expenses_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Expense Added Succesfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Can not Add Details", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                params.put("data", formatedString);
                params.put("imageCode", createExpenseId());
                Log.d(TAG, "getParamsWithImage: " + params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void openDialogFor_item() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.exp_item_details);
        dialog.setTitle("Add item Details");
        final EditText et_itemName = (EditText) dialog.findViewById(R.id.exp_itemname);
        final EditText et_itemqty = (EditText) dialog.findViewById(R.id.exp_qty);
        final AppCompatButton cancel = (AppCompatButton) dialog.findViewById(R.id.exp_cancel);
        dialog.setCancelable(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final EditText et_unitprice = (EditText) dialog.findViewById(R.id.exp_unitPrice);
        AppCompatButton add = (AppCompatButton) dialog.findViewById(R.id.exp_addlist);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = et_itemName.getText().toString().trim();
                String qty = et_itemqty.getText().toString().trim();
                String unitPrice = et_unitprice.getText().toString().trim();
                if (name.length() > 0 && qty.length() > 0 && unitPrice.length() > 0)
                {
                    getDetails(name, qty, unitPrice);

                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Plase add All field", Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialog.show();

    }

    private void getDetails(String name, String qty, String unitPrice)
    {
        double total = Double.parseDouble(qty) * Double.parseDouble(unitPrice);
        totalQty += Integer.parseInt(qty);
        totalAmount += total;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("qty", qty);
            jsonObject.put("unit_price", unitPrice);
            jsonObject.put("total", String.valueOf(total));
            jsonArray.put(i, jsonObject);
            i++;

            Log.d(TAG, "getDetails:" + jsonArray);
            Log.d(TAG, "getDetails:TotalQty" + totalQty);
            Log.d(TAG, "getDetails: TotalAmount" + totalAmount);

            ExpensesListItems expensesListItems = new ExpensesListItems();
            expensesListItems.setItemName(name);
            expensesListItems.setQty(qty);
            expensesListItems.setUnitPrice(unitPrice);
            expensesListItems.setTotalPrice(String.valueOf(total));
            expensesListItems.setIsChecked(false);
            expensesListItemses.add(expensesListItems);
            Log.d(TAG, "getDetails:Object" + expensesListItems.toString());

            DiplayListItem();


        } catch (NullPointerException e) {

        } catch (JSONException e) {

        } catch (Exception e) {

        }


    }

    private void DiplayListItem()
    {
        et_totalQty.setText(String.valueOf(totalQty));
        et_subtotal.setText(String.valueOf(totalAmount));
        et_totalAmount.setText(String.valueOf(totalAmount));
        selectedItemAdapter = new ItemAdapter(expensesListItemses, Add_expenses.this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(selectedItemAdapter);


    }


    private void changeExpensesDate() {

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

        new DatePickerDialog(Add_expenses.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));

    }

    private void updateDate(Calendar myCalendar) {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(myCalendar.getTime());
        txt_date.setText(currentDateTimeString);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        if(requestCode== EXP_TYPE && resultCode ==RESULT_OK)
//        {
//            String result = data.getStringExtra("MESSAGE");
//            Log.d(TAG, "onActivityResult:"+result);
//                  getAllExpensesType();
//            Log.d(TAG, "onActivityResult: ");
//        }
//    }


    private void getAllExpensesType() {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_NEW_PRODUCT_EXP, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try {
                    Log.d("response", response);


                    Exp_Adapter exp_adapter = new Exp_Adapter(response);
                    exp_adapter.productParser();

                    ProductAdapter productAdapter = new ProductAdapter(Add_expenses.this, R.layout.product_list, Exp_Adapter.productName, Exp_Adapter.productId);
                    sp_type.setAdapter(productAdapter);
//

                } catch (Exception e) {
                    Log.e(TAG, "onResponse: ", e);
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(Add_expenses.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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

}


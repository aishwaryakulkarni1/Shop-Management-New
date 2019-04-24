package com.inevitablesol.www.shopmanagement.account;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import com.google.gson.Gson;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.Validation.Validation;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.settings.Utility;
import com.inevitablesol.www.shopmanagement.vendor_module.*;
import com.inevitablesol.www.shopmanagement.vendor_module.Record;
import com.squareup.picasso.Picasso;

import org.apache.poi.ss.formula.functions.EDate;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Edit_User extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText user_name,user_email,user_dateofbirth,user_mobile,user_empcode;
    private AppCompatButton edit_user;

    private GlobalPool globalPool;
    private String userRole;
    private Spinner spinner;
    private static final String TAG = "Edit_User";
    private ArrayList<com.inevitablesol.www.shopmanagement.account.Record> userClass;
    private Spinner userData;
    private TextInputEditText user_passWord;
    private Integer u_id;


    private final int PICK_IMAGE_REQUEST = 71;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;

    private ImageView imageView;
    private String userChoosenTask;
    private int requestCode = 103;
    private Uri filePath;
    private Bitmap imageCamera;

    FirebaseStorage storage;
    StorageReference storageReference;
    private String fileName;
    private Uri downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__user);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        globalPool = (GlobalPool) this.getApplication();
        user_name=(TextInputEditText)findViewById(R.id.user_name);
        spinner=(Spinner)findViewById(R.id.spnn_role);
        userData=(Spinner)findViewById(R.id.spnn_User);
        user_email=(TextInputEditText)findViewById(R.id.user_email);
        user_dateofbirth=(TextInputEditText)findViewById(R.id.user_dateofbirth);
        user_mobile=(TextInputEditText)findViewById(R.id.user_mobile);
        user_empcode=(TextInputEditText)findViewById(R.id.user_empcode);
        edit_user =(AppCompatButton)findViewById(R.id.edit_user);
        user_empcode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        user_passWord=(TextInputEditText)findViewById(R.id.user_password) ;
        edit_user.setOnClickListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_role, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        userRole=globalPool.getUserRole();

        int position=     adapter.getPosition(userRole);
        Log.d(TAG, "onCreate: Postion"+position);
        spinner.setSelection(position);

        imageView=(ImageView)findViewById(R.id.imageProfile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                browseImage();


            }
        });
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);
            android.widget.ListPopupWindow popupWindow1 = (android.widget.ListPopupWindow) popup.get(userData);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            double wi = (double) width / (double) dm.xdpi;
            double hi = (double) height / (double) dm.ydpi;
            // Set popupWindow height to 500px
            popupWindow.setHeight((height / 2));
            popupWindow1.setHeight((height / 2));
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {

        }


        getUser();



    }

    private void browseImage()
    {

        final CharSequence[] items = {"Choose from Library", "Take Photo",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_User.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    boolean result = Utility.checkPermissionCamera(Edit_User.this);
                    userChoosenTask = "Take Photo";
                    if (requestPermissionForCamera())
                        cameraIntent();
                } else if (items[item].equals("Choose from Library"))
                {
                    boolean result = Utility.checkPermission(Edit_User.this);
                    userChoosenTask = "Choose from Library";
                    if (result)
                        if (checkPermissionForGallary())
                            galleryIntent();
                } else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public boolean checkPermissionForGallary() {
        int result = ContextCompat.checkSelfPermission(Edit_User.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public boolean requestPermissionForCamera()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Edit_User.this, Manifest.permission.CAMERA))
        {
            Toast.makeText(Edit_User.this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            ActivityCompat.requestPermissions(Edit_User.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            return true;
        }
    }

    private void cameraIntent()
    {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, requestCode);
    }

    private void galleryIntent() {
        Intent cropIntent = new Intent();
        cropIntent.setType("image/jpg");
        cropIntent.setAction(Intent.ACTION_GET_CONTENT);
        //cropIntent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(cropIntent, "Select File"), PICK_IMAGE_REQUEST);
    }
    private void openpdf()
    {
        Intent cropIntent = new Intent();
        cropIntent.setType("application/pdf");
        cropIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(cropIntent, "Select File"), PICK_IMAGE_REQUEST);

    }

    private void getUser()
    {

        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_SUB_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, "onResponse:  Users" + response);
                JSONObject msg = null;
                try {
                    loading.dismiss();
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                      SubuserData subuserData=new SubuserData();
                       Gson gson=new Gson();
                                 subuserData=gson.fromJson(response,subuserData.getClass());
                                  userClass=(ArrayList<com.inevitablesol.www.shopmanagement.account.Record>)subuserData.getRecords();
                    final ArrayAdapter<com.inevitablesol.www.shopmanagement.account.Record> adapter =
                            new ArrayAdapter<com.inevitablesol.www.shopmanagement.account.Record>(getApplicationContext(), R.layout.spinner_item, userClass);
                    adapter.setDropDownViewResource(R.layout.spinner_item);


                    userData.setAdapter(adapter);
                    userData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {

                            com.inevitablesol.www.shopmanagement.account.Record record= adapter.getItem(position);
                            Log.d(TAG, "onItemClick: Record"+record.toString());
                            Log.d(TAG, "onItemSelected: User ID"+record.getUId());
                            updateFiled(record);
//
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent)
                        {

                        }
                    });


//                    spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//                        {
//                                                     com.inevitablesol.www.shopmanagement.account.Record record= adapter.getItem(position);
//                            Log.d(TAG, "onItemClick: Record"+record.toString());
//
//                        }
//                    });

//                                  SubUser_adapter subUser_adapter=new SubUser_adapter(userClass);
//                                  userData.setAdapter( subUser_adapter);


//                    VendorList vendorList=new VendorList();
//                    Gson gson=new Gson();
//                    vendorList= gson.fromJson(response,vendorList.getClass());
//                    vendorLists=(ArrayList<Record>)vendorList.getRecords();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(Edit_User.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", globalPool.getDbname());
                 params.put("dbname", globalPool.getDbname());


                //  params.put("dob", userDateofbirth);
                Log.d(TAG, "getParams:BankDetails" + params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void updateFiled(com.inevitablesol.www.shopmanagement.account.Record record)
    {
        user_email.setText(record.getUEmail());
//        user_dateofbirth.setText(record.getDob());
        user_empcode.setText(record.getEcode());
        user_mobile.setText(record.getUNumber());
        user_name.setText(record.getUName());
        user_passWord.setText(record.getUPass());

        if(!record.getLink().isEmpty())
        {
            try {
                Picasso.with(this).load(record.getLink()).fit().centerInside().into(imageView);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }else
            {
            imageView.setImageBitmap(null);
        }

        u_id=record.getUId();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            filePath = data.getData();

            if (filePath.toString().startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getApplication().getContentResolver().query(filePath, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d(TAG, "onActivityResult: File Name"+fileName);
                    }
                } finally {
                    cursor.close();
                }
            } else if (filePath.toString().startsWith("file://"))
            {
                File file=new File(filePath.toString());
                fileName = file.getName();
                Log.d(TAG, "onActivityResult: File"+fileName);
            }

            Log.d(TAG, "onActivityResult: File Path"+filePath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
        {



            imageCamera = (Bitmap) data.getExtras().get("data");
            filePath =    getImageUri(getApplicationContext(),imageCamera);

            Log.d(TAG, "onActivityResult: File Path"+filePath);

            try
            {
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(imageCamera);
                uploadImage();
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

            StorageReference ref = storageReference.child("SubUser_images/" + "photo"+System.currentTimeMillis());

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            downloadUrl=taskSnapshot.getDownloadUrl();
                            Log.d(TAG, "onSuccess: "+taskSnapshot.getDownloadUrl());
                            progressDialog.dismiss();
                            Toast.makeText(Edit_User.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Edit_User.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v)
    {

           switch (v.getId())
           {
               case R.id.edit_user:
                   saveData();
                   break;
           }

    }

    private void saveData()
    {
        final String userName=user_name.getText().toString().trim();
        final String  usermobile=user_mobile.getText().toString().trim();
        final String  useremail=user_email.getText().toString().trim();
        //  final String  userDateofbirth=user_dateofbirth.getText().toString().trim();
        final String empCode=user_empcode.getText().toString().trim();
        final String     role=spinner.getSelectedItem().toString().trim();
        final String userPassword=user_passWord.getText().toString().trim().trim();

        if(validateForm())
        {

            final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.USER_EDIT, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {
                    loading.dismiss();
                    Log.d(TAG, "onResponse: " + response);
                    JSONObject msg = null;
                    try {
                        msg = new JSONObject(response);
                        String message = msg.getString("message");
                        if (message.equalsIgnoreCase("succesfully updated")) {
                            sendSms(usermobile, userPassword);

                            finish();
                            Toast.makeText(Edit_User.this, "succesfully updated", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Edit_User.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                    }

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    Log.d("dbname", globalPool.getDbname());
                    params.put("dbname", globalPool.getDbname());
                    params.put("name", userName);
                    params.put("u_id", String.valueOf(u_id));
                    params.put("user_type", role);
                    params.put("mobile_number", usermobile);
                    params.put("email_id", useremail);
                    params.put("ecode", empCode);
                    params.put("link", String.valueOf(downloadUrl));
                    params.put("password", userPassword);
                    //  params.put("dob", userDateofbirth);
                    Log.d(TAG, "getParams:BankDetails" + params.toString());

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }else
            {
            Toast.makeText(Edit_User.this, "Please Add Valid Data", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean validateForm()
    {
        boolean flag=true;


        if(!Validation.hasText(user_name))
            return flag=false;

        if(!Validation.isValidPhone(user_mobile.getText().toString().trim()))
            return flag=false;

        if(!Validation.isEmailValid(user_email.getText().toString().trim()))
            return flag;
        if(!Validation.hasText(user_empcode))
            return flag=false;

        return flag;


    }

    private void sendSms(String usermobile, String userPassword)
    {
        String message = "Dear User, \n  Your User Id Has been created on M-Hourz.Please find Login \n Credentials Below \n  Mobile Number :"+usermobile + "Password :"+userPassword +
                "\n Thanks for Working With us \n "+globalPool.getShopName() ;
        String mobile = usermobile;


        String uri = Uri.parse("\n" +
                "http://bhashsms.com/api/sendmsg.php?")
                .buildUpon()
                .appendQueryParameter("user", "TEAM_MHOURZ")
                .appendQueryParameter("pass", "MECHATRON")
                .appendQueryParameter("text", message)
                .appendQueryParameter("sender", "MHOURZ")
                .appendQueryParameter("phone", mobile)
                .appendQueryParameter("priority", "ndnd")
                .appendQueryParameter("stype", "normal")
                .build().toString();

        Log.d(TAG, "TestMEssage: Uri"+uri);

        final Context context = getApplicationContext();
        StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null) {
                                JSONObject j = new JSONObject(response);
                                String type1 = j.getString("type");
                                if (type1.contains("success"))
                                {
                                    Toast.makeText(getApplication(), " message sent successfully", Toast.LENGTH_LONG).show();
                                } else
                                {
                                    Toast.makeText(getApplication(), "Message couldn't reach you, try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}

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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.inevitablesol.www.shopmanagement.Validation.Validation;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.purchase_module.AddPurchaseItem;
import com.inevitablesol.www.shopmanagement.settings.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Add_User extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText user_name,user_email,user_dateofbirth,user_mobile,user_empcode;
    private TextInputEditText user_passWord;
    private AppCompatButton save_user,sendsms;

    private GlobalPool globalPool;
    private String userRole;
    private Spinner spinner;
    private static final String TAG = "Add_User";

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__user);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        globalPool = (GlobalPool) this.getApplication();
        user_name=(TextInputEditText)findViewById(R.id.user_name);
        spinner=(Spinner)findViewById(R.id.spnn_role);
        user_email=(TextInputEditText)findViewById(R.id.user_email);
        user_dateofbirth=(TextInputEditText)findViewById(R.id.user_dateofbirth);
        user_mobile=(TextInputEditText)findViewById(R.id.user_mobile);
        user_empcode=(TextInputEditText)findViewById(R.id.user_empcode);
        user_passWord=(TextInputEditText)findViewById(R.id.user_password);
        save_user=(AppCompatButton)findViewById(R.id.user_save);
        sendsms=(AppCompatButton)findViewById(R.id.sendsms);
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
        sendsms.setOnClickListener(this);
        save_user.setOnClickListener(this);
        user_empcode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_role, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void browseImage()
    {

        final CharSequence[] items = {"Choose from Library", "Take Photo",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Add_User.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo"))
                {
                    boolean result = Utility.checkPermissionCamera(Add_User.this);
                    userChoosenTask = "Take Photo";
                    if (requestPermissionForCamera())
                        cameraIntent();
                } else if (items[item].equals("Choose from Library"))
                {
                    boolean result = Utility.checkPermission(Add_User.this);
                    userChoosenTask = "Choose from Library";
                    if (result)
                        if (checkPermissionForGallary())
                            galleryIntent();
                } else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }else
                    {

                    openpdf();
                }
            }
        });
        builder.show();
    }

    public boolean checkPermissionForGallary() {
        int result = ContextCompat.checkSelfPermission(Add_User.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        } else {
            return false;
        }
    }


    public boolean requestPermissionForCamera()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Add_User.this, Manifest.permission.CAMERA))
        {
            Toast.makeText(Add_User.this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            return false;
        } else
            {
            ActivityCompat.requestPermissions(Add_User.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
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


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.user_save:
                getDatails();
              break;
            case R.id.sendsms:
                getNumber();
                break;
              default:
                  Toast.makeText(Add_User.this, "Add_User", Toast.LENGTH_SHORT).show();


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            filePath = data.getData();

            if (filePath.toString().startsWith("content://"))
            {
                Cursor cursor = null;
                try {
                    cursor = getApplication().getContentResolver().query(filePath, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst())
                    {
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
                            Toast.makeText(Add_User.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Add_User.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Purchase", null);
        return Uri.parse(path);
    }

    private void getNumber()
    {
        final String  usermobile=user_mobile.getText().toString().trim();
        final String userPassword=user_passWord.getText().toString().trim().trim();
        if(usermobile.length()>0)
        {
            sendSms(usermobile,userPassword);

        }else {
            Toast.makeText(Add_User.this, "Add Mobile Number", Toast.LENGTH_SHORT).show();
        }



    }

    private void getDatails()
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
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.USER_CREATE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    Log.d(TAG, "onResponse: " + response);
                    JSONObject msg = null;
                    try {
                        msg = new JSONObject(response);
                        String message = msg.getString("message");
                        if (message.equalsIgnoreCase("Add User succesfully"))
                        {
                            sendSms(usermobile,userPassword);

                            finish();
                            Toast.makeText(Add_User.this, "succesfully added", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    if (error instanceof NoConnectionError)
                    {
                        Toast.makeText(Add_User.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                    }

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    Log.d("dbname", globalPool.getDbname());
                    params.put("dbname", globalPool.getDbname());
                    params.put("name", userName);
                    params.put("u_id", globalPool.getUserId());
                    params.put("user_type", role);
                    params.put("mobile_number", usermobile);
                    params.put("email_id", useremail);
                    params.put("ecode", empCode);
                    params.put("link", String.valueOf(downloadUrl));
                    params.put("password", userPassword);
                    Log.d(TAG, "getParams:BankDetails" + params.toString());

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }else if(usermobile.length()<10)
            {

            Toast.makeText(Add_User.this, "Please Add Valid mobile", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(Add_User.this, "Please Add Valid Email", Toast.LENGTH_SHORT).show();
        }





    }

    private void sendSms(String usermobile, String userPassword)
    {

        String message = "Dear User, \n  Your User Id Has been created on M-Hourz.  Please find Login \n Credentials Below \n  Mobile Number :"+usermobile + "Password :"+userPassword +
                "\n Thanks for Working With us.. \n "+globalPool.getShopName() ;
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
}

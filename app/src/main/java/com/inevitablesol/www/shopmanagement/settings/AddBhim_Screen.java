package com.inevitablesol.www.shopmanagement.settings;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddBhim_Screen extends Activity implements View.OnClickListener
{

    private final int requestCode = 20;
    private static final int SELECT_FILE = 1;
    public  String   userChoosenTask;
    private static final int CAMERA_PERMISSION_REQUEST_CODE =101 ;
    ImageView _photo;
    AppCompatButton take_photo;
    AppCompatButton uploadImage;
    private ProgressDialog loading;
    private SqlDataBase sqlDataBase;
    private static final String TAG = "AddBhim_Screen";


    FirebaseStorage storage;
    StorageReference storageReference;
    private String fileName;
    private Uri downloadUrl;
    private GlobalPool globalPool;
    private Uri filePath;
    private Bitmap imageCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_bhim__screen);
        take_photo=(AppCompatButton)findViewById(R.id.take_screen);
        take_photo.setOnClickListener(this);

        globalPool = (GlobalPool) this.getApplication();
        _photo=(ImageView)findViewById(R.id.bhimScreen);
        uploadImage=(AppCompatButton)findViewById(R.id.uploadBhi_Screen);
        uploadImage.setOnClickListener(this);
        sqlDataBase=new SqlDataBase(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private void selectImage()
    {
        final CharSequence[] items = { "Choose from Library","Take Photo",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddBhim_Screen.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {

                if (items[item].equals("Take Photo"))
                {
                    boolean result=Utility.checkPermissionCamera(AddBhim_Screen.this);
                    userChoosenTask="Take Photo";
                    if(requestPermissionForCamera())
                        cameraIntent();
                } else
                if (items[item].equals("Choose from Library"))
                {
                    boolean result=Utility.checkPermission(AddBhim_Screen.this);
                    userChoosenTask="Choose from Library";
                    if(result)
                        if(checkPermissionForGallary())
                            galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public Uri getImageUri(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public boolean requestPermissionForCamera(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddBhim_Screen.this, Manifest.permission.CAMERA))
        {
            Toast.makeText(AddBhim_Screen.this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            return  false;
        } else {
            ActivityCompat.requestPermissions(AddBhim_Screen.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
            return true;
        }
    }


    private void galleryIntent()
    {
        Intent cropIntent = new Intent();
        cropIntent.setType("image/jpg");
        cropIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(cropIntent, "Select File"),SELECT_FILE);
    }

    private void showPhoto(Uri photoUri){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoUri, "image/*");
        startActivity(intent);
    }

    public boolean checkPermissionForGallary()
    {
        int result = ContextCompat.checkSelfPermission(AddBhim_Screen.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else
        {
            return false;
        }
    }

    private void cameraIntent()
    {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK)
        {
            if(resultCode != RESULT_CANCELED)
            {
                filePath = data.getData();

                if (filePath.toString().startsWith("content://"))
                {
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


                imageCamera = (Bitmap) data.getExtras().get("data");
                filePath =    getImageUri(getApplicationContext(),imageCamera);

                Log.d(TAG, "onActivityResult: File Path"+filePath);

                try
                {
                    //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    _photo.setImageBitmap(imageCamera);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
        if(this.SELECT_FILE== requestCode && resultCode == RESULT_OK)
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
                _photo.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.take_screen:
                selectImage();
                break;
            case R.id.uploadBhi_Screen:
                uploadImage();
                  //uploadToDataBase();
                break;
        }

    }

    private void uploadToDataBase()
    {
        try
        {
            loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) _photo.getDrawable();
            String Image1 = getStringImage(bitmapDrawable.getBitmap());
            if(Image1!=null)
            {
                int i = sqlDataBase.addBhimAppScreen(Image1);
                if (i != -1)
                {
                    Toast.makeText(getApplicationContext(), "Image Added Successfully", Toast.LENGTH_LONG).show();
                    loading.dismiss();
                    finish();
                } else
                {
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), "Can't Added", Toast.LENGTH_LONG).show();

                }
            }else
            {
                Toast.makeText(getApplicationContext(),"Please Select Image",Toast.LENGTH_LONG).show();
                loading.dismiss();
            }
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Please Select Image",Toast.LENGTH_LONG).show();
            loading.dismiss();

            Log.d(TAG, "uploadToServer: ");
        }
    }

    public String getStringImage(Bitmap bmp)
    {
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }catch (Exception e)
        {
            return null;
        }

    }




    private void uploadImage()
    {

        if (filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child( globalPool.getDbname()+"/PaytmScreen/" +createImageFile());

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            downloadUrl=taskSnapshot.getDownloadUrl();
                            Log.d(TAG, "onSuccess: "+taskSnapshot.getDownloadUrl());
                            progressDialog.dismiss();
                            saveDocument();
                            Toast.makeText(AddBhim_Screen.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddBhim_Screen.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private String  createImageFile()
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName =  "Paytm/" + timeStamp + "_";

        return imageFileName;
    }

    private void saveDocument()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.Upload_DOcs, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                Log.d(TAG, "onResponse: " + response);
                JSONObject msg = null;
                try {
                    msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("Image Upoaded"))
                    {

                        Toast.makeText(AddBhim_Screen.this, "succesfully Uploaded", Toast.LENGTH_SHORT).show();
                        finish();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(AddBhim_Screen.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", globalPool.getDbname());
                params.put("dbname", globalPool.getDbname());
                params.put("imageName",fileName);
                params.put("type","Bhim");
                params.put("shopId", globalPool.getShopId());
                params.put("link", String.valueOf(downloadUrl));
                Log.d(TAG, "getParams:DOC Details" + params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

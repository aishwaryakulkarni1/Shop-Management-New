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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.inevitablesol.www.shopmanagement.settings.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class Add_Document extends AppCompatActivity
{

    private static final int PICK_PDF = 201;
    private Spinner mspinner;


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

    private static final String TAG = "Add_Document";

    private AppCompatButton saveDoc;
    private GlobalPool globalPool;
    private AppCompatButton browseFile;
    private TextView imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__document);
        globalPool = (GlobalPool) this.getApplication();
        imageView=(ImageView)findViewById(R.id.imageview);
        saveDoc=(AppCompatButton)findViewById(R.id.save_doc);
        imageName=(TextView)findViewById(R.id.imageName);
        browseFile=(AppCompatButton)findViewById(R.id.browseFile);
        browseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pdfFile();
            }
        });
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        saveDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveDocument();
            }
        });
        mspinner=(Spinner)findViewById(R.id.no_of_doc);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shop_document, android.R.layout.simple_spinner_item);
        mspinner.setAdapter(adapter);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                browseImage();


            }
        });
    }

    private void pdfFile()
    {
        Intent cropIntent = new Intent();
        cropIntent.setType("application/pdf");
        cropIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(cropIntent, "Select File"), PICK_PDF);

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



                        Toast.makeText(Add_Document.this, "succesfully Uploaded", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Add_Document.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", globalPool.getDbname());
                params.put("dbname", globalPool.getDbname());
                params.put("imageName",fileName);
                params.put("type",mspinner.getSelectedItem().toString());
                params.put("shopId", globalPool.getShopId());
                params.put("link", String.valueOf(downloadUrl));
                Log.d(TAG, "getParams:DOC Details" + params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void browseImage()
    {

        final CharSequence[] items = {"Choose Image from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Add_Document.this);
        builder.setTitle("Add Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo"))
                {
                    boolean result = Utility.checkPermissionCamera(Add_Document.this);
                    userChoosenTask = "Take Photo";
                    if (requestPermissionForCamera())
                        cameraIntent();
                } else if (items[item].equals("Choose Image from Library"))
                {
                    boolean result = Utility.checkPermission(Add_Document.this);
                    userChoosenTask = "Choose from Library";
                    if (result)
                        if (checkPermissionForGallary())
                            galleryIntent();
                } else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
                else {
                    openpdf();
                }
            }
        });
        builder.show();
    }

    public boolean checkPermissionForGallary()
    {
        int result = ContextCompat.checkSelfPermission(Add_Document.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        } else {
            return false;
        }
    }


    public boolean requestPermissionForCamera()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Add_Document.this, Manifest.permission.CAMERA))
        {
            Toast.makeText(Add_Document.this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            ActivityCompat.requestPermissions(Add_Document.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
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
        startActivityForResult(Intent.createChooser(cropIntent, "Select File"), PICK_PDF);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            try
            {
            filePath = data.getData();
            Log.d(TAG, "onActivityResult: File PATH"+filePath);

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

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(requestCode == PICK_PDF && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            try {
            filePath = data.getData();

            if (filePath.toString().startsWith("content://"))
            {
                Cursor cursor = null;
                try {
                    cursor = getApplication().getContentResolver().query(filePath, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst())
                    {
                        fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        imageName.setText(fileName);
                        Log.d(TAG, "onActivityResult: File Name"+fileName);
                    }
                } finally {
                    cursor.close();
                }
            } else if (filePath.toString().startsWith("file://"))
            {
                File file=new File(filePath.toString());
                fileName = file.getName();
                imageName.setText(fileName);
                Log.d(TAG, "onActivityResult: File"+fileName);
            }

            Log.d(TAG, "onActivityResult: File Path"+filePath);

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadImage();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else
        {







            try
            {
                imageCamera = (Bitmap) data.getExtras().get("data");
                filePath =    getImageUri(getApplicationContext(),imageCamera);
                Log.d(TAG, "onActivityResult: File PATH"+filePath);
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
                            Toast.makeText(Add_Document.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(Add_Document.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public Uri getImageUri(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Purchase", null);
        return Uri.parse(path);
    }
}

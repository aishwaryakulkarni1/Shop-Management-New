package com.inevitablesol.www.shopmanagement.settings;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FileBody;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;
import java.security.cert.CertPathBuilder;

public class Import_Item extends AppCompatActivity {

    private AppCompatButton add_file;
    private int PICK_XlS=101;
    private int PICK_PDF=102;
    private Uri filePath;
    private String fileName;
    private TextView txt_fileName;
    private AppCompatButton bt_upload;
    private NotificationManager mNotifyManager;
    private CertPathBuilder mBuilder;

    private GlobalPool globalPool;
    private static final String TAG = "Import_Item";
    private AppCompatButton downloadFile;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import__item);
        txt_fileName=(TextView)findViewById(R.id.txt_fileName);
        bt_upload=(AppCompatButton)findViewById(R.id.bt_uploadfile);
        globalPool=(GlobalPool)this.getApplicationContext();
        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_upload.setClickable(false);
                uploadFile();
            }
        });
        downloadFile=(AppCompatButton)findViewById(R.id.downloadFile);
        downloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/uc?export=download&id=10lzJtP-T7mKPnOA9FMwGG67Zv5SnGxwD")));



            }
        });

        add_file=(AppCompatButton)findViewById(R.id.add_file);
        add_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                pdfFile();
            }
        });
    }

    private void uploadFile()
    {

        if(isStoragePermissionGranted())
        {
            File file = new File(filePath.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                Log.d(TAG, "uploadFile: New Change" + FilePath.getPath(this, filePath));


                FileBody fileBody = new FileBody(file);

                Log.d(TAG, "uploadFile: New Path" + new File(FilePath.getPath(this, filePath)));


                if(fileName.contains("Items Import"))
                {
                    Ion.with(this)
                            .load("POST", "http://35.161.99.113:9000/webapi/product/itemimportdata")
                            .uploadProgressHandler(new ProgressCallback()
                            {
                                @Override
                                public void onProgress(long uploaded, long total) {
                                    Log.d(TAG, "onProgress: " + uploaded + "/" + total);
                                    // Displays the progress bar for the first time.

                                }
                            })
                            .setTimeout(60 * 60 * 1000)
                            .setMultipartFile("file", "application/csv",  new File(FilePath.getPath(this, filePath)))
                            .setMultipartParameter("dbname", globalPool.getDbname())
                            .asJsonObject()
                            // run a callback on completion
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    // When the loop is finished, updates the notification
                                    Log.d(TAG, "onCompleted: File" + result);
                                    Log.d(TAG, "onCompleted: Exception" + e);
                                    Toast.makeText(getApplicationContext(), "Item uploaded successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });

                }else
                {
                    Toast.makeText(globalPool, "Select Customer xls only", Toast.LENGTH_SHORT).show();

                }


            } else
            {
                Log.d(TAG, "uploadFile: " + checkPermissionForGallary());
                Toast.makeText(globalPool, "Please Check Read Permision", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void pdfFile()
    {
        try {
            Intent cropIntent = new Intent();
            cropIntent.setAction(Intent.ACTION_GET_CONTENT);
            cropIntent.addCategory(Intent.CATEGORY_OPENABLE);
            cropIntent.setType("text/csv");

            startActivityForResult(Intent.createChooser(cropIntent, "Select File"), PICK_PDF);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public  boolean isStoragePermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
            {
                Log.v(TAG,"Permission is granted");
                return true;
            } else
            {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    public boolean checkPermissionForGallary()
    {
        int result = ContextCompat.checkSelfPermission(Import_Item.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_PDF && resultCode == RESULT_OK
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
                            txt_fileName.setText(fileName);
                            Log.d(TAG, "onActivityResult: File Sixe"+ cursor.getString(cursor.getColumnIndex(OpenableColumns.SIZE)));
                            Log.d(TAG, "onActivityResult: File Name"+fileName);
                            Log.d(TAG, "onActivityResult: "+getContentResolver().openInputStream(filePath)); ;
                        }
                    } finally
                    {
                        cursor.close();
                    }
                } else if (filePath.toString().startsWith("file://"))
                {
                    File file=new File(filePath.toString());
                    fileName = file.getName();
                    txt_fileName.setText(fileName);
                    Log.d(TAG, "onActivityResult: else File"+fileName);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }




        }
    }
}

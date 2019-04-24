package com.inevitablesol.www.shopmanagement.account;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
import com.inevitablesol.www.shopmanagement.Validation.Validation;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.billing_module.Billing_BankDetails;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.settings.Utility;
import com.inevitablesol.www.shopmanagement.wishList.Add_WishList;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User_Profile extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "User_Profile";


    private TextInputEditText user_name,user_email,user_dateofbirth,user_mobile,user_empcode,user_createdby,user_Createdon;
    private AppCompatButton save_user;

    private GlobalPool globalPool;
    private String userRole;
    private Spinner spinner;
    private ImageView imageView;


    FirebaseStorage storage;
    StorageReference storageReference;
    private String fileName;
    private Uri downloadUrl;
    private String userChoosenTask;

    private final int PICK_IMAGE_REQUEST = 71;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private Bitmap imageCamera;
    private Uri filePath;
    private int requestCode = 103;
    ImageView  img_dob;

    private TextView txt_userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile);
        globalPool = (GlobalPool) this.getApplication();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        user_name=(TextInputEditText)findViewById(R.id.user_name);
        spinner=(Spinner)findViewById(R.id.spnn_role);
        user_email=(TextInputEditText)findViewById(R.id.user_email);
        user_dateofbirth=(TextInputEditText)findViewById(R.id.user_dateofbirth);
        user_mobile=(TextInputEditText)findViewById(R.id.user_mobile);
        user_empcode=(TextInputEditText)findViewById(R.id.user_empcode);
        save_user=(AppCompatButton)findViewById(R.id.user_save);
        save_user.setOnClickListener(this);
        txt_userType=(TextView)findViewById(R.id.txt_userType);
        img_dob=(ImageView)findViewById(R.id.img_dateofBirth);

        img_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");

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

                new DatePickerDialog(User_Profile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                Log.d("date", String.valueOf(myCalendar.getTime()));


            }
        });
        user_createdby=(TextInputEditText)findViewById(R.id.user_createdby);
        user_Createdon=(TextInputEditText)findViewById(R.id.user_createdOn);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_role, android.R.layout.simple_spinner_item);
        user_empcode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

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

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

         if(globalPool instanceof GlobalPool && globalPool!=null)
         {
             user_name.setText(globalPool.getUsername());
             user_email.setText(globalPool.getUserEmail());
             user_mobile.setText(globalPool.getUsermobile());
             userRole=globalPool.getUserRole();
             user_createdby.setText(userRole);
             user_dateofbirth.setText(globalPool.getDOB());
             user_empcode.setText(globalPool.getEmpCode());
             user_Createdon.setText(globalPool.getCreated_on());
             Log.d(TAG, "onCreate: Role"+userRole);
             txt_userType.setText(userRole);
             int position=     adapter.getPosition(userRole);
             Log.d(TAG, "onCreate: Postion"+position);
             spinner.setSelection(position);

             try {
                 Picasso.with(this).load(globalPool.getProfile_Pic()).fit().centerInside().into(imageView);
             } catch (Exception e)
             {
                 e.printStackTrace();
             }
         }else {
             Toast.makeText(this, "Please Log In ", Toast.LENGTH_SHORT).show();
         }

         save_user.setText("EDIT");
         save_user.setTag(1);



        Log.d(TAG, "onCreate: ");

//        user_dateofbirth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//
//        });
    }

    private void updateDate(Calendar myCalendar)
    {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyy-MM-dd");
        String currentDateTimeString = dateFormat.format(myCalendar.getTime());
        user_dateofbirth.setText(currentDateTimeString);
    }


    private void browseImage()
    {

        final CharSequence[] items = {"Choose from Library", "Take Photo",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(User_Profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    boolean result = Utility.checkPermissionCamera(User_Profile.this);
                    userChoosenTask = "Take Photo";
                    if (requestPermissionForCamera())
                        cameraIntent();
                } else if (items[item].equals("Choose from Library"))
                {
                    boolean result = Utility.checkPermission(User_Profile.this);
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

    public boolean checkPermissionForGallary()
    {
        int result = ContextCompat.checkSelfPermission(User_Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public boolean requestPermissionForCamera()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(User_Profile.this, Manifest.permission.CAMERA))
        {
            Toast.makeText(User_Profile.this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            ActivityCompat.requestPermissions(User_Profile.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            return true;
        }
    }

    private void cameraIntent()
    {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, requestCode);
    }

    private void galleryIntent()
    {
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
                    if (cursor != null && cursor.moveToFirst())
                    {
                        fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d(TAG, "onActivityResult: File Name"+fileName);
                    }
                } finally
                {
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





            try
            {
                imageCamera = (Bitmap) data.getExtras().get("data");
                filePath =    getImageUri(getApplicationContext(),imageCamera);

                Log.d(TAG, "onActivityResult: File Path"+filePath);
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

            StorageReference ref = storageReference.child("Profile_images/" + "Profile");

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            downloadUrl=taskSnapshot.getDownloadUrl();
                            Log.d(TAG, "onSuccess: "+taskSnapshot.getDownloadUrl());
                            progressDialog.dismiss();
                            Toast.makeText(User_Profile.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(User_Profile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }


    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.user_save:
                final int status =(Integer) v.getTag();
                if(status == 1) 
                {
                    Log.d(TAG, "onClick: If");
                   
                    user_mobile.setEnabled(true);
                    user_email.setEnabled(true);
                    user_name.setEnabled(true);
                    user_dateofbirth.setEnabled(true);
                    user_empcode.setEnabled(true);
                    user_createdby.setEnabled(true);
                    user_Createdon.setEnabled(true);

                    save_user.setText("save");

                    v.setTag(0); //pause
                } else 
                    {
                        Log.d(TAG, "onClick: Else");
                    save_user.setText("EDIT");
                    v.setTag(1); //pause
                        getDatails();
                }



                break;
                default:
                    Toast.makeText(this, "Wrong Choice", Toast.LENGTH_SHORT).show();
        }

    }

    private void getDatails()
    {
        final String userName=user_name.getText().toString().trim();
        final String  usermobile=user_mobile.getText().toString().trim();
        final String  useremail=user_email.getText().toString().trim();
        final String  userDateofbirth=user_dateofbirth.getText().toString().trim();
        final String       empCode=user_empcode.getText().toString().trim();
        final String     role     =spinner.getSelectedItem().toString().trim();

       if(validateForm())
       {
           final ProgressDialog loading = ProgressDialog.show(this, "Loading....", "Please wait...", false, false);
           StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.USERUPDATE_PROFILE, new Response.Listener<String>() {
               @Override
               public void onResponse(String response)
               {
                   Log.d(TAG, "onResponse: " + response);
                   JSONObject msg = null;
                   try {
                       msg = new JSONObject(response);
                       String message = msg.getString("message");
                       if(message.equalsIgnoreCase("succesfully updated"))
                       {
                           globalPool.setUsername(userName);
                           globalPool.setUserRole(role);
                           globalPool.setUserEmail(useremail);
                           globalPool.setUsermobile(usermobile);
                           globalPool.setDOB(userDateofbirth);
                           globalPool.setEmpCode(empCode);
                           globalPool.setProfile_Pic(String.valueOf(downloadUrl));
                           finish();
                           Toast.makeText(User_Profile.this, "succesfully updated" , Toast.LENGTH_SHORT).show();
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
                       Toast.makeText(User_Profile.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();

                   }

               }
           }) {
               @Override
               protected Map<String, String> getParams() throws AuthFailureError {
                   Map<String, String> params = new HashMap<>();
                   Log.d("dbname", globalPool.getDbname());
                   // params.put("dbname", globalPool.getDbname());
                   params.put("name", userName);
                   params.put("u_id",globalPool.getUserId());
                   params.put("user_type", role);
                   params.put("mobile_number", usermobile);
                   params.put("email_id", useremail);
                   params.put("ecode", empCode);
                   params.put("dob", userDateofbirth);
                   params.put("link", String.valueOf(downloadUrl));
                   Log.d(TAG, "getParams:BankDetails" + params.toString());

                   return params;
               }
           };
           RequestQueue requestQueue = Volley.newRequestQueue(this);
           requestQueue.add(stringRequest);


       }else
           {
           Toast.makeText(User_Profile.this, "Please Add Valid Field", Toast.LENGTH_SHORT).show();
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
}

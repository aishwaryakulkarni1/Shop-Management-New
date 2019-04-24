package com.inevitablesol.www.shopmanagement.account;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
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
import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.LonginDetails.SigninActivity;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.itextpdf.text.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class My_account extends BaseActivity
{
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE =101 ;
    private ImageView img_profile;
    private  ImageView img_roleMagnt,img_shop;

    private ImageView img_documents;
   private  ImageView resetPassword;
    private GlobalPool globalPool;
    private AppCompatEditText otp_number;
    private int randomInt;
    private String mobile;
    private static final String TAG = "My_account";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_my_account,frameLayout);
        globalPool = (GlobalPool) this.getApplication();
        img_profile=(ImageView)findViewById(R.id.user_profile);

        img_roleMagnt=(ImageView)findViewById(R.id.role_management);
        img_documents=(ImageView)findViewById(R.id.img_documents);
        resetPassword=(ImageView)findViewById(R.id.shop_resetpassword);
        img_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(My_account.this,Document_Activity.class));

            }
        });
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showDialog();


            }
        });

        mDrawerList.setItemChecked(position, true);
        String logCheck =listArray[position];
        if (logCheck.equalsIgnoreCase("Logout"))
        {
//            setTitle(listArray[0]);
            toolbar.setTitle(listArray[0]);
        }else
        {

//            setTitle(listArray[position]);
            toolbar.setTitle("My Account");
        }

        img_shop=(ImageView)findViewById(R.id.img_shopdetails);
        img_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(My_account.this,User_shopDetails.class));
            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(My_account.this,User_Profile.class));

            }
        });
        img_roleMagnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(My_account.this,Role_management.class));


            }
        });
    }


    private void showDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmpassword);
       mobile= globalPool.getUsermobile() ;
        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Do you want to reset password for " + globalPool.getUsermobile() +  "?");
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (globalPool.getUsermobile() != null && globalPool.getUsermobile().length() == 10)
                {
                    checkMobile(globalPool.getUsermobile());
                } else
                    {

                    Toast.makeText(getApplicationContext(), "Please enter valid mobile number", Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();


            }
        });

        Button cancelDialog = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }



    public void checkMobile(final String contact)
    {

        final Context ctxjson = getApplication();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.URL_MOBILE_VERIFY,
                new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.d("response",response);
                            JSONObject j = new JSONObject(response);
                            String valid_user = j.getString("message");

                            if (valid_user.equalsIgnoreCase("Correct number"))
                            {
                                Toast.makeText(getApplication(),"correct number",Toast.LENGTH_LONG).show();
                                verifyPass(contact);
                            } else if (valid_user.equals("true"))
                            {
                                Toast.makeText(My_account.this, "User not recognised. Please sign up.", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(My_account.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(My_account.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("u_number", contact);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctxjson);
        requestQueue.add(stringRequest);


    }

    public void verifyPass(final String contact3)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.URL_REQUEST_SMS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                Log.d("responsePassword",response);
                try
                {

                    JSONObject jsonObject=new JSONObject(response);
                    String message=       jsonObject.getString("message");

                    if (message.toString().trim().equalsIgnoreCase("succesfully update"))
                    {


                        sendMessage(String.valueOf(randomInt));

                        final Dialog dialog = new Dialog(My_account.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.verify_otp_dialog);
                        Button verifyOTPBtn = (Button) dialog.findViewById(R.id.btn_verify);
                        otp_number = (AppCompatEditText) dialog.findViewById(R.id.input_otp);


                        Button cancel = (Button) dialog.findViewById(R.id.cancel_btn);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.cancel();
                            }
                        });

                        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                final String otp1 = otp_number.getText().toString();

                                if (otp1 != null && globalPool.getUsermobile() != null && otp1.length() >= 5) {

                                    verifyotp(otp1);
                                    dialog.dismiss();
                                } else
                                    {

                                    Toast.makeText(getApplicationContext(), "Please enter valid OTP", Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(My_account.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(My_account.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                randomInt = (int) (1000000.0 * Math.random());

                params.put("u_number", contact3);
                params.put("otp", String.valueOf(randomInt));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

   public void sendMessage(String otp)
{
    String message1 = new String();

    String message = "Hey there, here is your OTP: " + otp;
    String mobile = globalPool.getUsermobile();

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
                        if (response != null)
                        {
                            JSONObject j = new JSONObject(response);
                            String type1 = j.getString("type");
                            if (type1.contains("success"))
                            {
                                Toast.makeText(getApplication(), "OTP message sent successfully", Toast.LENGTH_LONG).show();
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
                public void onErrorResponse(VolleyError error)
                {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

    RequestQueue requestQueue = Volley.newRequestQueue(context);
    requestQueue.add(stringRequest);
}

    public void verifyotp(final String otp)
    {

        final Context ctxjson = getApplication();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.VERIFY_OTP,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {

                            String resp = response.toString().trim();
                            Log.d("otpVerified", resp);
                            JSONObject jsonObject=new JSONObject(resp);
                            String res_message=jsonObject.getString("message");


                            if (res_message.equalsIgnoreCase("verify OTP"))
                            {

                                final Dialog dialog_pass = new Dialog(My_account.this);
                                dialog_pass.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog_pass.setContentView(R.layout.change_password_dialog);
                                final TextView user_number = (TextView) dialog_pass.findViewById(R.id.user_number);

                                final TextView textView=(TextView)dialog_pass.findViewById(R.id.phoneNumber);
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        callToCustomer();
                                    }
                                });
                                user_number.setText(mobile);
                                final AppCompatEditText new_pass = (AppCompatEditText) dialog_pass.findViewById(R.id.input_newpassword);
                                final AppCompatEditText retype_pass = (AppCompatEditText) dialog_pass.findViewById(R.id.input_repassword);
                                final AppCompatButton reset = (AppCompatButton) dialog_pass.findViewById(R.id.update_pass);
                                AppCompatButton cancel_pass = (AppCompatButton) dialog_pass.findViewById(R.id.cancel_pass);
                                cancel_pass.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        dialog_pass.dismiss();
                                    }
                                });


                                reset.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        final String new_password = new_pass.getText().toString();
                                        final String re_enteerpass = retype_pass.getText().toString();

                                        if (new_password.equals(re_enteerpass) && !new_password.equals("")) {

                                            updatePassword(new_password);
                                            dialog_pass.dismiss();

                                        } else
                                            {
                                            retype_pass.setError("Passwords should be same");

                                        }


                                    }
                                });

                                dialog_pass.setCancelable(false);
                                dialog_pass.setCanceledOnTouchOutside(false);
                                dialog_pass.show();


                            } else
                            {

                                Toast.makeText(ctxjson, "Please enter a valid otp.", Toast.LENGTH_SHORT).show();

                            }
                        }
                        catch (JSONException e)
                        {

                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.d(TAG, "onErrorResponse: Errr"+error);
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(My_account.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(My_account.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("otp", otp);
                params.put("u_number", mobile);
                Log.d(TAG, "getParams: Otp"+params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctxjson);
        requestQueue.add(stringRequest);

    }


    public void updatePassword(final String pass)
    {
        final Context ctxjson = getApplication();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.URL_UPDATE_PASSWORD,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            String resp = response.toString().trim();
                            JSONObject jsonObject = new JSONObject(resp);
                            String message= jsonObject.getString("message");
                            Log.d("message",message);
                            if(message.equalsIgnoreCase("password updated succesfully"))
                            {

                                Toast.makeText(ctxjson, "Password updated successfully.", Toast.LENGTH_SHORT).show();

                            }else
                            {

                            }


                        }catch (JSONException e)
                        {

                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // fl_progress.setVisibility(View.GONE);
                        Log.d("Update Failed ", error.getMessage());
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(My_account.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(SigninActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("Update Failed ", error.getMessage());
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                Log.d(TAG, "getParams: ");
                params.put("u_number", mobile);
                params.put("u_pass", pass);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctxjson);
        requestQueue.add(stringRequest);
    }


    private void callToCustomer()
    {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else
        {
            callPhone();
        }
    }



    private void callPhone()
    {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + "7861998866"));


        if (ActivityCompat.checkSelfPermission(My_account.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        startActivity(callIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: My Account");
    }
}

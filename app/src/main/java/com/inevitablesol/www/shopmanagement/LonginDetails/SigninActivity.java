package com.inevitablesol.www.shopmanagement.LonginDetails;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.RegistrationModule.ShopRegistrationActivity;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;
import com.inevitablesol.www.shopmanagement.billing_module.BillingHistory;

import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.sql_lite.ItemDetailsParser;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

//    private static final String URL_MOBILE_VERIFY ="http://35.161.99.113:9000/api/users/forget_password" ;
//    private static final String URL_REQUEST_SMS ="http://35.161.99.113:9000/api/users/verifyOtp" ;
//    private static final String URL_VERIFY_OTP ="http://35.161.99.113:9000/api/users/otpCheck" ;
//    private static final String URL_UPDATE_PASSWORD = "http://35.161.99.113:9000/api/users/changePass";
    AppCompatButton btn_register;
    EditText input_mobile_no,input_pass;
    LinearLayout ll_userinfo;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
  //  DbHandler dbHandler;
    private  String mobile;
    TextView _forgotPassword,tv_registershop;
    AppCompatButton btn_login;
    private int randomInt;
    private AppCompatEditText otp_number;
 ///   private SqlDataBase sqlDataBase;
    private static final String GET_STOCKDETAILS ="http://35.161.99.113:9000/api/item/list" ;
   // private  GlobalClass globalClass;

    private SqlDataBase sqlDataBase;
    private String dbname;

    private static final String TAG = "SigninActivity";
    GlobalPool globalPool;
    private int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getSupportActionBar().hide();
        tv_registershop = (TextView) findViewById(R.id.tv_registershop);
        tv_registershop.setOnClickListener(this);
        input_mobile_no = (EditText) findViewById(R.id.input_mobile_no);
        input_pass = (EditText) findViewById(R.id.input_pass);
        btn_login = (AppCompatButton) findViewById(R.id.btn_login);
        _forgotPassword=(TextView)findViewById(R.id.tv_forgotpwd);
        _forgotPassword.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        globalPool= (GlobalPool)this.getApplication();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sqlDataBase=new SqlDataBase(this);



    }

    @Override
    public void onClick(View view)
    {
        int viewID = view.getId();
        switch (viewID)
        {
            case R.id.tv_forgotpwd:
                if(checkMobile())
                {
                    Toast.makeText(getApplication(),"Please Enter Valid Number",Toast.LENGTH_LONG).show();

                }else
                {
                   showDialog();
                }
                break;
            case R.id.btn_login:
                //  sign_in();
                checkValidation();
                break;
            case R.id.tv_registershop:
                Intent intent = new Intent(this,ShopRegistrationActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
    }
    IntentFilter filter = new IntentFilter("otpAdded");
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getExtras().getString("message");
            Log.d("messageOtp", value);

            otp_number.setText(value.toString().trim());

            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private boolean checkMobile()
    {
        String username = input_mobile_no.getText().toString().trim();
        if(username.isEmpty()  || username.length()<10)
        {
            Toast.makeText(getApplication(),"Please Enter Valid Number",Toast.LENGTH_LONG).show();
            return true;

        }else
        {
            return false;
        }
    }

    private void checkValidation()
    {

        String username = input_mobile_no.getText().toString().trim();
        String password = input_pass.getText().toString().trim();
        if(username.isEmpty()  || username.length()<10)
        {
            Toast.makeText(getApplication(),"Please Enter Valid Number",Toast.LENGTH_LONG).show();
        }else
        {
            sign_in(username,password);
        }


    }



    public void sign_in(final String username, final String password)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.LOGING, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                String resp = response.toString().trim();
                Log.d("Login Details", resp);




                try
                {

                    JSONObject msg = new JSONObject(resp);
                    String message = msg.getString("message");

                    if (message.equalsIgnoreCase("Login succesfully"))
                    {

                        JSONObject obj = new JSONObject(resp).getJSONObject("records");
                        String userId = obj.getString("u_id");
                        String username = obj.getString("u_name");
                        String usermobile = obj.getString("u_number");
                        String useremail = obj.getString("u_email");
                        String userrole = obj.getString("u_role");
                        String userpassword = obj.getString("u_pass");
                        String shop_id=obj.getString("s_id");
                        String link=obj.getString("link");
                         dbname=obj.getString("dbname");

                           if(globalPool instanceof  GlobalPool)
                           {
                               try {
                                   globalPool.setDbname(dbname);
                                   globalPool.setUserEmail(useremail);
                                   globalPool.setUsermobile(usermobile);
                                   globalPool.setUserRole(userrole);
                                   globalPool.setUsername(username);
                                   globalPool.setUserId(userId);
                                   globalPool.setShopId(shop_id);
                                   globalPool.setShopNumber(usermobile);
                                   globalPool.setShop_address(obj.getString("address"));
                                   globalPool.setGstNumnebr(obj.getString("gstin_no"));
                                   globalPool.setShop_email(useremail);
                                   globalPool.setShop_state(obj.getString("state"));
                                   globalPool.setEmpCode(obj.getString("ecode"));
                                   globalPool.setCreated_on(obj.getString("created_on"));
                                   globalPool.setProfile_Pic(link);

                                   globalPool.setShopName(obj.getString("shopName"));
                               }catch (Exception e)
                               {
                                   Log.d(TAG, "onResponse:Global Class"+e);
                               }
                           }


                       if( sqlDataBase.check_items())
                       {
                           Log.d(TAG, "onResponse:DB Available");

                       }else
                       {
                          getAllItemDetails();

                       }


                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("userId", userId);
                        editor.putString("username", username);
                        editor.putString("usermobile", usermobile);
                        editor.putString("useremail", useremail);
                        editor.putString("userrole", userrole);
                        editor.putString("userpassword", userpassword);
                        editor.putString("s_id",shop_id);
                        editor.putString("dbname",dbname);
                        editor.putString("gstNo",obj.getString("gstin_no"));
                        editor.putString("address",obj.getString("address"));
                        editor.putString("state",obj.getString("state"));

                      //  globalClass.setDbName(dbname);
                        editor.commit();

                       getPassCodeDeatails();

//                        Intent intent = new Intent(SigninActivity.this, BillingHistory.class);
//                        startActivity(intent);
//                        finish();

                    } else if(message.equalsIgnoreCase("You discontinue your service"))
                    {
                        Toast.makeText(SigninActivity.this, message, Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(SigninActivity.this, "Please enter correct username and password ", Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(SigninActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        } )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                Log.d("u_number",username);
                Log.d("u_pass",password);
                params.put("u_number", username);
                params.put("u_pass",password);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getPassCodeDeatails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.PASSCODE_DEATILS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();
                Log.d("Pass Code Deatils", resp);

                     try
                     {
                         JSONObject obj = new JSONObject(resp);
                          String  jsonObject1=obj.getString("message");
                           if(jsonObject1.equalsIgnoreCase("Passcode is not needed"))
                           {
                               Intent intent = new Intent(SigninActivity.this, BillingHistory.class);
                               startActivity(intent);
                               finish();
                               return;

                           }else
                           {
                               JSONArray jsonArray=obj.getJSONArray("records");
                               JSONObject jsonObject=jsonArray.getJSONObject(0);
                               String passcode=jsonObject.getString("passcode");

                               Log.d(TAG, "onResponse:passCode"+passcode);

                               if(passcode.length()>0)
                               {
                                   showPassCode(passcode);
                               }else
                               {
                                   Intent intent = new Intent(SigninActivity.this, BillingHistory.class);
                                   startActivity(intent);
                                   finish();
                               }

                           }



                     }catch (Exception e)
                     {
                         Log.d(TAG, "onResponse:E"+e);

                     }
//




            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(SigninActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        } )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("dbname", dbname);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showPassCode(final String passcode)
    {

        final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.password_dialog);
                dialog.setTitle("Add Pass Code");
                final  EditText et_passCode=(TextInputEditText)dialog.findViewById(R.id.et_passcode);
                AppCompatButton bt_save=(AppCompatButton)dialog.findViewById(R.id.save_passCode);
                AppCompatButton cancel=(AppCompatButton)dialog.findViewById(R.id.passcode_cancel);





                 bt_save.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {

                         String passCode_et=et_passCode.getText().toString().trim();
                         if(passCode_et.length()>0)
                         {

                             if(passcode.equals(passCode_et))
                             {
                                 Intent intent = new Intent(SigninActivity.this, BillingHistory.class);
                                 startActivity(intent);
                                 finish();

                             }else
                             {
                                 Toast.makeText(getApplicationContext(),"Invalid passCode",Toast.LENGTH_LONG).show();
                             }

                         }

                     }
                 });
                dialog.setCancelable(false);
                cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

    }

    private void savePassCode(String passCode)
    {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.PASSCODE_DEATILS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                String resp = response.toString().trim();
                Log.d("Pass Code Deatils", resp);

                try
                {
                                       JSONObject obj = new JSONObject(resp);
                    JSONArray jsonArray=obj.getJSONArray("records");
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String passcode=jsonObject.getString("passcode");


                }catch (Exception e)
                {

                }
//




            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(SigninActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        } )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("dbname", dbname);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void showDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmpassword);
        mobile= input_mobile_no.getText().toString();
        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Do you want to reset password for " + mobile + " ? ");
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        //image.setImageResource(R.drawable.ic_home);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (mobile != null && mobile.length() == 10)
                {
                    checkMobile(mobile);
                } else {
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
                new Response.Listener<String>() {

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
                                Toast.makeText(SigninActivity.this, "User not recognised. Please sign up.", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(SigninActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SigninActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=       jsonObject.getString("message");

                    if (message.toString().trim().equalsIgnoreCase("succesfully update"))
                    {

                        //Toast.makeText(LoginActivity.this, "Sending OTP message", Toast.LENGTH_LONG).show();
                        sendMessage(String.valueOf(randomInt));

                        final Dialog dialog = new Dialog(SigninActivity.this);
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
                                if (otp1 != null && mobile != null && otp1.length() >= 5)
                                {

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
            public void onErrorResponse(VolleyError error)
            {
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(SigninActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(SigninActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        String mobile = input_mobile_no.getText().toString();
        try
        {
            message1 = URLEncoder.encode(message, "utf-8");
        } catch (UnsupportedEncodingException e) {

        }
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
                    public void onErrorResponse(VolleyError error) {
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
                            //Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();


                            if (res_message.equalsIgnoreCase("verify OTP"))
                            {
                                //fl_progress.setVisibility(View.GONE);

                                final Dialog dialog_pass = new Dialog(SigninActivity.this);
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
                                        // fl_progress.setVisibility(View.GONE);
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
                            Toast.makeText(SigninActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SigninActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

                        // fl_progress.setVisibility(View.GONE);
                        // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // fl_progress.setVisibility(View.GONE);
                        Log.d("Update Failed ", error.getMessage());
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(SigninActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
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




    private void getAllItemDetails()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GET_STOCKDETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try
                {
                String resp = response.toString().trim();

                Log.d("Item Details in SignIn",resp);

                JSONObject jsonObject = new JSONObject(resp);
                String message= jsonObject.getString("message");
                Log.d("message",message);
                    if(message.equalsIgnoreCase("Data not available"))
                    {
                        Log.d(TAG, "onResponse:"+message);
                    }
                         else
                    {
                        ItemDetailsParser itemDetailsParser = new ItemDetailsParser(resp);
                        itemDetailsParser.parseItemList();
                        String selectedItemQty = "0";


                        String[] p_id =        ItemDetailsParser.product_id;
                        String[] item_id =       ItemDetailsParser.item_id;
                        String[] item_name =      ItemDetailsParser.item_name;
                        String[] stock_qty =       ItemDetailsParser.stock_qt;

                        String[] item_mrp =        ItemDetailsParser.item_mrp;
                        String[] purchase_price =   ItemDetailsParser.purchase_price;
                        String[] gst =              ItemDetailsParser.gst;
                        String[] totalPrice =       ItemDetailsParser.total_price;
                        String[] discount =        ItemDetailsParser.discount;
                        String[] unit_Price =      ItemDetailsParser.unit_price;
                        String[] hsnCode =         ItemDetailsParser.hsn;
                        String[] status =          ItemDetailsParser.status;

                        String[] mUnit=ItemDetailsParser.mUnit;
                        String [] unit=ItemDetailsParser.unit;
                        String [] itembarcode=ItemDetailsParser.item_barcode;
                        String [] shortcut=ItemDetailsParser.shortcut;


                        for (int i = 0; i < p_id.length; i++)
                        {
                            if (Integer.parseInt(stock_qty[i]) > 0 || status[i].equalsIgnoreCase("infinite"))
                            {
                                sqlDataBase.addItemDetails(p_id[i], item_id[i], item_name[i], stock_qty[i], selectedItemQty, item_mrp[i], purchase_price[i],
                                        totalPrice[i], gst[i], discount[i], unit_Price[i], hsnCode[i], status[i],mUnit[i],unit[i],itembarcode[i],shortcut[i]);

                            } else {
                                Log.d(TAG, "onResponse: QTY less then Zero " + Integer.parseInt(stock_qty[i]));
                                Log.d(TAG, "onResponse: Menu Item " + status[i]);
                            }


                        }
                    }

                }catch (NumberFormatException e)
                {

                }catch (NullPointerException e)
                {

                }catch (Exception e)
                {

                }

                    Log.d("new Db Installed","");










            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(SigninActivity.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                if(globalPool.isMenuItemStatus())
                {
                    params.put("menu", "enable");

                }else
                {
                    params.put("menu", "disable");

                }

                params.put("dbname", dbname);
                Log.d(TAG, "getParams: Menu Param"+params.toString());
                return params;
            }

        } ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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


        if (ActivityCompat.checkSelfPermission(SigninActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        startActivity(callIntent);
    }
}





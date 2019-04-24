package com.inevitablesol.www.shopmanagement.wishList;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.Validation.Validation;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Add_WishList extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText et_custName, et_mobileNumber, et_emailId, et_description;

    private TextInputEditText et_qty;
    private TextView txt_stillDate, txt_remainder;
    private ImageView img_date, img_remainder;
    private static final String TAG = "Add_WishList";
    private Context context = Add_WishList.this;
    private RecyclerView recyclerView;
    private AppCompatButton add_rows, delete_rows;
    private AppCompatButton bt_save;

    // pre Views

    private SharedPreferences sharedpreferences;
    public final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    private JSONArray jsonArray = new JSONArray();
    private JSONArray jsonArray2 = new JSONArray();
    private int i = 0;
    private int totalQty = 0;
    private double totalAmount = 0.0;
    private ArrayList<WishListItems_pojo> expensesListItemses_new = new ArrayList<>();
    private ArrayList<WishListItems_pojo> expensesListItemses = new ArrayList<>();
    private WishListAdapter selectedItemAdapter;
    private String message1;

    private LinearLayout addReminder;

    private static final String MySETTINGS = "MySetting";
    private SharedPreferences sharedpreferences2;

    private static boolean SMSFLAG;
    private String userMobile;
    private boolean flag;
    private String mobile;
    private int CAMERA_PERMISSION_REQUEST_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_wish_list);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        et_custName = (TextInputEditText) findViewById(R.id.wish_custName);
        et_mobileNumber = (TextInputEditText) findViewById(R.id.wish_mobileNumber);
        et_emailId = (TextInputEditText) findViewById(R.id.wish_mailId);
        txt_stillDate = (TextView) findViewById(R.id.wish_stillDate);
        txt_remainder = (TextView) findViewById(R.id.wish_setRemainder);
        img_date = (ImageView) findViewById(R.id.wish_imgDate);
        et_qty = (TextInputEditText) findViewById(R.id.wish_et_qty);
        img_date.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.wish_recyclerView);
        add_rows = (AppCompatButton) findViewById(R.id.wish_addRow);
        delete_rows = (AppCompatButton) findViewById(R.id.wish_deleteRow);
        add_rows.setOnClickListener(this);
        delete_rows.setOnClickListener(this);
        bt_save = (AppCompatButton) findViewById(R.id.add_to_wishList);
        bt_save.setOnClickListener(this);
        et_description = (TextInputEditText) findViewById(R.id.wish_description);
        img_remainder = (ImageView) findViewById(R.id.wish_img_remainder);
        img_remainder.setOnClickListener(this);
        addReminder = (LinearLayout) findViewById(R.id.linear_addReminder);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        dbname = (sharedpreferences.getString("dbname", null));
        userMobile = sharedpreferences.getString("usermobile", "");

        currentDate();
        et_emailId.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!Validation.isValidEmail(s.toString()))
                {
                    et_emailId.setError("Invalid Email");
                }


            }
        });
        et_mobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!Validation.isValidPhone(s.toString()))
                {
                    et_mobileNumber.setError("Invalid mobile");
                }

            }
        });

        try
        {

            sharedpreferences2 = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
            String wishList_reminder = (sharedpreferences2.getString("wishList_sms", null));
            Log.d(TAG, "onCreate:wishList_sms" + wishList_reminder);
            if (wishList_reminder != null)
            {
                if (wishList_reminder.equalsIgnoreCase("Yes"))
                {

                    SMSFLAG = true;
                    addReminder.setVisibility(View.VISIBLE);

                } else
                    {
                    SMSFLAG = false;
                    addReminder.setVisibility(View.GONE);

                }
            }

        } catch (NullPointerException e) {
            Log.d(TAG, "onCreate:error" + e);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.wish_imgDate:
                changeWishDate(txt_stillDate);
                break;
            case R.id.wish_img_remainder:
                changeWishDateReminder(txt_remainder);
                break;
            case R.id.add_to_wishList:
                String email=et_emailId.getText().toString().trim();
//                if(isValidEmail(email))
//                {
                    save_Data();

//                }else
//                {
//                    Toast.makeText(context, "Please enter Valid Email", Toast.LENGTH_SHORT).show();
//                }

                break;
            case R.id.wish_addRow:
                addNewRow();
                break;
            case R.id.wish_deleteRow:
                // DeleteRows();
                _deleteRow();
                break;
            default:
                Toast.makeText(context, "Wrong Choice", Toast.LENGTH_SHORT).show();

        }

    }
    public final static boolean isValidEmail(CharSequence target)
    {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void _deleteRow() {
        totalAmount = 0.0;
        ;
        totalQty = 0;
        expensesListItemses_new = new ArrayList<>();
        // Log.d(TAG, "_deleteRow: Value"+i);
        Log.d(TAG, "_deleteRow: JSON" + jsonArray);
        Log.d(TAG, "DeleteRows:BeforeDelete" + expensesListItemses);
        Log.d(TAG, "DeleteRows:BeforeDelete new" + expensesListItemses_new);


        Log.d(TAG, "After iterating " + expensesListItemses);

        for (WishListItems_pojo id : expensesListItemses)
        {
            try {
                if (id.isChecked()) {
                    flag = true;
                    //expensesListItemses.remove(id);
                    Log.d(TAG, "_deleteRow:Object" + id.toString());
                } else {
                    // flag=false;
                    Log.d(TAG, "_deleteRow:Else");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (flag) {

            Log.d(TAG, "_deleteRow: I " + i);
            // Log.d(TAG, "_new :Size"+expensesListItemses_new.size());

            Iterator<WishListItems_pojo> wishListItems_pojoIterator = expensesListItemses.iterator();
            while (wishListItems_pojoIterator.hasNext()) {
                WishListItems_pojo pj = wishListItems_pojoIterator.next();
                Log.d(TAG, "_deleteRow: pj" + pj + toString());
                if (pj.isChecked()) {
                    i--;
                    wishListItems_pojoIterator.remove();
                }

            }
            getDetails(expensesListItemses);
            DiplayListItem();
            if (expensesListItemses.isEmpty()) {
                selectedItemAdapter.clearView();
            }

//

        } else
        {
            Toast.makeText(context, "Please Select At Least One", Toast.LENGTH_SHORT).show();
        }

        flag = false;


//


    }

    private void DeleteRows() {
        totalAmount = 0.0;
        ;
        totalQty = 0;
        expensesListItemses_new = new ArrayList<>();
        Log.d(TAG, "DeleteRows:BeforeDelete" + expensesListItemses.size());
        Log.d(TAG, "_delete_itemList:before" + expensesListItemses);
        //  selectedItemAdapter.removeItem(expensesListItemses);

        try {


            for (WishListItems_pojo id : expensesListItemses) {

                if (id.isChecked()) {

                    Log.i(TAG, "DeleteRows:If" + id.toString());
                } else {
                    Log.i(TAG, "DeleteRows:else" + id.toString());
                    try {
                        totalQty += Integer.parseInt(id.getQty());
                        expensesListItemses_new.add(id);
                        i--;

                    } catch (Exception e) {
                        Log.d(TAG, "DeleteRows() called" + e);

                    }
                }

                Log.d(TAG, "DeleteRows:Qty" + totalQty);

            }


            for (WishListItems_pojo id : expensesListItemses) {
                Log.d(TAG, "_List:" + id.toString());


            }
            for (WishListItems_pojo id : expensesListItemses_new) {
                Log.d(TAG, "_newList:" + id.toString());

            }


        } catch (Exception e) {
            Log.d(TAG, "_delete_itemList:Error" + e);
        }
        Log.d(TAG, "_delete_itemList:Size" + expensesListItemses_new);
//        if(expensesListItemses_new)
//        {
        Diplay_new_List();
        getDetails(expensesListItemses_new);
        if (expensesListItemses.isEmpty()) {
            selectedItemAdapter.clearView();
        }
//        }else
//        {
//            Toast.makeText(context, "Please Check Atleast One", Toast.LENGTH_SHORT).show();
//        }

    }

    private void getDetails(ArrayList<WishListItems_pojo> expensesListItemses_new) {
        int j = 0;
        try {
            jsonArray = new JSONArray();
            for (WishListItems_pojo obj : expensesListItemses_new) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", obj.getName());
                jsonObject.put("qty", obj.getQty());
                jsonObject.put("company", obj.getCompany());
                jsonArray.put(j, jsonObject);
                j++;
                totalQty += Integer.parseInt(obj.getQty());
                Log.d(TAG, "getDetails:Delete" + jsonArray);
                Log.d(TAG, "getDetails:TotalQty" + totalQty);
                Log.d(TAG, "getDetails:Json" + jsonArray);
            }


        } catch (NullPointerException e) {

        } catch (JSONException e) {

        } catch (Exception e) {

        }


    }

    private void addNewRow()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.wish_item_dailog);
        dialog.setTitle("Add item Details");

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 6);


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
        final EditText company = (EditText) dialog.findViewById(R.id.exp_company);
        AppCompatButton add = (AppCompatButton) dialog.findViewById(R.id.exp_addlist);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = et_itemName.getText().toString().trim();
                String qty = et_itemqty.getText().toString().trim();
                String cname = company.getText().toString().trim();
                if (name.length() > 0 && qty.length() > 0 && cname.length() > 0)
                {
                    getDetails(name, qty, cname);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Please add All field", Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialog.show();

    }


    private void getDetails(String name, String qty, String company) {

        totalQty += Integer.parseInt(qty);

        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("qty", qty);
            jsonObject.put("company", company);

            jsonArray.put(i, jsonObject);
            i++;

            Log.d(TAG, "getDetails:" + jsonArray);
            Log.d(TAG, "getDetails:TotalQty" + totalQty);

            WishListItems_pojo expensesListItems = new WishListItems_pojo();
            expensesListItems.setName(name);
            expensesListItems.setQty(qty);
            expensesListItems.setCompany(company);
            expensesListItems.setChecked(false);
            Log.d(TAG, "getDetails:Object" + expensesListItems.toString());
            expensesListItemses.add(expensesListItems);
            //DeleteRows();

            Log.d(TAG, "getDetails:JSOArray" + jsonArray);

            DiplayListItem();


        } catch (NullPointerException e) {
            Log.e(TAG, "getDetails:", e);
        } catch (JSONException e) {
            Log.e(TAG, "getDetails:", e);

        } catch (Exception e) {
            Log.e(TAG, "getDetails:", e);

        }


    }

    private void DiplayListItem() {
        Log.d(TAG, "DiplayListItem:Sixe" + expensesListItemses.size());

        et_qty.setText(String.valueOf(totalQty));
        selectedItemAdapter = new WishListAdapter(expensesListItemses, Add_WishList.this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(selectedItemAdapter);


    }

    private void save_Data() {


        String c_name = et_custName.getText().toString().trim();
        mobile = et_mobileNumber.getText().toString().trim();
        String emailId = et_emailId.getText().toString().trim();
        String qty = et_qty.getText().toString().trim();

        String tilldate = txt_stillDate.getText().toString().trim();
        String remainder = txt_remainder.getText().toString().trim();
        String description = et_description.getText().toString().trim();
        try
        {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("cust_name", c_name);
            jsonObject.put("mob_no", mobile);
            jsonObject.put("email", emailId);
            jsonObject.put("till_date", tilldate);
            jsonObject.put("total_amt", remainder);
            jsonObject.put("reminder_date", remainder);
            jsonObject.put("description", description);
            jsonObject.put("totalqty", totalQty);
            jsonObject.put("status", "Pending");
            jsonObject.put("itemArray", jsonArray);

            Log.d(TAG, "save_Data:" + jsonObject);
            String jsonstr = jsonObject.toString();
            String formatedString = jsonstr.replaceAll("\\\\", "");
            Log.d(TAG, "save_Data:" + formatedString);

            if (totalQty > 0 && jsonArray.length() > 0 && c_name.length() > 0  && mobile.length() > 9 ) {
                save_toDB(formatedString);
            } else if (mobile.length() < 9)
            {
                Toast.makeText(context, "Invalid Mobile", Toast.LENGTH_SHORT).show();

            } else if (description.length() < 0)
            {
                Toast.makeText(getApplication(), "Please add Description", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }

    }

    private void Diplay_new_List()
    {
        if (selectedItemAdapter != null)
        {
            selectedItemAdapter.clearView();
            selectedItemAdapter.notifyDataSetChanged();
            et_qty.setText(String.valueOf(totalQty));

            selectedItemAdapter = new WishListAdapter(expensesListItemses_new, Add_WishList.this);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(selectedItemAdapter);
        }

    }


    private void save_toDB(final String formatedString) {
        final ProgressDialog loading = ProgressDialog.show(this, "saving ...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.CREATE_WISHLIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.toString().trim();
                Log.d("Added Successfully", resp);
                try {
                    loading.dismiss();

                    JSONObject msg = new JSONObject(response);
                    String message = msg.getString("message");

                    if (message.equalsIgnoreCase("wish Added"))
                    {

                        if (addReminder.getVisibility() == View.VISIBLE) {

                            if (SMSFLAG)
                            {
                                String reminderDate = txt_remainder.getText().toString().trim();
                                sendSchedulingMessage(reminderDate);
                                if(requestPermissionForCamera())
                                {
                                    sendSMS(mobile, " message From wish List");
                                }else
                                {
                                    Log.d(TAG, "onResponse: Permission needed");
                                }

                            } else {
                                Toast.makeText(getApplication(), "Reminder SMS Not Send", Toast.LENGTH_SHORT).show();
                            }

                        }


                        Intent intent = new Intent(context, WishList_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Wish List Added Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Can not Add Details", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.d(TAG, "onErrorResponse:" + error);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                params.put("data", formatedString);
                Log.d(TAG, "getParamsWithImage: " + formatedString);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void currentDate()
    {
        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MMM/yy");
        // String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        String currentDateTimeString = dateFormat.format(dt);
        txt_stillDate.setText(currentDateTimeString);
        txt_remainder.setText(currentDateTimeString);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed() called");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void changeWishDate(final TextView txt)
    {

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

                updateDate(myCalendar, txt);
            }

        };

        new DatePickerDialog(Add_WishList.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));

    }

    private void updateDate(Calendar myCalendar, TextView txt) {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MMM-yy");
        String currentDateTimeString = dateFormat.format(myCalendar.getTime());
        txt.setText(currentDateTimeString);

    }


    private void changeWishDateReminder(final TextView txt)
    {

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

                updateDateReminder(myCalendar, txt);
            }

        };

        new DatePickerDialog(Add_WishList.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.d("date", String.valueOf(myCalendar.getTime()));

    }

    private void updateDateReminder(Calendar myCalendar, TextView txt) {
        java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(myCalendar.getTime());
        txt.setText(currentDateTimeString);

    }


    public void sendSchedulingMessage(String ScheduleDate2) {


        String message = "Dear User, !\n -" +
                " This is just a Friendly Reminder for Wish list of your Customer. \n " +
                " Please View in Wish List." +
                "Thanks You";

        // schtime=2016-04-09 15:40:14
        String mobile_no = userMobile;//tv_custNumber.getText().toString().trim();
        final String schtime = ScheduleDate2 +" "+"11:00";
        Log.d(TAG, "sendDue_payment_Sms: SchTime"+schtime);
        final String uri = Uri.parse("\n" +
                "http://bhashsms.com/api/schedulemsg.php?")
                .buildUpon()
                .appendQueryParameter("user", "TEAM_MHOURZ")
                .appendQueryParameter("pass", "MECHATRON")
                .appendQueryParameter("text", message)
                .appendQueryParameter("sender", "MHOURZ")
                .appendQueryParameter("phone", mobile_no)
                .appendQueryParameter("priority", "ndnd")
                .appendQueryParameter("stype", "normal")
                .appendQueryParameter("time", schtime)
                .build().toString();
        Log.d(TAG, "sendSchedulingMessage: "+uri);

        StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null) {

                                if ((response.matches(".*\\d+.*")))
                                {
                                    Log.d(TAG, "onResponse:Wish List" + uri);
                                    Log.d(TAG, "onResponse:shecdule" + schtime);
                                    Toast.makeText(getApplication(), " SCHEDULE MESSAGE SEND SUCCESSFULLY", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplication(), "  ", Toast.LENGTH_LONG).show();

                                }
                            }
                        } catch (Exception e) {

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

    public boolean requestPermissionForCamera()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Add_WishList.this, Manifest.permission.SEND_SMS))
        {
            Toast.makeText(Add_WishList.this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            ActivityCompat.requestPermissions(Add_WishList.this, new String[]{Manifest.permission.SEND_SMS}, CAMERA_PERMISSION_REQUEST_CODE);
            return true;
        }
    }
}
package com.inevitablesol.www.shopmanagement.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.Validation.Validation;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;

public class Billing_notifications extends AppCompatActivity implements View.OnClickListener
{

    private RadioGroup radioGroup_reminder_wish_sms;
    private RadioButton radioButton_sms_yes,radioButton_sms_no;


    private  RadioGroup radioGroup_wish_mail;
    private  RadioButton radioButton_wish_mail_yes,getRadioButton_wish_mail__no;

    private GlobalPool globalPool;
    private static final String TAG = "Billing_notifications";
    private static final String MySETTINGS = "MySetting";
    private SharedPreferences sharedpreferences;

    private  RadioGroup radioGroup_wish_noti;
    private  RadioButton radioButton_wish_noti_yes, radioButton_wish_noti_no;

    private RadioGroup radioGroup_paymentReminder;
    private  RadioButton radioButton_paymentReminder_yes,radioButton_paymentReminder_no;
       private LinearLayout linearLayout_paymentReminder;
     private TextInputEditText et_paymentDays;

    private  RadioButton radioButton_makeBillYes,getRadioButton_makeBillNo;
    private  RadioGroup radioGroup_makeBill;

    private  RadioButton getRadioButton_makeBill_smsYes,getGetRadioButton_makeBill_smsno;
    private  RadioGroup radioGroup_makebill_sms;


    private  RadioGroup radioGroup_purchase_sms;
    private  RadioButton radioButton_purchase_sms_yes,getRadioButton_purchase_sms_no;

    private TextInputEditText et_mobile,et_email;

    private  RadioGroup rg_makebill_mail;
    private  RadioButton rb_makebill_mail_yes;
    private  RadioButton rb_makebill_mail_no;
    private AppCompatButton bt_save;

    // purchase mail

     private  RadioGroup  rg_purchase_noti;
     private  RadioButton rb_purchase_noti_yes;
     private  RadioButton rb_purchase_noti_no;


    private  RadioGroup  rg_purchase_mail;
    private  RadioButton rb_purchase_mail_yes;
    private  RadioButton rb_purchase_mail_no;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_notifications);
         radioGroup_reminder_wish_sms=(RadioGroup)findViewById(R.id.rg_wish_noti_sms);
         radioButton_sms_yes=(RadioButton)findViewById(R.id.rb_wish_sms_yes);
         radioButton_sms_no=(RadioButton)findViewById(R.id.rb_wish_sms_no);

          rg_makebill_mail=(RadioGroup)findViewById(R.id.rg_makeBill_mail);
          rb_makebill_mail_no=(RadioButton) findViewById(R.id.rb_makebill_mail_no);
          rb_makebill_mail_yes=(RadioButton)findViewById(R.id.rb_makebill_mail_yes);


          rg_purchase_noti=(RadioGroup)findViewById(R.id.rg_purchase_noti);
          rb_purchase_noti_no=(RadioButton)findViewById(R.id.rb_purchase_noti_no);
          rb_purchase_noti_yes=(RadioButton)findViewById(R.id.rb_purchase_noti_yes);



        rg_purchase_mail=(RadioGroup)findViewById(R.id.rg_purchase_mail);
        rb_purchase_mail_no=(RadioButton)findViewById(R.id.rb_purchase_mail_no);
        rb_purchase_mail_yes=(RadioButton)findViewById(R.id.rb_purchase_mail_yes);



        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                 globalPool=(GlobalPool)this.getApplicationContext();

        bt_save=(AppCompatButton)findViewById(R.id.save_details);
        radioGroup_wish_mail=(RadioGroup)findViewById(R.id.rg_wish_mail);
          radioButton_wish_mail_yes=(RadioButton)findViewById(R.id.rb_wish_mail_yes);
          getRadioButton_wish_mail__no=(RadioButton)findViewById(R.id.rb_wish_mail_no);

            radioGroup_wish_noti=(RadioGroup)findViewById(R.id.rg_wish_noti);
            radioButton_wish_noti_yes=(RadioButton)findViewById(R.id.rb_wish_noti_yes);
             radioButton_wish_noti_no=(RadioButton)findViewById(R.id.rb_wish_noti_no);
           radioGroup_paymentReminder=(RadioGroup)findViewById(R.id.rg_paymentARemainder);
          radioButton_paymentReminder_yes=(RadioButton)findViewById(R.id.rb_payment_reminder_yes);
          radioButton_paymentReminder_no=(RadioButton)findViewById(R.id.rb_payment_reminder_no);
         et_paymentDays=(TextInputEditText)findViewById(R.id.et_pay_dueDays);

         radioGroup_makeBill=(RadioGroup)findViewById(R.id.rg_makeBillreminder);
         getRadioButton_makeBillNo=(RadioButton)findViewById(R.id.rb_makebill_no_notification);
         radioButton_makeBillYes=(RadioButton)findViewById(R.id.rb_makebill_yes_notification);
         radioGroup_makebill_sms=(RadioGroup)findViewById(R.id.rg_makeBill_sms);
         getGetRadioButton_makeBill_smsno=(RadioButton)findViewById(R.id.rb_makebill_sms_no);
        // radioButton_sms_yes=(RadioButton)findViewById(R.id.rb_makebill_sms_yes);

        radioGroup_purchase_sms=(RadioGroup)findViewById(R.id.rg_purchase_sms);
        radioButton_purchase_sms_yes=(RadioButton)findViewById(R.id.rb_purchase_sms_yes);
        getRadioButton_purchase_sms_no=(RadioButton)findViewById(R.id.rb_purchase_sms_no);

        et_mobile =(TextInputEditText)findViewById(R.id.mobilenumber);
        et_email=(TextInputEditText)findViewById(R.id.email);

          if(globalPool instanceof GlobalPool)
          {
                if(globalPool!=null)
                {
                     et_email.setText(globalPool.getUser__noti_email());
                     et_mobile.setText(globalPool.getUser_noti_mobile());
                }
          }


        bt_save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String mobile= et_mobile.getText().toString().trim();
                String email=  et_email.getText().toString().trim();

                Log.d(TAG, "onClick: email"+email+" mobile"+mobile);
                Log.d(TAG, "onClick: Email"+Validation.isEmailValid(email));
                Log.d(TAG, "onClick: Mobile"+Validation.isValidPhone(mobile));

                if(Validation.isEmailValid(email) && Validation.isValidPhone(mobile))
                {
                    globalPool.setUser__noti_email(email);
                    globalPool.setUser_noti_mobile(mobile);
                    Toast.makeText(globalPool, "Added Successfully", Toast.LENGTH_SHORT).show();

                }else
                {
                    Toast.makeText(globalPool, "Please enter Valid Input", Toast.LENGTH_SHORT).show();
                }


            }
        });









        radioGroup_purchase_sms.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId) {
                    case R.id.rb_purchase_sms_yes:
                        purchase_Sms("yes");
                        globalPool.setPruchasesms(true);
                        break;
                    case R.id.rb_purchase_sms_no:
                        purchase_Sms("no");
                        globalPool.setPruchasesms(false);
                        break;
                }
            }
        });

        rg_purchase_noti.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId) {
                    case R.id.rb_purchase_noti_yes:
                        purchase_notification("yes");

                        break;
                    case R.id.rb_purchase_noti_no:
                        purchase_notification("no");

                        break;
                }
            }
        });


        rg_purchase_mail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId) {
                    case R.id.rb_purchase_mail_yes:
                        purchase_mail_status("yes");

                        break;
                    case R.id.rb_purchase_mail_no:
                        purchase_mail_status("no");
                        break;
                }
            }
        });

        radioGroup_makebill_sms.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                switch (checkedId)
                {
                    case R.id.rb_makebill_sms_yes:
                        makeBill_Sms("yes");
                        break;
                    case R.id.rb_makebill_sms_no:
                        makeBill_Sms("no");
                        break;

                }
            }
        });


        rg_makebill_mail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                switch (checkedId)
                {
                    case R.id.rb_makebill_mail_yes:
                        makeBill_mail("yes");
                        break;
                    case R.id.rb_makebill_mail_no:
                        makeBill_mail("no");
                        break;

                }
            }
        });





         linearLayout_paymentReminder=(LinearLayout)findViewById(R.id.linear_paymentReminder);
        radioGroup_makeBill.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId) {


                 switch (checkedId)
                 {
                     case R.id.rb_makebill_yes_notification:
                          break;
                     case  R.id.rb_makebill_no_notification:
                         break;

                 }
             }
         });

        linearLayout_paymentReminder.setVisibility(View.GONE);
        radioGroup_paymentReminder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                switch (checkedId)
                {
                    case R.id.rb_payment_reminder_yes:
                        globalPool.setReminderDueStatus(true);
                        savePaymentReminder("yes");
                        savePaymentReminder();
                        linearLayout_paymentReminder.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_payment_reminder_no:
                        globalPool.setReminderDueStatus(false);
                        savePaymentReminder("No");
                         linearLayout_paymentReminder.setVisibility(View.GONE);
                        break;
                }
            }
        });
        et_paymentDays.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                Log.d(TAG, "afterTextChanged: Days"+s);
               // savePaymentReminder("yes");
                savePaymentReminder();
                linearLayout_paymentReminder.setVisibility(View.VISIBLE);

            }
        });



        radioGroup_wish_noti.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                switch (checkedId)
                {
                    case R.id.rb_wish_noti_yes:
                        saveWishListStatus_noti("yes");
                        break;
                    case R.id.rb_wish_noti_no:
                        saveWishListStatus_noti("No");
                        break;
                }
            }
        });


        radioGroup_wish_mail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                switch (checkedId)
                {
                    case R.id.rb_wish_mail_yes:
                        saveWishListStatus_mail("yes");
                        break;
                    case R.id.rb_wish_mail_no:
                        saveWishListStatus_mail("No");
                        break;
                }
            }
        });






        radioGroup_reminder_wish_sms.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.rb_wish_sms_yes:
                        saveWishListsms("yes");
                        break;
                    case R.id.rb_wish_sms_no:
                        saveWishListsms("No");
                        break;
                }
            }
        });



            sharedpreferences = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
            try
            {
                String Status_sms = (sharedpreferences.getString("wishList_sms", null));
             if(Status_sms !=null)
             {
                 if (Status_sms.equalsIgnoreCase("Yes"))
                 {

                     radioButton_sms_yes.setChecked(true);

                 } else
                     {

                     radioButton_sms_no.setChecked(true);

                 }
             }
            Log.d(TAG, "onCreate:WishList sms "+Status_sms);
        }catch (Exception e)
        {

        }
        try
        {
            String Status_reminder = (sharedpreferences.getString("wishList_reminder", null));
            if(Status_reminder !=null)
            {
                if (Status_reminder.equalsIgnoreCase("Yes"))
                {
                    radioButton_wish_noti_yes.setChecked(true);

                } else
                {

                    radioButton_wish_noti_no.setChecked(true);

                }
            }
            Log.d(TAG, "onCreate:WishList noti"+Status_reminder);
        }catch (Exception e)
        {

        }

        sharedpreferences = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
        try
        {
            String Status_mail = (sharedpreferences.getString("wishList_mail", null));
            if(Status_mail !=null)
            {
                if (Status_mail.equalsIgnoreCase("Yes"))
                {
                    radioButton_wish_mail_yes.setChecked(true);

                } else
                {
                    getRadioButton_wish_mail__no.setChecked(true);

                }
            }
            Log.d(TAG, "onCreate:WishList email "+Status_mail);
        }catch (Exception e)
        {

        }




        sharedpreferences = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
        try
        {
            String purchase_sms = (sharedpreferences.getString("purchase_sms", null));
            if(purchase_sms !=null)
            {
                if (purchase_sms.equalsIgnoreCase("Yes"))
                {
                    radioButton_purchase_sms_yes.setChecked(true);

                } else
                {
                    getRadioButton_purchase_sms_no.setChecked(true);

                }
            }
            Log.d(TAG, "onCreate:purchase_sms"+purchase_sms);
        }catch (Exception e)
        {

        }


        sharedpreferences = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
        try
        {
            String Status_sms = (sharedpreferences.getString("makeBill_sms", null));
            if(Status_sms !=null)
            {
                if (Status_sms.equalsIgnoreCase("Yes"))
                {
                    radioButton_makeBillYes.setChecked(true);

                } else {
                    getRadioButton_makeBillNo.setChecked(true);

                }
            }
            Log.d(TAG, "onCreate:Make a bill Sms "+Status_sms);
        }catch (Exception e)
        {

        }


        sharedpreferences = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
        try
        {
            String Status_mail = (sharedpreferences.getString("makeBill_mail", null));
            if(Status_mail !=null)
            {
                if (Status_mail.equalsIgnoreCase("Yes"))
                {
                    rb_makebill_mail_yes.setChecked(true);

                } else
                    {

                    rb_makebill_mail_no.setChecked(true);

                }
            }
            Log.d(TAG, "onCreate:Make a bill mail "+Status_mail);
        }catch (Exception e)
        {

        }



        try
        {
            String Status_paymentreminder = (sharedpreferences.getString("paymentReminder", null));
            String Status_Days = (sharedpreferences.getString("paymentReminder_Days", null));

            Log.d(TAG, "onCreate: days"+Status_paymentreminder);
            Log.d(TAG, "onCreate: Payment Reminder"+Status_Days);

            if(Status_paymentreminder !=null)
            {
                if (Status_paymentreminder.equalsIgnoreCase("Yes"))
                {
                    radioButton_paymentReminder_yes.setChecked(true);
                    et_paymentDays.setText(Status_Days);

                } else
                {
                    radioButton_paymentReminder_no.setChecked(true);

                }
            }
            Log.d(TAG, "onCreate: Payment WishList"+Status_paymentreminder);
        }catch (Exception e)
        {

        }


        try
        {

            String purchase_noti = (sharedpreferences.getString("purchase_noti", null));


            Log.d(TAG, "onCreate: Payment Reminder"+purchase_noti);

            if(purchase_noti !=null)
            {
                if (purchase_noti.equalsIgnoreCase("Yes"))
                {
                    rb_purchase_noti_yes.setChecked(true);


                } else
                {
                    rb_purchase_noti_no.setChecked(true);

                }
            }
            Log.d(TAG, "onCreate :purchase_noti"+purchase_noti);
        }catch (Exception e)
        {

        }


        try
        {

            String purchase_mail = (sharedpreferences.getString("purchase_mail", null));


            Log.d(TAG, "onCreate: purchase_mail"+purchase_mail);

            if(purchase_mail !=null)
            {
                if (purchase_mail.equalsIgnoreCase("Yes"))
                {
                    rb_purchase_mail_yes.setChecked(true);


                } else
                {
                    rb_purchase_mail_no.setChecked(true);

                }
            }
            Log.d(TAG, "onCreate :purchase_mail"+purchase_mail);
        }catch (Exception e)
        {

        }








    }

    private void purchase_mail_status(String status)
    {
        Log.d(TAG, "saveStatus:purchase mail"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("purchase_mail",status);
        editor.commit();

    }

    private void purchase_notification(String status)
    {
        Log.d(TAG, "saveStatus:purchase sms"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("purchase_noti",status);
        editor.commit();

    }

    private void makeBill_mail(String status)
    {
        Log.d(TAG, "saveStatus:make bill mail"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("makeBill_mail",status);
        editor.commit();
    }

    private void purchase_Sms(String status)
    {

        Log.d(TAG, "saveStatus:purchase sms"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("purchase_sms",status);
        editor.commit();

    }

    private void makeBill_Sms(String status)
    {

        Log.d(TAG, "saveStatus:make bill sms"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("makeBill_sms",status);
        editor.commit();

    }

    private void savePaymentReminder()
    {
        String Days= et_paymentDays.getText().toString().trim();
        globalPool.setReminderDays(Days);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("paymentReminder_Days",Days);
        editor.commit();
    }

    private void savePaymentReminder(String status)
    {

        Log.d(TAG, "saveStatus: payment_reminder"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("paymentReminder",status);
        editor.commit();
    }

    private void saveWishListStatus_noti(String status)
    {
        Log.d(TAG, "saveStatus: reminder"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("wishList_reminder",status);
        editor.commit();

    }

    private void saveWishListStatus_mail(String status)
    {

        Log.d(TAG, "saveStatus:mail"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("wishList_mail",status);
        editor.commit();

    }


    private void saveWishListsms(String status)
    {
        Log.d(TAG, "saveStatus:WishList"+status);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("wishList_sms",status);
        editor.commit();

    }


    @Override
    public void onClick(View v)
    {


    }
}

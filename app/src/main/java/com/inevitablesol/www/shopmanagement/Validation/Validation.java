package com.inevitablesol.www.shopmanagement.Validation;

import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anup Borde on 09-10-2015.
 */
@SuppressWarnings({"ALL", "BooleanMethodIsAlwaysInverted"})
public class Validation
{
    //Regular Expression
    private static final String PHONE_REGEX = "\\d{3}-\\d{7}";
    //Error msg
    private static final String PHONE_MSG = "Invalid Number";
    private static final String REQUIRED_MSG = "required";
    private static final String PASSWORD_MSG ="INVALID PASSWORD";

    private static final String TAG = "Validation";

    // call this method when you need to check phone number validation
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isPhoneNumber(TextInputEditText editText, boolean required) {
        String number = editText.getText().toString().trim();
        editText.setError(null);
        if (number.length()<10)
        {

                editText.setError(PHONE_MSG);
                return false;


        }
        return true;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public  static  boolean isValidPhone(String phone)
    {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone))
        {
            if(phone.length() < 10 )
            {

                check = false;

            }
            else
            {
                check = true;

            }
        }
        else
        {
            check=false;
        }
        return check;
    }

    public static boolean isValidPassword(EditText editText, boolean required) {
        String password = editText.getText().toString().trim();
        editText.setError(null);
        if (password.length()<8)
        {

            editText.setError(PASSWORD_MSG);
            return false;


        }
        return true;
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        };

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0)
        {
            Log.d(TAG, "hasText:Length"+text.length());
            editText.setError(REQUIRED_MSG);
            return false;
        }else
        {
            return true;
        }


    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(TextInputEditText editText) {

        String text = editText.getText().toString().trim();
        Log.d(TAG, "hasText:Length"+text);
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0)
        {
            Log.d(TAG, "hasText:Length"+text.length());
            editText.setError(REQUIRED_MSG);
            return false;
        }
        Log.d(TAG, "hasText: Test");
        return true;
    }


    public static boolean hasText(TextView textView) {

        String text = textView.getText().toString().trim();
        textView.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            textView.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }



    public static boolean checkQty(TextView textView) {

        String text = textView.getText().toString().trim();
        int qty = Integer.parseInt(text);
        textView.setError(null);

        // length 0 means there is no text
        if (qty == 0) {
            textView.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }
}

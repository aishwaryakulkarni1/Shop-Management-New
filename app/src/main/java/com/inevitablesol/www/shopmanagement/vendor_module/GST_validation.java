package com.inevitablesol.www.shopmanagement.vendor_module;

import android.util.Log;

/**
 * Created by Pritam on 06-11-2017.
 */

public class GST_validation
{
    private static final String TAG = "GST_validation";

    private static boolean flag ;

    int leng ;


    public  boolean isValidGst(String  gstIn) {

        if (gstIn.length() == 15)
        {
            int leng = gstIn.length();
            int i=0;

            while (leng != i) {
                if (i < 2) {
                    if (Character.isDigit(gstIn.charAt(i)))
                    {
                        Log.d(TAG, "isValidGst:if" + "i" + i + gstIn.charAt(i));
                        i++;
                    } else {
                        Log.d(TAG, "isValidGst:else" + "i" + i + gstIn.charAt(i));
                        return false;
                    }
                } else if (i >= 2 && i < 7) {
                    if (Character.isLetter(gstIn.charAt(i))) {
                        Log.d(TAG, "isValidGst 2 -7:if" + "i" + i + gstIn.charAt(i));
                        i++;
                    } else {
                        Log.d(TAG, "isValidGst 2 -7:else" + "i" + i + gstIn.charAt(i));
                        return false;
                    }
                } else if (i >= 7 && i < 11) {
                    if (Character.isDigit(gstIn.charAt(i))) {
                        Log.d(TAG, "isValidGst 7 -12:if" + "i" + i + gstIn.charAt(i));
                        i++;
                    } else {
                        Log.d(TAG, "isValidGst 7 -12:else" + "i" + i + gstIn.charAt(i));
                        return false;
                    }

                } else if (i == 11) {
                    if (Character.isLetter(gstIn.charAt(i))) {


                        Log.d(TAG, "isValidGst 11:if" + "i" + i + gstIn.charAt(i));
                        i++;
                    } else {
                        Log.d(TAG, "isValidGst 7 -11:else" + "i" + i + gstIn.charAt(i));
                        return false;
                    }

                } else if (i == 12) {
                    if (Character.isDigit(gstIn.charAt(i))) {

                        Log.d(TAG, "isValidGst 12:if" + "i" + i + gstIn.charAt(i));
                        i++;

                    } else {

                        return false;
                    }

                } else if (i == 13)
                {
                    if (Character.isLetter(gstIn.charAt(i))) {

                        Log.d(TAG, "isValidGst 13:if" + "i" + i + gstIn.charAt(i));
                        i++;


                    } else {
                        return false;
                    }

                } else
                {


                        Log.d(TAG, "isValidGst 14:if" + "i" + i + gstIn.charAt(i));
                        i++;
                }

            }
            } else
             {
                 Log.d(TAG, "isValidGst:else");

            return false;

            }
            return true;
        }


    public static boolean isValid_Gst(String gst)
    {
         boolean flag=true;
        if (gst.length() == 15) 
        {
            int leng = gst.length();
            int i = 0;
            Log.d(TAG, "isValid_Gst: Length"+leng);

            while (i!= 14)
            {
              
                if(i==0 || i== 1 || i==7 || i== 8 || i==9 || i==10 || i ==12 )
                {
                    Log.d(TAG, "isValid_Gst: Digit  Value of i"+i);
                        if(!Character.isDigit(gst.charAt(i)))
                        {
                            Log.d(TAG, "isValid_Gst: not valid ");
                           flag=false;
                            break;
                        }
                }
                else
                {
                    Log.d(TAG, "isValid_Gst: (Letter Value of i"+i);
                    if(!Character.isLetter(gst.charAt(i)))
                    {
                        flag=false;
                        Log.d(TAG, "isValid_Gst:Letter  not valid ");
                        break;
                    }
                }
                i++;
                

            }
        }else
            {
               
                Log.d(TAG, "isValidGst:else");
                return flag=false;
            }
          return  flag;

        }



}

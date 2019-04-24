package com.inevitablesol.www.shopmanagement.LonginDetails;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Pritam on 10-05-2017.
 */

public class ReadSmsForOTP extends BroadcastReceiver
{
    public ReadSmsForOTP() {
    }
  //  private static SmsListener mListener;
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent)
    {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody().split(":")[1];
                    Log.d("lenghtmessage",message);

                    //message = message.substring(0, message.length());
                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    // mListener.messageReceived(message);

                    Intent myIntent = new Intent("otpAdded");
                    myIntent.putExtra("message",message);
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//                    Intent intent1 = new Intent("");
                    context.sendBroadcast(myIntent);
                    // Show Alert

                } // end for loop
            } // bundle is null

        } catch (Exception e)
        {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }

    }
}

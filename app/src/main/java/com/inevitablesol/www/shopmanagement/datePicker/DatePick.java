package com.inevitablesol.www.shopmanagement.datePicker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.analysis.Total_Analyasis;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pritam on 05-02-2018.
 */

public class DatePick extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    private static final String TAG = "DatePick";
    private static  DatePick datePick;
     Total_Analyasis activity;


    private  DatePick(Total_Analyasis activity)
    {
        this.activity=  activity;

    }
      public static DatePick  getInastance(Total_Analyasis activity)
      {

             if(datePick==null)
             {
                 datePick=new DatePick(activity);
             }
          return datePick;
      }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        final Calendar c = Calendar.getInstance();
        Log.d(TAG, "onDateSet: ");
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date date1 = c.getTime();

        //updateDate(myCalendar);
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(c.getTime());
        Log.d(TAG, "onClick:Result"+currentDateTimeString);
         activity.setDate(currentDateTimeString);
       // textView.setText(currentDateTimeString);

    }

    public  interface  ShowDate
    {
        void  setDate(String text);
    }
}

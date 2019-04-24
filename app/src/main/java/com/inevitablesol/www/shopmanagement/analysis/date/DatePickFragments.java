package com.inevitablesol.www.shopmanagement.analysis.date;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.inevitablesol.www.shopmanagement.report.ReportFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pritam on 18-12-2017.
 */
public class DatePickFragments extends DialogFragment  implements DatePickerDialog.OnDateSetListener
{
    private static final String TAG = "DatePickFragments";
    private static DatePickFragments ourInstance = null;
    private OnDateSetItem onDateSetItem;
    public  static ReportFragment reportFragment;

    private FragmentActivity fragmentActivity;

//    public DatePickFragments(ReportFragment onClickListener)
//    {
//        this.reportFragment=onClickListener;
//    }

     public DatePickFragments() {

    }

    public static DatePickFragments getInstance(ReportFragment onClickListener)
    {
        reportFragment=onClickListener;

        if (ourInstance == null)
            ourInstance = new DatePickFragments();

        return ourInstance;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        return dialog;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //updateDate(myCalendar);
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(myCalendar.getTime());
//        txtdate.setText(currentDateTimeString);

        if(onDateSetItem!=null)
        {
            onDateSetItem.getDate(currentDateTimeString);

        }else
        {
            if(reportFragment instanceof ReportFragment)
            {
                reportFragment.getDate(currentDateTimeString);
            }else {
                Log.d(TAG, "onDateSet: ");
            }
           
        }

    }

    @Override
    public void onAttach(Context context)
    {

        super.onAttach(context);
        if(context instanceof OnDateSetItem)
        {
            onDateSetItem=(OnDateSetItem)context;
        }

        Log.d(TAG, "onAttach:" + context.toString());
    }

    public  interface  OnDateSetItem
    {
        public void getDate(String date);
    }



}

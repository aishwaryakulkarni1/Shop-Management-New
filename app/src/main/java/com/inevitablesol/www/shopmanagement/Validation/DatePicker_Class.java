package com.inevitablesol.www.shopmanagement.Validation;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Pritam on 25-01-2018.
 */

public class DatePicker_Class
{
    private final String TAG = "DatePicker_Class";
    private  String   formatedDate;

    private  static  DatePicker_Class datePicker_class;


    private  DatePicker_Class()
    {

    }

                              public static DatePicker_Class getInstance()
                              {
                                  if (datePicker_class==null)
                                  {
                                    return    new DatePicker_Class();
                                  }else
                                  {
                                      return datePicker_class;
                                  }
                              }

    public String  getMonth(Context context, final TextView textView)
    {

        new YearMonthPickerDialog(context, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);

                SimpleDateFormat dateFormat =new java.text.SimpleDateFormat("yyyy-MM-dd");

                Log.d(TAG, "onYearMonthSet: " + dateFormat.format(calendar.getTime()));
                formatedDate= dateFormat.format(calendar.getTime());
                textView.setText(formatedDate);
            }
        }).show();
        return    formatedDate;

    }

    public String  getYear(Context context) {

        new YearMonthPickerDialog(context, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                Log.d(TAG, "onYearMonthSet: " + dateFormat.format(calendar.getTime()));
                formatedDate= dateFormat.format(calendar.getTime());

            }

        }).show();

           return    formatedDate;
    }

}

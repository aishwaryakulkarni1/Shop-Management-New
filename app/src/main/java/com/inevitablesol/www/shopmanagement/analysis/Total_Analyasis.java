package com.inevitablesol.www.shopmanagement.analysis;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.datePicker.DatePick;
import com.itextpdf.text.Image;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Total_Analyasis extends AppCompatActivity implements DatePick.ShowDate
{

    private RadioGroup radioGroup;
    private TextView txt_id,txt_Date,txt_weekTodate,txt_weekfromDate,txt_month,txt_year;
    private static final String TAG = "Total_Analyasis";

    private AppCompatButton imgView;

    private   ImageView  mDayImage,weekFromDate,weekTodate;
   private  Total_Analyasis total_analyasis=this;
    private ImageView month;
    private String nextMonth;
    private String nextYear;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total__analyasis);
        txt_Date=(TextView)findViewById(R.id.txt_date);
        radioGroup=(RadioGroup)findViewById(R.id.report_analyasis);
       // weekTodate=(ImageView)findViewById(R.id.img_weekTodate);
        txt_weekfromDate=(TextView)findViewById(R.id.txt_fromdate) ;
        txt_month=(TextView)findViewById(R.id.txt_month);
        txt_year=(TextView)findViewById(R.id.year_analysis);

         currentDate();

        txt_weekTodate=(TextView)findViewById(R.id.txt_todate);
        ((ImageView)findViewById(R.id.month_img)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chageMonth();
            }
        });

        ((ImageView)findViewById(R.id.year_ic)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chageYear();
            }


        });
        weekFromDate=(ImageView)findViewById(R.id.img_weekFromdate);

        weekFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                 changeDate(txt_weekfromDate);

            }
        });
//        weekTodate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//
//               // changeDate(txt_weekTodate);
//
//            }
//        });

        mDayImage=(ImageView)findViewById(R.id.img_analysis_date);
        mDayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                changeDate(txt_Date);
//                Log.d(TAG, "onClick:"+txt_Date);
//                DatePick  datePick=DatePick.getInastance(total_analyasis);
//                datePick.show(getSupportFragmentManager(), "datePicker");

            }
        });


        imgView=(AppCompatButton)findViewById(R.id.totalView_analysis);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                try
                {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) findViewById(selectedId);
                    String data=radioButton.getText().toString().trim();

                    Log.d(TAG, "onClick:"+data);


                    if(data.equalsIgnoreCase("Day"))
                    {
                        Intent intent=new Intent(Total_Analyasis.this,Total_DayAnalysis.class);
                        intent.putExtra("Date",txt_Date.getText());
                         if(TextUtils.isEmpty(txt_Date.getText()))
                         {
                             Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_SHORT).show();
                         }else
                         {
                             startActivity(intent);
                         }


                    }else if(data.equalsIgnoreCase("Week"))
                    {
                        Intent intent=new Intent(Total_Analyasis.this,Total_WeekAnalysis.class);
                          intent.putExtra("to_date",txt_weekTodate.getText().toString().trim());
                           intent.putExtra("from_date",txt_weekfromDate.getText().toString().trim());
                        if(TextUtils.isEmpty(txt_weekfromDate.getText()))
                        {
                            Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            startActivity(intent);
                        }

                    }else if(data.equalsIgnoreCase("month"))
                    {
                        Intent intent=new Intent(Total_Analyasis.this,Total_MonthAnalysis.class);
                        intent.putExtra("to_date",nextMonth);
                        intent.putExtra("from_date",txt_month.getText().toString().trim());
                        if(!TextUtils.isEmpty(txt_month.getText()))
                        {
                            startActivity(intent);
                        }else
                        {
                            Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_SHORT).show();
                            //
                        }

                      //  startActivity(new Intent(Total_Analyasis.this,Total_MonthAnalysis.class));

                    }else
                    {

                        Intent intent=new Intent(Total_Analyasis.this,Total_YearAnalysis.class);
                        intent.putExtra("to_date",nextYear);
                        intent.putExtra("from_date",txt_year.getText().toString().trim());
                        if(!TextUtils.isEmpty(txt_year.getText()))
                        {
                            startActivity(intent);
                        }else
                        {
                            Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_SHORT).show();
                            //
                        }
                       // startActivity(new Intent(Total_Analyasis.this,Total_YearAnalysis.class));

                    }
                } catch (NullPointerException e)
                {
                    Toast.makeText(Total_Analyasis.this,"Please select", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }
        });


//

    }

    private void currentDate()
    {
        Calendar calendar = Calendar.getInstance();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeString = dateFormat.format(calendar.getTime());
        txt_Date.setText(currentDateTimeString);

    }


    private void chageYear()
    {

        new YearMonthPickerDialog(this, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month)
            {
                Log.d(TAG, "onYearMonthSet: Year Month"+year+" "+month);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, 0);
               calendar.set(Calendar.DAY_OF_MONTH, 1);


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                txt_year.setText(dateFormat.format(calendar.getTime()));
                // calendar.set(calendar.MONTH,month+1);
                Log.d(TAG, "onYearSet: " + dateFormat.format(calendar.getTime()));
                calendar.add(Calendar.YEAR,1);
                calendar.add(Calendar.DATE,-1);
//                calendar.set(Calendar.MONTH, 0);
//                calendar.set(Calendar.DAY_OF_WEEK, 1);

                nextYear=dateFormat.format(calendar.getTime());
                Log.d(TAG, "onYearSet: " + nextYear);

            }
        }).show();
    }


    private void chageMonth()
    {

        new YearMonthPickerDialog(this, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month)
            {
                Log.d(TAG, "onYearMonthSet: Year Month"+year+" "+month);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, 1);


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                txt_month.setText(dateFormat.format(calendar.getTime()));
               // calendar.set(calendar.MONTH,month+1);
                Log.d(TAG, "onYearMonthSet: " + dateFormat.format(calendar.getTime()));
                calendar.add(Calendar.MONTH,1);
                calendar.add(Calendar.DATE,-1);

                  nextMonth=dateFormat.format(calendar.getTime());

                    //calendar.add(Calendar.DATE,-1);

               // nextMonth=dateFormat.format(calendar.getTime());


                Log.d(TAG, "onYearMonthSet:" + dateFormat.format(calendar.getTime()));




            }
        }).show();
    }

    private void changeDate(final TextView txt_date)
    {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date date1 = myCalendar.getTime();

                //updateDate(myCalendar);
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                txt_date.setText(currentDateTimeString);

                 myCalendar.add(Calendar.DAY_OF_MONTH,6);

                txt_weekTodate.setText(dateFormat.format(myCalendar.getTime()));

            }

        };

        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        Log.d("date", String.valueOf(myCalendar.getTime()));
    }


    @Override
    public void setDate(String text) {
        Log.d(TAG, "setDate: "+text);
        txt_Date.setText(text);
    }
}

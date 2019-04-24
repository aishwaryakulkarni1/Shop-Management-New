package com.inevitablesol.www.shopmanagement.analysis;

import android.app.DatePickerDialog;
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
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Customer_Analyasis extends AppCompatActivity
{

    private RadioGroup radioGroup;
    private RadioButton  analysis_week;

    private AppCompatButton  view_analysis;
    private static final String TAG = "Customer_Analyasis";
    private ImageView weekFromDate;
    private TextView txt_weekfromDate;
    private TextView txt_weekTodate;
    private  TextView txt_month;
    private String nextMonth;
    private TextView txt_year;
    private String nextYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__analyasis);
        txt_weekfromDate=(TextView)findViewById(R.id.txt_fromdate) ;
        txt_weekTodate=(TextView)findViewById(R.id.txt_todate);
        radioGroup=(RadioGroup)findViewById(R.id.cust_report_analysis);
        view_analysis=(AppCompatButton)findViewById(R.id.customer_viewanalysis);
        weekFromDate=(ImageView)findViewById(R.id.img_weekFromdate);
        txt_month=(TextView)findViewById(R.id.txt_month);

        txt_year=(TextView)findViewById(R.id.year_analysis);
        ((ImageView)findViewById(R.id.month_img)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chageMonth();
            }
        });

        weekFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                changeDate(txt_weekfromDate);

            }
        });

        ((ImageView)findViewById(R.id.year_ic)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chageYear();
            }


        });
        
        view_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {

                try
                {

                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton)findViewById(selectedId);
                    String data=radioButton.getText().toString().trim();
                    Log.d(TAG, "onClick:"+data);
                    if(data.equalsIgnoreCase("week"))
                    {
                        Intent intent=new Intent(Customer_Analyasis.this,Customer_weekAnalysis.class);
                        intent.putExtra("to_date",txt_weekTodate.getText().toString().trim());
                        intent.putExtra("from_date",txt_weekfromDate.getText().toString().trim());
                        if(TextUtils.isEmpty(txt_weekfromDate.getText()))
                        {
                            Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            startActivity(intent);
                        }

                       // startActivity(new Intent(Customer_Analyasis.this,Customer_weekAnalysis.class));

                    }else if(data.equalsIgnoreCase("month"))
                    {

                        Intent intent=new Intent(Customer_Analyasis.this,Customer_monthAnalysis.class);
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
                      //  startActivity(new Intent(Customer_Analyasis.this,Customer_monthAnalysis.class));

                    }else
                    {

                        Intent intent=new Intent(Customer_Analyasis.this,Customer_YearAnalysis.class);
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
                       // startActivity(new Intent(Customer_Analyasis.this,Customer_YearAnalysis.class));

                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });

    }

    @Override
    public void onBackPressed() 
    {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed:");
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

        DatePickerDialog dpdialog =  new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dpdialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
        dpdialog.show();

        Log.d("date", String.valueOf(myCalendar.getTime()));
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
                Log.d(TAG, "onYearMonthSet:" + dateFormat.format(calendar.getTime()));




            }
        }).show();
    }

}

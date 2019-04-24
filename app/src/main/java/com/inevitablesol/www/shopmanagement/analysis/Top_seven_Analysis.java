package com.inevitablesol.www.shopmanagement.analysis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Top_seven_Analysis extends AppCompatActivity
{

    private AppCompatButton img_topSeven;
    private TextView txt_month,txt_year;
    private static final String TAG = "Top_seven_Analysis";
    private String nextMonth;
    private String nextYear;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_seven_analysis);
        img_topSeven = (AppCompatButton) findViewById(R.id.topseven_viewanalysis);
        txt_month = (TextView) findViewById(R.id.txt_month);
        txt_year = (TextView) findViewById(R.id.txt_year);
        radioGroup=(RadioGroup)findViewById(R.id.top_analysis);

        img_topSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton)findViewById(selectedId);
                    String data=radioButton.getText().toString().trim();
                    Log.d(TAG, "onClick:"+data);

                    if(data.equalsIgnoreCase("Month"))
                    {
                        Intent intent=new Intent(Top_seven_Analysis.this,Top_seven_View.class);
                        intent.putExtra("id","1");
                        intent.putExtra("from_date",txt_month.getText().toString().trim());
                        if(!TextUtils.isEmpty(txt_month.getText()))
                        {
                            startActivity(intent);
                        }else
                        {
                            Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_SHORT).show();
                            //
                        }

                    }else
                    {

                        Intent intent=new Intent(Top_seven_Analysis.this,Top_seven_View.class);
                        intent.putExtra("id","2");
                        intent.putExtra("from_date",txt_year.getText().toString().trim());
                        if(!TextUtils.isEmpty(txt_year.getText()))
                        {
                            startActivity(intent);
                        }else
                        {
                            Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_SHORT).show();
                            //
                        }

                    }
                } catch (Exception e)
                {
                    Toast.makeText(Top_seven_Analysis.this, "Please Select Something", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                // startActivity(new Intent(Top_seven_Analysis.this, Top_seven_View.class));
            }
        });

        ((ImageView) findViewById(R.id.month_img)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chageMonth();

            }
        });

        ((ImageView) findViewById(R.id.year_ic)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chageYear();

            }
        });
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



    }


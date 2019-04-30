package com.inevitablesol.www.shopmanagement.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;

public class PrinterSetting extends AppCompatActivity {

    private RadioGroup radioGroup_Printer;
    private RadioButton radioButton_thermal,radioButton_regular;
    GlobalPool globalPool;
    private SharedPreferences sharedpreferences;
    private static final String TAG = "PrinterSetting";
    private static final String MySETTINGS = "MySetting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_setting);

        radioGroup_Printer = findViewById(R.id.rg_printer);
        radioButton_thermal = findViewById(R.id.rb_thermal_print);
        radioButton_regular = findViewById(R.id.rb_regular_print);
        globalPool = (GlobalPool) getApplicationContext();

        radioGroup_Printer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked) {

                Log.d(TAG,"onCheckedChanged"+checked);
                switch(checked){
                    case R.id.rb_thermal_print:
                        savePrinterStatus("enable");
                        globalPool.setPrinterStatus(true);
                        break;

                    case R.id.rb_regular_print:
                        savePrinterStatus("disable");
                        globalPool.setPrinterStatus(false);
                        break;
                }
            }
        });

        sharedpreferences = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);
        try{
            String printer = sharedpreferences.getString("printer",null);

            if(printer!=null){
                if(printer.equalsIgnoreCase("yes"))
                    radioButton_thermal.setChecked(true);
                else
                    radioButton_regular.setChecked(true);
            }
        }
        catch(Exception e){
            Log.d(TAG, "onCreate() returned: " +e);
        }
    }

    private void savePrinterStatus(String status){
        Log.d(TAG,"saveStatus: printer"+status);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("printer",status);
        editor.commit();
    }
}
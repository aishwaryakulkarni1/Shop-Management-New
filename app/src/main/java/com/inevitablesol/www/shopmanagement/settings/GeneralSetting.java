package com.inevitablesol.www.shopmanagement.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.inevitablesol.www.shopmanagement.R;

public class GeneralSetting extends AppCompatActivity
{

    private LinearLayout ll_theme,ll_invocieTheme,ll_printerSetting,ll_inventorySetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_setting);
        ll_invocieTheme=(LinearLayout)findViewById(R.id.ll_invoceTheme);
        ll_theme=(LinearLayout)findViewById(R.id.ll_theme);
        ll_printerSetting=(LinearLayout)findViewById(R.id.ll_printingTheme);
        ll_inventorySetting=(LinearLayout)findViewById(R.id.ll_inventorySetting);
        ll_inventorySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(GeneralSetting.this,InventorySetting.class));
            }
        });

        ll_theme.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(GeneralSetting.this,CustomAppTheme.class));

            }
        });

        ll_invocieTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(GeneralSetting.this,InvocieTheme.class));

            }
        });

        ll_printerSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(GeneralSetting.this,PrinterSetting.class));

            }
        });
    }
}

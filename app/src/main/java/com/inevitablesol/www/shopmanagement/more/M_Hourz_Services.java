package com.inevitablesol.www.shopmanagement.more;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.inevitablesol.www.shopmanagement.R;

public class M_Hourz_Services extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m__hourz__services);
        ((AppCompatButton)findViewById(R.id.access_service)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(M_Hourz_Services.this,More_Services.class));

            }
        });
    }
}

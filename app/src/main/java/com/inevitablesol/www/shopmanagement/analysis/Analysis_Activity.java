package com.inevitablesol.www.shopmanagement.analysis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.R;

public class Analysis_Activity extends BaseActivity
{

    private ImageView img_topsenven,img_totalAnalysis;
    private  ImageView img_custView;
    private static final String TAG = "Analysis_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_analysis);
        getLayoutInflater().inflate(R.layout.activity_analysis, frameLayout);
        img_topsenven=(ImageView)findViewById(R.id.img_topseven_analysis);
        img_totalAnalysis=(ImageView)findViewById(R.id.total_analysis);
        img_custView=(ImageView)findViewById(R.id.img_custId);

        img_custView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                startActivity(new Intent(Analysis_Activity.this,Customer_Analyasis.class));

            }
        });
        img_totalAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Analysis_Activity.this,Total_Analyasis.class));

            }
        });
        img_topsenven.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Analysis_Activity.this,Top_seven_Analysis.class));
            }
        });

        String logCheck =listArray[position];
        if (logCheck.equalsIgnoreCase("Logout"))
        {
//            setTitle(listArray[0]);
            toolbar.setTitle(listArray[0]);
        }else
        {

//            setTitle(listArray[position]);
            toolbar.setTitle(listArray[position]);
        }


    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
    }
}

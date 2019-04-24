package com.inevitablesol.www.shopmanagement.report;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.inevitablesol.www.shopmanagement.R;

public class ReportActivity_Dailog extends AppCompatActivity implements ReportFragment.OnFragmentInteractionListener {

    private static final String TAG = "ReportActivity_Dailog";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testfragment);
        ReportFragment reportFragment=new ReportFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        Bundle b= getIntent().getExtras();

        b.putString("DayBook","DayBook");
        b.putString("Discount","Discount");
        b.putString("ProfitLoss","ProfitLoss");
        b.putString("Sales","Sales");
        b.putString("Purchase","Purchase");
        b.putString("Expenses","Expenses");
        b.putString("Tax","Tax");
        b.putString("TaxRate","TaxRate");
        reportFragment.setArguments(b);
        fragmentTransaction.add(R.id.fragment,reportFragment);
        fragmentTransaction.commit();


    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {
        Log.d(TAG, "onFragmentInteraction: ");

    }
}

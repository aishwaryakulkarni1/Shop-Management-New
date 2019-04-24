package com.inevitablesol.www.shopmanagement.customer_module;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.billing_module.Billing_ChequeDetails;
import com.inevitablesol.www.shopmanagement.settings.Billing_Settings;

import static com.inevitablesol.www.shopmanagement.R.id.ViewCustomer;

public class CustomerManagement extends BaseActivity implements View.OnClickListener {
  private   ImageView addCustomer,viewCustomer;
    private ImageView editcustomer;
    private static final String TAG = "CustomerManagement";
    private static final int MENU_ITEM_ITEM1 = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_customer_management);
        getLayoutInflater().inflate(R.layout.activity_customer_management, frameLayout);


        mDrawerList.setItemChecked(position, true);
        addCustomer=(ImageView)findViewById(R.id.addCustmor);
        addCustomer.setOnClickListener(this);
        editcustomer=(ImageView)findViewById(R.id.editCust);
        editcustomer.setOnClickListener(this);
        viewCustomer=(ImageView)findViewById(ViewCustomer);
        viewCustomer.setOnClickListener(this);

//        setTitle(listArray[position]);
        toolbar.setTitle(listArray[position]);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        setSupportActionBar(toolbar);

      //  toolbar.getMenu().add(Menu.NONE,MENU_ITEM_ITEM1,Menu.NONE,R.string.ExportCust);

        toolbar.inflateMenu(R.menu.menu_cust);

        ;
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.addCustmor:
                startActivity(new Intent(CustomerManagement.this,AddCustomer.class));
                break;
            case  R.id.editCust:
                  startActivity(new Intent(CustomerManagement.this,UpdateCustomer.class));

                break;
            case ViewCustomer:
                Intent intent2=new Intent(CustomerManagement.this,ViewCustomer.class);
                startActivity(intent2);
                break;

        }


    }

//  @Override
public boolean onCreateOptionsMenu(Menu menu)
{

    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_printclas, menu);
    return super.onCreateOptionsMenu(menu);
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.imp_customer:
                Log.d(TAG, "onOptionsItemSelected: Import");
                return true;
            case R.id.exp_customer:
                Log.d(TAG, "onOptionsItemSelected:Export ");
                return true;


        }
        return super.onOptionsItemSelected(item);
    }
}

package com.inevitablesol.www.shopmanagement.LonginDetails;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.ItemModule.ViewItemDetail;
import com.inevitablesol.www.shopmanagement.LonginDetails.adapter.CustomList;
import com.inevitablesol.www.shopmanagement.MenuItemModule.MenuItemDetailsActivity;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.account.My_account;
import com.inevitablesol.www.shopmanagement.analysis.Analysis_Activity;
import com.inevitablesol.www.shopmanagement.analysis.Total_Analyasis;
import com.inevitablesol.www.shopmanagement.billing_module.MakeBill_selectCustomer;
import com.inevitablesol.www.shopmanagement.customer_module.AddCustomer;
import com.inevitablesol.www.shopmanagement.customer_module.UpdateCustomer;
import com.inevitablesol.www.shopmanagement.customer_module.ViewCustomer;
import com.inevitablesol.www.shopmanagement.expenses.Expenses_Activity;
import com.inevitablesol.www.shopmanagement.more.More_Activity;
import com.inevitablesol.www.shopmanagement.printerClasses.GlobalPool;
import com.inevitablesol.www.shopmanagement.purchase_module.Purchase_Home;
import com.inevitablesol.www.shopmanagement.billing_module.BillingHistory;
import com.inevitablesol.www.shopmanagement.customer_module.CustomerManagement;
import com.inevitablesol.www.shopmanagement.report.Report_Activity;
import com.inevitablesol.www.shopmanagement.sales.SalesActivity;
import com.inevitablesol.www.shopmanagement.service.Service_AddServices;
import com.inevitablesol.www.shopmanagement.service.Services_Activity;
import com.inevitablesol.www.shopmanagement.settings.SettingsActivity;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.inevitablesol.www.shopmanagement.wishList.WishList_Activity;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import static com.inevitablesol.www.shopmanagement.R.id.ViewCustomer;

public class BaseActivity extends AppCompatActivity
{


    protected FrameLayout frameLayout;


    protected ListView mDrawerList;
    protected String[] listArray = { "Home","Make a Bill", "Items", "Menu Items", "Purchase","Expenses" ,
            "Wish List" ,"Sales","Customers","Services","Reports","Analysis","Settings","My Account","More","Logout"};

    protected static int position;
    private static boolean isLaunch = true;

    public DrawerLayout mDrawerLayout;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    public Toolbar toolbar;
    private static final String TAG = "BaseActivity";
    private SqlDataBase sqlDataBase;

    private GlobalPool globalPool;
    ImageView imageView;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_base_layout);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        globalPool=(GlobalPool)this.getApplicationContext();
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        View header = (View) getLayoutInflater().inflate(R.layout.nav_header, null);
        username = (TextView) header.findViewById(R.id.tv_name);
         imageView=(ImageView)header.findViewById(R.id.profile);
         imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                startActivity(new Intent(BaseActivity.this, My_account.class));
             }
         });

        getSupportActionBar().hide();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         try
         {
         String u_name = sharedpreferences.getString("username", "");
        username.setText(globalPool.getUsername());
        mDrawerList.addHeaderView(header);

        sqlDataBase=new SqlDataBase(this);


             Ion.with(imageView)
                     .placeholder(R.drawable.app_icon)
                     .load(globalPool.getProfile_Pic());
             Log.d(TAG, "onCreate: Imge Path "+globalPool.getProfile_Pic());




            //Picasso.with(this).load(globalPool.getProfile_Pic()).fit().centerInside().into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Drawable drawable = BaseActivity.this.getResources().getDrawable(R.color.white);
//        drawer.setBackgroundDrawable(drawable);

        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Integer[] imageId =
                {
                        R.drawable.homebutton,
                        R.drawable.make_abill,
                        R.drawable.item,
                        R.drawable.menu,
                        R.drawable.purchase,
                        R.drawable.expenses,
                        R.drawable.wishlist,
                        R.drawable.sales,
                        R.drawable.side_customer,
                        R.drawable.service,
                        R.drawable.side_report,
                        R.drawable.analysis,
                        R.drawable.setting,
                        R.drawable.myaccount,
                        R.drawable.more,
                        R.drawable.logout
                };

        CustomList adapter = new CustomList(BaseActivity.this,listArray,imageId);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openActivity(i -1);
            }
        });





        if (isLaunch)
        {

            isLaunch = false;
            openActivity(0);
        }
    }

    /**
     * @param position Launching activity when any list item is clicked.
     */
    protected void openActivity(int position)
    {


//		mDrawerList.setItemChecked(position, true);
//		setTitle(listArray[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        BaseActivity.position = position;
        Log.d(TAG, "openActivity:"+position);
        //Setting currently selected position in this field so that it will be available in our child activities.

        switch (position)
        {
            case 0:
//                Toast.makeText(getApplicationContext(),"Under Working",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, BillingHistory.class));
                break;
            case 1:
//                Toast.makeText(getApplicationContext(),"Under Working",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MakeBill_selectCustomer.class));
                break;
            case 2:
                startActivity(new Intent(this, ViewItemDetail.class));
                break;

            case 3:
                  if(globalPool.isMenuItemStatus())
                  {
                      startActivity(new Intent(this, MenuItemDetailsActivity.class));
                  }else
                  {
                      Toast.makeText(this, "Please Enable From Setting", Toast.LENGTH_SHORT).show();
                  }
                break;
            case 4:
               // Toast.makeText(getApplicationContext(),"Under Working",Toast.LENGTH_LONG).show();
               startActivity(new Intent(this, Purchase_Home.class));
                break;

            case 5:
                // Toast.makeText(getApplicationContext(),"Under Working",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,Expenses_Activity.class));
                break;
            case 6:
               // Toast.makeText(getApplicationContext(),"Under Working",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,WishList_Activity.class));
                break;

            case 7:
                startActivity(new Intent(this, SalesActivity.class));

              //  startActivity(new Intent(this,MyAccountActivity.class));
                break;

            case 8:
                //Toast.makeText(getApplicationContext(),"Under Working",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, CustomerManagement.class));

                break;

            case 9:
              //  Toast.makeText(getApplicationContext(),"Under Working",Toast.LENGTH_LONG).show();
                  startActivity(new Intent(this, Services_Activity.class));
                Toast.makeText(getApplicationContext(),"Under Working",Toast.LENGTH_LONG).show();

                break;
            case 10:
                //  Toast.makeText(getApplicationContext(),"Under Working",Toast.LENGTH_LONG).show();
                 startActivity(new Intent(BaseActivity.this,Report_Activity.class));
                //Toast.makeText(getApplicationContext(),"Under Working",Toast.LENGTH_LONG).show();

                break;
            case 11:
                //  Toast.makeText(getApplicationContext(),"Under Working",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(BaseActivity.this,Analysis_Activity.class));
             //   Toast.makeText(getApplicationContext(),"Under Working",Toast.LENGTH_LONG).show();

                break;

            case 12:
             //   Toast.makeText(getApplicationContext(),"Under Working",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,SettingsActivity.class));
                break;


            case 13:
             //   Toast.makeText(getApplicationContext(),"Under Working",Toast.LENGTH_LONG).show();

                  startActivity(new Intent(this,My_account.class));
                break;
            case 14:
                startActivity(new Intent(BaseActivity.this,More_Activity.class));
                Toast.makeText(this, "Work in progress", Toast.LENGTH_SHORT).show();
                break;
            case 15:
                confirmDialog();
                break;
            default:
                break;
        }

//        Toast.makeText(this, "Selected Item Position::"+position, Toast.LENGTH_LONG).show();
    }


    /* We can override onBackPressed method to toggle navigation drawer*/
    @Override
    public void onBackPressed()
    {
        Log.d(TAG, "onBackPressed: BaseClass");
        if(mDrawerLayout.isDrawerOpen(mDrawerList)){

            mDrawerLayout.closeDrawer(mDrawerList);
        }else
        {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mDrawerLayout.openDrawer(mDrawerList);
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_printclas, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void confirmDialog()
    {
        // custom dialog
         final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_dialog);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Do you want to logout?");

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                editor = sharedpreferences.edit().clear();
                sqlDataBase.deleteDataBase();
                editor.commit(); // commit changes
                Intent intent = new Intent(BaseActivity.this, SigninActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.dismiss();


            }
        });

        Button cancelDialog = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        dialog.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Base Activity");
        username.setText(globalPool.getUsername());
    }



}

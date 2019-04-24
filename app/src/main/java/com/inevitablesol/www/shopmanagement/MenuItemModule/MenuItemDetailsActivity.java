package com.inevitablesol.www.shopmanagement.MenuItemModule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.R;

public class MenuItemDetailsActivity extends BaseActivity implements View.OnClickListener {

    ImageView addItem,editItem,viewItem,removeItem,historyItem;
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs";
    private String dbname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_menu_item_details, frameLayout);
        mDrawerList.setItemChecked(position, true);

        String logCheck =listArray[position];
        if (logCheck.equalsIgnoreCase("Logout"))
        {
//            setTitle(listArray[0]);
            toolbar.setTitle(listArray[0]);
        }else{

//            setTitle(listArray[position]);
            toolbar.setTitle(listArray[position]);
        }

        addItem = (ImageView) findViewById(R.id.addItem);
        editItem = (ImageView) findViewById(R.id.editItem);
        viewItem = (ImageView) findViewById(R.id.viewItem);
        removeItem = (ImageView) findViewById(R.id.removeItem);
        historyItem = (ImageView) findViewById(R.id.historyItem);
        historyItem.setOnClickListener(this);

        addItem.setOnClickListener(this);
        viewItem.setOnClickListener(this);
        removeItem.setOnClickListener(this);
        editItem.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId)
        {
            case R.id.addItem:
                Intent addIntent = new Intent(this,AddMenuItem.class);
                startActivity(addIntent);
                break;
            case R.id.editItem:
                Intent editIntent = new Intent(this,ViewMenuActivity.class);
                editIntent.putExtra("viewtype","edit");
                startActivity(editIntent);
                break;
            case R.id.viewItem:
                Intent viewIntent = new Intent(this,ViewMenuActivity.class);
                viewIntent.putExtra("viewtype","view");
                startActivity(viewIntent);
                break;
            case R.id.historyItem:
                Intent removeIntent = new Intent(this, MenuHistoryItem.class);
                startActivity(removeIntent);
                break;
            case R.id.removeItem:
                Intent remove = new Intent(this, ViewMenuActivity.class);
                remove.putExtra("viewtype","remove");
                startActivity(remove);
                break;
          //  case R.id.historyItem:
             //   break;
        }
    }
}

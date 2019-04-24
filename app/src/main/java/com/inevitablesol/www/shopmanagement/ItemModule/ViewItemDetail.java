package com.inevitablesol.www.shopmanagement.ItemModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.R;

public class ViewItemDetail extends BaseActivity implements View.OnClickListener {

    ImageView addItem, editItem, viewItem, removeItem, historyItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.item_menu, frameLayout);
        mDrawerList.setItemChecked(position, true);
        addItem = (ImageView) findViewById(R.id.addItem);
        editItem = (ImageView) findViewById(R.id.editItem);
        viewItem = (ImageView) findViewById(R.id.viewItem);
        removeItem = (ImageView) findViewById(R.id.removeItem);
        historyItem = (ImageView) findViewById(R.id.historyItem);
        addItem.setOnClickListener(this);
        editItem.setOnClickListener(this);
        viewItem.setOnClickListener(this);
        removeItem.setOnClickListener(this);
        historyItem.setOnClickListener(this);
        mDrawerList.setItemChecked(position, true);
        String logCheck = listArray[position];
        if (logCheck.equalsIgnoreCase("Logout")) {
//            setTitle(listArray[0]);
            toolbar.setTitle(listArray[0]);
        } else {

//            setTitle(listArray[position]);
            toolbar.setTitle(listArray[position]);
        }


        //setContentView(R.layout.item,frameLayout);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.addItem:
                Intent intent = new Intent(ViewItemDetail.this, Add_Item.class);
                intent.putExtra("viewtype", "addItem");
                startActivity(intent);
//                Toast.makeText(getApplication(),"Add Item",Toast.LENGTH_LONG).show();

                break;
            case R.id.editItem:
                Intent editIntent = new Intent(ViewItemDetail.this, Edit_Items.class);
                editIntent.putExtra("viewtype","editItem");
                startActivity(editIntent);
//                Toast.makeText(getApplication(),"edit Item",Toast.LENGTH_LONG).show();
                break;
            case R.id.viewItem:
                Intent viewIntent = new Intent(ViewItemDetail.this, View_Items.class);
                viewIntent.putExtra("viewtype", "viewItem");
                startActivity(viewIntent);
                // Toast.makeText(getApplication(),"view Item",Toast.LENGTH_LONG).show();
                break;
            case R.id.removeItem:
                Intent removeIntent = new Intent(ViewItemDetail.this, Remove_Items.class);
             //   Intent removeIntent = new Intent(ViewItemDetail.this, ViewItems.class);
                removeIntent.putExtra("viewtype", "removeItem");
                startActivity(removeIntent);
                break;
            case R.id.historyItem:
                Intent history_intent = new Intent(ViewItemDetail.this,HistoryActivity.class);
                startActivity(history_intent);
                break;


        }


    }
}

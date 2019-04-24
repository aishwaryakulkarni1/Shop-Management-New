package com.inevitablesol.www.shopmanagement.ItemModule.Parser;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.inevitablesol.www.shopmanagement.ItemModule.EditItemActivity;
import com.inevitablesol.www.shopmanagement.R;


/**
 * Created by Pritam on 20-07-2017.
 */

public class TextWatcherClass implements TextWatcher
{
    private static final String TAG = "TextWatcherClass";
    View view;
    EditItemActivity editItemActivity;

    public  TextWatcherClass(View view)
    {
        this.view=view;
    }

    public TextWatcherClass(View view, EditItemActivity context)
    {
        this.view=view;
        this.editItemActivity=context;
    }




    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        String text = s.toString();
        switch(view.getId())
        {
            case R.id.input_Discount:
                Log.d(TAG, "afterTextChanged: ");
                editItemActivity.calculateTotalPrice();
                break;
            case R.id.input_totalitemPrice:
                Log.d(TAG, "afterTextChanged: ");
                editItemActivity.calculateTotalDiscount();
                break;
        }

    }
}

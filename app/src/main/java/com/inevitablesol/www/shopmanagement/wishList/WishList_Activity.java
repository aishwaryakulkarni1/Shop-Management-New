package com.inevitablesol.www.shopmanagement.wishList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.R;

public class WishList_Activity extends BaseActivity implements View.OnClickListener {

    private ImageView Add_WhishList;

    private ImageView view_whishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         getLayoutInflater().inflate(R.layout.activity_whish_list, frameLayout);
        mDrawerList.setItemChecked(position, true);
          Add_WhishList=(ImageView)findViewById(R.id.addWishList);
        Add_WhishList.setOnClickListener(this);
        view_whishList=(ImageView)findViewById(R.id.ViewWishList);
        view_whishList.setOnClickListener(this);
        String logCheck =listArray[position];
        if (logCheck.equalsIgnoreCase("Logout"))
        {
            toolbar.setTitle(listArray[0]);
        }else
        {
            toolbar.setTitle(listArray[position]);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.addWishList:
                startActivity(new Intent(this,Add_WishList.class));
                break;
            case R.id.ViewWishList:
                startActivity(new Intent(this,View_WishList.class));
                break;
            default:
                Toast.makeText(this, "wroung Choice", Toast.LENGTH_SHORT).show();
                break;

        }

    }
}

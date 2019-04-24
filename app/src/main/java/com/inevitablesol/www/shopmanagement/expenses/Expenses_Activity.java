package com.inevitablesol.www.shopmanagement.expenses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.LonginDetails.BaseActivity;
import com.inevitablesol.www.shopmanagement.R;

public class Expenses_Activity extends BaseActivity implements View.OnClickListener {
    private ImageView add_expenses,view_expenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.expensive_home, frameLayout);
       // setContentView(R.layout.expensive_home);
        mDrawerList.setItemChecked(position, true);
        add_expenses=(ImageView)findViewById(R.id.addExpensive);
        add_expenses.setOnClickListener(this);
        view_expenses=(ImageView)findViewById(R.id.viewExpensive);
        view_expenses.setOnClickListener(this);
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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.addExpensive:
                startActivity(new Intent(this,Add_expenses.class));
                break;
            case R.id.viewExpensive:
                   startActivity(new Intent(this,View_expense.class));
                break;
            default:
                Toast.makeText(this, "wroung Choice", Toast.LENGTH_SHORT).show();
                break;

        }

    }
}

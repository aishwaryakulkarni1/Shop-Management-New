package com.inevitablesol.www.shopmanagement.product_info;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.inevitablesol.www.shopmanagement.R;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView add_product;
    ImageView view_product;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        add_product=(ImageView)findViewById(R.id.add_new_productType);
        view_product=(ImageView)findViewById(R.id.viewProductType);
        add_product.setOnClickListener(this);
        view_product.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.add_new_productType:
                startActivity(new Intent(ProductActivity.this,Add_New_product.class));
                break;
            case R.id.viewProductType:
                startActivity(new Intent(ProductActivity.this, ProductViewDetails.class));
                break;

        }

    }
}

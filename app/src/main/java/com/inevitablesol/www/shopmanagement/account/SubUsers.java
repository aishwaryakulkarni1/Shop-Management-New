package com.inevitablesol.www.shopmanagement.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.inevitablesol.www.shopmanagement.R;

public class SubUsers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_users);

        ImageView imageView=(ImageView)findViewById(R.id.addUser);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SubUsers.this,Add_User.class));
            }
        });

        ImageView  edit_subUser=(ImageView)findViewById(R.id.editSub_user);
        edit_subUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(SubUsers.this,Edit_User.class));
            }
        });

        ImageView   viewUser=(ImageView)findViewById(R.id.view_subUser);
        viewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SubUsers.this,ViewSubUser.class));
            }
        });
    }
}

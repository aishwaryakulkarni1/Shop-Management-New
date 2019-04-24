package com.inevitablesol.www.shopmanagement.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.inevitablesol.www.shopmanagement.R;

public class Role_management extends AppCompatActivity
{

    private ImageView addUser;
    private static final String TAG = "Role_management";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_management);

        ImageView imageView=(ImageView)findViewById(R.id.addUser);
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Role_management.this,Add_User.class));
            }
        });

        ImageView  edit_subUser=(ImageView)findViewById(R.id.editSub_user);
        edit_subUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(Role_management.this,Edit_User.class));
            }
        });

        ImageView   viewUser=(ImageView)findViewById(R.id.view_subUser);
        viewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Role_management.this,ViewSubUser.class));
            }
        });
        Log.d(TAG, "onCreate: ");
        
        
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

}

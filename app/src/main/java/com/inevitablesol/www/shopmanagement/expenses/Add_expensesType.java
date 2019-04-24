package com.inevitablesol.www.shopmanagement.expenses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.WebApi.WebApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Add_expensesType extends AppCompatActivity implements View.OnClickListener {
    AppCompatButton add_type;
    EditText et_expensesType;


    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String dbname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses_type);
        et_expensesType=(EditText)findViewById(R.id.new_expType);
        add_type=(AppCompatButton)findViewById(R.id.bt_addexpType);
        add_type.setOnClickListener(this);
        et_expensesType.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.bt_addexpType:
                String type=et_expensesType.getText().toString().trim();
                if(type.length()>0 && !type.isEmpty())
                {
                    saveExpensesType(type);

                }else
                {
                    Toast.makeText(this, "Please Add Type", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void saveExpensesType(final String type)
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.ADDNEW_EXPENSESTYPE, new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try {
                    Log.d("response",response);
                    JSONObject msg = new JSONObject(response);
                    String message = msg.getString("message");
                    if (message.equalsIgnoreCase("category Added"))
                    {
                        Toast.makeText(Add_expensesType.this, "Expense Added Succesfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.putExtra("MESSAGE","added");
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                    }

                }catch (JSONException e)
                {

                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(Add_expensesType.this, "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("dbname", dbname);
                Log.d("cat",type);

                params.put("dbname", dbname);
                params.put("cat", type);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    }


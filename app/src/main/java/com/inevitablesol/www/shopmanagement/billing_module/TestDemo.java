package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import com.inevitablesol.www.shopmanagement.customer_module.CustomerParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDemo extends AppCompatActivity {

    private SimpleAdapter newAdapter;
    private static final String TAG = "TestDemo";
    private SearchView searchViewCustomer;
    private String custSelected;


    private static final String MyPREFERENCES = "MyPrefs";
    private ListView listView;
    private EditText searchCustomer;
    //private SqlDataBase sqlDataBase;
    private SharedPreferences sharedpreferences;
    private String dbname;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cust_for_bill);
        searchViewCustomer=(SearchView)findViewById(R.id.simpleSearchView);
        searchViewCustomer.setIconifiedByDefault(false);
        searchViewCustomer.setQueryHint("Search Customer");
        searchViewCustomer.setIconified(true);
        listView=(ListView)findViewById(R.id.list_customerInfo);
        View closeButton = searchViewCustomer.findViewById(android.support.v7.appcompat.R.id.search_close_btn);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                EditText et = (EditText) findViewById(R.id.search_src_text);

                //Clear the text from EditText view
                et.setText("");
                //Clear query
                searchViewCustomer.setQuery("", false);
                //Collapse the action view
//                searchViewCustomer.onActionViewCollapsed();
                getAllCustomerDetails();

            }
        });
        searchViewCustomer.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Log.d(TAG, "onQueryTextSubmit: "+query);
                if(!query.isEmpty() && query.length()>0)
                {
                    TestDemo.this.newAdapter.getFilter().filter(query);
                    listView.setVisibility(View.VISIBLE);

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if(!newText.isEmpty() && newText.length()>0)
                {
                    TestDemo.this.newAdapter.getFilter().filter(newText);
                    listView.setVisibility(View.VISIBLE);

                }
                return true;
            }

        });

        getAllCustomerDetails();

    }



    private void getAllCustomerDetails()
    {

        final ProgressDialog loading = ProgressDialog.show(this,"Loading....","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.GETCUSTINFO, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {

                    loading.dismiss();
                    String resp = response.toString().trim();
                    Log.d(TAG, "onResponse:CustDetails "+resp);
                    JSONObject jsonObject = new JSONObject(resp);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("Data not available"))
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else
                    {
                        final CustomerParser customerParser=new CustomerParser(resp);
                        customerParser.custDetails();
                        //=customerParser.makeArray();

                        final List<Map<String, String>> cust = new ArrayList<Map<String, String>>();
                        Map<String, String> map;
                        int counter = CustomerParser.cust_Name.length;
                        for (int i = 0; i < counter; i++)
                        {
                            map = new HashMap<>();
                            map.put("name", CustomerParser.cust_Name[i]);
                            map.put("mobile", CustomerParser.mobile[i]);
                            cust.add(map);
                            Log.d(TAG, "onResponse: "+cust);
                        }

                        newAdapter = new SimpleAdapter(TestDemo.this, cust, R.layout.list_customerinfo, new String[]{"name", "mobile"}, new int[]{R.id.customer_name, R.id.customer_mobile});
                        listView.setAdapter(newAdapter);

//                        searchViewCustomer.addTextChangedListener(new TextWatcher()
//                        {
//                            @Override
//                            public void beforeTextChanged(CharSequence s, int start, int count, int after)
//                            {
//                                listView.setVisibility(View.INVISIBLE);
//                            }
//
//                            @Override
//                            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
//                            {
//                                listView.setVisibility(View.VISIBLE);
//                            }
//
//                            @Override
//                            public void afterTextChanged(Editable s)
//                            {
//                                TestDemo.this.newAdapter.getFilter().filter(s);
//                                listView.setVisibility(View.VISIBLE);
//                            }
//                        });


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Map custSel = (Map) newAdapter.getItem(position);
                                custSelected = (String) custSel.get("name") + " " + custSel.get("mobile");
                                searchViewCustomer.setQuery(custSelected,false);

                                listView.setVisibility(View.GONE);
                              //  getCustomerInfo((String)custSel.get("name"));

                            }
                        });


//


                    }
                }catch(Exception e)
                {
                    Log.d("Exception", "" + e);
                }



            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();

                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplication(), "Please connect to the internet before continuing", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("dbname", dbname);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}

package com.inevitablesol.www.shopmanagement.purchase_module;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

/**
 * Created by Pritam on 07-07-2017.
 */

public class ItemAdapter extends ArrayAdapter<String>
{

    private final Activity context;
    private final String[] i_id;
    private final String [] i_name;
    public ItemAdapter(Activity context, int resource, String[] name, String[] id) {
        super(context, R.layout.product_list, R.id.txt_productId, id);
        this.context = context;
        this.i_id = id;
        this.i_name = name;



    }

    public ItemAdapter(Context context, int product_list, String[] iName, String[] iId)
    {
        super(context, R.layout.product_list, R.id.txt_productId, iId);
        this.context = (Activity) context;
        this.i_id = iId;
        this.i_name = iName;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        //  LayoutInflater inflater = context.getLayoutInflater();


        return getCustomView(position, view, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent)
    {
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView= inflater.inflate(R.layout.product_list, null, true);
        TextView txtname = (TextView) rowView.findViewById(R.id.txt_productName);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt_productId);


        txtname.setText(i_name[position]);
        // txtTitle.setText(pid[position]);

        return rowView;
    }
}

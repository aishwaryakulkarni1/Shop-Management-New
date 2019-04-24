package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

/**
 * Created by Pritam on 10-08-2017.
 */

public class SuppierAdapter  extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] pid;
    private final String[] state;

    public SuppierAdapter (Activity context, int resource, String[] name, String[] id)
    {
        super(context, R.layout.supplier_list, R.id.txt_stateName, id);
        this.context = context;
        this.pid = id;
        this.state = name;


    }

    public SuppierAdapter (Context context, int product_list, String[] vName, String[] vId)
    {
        super(context, R.layout.supplier_list, R.id.txt_Id, vId);
        this.context = (Activity) context;
        this.pid = vId;
        this.state = vName;
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
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.supplier_list, null, true);
        TextView txtname = (TextView) rowView.findViewById(R.id.txt_stateName);
        TextView txtID = (TextView) rowView.findViewById(R.id.txt_Id);
        txtname.setText(state[position]);
        // txtTitle.setText(pid[position]);

        return rowView;
    }

    @Override
    public int getCount()
    {
        int count = super.getCount();
        return count > 0 ? count - 1 : count;

    }
}
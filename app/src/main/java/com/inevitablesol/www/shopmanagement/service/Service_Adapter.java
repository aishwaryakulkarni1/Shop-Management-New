package com.inevitablesol.www.shopmanagement.service;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

/**
 * Created by Pritam on 15-06-2017.
 */

public class Service_Adapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] sid;
    private final String[] sname;


    public Service_Adapter( Activity context, int resource, String[] id, String[] name) {
        super(context, R.layout.service_list, R.id.txt_serviceName, id);
        this.context = context;
        this.sid = id;
        this.sname = name;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        //  LayoutInflater inflater = context.getLayoutInflater();

        return getCustomView(position, view, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.service_list, null, true);
        TextView id = (TextView) rowView.findViewById(R.id.txt_serviceId);
        TextView txt_Name = (TextView) rowView.findViewById(R.id.txt_serviceName);



        id.setText(sid[position]);
        txt_Name.setText(sname[position]);


        return rowView;
    }

}
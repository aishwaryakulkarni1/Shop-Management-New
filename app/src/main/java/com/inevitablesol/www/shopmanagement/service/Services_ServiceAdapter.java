package com.inevitablesol.www.shopmanagement.service;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;


import java.util.ArrayList;

/**
 * Created by Pritam on 13-06-2017.
 */

public class Services_ServiceAdapter extends RecyclerView.Adapter<Services_ServiceAdapter .ViewHolder>
{

    private ArrayList<Services> listData;
     private  Service_ViewServices service_viewServices;
    Service_EditService service_editService;

    public Services_ServiceAdapter(ArrayList<Services> services, Service_ViewServices service_viewServices)
    {
        this.listData=services;
         this.service_viewServices=service_viewServices;
    }

    public Services_ServiceAdapter(ArrayList<Services> services, Service_EditService service_editService)
    {
        this.listData=services;
        this.service_editService=service_editService;
    }


    @Override
    public Services_ServiceAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_vendor, viewGroup, false);
        return new Services_ServiceAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        final Services list = listData.get(position);
        viewHolder.tv_name.setText(String.valueOf(list.getService_name()));
        viewHolder.tv_mobile.setText(String.valueOf(list.getService_duration()));


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_mobile;//tv_product_type;

        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tv_custname);

            tv_mobile = (TextView) view.findViewById(R.id.tv_mobile);

        }
    }
}

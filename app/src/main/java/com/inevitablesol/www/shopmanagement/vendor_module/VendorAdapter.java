package com.inevitablesol.www.shopmanagement.vendor_module;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.purchase_module.PurchaseView;

import java.util.ArrayList;

/**
 * Created by Pritam on 25-05-2017.
 */

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.ViewHolder> {


        private ArrayList<Record> listData;
        private EditVendorActivity editVendorActivity;
        private  View_Vendor viewVendor;
        private  PurchaseView purchaseView;



        public VendorAdapter(ArrayList<Record> listData, View_Vendor viewVendor)
        {

            this.listData = listData;
            this.viewVendor = viewVendor;
        }

    public VendorAdapter(ArrayList<Record> vendorInfo, EditVendorActivity editVendorActivity)
    {
        this.listData = vendorInfo;
        this.editVendorActivity=editVendorActivity;
    }

    public VendorAdapter(ArrayList<Record> vendorLists, PurchaseView purchaseView)
    {
        this.listData=vendorLists;
        this.purchaseView=purchaseView;

    }


    @Override
        public VendorAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_vendor, viewGroup, false);
            return new VendorAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(VendorAdapter.ViewHolder viewHolder, int i)
        {

            final Record list = listData.get(i);
            viewHolder.tv_name.setText(String.valueOf(list.getCompany()));
            viewHolder.tv_mobile.setText(String.valueOf(list.getMobileNo()));

        }

        @Override
        public int getItemCount()
        {
            return listData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView tv_name,tv_mobile;//tv_product_type;
            public ViewHolder(View view) {
                super(view);

                tv_name = (TextView) view.findViewById(R.id.tv_custname);
                tv_mobile = (TextView) view.findViewById(R.id.tv_mobile);

            }
        }

      public  void  clearView()
      {
          int size=listData.size();

          if(size>0)
          {
              for(int i=0;i<listData.size();i++)
              {
                   this.listData.remove(i);
              }
              this.notifyItemRangeRemoved(0, size);
          }
      }
}

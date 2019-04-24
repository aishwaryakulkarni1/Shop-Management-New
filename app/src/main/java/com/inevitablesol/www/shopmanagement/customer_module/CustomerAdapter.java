package com.inevitablesol.www.shopmanagement.customer_module;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.inevitablesol.www.shopmanagement.R;

import java.util.ArrayList;

/**
 * Created by Pritam on 25-05-2017.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> implements Filterable

{
    private ArrayList<CustomerInfo> listData;

    private ArrayList<CustomerInfo> filteredData;

    private ViewCustomer view_customer;
    ViewCustomer  customerEditView;
    ViewCustomer viewCustomer;


   private ItemFilter mFilter ;
    private   UpdateCustomer updateCustomer;



    public CustomerAdapter(ArrayList<CustomerInfo> listData, ViewCustomer customerEditView)
    {

        this.listData = listData;
        this.customerEditView = customerEditView;

    }

    public CustomerAdapter(ArrayList<CustomerInfo> customerInfos, UpdateCustomer updateCustomer)
    {
       this.listData=customerInfos;
        this.updateCustomer=updateCustomer;

    }

//    public CustomerAdapter(ArrayList<CustomerInfo> vendorInfo, EditVendorActivity editVendorActivity)
//    {
//        this.listData = vendorInfo;
//        this.editVendorActivity=editVendorActivity;
//    }
//
//    public CustomerAdapter(ArrayList<CustomerInfo> customerInfos, ViewCustomer viewCustomer)
//    {
//        this.listData = customerInfos;
    
//        this.viewCustomer=viewCustomer;
//
//    }
//
//    public CustomerAdapter(ArrayList<CustomerInfo> custDetails, Sales_CustomerActivity sales_customerActivity)
//    {
//        this.listData=custDetails;
//        this.sales_customerActivity=sales_customerActivity;
//    }
//
//
//    public CustomerAdapter(ArrayList<CustomerInfo> customerInfos, Services_Customer_Edit services_customer_edit)
//    {
//        this.listData=customerInfos;
//        this.services_customer_edit=services_customer_edit;
//
//    }

    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_vendor, viewGroup, false);
        return new CustomerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerAdapter.ViewHolder viewHolder, int i)
    {

        final CustomerInfo list = listData.get(i);
        viewHolder.tv_name.setText(String.valueOf(list.getCustomer_name()));
        viewHolder.tv_mobile.setText(String.valueOf(list.getMobile_numbe()));

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public Filter getFilter()
    {
        if(mFilter == null)
            mFilter = new ItemFilter(this, listData);
        return mFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_mobile;//tv_product_type;
        public ViewHolder(View view)
        {
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

    private class ItemFilter extends Filter
    {
         private  CustomerAdapter itemFilter;
         private  ArrayList<CustomerInfo> originalList;
        private  ArrayList<CustomerInfo> nlist;
        public ItemFilter(CustomerAdapter customerAdapter, ArrayList<CustomerInfo> listData)
        {
            super();
            this.itemFilter=customerAdapter;
             this.originalList=listData;
            this.nlist= new ArrayList<CustomerInfo>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();


            int count = originalList.size();

            for (final CustomerInfo user :originalList ) {
                if (user.getCustomer_name().contains(filterString)) {
                    nlist.add(user);
                }
            }



            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {

                     itemFilter.filteredData.addAll((ArrayList<CustomerInfo>) results.values);
                      itemFilter.notifyDataSetChanged();
                     Log.d("test",""+filteredData.get(0).getCustomer_name());
        }

    }

}

package com.inevitablesol.www.shopmanagement.MenuItemModule;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.ItemModule.AddToItems;
import com.inevitablesol.www.shopmanagement.ItemModule.StockInfo;
import com.inevitablesol.www.shopmanagement.ItemModule.ViewItems;
import com.inevitablesol.www.shopmanagement.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pritam on 17-11-2017.
 */

public class Menu_StockAdapter   extends RecyclerView.Adapter<Menu_StockAdapter.ViewHolder> implements Filterable
{



    private AddToItems addToItems;
    private ArrayList<StockInfo> listData;
    private ViewItems stockViewActivity;
    private ViewMenuActivity viewMenuActivity;
    private  MenuHistoryItem menuHistoryItem;

    private List<StockInfo> contactListFiltered =new ArrayList<StockInfo>();

    private static final String TAG = "Menu_StockAdapter";
    public Menu_StockAdapter(ArrayList<StockInfo> listData, ViewItems stockViewActivity)
    {

        this.listData = listData;
        this.stockViewActivity = stockViewActivity;
    }


    public Menu_StockAdapter(ArrayList<StockInfo> listData, ViewMenuActivity viewMenuActivity) {

        this.listData = listData;
        this.viewMenuActivity= viewMenuActivity;
    }

    public Menu_StockAdapter(ArrayList<StockInfo> filteredList, MenuHistoryItem menuHistoryItem)
    {
        this.listData=filteredList;
        this.menuHistoryItem=menuHistoryItem;
    }



    @Override
    public Menu_StockAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_menu, viewGroup, false);
        return new Menu_StockAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Menu_StockAdapter.ViewHolder viewHolder, int i) {

        final StockInfo list = listData.get(i);
        viewHolder.tv_itemname.setText(String.valueOf(list.getItem_name()));
        viewHolder.tv_stockprice.setText(String.valueOf(list.getTotalPrice()));
      //  viewHolder.tv_storage_qty.setText(String.valueOf(list.getStorage_qty()));
        // viewHolder.tv_mrp.setText(String.valueOf(list.getMrp()));
        //viewHolder.tv_product_type.setText(String.valueOf(list.getProduct_type()));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_itemname,tv_stockprice,tv_mrp,tv_product_type,tv_storage_qty;
        public ViewHolder(View view) {
            super(view);

            tv_itemname = (TextView) view.findViewById(R.id.tv_itemname);
            tv_stockprice = (TextView) view.findViewById(R.id.tv_price);

//            tv_mrp = (TextView) view.findViewById(R.id.tv_mrp);
//            tv_product_type = (TextView) view.findViewById(R.id.tv_product_type);
           // tv_storage_qty = (TextView) view.findViewById(R.id.tv_storage_qty);

        }
    }

    public  void  clearView()
    {
        int size=this.listData.size();

        if(size>0)
        {
            for(int i=0;i<size;i++)
            {
                this.listData.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

    // Do Search...

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                Log.d(TAG, "performFiltering: Constarint"+constraint);
                if (charString.isEmpty())
                {
                    contactListFiltered = listData;
                } else
                {
                    List<StockInfo> filteredList = new ArrayList<>();
                    for (StockInfo item : listData)
                    {
                        Log.d(TAG, "performFilter: "+item);
                        Log.d(TAG, "performFiltering: Constarint"+constraint);

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (item.getItem_name().toLowerCase().startsWith(charString.toLowerCase()))
                        {
                            // Adding Matched items
                            filteredList.add(item);
                        }
//                        if (item.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
//                            filteredList.add(item);
//                        }
                    }

                    contactListFiltered = filteredList;
                }

                Log.d(TAG, "performFiltering: "+contactListFiltered);

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                Log.d(TAG, "publishResults: Value"+ filterResults.values);
                Log.d(TAG, "publishResults: Value"+ contactListFiltered);
                // contactListFiltered.clear();
                Log.d(TAG, "publishResults: "+contactListFiltered);
                //  contactListFiltered.addAll((ArrayList<StockInfo>) filterResults.values);
                //  contactListFiltered = (;
                listData.clear();
                Log.d(TAG, "publishResults: List"+listData);
                listData.addAll(contactListFiltered);
                Log.d(TAG, "publishResults: "+contactListFiltered);
                Log.d(TAG, "publishResults: "+listData);
                notifyDataSetChanged();
            }
        };
    }


}

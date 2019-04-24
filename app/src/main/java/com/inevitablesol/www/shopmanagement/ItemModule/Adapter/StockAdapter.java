package com.inevitablesol.www.shopmanagement.ItemModule.Adapter;

/**
 * Created by Anup on 22-02-2017.
 */

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
import com.inevitablesol.www.shopmanagement.ItemModule.Add_Item;
import com.inevitablesol.www.shopmanagement.ItemModule.Edit_Items;
import com.inevitablesol.www.shopmanagement.ItemModule.HistoryActivity;
import com.inevitablesol.www.shopmanagement.ItemModule.Remove_Items;
import com.inevitablesol.www.shopmanagement.ItemModule.View_Items;
import com.inevitablesol.www.shopmanagement.MenuItemModule.MenuHistoryItem;
import com.inevitablesol.www.shopmanagement.MenuItemModule.ViewMenuActivity;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.ItemModule.StockInfo;
import com.inevitablesol.www.shopmanagement.ItemModule.ViewItems;

import com.inevitablesol.www.shopmanagement.product_info.ProductViewDetails;
import com.inevitablesol.www.shopmanagement.purchase_module.Purchase_Stock_Storage;
import com.inevitablesol.www.shopmanagement.sql_lite.ItemDetalisClass;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder>  implements Filterable {



    private AddToItems addToItems;
    private ArrayList<StockInfo> listData=new ArrayList<StockInfo>();
    private  ViewItems stockViewActivity;
    private ViewMenuActivity viewMenuActivity;
    private  Add_Item add_item;
    private  Edit_Items edit_items;
    private View_Items view_items;

    private  Remove_Items remove_items;
    private HistoryActivity historyActivity;

    private  MenuHistoryItem menuHistoryItem;

    private  Purchase_Stock_Storage purchase_stock_storage;
    private  ProductViewDetails productViewDetails;

    private static final String TAG = "StockAdapter";
    private List<StockInfo> contactListFiltered =new ArrayList<StockInfo>();

    public StockAdapter(ArrayList<StockInfo> listData, ViewItems stockViewActivity) {

        this.listData = listData;
        this.stockViewActivity = stockViewActivity;
    }

    public StockAdapter(ArrayList<StockInfo> listData, AddToItems addToItems) {

        this.listData = listData;
        this.addToItems = addToItems;
    }

    public StockAdapter(ArrayList<StockInfo> listData, ViewMenuActivity viewMenuActivity) {

        this.listData = listData;
        this.viewMenuActivity= viewMenuActivity;
    }

    public StockAdapter(ArrayList<StockInfo> stockInfoArrayList, Add_Item add_item)
    {
        this.listData = stockInfoArrayList;
        this.add_item=add_item;
    }

    public StockAdapter(ArrayList<StockInfo> stockInfoArrayList, Edit_Items edit_items)
    {
        this.listData=stockInfoArrayList;
        this.edit_items=edit_items;
    }

    public StockAdapter(ArrayList<StockInfo> stockInfoArrayList, Remove_Items remove_items)
    {
        this.listData=stockInfoArrayList;
        this.remove_items=remove_items;
    }

    public StockAdapter(ArrayList<StockInfo> stockInfoArrayList, View_Items view_items)
    {
        this.listData=stockInfoArrayList;
        this.contactListFiltered=stockInfoArrayList;
        this.view_items=view_items;
    }

    public StockAdapter(ArrayList<StockInfo> stockInfoArrayList, HistoryActivity historyActivity)
    {
        this.listData=stockInfoArrayList;
        this.historyActivity=historyActivity;


    }

    public StockAdapter(ArrayList<StockInfo> stockInfoArrayList, MenuHistoryItem menuHistoryItem)
    {
        this.listData=stockInfoArrayList;
        this.menuHistoryItem=menuHistoryItem;

    }

    public StockAdapter(ArrayList<StockInfo> stockInfoArrayList, Purchase_Stock_Storage purchase_stock_storage)
    {

        this.listData=stockInfoArrayList;
        this.purchase_stock_storage=purchase_stock_storage;

    }

    public StockAdapter(ArrayList<StockInfo> filteredList, ProductViewDetails productViewDetails)
    {
        this.listData=filteredList;
        this.productViewDetails=productViewDetails;
    }




    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_rowlayout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StockAdapter.ViewHolder viewHolder, int i) 
    {
        Log.d(TAG, "onBindViewHolder: ");

        final StockInfo list = listData.get(i);
        viewHolder.tv_itemname.setText(String.valueOf(list.getItem_name()));
        viewHolder.tv_stockqty.setText(String.valueOf(list.getStock_qty()));
        viewHolder.tv_storage_qty.setText(String.valueOf(list.getStorage_qty()));
       // viewHolder.tv_mrp.setText(String.valueOf(list.getMrp()));
        //viewHolder.tv_product_type.setText(String.valueOf(list.getProduct_type()));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_itemname,tv_stockqty,tv_mrp,tv_product_type,tv_storage_qty;
        public ViewHolder(View view) {
            super(view);

            tv_itemname = (TextView) view.findViewById(R.id.tv_itemname);
            tv_stockqty = (TextView) view.findViewById(R.id.tv_stockqty);
//            tv_mrp = (TextView) view.findViewById(R.id.tv_mrp);
//            tv_product_type = (TextView) view.findViewById(R.id.tv_product_type);
            tv_storage_qty = (TextView) view.findViewById(R.id.tv_storage_qty);

        }
    }

    public  void  clearView()
    {
        try {
            int size=this.listData.size();

            if(size>0)
            {
                for(int i=0;i<size;i++)
                {
                    this.listData.remove(0);
                }
                this.notifyItemRangeRemoved(0, size);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


         // Do Search...
    public void filter(final String text)
    {
        Log.d(TAG, "run:List Data "+listData.size());
      //  Log.d(TAG, "run:contactListFiltered Data "+contactListFiltered.size());

        // Searching could be complex..so we will dispatch it to a different thread...
//        new Thread(new Runnable() {
//            @Override
//            public void run() {

                // Clear the filter list
               contactListFiltered.addAll(listData);
                listData.clear();
             Log.d(TAG, "run:List Data "+listData.size());


                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text))
                {

                    listData.addAll(listData);

                } else
                {
                    Log.d(TAG, "filter: "+contactListFiltered);
                    // Iterate in the original List and add it to filter list...
                    for (StockInfo item : contactListFiltered)
                    {
                        Log.d(TAG, "filter: "+contactListFiltered);

                        if (item.getItem_name().toLowerCase().startsWith(text.toLowerCase())) {
                            // Adding Matched items
                            listData.add(item);
                        }
                    }
                    Log.d(TAG, "run:List Data "+listData);
                }


                notifyDataSetChanged();
                // Set on UI Thread
//                ((Activity) stockViewActivity).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Notify the List that the DataSet has changed...
//                        notifyDataSetChanged();
//                    }
//                });

//            }
//        }).start();

    }

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


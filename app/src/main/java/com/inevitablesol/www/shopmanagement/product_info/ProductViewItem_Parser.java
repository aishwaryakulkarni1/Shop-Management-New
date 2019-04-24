package com.inevitablesol.www.shopmanagement.product_info;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.ItemModule.Adapter.StockAdapter;
import com.inevitablesol.www.shopmanagement.ItemModule.StockInfo;
import com.inevitablesol.www.shopmanagement.ItemModule.ViewItems;
import com.inevitablesol.www.shopmanagement.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pritam on 01-12-2017.
 */

public class ProductViewItem_Parser extends RecyclerView.Adapter<ProductViewItem_Parser.ViewHolder>  implements Filterable
{


    private ArrayList<StockInfo> listData;
    private static final String TAG = "ProductViewItem_Parser";

    private ProductViewDetails productViewItem_parser;
    private List<StockInfo> contactListFiltered;

    public ProductViewItem_Parser(ArrayList<StockInfo> listData, ProductViewDetails  productViewItem_parser) {

        this.listData = listData;
        this.productViewItem_parser = productViewItem_parser;
    }



    public ProductViewItem_Parser.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_item_details, viewGroup, false);
        return new ProductViewItem_Parser.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewItem_Parser.ViewHolder viewHolder, int i) {

        final StockInfo list = listData.get(i);
        viewHolder.tv_itemname.setText(String.valueOf(list.getItem_name()));
        viewHolder.tv_hss.setText(String.valueOf(list.getHsn_ssc_code()));
        //viewHolder.tv_storage_qty.setText(String.valueOf(list.getStorage_qty()));
        // viewHolder.tv_mrp.setText(String.valueOf(list.getMrp()));
        //viewHolder.tv_product_type.setText(String.valueOf(list.getProduct_type()));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_itemname,tv_hss,tv_mrp,tv_product_type,tv_storage_qty;
        public ViewHolder(View view) {
            super(view);

            tv_itemname = (TextView) view.findViewById(R.id.tv_itemname);
            tv_hss = (TextView) view.findViewById(R.id.tv_hss);
//            tv_mrp = (TextView) view.findViewById(R.id.tv_mrp);
//            tv_product_type = (TextView) view.findViewById(R.id.tv_product_type);
        //    tv_storage_qty = (TextView) view.findViewById(R.id.tv_storage_qty);

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

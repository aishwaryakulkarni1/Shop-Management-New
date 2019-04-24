package com.inevitablesol.www.shopmanagement.expenses;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

import java.util.ArrayList;

/**
 * Created by Pritam on 14-09-2017.
 */

public class ItemAdapterListView extends RecyclerView.Adapter<ItemAdapterListView.ViewHolder> {
    private ArrayList<ExpensesListItems> listData;
    Add_expenses add_expenses;
    private SelectedExpensiveDetails selectedExpensiveDetails;

    private static final String TAG = "ItemAdapterView";



    public ItemAdapterListView(ArrayList<ExpensesListItems> iLists, SelectedExpensiveDetails selectedExpensiveDetails) {
        this.listData = iLists;
        this.selectedExpensiveDetails = selectedExpensiveDetails;
    }


    @Override
    public ItemAdapterListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_view_item_exp, viewGroup, false);
        return new ItemAdapterListView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemAdapterListView.ViewHolder viewHolder, int i) {

        final ExpensesListItems list = listData.get(i);
        Log.d(TAG, "onBindViewHolder:"+list.toString());
        viewHolder.tv_name.setText(String.valueOf(list.getItemName()));
        viewHolder.tv_itemprice.setText(String.valueOf(list.getTotalPrice()));
        viewHolder.tv_itemUnitPrice.setText(String.valueOf(list.getUnitPrice()));
        viewHolder.tv_itemQty.setText(list.getQty());


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_itemprice, tv_itemUnitPrice, tv_itemQty;


        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tv_itemname);
            tv_itemprice = (TextView) view.findViewById(R.id.tv_itemPrice);
            tv_itemUnitPrice = (TextView) view.findViewById(R.id.tv_itemUnitPrice);
            tv_itemQty = (TextView) view.findViewById(R.id.tv_itemQty);


        }
    }

    public void clearView() {
        int size = listData.size();

        if (size > 0) {
            for (int i = 0; i < listData.size(); i++) {
                this.listData.remove(i);
            }
            this.notifyItemRangeRemoved(0, size);
        }

    }
}
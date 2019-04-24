package com.inevitablesol.www.shopmanagement.expenses;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.wishList.Add_WishList;


import java.util.ArrayList;

/**
 * Created by Pritam on 12-09-2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ArrayList<ExpensesListItems> listData;
    Add_expenses add_expenses;
    private SelectedExpensiveDetails selectedExpensiveDetails;
    Add_WishList add_wishList;

    private static final String TAG = "ItemAdapter";

    public ItemAdapter(ArrayList<ExpensesListItems> expensesListItemses, Add_expenses add_expenses) {
        this.listData = expensesListItemses;
        this.add_expenses = add_expenses;
    }



    public ItemAdapter(ArrayList<ExpensesListItems> iLists, SelectedExpensiveDetails selectedExpensiveDetails)
    {
        this.listData = iLists;
        this.selectedExpensiveDetails=selectedExpensiveDetails;
    }

    public ItemAdapter(ArrayList<ExpensesListItems> expensesListItemses, Add_WishList add_wishList)
    {
        this.listData=expensesListItemses;
        this.add_wishList=add_wishList;
    }


    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_view_item, viewGroup, false);
        return new ItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder viewHolder, int i) {

        final ExpensesListItems list = listData.get(i);
        viewHolder.tv_name.setText(String.valueOf(list.getItemName()));
        viewHolder.tv_itemprice.setText(String.valueOf(list.getTotalPrice()));
        viewHolder.tv_itemUnitPrice.setText(String.valueOf(list.getUnitPrice()));
        viewHolder.tv_itemQty.setText(list.getQty());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
            {
                if(isChecked)
                {
                    list.setIsChecked(true);
                    Log.d(TAG, "onCheckedChanged:");
                }else 
                {
                    list.setIsChecked(false);
                }
                

            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_itemprice, tv_itemUnitPrice, tv_itemQty;
        private CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tv_itemname);
            tv_itemprice = (TextView) view.findViewById(R.id.tv_itemPrice);
            tv_itemUnitPrice = (TextView) view.findViewById(R.id.tv_itemUnitPrice);
            tv_itemQty = (TextView) view.findViewById(R.id.tv_itemQty);
            checkBox=(CheckBox)view.findViewById(R.id.exp_checkBox);


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
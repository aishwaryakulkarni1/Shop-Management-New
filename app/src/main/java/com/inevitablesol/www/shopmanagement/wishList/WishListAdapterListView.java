package com.inevitablesol.www.shopmanagement.wishList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.expenses.Add_expenses;

import java.util.ArrayList;

/**
 * Created by Pritam on 15-09-2017.
 */

public class WishListAdapterListView  extends RecyclerView.Adapter<WishListAdapterListView.ViewHolder> {
    private ArrayList<WishListItems_pojo> listData;
    Add_expenses add_expenses;
    private SelectedWishDetails selectedWishDetails;

    private static final String TAG = "ItemAdapterView";



    public WishListAdapterListView(ArrayList<WishListItems_pojo> iLists, SelectedWishDetails selectedExpensiveDetails) {
        this.listData = iLists;
        this.selectedWishDetails = selectedExpensiveDetails;
    }


    @Override
    public WishListAdapterListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_list_view_item_wish, viewGroup, false);
        return new WishListAdapterListView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WishListAdapterListView.ViewHolder viewHolder, int i) {

        final WishListItems_pojo list = listData.get(i);
        Log.d(TAG, "onBindViewHolder:"+list.toString());
        viewHolder.tv_name.setText(String.valueOf(list.getName()));
        viewHolder.tv_company.setText(String.valueOf(list.getCompany()));
        viewHolder.tv_itemQty.setText(String.valueOf(list.getQty()));
        viewHolder.tv_itemQty.setText(list.getQty());


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_company, tv_itemQty;


        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tv_itemname);
            tv_company = (TextView) view.findViewById(R.id.tv_comp);
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

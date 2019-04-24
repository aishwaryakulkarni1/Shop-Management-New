package com.inevitablesol.www.shopmanagement.wishList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

import java.util.ArrayList;

/**
 * Created by Pritam on 15-09-2017.
 */

public class WishAdapter  extends RecyclerView.Adapter<WishAdapter.ViewHolder> {


    private ArrayList<WishGsonList> listData;
    private WishAdapter editVendorActivity;

    private View_WishList view_expense;


    public WishAdapter(ArrayList<WishGsonList> listData, View_WishList viewVendor) {

        this.listData = listData;
        this.view_expense = viewVendor;
    }


    @Override
    public WishAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gson_wish_list, viewGroup, false);
        return new WishAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WishAdapter.ViewHolder viewHolder, int i) {

        final WishGsonList list = listData.get(i);
        viewHolder.tv_date.setText(String.valueOf(list.getTillDate()));
        viewHolder.tx_status.setText(String.valueOf(list.getStatus()));
        viewHolder.tv_name.setText(String.valueOf(list.getCustName()));


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_date, tv_name, tx_status;

        public ViewHolder(View view) {
            super(view);

            tv_date = (TextView) view.findViewById(R.id.wish_date);
            tv_name = (TextView) view.findViewById(R.id.wish_cname);
            tx_status = (TextView) view.findViewById(R.id.wish_status);


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

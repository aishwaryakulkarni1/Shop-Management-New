package com.inevitablesol.www.shopmanagement.purchase_module;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

import java.util.ArrayList;

/**
 * Created by Pritam on 16-08-2017.
 */

public class View_SelectedList extends RecyclerView.Adapter<View_SelectedList.ViewHolder>
{
    private ArrayList<ItemList> listData;
    private PurchaseView_listItem  purchaseView;


    public View_SelectedList(ArrayList<ItemList> listData, PurchaseView_listItem viewVendor)
    {

        this.listData = listData;
        this.purchaseView = viewVendor;
    }




    @Override
    public View_SelectedList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_list, viewGroup, false);
        return new View_SelectedList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(View_SelectedList.ViewHolder viewHolder, int i) {

        final ItemList  list = listData.get(i);
        viewHolder.tv_name.setText(String.valueOf(list.getItemName()));
        viewHolder.tv_itemdiscount.setText(String.valueOf(list.getDiscount()));
        viewHolder.tv_itemUnitPrice.setText(String.valueOf(list.getUnitPrice()));
        viewHolder.tv_itemQty.setText(list.getQty());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_itemdiscount, tv_itemUnitPrice,tv_itemQty;

        public ViewHolder(View view)
        {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tv_itemname);

            tv_itemUnitPrice = (TextView) view.findViewById(R.id.tv_itemUnitPrice);
            tv_itemQty=(TextView)view.findViewById(R.id.tv_itemQty);
            tv_itemdiscount=(TextView)view.findViewById(R.id.tv_itemDiscount);


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

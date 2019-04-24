package com.inevitablesol.www.shopmanagement.billing_module;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.Quotation.Quotation_Details;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.purchase_module.ItemList;
import com.inevitablesol.www.shopmanagement.purchase_module.PurchaseView_listItem;
import com.inevitablesol.www.shopmanagement.purchase_module.View_SelectedList;

import java.util.ArrayList;

/**
 * Created by Pritam on 29-11-2017.
 */

public class Invoice_list_recycler  extends RecyclerView.Adapter<Invoice_list_recycler.ViewHolder> {
    private ArrayList<Invoice_item_list> listData;
    private   Invoice_Details  purchaseView;
    private  Quotation_Details quotation_details;


    public Invoice_list_recycler(ArrayList<Invoice_item_list> listData, Invoice_Details viewVendor) {

        this.listData = listData;
        this.purchaseView = viewVendor;
    }

    public Invoice_list_recycler(ArrayList<Invoice_item_list> list, Quotation_Details quotation_details)
    {
        this.listData = list;
        this.quotation_details = quotation_details;

    }


    @Override
    public Invoice_list_recycler.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_list_item_invocie, viewGroup, false);
        return new Invoice_list_recycler.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Invoice_list_recycler.ViewHolder viewHolder, int i) {

        final Invoice_item_list list = listData.get(i);
        viewHolder.tv_name.setText(String.valueOf(list.getItemName()));
        viewHolder.tv_itemUnitPrice.setText(String.valueOf(list.getItemPrice()));
        viewHolder.tv_itemQty.setText(list.getItemQty());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_itemdiscount, tv_itemUnitPrice, tv_itemQty;

        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tv_itemname);

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
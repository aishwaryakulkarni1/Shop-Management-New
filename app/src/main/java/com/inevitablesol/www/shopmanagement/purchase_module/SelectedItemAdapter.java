package com.inevitablesol.www.shopmanagement.purchase_module;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.expenses.Add_expenses;

import java.util.ArrayList;

/**
 * Created by Pritam on 21-07-2017.
 */

public class SelectedItemAdapter extends RecyclerView.Adapter<SelectedItemAdapter.ViewHolder>
{
    private ArrayList<SelectedItemClass> listData;
    private AddPurchaseDetails  purchaseView;
    Add_expenses add_expenses;

    private BarcodeProduct barcodeProduct;


    public SelectedItemAdapter(ArrayList<SelectedItemClass> listData, AddPurchaseDetails viewVendor)
    {

        this.listData = listData;
        this.purchaseView = viewVendor;
    }

    public SelectedItemAdapter(ArrayList<SelectedItemClass> listData, BarcodeProduct barcodeProduct)
    {
        this.listData = listData;
        this.barcodeProduct = barcodeProduct;

    }


    @Override
    public SelectedItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_view_item_purchases, viewGroup, false);
        return new SelectedItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectedItemAdapter.ViewHolder viewHolder, int i) {

        final SelectedItemClass list = listData.get(i);
        viewHolder.tv_name.setText(String.valueOf(list.getItemName()));
        viewHolder.tv_itemprice.setText(String.valueOf(list.getItemPrice()));
        viewHolder.tv_itemUnitPrice.setText(String.valueOf(list.getItemUnitPrice()));
        viewHolder.tv_itemQty.setText(list.getItemQty());
        viewHolder.checkBoxDeleteItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    list.setChecked(true);
                }else
                {
                    list.setChecked(false);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_itemprice, tv_itemUnitPrice,tv_itemQty;
        private CheckBox checkBoxDeleteItem;

        public ViewHolder(View view)
        {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tv_itemname);
            tv_itemprice = (TextView) view.findViewById(R.id.tv_itemPrice);
            tv_itemUnitPrice = (TextView) view.findViewById(R.id.tv_itemUnitPrice);
            tv_itemQty=(TextView)view.findViewById(R.id.tv_itemQty);
            checkBoxDeleteItem=(CheckBox)view.findViewById(R.id.purches_delete_item);


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

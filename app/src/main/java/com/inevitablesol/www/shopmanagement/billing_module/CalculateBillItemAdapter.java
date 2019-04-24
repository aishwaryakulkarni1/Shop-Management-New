package com.inevitablesol.www.shopmanagement.billing_module;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.InstantBill_selectedItem;
import com.inevitablesol.www.shopmanagement.Quotation.Quotation_cart_item;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.sql_lite.ItemDetalisClass;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;


import java.util.ArrayList;

/**
 * Created by Pritam on 17-05-2017.
 */

public class CalculateBillItemAdapter extends RecyclerView.Adapter<CalculateBillItemAdapter.ViewHolder> {

    private ArrayList<ItemDetalisClass> listData;
//    private ArrayList<SelectedItem> selected;
    BillingCartItems billingCartItems;
    Quotation_cart_item quotation_cart_item;

    SqlDataBase sqlDataBase;
    Context context;
    InstantBill_selectedItem instantBill_selectedItem;
    private static final String TAG = "CalculateBillItemAdapte";


    public CalculateBillItemAdapter(Context context, ArrayList<ItemDetalisClass> listData, BillingCartItems billingCartItems) {
        this.context=context;
        this.listData = listData;
        this.billingCartItems = billingCartItems;
    }

    public CalculateBillItemAdapter(Context context, ArrayList<ItemDetalisClass> listData, Quotation_cart_item quotation_cart_item)
    {
        this.context=context;
        this.listData = listData;
        this.quotation_cart_item=quotation_cart_item;
    }

    public CalculateBillItemAdapter(Context context, ArrayList<ItemDetalisClass> listData, InstantBill_selectedItem instantBill_selectedItem)
    {

        this.context=context;
        this.listData = listData;
        this.instantBill_selectedItem =instantBill_selectedItem;
    }


    @Override
    public CalculateBillItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_list_item_for_billing, viewGroup, false);
        sqlDataBase=new SqlDataBase(context);
        return new CalculateBillItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CalculateBillItemAdapter.ViewHolder viewHolder, int i) {

        final ItemDetalisClass list = listData.get(i);
        viewHolder.tv_itemname.setText(String.valueOf(list.getItem_name()));
        viewHolder.tv_mrp.setText(String.valueOf(list.getTotal_taxableValue()));
        viewHolder.txt_Qty.setText(String.valueOf(list.getSelectd_qty()));
        viewHolder.tv_unit.setText(String.valueOf(list.getShortcut()));

        Log.d(TAG, "onBindViewHolder: "+list.getTotal_taxableValue() );

    }




    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_itemname, txt_Qty, tv_mrp, tv_unit;
        ImageView addItem, subItem;
        TextView sumValue;

        public ViewHolder(View view)
        {
            super(view);

            tv_itemname = (TextView) view.findViewById(R.id.tv_itemname);
             tv_unit = (TextView) view.findViewById(R.id.txt_billUnit);
            tv_mrp = (TextView) view.findViewById(R.id.tv_mrp);

            txt_Qty = (TextView) view.findViewById(R.id.txt_billQty);

//           floatingActionButton=(FloatingActionButton)view.findViewById(R.id.addQty);
        }
    }
}

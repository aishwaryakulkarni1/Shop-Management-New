package com.inevitablesol.www.shopmanagement.purchase_module;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

import java.util.ArrayList;

/**
 * Created by Pritam on 21-07-2017.
 */

public class PurchaseViewAdapter extends RecyclerView.Adapter<PurchaseViewAdapter.ViewHolder>
{
    private ArrayList<PurchaseViewRecord> listData;
      private  PurchaseView purchaseView;



    public PurchaseViewAdapter(ArrayList<PurchaseViewRecord> listData, PurchaseView purchaseView)
    {

        this.listData = listData;
        this.purchaseView =purchaseView;
    }




    @Override
    public PurchaseViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.v_list, viewGroup, false);
        return new PurchaseViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PurchaseViewAdapter.ViewHolder viewHolder, int i)
    {



        final PurchaseViewRecord list = listData.get(i);
        viewHolder.tv_name.setText(String.valueOf(list.getCompany()));
        viewHolder.tv_invoice.setText(String.valueOf(list.getInvoiceNo()));

    }

    @Override
    public int getItemCount()
    {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_name,tv_invoice;
        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tv_p_name);
            tv_invoice= (TextView) view.findViewById(R.id.tv_invioce);



        }
    }

    public  void  clearView()
    {
        int size=listData.size();

        if(size>0)
        {
            for(int i=0;i<listData.size();i++)
            {
                this.listData.remove(i);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }
}

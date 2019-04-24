package com.inevitablesol.www.shopmanagement;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.billing_module.Transaction_History;
import com.inevitablesol.www.shopmanagement.billing_module.Transrecord;
import com.inevitablesol.www.shopmanagement.purchase_module.PurchaseView;
import com.inevitablesol.www.shopmanagement.vendor_module.EditVendorActivity;
import com.inevitablesol.www.shopmanagement.vendor_module.Record;
import com.inevitablesol.www.shopmanagement.vendor_module.VendorAdapter;
import com.inevitablesol.www.shopmanagement.vendor_module.View_Vendor;

import java.util.ArrayList;

/**
 * Created by Pritam on 13-06-2018.
 */

public class Transaction_Adapter extends RecyclerView.Adapter<Transaction_Adapter.ViewHolder>
{
    private ArrayList<Transrecord> listData;
    private Transaction_History transaction_history;
    private static final String TAG = "Transaction_Adapter";

    public Transaction_Adapter(ArrayList<Transrecord> listData, Transaction_History transaction_history)
    {
        this.listData = listData;
        this.transaction_history=transaction_history;
    }


    @Override
    public Transaction_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_layout, viewGroup, false);
        return new Transaction_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Transaction_Adapter.ViewHolder viewHolder, int i)
    {

        final Transrecord list = listData.get(i);
        viewHolder.tv_id.setText(String.valueOf(list.getInvoiceId()));
        viewHolder.tv_amount.setText(String.valueOf(list.getTotal()));
        viewHolder.tx_AmountPiad.setText(String.valueOf(list.getAmountPaid()));

        if(list.getStatus().equalsIgnoreCase("Settled"))
        {
            viewHolder.tv_charges.setText(String.valueOf(list.getCharges()));
        }else {
            viewHolder.tv_charges.setText("0");

        }


        viewHolder.tv_status.setText(String.valueOf(list.getStatus()));

    }


    @Override
    public int getItemCount()
    {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_id,tv_amount,tv_charges,tv_status,tx_AmountPiad;
        public ViewHolder(View view) {
            super(view);

            tv_id = (TextView) view.findViewById(R.id.tv_invoiceID);
            tv_amount = (TextView) view.findViewById(R.id.tv_Amount);
            tv_charges = (TextView) view.findViewById(R.id.tv_charges);
            tv_status=(TextView)view.findViewById(R.id.tv_status);
            tx_AmountPiad=(TextView)view.findViewById(R.id.tv_AmountPaid);

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

   /* public  <E> void getCurrentBalance(ArrayList<E> es)
    {
Log.d(TAG, "getCurrentBalance: "+es);

        for (E e:es)
        {
            Log.d(TAG, "getCurrentBalance: "+e);
        }

    }
    */

    public  double getCurrentBalance(ArrayList<Transrecord> es)
    {
        double curruntAmount=0.0;
        Log.d(TAG, "getCurrentBalance: "+es);
        if(es.isEmpty())
        {
            return curruntAmount;
        }

        for (Transrecord e:es)
        {
                if(e.getStatus().equalsIgnoreCase("Captured"))
                {
                    curruntAmount+=Double.parseDouble(e.getAmount());
                }
            Log.d(TAG, "getCurrentBalance: "+e);
        }
         return  curruntAmount;
    }

}

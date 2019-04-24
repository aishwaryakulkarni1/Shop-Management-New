package com.inevitablesol.www.shopmanagement.billing_module;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.DateFormat.ParseDate;
import com.inevitablesol.www.shopmanagement.R;


import java.util.ArrayList;

/**
 * Created by Pritam on 07-08-2017.
 */

public class InvoiceAdapter  extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder>
{


    private ArrayList<InvoiceRecord> listData;
    private Invoice_History  invoice_history;



    public InvoiceAdapter (ArrayList<InvoiceRecord> listData, Invoice_History  invoice_history)
    {

        this.listData = listData;
        this.invoice_history =invoice_history;
    }




    @Override
    public InvoiceAdapter .ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inv_history_list, viewGroup, false);
        return new InvoiceAdapter .ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final InvoiceRecord list = listData.get(position);
        holder.tv_invoiceid.setText(String.valueOf(list.getInvoiceId()));
        holder.tv_total.setText(String.valueOf(list.getTotal()));
        holder.tv_inv_amount.setText(list.getAmount());
        holder.tv_date.setText(ParseDate.geDate(list.getDateTime()));
        holder.tv_mode.setText(list.getShorpmode());
    }


    @Override
    public int getItemCount()
    {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_date,tv_invoiceid,tv_inv_amount,tv_mode,tv_total;
        public ViewHolder(View view)
        {
            super(view);
            tv_invoiceid = (TextView) view.findViewById(R.id.tv_inv_invId);
            tv_date= (TextView) view.findViewById(R.id.tv_inv_dateTime);
            tv_inv_amount=(TextView)view.findViewById(R.id.tv_inv_amount);
            tv_mode=(TextView)view.findViewById(R.id.tv_inv_modeofPayment);
            tv_total=(TextView)view.findViewById(R.id.tv_inv_total);
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

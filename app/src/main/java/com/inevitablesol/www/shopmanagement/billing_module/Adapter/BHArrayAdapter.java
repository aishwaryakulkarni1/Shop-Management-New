package com.inevitablesol.www.shopmanagement.billing_module.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.billing_module.InvoiceHistoryInfo;

import java.util.ArrayList;


/**
 * Created by Anup on 10-08-2017.
 */

public class BHArrayAdapter extends RecyclerView.Adapter<BHArrayAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    private static final String TAG = "BhArrayAdapter";
    private ArrayList<InvoiceHistoryInfo> itemList;

    // Constructor of the class
    public BHArrayAdapter(ArrayList<InvoiceHistoryInfo> itemList) {
        this.itemList = itemList;
    }




    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bh_row, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition)
    {
        TextView srno = holder.srno;
        TextView orderId = holder.invoice_id;
        TextView orderStatus = holder.amount_paid;
        TextView pickup = holder.balance_due;
        TextView pickTime = holder.mode_of_payment;


      //  srno.setText(""+(listPosition+1));
        orderId.setText(itemList.get(listPosition).getInvoice_id());
        orderStatus.setText(itemList.get(listPosition).getTotal());
                    double dueBalance= Double.parseDouble(itemList.get(listPosition).getBalance_due());

                         if (dueBalance>0)
                         {
                             Log.d(TAG, "onBindViewHolder:iF Due Balance"+dueBalance);
                             pickup.setText(itemList.get(listPosition).getBalance_due());
                              pickup.setTextColor(android.graphics.Color.RED);
                         }else
                         {
                             pickup.setTextColor(android.graphics.Color.WHITE);
                             Log.d(TAG, "onBindViewHolder:else Due Balance"+dueBalance);
                             pickup.setText(itemList.get(listPosition).getBalance_due());
                         }

        pickTime.setText(itemList.get(listPosition).getShortCut());
    }


    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView srno, invoice_id, amount_paid, balance_due, mode_of_payment;

        public ViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
           // srno = (TextView) itemView.findViewById(R.id.tv_srno);
            invoice_id = (TextView) itemView.findViewById(R.id.tv_inv_no);
            amount_paid = (TextView) itemView.findViewById(R.id.tv_amt);
            balance_due = (TextView) itemView.findViewById(R.id.tv_bal);
            mode_of_payment = (TextView) itemView.findViewById(R.id.tv_mode);

        }

        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + invoice_id.getText());
        }
    }
}

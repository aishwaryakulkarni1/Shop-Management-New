package com.inevitablesol.www.shopmanagement.billing_module.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.billing_module.Parser.Quotation_pojo;

import java.util.ArrayList;

/**
 * Created by Pritam on 14-08-2017.
 */

public class QuotationHistory_Adapter extends RecyclerView.Adapter<QuotationHistory_Adapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    private ArrayList<Quotation_pojo> itemList;

    // Constructor of the class
    public QuotationHistory_Adapter(ArrayList<Quotation_pojo> itemList) {
        this.itemList = itemList;
    }




    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    // specify the row layout file and click for each row
    @Override
    public QuotationHistory_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lay_quotattion_history, parent, false);
        QuotationHistory_Adapter.ViewHolder myViewHolder = new QuotationHistory_Adapter.ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final QuotationHistory_Adapter.ViewHolder holder, final int listPosition) {

        TextView qId = holder.q_id;
        TextView amount= holder.amount;



        //  srno.setText(""+(listPosition+1));
        qId.setText(itemList.get(listPosition).getQ_id());
        amount.setText(itemList.get(listPosition).getAmount());

    }


    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView srno, q_id, amount;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            // srno = (TextView) itemView.findViewById(R.id.tv_srno);
            q_id = (TextView) itemView.findViewById(R.id.tv_q_no);
            amount = (TextView) itemView.findViewById(R.id.tv_bal);


        }

        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + q_id.getText());
        }
    }
}


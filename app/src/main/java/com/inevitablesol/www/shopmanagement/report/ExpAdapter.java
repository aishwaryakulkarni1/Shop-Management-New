package com.inevitablesol.www.shopmanagement.report;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

import java.util.ArrayList;

/**
 * Created by Pritam on 20-04-2018.
 */

public class ExpAdapter extends RecyclerView.Adapter<ExpAdapter.ViewPurchaseReport>
{

    private ArrayList<Exprecord> listData;

    private  double  totalAmount;
    public   double   totalBalance;


    public ExpAdapter(ArrayList<Exprecord> list)
    {
        this.listData = list;
    }


    @Override
    public ExpAdapter.ViewPurchaseReport onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_purchaseview, parent, false);
        return new ExpAdapter.ViewPurchaseReport(view);
    }

    @Override
    public void onBindViewHolder(ExpAdapter.ViewPurchaseReport holder, int position)
    {
        Exprecord purchaserecord=listData.get(position);
        holder.tv_createdDate.setText(purchaserecord.getCreatedDate());
        holder.tv_taxableValue.setText(purchaserecord.getSubTotal());
        holder.tv_rp_totalAmount.setText(purchaserecord.getTotalAmt());
        holder.tv_rp_totalGst.setText(purchaserecord.getTotalGst());
        holder.tv_balnce.setText(purchaserecord.getBalance());


    }

    @Override
    public int getItemCount() {
        return  listData == null ? 0 :listData.size();    }

    public  class  ViewPurchaseReport extends RecyclerView.ViewHolder
    {
        private TextView tv_createdDate,tv_taxableValue,tv_rp_totalAmount,tv_rp_totalGst,tv_balnce;

        public ViewPurchaseReport(View itemView)
        {
            super(itemView);



            tv_createdDate = (TextView) itemView.findViewById(R.id.tv_rp_createdDate);
            tv_taxableValue = (TextView) itemView.findViewById(R.id.tv_rp_taxvalue);
            tv_rp_totalGst=(TextView)itemView.findViewById(R.id.tv_rp_totalgst);
            tv_rp_totalAmount=(TextView)itemView.findViewById(R.id.tv_rp_totalAmount);
            tv_balnce=(TextView)itemView.findViewById(R.id.tv_rp_balance);
        }
    }

    public  void  clearView()
    {
        int size=listData.size();
        listData.clear();

//        if(size>0)
//        {
//            for(int i=0;i<listData.size();i++)
//            {
//                this.listData.remove(i);
//            }
        this.notifyItemRangeRemoved(0, size);
        //}
    }

    public    double getTotalAmount()
    {
        for (Exprecord listDate : listData )
        {
            totalAmount+=Double.parseDouble(listDate.getTotalAmt());
            totalBalance+=Double.parseDouble(listDate.getBalance());

        }
        return  totalAmount;
    }
}


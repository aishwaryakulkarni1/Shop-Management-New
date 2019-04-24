package com.inevitablesol.www.shopmanagement.report;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

import java.util.ArrayList;

/**
 * Created by Pritam on 25-04-2018.
 */

public class SaleAdater extends RecyclerView.Adapter<SaleAdater.ViewPurchaseReport> {

    private ArrayList<Salerecord> listData;
    private  double  totalAmount;
    public   double   totalBalance;


    public SaleAdater(ArrayList<Salerecord> list)
    {
        this.listData = list;
    }



    @Override
    public SaleAdater.ViewPurchaseReport onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_purchaseview, parent, false);
        return new SaleAdater.ViewPurchaseReport(view);
    }

    @Override
    public void onBindViewHolder(SaleAdater.ViewPurchaseReport holder, int position) {
        Salerecord purchaserecord = listData.get(position);
        holder.tv_createdDate.setText(purchaserecord.getCreatedDate());
        holder.tv_taxableValue.setText(purchaserecord.getTaxableValue());
        holder.tv_rp_totalAmount.setText(purchaserecord.getTotal());
        holder.tv_rp_totalGst.setText(purchaserecord.getTotalGst());
        holder.tv_balnce.setText(purchaserecord.getBalanceDue());


    }

    @Override
    public int getItemCount() {
        return listData == null ? 0 : listData.size();
    }

    public class ViewPurchaseReport extends RecyclerView.ViewHolder {
        private TextView tv_createdDate, tv_taxableValue, tv_rp_totalAmount, tv_rp_totalGst, tv_balnce;

        public ViewPurchaseReport(View itemView) {
            super(itemView);


            tv_createdDate = (TextView) itemView.findViewById(R.id.tv_rp_createdDate);
            tv_taxableValue = (TextView) itemView.findViewById(R.id.tv_rp_taxvalue);
            tv_rp_totalGst = (TextView) itemView.findViewById(R.id.tv_rp_totalgst);
            tv_rp_totalAmount = (TextView) itemView.findViewById(R.id.tv_rp_totalAmount);
            tv_balnce = (TextView) itemView.findViewById(R.id.tv_rp_balance);
        }
    }

    public void clearView()
    {
        int size = listData.size();
        listData.clear();


        this.notifyItemRangeRemoved(0, size);
        //}
    }
      public    double getTotalAmount()
      {
          for (Salerecord listDate : listData )
          {
                 totalAmount+=Double.parseDouble(listDate.getTotal());
                 totalBalance+=Double.parseDouble(listDate.getBalanceDue());

          }
           return  totalAmount;
      }
}

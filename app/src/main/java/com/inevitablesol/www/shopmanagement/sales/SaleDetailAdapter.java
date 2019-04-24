package com.inevitablesol.www.shopmanagement.sales;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.inevitablesol.www.shopmanagement.DateFormat.ParseDate;
import com.inevitablesol.www.shopmanagement.R;

import java.util.ArrayList;

/**
 * Created by Pritam on 30-05-2017.
 */

public class SaleDetailAdapter extends RecyclerView.Adapter<SaleDetailAdapter.ViewHolder>
{
    private ArrayList<SaleInfo> listData;

    private SalesActivity salesActivity;
    Sales_ModeOfPayment sales_modeOfPayment;
    Sales_TotalSales sales_totalSales;
     public  double totalAmount=0.0;
   private  Sales_CustomerActivity sales_customerActivity;
  //  private  Services_Customer_View services_customer_view;

    Sale_Users sale_users;
    private static final String TAG = "SaleDetailAdapter";
 //   Sales_ProductType sales_productType;
    Sales_DateTime sales_dateTime;

    private Sales_ProductType sales_productType;
    public SaleDetailAdapter(ArrayList<SaleInfo> listData, SalesActivity salesActivity)
    {
        this.listData = listData;
        this.salesActivity = salesActivity;
    }

    public SaleDetailAdapter(ArrayList<SaleInfo> listData, Sales_ModeOfPayment sales_modeOfPayment)
    {
        this.listData = listData;
        this.sales_modeOfPayment=sales_modeOfPayment;
    }

    public SaleDetailAdapter(ArrayList<SaleInfo> saleInfos, Sales_TotalSales sales_totalSales)
    {
         this.listData=saleInfos;
        this.sales_totalSales=sales_totalSales;
    }

//    public SaleDetailAdapter(ArrayList<SaleInfo> saleInfos, Sales_ProductType sales_productType)
//    {
//        this.listData=saleInfos;
//        this.sales_productType=sales_productType;
//
//    }

    public SaleDetailAdapter(ArrayList<SaleInfo> saleInfos, Sales_DateTime sales_dateTime)
    {
        this.listData=saleInfos;
         this.sales_dateTime=sales_dateTime;
       }

    public SaleDetailAdapter(ArrayList<SaleInfo> saleInfos, Sale_Users sale_users)
    {
        this.listData=saleInfos;
        this.sale_users=sale_users;
    }

    public SaleDetailAdapter(ArrayList<SaleInfo> saleInfos, Sales_CustomerActivity sales_customerActivity)
    {
         this.listData=saleInfos;
          this.sales_customerActivity=sales_customerActivity;
    }

    public SaleDetailAdapter(ArrayList<SaleInfo> saleInfos, Sales_ProductType sales_productType)
    {
        this.listData=saleInfos;
        this.sales_productType=sales_productType;

    }

//    public SaleDetailAdapter(ArrayList<SaleInfo> saleInfos, Services_Customer_View services_customer_view)
//    {
//        this.listData=saleInfos;
//        this.services_customer_view=services_customer_view;
//    }



    @Override
    public SaleDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sales_view, viewGroup, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SaleDetailAdapter.ViewHolder holder, int i)
    {

         final SaleInfo list = listData.get(i);
         holder.tv_saleCreatedDate.setText(ParseDate.geDate(list.getCreated_Date()));
         holder.tv_item.setText(list.getInvoice_id());
         holder.tv_mrp.setText(list.getModeOfPayment());
         holder.tv_modeOfPayment.setText(list.getTotalAmount());
         holder.tv_totalAmount.setText(list.getBalanceDue());
        double dueBalance= Double.parseDouble(list.getBalanceDue());

        if (dueBalance>0)
        {
            Log.d(TAG, "onBindViewHolder:iF Due Balance"+dueBalance);
            holder.tv_totalAmount.setText(list.getBalanceDue());
            holder.tv_totalAmount.setTextColor(android.graphics.Color.RED);
        }else
        {
            holder.tv_totalAmount.setTextColor(android.graphics.Color.WHITE);
            Log.d(TAG, "onBindViewHolder:else Due Balance"+dueBalance);
            holder.tv_totalAmount.setText(list.getBalanceDue());
        }





    }

    @Override
    public int getItemCount()
    {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_saleCreatedDate,tv_item,tv_mrp,tv_totalAmount,tv_modeOfPayment;

        public ViewHolder(View view)
        {
            super(view);
            tv_saleCreatedDate = (TextView) view.findViewById(R.id.tv_sale_createdDate);
             tv_item=            (TextView) view.findViewById(R.id.tv_sale_item);
            tv_mrp =            (TextView) view.findViewById(R.id.tv_sale_mrp);
            tv_modeOfPayment =  (TextView) view.findViewById(R.id.tv_sale_modeofPayment);
            tv_totalAmount =    (TextView) view.findViewById(R.id.tv_sale_totalAmount);

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

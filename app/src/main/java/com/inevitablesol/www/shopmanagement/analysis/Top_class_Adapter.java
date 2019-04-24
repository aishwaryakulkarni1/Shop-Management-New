package com.inevitablesol.www.shopmanagement.analysis;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

import java.util.ArrayList;

/**
 * Created by Pritam on 05-04-2018.
 */

public class Top_class_Adapter extends RecyclerView.Adapter<Top_class_Adapter.ViewHolder>
{
    private ArrayList<Top_seven_Class> listData;
    private HighestSellingDay  purchaseView;
    private HighestPurchaseDay highestPurchaseDay;
    private RepetativeCustomer repetativeCustomer;
    private  HighestCustomerDay highestCustomerDay;
    private  HighestSaleToCustomer highestSaleToCustomer;
    private  SellingProduct sellingProduct;


    public Top_class_Adapter(ArrayList<Top_seven_Class> listData, HighestSellingDay viewVendor)
    {

        this.listData = listData;
        this.purchaseView = viewVendor;
    }

    public Top_class_Adapter(ArrayList<Top_seven_Class> top_seven_classes, HighestPurchaseDay highestPurchaseDay)
    {
        this.listData=top_seven_classes;
        this.highestPurchaseDay=highestPurchaseDay;

    }

    public Top_class_Adapter(ArrayList<Top_seven_Class> top_seven_classes, RepetativeCustomer repetativeCustomer)
    {
        this.listData=top_seven_classes;
        this.repetativeCustomer=repetativeCustomer;
    }

    public Top_class_Adapter(ArrayList<Top_seven_Class> top_seven_classes, HighestCustomerDay highestCustomerDay)
    {
        this.highestCustomerDay=highestCustomerDay;
        this.listData=top_seven_classes;
    }

    public Top_class_Adapter(ArrayList<Top_seven_Class> top_seven_classes, HighestSaleToCustomer highestSaleToCustomer)
    {
        this.highestSaleToCustomer=highestSaleToCustomer;
        this.listData=top_seven_classes;

    }

    public Top_class_Adapter(ArrayList<Top_seven_Class> top_seven_classes, SellingProduct sellingProduct)
    {
        this.sellingProduct=sellingProduct;
        this.listData=top_seven_classes;

    }


    @Override
    public Top_class_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_seven_data, viewGroup, false);
        return new Top_class_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Top_class_Adapter.ViewHolder viewHolder, int i) {

        final Top_seven_Class  list = listData.get(i);
        if(repetativeCustomer!=null || highestSaleToCustomer!=null)
        {
            viewHolder.tv_sr.setText(String.valueOf(i+1));
            viewHolder.top_date.setText(String.valueOf(list.getCreated_Date()));
            viewHolder.tv_top_inv.setText(String.valueOf(list.getInvoice_count()));
//            viewHolder.tv_top_total.setText(list.getTotal());

        }else
            {
            viewHolder.tv_sr.setText(String.valueOf(i+1));
            viewHolder.top_date.setText(String.valueOf(list.getCreated_Date()));
            viewHolder.tv_top_inv.setText(String.valueOf(list.getInvoice_count()));
            viewHolder.tv_top_total.setText(list.getTotal());

        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_sr, top_date, tv_top_inv,tv_top_total;

        public ViewHolder(View view)
        {
            super(view);

            tv_sr = (TextView) view.findViewById(R.id.top_srn);

            top_date = (TextView) view.findViewById(R.id.top_inv_date);
            tv_top_inv=(TextView)view.findViewById(R.id.top_totalinv);
            tv_top_total=(TextView)view.findViewById(R.id.top_total);


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

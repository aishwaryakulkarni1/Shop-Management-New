package com.inevitablesol.www.shopmanagement.billing_module;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;

import java.util.ArrayList;

/**
 * Created by Pritam on 15-05-2017.
 */

public class BillingStockAdater extends RecyclerView.Adapter<BillingStockAdater.ViewHolder>
{

    private ArrayList<StockInfo> listData;

    private MakeBillingCart makeBillingCart;
    //private MakeQuotatioCart  makeQuotatioCart;




    public BillingStockAdater(ArrayList<StockInfo> listData, MakeBillingCart makeBillingCart) {

        this.listData = listData;
        this.makeBillingCart = makeBillingCart;
    }



    @Override
    public BillingStockAdater.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.billing_selected_item, viewGroup, false);
        return new BillingStockAdater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BillingStockAdater.ViewHolder viewHolder, int i)
    {

        final StockInfo list = listData.get(i);



        viewHolder.tv_itemname.setText(String.valueOf(list.getItem_name()));

        viewHolder.tv_mrp.setText(String.valueOf(list.getMrp()));
        viewHolder.sumValue.setText(String.valueOf(list.getSelectedQuantity()));

        ((ViewHolder) viewHolder).addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String p_id=list.getItem_id();
                 String p_name=list.getItem_name();
                 String mrp=list.getMrp();

                 String qty= viewHolder.sumValue.getText().toString().trim();


                Log.d("p_name",p_name);
                Log.d("mrp",mrp);
                     int quantity=Integer.parseInt(qty);
                           quantity++;
                     viewHolder.sumValue.setText(String.valueOf(quantity));
                  list.setSelectedQuantity(String.valueOf(quantity));



                  Log.d("qty",qty);


            }
        });

        ((ViewHolder) viewHolder).subItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String p_name=list.getItem_name();
                String mrp=list.getMrp();
                  Log.d("p_name",p_name);
                   Log.d("mrp",mrp);
                String qty= viewHolder.sumValue.getText().toString().trim();
                int quantity=Integer.parseInt(qty);
                if(quantity>1)
                {
                    quantity--;
                    ((ViewHolder) viewHolder).sumValue.setText(String.valueOf(quantity));
                    list.setSelectedQuantity(String.valueOf(quantity));
                    Log.d("qty",qty);

                }else
                {

                }




            }

        });
        int totalQuantity= Integer.parseInt(viewHolder.sumValue.getText().toString().trim());
        Log.d("totalQuantity", String.valueOf(totalQuantity));

    }



    //private void getCount()
//    {
//        for (StockInfo stockInfo:listData)
//        {
//
//
//        }
//
//
//    }

    @Override
    public int getItemCount()
    {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_itemname,tv_stockqty,tv_mrp,tv_product_type;
        ImageView addItem,subItem;TextView sumValue;
        public ViewHolder(View view) {
            super(view);

            tv_itemname = (TextView) view.findViewById(R.id.tv_itemname);
           // tv_stockqty = (TextView) view.findViewById(R.id.tv_stockqty);
            tv_mrp = (TextView) view.findViewById(R.id.tv_mrp);
             addItem=(ImageView)view.findViewById(R.id.addItemForBilling);
            subItem=(ImageView)view.findViewById(R.id.subValuesForBilling);
            sumValue=(TextView)view.findViewById(R.id.txt_calValues);

//            floatingActionButton=(FloatingActionButton)view.findViewById(R.id.addQty);


        }
    }

}

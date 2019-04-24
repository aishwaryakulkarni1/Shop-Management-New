package com.inevitablesol.www.shopmanagement.expenses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.purchase_module.GetPurchaseImages;
import com.inevitablesol.www.shopmanagement.purchase_module.PurchaseImageAdapter;
import com.inevitablesol.www.shopmanagement.purchase_module.PurchaseImagePoJo;

import java.util.ArrayList;

/**
 * Created by Pritam on 25-04-2018.
 */

public class ExpenseAdapetrListView   extends RecyclerView.Adapter<ExpenseAdapetrListView.ViewSubUser>
{


        private ArrayList<GetImagesForExpense> listData;

        public ExpenseAdapetrListView(ArrayList<GetImagesForExpense> listdata)
        {
            this.listData = listdata;
        }

        public ExpenseAdapetrListView(Context applicationContext, GetExpenseImages getPurchaseImages, ArrayList<GetImagesForExpense> imageArray)
        {
            this.listData=imageArray;
        }


        @Override
        public ExpenseAdapetrListView.ViewSubUser onCreateViewHolder(ViewGroup viewGroup, int viewType)
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_vendor, viewGroup, false);
            return new ExpenseAdapetrListView.ViewSubUser(view);
        }

        @Override
        public void onBindViewHolder(ExpenseAdapetrListView.ViewSubUser holder, int position)
        {
            GetImagesForExpense alluser = listData.get(position);
            holder.tv_name.setText(String.valueOf(alluser.getId()));
            holder.tv_mobile.setText(String.valueOf(alluser.getImageCode()));


        }

        @Override
        public int getItemCount()
        {
            return  listData == null ? 0 :listData.size();
        }

public class ViewSubUser  extends RecyclerView.ViewHolder
{
    private TextView tv_name,tv_mobile;//tv_product_type;

    public ViewSubUser(View itemView)
    {
        super(itemView);



        tv_name = (TextView) itemView.findViewById(R.id.tv_custname);
        tv_mobile = (TextView) itemView.findViewById(R.id.tv_mobile);
    }
}

    public  void  clearView() {
        int size = listData.size();
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
}
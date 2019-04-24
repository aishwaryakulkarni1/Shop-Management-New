package com.inevitablesol.www.shopmanagement.expenses;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.vendor_module.EditVendorActivity;

import java.util.ArrayList;

/**
 * Created by Pritam on 14-09-2017.
 */

public class Gson_Exp_Adapter extends RecyclerView.Adapter<Gson_Exp_Adapter.ViewHolder> {


    private ArrayList<ExpList> listData;
    private EditVendorActivity editVendorActivity;

    private View_expense view_expense;

    public  static  double totalAmount=0.0;
   // taxableValue1=Math.round(taxableValue * 100.0) / 100.0;
   private static final String TAG = "Gson_Exp_Adapter";


    public Gson_Exp_Adapter(ArrayList<ExpList> listData, View_expense viewVendor)
    {
        totalAmount=0.0;

        this.listData = listData;
        this.view_expense = viewVendor;
    }



    @Override
    public Gson_Exp_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gson_exp_list, viewGroup, false);
        return new Gson_Exp_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Gson_Exp_Adapter.ViewHolder viewHolder, int i)
    {

        final ExpList list = listData.get(i);
        viewHolder.tv_date.setText(String.valueOf(list.getEdate()));
        viewHolder.tv_mode.setText(String.valueOf(list.getPaymentMode()));
        viewHolder.tv_amount.setText(String.valueOf(list.getTotalAmt()));
        viewHolder.tx_balance.setText(String.valueOf(list.getBalance()));
//       try {
//           totalAmount+=Math.round(Double.parseDouble(list.getAmountPaid() )*100.0/100.0);
//           Log.d(TAG, "ExceptionalAmount:Amount"+totalAmount);
//
//       }catch (NumberFormatException e)
//       {
//            Log.d(TAG, "ExceptionalAmount:Amount"+list.getAmountPaid());
//           Log.d(TAG, "ExceptionalAmount:Amount"+list.getId());
//
//
//       }





    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_date, tv_mode,tv_amount,tx_balance;

        public ViewHolder(View view) {
            super(view);

            tv_date = (TextView) view.findViewById(R.id.exp_date);
            tv_mode = (TextView) view.findViewById(R.id.exp_mode);
             tx_balance=(TextView)view.findViewById(R.id.exp_balance);
             tv_amount=(TextView)view.findViewById(R.id.exp_amount);

        }
    }

    public void clearView()
    {
        int size = listData.size();
          listData.clear();
          this.notifyDataSetChanged();
       Log.d(TAG, "clearView: Size"+size);
//
//        if (size > 0)
//        {
//            for (int i = 0; i < listData.size(); i++)
//            {
//                this.listData.remove(0);
//            }
//            this.notifyItemRangeRemoved(0, size);
//            this.notifyDataSetChanged();
//            Log.d(TAG, "clearView: After Remove Size"+listData.size());
//        }
    }
}

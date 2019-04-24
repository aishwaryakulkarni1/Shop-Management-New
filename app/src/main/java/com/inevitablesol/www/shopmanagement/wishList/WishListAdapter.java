package com.inevitablesol.www.shopmanagement.wishList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.expenses.Add_expenses;
import com.inevitablesol.www.shopmanagement.expenses.SelectedExpensiveDetails;

import java.util.ArrayList;

/**
 * Created by Pritam on 15-09-2017.
 */

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> implements RemomeItemFromList
{

private ArrayList<WishListItems_pojo> listData;
        Add_expenses add_expenses;
private SelectedExpensiveDetails selectedExpensiveDetails;
        Add_WishList add_wishList;

private static final String TAG = "ItemAdapter";

public WishListAdapter(ArrayList<WishListItems_pojo> expensesListItemses, Add_WishList add_wishList)
{
        this.listData = expensesListItemses;
        this.add_wishList = add_wishList;
}




    @Override
public WishListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_list_wish_list, viewGroup, false);
        return new WishListAdapter.ViewHolder(view);
        }

@Override
public void onBindViewHolder(WishListAdapter.ViewHolder viewHolder, final int i)
{

final WishListItems_pojo list = listData.get(i);

               viewHolder.tv_name.setText(String.valueOf(list.getName()));
               viewHolder.tv_itemprice.setText(String.valueOf(list.getCompany()));

               viewHolder.tv_itemQty.setText(list.getQty());
               viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                       if (isChecked) {
                           list.setChecked(true);
                         //  removeAt(i);
                           Log.d(TAG, "onCheckedChanged:");
                       } else {
                           Log.d(TAG, "onCheckedChanged:");
                           list.setChecked(false);
                       }


                   }
               });


        }

@Override
public int getItemCount() {
        return listData.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder
{
    private TextView tv_name, tv_itemprice, tv_itemUnitPrice, tv_itemQty;
    private CheckBox checkBox;

    public ViewHolder(View view) {
        super(view);

        tv_name = (TextView) view.findViewById(R.id.tv_itemname);
        tv_itemprice = (TextView) view.findViewById(R.id.tv_itemCompany);

        tv_itemQty = (TextView) view.findViewById(R.id.tv_itemQty);
        checkBox=(CheckBox)view.findViewById(R.id.exp_checkBox);


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


    public void removeAt(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listData.size());

    }


    @Override
    public void removeItem(ArrayList<WishListItems_pojo> wishListItems_pojo)
    {
        Log.d(TAG, "removeItem: ");
      //  expensesListItemses_new=new ArrayList<>();
     //   Log.d(TAG, "DeleteRows:BeforeDelete"+expensesListItemses.size());
     //   Log.d(TAG, "_delete_itemList:before"+expensesListItemses);


    }


}

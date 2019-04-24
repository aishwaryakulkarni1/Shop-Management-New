package com.inevitablesol.www.shopmanagement.account;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.billing_module.Adapter.BHArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pritam on 21-03-2018.
 */



public class SubUser_adapter extends RecyclerView.Adapter<SubUser_adapter.DataView>
{

    private  ArrayList<Record> records;


    public  SubUser_adapter(ArrayList<Record> records)
    {
        this.records=records;
    }

    @Override
    public DataView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_user_list, parent, false);
        SubUser_adapter.DataView  dataView= new SubUser_adapter.DataView (view);
        return dataView;
    }

    @Override
    public void onBindViewHolder(SubUser_adapter.DataView holder, int position)
    {
        TextView id=holder.id;
        TextView  name=holder.name;
        id.setText(records.get(position).getUId());
        name.setText(records.get(position).getUName());


    }

    @Override
    public int getItemCount() {
        return records == null ? 0 : records.size();
    }


    static class DataView extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView id, name;

        public DataView(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            // srno = (TextView) itemView.findViewById(R.id.tv_srno);
            id = (TextView) itemView.findViewById(R.id.txt_productId);
            name = (TextView) itemView.findViewById(R.id.txt_productName);


        }

        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + id.getText());
        }
    }
}

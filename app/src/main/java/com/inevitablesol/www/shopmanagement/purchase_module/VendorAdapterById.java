package com.inevitablesol.www.shopmanagement.purchase_module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.vendor_module.Record;
import com.inevitablesol.www.shopmanagement.vendor_module.View_Vendor;

import java.util.ArrayList;

/**
 * Created by Pritam on 07-07-2017.
 */

public class VendorAdapterById extends BaseAdapter
{
    private ArrayList<Record> listData;

    private View_Vendor viewVendor;
    private Context context;


    public VendorAdapterById(ArrayList<Record> listData,AddPurchaseItem addPurchaseItem)
    {
        this.listData = listData;
        this.context=addPurchaseItem;

    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        if (convertView== null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.drop_dwon, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.p_text);
        textView.setText(listData.get(position).getCompany());

        return convertView;
    }
}

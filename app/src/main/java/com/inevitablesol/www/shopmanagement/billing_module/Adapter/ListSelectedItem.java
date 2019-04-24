package com.inevitablesol.www.shopmanagement.billing_module.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.Quotation.Quotation_billing_cart;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.billing_module.MakeBillingCart;
import com.inevitablesol.www.shopmanagement.billing_module.Make_InstantBill;
import com.inevitablesol.www.shopmanagement.sql_lite.ItemDetalisClass;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;

import java.util.ArrayList;

/**
 * Created by Pritam on 21-09-2017.
 */

public class ListSelectedItem  extends ArrayAdapter<ItemDetalisClass>
{



    ArrayList<ItemDetalisClass> list = new ArrayList<>();
    private static final String TAG = "ListSelectedItem";
    MakeBillingCart makeBillingCart;


    SqlDataBase sqlDataBase;
    Make_InstantBill make_instantBill;
    Quotation_billing_cart quotation_billing_cart;



    private  Context  context;

    public  ListSelectedItem(Context context, int textViewResourceId, ArrayList<ItemDetalisClass> objects) {
        super(context, textViewResourceId, objects);
         this.context=context;
        this. makeBillingCart= (MakeBillingCart) context;
        this.list = objects;
        sqlDataBase=new SqlDataBase(context);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final ViewHolder  view ;
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.billing_selected_item, null);
            view = new ViewHolder();
            view.tv_itemname = (TextView) convertView.findViewById(R.id.tv_itemname);
            // tv_stockqty = (TextView) view.findViewById(R.id.tv_stockqty);
            view.tv_mrp = (TextView) convertView.findViewById(R.id.tv_mrp);
            view.addItem = (ImageView) convertView.findViewById(R.id.addItemForBilling);
            view.subItem = (ImageView) convertView.findViewById(R.id.subValuesForBilling);
            view.sumValue = (TextView) convertView.findViewById(R.id.txt_calValues);
           // view.sumValue.setText(String.valueOf(list.get(position).getSelectd_qty()));

            Log.d(TAG, "getView: Position"+position);
//        }
            convertView.setTag(view);
        }else
        {
            view=(ViewHolder)convertView.getTag();
            Log.d(TAG, "getView:Else"+position);
        }
        view.tv_itemname.setText(String.valueOf(list.get(position).getItem_name()));
        view.tv_mrp.setText(String.valueOf(list.get(position).getTotalPrice()));


      view.  addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            //    view.sumValue.setText("20");

             ///   Log.d(TAG, "onClick() called with: v = [" + position + "]");

                try
                {
                String item_id = list.get(position).getItem_id();
                String item_name = list.get(position).getItem_name();
                String totalPrice = list.get(position).getTotalPrice();
                String  stockQty=list.get(position).getItem_qty();
                String  dicount=list.get(position).getDiscount();
                String  gst=list.get(position).getGst_per();
                String  unit_price=list.get(position).getUnit_price();
                String purchase_price=list.get(position).getItem_purchase();
                String status=list.get(position).getStatus();
                    String changedUnit=list.get(position).getUnit();


                    String qty = view.sumValue.getText().toString().trim();
                    Log.d(TAG, "onClick:Qty"+qty);
                Log.d("qty", stockQty);
                Log.d("item",item_name);
                Log.d("item_id", item_id);
                Log.d("totalPrice", totalPrice);
                Log.d("purchase_price",purchase_price);
                Log.d("GST",gst);

                Log.d("discount",dicount);
                Log.d("UnitPrice:",unit_price);
                Log.d("Status",status);
                int quantity = Integer.parseInt(qty);
                quantity++;
                if(quantity<=Integer.parseInt(stockQty) || status.equalsIgnoreCase("infinite"))
                {
                    double totalAmont = Double.parseDouble(totalPrice) * quantity;
                    Log.d("totalAount", String.valueOf(totalAmont));
                    view.sumValue.setText(String.valueOf(quantity));
                    Log.d(TAG, "onClick:Qty"+quantity);
                   // addItem(item_id,String.valueOf(quantity),totalPrice,dicount,gst,totalAmont,unit_price);
                    addItem(item_id,String.valueOf(quantity),totalPrice,dicount,gst,totalAmont,unit_price,changedUnit);


                    getTotalQunatity();
                    notifyDataSetChanged();
                }else
                {
                    Toast.makeText(context,"Stock not Available",Toast.LENGTH_LONG).show();
                }

            }catch (Exception e)
            {
                Log.d(TAG, "onClick:Add Item",e);


            }



        }
        });

        view.subItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                String item_id =list.get(position).getItem_id();
                String p_name = list.get(position).getItem_name();
                String totalPrice =list.get(position).getTotalPrice();
                String  stockQty=list.get(position).getItem_qty();
                String  dicount =list.get(position).getDiscount();
                String  gst =list.get(position).getGst_per();
                String  unit_price= list.get(position).getUnit_price();
                String status= list.get(position).getStatus();
                Log.d("stockQty",stockQty);

                String purchase_price=list.get(position).getItem_purchase();

                String qty = view.sumValue.getText().toString().trim();
                Log.d("qty", stockQty);
                Log.d("item_id", item_id);
                Log.d("totalPrice", totalPrice);
                Log.d("purchase_price",purchase_price);
                Log.d("GST",gst);
                Log.d("discount",dicount);
                Log.d(TAG, "UnitPrice:"+unit_price);
                    String changedUnit=list.get(position).getUnit();

                String item_qty = view.sumValue.getText().toString().trim();
                int quantity = Integer.parseInt(item_qty);
                if (quantity >0 || status.equalsIgnoreCase("infinite"))
                {
                    quantity--;
                   view. sumValue.setText(String.valueOf(quantity));

                    double totalAmont=Double.parseDouble(totalPrice)*quantity;
                    Log.d("totalAount", String.valueOf(totalAmont));
                 //   addItem(item_id,String.valueOf(quantity),totalPrice,dicount,gst,totalAmont,unit_price);
                    addItem(item_id,String.valueOf(quantity),totalPrice,dicount,gst,totalAmont,unit_price,changedUnit);


                    //  sqlDataBase.updateQty(item_id, String.valueOf(quantity), String.valueOf(totalAmont));
                    getTotalQunatity();
//                         double amount=sqlDataBase.getTotalAmount();
//                         Log.d("Amount", String.valueOf(amount));


                } else
                {

                }

            }catch (Exception e)
            {
                Log.d(TAG, "onClick:Substtract",e);
            }


        }
        });

        return convertView;

    }



    private void addItem(String item_id, String qty, String totalPrice, String dicount, String gst, double totalAmont, String unitPrice, String changedUnit)
    {
        double taxableValue=totalAmont;

        String taxable_value=String.valueOf(Math.round(taxableValue*100.0)/100.00f);

        double gst_per=  Double.parseDouble(gst)/100.00f;
        double cal_gst=  taxableValue*gst_per;
        String total_gst=  String.valueOf(Math.round(cal_gst*100.0)/100.00f);
        Log.d(TAG, "addItem:ItemId"+item_id);
        Log.d(TAG, "addItem:Qty"+qty);
        Log.d(TAG, "addItem:taxableValue"+taxable_value);
        Log.d(TAG, "addItem:get_per"+gst_per);
        Log.d(TAG, "addItem:cal_gst"+total_gst);
        Log.d(TAG, "addItem:TotalAmount"+totalAmont);
        Log.d(TAG, "addItem:changedUnit"+changedUnit);


        // sqlDataBase.addSelectedItemFromBilling(item_id,qty,totalAmont,taxable_value,total_gst);
        sqlDataBase.updateQty(item_id, qty, String.valueOf(totalAmont),taxable_value,String.valueOf(cal_gst),changedUnit);
//////
//////

    }



    private void getTotalQunatity()
    {
        try
        {
            int  totalQty=sqlDataBase.getTotalQunatitiy();
            Log.d("finalQuantity", String.valueOf(totalQty));


            if(make_instantBill!=null)
            {
                make_instantBill.showQunatity(totalQty);
            }else if(quotation_billing_cart !=null)
            {
                quotation_billing_cart.showQunatity(totalQty);
            }
            else
            {
                makeBillingCart.showQunatity(totalQty);
            }

        }catch (Exception e)
        {
            Log.d(TAG, "getTotalQunatity: ",e);
        }



    }

    @Override
    public int getViewTypeCount() {
        return this.getCount() ;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolder
    {

        TextView tv_itemname, tv_stockqty, tv_mrp, tv_product_type;
        ImageView addItem, subItem;
        TextView sumValue;

    }

}

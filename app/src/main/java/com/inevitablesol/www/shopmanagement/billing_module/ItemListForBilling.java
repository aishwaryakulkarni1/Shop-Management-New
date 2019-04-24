package com.inevitablesol.www.shopmanagement.billing_module;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.Quotation.Quotation_billing_cart;
import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.measurements.Measurment_Unit;
import com.inevitablesol.www.shopmanagement.sql_lite.ItemDetalisClass;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;


import java.util.ArrayList;

/**
 * Created by Pritam on 17-05-2017.
 */

public class ItemListForBilling extends RecyclerView.Adapter<ItemListForBilling.ViewHolder>  implements AddMoreQty
{

    private ArrayList<ItemDetalisClass> listData;
    MakeBillingCart makeBillingCart;


      SqlDataBase sqlDataBase;
    Activity context;
    Make_InstantBill make_instantBill;
    Quotation_billing_cart  quotation_billing_cart;
    private static final String TAG = "ItemListForBilling";
    ArrayAdapter<CharSequence> adapter2;
    double totalAmont = 0.0;
    private double totalTaxableValue=0.0;


    public ItemListForBilling(Activity context, ArrayList<ItemDetalisClass> listData, MakeBillingCart makeBillingCart) {
         this.context=context;
        this.listData = listData;
        this.makeBillingCart = makeBillingCart;
        Log.d(TAG, "ItemListForBilling: ");
    }

    public ItemListForBilling(Activity context, ArrayList<ItemDetalisClass> listData, Make_InstantBill make_instantBill)
    {
        this.context=context;
        this.listData = listData;
        this.make_instantBill=make_instantBill;

    }

    public ItemListForBilling(Activity applicationContext, ArrayList<ItemDetalisClass> listData, Quotation_billing_cart context)
    {
        this.context=applicationContext;
        this.listData = listData;
        this.quotation_billing_cart=context;

    }



    @Override
    public ItemListForBilling.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.billing_selected_item, viewGroup, false);
           sqlDataBase=new SqlDataBase(context);
        return new ItemListForBilling.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemListForBilling.ViewHolder viewHolder, final int i)
    {


        final ItemDetalisClass list = listData.get(i);




        viewHolder.tv_itemname.setText(String.valueOf(list.getItem_name()));

        viewHolder.tv_mrp.setText(String.valueOf(list.getTotalPrice()));
        viewHolder.sumValue.setText(String.valueOf(list.getSelectd_qty()));
        viewHolder.txt_shortCut.setText(list.getShortcut());



             viewHolder.sumValue.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v)
                 {


                     Log.d(TAG, "onBindViewHolder: Updated Object "+list.toString());

                     final Dialog dialog=new Dialog(context);
                     dialog.setContentView(R.layout.add_more_qty);
                     final TextInputEditText texxtqty=(TextInputEditText)dialog.findViewById(R.id.more_qty);

                     final  TextView itemName=(TextView)dialog.findViewById(R.id.itemName);
                     final  TextView  UnitPrice=(TextView)dialog.findViewById(R.id.unitPrice);
                     final  TextView  dis_inRepees=(TextView)dialog.findViewById(R.id.dis_inRepees);
                     final TextView  discountedPrice=(TextView)dialog.findViewById(R.id.discoutedPrice);
                     final TextView  finalPrice=(TextView)dialog.findViewById(R.id.finalprice);

                     final  TextView txt_price=(TextView)dialog.findViewById(R.id.txt_price);



                     final  TextView txt_mearmentUnit=(TextView)dialog.findViewById(R.id.measurementUnit);
                     final Spinner     sp_unit=(Spinner)dialog.findViewById(R.id.sp_measurementUnit);

                      Log.d(TAG, "openDialog: Qty"+texxtqty);
                     String item_id = list.getItem_id();
                     String item_name = list.getItem_name();
                     itemName.setText(item_name);
                     UnitPrice.setText(list.getUnit_price());
                     dis_inRepees.setText(list.getDiscount());
                     discountedPrice.setText(String.valueOf(Double.parseDouble(list.getUnit_price())-Double.parseDouble(list.getDiscount())));
                     finalPrice.setText(list.getTotalPrice());
                     txt_mearmentUnit.setText(list.getUnit());

                     try {
                         final String unit=list.getmUnit();


                         if(unit.equalsIgnoreCase("Unit"))
                         {
                             adapter2  = ArrayAdapter.createFromResource(context,
                                     R.array.unit, android.R.layout.simple_spinner_item);

                         }else if(unit.equalsIgnoreCase("Gram"))
                         {
                             adapter2  = ArrayAdapter.createFromResource(context,
                                     R.array.gram, android.R.layout.simple_spinner_item);

                         }else if(unit.equalsIgnoreCase("liter"))
                         {
                             adapter2  = ArrayAdapter.createFromResource(context,
                                     R.array.liter, android.R.layout.simple_spinner_item);

                         }
                         else if(unit.equalsIgnoreCase("meter"))
                         {
                             adapter2  = ArrayAdapter.createFromResource(context,
                                     R.array.meter, android.R.layout.simple_spinner_item);

                         }
                         adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                         sp_unit.setAdapter(adapter2);
                         int position=adapter2.getPosition(list.getUnit());
                         sp_unit.setSelection(position);

                           texxtqty.addTextChangedListener(new TextWatcher()
                           {
                               @Override
                               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                               }

                               @Override
                               public void onTextChanged(CharSequence s, int start, int before, int count) {

                               }

                               @Override
                               public void afterTextChanged(Editable s)
                               {
                                   Log.d(TAG, "afterTextChanged: "+s.toString());
                                   String qty = texxtqty.getText().toString().trim();
                                 //  String unitChar = txt_mearmentUnit.getText().toString().substring(0, 1);
                                   String unitChar = txt_mearmentUnit.getText().toString();

                                   _makeAmount(sp_unit.getSelectedItem().toString(),texxtqty,qty,unitChar,txt_price);

                               }
                           });

                         sp_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                         {
                             @Override
                             public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                             {
                                 String qty = texxtqty.getText().toString().trim();
                                 String unitChar = txt_mearmentUnit.getText().toString();//;l.substring(0, 1);

                                 _makeAmount(sp_unit.getSelectedItem().toString(),texxtqty,qty,unitChar,txt_price);

                             }

                             @Override
                             public void onNothingSelected(AdapterView<?> parent) {

                             }
                         });
                     } catch (Exception e )
                     {
                         e.printStackTrace();
                     }


                     dialog.setCancelable(false);

                     AppCompatButton add=(AppCompatButton)dialog.findViewById(R.id.add_qty);
                     add.setOnClickListener(new View.OnClickListener()
                     {
                         @Override
                         public void onClick(View v) 
                         {
                             Log.d(TAG, "onClick: ");

                             try
                             {
                                 String item_id = list.getItem_id();
                                 String totalPrice = list.getTotalPrice();
                                 String  stockQty=list.getItem_qty();
                                 String  dicount=list.getDiscount();
                                 String  gst=list.getGst_per();
                                 String  unit_price=list.getUnit_price();
                                 String purchase_price=list.getItem_purchase();
                                 String status=list.getStatus();
                                 String changedUnit=sp_unit.getSelectedItem().toString().trim();

                                 String qty = texxtqty.getText().toString().trim();
                                 Log.d("qty", stockQty);

                                 Log.d("totalPrice", totalPrice);
                                 Log.d("purchase_price",purchase_price);
                                 Log.d("GST",gst);
                                 Log.d("discount",dicount);
                                 Log.d("UnitPrice:",unit_price);
                                 Log.d("Status",status);
                                 Log.d(TAG, "unit"+list.getUnit());
                                 int quantity = Integer.parseInt(qty);

                                 String  unit=sp_unit.getSelectedItem().toString();
                                 Log.d(TAG, "onClick: Unit"+unit);

//                                 quantity++;
                                 if(quantity<=Integer.parseInt(stockQty) || status.equalsIgnoreCase("infinite"))
                                 {
                                     //Double.parseDouble(totalPrice) * quantity;
                                     viewHolder.sumValue.setText(String.valueOf(quantity));

                                     try
                                     {
                                         ItemDetalisClass list = listData.get(i);
                                         list.setSelectd_qty(String.valueOf(quantity));

                                         addItem(item_id,String.valueOf(quantity),totalPrice,dicount,gst,totalAmont,unit_price,changedUnit);
                                     } catch (NumberFormatException e)
                                     {
                                         e.printStackTrace();
                                     }
                                      dialog.cancel();

                                     getTotalQunatity();
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

                     AppCompatButton cancel=(AppCompatButton)dialog.findViewById(R.id.dis_cancel);
                     cancel.setOnClickListener(new View.OnClickListener()
                     {
                         @Override
                         public void onClick(View v) {

                             dialog.dismiss();
                         }
                     });
                     dialog.show();


                 }

                 private void _makeAmount(String position, TextInputEditText texxtqty, String qty, String unitChar, TextView txt_price)
                 {
                     Log.d(TAG, "_makeAmount: ");

                     try
                     {
                         totalAmont=0.0;
                         totalTaxableValue=0.0;


                       //  String munit = adapter2.getItem(position).toString();
                         Log.d(TAG, "onItemSelected: Unit" + position);
                         Log.d(TAG, "onItemSelected: Unit" + list.toString());
                       //  String qty = texxtqty.getText().toString().trim();
                         int quantity = Integer.parseInt(qty);

                        // String unitChar = txt_mearmentUnit.getText().toString().substring(0, 1);
                        // String SubUnit = position;
                         String joinString = unitChar.concat(position);
                         double discountedPrice=Double.parseDouble(list.getUnit_price())-Double.parseDouble(list.getDiscount());
                         Log.d(TAG, "onClick: String " + joinString);
                         Log.d(TAG, "onItemSelected: DisCountedPrice "+discountedPrice);
                         totalAmont=getCalulatedAmount(joinString,Double.parseDouble(list.getTotalPrice()),quantity);
                         Log.d(TAG, "onItemSelected: totalAmount"+totalAmont);
                         totalTaxableValue=getCalulatedAmount(joinString,discountedPrice,quantity);
                         Log.d(TAG, "onItemSelected: Taxable Value"+totalTaxableValue);
                         Log.d(TAG, "onClick: " + totalAmont);
                         txt_price.setText( String.valueOf(Math.round(totalAmont * 100.0) / 100.00f));
                     }catch (Exception ee )
                     {

                     }finally
                     {


                     }
                 }
             });

        

        ((ItemListForBilling.ViewHolder) viewHolder).addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               try
               {

                   String item_id = list.getItem_id();
                   String item_name = list.getItem_name();
                   String totalPrice = list.getTotalPrice();
                   String  stockQty=list.getItem_qty();
                   String  dicount=list.getDiscount();
                   String  gst=list.getGst_per();
                   String  unit_price=list.getUnit_price();
                   String purchase_price=list.getItem_purchase();
                   String status=list.getStatus();
                   String changedUnit=list.getUnit();


                   String qty = viewHolder.sumValue.getText().toString().trim();
                   Log.d("qty", stockQty);
                   Log.d("item",item_name);
                   Log.d("item_id", item_id);
                   Log.d("totalPrice", totalPrice);
                   Log.d("purchase_price",purchase_price);
                   Log.d("GST",gst);
                   Log.d("discount",dicount);
                   Log.d("UnitPrice:",unit_price);
                   Log.d("Status",status);
                   Log.d(TAG, "onClick: Unit"+list.getUnit());
                   Log.d(TAG, "onClick: MUnit"+list.getmUnit());
                   int quantity = Integer.parseInt(qty);
                   quantity++;
                   if(quantity<=Integer.parseInt(stockQty) || status.equalsIgnoreCase("infinite"))
                   {
                       double totalAmont = Double.parseDouble(totalPrice) * quantity;
                       Log.d("totalAount", String.valueOf(totalAmont));
                       viewHolder.sumValue.setText(String.valueOf(quantity));
                       ItemDetalisClass list = listData.get(i);
                                      list.setSelectd_qty(String.valueOf(quantity));

                       totalTaxableValue=quantity*((Double.parseDouble(unit_price)-Double.parseDouble(dicount)));
                       Log.d(TAG, "onClick: Taxable Value"+totalTaxableValue);
                       Log.d(TAG, "onClick: txvalue"+quantity*((Double.parseDouble(unit_price)-Double.parseDouble(dicount))));

                       try
                       {
                           addItem(item_id,String.valueOf(quantity),totalPrice,dicount,gst,totalAmont, unit_price, changedUnit);

                       } catch (NumberFormatException e)
                       {

                           e.printStackTrace();
                       }


                       getTotalQunatity();
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

        ((ItemListForBilling.ViewHolder) viewHolder).subItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                 try
                 {

                     String item_id = list.getItem_id();
                     String p_name = list.getItem_name();
                     String totalPrice = list.getTotalPrice();
                     String  stockQty=list.getItem_qty();
                     String  dicount=list.getDiscount();
                     String  gst=list.getGst_per();
                     String  unit_price=list.getUnit_price();
                     String status=list.getStatus();
                     String changedUnit=list.getUnit();

                     Log.d("stockQty",stockQty);

                     String purchase_price= list.getItem_purchase();


                     Log.d("qty", stockQty);
                     Log.d("item_id", item_id);
                     Log.d("totalPrice", totalPrice);
                     Log.d("purchase_price",purchase_price);
                     Log.d("GST",gst);
                     Log.d("discount",dicount);
                     Log.d(TAG, "Status"+status);
                     Log.d(TAG, "UnitPrice:"+unit_price);

                     String item_qty = viewHolder.sumValue.getText().toString().trim();
                     int quantity = Integer.parseInt(item_qty);
                     if (quantity >0)
                     {
                         quantity--;
                         ((ItemListForBilling.ViewHolder) viewHolder).sumValue.setText(String.valueOf(quantity));

                         ItemDetalisClass list = listData.get(i);
                         list.setSelectd_qty(String.valueOf(quantity));

                         double totalAmont=Double.parseDouble(totalPrice)*quantity;
                         Log.d("totalAount", String.valueOf(totalAmont));
                         totalTaxableValue=quantity*((Double.parseDouble(unit_price)-Double.parseDouble(dicount)));
                         addItem(item_id,String.valueOf(quantity),totalPrice,dicount,gst,totalAmont, unit_price, changedUnit);
                       //  sqlDataBase.updateQty(item_id, String.valueOf(quantity), String.valueOf(totalAmont));
                         getTotalQunatity();
//                         double amount=sqlDataBase.getTotalAmount();
//                         Log.d("Amount", String.valueOf(amount));


                     } else
                     {
                         Toast.makeText(context, "Invalid Quantity", Toast.LENGTH_SHORT).show();

                     }

                 }catch (Exception e)
                 {
                     Log.d(TAG, "onClick:Substract",e);
                 }



            }

        });
        int totalQuantity = Integer.parseInt(viewHolder.sumValue.getText().toString().trim());
        Log.d("totalQuantity", String.valueOf(totalQuantity));


    }




    private void addItem(String item_id, String qty, String totalPrice, String dicount, String gst, double totalAmont, String unitPrice, String changedUnit) throws NumberFormatException
    {
      //  double taxableValue=Double.parseDouble(qty)*((Double.parseDouble(unit_price)-Double.parseDouble(dicount)));
        Log.d(TAG, "addItem: TotalTaxableVAlue"+totalTaxableValue);
        double taxableValue=totalTaxableValue;

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
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public void set(ItemDetalisClass itemDetalisClass)
    {
        Log.d(TAG, "set: "+itemDetalisClass.toString());

    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_itemname, tv_stockqty, tv_mrp, tv_product_type;
        ImageView addItem, subItem;
        TextView sumValue;
        TextView txt_shortCut;

        public ViewHolder(View view)
        {
            super(view);

            tv_itemname = (TextView) view.findViewById(R.id.tv_itemname);
            // tv_stockqty = (TextView) view.findViewById(R.id.tv_stockqty);
            tv_mrp = (TextView) view.findViewById(R.id.tv_mrp);
            addItem = (ImageView) view.findViewById(R.id.addItemForBilling);
            subItem = (ImageView) view.findViewById(R.id.subValuesForBilling);
            sumValue = (TextView) view.findViewById(R.id.txt_calValues);
            txt_shortCut=(TextView)view.findViewById(R.id.tv_shortcut);



//           floatingActionButton=(FloatingActionButton)view.findViewById(R.id.addQty);


        }
    }



    public  double   getCalulatedAmount(String joinString ,double amount,int quantity)
    {
        double mtotalAmont=0.0;
        try {
            switch (joinString)
            {
                case "Unit (Un)Unit (Un)":
                    mtotalAmont = Measurment_Unit.unitTounit(amount, quantity);
                    Log.d(TAG, "onClick:UU " + mtotalAmont);
                    break;

                case "Unit (Un)Nos (n)":
                    mtotalAmont = Measurment_Unit.unitTonos(amount, quantity);
                    Log.d(TAG, "onClick:UN " + mtotalAmont);
                    break;

                case "Unit (Un)Pieces (p)":
                    mtotalAmont = Measurment_Unit.unitTopeice(amount, quantity);
                    Log.d(TAG, "onClick:UP " + mtotalAmont);
                    break;

                case "Unit (Un)Full (f)":
                    mtotalAmont = Measurment_Unit.unitTofull(amount, quantity);
                    Log.d(TAG, "onClick:UF " + mtotalAmont);
                    break;

                case "Unit (Un)Half (h)":
                    mtotalAmont = Measurment_Unit.unitTohalf(amount, quantity);
                    Log.d(TAG, "onClick:UH" + mtotalAmont);
                    break;

                case "Nos (n)Unit (Un)":
                    mtotalAmont = Measurment_Unit.nosTounit(amount, quantity);
                    Log.d(TAG, "onClick:NU " + mtotalAmont);
                    break;

                case "Nos (n)Nos (n)":
                    mtotalAmont = Measurment_Unit.nosTonos(amount, quantity);
                    Log.d(TAG, "onClick:NN " + mtotalAmont);
                    break;

                case "Nos (p)Pieces (p)":
                    mtotalAmont = Measurment_Unit.nosTopeice(amount, quantity);
                    Log.d(TAG, "onClick:NP " + mtotalAmont);
                    break;

                case "Nos (n)Full (f)":
                    mtotalAmont = Measurment_Unit.nosTofull(amount, quantity);
                    Log.d(TAG, "onClick:NF " + mtotalAmont);
                    break;

                 case "Nos (n)Half (h)":
                    mtotalAmont = Measurment_Unit.nosTohalf(amount, quantity);
                    Log.d(TAG, "onClick:NH" + mtotalAmont);
                    break;

                 case "Pieces (p)Unit (Un)":
                    mtotalAmont = Measurment_Unit.peiceTounit(amount, quantity);
                    Log.d(TAG, "onClick:PU " + mtotalAmont);
                    break;

                case "Pieces (p)Nos (n)":
                    mtotalAmont = Measurment_Unit.peiceTonos(amount, quantity);
                    Log.d(TAG, "onClick:PN " + mtotalAmont);
                    break;

                case "Pieces (p)Pieces (p)":
                    mtotalAmont = Measurment_Unit.peiceTopeice(amount, quantity);
                    Log.d(TAG, "onClick:PP " + mtotalAmont);
                    break;

                case "Pieces (p)Full (f)":
                    mtotalAmont = Measurment_Unit.peiceTofull(amount, quantity);
                    Log.d(TAG, "onClick:PF " + mtotalAmont);
                    break;

                case "Pieces (p)Half (h)":
                    mtotalAmont = Measurment_Unit.peiceTohalf(amount, quantity);
                    Log.d(TAG, "onClick:PH" + mtotalAmont);
                    break;

                case "Full (f)Unit (Un)":
                    mtotalAmont = Measurment_Unit.fullTounit(amount, quantity);
                    Log.d(TAG, "onClick:FU " + mtotalAmont);
                    break;

                case "Full (f)Nos (n)":
                    mtotalAmont = Measurment_Unit.fullTonos(amount, quantity);
                    Log.d(TAG, "onClick:FN " + mtotalAmont);
                    break;

                case "Full (f)Pieces (p)":
                    mtotalAmont = Measurment_Unit.fullTopeice(amount, quantity);
                    Log.d(TAG, "onClick:FP " + mtotalAmont);
                    break;

                case "Full (f)Full (f)":
                    mtotalAmont = Measurment_Unit.fullTofull(amount, quantity);
                    Log.d(TAG, "onClick:FF " + mtotalAmont);
                    break;

                case "Full (f)Half (h)":
                    mtotalAmont = Measurment_Unit.fullTohalf(amount, quantity);
                    Log.d(TAG, "onClick:FH" + mtotalAmont);
                    break;

                case "Half (h)Unit (Un)":
                    mtotalAmont = Measurment_Unit.halfTounit(amount, quantity);
                    Log.d(TAG, "onClick:HU " + mtotalAmont);
                    break;

                case "Half (h)Nos (n)":
                    mtotalAmont = Measurment_Unit.halfTonos(amount, quantity);
                    Log.d(TAG, "onClick:HN " + mtotalAmont);
                    break;

                case "Half(h)Pieces (p)":
                    mtotalAmont = Measurment_Unit.halfTopeice(amount, quantity);
                    Log.d(TAG, "onClick:HP " + mtotalAmont);
                    break;

                case "Half (h)Full (f)":
                    mtotalAmont = Measurment_Unit.halfTofull(amount, quantity);
                    Log.d(TAG, "onClick:HF " + mtotalAmont);
                    break;

                case "Half (h)Half (h)":
                    mtotalAmont = Measurment_Unit.halfTohalf(amount, quantity);
                    Log.d(TAG, "onClick:HH" + mtotalAmont);
                    break;

                case "Feet (ft)Milimeter (mm)":
                    mtotalAmont = Measurment_Unit.feetTomillimeter(amount, quantity);
                    Log.d(TAG, "onClick:FMM " + mtotalAmont);
                    break;

                case "Feet (ft)Feet (ft)":
                    mtotalAmont = Measurment_Unit.feetTofeet(amount, quantity);
                    Log.d(TAG, "onClick:FF " + mtotalAmont);
                    break;

                case "Feet (ft)Centimeter (cm)":
                    mtotalAmont = Measurment_Unit.feetTocentimeter(amount, quantity);
                    Log.d(TAG, "onClick:FC " + mtotalAmont);
                    break;

                case "Feet (ft)Inch (in)":
                    mtotalAmont = Measurment_Unit.feetToinch(amount, quantity);
                    Log.d(TAG, "onClick:FF " + mtotalAmont);
                    break;

                case "Feet (ft)Meter (m)":
                    mtotalAmont = Measurment_Unit.feetTometer(amount, quantity);
                    Log.d(TAG, "onClick:FM " + mtotalAmont);
                    break;

                case "Feet (ft)KiloMeter (km)":
                    mtotalAmont = Measurment_Unit.feetTokilometer(amount, quantity);
                    Log.d(TAG, "onClick:FK " + mtotalAmont);
                    break;

                case "Milimeter (mm)Milimeter (mm)":
                    mtotalAmont = Measurment_Unit.millimeterTomillimeter(amount, quantity);
                    Log.d(TAG, "onClick:MMMM " + mtotalAmont);
                    break;

                case "Milimeter (mm)Feet (ft)":
                    mtotalAmont = Measurment_Unit.millimeterTofeet(amount, quantity);
                    Log.d(TAG, "onClick:MMF " + mtotalAmont);
                    break;

                case "Milimeter (mm)Centimeter (cm)":
                    mtotalAmont = Measurment_Unit.millimeterTocentimeter(amount, quantity);
                    Log.d(TAG, "onClick:MMC " + mtotalAmont);
                    break;

                case "Milimeter (mm)Inch (in)":
                    mtotalAmont = Measurment_Unit.millimeterToinch(amount, quantity);
                    Log.d(TAG, "onClick:MMF " + mtotalAmont);
                    break;

                case "Milimeter (mm)Meter (m)":
                    mtotalAmont = Measurment_Unit.millimeterTometer(amount, quantity);
                    Log.d(TAG, "onClick:MMM " + mtotalAmont);
                    break;

                case "Milimeter (mm)KiloMeter (km)":
                    mtotalAmont = Measurment_Unit.millimeterTokilometer(amount, quantity);
                    Log.d(TAG, "onClick:MMK " + mtotalAmont);
                    break;

                case "Centimeter (cm)Milimeter (mm)":
                    mtotalAmont = Measurment_Unit.centimeterTomillimeter(amount, quantity);
                    Log.d(TAG, "onClick:CMM " + mtotalAmont);
                    break;

                case "Centimeter (cm)Feet (ft)":
                    mtotalAmont = Measurment_Unit.centimeterTofeet(amount, quantity);
                    Log.d(TAG, "onClick:CF " + mtotalAmont);
                    break;

                case "Centimeter (cm)Centimeter (cm)":
                    mtotalAmont = Measurment_Unit.centimeterTocentimeter(amount, quantity);
                    Log.d(TAG, "onClick:CC " + mtotalAmont);
                    break;

                case "Centimeter (cm)Inch (in)":
                    mtotalAmont = Measurment_Unit.centimeterToinch(amount, quantity);
                    Log.d(TAG, "onClick:CF " + mtotalAmont);
                    break;
                case "Centimeter (cm)Meter (m)":
                    mtotalAmont = Measurment_Unit.centimeterTometer(amount, quantity);
                    Log.d(TAG, "onClick:CM " + mtotalAmont);
                    break;

                case "Centimeter (cm)KiloMeter (km)":
                    mtotalAmont = Measurment_Unit.centimeterTokilometer(amount, quantity);
                    Log.d(TAG, "onClick:CK " + mtotalAmont);
                    break;

                case "Inch (in)Milimeter (mm)":
                    mtotalAmont = Measurment_Unit.inchTomillimeter(amount, quantity);
                    Log.d(TAG, "onClick:IMM " + mtotalAmont);
                    break;

                case "Inch (in)Feet (ft)":
                    mtotalAmont = Measurment_Unit.inchTofeet(amount, quantity);
                    Log.d(TAG, "onClick:IF " + mtotalAmont);
                    break;

                case "Inch (in)Centimeter (cm)":
                    mtotalAmont = Measurment_Unit.inchTocentimeter(amount, quantity);
                    Log.d(TAG, "onClick:IC " + mtotalAmont);
                    break;

                case "Inch (in)Inch (in)":
                    mtotalAmont = Measurment_Unit.inchToinch(amount, quantity);
                    Log.d(TAG, "onClick:IF " + mtotalAmont);
                    break;

                case "Inch (in)Meter (m)":
                    mtotalAmont = Measurment_Unit.inchTometer(amount, quantity);
                    Log.d(TAG, "onClick:IM " + mtotalAmont);
                    break;

                case "Inch (in)KiloMeter (km)":
                    mtotalAmont = Measurment_Unit.inchTokilometer(amount, quantity);
                    Log.d(TAG, "onClick:IK " + mtotalAmont);
                    break;

                case "Meter (m)Milimeter (mm)":
                    mtotalAmont = Measurment_Unit.meterTomillimeter(amount, quantity);
                    Log.d(TAG, "onClick:MMM " + mtotalAmont);
                    break;
                case "Meter (m)Centimeter (cm)":
                    mtotalAmont = Measurment_Unit.meterTocentimeter(amount, quantity);
                    Log.d(TAG, "onClick:MC " + mtotalAmont);
                    break;

                case "Meter (m)Inch (in)":
                    mtotalAmont = Measurment_Unit.meterToinch(amount, quantity);
                    Log.d(TAG, "onClick:MF " + mtotalAmont);
                    break;

                case "Meter (m)Meter (m)":
                    mtotalAmont = Measurment_Unit.meterTometer(amount, quantity);
                    Log.d(TAG, "onClick:MM " + mtotalAmont);
                    break;

                case "Meter (m)KiloMeter (km)":
                    mtotalAmont = Measurment_Unit.meterTokilometer(amount, quantity);
                    Log.d(TAG, "onClick:MK " + mtotalAmont);
                    break;

                case "KiloMeter (km)Milimeter (mm)":
                    mtotalAmont = Measurment_Unit.kilometerTomillimeter(amount, quantity);
                    Log.d(TAG, "onClick:KMM " + mtotalAmont);
                    break;

                case "Meter (m)Feet (ft)":
                    mtotalAmont = Measurment_Unit.meterTofeet(amount, quantity);
                    Log.d(TAG, "onClick:MF " + mtotalAmont);
                    break;

                case "KiloMeter (km)Feet (ft)":
                    mtotalAmont = Measurment_Unit.kilometerTofeet(amount, quantity);
                    Log.d(TAG, "onClick:KC " + mtotalAmont);
                    break;

                case "KiloMeter (km)Centimeter (cm)":
                    mtotalAmont = Measurment_Unit.kilometerTocentimeter(amount, quantity);
                    Log.d(TAG, "onClick:KC " + mtotalAmont);
                    break;

                case "KiloMeter (km)Inch (in)":
                    mtotalAmont = Measurment_Unit.kilometerToinch(amount, quantity);
                    Log.d(TAG, "onClick:KF " + mtotalAmont);
                    break;

                case "KiloMeter (km)KiloMeter (km)":
                    mtotalAmont = Measurment_Unit.kilometerTokilometer(amount, quantity);
                    Log.d(TAG, "onClick:KM " + mtotalAmont);
                    break;

                case "KiloMeter (km)Meter (m)":
                    mtotalAmont = Measurment_Unit.kilometerTometer(amount, quantity);
                    Log.d(TAG, "onClick:KK " + mtotalAmont);
                    break;

                case "Milligram (gm)Milligram (mg)":
                    mtotalAmont = Measurment_Unit.milligramTomilligram(amount, quantity);
                    Log.d(TAG, "onClick:MM " + mtotalAmont);
                    break;

                case "Milligram (gm)Gram (gm)":
                    mtotalAmont = Measurment_Unit.milligramTogram(amount, quantity);
                    Log.d(TAG, "onClick:MG " + mtotalAmont);
                    break;

                case "Milligram (gm)Kilogram (kg)":
                    mtotalAmont = Measurment_Unit.milligramTokilogram(amount, quantity);
                    Log.d(TAG, "onClick:MK " + mtotalAmont);
                    break;

                case "Milligram (gm)Quintal (ql)":
                    mtotalAmont = Measurment_Unit.milligramToquintal(amount, quantity);
                    Log.d(TAG, "onClick:MQ " + mtotalAmont);
                    break;

                case "Milligram (gm)Tonne (t)":
                    mtotalAmont = Measurment_Unit.milligramTotonne(amount, quantity);
                    Log.d(TAG, "onClick:MM " + mtotalAmont);
                    break;

                case "Gram (gm)Milligram (mg)":
                    mtotalAmont = Measurment_Unit.gramTomilligram(amount, quantity);
                    Log.d(TAG, "onClick:GM " + mtotalAmont);
                    break;

                case "Gram (gm)Gram (gm)":
                    mtotalAmont = Measurment_Unit.gramTogram(amount, quantity);
                    Log.d(TAG, "onClick:GG " + mtotalAmont);
                    break;

                case "Gram (gm)Kilogram (kg)":
                    mtotalAmont = Measurment_Unit.gramTokilogram(amount, quantity);
                    Log.d(TAG, "onClick:GK " + mtotalAmont);
                    break;

                case "Gram (gm)Quintal (ql)":
                    mtotalAmont = Measurment_Unit.gramToquintal(amount, quantity);
                    Log.d(TAG, "onClick:GQ " + mtotalAmont);
                    break;

                case "Gram (gm)Tonne (t)":
                    mtotalAmont = Measurment_Unit.gramTotonne(amount, quantity);
                    Log.d(TAG, "onClick:GM " + mtotalAmont);
                    break;

                case "Kilogram )kg)Milligram (mg)":
                    mtotalAmont = Measurment_Unit.kilogramTomilligram(amount, quantity);
                    Log.d(TAG, "onClick:KM " + mtotalAmont);
                    break;

                case "Kilogram (kg)Kilogram (kg)":
                    mtotalAmont = Measurment_Unit.kilogramTokilogram(amount, quantity);
                    Log.d(TAG, "onClick:KG " + mtotalAmont);
                    break;

                case "Kilogram (kg)Gram (gm)":
                    mtotalAmont = Measurment_Unit.kilogramTogram(amount, quantity);
                    Log.d(TAG, "onClick:KG " + mtotalAmont);
                    break;



                case "Kilogram (kg)Quintal (ql)":
                    mtotalAmont = Measurment_Unit.kilogramToquintal(amount, quantity);
                    Log.d(TAG, "onClick:KQ " + mtotalAmont);
                    break;

                case "Kilogram (kg)Tonne (t)":
                    mtotalAmont = Measurment_Unit.kilogramTotonne(amount, quantity);
                    Log.d(TAG, "onClick:KM " + mtotalAmont);
                    break;

                case "Quintal (ql)Milligram (mg)":
                    mtotalAmont = Measurment_Unit.quintalTomilligram(amount, quantity);
                    Log.d(TAG, "onClick: QM" + mtotalAmont);
                    break;

                case "Quintal (ql)Gram (gm)":
                    mtotalAmont = Measurment_Unit.quintalTogram(amount, quantity);
                    Log.d(TAG, "onClick: QG" + mtotalAmont);
                    break;

                case "Quintal (ql)Kilogram (kg)":
                    mtotalAmont = Measurment_Unit.quintalTokilogram(amount, quantity);
                    Log.d(TAG, "onClick:QK " + mtotalAmont);
                    break;

                case "Quintal (ql)Quintal (ql)":
                    mtotalAmont = Measurment_Unit.quintalToquintal(amount, quantity);
                    Log.d(TAG, "onClick: QQ" + mtotalAmont);
                    break;

                case "Quintal (ql)Tonne (t)":
                    mtotalAmont = Measurment_Unit.quintalTotonne(amount, quantity);
                    Log.d(TAG, "onClick:QT " + mtotalAmont);
                    break;

                case "Tonne (t)Milligram (mg)":
                    mtotalAmont = Measurment_Unit.tonneTomilligram(amount, quantity);
                    Log.d(TAG, "onClick: TM" + mtotalAmont);
                    break;

                case "Tonne (t)Gram (gm)":
                    mtotalAmont = Measurment_Unit.tonneTogram(amount, quantity);
                    Log.d(TAG, "onClick: TG" + mtotalAmont);
                    break;
                case "Tonne (t)Kilogram (kg)":
                    mtotalAmont = Measurment_Unit.tonneTokilogram(amount, quantity);
                    Log.d(TAG, "onClick:TK " + mtotalAmont);
                    break;
                case "Tonne (t)Quintal (ql)":
                    mtotalAmont = Measurment_Unit.tonneToquintal(amount, quantity);
                    Log.d(TAG, "onClick: TQ" + mtotalAmont);
                    break;

                case "Tonne (t)Tonne (t)":
                    mtotalAmont = Measurment_Unit.tonneTotonne(amount, quantity);
                    Log.d(TAG, "onClick:TT " + mtotalAmont);
                    break;

                case "Mililiter (ml)Mililiter (ml)":
                    mtotalAmont = Measurment_Unit.milliliterTomilileter(amount, quantity);
                    Log.d(TAG, "onClick:MM " + mtotalAmont);
                    break;

                case "Mililiter (ml)Liter(l)":
                    mtotalAmont = Measurment_Unit.milliliterToliter(amount, quantity);
                    Log.d(TAG, "onClick:ML " + mtotalAmont);
                    break;

                case "Mililiter (ml)Kiloliter (kl)":
                    mtotalAmont = Measurment_Unit.milliliterTokiloliter(amount, quantity);
                    Log.d(TAG, "onClick:MK " + mtotalAmont);
                    break;

                case "Liter(l)Mililiter (ml)":
                    mtotalAmont = Measurment_Unit.literTomilileter(amount, quantity);
                    Log.d(TAG, "onClick:LM " + mtotalAmont);
                    break;

                case "Liter(l)Liter(l)":
                    mtotalAmont = Measurment_Unit.literToliter(amount, quantity);
                    Log.d(TAG, "onClick:LL " + mtotalAmont);
                    break;

                case "Liter(l)Kiloliter(kl)":
                    mtotalAmont = Measurment_Unit.literTokiloliter(amount, quantity);
                    Log.d(TAG, "onClick:LK " + mtotalAmont);
                    break;

                case "Kiloliter (kl)Mililiter (ml)":
                    mtotalAmont = Measurment_Unit.kiloliterTomilileter(amount, quantity);
                    Log.d(TAG, "onClick:KM " + mtotalAmont);
                    break;
                case "Kiloliter (kl)Liter(l)":
                    mtotalAmont = Measurment_Unit.kiloliterToliter(amount, quantity);
                    Log.d(TAG, "onClick:KL " + mtotalAmont);
                    break;
                case "Kiloliter (kl)Kiloliter(kl)":
                    mtotalAmont = Measurment_Unit.kiloliterTokiloliter(amount, quantity);
                    Log.d(TAG, "onClick:KK " + mtotalAmont);
                    break;

                default:
                    Toast.makeText(context, "Wrong Choice", Toast.LENGTH_SHORT).show();


            }
            Log.d(TAG, "onClick: " + mtotalAmont);

        } catch (Exception ee) {

        } finally {


        }

        return mtotalAmont;
    }



}
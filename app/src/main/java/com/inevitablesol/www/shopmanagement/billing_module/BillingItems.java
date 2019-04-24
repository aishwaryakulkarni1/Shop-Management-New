package com.inevitablesol.www.shopmanagement.billing_module;

/**
 * Created by Pritam on 27-07-2017.
 */

public class BillingItems
{

     private  String id;
     private  String p_id;
    private  String item_id;
    private  String item_name;
    private  String stock_qty;
    private  String selected_qty;
    private  String item_mrp;
    private  String purchase_price;
    private  String unit_price;
    private  String total_price;
    private  String item_gst;
    private  String discount;
    private  String total_Calculated_price;
    private  String taxablePrice;
    private  String total_cal_gst;
    private  String hsnCode;
    private  String status;

    private  String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getmUnit() {
        return mUnit;
    }

    public void setmUnit(String mUnit) {
        this.mUnit = mUnit;
    }

    public String getItembarcode() {
        return itembarcode;
    }

    public void setItembarcode(String itembarcode) {
        this.itembarcode = itembarcode;
    }

    private  String mUnit;
    private  String itembarcode;

    public String getChangedUnit() {
        return changedUnit;
    }

    public void setChangedUnit(String changedUnit) {
        this.changedUnit = changedUnit;
    }

    private  String changedUnit;

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    private  String shortcut;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHsnCode()
    {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getStock_qty() {
        return stock_qty;
    }

    public void setStock_qty(String stock_qty) {
        this.stock_qty = stock_qty;
    }

    public String getSelected_qty() {
        return selected_qty;
    }

    public void setSelected_qty(String selected_qty) {
        this.selected_qty = selected_qty;
    }

    public String getItem_mrp() {
        return item_mrp;
    }

    public void setItem_mrp(String item_mrp) {
        this.item_mrp = item_mrp;
    }

    public String getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(String purchase_price) {
        this.purchase_price = purchase_price;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getItem_gst() {
        return item_gst;
    }

    public void setItem_gst(String item_gst) {
        this.item_gst = item_gst;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotal_Calculated_price() {
        return total_Calculated_price;
    }

    public void setTotal_Calculated_price(String total_Calculated_price) {
        this.total_Calculated_price = total_Calculated_price;
    }

    public String getTaxablePrice() {
        return taxablePrice;
    }

    public void setTaxablePrice(String taxablePrice) {
        this.taxablePrice = taxablePrice;
    }

    public String getTotal_cal_gst() {
        return total_cal_gst;
    }

    public void setTotal_cal_gst(String total_cal_gst) {
        this.total_cal_gst = total_cal_gst;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "BillingItems{" +
                "id='" + id + '\'' +
                ", p_id='" + p_id + '\'' +
                ", item_id='" + item_id + '\'' +
                ", item_name='" + item_name + '\'' +
                ", stock_qty='" + stock_qty + '\'' +
                ", selected_qty='" + selected_qty + '\'' +
                ", item_mrp='" + item_mrp + '\'' +
                ", purchase_price='" + purchase_price + '\'' +
                ", unit_price='" + unit_price + '\'' +
                ", total_price='" + total_price + '\'' +
                ", item_gst='" + item_gst + '\'' +
                ", discount='" + discount + '\'' +
                ", total_Calculated_price='" + total_Calculated_price + '\'' +
                ", taxablePrice='" + taxablePrice + '\'' +
                ", total_cal_gst='" + total_cal_gst + '\'' +
                '}';
    }


}

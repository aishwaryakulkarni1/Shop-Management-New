package com.inevitablesol.www.shopmanagement.sql_lite;

/**
 * Created by Pritam on 17-05-2017.
 */

public class ItemDetalisClass
{
    private  String product_id;
     private  String item_name;
     private   String mrp;
     private  String  selectd_qty;
    private  String  item_id;
    private  String item_qty;
    private  String item_purchase;

    private  String total_calculared_price;
    private  String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getmUnit() {
        return mUnit;
    }

    public void setmUnit(String mUnit) {
        this.mUnit = mUnit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    private  String  mUnit;
    private  String  unit;

    public String getChnagedUnit() {
        return chnagedUnit;
    }

    public void setChnagedUnit(String chnagedUnit) {
        this.chnagedUnit = chnagedUnit;
    }

    private  String chnagedUnit;
    private  String   itemBarcode;

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    private String shortcut;

    @Override
    public String toString() {
        return "ItemDetalisClass{" +
                "product_id='" + product_id + '\'' +
                ", item_name='" + item_name + '\'' +
                ", mrp='" + mrp + '\'' +
                ", selectd_qty='" + selectd_qty + '\'' +
                ", item_id='" + item_id + '\'' +
                ", item_qty='" + item_qty + '\'' +
                ", item_purchase='" + item_purchase + '\'' +
                ", total_calculared_price='" + total_calculared_price + '\'' +
                ", status='" + status + '\'' +
                ", mUnit='" + mUnit + '\'' +
                ", unit='" + unit + '\'' +
                ", chnagedUnit='" + chnagedUnit + '\'' +
                ", itemBarcode='" + itemBarcode + '\'' +
                ", shortcut='" + shortcut + '\'' +
                ", calculated_gst='" + calculated_gst + '\'' +
                ", total_taxableValue='" + total_taxableValue + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", unit_price='" + unit_price + '\'' +
                ", gst_per='" + gst_per + '\'' +
                ", discount='" + discount + '\'' +
                ", gstOnitem='" + gstOnitem + '\'' +
                '}';
    }

    private  String calculated_gst;
    private  String  total_taxableValue;

    public String getTotal_calculared_price() {
        return total_calculared_price;
    }

    public void setTotal_calculared_price(String total_calculared_price) {
        this.total_calculared_price = total_calculared_price;
    }

    public String getCalculated_gst() {
        return calculated_gst;
    }

    public void setCalculated_gst(String calculated_gst) {
        this.calculated_gst = calculated_gst;
    }

    public String getTotal_taxableValue() {
        return total_taxableValue;
    }

    public void setTotal_taxableValue(String total_taxableValue) {
        this.total_taxableValue = total_taxableValue;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getItem_purchase() {
        return item_purchase;
    }

    public void setItem_purchase(String item_purchase) {
        this.item_purchase = item_purchase;
    }

    private  String totalPrice;
    private   String unit_price;

    public String getGst_per() {
        return gst_per;
    }

    public void setGst_per(String gst_per) {
        this.gst_per = gst_per;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getGstOnitem() {
        return gstOnitem;
    }

    public void setGstOnitem(String gstOnitem) {
        this.gstOnitem = gstOnitem;
    }

    private String  gst_per;
    private  String  discount;
    private  String  gstOnitem;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(String item_qty) {
        this.item_qty = item_qty;
    }


    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getSelectd_qty() {
        return selectd_qty;
    }

    public void setSelectd_qty(String selectd_qty) {
        this.selectd_qty = selectd_qty;
    }


}

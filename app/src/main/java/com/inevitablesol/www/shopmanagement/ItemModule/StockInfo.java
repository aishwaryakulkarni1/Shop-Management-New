package com.inevitablesol.www.shopmanagement.ItemModule;

import java.io.Serializable;

/**
 * Created by Anup on 22-02-2017.
 */

public class StockInfo implements Serializable
{
    public int item_id;
    public String item_name;
    public String company;
    public String owner;
    public String specification;
    public String product_type;
    public String product_id;
    public String hsn_ssc_code;
    public String storage_qty ="0";
    public String stock_qty="0";
    public String original_price="0";
    public String mrp="0";

    private  String totalPrice="0";
    private  String unitPrice="0";
    private  String  discount="0";
    private  String gst="0";


    public String getMunit() {
        return munit;
    }

    public void setMunit(String munit) {
        this.munit = munit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getItembarcode() {
        return itembarcode;
    }

    public void setItembarcode(String itembarcode) {
        this.itembarcode = itembarcode;
    }

    private  String munit;
    private  String unit;
    private String  itembarcode ="0";

    public String getO_mrp() {
        return o_mrp;
    }

    public void setO_mrp(String o_mrp) {
        this.o_mrp = o_mrp;
    }

    public String getO_price() {
        return o_price;
    }

    public void setO_price(String o_price) {
        this.o_price = o_price;
    }

    public String getO_gst() {
        return o_gst;
    }

    public void setO_gst(String o_gst) {
        this.o_gst = o_gst;
    }


    @Override
    public String toString() {
        return "StockInfo{" +
                "item_id=" + item_id +
                ", item_name='" + item_name + '\'' +
                ", company='" + company + '\'' +
                ", owner='" + owner + '\'' +
                ", specification='" + specification + '\'' +
                ", product_type='" + product_type + '\'' +
                ", product_id='" + product_id + '\'' +
                ", hsn_ssc_code='" + hsn_ssc_code + '\'' +
                ", storage_qty='" + storage_qty + '\'' +
                ", stock_qty='" + stock_qty + '\'' +
                ", original_price='" + original_price + '\'' +
                ", mrp='" + mrp + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", discount='" + discount + '\'' +
                ", gst='" + gst + '\'' +
                ", munit='" + munit + '\'' +
                ", unit='" + unit + '\'' +
                ", itembarcode='" + itembarcode + '\'' +
                ", o_price='" + o_price + '\'' +
                ", o_gst='" + o_gst + '\'' +
                ", o_mrp='" + o_mrp + '\'' +
                ", disPrice='" + disPrice + '\'' +
                ", disCountPer='" + disCountPer + '\'' +
                ", selectedQuantity='" + selectedQuantity + '\'' +
                '}';
    }

    private  String o_price="0";
    private  String  o_gst="0";
    private  String o_mrp="0";

    public String getDisPrice() {
        return disPrice;
    }

    public void setDisPrice(String disPrice) {
        this.disPrice = disPrice;
    }

    public String getDisCountPer() {
        return disCountPer;
    }

    public void setDisCountPer(String disCountPer) {
        this.disCountPer = disCountPer;
    }

    private  String disPrice="0";
    private  String disCountPer="0";

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    private  String selectedQuantity;

    public String getStorage_qty() {
        return storage_qty;
    }

    public String getSelectedQuantity()
    {
        return selectedQuantity;
    }

    public void setSelectedQuantity(String selectedQuantity)
    {
        this.selectedQuantity = selectedQuantity;
    }

    public int getItem_id() {
        return item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getProduct_type() {
        return product_type;
    }

    public String getCompany() {
        return company;
    }

    public String getOwner() {
        return owner;
    }

    public String getStock_qty() {
        return stock_qty;
    }

    public String getMrp() {
        return mrp;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public String getSpecification() {
        return specification;
    }

    public String getHsn_ssc_code() {
        return hsn_ssc_code;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setStorage_qty(String storage_qty) {
        this.storage_qty = storage_qty;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setStock_qty(String stock_qty) {
        this.stock_qty = stock_qty;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public void setHsn_ssc_code(String hsn_ssc_code) {
        this.hsn_ssc_code = hsn_ssc_code;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

}

package com.inevitablesol.www.shopmanagement.purchase_module;

/**
 * Created by Pritam on 21-07-2017.
 */

public class SelectedItemClass
{
    private  String itemName;
    private  String itemId;
    private  String itemPrice;
    private  String itemUnitPrice;
    private  String itemGst;
    private  String itemQty;
    private   boolean isChecked;
    private  String Org_price;
    private  String mrp;
    private  String  dicount;
    private  String GST_price;

    public String getP_mrp() {
        return p_mrp;
    }

    public void setP_mrp(String p_mrp) {
        this.p_mrp = p_mrp;
    }

    private  String p_mrp;

    public String getP_orgPrice() {
        return p_orgPrice;
    }

    public void setP_orgPrice(String p_orgPrice) {
        this.p_orgPrice = p_orgPrice;
    }

    public String getP_gst() {
        return p_gst;
    }

    public void setP_gst(String p_gst) {
        this.p_gst = p_gst;
    }

    private  String p_orgPrice;
    private  String  p_gst;

    public String getGST_price() {
        return GST_price;
    }

    public void setGST_price(String GST_price) {
        this.GST_price = GST_price;
    }

    public String getOrg_price() {
        return Org_price;
    }

    public void setOrg_price(String org_price) {
        Org_price = org_price;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getDicount() {
        return dicount;
    }

    public void setDicount(String dicount) {
        this.dicount = dicount;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemUnitPrice() {
        return itemUnitPrice;
    }

    public void setItemUnitPrice(String itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
    }

    public String getItemGst() {
        return itemGst;
    }

    public void setItemGst(String itemGst) {
        this.itemGst = itemGst;
    }

    @Override
    public String toString() {
        return "SelectedItemClass{" +
                "itemName='" + itemName + '\'' +
                ", itemId='" + itemId + '\'' +
                ", itemPrice='" + itemPrice + '\'' +
                ", itemUnitPrice='" + itemUnitPrice + '\'' +
                ", itemGst='" + itemGst + '\'' +
                ", itemQty='" + itemQty + '\'' +
                ", isChecked=" + isChecked +
                ", Org_price='" + Org_price + '\'' +
                ", mrp='" + mrp + '\'' +
                ", dicount='" + dicount + '\'' +
                ", GST_price='" + GST_price + '\'' +
                ", p_mrp='" + p_mrp + '\'' +
                ", p_orgPrice='" + p_orgPrice + '\'' +
                ", p_gst='" + p_gst + '\'' +
                '}';
    }
}

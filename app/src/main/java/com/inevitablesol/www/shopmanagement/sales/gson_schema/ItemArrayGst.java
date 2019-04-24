
package com.inevitablesol.www.shopmanagement.sales.gson_schema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemArrayGst {

    @SerializedName("item_id")
    @Expose
    private Integer itemId;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("hsn_code")
    @Expose
    private String hsnCode;
    @SerializedName("gst")
    @Expose
    private String gst;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

}


package com.inevitablesol.www.shopmanagement.report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Purchaserecord {

    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("gst_total")
    @Expose
    private String gstTotal;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("balance_due")
    @Expose
    private String balanceDue;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getGstTotal() {
        return gstTotal;
    }

    public void setGstTotal(String gstTotal) {
        this.gstTotal = gstTotal;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(String balanceDue) {
        this.balanceDue = balanceDue;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}

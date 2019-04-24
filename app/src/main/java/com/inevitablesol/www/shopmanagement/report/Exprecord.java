
package com.inevitablesol.www.shopmanagement.report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Exprecord {

    @SerializedName("sub_total")
    @Expose
    private String subTotal;
    @SerializedName("total_gst")
    @Expose
    private String totalGst;
    @SerializedName("total_amt")
    @Expose
    private String totalAmt;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTotalGst() {
        return totalGst;
    }

    public void setTotalGst(String totalGst) {
        this.totalGst = totalGst;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}

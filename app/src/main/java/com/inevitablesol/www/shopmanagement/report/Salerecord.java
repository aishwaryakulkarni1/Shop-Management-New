
package com.inevitablesol.www.shopmanagement.report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Salerecord {

    @SerializedName("taxable_value")
    @Expose
    private String taxableValue;
    @SerializedName("total_gst")
    @Expose
    private String totalGst;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("balance_due")
    @Expose
    private String balanceDue;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public String getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(String taxableValue) {
        this.taxableValue = taxableValue;
    }

    public String getTotalGst() {
        return totalGst;
    }

    public void setTotalGst(String totalGst) {
        this.totalGst = totalGst;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

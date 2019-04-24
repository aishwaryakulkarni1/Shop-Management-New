
package com.inevitablesol.www.shopmanagement.purchase_module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseViewRecord {

    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("purchase_id")
    @Expose
    private Integer purchaseId;
    @SerializedName("vendor_id")
    @Expose
    private Integer vendorId;
    @SerializedName("company")
    @Expose
    private String company;

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

}

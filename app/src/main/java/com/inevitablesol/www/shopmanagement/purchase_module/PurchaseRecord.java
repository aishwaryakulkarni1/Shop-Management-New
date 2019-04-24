
package com.inevitablesol.www.shopmanagement.purchase_module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseRecord
{



    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("vendor_id")
    @Expose
    private Integer vendorId;
    @SerializedName("company")
    @Expose
    private String companyName;

    @SerializedName("purchase_id")
    @Expose
    private String purchase_id;

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public String getcompanyName() {
        return companyName;
    }

    public void setcompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPurchase_id() {
        return purchase_id;
    }

    public void setPurchase_id(String purchase_id) {
        this.purchase_id = purchase_id;
    }

    @Override
    public String toString() {
        return "PurchaseRecord{" +
                "invoiceNo='" + invoiceNo + '\'' +
                ", vendorId=" + vendorId +
                ", Name='" + companyName + '\'' +
                ", purchase_id='" + purchase_id + '\'' +
                '}';
    }
}

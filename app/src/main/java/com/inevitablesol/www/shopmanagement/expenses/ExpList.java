
package com.inevitablesol.www.shopmanagement.expenses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cat_id")
    @Expose
    private Integer catId;
    @SerializedName("edate")
    @Expose
    private String edate;
    @SerializedName("exp_name")
    @Expose
    private String expName;
    @SerializedName("sub_total")
    @Expose
    private String subTotal;
    @SerializedName("total_gst")
    @Expose
    private String totalGst;
    @SerializedName("total_qty")
    @Expose
    private String totalQty;
    @SerializedName("other_charges")
    @Expose
    private String otherCharges;
    @SerializedName("total_amt")
    @Expose
    private String totalAmt;
    @SerializedName("amount_paid")
    @Expose
    private String amountPaid;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("ref_no")
    @Expose
    private String refNo;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("imageCode")
    @Expose
    private String imageCode;

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode)
    {
        this.imageCode = imageCode;
    }



    //imageCode

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

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

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getOtherCharges() {
        return otherCharges;
    }

    public void setOtherCharges(String otherCharges) {
        this.otherCharges = otherCharges;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "ExpList{" +
                "id=" + id +
                ", catId=" + catId +
                ", edate='" + edate + '\'' +
                ", expName='" + expName + '\'' +
                ", subTotal='" + subTotal + '\'' +
                ", totalGst='" + totalGst + '\'' +
                ", totalQty='" + totalQty + '\'' +
                ", otherCharges='" + otherCharges + '\'' +
                ", totalAmt='" + totalAmt + '\'' +
                ", amountPaid='" + amountPaid + '\'' +
                ", balance='" + balance + '\'' +
                ", paymentMode='" + paymentMode + '\'' +
                ", refNo='" + refNo + '\'' +
                ", description='" + description + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}

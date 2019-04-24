
package com.inevitablesol.www.shopmanagement.sales.gson_schema;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecordsGst {

    @SerializedName("taxable_value")
    @Expose
    private String taxableValue;
    @SerializedName("cgst")
    @Expose
    private String cgst;
    @SerializedName("igst")
    @Expose
    private String igst;
    @SerializedName("sgst")
    @Expose
    private String sgst;
    @SerializedName("total_gst")
    @Expose
    private String totalGst;
    @SerializedName("balance_due")
    @Expose
    private String balanceDue;
    @SerializedName("other_charges")
    @Expose
    private String otherCharges;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("mode_of_payment")
    @Expose
    private String modeOfPayment;
    @SerializedName("amount_paid")
    @Expose
    private String amountPaid;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("place_of_supply")
    @Expose
    private String placeOfSupply;
    @SerializedName("itemArrayGst")
    @Expose
    private List<ItemArrayGst> itemArrayGst = null;

    public String getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(String taxableValue) {
        this.taxableValue = taxableValue;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getTotalGst() {
        return totalGst;
    }

    public void setTotalGst(String totalGst) {
        this.totalGst = totalGst;
    }

    public String getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(String balanceDue) {
        this.balanceDue = balanceDue;
    }

    public String getOtherCharges() {
        return otherCharges;
    }

    public void setOtherCharges(String otherCharges) {
        this.otherCharges = otherCharges;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public List<ItemArrayGst> getItemArrayGst() {
        return itemArrayGst;
    }

    public void setItemArrayGst(List<ItemArrayGst> itemArrayGst) {
        this.itemArrayGst = itemArrayGst;
    }

}

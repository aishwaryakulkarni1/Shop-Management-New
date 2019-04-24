
package com.inevitablesol.www.shopmanagement.sales.gson_schema;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Records {

    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("place_of_supply")
    @Expose
    private String placeOfSupply;
    @SerializedName("shipping_charges")
    @Expose
    private String shippingCharges;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("amount_paid")
    @Expose
    private String amountPaid;
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
    @SerializedName("itemArray")
    @Expose
    private List<ItemArray> itemArray = null;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public String getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(String shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
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

    public List<ItemArray> getItemArray() {
        return itemArray;
    }

    public void setItemArray(List<ItemArray> itemArray) {
        this.itemArray = itemArray;
    }

}

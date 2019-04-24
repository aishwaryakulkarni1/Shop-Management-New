
package com.inevitablesol.www.shopmanagement.billing_module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class  InvoiceRecord  implements Serializable
{

    @SerializedName("payment_id")
    @Expose
    private Integer paymentId;



    @SerializedName("invoice_id")
    @Expose
    private Integer invoiceId;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("mode_of_payment")
    @Expose
    private String modeOfPayment;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("description")
    @Expose
    private String description;

    public String getTransno() {
        return transno;
    }

    public void setTransno(String transno) {
        this.transno = transno;
    }

    @SerializedName("transno")
    @Expose
    private String transno;
    @SerializedName("cust_name")
    @Expose
    private String customerName;


    @SerializedName("cust_number")
    @Expose
    private String mobile_number;

    @SerializedName("cust_email_id")
    @Expose
    private String email_id;

    @SerializedName("balance_due")
    @Expose
    private String balance_due;

    @SerializedName("shortPmode")
    @Expose
    private String shorpmode;

    public String getShorpmode() {
        return shorpmode;
    }

    public void setShorpmode(String shorpmode) {
        this.shorpmode = shorpmode;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @SerializedName("balance")
    @Expose
    private String balance;

    @SerializedName("total_gst")
    @Expose
    private String total_gst;

    @SerializedName("taxable_value")
    @Expose
    private String taxable_value;
    @SerializedName("amount_paid")
    @Expose
    private String amount_paid;

    @SerializedName("total")
    @Expose
    private String total;

    public String getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(String amount_paid) {
        this.amount_paid = amount_paid;
    }

    public String getTotal_gst() {
        return total_gst;
    }

    public void setTotal_gst(String total_gst) {
        this.total_gst = total_gst;
    }

    public String getTaxable_value() {
        return taxable_value;
    }

    public void setTaxable_value(String taxable_value) {
        this.taxable_value = taxable_value;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }





    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getBalance_due() {
        return balance_due;
    }

    public void setBalance_due(String balance_due) {
        this.balance_due = balance_due;
    }




    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        return "InvoiceRecord{" +
                "paymentId=" + paymentId +
                ", invoiceId=" + invoiceId +
                ", customerId=" + customerId +
                ", modeOfPayment='" + modeOfPayment + '\'' +
                ", amount='" + amount + '\'' +
                ", status='" + status + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", description='" + description + '\'' +
                ", transno='" + transno + '\'' +
                ", customerName='" + customerName + '\'' +
                ", mobile_number='" + mobile_number + '\'' +
                ", email_id='" + email_id + '\'' +
                ", balance_due='" + balance_due + '\'' +
                ", shorpmode='" + shorpmode + '\'' +
                ", balance='" + balance + '\'' +
                ", total_gst='" + total_gst + '\'' +
                ", taxable_value='" + taxable_value + '\'' +
                ", amount_paid='" + amount_paid + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}

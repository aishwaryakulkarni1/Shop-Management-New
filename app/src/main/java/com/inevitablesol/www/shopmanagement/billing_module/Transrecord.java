
package com.inevitablesol.www.shopmanagement.billing_module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transrecord {

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
    @SerializedName("transno")
    @Expose
    private String transno;
    @SerializedName("shortPmode")
    @Expose
    private String shortPmode;


    @SerializedName("charges")
    @Expose
    private String charges;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @SerializedName("total")
    @Expose
    private String total;


    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    @SerializedName("amount_paid")
    @Expose
    private String amountPaid;

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
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

    public String getTransno() {
        return transno;
    }

    public void setTransno(String transno) {
        this.transno = transno;
    }

    public String getShortPmode() {
        return shortPmode;
    }

    public void setShortPmode(String shortPmode) {
        this.shortPmode = shortPmode;
    }

    @Override
    public String toString() {
        return "Transrecord{" +
                "paymentId=" + paymentId +
                ", invoiceId=" + invoiceId +
                ", customerId=" + customerId +
                ", modeOfPayment='" + modeOfPayment + '\'' +
                ", amount='" + amount + '\'' +
                ", status='" + status + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", description='" + description + '\'' +
                ", transno='" + transno + '\'' +
                ", shortPmode='" + shortPmode + '\'' +
                '}';
    }
}

package com.inevitablesol.www.shopmanagement.sales;

/**
 * Created by Pritam on 30-05-2017.
 */

public class SaleInfo
{
    private  String invoice_id;
    private  String  created_Date;
    private  String  amountPaid;
    private  String   balanceDue;
    private  String   totalAmount;
    private  String   modeOfPayment;

    // New CR

     private  String  taxable_value;
    private  String   total_gst;
    private  String   status;

    public String getTaxable_value() {
        return taxable_value;
    }

    public void setTaxable_value(String taxable_value) {
        this.taxable_value = taxable_value;
    }

    public String getTotal_gst() {
        return total_gst;
    }

    public void setTotal_gst(String total_gst) {
        this.total_gst = total_gst;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
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

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getCreated_Date() {
        return created_Date;
    }

    public void setCreated_Date(String created_Date) {
        this.created_Date = created_Date;
    }

    @Override
    public String toString() {
        return "SaleInfo{" +
                "invoice_id='" + invoice_id + '\'' +
                ", created_Date='" + created_Date + '\'' +
                ", amountPaid='" + amountPaid + '\'' +
                ", balanceDue='" + balanceDue + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", modeOfPayment='" + modeOfPayment + '\'' +
                ", taxable_value='" + taxable_value + '\'' +
                ", total_gst='" + total_gst + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}

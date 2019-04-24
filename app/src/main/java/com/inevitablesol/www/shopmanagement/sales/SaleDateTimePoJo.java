package com.inevitablesol.www.shopmanagement.sales;

/**
 * Created by Pritam on 04-10-2017.
 */

public class SaleDateTimePoJo
{
    private  String invoice_id;
    private  String  created_Date;
    private  String  amountPaid;
    private  String   balanceDue;
    private  String   totalAmount;
    private  String   modeOfPayment;

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
        return "SaleDateTimePoJo{" +
                "invoice_id='" + invoice_id + '\'' +
                ", created_Date='" + created_Date + '\'' +
                ", amountPaid='" + amountPaid + '\'' +
                ", balanceDue='" + balanceDue + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", modeOfPayment='" + modeOfPayment + '\'' +
                '}';
    }
}

package com.inevitablesol.www.shopmanagement.billing_module;

/**
 * Created by Anup on 10-08-2017.
 */

public class InvoiceHistoryInfo {
    String srno,invoice_id,amount_paid,balance_due,mode_of_payment,shortCut;

    public String getSrno() {
        return srno;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public String getShortCut() {
        return shortCut;
    }

    public void setShortCut(String shortCut) {
        this.shortCut = shortCut;
    }

    public String getAmount_paid() {
        return amount_paid;
    }

    public String getBalance_due() {
        return balance_due;
    }

    public String getMode_of_payment() {
        return mode_of_payment;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public void setAmount_paid(String amount_paid) {
        this.amount_paid = amount_paid;
    }

    public void setBalance_due(String balance_due) {
        this.balance_due = balance_due;
    }

    public void setMode_of_payment(String mode_of_payment) {
        this.mode_of_payment = mode_of_payment;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    String total;
    public void setSrno(String srno) {
        this.srno = srno;
    }

    @Override
    public String toString() {
        return "InvoiceHistoryInfo{" +
                "srno='" + srno + '\'' +
                ", invoice_id='" + invoice_id + '\'' +
                ", amount_paid='" + amount_paid + '\'' +
                ", balance_due='" + balance_due + '\'' +
                ", total='" + total + '\'' +
                ", mode_of_payment='" + mode_of_payment + '\'' +
                '}';
    }
}

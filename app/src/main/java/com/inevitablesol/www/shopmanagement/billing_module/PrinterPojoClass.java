package com.inevitablesol.www.shopmanagement.billing_module;

import java.io.Serializable;

/**
 * Created by Pritam on 06-04-2018.
 */

public class PrinterPojoClass implements Serializable
{


    private String email;
    private String phone;
    private String address;
    private String custid;
    private String h_status;
    private String gst;
    private String supplier;
    private String taxableValue;
    private  String cgst;
    private  String sgst;
    private  String igst;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    private  String custName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getH_status() {
        return h_status;
    }

    public void setH_status(String h_status) {
        this.h_status = h_status;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

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

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }

    public String getShipping_charge() {
        return shipping_charge;
    }

    public void setShipping_charge(String shipping_charge) {
        this.shipping_charge = shipping_charge;
    }

    public String getOtherCharge() {
        return otherCharge;
    }

    public void setOtherCharge(String otherCharge) {
        this.otherCharge = otherCharge;
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

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalGst() {
        return totalGst;
    }

    public void setTotalGst(String totalGst) {
        this.totalGst = totalGst;
    }

    private  String shipping_charge;
    private  String otherCharge;
    private  String amountPaid;
    private  String balanceDue;
    private  String totalAmount;
    private  String totalGst;
//                    "email"
//                    intent.putExtra("phone", custMobile);
//                    intent.putExtra("address", address);
//                    intent.putExtra("custid", cust_Id);
//                    intent.putExtra("h_status", deliver_status);
//                    intent.putExtra("gst", gst_in);
//                    intent.putExtra("supplier", supplier);
//                    intent.putExtra("taxableValue", taxablevalue);
//                    intent.putExtra("cgst", cgst);
//                    intent.putExtra("sgst", sgst);
//                    intent.putExtra("igst", igst);
//                    intent.putExtra("shipping_charge", shipping_charge);
//                    intent.putExtra("other_charge", other_charge);
//                    intent.putExtra("amountpaid", amountpaid);
//                    intent.putExtra("balanceDue", amountDue);
//                    intent.putExtra("totalAmount", totalAmount);
//                    intent.putExtra("totalGst", totalGst);


    @Override
    public String toString() {
        return "PrinterPojoClass{" +
                "email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", custid='" + custid + '\'' +
                ", h_status='" + h_status + '\'' +
                ", gst='" + gst + '\'' +
                ", supplier='" + supplier + '\'' +
                ", taxableValue='" + taxableValue + '\'' +
                ", cgst='" + cgst + '\'' +
                ", sgst='" + sgst + '\'' +
                ", igst='" + igst + '\'' +
                ", shipping_charge='" + shipping_charge + '\'' +
                ", otherCharge='" + otherCharge + '\'' +
                ", amountPaid='" + amountPaid + '\'' +
                ", balanceDue='" + balanceDue + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", totalGst='" + totalGst + '\'' +
                '}';
    }
}

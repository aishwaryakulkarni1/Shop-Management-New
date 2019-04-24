package com.inevitablesol.www.shopmanagement.billing_module.Parser;

/**
 * Created by Pritam on 14-08-2017.
 */

public class Quotation_pojo
{

    private  String q_id;
    private  String  amount;
    private   String email;
    private  String  mobile;
    private  String totalGst;

    private  String  custName;
    private  String custMobile;
    private  String taxableValue;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    private  String total;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Quotation_pojo{" +
                "q_id='" + q_id + '\'' +
                ", amount='" + amount + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", total='" + total + '\'' +
                ", totalGst='" + totalGst + '\'' +
                ", custName='" + custName + '\'' +
                ", custMobile='" + custMobile + '\'' +
                ", taxableValue='" + taxableValue + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }

    private  String  createdDate;


    public String getTotalGst() {
        return totalGst;
    }

    public void setTotalGst(String totalGst) {
        this.totalGst = totalGst;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustMobile() {
        return custMobile;
    }

    public void setCustMobile(String custMobile) {
        this.custMobile = custMobile;
    }

    public String getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(String taxableValue) {
        this.taxableValue = taxableValue;
    }



    public String getQ_id() {
        return q_id;
    }

    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}

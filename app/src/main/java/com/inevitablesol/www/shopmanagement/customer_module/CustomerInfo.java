package com.inevitablesol.www.shopmanagement.customer_module;

/**
 * Created by Pritam on 25-05-2017.
 */

public class CustomerInfo
{
    private  String customer_id;
    private   String customer_name;
    private  String  mobile_numbe;
    private  String  email_id;
    private  String  home_delivery;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private  String  state;

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getPlaceofsupply() {
        return placeofsupply;
    }

    public void setPlaceofsupply(String placeofsupply) {
        this.placeofsupply = placeofsupply;
    }

    private  String gstin;
    private  String placeofsupply;


    private  String address;

    public String getCustomer_id()
    {

        return customer_id;
    }

    public String getGstStatus() {
        return gstStatus;
    }

    public void setGstStatus(String gstStatus) {
        this.gstStatus = gstStatus;
    }

    private String gstStatus;

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getMobile_numbe() {
        return mobile_numbe;
    }

    public void setMobile_numbe(String mobile_numbe) {
        this.mobile_numbe = mobile_numbe;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getHome_delivery() {
        return home_delivery;
    }

    public void setHome_delivery(String home_delivery) {
        this.home_delivery = home_delivery;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CustomerInfo{" +
                "customer_id='" + customer_id + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", mobile_numbe='" + mobile_numbe + '\'' +
                ", email_id='" + email_id + '\'' +
                ", home_delivery='" + home_delivery + '\'' +
                ", gstin='" + gstin + '\'' +
                ", placeofsupply='" + placeofsupply + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

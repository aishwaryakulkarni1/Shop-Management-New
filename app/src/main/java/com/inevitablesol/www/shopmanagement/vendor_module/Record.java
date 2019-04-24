
package com.inevitablesol.www.shopmanagement.vendor_module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("vendor_id")
    @Expose
    private Integer vendorId;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("owner_name")
    @Expose
    private String ownerName;
    @SerializedName("contact_person")
    @Expose
    private String contactPerson;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("gst_details")
    @Expose
    private String gstDetails;

    @SerializedName("gstin_no")
    @Expose
    private String gstinNo;

    @SerializedName("hsn_ssc_code")
    @Expose
    private String createdBy;

    @SerializedName("created_date")
    @Expose
    private String createdDate;

    @SerializedName("u_id")
    @Expose
    private String uID;

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    @SerializedName("stateCode")
    @Expose
    private String stateCode;

    @SerializedName("s_id")
    @Expose
    private String sID;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    @SerializedName("state")
    @Expose
    private String state;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getU_pass() {
        return u_pass;
    }

    public void setU_pass(String u_pass) {
        this.u_pass = u_pass;
    }

    public String getU_role() {
        return u_role;
    }

    public void setU_role(String u_role) {
        this.u_role = u_role;
    }

    public String getU_mail() {
        return u_mail;
    }

    public void setU_mail(String u_mail) {
        this.u_mail = u_mail;
    }

    public String getU_number() {
        return u_number;
    }

    public void setU_number(String u_number) {
        this.u_number = u_number;
    }

    @SerializedName("u_name")
    @Expose
    private String uname;

    @SerializedName("u_number")
    @Expose
    private String u_number;

    @SerializedName("u_email")
    @Expose
    private String u_mail;

    @SerializedName("u_role")
    @Expose
    private String u_role;

    @SerializedName("u_pass")
    @Expose
    private String u_pass;

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    @Override
    public String toString() {
        return "Record{" +
                "vendorId=" + vendorId +
                ", company='" + company + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", emailId='" + emailId + '\'' +
                ", address='" + address + '\'' +
                ", gstDetails='" + gstDetails + '\'' +
                ", gstinNo='" + gstinNo + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", uID='" + uID + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", sID='" + sID + '\'' +
                ", uname='" + uname + '\'' +
                ", u_number='" + u_number + '\'' +
                ", u_mail='" + u_mail + '\'' +
                ", u_role='" + u_role + '\'' +
                ", u_pass='" + u_pass + '\'' +
                '}';
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGstDetails() {
        return gstDetails;
    }

    public void setGstDetails(String gstDetails) {
        this.gstDetails = gstDetails;
    }

    public String getGstinNo() {
        return gstinNo;
    }

    public void setGstinNo(String gstinNo) {
        this.gstinNo = gstinNo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}

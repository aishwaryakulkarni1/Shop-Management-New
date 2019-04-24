
package com.inevitablesol.www.shopmanagement.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Alluser  implements Serializable {

    @SerializedName("u_id")
    @Expose
    private Integer uId;
    @SerializedName("s_id")
    @Expose
    private Integer sId;
    @SerializedName("u_name")
    @Expose
    private String uName;
    @SerializedName("u_number")
    @Expose
    private String uNumber;
    @SerializedName("u_email")
    @Expose
    private String uEmail;
    @SerializedName("u_role")
    @Expose
    private String uRole;
    @SerializedName("u_pass")
    @Expose
    private String uPass;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("dbname")
    @Expose
    private String dbname;
    @SerializedName("gstin_no")
    @Expose
    private String gstinNo;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("shopName")
    @Expose
    private String shopName;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("ecode")
    @Expose
    private String ecode;
    @SerializedName("created_on")
    @Expose
    private String createdOn;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @SerializedName("link")
    @Expose
    private String link;

    public Integer getUId() {
        return uId;
    }

    public void setUId(Integer uId) {
        this.uId = uId;
    }

    public Integer getSId() {
        return sId;
    }

    public void setSId(Integer sId) {
        this.sId = sId;
    }

    public String getUName() {
        return uName;
    }

    public void setUName(String uName) {
        this.uName = uName;
    }

    public String getUNumber() {
        return uNumber;
    }

    public void setUNumber(String uNumber) {
        this.uNumber = uNumber;
    }

    public String getUEmail() {
        return uEmail;
    }

    public void setUEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getURole() {
        return uRole;
    }

    public void setURole(String uRole) {
        this.uRole = uRole;
    }

    public String getUPass() {
        return uPass;
    }

    public void setUPass(String uPass) {
        this.uPass = uPass;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getGstinNo() {
        return gstinNo;
    }

    public void setGstinNo(String gstinNo) {
        this.gstinNo = gstinNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEcode() {
        return ecode;
    }

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }

    @Override
    public String toString() {
        return "Alluser{" +
                "uId=" + uId +
                ", sId=" + sId +
                ", uName='" + uName + '\'' +
                ", uNumber='" + uNumber + '\'' +
                ", uEmail='" + uEmail + '\'' +
                ", uRole='" + uRole + '\'' +
                ", uPass='" + uPass + '\'' +
                ", otp='" + otp + '\'' +
                ", dbname='" + dbname + '\'' +
                ", gstinNo='" + gstinNo + '\'' +
                ", address='" + address + '\'' +
                ", state='" + state + '\'' +
                ", shopName='" + shopName + '\'' +
                ", dob='" + dob + '\'' +
                ", ecode='" + ecode + '\'' +
                ", createdOn='" + createdOn + '\'' +
                '}';
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

}

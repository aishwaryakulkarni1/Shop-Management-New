
package com.inevitablesol.www.shopmanagement.wishList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WishGsonList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cust_name")
    @Expose
    private String custName;
    @SerializedName("mob_no")
    @Expose
    private String mobNo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("till_date")
    @Expose
    private String tillDate;

    @SerializedName("reminder_date")
    @Expose
    private String reminderDate;

    @SerializedName("totalqty")
    @Expose
    private String totalqty;




    @SerializedName("status")
    @Expose
    private String status;

    public String getTotalqty() {
        return totalqty;
    }

    public void setTotalqty(String totalqty) {
        this.totalqty = totalqty;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTillDate() {
        return tillDate;
    }

    public void setTillDate(String tillDate) {
        this.tillDate = tillDate;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "WishGsonList{" +
                "id=" + id +
                ", custName='" + custName + '\'' +
                ", mobNo='" + mobNo + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", tillDate='" + tillDate + '\'' +
                ", reminderDate='" + reminderDate + '\'' +
                ", totalqty='" + totalqty + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}

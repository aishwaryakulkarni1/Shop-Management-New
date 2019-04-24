
package com.inevitablesol.www.shopmanagement.report;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseReports {

    @SerializedName("message")
    @Expose
    private Boolean message;
    @SerializedName("purchaserecords")
    @Expose
    private List<Purchaserecord> purchaserecords = null;
    @SerializedName("msgclass")
    @Expose
    private String msgclass;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    public Boolean getMessage() {
        return message;
    }

    public void setMessage(Boolean message) {
        this.message = message;
    }

    public List<Purchaserecord> getPurchaserecords() {
        return purchaserecords;
    }

    public void setPurchaserecords(List<Purchaserecord> purchaserecords) {
        this.purchaserecords = purchaserecords;
    }

    public String getMsgclass() {
        return msgclass;
    }

    public void setMsgclass(String msgclass) {
        this.msgclass = msgclass;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

}

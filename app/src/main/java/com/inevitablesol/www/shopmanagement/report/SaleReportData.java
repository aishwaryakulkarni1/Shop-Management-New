
package com.inevitablesol.www.shopmanagement.report;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaleReportData {

    @SerializedName("message")
    @Expose
    private Boolean message;
    @SerializedName("salerecords")
    @Expose
    private List<Salerecord> salerecords = null;
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

    public List<Salerecord> getSalerecords() {
        return salerecords;
    }

    public void setSalerecords(List<Salerecord> salerecords) {
        this.salerecords = salerecords;
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

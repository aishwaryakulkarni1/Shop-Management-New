
package com.inevitablesol.www.shopmanagement.billing_module;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TranHistory {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("transrecords")
    @Expose
    private List<Transrecord> transrecords = null;
    @SerializedName("msgclass")
    @Expose
    private String msgclass;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Transrecord> getTransrecords() {
        return transrecords;
    }

    public void setTransrecords(List<Transrecord> transrecords) {
        this.transrecords = transrecords;
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

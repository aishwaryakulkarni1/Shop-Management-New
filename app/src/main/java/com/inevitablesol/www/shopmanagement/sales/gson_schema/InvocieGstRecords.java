
package com.inevitablesol.www.shopmanagement.sales.gson_schema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvocieGstRecords {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("records")
    @Expose
    private Records records;
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

    public Records getRecords() {
        return records;
    }

    public void setRecords(Records records) {
        this.records = records;
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

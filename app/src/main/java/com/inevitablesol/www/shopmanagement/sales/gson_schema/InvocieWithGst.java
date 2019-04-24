
package com.inevitablesol.www.shopmanagement.sales.gson_schema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvocieWithGst {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("recordsGst")
    @Expose
    private RecordsGst recordsGst;
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

    public RecordsGst getRecordsGst() {
        return recordsGst;
    }

    public void setRecordsGst(RecordsGst recordsGst) {
        this.recordsGst = recordsGst;
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

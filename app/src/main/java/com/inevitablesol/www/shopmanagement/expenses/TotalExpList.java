
package com.inevitablesol.www.shopmanagement.expenses;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalExpList {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("records")
    @Expose
    private List<ExpList> expList = null;
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

    public List<ExpList> getExpList() {
        return expList;
    }

    public void setExpList(List<ExpList> expList) {
        this.expList = expList;
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

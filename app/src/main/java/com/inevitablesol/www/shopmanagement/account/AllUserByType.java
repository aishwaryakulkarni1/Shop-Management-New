
package com.inevitablesol.www.shopmanagement.account;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllUserByType {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("alluser")
    @Expose
    private List<Alluser> alluser = null;
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

    public List<Alluser> getAlluser() {
        return alluser;
    }

    public void setAlluser(List<Alluser> alluser) {
        this.alluser = alluser;
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

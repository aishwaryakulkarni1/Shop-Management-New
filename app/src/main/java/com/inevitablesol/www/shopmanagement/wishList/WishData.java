
package com.inevitablesol.www.shopmanagement.wishList;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WishData {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("records")
    @Expose
    private List<WishGsonList> wishGsonList = null;
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

    public List<WishGsonList> getWishGsonList() {
        return wishGsonList;
    }

    public void setWishGsonList(List<WishGsonList> wishGsonList) {
        this.wishGsonList = wishGsonList;
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

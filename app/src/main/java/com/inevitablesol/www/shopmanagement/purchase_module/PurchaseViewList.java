
package com.inevitablesol.www.shopmanagement.purchase_module;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseViewList {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("purchase_view_records")
    @Expose
    private List<PurchaseViewRecord> purchaseViewRecords = null;
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

    public List<PurchaseViewRecord> getPurchaseViewRecords() {
        return purchaseViewRecords;
    }

    public void setPurchaseViewRecords(List<PurchaseViewRecord> purchaseViewRecords) {
        this.purchaseViewRecords = purchaseViewRecords;
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

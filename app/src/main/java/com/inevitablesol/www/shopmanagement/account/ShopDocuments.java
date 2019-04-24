
package com.inevitablesol.www.shopmanagement.account;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopDocuments {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("documentList")
    @Expose
    private List<DocumentList> documentList = null;
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

    public List<DocumentList> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<DocumentList> documentList) {
        this.documentList = documentList;
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

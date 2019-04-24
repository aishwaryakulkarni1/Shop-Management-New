
package com.inevitablesol.www.shopmanagement.billing_module;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvocieHistoryList {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("invoice_records")
    @Expose
    private List<InvoiceRecord> invoiceRecords = null;
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

    public List<InvoiceRecord> getInvoiceRecords() {
        return invoiceRecords;
    }

    public void setInvoiceRecords(List<InvoiceRecord> invoiceRecords) {
        this.invoiceRecords = invoiceRecords;
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

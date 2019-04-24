package com.inevitablesol.www.shopmanagement.service;

/**
 * Created by Pritam on 13-06-2017.
 */
public class Services
{
    private  String Service_id;
    private  String service_name;
    private  String service_amount;
    private  String  service_due;
    private  String service_duration;

    public String getService_id() {
        return Service_id;
    }

    public void setService_id(String service_id) {
        Service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_amount() {
        return service_amount;
    }

    public void setService_amount(String service_amount) {
        this.service_amount = service_amount;
    }

    public String getService_due() {
        return service_due;
    }

    public void setService_due(String service_due) {
        this.service_due = service_due;
    }

    public String getService_duration() {
        return service_duration;
    }

    public void setService_duration(String service_duration) {
        this.service_duration = service_duration;
    }
}

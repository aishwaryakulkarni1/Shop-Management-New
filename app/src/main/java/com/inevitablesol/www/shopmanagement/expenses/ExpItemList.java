package com.inevitablesol.www.shopmanagement.expenses;

/**
 * Created by Pritam on 14-09-2017.
 */
public class ExpItemList
{
    private String id;
    private  String eid;
    private  String name;
    private  String unit_price;
    private  String total;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ExpItemList{" +
                "id='" + id + '\'' +
                ", eid='" + eid + '\'' +
                ", name='" + name + '\'' +
                ", unit_price='" + unit_price + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}

package com.inevitablesol.www.shopmanagement.wishList;

/**
 * Created by Pritam on 15-09-2017.
 */

public class WishListItems_pojo
{
    private String name;
    private  String qty;
    private  String  company;
    private  boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "WishListItems_pojo{" +
                "name='" + name + '\'' +
                ", qty='" + qty + '\'' +
                ", company='" + company + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}

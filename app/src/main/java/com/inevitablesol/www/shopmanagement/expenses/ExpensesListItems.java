package com.inevitablesol.www.shopmanagement.expenses;

/**
 * Created by Pritam on 12-09-2017.
 */
public class ExpensesListItems
{
    private String itemName;
    private  String qty;
    private  String unitPrice;
    private  String totalPrice;
    private  boolean isChecked;

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "ExpensesListItems{" +
                "itemName='" + itemName + '\'' +
                ", qty='" + qty + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}

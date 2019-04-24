package com.inevitablesol.www.shopmanagement.billing_module;

/**
 * Created by Anup on 22-02-2017.
 */

public class StockInfo
{
    public String item_id;
    public String item_name;
    public String product_type;
    public String product_comp;
    public String product_owner;
    public String stock_qty;
    public String mrp;
    public String original_price;
    public String specifications;
    public String created_by;
    public String modify_by;

    private  String selectedQuantity;

    public String getSelectedQuantity()
    {
        return selectedQuantity;
    }

    public void setSelectedQuantity(String selectedQuantity)
    {
        this.selectedQuantity = selectedQuantity;
    }

    public  String getItem_id() {
        return item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getProduct_type() {
        return product_type;
    }

    public String getProduct_comp() {
        return product_comp;
    }

    public String getProduct_owner() {
        return product_owner;
    }

    public String getStock_qty() {
        return stock_qty;
    }

    public String getMrp() {
        return mrp;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public String getSpecifications() {
        return specifications;
    }

    public String getCreated_by() {
        return created_by;
    }

    public String getModify_by() {
        return modify_by;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public void setProduct_comp(String product_comp) {
        this.product_comp = product_comp;
    }

    public void setProduct_owner(String product_owner) {
        this.product_owner = product_owner;
    }

    public void setStock_qty(String stock_qty) {
        this.stock_qty = stock_qty;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public void setModify_by(String modify_by) {
        this.modify_by = modify_by;
    }
}

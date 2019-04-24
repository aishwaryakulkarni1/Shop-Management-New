package com.inevitablesol.www.shopmanagement.expenses;

/**
 * Created by Pritam on 13-09-2017.
 */
public class ExpensesType
{

    private String exp_Type_id;
    private  String  type;

    @Override
    public String toString() {
        return "ExpensesType{" +
                "exp_Type_id='" + exp_Type_id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getExp_Type_id() {
        return exp_Type_id;
    }

    public void setExp_Type_id(String exp_Type_id) {
        this.exp_Type_id = exp_Type_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

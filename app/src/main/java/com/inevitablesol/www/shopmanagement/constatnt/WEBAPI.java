package com.inevitablesol.www.shopmanagement.constatnt;

/**
 * Created by Pritam on 05-04-2018.
 */

public interface WEBAPI
{
   String   HighestSale="http://35.161.99.113:9000/webapi/sale/highestSale" ;
    String HighestPurchaseDay="http://35.161.99.113:9000/webapi/sale/highestPurchase";
    String HighestRepeatativeCustomer="http://35.161.99.113:9000/webapi/sale/highestrepCust";
    String HighestCustomerADay="http://35.161.99.113:9000/webapi/sale/highestTotalCust";
    String HighestSaleToCustomer="http://35.161.99.113:9000/webapi/sale/highestsaletocust";
    String PURCHASEREPORT="http://35.161.99.113:9000/webapi/report/purchaseReport";
    String EXPENSEREPORT="http://35.161.99.113:9000/webapi/report/expReport";
       String TAXREPORT = "http://35.161.99.113:9000/webapi/report/taxReport";

    String SALEREPORT="http://35.161.99.113:9000/webapi/report/saleReport";
}

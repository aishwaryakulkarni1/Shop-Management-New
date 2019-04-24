package com.inevitablesol.www.shopmanagement.WebApi;

/**
 * Created by Pritam on 05-07-2017.
 */

public class WebApi
{
    public static final String ADDNEW_EXPENSESTYPE = "http://35.161.99.113:9000/webapi/expense/addcat";
    public static final String GETSHOPDETAILS ="http://35.161.99.113:9000/webapi/admin/shop_details" ;
    public static final String GET_VENDORBYCOMPANY = "http://35.161.99.113:9000/webapi/vendor/view_vendor";
    public static final String USERUPDATE_PROFILE = "http://35.161.99.113:9000/webapi/users/editProfile";
    public static final String USER_CREATE = "http://35.161.99.113:9000/webapi/users/create_user";
    public static final String GET_SUB_USER ="http://35.161.99.113:9000/webapi/users/usersList" ;
    public static final String USER_EDIT = "http://35.161.99.113:9000/webapi/users/edit";
    public static final String USER_GETSUBUSEER ="http://35.161.99.113:9000/webapi/users/all_users" ;
    //Login Api

    public  static String LOGING ="http://35.161.99.113:9000/webapi/users/sign_in";
    public static String SHOPMGMT="http://35.161.99.113:9000/webapi/";

    //prodcut

    public  static final String GET_PRODUCT = " http://35.161.99.113:9000/webapi/product/list";



    //productApi
    public static String  ADDNEW_PRODUCTTYPE=SHOPMGMT+"product/create";
    public  static String  GET_NEW_PRODUCT="http://35.161.99.113:9000/webapi/product/list";
    public static String  ADD_NEW_ITEM="http://35.161.99.113:9000/webapi/product/create_item";


    /// Login SECTION

    public static String URL_MOBILE_VERIFY="http://35.161.99.113:9000/webapi/users/forget_password";
    public static String URL_REQUEST_SMS="http://35.161.99.113:9000/webapi/users/verifyOtp";
    public static  String VERIFY_OTP="http://35.161.99.113:9000/webapi/users/otpCheck";
    public  static  String URL_UPDATE_PASSWORD="http://35.161.99.113:9000/webapi/users/changePass";
    public static String GET_PRODUCT_TYPE="http://35.161.99.113:9000/webapi/product/getProductType";

    // VENDOR API

    public  static  String ADD_VENDOR="http://35.161.99.113:9000/webapi/vendor/create";
    public static String GETVENDDOR_DETAILS="http://35.161.99.113:9000/webapi/vendor/list";
    public static String UPDATE_VENDOR="http://35.161.99.113:9000/webapi/vendor/edit";
    public  static String GETVENDORBYID="http://35.161.99.113:9000/webapi/vendor/vendor_info";
    public  static  String GETCOMPANYBYID="http://35.161.99.113:9000/webapi/vendor/view_vendor1";

    //Item Api
     public static String GETITEMDETAISBYID="http://35.161.99.113:9000/webapi/product/product_info";
    public  static String GETITEMDETAILSBYITEMID="http://35.161.99.113:9000/webapi/product/item_info";
    public  static String GETITEMDETAILSBYBARCODE="http://35.161.99.113:9000/webapi/product/barcode_info";


    //Add Purchase

    public  static  String ADD_PURCHASE="http://35.161.99.113:9000/webapi/purchase/create";
    public  static  String VIEW_PURCHASE="http://35.161.99.113:9000/webapi/purchase/view";


    //customer module

    public  static String GETCUSTINFO="http://35.161.99.113:9000/webapi/customer/cust_list";
    public  static  String GETCUSTBYNAME="http://35.161.99.113:9000/webapi/customer/getCustomerDetails";

    public static String ADD_CUSTOMER="http://35.161.99.113:9000/webapi/customer/create_new";
    public static String DELETE_CUSTOMER="http://35.161.99.113:9000/webapi/customer/delete";
    public static String UPDATE_CUSTOMER="http://35.161.99.113:9000/webapi/customer/edit";


    // Stock List

    public static final String GET_STOCKDETAILS = "http://35.161.99.113:9000/webapi/product/stock_list";

    public static String UPDATE_STOCK="http://35.161.99.113:9000/webapi/product/edit";
    public static String GETALLVENDOR="http://35.161.99.113:9000/webapi/purchase/allview";
    public static String GETVENDORBYOWNER="http://35.161.99.113:9000/webapi/purchase/view";


    public  static String   GETINVOICEBYDATE ="http://35.161.99.113:9000/webapi/invoice/date";
    public static String GETINVOICEBYNUMBER="http://35.161.99.113:9000/webapi/invoice/history";
    public static String GETPURCHASEDETAILSBYID="http://35.161.99.113:9000/webapi/purchase/details";

    public static String BILLING_HISTORY ="http://35.161.99.113:9000/webapi/invoice/date";



    //get Suplier

    public  static  String GETSUPLER="http://35.161.99.113:9000/webapi/placeofsupply/list";
    public static String CREATEBILL="http://35.161.99.113:9000/webapi/invoice/create_inv";
    public static String SENDMAILAND_SMS="http://35.161.99.113:9000/webapi/users/link";
    public static String AddEMIDETAILS="http://35.161.99.113:9000/webapi/invoice/addemi";


    public  static  String CREATEQUOTATION="http://35.161.99.113:9000/webapi/quotation/create";
    public static String QUOTATION_HISTORY="http://35.161.99.113:9000/webapi/quotation/date";
    public static String CREATE_EXPENSESE="http://35.161.99.113:9000/webapi/expense/create";
    public static String GET_NEW_PRODUCT_EXP="http://35.161.99.113:9000/webapi/expense/displaycat";
    public static String GET_EXP_BY_P_ID="http://35.161.99.113:9000/webapi/expense/details";
    public static String GET_EXP_BY_DATE="http://35.161.99.113:9000/webapi/expense/date";
    public static String ADD_REMAININGAMOUNT="http://35.161.99.113:9000/webapi/expense/paybalance";
    public static String CREATE_WISHLIST="http://35.161.99.113:9000/webapi/wish/create";
    public static String GET_WISHLISTBYNAME="http://35.161.99.113:9000/webapi/wish/viewall";
    public static String GET_Wish_BY_P_ID=" http://35.161.99.113:9000/webapi/wish/singlewish";
    public static String GET_Wish_STATUSDONE="http://35.161.99.113:9000/webapi/wish/updatewish";
    public static String GET_TOTALSALE_YEAR="http://35.161.99.113:9000/webapi/sale/year";
    public static String INVIOCEAMOUNTPAIDHISTORY="http://35.161.99.113:9000/webapi/invoice/updatepayment";
    public static String GET_SHOPDETAILS="http://35.161.99.113:9000/webapi/admin/shop_info";
    public static String UPDATE_SHOP_DETAILS="http://35.161.99.113:9000/webapi/admin/editshop";
    public static String SAVEBANKDETIALS="http://35.161.99.113:9000/webapi/setting/saveaccount";
    public static String VIEWACCOUNT_DETAILS="http://35.161.99.113:9000/webapi/setting/viewaccount";
    public static String SAVEBP99="http://35.161.99.113:9000/webapi/setting/savepb99";
    public static String GETPAYTMDEATLS="http://35.161.99.113:9000/webapi/setting/viewpb99";
    public static String PASSCODE_DEATILS="http://35.161.99.113:9000/webapi/setting/verifyPasscode";
    public static String SAVEPASSCODE="http://35.161.99.113:9000/webapi/setting/savePasscode";
    public static String GETSINGLEINVOICEREPORT="http://35.161.99.113:9000/webapi/invoice/invoiceNonGst";
    public static String GETTOTALCOUNT="http://35.161.99.113:9000/webapi/sale/totalCustSaleSum";
    public static String GETTOTALCOUNT_FROMDATE="http://35.161.99.113:9000/webapi/sale/totalSaleSum";
    public static String GETTOTALCOUNT_PRODUCTTYPE="http://35.161.99.113:9000/webapi/sale/totalItemSaleSum";
    public static String GETTOTALCOUNT_MODEOFPAYMENT=" http://35.161.99.113:9000/webapi/sale/totalModSaleSum";
    public static String GETGSTINVOCIE_DETAILS="http://35.161.99.113:9000/webapi/invoice/invoiceGst";
    public static String GET_TOTAL_AMOUNT="http://35.161.99.113:9000/webapi/expense/sumamount";
    public static String UploadPurchaseImage="http://35.161.99.113:9000/webapi/purchase/saveLink";
    public static String GetPurchaseImage="http://35.161.99.113:9000/webapi/purchase/getLink";
    public static String UploadExpensesImage="http://35.161.99.113:9000/webapi/expense/addLink";
    public static String GetExpenseImage="http://35.161.99.113:9000/webapi/expense/getLink";
    public static String Upload_DOcs="http://35.161.99.113:9000/webapi/users/saveLink";
    public static String SHOP_DOCUMENTS="http://35.161.99.113:9000/webapi/users/getLink";
    public static String GETEMIDETAILS="http://35.161.99.113:9000/webapi/invoice/getEmi";
    public static String AddCHEQUE="http://35.161.99.113:9000/webapi/invoice/addcheque";
    public static String GETCHECK_DETAILS="http://35.161.99.113:9000/webapi/invoice/getChk";
}

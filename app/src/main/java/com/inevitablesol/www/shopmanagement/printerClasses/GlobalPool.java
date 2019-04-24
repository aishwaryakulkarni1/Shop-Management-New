package com.inevitablesol.www.shopmanagement.printerClasses;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.inevitablesol.www.shopmanagement.R;

import java.io.InputStream;
import java.io.OutputStream;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class GlobalPool extends Application
{
	/**Bluetooth communication connection object*/
	public BluetoothComm mBTcomm = null;
	public static InputStream misIn = BluetoothComm.misIn;
	/** Output stream object */
	public static OutputStream mosOut = BluetoothComm.mosOut;
	public  static  boolean isConnected;

	private String Username;
	private  String  UserEmail;

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	private  String shopId;

	public String getDOB() {
		return DOB;
	}

	public void setDOB(String DOB) {
		this.DOB = DOB;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	private String DOB;
	private  String empCode;

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}

	private  String created_on;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private  String userId;

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getUserEmail() {
		return UserEmail;
	}

	public void setUserEmail(String userEmail) {
		UserEmail = userEmail;
	}

	public String getUsermobile() {
		return Usermobile;
	}

	public void setUsermobile(String usermobile) {
		Usermobile = usermobile;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	private  String   Usermobile;
	private String    userRole;


	private  String user__noti_email;

	public String getUser__noti_email() {
		return user__noti_email;
	}

	public void setUser__noti_email(String user__noti_email) {
		this.user__noti_email = user__noti_email;
	}

	public String getUser_noti_mobile() {
		return user_noti_mobile;
	}

	public void setUser_noti_mobile(String user_noti_mobile) {
		this.user_noti_mobile = user_noti_mobile;
	}

	private  String user_noti_mobile;


	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	private  String shopName;

	public  String getH_status() {
		return h_status;
	}

	public  void setH_status(String h_status) {
		GlobalPool.h_status = h_status;
	}

	public  static String h_status;
	private  boolean gst_status=true;

	public boolean getGst_status()
	{
		return gst_status;
	}

	public void setGst_status(boolean gst_status)
	{
		this.gst_status = gst_status;
	}

	public String getDbname()
	{
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	private boolean reminderDueStatus;

	public boolean isReminderDueStatus() {
		return reminderDueStatus;
	}

	public void setReminderDueStatus(boolean reminderDueStatus) {
		this.reminderDueStatus = reminderDueStatus;
	}

	public String getReminderDays() {
		return reminderDays;
	}

	public void setReminderDays(String reminderDays) {
		this.reminderDays = reminderDays;
	}

	private  String reminderDays;


	public boolean isMenuItemStatus()
	{
		return menuItemStatus;
	}

	public void setMenuItemStatus(boolean menuItemStatus)
	{
		this.menuItemStatus = menuItemStatus;
	}

	private  boolean menuItemStatus=true;


	private  String   dbname;
	private static final String TAG = "GlobalPool";
	private SharedPreferences sharedpreferences;
	private static final String MySETTINGS = "MySetting";

	public boolean isTest() {
		return Test;
	}

	public void setTest(boolean test)
	{
		Test = test;
	}

	private  boolean Test;

	public boolean isPruchasesms() {
		return pruchasesms;
	}

	public void setPruchasesms(boolean pruchasesms) {
		this.pruchasesms = pruchasesms;
	}

	private  boolean pruchasesms;

	public String getGstNumnebr() {
		return gstNumnebr;
	}

	public void setGstNumnebr(String gstNumnebr)
	{
		this.gstNumnebr = gstNumnebr;
	}

	private  String gstNumnebr;

	public String getShopNumber() {
		return shopNumber;
	}

	public void setShopNumber(String shopNumber) {
		this.shopNumber = shopNumber;
	}

	private  String shopNumber;

	public String getShop_address() {
		return shop_address;
	}

	public void setShop_address(String shop_address) {
		this.shop_address = shop_address;
	}

	public String getShop_email() {
		return shop_email;
	}

	public void setShop_email(String shop_email) {
		this.shop_email = shop_email;
	}

	private  String shop_address;
	private  String shop_email;

	public String getShop_state() {
		return shop_state;
	}

	public void setShop_state(String shop_state) {
		this.shop_state = shop_state;
	}

	private String shop_state;

	public String getProfile_Pic() {
		return profile_Pic;
	}

	public void setProfile_Pic(String profile_Pic) {
		this.profile_Pic = profile_Pic;
	}

	private String profile_Pic;


	@Override
	public void onCreate(){
		super.onCreate();
		try
		{
			Log.d(TAG, "onCreate: Menu Item"+menuItemStatus);
			Log.d(TAG, "onCreate: GlobalPool");
			sharedpreferences = getSharedPreferences(MySETTINGS, Context.MODE_PRIVATE);

			String purchaseSms=(sharedpreferences.getString("purchase_sms",null));
			SharedPreferences.Editor editor = sharedpreferences.edit();
			Log.d(TAG, "onCreate: "+purchaseSms);
			if(purchaseSms == null)
			{
				editor.putString("purchase_sms","no");
 			    editor.commit();
 			    setPruchasesms(false);

			}else if(purchaseSms.equalsIgnoreCase("Yes"))
			{
				setPruchasesms(true);
				Log.d(TAG, "onCreate: Yes");
			}else
				{
					Log.d(TAG, "onCreate: No ");
					setPruchasesms(false);

			}



//
//			editor.putString("wishList_mail","no");
//			editor.putString("wishList_reminder","no");
//			editor.putString("wishList_sms","no");
//
//			CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                    .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
//                    .setFontAttrId(R.attr.fontPath)
//                    .build()
           // );
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Set up a Bluetooth connection 
	// * @param String sMac Bluetooth hardware address
	 * @return Boolean
	 * */
	public boolean createConn(String sMac)
	{
		Log.e("Connect","Create Connection");
		if (null == this.mBTcomm)
		{   
			
			this.mBTcomm = new BluetoothComm(sMac);
			if (this.mBTcomm.createConn())
			{
				isConnected=true;
				
				return true;
			}
			else{
				isConnected=false;
				this.mBTcomm = null;
				return false;
			}
		}
		else
			return true;
	}
	
	/**
	 * Close and release the connection
	 * @return void
	 * */
	public void closeConn(){
		if (null != this.mBTcomm){
			this.mBTcomm.closeConn();
			this.mBTcomm = null;
		}
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}

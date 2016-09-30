package com.ankit.pointofsolution.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceActivity;
import android.telephony.TelephonyManager;


import com.ankit.pointofsolution.config.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Preferences - To retrieve and save from Shared Preferences in the Android
 * device
 * 
 */
public class Preferences {

	private Activity activity = null;
	private Context context = null;
	private SharedPreferences prefsPrivate;
	public static final String PREFS_PRIVATE = Constants.APP_NAME+ "_PREFS_PRIVATE";
	Gson gson = new Gson();

	public static final String KEY_IS_DEVICE_VERIFIED = "IS_DEVICE_VERIFIED";
	public static final String KEY_ORDER_DETAILS = "ORDER_DETAILS";

	public static final String KEY_LOGGED_USERNAME = "LOGGED_USERNAME";
	public static final String KEY_LOGGED_PASSWORD = "LOGGED_PASSWORD";
	public static final String KEY_IS_LOGGED_IN = "IS_LOGGED_IN";
	public static final String KEY_USER_DATA = "USER_DATA";
	public static final String KEY_IMPORT_DATA = "IMPORT_DATA";
	public static final String KEY_PRODUCT_DATA ="PRODUCT_DATA";
	public static final String KEY_SKU_CODE ="SKU_CODE";
	public static final String KEY_ITEM_PRICE ="ITEM_PRICE";
	public static final String KEY_ITEM_NAME ="ITEM_NAME";
	public static final String KEY_ITEM_BRAND ="ITEM_BRAND";
	public static final String KEY_ADMIN_CODE ="ADMIN_CODE";
	public static final String KEY_AUTO_INCREAMENT_ID = "AUTO_INCREAMENT_ID";
	public static final String KEY_CURRENT_ORDER_ID = "CURRENT_ORDER_ID";


	public Preferences(Activity thisActivity) {
		activity = thisActivity;
		prefsPrivate = activity.getSharedPreferences(PREFS_PRIVATE,
				MODE_PRIVATE);
	}

	public Preferences(PreferenceActivity thisActivity) {
		activity = thisActivity;
		prefsPrivate = activity.getSharedPreferences(PREFS_PRIVATE,
				MODE_PRIVATE);
	}

	public Preferences(Context context) {
		this.context = context;
		prefsPrivate = this.context.getSharedPreferences(PREFS_PRIVATE,
				MODE_PRIVATE);

	}

	/**
	 * Method to save the values with that key in SharedPreferences.
	 * 
	 * @param keyValue
	 *            Key name to be stored.
	 * @param strValue
	 *            Value to be stored with that key name.
	 */

	public void putValue(String keyValue, String strValue) {
		Editor prefsPrivateEditor = prefsPrivate.edit();
		prefsPrivateEditor.putString(keyValue, strValue);
		prefsPrivateEditor.commit();
	}

	/**
	 * Method to save the values with that key in SharedPreferences.
	 * 
	 * @param keyValue
	 *            Key name to be stored.
	 * //@param strValue
	 *            Value to be stored with that key name.
	 */

	public void putValue(String keyValue, float dValue) {
		Editor prefsPrivateEditor = prefsPrivate.edit();
		prefsPrivateEditor.putFloat(keyValue, dValue);
		prefsPrivateEditor.commit();
	}

	/**
	 * Method to save the values with that key in SharedPreferences.
	 * 
	 * @param keyValue
	 *            Key name to be stored.
	 * //@param strValue
	 *            Value to be stored with that key name.
	 */

	public void putValue(String keyValue, int dValue) {
		Editor prefsPrivateEditor = prefsPrivate.edit();
		prefsPrivateEditor.putInt(keyValue, dValue);
		prefsPrivateEditor.commit();
	}

	/**
	 * Method to save the boolean value with that key in SharedPreferences.
	 * 
	 * @param keyValue
	 *            Key name to be stored.
	 * @param bValue
	 *            Value to be stored with that key name.
	 */

	public void putValue(String keyValue, boolean bValue) {
		Editor prefsPrivateEditor = prefsPrivate.edit();
		prefsPrivateEditor.putBoolean(keyValue, bValue);
		prefsPrivateEditor.commit();
	}

	/**
	 * Method to get the value from SharedPreferences.
	 * 
	 * @param keyValue
	 *            The key name to be retrieved.
	 * //@param defualtValue
	 *            Default value if key not found.
	 * @return Returns the stored value of the key
	 */

	public boolean getValue(String keyValue, boolean bValue) {
		return prefsPrivate.getBoolean(keyValue, bValue);
	}

	/**
	 * Method to get the value from SharedPreferences.
	 * 
	 * @param keyValue
	 *            The key name to be retrieved.
	 * //@param defualtValue
	 *            Default value if key not found.
	 * @return Returns the stored value of the key
	 */

	public float getValue(String keyValue, float val) {
		return prefsPrivate.getFloat(keyValue, val);
	}

	/**
	 * Method to get the value from SharedPreferences.
	 * 
	 * @param keyValue
	 *            The key name to be retrieved.
	 * //@param defualtValue
	 *            Default value if key not found.
	 * @return Returns the stored value of the key
	 */

	public int getValue(String keyValue, int val) {
		return prefsPrivate.getInt(keyValue, val);
	}

	/**
	 * Method to get the value from SharedPreferences.
	 * 
	 * @param keyValue
	 *            The key name to be retrieved.
	 * @param defualtValue
	 *            Default value if key not found.
	 * @return Returns the stored value of the key
	 */

	public String getValue(String keyValue, String defualtValue) {
		return prefsPrivate.getString(keyValue, defualtValue);
	}

	public void saveUsername(String sUsername) {
		putValue(KEY_LOGGED_USERNAME, sUsername);
	}
	public String getUsername() {
		return getValue(KEY_LOGGED_USERNAME, "");
	}

	public void savePassword(String sPassword) {
		putValue(KEY_LOGGED_PASSWORD, sPassword);
	}
	public String getPassword() {
		return getValue(KEY_LOGGED_PASSWORD, "");
	}

	public void setisDeviceVerified(boolean bDeviceVerified) {putValue(KEY_IS_DEVICE_VERIFIED, bDeviceVerified);}
	public boolean isDeviceVerified() { return getValue(KEY_IS_DEVICE_VERIFIED, false); }

	public void setisLoggedin(boolean bIsloggedin) {
		putValue(KEY_IS_LOGGED_IN, bIsloggedin);
	}
	public boolean isLoggedin() { return getValue(KEY_IS_LOGGED_IN, false); }

	public void setUserData(String sUserData) {
		putValue(KEY_USER_DATA, sUserData);
	}
	public String getUserData() {
		return getValue(KEY_USER_DATA, "");
	}

	public void setProductData(String sProductData) {
		putValue(KEY_PRODUCT_DATA, sProductData);
	}
	public String getProductData() {
		return getValue(KEY_PRODUCT_DATA, "");
	}

	public void setSkuCode(String sSkuCode) {
		putValue(KEY_SKU_CODE, sSkuCode);
	}
	public String getSkuCode() {
		return getValue(KEY_SKU_CODE, "");
	}

	public void setOrderDetails(HashMap<String, String> orderDetailsMap)
	{
		//putValue(KEY_ORDER_DETAILS, a);
		SharedPreferences keyValues = activity.getSharedPreferences(KEY_ORDER_DETAILS, Context.MODE_PRIVATE);
		SharedPreferences.Editor keyValuesEditor = keyValues.edit();
		for (String s : orderDetailsMap.keySet()) {
			keyValuesEditor.putString(s, orderDetailsMap.get(s));
		}
		keyValuesEditor.commit();
	}
	public Map<String, String> getOrderDetails() {
		SharedPreferences keys = activity.getSharedPreferences(KEY_ORDER_DETAILS, Context.MODE_PRIVATE);
		return (Map<String, String>) keys.getAll();
	}
	public boolean checkOrderIdExist(String orderId) {
		boolean isorderIdExsit = false;
		SharedPreferences keys = activity.getSharedPreferences(KEY_ORDER_DETAILS, Context.MODE_PRIVATE);
		 HashMap<String, String> orderdetialsMap = (HashMap<String, String>) keys.getAll();
		if(orderdetialsMap.containsKey(orderId)) { isorderIdExsit = true; }
		return isorderIdExsit;
	}
	public String getOrderStatusByOrderId(String orderId)
	{
		String a = null;
		SharedPreferences keys = activity.getSharedPreferences(KEY_ORDER_DETAILS, Context.MODE_PRIVATE);
		HashMap<String, String> orderdetialsMap = (HashMap<String, String>) keys.getAll();
		String orderdetails = orderdetialsMap.get(orderId);
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(orderdetails);
			a = jsonObject.getString(Constants.KEY_ORDER_STATUS);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return a;
	}
	/*public String updateOrderStatusByOrderId(String orderId, String status)
	{
		String a = null;
		SharedPreferences keys = activity.getSharedPreferences(KEY_ORDER_DETAILS, Context.MODE_PRIVATE);
		HashMap<String, String> orderdetialsMap = (HashMap<String, String>) keys.getAll();
		String orderdetails = orderdetialsMap.get(orderId);
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(orderdetails);
			a = jsonObject.put(Constants.KEY_ORDER_STATUS, status);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return a;
	}*/

	public void setAutoIncreamentId(int sAutoincreamentId) {
		putValue(KEY_AUTO_INCREAMENT_ID, sAutoincreamentId);
	}
	public int getAutoIncreamentId() {
		return getValue(KEY_AUTO_INCREAMENT_ID, 0);
	}

	public void setCurrentOdrerId(String sCurrentOdrerId) {
		putValue(KEY_CURRENT_ORDER_ID, sCurrentOdrerId);
	}
	public String getCurrentOdrerId() {
		return getValue(KEY_CURRENT_ORDER_ID, "");
	}
		/*public void saveMarketplaces(Map<String, String> stringMap) {
				if (stringMap != null) {
				SharedPreferences keys = activity.getSharedPreferences(KEY_MARKETPLACE_LIST, Context.MODE_PRIVATE);
				Editor editor = keys.edit();
				for (String s : stringMap.keySet()) {
				editor.putString(s, stringMap.get(s));
				}
				editor.commit();
				}
				}

		public Map<String, String> loadMarketplaces() {
				SharedPreferences keys = activity.getSharedPreferences(KEY_MARKETPLACE_LIST, Context.MODE_PRIVATE);
				return (Map<String, String>) keys.getAll();
				}*/
	public void clearFilePaths() {
		Editor prefsEditor = prefsPrivate.edit();

		prefsEditor.putString(Preferences.KEY_IS_DEVICE_VERIFIED, "");

		prefsEditor.commit();
	}

	/*
   		get the IMEI no of device
    */
	public String getIMEI()
	{
		TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		String sIMEI = telephonyManager.getDeviceId();
		return sIMEI;
	}

}

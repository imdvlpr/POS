package com.ankit.pointofsolution.config;

public class Constants {

	public static final String APP_NAME = "PointofSales";
	public static final boolean DEBUG = false;
	//jsons objects
	public static final String USERS = "users";
	public static final String PRODUCTS = "products";
	public static final String POS_TOKEN = "posToken";
	public static final String API_STATUS = "status";
	public static final String API_MESSAGE = "message";
	public static final String sIMEI = "123456789098765";


	public static final String CRASH_REPORT_EMAIL_ID = "ankitjain@ssism.org";
	public static final boolean DELETE_FILES_AFTER_UPLOAD = true; 

	public static final int OS_VERSION_KIT_KAT = 19;	
	public static final int OS_VERSION_LOLLIPOP = 21;
	public static final int OS_VERSION_LOLLIPOP_MR2 = 22;
	public static final int OS_VERSION_MARSHMALLOW = 23;
	public static final int OS_VERSION_MARSHMALLOW_MR2 = 24;

	//For Network Operations
	public static final String PING_SUCCESS = "PING_SUCCESS";
	public static final String PING_FAIL = "PING_FAIL";
	public static final String CONNECTION_SUCCESS = "CONNECTION_SUCCESS";
	public static final String CONNECTION_FAIL = "CONNECTION_FAIL";
	//--Network Operations

	public static final String ADD_ITEM_TYPE_1 = "ADD_ITEM_MANULLY";
	public static final String ADD_ITEM_TYPE_2 = "ADD_IN_CATALOG";
	public static final String ADD_COUPONS = "ADD_COUPONS";

	public static final String ORDER_STORAGE_STATUS = "NOT_SYNCED";
	public static final String ORDER_INITIAL_STATUS = "IN_PROGRESS";
	public static final String ORDER_FINAL_STATUS = "COMPLETED";
	// Spinner loop max values
	public static final int QUANTITY_MAX_VALUE = 5;
	public static final Integer INITIAL_ORDER_ID = 100001;

	//Status Keys & values
	public static final String KEY_PRODUCT_DETAILS ="productsdetails";
	public static final String KEY_ORDER_STATUS = "orderStatus";
	public static final String KEY_ORDER_STORAGE_STATUS = "orderStorageStatus";
	public static final String KEY_TIMESTAMP = "orderTimestamp";
	public static final String KEY_EDIT_ORDER = "editorder";
	public static final String KEY_EDIT_ORDER_VALUE = "editorderValue";
	public static final String KEY_CASH_PAYMENT = "CASH_PAYMENT";
	public static final String KEY_DEBIT_CARD_PAYMENT = "DEBIT_CARD_PAYMENT";
	public static final String KEY_PAYMENT_STATUS = "SUCCESS";
	//db helpers




}

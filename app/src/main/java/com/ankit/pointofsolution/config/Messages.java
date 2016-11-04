package com.ankit.pointofsolution.config;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class Messages {

	public static final String ERROR_PRECEDE = "Error - ";
	public static final String IS_SERVER_ALIVE = "Please wait for a moment, Checking weather server is active or not.";
	public static final String NO_INTERNET = "Unable to process. Internet connection unavailable. ";
	public static final String NO_API = "Unable to process. Server API connection problem occured.";
	public static final String CONN_TIMEDOUT = "Connection timeout.";
	public static final String SERVER_NOT_AVAILABLE = "Server is down. Please contact administrator.";

	public static final String CONFIRM_CLOSE_APP = "Do you want to close the application?";
	public static final String PLZ_WAIT_AUTHENTICATE = "Please wait while authenticating...";
	public static final String PLZ_WAIT_UPLOADING = "Please wait while uploading...";
	public static final String PLZ_WAIT_DOWNLOADING = "Please wait while downloading...";
	public static final String ERROR_GENERAL = "Error occured while processing.";
	public static final String UNAUTHORIZED_ACCESS = "Unauthorized: Access is denied due to invalid credentials\"";
	public static final String DEVICE_DEACTIVE = "Device deactived. Please contact admin!";

	public static final String ENTER_VERIFICATION_CODE = "Verification Code cannot be empty";

	public static final String INVALID_USERNAME_PWD = "Invalid username/password or inactive user!";
	//public static final String ACCESS_DENIED = "Access denied";
	public static final String ACCESS_DENIED = "Incorrect verification code. Please contact admin!";
	// Alerts
	public static final String MSG_INVALID_USER = "You are an inactive user!";
	public static final String NO_ITEMS_IN_CART = "Cart is empty. please add items in cart.";
	public static final String CHECK_CART = "Please check your previous order.";
	public static final String EMPTY_ORDERS = "Orders not placed yet!";
	public static final String INVALID_COUPON = "Invalid Coupon!";
	public static final String EXPIRE_COUPON = "Coupon Expired!";
	public static final String REPLACE_COUPON = "One coupon is already exist. Do you want to replace coupon?";
	public static final String EXIST_COUPON = "Replace Coupon";
	public static final String MAIL_SUBJECT = "Thanks for order";


	//Sync messages
	public static final String SYNC_SUCCESS = "Synced successfully!";




	public static String getEmptyText(String str) {
		return String.format("Please enter %s!", str);
	}

	public static String getEmptyChoose(String str) {
		return String.format("Please choose %s!", str);
	}

	public static String getInvalidText(String str) {
		return String.format("Invalid %s value", str);
	}

	public static String getInvalidConfigText(String str) {
		return String.format("%s test not configured properly.", str);
	}

	public static String getInvalidChoose(String str) {
		return String.format("Please choose valid %s!", str);
	}

	public static String err(Exception e) {
		return ERROR_PRECEDE + e.toString();
	}

	public static String errMsg(Exception ex) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		ex.printStackTrace(printWriter);
		String stacktrace = result.toString();
		printWriter.close();
		return ERROR_PRECEDE + stacktrace;
	}
}

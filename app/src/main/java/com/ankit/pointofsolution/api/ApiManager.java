package com.ankit.pointofsolution.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.ankit.pointofsolution.LoginActivity;
import com.ankit.pointofsolution.config.Constants;
import com.ankit.pointofsolution.config.Messages;
import com.ankit.pointofsolution.storage.Preferences;
import com.ankit.pointofsolution.utility.L;
import com.ankit.pointofsolution.utility.Validator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.FileNotFoundException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Ankit on 26-Aug-16.
 */
public class ApiManager {
    public static final String API_SERVICE_URL = "https://stage.gogreenbasket.com/api/v1/pos_services/";
    public static final String PING_URL = "is_live";
    public static final String DEVICE_AUTHENTICATION = "authenticate_pos";
    public static final String IMPORT_DATA = "users_data";
    private Activity activity = null;
    private Context context = null;

    private String apiMethodName, apiParams;
    private LinkedHashMap<String, String> apiParamsList;
    private String errorMsg, errorStackMsg = "";
    private Exception api_exception;
    private String response;
    private Status status;

    public boolean Authenticate_device(String verificationcode){
        return false;
    }

    public ApiManager(Activity activity) {
        this.activity = activity;
    }

    public ApiManager(Context context) {
        this.context = context;
    }


    /**
     * Method which initialize the request to server and process. It will
     * returns the status whether the request success of error.F
     *
     * @return Status value.
     */
    public Status processDeviceAuth(String sVerificationCode) {
        this.status = Status.NONE;
        String sIMEI = "917239378970939";//getIMEI();

         String strLoginUrl = API_SERVICE_URL +  DEVICE_AUTHENTICATION;
         try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(strLoginUrl);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("imei", sIMEI));
            nameValuePairs.add(new BasicNameValuePair("uid", sVerificationCode));

            L.log(sIMEI);

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse responseObj = httpclient.execute(httppost);
            int status_code = responseObj.getStatusLine().getStatusCode();
            L.log("status_code : " + status_code);
            if (status_code == 200) {
                HttpEntity resEntity = responseObj.getEntity();
                response = EntityUtils.toString(resEntity);
            }
            if (Validator.isEmpty(response)) {

                this.status = Status.ERROR;
                this.errorMsg = Messages.ACCESS_DENIED;
            } else {
                this.status = Status.SUCCESS;
                // On Success import data f
                processImportdata();
            }
        } catch (UnknownHostException uhEx) {
            L.error(uhEx);
            this.status = Status.ERROR;
            this.errorMsg = Messages.NO_INTERNET;
        } catch (HttpHostConnectException hhcEx) {
            L.error(hhcEx);
            this.status = Status.ERROR;
            this.errorMsg = Messages.SERVER_NOT_AVAILABLE;
        } catch (HttpResponseException httpResEx) {
            L.error(httpResEx);
            this.status = Status.ERROR;
            this.errorMsg = Messages.SERVER_NOT_AVAILABLE;
        } catch (SocketException exTO) {
            L.error(exTO);
            this.status = Status.ERROR;
            this.errorMsg = Messages.ERROR_PRECEDE
                    + Messages.SERVER_NOT_AVAILABLE;
        } catch (SocketTimeoutException exTO) {
            L.error(exTO);
            this.status = Status.ERROR;
            this.errorMsg = Messages.ERROR_PRECEDE + Messages.CONN_TIMEDOUT;
        } catch (ConnectTimeoutException exTO) {
            L.error(exTO);
            this.status = Status.ERROR;
            this.errorMsg = Messages.ERROR_PRECEDE + Messages.CONN_TIMEDOUT;
        } catch (FileNotFoundException ex) {
            L.error(ex);
            this.status = Status.ERROR;
            this.errorMsg = Messages.ERROR_PRECEDE + Messages.NO_API;
        } catch (Exception ex) {
            L.error(ex);
            this.status = Status.ERROR;
            this.errorMsg = Messages.ERROR_GENERAL;
        }
        L.log("status = " + status);
        return status;
    }

    /**
     * Method which initialize the request to server and process. It will
     * returns the status whether the request success of error.F
     *
     * @return Status value.
     */
    public Status processImportdata() {
        this.status = Status.NONE;
        //getIMEI();

        String strLoginUrl = API_SERVICE_URL +  IMPORT_DATA;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(strLoginUrl);

            httpget.addHeader("posToken","123456789");

            HttpResponse responseObj = httpclient.execute(httpget);
            int status_code = responseObj.getStatusLine().getStatusCode();
            L.log("status_code : " + status_code);
            if (status_code == 200) {
                HttpEntity resEntity = responseObj.getEntity();
                response = EntityUtils.toString(resEntity);
            }
            if (Validator.isEmpty(response)) {

                this.status = Status.ERROR;
                this.errorMsg = Messages.ACCESS_DENIED;
            } else {
                this.status = Status.SUCCESS;
            }
        } catch (UnknownHostException uhEx) {
            L.error(uhEx);
            this.status = Status.ERROR;
            this.errorMsg = Messages.NO_INTERNET;
        } catch (HttpHostConnectException hhcEx) {
            L.error(hhcEx);
            this.status = Status.ERROR;
            this.errorMsg = Messages.ACCESS_DENIED;
        } catch (HttpResponseException httpResEx) {
            L.error(httpResEx);
            this.status = Status.ERROR;
            this.errorMsg = Messages.ACCESS_DENIED;
        } catch (SocketException exTO) {
            L.error(exTO);
            this.status = Status.ERROR;
            this.errorMsg = Messages.ERROR_PRECEDE
                    + Messages.ACCESS_DENIED;
        } catch (SocketTimeoutException exTO) {
            L.error(exTO);
            this.status = Status.ERROR;
            this.errorMsg = Messages.ERROR_PRECEDE + Messages.CONN_TIMEDOUT;
        } catch (ConnectTimeoutException exTO) {
            L.error(exTO);
            this.status = Status.ERROR;
            this.errorMsg = Messages.ERROR_PRECEDE + Messages.CONN_TIMEDOUT;
        } catch (FileNotFoundException ex) {
            L.error(ex);
            this.status = Status.ERROR;
            this.errorMsg = Messages.ERROR_PRECEDE + Messages.NO_API;
        } catch (Exception ex) {
            L.error(ex);
            this.status = Status.ERROR;
            this.errorMsg = Messages.ERROR_GENERAL;
        }
        L.log("status = " + status);
        return status;
    }


    /**
     * Enumeration values for the request status.
     *
     */
    public enum Status {
        NONE, SUCCESS, FAILED, ERROR
    };
    public String getIMEI() {

        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String sIMEI = telephonyManager.getDeviceId();
        return sIMEI;
    }
    /**
     * To get the status of the request.
     *
     * @return Returns the Status value of request.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * To get the full error stack.
     *
     * @return Returns the error stack.
     */
    public String getErrorStack() {
        return errorStackMsg;
    }

    /**
     * To get the expection occured while sending request.
     *
     * @return Exception of request.
     */
    public Exception getException() {
        return api_exception;
    }

    /**
     * To get the response returned from the server.
     *
     * @return Returns the response string.
     */
    public String getResponse() {
        return response;
    }

    /**
     * To get the error message.
     *
     * @return Error message.
     */
    public String getErrorMessage() {
        return errorMsg;
    }

    /*
    Logout function
     */
    public void logout(Preferences pref)
    {
        pref.saveUsername("");
        pref.setisLoggedin(false);
        Intent i = new Intent(activity, LoginActivity.class);
        activity.startActivity(i);
        activity.finish();

    }

}

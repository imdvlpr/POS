package com.ankit.pointofsolution.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ankit.pointofsolution.LoginActivity;
import com.ankit.pointofsolution.config.Constants;
import com.ankit.pointofsolution.config.Messages;
import com.ankit.pointofsolution.storage.DBHelper;
import com.ankit.pointofsolution.storage.Preferences;
import com.ankit.pointofsolution.utility.L;
import com.ankit.pointofsolution.utility.Utility;
import com.ankit.pointofsolution.utility.Validator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Ankit on 26-Aug-16.
 */
public class ApiManager {
    public static final String API_SERVICE_URL = "https://stage.gogreenbasket.com/api/v1/pos/";//https://stage.gogreenbasket.com/api/v1/pos/
    public static final String PING_URL = "is_live";
    public static final String DEVICE_AUTHENTICATION = "authenticate_pos";
    public static final String USERS_DATA = "users_data";
    public static final String STORE_PRODUCT_DATA = "store_products";
    private Activity activity = null;
    private Context context = null;

    private String errorMsg, errorStackMsg = "";
    private String response;
    private Status status;
    private Preferences pref;
    private Utility utility;
    private DBHelper dbHelper;
    String posToken;

    public ApiManager(Activity activity) {
        this.activity = activity;
        pref = new Preferences(activity);
        dbHelper = new DBHelper(activity);
        utility = new Utility(pref, dbHelper);
    }

    /**
     * Method which initialize the request to server and process. It will
     * returns the status whether the request success of error.F
     *
     * @return Status value.
     */
    public Status processDeviceAuth(String sVerificationCode) {
        this.status = Status.NONE;
        //pref.getIMEI()
        String json= "";

         String strLoginUrl = API_SERVICE_URL +  DEVICE_AUTHENTICATION;
         try {
           /* HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(strLoginUrl);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("imei", sIMEI));
            nameValuePairs.add(new BasicNameValuePair("deviceVerificationId", sVerificationCode));
            httppost.setHeader("Content-type", "application/json");
            L.log(sIMEI);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse responseObj = httpclient.execute(httppost);*/
             JSONObject jsonObject = new JSONObject();

                 //HttpResponse response;
                 jsonObject.accumulate("imei", Constants.sIMEI);
                 jsonObject.accumulate("deviceVerificationId", sVerificationCode);
                 json = jsonObject.toString();
                 System.out.println("json:"+json);
                 HttpClient httpClient = new DefaultHttpClient();
                 HttpPost httpPost = new HttpPost(strLoginUrl);
                 httpPost.setEntity(new StringEntity(json, "UTF-8"));
                 httpPost.setHeader("Content-Type", "application/json");
                 httpPost.setHeader("Accept-Encoding", "application/json");
                 httpPost.setHeader("Accept-Language", "en-US");
                 HttpResponse responseObj = httpClient.execute(httpPost);
                 //String sresponse = response.getEntity().toString();

            int status_code = responseObj.getStatusLine().getStatusCode();
             System.out.println("status_code : " + status_code);
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
                // On Success import data
                JSONObject jsonObj = new JSONObject(response);
                System.out.println("response:"+response);
                if(jsonObj.has(Constants.POS_TOKEN))
                {
                    posToken = jsonObj.getString(Constants.POS_TOKEN);

                    //Set postoken for future reference.
                    pref.setPosToken(posToken);
                    //call functions to get responses from the APIs
                    processImportUsersData();
                    processImportItemData();
                }else if(jsonObj.has(Constants.API_STATUS))
                {
                    this.status = Status.ERROR;
                    this.errorMsg = jsonObj.getString(Constants.API_MESSAGE);
                }else{
                    this.status = Status.ERROR;
                    this.errorMsg = Messages.ACCESS_DENIED;
                }
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
    public Status processImportUsersData() {
        this.status = Status.NONE;
        //getIMEI();

        String strLoginUrl = API_SERVICE_URL +  USERS_DATA;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(strLoginUrl);

            httpget.addHeader("posToken",pref.getPosToken());
            httpget.addHeader("imei",Constants.sIMEI);

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
                JSONObject jsonObj = new JSONObject(response);
                if(jsonObj.has(Constants.USERS)) {
                    this.status = Status.SUCCESS;
                    //System.out.println("response users:"+ response);
                    utility.saveUsersindb(response);
                }else if(jsonObj.has(Constants.API_STATUS))
                {
                    this.status = Status.ERROR;
                    this.errorMsg = jsonObj.getString(Constants.API_MESSAGE);
                }else{
                    this.status = Status.ERROR;
                    this.errorMsg = Messages.ACCESS_DENIED;
                }
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
     * Method which initialize the request to server and process. It will
     * returns the status whether the request success of error.F
     *
     * @return Status value.
     */
    public Status processImportItemData() {
        this.status = Status.NONE;
        //getIMEI();

        String strLoginUrl = API_SERVICE_URL +  STORE_PRODUCT_DATA;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(strLoginUrl);
            System.out.println("pref.getPosToken():"+pref.getPosToken());
            httpget.addHeader("posToken",pref.getPosToken());
            httpget.addHeader("imei",Constants.sIMEI);

            HttpResponse responseObj = httpclient.execute(httpget);
            int status_code = responseObj.getStatusLine().getStatusCode();
            System.out.println("status_code : " + status_code);
            L.log("status_code : " + status_code);
            if (status_code == 200) {
                HttpEntity resEntity = responseObj.getEntity();
                response = EntityUtils.toString(resEntity);
            }
            System.out.println("response items:"+ response);
            if (Validator.isEmpty(response)) {
                this.status = Status.ERROR;
                this.errorMsg = Messages.ACCESS_DENIED;
            } else {
                JSONObject jsonObj = new JSONObject(response);
                if(jsonObj.has(Constants.PRODUCTS)) {
                    this.status = Status.SUCCESS;
                    System.out.println("response items123:"+ response);
                    utility.saveItemsindb(response);
                }else if(jsonObj.has(Constants.API_STATUS))
                {
                    this.status = Status.ERROR;
                    this.errorMsg = jsonObj.getString(Constants.API_MESSAGE);
                }else{
                    this.status = Status.ERROR;
                    this.errorMsg = Messages.ACCESS_DENIED;
                }
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

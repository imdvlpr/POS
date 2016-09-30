package com.ankit.pointofsolution.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.api.ApiManager;
import com.ankit.pointofsolution.config.Messages;
import com.ankit.pointofsolution.config.StringUtils;

import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

/**
 * Created by Ankit on 26-Aug-16.
 */
public class NetworkOperations {
    private Context context;
    private Activity activity;
    private static final String LOG_TAG = "NetworkOperationsTag";
    private static final String URL = ApiManager.API_SERVICE_URL+ApiManager.PING_URL;
    public boolean status;
    private String errorMsg = "";

    public NetworkOperations(Activity activity) { this.activity = activity;}
    public NetworkOperations(Context context) { this.context = context;}

    public boolean isInternetAvailable() {
        NetworkInfo info = null;
        if (activity != null)
            info = ((ConnectivityManager) activity
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
        else
            info = ((ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return false;
        else
            return true;
    }

    public boolean hasActiveInternetConnection(final Context context) {

        if (isInternetAvailable()) {
           // new Thread(new Runnable() {
                //Message msg = new Message();
               // Bundle bndle = new Bundle();
                //@Override
               // public void run() {
                    try {

                        HttpURLConnection urlc = (HttpURLConnection) (new URL(URL).openConnection());
                        urlc.setRequestProperty("User-Agent", "Test");
                        urlc.setRequestProperty("Connection", "close");
                        //urlc.setRequestMethod("GET");
                        urlc.setConnectTimeout(1500);
                        urlc.connect();
                    if(urlc.getResponseCode() == 200) {
                            status = true;
                        }else {
                        status = false;
                        errorMsg = Messages.SERVER_NOT_AVAILABLE;
                    }
                    }
                    catch (UnknownHostException uhEx) {
                        L.error(uhEx);
                        status = false;
                        errorMsg = Messages.NO_INTERNET;
                    } catch (HttpHostConnectException hhcEx) {
                        L.error(hhcEx);
                        status = false;
                        errorMsg = Messages.SERVER_NOT_AVAILABLE;
                    } catch (HttpResponseException httpResEx) {
                        L.error(httpResEx);
                        status = false;
                        errorMsg = Messages.SERVER_NOT_AVAILABLE;
                    } catch (SocketException exTO) {
                        L.error(exTO);
                        status = false;
                        errorMsg = Messages.ERROR_PRECEDE
                                + Messages.SERVER_NOT_AVAILABLE;
                    } catch (SocketTimeoutException exTO) {
                        L.error(exTO);
                        status = false;
                        errorMsg = Messages.ERROR_PRECEDE + Messages.CONN_TIMEDOUT;
                    } catch (ConnectTimeoutException exTO) {
                        L.error(exTO);
                        status = false;
                        errorMsg = Messages.ERROR_PRECEDE + Messages.CONN_TIMEDOUT;
                    } catch (FileNotFoundException ex) {
                        L.error(ex);
                        status = false;
                        errorMsg = Messages.ERROR_PRECEDE + Messages.NO_API;
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error checking internet connection", e);
                    } catch (Exception ex) {
                        L.error(ex);
                        status = false;
                        errorMsg = Messages.ERROR_GENERAL;
                    }
                //}
            // }).start();

        } else {
            status = false;
            errorMsg = Messages.NO_INTERNET;
            Log.d(LOG_TAG, "No network available!");
        }
        return status;
    }
    /**
     * To get the error message.
     *
     * @return Error message.
     */
    public String getErrorMessage() {
        return errorMsg;
    }

    /**
     * To get the Status message.
     *
     * @return Status message.
     */
    public boolean getStatus() {
        return status;
    }

}

package com.ankit.pointofsolution;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ankit.pointofsolution.Models.Userdata;
import com.ankit.pointofsolution.api.ApiManager;
import com.ankit.pointofsolution.api.ResponseCodes;
import com.ankit.pointofsolution.config.Constants;
import com.ankit.pointofsolution.config.Messages;
import com.ankit.pointofsolution.config.StringUtils;
import com.ankit.pointofsolution.storage.Preferences;
import com.ankit.pointofsolution.utility.NetworkOperations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;

public class DeviceVerificationActivity extends AppCompatActivity  {

    private CoordinatorLayout coordinatorLayout;
    private NetworkOperations noptn;
    EditText VerificationCode;
    private Button verifyBtn;
    private String verficationcode;
    public ApiManager apiManager;
    private String sResponseCode,sResponseDesc;
    private Activity activity;
    private Context context;
    private ProgressDialog prgLoading = null;
    private Preferences preferences;
    JSONObject jsonObj;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_verification);
        noptn = new NetworkOperations(DeviceVerificationActivity.this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        apiManager = new ApiManager(this);
        activity = this;
        verifyBtn = (Button) findViewById(R.id.Verification_button);
        VerificationCode = (EditText) findViewById(R.id.VerificationCode);
        preferences = new Preferences(activity);

        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verficationcode = VerificationCode.getText().toString();
                if (!verficationcode.isEmpty()) {
                    checkforPremission();
                    }
                    else {
                        showSnack(false, Messages.ENTER_VERIFICATION_CODE);
                    }
            }
        });
    }



    private void showSnack(boolean isConnected,String status)
        {
         Snackbar snackbar = Snackbar.make(coordinatorLayout, status, Snackbar.LENGTH_LONG);
         View sbView = snackbar.getView();
         TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
         textView.setTextColor(Color.RED);
         snackbar.show();
         Toast.makeText(this,status,Toast.LENGTH_LONG).show();
         }
    /**
     * To show the loading dialog with given message
     *
     * @param msg
     *            Message to show when showing dialog.
     */
    public void showLoading(String msg) {

        if (prgLoading != null && prgLoading.isShowing())
            prgLoading.setMessage(msg);
        else
            prgLoading = ProgressDialog.show(activity, "", msg);

    }
    /**
     * To close the loading dialog
     */

    protected void hideLoading() {
        try {
            if (prgLoading != null && prgLoading.isShowing()) {
                prgLoading.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkforPremission() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("Read Phone State");
        else  doWork();

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                /*showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {*/
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            /*}
                        });*/
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }

        //insertDummyContact();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted we can use our function here
                    //insertDummyContact();
                    doWork();
                } else {
                    // Permission Denied
                    Toast.makeText(DeviceVerificationActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DeviceVerificationActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public void doWork(){
        //Checks for Internet connectivity.
        if (noptn.hasActiveInternetConnection(DeviceVerificationActivity.this)) {
            //ToDo write your code here to check
            // Create Inner Thread Class
            //Show loader
            showLoading(Messages.PLZ_WAIT_AUTHENTICATE);
            Thread background = new Thread(new Runnable() {
                Message msg = new Message();
                Bundle bndle = new Bundle();

                @Override
                public void run() {
                    ApiManager.Status status = apiManager.processDeviceAuth(verficationcode);
                    if (status == ApiManager.Status.ERROR) {
                        sResponseCode = StringUtils.ERROR_CODE;
                        sResponseDesc = apiManager.getErrorMessage();
                        bndle.putString(StringUtils.CODE, sResponseCode);
                        bndle.putString(StringUtils.DESC, sResponseDesc);
                    } else {
                        sResponseCode = StringUtils.SUCCESS;
                        sResponseDesc = apiManager.getResponse();
//                                    System.out.println("sResponse: " + sResponseDesc);
                    }
                    msg.setData(bndle);
                    bndle.putString(StringUtils.DESC, sResponseDesc);
                    bndle.putString(StringUtils.CODE, sResponseCode);
                    handler.sendMessage(msg);
                }

                // Define the Handler that receives messages from the thread and update the progress
                private Handler handler = new Handler() {

                    public void handleMessage(Message msg) {

                        sResponseCode = msg.getData().getString(StringUtils.CODE);
                        sResponseDesc = msg.getData().getString(StringUtils.DESC);
                        if (sResponseCode.equals(StringUtils.SUCCESS)) {
                            if ((null != sResponseDesc)) {
                               // System.out.println("sResponseDesc:" + sResponseDesc);
                                // Store data in shared preference
                                try {
                                    jsonObj = new JSONObject(sResponseDesc);
                                    preferences.setUserData(jsonObj.getJSONArray(Constants.USERS).toString());
                                    preferences.setProductData(jsonObj.getJSONArray(Constants.PRODUCTS).toString());
                                } catch (Exception e) {
                                }
                                preferences.setisDeviceVerified(true);
                                startActivity(new Intent(DeviceVerificationActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                // ALERT MESSAGE
                                showSnack(false, Messages.ACCESS_DENIED);
                            }
                        } else if (sResponseCode.equals(StringUtils.ERROR_CODE)) {
                            showSnack(false, apiManager.getErrorMessage());
                        }
                        hideLoading();
                    }
                };

            });
            background.start();
        } else {
            showSnack(false, noptn.getErrorMessage());
        }
    }
}

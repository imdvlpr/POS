package com.ankit.pointofsolution.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.ankit.pointofsolution.api.ApiManager;
import com.ankit.pointofsolution.config.Messages;
import com.ankit.pointofsolution.config.StringUtils;
import com.ankit.pointofsolution.storage.DBHelper;
import com.ankit.pointofsolution.storage.Preferences;

import java.util.ArrayList;

/**
 * Created by Ankit on 19-Oct-16.
 */

public class SyncAdapter extends AsyncTask<String, String, String> {

    public ApiManager apiManager;
    public ArrayList<String> values;
    private ProgressDialog pDialog;
    private String sResponseCode, sResponseDesc;
    private Utility utility;
    private Preferences preferences;
    private Activity activity;
    private DBHelper dbHelper;
    String status;
    String sApitocall;

    public SyncAdapter(Activity activity, String apitocall) {
        this.activity = activity;
        apiManager = new ApiManager(activity);
        preferences=new Preferences(activity);
        dbHelper=new DBHelper(activity);
        utility=new Utility(preferences,dbHelper);
        sApitocall = apitocall;
    }
    public SyncAdapter(Activity activity) {
        this.activity = activity;
        apiManager = new ApiManager(activity);
        preferences=new Preferences(activity);
        dbHelper=new DBHelper(activity);
        utility=new Utility(preferences,dbHelper);
    }

    @Override
    protected void onPreExecute() {
        /*super.onPreExecute();
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Please wait while Records Synching with Server.");
        pDialog.show();*/
    }

    @Override
    protected String doInBackground(String... params) {

            //ToDo write your code here to check
            // Create Inner Thread Class\
            Thread background = new Thread(new Runnable() {
                Message msg = new Message();
                Bundle bndle = new Bundle();
                ApiManager.Status APIstatus;
                @Override
                public void run() {
                    /*switch (sApitocall)
                    {
                        case "product":
                            APIstatus = apiManager.processImportItemData();
                            break;
                        case "orders":
                            APIstatus = apiManager.processExpotOrdersData(utility.getAllOrdersJson());
                            break;
                        case "customers":
                            APIstatus = apiManager.processExpotCustomerData();
                            break;
                        default:
                            APIstatus = ApiManager.Status.ERROR;
                    }*/
                    ApiManager.Status APIstatus = apiManager.processImportItemData();
                    if (APIstatus == ApiManager.Status.ERROR) {
                        sResponseCode = StringUtils.ERROR_CODE;
                        sResponseDesc = apiManager.getErrorMessage();
                        bndle.putString(StringUtils.CODE, sResponseCode);
                        bndle.putString(StringUtils.DESC, sResponseDesc);
                    } else {
                        sResponseCode = StringUtils.SUCCESS;
                        sResponseDesc = apiManager.getResponse();
                    }
                    msg.setData(bndle);
                    bndle.putString(StringUtils.DESC, sResponseDesc);
                    bndle.putString(StringUtils.CODE, sResponseCode);
                    handler.sendMessage(msg);
                }

                // Define the Handler that receives messages from the thread and update the progress
                private Handler handler = new Handler(Looper.getMainLooper()) {

                    public void handleMessage(Message msg) {

                        sResponseCode = msg.getData().getString(StringUtils.CODE);
                        sResponseDesc = msg.getData().getString(StringUtils.DESC);
                        if (sResponseCode.equals(StringUtils.SUCCESS)) {
                            if ((null != sResponseDesc)) {
                                status = sResponseCode;
                                 System.out.println("sResponseDesc:" + sResponseCode);
                                 hideLoading(status);
                            }else{
                                status = StringUtils.ERROR_CODE;
                                hideLoading(status);
                            }
                        }else{
                            status = StringUtils.ERROR_CODE;
                           // hideLoading(status);
                        }
                    }

                };
            });
            background.start();

        return status;
    }
    /**
     * To close the loading dialog
     */

    protected void hideLoading(String status) {
        try {
            //status = this.status;
            System.out.println("onPostExecute"+ this.status );
            if(status!=null)
            {
                Toast.makeText(activity,"Data Synced to server successfully.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(activity,Messages.UNAUTHORIZED_ACCESS, Toast.LENGTH_LONG).show();
            }
            //pDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void onPostExecute(String status) {

       // pDialog.dismiss();
    }


}

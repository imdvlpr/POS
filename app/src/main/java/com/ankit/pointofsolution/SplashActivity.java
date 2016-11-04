package com.ankit.pointofsolution;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ankit.pointofsolution.storage.DBHelper;
import com.ankit.pointofsolution.storage.Preferences;

public class SplashActivity extends AppCompatActivity {
    private Preferences preferences;
    private Intent intent;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences = new Preferences(SplashActivity.this);
        dbHelper = new DBHelper(this);
        dbHelper.getAllContacts();
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    System.out.println("preferences:"+ preferences.toString());
                    boolean login_status = preferences.isLoggedin();
                    if(!login_status) {
                        System.out.println("preferences.isDeviceVerified() : "+preferences.isDeviceVerified());
                        if (preferences.isDeviceVerified()) {
                            intent = new Intent(SplashActivity.this, LoginActivity.class);
                        } else {
                            intent = new Intent(SplashActivity.this, DeviceVerificationActivity.class);
                        }
                     }else {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    }
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}

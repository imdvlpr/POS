package com.ankit.pointofsolution;

import android.content.Intent;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        dbHelper.getAllCotacts();
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    if(!preferences.isLoggedin()) {
//                        System.out.println("preferences.isDeviceVerified() : "+preferences.isDeviceVerified());
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

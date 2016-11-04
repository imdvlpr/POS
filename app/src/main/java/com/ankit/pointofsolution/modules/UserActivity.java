package com.ankit.pointofsolution.modules;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.ankit.pointofsolution.Models.Userdata;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.adapter.UsersAdapter;
import com.ankit.pointofsolution.storage.DBHelper;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    public static ArrayList<Userdata> userDetailsArrayList;
    private static DBHelper dbHelper;
    private static ListView listView;
    public static UsersAdapter usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        dbHelper=new DBHelper(this);
        Resources res = getResources();
        userDetailsArrayList = dbHelper.getalluser();
        listView = (ListView) findViewById(R.id.user_list);
        usersAdapter = new UsersAdapter(this,userDetailsArrayList,res);
        listView.setAdapter(usersAdapter);
        //usersAdapter.notifyDataSetChanged();
    }
}

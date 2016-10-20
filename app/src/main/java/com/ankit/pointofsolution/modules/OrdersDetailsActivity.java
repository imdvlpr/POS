package com.ankit.pointofsolution.modules;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.ankit.pointofsolution.Models.OrderDetails;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.adapter.CustomAdapter;
import com.ankit.pointofsolution.storage.DBHelper;

import java.util.ArrayList;

public class OrdersDetailsActivity extends AppCompatActivity {

    DBHelper dbHelper;
    private ListView listView;
    private CustomAdapter customAdapter;
    private Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DBHelper(this);
        res = getResources();
        final String s= getIntent().getExtras().get("orderID").toString();
        TextView orderId = (TextView) findViewById(R.id.orderId);

        orderId.setText(s);
        ArrayList<OrderDetails> orderDetailsArrayList = dbHelper.getOrderDetailsByOrderId(s);
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.orderDetailsList);
        listView = (ListView) findViewById(R.id.orderDetailsList);
        customAdapter = new CustomAdapter(this, orderDetailsArrayList, res);
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listof_orders, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

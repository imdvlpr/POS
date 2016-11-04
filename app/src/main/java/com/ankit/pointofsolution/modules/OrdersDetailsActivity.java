package com.ankit.pointofsolution.modules;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ankit.pointofsolution.Models.OrderDetails;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.adapter.CustomAdapter;
import com.ankit.pointofsolution.storage.DBHelper;

import java.util.ArrayList;

public class OrdersDetailsActivity extends AppCompatActivity {

    DBHelper dbHelper;
    //private ListView listView;
    // private CustomAdapter customAdapter;
    private CustomAdapter customAdapter;
    public static ExpandableListView listView;
    public static  ArrayList<OrderDetails> orderDetailsArrayList;
    private Resources res;
    //public static ExpandableListView listView;
    public static TextView oTotalconut,oitemCount,orderDate;
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
        orderDetailsArrayList = dbHelper.getOrderDetailsByOrderId(s);
        String orderDateByOrderId = dbHelper.getOrderDateByOrderId(s);


        // Get ListView object from xml
        oTotalconut = (TextView) findViewById(R.id.totalconut);
        oitemCount   = (TextView) findViewById(R.id.itemcountno);
        orderDate = (TextView) findViewById(R.id.orderDate);
        String[] sorderDate = orderDateByOrderId.split(" ");
        orderDate.setText(sorderDate[0]);
        listView = (ExpandableListView) findViewById(R.id.list);

        // listView = (ListView) findViewById(R.id.orderDetailsList);
        // customAdapter = new CustomAdapter(this, orderDetailsArrayList, res);
        customAdapter = new CustomAdapter(this, orderDetailsArrayList, res, this);
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
        for(int i=0;i<orderDetailsArrayList.size();i++)
        {
            if(orderDetailsArrayList.get(i).getCouponsArrayList()!=null)
            {
                listView.expandGroup(i);
            }
        }
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                return true;
            }
        });
        // Listview Group expanded listener
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });
        // Listview Group collasped listener
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });
        // Listview on child click listener
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                return false;
            }
        });
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

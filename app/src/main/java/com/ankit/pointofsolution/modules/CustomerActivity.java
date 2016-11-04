package com.ankit.pointofsolution.modules;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ankit.pointofsolution.MainActivity;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.api.ApiManager;
import com.ankit.pointofsolution.config.Messages;
import com.ankit.pointofsolution.storage.DBHelper;
import com.ankit.pointofsolution.storage.Preferences;
import com.ankit.pointofsolution.utility.NetworkOperations;
import com.ankit.pointofsolution.utility.SyncAdapter;
import com.ankit.pointofsolution.utility.Utility;

public class CustomerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText customername,customeremail,customerstreet,customerplace,customerphone;
    private String scustomername=null,scustomeremail=null,
                    scustomerstreet=null,scustomerplace=null,
                    scustomerphone=null;
    private DBHelper dbHelper;
    private ApiManager apiManager;
    Preferences pref;
    public DrawerLayout drawer;
    Utility utility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        apiManager = new ApiManager(this);
        pref = new Preferences(this);
        utility = new Utility();
        customername = (EditText)findViewById(R.id.customername);
        customeremail = (EditText) findViewById(R.id.customeremail);
        customerstreet = (EditText) findViewById(R.id.customerstreet);
        customerplace = (EditText) findViewById(R.id.customerplace);
        customerphone = (EditText) findViewById(R.id.customerphone);

        Button addcustomerButton = (Button) findViewById(R.id.addcustomer);
        addcustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //get the values from the add item form
                    scustomername = customername.getText().toString();
                    scustomeremail = customeremail.getText().toString();
                    scustomerstreet = customerstreet.getText().toString();
                    scustomerplace = customerplace.getText().toString();
                    scustomerphone = customerphone.getText().toString();

                    if(!scustomerphone.equals(""))
                    {
                    System.out.println("customerList: " + scustomername + ", " + scustomeremail + " , " + scustomerstreet);
                    dbHelper = new DBHelper(getApplicationContext());
                    dbHelper.insertCustomer(scustomername, scustomeremail, scustomerphone, scustomerstreet, scustomerplace);
                    Toast.makeText(CustomerActivity.this, "Customer Information Successfully Inserted", Toast.LENGTH_SHORT)
                            .show();
                    startActivity(new Intent(CustomerActivity.this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(CustomerActivity.this, "Please enter atleast phone no.", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.common, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            // Handle the cart action
            Intent i = new Intent(CustomerActivity.this,MainActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_return) {

        } else if (id == R.id.nav_reports) {

            //working code checks weather orders exist .....
            if(dbHelper.getAllOrders().size()>0) {
                Intent i = new Intent(this, ListofOrdersActivity.class);
                startActivity(i);
            }else
            {
                Toast.makeText(this, Messages.EMPTY_ORDERS,Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.nav_reciept) {

        }else if (id == R.id.nav_customer) {
            Intent i = new Intent(this, CustomerActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_logout) {
            apiManager.logout(pref);
        } else if (id == R.id.nav_help) {
        }
        else if (id == R.id.sync) {
            NetworkOperations noptn = new NetworkOperations(this);
            if (noptn.hasActiveInternetConnection(this)) {
                new SyncAdapter(this).execute();
                /*SyncAdapter syncAdapter = new SyncAdapter(this, "products");
                syncAdapter.execute();
                utility.StartAsyncTaskInParallel(syncAdapter);
                SyncAdapter syncAdapter1 = new SyncAdapter(this, "orders");
                syncAdapter1.execute();
                utility.StartAsyncTaskInParallel(syncAdapter1);
                SyncAdapter syncAdapter2 = new SyncAdapter(this, "customers");
                syncAdapter2.execute();
                utility.StartAsyncTaskInParallel(syncAdapter2);*/
            }
            else {
                Toast.makeText(this,"Please connect Internet.", Toast.LENGTH_LONG).show();
            }
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

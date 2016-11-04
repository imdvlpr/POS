package com.ankit.pointofsolution;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ankit.pointofsolution.api.ApiManager;
import com.ankit.pointofsolution.config.Constants;
import com.ankit.pointofsolution.storage.DBHelper;
import com.ankit.pointofsolution.storage.Preferences;
import com.ankit.pointofsolution.utility.NetworkOperations;
import com.ankit.pointofsolution.utility.SyncAdapter;

public class PaymentOptionsActivity extends MainActivity {

    Button b1, b2, b3;
    Preferences pref;
    ApiManager apiManager;
    DBHelper dbHelper;
    private String price;
    private double enterprice;
    private String tp=null;
    private double total_price;
    private double rmainprice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = new Preferences(this);
        apiManager = new ApiManager(this);
        dbHelper = new DBHelper(this);
        b1 = (Button) findViewById(R.id.btncash);
        b2 = (Button) findViewById(R.id.btnEdit);
        b3 = (Button) findViewById(R.id.btndc);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
       /*dbHelper.updateOrderStatus(pref.getCurrentOdrerId(), Constants.KEY_CASH_PAYMENT
        , Constants.KEY_PAYMENT_STATUS, Constants.ORDER_FINAL_STATUS);
        Intent i = new Intent(PaymentOptionsActivity.this,PaymentConfirmation.class);
        startActivity(i);*/
                qdialog();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentOptionsActivity.this,MainActivity.class);
                i.putExtra(Constants.KEY_ORDER_STATUS,Constants.KEY_EDIT_ORDER_VALUE);
                startActivity(i);

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price=null;

                dbHelper.updateOrderStatus(pref.getCurrentOdrerId(), Constants.KEY_DEBIT_CARD_PAYMENT
                        , Constants.KEY_PAYMENT_STATUS, Constants.ORDER_FINAL_STATUS,price);
                Intent i = new Intent(PaymentOptionsActivity.this,MainActivity.class);
                startActivity(i);
            }
        });



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.common, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PaymentOptionsActivity.this,MainActivity.class);
        i.putExtra("EDIT","editOrder");
        startActivity(i);
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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            apiManager.logout(pref);
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
            Intent i = new Intent(PaymentOptionsActivity.this,MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_return) {

        } else if (id == R.id.nav_reports) {

        } else if (id == R.id.nav_reciept) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_logout) {
            apiManager.logout(pref);
        } else if (id == R.id.nav_help) {

        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void qdialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_price);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        Button btnSaveButton = (Button) dialog.findViewById(R.id.PayPrint);
        final TextView totalprice = (TextView) dialog.findViewById(R.id.totalPrice);
        final TextView rprice = (TextView) dialog.findViewById(R.id.rPrice);
        final EditText eCashAmount = (EditText) dialog.findViewById(R.id.CashAmount);
        totalprice.setText(pref.getPrice());
        eCashAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                price=eCashAmount.getText().toString();
                if(price.equals("")) {
                    enterprice =0.0;
                }
                else{
                    enterprice = Double.parseDouble(price);
                }
                tp=totalprice.getText().toString();
                total_price =Double.parseDouble(tp);
                rmainprice=enterprice-total_price;
                if(rmainprice<0.0)
                {
                    rprice.setText(String.format("%.02f", rmainprice));
                    View focusView = null;
                    boolean cancel = false;
                    eCashAmount.setError(getString(R.string.error_valid_amount));
                    focusView = eCashAmount;
                    cancel = true;

                }
                else {
                    rprice.setText(String.format("%.02f", rmainprice));
                }
            }
            public void afterTextChanged(Editable s) {

            }
        });

        btnSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView = null;
                boolean cancel = false;
                //double enterprice=0.0;
                //final TextView remainningprice = (TextView) dialog.findViewById(R.id.Remainingprice);

                if (TextUtils.isEmpty(price)) {
                    eCashAmount.setError(getString(R.string.error_field_required));
                    focusView = eCashAmount;
                    cancel = true;
                }
                else{
                    enterprice =Double.parseDouble(price);
                    total_price =Double.parseDouble(tp);

                    if (enterprice<total_price) {
                    System.out.println("");
                    eCashAmount.setError(getString(R.string.error_valid_amount));
                    focusView = eCashAmount;
                    cancel = true;
                }
                else {
                    dbHelper.updateOrderStatus(pref.getCurrentOdrerId(), Constants.KEY_CASH_PAYMENT
                            , Constants.KEY_PAYMENT_STATUS, Constants.ORDER_FINAL_STATUS,price);
                        NetworkOperations noptn = new NetworkOperations(PaymentOptionsActivity.this);
                        if (noptn.hasActiveInternetConnection(PaymentOptionsActivity.this)) {
                            new SyncAdapter(PaymentOptionsActivity.this).execute();
                        }
                    Intent i = new Intent(PaymentOptionsActivity.this, PaymentConfirmation.class);
                    startActivity(i);
                    finish();
                    dialog.hide();
                }
                }
            }
        });
    }


}

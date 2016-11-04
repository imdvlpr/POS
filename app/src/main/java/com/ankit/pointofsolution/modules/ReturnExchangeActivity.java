package com.ankit.pointofsolution.modules;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ankit.pointofsolution.IntentIntegrator.IntentIntegrator;
import com.ankit.pointofsolution.IntentIntegrator.IntentResult;
import com.ankit.pointofsolution.Models.OrderDetails;
import com.ankit.pointofsolution.Models.Productdata;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.adapter.ReturnExchangeAdapter;
import com.ankit.pointofsolution.storage.DBHelper;
import com.ankit.pointofsolution.utility.Utility;

import java.util.ArrayList;

public class ReturnExchangeActivity extends AppCompatActivity {

    private String scanContent;
    private Utility utility;
    private Productdata productdata;
    private static DBHelper dbHelper;
    private ReturnExchangeAdapter returnexchangeAdapter;
    private ExpandableListView listView;
    private Resources res;
    private Dialog dialog;
    String orderid;
    String receiptno = null;
    View focusView = null;
    private Context context;
    boolean cancel = false;
    private Activity activity;
    public DrawerLayout drawer;
    public static ArrayList<OrderDetails> orderDetailsArrayList;
    public static TextView vTotalconut,itemCount;
    private TextView  reorderId,reorderdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_exchange);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vTotalconut = (TextView) findViewById(R.id.retotalconut);
        itemCount   = (TextView) findViewById(R.id.reitemcountno);
        reorderId = (TextView) findViewById(R.id.reorderId);
        reorderdate = (TextView) findViewById(R.id.reorderDate);



        if (orderid != null) {

            System.out.println("hello");
            dialog.hide();
            dialog.cancel();
        } else {
            System.out.println("hi");
            qdialog();
        }

        listView = (ExpandableListView) findViewById(R.id.returnexchangelist);
        res = getResources();
        dbHelper = new DBHelper(this);
        context = this;
        activity = this;
    }


    public void qdialog() {
        // final View focusView = null;
        //final boolean cancel = false;
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.return_exchange_dialog);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        final EditText receiptid = (EditText) dialog.findViewById(R.id.receiptid);


        Button btnSaveButton = (Button) dialog
                .findViewById(R.id.enterreceiptno);
        Button ScanReceipt = (Button) dialog
                .findViewById(R.id.ScanReceipt);
        ScanReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(ReturnExchangeActivity.this);
                scanIntegrator.initiateScan();
            }
        });
        btnSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                receiptno = receiptid.getText().toString();
                System.out.println("rrrr" + receiptno);
                if (TextUtils.isEmpty(receiptno)) {
                    receiptid.setError(getString(R.string.error_field_required));
                    focusView = receiptid;
                    cancel = true;
                } else {
                    orderDetailsArrayList = dbHelper.getOrderDetailsByOrderId(receiptno);
                    String orderDateByOrderId = dbHelper.getOrderDateByOrderId(receiptno);
                    if (orderDetailsArrayList.size() > 0) {
                        orderid = receiptno;
                        reorderId.setText(orderid);
                        reorderdate.setText(orderDateByOrderId);
                        System.out.println("hi rohit");
                        returnexchangeAdapter = new ReturnExchangeAdapter(context, orderDetailsArrayList, res, activity);
                        listView.setAdapter(returnexchangeAdapter);
                        dialog.hide();
                        dialog.cancel();
                    } else {
                        System.out.println("hi gane");
                        receiptid.setError(getString(R.string.invalid_receipt));
                        focusView = receiptid;
                        cancel = true;
                    }
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            scanContent = scanningResult.getContents();
            orderid = scanContent.substring(1);
            System.out.println("scanContent:" + orderid);

            if (orderid != null) {
                orderDetailsArrayList = dbHelper.getOrderDetailsByOrderId(orderid);
                String orderDateByOrderId = dbHelper.getOrderDateByOrderId(receiptno);
                reorderId.setText(orderid);
                reorderdate.setText(orderDateByOrderId);
                returnexchangeAdapter = new ReturnExchangeAdapter(this, orderDetailsArrayList, res, this);
                listView.setAdapter(returnexchangeAdapter);
                hide();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }

    public void hide() {
        dialog.hide();
        dialog.cancel();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }
}
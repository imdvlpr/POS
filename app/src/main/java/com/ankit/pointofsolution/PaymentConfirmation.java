package com.ankit.pointofsolution;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ankit.pointofsolution.Models.OrderDetails;
import com.ankit.pointofsolution.bluetoothprint.DeviceListActivity;
import com.ankit.pointofsolution.bluetoothprint.UnicodeFormatter;
import com.ankit.pointofsolution.config.Messages;
import com.ankit.pointofsolution.storage.DBHelper;
import com.ankit.pointofsolution.storage.Preferences;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.lang.System.out;

/**
 * Created by rinzinchoephel on 19/10/16.
 */

public class PaymentConfirmation extends AppCompatActivity implements Runnable {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    private Preferences pref;
    DBHelper dbHelper;
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    private String sOrderid;
    Button mScan, mPrint, mDisc;
    BluetoothAdapter mBluetoothAdapter;
    private ArrayList<OrderDetails> orderDetailsArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = new Preferences(this);
        dbHelper = new DBHelper(this);
        out.println("pref.getCurrentOdrerId()----"+pref.getCurrentOdrerId());
        sOrderid = pref.getCurrentOdrerId();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        insertDummyContactWrapper();

        Button remailBtn = (Button) findViewById(R.id.remail);
        Button rtextBtn = (Button) findViewById(R.id.rtext);
        Button rprintBtn = (Button) findViewById(R.id.rprint);
        Button cancel = (Button) findViewById(R.id.cancel);
        remailBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendEmail();
            }
        });
        rtextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentConfirmation.this, Text.class);
                startActivity(i);
                //finish();
            }
        });
        rprintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent i = new Intent(PaymentConfirmation.this, Main.class);
                i.putExtra("Scan","Bluetooth");
                startActivity(i);*/
                //finish();
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(PaymentConfirmation.this, "Device Not Support", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(PaymentConfirmation.this,
                                DeviceListActivity.class);
                        startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentConfirmation.this, MainActivity.class);
                startActivity(i);
                //finish();
            }
        });

    }

    protected void sendEmail() {
        orderDetailsArrayList = dbHelper.getOrderDetailsByOrderId(sOrderid);
        String orderDateByOrderId = dbHelper.getOrderDateByOrderId(sOrderid);
        double totalprice = 0;
        String BILL = "Dear Customer,\n Thanks for the order";
        BILL = "\nOdrer No: " +sOrderid+ "    \n"+ orderDateByOrderId+"\n" + "POS-"+pref.getStoreName()+"\n";
        BILL = BILL+ "------------------------------\n";
        //Receipt Header
        BILL = BILL + "Item       Price   Qty   total\n";
        BILL = BILL + "------------------------------\n";
        for(int i=0;i<orderDetailsArrayList.size();i++)
        {
            double price = orderDetailsArrayList.get(i).getItemQty()*orderDetailsArrayList.get(i).getItemPrice();
            totalprice=totalprice+price;
            BILL = BILL + orderDetailsArrayList.get(i).getsItemName()+"       "+orderDetailsArrayList.get(i).getItemPrice()
                    +"    "+orderDetailsArrayList.get(i).getItemQty()+"   "+String.valueOf(String.format("%.02f", price))+"\n";
        }
        BILL = BILL + "------------------------------";
        BILL = BILL + "\n\n";
        BILL = BILL + "No. of Items:  " + "     " + orderDetailsArrayList.size()+"\n";
        BILL = BILL + "Total Value:" + "     " + String.valueOf(String.format("%.02f", totalprice))+"\n";
        BILL = BILL + "------------------------------\n\n";

        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, Messages.MAIL_SUBJECT);
        emailIntent.putExtra(Intent.EXTRA_TEXT, BILL);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            //finish();
            Log.i("Done sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(PaymentConfirmation.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        //finish();
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(new Intent(PaymentConfirmation.this,MainActivity.class));
                        //finish();
                    }
                }).create().show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.common, menu);
        return true;
    }
    private void insertDummyContactWrapper(){
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(permissionsList, Manifest.permission.READ_SMS))
                permissionsNeeded.add("Read SMS");
            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                }
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        }

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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                } else {
                    // Permission Denied
                    Toast.makeText(PaymentConfirmation.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("User Permission Required").setMessage("We need permission to continue this app")
                            .setCancelable(false)
                            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    insertDummyContactWrapper();
                                    someMethod();
                                }
                            }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(1);
                            finish();
                        }
                    });
                    final AlertDialog alert = builder.create();
                    alert.show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PaymentConfirmation.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void someMethod() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }


    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mDeviceAddress);

                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(PaymentConfirmation.this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;
            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(PaymentConfirmation.this, DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(PaymentConfirmation.this, "Bluetooth activate denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            mBluetoothConnectProgressDialog.cancel();
            Toast.makeText(PaymentConfirmation.this, "Connecting failed", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(PaymentConfirmation.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
            print();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }
        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }
    public void print()
    {
        Thread t = new Thread() {
            public void run() {
                try {
                    orderDetailsArrayList = dbHelper.getOrderDetailsByOrderId(sOrderid);
                    String orderDateByOrderId = dbHelper.getOrderDateByOrderId(sOrderid);
                    double totalprice = 0;
                    OutputStream os = mBluetoothSocket.getOutputStream();
                    String BILL = "";
                    BILL = "\nOdrer No: " +sOrderid+ "    \n"+ orderDateByOrderId+"\n" + "POS-"+pref.getStoreName()+"\n";
                    BILL = BILL+ "------------------------------\n";
                    //Receipt Header
                    BILL = BILL + "Item       Price   Qty   total\n";
                    BILL = BILL + "------------------------------\n";
                    for(int i=0;i<orderDetailsArrayList.size();i++)
                    {
                        double price = orderDetailsArrayList.get(i).getItemQty()*orderDetailsArrayList.get(i).getItemPrice();
                        totalprice=totalprice+price;
                        BILL = BILL + orderDetailsArrayList.get(i).getsItemName()+"       "+orderDetailsArrayList.get(i).getItemPrice()
                                +"    "+orderDetailsArrayList.get(i).getItemQty()+"   "+String.valueOf(String.format("%.02f", price))+"\n";
                    }
                    BILL = BILL + "------------------------------";
                    BILL = BILL + "\n\n";
                    BILL = BILL + "No. of Items:  " + "     " + orderDetailsArrayList.size()+"\n";
                    BILL = BILL + "Total Value:" + "     " + String.valueOf(String.format("%.02f", totalprice))+"\n";
                    BILL = BILL + "------------------------------\n\n";
                    os.write(BILL.getBytes());
                    //This is printer specific code you can comment ==== > Start

                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 104;
                    os.write(intToByteArray(h));
                    int n = 162;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));

                    // Print BarCode
                    int gs1 = 29;
                    os.write(intToByteArray(gs1));
                    int k = 107;
                    os.write(intToByteArray(k));
                    int m = 73;
                    os.write(intToByteArray(m));

                    String barCodeVal = "R"+sOrderid;// "HELLO12345678912345012";
                    out.println("Barcode Length : "
                            + barCodeVal.length());
                    int n1 = barCodeVal.length();
                    os.write(intToByteArray(n1));

                    for (int i = 0; i < barCodeVal.length(); i++) {
                        os.write((barCodeVal.charAt(i) + "").getBytes());
                    }
                    startActivity(new Intent(PaymentConfirmation.this,MainActivity.class));
                    finish();
                    //printer specific code you can comment ==== > End
                } catch (Exception e) {
                    Log.e("Main", "Exe ", e);
                }
            }
        };
        t.start();
    }
}
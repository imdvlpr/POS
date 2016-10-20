package com.ankit.pointofsolution;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ankit.pointofsolution.IntentIntegrator.IntentIntegrator;
import com.ankit.pointofsolution.IntentIntegrator.IntentResult;
import com.ankit.pointofsolution.Models.Coupons;
import com.ankit.pointofsolution.Models.OrderDetails;
import com.ankit.pointofsolution.Models.Productdata;
import com.ankit.pointofsolution.adapter.ExpandableListAdapterTest;
import com.ankit.pointofsolution.api.ApiManager;
import com.ankit.pointofsolution.config.Constants;
import com.ankit.pointofsolution.config.Messages;
import com.ankit.pointofsolution.dialog_fragments.AddItemCouponsFragment;
import com.ankit.pointofsolution.dialog_fragments.AddItemManullyFragment;
import com.ankit.pointofsolution.dialog_fragments.DialogueAlertsFragment;
import com.ankit.pointofsolution.modules.CustomerActivity;
import com.ankit.pointofsolution.modules.ListofOrdersActivity;
import com.ankit.pointofsolution.storage.DBHelper;
import com.ankit.pointofsolution.storage.Preferences;
import com.ankit.pointofsolution.utility.GetResponseDialogListener;
import com.ankit.pointofsolution.utility.SyncAdapter;
import com.ankit.pointofsolution.utility.Utility;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GetResponseDialogListener {
    ApiManager apiManager;
    Preferences pref;
    private TextView vUseremail;
    public static ExpandableListAdapterTest listViewAdpter;
    public static ExpandableListView listView;
    public ArrayList<String> values;

    private Productdata productdata;
    private String scanContent;
    private Utility utility;
    private static OrderDetails orderDetails;
    public static ArrayList<OrderDetails> orderDetailsArrayList;
    public int totalprice;
    public static TextView vTotalconut,itemCount;
    Intent i;
    private static DBHelper dbHelper;
    public String orderId;
    public DrawerLayout drawer;
    private ArrayList<Coupons> couponsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        apiManager = new ApiManager(this);
        pref = new Preferences(this);
        Resources res = getResources();
        productdata = new Productdata();
        dbHelper = new DBHelper(this);
        utility = new Utility(pref,dbHelper);

        // get the listview
        listView = (ExpandableListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        orderDetails = new OrderDetails();
        Bundle b = getIntent().getExtras();
        orderDetailsArrayList = new ArrayList<OrderDetails>();
        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        vTotalconut = (TextView) findViewById(R.id.totalconut);
        itemCount   = (TextView) findViewById(R.id.itemcount);

        ImageView paymentoptionsbtn = (ImageView) findViewById(R.id.paymentoptions);
        paymentoptionsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalprice=0;
                listViewAdpter.notifyDataSetChanged();
//                System.out.println("orderDetailsArrayList.size(): "+orderDetailsArrayList.size());
                if(orderDetailsArrayList.size()>0) {
                    //Checks and generate the unique order id ...
                    orderId = utility.genrateOrderID();
                    pref.setCurrentOdrerId(orderId);
                    //if order id is null in case...
                    if (orderId != null) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (dbHelper.numberOfOrdersByOrderId(orderId) > 0) {

                                    for (int i = 0; i < orderDetailsArrayList.size(); i++) {
                                        OrderDetails orderDetails = orderDetailsArrayList.get(i);
                                        //System.out.println("orderDetailses:" + orderDetails.getItemSku());
                                        int count = dbHelper.numberOfOrderDetailsByOrderId(pref.getCurrentOdrerId(),
                                                                                         orderDetails.getItemSku());
                                        if (count>0) {
                                            dbHelper.updateQuantitybyOidSku(pref.getCurrentOdrerId(),orderDetails.getItemSku(),
                                                                                     String.valueOf(orderDetails.getItemQty()));
                                        }else {
                                            dbHelper.insertOrderDetails(pref.getCurrentOdrerId(), orderDetails.getItemSku(),
                                                    String.valueOf(orderDetails.getItemQty()), String.valueOf(orderDetails.getItemPrice()),
                                                    orderDetails.getsItemName());
                                        }
                                    }
                                }else
                                {
                             // In that part of code we need to put other details like OrderID, order Status, Offer on item
                            dbHelper.insertOrders(orderId, Constants.ORDER_INITIAL_STATUS, Constants.ORDER_STORAGE_STATUS, "", "");
                            for (int i = 0; i < orderDetailsArrayList.size(); i++) {
                                OrderDetails orderDetails = orderDetailsArrayList.get(i);
                                //    System.out.println("orderDetailses:" + orderDetails.getItemSku());
                                int count = dbHelper.numberOfOrderDetailsByOrderId(pref.getCurrentOdrerId(),
                                        orderDetails.getItemSku());
                                if (count>0) {
                                    double qty = orderDetails.getItemQty()+dbHelper.getQuantityByOrderId(pref.getCurrentOdrerId()
                                                                                            ,orderDetails.getItemSku());
                                dbHelper.updateQuantitybyOidSku(pref.getCurrentOdrerId(),orderDetails.getItemSku(),
                                                    String.valueOf(qty));
                                }else {
                                dbHelper.insertOrderDetails(pref.getCurrentOdrerId(), orderDetails.getItemSku(),
                                        String.valueOf(orderDetails.getItemQty()), String.valueOf(orderDetails.getItemPrice()),
                                        orderDetails.getsItemName());
                                        }
                                    }
                                    // String s = gson.toJson(orderDetailsArrayList, new TypeToken<ArrayList<OrderDetails>>(){}.getType());
                                    //utility.makeOrders(s);
                                }
                                i = new Intent(MainActivity.this, PaymentOptionsActivity.class);
                                startActivity(i);
                            }
                        }).start();
                    } else {
                        Toast.makeText(getBaseContext(), Messages.CHECK_CART, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), Messages.NO_ITEMS_IN_CART, Toast.LENGTH_LONG).show();
                }
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

        vUseremail = (TextView)header.findViewById(R.id.usermail);
        vUseremail.setText(pref.getUsername());

        if(savedInstanceState != null){
            orderDetailsArrayList = savedInstanceState.getParcelableArrayList("d");
            listViewAdpter = new ExpandableListAdapterTest(this, orderDetailsArrayList, res, this);
            // Assign adapter to ListView
            listView.setAdapter(listViewAdpter);
            listViewAdpter.notifyDataSetChanged();
        }else if(b != null)
        {
            orderDetailsArrayList = dbHelper.getOrderDetailsByOrderId(pref.getCurrentOdrerId());
            System.out.println("orderDetailsArrayList : "+orderDetailsArrayList.size());
            listViewAdpter = new ExpandableListAdapterTest(this, orderDetailsArrayList, res, this);
            listView.setAdapter(listViewAdpter);
        }
        else {
            listViewAdpter = new ExpandableListAdapterTest(this, orderDetailsArrayList, res, this);
            // Assign adapter to ListView
            listView.setAdapter(listViewAdpter);
            listViewAdpter.notifyDataSetChanged();
        }

        // Listview Group click listener
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                 Toast.makeText(getApplicationContext(),
                 "Group Clicked " + orderDetailsArrayList.get(groupPosition),
                Toast.LENGTH_SHORT).show();
                System.out.println("qnty:"+"---"+ groupPosition);
                return false;
            }
        });

        // Listview Group expanded listener
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        orderDetailsArrayList.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // Listview Group collasped listener
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        orderDetailsArrayList.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

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
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putParcelableArrayList("d", orderDetailsArrayList);
            super.onSaveInstanceState(savedInstanceState);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.add_item){
            showAlert(Constants.ADD_ITEM_TYPE_1);;
            return true;
        }
        if(id == R.id.add_coupon){
            showAlert(Constants.ADD_COUPONS);;
            return true;
        }
        if(id == R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
            return true;
        }
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
            Intent i = new Intent(MainActivity.this,MainActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_return) {

        } else if (id == R.id.nav_reports) {

            //working code checks weather orders exist .....
            if(dbHelper.getAllOrders().size()>0) {
                Intent i = new Intent(this, ListofOrdersActivity.class);
                startActivity(i);
            }else
            {
                Toast.makeText(this,Messages.EMPTY_ORDERS,Toast.LENGTH_LONG).show();
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
            new SyncAdapter(this).execute();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            double d = 3;
            scanContent = scanningResult.getContents();
            // String scanFormat = scanningResult.getFormatName();
//            System.out.println("scanContent: "+scanContent);
            if(scanContent != null){
                productdata = utility.getProductDetailsbyCouponsItemSku(scanContent);
                // Firstly take data in model object /
             if(productdata!=null)
                {
                    updateResultSkuCode(productdata);
                }
                else
                {
                    pref.setSkuCode(scanContent);
                    showAlert(Constants.ADD_ITEM_TYPE_2);
                }
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    //Show alert to to ask weather he want to add new item or not...
    public void showAlert(String AddType)
    {
        if(AddType.equalsIgnoreCase(Constants.ADD_ITEM_TYPE_1)) {
            AddItemManullyFragment newFragment = new AddItemManullyFragment();
            newFragment.show(this.getFragmentManager().beginTransaction(), "Abd");
        }
        else if(AddType.equalsIgnoreCase(Constants.ADD_ITEM_TYPE_2)) {
            DialogueAlertsFragment newFragment = new DialogueAlertsFragment();
            newFragment.show(this.getFragmentManager().beginTransaction(), "Abd");
        }
        else if(AddType.equalsIgnoreCase(Constants.ADD_COUPONS)) {
            AddItemCouponsFragment newFragment = new AddItemCouponsFragment();
            newFragment.show(this.getFragmentManager().beginTransaction(), "Abd");
        }
    }

    public void updateResultSkuCode(Productdata productdata1)
    {
        if(productdata1!=null) {
            boolean itemInList = false;
            int pos=0;
            for(int i= 0; i<orderDetailsArrayList.size();i++)
            {
                if(orderDetailsArrayList.get(i).getItemSku().contains(productdata1.getSku()))
                {
                    System.out.println("sku codes:"+i+orderDetailsArrayList.get(i).getItemSku());
                    itemInList = true;
                    pos = i;
                    break;
                }
            }
            if(itemInList) {
                if (productdata1.getOffers() != null) {
                    couponsArrayList = new ArrayList<>();
                    System.out.println("offers set" + couponsArrayList.size()+"  -pos---"+pos);
                    //couponsArrayList.clear();
                    couponsArrayList.add(productdata1.getCoupons());
                    orderDetailsArrayList.get(pos).setCouponsArrayList(couponsArrayList);
                    listView.expandGroup(pos,true);
                } else {
                    orderDetailsArrayList.get(pos).setItemQty(orderDetailsArrayList.get(pos).getItemQty() + 1);
                }
                //couponsArrayList.clear();
            }
            else {
                if (productdata1.getOffers()!=null) {
                    Toast.makeText(MainActivity.this, Messages.INVALID_COUPON, Toast.LENGTH_SHORT).show();
                }
                else{
                    orderDetails = new OrderDetails();
                    orderDetails.setItemPrice(Float.parseFloat(productdata1.getPrice()));
                    orderDetails.setItemQty(1);
                    orderDetails.setItemSku(productdata1.getSku());
                    orderDetails.setsItemName(productdata1.getProductName());
                    //orderDetails.setOrderValue("");
                    orderDetailsArrayList.add(orderDetails);
                }
            }
            listView.setVisibility(View.VISIBLE);
            listViewAdpter.notifyDataSetChanged();
        }
    }

}

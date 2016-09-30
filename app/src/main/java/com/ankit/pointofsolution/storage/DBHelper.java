package com.ankit.pointofsolution.storage;

/**
 * Created by Ankit on 26-Sep-16.
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.ankit.pointofsolution.Models.OrderDetails;
import com.ankit.pointofsolution.Models.Orders;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pos.db";
    //Contact Constants
    public static final String CUSTOMER_TABLE_NAME = "customer";
    public static final String CUSTOMER_COLUMN_ID = "id";
    public static final String CUSTOMER_COLUMN_NAME = "name";
    public static final String CUSTOMER_COLUMN_EMAIL = "email";
    public static final String CUSTOMER_COLUMN_STREET = "street";
    public static final String CUSTOMER_COLUMN_CITY = "place";
    public static final String CUSTOMER_COLUMN_PHONE = "phone";

    //Orders Constants
    public static final String ORDERS_TABLE_NAME = "orders";
    public static final String ORDERS_COLUMN_ID = "oId";
    public static final String ORDERS_COLUMN_ORDERID = "orderId";
    public static final String ORDERS_COLUMN_ORDERSTATUS = "orderStatus";
    public static final String ORDERS_COLUMN_ORDERSTORAGESTATUS = "orderStorageStatus";
    public static final String ORDERS_COLUMN_ORDEROFFERS = "orderOffers";
    public static final String ORDERS_COLUMN_ORDERPROMOTIONS = "orderPromotions";
    public static final String ORDERS_COLUMN_ORDERCREATEDAT = "orderCreatedAt";
    public static final String ORDERS_COLUMN_ORDERUPDATEDAT = "orderUpdatedAt";

    //Orders Constants
    public static final String OD_TABLE_NAME = "order_details";
    public static final String OD_COLUMN_ID = "odId";
    public static final String OD_COLUMN_ORDERID = "orderId";
    public static final String OD_COLUMN_ORDERSTATUS = "orderStatus";
    public static final String OD_COLUMN_PRODUCTSKU = "productSku";
    public static final String OD_COLUMN_PRODUCTQTY = "productQty";
    public static final String OD_COLUMN_PRODUCTPRICE = "productPrice";
    public static final String OD_COLUMN_PRODUCTNAME = "productName";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        //Create contact query
        String customer_qry = "create table "+CUSTOMER_TABLE_NAME+" " +
                "(id integer primary key, name text,phone text,email text, street text,place text)";
        db.execSQL(customer_qry);
        //Create orders query
        String orders_qry = "create table " +ORDERS_TABLE_NAME+
                "(oId integer primary key, orderId text, orderStatus text,orderStorageStatus text,orderOffers text," +
                            "orderPromotions text, orderCreatedAt text,orderUpdatedAt text)";
//        System.out.println("onCreate: orders_qry: "+ orders_qry);
        db.execSQL(orders_qry);
        //Create order details query
        String order_details_qry = "create table " +OD_TABLE_NAME+
                "(odId integer primary key, orderId text, productSku text,productQty text,productPrice text,productName text)";
//        System.out.println("onCreate: order_details_qry: "+ order_details_qry);
        db.execSQL(order_details_qry);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+CUSTOMER_TABLE_NAME+"");
        onCreate(db);
    }
    // Operations for contact tables
    public boolean insertContact(String name, String phone, String email, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.insert(CUSTOMER_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+CUSTOMER_TABLE_NAME+" where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CUSTOMER_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update(CUSTOMER_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CUSTOMER_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+CUSTOMER_TABLE_NAME+"", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CUSTOMER_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    // order table operations
    public boolean insertOrders(String orderId, String orderStatus, String orderStorageStatus, String orderOffers,
                                                     String orderPromotions)
    {
        String orderCreatedAt = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDERS_COLUMN_ORDERID, orderId);
        contentValues.put(ORDERS_COLUMN_ORDERSTATUS, orderStatus);
        contentValues.put(ORDERS_COLUMN_ORDERSTORAGESTATUS, orderStorageStatus);
        contentValues.put(ORDERS_COLUMN_ORDEROFFERS, orderOffers);
        contentValues.put(ORDERS_COLUMN_ORDERPROMOTIONS, orderPromotions);
        contentValues.put(ORDERS_COLUMN_ORDERCREATEDAT, orderCreatedAt);
        contentValues.put(ORDERS_COLUMN_ORDERUPDATEDAT, orderCreatedAt);
        db.insert(ORDERS_TABLE_NAME, null, contentValues);
        return true;
    }
    public ArrayList<Orders> getAllOrders(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ORDERS_TABLE_NAME+" ", null );
        ArrayList<Orders> ordersArrayList = new ArrayList<>();
        System.out.println("count:"+res.getCount());
        if (res.moveToFirst()) {
            do {
                Orders orders = new Orders();
                orders.setOrderId(res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDERID)));
                orders.setOrderStatus(res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDERSTATUS)));
                orders.setOrderOffers(res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDEROFFERS)));
                orders.setOrderPromotions(res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDERPROMOTIONS)));
                orders.setOrderStorageStatus(res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDERSTORAGESTATUS)));
                orders.setOrderCreatedAt(res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDERCREATEDAT)));
                orders.setOrderUpdatedAt(res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDERUPDATEDAT)));
                ordersArrayList.add(orders);
            } while (res.moveToNext());
        }
        return ordersArrayList;
    }

    public int numberOfOrdersByOrderId(String orderId){
        /*SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ORDERS_TABLE_NAME);
        return numRows;*/
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ORDERS_TABLE_NAME+" where orderId='"+orderId+"'", null );
        int numRows = res.getCount();
        System.out.println("numRows:"+numRows);
        return numRows;
    }
    public boolean updateOrders(String orderId, String orderStatus, String orderStorageStatus, String orderOffers,
                                                        String orderPromotions,String orderCreatedAt,String orderUpdatedAt)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDERS_COLUMN_ORDERID, orderId);
        contentValues.put(ORDERS_COLUMN_ORDERSTATUS, orderStatus);
        contentValues.put(ORDERS_COLUMN_ORDERSTORAGESTATUS, orderStorageStatus);
        contentValues.put(ORDERS_COLUMN_ORDEROFFERS, orderOffers);
        contentValues.put(ORDERS_COLUMN_ORDERPROMOTIONS, orderPromotions);
        contentValues.put(ORDERS_COLUMN_ORDERCREATEDAT, orderCreatedAt);
        contentValues.put(ORDERS_COLUMN_ORDERUPDATEDAT, orderUpdatedAt);
        db.update(ORDERS_TABLE_NAME, contentValues, "orderId = ? ", new String[] { orderId } );
        return true;
    }

    public boolean updateOrderStatus(String orderId, String orderStatus)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDERS_COLUMN_ORDERSTATUS, orderStatus);
        db.update(ORDERS_TABLE_NAME, contentValues, "orderId = ? ", new String[] { orderId } );
        return true;
    }

    public Integer deleteOrders(String orderId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ORDERS_TABLE_NAME, "orderId = ? ", new String[] { orderId });
    }

    public ArrayList<String> getAllOrdersId()
    {
        ArrayList<String> array_list = new ArrayList<String>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ORDERS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDERID)));
            res.moveToNext();
        }
        return array_list;
    }

    // order details table operations
    public boolean insertOrderDetails(String orderId, String productSku, String productQty, String productPrice, String productName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OD_COLUMN_ORDERID, orderId);
        contentValues.put(OD_COLUMN_PRODUCTSKU, productSku);
        contentValues.put(OD_COLUMN_PRODUCTQTY, productQty);
        contentValues.put(OD_COLUMN_PRODUCTPRICE, productPrice);
        contentValues.put(OD_COLUMN_PRODUCTNAME, productName);
        db.insert(OD_TABLE_NAME, null, contentValues);
        return true;
    }
    public ArrayList<OrderDetails> getOrderDetailsByOrderId(String orderId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+OD_TABLE_NAME+" where orderId='"+orderId+"'", null );
        System.out.println("getOrderDetailsByOrderId:"+orderId);
        ArrayList<OrderDetails> orderDetailsArrayList = new ArrayList<>();
        System.out.println("count:"+res.getCount());
        if (res.moveToFirst()) {
            do {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setItemSku(res.getString(res.getColumnIndex(OD_COLUMN_PRODUCTSKU)));
                orderDetails.setItemPrice(Float.parseFloat(res.getString(res.getColumnIndex(OD_COLUMN_PRODUCTPRICE))));
                orderDetails.setItemQty(Integer.parseInt(res.getString(res.getColumnIndex(OD_COLUMN_PRODUCTQTY))));
                orderDetails.setsItemName(res.getString(res.getColumnIndex(OD_COLUMN_PRODUCTNAME)));
                orderDetailsArrayList.add(orderDetails);
            } while (res.moveToNext());
        }
        return orderDetailsArrayList;
    }
    public String getOrderStatusByOrderId(String orderId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ORDERS_TABLE_NAME+" where orderId='"+orderId+"'", null );
        String status = null;
        if (res.moveToFirst()) {
            do {
            status = res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDERSTATUS)); ;
            } while (res.moveToNext());
        }
        return status;
    }

    public int numberOfOrderDetails(String orderId){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, OD_TABLE_NAME);
        return numRows;
    }
    public int numberOfOrderDetailsByOrderId(String orderId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+OD_TABLE_NAME+" where orderId='"+orderId+"'", null );
        int numRows = res.getCount();
        System.out.println("numRows:"+numRows);
        return numRows;
    }
    public int numberOfOrderDetailsByOrderId(String orderId, String productSku){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+OD_TABLE_NAME+" where orderId='"+orderId+"' AND productSku='"+productSku+"'", null );
        int numRows = res.getCount();
        System.out.println("numRows:"+numRows);
        return numRows;
    }
    //get qty by order id & item sku code.....
    public int getQuantityByOrderId(String orderId, String productSku){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select SUM("+OD_COLUMN_PRODUCTQTY+") AS "+OD_COLUMN_PRODUCTQTY+" from "+OD_TABLE_NAME+" where orderId='"+orderId+"' AND productSku='"+productSku+"'", null );
        int qty = 0;
        if (res.moveToFirst()) {
            do {
                qty = Integer.parseInt(res.getString(res.getColumnIndex(OD_COLUMN_PRODUCTQTY)));
                System.out.println("qty : "+qty);
            } while (res.moveToNext());
        }
        return qty;
    }

    public boolean updateQuantitybyOidSku(String orderId, String productSku, String productQty)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OD_COLUMN_PRODUCTQTY, productQty);
        db.update(OD_TABLE_NAME, contentValues, "orderId = ? AND productSku = ? ", new String[] { orderId, productSku } );
        return true;
    }

    public Integer deleteOrderDetails(String orderId, String productSku)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(OD_TABLE_NAME, "orderId = ? AND productSku = ? ", new String[] { orderId, productSku });
    }

    public ArrayList<String> getAllOrderDetailsProductSku()
    {
        ArrayList<String> array_list = new ArrayList<String>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+OD_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(OD_COLUMN_PRODUCTSKU)));
            res.moveToNext();
        }
        return array_list;
    }

}

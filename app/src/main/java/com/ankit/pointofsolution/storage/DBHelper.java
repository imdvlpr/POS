package com.ankit.pointofsolution.storage;

/**
 * Created by Ankit on 26-Sep-16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ankit.pointofsolution.Models.Coupons;
import com.ankit.pointofsolution.Models.OrderDetails;
import com.ankit.pointofsolution.Models.Orders;
import com.ankit.pointofsolution.Models.Productdata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
    public static final String ORDERS_COLUMN_PAYMENTTYPE = "orderPaymentType";
    public static final String ORDERS_COLUMN_PAYMENTSTATUS = "orderPaymentStatus";

    //Orders Constants
    public static final String OD_TABLE_NAME = "order_details";
    public static final String OD_COLUMN_ID = "odId";
    public static final String OD_COLUMN_ORDERID = "orderId";
    public static final String OD_COLUMN_ORDERSTATUS = "orderStatus";
    public static final String OD_COLUMN_PRODUCTSKU = "productSku";
    public static final String OD_COLUMN_PRODUCTQTY = "productQty";
    public static final String OD_COLUMN_PRODUCTPRICE = "productPrice";
    public static final String OD_COLUMN_PRODUCTNAME = "productName";

    //Item Constants

    public static final String ITEM_TABLE_NAME = "items";
    public static final String ITEM_COLUMN_ID = "itemId";
    public static final String ITEM_COLUMN_NAME = "itemName";
    public static final String ITEM_COLUMN_PRICE = "itemPrice";
    public static final String ITEM_COLUMN_BRAND = "itemBrand";
    public static final String ITEM_COLUMN_ADMINCODE= "itemAdminCode";
    public static final String ITEM_COLUMN_SKUCODE = "itemSkuCode";

    public static final String USER_TABLE_NAME = "users";
    public static final String USER_COLUMN_PASSWORD = "userEncryptedPassword";


    //Coupon Constants
    public static final String CP_TABLE_NAME = "coupons";
    public static final String CP_ID = "cpId";
    public static final String CP_ITEM_CODE = "cpItemSkuCode";
    public static final String CP_COUPON_SKU_CODE = "cpCouponSkuCode";
    public static final String CP_VALUE = "cpValue";
    public static final String CP_START_DATE = "cpStartDate";
    public static final String CP_END_DATE = "cpEndDate";
    public static final String CP_STATUS = "cpStatus";
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
                 "orderPromotions text, orderPaymentType text, orderPaymentStatus text, orderCreatedAt text, " +
                 "orderUpdatedAt text )";
//        System.out.println("onCreate: orders_qry: "+ orders_qry);
        db.execSQL(orders_qry);
        //Create order details query
        String order_details_qry = "create table " +OD_TABLE_NAME+
                "(odId integer primary key, orderId text, productSku text,productQty text,productPrice text,productName text" +
                ",procductCcode text, procductCvalue text )";
//        System.out.println("onCreate: order_details_qry: "+ order_details_qry);
        db.execSQL(order_details_qry);

        //Create item query
        String item_qry = "create table "+ITEM_TABLE_NAME+" " +
                "(itemId integer primary key, itemSkuCode text, itemName text, itemPrice text, itemBrand text, itemAdminCode text," +
                "itemOffers text, itemPromotions text, itemTax text, itemCreatedBy text, itemCreatedAt text, itemUpdatedBy text," +
                "itemUpdatedAt text, itemStatus text default 'active')";
        db.execSQL(item_qry);

        //Store users query
        String user_qry = "create table "+USER_TABLE_NAME+" " +
                "(userId integer primary key, userName text, userEncryptedPassword text, userRole text, userCreatedAt text, " +
                "userUpdatedAt text, userStatus text default 'active')";
        db.execSQL(user_qry);

        //Create Coupons details query
        String coupon_qry = "create table " +CP_TABLE_NAME+
                "(cpId integer primary key, cpItemSkuCode text, cpCouponSkuCode text,cpValue text,cpStartDate text," +
                            "cpEndDate text,cpStatus text default 'active')";
//        System.out.println("onCreate: order_details_qry: "+ order_details_qry);
        db.execSQL(coupon_qry);
        //Insert Coupons details query
        String coupon_insert_qry = "INSERT INTO coupons (cpItemSkuCode,cpCouponSkuCode,cpValue,cpStartDate,cpEndDate) " +
                "VALUES ('ASAP1','1234','1','2016-10-13 01.20.20','2016-10-21 02.30.30'),('ASAP2','12345','46','2016-10-13 01.20.20'" +
                                                        ",'2016-10-15 02.30.30')";
        db.execSQL(coupon_insert_qry);

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
//        System.out.println("count:"+res.getCount());
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
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ORDERS_TABLE_NAME+" where orderId='"+orderId+"'", null );
        int numRows = res.getCount();
        System.out.println("numberOfOrdersByOrderId numRows:"+numRows);
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

    public boolean updateOrderStatus(String orderId, String paymentType, String paymentStatus, String orderStatus)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDERS_COLUMN_ORDERSTATUS, orderStatus);
        contentValues.put(ORDERS_COLUMN_PAYMENTTYPE, paymentType);
        contentValues.put(ORDERS_COLUMN_PAYMENTSTATUS, paymentStatus);
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
        ArrayList<OrderDetails> orderDetailsArrayList = new ArrayList<>();
        if (res.moveToFirst()) {
            do {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setItemSku(res.getString(res.getColumnIndex(OD_COLUMN_PRODUCTSKU)));
                orderDetails.setItemPrice(Float.parseFloat(res.getString(res.getColumnIndex(OD_COLUMN_PRODUCTPRICE))));
                orderDetails.setItemQty(Float.parseFloat(res.getString(res.getColumnIndex(OD_COLUMN_PRODUCTQTY))));
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
        if(res.getCount()>0) {
            if (res.moveToFirst()) {
                do {
                    status = res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDERSTATUS));
                    ;
                } while (res.moveToNext());
            }
        }else status = "false";
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
        return numRows;
    }
    public int numberOfOrderDetailsByOrderId(String orderId, String productSku){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+OD_TABLE_NAME+" where orderId='"+orderId+"' AND productSku='"+productSku+"'", null );
        int numRows = res.getCount();
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


    //Import data db operations for Items, Users, Coupons, Modules, Settings etc..
    //Items table operations
    public boolean insertItem(String itemPrice,String itemName,String brand,String SkuCode, String sAdminCode)
    {
        String itemCreatedAt = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date());
        //System.out.println("insertItem: " + itemPrice + ", " + itemName + " , " + SkuCode);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("itemPrice", itemPrice);
        contentValues.put("itemName", itemName);
        contentValues.put("itemSkuCode", SkuCode);
        contentValues.put("itemAdminCode", sAdminCode);
        contentValues.put("itemBrand", brand);
        contentValues.put("itemCreatedAt", itemCreatedAt);
        contentValues.put("itemUpdatedAt", itemCreatedAt);
        db.insert(ITEM_TABLE_NAME, null, contentValues);
        return true;
    }
    //Store Users details
    public boolean insertUser(String userName,String userEncryptedPassword,String userRole)
    {
        String userCreatedAt = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", userName);
        contentValues.put("userEncryptedPassword", userEncryptedPassword);
        contentValues.put("userRole", userRole);
        contentValues.put("userCreatedAt", userCreatedAt);
        contentValues.put("userUpdatedAt", userCreatedAt);
        db.insert(USER_TABLE_NAME, null, contentValues);
        return true;
    }
    //Check users Mail exist or not
    public String checkUserEmail(String mEmail)
    {
        String passWord=null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+USER_TABLE_NAME+" where userName='"+mEmail+"'", null);
        //System.out.println("res: "+res.getCount());
        if (res.moveToFirst()) {
            do {
                passWord=res.getString(res.getColumnIndex(USER_COLUMN_PASSWORD));
            } while (res.moveToNext());
        }
        return passWord;
    }

    public Productdata getItemBySkuCode(String skuCode){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ITEM_TABLE_NAME+" where itemSkuCode='"+skuCode+"'", null );
        //System.out.println("getOrderDetailsByOrderId:"+skuCode);
        Productdata productdata = null;
        //System.out.println("count:"+res.getCount());
        if (res.moveToFirst()) {
            do {
                productdata = new Productdata();
                productdata.setProductName(res.getString(res.getColumnIndex(ITEM_COLUMN_NAME)));
                productdata.setPrice(res.getString(res.getColumnIndex(ITEM_COLUMN_PRICE)));
                productdata.setBrand(res.getString(res.getColumnIndex(ITEM_COLUMN_BRAND)));
                productdata.setSku(res.getString(res.getColumnIndex(ITEM_COLUMN_SKUCODE)));
            } while (res.moveToNext());
        }
        return productdata;
    }
    //Get item sku code using coupons sku code
    public Coupons getItemCodeByCouponsCode(String Couponcode){
        SQLiteDatabase db = this.getReadableDatabase();
        Coupons coupons = new Coupons();
        Cursor res =  db.rawQuery("select * from "+CP_TABLE_NAME+" where cpCouponSkuCode='"+Couponcode+"'", null);
        if (res.moveToFirst()) {
            do{
                coupons.setCpItemSkuCode(res.getString(res.getColumnIndex(CP_ITEM_CODE)));
                coupons.setCpValue(res.getString(res.getColumnIndex(CP_VALUE)));
                coupons.setCpStartDate(res.getString(res.getColumnIndex(CP_START_DATE)));
                coupons.setCpEndDate(res.getString(res.getColumnIndex(CP_END_DATE)));

            }while(res.moveToNext());
        }
        return coupons;
    }
    public Productdata getItemByCouponsItemSkuCode(String skuCode){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ITEM_TABLE_NAME+","+CP_TABLE_NAME+"" +
                " where (itemSkuCode='"+skuCode+"' AND  cpItemSkuCode='"+skuCode+"')", null );
        //System.out.println("getOrderDetailsByOrderId:"+skuCode);
        Productdata productdata = null;
        System.out.println("count:"+res.getCount());
        if (res.moveToFirst()) {
            do {
                productdata = new Productdata();
                productdata.setProductName(res.getString(res.getColumnIndex(ITEM_COLUMN_NAME)));
                productdata.setPrice(res.getString(res.getColumnIndex(ITEM_COLUMN_PRICE)));
                productdata.setBrand(res.getString(res.getColumnIndex(ITEM_COLUMN_BRAND)));
                productdata.setSku(res.getString(res.getColumnIndex(ITEM_COLUMN_SKUCODE)));
                productdata.setOffers(res.getString(res.getColumnIndex(CP_VALUE)));
                Coupons c = new Coupons();
                c.setCpCouponSkuCode(res.getString(res.getColumnIndex(CP_COUPON_SKU_CODE)));
                c.setCpItemSkuCode(res.getString(res.getColumnIndex(CP_ITEM_CODE)));
                c.setCpStartDate(res.getString(res.getColumnIndex(CP_START_DATE)));
                c.setCpEndDate(res.getString(res.getColumnIndex(CP_END_DATE)));
                c.setCpValue(res.getString(res.getColumnIndex(CP_VALUE)));
                productdata.setCoupons(c);
            } while (res.moveToNext());

        }
        return productdata;
    }
    public boolean updateItem(String productPrice,String productName,String productBrand, String productSku)
    {
        String itemUpdatedAt = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //System.out.println("updateItem: " + productPrice + ", " + productName + " , " + productSku);
        contentValues.put("itemPrice", productPrice);
        contentValues.put("itemName", productName);
        contentValues.put("itemBrand", productBrand);
        contentValues.put("itemUpdatedAt", itemUpdatedAt);
        db.update(ITEM_TABLE_NAME, contentValues, "itemSkuCode='"+productSku+"'", null);
        return true;
    }




}

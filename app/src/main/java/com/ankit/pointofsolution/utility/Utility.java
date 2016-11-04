package com.ankit.pointofsolution.utility;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import com.ankit.pointofsolution.Models.Customers;
import com.ankit.pointofsolution.Models.OrderDetails;
import com.ankit.pointofsolution.Models.Orders;
import com.ankit.pointofsolution.Models.Productdata;
import com.ankit.pointofsolution.config.Constants;
import com.ankit.pointofsolution.storage.DBHelper;
import com.ankit.pointofsolution.storage.Preferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ankit on 08-Sep-16.
 */
public class Utility {
    private ArrayList<Productdata> productdataList;
    private boolean status;
    private Preferences pref;
    private DBHelper dbHelper;

    public Utility(Preferences pref, DBHelper dbHelper){
        this.pref = pref;
        this.dbHelper = dbHelper;
    }

    public Utility() {
    }

    public boolean AdditemCatalog(Productdata productdata)
    {
        productdataList = new ArrayList<Productdata>();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        try {
            JSONArray jsonArray = new JSONArray(pref.getProductData());
            for(int i=0; i<jsonArray.length();i++)
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Productdata productdata1 = new Productdata();
//              System.out.println("jsonObject1: "+jsonObject1);
                productdata1.setProductName(String.valueOf(jsonObject1.get("productName")));
                productdata1.setPrice(String.valueOf(jsonObject1.get("price")));
                productdata1.setBrand(String.valueOf(jsonObject1.get("brand")));
                productdata1.setSku(String.valueOf(jsonObject1.get("sku")));
                productdataList.add(productdata1);
            }
           // System.out.println("productdataList.length(): "+productdataList.size()+" productdata:"+productdata.getSku());
            productdataList.add(jsonArray.length(),productdata);
//            System.out.println("productdataList.length()2: "+productdataList.size());
            String json = gson.toJson(productdataList);
//            System.out.println("json: "+json);
            if(productdataList.size()==jsonArray.length()+1)
            {
                status = true;
                pref.setProductData(json);
            }else status = false;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    public Productdata getProductDetailsbySku(String skucode)
    {
        /*Productdata productdata = new Productdata();
        try {
            JSONArray jsonArray = new JSONArray(pref.getProductData());
            for(int i=0; i<jsonArray.length();i++)
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if(String.valueOf(jsonObject1.get("sku")).equals(skucode)) {
                    productdata.setProductName(String.valueOf(jsonObject1.get("productName")));
                    productdata.setPrice(String.valueOf(jsonObject1.get("price")));
                    productdata.setBrand(String.valueOf(jsonObject1.get("brand")));
                    productdata.setSku(String.valueOf(jsonObject1.get("sku")));
                    break;
                 }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        Productdata productdata = dbHelper.getItemBySkuCode(skucode);
    return productdata;
    }

    public String genrateOrderID()
    {
        int oid = 0;
        String orderid;
        String imei =  pref.getIMEI();
        String slast5imei = imei.substring(Math.max(imei.length() - 5, 0));
        if(pref.getAutoIncreamentId()==0)   //|| pref.getAutoIncreamentId()==Constants.INITIAL_ORDER_ID
        {
            oid = Constants.INITIAL_ORDER_ID;
            pref.setAutoIncreamentId(oid);
        }else{
            oid = pref.getAutoIncreamentId();
        }
        orderid = slast5imei+oid;
        //System.out.println("dbHelper.getOrderStatusByOrderId(orderid):"+orderid+":"+dbHelper.getOrderStatusByOrderId(orderid));
        //dbHelper.numberOfOrderDetailsByOrderId(orderid)>0 &&
        if(!dbHelper.getOrderStatusByOrderId(orderid).equals("false")
                && !dbHelper.getOrderStatusByOrderId(orderid).equals(Constants.ORDER_INITIAL_STATUS))
        {
            int a = pref.getAutoIncreamentId()+1;
            pref.setAutoIncreamentId(a);
            orderid = slast5imei+a;
            //System.out.println("utility orderid:"+orderid);
        }
        //String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //orderid = timestamp+last 5 digit IMEI+ INITIAL_ORDER_ID(Auto increamented)
        //orderid = orderid+timestamp;
        return orderid;
    }
    public String genrateCustomerID()
    {
        String customerid = null;
        int cid = 0;
        String imei =  pref.getIMEI();
        String slast5imei = imei.substring(Math.max(imei.length() - 5, 0));
        slast5imei ="C"+slast5imei;
        if(pref.getCustomerAutoIncreamentId()==0)   //|| pref.getAutoIncreamentId()==Constants.INITIAL_ORDER_ID
        {
            cid = Constants.INITIAL_ORDER_ID;
            pref.setCustomerAutoIncreamentId(cid);
        }else {
            cid = pref.getCustomerAutoIncreamentId();
        }
        customerid = slast5imei + cid;
        System.out.println("dbHelper.getCustomerStatusByCustomerId(orderid):"+customerid+":"+dbHelper.getCustomerStatusByCustomerId(customerid));
        //dbHelper.numberOfOrderDetailsByOrderId(orderid)>0 &&
        if(dbHelper.getCustomerStatusByCustomerId(customerid))
            {
            int a = pref.getCustomerAutoIncreamentId()+1;
            pref.setCustomerAutoIncreamentId(a);
            customerid = slast5imei + a;
            System.out.println("utility customerid:"+customerid);
        }
        return customerid;
    }

   public void saveItemsindb(String data)
   {
       JSONArray jArray = null;
       try {
           JSONObject jsonObj = new JSONObject(data);
           jArray = jsonObj.getJSONArray(Constants.PRODUCTS);

           for(int i=0;i<jArray.length();i++)
           {
               JSONObject json_data = jArray.getJSONObject(i);
               String productName = json_data.getString("productName");
               String productPrice= json_data.getString("price");
               String productBrand= json_data.getString("brand");
               String productSku= json_data.getString("sku");
               Productdata productdata = dbHelper.getItemBySkuCode(productSku);
               if(productdata!=null && productdata.getSku().equals(productSku)) {
                   dbHelper.updateItem(productPrice,productName,productBrand,productSku);
               }
               else
               {
                   dbHelper.insertItem(productPrice, productName, productBrand, productSku, "");
               }
               //dbHelper.insertItem(productPrice,productName,productBrand, productSku, "");
           }
       } catch (JSONException e) {
           e.printStackTrace();
       }
   }
    public void saveUsersindb(String data)
    {
        JSONArray jArray = null;
        try {
            JSONObject jsonObj = new JSONObject(data);
            jArray = jsonObj.getJSONArray(Constants.USERS);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject json_data = jArray.getJSONObject(i);
                String userName = json_data.getString("userId");
                String userEncryptedPassword= json_data.getString("encryptedPassword");
                String userRole= json_data.getString("role");
                dbHelper.insertUser(userName,userEncryptedPassword,userRole);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Productdata getProductDetailsbyCouponsItemSku(String skucode)
    {
        Productdata productdata = dbHelper.getItemByCouponsItemSkuCode(skucode);
        return productdata;
    }
    public Productdata getProductDetailsbyCouponsItemSku12(String skucode,String cCode)
    {

        Productdata productdata = dbHelper.getItemByCouponsItemSkuCode12(skucode,cCode);
        return productdata;
    }
// Get common deatils and make a Json
    public String getAllCustomerJson()
    {   String result = null;
        ArrayList<Customers> customersArrayList = dbHelper.getAllCustomer();
        JSONArray jArray = new JSONArray();
        try {
            for(int i=0;i<customersArrayList.size();i++)
            {
                JSONObject json_data = new JSONObject();
                json_data.put("customerId",customersArrayList.get(i).getCustomerid());
                json_data.put("name",customersArrayList.get(i).getCustomername());
                json_data.put("email",customersArrayList.get(i).getCustomeremail());
                json_data.put("address",customersArrayList.get(i).getCustomerstreet());
                json_data.put("phone_number",customersArrayList.get(i).getCustomerphone());
                json_data.put("imei", pref.getIMEI());
                jArray.put(i,json_data);
            }
            result = jArray.toString();
            System.out.println("jarray:"+jArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    // Get orders & orders deatils and make a Json
    public String getAllOrdersJson()
    {
        String result = "";
        JSONArray jArray = new JSONArray();
        JSONArray jArray1 = new JSONArray();
        ArrayList<Orders> ordersArrayList = dbHelper.getAllOrders();
        try {
            for(int i=0;i<ordersArrayList.size();i++)
            {
                JSONObject json_data = new JSONObject();
                json_data.put("orderId",ordersArrayList.get(i).getOrderId());
                json_data.put("orderStatus",ordersArrayList.get(i).getOrderStatus());
                json_data.put("orderOffers",ordersArrayList.get(i).getOrderOffers());
                json_data.put("orderPromotions",ordersArrayList.get(i).getOrderPromotions());
                json_data.put("orderPaymentType",ordersArrayList.get(i).getOrderPaymentType());
                json_data.put("orderPaymentPrice",ordersArrayList.get(i).getOrderPaymentPrice());
                json_data.put("orderPaymentStatus",ordersArrayList.get(i).getOrderPaymentStatus());
                json_data.put("orderCustomerId",ordersArrayList.get(i).getOrderCustomerId());
                json_data.put("orderCreatedBy",ordersArrayList.get(i).getOrderCreatedBy());
                json_data.put("orderCreatedAt",ordersArrayList.get(i).getOrderCreatedAt());
                ArrayList<OrderDetails> orderDetailsArrayList = dbHelper.getOrderDetailsByOrderId(ordersArrayList.get(i).getOrderId());
                for(int j=0;j<orderDetailsArrayList.size();j++) {
                    JSONObject json_data1 = new JSONObject();
                    json_data1.put("productName",orderDetailsArrayList.get(j).getsItemName());
                    json_data1.put("productPrice",orderDetailsArrayList.get(j).getItemPrice());
                    json_data1.put("productSku",orderDetailsArrayList.get(j).getItemSku());
                    json_data1.put("productQty",orderDetailsArrayList.get(j).getItemQty());
                    String couponcode = "",couponvalue = "";
                    if(orderDetailsArrayList.get(j).getCouponsArrayList()!=null) {
                        couponcode = orderDetailsArrayList.get(j).getCouponsArrayList().get(0).getCpCouponSkuCode();
                        couponvalue = orderDetailsArrayList.get(j).getCouponsArrayList().get(0).getCpValue();
                    }
                    json_data1.put("procductCcode", couponcode);
                    json_data1.put("procductCvalue", couponvalue);
                    jArray1.put(j,json_data1);
                }
                json_data.put("products",jArray1);
                jArray.put(i,json_data);
            }
            System.out.println("jarray:"+jArray);
            result = jArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void StartAsyncTaskInParallel(SyncAdapter task) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }

}

package com.ankit.pointofsolution.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Ankit on 08-Sep-16.
 */
public class OrderDetails implements Parcelable {

    private String sOrderId;
    private String sItemSku;
    private String sItemName;
    private double iItemQty;
    private float iItemPrice;
    private String sOrderStatus;
    private String OrderValue;
    private ArrayList<Coupons> couponsArrayList;


    public OrderDetails() {
    }

    protected OrderDetails(Parcel in) {
        sOrderId = in.readString();
        sItemSku = in.readString();
        sItemName = in.readString();
        iItemQty = in.readDouble();
        iItemPrice = in.readFloat();
        sOrderStatus = in.readString();
    }

    public static final Creator<OrderDetails> CREATOR = new Creator<OrderDetails>() {
        @Override
        public OrderDetails createFromParcel(Parcel in) {
            return new OrderDetails(in);
        }

        @Override
        public OrderDetails[] newArray(int size) {
            return new OrderDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sOrderId);
        parcel.writeString(sItemSku);
        parcel.writeString(sItemName);
        parcel.writeDouble(iItemQty);
        parcel.writeFloat(iItemPrice);
        parcel.writeString(sOrderStatus);
    }

    public void setOrderId(String sOrderId)
    {
        this.sOrderId = sOrderId;
    }
    public String getOrderId()
    {
        return sOrderId;
    }

    public void setItemSku(String sItemSku)
    {
        this.sItemSku = sItemSku;
    }
    public String getItemSku()
    {
        return sItemSku;
    }

    public void setsItemName(String sItemName) { this.sItemName = sItemName;}
    public String getsItemName() { return sItemName; }

    public void setItemQty(double iItemQty)
    {
        this.iItemQty = iItemQty;
    }
    public double getItemQty() { return iItemQty;  }

    public void setItemPrice(float iItemPrice)
    {
        this.iItemPrice = iItemPrice;
    }
    public float getItemPrice() { return iItemPrice;  }

    public void setOrderStatus(String sOrderStatus)
    {
        this.sOrderStatus = sOrderStatus;
    }
    public String getOrderStatus()
    {
        return sOrderStatus;
    }

    public String getOrderValue() {
        return OrderValue;
    }

    public void setOrderValue(String orderValue) {
        OrderValue = orderValue;
    }
    public ArrayList<Coupons> getCouponsArrayList() {
        return couponsArrayList;
    }

    public void setCouponsArrayList(ArrayList<Coupons> couponsArrayList) {
        this.couponsArrayList = couponsArrayList;
    }


}

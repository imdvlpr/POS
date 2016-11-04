package com.ankit.pointofsolution.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ankit on 29-Sep-16.
 */

public class Orders implements Parcelable {

    private String orderId;
    private String orderStatus;
    private String orderStorageStatus;
    private String orderOffers;
    private String orderPromotions;
    private String orderPaymentType;
    private String orderPaymentPrice;
    private String orderPaymentStatus;
    private String orderCreatedAt;
    private String orderUpdatedAt;

    private String orderCreatedBy;
    private String orderCustomerId;


    public Orders() {
    }


    protected Orders(Parcel in) {
        orderId = in.readString();
        orderStatus = in.readString();
        orderStorageStatus = in.readString();
        orderOffers = in.readString();
        orderPromotions = in.readString();
        orderPaymentType = in.readString();
        orderPaymentPrice = in.readString();
        orderPaymentStatus = in.readString();
        orderCreatedAt = in.readString();
        orderUpdatedAt = in.readString();
        orderCreatedBy = in.readString();
        orderCustomerId = in.readString();
    }

    public static final Creator<Orders> CREATOR = new Creator<Orders>() {
        @Override
        public Orders createFromParcel(Parcel in) {
            return new Orders(in);
        }

        @Override
        public Orders[] newArray(int size) {
            return new Orders[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStorageStatus() {
        return orderStorageStatus;
    }

    public void setOrderStorageStatus(String orderStorageStatus) {
        this.orderStorageStatus = orderStorageStatus;
    }

    public String getOrderOffers() {
        return orderOffers;
    }

    public void setOrderOffers(String orderOffers) {
        this.orderOffers = orderOffers;
    }

    public String getOrderPromotions() {
        return orderPromotions;
    }

    public void setOrderPromotions(String orderPromotions) {
        this.orderPromotions = orderPromotions;
    }

    public String getOrderCreatedAt() {
        return orderCreatedAt;
    }

    public void setOrderCreatedAt(String orderCreatedAt) {
        this.orderCreatedAt = orderCreatedAt;
    }

    public String getOrderUpdatedAt() {
        return orderUpdatedAt;
    }

    public void setOrderUpdatedAt(String orderUpdatedAt) {
        this.orderUpdatedAt = orderUpdatedAt;
    }

    public String getOrderPaymentType() {
        return orderPaymentType;
    }

    public void setOrderPaymentType(String orderPaymentType) {
        this.orderPaymentType = orderPaymentType;
    }

    public String getOrderPaymentPrice() {
        return orderPaymentPrice;
    }

    public void setOrderPaymentPrice(String orderPaymentPrice) {
        this.orderPaymentPrice = orderPaymentPrice;
    }

    public String getOrderPaymentStatus() {
        return orderPaymentStatus;
    }

    public void setOrderPaymentStatus(String orderPaymentStatus) {
        this.orderPaymentStatus = orderPaymentStatus;
    }

    public String getOrderCreatedBy() {
        return orderCreatedBy;
    }

    public void setOrderCreatedBy(String orderCreatedBy) {
        this.orderCreatedBy = orderCreatedBy;
    }

    public String getOrderCustomerId() {
        return orderCustomerId;
    }

    public void setOrderCustomerId(String orderCUstomerId) {
        this.orderCustomerId = orderCUstomerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderId);
        parcel.writeString(orderStatus);
        parcel.writeString(orderStorageStatus);
        parcel.writeString(orderOffers);
        parcel.writeString(orderPromotions);
        parcel.writeString(orderPaymentType);
        parcel.writeString(orderPaymentPrice);
        parcel.writeString(orderPaymentStatus);
        parcel.writeString(orderCreatedAt);
        parcel.writeString(orderUpdatedAt);
        parcel.writeString(orderCreatedBy);
        parcel.writeString(orderCustomerId);
    }
}

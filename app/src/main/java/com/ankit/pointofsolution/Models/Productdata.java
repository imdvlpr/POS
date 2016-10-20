package com.ankit.pointofsolution.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ankit on 30-Aug-16.
 */
public class Productdata implements Parcelable {
    public String productName;
    public String price;
    public String sku;
    public String brand;
    public String offers;
    public Coupons coupons;

    public Productdata(){}

    public Productdata(String productName, String price, String sku, String brand, String offers) {
        this.productName = productName;
        this.price = price;
        this.sku = sku;
        this.brand = brand;
        this.offers = offers;
    }

    protected Productdata(Parcel in) {
        productName = in.readString();
        price = in.readString();
        sku = in.readString();
        brand = in.readString();
        offers = in.readString();
    }

    public static final Creator<Productdata> CREATOR = new Creator<Productdata>() {
        @Override
        public Productdata createFromParcel(Parcel in) {
            return new Productdata(in);
        }

        @Override
        public Productdata[] newArray(int size) {
            return new Productdata[size];
        }
    };

    public void setProductName(String productName)
    {
        this.productName = productName;
    }
    public String getProductName()
    {
        return productName;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }
    public String getPrice()
    {
        return price;
    }

    public void setSku(String sku)
    {
        this.sku = sku;
    }
    public String getSku() { return sku;  }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }
    public String getBrand() { return brand;  }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productName);
        parcel.writeString(price);
        parcel.writeString(sku);
        parcel.writeString(brand);
        parcel.writeString(offers);
    }

    public Coupons getCoupons() {
        return coupons;
    }

    public void setCoupons(Coupons coupons) {
        this.coupons = coupons;
    }
}

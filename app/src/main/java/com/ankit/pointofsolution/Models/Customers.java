package com.ankit.pointofsolution.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ankit on 28-Oct-16.
 */

public class Customers implements Parcelable {
    private String customerid;
    private String customername;
    private String customeremail;
    private String customerstreet;
    private String customerplace;
    private String customerphone;
    private String customerphonecode;
    private String customerloyaltyid;

    public Customers() {
    }

    protected Customers(Parcel in) {
        customername = in.readString();
        customeremail = in.readString();
        customerstreet = in.readString();
        customerplace = in.readString();
        customerphone = in.readString();
        customerid = in.readString();
        customerphonecode = in.readString();
        customerloyaltyid = in.readString();
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getCustomeremail() {
        return customeremail;
    }

    public void setCustomeremail(String customeremail) {
        this.customeremail = customeremail;
    }

    public String getCustomerstreet() {
        return customerstreet;
    }

    public void setCustomerstreet(String customerstreet) {
        this.customerstreet = customerstreet;
    }

    public String getCustomerplace() {
        return customerplace;
    }

    public void setCustomerplace(String customerplace) {
        this.customerplace = customerplace;
    }

    public String getCustomerphone() {
        return customerphone;
    }

    public void setCustomerphone(String customerphone) {
        this.customerphone = customerphone;
    }

    public String getCustomerphonecode() {
        return customerphonecode;
    }

    public void setCustomerphonecode(String customerphonecode) {
        this.customerphonecode = customerphonecode;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getCustomerloyaltyid() {
        return customerloyaltyid;
    }

    public void setCustomerloyaltyid(String customerloyaltyid) {
        this.customerloyaltyid = customerloyaltyid;
    }

    public static final Creator<Customers> CREATOR = new Creator<Customers>() {
        @Override
        public Customers createFromParcel(Parcel in) {
            return new Customers(in);
        }

        @Override
        public Customers[] newArray(int size) {
            return new Customers[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customername);
        dest.writeString(customeremail);
        dest.writeString(customerstreet);
        dest.writeString(customerplace);
        dest.writeString(customerphone);
        dest.writeString(customerid);
        dest.writeString(customerphonecode);
        dest.writeString(customerloyaltyid);
    }
}

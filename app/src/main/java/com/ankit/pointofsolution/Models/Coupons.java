package com.ankit.pointofsolution.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ankit on 12-Oct-16.
 */

public class Coupons implements Parcelable {
    private String cpItemSkuCode;
    private String cpCouponSkuCode;
    private String cpValue;
    private String cpStartDate;
    private String cpEndDate;

    public Coupons() {
    }

    public String getCpItemSkuCode() {
        return cpItemSkuCode;
    }

    public void setCpItemSkuCode(String cpItemSkuCode) {
        this.cpItemSkuCode = cpItemSkuCode;
    }

    public String getCpCouponSkuCode() {
        return cpCouponSkuCode;
    }

    public void setCpCouponSkuCode(String cpCouponSkuCode) {
        this.cpCouponSkuCode = cpCouponSkuCode;
    }

    public String getCpValue() {
        return cpValue;
    }

    public void setCpValue(String cpValue) {
        this.cpValue = cpValue;
    }

    public String getCpStartDate() {
        return cpStartDate;
    }

    public void setCpStartDate(String cpStartDate) {
        this.cpStartDate = cpStartDate;
    }

    public String getCpEndDate() {
        return cpEndDate;
    }

    public void setCpEndDate(String cpEndDate) {
        this.cpEndDate = cpEndDate;
    }

    protected Coupons(Parcel in) {
        cpItemSkuCode = in.readString();
        cpCouponSkuCode = in.readString();
        cpValue = in.readString();
        cpStartDate = in.readString();
        cpEndDate = in.readString();
    }

    public static final Creator<Coupons> CREATOR = new Creator<Coupons>() {
        @Override
        public Coupons createFromParcel(Parcel in) {
            return new Coupons(in);
        }

        @Override
        public Coupons[] newArray(int size) {
            return new Coupons[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cpItemSkuCode);
        parcel.writeString(cpCouponSkuCode);
        parcel.writeString(cpValue);
        parcel.writeString(cpStartDate);
        parcel.writeString(cpEndDate);
    }

}

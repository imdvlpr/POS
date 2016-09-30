package com.ankit.pointofsolution.Models;

import java.io.Serializable;

/**
 * Created by Ankit on 30-Aug-16.
 */
public class Productdata implements Serializable {
    public String productName;
    public String price;
    public String sku;
    public String brand;
    public int offers;


    public Productdata(){}

    public Productdata(String productName, String price, String sku, String brand, int offers) {
        this.productName = productName;
        this.price = price;
        this.sku = sku;
        this.brand = brand;
        this.offers = offers;
    }

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

    public int getOffers() {
        return offers;
    }

    public void setOffers(int offers) {
        this.offers = offers;
    }
}

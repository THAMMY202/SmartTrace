package com.smart.trace.model;

public class Product {
    private String name;
    private String maker;
    private String model;
    private String serialNumber;
    private String picture;
    private String price;
    private String condition;
    private String key;
    private String seller;
    private String sellerUserID;
    private String buyerUserID;

    public String getSellerUserID() {
        return sellerUserID;
    }

    public void setSellerUserID(String sellerUserID) {
        this.sellerUserID = sellerUserID;
    }

    public String getBuyerUserID() {
        return buyerUserID;
    }

    public void setBuyerUserID(String buyerUserID) {
        this.buyerUserID = buyerUserID;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }



    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }



    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public Product(String name, String maker, String model, String serialNumber, String picture, String key,String price,String seller,String condition,String sellerUserID,String buyerUserID) {
        this.name = name;
        this.maker = maker;
        this.model = model;
        this.serialNumber = serialNumber;
        this.picture = picture;
        this.key = key;
        this.price = price;
        this.seller = seller;
        this.condition = condition;
        this.sellerUserID = sellerUserID;
        this.buyerUserID = buyerUserID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Product(){

    }
}

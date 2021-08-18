package com.smart.trace.model;

/*
*  newImage.put("name", editTextName.getText().toString());
        newImage.put("model", editTextModel.getText().toString());
        newImage.put("serialNumber", editTextSerialNumber.getText().toString());
        newImage.put("message", editTextMessage.getText().toString());
        newImage.put("userID", userID);
*
* */

public class BlackListedItem {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private String model;
    private String serialNumber;
    private String message;
    private String userID;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public  BlackListedItem(){}

    public BlackListedItem(String model, String serialNumber, String message, String userID,String name) {
        this.model = model;
        this.serialNumber = serialNumber;
        this.message = message;
        this.userID = userID;
        this.name = name;
    }
}

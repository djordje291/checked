package com.djordjeratkovic.checked.model;

import com.djordjeratkovic.checked.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpirationDate {
    //TODO: add price here so you can have different prices for the same product

    private Date expirationDate;
    private int quantity;
    private int price;

    public ExpirationDate() {
    }

    public ExpirationDate(Date expirationDate, int quantity) {
        this.expirationDate = expirationDate;
        this.quantity = quantity;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String gatDateString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        return simpleDateFormat.format(expirationDate);
    }
}

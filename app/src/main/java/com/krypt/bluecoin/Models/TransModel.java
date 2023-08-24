package com.krypt.bluecoin.Models;

public class TransModel {
    String transid;
    String Amount;
    String currency;

    String date;

    public TransModel(String transid, String amount, String currency, String date) {
        this.transid = transid;
        Amount = amount;
        this.currency = currency;
        this.date = date;
    }

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

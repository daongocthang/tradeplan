package com.standalone.tradeplan.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class DataPlan implements Serializable {

    private int id;
    private String symbol;
    private int quantity;
    private double entryPoint;
    private double targetPrice;
    private double stopLoss;
    private int RRR;
    private String date;
    private String setups;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(double entryPoint) {
        this.entryPoint = entryPoint;
    }

    public double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }

    public double getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(double stopLoss) {
        this.stopLoss = stopLoss;
    }

    public int getRRR() {
        return RRR;
    }

    public void setRRR(int ratio) {
        this.RRR = ratio;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String strDate) {
        this.date = strDate;
    }

    public String getSetups() {
        return setups;
    }

    public void setSetups(String setups) {
        this.setups = setups;
    }

    @NonNull
    public String toString() {
        return String.format("id={},date={},symbol={}", id, symbol);
    }
}

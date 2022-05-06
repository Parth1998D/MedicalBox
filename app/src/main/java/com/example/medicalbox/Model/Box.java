package com.example.medicalbox.Model;

import java.util.ArrayList;

public class Box {
    private String med;
    private int boxNumber;
    private int take;
    private int available;
    private String illness;
    private ArrayList<String> timeList;

    public Box() {}

    public Box(String med, int boxNumber, int take, int available, String illness, ArrayList<String> timeList) {
        this.med = med;
        this.boxNumber = boxNumber;
        this.take = take;
        this.available = available;
        this.illness = illness;
        this.timeList = timeList;
    }

    public String getMed() {
        return med;
    }

    public void setMed(String med) {
        this.med = med;
    }

    public int getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(int boxNumber) {
        this.boxNumber = boxNumber;
    }

    public int getTake() {
        return take;
    }

    public void setTake(int take) {
        this.take = take;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getIllness() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }

    public ArrayList<String> getTimeList() {
        return timeList;
    }

    public void setTimeList(ArrayList<String> timeList) {
        this.timeList = timeList;
    }
}



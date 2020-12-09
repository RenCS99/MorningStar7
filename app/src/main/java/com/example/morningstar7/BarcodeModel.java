package com.example.morningstar7;

import androidx.annotation.NonNull;

public class BarcodeModel {

    private int barcodeId;
    private int containerName;
    private double latitude;
    private double longitude;
    private int row;
    private int section;
    private String lastUpdated;
    private int sync_status;

    // Constructors

    public BarcodeModel(int barcodeId, int containerName, double latitude, double longitude, int row, int section, String lastUpdated, int sync_status) {
        this.barcodeId = barcodeId;
        this.containerName = containerName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.row = row;
        this.section = section;
        this.lastUpdated = lastUpdated;
        this.sync_status = sync_status;
    }


    // toString is necessary for printing the contents of a class object

    @NonNull
    @Override
    public String toString() {
        return "UserRegistrationModel{" +
                ", barcodeId='" + barcodeId + '\'' +
                ", containerName='" + containerName + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", row='" + row + '\'' +
                ", section='" + section + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", syncStatus='" + sync_status +
                '}';
    }

    // Getters and Setters

    public int getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(int barcodeId) {
        this.barcodeId = barcodeId;
    }

    public int getContainerName() {
        return containerName;
    }

    public void setContainerName(int containerName) {
        this.containerName = containerName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getSync_status() {
        return sync_status;
    }

    public void setSync_status(int sync_status) {
        this.sync_status = sync_status;
    }
}
package com.example.morningstar7;

public class SyncEntry {
    private String barcode_id;
    private String container_name;
    private double latitude;
    private double longitude;
    private int barcode_row;
    private int barcode_sec;
    private String lastUpdated;
    private int sync_status;

    SyncEntry(String barcode_id, String container_name, double latitude, double longitude, int barcode_row, int barcode_sec, String lastUpdated, int Sync_status){
        this.setBarcode_id(barcode_id);
        this.setContainer_name(container_name);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setBarcode_row(barcode_row);
        this.setBarcode_sec(barcode_sec);
        this.setLastUpdated(lastUpdated);
        this.setSync_status(Sync_status);
    }

    public String getBarcode_id() {
        return barcode_id;
    }

    public void setBarcode_id(String barcode_id) {
        this.barcode_id = barcode_id;
    }

    public String getContainer_name() {
        return container_name;
    }

    public void setContainer_name(String container_name) {
        this.container_name = container_name;
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

    public int getBarcode_row() {
        return barcode_row;
    }

    public void setBarcode_row(int barcode_row) {
        this.barcode_row = barcode_row;
    }

    public int getBarcode_sec() {
        return barcode_sec;
    }

    public void setBarcode_sec(int barcode_sec) {
        this.barcode_sec = barcode_sec;
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

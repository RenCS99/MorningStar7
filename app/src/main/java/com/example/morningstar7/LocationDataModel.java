package com.example.morningstar7;

public class LocationDataModel {

    private int containerId;
    private float latitude;
    private float longitude;

    public LocationDataModel(int containerId, float latitude, float longitude){
        this.containerId = containerId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationDataModel(){

    }

    // toString is necessary for printing the contents of a class object

    @Override
    public String toString(){
        return "LocationDataModel{" + "containerId=" + containerId +
                ", latitude=" + latitude + ", longitude=" + longitude + '}';
    }

    // Getters and Setters


    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}

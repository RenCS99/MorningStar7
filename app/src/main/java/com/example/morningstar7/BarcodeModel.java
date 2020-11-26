package com.example.morningstar7;

public class BarcodeModel {

    private int containerId;
    private int barcodeId;
    private int containerName;
    private int dirtyBit;

    // Constructors

    public BarcodeModel(int containerId, int barcodeId, int containerName, int dirtyBit){
        this.containerId = containerId;
        this.barcodeId = barcodeId;
        this.containerName = containerName;
        this.dirtyBit = dirtyBit;
    }


    // toString is necessary for printing the contents of a class object

    @Override
    public String toString() {
        return "UserRegistrationModel{" +
                "containerId=" + containerId +
                ", barcodeId='" + barcodeId + '\'' +
                ", containerName='" + containerName + '\'' +
                ", dirtyBit='" + dirtyBit + '\'' +
                '}';
    }

    // Getters and Setters


    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

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

    public int getDirtyBit() {
        return dirtyBit;
    }

    public void setDirtyBit(int dirtyBit) {
        this.dirtyBit = dirtyBit;
    }
}
package com.example.morningstar7;

public class SyncEntry {
    private int scan_id;
    private int Sync_status;

    SyncEntry(int item, int Sync_status){
        this.setScan_id(item);
        this.setSync_status(Sync_status);
    }

    public int getScan_id() {
        return scan_id;
    }

    public int getSync_status() {
        return Sync_status;
    }

    public void setScan_id(int scan_id) { this.scan_id = scan_id; }

    public void setSync_status(int sync_status) {
        Sync_status = sync_status;
    }
}

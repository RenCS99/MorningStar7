package com.example.morningstar7;

public class RowsAndSectionsModel {

    private int containerId;
    private int row;
    private int section;

    // Constructors

    public RowsAndSectionsModel(int containerId, int row, int section){
        this.containerId = containerId;
        this.row = row;
        this.section = section;
    }

    public RowsAndSectionsModel(){

    }

    // toString is necessary for printing the contents of a class object

    @Override
    public String toString(){
        return "RowsAndSectionsModel{" + "id=" + containerId + ", row= " + row
                + ", section=" + section + '}';
    }

    // Getters and Setters

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }
//returning corect num of row
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
}

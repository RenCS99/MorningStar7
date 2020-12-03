package com.example.morningstar7;

public class RowColumnBarcodeModel {
// vars
    private int id;
    private int row;
    private int column;
    private String barcode;


    public RowColumnBarcodeModel(int anInt, String string, String cursorString) {

    }
//parms of rows and cols
    public RowColumnBarcodeModel(int id, int row, int column, String barcode) {
        this.id = id;
        this.row = row;
        this.column = column;
        this.barcode = barcode;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    //grab id
    public String toString() {
        return "RowColumnBardcodeModel{" +
                "id=" + id +
                ", row=" + row +
                ", column=" + column +
                ", barcode='" + barcode + '\'' +
                '}';
    }
}

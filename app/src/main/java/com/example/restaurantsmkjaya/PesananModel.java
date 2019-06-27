package com.example.restaurantsmkjaya;

public class PesananModel {
    private String detailId, name, qty, table;

    private byte[] photoUrl;

    public PesananModel(String detailId, String name, String qty, String table, byte[] photoUrl) {
        this.detailId = detailId;
        this.name = name;
        this.qty = qty;
        this.table = table;
        this.photoUrl = photoUrl;
    }

    public String getDetailId() {
        return detailId;
    }

    public String getName() {
        return name;
    }

    public String getQty() {
        return qty;
    }

    public String getTable() {
        return table;
    }

    public byte[] getPhotoUrl() {
        return photoUrl;
    }
}

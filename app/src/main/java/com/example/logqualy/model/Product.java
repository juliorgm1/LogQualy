package com.example.logqualy.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String nameProduct;
    private String descriptionProduct;
    private String date;
    private String photoProduct;

    public Product() {
    }

    public Product(String nameProduct, String descriptionProduct, String date, String photoProduct) {
        this.nameProduct = nameProduct;
        this.descriptionProduct = descriptionProduct;
        this.date = date;
        this.photoProduct = photoProduct;
    }

    public String getId() {
        return id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public String getDescriptionProduct() {
        return descriptionProduct;
    }

    public String getDate() {
        return date;
    }

    public String getPhotoProduct() {
        return photoProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescriptionProduct(String descriptionProduct) {
        this.descriptionProduct = descriptionProduct;
    }
}

package com.example.logqualy.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Product implements Serializable {
    private String id;
    private String nameProduct;
    private String descriptionProduct;
    private String date;
    private String photoProduct;

    public Product() {
    }

    public Product(String nameProduct, String descriptionProduct) {
        Locale BRAZIL = new Locale("pt","BR");
        this.nameProduct = nameProduct;
        this.descriptionProduct = descriptionProduct;
        this.date = DateFormat.getDateInstance(DateFormat.MEDIUM, BRAZIL).format(
                Calendar.getInstance().getTime()
        );
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
        return String.valueOf(date);
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

    public void setPhotoProduct(String photoProduct) {
        this.photoProduct = photoProduct;
    }
}

package com.barath.app.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue
    @Column(name="PRODUCT_ID")
    private Long productId;

    @Column(name="PRODUCT_NAME")
    private String productName;

    @Column(name="LOCATION_NAME")
    private String locationName;

    public Product(String productName, String locationName) {
        this.productName = productName;
        this.locationName = locationName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", locationName='" + locationName + '\'' +
                '}';
    }

    public Product() {
    }
}

package com.barath.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barath.app.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByProductName(String productName);
    List<Product> findByLocationName(String locationName);
    List<Product> findByLocationNameAndProductName(String locationName,String productName);
}

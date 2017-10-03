package com.barath.app.rest;

import com.barath.app.model.Product;

import com.barath.app.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.Cacheable;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/product",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProductController {


    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/create")
    public Product createProduct(@RequestBody  Product product) {

        return this.productService.createProduct(product);

    }


   @GetMapping(value = "/get/")
    public List<Product> getProductByName(@RequestParam(name = "productName") String productName) {

        if(!StringUtils.isEmpty(productName)) {
            return this.productService.getProductByName(productName);
        }
        return null;
    }



    @Cacheable("/all")
    public List<Product> getProducts() {
        return this.productService.getProducts();
    }


}
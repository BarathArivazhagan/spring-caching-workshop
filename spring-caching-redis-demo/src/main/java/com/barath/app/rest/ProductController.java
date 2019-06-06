package com.barath.app.rest;

import com.barath.app.entity.Product;
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
@RequestMapping(value = "/products",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProductController {


    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @PostMapping(value = "/new")
    public Product createProduct(@RequestBody  Product product) {

        return this.productService.createProduct(product);

    }


   @GetMapping(value = "/byName/")
    public List<Product> getProductByName(@RequestParam(name = "productName") String productName) {

        if(!StringUtils.isEmpty(productName)) {
            return this.productService.getProductByName(productName);
        }
        return null;
    }

    @PutMapping(value="/update")
    public Product updateProduct(@RequestBody  Product product) {

        return this.productService.updateProduct(product);

    }

    
    @PostMapping
    public List<Product> createProducts(@RequestBody List<Product> products) {
        return this.productService.createProducts(products);
    }
    
    @GetMapping
    public List<Product> getProducts() {
        return this.productService.getProducts();
    }

    @PostMapping("/clear")
    public String clearCache(){
        this.productService.clearCache();

        return "All caches are cleared";
    }


}
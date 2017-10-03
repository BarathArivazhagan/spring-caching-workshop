package com.barath.app.service;


import com.barath.app.model.Product;
import com.barath.app.repository.ProductRepository;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class ProductService {

    private static final Logger logger= LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository=productRepository;
    }


    public Product createProduct(Product product){

        logger.info("Saving the product with details {}",product.toString());
        if(!isProductExists(product)) {
            return this.productRepository.save(product);
        }
        return this.productRepository.findByProductName(product.getProductName()).stream().findFirst().get();

    }


    @Cacheable("productsByName")
    public List<Product> getProductByName(String productName){

        return this.productRepository.findByProductName(productName);

    }

    public boolean isProductExists(Product product){

        List<Product> products=this.productRepository.findByLocationAndProductName(product.getLocationName(),product.getProductName());
        if(products !=null && !products.isEmpty()){
            logger.info("product doesnt exists ");
            return false;
        }
        logger.info("product exists ");
        return true ;

    }

    @Cacheable("products")
    public List<Product> getProducts(){
        return this.productRepository.findAll();
    }

    @PostConstruct
    public void init(){

        //
    }
}

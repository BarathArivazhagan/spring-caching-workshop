package com.barath.app.service;


import com.barath.app.entity.Product;
import com.barath.app.repository.ProductRepository;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private static final Logger logger= LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository=productRepository;
    }

   @Caching(evict= {  @CacheEvict(cacheNames= {"products"},allEntries=true)}, 
		   put = {  @CachePut(value = "productsByName",key = "#product.productName") })
    public Product createProduct(Product product){

        if(logger.isInfoEnabled()) { logger.info("Saving the product with details {}",product.toString()); }       
        return this.productRepository.save(product);     
    }


    @Cacheable(value = "productsByName")
    public List<Product> getProductByName(String productName){
    	if(logger.isInfoEnabled()) {  logger.info("Getting product by name {}",productName); }
        return this.productRepository.findByProductName(productName);

    }

    @Caching(evict= {  @CacheEvict(cacheNames= {"products"},allEntries=true)}, 
 		   put = {  @CachePut(value = "productsByName",key = "#product.productName") })
    public Product updateProduct(Product product){    	
        if(logger.isInfoEnabled()) { logger.info("updating the product with details {}",product); }      
        return this.productRepository.save(product);
       
    }

    public boolean isProductExists(Product product){

        List<Product> products=this.productRepository.findByLocationNameAndProductName(product.getLocationName(),product.getProductName());
        if(products !=null && !products.isEmpty()){
            logger.info("product doesnt exists ");
            return false;
        }
        logger.info("product exists ");
        return true ;

    }

    @Cacheable(value="products")
    public List<Product> getProducts(){

        logger.info("GETTING ALL THE PRODUCTS");
        return this.productRepository.findAll();
    }
    
    @CacheEvict(allEntries=true, cacheNames= {"products"} )
	public List<Product> createProducts(List<Product> products) {
		
		return this.productRepository.saveAll(products);
	}

    @PostConstruct
    public void init(){

        //initialize some products to test the caching
        Product product1=new Product("TV","CHENNAI");
        Product product2=new Product("MOBILE","DELHI");
        Product product3=new Product("HEADPHONE","MUMBAI");
        Product product4=new Product("NETWORKCABLE","ANDHRA PRADESH");
        Product product5=new Product("KEYBOARD","KERALA");
        this.productRepository.saveAll(Arrays.asList(product1,product2,product3,product4,product5));
    }

    @CacheEvict(cacheNames = {"productsByName","products"},allEntries = true)
    public void clearCache(){

    }


}

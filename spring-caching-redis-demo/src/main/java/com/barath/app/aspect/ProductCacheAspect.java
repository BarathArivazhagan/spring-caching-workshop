package com.barath.app.aspect;

import java.lang.invoke.MethodHandles;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.barath.app.service.ProductService;

@Component
@Aspect
@Order(1)
public class ProductCacheAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final ProductService productService;
	
	
	
	public ProductCacheAspect(ProductService productService) {
		super();
		this.productService = productService;
	}

	@Pointcut(value="execution(* com.barath.app.service.ProductService.*(..)) && @annotation(org.springframework.cache.annotation.Caching)")
    public void anyCachingOperation() {}
	
	@Pointcut(value="execution(* com.barath.app.service.ProductService.*(..)) && @annotation(org.springframework.cache.annotation.CacheEvict)")
    public void anyCacheEvictOperation() {}
	
	@After(value="anyCacheEvictOperation() || anyCachingOperation() ")
	public void eagerlyLoadProductsCache() {
		
		if(logger.isInfoEnabled()) {
			logger.info("cache evict operation is invoked");
		}
		// load products eagerly post evict operation
		this.productService.getProducts();
		
	}	
	

}

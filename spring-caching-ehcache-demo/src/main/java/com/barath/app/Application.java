package com.barath.app;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableCaching
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

@RestController
class CodeCategoryController{
	
	private final CodeCategoryService service;
	
	
	
	public CodeCategoryController(CodeCategoryService service) {
		super();
		this.service = service;
	}




	@GetMapping("/code")
	public List<CodeCategory> findByCodeValue(@RequestParam("value") @NotNull String code){

	   return this.service.findByCode(code);
	}
	
	
	
}

@Service
class CodeCategoryService{
	
	private final CodeCategoryRepository repo;
	private final CacheManager cacheManager;

	public CodeCategoryService(CodeCategoryRepository repo,CacheManager cacheManager) {
		super();
		this.repo = repo;
		this.cacheManager=cacheManager;
	}
	
	public List<CodeCategory> findByCode(String code){
		printCache();
		return this.repo.findByCodeValue(code);
	}
	
	@PostConstruct
	public void init() {
		
		Arrays.asList(new CodeCategory(1L, "100"),new CodeCategory(2L, "200"),new CodeCategory(3L, "300"))
		  .stream().forEach(repo::save);
		printCache();
	
	}
	
	private void printCache() {
		Cache<String, Object> cache= this.cacheManager.getCache("codeValues");
		cache.forEach( entry ->{
			System.out.println("key "+ entry.getKey()+" value "+entry.getValue());
		});
	}
	
	@EventListener(classes = ApplicationStartedEvent.class )
	public void listenToStart(ApplicationStartedEvent event) {
		this.repo.findByCodeValue("100");
	}

	
	
}




interface CodeCategoryRepository extends JpaRepository<CodeCategory, Long>{
	
	@Cacheable(value = "codeValues")
	List<CodeCategory> findByCodeValue(String code);
}

@Entity
@Table
class CodeCategory implements Serializable{
	
	private static final long serialVersionUID = -9165352047415329955L;

	@Id
	private Long codeId;
	
	private String codeValue;

	public Long getCodeId() {
		return codeId;
	}

	public void setCodeId(Long codeId) {
		this.codeId = codeId;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String code) {
		this.codeValue = code;
	}

	@Override
	public String toString() {
		return "CodeCategory [codeId=" + codeId + ", code=" + codeValue + "]";
	}

	public CodeCategory(Long codeId, String codeValue) {
		super();
		this.codeId = codeId;
		this.codeValue = codeValue;
	}

	public CodeCategory() {
		super();
		
	}
	
	
	
	
}

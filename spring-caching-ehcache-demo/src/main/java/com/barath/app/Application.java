package com.barath.app;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.cache.CacheManager;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.ehcache.core.EhcacheManager;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheManagerUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	public CodeCategoryService(CodeCategoryRepository repo) {
		super();
		this.repo = repo;
	}
	
	public List<CodeCategory> findByCode(String code){
		return this.repo.findByCodeValue(code);
	}
	
	@PostConstruct
	public void init() {
		
		Arrays.asList(new CodeCategory(1L, "100"),new CodeCategory(2L, "200"),new CodeCategory(3L, "300"))
		  .stream().forEach(repo::save);
		this.repo.findByCodeValue("100");
	}
	
	
}

interface CodeCategoryRepository extends JpaRepository<CodeCategory, Long>{
	
	@Cacheable(value = "codeValues",key = "#code")
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

package com.barath.app.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@Configuration
public class RedisCacheConfiguration {


    @Value("${redis.hostname}")
    private String redisHostName;


    @Value("${redis.port}")
    private int redisPort;


    @Value("${spring.cache.cache-names}")
    private List<String> cacheNames;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){

        JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisHostName);
        jedisConnectionFactory.setPort(redisPort);
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    public CacheManager redisCacheManager(RedisTemplate redisTemplate){

        RedisCacheManager redisCacheManager=new RedisCacheManager(redisTemplate);
        redisCacheManager.setCacheNames(cacheNames);
        redisCacheManager.setDefaultExpiration(1000);

        return redisCacheManager;
    }



}

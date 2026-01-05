package com.pm.journalapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

//service to interact with redis
@Slf4j
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> entityClass){
        try{
            Object value = redisTemplate.opsForValue().get(key);
            if(value!=null){
                //convert the retrieved value to desired entity type
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(value.toString(), entityClass);
            }

        } catch (Exception e) {
            log.error("Exception ", e);
        }
        return null;
    }

    //ttl -> time limit for expiration of cache
    public void set(String key, Object value, Long ttl) {
        try{
            if(key==null || value == null || ttl== null){
                log.error("Set called with null values.");
                return;
            }
            //convert the value to string
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
            log.info("Set {}: {} pair in redis for {} seconds", key,jsonValue, ttl);
        } catch (Exception e) {
            log.error("Exception ", e);
        }
    }
}

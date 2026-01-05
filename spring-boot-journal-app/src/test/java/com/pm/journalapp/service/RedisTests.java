package com.pm.journalapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void testSendEmail(){
        redisTemplate.opsForValue().set("email", "anshika@gmail.com");

        Object value = redisTemplate.opsForValue().get("email");
        System.out.println("Value of email: " +value);
        assertNotNull(value);
    }
}

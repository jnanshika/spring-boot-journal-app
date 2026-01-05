package com.pm.journalapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@SpringBootApplication
@EnableTransactionManagement
public class JournalAppApplication {

    private static final Logger log = LoggerFactory.getLogger(JournalAppApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(JournalAppApplication.class, args);
    }

    //Mongodb does not support atomicity by default, you have to explicitly configure it
    @Bean
    public PlatformTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        log.info("Creating transactionManager bean");
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}

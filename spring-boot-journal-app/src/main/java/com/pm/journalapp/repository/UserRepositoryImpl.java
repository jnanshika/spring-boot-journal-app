package com.pm.journalapp.repository;

import com.pm.journalapp.entity.User;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import java.util.List;

//used to write custom queries
public class UserRepositoryImpl {

    //mongoTemplate- use to interact with mongodb
    private MongoTemplate mongoTemplate;
    public UserRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public List<User> getUserForSA(){
        Query query = new Query();
        //criteria - set of rules or instructions. implemented in combination with query.
        query.addCriteria(Criteria.where("email").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        List<User> users=  mongoTemplate.find(query, User.class); //(query, where to run query). here collection is mentioned on class, so this query will run on users collection.
        return users;
    }
}

package com.pm.journalapp.service;

import com.pm.journalapp.entity.User;
import com.pm.journalapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public  UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveNewUser(User user){
        User savedUser = null;
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("user"));
            savedUser = userRepository.save(user);
        }
        catch (Exception e){
            log.error("Exception occurred in saveEntry. " + e.getMessage());
        }
        return savedUser;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(ObjectId id){
        return userRepository.findById(id);
    }

    public void deleteUserById(ObjectId id){
        userRepository.deleteById(id);
    }

    public User updateUserById(ObjectId id, @RequestBody User user){
        return userRepository.save(user);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User authenticatedUser(){
        log.info("Authentication started...");
        //authenticate user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("username is {}", username);
        //get entries for logged user
        User user = findByUserName(username);
        if(user == null){
            log.info("User not found with username={}", username);
            return null;
        }
        log.info("User found with username= {}", username);
        return user;
    }

    //only to update entry in users collection
    public void updateJournalEntryInUser(User user){
        try{
            log.info("Updating journal entry in user collection");
            userRepository.save(user);
        }
        catch (Exception e){
            log.error("Exception occurred in saveJournalEntryInUser. Message= {} ", e.getMessage());
        }
    }

    public User saveNewAdmin(User user){
        User savedUser= null;
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("admin"));
            savedUser = userRepository.save(user);
        }
        catch (Exception e){
            log.error("Exception occurred in saveEntry. " + e.getMessage());
        }
        return savedUser;
    }
}

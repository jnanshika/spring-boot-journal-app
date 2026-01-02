package com.pm.journalapp.controller;

import com.pm.journalapp.entity.User;
import com.pm.journalapp.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final UserService userService;
    public PublicController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-user")
    public void createUser(@RequestBody User user){
        userService.saveNewUser(user);
    }
    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK";
    }
}

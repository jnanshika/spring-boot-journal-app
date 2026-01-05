package com.pm.journalapp.controller;

import com.pm.journalapp.api.response.WeatherResponse;
import com.pm.journalapp.entity.User;
import com.pm.journalapp.service.UserService;
import com.pm.journalapp.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final WeatherService weatherService;
    public UserController(UserService userService, WeatherService weatherService) {
        this.userService = userService;
        this.weatherService = weatherService;
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDb = userService.findByUserName(username);
        if(userInDb == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userInDb.setPassword(user.getPassword());
        userInDb.setUserName(user.getUserName());
        userService.saveNewUser(userInDb);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/greeting")
    public ResponseEntity<?> greeting(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");
        String weatherFeelsLike = "";
        if( weatherResponse !=null)
            weatherFeelsLike = ", weather in Mumbai feels like " + weatherResponse.getCurrent().getFeelslike();
        return new ResponseEntity<>("Hey! " + authentication.getName() + weatherFeelsLike, HttpStatus.OK);
    }
}

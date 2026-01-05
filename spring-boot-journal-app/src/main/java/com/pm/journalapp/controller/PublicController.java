package com.pm.journalapp.controller;

import com.pm.journalapp.dto.LoginRequestDto;
import com.pm.journalapp.entity.User;
import com.pm.journalapp.service.UserDetailsServiceImpl;
import com.pm.journalapp.service.UserService;
import com.pm.journalapp.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/public")
public class PublicController {

    private final UserService userService;
    public PublicController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK";
    }

    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User savedUser = userService.saveNewUser(user);
        if( savedUser != null)
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        return new ResponseEntity<>(savedUser, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto user){
        try{
            //check if username and password are correct. password will be checked using passwordEncoder
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            //get userdetails of the user
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            //generate token
            String token = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        catch (AuthenticationException e) {
            log.error("Exception occurred while creating Authentication token");
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}

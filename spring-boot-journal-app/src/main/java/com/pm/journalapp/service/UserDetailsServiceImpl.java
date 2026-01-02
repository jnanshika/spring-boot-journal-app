package com.pm.journalapp.service;

import com.pm.journalapp.entity.User;
import com.pm.journalapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository  userRepository;
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Entered loadUserByUsername in userDetailImpl.");
        User user = userRepository.findByUserName(username);
        if (user != null && user.getUserName()!=null) {
            log.info("returning user to security layer with username={}, password= {}, role={}",user.getUserName(), user.getPassword(), user.getRoles());
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
//                    .authorities((GrantedAuthority) user.getRoles())
                    .build();

        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}

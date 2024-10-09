package com.solidcode.library.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BookUserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if ("hardcodedUser".equals(username)) {

            return new User("hardcodedUser", "", new ArrayList<>());
        } else {

            throw new UsernameNotFoundException("User not found");
        }
    }
}

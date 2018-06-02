package com.efrei.authenticator.controller;

import com.efrei.authenticator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticatorController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/hello")
    public String getHello(){
        return "Hello";
    }
}

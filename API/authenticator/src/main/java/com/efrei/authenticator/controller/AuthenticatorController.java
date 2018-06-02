package com.efrei.authenticator.controller;

import com.efrei.authenticator.model.User;
import com.efrei.authenticator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthenticatorController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/hello")
    public String getHello(){
        return "Hello";
    }

    @GetMapping("/users")
    public List<User> getAll(){
        return userRepository.findAll();
    }
}

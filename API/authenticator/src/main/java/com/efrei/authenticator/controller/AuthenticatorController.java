 package com.efrei.authenticator.controller;

import com.efrei.authenticator.dto.BasicAPIResponseDTO;
import com.efrei.authenticator.dto.LoginRequestDTO;
import com.efrei.authenticator.dto.SignUpRequestDTO;
import com.efrei.authenticator.repository.UserRepository;
import com.efrei.authenticator.repository.WebsiteRepository;
import com.efrei.authenticator.security.JwtTokenProvider;
import com.efrei.authenticator.services.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class AuthenticatorController {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    WebsiteRepository websiteRepository;

    @Autowired
    JwtTokenProvider tokenProvider;
    
    @Autowired
    UserDetailsServiceImpl service;

    @PostMapping("/token")
    public ResponseEntity<?> token(@Valid @RequestBody LoginRequestDTO loginRequest) {
    	return service.login(loginRequest.getUsernameOrEmail(),loginRequest.getPassword());
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO login, String url){
    	
    	//TODO
    	return null;
    }

    @PostMapping()
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequestDTO signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
       return service.register(signUpRequest.getUsername(),signUpRequest.getPassword(),signUpRequest.getEmail());
    }
    
    @GetMapping("/pincode")
    public ResponseEntity<?> getPincode(@Valid @RequestParam("token") String token) {
    	if(!tokenProvider.validateToken(token)) {
    		return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Token invalid"),
                    HttpStatus.BAD_REQUEST);
    	}
    	return service.getPincode(token);
    }
    
    @GetMapping("/websites")
    public ResponseEntity<?> getWebsites(@Valid @RequestParam("token") String token){
    	if(!tokenProvider.validateToken(token)) {
    		return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Token invalid"),
                    HttpStatus.BAD_REQUEST);
    	}
    	return service.getWebsites(token);
    }
}
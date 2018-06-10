 package com.efrei.authenticator.controller;

import com.efrei.authenticator.dto.BasicAPIResponseDTO;
import com.efrei.authenticator.dto.LoginRequestDTO;
import com.efrei.authenticator.dto.SignUpRequestDTO;
import com.efrei.authenticator.repository.UserRepository;
import com.efrei.authenticator.repository.WebsiteRepository;
import com.efrei.authenticator.security.JwtTokenProvider;
import com.efrei.authenticator.services.UserDetailsServiceImpl;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

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
    @ApiOperation("Create token based on login entity")
    public ResponseEntity<?> token(
    		@ApiParam("Login entity")@Valid @RequestBody LoginRequestDTO loginRequest) {
    	return service.login(loginRequest.getUsernameOrEmail(),loginRequest.getPassword());
    }
    
    @PostMapping("/login")
    @ApiOperation("Login into the application")
    public ResponseEntity<?> authenticateUser(
    		@ApiParam("Login entity") @Valid @RequestBody LoginRequestDTO login,
    		@ApiParam("Url of a website") @RequestParam("url") String url){
    	
    	//TODO
    	return null;
    }

    @PostMapping()
    @ApiOperation("Register a new user based on user entity")
    public ResponseEntity<?> registerUser(
    		@ApiParam("User entity") @Valid @RequestBody SignUpRequestDTO signUpRequest) {
    	
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
    @ApiOperation("Get a pincode based on token")
    public ResponseEntity<?> getPincode(
    		@ApiParam("Token of a user")@Valid @RequestParam("token") String token) {
    	//TODO check the origin (good mobile???)
    	
    	if(!tokenProvider.validateToken(token)) {
    		return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Token invalid"),
                    HttpStatus.BAD_REQUEST);
    	}
    	return service.getPincode(token);
    }
    
    @GetMapping("/websites")
    @ApiOperation("Get websites of a user based on token")
    public ResponseEntity<?> getWebsites(
    		@ApiParam("Token of a user") @Valid @RequestParam("token") String token){
    	if(!tokenProvider.validateToken(token)) {
    		return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Token invalid"),
                    HttpStatus.BAD_REQUEST);
    	}
    	return service.getWebsites(token);
    }
    
    @GetMapping("/websites/action-required")
    @ApiOperation("Get websites which need action based on token")
    public ResponseEntity<?> getWebsitesActionRequired(
    		@ApiParam("Token of a user") @Valid @RequestParam("token") String token){
    	if(!tokenProvider.validateToken(token)) {
    		return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Token invalid"),
                    HttpStatus.BAD_REQUEST);
    	}
    	return service.getWebsitesActionRequired(token);
    }
}
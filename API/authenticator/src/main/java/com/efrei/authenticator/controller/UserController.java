package com.efrei.authenticator.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.efrei.authenticator.dto.BasicAPIResponseDTO;
import com.efrei.authenticator.dto.SignUpRequestDTO;
import com.efrei.authenticator.repository.UserRepository;
import com.efrei.authenticator.repository.WebsiteRepository;
import com.efrei.authenticator.security.JwtTokenProvider;
import com.efrei.authenticator.services.UserDetailsServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
    UserRepository userRepository;
	
	@Autowired
	WebsiteRepository websiteRepository;
	
	@Autowired
    UserDetailsServiceImpl service;
	
	@Autowired
    JwtTokenProvider tokenProvider;
	
	@PostMapping()
    @ApiOperation("Register a new user based on user entity")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = String.class),
			@ApiResponse(code = 400, message = "Login is already taken") })
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
    @ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = String.class),
			@ApiResponse(code = 400, message = "Token invalid") })
    public ResponseEntity<?> getPincode(
    		@ApiParam("Token of a user")@Valid @RequestParam("token") String token) {
    	//TODO check the origin (good mobile???)
    	
    	if(!tokenProvider.validateToken(token)) {
    		return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Token invalid"),
                    HttpStatus.BAD_REQUEST);
    	}
    	return service.getPincode(token);
    }
}

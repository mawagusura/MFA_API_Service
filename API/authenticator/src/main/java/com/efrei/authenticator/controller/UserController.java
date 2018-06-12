package com.efrei.authenticator.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.efrei.authenticator.dto.BasicAPIResponseDTO;
import com.efrei.authenticator.model.UserWebsite;
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
    
    
    @GetMapping("/websites")
	@ApiOperation("Get websites of a user based on token")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Token invalid") })

	public ResponseEntity<?> getWebsites(@ApiParam("Token of a user") @Valid @RequestParam("token") String token) {
		if (!tokenProvider.validateToken(token)) {
			return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Token invalid"),
					HttpStatus.BAD_REQUEST);
		}
		return service.getWebsites(token);
	}

	@GetMapping("/websites/action-required")
	@ApiOperation("Get websites which need action based on token")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = UserWebsite.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Token invalid") })
	public ResponseEntity<?> getWebsitesActionRequired(
			@ApiParam("Token of a user") @Valid @RequestParam("token") String token) {
		if (!tokenProvider.validateToken(token)) {
			return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Token invalid"),
					HttpStatus.BAD_REQUEST);
		}
		return service.getWebsitesActionRequired(token);
	}
	
	
	@PostMapping("/websites/validate")
	@ApiOperation("Validate double authentification")
	public ResponseEntity<?> validatAuth(@Valid @RequestParam("tokens")String tokenMobile, String tokenUser ){
		//TODO
		return null;
	}
}

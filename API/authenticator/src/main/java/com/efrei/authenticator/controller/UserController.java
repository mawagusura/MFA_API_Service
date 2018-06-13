package com.efrei.authenticator.controller;

import javax.validation.Valid;

import com.efrei.authenticator.model.User;
import com.efrei.authenticator.security.BasicUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.efrei.authenticator.dto.BasicAPIResponseDTO;
import com.efrei.authenticator.dto.ValidateDTO;
import com.efrei.authenticator.model.UserWebsite;
import com.efrei.authenticator.repository.UserRepository;
import com.efrei.authenticator.repository.WebsiteRepository;
import com.efrei.authenticator.security.JwtTokenProvider;
import com.efrei.authenticator.services.UserDetailsServiceImpl;

import io.swagger.annotations.ApiOperation;
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
    
    
    @GetMapping("/websites")
	@ApiOperation("Get websites of a user based")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK")})

	public ResponseEntity<?> getWebsites() {
    	String username =((BasicUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

		return service.getWebsites(username);
	}

	@GetMapping("/websites/action-required")
	@ApiOperation("Get websites which need action based")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = UserWebsite.class, responseContainer = "List")})
	public ResponseEntity<?> getWebsitesActionRequired() {
		String username =((BasicUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		return service.getWebsitesActionRequired(username);
	}
	
	
	@PostMapping("/websites/validate")
	@ApiOperation("Validate double authentification")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "URL not register or code invalid")
	})
	public ResponseEntity<?> validateAuth(@Valid @RequestBody()ValidateDTO dto){
		
		if(!websiteRepository.existsByUrl(dto.getUrl())) {
			return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "URL not register"),
                    HttpStatus.BAD_REQUEST);
		}
		String username =((BasicUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		User user = userRepository.findByUsername(username).get();
		if(!dto.getPinCode().equals(user.getPincode())) {
			return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Code invalid"),
                    HttpStatus.BAD_REQUEST);
		}
		
		return service.validate(dto.getUrl(),user);
	}
}

package com.efrei.authenticator.controller;

import com.efrei.authenticator.dto.BasicAPIResponseDTO;
import com.efrei.authenticator.dto.LoginRequestDTO;
import com.efrei.authenticator.model.UserWebsite;
import com.efrei.authenticator.repository.WebsiteRepository;
import com.efrei.authenticator.security.JwtTokenProvider;
import com.efrei.authenticator.services.UserDetailsServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
	JwtTokenProvider tokenProvider;
	
	@Autowired
	WebsiteRepository repository;
	
	@Autowired
	UserDetailsServiceImpl service;

	@PostMapping("/token")
	@ApiOperation("Create token based on login entity")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	public ResponseEntity<?> token(@ApiParam("Login entity") @Valid @RequestBody LoginRequestDTO loginRequest) {
		return service.getToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword());
	}

	@PostMapping("/login")
	@ApiOperation("Login into the application")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class),
			@ApiResponse(code = 400, message = "Url invalid or login invalid", response = String.class) })
	public ResponseEntity<?> authenticateUser(@ApiParam("Login entity") @Valid @RequestBody LoginRequestDTO login,
			@ApiParam("Url of a website") @Valid @RequestParam("url") String url) {
		
		if(!repository.existsByUrl(url)) {
			return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Url invalid"),
					HttpStatus.BAD_REQUEST);
		}
		return service.login(login, url);
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
}
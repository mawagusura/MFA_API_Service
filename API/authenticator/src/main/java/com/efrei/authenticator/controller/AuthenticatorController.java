package com.efrei.authenticator.controller;

import com.efrei.authenticator.dto.BasicAPIResponseDTO;
import com.efrei.authenticator.dto.LoginRequestDTO;
import com.efrei.authenticator.dto.SignUpRequestDTO;
import com.efrei.authenticator.repository.UserRepository;
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
    UserRepository userRepository;
	
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
	
}
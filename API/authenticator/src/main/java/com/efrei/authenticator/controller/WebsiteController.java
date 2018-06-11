package com.efrei.authenticator.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.efrei.authenticator.dto.BasicAPIResponseDTO;
import com.efrei.authenticator.dto.CreateWebsiteDTO;
import com.efrei.authenticator.model.User;
import com.efrei.authenticator.model.UserWebsite;
import com.efrei.authenticator.model.Website;
import com.efrei.authenticator.repository.UserRepository;
import com.efrei.authenticator.repository.WebsiteRepository;
import com.efrei.authenticator.security.JwtTokenProvider;
import com.efrei.authenticator.services.WebsiteService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/websites")
public class WebsiteController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	WebsiteRepository websiteRepository;

	@Autowired
	WebsiteService service;

	@Autowired
	WebsiteRepository repository;

	@PostMapping()
	@ApiOperation("Register a website based on website entity")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Token invalid or site already registered") })
	public ResponseEntity<?> registerWebiste(@ApiParam("Website entity") @Valid @RequestBody CreateWebsiteDTO dto) {

		if (!tokenProvider.validateToken(dto.getManagerToken())) {
			return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Token not valid"),
					HttpStatus.BAD_REQUEST);
		}
		if (websiteRepository.existsByUrl(dto.getSite())) {
			return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Site is already taken!"),
					HttpStatus.BAD_REQUEST);
		}

		return service.createSite(userRepository.findById(tokenProvider.getUserIdFromJWT(dto.getManagerToken())),
				dto.getSite());
	}

	@GetMapping("users")
	@ApiOperation("Get users who are register to a specific website based on url and token")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = UserWebsite.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Token invalid or url not register or user isn't admin") })
	public ResponseEntity<?> getAllUser(@ApiParam("Token of a user") @Valid @RequestParam("token") String token,
			@ApiParam("Url of website") @Valid @RequestHeader("url") String url) {

		if (!tokenProvider.validateToken(token)) {
			return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Token not valid"),
					HttpStatus.BAD_REQUEST);
		}

		Website website = repository.findByUrl(url);
		if (website == null) {
			return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Url not register"),
					HttpStatus.BAD_REQUEST);
		}

		// Admin
		Long id = tokenProvider.getUserIdFromJWT(token);
		User user = userRepository.findById(id).get();

		if (user != website.getAdmin()) {
			return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "User not admin"),
					HttpStatus.BAD_REQUEST);
		}

		return service.getUsers(website);
	}

	@PostMapping("/action-required")
	@ApiOperation("Create a double verification based on url and token")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Token invalid or url not register or user not register") })
	public ResponseEntity<?> setActionRequired(@ApiParam("Token of a user") @Valid @RequestParam("token") String token,
			@ApiParam("Url of website") @Valid @RequestHeader("url") String url) {

		if (!tokenProvider.validateToken(token)) {
			return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Token not valid"),
					HttpStatus.BAD_REQUEST);
		}
		Website website = repository.findByUrl(url);
		if (website == null) {
			return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Url not register"),
					HttpStatus.BAD_REQUEST);
		}
		if (!existWebsite(token, url)) {
			return new ResponseEntity<BasicAPIResponseDTO>(
					new BasicAPIResponseDTO(false, "User not register for this website"), HttpStatus.BAD_REQUEST);
		}

		return service.setActionRequired(token, website);
	}
	

	private boolean existWebsite(String token, String url) {
		Long id = tokenProvider.getUserIdFromJWT(token);
		Website site = repository.findByUrl(url);
		for (UserWebsite usr : site.getUsers()) {
			if (usr.getUser().getId() == id) {
				return true;
			}
		}
		return false;
	}
}
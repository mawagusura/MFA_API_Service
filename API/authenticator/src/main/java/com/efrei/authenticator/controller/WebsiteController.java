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
import com.efrei.authenticator.repository.UserRepository;
import com.efrei.authenticator.repository.WebsiteRepository;
import com.efrei.authenticator.security.JwtTokenProvider;
import com.efrei.authenticator.services.WebsiteService;

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
	
	@PostMapping()
	public ResponseEntity<?> registerWebiste(@Valid @RequestBody CreateWebsiteDTO dto){
		if(!tokenProvider.validateToken(dto.getManagerToken())) {
            return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Token not valid"),
                    HttpStatus.BAD_REQUEST);
        }		
		if(websiteRepository.existsByUrl(dto.getSite())) {
            return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Site is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
		
		return service.createSite(userRepository.findById(
				tokenProvider.getUserIdFromJWT(dto.getManagerToken())),
				dto.getSite());
	}
	
	@GetMapping("users")
	public ResponseEntity<?> getAllUser(@Valid @RequestParam("token") String token, @Valid @RequestHeader("")String url){
		if(!tokenProvider.validateToken(token)) {
            return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Token not valid"),
                    HttpStatus.BAD_REQUEST);
        }
		return service.getUsers(token,url);	
	}
}
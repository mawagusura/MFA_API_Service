package com.efrei.authenticator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.efrei.authenticator.dto.BasicAPIResponseDTO;
import com.efrei.authenticator.dto.CreateWebsiteDTO;
import com.efrei.authenticator.repository.UserRepository;
import com.efrei.authenticator.repository.WebSiteRepository;
import com.efrei.authenticator.security.JwtTokenProvider;
import com.efrei.authenticator.services.WebsiteService;

@RestController
@RequestMapping("/api/website")
public class WebsiteController {
	
	@Autowired
    UserRepository userRepository;
	
	@Autowired
    JwtTokenProvider tokenProvider;
	
	@Autowired
	WebSiteRepository websiteRepository;

	@Autowired
	WebsiteService service;
	
	@PostMapping()
	public ResponseEntity<?> registerWebiste(CreateWebsiteDTO dto){
		if(!tokenProvider.validateToken(dto.getManagerToken())) {
            return new ResponseEntity(new BasicAPIResponseDTO(false, "Token not valid"),
                    HttpStatus.BAD_REQUEST);
        }		
		if(websiteRepository.existsByUrl(dto.getSite())) {
            return new ResponseEntity(new BasicAPIResponseDTO(false, "Site is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
		
		return service.createSite(userRepository.findById(
				tokenProvider.getUserIdFromJWT(dto.getManagerToken())),
				dto.getSite());
	}
}
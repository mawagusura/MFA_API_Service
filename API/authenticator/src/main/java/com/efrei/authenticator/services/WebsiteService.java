package com.efrei.authenticator.services;

import java.util.ArrayList;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.efrei.authenticator.dto.BasicAPIResponseDTO;
import com.efrei.authenticator.model.User;
import com.efrei.authenticator.model.UserWebsite;
import com.efrei.authenticator.model.Website;
import com.efrei.authenticator.repository.WebsiteRepository;
import com.efrei.authenticator.security.JwtTokenProvider;

@Service
public class WebsiteService {

	@Autowired
	WebsiteRepository repository;
	
	@Autowired
	JwtTokenProvider tokenProvider;
	
	public ResponseEntity<?> createSite(Optional<User> user, String site) {
		Website website=new Website();
		website.setUrl(site);
		repository.save(website);
		return ResponseEntity.ok("OK");
	}

	public ResponseEntity<?> getUsers(@Valid Website website) {
		return ResponseEntity.ok(new ArrayList<UserWebsite>().addAll(website.getUsers()));
	}

	public ResponseEntity<?> setActionRequired(@Valid String token, @Valid Website website) {
		Long id=tokenProvider.getUserIdFromJWT(token);
		for(UserWebsite usr:website.getUsers()) {
			if(usr.getUser().getId()==id) {
				usr.setWaiting(true);
				return ResponseEntity.ok("OK");
			}
		}
		return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "User not register in this website"),
				HttpStatus.BAD_REQUEST);
	}

}

package com.efrei.authenticator.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.efrei.authenticator.model.User;
import com.efrei.authenticator.model.Website;
import com.efrei.authenticator.repository.WebsiteRepository;

@Service
public class WebsiteService {

	@Autowired
	WebsiteRepository repository;
	
	public ResponseEntity<?> createSite(Optional<User> user, String site) {
		Website website=new Website();
		website.setUrl(site);
		repository.save(website);
		
		
		
		return null;
		
	}

	public ResponseEntity<?> getUsers(@Valid String token, @Valid String url) {
		Website website=repository.findByUrl(url);
		
		
		return null;
	}

}

package com.efrei.authenticator.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.efrei.authenticator.model.User;
import com.efrei.authenticator.model.Website;
import com.efrei.authenticator.repository.WebSiteRepository;

@Service
public class WebsiteService {

	@Autowired
	WebSiteRepository repository;
	
	public ResponseEntity<?> createSite(Optional<User> user, String site) {
		Website website=new Website();
		website.setUrl(site);
		repository.save(website);
		
		
		
		return null;
		
	}

}

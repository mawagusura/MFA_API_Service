package com.efrei.authenticator.controller;

import com.efrei.authenticator.dto.AuthentificationDTO;
import com.efrei.authenticator.exception.ResourceNotFoundException;
import com.efrei.authenticator.model.User;
import com.efrei.authenticator.model.Website;
import com.efrei.authenticator.repository.UserRepository;
import com.efrei.authenticator.repository.WebSiteRepository;
import com.efrei.authenticator.services.AuthentificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthenticatorController {

    @Autowired
    private UserRepository userRepository;
  
    @Autowired
    private WebSiteRepository websiteRepository;
    
    @Autowired
    private AuthentificationService service;


    /*
    @PostMapping("tokens")
    public String getToken(AuthentificationDTO dto){
    	checkNotNull(dto);
    	return service.getToken(getUser(dto));
    }

	@PostMapping
    public void createAccount(AuthentificationDTO dto, String websiteUrl) {
		checkNotNull(dto);
		
		service.createAccount(getUser(dto),getWebSite(websiteUrl));
	}

	@GetMapping("/users")
    public List<User> getAll(){
        return userRepository.findAll();
    }
    
    
    private void checkNotNull(Object dto) {
		if(dto == null) {
			throw new IllegalArgumentException();
		}	
	}


    private User getUser(AuthentificationDTO dto) {
    	List<User> users=userRepository.findAll();
    	
		for(User user: users) {
			if(user.getPassword().equals(dto.getPassword()) && user.getLogin().equals(dto.getLogin())) {
				return user;
			}
		}
		throw new ResourceNotFoundException();
    }
    
    private Website getWebSite(String websiteUrl) {
    	List<Website> websites= websiteRepository.findAll();
		return null;
	}
	*/
}

package com.efrei.authenticator.controller;

import com.efrei.authenticator.dto.BasicAPIResponseDTO;
import com.efrei.authenticator.dto.JwtAuthenticationDTO;
import com.efrei.authenticator.dto.LoginRequestDTO;
import com.efrei.authenticator.dto.SignUpRequestDTO;
import com.efrei.authenticator.model.Role;
import com.efrei.authenticator.model.RoleName;
import com.efrei.authenticator.model.User;
import com.efrei.authenticator.repository.RoleRepository;
import com.efrei.authenticator.repository.UserRepository;
import com.efrei.authenticator.repository.WebSiteRepository;
import com.efrei.authenticator.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthenticatorController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationDTO(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequestDTO signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new BasicAPIResponseDTO(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new BasicAPIResponseDTO(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User( signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new BasicAPIResponseDTO(true, "User registered successfully"));
    }

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

package com.efrei.authenticator.services;

import com.efrei.authenticator.security.BasicUser;
import com.efrei.authenticator.security.JwtTokenProvider;
import com.efrei.authenticator.dto.BasicAPIResponseDTO;
import com.efrei.authenticator.dto.JwtAuthenticationDTO;
import com.efrei.authenticator.dto.LoginRequestDTO;
import com.efrei.authenticator.model.User;
import com.efrei.authenticator.model.UserWebsite;
import com.efrei.authenticator.model.Website;
import com.efrei.authenticator.repository.UserRepository;
import com.efrei.authenticator.repository.WebsiteRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	WebsiteRepository websiteRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		// Let people login with either username or email
		User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail));

		return BasicUser.create(user);
	}

	// This method is used by JWTAuthenticationFilter
	@Transactional
	public UserDetails loadUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));

		return BasicUser.create(user);
	}

	public ResponseEntity<?> getPincode(String username) {

		Optional<User> user = userRepository.findByUsername(username);

		return ResponseEntity.ok(new BasicAPIResponseDTO(true,user.get().getPincode()));
	}

	public ResponseEntity<?> getWebsites(@Valid String token) {
		long id = tokenProvider.getUserIdFromJWT(token);
		Optional<User> user = userRepository.findById(id);

		return ResponseEntity.ok((new ArrayList<UserWebsite>()).addAll(user.get().getWebsites()));
	}

	public ResponseEntity<?> register(String username, String password, String email, String pincode) {
		// Creating user's account
		User user = new User(username, email, password,pincode);

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		User result = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
				.buildAndExpand(result.getUsername()).toUri();

		return ResponseEntity.created(location).body(new BasicAPIResponseDTO(true, "User registered successfully"));
	}

	public ResponseEntity<?> getToken(String usernameOrEmail, String password) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(usernameOrEmail, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationDTO(jwt));
	}

	public ResponseEntity<?> getWebsitesActionRequired(@Valid String token) {
		Long id=tokenProvider.getUserIdFromJWT(token);
		Optional<User> user=userRepository.findById(id);
		List<UserWebsite> listUser=new ArrayList<UserWebsite>();
		for(UserWebsite usrWeb:user.get().getWebsites()) {
			if(usrWeb.isWaiting()) {
				listUser.add(usrWeb);
			}
		}
		
		return ResponseEntity.ok(listUser);
	}

	public ResponseEntity<?> login(@Valid LoginRequestDTO login, String url) {
		Website website = websiteRepository.findByUrl(url);
		for (UserWebsite usr : website.getUsers()) {
			if (usr.getUser().getUsername().equals(login.getUsernameOrEmail())
					|| usr.getUser().getEmail().equals(login.getUsernameOrEmail())
							&& usr.getUser().getPassword().equals(login.getPassword())) {
				Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(login.getUsernameOrEmail(), login.getPassword()));

				SecurityContextHolder.getContext().setAuthentication(authentication);

				String jwt = tokenProvider.generateToken(authentication);
				return ResponseEntity.ok(new JwtAuthenticationDTO(jwt));
			}

		}
		return new ResponseEntity<BasicAPIResponseDTO>(new BasicAPIResponseDTO(false, "Login invalid"),
				HttpStatus.BAD_REQUEST);
	}

}
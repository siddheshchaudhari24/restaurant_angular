package com.application.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.dao.UserRepository;
import com.application.dtos.AuthenticationRequest;
import com.application.dtos.AuthenticationResponse;
import com.application.dtos.SignupRequest;
import com.application.dtos.UserDto;
import com.application.model.User;
import com.application.service.auth.AuthService;
import com.application.service.auth.jwt.UserDetailsServiceImpl;
import com.application.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;
	
	private final AuthenticationManager authenticationManager;
	
	private final UserDetailsServiceImpl userDetailsService;
	
	private final JwtUtil jwtUtil;
	
	private final UserRepository userRepository;
	
	public AuthController(AuthService authService, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsServiceImpl, JwtUtil jwtUtil, UserRepository userRepository) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsServiceImpl;
		this.authService = authService;
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
		UserDto createdUserDto = authService.createUser(signupRequest);
		if(createdUserDto == null) {
			return new ResponseEntity<>("User not created. Come again later", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
		}catch (BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect username or password");
		}catch (DisabledException disabledException) {
			// TODO: handle exception
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not active");
			return null;
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		final String jwt = jwtUtil.generateToken(userDetails.getUsername());
		Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		authenticationResponse.setJwt(jwt);
		authenticationResponse.setUserRole(optionalUser.get().getUserRole());
		authenticationResponse.setUserId(optionalUser.get().getId());
		return authenticationResponse;
		
	}

}

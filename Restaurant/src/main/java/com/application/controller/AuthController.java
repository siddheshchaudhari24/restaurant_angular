package com.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.application.dtos.SignupRequest;
import com.application.dtos.UserDto;
import com.application.service.auth.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
		UserDto createdUserDto = authService.createUser(signupRequest);
		if(createdUserDto == null) {
			return new ResponseEntity<>("User not created. Come again later", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
	}

}

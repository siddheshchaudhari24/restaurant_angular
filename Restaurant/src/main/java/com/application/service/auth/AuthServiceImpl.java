package com.application.service.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.application.dao.UserRepository;
import com.application.dtos.SignupRequest;
import com.application.dtos.UserDto;
import com.application.enums.UserRole;
import com.application.model.User;

@Service
public class AuthServiceImpl implements AuthService{
	
	private final UserRepository userRepository;
	
	public AuthServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDto createUser(SignupRequest signupRequest) {
		// TODO Auto-generated method stub
		User user = new User();
		user.setName(signupRequest.getName());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
		user.setUserRole(UserRole.CUSTOMER);
		User createdUser = userRepository.save(user);
		UserDto createduserDto = new UserDto();
		createduserDto.setId(createdUser.getId());
		createduserDto.setName(createdUser.getName());
		createduserDto.setEmail(createdUser.getEmail());
		createduserDto.setUserRole(createdUser.getUserRole());
		return createduserDto;
	}

}

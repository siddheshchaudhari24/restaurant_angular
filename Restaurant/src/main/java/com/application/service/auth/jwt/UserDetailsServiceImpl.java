package com.application.service.auth.jwt;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.application.dao.UserRepository;
import com.application.model.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	public UserDetailsServiceImpl(UserRepository userRepository) {
		// TODO Auto-generated constructor stub
		this.userRepository = userRepository;
	}
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> optionalUser = userRepository.findFirstByEmail(email);
		if (!optionalUser.isPresent()) throw new UsernameNotFoundException("User not found",null);
		return new org.springframework.security.core.userdetails.User(optionalUser.get().getEmail(), optionalUser.get().getPassword(),new ArrayList<>());
	}

}

package com.application.service.auth;

import com.application.dtos.SignupRequest;
import com.application.dtos.UserDto;

public interface AuthService {

	UserDto createUser(SignupRequest signupRequest);

}

package com.codePamplonaEcomerc.pamplonaecomerc.services.auth;

import com.codePamplonaEcomerc.pamplonaecomerc.dto.SignupRequest;
import com.codePamplonaEcomerc.pamplonaecomerc.dto.UserDto;

public interface AuthService {

    UserDto createUser(SignupRequest signupRequest);

    Boolean hasUserWithEmail(String email);
}

package com.ecjtaneo.room_reservation_api.auth;

import com.ecjtaneo.room_reservation_api.auth.dto.AuthRegisterDto;
import com.ecjtaneo.room_reservation_api.common.dto.MessageResponseDto;
import com.ecjtaneo.room_reservation_api.common.exception.ResourceConflictException;
import com.ecjtaneo.room_reservation_api.common.exception.ValidationException;
import com.ecjtaneo.room_reservation_api.infrastructure.security.recaptcha.RecaptchaService;
import com.ecjtaneo.room_reservation_api.user.UserService;
import com.ecjtaneo.room_reservation_api.user.dto.UserCreationCommandDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private RecaptchaService recaptchaService;

    public AuthService(
        UserService userService,
        PasswordEncoder passwordEncoder,
        RecaptchaService recaptchaService,
        AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.recaptchaService = recaptchaService;
    }

    public MessageResponseDto register(AuthRegisterDto user) {
        if(userService.isUsernameTaken(user.username())) throw new ResourceConflictException("User already exists");
        if(!recaptchaService.validate(user.recaptchaToken())) throw new ValidationException("Recaptcha validation failed");

        String hashPassword = passwordEncoder.encode(user.password());
        userService.createUser(new UserCreationCommandDto(user.username(), hashPassword));

        return new MessageResponseDto("Successfully registered.");
    }


}

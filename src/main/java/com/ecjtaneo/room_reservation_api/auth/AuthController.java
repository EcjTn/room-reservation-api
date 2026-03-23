package com.ecjtaneo.room_reservation_api.auth;


import com.ecjtaneo.room_reservation_api.auth.dto.AuthRegisterDto;
import com.ecjtaneo.room_reservation_api.common.dto.MessageResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDto register(@RequestBody @Valid AuthRegisterDto user) {
        return authService.register(user);
    }

}

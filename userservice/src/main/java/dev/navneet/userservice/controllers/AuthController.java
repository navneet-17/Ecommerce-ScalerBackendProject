package dev.navneet.userservice.controllers;

import dev.navneet.userservice.dtos.*;
import dev.navneet.userservice.models.SessionStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.navneet.userservice.services.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpRequestDto signUpRequest) {
        UserDto userDto = authService.signUp(signUpRequest.getEmail(), signUpRequest.getPassword());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequest) {
        return authService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequestDto logoutRequest) {
        return authService.logout(logoutRequest.getToken(), logoutRequest.getUserId());
    }

    // Updated as per our JWT IMplementation
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestBody String jwtToken) {
        return authService.validateToken(jwtToken);
    }

//    @PostMapping("/validate")
//    public ResponseEntity<SessionStatus> validateToken(ValidateTokenRequestDto validateTokenRequest) {
//        SessionStatus sessionStatus = authService.validate(validateTokenRequest.getToken(),
//                                                     validateTokenRequest.getUserId());
//        return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
//
//    }
}

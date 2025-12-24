package com.example.demo.service;

import com.example.demo.dto.AuthRequestDto;
import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.RegisterRequestDto;

public interface AuthService {

    /**
     * Login user and return JWT token
     * Used in test: t56_authService_loginSuccessWithFixedPassword
     */
    AuthResponseDto login(AuthRequestDto request);

    /**
     * Register user
     * Used in test: t57_authService_registerDuplicateEmailFails
     * Must return void
     */
    void register(RegisterRequestDto request);
}

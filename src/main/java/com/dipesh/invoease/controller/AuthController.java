package com.dipesh.invoease.controller;

import com.dipesh.invoease.dto.request.LoginRequest;
import com.dipesh.invoease.dto.request.SignupRequest;
import com.dipesh.invoease.dto.response.AuthResponse;
import com.dipesh.invoease.entity.User;
import com.dipesh.invoease.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        User user = authService.signup(request);
        return ResponseEntity.ok(Map.of(
                "message", "Signup successful",
                "userId", user.getId(),
                "tenantId", user.getTenant().getId()
        ));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
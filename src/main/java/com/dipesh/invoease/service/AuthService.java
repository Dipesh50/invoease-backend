package com.dipesh.invoease.service;

import com.dipesh.invoease.dto.request.LoginRequest;
import com.dipesh.invoease.dto.request.SignupRequest;
import com.dipesh.invoease.dto.response.AuthResponse;
import com.dipesh.invoease.entity.Tenant;
import com.dipesh.invoease.entity.User;
import com.dipesh.invoease.repository.TenantRepository;
import com.dipesh.invoease.repository.UserRepository;
import com.dipesh.invoease.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        Tenant tenant = new Tenant();
        tenant.setBusinessName(request.getBusinessName());
        tenant = tenantRepository.save(tenant);

        User user = new User();
        user.setTenant(tenant);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.OWNER);

        return userRepository.save(user);
    }
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getTenant().getId(), user.getRole().name());

        return new AuthResponse(token, "Bearer", user.getId(), user.getTenant().getId(), user.getRole().name());
    }
}
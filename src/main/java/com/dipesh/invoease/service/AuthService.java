package com.dipesh.invoease.service;

import com.dipesh.invoease.dto.request.SignupRequest;
import com.dipesh.invoease.entity.Tenant;
import com.dipesh.invoease.entity.User;
import com.dipesh.invoease.repository.TenantRepository;
import com.dipesh.invoease.repository.UserRepository;
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
}
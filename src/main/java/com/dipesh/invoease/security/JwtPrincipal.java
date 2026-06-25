package com.dipesh.invoease.security;

public record JwtPrincipal(Long userId, Long tenantId, String role) {
}
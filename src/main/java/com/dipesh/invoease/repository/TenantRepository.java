package com.dipesh.invoease.repository;

import com.dipesh.invoease.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
}
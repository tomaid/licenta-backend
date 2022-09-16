package com.licenta.licenta.repository;

import com.licenta.licenta.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByNume(String nume);
    Optional<Role> findById(Role userRole);
}

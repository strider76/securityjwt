package com.example.security.dao;

import com.example.security.model.ERole;
import com.example.security.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, String> {

    Optional<Roles> findByName(ERole role);

}

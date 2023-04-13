package com.tus.individual.project.repository;

import java.util.Optional;

import com.tus.individual.project.model.ERole;
import com.tus.individual.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}

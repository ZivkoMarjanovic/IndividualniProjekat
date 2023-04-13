package com.tus.individual.project.repository;

import com.tus.individual.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String name);

    Boolean existsByUsername(String name);

    Boolean existsByEmail(String email);

    @Transactional
    void deleteByUsernameAndEmail(String username, String email);
}
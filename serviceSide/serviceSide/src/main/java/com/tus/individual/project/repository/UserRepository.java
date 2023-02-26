package com.tus.individual.project.repository;

import com.tus.individual.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long>{

}
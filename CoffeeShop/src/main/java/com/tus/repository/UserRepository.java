package com.tus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tus.model.User;


public interface UserRepository extends JpaRepository<User, Long>{

}
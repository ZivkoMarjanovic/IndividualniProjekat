package com.tus.repository;
import com.tus.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product AS p WHERE p.isActive = true")
    List<Product> findActive();
}

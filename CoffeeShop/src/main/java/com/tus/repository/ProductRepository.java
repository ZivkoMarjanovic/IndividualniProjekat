package com.tus.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tus.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}

package com.tus.controlers;

import com.tus.dto.OrderCreateRequest;
import com.tus.dto.OrderDto;
import com.tus.dto.ProductQuantityRequest;
import com.tus.model.Order;
import com.tus.model.Product;
import com.tus.model.ProductQuantity;
import com.tus.repository.OrderRepository;
import com.tus.repository.ProductRepository;
import com.tus.services.CoffeeShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class CoffeeShopController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CoffeeShopService coffeeShopService;

    @GetMapping("/products/active")
    public ResponseEntity<List<Product>> getActiveProducts() {
        try {
            List<Product> products = productRepository.findActive();
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/order")
    public ResponseEntity order(@RequestBody OrderCreateRequest orderRequest) {
        try {
            coffeeShopService.createOrder(orderRequest);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

}

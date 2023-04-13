package com.tus.controlers;

import com.tus.dto.OrderCreateRequest;
import com.tus.model.Product;
import com.tus.repository.ProductRepository;
import com.tus.services.CoffeeShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CoffeeShopController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CoffeeShopService coffeeShopService;

    @PostMapping("/order")
    public ResponseEntity orderCreate(@RequestBody OrderCreateRequest orderRequest) {
        try {
            return coffeeShopService.createOrder(orderRequest);
        } catch (Exception exception) {
            System.out.println("ALEX exception: " + Arrays.toString(exception.getStackTrace()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity orderGetById(@PathVariable long orderId) {
        try {
            return coffeeShopService.getOrderById(orderId);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PutMapping("/order/{orderId}")
    public ResponseEntity orderUpdate(@PathVariable long orderId, @RequestBody OrderCreateRequest orderRequest) {
        try {
            return coffeeShopService.updateOrder(orderRequest, orderId);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity orderDelete(@PathVariable long orderId) {
        try {
            return coffeeShopService.deleteOrder(orderId);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @GetMapping("/products/active")
    public ResponseEntity<List<Product>> getActiveProducts() {
        try {
            List<Product> products = productRepository.findActive();
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

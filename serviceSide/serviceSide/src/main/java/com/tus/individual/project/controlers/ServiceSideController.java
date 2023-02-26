package com.tus.individual.project.controlers;

import com.tus.individual.project.dto.OrderCreateRequest;
import com.tus.individual.project.dto.OrderDto;
import com.tus.individual.project.dto.ProductQuantityRequest;
import com.tus.individual.project.model.Order;
import com.tus.individual.project.model.Product;
import com.tus.individual.project.model.ProductQuantity;
import com.tus.individual.project.repository.OrderRepository;
import com.tus.individual.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ServiceSideController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/products/active")
    public List<Product> getActiveProducts() {
        return productRepository.findActive();
    }

    @GetMapping("/products/all")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/orders")
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> ordersDto = new ArrayList<>();
        for (Order order:orders) {
            ordersDto.add(new OrderDto(order.getOrder_total(), order.getCustomer_name(), order.getCustomer_email(), order.isPaid(), order.getCreated(), order.getFinished(), order.getProductsWithQuantities()));
        }
        return ordersDto;
    }
}

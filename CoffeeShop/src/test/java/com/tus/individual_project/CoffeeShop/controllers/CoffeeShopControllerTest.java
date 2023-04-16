package com.tus.individual_project.CoffeeShop.controllers;

import com.tus.controlers.CoffeeShopController;
import com.tus.dto.OrderCreateRequest;
import com.tus.dto.ProductQuantityRequest;
import com.tus.model.Order;
import com.tus.model.Product;
import com.tus.repository.OrderRepository;
import com.tus.repository.ProductRepository;
import com.tus.services.CoffeeShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class CoffeeShopControllerTest {

    @Autowired
    private CoffeeShopController coffeeShopController;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    private CoffeeShopService coffeeShopService;

    private List<Product> products = new ArrayList<>();

    private List<ProductQuantityRequest> productsWithQuantities = new ArrayList<>();

    private OrderCreateRequest orderCreateRequest;

    @BeforeEach
    void setUp() {
        Product late = new Product("Late", "Coffe with lot of milk", 3.6, true);
        Product cappuccino = new Product("Cappuccino", "Regular Coffe with milk", 3.8, true);
        products.add(late);
        products.add(cappuccino);
        ProductQuantityRequest oneLate = new ProductQuantityRequest("Late", 1);
        ProductQuantityRequest oneCappuccino = new ProductQuantityRequest("Cappuccino", 1);
        productsWithQuantities.add(oneLate);
        productsWithQuantities.add(oneCappuccino);
    }

    @Test
    void getActiveProductsSuccess() {
        when(productRepository.findActive()).thenReturn(products);
        ResponseEntity<List<Product>> response = coffeeShopController.getActiveProducts();
        assertEquals(response.getBody().size(), 2);
        verify(productRepository, times(1)).findActive();
    }

    @Test
    void getActiveProductsDBError() {
        when(productRepository.findActive()).thenThrow();
        ResponseEntity<List<Product>> response = coffeeShopController.getActiveProducts();
        assertEquals(500, response.getStatusCode().value());
        verify(productRepository, times(1)).findActive();
    }
}

package com.tus.individual_project.CoffeeShop.services;

import com.tus.dto.OrderCreateRequest;
import com.tus.dto.ProductQuantityRequest;
import com.tus.model.Order;
import com.tus.model.Product;
import com.tus.model.ProductQuantity;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class CoffeeShopServiceTest {

    @Autowired
    private CoffeeShopService coffeeShopService;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    OrderRepository orderRepository;

    private List<Product> products = new ArrayList<>();

    private List<ProductQuantityRequest> productsWithQuantities = new ArrayList<>();

    private OrderCreateRequest orderCreateRequest;

    @BeforeEach
    void setUp() throws ParseException {
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
    void createOrderSuccess() {
        orderCreateRequest = new OrderCreateRequest("Alex", "alex@email.com", productsWithQuantities);
        Order order = new Order();
        order.setId(1);
        when(productRepository.findActive()).thenReturn(products);
        when(orderRepository.save(any())).thenReturn(order);
        coffeeShopService.createOrder(orderCreateRequest);
        verify(orderRepository, times(1)).save(any());
    }

    @Test
    void createOrderNegativeQuantity() {
        productsWithQuantities.add(new ProductQuantityRequest("Late", -1));
        orderCreateRequest = new OrderCreateRequest("Alex", "alex@email.com", productsWithQuantities);
        Order order = new Order();
        order.setId(1);
        when(productRepository.findActive()).thenReturn(products);
        when(orderRepository.save(any())).thenReturn(order);
        assertThrows(RuntimeException.class, () -> coffeeShopService.createOrder(orderCreateRequest),
                "Expected to throw exception when quantity is less then 0, but it didn't");
    }
}

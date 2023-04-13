package com.tus.individual_project.CoffeeShop.services;

import com.tus.dto.OrderCreateRequest;
import com.tus.dto.ProductQuantityRequest;
import com.tus.model.Order;
import com.tus.model.Product;
import com.tus.model.ProductQuantity;
import com.tus.model.StatusEnum;
import com.tus.repository.OrderRepository;
import com.tus.repository.ProductRepository;
import com.tus.services.CoffeeShopService;
import net.bytebuddy.dynamic.DynamicType;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        orderCreateRequest = new OrderCreateRequest("Alex", "alex@email.com", productsWithQuantities, false);
        Order order = new Order();
        order.setId(1);
        order.setProductsWithQuantities(new ArrayList<>());
        when(productRepository.findActive()).thenReturn(products);
        when(orderRepository.save(any())).thenReturn(order);
        ResponseEntity response =  coffeeShopService.createOrder(orderCreateRequest);
        assertEquals(201, response.getStatusCode().value(),
                "Expected to return 201 when order is successfully created.");
    }

    @Test
    void createOrderNoChosenProducts() {
        orderCreateRequest = new OrderCreateRequest("Alex", "alex@email.com", new ArrayList<>(), false);
        Order order = new Order();
        order.setId(1);
        order.setProductsWithQuantities(new ArrayList<>());
        when(productRepository.findActive()).thenReturn(products);
        when(orderRepository.save(any())).thenReturn(order);
        ResponseEntity response =  coffeeShopService.createOrder(orderCreateRequest);
        assertEquals(400, response.getStatusCode().value(),
                "Expected to return 400 when there is no chosen products.");
    }

    @Test
    void createOrderNoProducts() {
        productsWithQuantities.add(new ProductQuantityRequest("Late", -1));
        orderCreateRequest = new OrderCreateRequest("Alex", "alex@email.com", productsWithQuantities, false);
        Order order = new Order();
        order.setId(1);
        when(productRepository.findActive()).thenReturn(products);
        when(orderRepository.save(any())).thenReturn(order);
        ResponseEntity response =  coffeeShopService.createOrder(orderCreateRequest);
        assertEquals(400, response.getStatusCode().value(),
                "Expected to throw exception when quantity is less then 0, but it didn't");
    }

    @Test
    void createOrderNegativeQuantity() {
        productsWithQuantities.add(new ProductQuantityRequest("Late", -1));
        orderCreateRequest = new OrderCreateRequest("Alex", "alex@email.com", productsWithQuantities, false);
        Order order = new Order();
        order.setId(1);
        when(productRepository.findActive()).thenReturn(products);
        when(orderRepository.save(any())).thenReturn(order);
        ResponseEntity response =  coffeeShopService.createOrder(orderCreateRequest);
        assertEquals(400, response.getStatusCode().value(),
                "Expected to throw exception when quantity is less then 0, but it didn't");
    }

    @Test
    void getOrderSuccess() {
        orderCreateRequest = new OrderCreateRequest("Alex", "alex@email.com", new ArrayList<>(), false);
        Order order = new Order();
        order.setId(1);
        order.setProductsWithQuantities(new ArrayList<>());
        when(productRepository.findActive()).thenReturn(products);
        when(orderRepository.save(any())).thenReturn(order);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        ResponseEntity response =  coffeeShopService.getOrderById(1L);
        assertEquals(200, response.getStatusCode().value(),
                "Expected to return 200 when order is fetched successfully.");
    }

    @Test
    void getOrderNegativeOrderNotFound() {
        orderCreateRequest = new OrderCreateRequest("Alex", "alex@email.com", new ArrayList<>(), false);
        Order order = new Order();
        order.setId(1);
        order.setProductsWithQuantities(new ArrayList<>());
        when(productRepository.findActive()).thenReturn(products);
        when(orderRepository.save(any())).thenReturn(order);
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity response =  coffeeShopService.getOrderById(1L);
        assertEquals(404, response.getStatusCode().value(),
                "Expected to return 404 when order does not exists with provided id.");
    }

    @Test
    void updateOrderSuccess() {
        orderCreateRequest = new OrderCreateRequest("Alex", "alex@email.com", productsWithQuantities, false);
        Order order = createOrderHelp();
        when(productRepository.findActive()).thenReturn(products);
        when(orderRepository.save(any())).thenReturn(order);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        ResponseEntity response =  coffeeShopService.updateOrder(orderCreateRequest, 1L);
        assertEquals(200, response.getStatusCode().value(),
                "Expected to return 200 when order is updated successfully.");
    }

    @Test
    void updateOrderConflict() {
        orderCreateRequest = new OrderCreateRequest("Alex", "alex@email.com", productsWithQuantities, false);
        Order order = new Order();
        order.setId(1);
        order.setProductsWithQuantities(new ArrayList<>());
        when(productRepository.findActive()).thenReturn(products);
        when(orderRepository.save(any())).thenReturn(order);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        ResponseEntity response =  coffeeShopService.updateOrder(orderCreateRequest, 1L);
        assertEquals(409, response.getStatusCode().value(),
                "Expected to return 409 when order is not in status that can be updated.");
    }

    @Test
    void updateOrderNotFound() {
        orderCreateRequest = new OrderCreateRequest("Alex", "alex@email.com", productsWithQuantities, false);
        Order order = new Order();
        order.setId(1);
        order.setProductsWithQuantities(new ArrayList<>());
        when(productRepository.findActive()).thenReturn(products);
        when(orderRepository.save(any())).thenReturn(order);
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity response =  coffeeShopService.updateOrder(orderCreateRequest, 1L);
        assertEquals(404, response.getStatusCode().value(),
                "Expected to return 404 when order to be updated, not found.");
    }

    @Test
    void deleteOrderSuccess() {
        orderCreateRequest = new OrderCreateRequest("Alex", "alex@email.com", productsWithQuantities, false);
        Order order = createOrderHelp();
        when(productRepository.findActive()).thenReturn(products);
        when(orderRepository.save(any())).thenReturn(order);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        ResponseEntity response =  coffeeShopService.deleteOrder(1L);
        assertEquals(204, response.getStatusCode().value(),
                "Expected to return 204 when order is deleted successfully.");
    }

    @Test
    void deleteOrderConflict() {
        orderCreateRequest = new OrderCreateRequest("Alex", "alex@email.com", productsWithQuantities, false);
        Order order = new Order();
        order.setId(1);
        order.setProductsWithQuantities(new ArrayList<>());
        order.setStatus(StatusEnum.COLLECTED);
        when(productRepository.findActive()).thenReturn(products);
        when(orderRepository.save(any())).thenReturn(order);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        ResponseEntity response =  coffeeShopService.deleteOrder(1L);
        assertEquals(405, response.getStatusCode().value(),
                "Expected to return 405 when order is not in status that can be updated.");
    }

    @Test
    void deleteOrderNotFound() {
        orderCreateRequest = new OrderCreateRequest("Alex", "alex@email.com", productsWithQuantities, false);
        Order order = new Order();
        order.setId(1);
        order.setProductsWithQuantities(new ArrayList<>());
        when(productRepository.findActive()).thenReturn(products);
        when(orderRepository.save(any())).thenReturn(order);
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity response =  coffeeShopService.deleteOrder(1L);
        assertEquals(404, response.getStatusCode().value(),
                "Expected to return 404 when order to be deleted, not found.");
    }



    private Order createOrderHelp() {
        Order order = new Order();
        order.setId(1);
        order.setProductsWithQuantities(new ArrayList<>());
        order.setStatus(StatusEnum.PLACED);
        order.setOrder_total(10);
        order.setCustomer_name("Alex");
        order.setCustomer_email("alex@gmail.com");
        order.setPaid(false);
        return order;
    }
}

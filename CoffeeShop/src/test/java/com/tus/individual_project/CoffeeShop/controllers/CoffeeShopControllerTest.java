package com.tus.individual_project.CoffeeShop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tus.controlers.CoffeeShopController;
import com.tus.dto.OrderCreateRequest;
import com.tus.dto.ProductQuantityRequest;
import com.tus.model.Order;
import com.tus.model.Product;
import com.tus.model.StatusEnum;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class CoffeeShopControllerTest {

    @Autowired
    private CoffeeShopController coffeeShopController;

    MockMvc mockMvc;

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
        mockMvc = MockMvcBuilders.standaloneSetup(coffeeShopController).build();
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

//    @Test
//    void orderDeleteSuccess() {
//        when(coffeeShopService.deleteOrder(1).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
//        ResponseEntity<List<Product>> response = coffeeShopController.orderDelete(1);
//        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
//        verify(coffeeShopService, times(1)).deleteOrder(any());
//    }

    @Test
    void getOrderByIdSuccess() throws Exception {
        Order order = getPaidOrder();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        MvcResult result = mockMvc.perform(get("/order/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getOrderByIdInternalServerError() throws Exception {
        Order order = getPaidOrder();
        when(orderRepository.findById(1L)).thenThrow();
        MvcResult result = mockMvc.perform(get("/order/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void orderUpdateSuccess() throws Exception {
        OrderCreateRequest orderCreateRequest = getOrderCreateRequest();
        String orderCreateRequestJson = new ObjectMapper().writeValueAsString(orderCreateRequest);
        MvcResult result = mockMvc.perform(put("/order/1").content(orderCreateRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void orderUpdateInternalServerError() throws Exception {
        OrderCreateRequest orderCreateRequest = getOrderCreateRequest();
        String orderCreateRequestJson = new ObjectMapper().writeValueAsString(orderCreateRequest);
        Order order = getPaidOrder();
        when(orderRepository.findById(1L)).thenThrow();
        MvcResult result = mockMvc.perform(put("/order/1").content(orderCreateRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void orderDeleteSuccess() throws Exception {
        MvcResult result = mockMvc.perform(delete("/order/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void orderDeleteInternalServerError() throws Exception {
        when(orderRepository.findById(1L)).thenThrow();
        MvcResult result = mockMvc.perform(delete("/order/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    private List<Order> getOrdersList() {
        List<Order> orders = new ArrayList<>();
        orders.add(getPaidOrder());
        orders.add(getPlacedOrder());

        return orders;
    }

    private Order getPaidOrder() {
        Order order = new Order();
        order.setOrder_total(4);
        order.setStatus(StatusEnum.PAID);

        return order;
    }

    private Order getPlacedOrder() {
        Order order = new Order();
        order.setOrder_total(4);
        order.setStatus(StatusEnum.PLACED);

        return order;
    }

    private OrderCreateRequest getOrderCreateRequest() {
        ProductQuantityRequest productQuantityRequest = new ProductQuantityRequest("DOPPIO", 3);
        List<ProductQuantityRequest> productQuantityRequestList = new ArrayList<>();
        productQuantityRequestList.add(productQuantityRequest);
        OrderCreateRequest orderCreateRequest= new OrderCreateRequest("alex", "aleks@gmail.com", productQuantityRequestList, true);
        return orderCreateRequest;
    }
}

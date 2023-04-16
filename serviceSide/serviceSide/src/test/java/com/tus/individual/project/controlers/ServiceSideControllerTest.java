package com.tus.individual.project.controlers;

import com.tus.individual.project.model.Order;
import com.tus.individual.project.model.StatusEnum;
import com.tus.individual.project.repository.OrderRepository;
import com.tus.individual.project.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
class ServiceSideControllerTest {

    @Autowired
    ServiceSideController serviceSideController;

    MockMvc mockMvc;

    @MockBean
    OrderRepository orderRepository;
    @MockBean
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(serviceSideController).build();
    }

    @Test
    @WithMockUser(username="aleksandra",roles={"COFFEE_MAKER"})
    void getActiveProductsSuccess() throws Exception {
        when(productRepository.findActive()).thenReturn(new ArrayList<>());
        MvcResult result = mockMvc.perform(get("/products/active").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username="aleksandra",roles={"COFFEE_MAKER"})
    void getAllProductsSuccess() throws Exception {
        when(productRepository.findAll()).thenReturn(new ArrayList<>());
        MvcResult result = mockMvc.perform(get("/products/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username="aleksandra",roles={"COFFEE_MAKER"})
    void getOrdersToBePreparedSuccess() throws Exception {
        List<Order> orders = getOrdersList();
        when(orderRepository.findAllInStatusPaidAndServed()).thenReturn(orders);
        MvcResult result = mockMvc.perform(get("/orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username="aleksandra",roles={"MANAGER"})
    void getOrderByIdSuccess() throws Exception {
        Order order = getPaidOrder();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        MvcResult result = mockMvc.perform(get("/order/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username="aleksandra",roles={"COFFEE_MAKER"})
    void getOrderByIdInternalServerError() throws Exception {
        Order order = getPaidOrder();
        when(orderRepository.findById(1L)).thenThrow();
        MvcResult result = mockMvc.perform(get("/order/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @WithMockUser(username="aleksandra",roles={"MANAGER"})
    void orderUpdateSuccess() throws Exception {
        Order order = getPaidOrder();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        MvcResult result = mockMvc.perform(put("/order/1/PAID").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Mockito.verify(orderRepository, Mockito.times(1)).flush();
    }

    @Test
    @WithMockUser(username="aleksandra",roles={"MANAGER"})
    void orderUpdateInternalServerError() throws Exception {
        Order order = getPaidOrder();
        when(orderRepository.findById(1L)).thenThrow();
        MvcResult result = mockMvc.perform(put("/order/1/PAID").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @WithMockUser(username="aleksandra",roles={"MANAGER"})
    void getWeeklyStatsSuccess() throws Exception {
        Order order = getPaidOrder();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        MvcResult result = mockMvc.perform(get("/orders/week").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test()
    @WithMockUser(username="aleksandra",roles={"COFFEE_MAKER"})
    void getWeeklyStatsUnauthorized() throws Exception {
        Assertions.assertThrows(AccessDeniedException.class, () -> serviceSideController.getWeeklyStats());
    }

    @Test
    @WithMockUser(username="aleksandra",roles={"MANAGER"})
    void getWeeklyStatsInternalServerError() throws Exception {
        Order order = getPaidOrder();
        when(orderRepository.getWeeklyReport(any())).thenThrow();
        MvcResult result = mockMvc.perform(get("/orders/week").contentType(MediaType.APPLICATION_JSON))
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
}

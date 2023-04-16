package com.tus.individual.project.controlers;

import org.springframework.security.access.prepost.PreAuthorize;
import com.tus.individual.project.dto.OrderDto;
import com.tus.individual.project.dto.OrderSaleDto;
import com.tus.individual.project.model.Order;
import com.tus.individual.project.model.Product;
import com.tus.individual.project.model.StatusEnum;
import com.tus.individual.project.repository.OrderRepository;
import com.tus.individual.project.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import javax.persistence.Tuple;

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
    @PreAuthorize("hasAnyRole('COFFEE_MAKER', 'MANAGER')")
    public List<OrderDto> getOrdersToBePrepared() {
        List<Order> orders = orderRepository.findAllInStatusPaidAndServed();
        List<OrderDto> ordersDto = new ArrayList<>();
        for (Order order:orders) {
            ordersDto.add(new OrderDto(order.getId(), order.getOrder_total(), order.getCustomer_name(), order.getCustomer_email(), order.isPaid(), order.getCreated(), order.getFinished(), order.getStatus().name(), order.getProductsWithQuantities()));
        }
        return ordersDto;
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<?> getUser(@PathVariable long id) {
        try {
            Optional<Order> myOrder = orderRepository.findById(id);
            if (myOrder.isEmpty()) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }
            Order order = myOrder.get();
            OrderDto orderToBeReturned = new OrderDto(order.getId(), order.getOrder_total(), order.getCustomer_name(), order.getCustomer_email(), order.isPaid(), order.getCreated(), order.getFinished(), order.getStatus().name(), order.getProductsWithQuantities());
            return new ResponseEntity<>(orderToBeReturned, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println("Something wrong: " + exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }

    }

    @PutMapping("/order/{orderId}/{status}")
    public ResponseEntity orderUpdate(@PathVariable long orderId, @PathVariable String status) {
        try {
            Optional<Order> myOrder = orderRepository.findById(orderId);
            if (myOrder.isEmpty()) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }
            Order order = myOrder.get();
            order.setStatus(StatusEnum.valueOf(status));
            orderRepository.flush();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

	@GetMapping("/orders/week")
	@PreAuthorize("hasAnyRole('MANAGER')")
	public List<OrderSaleDto> getWeeklyStats() {
		List <OrderSaleDto> orderSaleDtos = new ArrayList<>();
		try {
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.add(Calendar.DATE, -7);
			
			Date currentDate = today.getTime();
			List <Tuple> result = orderRepository.getWeeklyReport(currentDate);
			result.forEach(row ->{
				String total = row.get("total").toString();
				String number  = row.get("number").toString();
				String day  = row.get("day").toString();
				OrderSaleDto newOrderSaleDto = new OrderSaleDto(total, number, day);
				orderSaleDtos.add(newOrderSaleDto);
			});
			return orderSaleDtos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}

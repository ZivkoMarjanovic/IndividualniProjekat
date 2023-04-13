package com.tus.services;

import com.tus.dto.OrderCreateRequest;
import com.tus.dto.OrderDto;
import com.tus.dto.ProductQuantityRequest;
import com.tus.model.Order;
import com.tus.model.Product;
import com.tus.model.ProductQuantity;
import com.tus.model.StatusEnum;
import com.tus.repository.OrderRepository;
import com.tus.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CoffeeShopService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    public ResponseEntity createOrder (OrderCreateRequest orderRequest) {
        if (orderRequest.getProductsWithQuantities().size() < 1) {
            return new ResponseEntity<>("Please choose products that you want to order.", HttpStatus.BAD_REQUEST);
        }

        if (orderRequest.getCustomerName() == null || orderRequest.getCustomerName().isEmpty() ||
                orderRequest.getCustomerEmail().isEmpty() || orderRequest.getCustomerEmail() == null) {
            return new ResponseEntity<>("Fields CustomerName and Email have to be populated.", HttpStatus.BAD_REQUEST);
        }

        List<Product> activeProducts = productRepository.findActive();
        Map<String, Product> productsMap = new HashMap<>();
        activeProducts.forEach(product -> productsMap.put(product.getProduct_name(), product));
        double total = 0.0;
        List<ProductQuantityRequest> productQuantityRequests = orderRequest.getProductsWithQuantities();
        for (ProductQuantityRequest productQuantity : productQuantityRequests) {
            String productName = productQuantity.getProductName();
            Product currentProduct = productsMap.get(productName);
            if (productQuantity.getQuantity() < 0) {
                System.out.println("Quantity can not have negative value.");
                return new ResponseEntity<>("Quantity can not have negative value.", HttpStatus.BAD_REQUEST);
            }
            if (productQuantity.getQuantity() > 0) {
                total = total + productQuantity.getQuantity() * currentProduct.getPrice();
            }
        }
        System.out.println("Alex order total: " + total);
        if (total < 1) {
            return new ResponseEntity<>("Please choose products that you want to order.", HttpStatus.BAD_REQUEST);
        }

        List<ProductQuantity> productQuantities = new ArrayList<>();
        for (ProductQuantityRequest productQuantityRequest : productQuantityRequests) {
            ProductQuantity currentProductQuantity = new ProductQuantity();
            String productName = productQuantityRequest.getProductName();
            Product currentProduct = productsMap.get(productName);
            currentProductQuantity.setProduct(currentProduct);
            currentProductQuantity.setQuantity(productQuantityRequest.getQuantity());
            productQuantities.add(currentProductQuantity);
        }

        Order newOrder = new Order();
        newOrder.setOrder_total(total);
        newOrder.setCustomer_name(orderRequest.getCustomerName());
        newOrder.setCustomer_email(orderRequest.getCustomerEmail());
        newOrder.setPaid(false);
        newOrder.setStatus(StatusEnum.PLACED);
        newOrder.setCreated(new Date());
        newOrder.setProductsWithQuantities(productQuantities);

        newOrder.getProductsWithQuantities().forEach(productQuantity -> {
            productQuantity.setOrder(newOrder);
        });

        Order order = orderRepository.save(newOrder);
        System.out.println("New order prepared with id: " + order.getId());

        OrderDto orderDto = createOrderDto(order);

        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    public ResponseEntity getOrderById(long orderId) {

        Optional<Order> optionalOrder =  orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
        Order order = optionalOrder.get();

        OrderDto orderDto = createOrderDto(order);

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    public ResponseEntity updateOrder(OrderCreateRequest orderRequest, long orderId) {
        if (orderRequest.getProductsWithQuantities().size() < 1) {
            return new ResponseEntity<>("Please choose products that you want to order.", HttpStatus.BAD_REQUEST);
        }

        if (orderRequest.getCustomerName() == null || orderRequest.getCustomerName().isEmpty() ||
                orderRequest.getCustomerEmail().isEmpty() || orderRequest.getCustomerEmail() == null) {
            return new ResponseEntity<>("Fields CustomerName and Email have to be populated.", HttpStatus.BAD_REQUEST);
        }

        Optional<Order> optionalOrder =  orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return new ResponseEntity<>("Order to be updated, not found", HttpStatus.NOT_FOUND);
        }
        Order updateOrder = optionalOrder.get();

        if (!(updateOrder.getStatus() == StatusEnum.PLACED || updateOrder.getStatus() == StatusEnum.UPDATED)) {
            return new ResponseEntity<>("Order is already paid and can not be updated.", HttpStatus.CONFLICT);
        }

        List<Product> activeProducts = productRepository.findActive();
        Map<String, Product> productsMap = new HashMap<>();
        activeProducts.forEach(product -> productsMap.put(product.getProduct_name(), product));
        double total = 0.0;
        List<ProductQuantityRequest> productQuantityRequests = orderRequest.getProductsWithQuantities();
        for (ProductQuantityRequest productQuantity : productQuantityRequests) {
            String productName = productQuantity.getProductName();
            Product currentProduct = productsMap.get(productName);
            if (productQuantity.getQuantity() < 0) {
                System.out.println("Quantity can not have negative value.");
                return new ResponseEntity<>("Quantity can not have negative value.", HttpStatus.BAD_REQUEST);
            }
            if (productQuantity.getQuantity() > 0) {
                total = total + productQuantity.getQuantity() * currentProduct.getPrice();
            }
        }
        System.out.println("Alex order total: " + total);
        if (total < 1) {
            return new ResponseEntity<>("Please choose products that you want to order.", HttpStatus.BAD_REQUEST);
        }

        List<ProductQuantity> productQuantities = new ArrayList<>();
        for (ProductQuantityRequest productQuantityRequest : productQuantityRequests) {
            ProductQuantity currentProductQuantity = new ProductQuantity();
            String productName = productQuantityRequest.getProductName();
            Product currentProduct = productsMap.get(productName);
            currentProductQuantity.setProduct(currentProduct);
            currentProductQuantity.setQuantity(productQuantityRequest.getQuantity());
            productQuantities.add(currentProductQuantity);
        }

        Order newUpdateOrder = new Order();
        newUpdateOrder.setId(orderId);
        newUpdateOrder.setOrder_total(total);
        newUpdateOrder.setCustomer_name(orderRequest.getCustomerName());
        newUpdateOrder.setCustomer_email(orderRequest.getCustomerEmail());
        if (Optional.ofNullable(orderRequest.isPaid()).orElse(false)) {
            newUpdateOrder.setStatus(StatusEnum.PAID);
            newUpdateOrder.setPaid(true);
        } else {
            newUpdateOrder.setStatus(StatusEnum.UPDATED);
            newUpdateOrder.setPaid(false);
        }
        newUpdateOrder.setCreated(new Date());
        newUpdateOrder.setProductsWithQuantities(productQuantities);

        newUpdateOrder.getProductsWithQuantities().forEach(productQuantity -> {
            productQuantity.setOrder(updateOrder);
        });

        newUpdateOrder.getProductsWithQuantities().forEach(productQuantity -> {
            System.out.println("Orderid: " + productQuantity.getOrder().getId());
            System.out.println("ProductName: " + productQuantity.getProduct().getProduct_name());
            System.out.println("Quantity: " + productQuantity.getQuantity());
            System.out.println("-------------------------------------------------------------------------");
        });

        Order order = orderRepository.save(newUpdateOrder);
        System.out.println("Updated order with id: " + order.getId());

        OrderDto orderDto = createOrderDto(order);

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    public ResponseEntity deleteOrder(long orderId) {
        Optional<Order> optionalOrder =  orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return new ResponseEntity<>("Order to be updated, not found", HttpStatus.NOT_FOUND);
        }
        Order orderToDelete = optionalOrder.get();

        if (orderToDelete.getStatus() == StatusEnum.SERVED || orderToDelete.getStatus() == StatusEnum.COLLECTED) {
            return new ResponseEntity<>("Order is in the status that can not be deleted.", HttpStatus.METHOD_NOT_ALLOWED);
        }

        orderRepository.deleteById(orderId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private OrderDto createOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        List<ProductQuantityRequest> productQuantityDto = new ArrayList<>();
        order.getProductsWithQuantities().forEach(productQuantity -> {
            ProductQuantityRequest productQuantityRequest = new ProductQuantityRequest(productQuantity.getProduct().getProduct_name(), productQuantity.getQuantity());
            productQuantityDto.add(productQuantityRequest);
        });
        orderDto.setProductQuantityRequests(productQuantityDto);

        orderDto.setOrderId(order.getId());
        orderDto.setOrderTotal(order.getOrder_total());
        orderDto.setCustomerEmail(order.getCustomer_email());
        orderDto.setCustomerName(order.getCustomer_name());
        orderDto.setPaid(order.isPaid());

        return orderDto;
    }
}

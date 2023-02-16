package com.tus.controlers;

import com.tus.dto.OrderCreateRequest;
import com.tus.dto.OrderDto;
import com.tus.dto.ProductQuantityRequest;
import com.tus.model.Order;
import com.tus.model.Product;
import com.tus.model.ProductQuantity;
import com.tus.repository.OrderRepository;
import com.tus.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/order")
    public void order(@RequestBody OrderCreateRequest orderRequest) {
        List<Product> activeProducts = productRepository.findActive();
        Map<String, Product> productsMap = new HashMap<>();
        activeProducts.forEach(product -> productsMap.put(product.getProduct_name(), product));
        double total = 0.0;
        List<ProductQuantityRequest> productQuantityRequests = orderRequest.getProductsWithQuantities();
        for (ProductQuantityRequest productQuantity : productQuantityRequests) {
            String productName = productQuantity.getProductName();
            Product currentProduct = productsMap.get(productName);
            total = total + productQuantity.getQuantity() * currentProduct.getPrice();
        }
        System.out.println("Alex order total: " + total);

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
        newOrder.setCustomer_name(orderRequest.getCustomer_name());
        newOrder.setCustomer_email(orderRequest.getCustomer_email());
        newOrder.setPaid(true);
        newOrder.setCreated(new Date());
        newOrder.setProductsWithQuantities(productQuantities);
        System.out.println("New order prepared with id: " + newOrder.getId());

        newOrder.getProductsWithQuantities().forEach(productQuantity -> {
            productQuantity.setOrder(newOrder);
        });

        orderRepository.save(newOrder);
    }

}

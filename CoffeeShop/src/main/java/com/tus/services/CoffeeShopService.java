package com.tus.services;

import com.tus.dto.OrderCreateRequest;
import com.tus.dto.ProductQuantityRequest;
import com.tus.model.Order;
import com.tus.model.Product;
import com.tus.model.ProductQuantity;
import com.tus.repository.OrderRepository;
import com.tus.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CoffeeShopService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    public void createOrder (OrderCreateRequest orderRequest) {
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
                throw new RuntimeException("Quantity can not have negative value.");
            }
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
        newOrder.setCustomer_name(orderRequest.getCustomerName());
        newOrder.setCustomer_email(orderRequest.getCustomerEmail());
        newOrder.setPaid(true);
        newOrder.setCreated(new Date());
        newOrder.setProductsWithQuantities(productQuantities);

        newOrder.getProductsWithQuantities().forEach(productQuantity -> {
            productQuantity.setOrder(newOrder);
        });

        Order order = orderRepository.save(newOrder);
        System.out.println("New order prepared with id: " + order.getId());
    }

}

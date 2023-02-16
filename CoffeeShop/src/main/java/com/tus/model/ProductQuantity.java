package com.tus.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="product_quantity")
public class ProductQuantity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name="quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public ProductQuantity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductQuantity)) return false;
        ProductQuantity that = (ProductQuantity) o;
        return getQuantity() == that.getQuantity() && getProduct().equals(that.getProduct()) && getOrder().equals(that.getOrder());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProduct(), getQuantity(), getOrder());
    }
}

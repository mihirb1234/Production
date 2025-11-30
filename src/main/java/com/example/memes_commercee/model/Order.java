package com.example.memes_commercee.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "txn_amount", precision = 10, scale = 2)
    public BigDecimal txnAmount;

    @Column(name = "proxy_price", precision = 10, scale = 2)
    public BigDecimal proxyPrice;

    @Column(name = "price", precision = 10, scale = 2)
    public BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public OrderStatus status;

    @Column(name = "payment_done")
    public Boolean paymentDone;

    @Column(name = "order_refund_status", length = 50)
    public String orderRefundStatus;

    @Column(name = "order_cancellable")
    public Boolean orderCancellable;

    @Column(name = "payment_type", length = 50)
    public String paymentType;

    @Column(name = "order_reference", unique = true, length = 100)
    public String orderReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    // Default constructor required by JPA
    public Order() {}

    // Constructor for creating new orders
    public Order(User user, BigDecimal price, OrderStatus status, String paymentType) {
        this.user = user;
        this.price = price;
        this.status = status;
        this.paymentType = paymentType;
        this.paymentDone = false;
        this.orderCancellable = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (paymentDone == null) {
            paymentDone = false;
        }
        if (orderCancellable == null) {
            orderCancellable = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

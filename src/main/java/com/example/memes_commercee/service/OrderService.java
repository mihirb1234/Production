package com.example.memes_commercee.service;

import com.example.memes_commercee.model.Order;
import com.example.memes_commercee.model.OrderStatus;
import com.example.memes_commercee.model.User;
import com.example.memes_commercee.repository.OrderRepository;
import com.example.memes_commercee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return orderRepository.findByUserOrderByCreatedAtDesc(userOpt.get());
        }
        throw new RuntimeException("User not found");
    }

    public List<Order> getOrdersByUserAndStatus(Long userId, OrderStatus status) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return orderRepository.findByUserAndStatus(userOpt.get(), status);
        }
        throw new RuntimeException("User not found");
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getPaidOrders() {
        return orderRepository.findByPaymentDone(true);
    }

    public List<Order> getUnpaidOrders() {
        return orderRepository.findByPaymentDone(false);
    }

    public List<Order> getCancellableOrders() {
        return orderRepository.findByOrderCancellable(true);
    }

    public Order createOrder(Long userId, Order order) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            order.user = userOpt.get();
            return orderRepository.save(order);
        }
        throw new RuntimeException("User not found");
    }

    public Order updateOrder(Long id, Order updatedOrder) {
        Optional<Order> existingOrderOpt = orderRepository.findById(id);
        if (existingOrderOpt.isPresent()) {
            Order existingOrder = existingOrderOpt.get();

            existingOrder.txnAmount = updatedOrder.txnAmount;
            existingOrder.proxyPrice = updatedOrder.proxyPrice;
            existingOrder.price = updatedOrder.price;
            existingOrder.status = updatedOrder.status;
            existingOrder.paymentDone = updatedOrder.paymentDone;
            existingOrder.orderRefundStatus = updatedOrder.orderRefundStatus;
            existingOrder.orderCancellable = updatedOrder.orderCancellable;
            existingOrder.paymentType = updatedOrder.paymentType;
            existingOrder.orderReference = updatedOrder.orderReference;

            return orderRepository.save(existingOrder);
        }
        throw new RuntimeException("Order not found");
    }

    public void deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    // Business logic methods
    public Order markPaymentDone(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.paymentDone = true;
            // When payment is done, order might not be cancellable anymore
            order.orderCancellable = false;
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }

    public Order cancelOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (!order.orderCancellable) {
                throw new RuntimeException("Order cannot be cancelled");
            }
            order.status = OrderStatus.CANCELLED;
            order.orderCancellable = false;
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }

    public Long getUserOrderCount(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return orderRepository.countByUser(userOpt.get());
        }
        throw new RuntimeException("User not found");
    }

    public Double getUserTotalSpent(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            Double total = orderRepository.getTotalSpentByUser(userOpt.get());
            return total != null ? total : 0.0;
        }
        throw new RuntimeException("User not found");
    }
}

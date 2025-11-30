package com.example.memes_commercee.controller;

import com.example.memes_commercee.model.Order;
import com.example.memes_commercee.model.OrderStatus;
import com.example.memes_commercee.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Get all orders for a specific user
     * GET /api/orders/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserOrders(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.getOrdersByUser(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User orders retrieved successfully");
            response.put("data", orders);
            response.put("count", orders.size());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while retrieving orders");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get orders for a specific user with a specific status
     * GET /api/orders/user/{userId}/status/{status}
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<?> getUserOrdersByStatus(@PathVariable Long userId, @PathVariable OrderStatus status) {
        try {
            List<Order> orders = orderService.getOrdersByUserAndStatus(userId, status);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User orders retrieved successfully");
            response.put("data", orders);
            response.put("count", orders.size());
            response.put("status", status);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while retrieving orders");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get order count for a specific user
     * GET /api/orders/user/{userId}/count
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<?> getUserOrderCount(@PathVariable Long userId) {
        try {
            Long count = orderService.getUserOrderCount(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User order count retrieved successfully");
            response.put("userId", userId);
            response.put("orderCount", count);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while retrieving order count");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get total amount spent by a user
     * GET /api/orders/user/{userId}/total-spent
     */
    @GetMapping("/user/{userId}/total-spent")
    public ResponseEntity<?> getUserTotalSpent(@PathVariable Long userId) {
        try {
            Double totalSpent = orderService.getUserTotalSpent(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User total spent retrieved successfully");
            response.put("userId", userId);
            response.put("totalSpent", totalSpent);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while retrieving total spent");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get a specific order by ID
     * GET /api/orders/{orderId}
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        try {
            Optional<Order> orderOpt = orderService.getOrderById(orderId);

            if (orderOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Order retrieved successfully");
                response.put("data", orderOpt.get());

                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Order not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while retrieving order");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Create a new order for a user
     * POST /api/orders/user/{userId}
     */
    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createOrder(@PathVariable Long userId, @RequestBody Order order) {
        try {
            Order createdOrder = orderService.createOrder(userId, order);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order created successfully");
            response.put("data", createdOrder);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while creating order");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Update an existing order
     * PUT /api/orders/{orderId}
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody Order order) {
        try {
            Order updatedOrder = orderService.updateOrder(orderId, order);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order updated successfully");
            response.put("data", updatedOrder);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while updating order");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Mark payment as done for an order
     * PUT /api/orders/{orderId}/payment-done
     */
    @PutMapping("/{orderId}/payment-done")
    public ResponseEntity<?> markPaymentDone(@PathVariable Long orderId) {
        try {
            Order updatedOrder = orderService.markPaymentDone(orderId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Payment marked as done successfully");
            response.put("data", updatedOrder);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while marking payment as done");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Cancel an order
     * PUT /api/orders/{orderId}/cancel
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try {
            Order cancelledOrder = orderService.cancelOrder(orderId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order cancelled successfully");
            response.put("data", cancelledOrder);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while cancelling order");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Delete an order
     * DELETE /api/orders/{orderId}
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order deleted successfully");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while deleting order");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}

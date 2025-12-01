package com.example.memes_commercee.controller;

import com.example.memes_commercee.model.Order;
import com.example.memes_commercee.model.OrderStatus;
import com.example.memes_commercee.repository.OrderRepository;
import com.example.memes_commercee.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Get all orders (paginated)
     * GET /api/orders
     */
    @GetMapping
    public ResponseEntity<?> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String status) {
        try {
            List<Order> orders;

            if (status != null && !status.isEmpty()) {
                try {
                    OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
                    orders = orderService.getOrdersByStatus(orderStatus);
                } catch (IllegalArgumentException e) {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "Invalid status. Valid values: PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED");
                    return ResponseEntity.badRequest().body(errorResponse);
                }
            } else {
                orders = orderService.getAllOrders();
            }

            // Simple pagination (in real app, you'd use Spring Data's Pageable)
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, orders.size());
            List<Order> paginatedOrders = orders.subList(startIndex, endIndex);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "All orders retrieved successfully");
            response.put("data", paginatedOrders);
            response.put("count", paginatedOrders.size());
            response.put("totalCount", orders.size());
            response.put("page", page);
            response.put("size", size);
            response.put("totalPages", (int) Math.ceil((double) orders.size() / size));

            if (status != null && !status.isEmpty()) {
                response.put("statusFilter", status);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while retrieving orders");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

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
     * Simple test endpoint - Get all orders (no pagination)
     * GET /api/orders/all
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllOrdersSimple() {
        try {
            System.out.println("🔍 getAllOrdersSimple called");
            List<Order> orders = orderRepository.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "All orders retrieved successfully");
            response.put("data", orders);
            response.put("count", orders.size());

            System.out.println("✅ Returning " + orders.size() + " orders");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error in getAllOrdersSimple: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get all orders (paginated) - NEW API ENDPOINT
     * GET /api/orders/list?page=0&size=20&sort=createdAt&direction=desc
     */
    @GetMapping("/list")
    public ResponseEntity<?> getAllOrdersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        System.out.println("🔍 getAllOrdersPaginated called with: page=" + page + ", size=" + size + ", sort=" + sort + ", direction=" + direction);
        try {
            // For simplicity, we'll return all orders without pagination for now
            // In a production app, you'd implement proper pagination
            List<Order> orders = orderRepository.findAll();

            // Apply basic sorting (you could enhance this with proper sorting logic)
            if ("createdAt".equals(sort) && "desc".equals(direction)) {
                orders.sort((o1, o2) -> o2.createdAt.compareTo(o1.createdAt));
            } else if ("createdAt".equals(sort) && "asc".equals(direction)) {
                orders.sort((o1, o2) -> o1.createdAt.compareTo(o2.createdAt));
            } else if ("price".equals(sort) && "desc".equals(direction)) {
                orders.sort((o1, o2) -> o2.price.compareTo(o1.price));
            } else if ("price".equals(sort) && "asc".equals(direction)) {
                orders.sort((o1, o2) -> o1.price.compareTo(o2.price));
            }

            // Calculate pagination manually (in production, use Spring Data pagination)
            int totalElements = orders.size();
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalElements);

            List<Order> paginatedOrders = orders.subList(startIndex, endIndex);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "All orders retrieved successfully");
            response.put("data", paginatedOrders);
            response.put("totalElements", totalElements);
            response.put("totalPages", (int) Math.ceil((double) totalElements / size));
            response.put("currentPage", page);
            response.put("size", size);
            response.put("numberOfElements", paginatedOrders.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while retrieving orders");
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
     * Get orders by status
     * GET /api/orders/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable OrderStatus status) {
        try {
            List<Order> orders = orderRepository.findByStatus(status);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Orders retrieved successfully");
            response.put("data", orders);
            response.put("count", orders.size());
            response.put("status", status);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while retrieving orders");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Search orders by price range
     * GET /api/orders/search?minPrice=50&maxPrice=200
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchOrdersByPrice(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        try {
            List<Order> allOrders = orderRepository.findAll();
            List<Order> filteredOrders = new ArrayList<>();

            for (Order order : allOrders) {
                boolean matchesMin = minPrice == null || order.price.compareTo(minPrice) >= 0;
                boolean matchesMax = maxPrice == null || order.price.compareTo(maxPrice) <= 0;

                if (matchesMin && matchesMax) {
                    filteredOrders.add(order);
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Orders searched successfully");
            response.put("data", filteredOrders);
            response.put("count", filteredOrders.size());
            response.put("filters", Map.of(
                "minPrice", minPrice,
                "maxPrice", maxPrice
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while searching orders");
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

package com.example.memes_commercee.repository;

import com.example.memes_commercee.model.Order;
import com.example.memes_commercee.model.OrderStatus;
import com.example.memes_commercee.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    List<Order> findByUserAndStatus(User user, OrderStatus status);

    List<Order> findByUserOrderByCreatedAtDesc(User user);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByPaymentDone(Boolean paymentDone);

    List<Order> findByOrderCancellable(Boolean orderCancellable);

    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByUserAndDateRange(@Param("user") User user,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.user = :user ORDER BY o.createdAt DESC")
    List<Order> findUserOrdersOrderedByDateDesc(@Param("user") User user);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.user = :user")
    Long countByUser(@Param("user") User user);

    @Query("SELECT SUM(o.price) FROM Order o WHERE o.user = :user AND o.paymentDone = true")
    Double getTotalSpentByUser(@Param("user") User user);
}

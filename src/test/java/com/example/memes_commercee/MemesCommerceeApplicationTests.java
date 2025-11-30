package com.example.memes_commercee;

import com.example.memes_commercee.model.Order;
import com.example.memes_commercee.model.OrderStatus;
import com.example.memes_commercee.model.User;
import com.example.memes_commercee.repository.OrderRepository;
import com.example.memes_commercee.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MemesCommerceeApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Test
	void contextLoads() {
		// Test that the application context loads successfully
		assertThat(userRepository).isNotNull();
		assertThat(orderRepository).isNotNull();
	}

	@Test
	void testUserEntity() {
		// Create a test user
		User user = new User();
		user.fullName = "Test User";
		user.email = "test@example.com";
		user.phone = "+1234567890";

		// Save the user
		User savedUser = userRepository.save(user);

		// Verify the user was saved
		assertThat(savedUser.id).isNotNull();
		assertThat(savedUser.fullName).isEqualTo("Test User");
		assertThat(savedUser.email).isEqualTo("test@example.com");
		assertThat(savedUser.phone).isEqualTo("+1234567890");
		assertThat(savedUser.createdAt).isNotNull();
	}

	@Test
	void testOrderEntity() {
		// First create a user
		User user = new User();
		user.fullName = "Order Test User";
		user.email = "order.test@example.com";
		user.phone = "+1234567890";
		User savedUser = userRepository.save(user);

		// Create an order for the user
		Order order = new Order();
		order.user = savedUser;
		order.price = new BigDecimal("100.50");
		order.status = OrderStatus.PENDING;
		order.paymentType = "CREDIT_CARD";
		order.orderReference = "TEST-ORDER-001";

		// Save the order
		Order savedOrder = orderRepository.save(order);

		// Verify the order was saved
		assertThat(savedOrder.id).isNotNull();
		assertThat(savedOrder.user.id).isEqualTo(savedUser.id);
		assertThat(savedOrder.price).isEqualByComparingTo(new BigDecimal("100.50"));
		assertThat(savedOrder.status).isEqualTo(OrderStatus.PENDING);
		assertThat(savedOrder.paymentType).isEqualTo("CREDIT_CARD");
		assertThat(savedOrder.createdAt).isNotNull();
	}

	@Test
	void testUserOrderRelationship() {
		// Create a user
		User user = new User();
		user.fullName = "Relationship Test User";
		user.email = "relationship.test@example.com";
		user.phone = "+1234567890";
		User savedUser = userRepository.save(user);

		// Create multiple orders for the user
		Order order1 = new Order();
		order1.user = savedUser;
		order1.price = new BigDecimal("50.00");
		order1.status = OrderStatus.PENDING;
		order1.paymentType = "CREDIT_CARD";
		order1.orderReference = "TEST-ORDER-002";

		Order order2 = new Order();
		order2.user = savedUser;
		order2.price = new BigDecimal("75.25");
		order2.status = OrderStatus.CONFIRMED;
		order2.paymentType = "PAYPAL";
		order2.orderReference = "TEST-ORDER-003";

		orderRepository.save(order1);
		orderRepository.save(order2);

		// Test the relationship by querying orders by user
		var userOrders = orderRepository.findByUser(savedUser);
		assertThat(userOrders).hasSize(2);

		// Test repository methods
		var pendingOrders = orderRepository.findByUserAndStatus(savedUser, OrderStatus.PENDING);
		assertThat(pendingOrders).hasSize(1);

		var confirmedOrders = orderRepository.findByUserAndStatus(savedUser, OrderStatus.CONFIRMED);
		assertThat(confirmedOrders).hasSize(1);
	}
}

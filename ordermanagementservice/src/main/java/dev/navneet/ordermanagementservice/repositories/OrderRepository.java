package dev.navneet.ordermanagementservice.repositories;

import dev.navneet.ordermanagementservice.models.Order;
import dev.navneet.ordermanagementservice.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    // Custom method to find orders by status
    List<Order> findByStatus(OrderStatus status);
    Optional<List<Order>>  findByStatusAndUserId(OrderStatus orderStatus, Long userId);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :orderId")
    Optional<Order> findByIdWithOrderItems(@Param("orderId") Long orderId);



}


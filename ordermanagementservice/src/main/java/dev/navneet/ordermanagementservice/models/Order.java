package dev.navneet.ordermanagementservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Double totalAmount;
    private String deliveryAddress;
    private String paymentMethod;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime orderDate = LocalDateTime.now();
    private LocalDateTime expectedDeliveryDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @Override
    public String toString() {
        return "Order Details {" +
                "id = "+ id +
                "totalAmount = "+ totalAmount +
                "deliveryAddress = "+ deliveryAddress +
                "status = "+ status +
                "orderDate = "+ orderDate +
                "expectedDeliveryDate = "+ expectedDeliveryDate +
                '}';
    }
}

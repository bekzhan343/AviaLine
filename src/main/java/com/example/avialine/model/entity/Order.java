package com.example.avialine.model.entity;

import com.example.avialine.enums.Currency;
import com.example.avialine.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "regnum", unique = true, nullable = false, length = 6)
    private String regnum;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, length = 3)
    private Currency currency;

    @Column(name = "passengers_count", nullable = false)
    private Integer passengerCount;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;



}

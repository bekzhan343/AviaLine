package com.example.avialine.model.entity;

import com.example.avialine.enums.Currency;
import com.example.avialine.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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

    @Column(name = "base_fare", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseFare;

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

    @Builder.Default
    @Column(name = "tax_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxPercentage = new BigDecimal(20);

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "taxes", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxes;

    @OneToMany(mappedBy = "order")
    private List<Payment> payments;




}

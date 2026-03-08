package com.example.avialine.model.entity;

import com.example.avialine.enums.RefundStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "refund")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Какой заказ возвращаем
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Какой пассажир возвращает (из passenger_ids)
    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    // Сколько вернули
    @Column(name = "refund_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount;

    // Сколько штраф
    @Column(name = "penalty_amount", precision = 10, scale = 2)
    private BigDecimal penaltyAmount;

    // voluntary / involuntary
    @Column(name = "operation_mode", length = 20)
    private String operationMode;

    // full_refund — полный или частичный
    @Column(name = "full_refund")
    private Boolean fullRefund = false;

    // pretend=true значит только расчёт, без реального возврата
    @Column(name = "pretend")
    private Boolean pretend = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RefundStatus status; // PENDING, COMPLETED, REJECTED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
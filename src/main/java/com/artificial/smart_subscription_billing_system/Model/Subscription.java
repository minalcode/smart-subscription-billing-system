package com.artificial.smart_subscription_billing_system.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountId;

    private String planName;

    private long monthlyLimit;

    private long usedRequests;

    private BigDecimal monthlyPrice;

    private BigDecimal extraUsageRate;

    private String status;

    private String paymentStatus;

    private LocalDateTime billingCycleStart;

    private LocalDateTime billingCycleEnd;
}
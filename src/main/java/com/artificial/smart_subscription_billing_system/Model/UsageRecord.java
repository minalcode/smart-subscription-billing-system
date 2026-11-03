package com.artificial.smart_subscription_billing_system.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usage_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsageRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountId;

    private long requestCount;

    private LocalDateTime usageTime;
}
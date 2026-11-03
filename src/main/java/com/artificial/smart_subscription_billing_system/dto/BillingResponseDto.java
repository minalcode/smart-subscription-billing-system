package com.artificial.smart_subscription_billing_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingResponseDto {

    private String accountId;

    private String planName;

    private long monthlyLimit;

    private long usedRequests;

    private long extraRequests;

    private BigDecimal monthlyPrice;

    private BigDecimal extraUsageCharge;

    private BigDecimal totalBill;

    private String subscriptionStatus;

    private String paymentStatus;
}
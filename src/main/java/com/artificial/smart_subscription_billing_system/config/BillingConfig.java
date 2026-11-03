package com.artificial.smart_subscription_billing_system.config;

import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Map;

@Configuration
public class BillingConfig {

    public static final Map<String, PlanDetails> PLANS = Map.of(

            "FREE", new PlanDetails(
                    1000,
                    new BigDecimal("0.00"),
                    new BigDecimal("0.00")
            ),

            "BASIC", new PlanDetails(
                    10000,
                    new BigDecimal("499.00"),
                    new BigDecimal("0.10")
            ),

            "PRO", new PlanDetails(
                    100000,
                    new BigDecimal("1499.00"),
                    new BigDecimal("0.05")
            )
    );

    public record PlanDetails(
            long monthlyLimit,
            BigDecimal monthlyPrice,
            BigDecimal extraUsageRate
    ) {
    }
}
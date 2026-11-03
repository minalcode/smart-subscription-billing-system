package com.artificial.smart_subscription_billing_system.Service;

import com.artificial.smart_subscription_billing_system.Model.Subscription;
import com.artificial.smart_subscription_billing_system.Repository.SubscriptionRepository;
import com.artificial.smart_subscription_billing_system.config.BillingConfig;
import com.artificial.smart_subscription_billing_system.dto.BillingResponseDto;
import com.artificial.smart_subscription_billing_system.dto.SubscriptionRequestDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class BillingService {

    private final SubscriptionRepository subscriptionRepository;

    public BillingService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    // Create subscription
    public Subscription createSubscription(SubscriptionRequestDto dto) {

        if (dto.getAccountId() == null || dto.getAccountId().isBlank()) {
            throw new IllegalArgumentException("Account ID is required");
        }

        if (dto.getPlanName() == null) {
            throw new IllegalArgumentException("Plan name is required");
        }

        if (subscriptionRepository.findByAccountId(dto.getAccountId()).isPresent()) {
            throw new IllegalArgumentException("Account already has a subscription");
        }

        String planName = dto.getPlanName().toUpperCase();

        BillingConfig.PlanDetails plan =
                BillingConfig.PLANS.get(planName);

        if (plan == null) {
            throw new IllegalArgumentException(
                    "Invalid plan. Choose FREE, BASIC or PRO");
        }

        LocalDateTime now = LocalDateTime.now();

        Subscription subscription = new Subscription();

        subscription.setAccountId(dto.getAccountId());
        subscription.setPlanName(planName);
        subscription.setMonthlyLimit(plan.monthlyLimit());
        subscription.setUsedRequests(0);
        subscription.setMonthlyPrice(plan.monthlyPrice());
        subscription.setExtraUsageRate(plan.extraUsageRate());
        subscription.setStatus("ACTIVE");
        subscription.setPaymentStatus("UNPAID");
        subscription.setBillingCycleStart(now);
        subscription.setBillingCycleEnd(now.plusMonths(1));

        return subscriptionRepository.save(subscription);
    }

    // Calculate monthly bill
    public BillingResponseDto generateBill(String accountId) {

        Subscription subscription = subscriptionRepository
                .findByAccountId(accountId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Subscription not found"));

        long extraRequests = Math.max(
                0,
                subscription.getUsedRequests() - subscription.getMonthlyLimit()
        );

        BigDecimal extraUsageCharge =
                subscription.getExtraUsageRate()
                        .multiply(BigDecimal.valueOf(extraRequests))
                        .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalBill =
                subscription.getMonthlyPrice()
                        .add(extraUsageCharge)
                        .setScale(2, RoundingMode.HALF_UP);

        return new BillingResponseDto(
                subscription.getAccountId(),
                subscription.getPlanName(),
                subscription.getMonthlyLimit(),
                subscription.getUsedRequests(),
                extraRequests,
                subscription.getMonthlyPrice(),
                extraUsageCharge,
                totalBill,
                subscription.getStatus(),
                subscription.getPaymentStatus()
        );
    }

    // Mark bill as paid
    @Transactional
    public Subscription payBill(String accountId) {

        Subscription subscription = subscriptionRepository
                .findByAccountId(accountId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Subscription not found"));

        if ("PAID".equals(subscription.getPaymentStatus())) {
            throw new IllegalArgumentException("Bill is already paid");
        }

        subscription.setPaymentStatus("PAID");

        return subscriptionRepository.save(subscription);
    }
}
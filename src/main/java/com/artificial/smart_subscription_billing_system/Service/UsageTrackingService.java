package com.artificial.smart_subscription_billing_system.Service;

import com.artificial.smart_subscription_billing_system.Model.Subscription;
import com.artificial.smart_subscription_billing_system.Model.UsageRecord;
import com.artificial.smart_subscription_billing_system.Repository.SubscriptionRepository;
import com.artificial.smart_subscription_billing_system.Repository.UsageRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UsageTrackingService {

    private final SubscriptionRepository subscriptionRepository;
    private final UsageRecordRepository usageRecordRepository;

    public UsageTrackingService(
            SubscriptionRepository subscriptionRepository,
            UsageRecordRepository usageRecordRepository) {

        this.subscriptionRepository = subscriptionRepository;
        this.usageRecordRepository = usageRecordRepository;
    }

    @Transactional
    public UsageRecord trackUsage(String accountId, long requestCount) {

        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("Account ID is required");
        }

        if (requestCount <= 0) {
            throw new IllegalArgumentException(
                    "Request count must be greater than zero");
        }

        Subscription subscription = subscriptionRepository
                .findByAccountId(accountId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Subscription not found"));

        if (!"ACTIVE".equals(subscription.getStatus())) {
            throw new IllegalArgumentException(
                    "Subscription is not active");
        }

        LocalDateTime now = LocalDateTime.now();

        // Check billing cycle
        if (!now.isBefore(subscription.getBillingCycleEnd())) {

            subscription.setUsedRequests(0);

            subscription.setBillingCycleStart(
                    now.withDayOfMonth(1)
                            .toLocalDate()
                            .atStartOfDay()
            );

            subscription.setBillingCycleEnd(
                    subscription.getBillingCycleStart()
                            .plusMonths(1)
            );

            subscription.setPaymentStatus("UNPAID");
        }

        // Update usage
        subscription.setUsedRequests(
                subscription.getUsedRequests() + requestCount
        );

        subscriptionRepository.save(subscription);

        // Save usage record
        UsageRecord record = new UsageRecord();

        record.setAccountId(accountId);
        record.setRequestCount(requestCount);
        record.setUsageTime(now);

        return usageRecordRepository.save(record);
    }
}
package com.artificial.smart_subscription_billing_system.Repository;

import com.artificial.smart_subscription_billing_system.Model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository
        extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByAccountId(String accountId);
}
package com.artificial.smart_subscription_billing_system.Repository;

import com.artificial.smart_subscription_billing_system.Model.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsageRecordRepository
        extends JpaRepository<UsageRecord, Long> {

    List<UsageRecord> findByAccountId(String accountId);
}
package com.artificial.smart_subscription_billing_system.Controller;

import com.artificial.smart_subscription_billing_system.Model.Subscription;
import com.artificial.smart_subscription_billing_system.Model.UsageRecord;

import com.artificial.smart_subscription_billing_system.Service.BillingService;
import com.artificial.smart_subscription_billing_system.Service.UsageTrackingService;

import com.artificial.smart_subscription_billing_system.dto.SubscriptionRequestDto;
import com.artificial.smart_subscription_billing_system.dto.UsageRequestDto;
import com.artificial.smart_subscription_billing_system.dto.BillingResponseDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billingService;
    private final UsageTrackingService usageTrackingService;

    public BillingController(
            BillingService billingService,
            UsageTrackingService usageTrackingService) {

        this.billingService = billingService;
        this.usageTrackingService = usageTrackingService;
    }

    // Create subscription
    @PostMapping("/subscribe")
    public ResponseEntity<Subscription> subscribe(
            @RequestBody SubscriptionRequestDto dto) {

        return ResponseEntity.ok(
                billingService.createSubscription(dto)
        );
    }

    // Track API usage
    @PostMapping("/usage")
    public ResponseEntity<UsageRecord> trackUsage(
            @RequestBody UsageRequestDto dto) {

        return ResponseEntity.ok(
                usageTrackingService.trackUsage(
                        dto.getAccountId(),
                        dto.getRequestCount()
                )
        );
    }

    // Generate bill
    @GetMapping("/{accountId}")
    public ResponseEntity<BillingResponseDto> getBill(
            @PathVariable String accountId) {

        return ResponseEntity.ok(
                billingService.generateBill(accountId)
        );
    }

    // Pay bill
    @PostMapping("/{accountId}/pay")
    public ResponseEntity<Subscription> payBill(
            @PathVariable String accountId) {

        return ResponseEntity.ok(
                billingService.payBill(accountId)
        );
    }
}
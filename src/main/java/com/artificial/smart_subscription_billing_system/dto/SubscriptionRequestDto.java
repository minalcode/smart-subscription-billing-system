package com.artificial.smart_subscription_billing_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequestDto {

    private String accountId;

    private String planName;
}
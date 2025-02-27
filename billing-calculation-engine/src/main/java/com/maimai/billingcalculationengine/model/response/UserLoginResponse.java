package com.maimai.billingcalculationengine.model.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserLoginResponse {
    Long id;
    String username;
    String firstName;
    String token;

    @Builder.Default
    String type = "Bearer";
}

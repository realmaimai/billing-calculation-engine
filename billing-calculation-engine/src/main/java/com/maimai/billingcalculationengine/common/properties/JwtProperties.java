package com.maimai.billingcalculationengine.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
@Data
public class JwtProperties {
    // get property values from application.yml
    // need to have setter value
    private String secretKey;
    private long ttl;
    private String tokenName;

}

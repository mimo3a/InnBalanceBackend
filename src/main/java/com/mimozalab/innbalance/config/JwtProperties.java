package com.mimozalab.innbalance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class JwtProperties {
    
    private String jwtSecret = "InnBalanceSecretKeyThatIsAtLeast32CharactersLongForHS256";
    private long jwtExpirationMs = 3600000; // 1 hour
    
    public String getJwtSecret() {
        return jwtSecret;
    }
    
    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }
    
    public long getJwtExpirationMs() {
        return jwtExpirationMs;
    }
    
    public void setJwtExpirationMs(long jwtExpirationMs) {
        this.jwtExpirationMs = jwtExpirationMs;
    }
}

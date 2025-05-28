package org.app.config;

import org.app.DevProfile;
import org.app.ProductionProfile;
import org.app.SystemProfile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    @ConditionalOnProperty(name = "profile.dev", havingValue = "true")
    public SystemProfile devProfile(){
        return new DevProfile();
    }
    @Bean
    @ConditionalOnProperty(name = "profile.dev", havingValue = "false")
    public SystemProfile prodProfile(){
        return new ProductionProfile();
    }
}

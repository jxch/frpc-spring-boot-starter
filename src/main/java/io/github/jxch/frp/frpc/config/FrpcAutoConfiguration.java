package io.github.jxch.frp.frpc.config;

import io.github.jxch.frp.frpc.FrpcClient;
import io.github.jxch.frp.frpc.FrpcConnectionStatus;
import io.github.jxch.frp.frpc.FrpcRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(FrpcProperties.class)
@ConditionalOnProperty(prefix = "spring.frpc", name = "enabled", matchIfMissing = true)
public class FrpcAutoConfiguration {

    @Bean
    public FrpcRunner frpcRunner(FrpcProperties frpcProperties) {
        return new FrpcRunner(frpcProperties);
    }

    @Bean
    public FrpcConnectionStatus frpcConnectionStatus(FrpcProperties frpcProperties, RestTemplate restTemplate) {
        return new FrpcConnectionStatus(frpcProperties, restTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FrpcClient frpcClient(FrpcProperties frpcProperties, FrpcConnectionStatus frpcConnectionStatus, FrpcRunner frpcRunner) {
        return new FrpcClient(frpcProperties, frpcRunner, frpcConnectionStatus);
    }

}

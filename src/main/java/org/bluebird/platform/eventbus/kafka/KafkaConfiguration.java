package org.bluebird.platform.eventbus.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;

// Global Spring Kafka Configuration
@Configuration
public class KafkaConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "org.bluebird.kafka")
    public KafkaConfig kafkaConfig() {
        return new KafkaConfig();
    }

    @Bean
    public KafkaAdmin kafkaAdmin(KafkaConfig kafkaConfig) {
        final Map<String, Object> config = Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        return new KafkaAdmin(config);
    }
}
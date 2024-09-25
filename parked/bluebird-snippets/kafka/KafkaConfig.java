package org.bluebird.platform.eventbus.kafka;

import lombok.Data;

// Global Kafka Config object
@Data
public class KafkaConfig {
    private String bootstrapServers;
}

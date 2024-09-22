package org.bluebird.integrations.opennms.kafka;

import lombok.Data;

@Data
public class OpennmsKafkaTopicNames {
    private String events;
    private String alarms;
    private String nodes;
}

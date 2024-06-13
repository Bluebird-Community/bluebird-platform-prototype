package org.bluebird.platform.eventbus.integrations.opennms;

import lombok.Data;

@Data
public class OpennmsKafkaTopicNames {
    private String events;
    private String alarms;
    private String nodes;
}

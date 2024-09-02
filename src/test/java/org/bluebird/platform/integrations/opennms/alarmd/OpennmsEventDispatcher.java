package org.bluebird.platform.integrations.opennms.alarmd;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bluebird.integrations.opennms.kafka.OpennmsProtobufEventMapper;
import org.bluebird.platform.integration.opennms.protobuf.model.OpennmsModelProtos;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

// This is a test component to allow simulating events sent by OpenNMS via Kafka, without the need to have Kafka running.
// This is only meant to be used during test phase, and not in production
@Component
@AllArgsConstructor
@Import({OpennmsProtobufEventMapper.class}) // We need to import these, and dispatch event handling internally to these
@Slf4j
public class OpennmsEventDispatcher {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final OpennmsProtobufEventMapper eventMapper;

    public void sendNow(OpennmsModelProtos.Event opennmsEvent) {
        Objects.requireNonNull(opennmsEvent);
        applicationEventPublisher.publishEvent(opennmsEvent);
    }

    @EventListener
    // TODO MVR this is hacky as fuck,...
    void onProtobufEvent(OpennmsModelProtos.Event opennmsEvent) {
        // TODO MVR this is a duplication of OpennmsKafkaEventListener and should probably be re-used instead
        log.info("Received Event: {}", opennmsEvent);
        final var internalEvent = eventMapper.map(opennmsEvent);
        // TODO MVR this is a bit ugly
        internalEvent.setId(UUID.randomUUID());
        applicationEventPublisher.publishEvent(internalEvent);
    }
}

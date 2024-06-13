package org.bluebird.platform.eventbus.integrations.opennms;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bluebird.platform.integration.opennms.protobuf.model.OpennmsModelProtos;
import org.bluebird.platform.persistence.repository.EventRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
@KafkaListener(
        containerFactory = "opennmsKafkaListenerContainerFactory",
        topics = {
                "${org.bluebird.kafka.integration.opennms.topics.events}",
                "${org.bluebird.kafka.integration.opennms.topics.alarms}",
                "${org.bluebird.kafka.integration.opennms.topics.nodes}"
        })
public class OpennmsKafkaEventListener {

    private final OpennmsProtobufEventMapper eventMapper;
    private final EventRepository eventRepository;
    private final OpennmsKafkaTopicNames topicNames;
    private final ApplicationEventPublisher applicationEventPublisher;

    // Fetch all protobuf messages and try to dispatch accordingly
    @KafkaHandler(isDefault = true)
    public void onProtobufEvent(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, byte[] protobufBytes) {
        if (protobufBytes == null || protobufBytes.length == 0) {
            // empty message, wtf?
            return;
        }
        if (topic.equals(topicNames.getEvents())) {
            onEvent(parseEvent(protobufBytes));
            return;
        }
        if (topic.equals(topicNames.getAlarms())) {
            onAlarm(parseAlarm(protobufBytes));
            return;
        }
        if (topic.equals(topicNames.getNodes())) {
            onNode(parseNode(protobufBytes));
            return;
        }
        log.warn("Cannot handle received message for topic {}. Ignoring", topic);
    }

    @KafkaHandler
    public void onEvent(OpennmsModelProtos.Event event) {
        log.info("Received Event: {}", event);
        final var internalEvent = eventMapper.map(event);
        // TODO MVR this is a bit ugly
        internalEvent.setId(UUID.randomUUID());
        applicationEventPublisher.publishEvent(internalEvent);
    }

    @KafkaHandler
    public void onAlarm(OpennmsModelProtos.Alarm alarm) {
//        log.info("Received Alarm: {}", alarm);
    }

    @KafkaHandler
    public void onNode(OpennmsModelProtos.Node node) {
//        log.info("Received Node: {}", node);
    }

    private static OpennmsModelProtos.Event parseEvent(byte[] protobufBytes) {
        try {
            return OpennmsModelProtos.Event.parseFrom(protobufBytes);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private static OpennmsModelProtos.Alarm parseAlarm(byte[] protobufBytes) {
        try {
            return OpennmsModelProtos.Alarm.parseFrom(protobufBytes);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private static OpennmsModelProtos.Node parseNode(byte[] protobufBytes) {
        try {
            return OpennmsModelProtos.Node.parseFrom(protobufBytes);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}
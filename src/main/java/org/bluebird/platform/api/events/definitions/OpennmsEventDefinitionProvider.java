package org.bluebird.platform.api.events.definitions;


import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXB;
import lombok.extern.slf4j.Slf4j;
import org.opennms.integration.xml.eventconf.events.xml.XmlEvent;
import org.opennms.integration.xml.eventconf.events.xml.XmlEvents;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides event definitions from Opennms
 */
@Component
@Slf4j
public class OpennmsEventDefinitionProvider {
    private List<XmlEvent> eventsCache;
    private Map<String, XmlEvent> eventsMap;
    @Value("${org.bluebird.integration.opennms.home}")
    private String opennmsHome;

    @PostConstruct
    public void init() throws IOException {
        final var eventsHome = Paths.get(opennmsHome, "etc", "events");
        this.eventsCache = Files.list(eventsHome)
                .filter(it -> it.toFile().isFile())
                .filter(it -> it.toString().endsWith(".xml"))
                .filter(it -> it.getFileName().toString().startsWith("opennms"))
                .map(file -> {
                    try {
                        // TODO MVR this is a hack so it is actually loadable with newer jaxb versions due to namespace not set correctly for all entities
                        final var fileContent = Files.readString(file).replaceAll("<events xmlns=\"http://xmlns.opennms.org/xsd/eventconf\">", "<events>");
                        try (var is = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8))) {
                            return JAXB.unmarshal(is, XmlEvents.class);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                // TODO MVR global and others are ignored?!
                .filter(it -> !it.getEvents().isEmpty())
                .flatMap(it -> it.getEvents().stream())
                .toList();
        final var eventsMap = eventsCache.stream().collect(Collectors.groupingBy(XmlEvent::getUei));
        eventsMap.values().forEach(it -> {
            if (it.size() > 1) {
                log.warn("Event uei '{}' is not unique but occurred multiple times. Will use only one. The other one is ignored. This is problematic", it.getFirst().getUei());
            }
        });
        this.eventsMap = eventsMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, it -> it.getValue().getFirst()));
    }

    public List<XmlEvent> getEventDefinitions() {
        return eventsCache;
    }

    public XmlEvent getEventDefinitionByUei(String uei) {
        return findEventDefinitionByUei(uei).orElseThrow();
    }

    public Optional<XmlEvent> findEventDefinitionByUei(String uei) {
        return Optional.ofNullable(eventsMap.get(uei));
    }
}

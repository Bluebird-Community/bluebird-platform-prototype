package org.bluebird.platform.engine.alarms.definition;

import org.opennms.integration.xml.eventconf.events.xml.XmlEvent;

import java.util.Optional;

public class ConsolidationKeyProviders {
    public static String template(String reductionKey) {
        return reductionKey;
    }

    public static String template(Optional<XmlEvent> reductionKeyOptional) {
        return reductionKeyOptional
                .map(it -> it.getAlarmData().getClearKey())
                .orElse(null);
    }
}

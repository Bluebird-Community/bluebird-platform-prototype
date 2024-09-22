package org.bluebird.integrations.opennms.events;


import org.bluebird.integrations.opennms.events.xml.XmlEvent;

import java.util.Optional;

public class OpennmsConsolidationKeyProviders {
    public static String template(String reductionKey) {
        return reductionKey;
    }

    public static String template(Optional<XmlEvent> reductionKeyOptional) {
        return reductionKeyOptional
                .map(it -> it.getAlarmData().getClearKey())
                .orElse(null);
    }
}

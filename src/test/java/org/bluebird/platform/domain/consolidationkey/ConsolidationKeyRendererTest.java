package org.bluebird.platform.domain.consolidationkey;


import org.assertj.core.api.Assertions;
import org.bluebird.platform.domain.model.EventDTO;
import org.junit.jupiter.api.Test;

import java.util.Map;

class ConsolidationKeyRendererTest {

    private static final EventDTO EXAMPLE_EVENT = new EventDTO()
            .withUei("uei.opennms.org/nodes/nodeDown")
            .withNamespace("opennms")
            .withSource("dummy")
            .withRef("originalId")
            .withProperties(Map.of(
                    "dpname", "xxxx",
                    "nodeid", "17",
                    "interface", "127.0.0.1",
                    "service", "snmp"));

    private final ConsolidationKeyRenderer renderer = new ConsolidationKeyRenderer(new ConsolidationTokenParser());

    @Test
    void verifyParsingOnlyParameters() {
        final var rendered = renderer.render("{uei}:{dpname}:{nodeid}:{interface}:{service}", EXAMPLE_EVENT);
        Assertions.assertThat(rendered).isEqualTo("uei.opennms.org/nodes/nodeDown:xxxx:17:127.0.0.1:snmp");
    }

    @Test
    void verifyParsingWithConstants() {
        final var rendered = renderer.render("uei.opennms.org/nodes/snmp/interfaceOperDown:{dpname}:{nodeid}:1234test", EXAMPLE_EVENT);
        Assertions.assertThat(rendered).isEqualTo("uei.opennms.org/nodes/snmp/interfaceOperDown:xxxx:17:1234test");
    }
}